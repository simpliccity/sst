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

package org.simpliccity.sst.web.security.jwt.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.config.IfEnabled;
import org.simpliccity.sst.jwt.ClaimsUtils;
import org.simpliccity.sst.jwt.InvalidJwtTokenException;
import org.simpliccity.sst.jwt.JwtContent;
import org.simpliccity.sst.jwt.JwtReservedClaimNames;
import org.simpliccity.sst.jwt.JwtTokenBuilder;
import org.simpliccity.sst.jwt.JwtTokenReader;
import org.simpliccity.sst.web.security.jwt.service.annotation.EnableSstJwtTokenGenerationService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@IfEnabled(EnableSstJwtTokenGenerationService.class)
public class JwtTokenRequestController 
{
	private Log logger = LogFactory.getLog(this.getClass());
	
	private JwtTokenBuilder tokenBuilder;
	
	private JwtTokenReader tokenReader;
	
	public JwtTokenBuilder getTokenBuilder() 
	{
		return tokenBuilder;
	}

	public void setTokenBuilder(JwtTokenBuilder tokenBuilder) 
	{
		this.tokenBuilder = tokenBuilder;
	}

	public JwtTokenReader getTokenReader() 
	{
		return tokenReader;
	}

	public void setTokenReader(JwtTokenReader tokenReader) 
	{
		this.tokenReader = tokenReader;
	}

	@RequestMapping(path="/generateJwtToken", method=RequestMethod.POST)
	public String requestToken(@RequestBody JwtTokenRequest request)
	{
		logger.debug("Processing JWT token generation request: " + request + ".");
		
		Map<String, Object> claims = buildClaims(request);
		
		return getTokenBuilder().buildToken(request.getIssuer(), claims, request.getAlgorithm(), request.getTimeToLive());
	}
	
	@RequestMapping(path="/parseJwtToken", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public JwtTokenRequest readToken(@RequestBody String token) throws InvalidJwtTokenException
	{
		logger.debug("Processing JWT token parse request (" + token + ").");
		
		JwtContent content = getTokenReader().parseToken(token);
		
		JwtTokenRequest request = new JwtTokenRequest();
		request.setAlgorithm(content.getSigningAlgorithm());
		request.setIssuer(content.getIssuer());
		request.setSubject(content.getSubject());
		request.setClaims(ClaimsUtils.normalizeClaims(content.getClaims()));
		request.setTimeToLive(content.getTimeToLive());
		
		return request;
	}
	
	private Map<String, Object> buildClaims(JwtTokenRequest request)
	{
		Map<String, Object> claims = new HashMap<>();
		
		// Initialize with the request claims, if any
		if (request.getClaims() != null)
		{
			claims.putAll(request.getClaims());
		}
		
		// Add the request subject as a claim
		claims.put(JwtReservedClaimNames.SUBJECT, request.getSubject());
		
		return claims;
	}
}
