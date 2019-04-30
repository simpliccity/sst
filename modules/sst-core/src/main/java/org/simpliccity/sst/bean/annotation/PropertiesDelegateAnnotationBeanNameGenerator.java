/*
 *    Copyright 2012 Information Control Corporation
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
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.support.BeanNameGenerator;

/**
 * <p>An implementation of {@link DelegateAnnotationBeanNameGenerator} that loads mapping information from one
 * or more properties files.  Files named <code>META-INF/sst.annotation.delegates</code> located on the classpath
 * will be processed.</p>
 * 
 * <p>The properties files must meet the following conventions:</p>
 * <ul>
 * <li>the key for each entry must be the fully-qualified name of an annotation type recognized by Spring
 * annotation scanning</li>
 * <li>the value for each entry must be the fully-qualified name of the corresponding 
 * {@link org.springframework.beans.factory.support.BeanNameGenerator} class</li>
 * </ul>
 * 
 * <p>Each individual SST JAR file contains the necessary properties file to initialize any delegates needed to
 * support its functionality.  This class and its corresponding configuration mechanism replaces the
 * functionality of <code>BasePropertiesDelegateAnnotationBeanNameGenerator</code>.</p>
 * 
 * <p>Use the following configuration entries to apply this class as the bean name generator for annotation
 * scanning:</p>
 * 
 * <pre>
 * &lt;context:annotation-config /&gt;
 * &lt;context:component-scan base-package="org.simpliccity.sst,[your package]" 
 *	name-generator="org.simpliccity.sst.bean.annotation.PropertiesDelegateAnnotationBeanNameGenerator" /&gt;
 * </pre>
 * 
 * @author Kevin Fox
 * @since 0.2.0
 * 
 * @see org.simpliccity.sst.bean.annotation.AnnotationHandlerUtils
 *
 */
public class PropertiesDelegateAnnotationBeanNameGenerator extends DelegateAnnotationBeanNameGenerator
{
	
	private Log logger = LogFactory.getLog(PropertiesDelegateAnnotationBeanNameGenerator.class);
	private Map<Class<? extends Annotation>, BeanNameGenerator> delegates;

	/**
	 * Creates and initializes an instance of this {@link DelegateAnnotationBeanNameGenerator}.
	 * 
	 * @see org.simpliccity.sst.bean.annotation.AnnotationHandlerUtils
	 */
	public PropertiesDelegateAnnotationBeanNameGenerator()
	{
		super();
		
		delegates = AnnotationHandlerUtils.loadAnnotationHandlersFromPropertiesResource(BeanNameGenerator.class);
		logger.debug("Succesfully loaded generator mappings [" + delegates.size() + "]");
	}

	@Override
	protected Map<Class<? extends Annotation>, BeanNameGenerator> getDelegates()
	{
		return delegates;
	}
}
