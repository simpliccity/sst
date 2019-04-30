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

package org.simpliccity.sst.web.security.token;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.simpliccity.sst.web.security.token.TokenDescriptor;
import org.simpliccity.sst.web.security.token.AbstractHeaderTokenHandler;
import org.simpliccity.sst.web.security.token.AuthorizationBearerHeaderTokenHandler;
import org.simpliccity.sst.web.security.token.XAuthHeaderTokenHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;

@RunWith(MockitoJUnitRunner.class)
public class AbstractHeaderTokenHandlerTest 
{
	@Mock private HttpRequest request;
	@Mock private HttpHeaders headers;
	
	@Test
	public void testAddToken()
	{
		when(request.getHeaders()).thenReturn(headers);
		
		String token = "123";
		
		AbstractHeaderTokenHandler handler = new XAuthHeaderTokenHandler();
		
		handler.addToken(request, token);
		
		verify(headers, times(1)).add(handler.getHeaderName(), token);
	}
	
	@Test
	public void testAddTokenEmptyValue()
	{
		String token = null;
		
		AbstractHeaderTokenHandler handler = new XAuthHeaderTokenHandler();
		
		handler.addToken(request, token);
		
		verify(headers, never()).add(anyString(), anyString());		
	}
	
	@Test
	public void testAddTokenPrepareHeader()
	{
		when(request.getHeaders()).thenReturn(headers);
		
		String token = "123";
		String header = "Bearer 123";
		
		AbstractHeaderTokenHandler handler = new AuthorizationBearerHeaderTokenHandler();
		
		handler.addToken(request, token);
		
		verify(headers, times(1)).add(handler.getHeaderName(), header);		
	}
	
	@Test
	public void testExtractToken()
	{
		String token = "123";
		
		AbstractHeaderTokenHandler handler = new XAuthHeaderTokenHandler();

		when(request.getHeaders()).thenReturn(headers);
		when(headers.getFirst(handler.getHeaderName())).thenReturn(token);
		
		TokenDescriptor result = handler.extractToken(request);
		
		assertEquals("Extract token", token, result.getValue());
	}

	@Test
	public void testExtractTokenExtractFromHeader()
	{
		String token = "123";
		String header = "Bearer 123";
	
		AbstractHeaderTokenHandler handler = new AuthorizationBearerHeaderTokenHandler();

		when(request.getHeaders()).thenReturn(headers);
		when(headers.getFirst(handler.getHeaderName())).thenReturn(header);
		
		TokenDescriptor result = handler.extractToken(request);
		
		assertEquals("Extract token", token, result.getValue());
	}

	@Test
	public void testExtractTokenMissingHeader()
	{
		AbstractHeaderTokenHandler handler = new XAuthHeaderTokenHandler();

		when(request.getHeaders()).thenReturn(headers);
		when(headers.getFirst(handler.getHeaderName())).thenReturn(null);

		TokenDescriptor result = handler.extractToken(request);

		assertNull("Extract token missing header", result);
	}
}
