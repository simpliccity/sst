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

import static org.simpliccity.sst.ws.endpoint.security.SoapExtractorTypes.PAYLOAD_XML;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.service.security.annotation.ContentAlias;
import org.simpliccity.sst.service.security.annotation.SecuredMessage;
import org.simpliccity.sst.service.security.annotation.SemanticConstraint;
import org.simpliccity.sst.service.security.annotation.SemanticConstraintsPassOption;
import org.simpliccity.sst.ws.endpoint.security.schema.SendGreetingRequest;
import org.simpliccity.sst.ws.endpoint.security.schema.SendGreetingResponse;
import org.simpliccity.sst.ws.endpoint.security.schema.SendMultipleGreetingsRequest;
import org.simpliccity.sst.ws.endpoint.security.schema.SendMultipleGreetingsResponse;
import org.simpliccity.sst.ws.endpoint.security.schema.ShoutOutRequest;
import org.simpliccity.sst.ws.endpoint.security.schema.ShoutOutResponse;
import org.simpliccity.sst.ws.endpoint.security.schema.WaveRequest;
import org.simpliccity.sst.ws.endpoint.security.schema.WaveResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class SendGreetingEndpoint 
{
	private Log logger = LogFactory.getLog(getClass());

	// --- UserGuide payloadControl ---
	@PayloadRoot(localPart="SendGreetingRequest", namespace="http://www.simpliccity.org/schema/sst/test/access")
	// -ughighlight-
	@SecuredMessage
	// -ughighlight-
	(
		restrictions = 
		{
			@SemanticConstraint(semanticRule = "identity", contentExtractor = PAYLOAD_XML, contentSpec = "//test:SendGreetingRequest/test:from", aliases = {@ContentAlias(name="test", pattern="http://www.simpliccity.org/schema/sst/test/access")}),
			@SemanticConstraint(semanticRule = "positiveGreeting", contentExtractor = PAYLOAD_XML, contentSpec = "//test:SendGreetingRequest/test:message", aliases = {@ContentAlias(name="test", pattern="http://www.simpliccity.org/schema/sst/test/access")}),
			@SemanticConstraint(semanticRule = "junk", contentExtractor = PAYLOAD_XML, contentSpec = "//test:SendMultipleGreetingsRequest/test:message", aliases = {@ContentAlias(name="test", pattern="http://www.simpliccity.org/schema/sst/test/access")})
		},
		pass = SemanticConstraintsPassOption.ANY,
		roleExclusions = "ROLE_ADMIN"
	)
	@ResponsePayload
	public SendGreetingResponse sendGreeting(@RequestPayload SendGreetingRequest request)
	{
		// --- UserGuide payloadControl ---
		String greeting = buildGreeting(request.getFrom(), request.getTo(), request.getMessage());
				
		SendGreetingResponse response = new SendGreetingResponse();
		response.setGreeting(greeting);
		
		logger.debug("Sending greeting [" + greeting + "].");
		
		return response;
	}
	
	@PayloadRoot(localPart="SendMultipleGreetingsRequest", namespace="http://www.simpliccity.org/schema/sst/test/access")
	@SecuredMessage
	(
		restrictions = 
		{
			@SemanticConstraint(semanticRule = "identity", contentExtractor = PAYLOAD_XML,	contentSpec = "//test:SendMultipleGreetingsRequest/test:from", aliases = {@ContentAlias(name="test", pattern="http://www.simpliccity.org/schema/sst/test/access")}),
			@SemanticConstraint(semanticRule = "positiveGreeting", contentExtractor = PAYLOAD_XML,	contentSpec = "//test:SendMultipleGreetingsRequest/test:message", aliases = {@ContentAlias(name="test", pattern="http://www.simpliccity.org/schema/sst/test/access")})
		},
		pass = SemanticConstraintsPassOption.ALL,
		roleExclusions = "ROLE_ADMIN"
	)
	@ResponsePayload
	public SendMultipleGreetingsResponse sendMultipleGreetings(@RequestPayload SendMultipleGreetingsRequest request)
	{
		List<String> greetings = new ArrayList<String>();
		for (String to : request.getTo())
		{
			greetings.add(buildGreeting(request.getFrom(), to, request.getMessage()));
		}
		
		SendMultipleGreetingsResponse response = new SendMultipleGreetingsResponse();
		response.setGreeting(greetings);
		
		logger.debug("Sending greeting [" + greetings + "].");
		
		return response;
		
	}
		
	@PayloadRoot(localPart="WaveRequest", namespace="http://www.simpliccity.org/schema/sst/test/access")
	@SecuredMessage
	(
		// --- UserGuide restrictions ---
		restrictions = 
		{
			@SemanticConstraint(semanticRule = "identity", contentExtractor = PAYLOAD_XML,	contentSpec = "//test:WaveRequest/test:from", aliases = {@ContentAlias(name="test", pattern="http://www.simpliccity.org/schema/sst/test/access")}),
			@SemanticConstraint(semanticRule = "succeeds", contentExtractor = PAYLOAD_XML,	contentSpec = "//test:WaveRequest/test:name", aliases = {@ContentAlias(name="test", pattern="http://www.simpliccity.org/schema/sst/test/access")}),
			@SemanticConstraint(semanticRule = "even", contentExtractor = PAYLOAD_XML,	contentSpec = "//test:WaveRequest/test:name", aliases = {@ContentAlias(name="test", pattern="http://www.simpliccity.org/schema/sst/test/access")})
		},
		// --- UserGuide restrictions ---
		// --- UserGuide pass ---
		pass = SemanticConstraintsPassOption.MAJORITY,
		// --- UserGuide pass ---
		// --- UserGuide exclusions ---
		roleExclusions = "ROLE_ADMIN"
		// --- UserGuide exclusions ---
	)
	@ResponsePayload
	public WaveResponse wave(@RequestPayload WaveRequest request)
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append("Hi, ");
		
		String name;
		List<String> names = request.getName();
		if (names == null || names.size() == 0)
		{
			name = "Nobody";
		}
		else if (names.size() == 1)
		{
			name = names.get(0);
		}
		else
		{
			name = "Everyone";
		}
		builder.append(name);
		
		String greeting = builder.toString();
		
		WaveResponse response = new WaveResponse();
		response.setGreeting(greeting);
		
		logger.debug("Wave greeting [" + greeting + "].");
		
		return response;
	}
	
	@PayloadRoot(localPart="ShoutOutRequest", namespace="http://www.simpliccity.org/schema/sst/test/access")
	@ResponsePayload
	public ShoutOutResponse shoutOut(@RequestPayload ShoutOutRequest request)
	{
		ShoutOutResponse response = new ShoutOutResponse();
		response.setGreeting("Hey, " + request.getName());
		
		return response;
	}
	
	private String buildGreeting(String from, String to, String message)
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append("Hello ");
		builder.append(to);
		builder.append(",\n");
		
		builder.append(message);
		builder.append("\n\n");
		
		builder.append("Sincerely,\n");
		builder.append(from);
		
		String greeting = builder.toString();

		return greeting.toString();
	}
}
