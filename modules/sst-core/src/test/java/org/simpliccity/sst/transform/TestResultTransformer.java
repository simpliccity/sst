package org.simpliccity.sst.transform;

import org.simpliccity.sst.property.ValueHolder;
import org.simpliccity.sst.transform.annotation.OutTransform;
import org.simpliccity.sst.transform.annotation.Transformation;
import org.simpliccity.sst.transform.annotation.TransformationCacheMode;
import org.simpliccity.sst.transform.annotation.TransformationType;

@Transformation(source=TransformationResult.class, target=ValueHolder.class, direction=TransformationType.OUT, cache=TransformationCacheMode.JOIN)
public class TestResultTransformer 
{
	@OutTransform
	public ValueHolder buildValueHolder(TransformationResult input)
	{
		ValueHolder result = new ValueHolder();
		result.setValue(input.getResult());
		
		return result;
	}
}
