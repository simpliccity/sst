/*
 *    Copyright 2017 Information Control Company
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

package org.simpliccity.sst.bean.lookup;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSimpleAnnotationBeanMetadataLookupContainer<T> extends AbstractBeanMetadataLookupContainer<T> 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Key used to identify the <b>value</b> attribute ({@value}).
	 */	
	public static final String KEY_ATTRIBUTE_VALUE = "value";
	
	private String value;

	protected AbstractSimpleAnnotationBeanMetadataLookupContainer(Class<T> discriminator) 
	{
		super(discriminator);
	}

	@Override
	public Map<String, Object> getMetadataAttributes() 
	{
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(KEY_ATTRIBUTE_VALUE, getValue());
		
		return attributes;
	}

	/**
	 * Returns the <code>value</code> metadata value.
	 * 
	 * @return The <code>value</code> metadata value.
	 */
	public String getValue() 
	{
		return value;
	}

	/**
	 * Sets the <code>value</code> metadata value.
	 * 
	 * @param value The <code>value</code> metadata value.
	 */
	public void setValue(String value) 
	{
		this.value = value;
	}
}
