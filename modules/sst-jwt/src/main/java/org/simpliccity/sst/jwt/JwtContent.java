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

package org.simpliccity.sst.jwt;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtContent implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static final int MILLIS_PER_SECOND = 1000;
	
	private String signingAlgorithm;
	
	private Map<String, Serializable> claims;
	
	public JwtContent(String signingAlgorithm, Map<String, Serializable> claims)
	{
		this.signingAlgorithm = signingAlgorithm;
		
		this.claims = new HashMap<>();
		this.claims.putAll(claims);
	}

	public String getSigningAlgorithm() 
	{
		return signingAlgorithm;
	}
	
	public Map<String, Serializable> getClaims()
	{
		return Collections.unmodifiableMap(claims);
	}
	
	public Object getClaim(String name)
	{
		return claims.get(name);
	}
	
	public String getIssuer()
	{
		return (String) getClaim(JwtReservedClaimNames.ISSUER);
	}
	
	public String getSubject()
	{
		return (String) getClaim(JwtReservedClaimNames.SUBJECT);
	}
	
	public Date getIssuedAt()
	{
		return makeDate(getClaim(JwtReservedClaimNames.ISSUED_AT));
	}
	
	public Date getExpiration()
	{
		return makeDate(getClaim(JwtReservedClaimNames.EXPIRATION));
	}
	
	public int getTimeToLive()
	{
		long issuedTime = (getIssuedAt() == null) ? 0L : getIssuedAt().getTime();
		long expirationTime = (getExpiration() == null) ? 0L : getExpiration().getTime();
		
		return (getExpiration() == null) ? -1 : (int) ((expirationTime - issuedTime) / MILLIS_PER_SECOND);
	}
	
	@Override
	public String toString()
	{
		StringBuilder string = new StringBuilder("[");
		
		string.append("issuer:");
		string.append(getIssuer());
		string.append(", subject:");
		string.append(getSubject());
		string.append(", algorithm:");
		string.append(getSigningAlgorithm());
		string.append(", claims:");
		string.append(getClaims());
		string.append("]");
		
		return string.toString();
	}
	
	private Date makeDate(Object value)
	{
		Date result = null;
		
		if (value instanceof Date)
		{
			result = (Date) value;
		}
		else if (value instanceof Number)
		{
			long time = ((Number) value).longValue() * MILLIS_PER_SECOND;
			result = new Date(time);
		}
		
		return result;
	}
}
