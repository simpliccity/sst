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

package org.simpliccity.sst.ws.endpoint.security;

import static org.simpliccity.sst.ws.endpoint.security.SoapExtractorTypes.PAYLOAD_XML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Source;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.service.security.SemanticContentExtractor;
import org.simpliccity.sst.service.security.ServiceSemanticContext;
import org.simpliccity.sst.service.security.annotation.ContentAlias;
import org.simpliccity.sst.service.security.annotation.SemanticConstraint;
import org.simpliccity.sst.service.security.annotation.ServiceContentExtractor;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.w3c.dom.Node;

@ServiceContentExtractor(PAYLOAD_XML)
public class XpathSemanticContentExtractor implements SemanticContentExtractor 
{
	private Log logger = LogFactory.getLog(XpathSemanticContentExtractor.class);
	
	@Override
	public String[] extractContent(ServiceSemanticContext<?> semanticContext, SemanticConstraint constraint) 
	{
		List<String> results = new ArrayList<>();
		
		// Evaluate XPath expression
		Jaxp13XPathTemplate xpathTemplate = new Jaxp13XPathTemplate();
		xpathTemplate.setNamespaces(getNamespaces(constraint));
		List<Node> nodes = xpathTemplate.evaluateAsNodeList(constraint.contentSpec(), (Source) semanticContext.getPayload());
		
		// Aggregate results into an array
		for (Node node : nodes)
		{
			String value = (node.getTextContent() == null) ? "" : node.getTextContent().trim();
			results.add(value);
			
			logger.debug("Annotation path expression evaluates to [" + value + "]");
		}
		
		return results.toArray(new String[results.size()]);
	}
	
	private Map<String, String> getNamespaces(SemanticConstraint constraint)
	{
		Map<String, String> namespaces = new HashMap<>();
    	for (ContentAlias alias : constraint.aliases())
    	{
    		namespaces.put(alias.name(), alias.pattern());
    	}
    	logger.debug("Namespaces for evaluating annotation path: " + namespaces);
    	
    	return namespaces;
	}
}
