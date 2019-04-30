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

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * <p>A utility class that manages the process of loading annotation handler configurations from properties files.
 * Files named <code>META-INF/sst.annotation.handlers</code> located on the classpath will be processed.</p>
 * 
 * <p>The properties files must meet the following conventions:</p>
 * <ul>
 * <li>the key for each entry must be the fully-qualified name of an annotation type recognized by Spring
 * annotation scanning</li>
 * <li>the value for each entry must be the fully-qualified name of the corresponding handler class</li>
 * </ul>
 * 
 * <p>Each individual SST JAR file contains the necessary properties file to initialize any handlers needed to
 * support its functionality.</p>
 * 
 * @author Kevin Fox
 * @since 0.3.0
 *
 */
public final class AnnotationHandlerUtils 
{
	private static final String PROPERTIES_RESOURCE = "META-INF/sst.annotation.handlers";
	
	private static Log logger = LogFactory.getLog(AnnotationHandlerUtils.class);

	// Private constructor to hide default
	private AnnotationHandlerUtils()
	{
		super();
	}
	
	/**
	 * Retrieves instances of the configured annotation handlers of the specified type.  Although only a single handler can
	 * be specified for an annotation type, any given handler can implement multiple types.  This method makes it possible to 
	 * filter for only those handlers that support a particular type.
	 * 
	 * @param handlerType The type of handler to load.
	 * @param <T> The handler class.
	 * @return A map correlating an annotation type to an instance of the configured handler.
	 */
	public static <T> Map<Class<? extends Annotation>, T> loadAnnotationHandlersFromPropertiesResource(Class<T> handlerType)
	{
		Map<Class<? extends Annotation>, T> result = new HashMap<>();
		
		// Load properties files
		Properties handlerNames = null;
		try
		{
			logger.debug("Loading annotation handler mappings of type " + handlerType.getName() + " from all located instances of " + PROPERTIES_RESOURCE + ".");
			// Handles merging properties from multiple resources on the class path with the same name.
			// This allows the definition of delegate mappings to be handled locally in each JAR file
			// using a file with the specified resource name.
			handlerNames = PropertiesLoaderUtils.loadAllProperties(PROPERTIES_RESOURCE);
		}
		catch (IOException e)
		{
			// Allow processing to continue with a warning
			logger.warn("Unable to read properties resources.", e);
		} 
		
		// If any properties were loaded...
		if (handlerNames != null)
		{
			// ...loop through all property entries
			for (String key : handlerNames.stringPropertyNames())
			{
				String handlerName = handlerNames.getProperty(key);
				logger.debug("Creating annotation handler instance [" + key + ", " + handlerName + "]");
				
				// Load annotation class to use as key
				Class<? extends Annotation> annotationClass = getAnnotationClass(key);
				
				// Load handler class
				T handler = processHandlerClass(handlerName, handlerType);
				
				// If both are available, add to result
				if (annotationClass != null && handler != null)
				{
					result.put(annotationClass, handler);
					logger.debug("Added handler to result list [" + key + ", " + handlerName + "]");
				}			
			}
		}
		
		return result;		
	}
	
	@SuppressWarnings("unchecked")
	private static Class<? extends Annotation> getAnnotationClass(String annotationName)
	{
		Class<? extends Annotation> result = null;
		
		try 
		{
			// Load the annotation class
			result = (Class<? extends Annotation>) Class.forName(annotationName);
			logger.debug("Successfully loaded annotation class: " + annotationName);
		} 
		catch (ClassNotFoundException e) 
		{
			// Allow processing to continue with a warning, skipping this annotation
			logger.warn("Unable to load annotation class: " + annotationName, e);			
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T processHandlerClass(String handlerName, Class<T> desiredType)
	{
		T result = null;
		try
		{
			// Load handler class
			Class<?> handlerClass = Class.forName(handlerName);
			logger.debug("Successfully loaded handler class: " + handlerClass);
			
			// Check if it is of the desired type
			if (desiredType.isAssignableFrom(handlerClass))
			{
				// If so, create a new instance
				result = (T) handlerClass.newInstance();
				logger.debug("Successfully created instance of handler class: " + handlerName);
			}			
			
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) 
		{
			// Allow processing to continue with a warning, skipping this handler
			logger.warn("Unable to process specified handler class [" + handlerName + "].", e);
		} 
		
		return result;
	}
}
