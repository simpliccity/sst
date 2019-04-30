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

import java.io.IOException;
import java.security.Principal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.jwt.JwtTokenPrincipal;
import org.simpliccity.sst.jwt.UserJwtTokenGenerator;
import org.simpliccity.sst.service.security.ServiceSecurityHandler;
import org.simpliccity.sst.web.security.ClientHttpRequestServiceSemanticContext;
import org.simpliccity.sst.web.security.token.HttpRequestTokenHandler;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class JwtTokenClientHttpRequestInterceptor implements ClientHttpRequestInterceptor, InitializingBean
{
	private Log logger = LogFactory.getLog(this.getClass());
	
	private ServiceSecurityHandler securityHandler;
	
	private UserJwtTokenGenerator tokenGenerator;
	
	private HttpRequestTokenHandler tokenHandler;
	
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException 
	{
		ClientHttpRequestServiceSemanticContext semanticContext = new ClientHttpRequestServiceSemanticContext((ClientHttpRequest) request);
		Principal user = getSecurityHandler().getPrincipal(semanticContext);
		
		String token;
		if (user instanceof JwtTokenPrincipal && !((JwtTokenPrincipal) user).isExpired())
		{
			// Allows to propagate an existing JWT token for the user
			token = ((JwtTokenPrincipal) user).getJwtToken();
			logger.debug("Reused existing token for principal [" + user.getName() + "].");
		}
		else
		{
			// Generates a new token for the user
			token = getTokenGenerator().generateJwtToken(user);
			logger.debug("Generated new token for principal [" + user.getName() + "].");
		}
		
		if (token == null)
		{
			logger.warn("No JWT token was injected for the client HTTP request.");
		}
		else
		{
			getTokenHandler().addToken(request, token);
			logger.debug("A JWT token was injected for the client HTTP request using token handler [" + tokenHandler.getClass().getName() + "].");
		}
		
		return execution.execute(request, body);
	}
	
	public ServiceSecurityHandler getSecurityHandler() 
	{
		return securityHandler;
	}

	public void setSecurityHandler(ServiceSecurityHandler securityHandler) 
	{
		this.securityHandler = securityHandler;
	}

	public UserJwtTokenGenerator getTokenGenerator() 
	{
		return tokenGenerator;
	}

	public void setTokenGenerator(UserJwtTokenGenerator tokenGenerator) 
	{
		this.tokenGenerator = tokenGenerator;
	}

	public HttpRequestTokenHandler getTokenHandler() 
	{
		return tokenHandler;
	}

	public void setTokenHandler(HttpRequestTokenHandler tokenHandler) 
	{
		this.tokenHandler = tokenHandler;
	}

	@Override
	public void afterPropertiesSet() throws Exception 
	{
		if (securityHandler == null)
		{
			throw new BeanInitializationException("JwtTokenClientHttpRequestInterceptor incorrectly configured: service security handler must be specified.");
		}
		
		if (tokenGenerator == null)
		{
			throw new BeanInitializationException("JwtTokenClientHttpRequestInterceptor incorrectly configured: token generator must be specified.");
		}

		if (tokenHandler == null)
		{
			throw new BeanInitializationException("JwtTokenClientHttpRequestInterceptor incorrectly configured: token handler must be specified.");
		}
	}
}
