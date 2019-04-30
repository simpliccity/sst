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

package org.simpliccity.sst.web.security;

import java.lang.reflect.AnnotatedElement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.simpliccity.sst.service.security.AbstractSecuredMessageProcessor;
import org.simpliccity.sst.service.security.ServiceAccessException;
import org.simpliccity.sst.service.security.ServiceSemanticContext;
import org.simpliccity.sst.service.security.annotation.ApplyWhen;
import org.simpliccity.sst.service.security.annotation.SecuredMessage;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class ServiceAccessControlHandlerInterceptor extends AbstractSecuredMessageProcessor implements HandlerInterceptor 
{
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception interception) throws Exception 
	{
		// Empty implementation		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception 
	{
		processAccess(new HttpResponseServiceSemanticContext(request, response), handler, ApplyWhen.POST);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,	Object handler) throws Exception 
	{
		return processAccess(new HttpRequestServiceSemanticContext(request), handler, ApplyWhen.PRE);
	}

	@Override
	protected SecuredMessage getAnnotation(Object controlledObject, ServiceSemanticContext<?> semanticContext) 
	{
		AnnotatedElement element = (AnnotatedElement) ((controlledObject instanceof HandlerMethod) ? ((HandlerMethod) controlledObject).getMethod() : controlledObject);
		return element.getAnnotation(SecuredMessage.class);
	}
	
	@Override
	protected Object getControlledObject(Object object, ServiceSemanticContext<?> semanticContext)
	{
		Class<? extends Object> handlerClass = (object instanceof HandlerMethod) ? ((HandlerMethod) object).getBeanType() : object.getClass();
		
		HandlerMethodExtractorBeanMetadataLookupContainer container = new HandlerMethodExtractorBeanMetadataLookupContainer(handlerClass.getName());
		HandlerMethodExtractor extractor = (HandlerMethodExtractor) getLookupManager().lookupBeanByMetadata(container);
		
		// If there is an extractor specific to the type of handler, use it to retrieve the derived object
		return (extractor == null) ? object : extractor.getHandlerMethod(object, (HttpServletRequest) semanticContext.getRequest());
	}
	
	private boolean processAccess(ServiceSemanticContext<?> context, Object handler, ApplyWhen when) throws HttpServiceAccessException
	{
		getLogger().debug(when + ": processing access control for handler [" + handler + "]");
		
		// Evaluate access control, if any
		boolean allow = false;
		try
		{
			allow = evaluateAccessControl(context, handler, when);
		}
		catch (ServiceAccessException e)
		{
			// Wrap exception to ensure correct HTTP response status 
			throw new HttpServiceAccessException(e);
		}

		if (!allow)
		{
			// For error status handling, throw exception rather than returning false
			throw new HttpServiceAccessException("User does not have sufficient permissions for requested service");
		}
		
		return allow;
	}
}
