/*
 *    Copyright 2012 Information Control Corporation
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

package org.simpliccity.sst.service.security.rules;

import java.security.Principal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.simpliccity.sst.service.security.rules.IdentityServiceAccessControlRule;

@RunWith(MockitoJUnitRunner.class)
public class IdentityEndpointAccessControlRuleTest 
{
	private static final String USER1 = "user1";
	private static final String USER2 = "user2";
	
	private IdentityServiceAccessControlRule rule;
	@Mock private Principal mockPrincipal;
	
	@Before
	public void initialize()
	{
		rule = new IdentityServiceAccessControlRule();
	}
	
	/**
	 * Tests that a userid that matches the specified value passes the rule.
	 */
	@Test
	public void testMatchingValues()
	{
		when(mockPrincipal.getName()).thenReturn(USER1);
		
		assertTrue("User access allowed", rule.allowAccess(mockPrincipal, USER1));
	}
	
	/**
	 * Tests that a userid that does not match the specified value fails the rule.
	 */
	@Test
	public void testNonMatchingValues()
	{
		when(mockPrincipal.getName()).thenReturn(USER1);
		
		assertFalse("User access denied", rule.allowAccess(mockPrincipal, USER2));		
	}

	/**
	 * Tests that any userid passes the rule when no value is specified.
	 */
	@Test
	public void testUniversalAccess()
	{
		when(mockPrincipal.getName()).thenReturn(USER1);
		
		assertTrue("Universal access allowed - empty content", rule.allowAccess(mockPrincipal, ""));

		when(mockPrincipal.getName()).thenReturn(USER2);
		
		assertTrue("Universal access allowed - null content", rule.allowAccess(mockPrincipal, null));
	}

	/**
	 * Tests that anonymous access (no userid) fails the rule when a value is specified.
	 */
	@Test
	public void testAnonymousAccess()
	{
		when(mockPrincipal.getName()).thenReturn("");
		
		assertFalse("Anonymous access denied", rule.allowAccess(mockPrincipal, USER2));		
	}
}
