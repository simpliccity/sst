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

package org.simpliccity.sst.web.security;

import static org.simpliccity.sst.web.security.WebExtractorTypes.URI_VARIABLE;

import java.util.Map;

import org.simpliccity.sst.service.security.SemanticContentExtractor;
import org.simpliccity.sst.service.security.ServiceSemanticContext;
import org.simpliccity.sst.service.security.annotation.SemanticConstraint;
import org.simpliccity.sst.service.security.annotation.ServiceContentExtractor;
import org.springframework.web.util.UriTemplate;

@ServiceContentExtractor(URI_VARIABLE)
public class UriVariableSemanticContentExtractor implements	SemanticContentExtractor 
{
	@Override
	public Object[] extractContent(ServiceSemanticContext<?> semanticContext, SemanticConstraint constraint) 
	{
		// Get URI pattern from annotation
		String pattern = constraint.contentSpec();
		
		// Match the URI of the service request against the pattern and extract any variable(s)
		UriTemplate template = new UriTemplate(pattern);
		Map<String, String> result = template.match(semanticContext.getAddress());
		
		// Return the value of any variable(s) as an array (note that order is not assured)
		return (result.size() == 0) ? null : result.values().toArray(new String[result.size()]);
	}
}
