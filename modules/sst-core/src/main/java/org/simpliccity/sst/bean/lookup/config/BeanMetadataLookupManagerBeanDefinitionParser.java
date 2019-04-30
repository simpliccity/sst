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

import org.simpliccity.styleguide.annotation.CodeStyle;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * <p>The {@link org.springframework.beans.factory.xml.BeanDefinitionParser} used to parse the
 * <code>lookup</code> element of the <code>sst</code> namespace.  This element can be used
 * as a shortcut to configure the bean definition for the {@link org.simpliccity.sst.bean.lookup.BeanMetadataLookupManager} 
 * class that implements the <code>lookup</code> framework.</p>
 * 
 *  <p><b>Example</b></p>
 *  
 * <pre>
 * &lt;beans:xmlns="http://www.springframework.org/schema/beans"
 * 	      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 * 	      xmlns:sst="http://www.simpliccity.org/schema/sst"
 *        xsi:schemaLocation="http://www.simpliccity.org/schema/sst http://www.simpliccity.org/schema/sst/sst.xsd" /&gt;
 * &lt;sst:lookup /&gt;
 * </pre>
 *
 * <p>The <code>lookup</code> element will define a fully configured {@link org.simpliccity.sst.bean.lookup.BeanMetadataLookupManager}
 * bean and all dependencies, if one does not already exist.  This includes using 
 * {@link org.simpliccity.sst.bean.lookup.namespace.MappedNameBeanMetadataLookupStrategy} as the
 * default strategy for the manager, unless  one is specified using the optional <code>strategy</code> attribute.</p>
 * 
 * @author Kevin Fox
 * @since 0.3.0
 * 
 * @see BeanMetadataLookupManagerParserHelper
 *
 */
public class BeanMetadataLookupManagerBeanDefinitionParser extends AbstractBeanDefinitionParser 
{
	private static final String PROPERTY_STRATEGY = "strategy";
	
	@Override
	protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) 
	{
		AbstractBeanDefinition lookupManagerBean = null;
		
		// Handle "strategy" property
		String strategy = element.getAttribute(PROPERTY_STRATEGY);
		if (!StringUtils.hasText(strategy))
		{
			strategy = BeanMetadataLookupManagerParserHelper.BEAN_NAME_LOOKUP_STRATEGY;
		}
		
		// If no lookup manager has been defined yet, define one using the specified lookup strategy
		BeanDefinitionRegistry registry = parserContext.getRegistry();
		if (!registry.containsBeanDefinition(BeanMetadataLookupManagerParserHelper.BEAN_NAME_LOOKUP_MANAGER))
		{
			lookupManagerBean = BeanMetadataLookupManagerParserHelper.initializeLookupManager(registry, strategy);
		}
		
		return lookupManagerBean;
	}
	
	@CodeStyle(categoryId="configParser", topicId="forceId")
	// --- StyleGuide configParser.forceId ---
	@Override
	protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) throws BeanDefinitionStoreException
	{
		return BeanMetadataLookupManagerParserHelper.BEAN_NAME_LOOKUP_MANAGER;
	}
	// --- StyleGuide configParser.forceId ---
}
