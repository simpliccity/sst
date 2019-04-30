/*
 *    Copyright 2016 Information Control Corporation
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

package org.simpliccity.sst.service.security.config;

import org.simpliccity.styleguide.annotation.CodeStyle;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public abstract class AbstractServiceAccessControlBeanDefinitionParser extends AbstractBeanDefinitionParser 
{

	private static final String PROPERTY_SECURITY_HANDLER = "securityHandler";
	
	private static final String ATTRIBUTE_SECURITY_HANDLER_REF = "securityHandlerRef";
	
	@CodeStyle(categoryId="configParser", topicId="autoRegister")
	// --- StyleGuide configParser.autoRegister ---
	@Override
	protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) 
	{
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(getEnforcementClass());
		
		// Make the bean a singleton
		builder.setScope(BeanDefinition.SCOPE_SINGLETON);

		// Handle "securityHandlerRef" attribute
		// Use the specified reference, if any...
		String handlerRef = element.getAttribute(ATTRIBUTE_SECURITY_HANDLER_REF);
		if (StringUtils.hasText(handlerRef))
		{
			// Set the reference to the appropriate endpoint security handler bean
			builder.addPropertyReference(PROPERTY_SECURITY_HANDLER, handlerRef);
		}
		
		return builder.getBeanDefinition();
	}
	// --- StyleGuide configParser.autoRegister ---
	
	@SuppressWarnings("rawtypes")
	protected abstract Class getEnforcementClass();
}
