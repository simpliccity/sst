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

package org.simpliccity.sst.transform;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.bean.annotation.BeanAnnotationUtils;
import org.simpliccity.sst.transform.annotation.InTransform;
import org.simpliccity.sst.transform.annotation.OutTransform;
import org.simpliccity.sst.transform.annotation.Transformation;
import org.simpliccity.sst.transform.annotation.TransformationCacheMode;
import org.simpliccity.sst.transform.cache.TransformationCacheManager;
import org.simpliccity.sst.transform.cache.annotation.InTransformCacheIndex;
import org.simpliccity.sst.transform.cache.annotation.OutTransformCacheIndex;

/**
 * A proxy class used to provide a standard interface for an annotated transformation bean.
 * Since transformation beans are defined completely through the use of {@link Transformation}
 * and related annotations, not by implementing a particular interface, the actual method(s)
 * used to perform the configured transformation(s) must be invoked via reflection.  This proxy
 * class encapsulates the details for performing the appropriate method invocations by means of
 * reflection, including all necessary interactions with the 
 * {@link org.simpliccity.sst.transform.cache.TransformationCacheManager}.
 * 
 * @author Kevin Fox
 * @since 0.3.0
 * 
 * @see Transformer
 *
 */
public class TransformationProxy 
{
	private Log logger = LogFactory.getLog(this.getClass());
	
	// Transformation configuration
	private Object transformer;
	private boolean surfaceTransformationExceptions;
	private Transformation configuration;
	private Method outboundTransformationMethod;
	private Method inboundTransformationMethod;

	// Cache configuration
	private TransformationCacheManager cacheManager;
	private Method outboundCacheIndexMethod;
	private Method inboundCacheIndexMethod;
	
	/**
	 * Creates a new instance of TransformationProxy initialized with the cache manager and
	 * a transformation bean instance.
	 * 
	 * @param cacheManager The manager for transformation result caching.
	 * @param transformer An instance of a class marked with the {@link Transformation} annotation.
	 * @param surfaceTransformationExceptions A flag indicating whether exceptions thrown when invoking methods on
	 * the transformation bean should be rethrown.
	 */
	public TransformationProxy(TransformationCacheManager cacheManager, Object transformer, boolean surfaceTransformationExceptions)
	{
		this.cacheManager = cacheManager;
		this.transformer = transformer;
		this.surfaceTransformationExceptions = surfaceTransformationExceptions;
		
		// Get transformation annotation
		this.configuration = BeanAnnotationUtils.getAnnotationFromInstance(Transformation.class, transformer);
		
		// Find transformation methods
		this.outboundTransformationMethod = BeanAnnotationUtils.getAnnotatedMethodFromInstance(OutTransform.class, transformer);
		this.inboundTransformationMethod = BeanAnnotationUtils.getAnnotatedMethodFromInstance(InTransform.class, transformer);
		
		// Initialize cache information
		this.outboundCacheIndexMethod = BeanAnnotationUtils.getAnnotatedMethodFromInstance(OutTransformCacheIndex.class, transformer);
		this.inboundCacheIndexMethod = BeanAnnotationUtils.getAnnotatedMethodFromInstance(InTransformCacheIndex.class, transformer);
	}

	/**
	 * Returns the {@link TransformationCacheManager} associated with this proxy instance.
	 * 
	 * @return The cache manager used by this proxy.
	 */
	public TransformationCacheManager getCacheManager() 
	{
		return cacheManager;
	}

	/**
	 * Returns the transformation bean (a class annotated with {@link Transformation})
	 * represented by this proxy.
	 * 
	 * @return The transformation bean associated with this proxy.
	 */
	public Object getTransformer() 
	{
		return transformer;
	}

	/**
	 * Returns a flag indicating whether exceptions thrown when invoking methods on
	 * the transformation bean represented by this proxy should be rethrown.
	 * 
	 * @since 0.3.2
	 * 
	 * @return <code>true</code> if exceptions should be rethrown; <code>false</code> otherwise.
	 */
	public boolean isSurfaceTransformationExceptions() 
	{
		return surfaceTransformationExceptions;
	}

	/**
	 * Returns the configuration settings, contained in the {@link Transformation}
	 * annotation, for the associated transformation bean.
	 * 
	 * @return The transformation configuration for the associated bean.
	 */
	public Transformation getConfiguration()
	{
		return configuration;
	}
	
	/**
	 * Returns the method of the transformation bean used to perform outbound
	 * transformations, if any.
	 * 
	 * @return The outbound transformation method from the associated bean or
	 * <code>null</code> if none is specified.
	 * 
	 * @see OutTransform
	 */
	public Method getOutboundTransformationMethod()
	{
		return outboundTransformationMethod;
	}
	
	/**
	 * Returns the method of the transformation bean used to perform inbound
	 * transformations, if any.
	 * 
	 * @return The inbound transformation method from the associated bean or
	 * <code>null</code> if none is specified.
	 * 
	 * @see InTransform
	 */
	public Method getInboundTransformationMethod()
	{
		return inboundTransformationMethod;
	}

	/**
	 * Returns the effective caching mode to be employed by transformations using
	 * the associated bean.  If no explicit caching mode is specified in the bean's
	 * {@link Transformation} annotation, the default cache mode for the associated
	 * cache manager is used.
	 * 
	 * @return The effective cache mode used when performing transformations.
	 * 
	 * @see TransformationCacheManager
	 */
	public TransformationCacheMode getEffectiveCacheMode()
	{
		return (configuration.cache() == TransformationCacheMode.DEFAULT) ? cacheManager.getDefaultCacheModeEnum() : configuration.cache();
	}
		
	/**
	 * Returns the method of the transformation bean used to generate an
	 * outbound cache index, if any.
	 * 
	 * @return The method from the associated bean used to generate an index for caching
	 * outbound transformation results or <code>null</code> if none is specified.
	 * 
	 * @see OutTransformCacheIndex
	 */
	public Method getOutboundCacheIndexMethod()
	{
		return outboundCacheIndexMethod;
	}
	
	/**
	 * Returns the method of the transformation bean used to generate an
	 * inbound cache index, if any.
	 * 
	 * @return The method from the associated bean used to generate an index for caching
	 * inbound transformation results or <code>null</code> if none is specified.
	 * 
	 * @see InTransformCacheIndex
	 */
	public Method getInboundCacheIndexMethod()
	{
		return inboundCacheIndexMethod;
	}
	
	/**
	 * Performs an outbound transformation of the source object to the specified type.  The proxy
	 * delegates the job of performing the actual transformation to the outbound method, if any, of
	 * the underlying transformation bean.
	 * 
	 * @param source The instance to be transformed.
	 * @param targetClass The class to transform to.
	 * @param forceFlush A flag indicating whether to flush the object cache upon completion of this transformation.
	 * @param <T> The resulting type of the transformation.
	 * @return An instance of the target class produced by applying a transformation to the source instance;
	 * <code>null</code> if the transformation cannot be performed.
	 * @throws UnsupportedTransformationException If the underlying bean does designate an outbound transformation method.
	 * 
	 * @see #getOutboundTransformationMethod()
	 */
	public <T> T performOutTransformation(Object source, Class<T> targetClass, boolean forceFlush) throws UnsupportedTransformationException
	{
		if (outboundTransformationMethod == null)
		{
			throw new UnsupportedTransformationException("Outbound transformation not supported.");
		}
		
		return performTransformation(source, targetClass, outboundTransformationMethod, true, forceFlush);
	}

	/**
	 * Performs an inbound transformation of the source object to the specified type.  The proxy
	 * delegates the job of performing the actual transformation to the inbound method, if any, of
	 * the underlying transformation bean.
	 * 
	 * @param source The instance to be transformed.
	 * @param targetClass The class to transform to.
	 * @param forceFlush A flag indicating whether to flush the object cache upon completion of this transformation.
	 * @param <T> The resulting type of the transformation.
	 * @return An instance of the target class produced by applying a transformation to the source instance;
	 * <code>null</code> if the transformation cannot be performed.
	 * @throws UnsupportedTransformationException If the underlying bean does designate an inbound transformation method.
	 * 
	 * @see #getInboundTransformationMethod()
	 */
	public <T> T performInTransformation(Object source, Class<T> targetClass, boolean forceFlush) throws UnsupportedTransformationException
	{
		if (inboundTransformationMethod == null)
		{
			throw new UnsupportedTransformationException("Inbound transformation not supported.");
		}
		
		return performTransformation(source, targetClass, inboundTransformationMethod, false, forceFlush);
	}

	@SuppressWarnings("unchecked")
	private <T> T performTransformation(Object source, Class<T> targetClass, Method transformMethod, boolean outboundTransform, boolean forceFlush)
	{
		Object result;
		
		// Generate the cache index for the specified source object
		Object cacheIndex = getCacheIndex(source, outboundTransform);
		
		// Attempt to retrieve transformed object from object cache
		result = cacheManager.retrieveFromCache(targetClass, cacheIndex);
		
		// If we haven't found a result in the cache...
		if (result == null)
		{
			logger.debug("Caching not supported or no existing cached object available.");
			
			// ... use the appropriate transformation method, ...
		    if (transformMethod != null)
			{
				logger.debug("Attempting to transform object using transformation method [" + transformMethod.getName() + "].");
				// ... perform the transformation, ...
				result = invokeTransformerMethod(transformMethod, source);
				
				// ... and add the transformed object to the object cache
				cacheManager.updateCache(targetClass, cacheIndex, result);
			}
		}
		else
		{
			logger.debug("Retrieved object from cache: " + result + ".");
		}
		
		// Conditionally flush the object cache (depending on transformation settings and flag)
		cacheManager.flushCache(getEffectiveCacheMode(), forceFlush);
		
		return (T) result;
	}

	private Object getCacheIndex(Object value, boolean outBound)
	{
		Object result = null;
		
		// A cache index is only needed if the specific transformer supports caching
		if (cacheManager.usesCache(getEffectiveCacheMode()))
		{
			// If it does, look for a method annotated to generate the cache index for the direction of the transformation
			Method indexMethod = outBound ? outboundCacheIndexMethod : inboundCacheIndexMethod;
			
			// If no such method exists,...
			if (indexMethod == null)
			{
				// ... the object itself will be the index
				result = value;
				logger.debug("Using default cache index: " + result + ".");
			}
			else
			{
				// Otherwise, execute the appropriate method to generate the index
				logger.debug("Attempting to generate cache index using annotated method [" + indexMethod.getName() + "].");
				result = invokeTransformerMethod(indexMethod, value);
				logger.debug("Using generated cache index: " + result + ".");
			}
		}
		else
		{
			logger.debug("Transformation configuration does not support caching.  No cache index generated.");
		}
		
		return result;
	}
	
	private Object invokeTransformerMethod(Method method, Object input)
	{
		Object result = null;
		
		try 
		{
			result = method.invoke(transformer, input);
		}
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) 
		{
			handleTransformationException("Unable to invoke transformer method.", e);
			result = null;
		} 
		
		return result;
	}
	
	private void handleTransformationException(String message, Exception e)
	{
		if (isSurfaceTransformationExceptions())
		{
			logger.error(message, e);
			throw new TransformationException(message, e);
		}
		else
		{
			logger.debug(message, e);
		}
	}
}
