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

package org.simpliccity.sst.security.web.jwt;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.jwt.InvalidJwtTokenException;
import org.simpliccity.sst.jwt.IssuerConfig;
import org.simpliccity.sst.jwt.JwtContent;
import org.simpliccity.sst.jwt.JwtTokenReader;
import org.simpliccity.sst.security.web.token.TokenAuthenticationDetails;
import org.simpliccity.sst.security.web.token.TokenBasedAuthenticationToken;
import org.simpliccity.sst.web.security.token.TokenDescriptor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class JwtTokenAuthenticationProvider implements AuthenticationProvider, InitializingBean
{
	public static final String DEFAULT_AUTHORITY_CLAIM = "roles";
	
	public static final String DEFAULT_ROLE_PREFIX = "ROLE_";
	
	public static final String CACHE_JWT_AUTHENTICATION = "SSTCacheJWT2Authentication";
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	private Map<String, IssuerConfig> knownIssuers;
	
	private String authorityClaim = DEFAULT_AUTHORITY_CLAIM;
	
	private String rolePrefix = DEFAULT_ROLE_PREFIX;
	
	private boolean strict = true;
	
	private JwtTokenReader tokenReader;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException 
	{
		TokenBasedAuthenticationToken result = null;
		
		if (supports(authentication.getClass()))
		{
			JwtContent token = null;
			try
			{
				String tokenString = ((TokenDescriptor) authentication.getCredentials()).getValue();
				token = tokenReader.parseToken(tokenString);
			}
			catch (InvalidJwtTokenException e)
			{
				logger.error("Unable to parse JWT token.", e);
				if (isStrict())
				{
					throw new BadCredentialsException("Unable to parse JWT token for authentication.", e);
				}
			}
			
			if (token != null && isSupportedIssuer(token))
			{
				TokenAuthenticationDetails details = (TokenAuthenticationDetails) authentication.getDetails();
				
				result = new JwtBasedAuthenticationToken(details, token, getAuthorityClaim(), getRolePrefix());
			}
			else if (isStrict())
			{
				throw new BadCredentialsException("Unable to process token for authentication: unsupported issuer.");					
			}
		}
		
		return result;
	}

	@Override
	public boolean supports(Class<?> authentication) 
	{
		return TokenBasedAuthenticationToken.class.isAssignableFrom(authentication);
	}
	
	public Map<String, IssuerConfig> getKnownIssuers() 
	{
		return knownIssuers;
	}

	public void setKnownIssuers(Map<String, IssuerConfig> knownIssuers) 
	{
		this.knownIssuers = knownIssuers;
	}

	public String getAuthorityClaim() 
	{
		return authorityClaim;
	}

	public void setAuthorityClaim(String authorityClaim) 
	{
		this.authorityClaim = authorityClaim;
	}

	public String getRolePrefix() 
	{
		return rolePrefix;
	}

	public void setRolePrefix(String rolePrefix) 
	{
		this.rolePrefix = rolePrefix;
	}

	public boolean isStrict() 
	{
		return strict;
	}

	public void setStrict(boolean strict) 
	{
		this.strict = strict;
	}

	public JwtTokenReader getTokenReader() 
	{
		return tokenReader;
	}

	public void setTokenReader(JwtTokenReader tokenReader) 
	{
		this.tokenReader = tokenReader;
	}

	@Override
	public void afterPropertiesSet() throws Exception 
	{
		// Initialize issuer configurations, if none specified
		if (knownIssuers == null)
		{
			setKnownIssuers(new HashMap<String, IssuerConfig>());
		}		
	}
	
	private boolean isSupportedIssuer(JwtContent token)
	{
		boolean result;
		
		if (getKnownIssuers().size() == 0)
		{
			// If no issuers have been configured, all are accepted 
			result = true;
		}
		else
		{
			String issuer = token.getIssuer();
			
			// Look for fully specified issuer configuration
			IssuerConfig config = getKnownIssuers().get(issuer);
		
			// If an issuer is found, validate that token matches the issuer configuration; otherwise, false
			result = (config == null) ? false : config.supports(token.getSigningAlgorithm());
		}
		
		return result;
	}
}


