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

package org.simpliccity.sst.bean.lookup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * <p>Main controller for the <code>lookup</code> framework.  Manages the process of locating a bean in
 * the Spring application context by metadata, rather than directly by name or type.  This can simplify
 * the process of retrieving a bean programmatically based on runtime criteria in situations where the
 * name or type may not be known.  For example, the <code>transform</code> framework uses this class to
 * dynamically retrieve transformation beans (marked with the {@link org.simpliccity.sst.transform.annotation.Transformation}
 * annotation) based on the source and target classes they process.</p>
 * 
 * <p>This class can be configured to use a specific strategy to actually perform the lookup against the 
 * application context.  This makes it possible to extend the capabilities of the framework or adapt them
 * to changes in the core Spring Framework.</p>
 * 
 * <p>The <code>lookup</code> element of the custom <b>sst</b> namespace can be used as a shorthand way to
 * define a lookup manager bean.  Adding the following lines to the application configuration will create
 * a fully-configured bean of this class using the default namespace-based lookup strategy:</p>
 * 
 * <pre>
 * &lt;beans:xmlns="http://www.springframework.org/schema/beans"
 * 	      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 * 	      xmlns:sst="http://www.simpliccity.org/schema/sst"
 *        xsi:schemaLocation="http://www.simpliccity.org/schema/sst http://www.simpliccity.org/schema/sst/sst-0.3.0.xsd" /&gt;
 * &lt;sst:lookup /&gt;
 * </pre>
 * 
 * <p>The <code>BeanMetadataLookupManager</code> bean instance can then be injected using the 
 * {@link org.springframework.beans.factory.annotation.Autowired} annotation:</p>
 * 
 *  <pre>
 *  	{@literal @}Autowired
 *  	private BeanMetadataLookupManager lookupManager;
 *  </pre>
 * 
 * @author Kevin Fox
 * @since 0.3.0
 * 
 * @see org.simpliccity.sst.bean.lookup.config.BeanMetadataLookupManagerBeanDefinitionParser
 * @see org.simpliccity.sst.bean.lookup.namespace.MappedNameBeanMetadataLookupStrategy
 *
 */
public class BeanMetadataLookupManager implements ApplicationContextAware 
{
	private Log logger = LogFactory.getLog(this.getClass());
	
	private ApplicationContext context;	
	private BeanMetadataLookupStrategy lookupStrategy;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException 
	{
		this.context = applicationContext;
	}
	
	/**
	 * Returns the Spring {@link ApplicationContext} used to generate this bean.
	 * 
	 * @return The associated {@link ApplicationContext}.
	 */
	public ApplicationContext getApplicationContext()
	{
		return context;
	}
	
	/**
	 * Returns the strategy used to lookup beans from the application context.
	 * 
	 * @return An instance of {@link BeanMetadataLookupStrategy} used to lookup beans by metadata.
	 */
	public BeanMetadataLookupStrategy getLookupStrategy() 
	{
		return lookupStrategy;
	}

	/**
	 * Specifies the strategy used to lookup beans from the application context.
	 * 
	 * @param strategy An instance of {@link BeanMetadataLookupStrategy} used to lookup beans by metadata.
	 */
	public void setLookupStrategy(BeanMetadataLookupStrategy strategy) 
	{
		this.lookupStrategy = strategy;
	}

	/**
	 * Retrieves a bean from the application context based on the specified metadata.  The actual lookup is 
	 * delegated to the configured {@link BeanMetadataLookupStrategy} bean.  This class determines whether
	 * the lookup should be for a Java type or a Spring component type (i.e. annotation).
	 * 
	 * @param container A wrapper class for the metadata specifying the desired bean.
	 * @param <T> The class to which the container applies.
	 * @return A bean of the type specified by the metadata retrieved from the application context, if one is
	 * found; <code>null</code> otherwise.
	 */
	public <T> Object lookupBeanByMetadata(BeanMetadataLookupContainer<T> container)
	{
		Object result = null;
		
		try
		{
			if (container.isAnnotatedLookup())
			{
				result = getLookupStrategy().getAnnotatedBean(getApplicationContext(), container);
			}
			else if (container.isTypedLookup())
			{
				result = getLookupStrategy().getTypedBean(getApplicationContext(), container);				
			}
			else
			{
				result = null;
			}
		}
		catch (BeanMetadataLookupException e)
		{
			logger.debug("Unable to retrieve a bean using the specified metadata.", e);
		}
		
		return result;
	}
}
