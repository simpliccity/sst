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

import org.junit.Test;
import org.simpliccity.sst.web.security.token.AuthorizationBearerHeaderTokenHandler;

public class AuthorizationBearerHeaderTokenHandlerTest 
{
	@Test
	public void testPrepareHeader()
	{
		String token = "123";
		String expected = "Bearer 123";
		
		String result = new AuthorizationBearerHeaderTokenHandler().prepareHeader(token);
		
		assertEquals("Prepare header", expected, result);
	}
	
	@Test
	public void testExtractFromHeader()
	{
		String headerValue = "Bearer 123";
		
		String expected = "123";
		
		String result = new AuthorizationBearerHeaderTokenHandler().extractFromHeader(headerValue);
		
		assertEquals("Extract correct format", expected, result);
	}

	@Test
	public void testExtractFromHeaderLeadingWhitespace()
	{
		String headerValueLeadingWhitespace = "  Bearer 123";
	
		String expected = "123";

		String result = new AuthorizationBearerHeaderTokenHandler().extractFromHeader(headerValueLeadingWhitespace);
		
		assertEquals("Extract with leading whitespace", expected, result);
	}

	@Test
	public void testExtractFromHeaderInternalWhitespace()
	{
		String headerValueInternalWhitespace = "Bearer    123";
		
		String expected = "123";

		String result = new AuthorizationBearerHeaderTokenHandler().extractFromHeader(headerValueInternalWhitespace);
		
		assertEquals("Extract with internal whitespace", expected, result);
	}
	
	@Test
	public void testExtractFromHeaderTrailingWhitespace()
	{
		String headerValueTrailingWhitespace = "Bearer 123   ";
		
		String expected = "123";

		String result = new AuthorizationBearerHeaderTokenHandler().extractFromHeader(headerValueTrailingWhitespace);
		
		assertEquals("Extract with trailing whitespace", expected, result);
	}
	
	@Test
	public void testExtractMismatchFormat()
	{
		String headerValueMismatch = "Bad header format";
		
		String result = new AuthorizationBearerHeaderTokenHandler().extractFromHeader(headerValueMismatch);
		
		assertNull("Extract mismatched format", result);
	}
}
