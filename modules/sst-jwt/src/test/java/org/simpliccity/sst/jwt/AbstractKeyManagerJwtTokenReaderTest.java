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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.HashMap;

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
public class AbstractKeyManagerJwtTokenReaderTest 
{
	public static final String ALGORITHM = "ABC";
	
	public static final String TOKEN_GOOD = "good";
	
	public static final String TOKEN_BAD = "bad";
	
	@Mock(answer=Answers.CALLS_REAL_METHODS) private AbstractKeyManagerJwtTokenReader reader;

	@Mock private KeyManager keyManager;

	private JwtContent jwtContent = new JwtContent(ALGORITHM, new HashMap<String, Serializable>());
	
	@Mock private Log logger;
	
	@Before
	public void init() throws Exception
	{
		when(reader.getLogger()).thenReturn(logger);
		
		when(reader.parseAndValidateToken(TOKEN_GOOD)).thenReturn(jwtContent);
		when(reader.parseAndValidateToken(TOKEN_BAD)).thenThrow(Exception.class);
	}
	
	@Test
	public void testParseGoodToken() throws Exception
	{
		JwtContent content = reader.parseToken(TOKEN_GOOD);
		
		verify(reader).parseAndValidateToken(TOKEN_GOOD);
		
		assertEquals("Parse returns expected content.", jwtContent, content);
	}

	@Test(expected=InvalidJwtTokenException.class)
	public void testParseBadToken() throws Exception
	{
		reader.parseToken(TOKEN_BAD);		
	}

	@Test(expected=BeanInitializationException.class)
	public void testInitMissingKeyManager() throws Exception
	{
		reader.setKeyManager(null);
		reader.afterPropertiesSet();
	}
	
	@Test
	public void testCorrectInit() throws Exception
	{
		reader.setKeyManager(keyManager);
		reader.afterPropertiesSet();
		
		assertEquals("Key manager set correctly.", keyManager, reader.getKeyManager());
		assertEquals("Known issuers empty.", 0, reader.getKnownIssuers().size());
		
		verify(reader).initialize();
	}
}
