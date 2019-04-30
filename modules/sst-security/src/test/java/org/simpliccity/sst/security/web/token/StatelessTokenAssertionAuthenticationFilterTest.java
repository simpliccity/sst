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

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.simpliccity.sst.web.security.token.HttpRequestTokenHandler;
import org.simpliccity.sst.web.security.token.TokenDescriptor;
import org.simpliccity.sst.web.security.token.TokenSource;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RunWith(MockitoJUnitRunner.class)
public class StatelessTokenAssertionAuthenticationFilterTest 
{
	@Mock AuthenticationProvider authProvider;
	@Mock HttpRequestTokenHandler tokenHandler;
	@Mock private HttpServletRequest request;
	@Mock private HttpServletResponse response;
	@Mock private TestAuthenticationTokenWrapper wrapper;
	
	@Test
	public void testWrapAuthentication() throws AuthenticationException, IOException, ServletException
	{
		StatelessTokenAssertionAuthenticationFilter filter = getFilter(getTestToken());
		
		filter.setTokenWrapper(new TestAuthenticationTokenWrapper());
		
		Authentication authentication = filter.attemptAuthentication(request, response);
		
		assertTrue("Authentication token wrapped", authentication instanceof TestTokenBasedAuthenticationToken);
	}
	
	@Test
	public void testNoWrapAuthentication() throws AuthenticationException, IOException, ServletException
	{
		StatelessTokenAssertionAuthenticationFilter filter = getFilter(getTestToken());
		
		Authentication authentication = filter.attemptAuthentication(request, response);
		
		assertTrue("Authentication token not wrapped", authentication instanceof TokenBasedAuthenticationToken);		
	}
	
	@Test
	public void testFailAuthentication() throws IOException, ServletException
	{
		StatelessTokenAssertionAuthenticationFilter filter = getFilter(null);
		
		try
		{
			filter.attemptAuthentication(request, response);
		}
		catch (AuthenticationException e)
		{
			// This is expected, nothing to do
		}
		
		// Make sure that wrapper class not accessed when authentication provider returned null
		verify(wrapper, never()).wrapAuthenticationToken((TokenBasedAuthenticationToken) any());
	}
	
	private TokenBasedAuthenticationToken getTestToken()
	{
		TokenDescriptor descriptor = new TokenDescriptor(TokenSource.HEADER, "Test", "123");
		TokenAuthenticationDetails details = new TokenAuthenticationDetails(request, descriptor, null);
		TokenBasedAuthenticationToken token = new TokenBasedAuthenticationToken(details, null);
		
		return token;
	}
	
	private StatelessTokenAssertionAuthenticationFilter getFilter(TokenBasedAuthenticationToken token)
	{
		StatelessTokenAssertionAuthenticationFilter filter = new StatelessTokenAssertionAuthenticationFilter();
		
		when(authProvider.supports(TokenBasedAuthenticationToken.class)).thenReturn(true);
		when(authProvider.authenticate((Authentication) any())).thenReturn(token);
		ProviderManager manager = new ProviderManager(Arrays.asList(authProvider));
		filter.setAuthenticationManager(manager);

		when(tokenHandler.extractToken((HttpRequest) any())).thenReturn(new TokenDescriptor(TokenSource.HEADER, "Test", "123"));
		filter.setTokenHandler(tokenHandler);
		
		return filter;
	}
}
