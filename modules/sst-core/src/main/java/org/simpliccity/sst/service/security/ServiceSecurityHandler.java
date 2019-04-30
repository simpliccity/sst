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

package org.simpliccity.sst.service.security;

import java.security.Principal;

/**
 * An interface that defines an extension point for the <code>access</code> framework,
 * adapting it to work with a specific security implementation.  By relying on a bean 
 * implementing this interface, rather than any direct knowledge of the underlying security 
 * system, the framework remains decoupled from specifics of the runtime environment. 
 * 
 * @author Kevin Fox
 * @since 1.0.0
 * 
 */
public interface ServiceSecurityHandler 
{
	/**
	 * Returns the principal of the authenticated user (if any) associated with the
	 * service request represented by the specified semantic context.
	 * 
	 * @param semanticContext The context of the service request being processed.
	 * @return The principal representing the authenticated user associated with the
	 * service request.
	 */
	Principal getPrincipal(ServiceSemanticContext<?> semanticContext);
	
	/**
	 * Determines whether the authenticated user associated with the service request
	 * (as returned by {@link #getPrincipal(ServiceSemanticContext)}) is in any of
	 * the specified roles.
	 * 
	 * @param semanticContext The context of the service request being processed.
	 * @param roles A list of roles to check against the authenticated user associated
	 * with the service request.
	 * @return <code>true</code> if the user has any of the specified roles; <code>false</code> otherwise.
	 */
	boolean inApplicableRole(ServiceSemanticContext<?> semanticContext, String[] roles);
}
