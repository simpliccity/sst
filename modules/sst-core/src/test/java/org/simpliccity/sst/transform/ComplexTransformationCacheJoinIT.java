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

package org.simpliccity.sst.transform;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpliccity.sst.property.ValueHolder;
import org.simpliccity.sst.transform.annotation.TransformationCacheMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/ComplexTransformationCacheJoinIT.xml" })
public class ComplexTransformationCacheJoinIT 
{
	@Autowired
	private Transformer transformer;
	
	@Test
	public void testDefaultCacheMode()
	{
		Assert.assertEquals(TransformationCacheMode.JOIN, transformer.getCacheManager().getDefaultCacheModeEnum());
	}
	
	@Test
	public void testCachedIdenticalValue()
	{
		ValueHolder value = new ValueHolder();
		value.setValue("ABC");
		
		ComplexTransformationInput input1 = new ComplexTransformationInput();
		input1.setName("input1");
		input1.setValue(value);
		
		ComplexTransformationInput input2 = new ComplexTransformationInput();
		input2.setName("input2");
		input2.setValue(value);
		
		ComplexTransformationResult result1 = transformer.transform(input1, ComplexTransformationResult.class);
		ComplexTransformationResult result2 = transformer.transform(input2, ComplexTransformationResult.class);
		
		// Validate that transformations produced separate complex objects
		Assert.assertNotSame(result1, result2);
		
		// Validate that both complex objects have the same result value as the input value
		Assert.assertEquals(value.getValue(), result1.getResult().getResult());
		Assert.assertEquals(value.getValue(), result2.getResult().getResult());
		
		// Validate that the result objects of the complex objects are actually the same
		Assert.assertSame(result1.getResult(), result2.getResult());
	}
	
	@Test
	public void testFlushedCache()
	{
		ValueHolder value = new ValueHolder();
		value.setValue("ABC");
		
		ComplexTransformationInput input1 = new ComplexTransformationInput();
		input1.setName("input1");
		input1.setValue(value);
		
		ComplexTransformationInput input2 = new ComplexTransformationInput();
		input2.setName("input2");
		input2.setValue(value);
		
		ComplexTransformationResult result1 = transformer.transform(input1, ComplexTransformationResult.class, true);
		ComplexTransformationResult result2 = transformer.transform(input2, ComplexTransformationResult.class, true);
		
		// Validate that transformations produced separate complex objects
		Assert.assertNotSame(result1, result2);
		
		// Validate that both complex objects have the same result value as the input value
		Assert.assertEquals(value.getValue(), result1.getResult().getResult());
		Assert.assertEquals(value.getValue(), result2.getResult().getResult());
		
		// Validate that the result objects of the complex objects are actually different
		Assert.assertNotSame(result1.getResult(), result2.getResult());
	}
	
	@Test
	public void testIdentityIndex()
	{
		TransformationResult result = new TransformationResult();
		result.setResult("ABC");
		
		ComplexTransformationResult complex1 = new ComplexTransformationResult();
		complex1.setName("complex1");
		complex1.setResult(result);
		
		ComplexTransformationResult complex2 = new ComplexTransformationResult();
		complex2.setName("complex2");
		complex2.setResult(result);
		
		ComplexTransformationInput input1 = transformer.transform(complex1, ComplexTransformationInput.class);
		ComplexTransformationInput input2 = transformer.transform(complex2, ComplexTransformationInput.class);

		// Validate that transformations produced separate complex objects
		Assert.assertNotSame(input1, input2);
		
		// Validate that the complex objects have the same value object
		Assert.assertSame(input1.getValue(), input2.getValue());
	}
	
	@Test
	public void testJoinedTransformation()
	{
		TransformationResult result = new TransformationResult();
		result.setResult("ABC");
		
		ValueHolder value1 = transformer.transform(result, ValueHolder.class);
		ValueHolder value2 = transformer.transform(result, ValueHolder.class);
		
		// Validate that, through caching, the two transformations generated the same result object
		Assert.assertSame(value1, value2);
		
	}

	@Test
	public void testJoinedTransformationWithExplicitFlush()
	{
		TransformationResult result = new TransformationResult();
		result.setResult("ABC");
		
		ValueHolder value1 = transformer.transform(result, ValueHolder.class);
		ValueHolder value2 = transformer.transform(result, ValueHolder.class);
		
		// --- UserGuide cacheManagerFlush ---
		transformer.getCacheManager().flushCache();
		
		// --- UserGuide cacheManagerFlush ---
		
		ValueHolder value3 = transformer.transform(result, ValueHolder.class);
		
		// Validate that, through caching, the two transformations generated the same result object
		Assert.assertSame(value1, value2);
		
		// Validate that once the cache is flushed separate result objects are generated
		Assert.assertNotSame(value2, value3);
		
	}

	@Test
	public void testJoinedTransformationWithExplicitInlineFlush()
	{
		// --- UserGuide callTransform ---
		TransformationResult result = new TransformationResult();
		result.setResult("ABC");
		
		// -ughighlight-
		ValueHolder value1 = transformer.transform(result, ValueHolder.class);
		// -ughighlight-
		// --- UserGuide callTransform ---

		// --- UserGuide manualFlush ---
		ValueHolder value2 = transformer.transform(result, ValueHolder.class, true);
		
		// --- UserGuide manualFlush ---
		
		ValueHolder value3 = transformer.transform(result, ValueHolder.class);
		
		// Validate that, through caching, the two transformations generated the same result object
		Assert.assertSame(value1, value2);
		
		// Validate that once the cache is flushed separate result objects are generated
		Assert.assertNotSame(value2, value3);
	}

	@Test
	public void testFlushedDeepTransformation()
	{
		ValueHolder value = new ValueHolder();
		value.setValue("ABC");
		
		ComplexTransformationInput complex = new ComplexTransformationInput();
		complex.setName("complex");
		complex.setValue(value);
		
		DeepTransformationInput input1 = new DeepTransformationInput();
		input1.setName("input1");
		input1.setValue(value);
		input1.setInput(complex);
		
		DeepTransformationInput input2 = new DeepTransformationInput();
		input2.setName("input2");
		input2.setValue(value);
		input2.setInput(complex);
		
		DeepTransformationResult result1 = transformer.transform(input1, DeepTransformationResult.class);
		DeepTransformationResult result2 = transformer.transform(input2, DeepTransformationResult.class);
		
		// Validate that transformations produced separate complex objects
		Assert.assertNotSame(result1, result2);
		
		// Validate that shared value object preserved identity within deep transformation
		Assert.assertSame(result1.getResult(), result1.getComplex().getResult());
		Assert.assertSame(result2.getResult(), result2.getComplex().getResult());
		
		// Validate that the value objects are different across subsequent deep transformations
		Assert.assertNotSame(result1.getResult(), result2.getResult());
		
		// Validate that both complex objects have the same result value as the input value
		Assert.assertSame(value.getValue(), result1.getResult().getResult());
		Assert.assertSame(value.getValue(), result2.getResult().getResult());
	}

	@Test
	public void testUnbalancedTransformation()
	{
		String input = "UNBALANCED";
		
		ValueHolder value = transformer.transform(input, ValueHolder.class);
		
		Assert.assertEquals(input, value.getValue());
		
		String result = transformer.transform(value, String.class);
		
		Assert.assertNull(result);
	}

	@Test
	public void testNullTransformation()
	{
		ValueHolder result = transformer.transform(null, ValueHolder.class);
		
		Assert.assertNull(result);
	}
	
	@Test
	public void testBrokenTransformation()
	{
		BigDecimal bdResult = transformer.transform(new Integer(10), BigDecimal.class);
		Assert.assertNull("Invalid out transform", bdResult);
		
		Integer intResult = transformer.transform(BigDecimal.TEN, Integer.class);
		Assert.assertNull("Invalid in transform", intResult);
	}
	
	@Test
	public void testFailedTransformation()
	{
		BigDecimal bdResult = transformer.transform(new Double(10.0), BigDecimal.class);
		Assert.assertNull("Failing out transform", bdResult);
	}
}
