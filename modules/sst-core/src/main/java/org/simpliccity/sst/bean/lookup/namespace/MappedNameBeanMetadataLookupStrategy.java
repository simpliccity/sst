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

package org.simpliccity.sst.bean.lookup.namespace;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.bean.annotation.AnnotationHandlerUtils;
import org.simpliccity.sst.bean.lookup.BeanMetadataLookupContainer;
import org.simpliccity.sst.bean.lookup.BeanMetadataLookupException;
import org.simpliccity.sst.bean.lookup.BeanMetadataLookupStrategy;
import org.simpliccity.sst.bean.lookup.annotation.DynamicTypedComponent;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;

/**
 * <p>An implementation of {@link BeanMetadataLookupStrategy} that relies on the <code>namespace</code> framework
 * to incorporate bean metadata from annotations into a structured bean name when loading supported annotated Spring 
 * components.  The strategy then uses an instance of {@link BeanMetadataLookupNameMapper} to determine the name 
 * of the bean to retrieve based on the metadata specified for lookup.</p>
 * 
 * <p>Currently, this strategy supports only beans that are defined through component autodetection, whether defined as
 * typed beans using {@link org.simpliccity.sst.bean.lookup.annotation.DynamicTypedComponent} or with custom Spring annotations.
 * {@link org.simpliccity.sst.bean.annotation.AnnotationHandlerUtils} is used to load the available {@link BeanMetadataLookupNameMapper}
 * instances for supported annotation types.</p>
 * 
 * @author Kevin Fox
 * @since 0.3.0
 * 
 * @see org.simpliccity.sst.bean.namespace.NamespaceBeanNameGenerator
 * @see org.simpliccity.sst.bean.annotation.PropertiesDelegateAnnotationBeanNameGenerator
 * @see org.simpliccity.sst.bean.annotation.AnnotationHandlerUtils
 * @see org.simpliccity.sst.bean.lookup.namespace.BeanMetadataLookupNameMapper
 * @see org.simpliccity.sst.bean.lookup.annotation.DynamicTypedComponent
 *
 */
public class MappedNameBeanMetadataLookupStrategy implements BeanMetadataLookupStrategy
{
	private Log logger = LogFactory.getLog(this.getClass());
	
	private Map<Class<? extends Annotation>, BeanMetadataLookupNameMapper> annotationMappers ;
	
	/**
	 * Creates and initializes a new instance of <code>MappedNameBeanMetadataLookupStrategy</code>.
	 */
	public MappedNameBeanMetadataLookupStrategy()
	{
		super();
		
		annotationMappers = AnnotationHandlerUtils.loadAnnotationHandlersFromPropertiesResource(BeanMetadataLookupNameMapper.class);
		logger.debug("Succesfully loaded name mapper mappings [" + annotationMappers.size() + "]");
	}
	
	@Override
	public <T> Object getAnnotatedBean(BeanFactory factory,	BeanMetadataLookupContainer<T> container) throws BeanMetadataLookupException 
	{
		// Determine the name of the desired bean using the name mapper associated with the discriminator annotation
		@SuppressWarnings("unchecked")
		String beanName = generateLookupName((Class<? extends Annotation>) container.getLookupDiscriminator(), container);
		
		// Get the bean from the bean factory
		Object result = null;
		try
		{
			result = factory.getBean(beanName);
			logger.debug("Retrieved the named bean [" + beanName + "] from the bean factory.");
		}
		catch (BeansException e)
		{
			throw new BeanMetadataLookupException("Unable to retrieve the named bean [" + beanName + "] from the bean factory.", e);
		}
		
		return result;
	}

	@Override
	public <T> T getTypedBean(BeanFactory factory, BeanMetadataLookupContainer<T> container) throws BeanMetadataLookupException 
	{
		// Determine the name of the desired bean using the name mapper associated with the DynamicTypedComponent annotation
		String beanName = generateLookupName(DynamicTypedComponent.class, container);
		
		// Get the typed bean from the bean factory
		T result = null;
		try
		{
			result = factory.getBean(beanName, container.getLookupDiscriminator());
			logger.debug("Retrieved the named bean [" + beanName + "] from the bean factory.");
		}
		catch (BeansException e)
		{
			throw new BeanMetadataLookupException("Unable to retrieve the named bean [" + beanName + "] from the bean factory.", e);
		}

		return result;
	}
	
	private <T> String generateLookupName(Class<? extends Annotation> mapperKey, BeanMetadataLookupContainer<T> container) throws BeanMetadataLookupException
	{
		BeanMetadataLookupNameMapper mapper = annotationMappers.get(mapperKey);		
		if (mapper == null)
		{
			throw new BeanMetadataLookupException("No corresponding name mapper was found [" + mapperKey + "].");
		}
		
		// Use the name mapper to determine the name of the desired bean
		return mapper.generateNameFromMetadata(container);
	}
}
