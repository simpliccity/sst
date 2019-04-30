/*
 *    Copyright 2012 Information Control Corporation
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

package org.simpliccity.sst.transform.config;

import org.simpliccity.sst.bean.lookup.config.BeanMetadataLookupManagerParserHelper;
import org.simpliccity.sst.transform.Transformer;
import org.simpliccity.sst.transform.cache.TransformationCacheManager;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * <p>The {@link org.springframework.beans.factory.xml.BeanDefinitionParser} used to parse the
 * <code>transformer</code> element of the <code>sst</code> namespace.  This element can be used
 * as a shortcut to configure the bean definition for the {@link Transformer} class that implements
 * the <code>transform</code> framework.</p>
 *  
 *  <p><b>Example</b></p>
 *  
 * <pre>
 * &lt;beans:xmlns="http://www.springframework.org/schema/beans"
 * 	      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 * 	      xmlns:sst="http://www.simpliccity.org/schema/sst"
 *        xsi:schemaLocation="http://www.simpliccity.org/schema/sst http://www.simpliccity.org/schema/sst/sst.xsd" /&gt;
 * &lt;sst:transformer defaultCacheMode="JOIN" /&gt;
 * </pre>
 * 
 * <p>The <code>transformer</code> element will define a fully-configured {@link Transformer} bean
 * and all dependencies.  These include a {@link TransformationCacheManager} bean and, if not already 
 * defined, a {@link org.simpliccity.sst.bean.lookup.BeanMetadataLookupManager} bean.</p>
 * 
 * <p>The <code>transformer</code> element supports optional attributes that can specify the corresponding
 * settings for the transformer and associated {@link TransformationCacheManager}:</p>
 * <ul>
 * <li>disableCache</li>
 * <li>defaultCacheMode</li>
 * <li>globalCacheScope</li>
 * <li>cacheFactory</li>
 * <li>surfaceExceptions</li>
 * </ul>
 * 
 * @author Kevin Fox
 * @since 0.2.0
 * 
 * @see org.simpliccity.sst.parse.SstNamespaceHandler
 * @see org.simpliccity.sst.transform.Transformer
 * @see org.simpliccity.sst.transform.cache.TransformationCacheManager
 *
 */
public class TransformerBeanDefinitionParser extends AbstractBeanDefinitionParser 
{
	public static final String BEAN_NAME_TRANSFORMER = "sstTransformer";
	public static final String BEAN_NAME_CACHE_MANAGER = "sstTransformationCacheManager";
	
	private static final String PROPERTY_DISABLECACHE = "disableCache";
	private static final String PROPERTY_DEFAULTCACHEMODE = "defaultCacheMode";
	private static final String PROPERTY_GLOBALCACHESCOPE = "globalCacheScope";
	private static final String PROPERTY_CACHEFACTORY = "cacheFactory";
	private static final String PROPERTY_SURFACEEXCEPTIONS = "surfaceExceptions";
	private static final String PROPERTY_CACHE_MANAGER = "cacheManager";
	
	@Override
	protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) 
	{
		BeanDefinitionRegistry registry = parserContext.getRegistry();
		
		BeanDefinitionBuilder transformerBuilder = configureTransformer(element);

		// Force the bean to be a singleton - ensures proper handling of the cache
		transformerBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
		
		// Create a cache manager
		AbstractBeanDefinition cacheManager = configureCacheManager(element);
		registry.registerBeanDefinition(BEAN_NAME_CACHE_MANAGER, cacheManager);
		
		// Associate the cache manager bean with the transformer
		transformerBuilder.addPropertyReference(PROPERTY_CACHE_MANAGER, BEAN_NAME_CACHE_MANAGER);
		
		// If no lookup manager has been defined, register one using the default lookup strategy
		if (!registry.containsBeanDefinition(BeanMetadataLookupManagerParserHelper.BEAN_NAME_LOOKUP_MANAGER))
		{
			BeanMetadataLookupManagerParserHelper.registerLookupManager(registry, BeanMetadataLookupManagerParserHelper.DEFAULT_STRATEGY_CLASSNAME);
		}
		
		return transformerBuilder.getBeanDefinition();
	}

	@Override
	protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) throws BeanDefinitionStoreException
	{
		return BEAN_NAME_TRANSFORMER;
	}
	
	private BeanDefinitionBuilder configureTransformer(Element element)
	{
		BeanDefinitionBuilder transformerBuilder = BeanDefinitionBuilder.genericBeanDefinition(Transformer.class);

		// Force the bean to be a singleton - ensures proper handling of the cache
		transformerBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
		
		// Handle "surfaceExceptions" property
		String surfaceExceptions = element.getAttribute(PROPERTY_SURFACEEXCEPTIONS);
		if (StringUtils.hasText(surfaceExceptions))
		{
			transformerBuilder.addPropertyValue(PROPERTY_SURFACEEXCEPTIONS, Boolean.valueOf(surfaceExceptions));
		}
		
		return transformerBuilder;
	}

	private AbstractBeanDefinition configureCacheManager(Element element)
	{
		BeanDefinitionBuilder cacheManagerBuilder = BeanDefinitionBuilder.genericBeanDefinition(TransformationCacheManager.class);

		// Force the bean to be a singleton - ensures proper handling of the cache
		cacheManagerBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
		
		// Handle "disableCache" property
		String disableCache = element.getAttribute(PROPERTY_DISABLECACHE);
		if (StringUtils.hasText(disableCache))
		{
			cacheManagerBuilder.addPropertyValue(PROPERTY_DISABLECACHE, Boolean.valueOf(disableCache));
		}
		
		// Handle "defaultCacheMode" property
		String defaultCacheMode = element.getAttribute(PROPERTY_DEFAULTCACHEMODE);
		if (StringUtils.hasText(defaultCacheMode))
		{
			cacheManagerBuilder.addPropertyValue(PROPERTY_DEFAULTCACHEMODE, defaultCacheMode);
		}
		
		// Handle "globalCacheScope" property
		String globalCacheScope = element.getAttribute(PROPERTY_GLOBALCACHESCOPE);
		if (StringUtils.hasText(globalCacheScope))
		{
			cacheManagerBuilder.addPropertyValue(PROPERTY_GLOBALCACHESCOPE, Boolean.valueOf(globalCacheScope));
		}
		
		// Handle "cacheFactory" property
		String cacheFactory = element.getAttribute(PROPERTY_CACHEFACTORY);
		if (StringUtils.hasText(cacheFactory))
		{
			cacheManagerBuilder.addPropertyReference(PROPERTY_CACHEFACTORY, cacheFactory);
		}
		
		return cacheManagerBuilder.getBeanDefinition();
	}
}
