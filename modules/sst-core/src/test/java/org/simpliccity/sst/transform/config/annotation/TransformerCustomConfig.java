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

import org.simpliccity.sst.bean.annotation.PropertiesDelegateAnnotationBeanNameGenerator;
import org.simpliccity.sst.bean.lookup.config.annotation.EnableSstBeanLookup;
import org.simpliccity.sst.transform.Transformer;
import org.simpliccity.sst.transform.annotation.TransformationCacheMode;
import org.simpliccity.sst.transform.cache.TransformationCacheManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("transformerCustom")
@ComponentScan(basePackages="org.simpliccity.sst", nameGenerator=PropertiesDelegateAnnotationBeanNameGenerator.class)
@EnableSstBeanLookup
@EnableSstTransformer
public class TransformerCustomConfig extends TransformerConfigurerAdapter
{
	@Override
	public void configureTransformer(Transformer transformer) 
	{
		transformer.setSurfaceExceptions(true);
	}
	
	@Override
	public void configureCacheManager(TransformationCacheManager cacheManager) 
	{
		cacheManager.setDefaultCacheMode(TransformationCacheMode.JOIN.name());
		cacheManager.setGlobalCacheScope(true);
		cacheManager.setDisableCache(true);
		cacheManager.setCacheFactory(new NoopTransformationCacheFactory());
	}
}
