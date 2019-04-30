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

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Path.Node;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;

/**
 * <p>An adapter that allows the use of a JSR-303 Java Bean validation framework implementation
 * for Spring Web Flow model validation.  It relies on Spring support for the javax.validation
 * framework through the {@link org.springframework.validation.beanvalidation.LocalValidatorFactoryBean}.
 * The javax.validation bootstrap rules are used to determine the underlying framework implementation used.</p>
 * 
 * @author Kevin Fox
 *
 */
public class JavaBeanValidatorAdapter implements ValidatorAdapter
{
	@Autowired
	private Validator validator;
	
	/**
	 * Returns the @{link javax.validation.Validator} used by this class.
	 * 
	 * @return The @{link javax.validation.Validator} used by this class.
	 */
	protected Validator getValidator() 
	{
		return validator;
	}

	/**
	 * Specifies the @{link javax.validation.Validator} used by this class.
	 * 
	 * @param validator The @{link javax.validation.Validator} used by this class.
	 */
	protected void setValidator(Validator validator) 
	{
		this.validator = validator;
	}

	@Override
	public <T> void performValidation(String prefix, T modelObject, ValidationContext context, Class<?>... groups)
	{
		MessageContext messageContext = context.getMessageContext();
		Set<ConstraintViolation<T>> constraintViolations = getValidator().validate(modelObject, groups);
		for (ConstraintViolation<T> violation : constraintViolations)
		{
			String keyString = getKey(prefix, violation);
			messageContext.addMessage(new MessageBuilder().error().source(keyString).defaultText(violation.getMessage()).build());
		}
	}

	@Override
	public <T> void performValidation(T modelObject, ValidationContext context, Class<?>... groups)
	{
		performValidation("", modelObject, context, groups);
	}
	
	private <T> String getKey(String prefix, ConstraintViolation<T> violation)
	{
		Path path = violation.getPropertyPath();	
		Iterator<Node> itr = path.iterator();
		StringBuilder keyString = new StringBuilder(prefix);
		String tempString;
		
		while(itr.hasNext())
		{
			tempString = itr.next().getName();
			if(tempString!=null && !tempString.isEmpty())
			{
				if(keyString.length() > 0 )
				{
					keyString.append(".");
				}
				keyString.append(tempString);
			}
		}
		return keyString.toString();
	}
}
