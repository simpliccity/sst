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

import static org.simpliccity.sst.service.security.ExtractorTypes.PAYLOAD_SPEL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.service.security.annotation.SemanticConstraint;
import org.simpliccity.sst.service.security.annotation.ServiceContentExtractor;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * An implementation of {@link SemanticContentExtractor} that extracts content from the 
 * the payload of the {@link SemanticContext} by evaluating a Spring Expression Language 
 * (SpEL) expression against it.
 * 
 * @author Kevin Fox
 * @since 1.0.0
 *
 */
@ServiceContentExtractor(PAYLOAD_SPEL)
public class SpelSemanticContentExtractor implements SemanticContentExtractor 
{
	private Log logger = LogFactory.getLog(SpelSemanticContentExtractor.class);
	
	@Override
	public Object[] extractContent(ServiceSemanticContext<?> semanticContext, SemanticConstraint constraint) 
	{
		Object[] result = null;
		
		Object payload = semanticContext.getPayload();

		// Don't try to process an empty payload
		if (payload == null)
		{
			logger.warn("The specified semantic context does not have a payload.");
		}
		else
		{
			try
			{
				ExpressionParser parser = new SpelExpressionParser();
				Expression expression = parser.parseExpression(constraint.contentSpec());
				Object content = expression.getValue(payload);
				result = new Object[] {content};
			}
			catch (EvaluationException | ParseException e)
			{
				logger.error("Unable to process SpEL expression against context payload.", e);
			}
		}
		
		return result;
	}
}
