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

package org.simpliccity.sst.jwt;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.jwt.key.KeyManager;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;

public abstract class AbstractKeyManagerJwtTokenReader implements JwtTokenReader, InitializingBean
{
	private Log logger = LogFactory.getLog(this.getClass());
	
	private KeyManager keyManager;
	
	private Map<String, IssuerConfig> knownIssuers;
	
	protected Log getLogger() 
	{
		return logger;
	}

	public KeyManager getKeyManager() 
	{
		return keyManager;
	}

	public void setKeyManager(KeyManager keyManager) 
	{
		this.keyManager = keyManager;
	}

	public Map<String, IssuerConfig> getKnownIssuers() 
	{
		return Collections.unmodifiableMap(knownIssuers);
	}

	public void setKnownIssuers(Map<String, IssuerConfig> knownIssuers) 
	{
		this.knownIssuers = knownIssuers;
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (getKeyManager() == null)
		{
			throw new BeanInitializationException(getClass().getName() + "incorrectly configured: key manager must be specified.");
		}
		
		if (knownIssuers == null)
		{
			setKnownIssuers(new HashMap<String, IssuerConfig>());
		}
		
		initialize();
	}

	@Override
	public JwtContent parseToken(String token) throws InvalidJwtTokenException
	{
		JwtContent result = null;
		try 
		{
			result = parseAndValidateToken(token);
			getLogger().debug("Successfully parsed JWT token ( " + result + ").");
		}
		catch (Exception e)
		{
			getLogger().error("Unable to parse JWT token.", e);
			throw new InvalidJwtTokenException(e);
		}
		
		return result;
	}
	
	protected abstract JwtContent parseAndValidateToken(String token) throws Exception;
	
	protected void initialize()
	{
		// Default behavior, can be overridden by sublass
	}
}
