/*
 *    Copyright 2012 Information Control Corporation
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

package org.simpliccity.sst.web.property;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.simpliccity.sst.web.property.PropertyConfigurator;

public class PropertyConfiguratorTest 
{
	private static final String CATEGORY = "Test Category";
	
	private PropertyConfigurator configurator;
	private List<PropertyConfigurator.Entry> entries;
	
	@Before
	public void initialize()
	{
		configurator = new PropertyConfigurator();
		configurator.setPropertyCategory(CATEGORY);
		
		setupEntries(5);
		configurator.setEntries(entries);
	}
	
	/**
	 * Tests that property category is set correctly.
	 */
	@Test
	public void testCategory()
	{
		assertEquals("Configurator property category", CATEGORY, configurator.getPropertyCategory());
	}
	
	/**
	 * Tests that enries are set correctly.
	 */
	@Test
	public void testSetEntries()
	{
		List<PropertyConfigurator.Entry> tempEntries = configurator.getEntries();
		
		assertEquals("Entry list matches", entries, tempEntries);
	}
	
	/**
	 * Tests that the value map correctly represents the entries.
	 */
	@Test
	public void testGetValues()
	{
		Map<String, String> tempValues = configurator.getValues();
		
		assertEquals("Size of values map", entries.size(), tempValues.size());
		
		for (PropertyConfigurator.Entry entry : entries)
		{
			String key = entry.getKey();
			String value = entry.getValue();
			
			assertEquals("Entry contained in value map", value, tempValues.get(key));
		}
	}
	
	/**
	 * Tests that setting the values correctly updates the entries.
	 */
	@Test
	public void testSetValues()
	{
		Map<String, String> newValues = new HashMap<String, String>();
		
		for (PropertyConfigurator.Entry entry : entries)
		{
			String key = entry.getKey();
			String value = String.valueOf(Math.random());
			newValues.put(key, value);
		}
		configurator.setValues(newValues);
		
		assertEquals("Size of updated entries", newValues.size(), configurator.getEntries().size());
		
		for (PropertyConfigurator.Entry entry : configurator.getEntries())
		{
			String key = entry.getKey();
			String value = entry.getValue();
			
			assertEquals("Updated values contained in entries", newValues.get(key), value);
		}
	}
	
	private void setupEntries(int size)
	{
		entries = new ArrayList<PropertyConfigurator.Entry>();
		
		for (int i = 1; i <= size; i++)
		{
			PropertyConfigurator.Entry entry = new PropertyConfigurator.Entry();
			
			entry.setKey("Key" + i);
			entry.setValue("Value" + i);
			entry.setName("Name " + i);
			entry.setDescription("Description " + i);
			entry.setHint("Hint " + i);
			
			entries.add(entry);
		}
	}
}
