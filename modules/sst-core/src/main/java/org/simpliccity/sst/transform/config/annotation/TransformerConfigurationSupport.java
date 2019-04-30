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

package org.simpliccity.sst.transform.config.annotation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.transform.Transformer;
import org.simpliccity.sst.transform.cache.TransformationCacheManager;
import org.simpliccity.sst.transform.config.TransformerBeanDefinitionParser;
import org.springframework.context.annotation.Bean;

public class TransformerConfigurationSupport
{
	private Log logger = LogFactory.getLog(this.getClass());

	@Bean(name=TransformerBeanDefinitionParser.BEAN_NAME_TRANSFORMER)
	public Transformer transformer()
	{
		Transformer transformer = new Transformer();
		transformer.setCacheManager(cacheManager());
		logger.debug("Defined transformer bean.");
		
		configureTransformer(transformer);
		
		return transformer;
	}
	
	@Bean(name=TransformerBeanDefinitionParser.BEAN_NAME_CACHE_MANAGER) 
	public TransformationCacheManager cacheManager()
	{
		TransformationCacheManager manager = new TransformationCacheManager();
		logger.debug("Defined transformation cache manager bean.");
		
		configureCacheManager(manager);
		
		return manager;
	}

	protected void configureTransformer(Transformer transformer)
	{
		// Default implementation does nothing.  May be overridden.
	}
	
	protected void configureCacheManager(TransformationCacheManager manager)
	{
		// Default implementation does nothing.  May be overridden.	
	}
}
