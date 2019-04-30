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

/**
 * Defines the operation that is required to generate a bean name, likely within a bean namespace,
 * from metadata intended to identify a unique bean.
 * 
 * @author Kevin Fox
 * @since 0.3.0
 * 
 * @see org.simpliccity.sst.bean.lookup.namespace.MappedNameBeanMetadataLookupStrategy
 *
 */
public interface BeanMetadataLookupNameMapper 
{
	/**
	 * Generates the name of a unique Spring bean name from a identifying metadata.  The generated name
	 * can be used to lookup a bean from the {@link org.springframework.context.ApplicationContext}.
	 *  
	 * @param container A wrapper class for the metadata specifying the desired bean.
	 * @param <T> The class to which the container applies.
	 * @return The name of the bean identified by the metadata.
	 */
	<T> String generateNameFromMetadata(BeanMetadataLookupContainer<T> container);
}
