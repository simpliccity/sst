/*
 *    Copyright 2014 Information Control Corporation
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

package org.simpliccity.sst.ws.endpoint.security.config;

import org.simpliccity.sst.service.security.config.AbstractServiceAccessControlBeanDefinitionParser;
import org.simpliccity.sst.ws.endpoint.security.EndpointAccessValidatorInterceptor;

/**
 * <p>The {@link org.springframework.beans.factory.xml.BeanDefinitionParser} used to parse the
 * <code>access</code> element of the <code>sstws</code> namespace.  This element can be used
 * as a shortcut to configure the bean definition for the 
 * {@link org.simpliccity.sst.ws.endpoint.security.EndpointAccessValidatorInterceptor} 
 * that can be used to apply access control constraints to Spring-WS endpoints.</p>
 * 
 *  <p><b>Example</b></p>
 *  
 * <pre>
 * &lt;beans:xmlns="http://www.springframework.org/schema/beans"
 * 	      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 * 	      xmlns:sstws="http://www.simpliccity.org/schema/sstws"
 *        xsi:schemaLocation="http://www.simpliccity.org/schema/sstws http://www.simpliccity.org/schema/sst/sstws.xsd" /&gt;
 * &lt;sstws:access id="domainAccessValidator" /&gt;
 * </pre>
 *
 * <p>The <code>access</code> element will define a fully configured 
 * {@link org.simpliccity.sst.ws.endpoint.security.EndpointAccessValidatorInterceptor}
 * bean, and all dependencies, with the specified <code>id</code>.  The optional <code>securityHandlerRef</code>
 * attribute can be used to configure the interceptor with a reference to a particular 
 * {@link org.simpliccity.sst.service.security.ServiceSecurityHandler} implementation.  If the attribute is
 * omitted, a {@link org.simpliccity.sst.security.service.SpringSecurityServiceSecurityHandler} bean
 * is used by default.</p>
 * 
 * @author Kevin Fox
 * @since 1.0.0
 * 
 * @see org.simpliccity.sst.ws.parse.SstWsNamespaceHandler
 * @see org.simpliccity.sst.ws.endpoint.security.EndpointAccessValidatorInterceptor
 * @see org.simpliccity.sst.service.security.ServiceSecurityHandler
 * @see org.simpliccity.sst.security.service.SpringSecurityServiceSecurityHandler
 *
 */
public class AccessValidatorBeanDefinitionParser extends AbstractServiceAccessControlBeanDefinitionParser 
{
	@SuppressWarnings("rawtypes")
	@Override
	protected Class getEnforcementClass() 
	{
		return EndpointAccessValidatorInterceptor.class;
	}
}
