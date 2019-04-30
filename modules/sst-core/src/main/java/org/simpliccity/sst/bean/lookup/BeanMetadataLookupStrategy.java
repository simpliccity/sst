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

package org.simpliccity.sst.bean.lookup;

import org.springframework.beans.factory.BeanFactory;

/**
 * <p>Defines the operations that a strategy implementation for retrieving Spring beans by metadata from the application
 * context must support.  The {@link BeanMetadataLookupManager} uses a configured instance of a class that implements 
 * this interface to retrieve beans from the application context.  This makes it possible to enhance, extend and adapt 
 * the capabilities of the <code>lookup</code> framework over time.</p>
 * 
 * <p>This interface defines separate methods for retrieving typed beans and annotated Spring components.  This is to
 * allow for the fact that very different mechanisms may be required to compile and manage bean metadata depending on
 * how the bean is defined in the application context.  A particular strategy implementation may choose to support one
 * or both of the methods.  An unsupported method should throw a {@link BeanMetadataLookupException}.</p>
 * 
 * @author Kevin Fox
 * @since 0.3.0
 * 
 * @see BeanMetadataLookupManager
 *
 */
public interface BeanMetadataLookupStrategy 
{
	/**
	 * Retrieves a single bean from the bean factory (application context) based on the specified metadata.  The bean
	 * to be retrieved must have been annotated by the discriminator class associated with the metadata.
	 * 
	 * @param factory The bean factory to search for the desired bean.
	 * @param container A wrapper class for the metadata specifying the desired bean.
	 * @param <T> The class to which the container applies.
	 * @return A bean from the bean factory matching the specified metadata, if any; <code>null</code> otherwise.
	 * @throws BeanMetadataLookupException If an error is encountered retrieving the bean.
	 */
	<T> Object getAnnotatedBean(BeanFactory factory, BeanMetadataLookupContainer<T> container) throws BeanMetadataLookupException;
	
	/**
	 * <p>Retrieves a single bean from the bean factory (application context) based on the specified metadata.  The bean
	 * to be retrieved must be of the same type as the discriminator class associated with the metadata.</p>  
	 * 
	 * <p>Although an implementing class MAY choose to support other mechanisms for associating metadata with a bean of a specified
	 * type, it MUST support beans that are marked with the {@link org.simpliccity.sst.bean.lookup.annotation.DynamicTypedComponent}
	 * annotation.</p>
	 * 
	 * @param factory The bean factory to search for the desired bean.
	 * @param container A wrapper class for the metadata specifying the desired bean.
	 * @param <T> The class to which the container applies.
	 * @return A bean from the bean factory matching the specified metadata, if any; <code>null</code> otherwise.
	 * @throws BeanMetadataLookupException If an error is encountered retrieving the bean.
	 * 
	 * @see org.simpliccity.sst.bean.lookup.annotation.DynamicTypedComponent
	 */
	<T> T getTypedBean(BeanFactory factory, BeanMetadataLookupContainer<T> container) throws BeanMetadataLookupException;
}
