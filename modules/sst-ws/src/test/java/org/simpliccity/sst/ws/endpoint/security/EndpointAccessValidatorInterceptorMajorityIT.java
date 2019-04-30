/*
 *    Copyright 2013 Information Control Corporation
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

package org.simpliccity.sst.ws.endpoint.security;

import static org.springframework.ws.test.server.RequestCreators.withSoapEnvelope;
import static org.springframework.ws.test.server.ResponseMatchers.payload;
import static org.springframework.ws.test.server.ResponseMatchers.serverOrReceiverFault;

import javax.xml.transform.Source;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.test.server.MockWebServiceClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/EndpointAccessValidatorInterceptorIT.xml" })
public class EndpointAccessValidatorInterceptorMajorityIT 
{
	
	@Autowired
	private ApplicationContext context;
	
	private MockWebServiceClient client;
		
	@Before
	public void createClient()
	{
		client = MockWebServiceClient.createClient(context);		
	}
	
	/**
	 * Tests the behavior when only the first of three specified access control rules passes.
	 */
	@Test
	public void testPassFirstRuleOnly()
	{
		Source requestPayload = EndpointAccessValidatorInterceptorTestHelper.generateWaveMessage("userA", "password", "userA", new String[] {"James", "John", "Zeke"});
		
		client.sendRequest(withSoapEnvelope(requestPayload)).andExpect(serverOrReceiverFault());
	}
	
	/**
	 * Tests the behavior when only the second of three specified access control rules passes.
	 */
	@Test
	public void testPassSecondRuleOnly()
	{
		Source requestPayload = EndpointAccessValidatorInterceptorTestHelper.generateWaveMessage("userA", "password", "userB", new String[] {"James", "John", "Jerry"});
		
		client.sendRequest(withSoapEnvelope(requestPayload)).andExpect(serverOrReceiverFault());
	}
	
	/**
	 * Tests the behavior when only the third of three specified access control rules passes.
	 */
	@Test
	public void testPassThirdRuleOnly()
	{
		Source requestPayload = EndpointAccessValidatorInterceptorTestHelper.generateWaveMessage("userA", "password", "userB", new String[] {"John", "Zeke"});
		
		client.sendRequest(withSoapEnvelope(requestPayload)).andExpect(serverOrReceiverFault());
	}
	
	/**
	 * Tests the behavior when two of three specified access control rules pass.
	 */
	@Test
	public void testPassTwoRules()
	{
		Source requestPayload = EndpointAccessValidatorInterceptorTestHelper.generateWaveMessage("userA", "password", "userA", new String[] {"John", "Zeke"});
		
		Source responsePayload = EndpointAccessValidatorInterceptorTestHelper.generateWaveResultMessage("Everyone");
		
		client.sendRequest(withSoapEnvelope(requestPayload)).andExpect(payload(responsePayload));
	}
	
	/**
	 * Tests the behavior when all specified access control rules pass.
	 */
	@Test
	public void testPassAllRules()
	{
		Source requestPayload = EndpointAccessValidatorInterceptorTestHelper.generateWaveMessage("userA", "password", "userA", new String[] {"James", "John"});
		
		Source responsePayload = EndpointAccessValidatorInterceptorTestHelper.generateWaveResultMessage("Everyone");
		
		client.sendRequest(withSoapEnvelope(requestPayload)).andExpect(payload(responsePayload));
	}

	/**
	 * Tests the behavior when an access control rule passes via role exclusion.
	 */
	@Test
	public void testRoleExclusion()
	{
		Source requestPayload = EndpointAccessValidatorInterceptorTestHelper.generateWaveMessage("admin", "password", "userB", new String[] {"James", "John", "Zeke"});

		Source responsePayload = EndpointAccessValidatorInterceptorTestHelper.generateWaveResultMessage("Everyone");

		client.sendRequest(withSoapEnvelope(requestPayload)).andExpect(payload(responsePayload));
	}
	
	/**
	 * Tests the behavior when all of multiple access control rules fail.
	 */
	@Test
	public void testFailAllRules()
	{
		Source requestPayload = EndpointAccessValidatorInterceptorTestHelper.generateWaveMessage("userA", "password", "userB", new String[] {"James", "John", "Zeke"});
		
		client.sendRequest(withSoapEnvelope(requestPayload)).andExpect(serverOrReceiverFault());
	}
}
