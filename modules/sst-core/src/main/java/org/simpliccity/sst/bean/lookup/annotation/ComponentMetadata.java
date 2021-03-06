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

/**
 * Specifies a metadata attribute associated with a {@link DynamicTypedComponent}.
 * 
 * @author Kevin Fox
 * @since 1.0.0
 *
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface ComponentMetadata 
{
	/**
	 * Specifies the name of the metadata attribute.
	 * 
	 * @return The attribute name.
	 */
	String name();
	
	/**
	 * Specifies the value of the metadata attribute.
	 * 
	 * @return The attribute value.
	 */
	String value();
}
