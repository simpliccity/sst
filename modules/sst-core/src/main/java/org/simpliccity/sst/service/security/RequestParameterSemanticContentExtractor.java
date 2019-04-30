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

import static org.simpliccity.sst.service.security.ExtractorTypes.PARAMETER;

import org.simpliccity.sst.service.security.annotation.SemanticConstraint;
import org.simpliccity.sst.service.security.annotation.ServiceContentExtractor;

/**
 * A {@link SemanticContentExtractor} that evaluates the content specification of
 * a {@link SemanticConstraint} against the parameters of a service request.
 * 
 * @author Kevin Fox
 * @since 1.0.0
 *
 */
@ServiceContentExtractor(PARAMETER)
public class RequestParameterSemanticContentExtractor implements SemanticContentExtractor 
{
	@Override
	public Object[] extractContent(ServiceSemanticContext<?> semanticContext, SemanticConstraint constraint) 
	{
		// Get name of desired parameter from annotation
		String name = constraint.contentSpec();
		
		return semanticContext.getParameter(name);
	}
}
