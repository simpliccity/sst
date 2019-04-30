/*
 *    Copyright 2014 Information Control Corporation
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

package org.simpliccity.sst.property.source;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PropertyLoaderPropertySourceTest 
{
	private PropertyLoaderPropertySource propertySource;
	private JdbcPropertyLoaderBuilder builder;
	
	@Before
	public void initialize()
	{
		builder = new JdbcPropertyLoaderBuilder();
		
		propertySource = new PropertyLoaderPropertySource("testSource", builder.getPropertyLoader());
	}
	
	@After
	public void cleanup()
	{
		builder.cleanup();
	}

	 @Test
	 public void testPropertyResolution()
	 {
		 assertEquals("Correct value for testProperty", "abcdefg", propertySource.getProperty("testProperty"));
		 assertEquals("Correct value for testURL", "http://www.simpliccity.org", propertySource.getProperty("testURL"));
	 }
	 
	 @Test
	 public void testPropertyNames()
	 {
		 String[] names = propertySource.getPropertyNames();
		 List<String> nameList = Arrays.asList(names);
		 
		 assertTrue("Contains testProperty", nameList.contains("testProperty"));
		 assertTrue("Contains testURL", nameList.contains("testURL"));
	 }
}
