/*
 *    Copyright 2016 Information Control Company
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

package org.simpliccity.sst.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Utility functions to facilitate working with annotations for Java-based configuration.
 * 
 * @author Kevin Fox
 * @since 1.0.0
 *
 */
public final class ConfigurationAnnotationUtils 
{
	private ConfigurationAnnotationUtils()
	{
		super();
	}
	
	/**
	 * Determines whether the specified type is marked with an {@code @IfEnabled} annotation and returns the
	 * name of the enabling class, if so.
	 * 
	 * @param metadata The annotation metadata for the specified type.
	 * @return The name of the enabling class in the {@code @IfEnabled} annotation, if any; null, otherwise.
	 */
	public static String getEnablerName(AnnotatedTypeMetadata metadata)
	{
		String enablerClassName = null;
		if (metadata.isAnnotated(IfEnabled.class.getName()))
		{
			Map<String, Object> attributes = metadata.getAnnotationAttributes(IfEnabled.class.getName(), true);
			enablerClassName = (String) attributes.get("value");
		}
		
		return enablerClassName;
	}
	
	/**
	 * Extracts the class name for the type represented by the annotation metadata.
	 * 
	 * @param metadata The annotation metadata for the specified type.
	 * @return The name of the class represented by the metadata; "unknown" if the metadata does not represent a class.
	 */
	public static String getClassName(AnnotatedTypeMetadata metadata)
	{
		String result = "unknown";
		if (metadata instanceof ClassMetadata)
		{
			result = ((ClassMetadata) metadata).getClassName();
		}
		
		return result;
	}
	
	/**
	 * Provides the attribute values for a specific annotation represented within the annotation metadata. Spring property placeholders are 
	 * automatically resolved in the attribute values.
	 * 
	 * @param resolver The property resolver used to fill in placeholders in the attribute values.
	 * @param metadata The annotation metadata for the specified type.
	 * @param annotationName The name of the specific annotation for which attributes are wanted.
	 * @return A map of the attributes and values for the specified annotation.
	 */
	public static MultiValueMap<String, Object> getResolvedAttributes(PropertyResolver resolver, AnnotatedTypeMetadata metadata, String annotationName)
	{
		MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(annotationName);
		
		if (attributes == null)
		{
			// If no attributes, return an empty map
			attributes = new LinkedMultiValueMap<>();
		}
		else
		{
			// Loop through entries and resolve any property placeholders
			for (Entry<String, List<Object>> entry : attributes.entrySet())
			{
				List<Object> values = resolvePlaceholders(resolver, entry.getValue());
				attributes.put(entry.getKey(), values);
			}	
		}
		
		return attributes;
	}
	
	private static List<Object> resolvePlaceholders(PropertyResolver resolver, List<Object> values)
	{
		// Loop through values, resolving placeholders as appropriate
		for (int i = 0; i < values.size(); i++)
		{
			Object value = values.get(i);
			if (value instanceof String)
			{
				// Directly resolve placeholders in a string
				value = resolver.resolvePlaceholders((String) value);
			}
			else if (value instanceof Object[])
			{
				// Recursively call this method to resolve placholders in an array
				List<Object> resolved = resolvePlaceholders(resolver, Arrays.asList((Object[]) value));
				value = resolved.toArray();
			}
			else if (value instanceof AnnotationAttributes)
			{
				// Call the annotation variant of this method if the value is another annotation
				value = resolvePlaceholders(resolver, (AnnotationAttributes) value);
			}
			
			values.set(i, value);
		}
		
		return values;
	}
	
	private static AnnotationAttributes resolvePlaceholders(PropertyResolver resolver, AnnotationAttributes attributes)
	{
		// Loop through the attributes of the specified annotation...
		for (Entry<String, Object> attribute : attributes.entrySet())
		{
			// ... recursively calling the main resolution method to resolve any placeholders
			List<Object> values = Arrays.asList(attribute.getValue());
			values = resolvePlaceholders(resolver, values);
			attribute.setValue(values.get(0));
		}
		
		return attributes;
	}
}
