/*
 *    Copyright 2010 Information Control Corporation
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>A generic model class backing user interface support for dynamically
 * configuring application properties. Although the class can be used with
 * any MVC implementation, it is specifically designed to support the
 * <b>configuration</b> tag library in the SST Web Flow project.</p>
 * 
 * <p>An instance of this class is used to aggregate the metadata for a set
 * of related application properties.  The metadata can be used to generate the
 * UI for editing the corresponding properties.  Updated values are captured
 * and can be returned as a {@link java.util.Map} for further processing
 * within the application.</p>
 * 
 * @author Kevin Fox
 *
 */
public class PropertyConfigurator implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String propertyCategory;
	private List<Entry> entries;

	/**
	 * Specifies the category to which the various properties belong.
	 * 
	 * @param propertyCategory The name of the property category.
	 */
	public void setPropertyCategory(String propertyCategory) 
	{
		this.propertyCategory = propertyCategory;
	}

	/**
	 * Returns the category to which the various properties belong.
	 * 
	 * @return The name of the property category.
	 */
	public String getPropertyCategory() 
	{
		return propertyCategory;
	}

	/**
	 * Specifies the list of property entries to be configured.
	 * 
	 * @param entries A list of {@link PropertyConfigurator.Entry} elements configured
	 * with the metadata for the supported properties.
	 */
	public void setEntries(List<Entry> entries) 
	{
		this.entries = entries;
	}

	/**
	 * Returns the list of property entries to be configured.
	 * 
	 * @return A list of {@link PropertyConfigurator.Entry} elements configured
	 * with the metadata for the supported properties.
	 */
	public List<Entry> getEntries() 
	{
		return entries;
	}

	/**
	 * Specifies the configured values for each of the supported application properties.
	 * The {@link PropertyConfigurator.Entry} elements are updated with the
	 * corresponding values.
	 * 
	 * @param values A {@link java.util.Map} of the property values.  The map key is 
	 * the property name.
	 */
	public void setValues(Map<String, String> values)
	{
		// Copy the values from the map to the corresponding entries
		for (Entry entry : getEntries())
		{
			entry.setValue(values.get(entry.getKey()));
		}
	}
	
	/**
	 * Returns the property values as a {@link java.util.Map}.
	 * 
	 * @return A {@link java.util.Map} of the property values.  The map key is 
	 * the property name.
	 */
	public Map<String, String> getValues()
	{
		Map<String, String> result = new HashMap<>();
		for (Entry entry : getEntries())
		{
			result.put(entry.getKey(), entry.getValue());
		}
		
		return result;
	}

	/**
	 * A class defining the metadata of an individual application property.
	 * The information specified can be used to generate a user interface
	 * element for editing the specified property.
	 * 
	 * @author Kevin Fox
	 *
	 */
	public static class Entry implements Serializable
	{
		private static final long serialVersionUID = 1L;
		
		private String key;
		private String name;
		private String description;
		private String hint;
		private String value;
		
		/**
		 * Constructs an empty element.
		 */
		public Entry()
		{
			super();
		}
		
		/**
		 * Specifies the application property key.
		 * 
		 * @param key Key used to retrieve the application property.
		 */
		public void setKey(String key) 
		{
			this.key = key;
		}
		
		/**
		 * Returns the application property key.
		 * 
		 * @return The key used to retrieve the application property.
		 */
		public String getKey() 
		{
			return key;
		}

		/**
		 * Specifies the descriptive name of the application property.
		 * 
		 * @param name The name of the application property.
		 */
		public void setName(String name) 
		{
			this.name = name;
		}

		/**
		 * Returns the descriptive name of the application property.
		 * 
		 * @return The name of the application property.
		 */
		public String getName() 
		{
			return name;
		}

		/**
		 * Specifies the description of the application property.
		 * 
		 * @param description The application property description.
		 */
		public void setDescription(String description) 
		{
			this.description = description;
		}

		/**
		 * Returns the description of the application property.
		 * 
		 * @return The application property description.
		 */
		public String getDescription() 
		{
			return description;
		}

		/**
		 * Specifies the hint of reasonable values for the application property.
		 * 
		 * @param hint Text describing the property's reasonable values.
		 */
		public void setHint(String hint) 
		{
			this.hint = hint;
		}

		/**
		 * Returns the hint of reasonable values for the application property.
		 * 
		 * @return Text describing the property's reasonable values.
		 */
		public String getHint() 
		{
			return hint;
		}

		/**
		 * Specifies the value of the application property.
		 * 
		 * @param value The {@link java.lang.String} value of the property.
		 */
		public void setValue(String value) 
		{
			this.value = value;
		}

		/**
		 * Returns the value of the application property.
		 * 
		 * @return The {@link java.lang.String} value of the property.
		 */
		public String getValue() 
		{
			return value;
		}
	}
}
