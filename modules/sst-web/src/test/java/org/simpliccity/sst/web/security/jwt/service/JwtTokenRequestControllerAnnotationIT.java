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

package org.simpliccity.sst.web.security.jwt.service;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpliccity.sst.jwt.JwtContent;
import org.simpliccity.sst.jwt.JwtReservedClaimNames;
import org.simpliccity.sst.jwt.JwtTokenBuilder;
import org.simpliccity.sst.jwt.JwtTokenReader;
import org.simpliccity.sst.jwt.jjwt.JjwtSimpleJwtTokenReader;
import org.simpliccity.sst.web.security.TestJwtPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes=JwtTokenRequestControllerConfig.class)
@ActiveProfiles("jwtTokenRequestService")
public class JwtTokenRequestControllerAnnotationIT 
{
	private Log logger = LogFactory.getLog(this.getClass());
	
	public static final String JWT_ALGORITHM = JjwtSimpleJwtTokenReader.ALGORITHM_UNSIGNED;
	
	public static final String JWT_ISSUER = "SST";
	
	public static final int JWT_TIME_TO_LIVE = -1;
	
	@Autowired
	private JwtTokenBuilder jwtBuilder;
	
	@Autowired
	private JwtTokenReader jwtReader;
	
	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private MappingJackson2HttpMessageConverter converter;
	
	private MockMvc client;

	@Before
	public void initialize()
	{
		this.client = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void testGenerateSuccess() throws Exception
	{
		JwtTokenRequest request = generateJwtRequest("user1", "USER", "ADMIN");
		
		MockHttpServletResponse response = this.client.perform(post("/generateJwtToken").contentType(MediaType.APPLICATION_JSON_UTF8).content(convertJwtRequest(request)))
			.andExpect(status().isOk())
			.andReturn().getResponse();
		
		String token = response.getContentAsString();
		
		JwtContent content = jwtReader.parseToken(token);
		
		assertTrue("Parsed content matches request", compareRequestAndContent(request, content));
	}
	
	@Test
	public void testParseSuccess() throws Exception
	{
		this.client.perform(post("/parseJwtToken").contentType(MediaType.TEXT_PLAIN).content(generateJwtToken("user1", "USER", "ADMIN")))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(content().json(convertJwtRequest(generateJwtRequest("user1", "USER", "ADMIN"))));
	}
	
	private String generateJwtToken(String subject, String... roles)
	{
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put(JwtReservedClaimNames.SUBJECT, subject);
		claims.put(TestJwtPrincipal.ROLE_CLAIM, Arrays.asList(roles));
		
		return jwtBuilder.buildToken(JWT_ISSUER, claims, JWT_ALGORITHM, JWT_TIME_TO_LIVE);
	}
	
	private JwtTokenRequest generateJwtRequest(String subject, String... roles)
	{
		JwtTokenRequest request = new JwtTokenRequest();
		request.setAlgorithm(JWT_ALGORITHM);
		request.setIssuer(JWT_ISSUER);
		request.setSubject(subject);
		request.setTimeToLive(JWT_TIME_TO_LIVE);
		
		Map<String, Object> claims = new HashMap<>();
		claims.put(TestJwtPrincipal.ROLE_CLAIM, new ArrayList<String>(Arrays.asList(roles)));
		request.setClaims(claims);
		
		return request;
	}
	
	private String convertJwtRequest(JwtTokenRequest request) throws HttpMessageNotWritableException, IOException
	{
		MockHttpOutputMessage message = new MockHttpOutputMessage();
		
		converter.write(request, MediaType.APPLICATION_JSON_UTF8, message);
		
		return message.getBodyAsString();
	}
	
	private boolean compareRequestAndContent(JwtTokenRequest request, JwtContent content)
	{
		logger.debug("JWT token request for comparison: " + request);
		logger.debug("JWT token content for comparison: " + content);
		
		boolean result = true;
		
		result = result && request.getAlgorithm().equals(content.getSigningAlgorithm());
		result = result && request.getIssuer().equals(content.getIssuer());
		result = result && request.getSubject().equals(content.getSubject());
		result = result && (request.getTimeToLive() == content.getTimeToLive());
		
		for (Entry<String, Object> entry : request.getClaims().entrySet())
		{
			result = result && entry.getValue().equals(content.getClaim(entry.getKey()));
		}
		
		return result;
	}
}
