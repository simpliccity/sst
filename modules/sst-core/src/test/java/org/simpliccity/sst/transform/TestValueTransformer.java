/*
 *    Copyright 2011 Information Control Corporation
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

import static org.simpliccity.sst.transform.annotation.TransformationType.OUT;

import org.simpliccity.sst.property.ValueHolder;
import org.simpliccity.sst.transform.annotation.OutTransform;
import org.simpliccity.sst.transform.annotation.Transformation;
import org.simpliccity.sst.transform.cache.annotation.OutTransformCacheIndex;

// --- UserGuide deepTransformLeaf ---
@Transformation(source=ValueHolder.class, target=TransformationResult.class, direction=OUT)

// --- UserGuide deepTransformLeaf ---
public class TestValueTransformer 
{
	@OutTransform
	public TransformationResult buildTransformationResult(ValueHolder input)
	{
		TransformationResult result = new TransformationResult();
		result.setResult(input.getValue());
		
		return result;
	}
	
	@OutTransformCacheIndex
	public String getInputValue(ValueHolder input)
	{
		return input.getValue();
	}
}
