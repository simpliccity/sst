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

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * <p>Marks a class for use as an access control rule managed by 
 * {@link org.simpliccity.sst.ws.endpoint.security.EndpointAccessValidatorInterceptor}.
 * Classes annotated as an access control rule will be automatically recognized and loaded by Spring's
 * annotation scanning.</p>
 * 
 * <p>The <code>value</code> element of this annotation specifies the name by which the access control rule will be
 * referenced when assigning the rule through the {@link SecuredMessage} and {@link SemanticConstraint}
 * annotations.</p>
 * 
 * @author Kevin Fox
 *
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Component
public @interface ServiceContentExtractor 
{
	/**
	 * Specifies the name by which the access control rule will be referenced when assigning the rule through the 
	 * {@link SecuredMessage} and {@link SemanticConstraint} annotations.
	 * 
	 * @return Name of the access control rule.
	 */
	String value();
}
