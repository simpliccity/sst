/*
 *    Copyright 2011 Information Control Corporation
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

package org.simpliccity.sst.property;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/PropertyLoaderTest.xml" })
public class ClassPathResourcePropertyLoaderTest 
{
	@Autowired
	@Qualifier("Resource")
	private PropertyLoader loader;

	@Autowired
	@Qualifier("ResourceBad")
	private PropertyLoader badLoader;

	@Autowired
	@Qualifier("ResourceBadEncoding")
	private PropertyLoader badEncodingLoader;

	@Test
	public void loadProperties() throws Exception
	{
		Properties props = new Properties();
		loader.loadProperties(props);
		
		assertEquals("Test property", "HIJKLMN", props.getProperty("testProperty"));
		
		assertEquals("New property", "TEST", props.getProperty("newProperty"));
	}
	
	@Test(expected=FileNotFoundException.class)
	public void testBadResource() throws IOException
	{
		Properties props = new Properties();
		badLoader.loadProperties(props);
	}

	@Test(expected=UnsupportedEncodingException.class)
	public void testBadEncodingResource() throws IOException
	{
		Properties props = new Properties();
		badEncodingLoader.loadProperties(props);
	}
}
