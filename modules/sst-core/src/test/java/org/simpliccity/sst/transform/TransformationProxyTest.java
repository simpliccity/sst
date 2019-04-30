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

package org.simpliccity.sst.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.simpliccity.sst.bean.annotation.BeanAnnotationUtils;
import org.simpliccity.sst.transform.annotation.Transformation;
import org.simpliccity.sst.transform.annotation.TransformationCacheMode;
import org.simpliccity.sst.transform.cache.TransformationCacheManager;

@RunWith(MockitoJUnitRunner.class)
public class TransformationProxyTest 
{
	@Mock private TransformationCacheManager cacheManager;
	
	@Test
	public void testGetCacheManager()
	{
		TestResultTransformer transformer = new TestResultTransformer();
		TransformationProxy proxy = new TransformationProxy(cacheManager, transformer, false);
		
		assertEquals("Cache manager value", cacheManager, proxy.getCacheManager());
	}
	
	@Test
	public void testGetTransformer()
	{
		TestResultTransformer transformer = new TestResultTransformer();
		TransformationProxy proxy = new TransformationProxy(cacheManager, transformer, false);
		
		assertEquals("Transformer value", transformer, proxy.getTransformer());
	}
	
	@Test
	public void testGetConfiguration()
	{
		TestResultTransformer transformer = new TestResultTransformer();
		TransformationProxy proxy = new TransformationProxy(cacheManager, transformer, false);
		
		Transformation annotation = BeanAnnotationUtils.getAnnotationFromInstance(Transformation.class, transformer);
		
		assertEquals("Configuration value", annotation, proxy.getConfiguration());		
	}
	
	@Test
	public void testGetTransformerMethods()
	{
		TestValueTransformer transformer = new TestValueTransformer();
		TransformationProxy proxy = new TransformationProxy(cacheManager, transformer, false);
		
		assertEquals("Outbound transform method", "buildTransformationResult", proxy.getOutboundTransformationMethod().getName());
		assertEquals("Outbound cache index method", "getInputValue", proxy.getOutboundCacheIndexMethod().getName());
		assertNull("Inbound transform method", proxy.getInboundTransformationMethod());
		assertNull("Inbound cache index method", proxy.getInboundCacheIndexMethod());
	}
	
	@Test
	public void testGetEffectiveCacheMode()
	{
		TestResultTransformer transformer1 = new TestResultTransformer();
		TransformationProxy proxy1 = new TransformationProxy(cacheManager, transformer1, false);
		assertEquals("Explicit cache mode", TransformationCacheMode.JOIN, proxy1.getEffectiveCacheMode());
		
		TestValueTransformer transformer2 = new TestValueTransformer();
		TransformationProxy proxy2 = new TransformationProxy(cacheManager, transformer2, false);
		assertEquals("Default cache mode", cacheManager.getDefaultCacheModeEnum(), proxy2.getEffectiveCacheMode());
	}
}
