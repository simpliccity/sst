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

import org.simpliccity.sst.bean.lookup.BeanMetadataLookupContainer;
import org.simpliccity.sst.bean.namespace.AbstractAnnotationNamespaceBeanNameGenerator;

/**
 * An abstract base class for annotation bean name generators that are intended to support the namespace strategy
 * for bean metadata lookup.
 *  
 * @author Kevin Fox
 * @since 0.3.0
 * 
 * @see org.simpliccity.sst.bean.lookup.namespace.MappedNameBeanMetadataLookupStrategy
 *
 */
public abstract class AbstractLookupAnnotationNamespaceBeanNameGenerator extends AbstractAnnotationNamespaceBeanNameGenerator implements BeanMetadataLookupNameMapper
{
	@Override
	public <T> String generateNameFromMetadata(BeanMetadataLookupContainer<T> container)
	{
		return generateQualifiedName(getNameComponentsFromMetadata(container));
	}

	/**
	 * Returns a subset of the specified metadata as an ordered list of name components.  The components will
	 * be used to generate a namespace-based name for the bean with the name generator's naming pattern.
	 * 
	 * @param container A wrapper class for the metadata specifying the desired bean.
	 * @param <T> The class to which the container applies.
	 * @return A list of the components of the bean name.
	 */
	protected abstract <T> String[] getNameComponentsFromMetadata(BeanMetadataLookupContainer<T> container);
}
