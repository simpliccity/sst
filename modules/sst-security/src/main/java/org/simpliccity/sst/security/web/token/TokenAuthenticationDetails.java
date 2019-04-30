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

package org.simpliccity.sst.security.web.token;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.simpliccity.sst.web.security.token.TokenDescriptor;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class TokenAuthenticationDetails extends WebAuthenticationDetails 
{
	private static final long serialVersionUID = 1L;

	private TokenDescriptor token;
	
	private String target;
	
	private Map<String, Serializable> attributes;
	
	public TokenAuthenticationDetails(HttpServletRequest request, TokenDescriptor token, Map<String, Serializable> attributes)
	{
		super(request);
		
		this.token = token;
		
		this.target = request.getRequestURI();
		
		this.attributes = (attributes ==  null) ? new HashMap<String, Serializable>() : Collections.unmodifiableMap(attributes);
	}

	public TokenDescriptor getToken() 
	{
		return token;
	}

	public void setToken(TokenDescriptor token) 
	{
		this.token = token;
	}

	public String getTarget() 
	{
		return target;
	}

	public void setTarget(String target) 
	{
		this.target = target;
	}

	public Map<String, Serializable> getAttributes() 
	{
		return attributes;
	}

	public void setAttributes(Map<String, Serializable> attributes) 
	{
		this.attributes = attributes;
	}
}
