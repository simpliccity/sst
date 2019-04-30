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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.web.security.token.TokenDescriptor;
import org.simpliccity.sst.web.security.token.TokenSource;
import org.springframework.http.HttpRequest;

public abstract class AbstractHeaderTokenHandler implements HttpRequestTokenHandler 
{
	private Log logger = LogFactory.getLog(this.getClass());
	
	protected Log getLogger() 
	{
		return logger;
	}

	@Override
	public HttpRequest addToken(HttpRequest request, String token) 
	{
		
		// Allows subclasses to apply specific logic to prepare the header value from the token
		String value = prepareHeader(token);
		
		if (value == null)
		{
			getLogger().warn("Unable to set HTTP header [" + getHeaderName() + ".  Invalid value specified [" + token + "].");
		}
		else
		{
			request.getHeaders().add(getHeaderName(), value);
			getLogger().debug("Set HTTP header: name [" + getHeaderName() + "], token [" + value + "].");
		}
		
		return request;
	}

	@Override
	public TokenDescriptor extractToken(HttpRequest request) 
	{
		String value = request.getHeaders().getFirst(getHeaderName());
		
		TokenDescriptor token = null;
		if (value == null)
		{
			getLogger().warn("Unable to retrieve HTTP header value [" + getHeaderName() + "].");
		}
		else
		{
			// Allows subclasses to apply specific logic to extract the token from the header value
			token = new TokenDescriptor(TokenSource.HEADER, getHeaderName(), extractFromHeader(value));
			getLogger().debug("Retrieved token from HTTP header: name [" + getHeaderName() + "], token [" + token + "].");
		}
		
		return token;
	}

	public abstract String getHeaderName();
	
	protected String prepareHeader(String token)
	{
		// Default processing - returns token unchanged
		return token;
	}
	
	protected String extractFromHeader(String value)
	{
		// Default processing - returns token unchanged
		return value;
	}
}
