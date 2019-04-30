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

package org.simpliccity.sst.data.rest.security;

import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.web.security.HandlerMethodExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.rest.webmvc.RootResourceInformation;
import org.springframework.data.rest.webmvc.config.RootResourceInformationHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

public abstract class AbstractRepositoryControllerHandlerMethodExtractor implements HandlerMethodExtractor 
{
	private Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private RootResourceInformationHandlerMethodArgumentResolver resolver;
	
	@Resource(name = "repositoryExporterHandlerAdapter")
	private RequestMappingHandlerAdapter handlerAdapter;
	
	@Override
	public Method getHandlerMethod(Object handler, HttpServletRequest request) 
	{
		HandlerMethod method = (HandlerMethod) handler;
		MethodParameter parameter = method.getMethodParameters()[getParameterIndex()];
		
		ModelAndViewContainer modelAndView = new ModelAndViewContainer();
		NativeWebRequest webRequest = new DispatcherServletWebRequest(request);
		
		WebDataBinderFactory binderFactory = new ServletRequestDataBinderFactory(new ArrayList<InvocableHandlerMethod>(), handlerAdapter.getWebBindingInitializer());
		
		Method declarationMethod = null;
		if (resolver.supportsParameter(parameter))
		{
			try 
			{
				RootResourceInformation resourceInformation = resolver.resolveArgument(parameter, modelAndView, webRequest, binderFactory);
				
				declarationMethod = getMethodFromResource(resourceInformation, request);
			} 
			catch (Exception e) 
			{
				getLogger().error("Unable to extract resource information from controller parameters.", e);
			}
		}
		
		return declarationMethod;
	}
	
	protected abstract Method getMethodFromResource(RootResourceInformation resourceInformation, HttpServletRequest request);
	
	protected Log getLogger()
	{
		return logger;
	}

	protected int getParameterIndex()
	{
		return 0;
	}
}
