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

package org.simpliccity.sst.security.web.jwt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.simpliccity.sst.jwt.JwtContent;
import org.simpliccity.sst.jwt.JwtTokenPrincipal;
import org.simpliccity.sst.security.web.token.TokenAuthenticationDetails;
import org.simpliccity.sst.security.web.token.TokenBasedAuthenticationToken;
import org.simpliccity.sst.web.security.token.TokenDescriptor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtBasedAuthenticationToken extends TokenBasedAuthenticationToken implements JwtTokenPrincipal
{
	private static final long serialVersionUID = 1L;

	public static final String ATTRIBUTE_SIGNING_ALGORITHM = "signingAlgorithm";
	
	private JwtContent jwtContent;
	
	public JwtBasedAuthenticationToken(TokenAuthenticationDetails details)
	{
		super(details);
	}
	
	public JwtBasedAuthenticationToken(TokenAuthenticationDetails details, JwtContent content, String authorityClaim, String rolePrefix)
	{
		super(prepareAttributes(details, content), getAuthorities(content, authorityClaim, rolePrefix));
	}

	public JwtContent getJwtContent() 
	{
		return jwtContent;
	}

	public void setJwtContent(JwtContent jwtContent) 
	{
		this.jwtContent = jwtContent;
	}

	@Override
	public String getJwtToken() 
	{
		return ((TokenDescriptor) getCredentials()).getValue();
	}

	@Override
	public boolean isExpired()
	{
		boolean result = false;
		
		Date expiration = getJwtContent().getExpiration();
		
		if (expiration != null)
		{
			Date now = new Date();
			result = now.after(expiration);
		}
		
		return result;
	}
	
	private static TokenAuthenticationDetails prepareAttributes(TokenAuthenticationDetails details, JwtContent content)
	{
		Map<String, Serializable> attributes = new HashMap<>();
		
		// Copy claims from parsed JWT token
		attributes.putAll(content.getClaims());
		
		// Ensure that standard attributes are included
		attributes.put(TokenBasedAuthenticationToken.ATTRIBUTE_ISSUER, content.getIssuer());
		attributes.put(TokenBasedAuthenticationToken.ATTRIBUTE_PRINCIPAL, content.getSubject());
		
		// Include additional JWT information
		attributes.put(ATTRIBUTE_SIGNING_ALGORITHM, content.getSigningAlgorithm());
		
		details.setAttributes(attributes);
		
		return details;
	}
		
	@SuppressWarnings("unchecked")
	private static Collection<? extends GrantedAuthority> getAuthorities(JwtContent content, String authorityClaim, String rolePrefix)
	{
		Map<String, Serializable> claims = content.getClaims();
		
		List<String> roles = (List<String>) claims.get(authorityClaim);
		
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		if (roles != null)
		{
			for (String role : roles)
			{
				GrantedAuthority newAuthority = new SimpleGrantedAuthority(generateAuthorityName(rolePrefix, role));
				authorities.add(newAuthority);
			}
		}
		
		return authorities;
	}

	private static String generateAuthorityName(String rolePrefix, String role)
	{
		return role.startsWith(rolePrefix, 0) ? role : rolePrefix + role;
	}
}
