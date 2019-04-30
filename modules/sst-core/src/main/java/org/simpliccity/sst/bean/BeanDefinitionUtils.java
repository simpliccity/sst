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

package org.simpliccity.sst.bean;

import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.beans.factory.parsing.Location;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.io.Resource;

/**
 * Utility functions for working with Spring bean definitions, primarily in the
 * context of a {@link org.springframework.beans.factory.xml.BeanDefinitionParser}.
 * 
 * @author Kevin Fox
 * @since 1.0.0
 *
 */
public final class BeanDefinitionUtils 
{
	// Private constructor to hide default
	private BeanDefinitionUtils()
	{
		super();
	}
	
	/**
	 * Produces a {@link org.springframework.beans.factory.parsing.BeanDefinitionParsingException}
	 * describing a problem parsing a bean definition resource, as represented by the specified
	 * context and message.
	 * 
	 * @param parserContext The context of the bean definition parsing operation that failed.
	 * @param message The exception message of the failed operation.
	 * @return An exception describing the failed parsing operation.
	 */
	public static BeanDefinitionParsingException generateParsingException(ParserContext parserContext, String message)
	{
		Resource resource = parserContext.getReaderContext().getResource();
		Location location = new Location(resource);
		Problem problem = new Problem(message, location);
		
		return new BeanDefinitionParsingException(problem);
	}
}
