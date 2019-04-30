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

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;

import org.simpliccity.sst.property.PropertyLoader;
import org.springframework.core.env.EnumerablePropertySource;

/**
 * An adapter class that enables the use of a {@link PropertyLoader} implementation in
 * Spring's unified property management.  This class defines an {@link EnumerablePropertySource}
 * backed by a {@link PropertyLoader}.  Properties are loaded, using the specified loader, and
 * cached at the time of instantiation.
 * 
 * @author Kevin Fox
 * @since 1.0.0
 *
 */
public class PropertyLoaderPropertySource extends EnumerablePropertySource<PropertyLoader> 
{
	private Properties properties = new Properties();
	private String[] propNames;
	
	/**
	 * Creates a new {@link EnumerablePropertySource} backed by the specified {@link PropertyLoader}.
	 * Properties (and property names) are loaded using the {@link PropertyLoader} and cached at the time 
	 * of creation.  If the {@link PropertyLoader} is unable to load properties, it will fail quietly 
	 * (i.e. the cached properties will be empty).
	 * 
	 * @param name The name given the property source.
	 * @param source The underlying source for properties (in this case a {@link PropertyLoader}).
	 */
	public PropertyLoaderPropertySource(String name, PropertyLoader source)
	{
		super(name, source);
		
		// Use source as property loader to load properties into internal Properties instance
		try 
		{
			getSource().loadProperties(properties);
		} 
		catch (IOException e) 
		{
			// Log error but let execution continue 
			// (given the hierarchical nature of property sources, this may not be fatal)
			logger.error("Unable to initialize PropertyLoader-based PropertySource [" + name + "].", e);
		}
		
		// Cache property names from internal Properties instance
		Set<Object> keys = properties.keySet();
		propNames = keys.toArray(new String[keys.size()]);
	}

	@Override
	public Object getProperty(String name) 
	{
		return properties.get(name);
	}

	@Override
	public String[] getPropertyNames() 
	{
		return Arrays.copyOf(propNames, propNames.length);
	}
}
