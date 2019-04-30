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

package org.simpliccity.sst.security.web.token;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.simpliccity.sst.web.security.token.HttpRequestTokenHandler;
import org.simpliccity.sst.web.security.token.TokenDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * <p>An implementation of {@link org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter}
 * that can be used in a Spring Security configuration to enable token-based authentication for stateless web requests.
 * This is useful in scenarios in which the protected system is unable to directly authenticate the user associated
 * with a web request.  Instead, the calling system (or an intermediary) asserts the identity of the user through the use
 * of a secure token embedded in an HTTP request.</p>
 * 
 * <p>This filter can be combined in a single filter chain with other authentication mechanisms.  It is configured to process
 * all requests, looking for the presence of the specified token (the means of extracting the token is configurable, 
 * using an {@link HttpRequestTokenHandler}).  If the token is found, the value is processed as an authentication token.</p>
 *  
 * <p>The filter does not directly specify the type of token to use.  Instead, it can be configured with any 
 * {@link org.springframework.security.authentication.AuthenticationProvider} that supports {@link TokenBasedAuthenticationToken}, 
 * allowing it to be adapted for use with a variety of token specifications.</p>
 * 
 * <p>Because this filter is designed specifically for use with stateless web requests (e.g. ReST web service calls), it is
 * configured not to change the request destination upon successful authentication (see {@link NoopAuthenticationSuccessHandler}).</p>
 * 
 * @author Kevin Fox
 * @since 1.0.0
 *
 */
public class StatelessTokenAssertionAuthenticationFilter extends AbstractAuthenticationProcessingFilter 
{
	private HttpRequestTokenHandler tokenHandler;
	
	@Autowired(required=false)
	private AuthenticationTokenWrapper<? extends TokenBasedAuthenticationToken> tokenWrapper;
	
	/**
	 * Default constructor that configures basic behavior, including the processing of all requests
	 * and the use of the {@link NoopAuthenticationSuccessHandler}.
	 */
	public StatelessTokenAssertionAuthenticationFilter()
	{
		super(new AntPathRequestMatcher("/**"));
		setAuthenticationSuccessHandler(new NoopAuthenticationSuccessHandler());
	}
	
	public HttpRequestTokenHandler getTokenHandler() 
	{
		return tokenHandler;
	}

	public void setTokenHandler(HttpRequestTokenHandler tokenHandler) 
	{
		this.tokenHandler = tokenHandler;
	}

	public AuthenticationTokenWrapper<? extends TokenBasedAuthenticationToken> getTokenWrapper() 
	{
		return tokenWrapper;
	}

	public void setTokenWrapper(AuthenticationTokenWrapper<? extends TokenBasedAuthenticationToken> tokenWrapper) 
	{
		this.tokenWrapper = tokenWrapper;
	}

	/**
	 * Performs authentication by processing the value of the specified HTTP header as a security token using the configured
	 * {@link org.springframework.security.authentication.AuthenticationProvider}.
	 * 
	 * @param request The incoming servlet request to process.
	 * @param response The corresponding servlet response.
	 * @return The authenticated user or null.
	 * @throws AuthenticationException if authentication fails.
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException 
	{
		TokenDescriptor token = getTokenHandler().extractToken(new ServletServerHttpRequest(request));
		
		if (token == null)
		{
			String msg = "Expected authentication token not found in request.";
			logger.error(msg);
			throw new AuthenticationTokenMissingException(msg);
		}
		
		TokenAuthenticationDetails details = new TokenAuthenticationDetails(request, token, null);
		Authentication unauthenticated = new TokenBasedAuthenticationToken(details);
		
		Authentication authenticated = getAuthenticationManager().authenticate(unauthenticated);
		
		if (authenticated instanceof TokenBasedAuthenticationToken && getTokenWrapper() != null)
		{
			authenticated = getTokenWrapper().wrapAuthenticationToken((TokenBasedAuthenticationToken) authenticated);
		}
		
		return authenticated;
	}

	/**
	 * <p>Performs post-processing upon successful authentication.  Supplements the default behavior (see below) by
	 * continuing the filter chain.</p>
	 * 
	 * <p><b>Default behavior</b></p>
	 * <p>{@inheritDoc}</p>
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException 
	{
		super.successfulAuthentication(request, response, chain, authResult);
		
		chain.doFilter(request, response);
	}
}
