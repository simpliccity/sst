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
 * An interface that specifies the operation used to create an instance of a class
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
public interface TransformationCacheFactory 
{
	/**
	 * <p>Returns an instance of a particular implementation of {@link TransformationCache}
	 * for use by the <code>transform</code> framework.</p>
	 * 
	 * <p><b>Note</b> that the {@link org.simpliccity.sst.transform.Transformer} class supports
	 * both thread-local and global caching, depending on the configuration options selected.
	 * In order to avoid any unexpected behavior of the object cache for 
	 * {@link org.simpliccity.sst.transform.Transformer}, each call to this method should
	 * return a separate instance of {@link TransformationCache}.</p>
	 * 
	 * @return A new instance of {@link TransformationCache} for use by {@link org.simpliccity.sst.transform.Transformer}.
	 */
	TransformationCache getNewCacheInstance();
}
