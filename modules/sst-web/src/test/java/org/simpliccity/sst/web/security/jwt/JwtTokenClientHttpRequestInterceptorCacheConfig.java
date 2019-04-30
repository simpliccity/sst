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

package org.simpliccity.sst.web.security.jwt;

import java.util.Arrays;

import org.simpliccity.sst.bean.lookup.config.annotation.EnableSstBeanLookup;
import org.simpliccity.sst.jwt.UserJwtTokenGenerator;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableCaching
@Profile("jwtClientInterceptorCache")
@EnableSstBeanLookup
public class JwtTokenClientHttpRequestInterceptorCacheConfig extends JwtTokenClientHttpRequestInterceptorConfig 
{
	@Bean
	public CacheManager cacheManager()
	{
		SimpleCacheManager cacheManager = new SimpleCacheManager();
		cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache(UserJwtTokenGenerator.CACHE_JWT_TOKEN)));
		
		return cacheManager;
	}
}
