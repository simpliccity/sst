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

import java.io.Serializable;
import java.util.Map;

/**
 * Defines the operations required of a wrapper class representing metadata used to locate
 * a Spring bean with the <code>lookup</code> framework.  An implementation of this interface
 * will define specific metadata attributes that apply for a bean of the specified type.  Note
 * that in this context, "type" refers either to a standard Java type or to a Spring component
 * type (i.e. annotation).
 * 
 * @author Kevin Fox
 * @since 0.3.0
 *
 * @param <T> The type or annotation of a Spring bean for which this class represents metadata.
 */
public interface BeanMetadataLookupContainer<T> extends Serializable
{
	/**
	 * Designates the bean type (Java type or Spring component type) which a lookup using
	 * the metadata represented by this class will return.
	 * 
	 * @return The class of the Java type or Spring component type (i.e. annotation) to lookup.
	 */
	Class<T> getLookupDiscriminator();
	
	/**
	 * Indicates whether the lookup will be for an annotated Spring bean.
	 * 
	 * @return <code>true</code> if the discriminator is a Spring annotation; <code>false</code> otherwise.
	 * 
	 * @see #getLookupDiscriminator()
	 */
	boolean isAnnotatedLookup();
	
	/**
	 * Indicates whether the lookup will be for a Java type.
	 * 
	 * @return <code>true</code> if the discriminator is <b>not</b> a Spring annotation; <code>false</code> otherwise.
	 * 
	 * @see #getLookupDiscriminator()
	 */
	boolean isTypedLookup();
	
	/**
	 * Returns the metadata attributes to be used to lookup an instance of the specified bean type
	 * as a map of name/value pairs.
	 * 
	 * @return A map of the metadata attributes as name/value pairs.
	 */
	Map<String, Object> getMetadataAttributes();
}
