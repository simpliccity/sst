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

package org.simpliccity.sst.data.rest.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.Filter;

import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RestMediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "/AbstractAccessControlRepositoryEventListenerIT.xml" })
public class AbstractAccessControlRepositoryEventListenerIT 
{
	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc client;
	
	@Before
	public void initialize()
	{
		Filter filter = new DelegatingFilterProxy("springSecurityFilterChain", wac);
		this.client = MockMvcBuilders.webAppContextSetup(wac).addFilter(filter, "/*").build();
	}
	
	@Test
	public void testFindPass() throws Exception
	{
		String basicDigestHeaderValue = getAuthenticationHeader("user1", "password");

		this.client.perform(get("/properties/search/findByName").param("name", "user1.url").header("Authorization", basicDigestHeaderValue).contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(status().isOk())
			.andExpect(content().contentType(RestMediaTypes.HAL_JSON))
			.andExpect(jsonPath("value").value("http://www.simpliccity.org"));
	}

	@Test
	public void testFindFail() throws Exception
	{
		String basicDigestHeaderValue = getAuthenticationHeader("user2", "password");

		this.client.perform(get("/properties/search/findByName").param("name", "user1.url").header("Authorization", basicDigestHeaderValue).contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(status().isUnauthorized());		
	}
	
	@Test
	public void testDeleteFail() throws Exception
	{
		String basicDigestHeaderValue = getAuthenticationHeader("user2", "password");

		this.client.perform(delete("/properties/user1.letters").header("Authorization", basicDigestHeaderValue))
			.andExpect(status().isUnauthorized());		
	}

	@Test
	public void testDeletePass() throws Exception
	{
		String basicDigestHeaderValue = getAuthenticationHeader("user2", "password");

		this.client.perform(delete("/properties/user2.date").header("Authorization", basicDigestHeaderValue))
			.andExpect(status().isNoContent());		
	}

	private String getAuthenticationHeader(String username, String password)
	{
		String basicDigestHeaderValue = "Basic " + new String(Base64.encodeBase64((username + ":" + password).getBytes()));
		
		return basicDigestHeaderValue;
	}
}
