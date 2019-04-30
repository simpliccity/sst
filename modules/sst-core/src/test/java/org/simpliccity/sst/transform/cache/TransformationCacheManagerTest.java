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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TransformationCacheManagerTest 
{
	private TransformationCacheManager manager;
	
	@Before
	public void initializeCacheManager()
	{
		manager = new TransformationCacheManager();
		manager.setCacheFactory(new DefaultTransformationCacheFactory());
	}
	
	@Test
	public void testCacheMode() throws Exception
	{
		manager.setDefaultCacheMode("JOIN");
		manager.afterPropertiesSet();
		
		Assert.assertTrue("Default cache mode", "JOIN".equals(manager.getDefaultCacheMode()));
	}
	
	@Test
	public void testCacheFactory() throws Exception
	{
		manager.afterPropertiesSet();
		
		Assert.assertTrue("Default cache factory", manager.getCacheFactory() instanceof DefaultTransformationCacheFactory);
	}
	
	@Test
	public void testDisableCache() throws Exception
	{
		manager.setDisableCache(true);
		manager.afterPropertiesSet();
		
		Assert.assertTrue("Cache disabled", manager.isDisableCache());
		
		manager.updateCache(String.class, "Hello", "World");
		
		Assert.assertNull("No cached value when disabled", manager.retrieveFromCache(String.class, "Hello"));
		
		manager.flushCache();
		
		Assert.assertNull("Flush cache when disabled", manager.retrieveFromCache(String.class, "Hello"));		
	}
	
	@Test
	public void testGlobalCache() throws Exception
	{
		manager.setGlobalCacheScope(true);
		manager.afterPropertiesSet();
		
		Assert.assertTrue("Global cache", manager.isGlobalCacheScope());
		
		manager.updateCache(String.class, "Hello", "World");
		TransformationCacheTestRunner runner = new TransformationCacheTestRunner(manager, String.class, "Hello", "World");
		Thread testThread = new Thread(runner);
		testThread.start();
		testThread.join();
		
		Assert.assertTrue("Global value found", runner.isGlobalValueFound());
		Assert.assertTrue("Local value found", runner.isLocalValueFound());
		
		manager.flushCache();
		
		Assert.assertNull("Flush global cache", manager.retrieveFromCache(String.class, "Hello"));		
	}
	
	@Test
	public void testLocalCache() throws Exception
	{
		manager.setGlobalCacheScope(false);
		manager.afterPropertiesSet();
		
		Assert.assertFalse("Global cache", manager.isGlobalCacheScope());
		
		manager.updateCache(String.class, "Hello", "World");
		TransformationCacheTestRunner runner = new TransformationCacheTestRunner(manager, String.class, "Hello", "World");
		Thread testThread = new Thread(runner);
		testThread.start();
		testThread.join();
		
		Assert.assertFalse("Global value not found", runner.isGlobalValueFound());
		Assert.assertTrue("Local value found", runner.isLocalValueFound());
		
		manager.flushCache();
		
		Assert.assertNull("Flush local cache", manager.retrieveFromCache(String.class, "Hello"));		
	}
}
