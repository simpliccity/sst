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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.simpliccity.sst.bean.annotation.BeanAnnotationUtils;
import org.simpliccity.sst.service.security.AbstractSecuredMessageProcessor;
import org.simpliccity.sst.service.security.ServiceAccessException;
import org.simpliccity.sst.service.security.ServiceSecurityHandler;
import org.simpliccity.sst.service.security.ServiceSemanticContext;
import org.simpliccity.sst.service.security.annotation.ApplyWhen;
import org.simpliccity.sst.service.security.annotation.SecuredMessage;
import org.simpliccity.sst.web.security.HttpServiceAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class AbstractAccessControlRepositoryEventListener extends AbstractSecuredMessageProcessor
{
	@Autowired
	@Override
	public void setSecurityHandler(ServiceSecurityHandler securityHandler)
	{
		super.setSecurityHandler(securityHandler);
	}
	
	@Override
	protected SecuredMessage getAnnotation(Object controlledObject,	ServiceSemanticContext<?> semanticContext) 
	{
		return ((Method) controlledObject).getAnnotation(SecuredMessage.class);
	}
	
	protected Method getControlledMethod(Class<? extends Annotation> handlerAnnotation)
	{
		return BeanAnnotationUtils.getAnnotatedMethodFromInstance(handlerAnnotation, this);
	}
	
	protected void checkRepositoryEventAccess(Object entity, Class<? extends Annotation> handlerAnnotation) throws ServiceAccessException
	{
		Method handlerMethod = getControlledMethod(handlerAnnotation);
		
		boolean allow = evaluateAccessControl(new EntityServiceSemanticContext(entity), handlerMethod, ApplyWhen.PRE);
		
		if (!allow)
		{
			// For error status handling, throw exception
			throw new HttpServiceAccessException("User does not have sufficient permissions for requested service");
		}		
	}
}
