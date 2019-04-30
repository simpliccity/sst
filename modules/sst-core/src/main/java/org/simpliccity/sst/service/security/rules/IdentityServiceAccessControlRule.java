/*
 *    Copyright 2009 Information Control Corporation
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

package org.simpliccity.sst.service.security.rules;

import java.security.Principal;

import org.simpliccity.sst.service.security.ServiceAccessControlRule;
import org.simpliccity.sst.service.security.annotation.ServiceAccessValidator;
import org.simpliccity.styleguide.annotation.CodeStyle;

/**
 * A generic access control rule that checks whether the specified payload content
 * matches the name of the authenticated user associated with the web service call.
 * 
 * @author Kevin Fox
 *
 */
@CodeStyle(categoryId="webservice", topicId="accessvalidator")
// --- StyleGuide webservice.accessvalidator ---
@ServiceAccessValidator("identity")
public class IdentityServiceAccessControlRule implements ServiceAccessControlRule 
{
	// --- StyleGuide webservice.accessvalidator ---
	@Override
	public boolean allowAccess(Principal user, Object content)
	{
		String userName = user.getName();
		
		// Check to see whether specified id matches the current user 
		// (Any user is allowed access if the id is empty or whitespace)
		return (content == null || "".equals(content.toString().trim())) ? true : content.toString().trim().equals(userName);
	}
}
