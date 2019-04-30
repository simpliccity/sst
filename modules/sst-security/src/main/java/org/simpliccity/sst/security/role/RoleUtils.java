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

package org.simpliccity.sst.security.role;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public final class RoleUtils 
{
	// Private constructor to hide default
	private RoleUtils()
	{
		super();
	}

	public static boolean hasAuthority(Collection<GrantedAuthority> assigned, String role)
	{
		boolean result = false;
		for (GrantedAuthority authority : assigned)
		{
			result = authority.getAuthority().equals(role);
			if (result)
			{
				break;
			}
		}
		
		return result;
	}
}
