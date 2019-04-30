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

package org.simpliccity.sst.web.security.token;

import java.text.MessageFormat;
import java.text.ParseException;

import org.springframework.http.HttpHeaders;

public class AuthorizationBearerHeaderTokenHandler extends AbstractHeaderTokenHandler 
{
	private static final String PATTERN_SCHEMA_BEARER = "Bearer {0}";
	
	@Override
	public String getHeaderName() 
	{
		return HttpHeaders.AUTHORIZATION;
	}
	
	@Override
	protected String prepareHeader(String token) 
	{
		String value = null;
		
		if (token != null)
		{
			value = MessageFormat.format(PATTERN_SCHEMA_BEARER, token);
		}
		
		return value;
	}
	
	@Override
	protected String extractFromHeader(String value) 
	{
		String token = value.trim();
		
		Object[] parameters;
		try 
		{
			parameters = new MessageFormat(PATTERN_SCHEMA_BEARER).parse(token);
			
			if (parameters.length > 0)
			{
				token = ((String) parameters[0]).trim();
			}
		} 
		catch (ParseException e) 
		{
			token = null;
			getLogger().debug("Header value does not match expected format.", e);
		}
				
		return token;
	}
}
