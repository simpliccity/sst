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

package org.simpliccity.sst.data.rest.security;

import java.security.Principal;

import org.simpliccity.sst.service.security.ServiceAccessControlRule;
import org.simpliccity.sst.service.security.annotation.ServiceAccessValidator;

@ServiceAccessValidator("prefix")
public class UserPrefixAccessControlRule implements ServiceAccessControlRule 
{
	@Override
	public boolean allowAccess(Principal user, Object content) 
	{
		boolean result = false;
		
		String value = (String) content;
		int separatorPos = value.indexOf(".");
		
		if (separatorPos > 0)
		{
			String prefix = value.substring(0, separatorPos);
			result = user.getName().equals(prefix);
		}
		
		return result;
	}
}
