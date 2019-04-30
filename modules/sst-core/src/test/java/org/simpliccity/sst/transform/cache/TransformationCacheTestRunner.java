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

@SuppressWarnings("rawtypes")
public class TransformationCacheTestRunner implements Runnable 
{
	private TransformationCacheManager manager;
	private boolean globalValueFound;
	private boolean localValueFound;
	private Class cacheClass;
	private Object cacheIndex;
	private Object globalValue;
	
	public TransformationCacheTestRunner(TransformationCacheManager manager, Class cacheClass, Object cacheIndex, Object globalValue)
	{
		this.manager = manager;
		this.cacheClass = cacheClass;
		this.cacheIndex = cacheIndex;
		this.globalValue = globalValue;
	}

	@Override
	public void run() 
	{
		Object result = manager.retrieveFromCache(cacheClass, cacheIndex);
		setGlobalValueFound(result == globalValue);
		
		ValueHolder input = new TestCacheUpdater().updateCache(manager);
		ValueHolder output = new TestCacheRetriever().retrieveCachedValue(manager);
		setLocalValueFound(input == output);
	}

	public boolean isGlobalValueFound() 
	{
		return globalValueFound;
	}

	public void setGlobalValueFound(boolean globalValueFound) 
	{
		this.globalValueFound = globalValueFound;
	}

	public boolean isLocalValueFound() 
	{
		return localValueFound;
	}

	public void setLocalValueFound(boolean localValueFound) 
	{
		this.localValueFound = localValueFound;
	}
}
