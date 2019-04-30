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

package org.simpliccity.sst.bean.lookup.namespace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.simpliccity.sst.bean.lookup.BeanMetadataLookupContainer;
import org.simpliccity.sst.bean.lookup.annotation.DynamicTypedComponent;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * <p>Generates bean names in the <code>typecomponent</code> namespace for classes annotated with {@link DynamicTypedComponent}.
 * Also provides support for locating {@link DynamicTypedComponent} beans using the <code>namespace</code> bean metadata
 * lookup strategy.</p>
 * 
 * <p>This class will automatically be employed to name beans annotated with {@link DynamicTypedComponent} when Spring
 * annotation scanning is configured to use the {@link org.simpliccity.sst.bean.annotation.PropertiesDelegateAnnotationBeanNameGenerator}
 * bean name generator.</p>
 * 
 * @author Kevin Fox
 * @since 1.0.0
 * 
 * @see org.simpliccity.sst.transform.Transformer
 *
 */
public class DynamicTypedComponentAnnotationNamespaceBeanNameGenerator extends AbstractLookupAnnotationNamespaceBeanNameGenerator 
{
	/**
	 * Namespace name pattern ({@value}).
	 */
	public static final String NAME_PATTERN = "sst.typedcomponent.[{0}]";
	
	private static final String KEY_ATTRIBUTE_TYPE = "type";
	private static final String KEY_ATTRIBUTE_METADATA = "metadata";
	private static final String KEY_ATTRIBUTE_NAME = "name";
	private static final String KEY_ATTRIBUTE_VALUE = "value";
	
	@Override
	protected <T> String[] getNameComponentsFromMetadata(BeanMetadataLookupContainer<T> container) 
	{
		List<String> componentList = new ArrayList<>();
		
		// Add the type as the first element in the list
		String typeName = container.getLookupDiscriminator().getName();
		componentList.add(typeName);
		
		// Process the component metadata
		Map<String, Object> attributes = container.getMetadataAttributes();
		for (Map.Entry<String, Object> entry : attributes.entrySet())
		{
			// Build a string representation of the name/value pair represented
			String attributeComponent = entry.getKey() + "=" + entry.getValue();
			componentList.add(attributeComponent);
		}
		
		return componentList.toArray(new String[componentList.size()]);
	}

	@Override
	protected String getAnnotationClassName() 
	{
		return DynamicTypedComponent.class.getName();
	}

	@Override
	protected String[] getNameComponentsFromAnnotationMetadata(AnnotationMetadata annotation) 
	{
		List<String> componentList = new ArrayList<>();
		
		// Get the metadata for the DynamicTypedComponent annotation, using class names rather than classes
		Map<String, Object> attributes = annotation.getAnnotationAttributes(getAnnotationClassName(), true);
		
		// Add the type as the first element in the list
		String typeName = (String) attributes.get(KEY_ATTRIBUTE_TYPE);
		componentList.add(typeName);
		
		// Process the component metadata (one AnnotationAttributes instance per ComponentMetadata)
		AnnotationAttributes[] metadata = (AnnotationAttributes[]) attributes.get(KEY_ATTRIBUTE_METADATA);
		for (AnnotationAttributes metadataAttribute : metadata)
		{
			// Build a string representation of the name/value pair represented
			String attributeComponent = metadataAttribute.get(KEY_ATTRIBUTE_NAME) + "=" + metadataAttribute.get(KEY_ATTRIBUTE_VALUE);
			componentList.add(attributeComponent);			
		}
		
		return componentList.toArray(new String[componentList.size()]);
	}

	@Override
	protected String getNamespaceNamePattern() 
	{
		return NAME_PATTERN;
	}

	@Override
	protected String postProcessName(String baseName, String... nameComponents)
	{
		// Initialize a StringBuffer from the generated name
		StringBuilder result = new StringBuilder(baseName);
		
		// Append the metadata attributes to the name (skipping the first name component, which is the type)
		for (int i = 0; i < nameComponents.length; i++)
		{
			if (i > 0)
			{
				result.append(".<");
				result.append(nameComponents[i]);
				result.append(">");
			}
		}
		
		return result.toString();
	}
}
