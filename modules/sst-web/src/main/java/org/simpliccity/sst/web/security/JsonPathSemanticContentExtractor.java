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

import static org.simpliccity.sst.web.security.WebExtractorTypes.PAYLOAD_JSON;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.service.security.SemanticContentExtractor;
import org.simpliccity.sst.service.security.ServiceSemanticContext;
import org.simpliccity.sst.service.security.annotation.SemanticConstraint;
import org.simpliccity.sst.service.security.annotation.ServiceContentExtractor;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.JsonPathException;

@ServiceContentExtractor(PAYLOAD_JSON)
public class JsonPathSemanticContentExtractor implements SemanticContentExtractor 
{
	private Log logger = LogFactory.getLog(JsonPathSemanticContentExtractor.class);
	
	@Override
	public Object[] extractContent(ServiceSemanticContext<?> semanticContext, SemanticConstraint constraint) 
	{
		String[] content = null;
		
		// Get the path specification from the annotation
		String path = constraint.contentSpec();
		
		// Get the payload from the service context
		InputStream payload = (InputStream) semanticContext.getPayload();
		
		// Don't try to process an empty payload
		if (payload == null)
		{
			logger.warn("The specified semantic context does not have a payload.");
		}
		else
		{
			// Create a JSON path evaluator from the path specification
			JsonPath pathEvaluator = JsonPath.compile(path);
			try 
			{
				// If the path evaluates to a specific element,...
				if (pathEvaluator.isDefinite())
				{
					// ... evaluate the path against the payload to get a single result (from which to build an array)
					String result = pathEvaluator.read(payload);
					content = new String[] {result};
				}
				else
				{
					// ... otherwise, evaluate the path against the payload to get a list of results (which must be converted to an array)
					List<String> result = pathEvaluator.read(payload);
					content = result.toArray(new String[result.size()]);
				}
			} 
			catch (IOException e) 
			{
				// If evaluation of the payload fails, log an error and return empty result
				logger.error("Unable to process JSON payload.", e);
			}
			catch (JsonPathException e)
			{
				// If the specified path is not found in the payload, log an error and return empty result
				logger.error("The specified path is not valid for the JSON payload.", e);			
			}
		}
		
		return content;
	}
}
