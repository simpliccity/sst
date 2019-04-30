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

package org.simpliccity.sst.jwt.key.annotation;

import java.util.HashMap;
import java.util.Map;

import org.simpliccity.sst.bean.lookup.AbstractBeanMetadataLookupContainer;
import org.springframework.core.type.AnnotationMetadata;

public class KeySourceBeanMetadataLookupContainer extends AbstractBeanMetadataLookupContainer<KeySource>
{
	public static final String KEY_ATTRIBUTE_VALUE = "value";
	
	public static final String KEY_ATTRIBUTE_ALGORITHM = "algorithm";
	
	private static final long serialVersionUID = 1L;

	private String algorithm;
	
	public KeySourceBeanMetadataLookupContainer()
	{
		super(KeySource.class);
	}
	
	public KeySourceBeanMetadataLookupContainer(KeySource extractor)
	{
		super(KeySource.class);
		
		this.algorithm = extractor.value();
	}
	
	public KeySourceBeanMetadataLookupContainer(AnnotationMetadata annotation)
	{
		super(KeySource.class);
		
		// Get the metadata for the KeySource annotation, using class names rather than classes
		Map<String, Object> attributes = annotation.getAnnotationAttributes(KeySource.class.getName(), true);
		
		this.algorithm = (String) attributes.get(KEY_ATTRIBUTE_VALUE);
	}
	
	public String getAlgorithm() 
	{
		return algorithm;
	}

	public void setAlgorithm(String algorithm) 
	{
		this.algorithm = algorithm;
	}

	@Override
	public Map<String, Object> getMetadataAttributes() 
	{
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(KEY_ATTRIBUTE_ALGORITHM, getAlgorithm());
		
		return attributes;
	}
}
