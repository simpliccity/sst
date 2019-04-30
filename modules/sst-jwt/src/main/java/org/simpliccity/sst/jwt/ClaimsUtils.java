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
import java.util.HashMap;
import java.util.Map;

public final class ClaimsUtils 
{
	private ClaimsUtils()
	{
		super();
	}
	
	public static Map<String, Serializable> cleanseClaims(Map<String, Object> claims)
	{
		Map<String, Serializable> claimMap = new HashMap<>();
		
		for (Map.Entry<String, Object> entry : claims.entrySet())
		{
			if (entry.getValue() instanceof Serializable)
			{
				claimMap.put(entry.getKey(), (Serializable) entry.getValue());
			}
		}
		
		return claimMap;
	}
	
	public static Map<String, Object> normalizeClaims(Map<String, Serializable> claims)
	{
		return new HashMap<String, Object>(claims);
	}
}
