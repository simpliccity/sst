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

package org.simpliccity.sst.security.service;

import java.security.Principal;
import java.util.Collection;

import org.simpliccity.sst.security.role.RoleUtils;
import org.simpliccity.sst.service.security.ServiceSecurityHandler;
import org.simpliccity.sst.service.security.ServiceSemanticContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * An implementation of {@link org.simpliccity.sst.service.security.ServiceSecurityHandler} based on Spring 
 * Security.  This allows the {@link org.simpliccity.sst.ws.endpoint.security.EndpointAccessValidatorInterceptor}
 * to be used in a runtime environment that relies on Spring Security for authentication and authorization.
 * 
 * @author Kevin Fox
 * @since 1.0.0
 *
 */
public class SpringSecurityServiceSecurityHandler implements ServiceSecurityHandler 
{
	@Override
	public Principal getPrincipal(ServiceSemanticContext<?> semanticContext) 
	{
		return SecurityContextHolder.getContext().getAuthentication();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean inApplicableRole(ServiceSemanticContext<?> semanticContext, String[] roles) 
	{
		Principal user = getPrincipal(semanticContext);

		boolean result = false;
		if (user instanceof Authentication)
		{
			Collection<GrantedAuthority> assigned = (Collection<GrantedAuthority>) ((Authentication) user).getAuthorities();
			for (String role : roles)
			{
				result = RoleUtils.hasAuthority(assigned, role);
				if (result)
				{
					break;
				}
			}
		}
		
		return result;
	}
}
