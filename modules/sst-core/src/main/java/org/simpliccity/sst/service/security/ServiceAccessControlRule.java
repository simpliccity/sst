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

package org.simpliccity.sst.service.security;

import java.security.Principal;

/**
 * Defines the operation used to enforce a specific access control rule
 * on a service request within the <code>access</code> framework.  Classes 
 * implementing this interface are used to apply the rules specified
 * using the {@link org.simpliccity.sst.service.security.annotation.SecuredMessage}
 * and {@link org.simpliccity.sst.service.security.annotation.SemanticConstraint}
 * annotations.
 * 
 * @author Kevin Fox
 *
 */
public interface ServiceAccessControlRule 
{
	/**
	 * Evaluates the access control rule to determine whether to allow access
	 * to the service endpoint.
	 * 
	 * @param user The authenticated principal associated with the service request.
	 * @param content The selected request content to validate against.
	 * @return <code>true</code> if this rule passes; <code>false</code> if not.
	 */
	boolean allowAccess(Principal user, Object content);
}
