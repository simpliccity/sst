/*
 *    Copyright 2010 Information Control Corporation
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

package org.simpliccity.sst.flow.validation;

import org.springframework.binding.validation.ValidationContext;

/**
 * <p>A class implementing this interface serves to adapt a specific data validation
 * framework for use with the Spring Web Flow convention for model validation.  Custom
 * validation classes defined for use with a web flow can delegate all or a portion of
 * the actual validation logic to the third-party framework through the use of the adapter
 * class.</p>
 * 
 * <p>The adapter class is responsible for triggering the desired validation logic using
 * the specific validation framework.  It must also return the output of the validation
 * process on the Spring {@link org.springframework.binding.validation.ValidationContext}
 * for use by the web flow and view.  Note that a single adapter class should be able to
 * perform the validation for any desired model class.</p>
 * 
 * @author Kevin Fox
 *
 */
public interface ValidatorAdapter 
{
	/**
	 * Performs validation of a specified model object using a third-party validation framework.
	 * All validation logic included in the specified validation groups is applied.  Results of the 
	 * validation are reported in the {@link org.springframework.binding.validation.ValidationContext},
	 * generally through the associated {@link org.springframework.binding.message.MessageContext}.
	 * When reporting validation failures, the path to the affected property will begin with the
	 * specified prefix.
	 * 
	 * @param prefix Path prefix attached to properties failing validation.
	 * @param modelObject The model object to validate.  Potentially the root of an object graph
	 * for validation, if the framework support transitive validation.
	 * @param context The Spring validation context through which validation results are reported.
	 * @param groups A variable list of the groups of validation logic to perform.  The meaning of
	 * these groups is dependent on the underlying validation framework.  If no groups are specified,
	 * the default validation logic should be applied.
	 * @param <T> The type of the object to validate.
	 */
	<T> void performValidation(String prefix, T modelObject, ValidationContext context, Class<?>... groups);

	/**
	 * Performs validation of a specified model object using a third-party validation framework.
	 * All validation logic included in the specified validation groups is applied.  Results of the 
	 * validation are reported in the {@link org.springframework.binding.validation.ValidationContext},
	 * generally through the associated {@link org.springframework.binding.message.MessageContext}.
	 * 
	 * @param modelObject The model object to validate.  Potentially the root of an object graph
	 * for validation, if the framework support transitive validation.
	 * @param context The Spring validation context through which validation results are reported.
	 * @param groups A variable list of the groups of validation logic to perform.  The meaning of
	 * these groups is dependent on the underlying validation framework.  If no groups are specified,
	 * the default validation logic should be applied.
	 * @param <T> The type of the object to validate.
	 */
	<T> void performValidation(T modelObject, ValidationContext context, Class<?>... groups);
}
