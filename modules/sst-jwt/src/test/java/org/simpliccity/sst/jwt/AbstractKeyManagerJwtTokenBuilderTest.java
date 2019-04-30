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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.simpliccity.sst.jwt.key.KeyManager;
import org.springframework.beans.factory.BeanInitializationException;

@RunWith(MockitoJUnitRunner.class)
public class AbstractKeyManagerJwtTokenBuilderTest
{
	public static final String ALG_DEFAULT = "default";
	
	public static final String ALG_ALTERNATE = "alternate";
	
	public static final String ISSUER = "SST";
	
	@Mock(answer=Answers.CALLS_REAL_METHODS) private AbstractKeyManagerJwtTokenBuilder builder;
	
	@Mock private KeyManager keyManager;
	
	@Mock private Key defaultKey;
	
	@Mock private Log logger;
	
	@Before
	public void init()
	{
		builder.setKeyManager(keyManager);
		when(builder.getLogger()).thenReturn(logger);
		
		when(keyManager.getSigningKey(ALG_DEFAULT, ISSUER)).thenReturn(defaultKey);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testKeyProvided()
	{
		builder.buildToken(ISSUER, null, ALG_DEFAULT, -1);
		
		verify(builder).buildAndSignToken(eq(defaultKey), eq(ISSUER), (Map<String, Object>) isNull(), eq(ALG_DEFAULT), any(Date.class), (Date) isNull());
	}
	
	@Test(expected=BeanInitializationException.class)
	public void testInitMissingKeyManager() throws Exception
	{
		builder.setKeyManager(null);
		builder.afterPropertiesSet();
	}
	
	@Test
	public void testCorrectInit() throws Exception
	{
		builder.afterPropertiesSet();
		
		assertEquals("Key manager set correctly.", keyManager, builder.getKeyManager());
		
		verify(builder).initialize();
	}
}
