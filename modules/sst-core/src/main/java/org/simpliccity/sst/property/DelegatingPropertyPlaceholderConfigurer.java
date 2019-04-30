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

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * An extension to the Spring {@link org.springframework.beans.factory.config.PropertyPlaceholderConfigurer}
 * that delegates the loading of the application properties to an instance of {@link PropertyLoader}.
 * As a result, it provides all of the functionality of {@link org.springframework.beans.factory.config.PropertyPlaceholderConfigurer}
 * while allowing properties to be loaded from any source, not just file resources.
 * 
 * @author Kevin Fox
 *
 */
public class DelegatingPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer 
{
	private PropertyLoader propertyLoader;

	/**
	 * Returns the {@link PropertyLoader} that will be used to load the application properties
	 * used by this {@link org.springframework.beans.factory.config.PropertyPlaceholderConfigurer}.
	 * 
	 * @return The class used to load application properties.
	 */
	public PropertyLoader getPropertyLoader() 
	{
		return propertyLoader;
	}

	/**
	 * Specifies the {@link PropertyLoader} that will be used to load the application properties
	 * used by this {@link org.springframework.beans.factory.config.PropertyPlaceholderConfigurer}.
	 * 
	 * @param propertyLoader The class used to load application properties.
	 */
	public void setPropertyLoader(PropertyLoader propertyLoader) 
	{
		this.propertyLoader = propertyLoader;
	}
	
	@Override
	protected void loadProperties(Properties props) throws IOException
	{
		getPropertyLoader().loadProperties(props);
	}
}
