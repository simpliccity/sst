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
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

public abstract class AbstractEncodedMapKeyProvider implements KeyProvider 
{
	private Map<String, String> keyMap;
	
	@Override
	public Key getKey(String alias) throws KeyProviderException
	{
		String value = getKeyMap().get(alias);
		
		if (value == null)
		{
			throw new KeyProviderException("Unknown key alias.");
		}
		
		return new SecretKeySpec(KeyUtils.convertKeyStringToBytes(value), getJcaAlgorithm());
	}
	
	protected final Map<String, String> getKeyMap() 
	{
		return keyMap;
	}

	protected final void setKeyMap(Map<String, String> keyMap) 
	{
		this.keyMap = keyMap;
	}

	protected abstract String getJcaAlgorithm();
}
