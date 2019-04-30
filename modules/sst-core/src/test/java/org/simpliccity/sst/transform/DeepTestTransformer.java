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

import static org.simpliccity.sst.transform.annotation.TransformationCacheMode.FLUSH;
import static org.simpliccity.sst.transform.annotation.TransformationType.BIDIRECTIONAL;

import org.simpliccity.sst.property.ValueHolder;
import org.simpliccity.sst.transform.annotation.InTransform;
import org.simpliccity.sst.transform.annotation.OutTransform;
import org.simpliccity.sst.transform.annotation.Transformation;
import org.springframework.beans.factory.annotation.Autowired;

// --- UserGuide deepTransformRoot ---
@Transformation(source=DeepTransformationInput.class, target=DeepTransformationResult.class, direction=BIDIRECTIONAL, cache=FLUSH)

// --- UserGuide deepTransformRoot ---
public class DeepTestTransformer 
{
	@Autowired(required=false)
	private Transformer transformer;

	@OutTransform
	public DeepTransformationResult buildResult(DeepTransformationInput input)
	{
		DeepTransformationResult result = new DeepTransformationResult();
		
		result.setName(input.getName());
		result.setComplex(transformer.transform(input.getInput(), ComplexTransformationResult.class));
		result.setResult(transformer.transform(input.getValue(), TransformationResult.class));
		
		return result;
	}
	
	@InTransform
	public DeepTransformationInput buildInput(DeepTransformationResult result)
	{		
		DeepTransformationInput input = new DeepTransformationInput();
		
		input.setName(result.getName());
		input.setInput(transformer.transform(result.getComplex(), ComplexTransformationInput.class));
		input.setValue(transformer.transform(result.getResult(), ValueHolder.class));
		
		return input;
	}
}
