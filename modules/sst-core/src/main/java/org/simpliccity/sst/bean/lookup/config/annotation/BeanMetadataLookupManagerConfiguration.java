/*
 *    Copyright 2016 Information Control Company
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

package org.simpliccity.sst.bean.lookup.config.annotation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.bean.lookup.BeanMetadataLookupManager;
import org.simpliccity.sst.bean.lookup.BeanMetadataLookupStrategy;
import org.simpliccity.sst.bean.lookup.config.BeanMetadataLookupManagerParserHelper;
import org.simpliccity.sst.bean.lookup.namespace.MappedNameBeanMetadataLookupStrategy;
import org.simpliccity.sst.config.IfEnabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class provides Java-based configuration for the <code>lookup<code> framework when enabled
 * by the presence of the {@code @EnableSstBeanLookup} annotation on a {@code @Configuration} class.  
 * 
 * This configuration creates an instance of {@link BeanMetadataLookupManager} wired to either a user-defined
 * {@link BeanMetadataLookupStrategy} bean, if one is configured elsewhere, or a default strategy using {@link MappedNameBeanMetadataLookupStrategy},
 * otherwise.
 * 
 * @author Kevin Fox
 * @since 1.0.0
 * 
 * @see EnableSstBeanLookup
 */
@Configuration
@IfEnabled(EnableSstBeanLookup.class)
public class BeanMetadataLookupManagerConfiguration
{
	private Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired(required=false)
	private BeanMetadataLookupStrategy lookupStrategy;
	
	/**
	 * Configures the {@link BeanMetadataLookupManager} bean.
	 * 
	 * @return The configured {@link BeanMetadataLookupManager} bean.
	 */
	@Bean(name=BeanMetadataLookupManagerParserHelper.BEAN_NAME_LOOKUP_MANAGER)
	public BeanMetadataLookupManager lookupManager()
	{
		BeanMetadataLookupManager manager = new BeanMetadataLookupManager();
		
		BeanMetadataLookupStrategy strategy = (lookupStrategy == null) ? new MappedNameBeanMetadataLookupStrategy() : lookupStrategy;
		manager.setLookupStrategy(strategy);
		logger.debug("Defined lookup manager bean.");
		
		return manager;
	}
}
