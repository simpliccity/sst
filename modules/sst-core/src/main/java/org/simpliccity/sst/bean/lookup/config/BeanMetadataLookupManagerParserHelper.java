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

package org.simpliccity.sst.bean.lookup.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.bean.lookup.BeanMetadataLookupManager;
import org.simpliccity.sst.bean.lookup.namespace.MappedNameBeanMetadataLookupStrategy;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * A helper class used to setup a {@link BeanMetadataLookupManager} when processing elements
 * in the <code>sst</code> namespace.
 * 
 * @author Kevin Fox
 * @since 0.3.0
 * 
 * @see org.simpliccity.sst.bean.lookup.config.BeanMetadataLookupManagerBeanDefinitionParser
 * @see org.simpliccity.sst.transform.config.TransformerBeanDefinitionParser
 * @see org.simpliccity.sst.bean.lookup.BeanMetadataLookupStrategy
 *
 */
public final class BeanMetadataLookupManagerParserHelper 
{
	private static Log logger = LogFactory.getLog(BeanMetadataLookupManagerParserHelper.class);
	
	/**
	 * The name of a registered {@link BeanMetadataLookupManager}: {@value}.
	 */
	public static final String BEAN_NAME_LOOKUP_MANAGER = "sstBeanMetadataLookupManager";
	
	/**
	 * The name of a registered lookup strategy: {@value}.
	 */
	public static final String BEAN_NAME_LOOKUP_STRATEGY = "sstBeanMetadataLookupStrategy";
	
	/**
	 * The default lookup strategy class name: {@link org.simpliccity.sst.bean.lookup.namespace.MappedNameBeanMetadataLookupStrategy}.
	 */
	public static final String DEFAULT_STRATEGY_CLASSNAME = MappedNameBeanMetadataLookupStrategy.class.getName();
	
	/**
	 * The {@link BeanMetadataLookupManager} lookup strategy property: {@value}.
	 */
	public static final String PROPERTY_STRATEGY = "lookupStrategy";
	
	private BeanMetadataLookupManagerParserHelper()
	{
	}
	
	/**
	 * Registers a fully-configured {@link BeanMetadataLookupManager} using the defined strategy class.
	 *  
	 * @param registry The {@link BeanDefinitionRegistry} in which to register the generated bean definitions.
	 * @param strategyReference A reference to a bean to use as the strategy for the {@link BeanMetadataLookupManager}.
	 */
	public static void registerLookupManager(BeanDefinitionRegistry registry, String strategyReference)
	{
		AbstractBeanDefinition lookupManagerBean = initializeLookupManager(registry, strategyReference);
		
		// Register the lookup manager bean definition
		registry.registerBeanDefinition(BEAN_NAME_LOOKUP_MANAGER, lookupManagerBean);
	}
	
	/**
	 * Generates a bean definition for a {@link BeanMetadataLookupManager} using the defined strategy class.
	 * Note that the returned bean definition will not have been registered, but one for the specified
	 * strategy class will have been.
	 * 
	 * @param registry The {@link BeanDefinitionRegistry} in which to register generated bean definitions.
	 * @param strategyReference A reference to a bean to use as the strategy for the {@link BeanMetadataLookupManager}.
	 * @return The {@link AbstractBeanDefinition} defining a bean instance of {@link BeanMetadataLookupManager}.
	 */
	public static AbstractBeanDefinition initializeLookupManager(BeanDefinitionRegistry registry, String strategyReference)
	{
		// Create lookup manager bean definition
		BeanDefinitionBuilder lookupManagerBuilder = BeanDefinitionBuilder.genericBeanDefinition(BeanMetadataLookupManager.class);
		
		// Force the bean to be a singleton
		lookupManagerBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
		
		// Use an existing strategy bean reference, if specified; else setup the default strategy bean
		logger.debug("BeanMetaDataLookupStrategy reference: " + strategyReference);
		logger.debug("BeanMetaDataLookupStrategy bean reference found: " + registry.containsBeanDefinition(strategyReference));
		String strategyBeanName = registry.containsBeanDefinition(strategyReference) ? strategyReference : initializeDefaultStrategy(registry);
		
		// Associate the strategy bean with the lookup manager
		lookupManagerBuilder.addPropertyReference(PROPERTY_STRATEGY, strategyBeanName);
		
		return lookupManagerBuilder.getBeanDefinition();
	}
	
	private static String initializeDefaultStrategy(BeanDefinitionRegistry registry)
	{
		// Create lookup strategy bean definition
		BeanDefinitionBuilder strategy = BeanDefinitionBuilder.genericBeanDefinition(DEFAULT_STRATEGY_CLASSNAME);

		// Force the bean to be a singleton
		strategy.setScope(BeanDefinition.SCOPE_SINGLETON);
		
		registry.registerBeanDefinition(BEAN_NAME_LOOKUP_STRATEGY, strategy.getBeanDefinition());
		
		return BEAN_NAME_LOOKUP_STRATEGY;
	}
}
