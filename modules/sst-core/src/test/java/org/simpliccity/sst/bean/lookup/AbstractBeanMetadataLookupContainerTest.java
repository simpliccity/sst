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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.simpliccity.sst.transform.annotation.Transformation;

public class AbstractBeanMetadataLookupContainerTest 
{

	/**
	 * Tests that protected constructor correctly sets the container discriminator.
	 */
	@Test
	public void testConstructor()
	{
		AbstractBeanMetadataLookupContainer<Transformation> container = new TestBeanMetadataLookupContainer<Transformation>(Transformation.class);
		
		assertEquals("Discriminator should match constructor argument", Transformation.class, container.getLookupDiscriminator());
	}
	
	/**
	 * Tests that a discriminator class that is an annotation is correctly recognized.
	 */
	@Test
	public void testAnnotation()
	{
		AbstractBeanMetadataLookupContainer<Transformation> container = new TestBeanMetadataLookupContainer<Transformation>(Transformation.class);

		assertTrue("Container should represent annotated lookup", container.isAnnotatedLookup());
		assertFalse("Container should not represent typed lookup", container.isTypedLookup());
	}
	
	/**
	 * Tests that a discriminator class that is typed (i.e. not an annotation) is correctly recognized.
	 */
	@Test
	public void testTyped()
	{
		AbstractBeanMetadataLookupContainer<String> container = new TestBeanMetadataLookupContainer<String>(String.class);
		
		assertTrue("Container should not represent annotated lookup", container.isTypedLookup());
		assertFalse("Container should represent typed lookup", container.isAnnotatedLookup());
	}
	
	public class TestBeanMetadataLookupContainer<T> extends AbstractBeanMetadataLookupContainer<T>
	{
		protected TestBeanMetadataLookupContainer(Class<T> discriminator) 
		{
			super(discriminator);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public Map<String, Object> getMetadataAttributes() 
		{
			return null;
		}		
	}
}

