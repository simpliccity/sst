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

package org.simpliccity.sst.security.web.token;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import org.simpliccity.sst.web.security.token.TokenDescriptor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class TokenBasedAuthenticationToken extends AbstractAuthenticationToken 
{
	private static final long serialVersionUID = 1L;
	
	public static final String ATTRIBUTE_PRINCIPAL = "principal";
	
	public static final String ATTRIBUTE_ISSUER = "issuer";
	
	private TokenDescriptor token;
	
	public TokenBasedAuthenticationToken(TokenAuthenticationDetails details)
	{
		super(null);
		this.token = details.getToken();
		setDetails(details);
		setAuthenticated(false);
	}
	
	public TokenBasedAuthenticationToken(TokenAuthenticationDetails details, Collection<? extends GrantedAuthority> authorities)
	{
		super(authorities);
		this.token = details.getToken();
		setDetails(details);
		setAuthenticated(true);
	}

	@Override
	public Object getCredentials() 
	{
		return token;
	}

	@Override
	public Object getPrincipal() 
	{
		return getAttributes().get(ATTRIBUTE_PRINCIPAL);
	}
	
	public Object getIssuer()
	{
		return getAttributes().get(ATTRIBUTE_ISSUER);		
	}
	
	public Map<String, Serializable> getAttributes()
	{
		return ((TokenAuthenticationDetails) getDetails()).getAttributes();
	}
}
