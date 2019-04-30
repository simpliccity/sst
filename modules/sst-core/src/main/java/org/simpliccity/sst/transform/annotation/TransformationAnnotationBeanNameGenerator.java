/*
 *    Copyright 2009 Information Control Corporation
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

package org.simpliccity.sst.transform.annotation;

import org.simpliccity.sst.bean.lookup.BeanMetadataLookupContainer;
import org.simpliccity.sst.bean.lookup.namespace.AbstractLookupAnnotationNamespaceBeanNameGenerator;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.type.AnnotationMetadata;

/**
 * <p>Generates bean names in the <code>transformer</code> namespace for classes annotated with {@link Transformation}.
 * Also provides support for locating {@link Transformation} beans using the <code>namespace</code> bean metadata
 * lookup strategy.</p>
 * 
 * <p>This class will automatically be employed to name beans annotated with {@link Transformation} when Spring
 * annotation scanning is configured to use the {@link org.simpliccity.sst.bean.annotation.PropertiesDelegateAnnotationBeanNameGenerator}
 * bean name generator.</p>
 * 
 * @author Kevin Fox
 * 
 * @see org.simpliccity.sst.transform.Transformer
 *
 */
// --- UserGuide lookupNameGenerator ---
public class TransformationAnnotationBeanNameGenerator extends AbstractLookupAnnotationNamespaceBeanNameGenerator
{
	// --- UserGuide lookupNameGenerator ---
	// --- UserGuide namePattern ---
	/**
	 * Namespace name pattern ({@value}).
	 */
	public static final String NAME_PATTERN = "sst.transformer.<{0}>.<{1}>";
	
	// --- UserGuide namePattern ---
	
	// --- UserGuide namePattern ---
	@Override
	protected String getNamespaceNamePattern() 
	{
		return NAME_PATTERN;
	}
	// --- UserGuide namePattern ---

	// --- UserGuide isSupported ---
	@Override
	protected boolean isSupported(BeanDefinition definition)
	{
		AnnotationMetadata metadata = getAnnotation(definition);
		
		String[] nameComponents = getNameComponentsFromAnnotationMetadata(metadata);
		
		// Be sure that both source and target are populated
		return nameComponents.length == 2 && nameComponents[0] != null && nameComponents[1] != null;
	}
	// --- UserGuide isSupported ---

	// --- UserGuide nameComponents ---
	@Override
	protected String[] getNameComponentsFromAnnotationMetadata(AnnotationMetadata annotation)
	{
		TransformationBeanMetadataLookupContainer container = new TransformationBeanMetadataLookupContainer(annotation);
		
		return getNameComponentsFromMetadata(container);
	}

	@Override
	protected <T> String[] getNameComponentsFromMetadata(BeanMetadataLookupContainer<T> container)
	{
		TransformationBeanMetadataLookupContainer metadata = (TransformationBeanMetadataLookupContainer) container;
		
		String sourceName = metadata.getSource();
		String targetName = metadata.getTarget();

		return new String[] {sourceName, targetName};
	}
	// --- UserGuide nameComponents ---

	// --- UserGuide annotationClass ---
	@Override
	protected String getAnnotationClassName()
	{
		return Transformation.class.getName();
	}	
	// --- UserGuide annotationClass ---
}
