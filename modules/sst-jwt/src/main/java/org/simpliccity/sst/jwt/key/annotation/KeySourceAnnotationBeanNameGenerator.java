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

import org.simpliccity.sst.bean.lookup.BeanMetadataLookupContainer;
import org.simpliccity.sst.bean.lookup.namespace.AbstractLookupAnnotationNamespaceBeanNameGenerator;
import org.springframework.core.type.AnnotationMetadata;

public class KeySourceAnnotationBeanNameGenerator extends AbstractLookupAnnotationNamespaceBeanNameGenerator 
{
	/**
	 * Namespace name pattern ({@value}).
	 */
	public static final String NAME_PATTERN = "sst.signingKeySource.<{0}>";
	

	@Override
	protected String getNamespaceNamePattern() 
	{
		return NAME_PATTERN;
	}

	@Override
	protected String getAnnotationClassName() 
	{
		return KeySource.class.getName();
	}

	@Override
	protected String[] getNameComponentsFromAnnotationMetadata(AnnotationMetadata annotation) 
	{
		KeySourceBeanMetadataLookupContainer container = new KeySourceBeanMetadataLookupContainer(annotation);
		
		return getNameComponentsFromMetadata(container);
	}
	
	@Override
	protected <T> String[] getNameComponentsFromMetadata(BeanMetadataLookupContainer<T> container) 
	{
		KeySourceBeanMetadataLookupContainer metadata = (KeySourceBeanMetadataLookupContainer) container;
		
		String algorithm = metadata.getAlgorithm();
		
		return new String[] {algorithm};
	}
}
