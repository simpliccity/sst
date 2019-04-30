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

package org.simpliccity.sst.web.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpliccity.sst.jwt.JwtReservedClaimNames;
import org.simpliccity.sst.jwt.JwtTokenBuilder;
import org.simpliccity.sst.web.security.token.XAuthHeaderTokenHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "/ServiceAccessControlHandlerInterceptorIT.xml" })
public class ServiceAccessControlHandlerInterceptorIT 
{
	@Autowired
	private JwtTokenBuilder jwtBuilder;
	
	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc client;
	
	@Before
	public void initialize()
	{
		this.client = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@Test
	public void testJsonPayloadSuccess() throws Exception
	{
		String jwtToken = generateJwtToken("user1", "ROLE_USER");

		this.client.perform(post("/sendgreeting").header(XAuthHeaderTokenHandler.HEADER_NAME, jwtToken).contentType(MediaType.APPLICATION_JSON_UTF8).content(generateGreetingRequest("user1")))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(content().json(generateGreetingResponse()));
	}

	@Test
	public void testJsonPayloadAccessDenied() throws Exception
	{
		String jwtToken = generateJwtToken("user1", "ROLE_USER");

		this.client.perform(post("/sendgreeting").header(XAuthHeaderTokenHandler.HEADER_NAME, jwtToken).contentType(MediaType.APPLICATION_JSON_UTF8).content(generateGreetingRequest("user2")))
			.andExpect(status().isUnauthorized());
	}

	@Test
	public void testUriPatternSuccess() throws Exception
	{
		String jwtToken = generateJwtToken("user1", "ROLE_USER");

		this.client.perform(get("/greeting/1").header(XAuthHeaderTokenHandler.HEADER_NAME, jwtToken))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("greeting").value("Hi y'all"));
	}

	@Test
	public void testUriPatternAccessDenied() throws Exception
	{
		String jwtToken = generateJwtToken("user1", "ROLE_USER");

		this.client.perform(get("/greeting/2").header(XAuthHeaderTokenHandler.HEADER_NAME, jwtToken))
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testParameterSuccess() throws Exception
	{
		String jwtToken = generateJwtToken("user1", "ROLE_USER");

		this.client.perform(get("/getgreeting").header(XAuthHeaderTokenHandler.HEADER_NAME, jwtToken).param("greetingId", "1"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(content().json(generateGreetingResponse()));
	}

	@Test
	public void testParameterAccessDenied() throws Exception
	{
		String jwtToken = generateJwtToken("user1", "ROLE_USER");

		this.client.perform(get("/getgreeting").header(XAuthHeaderTokenHandler.HEADER_NAME, jwtToken).param("greetingId", "2"))
			.andExpect(status().isUnauthorized());
	}

//	@Test
//	public void testPostHandle() throws Exception
//	{
//		String basicDigestHeaderValue = getAuthenticationHeader("user1", "password");
//
//		this.client.perform(get("/southerngreeting").header("Authorization", basicDigestHeaderValue))
//			.andExpect(status().isOk())
//			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//			.andExpect(content().json(generateSouthernGreetingResponse()));
//	}

	private String generateGreetingRequest(String username)
	{
		return "{\"to\" : \"world\", \"from\" : \"" + username + "\", \"message\" : \"Hello\"}";
	}
	
	private String generateGreetingResponse()
	{
		return "{\"greeting\":\"Hello, world\"}";
	}
	
//	private String generateSouthernGreetingResponse()
//	{
//		return "{\"greeting\":\"Howdee!\"}";
//	}
	
	private String generateJwtToken(String subject, String... roles)
	{
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put(JwtReservedClaimNames.SUBJECT, subject);
		claims.put(TestJwtPrincipal.ROLE_CLAIM, Arrays.asList(roles));
		
		return jwtBuilder.buildToken("SST", claims, "HS512", 300);
	}
}
