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

package org.simpliccity.sst.transform.annotation;

import java.util.HashMap;
import java.util.Map;

import org.simpliccity.sst.bean.lookup.AbstractBeanMetadataLookupContainer;
import org.springframework.core.type.AnnotationMetadata;

/**
 * A wrapper class encapsulating the metadata values used to lookup a {@link Transformation} component
 * from the application context.
 * 
 * @author Kevin Fox
 * @since 0.3.0
 *
 */
public class TransformationBeanMetadataLookupContainer extends AbstractBeanMetadataLookupContainer<Transformation> 
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Key used to identify the <b>source</b> attribute ({@value}).
	 */
	public static final String KEY_ATTRIBUTE_SOURCE = "source";

	/**
	 * Key used to identify the <b>target</b> attribute ({@value}).
	 */
	public static final String KEY_ATTRIBUTE_TARGET = "target";
	
	private String source;
	private String target;
	
	/**
	 * Creates an instance of TransformationBeanMetadataLookupContainer without any metadata values.
	 */
	public TransformationBeanMetadataLookupContainer()
	{
		super(Transformation.class);
	}
	
	/**
	 * Creates an instance of TransformationBeanMetadataLookupContainer using metadata values extracted
	 * from the specified annotation.
	 * 
	 * @param xform The {@link Transformation} annotation from which to extract metadata values.
	 */
	public TransformationBeanMetadataLookupContainer(Transformation xform)
	{
		super(Transformation.class);
		
		this.source = xform.source().getCanonicalName();
		this.target = xform.target().getCanonicalName();
	}
	
	/**
	 * Creates an instance of TransformationBeanMetadataLookupContainer using the Spring annotation metadata.
	 * 
	 * @param metadata The Spring metadata for a {@link Transformation} annotation.
	 */
	public TransformationBeanMetadataLookupContainer(AnnotationMetadata metadata)
	{
		super(Transformation.class);

		// Get the metadata for the Transformation annotation, using class names rather than classes
		Map<String, Object> attributes = metadata.getAnnotationAttributes(Transformation.class.getName(), true);
		
		this.source = (String) attributes.get(KEY_ATTRIBUTE_SOURCE);
		this.target = (String) attributes.get(KEY_ATTRIBUTE_TARGET);
	}

	@Override
	public Map<String, Object> getMetadataAttributes() 
	{
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(KEY_ATTRIBUTE_SOURCE, getSource());
		attributes.put(KEY_ATTRIBUTE_TARGET, getTarget());
		
		return attributes;
	}

	/**
	 * Returns the <code>source</code> metadata value.
	 * 
	 * @return The <code>source</code> metadata value.
	 */
	public String getSource() 
	{
		return source;
	}

	/**
	 * Sets the <code>source</code> metadata value.
	 * 
	 * @param source The <code>source</code> metadata value.
	 */
	public void setSource(String source) 
	{
		this.source = source;
	}

	/**
	 * Returns the <code>target</code> metadata value.
	 * 
	 * @return The <code>target</code> metadata value.
	 */
	public String getTarget() 
	{
		return target;
	}

	/**
	 * Sets the <code>target</code> metadata value.
	 * 
	 * @param target the <code>target</code> metadata value.
	 */
	public void setTarget(String target) 
	{
		this.target = target;
	}
}
