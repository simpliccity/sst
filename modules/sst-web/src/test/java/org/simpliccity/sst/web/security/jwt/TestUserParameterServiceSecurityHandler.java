/*
 *    Copyright 2016 Information Control Company
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

package org.simpliccity.sst.web.security.jwt;

import java.security.Principal;

import org.simpliccity.sst.service.security.ServiceSecurityHandler;
import org.simpliccity.sst.service.security.ServiceSemanticContext;

public class TestUserParameterServiceSecurityHandler implements ServiceSecurityHandler 
{
	public static final String PARAMETER_USER = "user";
	
	@Override
	public Principal getPrincipal(ServiceSemanticContext<?> semanticContext) 
	{
		String name = (String) semanticContext.getParameter(PARAMETER_USER)[0];
		return new TestSimpleNamePrincipal(name);
	}

	@Override
	public boolean inApplicableRole(ServiceSemanticContext<?> semanticContext,	String[] roles) 
	{
		return false;
	}
}
