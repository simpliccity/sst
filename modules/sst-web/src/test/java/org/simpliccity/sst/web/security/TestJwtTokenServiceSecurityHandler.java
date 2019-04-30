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

package org.simpliccity.sst.web.security;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.simpliccity.sst.jwt.InvalidJwtTokenException;
import org.simpliccity.sst.jwt.JwtContent;
import org.simpliccity.sst.jwt.JwtTokenReader;
import org.simpliccity.sst.service.security.ServiceSecurityHandler;
import org.simpliccity.sst.service.security.ServiceSemanticContext;
import org.simpliccity.sst.web.security.token.HttpRequestTokenHandler;
import org.simpliccity.sst.web.security.token.TokenDescriptor;
import org.springframework.http.HttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;

public class TestJwtTokenServiceSecurityHandler implements ServiceSecurityHandler 
{
	private HttpRequestTokenHandler tokenHandler;
	
	private JwtTokenReader jwtTokenReader;
	
	public HttpRequestTokenHandler getTokenHandler() 
	{
		return tokenHandler;
	}

	public void setTokenHandler(HttpRequestTokenHandler tokenHandler) 
	{
		this.tokenHandler = tokenHandler;
	}

	public JwtTokenReader getJwtTokenReader() 
	{
		return jwtTokenReader;
	}

	public void setJwtTokenReader(JwtTokenReader jwtTokenReader) 
	{
		this.jwtTokenReader = jwtTokenReader;
	}

	@Override
	public Principal getPrincipal(ServiceSemanticContext<?> semanticContext) 
	{
		Principal result = null;
		
		HttpRequest request = new ServletServerHttpRequest((HttpServletRequest) semanticContext.getRequest());
		
		TokenDescriptor token = getTokenHandler().extractToken(request);
		
		try 
		{
			JwtContent content = getJwtTokenReader().parseToken(token.getValue());
			result = new TestJwtPrincipal(content);
		} 
		catch (InvalidJwtTokenException e) 
		{
			// Nothing to do
		}
		
		return result;
	}

	@Override
	public boolean inApplicableRole(ServiceSemanticContext<?> semanticContext, String[] roles) 
	{
		TestJwtPrincipal user = (TestJwtPrincipal) getPrincipal(semanticContext);
		
		List<String> userRoles = user.getRoles();
		
		boolean result = false;
		for (String role : roles)
		{
			result = userRoles.contains(role);
			
			if (result)
			{
				break;
			}
		}
		
		return result;
	}

}
