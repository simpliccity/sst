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

package org.simpliccity.sst.service.security.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>Annotation used to specify an access control check for a service endpoint in the {@code access} framework.
 * The framework is designed to evaluate domain-specific rules that require an understanding of the semantics
 * of the service request message.  This annotation allows these rules to be expressed as semantic constraints
 * on the message and to be specified with the code that defines the service endpoint.</p>
 *   
 * <p>A message is secured by applying one or more restrictions, as specified using the {@code @SemanticConstraint}
 * annotation.The {@link #pass()} attribute controls how multiple restrictions are applied to determine whether or 
 * not access is allowed.</p>
 * 
 * @author Kevin Fox
 * @since 1.0.0
 * 
 * @see org.simpliccity.sst.service.security.AbstractSecuredMessageProcessor
 * @see org.simpliccity.sst.service.security.SecuredMessageEvaluator
 *
 */
@Documented
@Target({TYPE, METHOD})
@Retention(RUNTIME)
public @interface SecuredMessage 
{
	/**
	 * Specifies the set of access control restrictions associated with the Spring-WS endpoint.
	 * 
	 * @return An array of {@link SemanticConstraint} annotations that specify the individual access control
	 * checks for the endpoint.
	 */
	SemanticConstraint[] restrictions() default {};
	
	/**
	 * <p>Specifies user roles that are exempt from the application of the specified access control restrictions.  This provides
	 * a way to bypass the restrictions if the user has sufficient permissions.</p>
	 * 
	 * <p>{@link org.simpliccity.sst.ws.endpoint.security.EndpointAccessValidatorInterceptor} uses its security handler to determine 
	 * the roles associated with the authenticated user.</p>
	 * 
	 * @since 1.0.0
	 * 
	 * @return An array of role names.
	 */
	String[] roleExclusions() default {};
	
	/**
	 * <p>Specifies how the results of the specified restrictions are tallied to determine whether this access control rule passes.
	 * Options are:</p>
	 * <ul>
	 * <li>ALL - Requires that all access control restrictions evaluate to <code>true</code>.</li>
	 * <li>ANY - Requires that at least one access control restriction evaluates to <code>true</code>.</li>
	 * <li>MAJORITY - Requires that the majority (more than half) of access control restrictions evaluate to <code>true</code>.</li>
	 * </ul>
	 * 
	 * @since 1.0.0
	 * 
	 * @return A constant of the {@link SemanticConstraintsPassOption} enum indicating how to tally the results of access control restrictions
	 * to determine whether or not the access control rule passes.
	 */
	SemanticConstraintsPassOption pass() default SemanticConstraintsPassOption.ALL;
	
	ApplyWhen apply() default ApplyWhen.PRE;
}
