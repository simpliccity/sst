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

package org.simpliccity.sst.bean.lookup.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * <p>Defines a Spring bean that can be programmatically retrieved using the metadata lookup framework.
 * This annotation provides a generic mechanism to associate distinguishing metadata with various
 * implementations of the same basic type.  Clients can then use the metadata lookup framework
 * to programmatically retrieve the desired implementation.</p>
 * 
 * <p>Note that this annotation serves as an alternative to creating a custom annotation to represent
 * a well-defined set of metadata.
 * {@link org.simpliccity.sst.bean.lookup.BeanMetadataLookupStrategy#getTypedBean(org.springframework.beans.factory.BeanFactory, org.simpliccity.sst.bean.lookup.BeanMetadataLookupContainer)}
 * will return beans marked with this annotation.</p>
 * 
 * @author Kevin Fox
 * @since 1.0.0
 *
 * @see org.simpliccity.sst.bean.lookup.BeanMetadataLookupManager
 * @see org.simpliccity.sst.bean.lookup.BeanMetadataLookupStrategy
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Component
public @interface DynamicTypedComponent 
{
	/**
	 * The Java type with which the corresponding bean will be associated.
	 * 
	 * @return The type of the marked bean.
	 */
	Class<?> type();
	
	/**
	 * The metadata attributes associated with the corresponding bean.  These
	 * attributes are used to distinguish between different implementations of
	 * the specified type.
	 * 
	 * @return The metadata attributes for the marked bean.
	 */
	ComponentMetadata[] metadata();
}
