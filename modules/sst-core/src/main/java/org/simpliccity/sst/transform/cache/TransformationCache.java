/*
 *    Copyright 2012 Information Control Corporation
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

package org.simpliccity.sst.transform.cache;

/**
 * An interface that specifies the operations that must be supported by a class
 * that provides object caching services for the <code>transform</code> framework.
 * Object caching allows the <code>transform</code> framework to maintain object
 * identity when transforming the same source object multiple times.
 * 
 * @author Kevin Fox
 * @since 0.2.0
 * 
 * @see org.simpliccity.sst.transform.Transformer
 *
 */
public interface TransformationCache 
{
	/**
	 * Adds a value of the specified type to the cache with the specified index.
	 * The type is determined by the target class of the transformation.  The
	 * index is generated from the source object.  A cached value added by
	 * this method is returned for the same type/index combination by the
	 * {@link #get(Class, Object)} method until {@link #flush()} is used
	 * to clear the cache.
	 * 
	 * @param type The source class of the object being transformed.
	 * @param index The index representing the source object being transformed.
	 * @param value The cached target object.
	 * @param <T> The class of the source object.
	 * 
	 * @see org.simpliccity.sst.transform.annotation.Transformation
	 */
	<T> void put(Class<T> type, Object index, Object value);
	
	/**
	 * Returns a value for the specified type/index combination from the
	 * cache.  For a value to be present in the cache, it must previously 
 	 * have been added by {@link #put(Class, Object, Object)} and {@link #flush()}
 	 * must not have been called in the interim.
 	 * 
	 * @param type The source class of the object being transformed.
	 * @param index The index representing the source object being transformed.
	 * @param <T> The class of the source object.
	 * @return The cached target object; <code>null</code> if none exists for the specified 
	 * type/index combination.
	 */
	<T> T get(Class<T> type, Object index);
	
	/**
	 * Clears all values from the object cache.
	 */
	void flush();
}
