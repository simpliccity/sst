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

/**
 * An abstract parent class for implementations of {@link BeanMetadataLookupContainer} that appropriately
 * handles the metadata discriminator.
 * 
 * @author Kevin Fox
 * @since 0.3.0
 *
 * @param <T> The type or annotation of a Spring bean for which this class represents metadata.
 */
public abstract class AbstractBeanMetadataLookupContainer<T> implements BeanMetadataLookupContainer<T> 
{
	private static final long serialVersionUID = 1L;
	
	private Class<T> discriminator;
	
	/**
	 * A constructor that specifies the metadata discriminator (i.e. Java type or Spring component type of
	 * the bean returned by a lookup using this metadata).
	 * 
	 * @param discriminator The class of the Java type or Spring component type (i.e. annotation) to lookup.
	 */
	protected AbstractBeanMetadataLookupContainer(Class<T> discriminator)
	{
		this.discriminator = discriminator;
	}
	
	@Override
	public Class<T> getLookupDiscriminator()
	{
		return discriminator;
	}
	
	@Override
	public boolean isAnnotatedLookup()
	{
		return discriminator.isAnnotation();
	}
	
	@Override
	public boolean isTypedLookup()
	{
		return !isAnnotatedLookup();
	}
}
