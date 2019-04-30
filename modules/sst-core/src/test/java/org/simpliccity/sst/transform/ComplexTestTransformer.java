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

import org.simpliccity.sst.property.ValueHolder;
import org.simpliccity.sst.transform.annotation.InTransform;
import org.simpliccity.sst.transform.annotation.OutTransform;
import org.simpliccity.sst.transform.annotation.Transformation;
import org.simpliccity.sst.transform.annotation.TransformationType;
import org.springframework.beans.factory.annotation.Autowired;

// --- UserGuide transformation ---
// -ughighlight-
@Transformation(source=ComplexTransformationInput.class, target=ComplexTransformationResult.class, direction=TransformationType.BIDIRECTIONAL)
// -ughighlight-
public class ComplexTestTransformer 
{
// --- UserGuide transformation ---
	@Autowired(required=false)
	private Transformer transformer;

	// --- UserGuide outTransform ---
	// -ughighlight-
	@OutTransform
	// -ughighlight-
	public ComplexTransformationResult buildResult(ComplexTransformationInput input)
	{
	// --- UserGuide outTransform ---
		
		// --- UserGuide recursiveTransform ---
		ComplexTransformationResult result = new ComplexTransformationResult();
		
		result.setName(input.getName());
		result.setResult(/* -h1- */transformer.transform(input.getValue(), TransformationResult.class)/* -h1- */);
		// --- UserGuide recursiveTransform ---
		
		return result;
	}
	
	// --- UserGuide inTransform ---
	// -ughighlight-
	@InTransform
	// -ughighlight-
	public ComplexTransformationInput buildInput(ComplexTransformationResult result)
	{
	// --- UserGuide inTransform ---
		
		ComplexTransformationInput input = new ComplexTransformationInput();
		
		input.setName(result.getName());
		input.setValue(transformer.transform(result.getResult(), ValueHolder.class));
		
		return input;
	}
}
