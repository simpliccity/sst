/*
 *    Copyright 2016 Information Control Company
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

package org.simpliccity.sst.config;

import java.util.ArrayDeque;
import java.util.Deque;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;

/**
 * A {@link ConfigurationCondition} that allows a configuration to be processed if some bean within the
 * {@link BeanDefinitionRegistry} is marked with a specified "enabler" annotation.  This prevents
 * the unintended loading of framework configuration classes.
 * 
 * <p>For example, a configuration class with these annotations</p>
 * 
 * <pre class="code">
 * &#064;Configuration
 * &#064;IfEnabled(EnableSstBeanLookup.class)
 * public class BeanMetadataLookupManagerConfiguration {
 * </pre>
 * 
 * <p>would pass this condition if another bean existed with the following annotations:</p>
 * 
 * <pre class="code">
 * &#064;Configuration
 * &#064;EnableSstBeanLookup
 * &#064;ComponentScan(basePackages="org.simpliccity.sst", nameGenerator=PropertiesDelegateAnnotationBeanNameGenerator.class)
 * public class MyConfiguration {
 * </pre>
 * 
 * @author Kevin Fox
 * @since 1.0.0
 * 
 * @see IfEnabled
 *
 */
public class IfEnabledCondition implements ConfigurationCondition 
{
	private Log logger = LogFactory.getLog(this.getClass());
	
	private Deque<String> enablerSearchStack = new ArrayDeque<>();
	
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) 
	{
		String className = ConfigurationAnnotationUtils.getClassName(metadata);
		String enabler = ConfigurationAnnotationUtils.getEnablerName(metadata);
		
		BeanDefinitionRegistry registry = context.getRegistry();
		
		boolean result = new EnablerChecker().checkActive(className, enabler, registry);
		
		if (!result)
		{
			logger.debug("Configuration disabled: configuration [" + className + "], enabler [" + enabler + "].");			
		}
				
		return result;
	}

	@Override
	public ConfigurationPhase getConfigurationPhase() 
	{
		return ConfigurationPhase.REGISTER_BEAN;
	}

	/**
	 * An inner class that simplifies the use of recursion to look for enabling annotations.
	 * 
	 * @author Kevin Fox
	 *
	 */
	private class EnablerChecker
	{
		/**
		 * Determines whether the specified class should be activated based on whether a bean exists in the registry
		 * with the specified "enabler" annotation.
		 * 
		 * @param className The class name of the bean being checked.
		 * @param enabler The class name of the "enabler" annotation.
		 * @param registry The Spring bean registry.
		 * @return <code>true</code> if a bean with the "enabler" annotation exists in the registry; <code>false</code> otherwise.
		 */
		public boolean checkActive(String className, String enabler, BeanDefinitionRegistry registry)
		{
			// Push the current "enabler" of interest on the search stack, to help prevent an endless loop
			enablerSearchStack.push(enabler);
			
			// Check the cache to see if this "enabler" has already been found
			boolean enabled = EnablerRegistry.contains(enabler);
			
			if (enabled)
			{
				// If so, we are done (log it)
				logger.debug("Configuration enabled: configuration [" + className + "], enabler [" + enabler + "], cached [yes].");
			}
			else
			{
				// Otherwise, we need to check all of the registered beans to see if any have the desired "enabler" as an annotation
				String[] names = registry.getBeanDefinitionNames();
				for (String name : names)
				{
					BeanDefinition bean = registry.getBeanDefinition(name);
					
					enabled = checkBeanForEnabler(enabler, registry, bean);
					
					if (enabled)
					{
						// If an applicable bean is found, the "enabler" is active (and we cache this knowledge)
						EnablerRegistry.addEnabler(enabler);
						logger.debug("Configuration enabled: configuration [" + className + "], enabler [" + enabler + "], cause [" + bean.getBeanClassName() + "].");
						break;
					}
				}
			}

			// Pop the current "enabler" of interest from the search stack
			enablerSearchStack.pop();
			
			return enabled;
		}
		
		private boolean checkBeanForEnabler(String enabler, BeanDefinitionRegistry registry, BeanDefinition bean)
		{
			boolean activatesEnabler = false;
			
			// Only mess with annotated beans
			if (bean instanceof AnnotatedBeanDefinition)
			{
				AnnotationMetadata annotationMetadata = ((AnnotatedBeanDefinition) bean).getMetadata();
				
				// This bean could have its own @IfEnabled annotation, which means we have found another "enabler" to consider
				String beanEnabler = ConfigurationAnnotationUtils.getEnablerName(annotationMetadata);
				boolean activeBean;
				if (beanEnabler == null)
				{
					// If not, life is easy, just process this bean for the current "enabler"
					activeBean = true;
				}
				else
				{
					// Before this bean can be considered to provide the original "enabler", it's necessary to ensure that it is active, itself (i.e. that its own "enabler" exists somewhere)
					if (enablerSearchStack.contains(beanEnabler))
					{
						// If the new "enabler" is already on the search stack, it's being processed, so skip this bean in the current pass
						activeBean = false;
					}
					else
					{
						// On the other hand, if we haven't previously checked on the new "enabler", make a recursive call to do so
						String beanClassName = ConfigurationAnnotationUtils.getClassName(annotationMetadata);
						activeBean = new EnablerChecker().checkActive(beanClassName, beanEnabler, registry);
					}
				}

				// If we know the bean to be active, check to see if it has the "enabler" annotation
				activatesEnabler = activeBean && annotationMetadata.isAnnotated(enabler);
			}
						
			return activatesEnabler;			
		}
	}
}

