/*
 *    Copyright 2009 Information Control Corporation
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

package org.simpliccity.sst.ws.endpoint.security;

import java.lang.reflect.AnnotatedElement;

import org.simpliccity.sst.service.security.AbstractSecuredMessageProcessor;
import org.simpliccity.sst.service.security.ServiceAccessException;
import org.simpliccity.sst.service.security.ServiceSemanticContext;
import org.simpliccity.sst.service.security.annotation.ApplyWhen;
import org.simpliccity.sst.service.security.annotation.SecuredMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.endpoint.MethodEndpoint;

/**
 * <p>An endpoint interceptor used to apply access control checks to incoming web service calls
 * before they are forwarded to the corresponding endpoint.  The specific access control rules
 * to be applied to an endpoint are configured using the 
 * {@link SecuredMessage} and {@link org.simpliccity.sst.service.security.annotation.SemanticConstraint} 
 * annotations.</p>
 * 
 * <p>Processing of individual access control checks is delegated to classes implementing the 
 * {@link org.simpliccity.sst.service.security.ServiceAccessControlRule} interface.  The set of available access 
 * control classes can be defined through Spring annotation scanning, using the 
 * {@link org.simpliccity.sst.service.security.annotation.ServiceAccessValidator} annotation.</p>
 * 
 * <p>This interceptor must be associated with the endpoint mapping using a configuration such as:</p>
 * <pre>
 * 
 * &lt;sws:interceptors&gt;
 *	.
 *	.
 *	&lt;ref bean="domainAccessValidator" /&gt;
 * &lt;/sws:interceptors&gt;
 * &lt;sstws:access id="domainAccessValidator" /&gt;
 * </pre>
 * 
 * <p>The interceptor must be configured to work with the underlying runtime environment by
 * setting the <code>securityHandler</code> property to reference a bean that implements the
 * {@link org.simpliccity.sst.service.security.ServiceSecurityHandler} interface for the applicable security system.  
 * The configuration shown above applies a default handler implementation; the <code>securityHandlerRef</code>
 * attribute of the <code>access-interceptor</code> element can be used to reference an
 * alternative implementation, if desired.</p>
 * 
 * @author Kevin Fox
 * 
 * @see org.simpliccity.sst.ws.endpoint.security.config.AccessValidatorBeanDefinitionParser
 *
 */
public class EndpointAccessValidatorInterceptor extends AbstractSecuredMessageProcessor implements EndpointInterceptor
{
	@Override
	public void afterCompletion(MessageContext messageContext, Object endpoint, Exception ex) 
	{
		// Empty implementation		
	}

	/**
	 * @return <code>true</code> to automatically continue processing of the response chain.
	 */
	@Override
	public boolean handleFault(MessageContext messageContext, Object endpoint) throws Exception 
	{
		// Does not address faults - pass through
		return true;
	}

	/**
	 * Processes all access control rules associated with the endpoint in the request chain through the 
	 * {@link SecuredMessage} and {@link org.simpliccity.sst.service.security.annotation.SemanticConstraint} 
	 * annotations to determine whether processing of the request should be allowed to continue.
	 * 
	 * @param messageContext Contains the incoming request message.
	 * @param endpoint Chosen endpoint to invoke.
	 * @return <code>true</code> if no access control rules are assigned to the endpoint or if at least
	 * one of the assigned rules passes; <code>false</code> otherwise.
	 */
	@Override
	public boolean handleRequest(MessageContext messageContext, Object endpoint) throws Exception 
	{
		getLogger().debug("Processing domain access validation for endpoint [" + endpoint + "]");
		
		// Evaluate access control, if any
		boolean allow = evaluateAccessControl(new MessageContextSemanticContext(messageContext), endpoint, ApplyWhen.PRE);

		if (!allow)
		{
			// For fault handling, throw exception rather than returning false
			throw new ServiceAccessException("User does not have sufficient permissions for requested service");
		}
		
		return allow;
	}

	/**
	 * @return <code>true</code> to automatically continue processing of the response chain.
	 */
	@Override
	public boolean handleResponse(MessageContext messageContext, Object endpoint) throws Exception 
	{
		// Does not address responses - pass through
		return true;
	}
	
	@Override
	protected SecuredMessage getAnnotation(Object controlledObject, ServiceSemanticContext<?> semanticContext)
	{
		AnnotatedElement element = (AnnotatedElement) ((controlledObject instanceof MethodEndpoint) ? ((MethodEndpoint) controlledObject).getMethod() : controlledObject);
		
		return element.getAnnotation(SecuredMessage.class);
	}
}
