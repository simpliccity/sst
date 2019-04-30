/*
 *    Copyright 2013 Information Control Corporation
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

@ServiceAccessValidator("positiveGreeting")
public class PositiveGreetingValidator implements ServiceAccessControlRule 
{
	public static final String POSITIVE_MESSAGE = "Success";
	
	@Override
	public boolean allowAccess(Principal user, Object content) 
	{
		// Only care about first content item
		return POSITIVE_MESSAGE.equalsIgnoreCase((String) content) ? true : false;
	}
}
