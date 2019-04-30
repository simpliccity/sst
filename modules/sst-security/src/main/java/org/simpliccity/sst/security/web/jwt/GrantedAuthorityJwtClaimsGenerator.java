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

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.simpliccity.sst.jwt.JwtClaimsGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class GrantedAuthorityJwtClaimsGenerator implements JwtClaimsGenerator 
{
	private static final String DEFAULT_ROLE_CLAIM = "roles";
	
	private String roleClaim = DEFAULT_ROLE_CLAIM;
	
	public String getRoleClaim() 
	{
		return roleClaim;
	}

	public void setRoleClaim(String roleClaim) 
	{
		this.roleClaim = roleClaim;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> generateClaims(Principal user, Map<String, Object> existingClaims) 
	{
		if (user instanceof Authentication)
		{
			List<String> roles = new ArrayList<>();
			
			Collection<GrantedAuthority> assigned = (Collection<GrantedAuthority>) ((Authentication) user).getAuthorities();
			for (GrantedAuthority authority : assigned)
			{
				roles.add(authority.getAuthority());
			}
			
			existingClaims.put(getRoleClaim(), roles);
		}
		
		return existingClaims;
	}
}
