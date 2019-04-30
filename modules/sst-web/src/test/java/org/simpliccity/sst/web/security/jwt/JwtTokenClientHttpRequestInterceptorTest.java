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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.simpliccity.sst.jwt.JwtTokenPrincipal;
import org.simpliccity.sst.jwt.UserJwtTokenGenerator;
import org.simpliccity.sst.service.security.ServiceSecurityHandler;
import org.simpliccity.sst.service.security.ServiceSemanticContext;
import org.simpliccity.sst.web.security.token.HttpRequestTokenHandler;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.mock.http.client.MockClientHttpRequest;

@RunWith(MockitoJUnitRunner.class)
public class JwtTokenClientHttpRequestInterceptorTest 
{
	public static final String JWT_TOKEN_CACHED = "ABC";

	public static final String JWT_TOKEN_GENERATED = "DEF";

	private MockClientHttpRequest request;
	
	@Mock private ClientHttpRequestExecution execution;
	
	@Mock private ServiceSecurityHandler securityHandler;
	
	@Mock private UserJwtTokenGenerator tokenGenerator;
	
	@Mock private HttpRequestTokenHandler tokenHandler;
	
	private JwtTokenClientHttpRequestInterceptor interceptor = new JwtTokenClientHttpRequestInterceptor();

	private JwtTokenClientHttpRequestInterceptor initInterceptor = new JwtTokenClientHttpRequestInterceptor();

	@Mock private JwtTokenPrincipal jwtUser;

	@Mock private JwtTokenPrincipal expiredUser;

	@Mock private Principal otherUser;
	
	@Mock private Principal unrecognizedUser;
		
	@Before
	public void init() throws URISyntaxException
	{
		request = new MockClientHttpRequest(HttpMethod.POST, new URI("http://test.com/test"));

		interceptor.setSecurityHandler(securityHandler);
		interceptor.setTokenGenerator(tokenGenerator);
		interceptor.setTokenHandler(tokenHandler);

		when(jwtUser.getName()).thenReturn("JWT Principal");
		when(jwtUser.isExpired()).thenReturn(false);
		when(jwtUser.getJwtToken()).thenReturn(JWT_TOKEN_CACHED);

		when(expiredUser.getName()).thenReturn("Expired JWT Principal");
		when(expiredUser.isExpired()).thenReturn(true);
	
		when(otherUser.getName()).thenReturn("Non-JWT Principal");

		when(unrecognizedUser.getName()).thenReturn("Unsupported Principal");

		when(tokenGenerator.generateJwtToken(expiredUser)).thenReturn(JWT_TOKEN_GENERATED);
		when(tokenGenerator.generateJwtToken(otherUser)).thenReturn(JWT_TOKEN_GENERATED);
		when(tokenGenerator.generateJwtToken(unrecognizedUser)).thenReturn(null);
	}
	
	@Test
	public void testNewPrincipal() throws IOException
	{
		when(securityHandler.getPrincipal(any(ServiceSemanticContext.class))).thenReturn(otherUser);
		
		interceptor.intercept(request, null, execution);
		
		verify(tokenHandler).addToken(request, JWT_TOKEN_GENERATED);
	}

	@Test
	public void testExistingJwtPrincipal() throws IOException
	{
		when(securityHandler.getPrincipal(any(ServiceSemanticContext.class))).thenReturn(jwtUser);
		
		interceptor.intercept(request, null, execution);
		
		verify(tokenHandler).addToken(request, JWT_TOKEN_CACHED);
	}

	@Test
	public void testExpiredJwtPrincipal() throws IOException
	{
		when(securityHandler.getPrincipal(any(ServiceSemanticContext.class))).thenReturn(expiredUser);
		
		interceptor.intercept(request, null, execution);
		
		verify(tokenHandler).addToken(request, JWT_TOKEN_GENERATED);
	}

	@Test
	public void testUnsupportedPrincipal() throws IOException
	{
		when(securityHandler.getPrincipal(any(ServiceSemanticContext.class))).thenReturn(unrecognizedUser);
		
		interceptor.intercept(request, null, execution);
		
		verify(tokenHandler, never()).addToken(request, null);
	}

	@Test(expected = BeanInitializationException.class)
	public void testInitMissingSecurity() throws Exception
	{
		initInterceptor.setSecurityHandler(null);
		initInterceptor.setTokenGenerator(tokenGenerator);
		initInterceptor.setTokenHandler(tokenHandler);
		
		initInterceptor.afterPropertiesSet();
	}

	@Test(expected = BeanInitializationException.class)
	public void testInitMissingGenerator() throws Exception
	{
		initInterceptor.setSecurityHandler(securityHandler);
		initInterceptor.setTokenGenerator(null);
		initInterceptor.setTokenHandler(tokenHandler);
		
		initInterceptor.afterPropertiesSet();
	}

	@Test(expected = BeanInitializationException.class)
	public void testInitMissingHandler() throws Exception
	{
		initInterceptor.setSecurityHandler(securityHandler);
		initInterceptor.setTokenGenerator(tokenGenerator);
		initInterceptor.setTokenHandler(null);
		
		initInterceptor.afterPropertiesSet();
	}
	
	public void testCorrectInit() throws Exception
	{
		initInterceptor.setSecurityHandler(securityHandler);
		initInterceptor.setTokenGenerator(tokenGenerator);
		initInterceptor.setTokenHandler(tokenHandler);
		
		initInterceptor.afterPropertiesSet();
	
		assertEquals("Security handler correctly set.", securityHandler, initInterceptor.getSecurityHandler());
		assertEquals("Token generator correctly set.", tokenGenerator, initInterceptor.getTokenGenerator());
		assertEquals("Token handler correctly set.", tokenHandler, initInterceptor.getTokenHandler());
	}
}
