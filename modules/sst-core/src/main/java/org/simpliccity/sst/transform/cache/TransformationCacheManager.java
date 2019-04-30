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

package org.simpliccity.sst.transform.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.transform.annotation.TransformationCacheMode;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>A class that manages the object caching capabilities for the <code>transform</code>
 * framework.  Object caching allows the <code>transform</code> framework to maintain object
 * identity when transforming the same source object multiple times.
 * 
 * <p>This class relies on an instance of a class implementing the {@link TransformationCache}
 * interface to perform the actual object caching.  The configured (or default) 
 * {@link TransformationCacheFactory} is used to retrieve the necessary cache instance.  Other
 * configurable settings also make it possible to disable all caching or to choose between a
 * single global cache or dedicated thread-level cache instances.</p>
 * 
 * <p>The settings for the TransformationCacheManager used by the active 
 * {@link org.simpliccity.sst.transform.Transformer} instance can either be configured explicitly,
 * by including bean definitions for the two classes in the application configuration, or implicitly
 * using the <code>transformer</code> element of the <code>sst</code> custom namespace.</p>
 * 
 * @author Kevin Fox
 * @since 0.3.0
 * 
 * @see org.simpliccity.sst.transform.Transformer
 * @see org.simpliccity.sst.transform.config.TransformerBeanDefinitionParser
 *
 */
public class TransformationCacheManager implements InitializingBean
{
	private Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private DefaultTransformationCacheFactory defaultCacheFactory;
	
	// Configurable bean properties
	
	/**
	 * A flag indicating whether object caching has been globally disabled (<b>Default</b>: {@value}).
	 */
	private boolean disableCache = false;
	
	/**
	 * The default cache mode used for transformations that do not explicitly specify one (<b>Default</b>: {@value}).
	 */
	private TransformationCacheMode defaultCacheMode = TransformationCacheMode.NONE;
	
	/**
	 * A flag indicating whether to use a single global cache or a dedicated cache per thread (<b>Default</b>: {@value}).  
	 */
	private boolean globalCacheScope = false;
	
	/**
	 * The instance of {@link TransformationCacheFactory} used to generate {@link TransformationCache} instance(s).
	 */
	private TransformationCacheFactory cacheFactory;

	private ThreadLocal<TransformationCache> localCache = new ThreadLocal<TransformationCache>() 
	{
		@Override protected TransformationCache initialValue()
		{
			// Get the cache factory that will actually be used (specifically set overrides default)
			TransformationCacheFactory activeCacheFactory = getActiveCacheFactory();
			
			return (activeCacheFactory == null) ? null : activeCacheFactory.getNewCacheInstance();
		}
		
		@Override public void remove()
		{
			// Flush the cache
			get().flush();
			
			// Allow the parent implementation to perform its cleanup
			super.remove();
		}
	};
	
	private TransformationCache globalCache;
	
	/**
	 * Indicates whether object caching has been globally disabled.
	 * 
	 * @return <code>true</code> if object caching is disabled; <code>false</code> if not.
	 * 
	 * @see #disableCache
	 */
	public boolean isDisableCache() 
	{
		return disableCache;
	}

	/**
	 * Specifies whether to globally disable object caching.
	 * 
	 * @param disableCache <code>true</code> if object caching should be disabled; <code>false</code> if not.
	 * 
	 * @see #disableCache
	 */
	public void setDisableCache(boolean disableCache) 
	{
		this.disableCache = disableCache;
	}

	/**
	 * Lists the default object caching mode in use.  The default mode applies for all transformations that do
	 * not specifically define a caching mode.
	 * 
	 * @return The default object caching mode.
	 * 
	 * @see #defaultCacheMode
	 * @see org.simpliccity.sst.transform.annotation.TransformationCacheMode
	 */
	public String getDefaultCacheMode() 
	{
		return getDefaultCacheModeEnum().name();
	}

	/**
	 * Specifies the default object caching mode to use.  The default mode applies for all transformations that do
	 * not specifically define a caching mode.
	 * 
	 * @param defaultCacheMode The default object caching mode.
	 * 
	 * @see #defaultCacheMode
	 * @see org.simpliccity.sst.transform.annotation.TransformationCacheMode
	 */
	public void setDefaultCacheMode(String defaultCacheMode) 
	{
		setDefaultCacheModeEnum(TransformationCacheMode.valueOf(defaultCacheMode));
	}
	
	/**
	 * Lists the default object caching mode in use as one of the values of the
	 * {@link org.simpliccity.sst.transform.annotation.TransformationCacheMode} enum.  The default mode applies for all
	 * transformations that do not specifically define a caching mode.
	 * 
	 * @return The default object caching mode as an enum value.
	 * 
	 * @see #defaultCacheMode
	 * @see org.simpliccity.sst.transform.annotation.TransformationCacheMode
	 */
	public TransformationCacheMode getDefaultCacheModeEnum()
	{
		return defaultCacheMode;		
	}
	
	private void setDefaultCacheModeEnum(TransformationCacheMode defaultCacheMode)
	{
		this.defaultCacheMode = defaultCacheMode;
	}

	/**
	 * Indicates whether the cache manager uses a single global cache or a dedicated cache per thread
	 * for object caching.  A global cache optimizes the effectiveness of the cache, while the use of thread-local
	 * caching provides a level of isolation to ensure thread safety and transformation accuracy.
	 * 
	 * @return <code>true</code> if a single global cache is used; <code>false</code> if a thread-local caching
	 * is employed.
	 * 
	 * @see #globalCacheScope
	 */
	public boolean isGlobalCacheScope() 
	{
		return globalCacheScope;
	}

	/**
	 * Specifies whether the cache manager uses a single global cache or a dedicated cache per thread
	 * for object caching.  A global cache optimizes the effectiveness of the cache, while the use of thread-local
	 * caching provides a level of isolation to ensure thread safety and transformation accuracy.
	 * 
	 * @param globalCacheScope <code>true</code> to use  a single global cache; <code>false</code> to employ a thread-local cache.
	 * 
	 * @see #globalCacheScope
	 */
	public void setGlobalCacheScope(boolean globalCacheScope) 
	{
		this.globalCacheScope = globalCacheScope;
	}

	/**
	 * Returns the instance of {@link TransformationCacheFactory} used to generate {@link TransformationCache}
	 * instance(s) to support object caching.  Unless otherwise specified, an instance of {@link DefaultTransformationCacheFactory}
	 * is used.
	 * 
	 * @return The active cache factory for the transformer.
	 * 
	 * @see #cacheFactory
	 * @see DefaultTransformationCacheFactory
	 */
	public TransformationCacheFactory getCacheFactory() 
	{
		return cacheFactory;
	}

	/**
	 * Specifies the instance of {@link TransformationCacheFactory} used to generate {@link TransformationCache}
	 * instance(s) to support object caching.
	 * 
	 * @param cacheFactory The {@link TransformationCacheFactory} used to create the cache instance(s) used for
	 * object caching.
	 * 
	 * @see #cacheFactory
	 */
	public void setCacheFactory(TransformationCacheFactory cacheFactory) 
	{
		this.cacheFactory = cacheFactory;
	}

	@Override
	public void afterPropertiesSet() throws Exception 
	{
		// Initialize the global cache using the active cache factory, if necessary
		if (!isDisableCache() && isGlobalCacheScope() && globalCache == null)
		{
			globalCache = getActiveCacheFactory().getNewCacheInstance();
		}
		
	}

	/**
	 * Retrieves an instance of the target class from the object cache using the specified cache index.
	 * 
	 * @param targetClass The type of the object to be retrieved.
	 * @param cacheIndex The cache index used to lookup the desired instance.
	 * @return A cached instance of the target class; <code>null</code> if none is found, caching is
	 * disabled or the cache index is <code>null</code>. 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object retrieveFromCache(Class targetClass, Object cacheIndex)
	{
		Object result = null;
		
		// If the cache index is non-null, caching is enabled...
		if (cacheIndex != null && !isDisableCache())
		{
			// ... so try to retrieve an existing instance of the specified type
			// from the appropriate cache
			logger.debug("Attempting to retrieve transformed object from cache.");
			result = getActiveCache().get(targetClass, cacheIndex);
		}
		
		return result;
	}
	
	/**
	 * Adds or updates an instance of the target class into the object cache using the specified cache index.
	 * 
	 * @param targetClass The type of the object to cache.
	 * @param cacheIndex The cache index by which the object may be retrieved from the cache.
	 * @param result The instance of the target class to be added to the cache.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void updateCache(Class targetClass, Object cacheIndex, Object result)
	{
		// If appropriate, add the result to the appropriate cache.
		if (cacheIndex != null && !isDisableCache())
		{
			logger.debug("Updating object cache with transformed object: " + result + ".");
			getActiveCache().put(targetClass, cacheIndex, result);
		}
		
	}
	
	/**
	 * Conditionally flushes the managed object cache based on the specified cache mode
	 * or flag.
	 * 
	 * @param mode The cache mode of the current transformation.  Flushing occurs if it is
	 * set to <code>FLUSH</code>.
	 * @param forceFlush A flag indicating whether to force flushing regardless of the
	 * transformation-specific setting.
	 * 
	 * @see #flushCache()
	 */
	public void flushCache(TransformationCacheMode mode, boolean forceFlush)
	{
		// If the transformer cache mode is FLUSH or flush has been specifically requested, ... 
		if (mode == TransformationCacheMode.FLUSH || (usesCache(mode) && forceFlush))
		{
			// ... flush the cache
			flushCache();
		}
	}
	
	/**
	 * Flushes the managed object cache, clearing out all previously cached values.  Note that failure
	 * to flush the object cache, through any of the available mechanisms, will prevent garbage collection
	 * of the cached values, their keys and any referenced objects.  Also note that this operation only
	 * flushes the active cache, as determined by the configured cache scope (global or thread-local).
	 * 
	 * @see TransformationCacheMode
	 * @see #setGlobalCacheScope(boolean)
	 */
	public void flushCache()
	{
		// If the cache is active ...
		if (!isDisableCache())
		{
			// ... cleanup the appropriate cache
			logger.debug("Flushing object cache.");
			if (isGlobalCacheScope())
			{
				globalCache.flush();
			}
			else
			{
				localCache.remove();
			}
		}
	}
	
	/**
	 * Determines whether the current transformation utilizes object caching, based
	 * on the setting of the <code>cache</code> element of its Transformation annotation.
	 * 
	 * @param mode The cache mode of the transformation.
	 * @return <code>true</code> if the transformation will use object caching; <code>false</code>
	 * otherwise.
	 * 
	 * @see org.simpliccity.sst.transform.annotation.Transformation#cache()
	 */
	public boolean usesCache(TransformationCacheMode mode)
	{
		boolean result;
		
		switch (mode)
		{
			case JOIN:
			case FLUSH:
				result = true;
				break;
			case NONE:
			default:
				result = false;
				break;
		}
		
		return result;
	}
	
	private TransformationCacheFactory getActiveCacheFactory()
	{
		return (cacheFactory == null) ? defaultCacheFactory : cacheFactory;
	}
	
	private TransformationCache getActiveCache()
	{
		TransformationCache result;
		
		if (isGlobalCacheScope())
		{
			result = globalCache;
		}
		else
		{
			result = localCache.get();
		}
		
		return result;
	}		
}
