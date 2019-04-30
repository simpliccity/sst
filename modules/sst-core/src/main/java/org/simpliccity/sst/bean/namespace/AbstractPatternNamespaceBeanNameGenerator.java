/*
 *    Copyright 2013 Information Control Corporation
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

package org.simpliccity.sst.bean.namespace;

import java.text.MessageFormat;

/**
 * <p>An abstract implementation of {@link NamespaceBeanNameGenerator} based on 
 * {@link java.text.MessageFormat MessageFormat}.  The name pattern must follow
 * the formatting rules for a {@link java.text.MessageFormat MessageFormat} pattern.</p>
 * 
 * @author Kevin Fox
 * @since 0.3.0
 *
 */
public abstract class AbstractPatternNamespaceBeanNameGenerator implements NamespaceBeanNameGenerator
{

	/**
	 * Generates a bean name using {@link java.text.MessageFormat#format(String, Object...)} according
	 * to the format pattern returned by {@link #getNamespaceNamePattern()}.  Arguments are passed 
	 * directly to {@link java.text.MessageFormat#format(String, Object...)} in the order received.
	 */
	@Override
	public String generateQualifiedName(String... nameComponents)
	{
		String baseName =  MessageFormat.format(getNamespaceNamePattern(), (Object[]) nameComponents);
		
		return postProcessName(baseName, nameComponents);
	}

	/**
	 * Specifies the pattern used to generate names for beans in the namespace.
	 * 
	 * @return The pattern defining the naming convention.  The name pattern 
	 * must follow the formatting rules for a {@link java.text.MessageFormat} 
	 * pattern.
	 */
	protected abstract String getNamespaceNamePattern();
	
	/**
	 * Allows additional processing of the name after the base name has been generated using the 
	 * specified naming pattern.  Subclasses can override this method to implement custom post
	 * processing.
	 * 
	 * @param baseName The name generated using the namespace pattern.
	 * @param nameComponents The original name components.
	 * @return The modified bean name.
	 * 
	 * @since 1.0.0
	 */
	protected String postProcessName(String baseName, String... nameComponents)
	{
		return baseName;
	}
}
