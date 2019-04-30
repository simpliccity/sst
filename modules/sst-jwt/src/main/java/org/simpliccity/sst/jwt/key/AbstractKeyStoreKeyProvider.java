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
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

public abstract class AbstractKeyStoreKeyProvider implements KeyProvider 
{
	private KeyStore keyStore;
	
	@Override
	public Key getKey(String alias) throws KeyProviderException 
	{
		Key key = null;
		
		try 
		{
			key = getKeyStore().getKey(alias, getPassword());
		} 
		catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) 
		{
			throw new KeyProviderException("Unable to retrieve key from key store using alias [" + alias + "].", e);
		}
		
		return key;
	}

	protected KeyStore getKeyStore() 
	{
		return keyStore;
	}

	protected void setKeyStore(KeyStore keyStore) 
	{
		this.keyStore = keyStore;
	}
	
	protected abstract char[] getPassword();
}
