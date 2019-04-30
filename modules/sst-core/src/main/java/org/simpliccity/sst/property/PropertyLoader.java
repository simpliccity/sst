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

/**
 * Loads application properties for use by a {@link DelegatingPropertiesFactoryBean} or a
 * {@link DelegatingPropertyPlaceholderConfigurer}.  The source of the properties is determined
 * by the implementing class.
 * 
 * @author Kevin Fox
 *
 */
public interface PropertyLoader 
{
	/**
	 * Loads application properties into the provided {@link java.util.Properties}
	 * object.
	 * 
	 * @param props The {@link java.util.Properties} object to be populated.
	 * @throws IOException If an I/O error occurs loading the properties.
	 */
	void loadProperties(Properties props) throws IOException;
}
