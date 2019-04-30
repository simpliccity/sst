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

package org.simpliccity.sst.security.web.jwt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpliccity.sst.jwt.UserJwtTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "/JwtTokenAuthenticationProviderIT.xml" })
public class JwtTokenAuthenticationProviderIT 
{
	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc client;
	
	@Autowired
	private UserJwtTokenGenerator jwt;
	
	@Before
	public void initialize()
	{
		Filter filter = new DelegatingFilterProxy("springSecurityFilterChain", wac);
		this.client = MockMvcBuilders.webAppContextSetup(wac).addFilter(filter, "/*").build();
	}

	@Test
	public void testTokenAuthenticationAccessAllowed() throws Exception
	{
		String jwtToken = generateJwtToken("user1", "USER");

		this.client.perform(get("/capitalize/user1").header("X-AUTH-TOKEN", jwtToken).contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(status().isOk())
			.andExpect(jsonPath("capitalizedString").value("USER1"));
	}

	@Test
	public void testTokenAuthenticationAccessDenied() throws Exception
	{
		String jwtToken = generateJwtToken("user2", "OTHER");

		this.client.perform(get("/capitalize/user2").header("X-AUTH-TOKEN", jwtToken).contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isForbidden());
	}

	@Test
	public void testNoTokenAccessDenied() throws Exception
	{
		this.client.perform(get("/capitalize/user1").contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isUnauthorized());
	}

	private String generateJwtToken(String subject, String... roles)
	{
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (String role : roles)
		{
			authorities.add(new SimpleGrantedAuthority(role));
		}
		
		Authentication principal = new UsernamePasswordAuthenticationToken(subject, null, authorities);
		
		return jwt.generateJwtToken(principal);
	}
}
