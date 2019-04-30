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

package org.simpliccity.sst.jwt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class PrincipalSubjectJwtClaimsGeneratorTest 
{
	public static final String USER1 = "user1";
	
	public static final String USER2 = "user2";
	
	private Principal user;
	
	private Map<String, Object> claims;
	
	private JwtClaimsGenerator generator;
	
	@Before
	public void init()
	{
		user = new SimpleTestPrincipal(USER1);
		claims = new HashMap<String, Object>();
		generator = new PrincipalSubjectJwtClaimsGenerator();		
	}
	
	@Test
	public void testAddSubject()
	{
		
		claims = generator.generateClaims(user, claims);
		
		String subject = (String) claims.get(JwtReservedClaimNames.SUBJECT);
		
		assertNotNull("Subject exists.", subject);
		assertEquals("Subject is correct.", USER1, subject);
	}
	
	@Test
	public void testOverrideSubject()
	{
		// Set an initial subject that should be overridden by the claims generation
		claims.put(JwtReservedClaimNames.SUBJECT, USER2);
		
		String subject = (String) claims.get(JwtReservedClaimNames.SUBJECT);

		assertNotNull("Original subject exists.", subject);
		assertEquals("Original subject is correct.", USER2, subject);		

		claims = generator.generateClaims(user, claims);
		
		subject = (String) claims.get(JwtReservedClaimNames.SUBJECT);
		
		assertNotNull("Overridden subject exists.", subject);
		assertEquals("Overridden subject is correct.", USER1, subject);		
	}
}
