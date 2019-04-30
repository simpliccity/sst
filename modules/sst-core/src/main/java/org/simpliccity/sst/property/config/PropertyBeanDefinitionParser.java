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

package org.simpliccity.sst.property.config;

import org.simpliccity.sst.bean.BeanDefinitionUtils;
import org.simpliccity.sst.property.DelegatingPropertiesFactoryBean;
import org.simpliccity.sst.property.DelegatingPropertyPlaceholderConfigurer;
import org.simpliccity.styleguide.annotation.CodeStyle;
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
 * <code>property</code> element of the <code>sst</code> namespace.  This element can be used
 * as a shortcut to configure bean definitions for {@link DelegatingPropertiesFactoryBean}
 * and {@link DelegatingPropertyPlaceholderConfigurer}.  Together, these enable resolution
 * of properties in bean configuration files and Spring annotations against various sources
 * of property information using customizable property loaders</p>
 *  
 *  <p><b>Example</b></p>
 *  
 * <pre>
 * &lt;beans:xmlns="http://www.springframework.org/schema/beans"
 * 	      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 * 	      xmlns:sst="http://www.simpliccity.org/schema/sst"
 *        xsi:schemaLocation="http://www.simpliccity.org/schema/sst http://www.simpliccity.org/schema/sst/sst.xsd" /&gt;
 * &lt;sst:property loader="propertyLoaderBean" /&gt;
 * </pre>
 * 
 * @author Kevin Fox
 * @since 1.0.0
 * 
 * @see org.simpliccity.sst.parse.SstNamespaceHandler
 * @see org.simpliccity.sst.property.DelegatingPropertiesFactoryBean
 * @see org.simpliccity.sst.property.DelegatingPropertyPlaceholderConfigurer
 *
 */
public class PropertyBeanDefinitionParser extends AbstractBeanDefinitionParser 
{
	private static final String PROPERTY_LOADER = "loader";
	private static final String PROPERTY_PROPERTY_LOADER = "propertyLoader";
	
	// Standard bean names expected by Spring for the properties factory and property configurer
	private static final String BEAN_NAME_PROPERTIES_FACTORY = "propertiesFactory";
	private static final String BEAN_NAME_PROPERTY_PLACEHOLDER = "propertyConfigurer";
	
	@CodeStyle(categoryId="configParser", topicId="manualRegister")
	@Override
	protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) 
	{
		// Handle "loader" property
		String loader = element.getAttribute(PROPERTY_LOADER);
		if (!StringUtils.hasText(loader))
		{
			throw BeanDefinitionUtils.generateParsingException(parserContext, "Error parsing <sst:property> tag - loader attribute is required.");
		}
		
		// --- StyleGuide configParser.manualRegister ---
		// Get bean definition registry
		BeanDefinitionRegistry registry = parserContext.getRegistry();
		
		// Create a bean definition for the PropertiesFactoryBean implementation and register it
		registry.registerBeanDefinition(BEAN_NAME_PROPERTIES_FACTORY, generateBeanDefinition(DelegatingPropertiesFactoryBean.class, loader));
		
		// Create a bean definition for the PropertyPlaceholderConfigurer implementation and register it
		registry.registerBeanDefinition(BEAN_NAME_PROPERTY_PLACEHOLDER, generateBeanDefinition(DelegatingPropertyPlaceholderConfigurer.class, loader));
		

		return null;
		// --- StyleGuide configParser.manualRegister ---
	}
	
	private AbstractBeanDefinition generateBeanDefinition(Class<?> beanClass, String loaderBeanReference)
	{
		BeanDefinitionBuilder beanBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClass);

		// Force the bean to be a singleton
		beanBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
		
		// Set the reference to the property loader bean
		beanBuilder.addPropertyReference(PROPERTY_PROPERTY_LOADER, loaderBeanReference);
		
		return beanBuilder.getBeanDefinition();
	}
}
