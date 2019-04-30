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

package org.simpliccity.sst.bean.namespace;

import org.simpliccity.sst.bean.BeanNameGeneratorListener;
import org.springframework.beans.factory.support.BeanNameGenerator;

/**
 * <p>Defines the operations required for a Spring bean name generator that 
 * names a set of related beans according to a specified naming convention.  
 * This can be used to partition the Spring application context into disjoint
 * namespaces.</p>
 * 
 * <p>This can be especially useful in support of annotation scanning when
 * combined with {@link org.simpliccity.sst.bean.annotation.DelegateAnnotationBeanNameGenerator}. 
 * By delegating naming of each supported annotation to a class that implements this interface,
 * the annotated beans can be named:</p>
 * <ul>
 * <li>on the basis of the information contained in the annotation
 * <li>according to a common convention</li>
 * <li>with minimal chance of naming conflicts</li>
 * </ul>
 * 
 * @author Kevin Fox
 * @since 0.3.0
 *
 */
public interface NamespaceBeanNameGenerator extends BeanNameGenerator 
{
	/**
	 * Generates a bean name that follows the specified naming convention
	 * for the namespace.
	 * 
	 * @param nameComponents A variable list of name components to be
	 * incorporated into the specified name pattern to generate a unique
	 * name.
	 * @return A bean name matching the namespace naming convention.
	 */
	String generateQualifiedName(String... nameComponents);
	
	/**
	 * Returns a list of objects to be notified that a bean has been processed
	 * by this name generator.
	 * 
	 * @return The listeners to be notified that the bean has been processed.
	 * 
	 * @see org.simpliccity.sst.bean.BeanNameGeneratorListener
	 */
	BeanNameGeneratorListener[] getListeners();
}
