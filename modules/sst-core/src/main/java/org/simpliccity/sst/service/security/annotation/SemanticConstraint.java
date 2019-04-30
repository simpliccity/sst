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

package org.simpliccity.sst.service.security.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>Annotation used to specify an individual access control check to be applied to a Spring-WS endpoint.
 * These checks are enforced by the .</p>
 * 
 * <p>The element values of this annotation are used to configure the specifics of the access control check
 * as applied by .</p>
 * 
 * @author Kevin Fox
 *
 */
@Documented
@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface SemanticConstraint 
{
	/**
	 * <p>Specifies the name of the access control rule to apply.  This name must correspond to a rule defined through
	 * Spring annotation scanning using the {@link ServiceAccessValidator} annotation.</p>
	 * 
	 * @return The name of the access control rule to apply.
	 */
	String semanticRule() default "identity";
	
	String contentExtractor();
	
	/**
	 * <p>Specifies an XPath expression which defines a portion of the web service request message payload to include
	 * in the access control check.  
	 * evaluates the expression against the intercepted request message and passes the value in a call to
	 * {@link org.simpliccity.sst.service.security.ServiceAccessControlRule#allowAccess(java.security.Principal, String...)}.</p>
	 * 
	 * @return An XPath expression to a node in the request message payload.
	 */
	String contentSpec();
	
	ContentAlias[] aliases() default {};
	
	String[] applicableRoles() default {};
}
