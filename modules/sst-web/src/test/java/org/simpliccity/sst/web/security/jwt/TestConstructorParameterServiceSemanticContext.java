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

package org.simpliccity.sst.web.security.jwt;

import java.util.Map;

import org.simpliccity.sst.service.security.ServiceSemanticContext;

public class TestConstructorParameterServiceSemanticContext implements ServiceSemanticContext<Object>
{
	private Map<String, String> parameters;
	
	public TestConstructorParameterServiceSemanticContext(Map<String, String> parms)
	{
		parameters = parms;
	}
	
	@Override
	public Object getContext() 
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getRequest() 
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getPayload() 
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] getParameter(String name) 
	{
		return new Object[] {parameters.get(name)};
	}

	@Override
	public String getAddress() 
	{
		throw new UnsupportedOperationException();
	}
}
