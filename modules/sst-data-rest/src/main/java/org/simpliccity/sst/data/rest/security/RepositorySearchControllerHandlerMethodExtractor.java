/*
 *    Copyright 2016 Information Control Corporation
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

package org.simpliccity.sst.data.rest.security;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.simpliccity.sst.bean.lookup.annotation.ComponentMetadata;
import org.simpliccity.sst.bean.lookup.annotation.DynamicTypedComponent;
import org.simpliccity.sst.web.security.HandlerMethodExtractor;
import org.springframework.data.rest.core.mapping.SearchResourceMappings;
import org.springframework.data.rest.webmvc.RootResourceInformation;
import org.springframework.web.util.UriTemplate;

@DynamicTypedComponent
(
	type = HandlerMethodExtractor.class,
	metadata = {@ComponentMetadata(name = "target", value = "org.springframework.data.rest.webmvc.RepositorySearchController")}
)
public class RepositorySearchControllerHandlerMethodExtractor extends AbstractRepositoryControllerHandlerMethodExtractor
{
	private static final String URI_PARAMETER = "search";
	private static final String URI_TEMPLATE = "/{repository}/search/{search}";
	
	@Override
	protected Method getMethodFromResource(RootResourceInformation resourceInformation, HttpServletRequest request) 
	{
		SearchResourceMappings searchMappings = resourceInformation.getSearchMappings();
		return searchMappings.getMappedMethod(getSearchPath(request));
	}
	
	private String getSearchPath(HttpServletRequest request)
	{
		UriTemplate template = new UriTemplate(URI_TEMPLATE);
		Map<String, String> result = template.match(request.getRequestURI());
		
		return result.get(URI_PARAMETER);
	}
}
