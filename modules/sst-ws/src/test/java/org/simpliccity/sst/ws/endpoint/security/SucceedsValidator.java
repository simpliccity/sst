/*
 *    Copyright 2014 Information Control Corporation
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

package org.simpliccity.sst.ws.endpoint.security;

import java.security.Principal;

import org.simpliccity.sst.service.security.ServiceAccessControlRule;
import org.simpliccity.sst.service.security.annotation.ServiceAccessValidator;

@ServiceAccessValidator("succeeds")
public class SucceedsValidator implements ServiceAccessControlRule 
{
	@Override
	public boolean allowAccess(Principal user, Object content) 
	{
		String userName = user.getName();
		
		boolean result = (userName.compareToIgnoreCase((String) content) > 0);
		
		return result;
	}
}
