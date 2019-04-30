/*
 *    Copyright 2013 Information Control Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.simpliccity.sst.bean.namespace;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.bean.BeanNameGeneratorListener;
import org.simpliccity.sst.bean.annotation.AnnotationBeanNameGeneratorEvent;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.type.AnnotationMetadata;

/**
 * <p>Provides base functionality needed to support bean naming for Spring annotation scanning.</p>
 * 
 * @author Michael Clark
 * @since 0.3.0
 *
 */
public abstract class AbstractAnnotationNamespaceBeanNameGenerator extends AbstractPatternNamespaceBeanNameGenerator 
{
	private Log logger = LogFactory.getLog(this.getClass());
	
	@Override
	public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry)
	{
		String result = null;
		
		// Check to see whether this bean definition has an appropriate annotation and is supported by this name generator
		if (isAnnotatedBean(definition) && isSupported(definition))
		{
			// Load the annotation information from the definition
			AnnotationMetadata annotation = getAnnotation(definition);
			
			// If available, ...
			if (annotation != null)
			{
				// ... generate the bean name from the name components in the annotation
				result = generateQualifiedName(getNameComponentsFromAnnotationMetadata(annotation));
				
				getLogger().debug("Generated name for class [" + definition.getBeanClassName() + "]: " + result);
				
				AnnotationBeanNameGeneratorEvent event = new AnnotationBeanNameGeneratorEvent(definition, result, annotation);
				
				// Loop through any configured listeners ...
				for (BeanNameGeneratorListener listener : getListeners()) 
				{
					// ... and notify them that the bean name has been generated
					listener.beanNameGenerated(event);
					getLogger().debug("Notified listener of type " + listener.getClass().getName());
				}
			}
		}
		
		return result;
	}

	@Override
	public BeanNameGeneratorListener[] getListeners() 
	{
		// Defaults to empty list
		return new BeanNameGeneratorListener[0];
	}

	/**
	 * Returns the fully-qualified class name of the annotation supported by this name generator.
	 * 
	 * @return The name of the supported annotation class.
	 */
	protected abstract String getAnnotationClassName();
	
	/**
	 * Extracts an ordered list of subcomponents for the bean name from the annotation metadata associated with the bean
	 * definition.  Subclasses must implement this method to address both the supported custom annotation and the naming
	 * pattern being used.
	 * 
	 * @param annotation The annotation metadata associated with the bean definition.
	 * @return The list of name subcomponents.
	 */
	protected abstract String[] getNameComponentsFromAnnotationMetadata(AnnotationMetadata annotation);
	
	/**
	 * <p>Indicates whether the particular bean definition is supported by the name generator.  This check is in addition
	 * to whether or not the bean definition has the supported annotation.  It is intended to allow subclasses to 
	 * perform more specific validity checks on the contents of the annotation.</p>
	 * 
	 * <p>The default implementation always returns <code>true</code>.  Subclasses can override as desired to incorporate
	 * annotation-specific validations.</p>
	 * 
	 * @param definition The bean definition being named.
	 * @return <code>true</code> if the name generator can create a name for this definition; <code>false</code> otherwise.
	 */
	protected boolean isSupported(BeanDefinition definition)
	{
		// Defaults to true
		return true;
	}
	
	/**
	 * Returns the {@link org.apache.commons.logging.Log} instance used by the class to log
	 * messages.
	 *  
	 * @return The {@link org.apache.commons.logging.Log} instance for this class.
	 */
	protected Log getLogger()
	{
		return logger;
	}
	
	/**
	 * Returns the annotation metadata associated with the bean definition.
	 * 
	 * @param definition The bean definition being named.
	 * @return The annotation metadata for this bean definition.
	 */
	protected AnnotationMetadata getAnnotation(BeanDefinition definition)
	{
		AnnotationMetadata result = null;
		
		if (definition instanceof AnnotatedBeanDefinition)
		{
			result = ((AnnotatedBeanDefinition) definition).getMetadata();
		}
		else
		{
			getLogger().debug("Unable to find information for annotation [" + getAnnotationClassName() + "] in bean class");
		}
		
		return result;
	}

	private boolean isAnnotatedBean(BeanDefinition definition) 
	{
		boolean result = false;
		
		if (definition instanceof AnnotatedBeanDefinition)
		{
			result = getAnnotation(definition).isAnnotated(getAnnotationClassName());
		}
		
		return result;
	}	
}
