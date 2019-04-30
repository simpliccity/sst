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

package org.simpliccity.sst.service.security.annotation;

import org.simpliccity.sst.bean.lookup.BeanMetadataLookupContainer;
import org.simpliccity.sst.bean.lookup.namespace.AbstractLookupAnnotationNamespaceBeanNameGenerator;
import org.springframework.core.type.AnnotationMetadata;

/**
 * <p>Generates bean names for classes annotated with {@link ServiceAccessValidator}.  Follows a naming convention
 * specified by {@link #getNamespaceNamePattern()}.</p>
 * 
 * <p>Specifically for use in conjunction with {@link org.simpliccity.sst.bean.annotation.DelegateAnnotationBeanNameGenerator}
 * to support Spring annotation scanning.  The mapping between {@link ServiceAccessValidator} and this class must
 * be established according to the requirements of the concrete subclass of 
 * {@link org.simpliccity.sst.bean.annotation.DelegateAnnotationBeanNameGenerator} that has been specified
 * in the configuration for Spring annotation scanning.</p>
 * 
 * @author Kevin Fox
 * 
 * @see org.simpliccity.sst.ws.endpoint.security.EndpointAccessValidatorInterceptor
 *
 */
public class ServiceContentExtractorAnnotationBeanNameGenerator extends AbstractLookupAnnotationNamespaceBeanNameGenerator
{
	/**
	 * Namespace name pattern ({@value}).
	 */
	public static final String NAME_PATTERN = "sst.serviceContentExtractor.<{0}>";
		
	@Override
	protected String getNamespaceNamePattern() 
	{
		return NAME_PATTERN;
	}
	
	@Override
	protected String getAnnotationClassName()
	{
		return ServiceContentExtractor.class.getName();
	}

	@Override
	protected String[] getNameComponentsFromAnnotationMetadata(AnnotationMetadata annotation)
	{
		ServiceContentExtractorBeanMetadataLookupContainer container = new ServiceContentExtractorBeanMetadataLookupContainer(annotation);
		
		return getNameComponentsFromMetadata(container);
	}
	
	@Override
	protected <T> String[] getNameComponentsFromMetadata(BeanMetadataLookupContainer<T> container)
	{
		ServiceContentExtractorBeanMetadataLookupContainer metadata = (ServiceContentExtractorBeanMetadataLookupContainer) container;
		
		String value = metadata.getValue();

		return new String[] {value};
	}
}
