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

import org.simpliccity.sst.service.security.annotation.SemanticConstraint;

/**
 * An interface that defines an extension point for the <code>access</code> framework, making it
 * possible to customize the ways in which service request content can be read to evaluate the
 * semantic constraints enforced by the framework.
 * 
 * @author Kevin Fox
 * @since 1.0.0
 *
 */
public interface SemanticContentExtractor 
{
	/**
	 * Extracts the specified content from the semantic context of a service request.  The content
	 * to extract is defined by the specification of a semantic constraint to apply to the request.
	 * The extracted content is used in evaluating whether the service request should be allowed
	 * or not.
	 * 
	 * @param semanticContext The context of a service request.
	 * @param constraint The semantic constraint to be applied to the request.
	 * @return The object(s) extracted from the service request, as defined by the constraint specification.
	 */
	Object[] extractContent(ServiceSemanticContext<?> semanticContext, SemanticConstraint constraint);
}
