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

package org.simpliccity.sst.service.security.annotation;

import java.util.Map;

import org.simpliccity.sst.bean.lookup.AbstractSimpleAnnotationBeanMetadataLookupContainer;
import org.springframework.core.type.AnnotationMetadata;

/**
 * A wrapper class encapsulating the metadata values used to lookup a {@link ServiceAccessValidator} component
 * from the application context.
 * 
 * @author Kevin Fox
 * @since 0.3.0
 *
 */
public class ServiceAccessValidatorBeanMetadataLookupContainer extends	AbstractSimpleAnnotationBeanMetadataLookupContainer<ServiceAccessValidator> 
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates an instance of ServiceAccessValidatorBeanMetadataLookupContainer without any metadata values.
	 */
	public ServiceAccessValidatorBeanMetadataLookupContainer()
	{
		super(ServiceAccessValidator.class);
	}
	
	/**
	 * Creates an instance of ServiceAccessValidatorBeanMetadataLookupContainer using metadata values extracted
	 * from the specified annotation.
	 * 
	 * @param validator The {@link ServiceAccessValidator} annotation from which to extract metadata values.
	 */
	public ServiceAccessValidatorBeanMetadataLookupContainer(ServiceAccessValidator validator)
	{
		super(ServiceAccessValidator.class);

		setValue(validator.value());
	}

	/**
	 * Creates an instance of ServiceAccessValidatorBeanMetadataLookupContainer using the Spring annotation metadata.
	 * 
	 * @param annotation The Spring metadata for a {@link ServiceAccessValidator} annotation.
	 */
	public ServiceAccessValidatorBeanMetadataLookupContainer(AnnotationMetadata annotation)
	{
		super(ServiceAccessValidator.class);
		
		// Get the metadata for the Transformation annotation, using class names rather than classes
		Map<String, Object> attributes = annotation.getAnnotationAttributes(ServiceAccessValidator.class.getName(), true);
		
		setValue((String) attributes.get(KEY_ATTRIBUTE_VALUE));
	}
}
