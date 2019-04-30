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

import org.springframework.stereotype.Component;

/**
 * The default {@link org.simpliccity.sst.transform.cache.TransformationCacheFactory}
 * used by {@link org.simpliccity.sst.transform.Transformer}.  An instance of this
 * factory is used if no other factory class is specified by the 
 * {@link org.simpliccity.sst.transform.Transformer} configuration.  This factory
 * returns an instance of {@link HashMapTransformationCache}.
 * 
 * @author Kevin Fox
 * @since 0.2.0
 * 
 * @see org.simpliccity.sst.transform.Transformer
 *
 */
@Component
public class DefaultTransformationCacheFactory implements TransformationCacheFactory 
{
	@Override
	public TransformationCache getNewCacheInstance() 
	{
		return new HashMapTransformationCache();
	}
}
