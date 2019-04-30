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

package org.simpliccity.sst.jwt.key;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.bean.lookup.BeanMetadataLookupManager;
import org.simpliccity.sst.jwt.key.annotation.KeySourceBeanMetadataLookupContainer;
import org.springframework.beans.factory.annotation.Autowired;

public class KeyManager 
{
	private Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private BeanMetadataLookupManager lookupManager;
	
	private Map<String, KeyProvider> providers = new HashMap<>();
	
	public Key getSigningKey(String algorithm, String alias)
	{
		KeyProvider provider = getKeyProvider(algorithm);
		
		Key key = null;
		
		if (provider == null)
		{
			logger.warn("No key provider found for specified algorithm [" + algorithm + "].");
		}
		else
		{
			try
			{
				key = provider.getKey(alias);
				logger.debug("Successfully retrieved key for algorithm [" + algorithm + "], alias [" + alias + "].");
			}
			catch (KeyProviderException e)
			{
				logger.error("Unable to retrieve key for algorithm [" + algorithm + "], alias [" + alias + "].", e);
			}
		}
		
		return key;
	}
	
	private KeyProvider getKeyProvider(String algorithm)
	{
		KeyProvider provider;
		
		if (providers.containsKey(algorithm))
		{
			provider = providers.get(algorithm);
		}
		else
		{
			KeySourceBeanMetadataLookupContainer container = new KeySourceBeanMetadataLookupContainer();
			container.setAlgorithm(algorithm);
			
			provider = (KeyProvider) lookupManager.lookupBeanByMetadata(container);
			
			providers.put(algorithm, provider);
		}
		
		return provider;
	}
}
