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

import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpliccity.sst.jwt.UserJwtTokenGenerator;
import org.simpliccity.sst.service.security.ServiceSecurityHandler;
import org.simpliccity.sst.web.security.SendGreetingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={JwtTokenClientHttpRequestInterceptorCacheConfig.class})
@ActiveProfiles({"jwtClientInterceptor", "jwtClientInterceptorCache"})
public class JwtTokenClientHttpRequestInterceptorCacheIT 
{
	@Autowired
	RestTemplate template;
	
	@Autowired
	UserJwtTokenGenerator generator;
	
	@Autowired
	ServiceSecurityHandler handler;
	
	@Test
	public void testAddToken()
	{
		MockRestServiceServer server = MockRestServiceServer.createServer(template);
		server.expect(header("X-AUTH-TOKEN", generateToken("test"))).andRespond(withSuccess());
		
		template.getForObject("http://simpliccity.org/test?user=test", SendGreetingResponse.class);
		
		server.verify();
	}

	@Test
	public void testAddAnotherToken()
	{
		MockRestServiceServer server = MockRestServiceServer.createServer(template);
		server.expect(header("X-AUTH-TOKEN", generateToken("test"))).andRespond(withSuccess());
		
		template.getForObject("http://simpliccity.org/test?user=test", SendGreetingResponse.class);
		
		server.verify();
	}

	@Test
	public void testAddSeparateToken()
	{
		MockRestServiceServer server = MockRestServiceServer.createServer(template);
		server.expect(header("X-AUTH-TOKEN", generateToken("jdoe"))).andRespond(withSuccess());
		
		template.getForObject("http://simpliccity.org/test?user=jdoe", SendGreetingResponse.class);
		
		server.verify();
	}
	
	private String generateToken(String name)
	{
		Map<String, String> parms = new HashMap<String, String>();
		parms.put(TestUserParameterServiceSecurityHandler.PARAMETER_USER, name);
		TestConstructorParameterServiceSemanticContext context = new TestConstructorParameterServiceSemanticContext(parms);
		
		Principal principal = handler.getPrincipal(context);
		String result = generator.generateJwtToken(principal);
		
		return result;
	}
}
