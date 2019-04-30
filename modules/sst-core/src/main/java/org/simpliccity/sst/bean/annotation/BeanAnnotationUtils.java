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

package org.simpliccity.sst.bean.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Utility functions for working with annotations on Spring beans.  These supplement
 * the functionality of {@link org.springframework.core.annotation.AnnotationUtils}.
 * 
 * @author Kevin Fox
 * @since 0.3.0
 *
 */
public final class BeanAnnotationUtils 
{
	// Private constructor to hide default
	private BeanAnnotationUtils()
	{
		super();
	}
	
	/**
	 * Retrieves the specified type-level annotation, if any, from the bean instance.
	 * 
	 * @param annotationType The type of annotation to retrieve.
	 * @param instance The bean instance to examine for an annotation of the specified type.
	 * @param <A> The annotation class.
	 * @return The annotation instance of the specified type applied to the bean instance.
	 */
	public static <A extends Annotation> A getAnnotationFromInstance(Class<A> annotationType, Object instance)
	{
		return instance.getClass().getAnnotation(annotationType);		
	}

	/**
	 * Retrieves the first method on the bean instance marked with a method-level annotation of the specified type.
	 * 
	 * @param methodAnnotationClass The type of annotation used to mark the method.
	 * @param instance The bean instance to examine for an annotation of the specified type.
	 * @return The method marked with the specified annotation.
	 */
	public static Method getAnnotatedMethodFromInstance(Class<? extends Annotation> methodAnnotationClass, Object instance)
	{
		Method result = null;
		
		for (Method method : instance.getClass().getMethods())
		{
			if (method.isAnnotationPresent(methodAnnotationClass))
			{
				result = method;
				break;
			}
		}
		
		return result;
	}	
}
