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

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.service.security.ServiceSemanticContext;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class ClientHttpRequestServiceSemanticContext implements	ServiceSemanticContext<ClientHttpRequest> 
{
	private Log logger = LogFactory.getLog(ClientHttpRequestServiceSemanticContext.class);
	
	private ClientHttpRequest clientRequest;
	
	private MultiValueMap<String, String> parameters;

	public ClientHttpRequestServiceSemanticContext(ClientHttpRequest request)
	{
		this.clientRequest = request;
		
		// Initialize query parameters
		UriComponents components = UriComponentsBuilder.fromHttpRequest(request).build();
		parameters = components.getQueryParams();
	}
	
	@Override
	public ClientHttpRequest getContext() 
	{
		return clientRequest;
	}

	@Override
	public Object getRequest() 
	{
		return clientRequest;
	}

	@Override
	public Object getPayload() 
	{
		Object payload = null;
		
		try 
		{
			payload = clientRequest.getBody();
		} 
		catch (IOException e) 
		{
			logger.error("Unable to retrieve client HTTP clientRequest output stream.", e);
		}
		
		return payload;
	}

	@Override
	public Object[] getParameter(String name) 
	{
		return parameters.get(name).toArray();
	}

	@Override
	public String getAddress() 
	{
		return clientRequest.getURI().toString();
	}
}
