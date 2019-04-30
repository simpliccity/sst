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

package org.simpliccity.sst.transform.cache;

import org.simpliccity.sst.property.ValueHolder;

public class TestCacheUpdater 
{
	public static final Class<ValueHolder> CACHE_CLASS = ValueHolder.class;
	public static final String CACHE_INDEX = "Test";

	public ValueHolder updateCache(TransformationCacheManager cache)
	{
		ValueHolder result = new ValueHolder();
		result.setValue(CACHE_INDEX);
		
		cache.updateCache(CACHE_CLASS, CACHE_INDEX, result);
		
		return result;
	}
}
