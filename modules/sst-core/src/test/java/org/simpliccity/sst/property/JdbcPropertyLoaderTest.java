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

import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactoryBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/PropertyLoaderTest.xml" })
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcPropertyLoaderTest 
{
	@Autowired
	@Qualifier("JDBC")
	private PropertyLoader loader;
	
	@Autowired
	@Qualifier("JDBC_BAD_QUERY")
	private PropertyLoader badQueryLoader;

	@Autowired
	@Qualifier("JDBC_BAD_CONFIG")
	private PropertyLoader badConfigLoader;

	@Autowired
	private EmbeddedDatabaseFactoryBean dbFactory;
	
	@After
	public void cleanup()
	{
		dbFactory.destroy();
	}
	
	@Test
	public void loadProperties() throws Exception
	{
		Properties props = new Properties();
		loader.loadProperties(props);
		
		assertEquals("Test property", "abcdefg", props.getProperty("testProperty"));
		
		assertEquals("Test URL", "http://www.simpliccity.org", props.getProperty("testURL"));
	}
	
	@Test(expected=IOException.class)
	public void testBadQuery() throws IOException
	{
		Properties props = new Properties();
		badQueryLoader.loadProperties(props);		
	}
	
	@Test(expected=IOException.class)
	public void testBadConfig() throws IOException
	{
		Properties props = new Properties();
		badConfigLoader.loadProperties(props);		
	}
}
