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

package org.simpliccity.sst.bean.annotation;

import java.util.Map;

import org.simpliccity.sst.bean.BeanNameGeneratorListener;
import org.simpliccity.sst.bean.namespace.AbstractAnnotationNamespaceBeanNameGenerator;
import org.springframework.core.type.AnnotationMetadata;

public class TestParticipantAnnotationBeanNameGenerator extends	AbstractAnnotationNamespaceBeanNameGenerator 
{
	public static final String NAME_PATTERN = "sst.testParticipant.<{0}>";
	
	private BeanNameGeneratorListener[] listeners = {new BeanCounterListener(), new BeanNameTrackerListener()};
	
	@Override
	protected String[] getNameComponentsFromAnnotationMetadata(AnnotationMetadata annotation) 
	{
		Map<String, Object> attributes = annotation.getAnnotationAttributes(TestParticipant.class.getName(), true);
		
		String[] result = {(String) attributes.get("name")};

		return result;
	}

	@Override
	protected String getAnnotationClassName() 
	{
		return TestParticipant.class.getName();
	}

	@Override
	protected String getNamespaceNamePattern() 
	{
		return NAME_PATTERN;
	}

	@Override
	public BeanNameGeneratorListener[] getListeners() 
	{
		return listeners;
	}
}
