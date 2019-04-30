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

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.annotation.Cacheable;

public class UserJwtTokenGenerator implements InitializingBean
{
	public static final String CACHE_JWT_TOKEN = "SSTCacheUserName2JWT";
	
	public static final long MILLIS_PER_SECOND = 1000L;
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	private String issuer;
	
	private String signingAlgorithm = IssuerConfig.DEFAULT_SIGNING_ALGORITHM;
	
	private List<JwtClaimsGenerator> claimsGenerators;
	
	private int timeToLive = -1;
	
	private JwtTokenBuilder tokenBuilder;
	
	public String getIssuer() 
	{
		return issuer;
	}

	public void setIssuer(String issuer) 
	{
		this.issuer = issuer;
	}

	public String getSigningAlgorithm() 
	{
		return signingAlgorithm;
	}

	public void setSigningAlgorithm(String signingAlgorithm) 
	{
		this.signingAlgorithm = signingAlgorithm;
	}

	public JwtClaimsGenerator[] getClaimsGenerators() 
	{
		return this.claimsGenerators.toArray(new JwtClaimsGenerator[this.claimsGenerators.size()]);
	}

	public void setClaimsGenerators(JwtClaimsGenerator[] generators) 
	{
		this.claimsGenerators = new ArrayList<>();
		this.claimsGenerators.addAll(Arrays.asList(generators));		
	}

	public int getTimeToLive() 
	{
		return timeToLive;
	}

	public void setTimeToLive(int timeToLive) 
	{
		this.timeToLive = timeToLive;
	}

	public JwtTokenBuilder getTokenBuilder() 
	{
		return tokenBuilder;
	}

	public void setTokenBuilder(JwtTokenBuilder tokenBuilder) 
	{
		this.tokenBuilder = tokenBuilder;
	}

	@Override
	public void afterPropertiesSet() throws Exception 
	{
		if (getClaimsGenerators() == null)
		{
			throw new BeanInitializationException("UserJwtTokenGenerator incorrectly configured: claims generators must be specified.");
		}

		if (getIssuer() == null)
		{
			throw new BeanInitializationException("UserJwtTokenGenerator incorrectly configured: issuer must be specified.");
		}		
	}	

	@Cacheable(cacheNames=CACHE_JWT_TOKEN, key="#root.target.generateKey(#root.target.issuer, #user.name)")
	public String generateJwtToken(Principal user)
	{
		String token = null;
		
		if (user == null)
		{
			logger.warn("Unable to generate a JWT token.  No user was specified.");
		}
		else
		{
			Map<String, Object> claims = generateClaims(user);
			token = tokenBuilder.buildToken(getIssuer(), claims, getSigningAlgorithm(), getTimeToLive());
			logger.debug("Generated new JWT token for user [" + user + "].");
		}
		
		return token;
	}

	public static String generateKey(String issuer, String userName)
	{
		return issuer + "." + userName;
	}
	
	private Map<String, Object> generateClaims(Principal user)
	{
		Map<String, Object> claims = new HashMap<>();
		
		for (JwtClaimsGenerator generator : getClaimsGenerators())
		{
			generator.generateClaims(user, claims);
		}
		
		return claims;
	}
}
