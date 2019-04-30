/*
 *    Copyright 2014 Information Control Corporation
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

package org.simpliccity.sst.transform.config.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpliccity.sst.transform.Transformer;
import org.simpliccity.sst.transform.annotation.TransformationCacheMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TransformerDefaultConfig.class)
@ActiveProfiles("transformerDefault")
public class TransformerConfigAnnotationDefaultsIT 
{
	@Autowired
	private Transformer transformer;

	@Test
	public void testDefaultCacheMode()
	{
		assertEquals("Default cache mode", TransformationCacheMode.NONE, transformer.getCacheManager().getDefaultCacheModeEnum());
	}
	
	@Test
	public void testDefaultDisableCache()
	{
		assertFalse("Disable caching", transformer.getCacheManager().isDisableCache());
	}
	
	@Test
	public void testDefaultGlobalCacheScope()
	{
		assertFalse("Global cache scope", transformer.getCacheManager().isGlobalCacheScope());
	}
	
	@Test
	public void testDefaultCacheFactory()
	{
		assertNull("Cache factory", transformer.getCacheManager().getCacheFactory());
	}
	
	@Test
	public void testDefaultSurfaceExceptions()
	{
		assertFalse("Surface exceptions", transformer.isSurfaceExceptions());
	}
}
