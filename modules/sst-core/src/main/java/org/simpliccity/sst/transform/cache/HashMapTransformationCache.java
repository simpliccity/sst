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

import java.util.HashMap;
import java.util.Map;

/**
 * A simple implementation of {@link org.simpliccity.sst.transform.cache.TransformationCache}
 * that uses a {@link java.util.HashMap} to store cached objects.  This is the default
 * cache implementation used by {@link org.simpliccity.sst.transform.Transformer}, unless
 * otherwise configured.
 * 
 * @author Kevin Fox
 * @since 0.2.0
 * 
 * @see DefaultTransformationCacheFactory
 *
 */
public class HashMapTransformationCache implements TransformationCache 
{
	@SuppressWarnings("rawtypes")
	private Map<Class, Map<Object, Object>> typeMap = new HashMap<>();

	@Override
	public <T> void put(Class<T> type, Object index, Object value) 
	{
		Map<Object, Object> typeCache = typeMap.get(type);
		
		if (typeCache == null)
		{
			typeCache = new HashMap<>();
			typeMap.put(type, typeCache);
		}
		
		typeCache.put(index, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> type, Object index) 
	{
		T result = null;
		
		Map<Object, Object> typeCache = typeMap.get(type);
		
		if (typeCache != null)
		{
			result = (T) typeCache.get(index);
		}
		
		return result;
	}

	@Override
	public void flush() 
	{
		typeMap.clear();
	}
}
