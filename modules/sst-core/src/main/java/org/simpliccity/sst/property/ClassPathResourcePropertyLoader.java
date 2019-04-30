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
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.simpliccity.styleguide.annotation.CodeStyle;
import org.simpliccity.styleguide.annotation.StyleCitation;
import org.simpliccity.styleguide.schema.CitationPlacement;
import org.simpliccity.styleguide.schema.CitationReferenceType;
import org.springframework.core.io.Resource;

/**
 * An implementation of {@link PropertyLoader} that reads application properties from a
 * property file in the classpath.  The file follows the standard format recognized by
 * {@link java.util.Properties}.
 * 
 * @author Kevin Fox
 * 
 * @see java.util.Properties
 *
 */
@CodeStyle
(
	categoryId="documentation", 
	topicId="styleguide", 
	marker="--- StyleGuide documentation.styleguide ---; highlight -sghighlight-", 
	citations={@StyleCitation(ref="singlestyle.html", type=CitationReferenceType.HTML, placement=CitationPlacement.TOP)}
)
public class ClassPathResourcePropertyLoader implements PropertyLoader 
{
	private Log logger = LogFactory.getLog(getClass());
	
	private String resourceName;
	private String characterEncoding = "UTF-8";
	
	/**
	 * Returns the name of the property file to load.  This file must exist in the classpath.
	 * 
	 * @return The name of the file to load.
	 */
	public String getResourceName() 
	{
		return resourceName;
	}

	/**
	 * Specifies the name of the property file to load.  This file must exist in the classpath.
	 * 
	 * @param resourceName The name of the file to load.
	 */
	public void setResourceName(String resourceName) 
	{
		this.resourceName = resourceName;
	}

	/**
	 * Specifies the character encoding used by the resource to be loaded.
	 * 
	 * @since 0.2.0
	 * 
	 * @param characterEncoding Name of the standard character set used for character encoding.
	 */
	public void setCharacterEncoding(String characterEncoding) 
	{
		this.characterEncoding = characterEncoding;
	}

	/**
	 * Returns the character encoding used for the resource to be loaded.  Defaults to UTF-8.
	 * 
	 * @since 0.2.0
	 * 
	 * @return The name of the standard character set used for character encoding.
	 */
	public String getCharacterEncoding() 
	{
		return characterEncoding;
	}

	// --- StyleGuide documentation.styleguide ---
	@Override
	// -sghighlight-
	@CodeStyle(categoryId="resource", topicId="loadClasspath")
	// -sghighlight-
	public void loadProperties(Properties props) throws IOException 
	{
		// --- StyleGuide resource.loadClasspath ---
		logger.debug("Loading resource on classpath: " + getResourceName());
		Resource resource = new ClassPathResource(getResourceName());

		// --- StyleGuide documentation.styleguide ---

		logger.debug("Reading properties from resource: " + getResourceName());
		try (Reader reader = new InputStreamReader(resource.getInputStream(), getCharacterEncoding()))
		{
			props.load(reader);
			logger.debug("Successfully read properties from resource: " + getResourceName());
		}
		// --- StyleGuide resource.loadClasspath ---
	}
}
