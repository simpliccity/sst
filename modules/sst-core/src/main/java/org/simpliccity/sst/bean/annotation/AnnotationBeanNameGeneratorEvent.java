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

package org.simpliccity.sst.bean.annotation;

import org.simpliccity.sst.bean.BeanNameGeneratorEvent;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.type.AnnotationMetadata;

/**
 * A {@link org.simpliccity.sst.bean.BeanNameGeneratorEvent} triggered when a name is 
 * generated for a bean through Spring annotation scanning by a custom 
 * {@link org.springframework.beans.factory.support.BeanNameGenerator}.
 * In addition to the basic information captured by a {@link org.simpliccity.sst.bean.BeanNameGeneratorEvent},
 * this subclass includes information on the annotation that caused the bean to be loaded.
 * 
 * @author Kevin Fox
 *
 */
public class AnnotationBeanNameGeneratorEvent extends BeanNameGeneratorEvent 
{
	private AnnotationMetadata annotation;

	/**
	 * Constructs a new event with all necessary information.
	 * 
	 * @param beanDefinition Newly named Spring component.
	 * @param beanName Name given to the bean.
	 * @param annotation Metadata for annotation that caused Spring to load the bean.
	 */
	public AnnotationBeanNameGeneratorEvent(BeanDefinition beanDefinition, String beanName, AnnotationMetadata annotation)
	{
		super(beanDefinition, beanName);
		setAnnotation(annotation);
	}
	
	/**
	 * Specifies the annotation that caused Spring to load the bean.
	 * 
	 * @param annotation The annotation Spring recognized as indicating a bean component.
	 */
	public final void setAnnotation(AnnotationMetadata annotation) 
	{
		this.annotation = annotation;
	}

	/**
	 * Returns the annotation that caused Spring to load the bean.
	 * 
	 * @return The annotation Spring recognized as indicating a bean component.
	 */
	public final AnnotationMetadata getAnnotation() 
	{
		return annotation;
	}
}
