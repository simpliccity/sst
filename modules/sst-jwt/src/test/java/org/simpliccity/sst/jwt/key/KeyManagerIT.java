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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.security.Key;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={KeyManagerConfig.class})
@ActiveProfiles("keyManager")
public class KeyManagerIT 
{
	@Autowired
	private KeyManager keyManager;
	
	@Autowired
	private HS512TestKeyProvider provider1;
	
	@Autowired
	private PS256TestKeyProvider provider2;
	
	@Test
	public void testGetProvider1Key() throws KeyProviderException
	{
		Key key = keyManager.getSigningKey(HS512TestKeyProvider.ALGORITHM, "SST");
		
		Key providerKey = provider1.getKey("SST");
		
		assertArrayEquals("HS512 keys match.", providerKey.getEncoded(), key.getEncoded());
	}

	@Test
	public void testGetProvider2Key() throws KeyProviderException
	{
		Key key = keyManager.getSigningKey(PS256TestKeyProvider.ALGORITHM, "SST");
		
		Key providerKey = provider2.getKey("SST");
		
		assertArrayEquals("PS256 keys match.", providerKey.getEncoded(), key.getEncoded());
	}
	
	@Test
	public void testDifferentAliasesSameProvider()
	{
		Key key1 = keyManager.getSigningKey(PS256TestKeyProvider.ALGORITHM, "SST");
		Key key2 = keyManager.getSigningKey(PS256TestKeyProvider.ALGORITHM, "ALT");

		assertFalse("Different alias keys differ.", Arrays.equals(key1.getEncoded(), key2.getEncoded()));
	}

	@Test
	public void testSameAliasDifferentProviders()
	{
		Key key1 = keyManager.getSigningKey(HS512TestKeyProvider.ALGORITHM, "SST");
		Key key2 = keyManager.getSigningKey(PS256TestKeyProvider.ALGORITHM, "SST");

		assertFalse("Different alias keys differ.", Arrays.equals(key1.getEncoded(), key2.getEncoded()));
	}
	
	@Test
	public void testUnknownAlias()
	{
		Key key = keyManager.getSigningKey(HS512TestKeyProvider.ALGORITHM, "UNKNOWN");
		
		assertNull("Unknown key alias.", key);
	}

	@Test
	public void testUnknownAlgorithm()
	{
		Key key = keyManager.getSigningKey("UNKNOWN", "SST");
		
		assertNull("Unknown key algorithm.", key);
	}
}
