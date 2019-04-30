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

import java.util.Map;

public class JwtTokenRequest 
{
	private String issuer;
	
	private String subject;
	
	private int timeToLive;
	
	private String algorithm;
	
	private Map<String, Object> claims;

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) 
	{
		this.issuer = issuer;
	}

	public String getSubject() 
	{
		return subject;
	}

	public void setSubject(String subject) 
	{
		this.subject = subject;
	}

	public int getTimeToLive() 
	{
		return timeToLive;
	}

	public void setTimeToLive(int timeToLive) 
	{
		this.timeToLive = timeToLive;
	}

	public String getAlgorithm() 
	{
		return algorithm;
	}

	public void setAlgorithm(String algorithm) 
	{
		this.algorithm = algorithm;
	}

	public Map<String, Object> getClaims() 
	{
		return claims;
	}

	public void setClaims(Map<String, Object> claims) 
	{
		this.claims = claims;
	}
	
	@Override
	public String toString()
	{
		StringBuilder string = new StringBuilder("[");
		
		string.append("issuer:");
		string.append(this.issuer);
		string.append(", subject:");
		string.append(this.subject);
		string.append(", algorithm:");
		string.append(this.algorithm);
		string.append(", ttl:");
		string.append(this.timeToLive);
		string.append(", claims:");
		string.append(this.claims);
		string.append("]");
		
		return string.toString();
	}
}
