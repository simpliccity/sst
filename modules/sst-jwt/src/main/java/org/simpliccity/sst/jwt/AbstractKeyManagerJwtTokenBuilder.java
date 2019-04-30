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

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.jwt.key.KeyManager;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;

public abstract class AbstractKeyManagerJwtTokenBuilder implements JwtTokenBuilder, InitializingBean
{
	public static final long MILLIS_PER_SECOND = 1000L;
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	private KeyManager keyManager;
	
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

	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (getKeyManager() == null)
		{
			throw new BeanInitializationException(getClass().getName() + "incorrectly configured: key manager must be specified.");
		}
				
		initialize();
	}

	@Override
	public String buildToken(String issuer, Map<String, Object> claims, String signingAlgorithm, int timeToLive) 
	{
		Key key = getKeyManager().getSigningKey(signingAlgorithm, issuer);
		Date issued = new Date();
		Date expiration = (timeToLive < 0) ? null : new Date(System.currentTimeMillis() + timeToLive * MILLIS_PER_SECOND);
				
		String token = buildAndSignToken(key, issuer, claims, signingAlgorithm, issued, expiration);
		getLogger().debug("Built new JWT token for issuer [" + issuer + "], claims [" + claims + "], algorithm [" + signingAlgorithm + "], expiration [" + expiration + "].");

		return token;
	}
	
	protected abstract String buildAndSignToken(Key key, String issuer, Map<String, Object> claims, String signingAlgorithm, Date issued, Date expiration);

	protected void initialize()
	{
		// Default behavior, can be overridden by sublass
	}
}
