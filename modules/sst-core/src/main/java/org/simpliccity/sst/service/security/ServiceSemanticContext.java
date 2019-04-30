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

package org.simpliccity.sst.service.security;

/**
 * <p>An interface that defines a generic representation of a service request
 * as a semantic context in which the <code>access</code> framework can
 * evaluate content restrictions.  This allows the framework to be adapted 
 * for use with a variety of different service mechanisms.</p>
 * 
 * <p>The restrictions enforced by the <code>access</code> framework, as
 * represented by {@link org.simpliccity.sst.service.security.annotation.SecuredMessage}
 * and {@link org.simpliccity.sst.service.security.annotation.SemanticConstraint},
 * are expressed in terms of the semantics of the service request.  This
 * interface provides operations that allow the framework to query the
 * semantics of the request without having specific knowledge of the 
 * underlying transport and messaging approach used.  This keeps the
 * framework loosely coupled to the underlying service mechanism and
 * allows it to be extended to apply to new service implementations,
 * over time.</p>
 * 
 * @author Kevin Fox
 * @since 1.0.0
 *
 * @see AbstractSecuredMessageProcessor
 * @see SemanticConstraintEvaluator
 *
 * @param <A> The class in the underlying service framework that represents the 
 * context of a service request.
 */
public interface ServiceSemanticContext<A>
{
	/**
	 * Retrieves the implementation-specific object that represents this context.
	 * 
	 * @return The implementation-specific context object.
	 */
	A getContext();
	
	/**
	 * Retrieves the implementation-specific object that represents the service request.
	 * 
	 * @return The implementation-specific service request object.
	 */
	Object getRequest();
	
	/**
	 * Retrieves the implementation-specific object that represents the payload of the service request.
	 * 
	 * @return The implementation-specific service request payload.
	 */
	Object getPayload();
	
	/**
	 * Retrieves the values of the named parameter associated with the service request, if any.
	 * 
	 * @param name The name of the parameter.
	 * @return The value(s) associated with this parameter on the underlying service request; <code>null</code> if none.
	 */
	Object[] getParameter(String name);
	
	/**
	 * Retrieves the implementation-specific address of the service request.
	 * 
	 * @return The address of the service request.
	 */
	String getAddress();
}
