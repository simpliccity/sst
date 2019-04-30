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

import static org.simpliccity.sst.service.security.ExtractorTypes.PARAMETER;
import static org.simpliccity.sst.web.security.WebExtractorTypes.PAYLOAD_JSON;
import static org.simpliccity.sst.web.security.WebExtractorTypes.URI_VARIABLE;

import org.simpliccity.sst.service.security.annotation.ApplyWhen;
import org.simpliccity.sst.service.security.annotation.SecuredMessage;
import org.simpliccity.sst.service.security.annotation.SemanticConstraint;
import org.simpliccity.sst.service.security.annotation.SemanticConstraintsPassOption;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendGreetingController 
{
	private static final String[] greetings = {"Hello, world", "Hi y'all", "Howdee!"};
	
	@RequestMapping(value="/sendgreeting", method=RequestMethod.POST)
	@SecuredMessage
	(
		restrictions = {@SemanticConstraint(contentExtractor=PAYLOAD_JSON, contentSpec="from")},
		pass = SemanticConstraintsPassOption.ALL
	)
	public SendGreetingResponse sendGreeting(@RequestBody SendGreetingRequest greeting)
	{
		SendGreetingResponse response = new SendGreetingResponse();
		response.setGreeting(greeting.getMessage() + ", " + greeting.getTo());
		
		return response;
	}
	
	@RequestMapping(value="/greeting/{id}", method=RequestMethod.GET)
	@SecuredMessage
	(
		restrictions = {@SemanticConstraint(semanticRule="userNumber", contentExtractor=URI_VARIABLE, contentSpec="/greeting/{id}", applicableRoles="ROLE_USER")},
		pass = SemanticConstraintsPassOption.ALL
	)
	public SendGreetingResponse getGreeting(@PathVariable String id)
	{
		int index = Integer.parseInt(id);
		
		SendGreetingResponse response = new SendGreetingResponse();
		response.setGreeting(greetings[index]);

		return response;
	}
	
	@RequestMapping(value="/getgreeting")
	@SecuredMessage
	(
		restrictions = {@SemanticConstraint(semanticRule="userNumber", contentExtractor=PARAMETER, contentSpec="greetingId")},
		pass = SemanticConstraintsPassOption.ALL
	)
	public SendGreetingResponse getGreetingByParameter(@RequestParam String greetingId)
	{
		int index = Integer.parseInt(greetingId) - 1;
		
		SendGreetingResponse response = new SendGreetingResponse();
		response.setGreeting(greetings[index]);

		return response;		
	}

	@RequestMapping(value="/southerngreeting")
	@SecuredMessage
	(
		restrictions = {@SemanticConstraint(semanticRule="drawl", contentExtractor=PAYLOAD_JSON, contentSpec="greeting")},
		pass = SemanticConstraintsPassOption.ALL,
		apply=ApplyWhen.POST
	)
	public SendGreetingResponse getSouthernGreeting()
	{
		SendGreetingResponse response = new SendGreetingResponse();
		response.setGreeting(greetings[2]);

		return response;		
	}
}
