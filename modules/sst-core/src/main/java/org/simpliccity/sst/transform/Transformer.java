/*
 *    Copyright 2009 Information Control Corporation
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.bean.annotation.BeanAnnotationUtils;
import org.simpliccity.sst.bean.lookup.BeanMetadataLookupManager;
import org.simpliccity.sst.bean.lookup.namespace.BeanMetadataLookupNameMapper;
import org.simpliccity.sst.transform.annotation.Transformation;
import org.simpliccity.sst.transform.annotation.TransformationAnnotationBeanNameGenerator;
import org.simpliccity.sst.transform.annotation.TransformationBeanMetadataLookupContainer;
import org.simpliccity.sst.transform.annotation.TransformationType;
import org.simpliccity.sst.transform.cache.TransformationCacheManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>Main controller for the <code>transform</code> framework.  Manages the process of transforming between
 * related object representations using classes annotated with {@link org.simpliccity.sst.transform.annotation.Transformation}.
 * <code>Transformer</code> is used to isolate the invoking class from the underlying implementation details for
 * the transformation.</p> 
 * 
 * <p><code>Transformer</code> itself places no constraints on the transformation implementation beyond the use of
 * {@link org.simpliccity.sst.transform.annotation.Transformation} and related annotations.  Transformation beans
 * will be located automatically when annotation scanning is enabled using the following configuration entries:</p>
 *
 * <pre>
 * &lt;context:annotation-config /&gt;
 * &lt;context:component-scan base-package="org.simpliccity.sst,[your package]" 
 *	name-generator="org.simpliccity.sst.bean.annotation.PropertiesDelegateAnnotationBeanNameGenerator" /&gt;
 * </pre>
 * 
 * <p>The <code>Transformer</code> instance must be obtained from the Spring container (e.g. by injection).
 * The custom <b>sst</b> namespace can be used to configure the transformer instance as follows:</p>
 *
 * <pre>
 * &lt;beans:xmlns="http://www.springframework.org/schema/beans"
 * 	      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 * 	      xmlns:sst="http://www.simpliccity.org/schema/sst"
 *        xsi:schemaLocation="http://www.simpliccity.org/schema/sst http://www.simpliccity.org/schema/sst/sst.xsd" /&gt;
 *        
 * &lt;sst:transformer defaultCacheMode="JOIN" /&gt;
 * </pre>
 * 
 * <p>The <code>Transformer</code> bean instance can then be injected using the {@link Autowired} annotation:</p>
 * 
 *  <pre>
 *  {@literal @}Autowired
 *  private Transformer transformer;
 *  </pre>
 *  
 * <p>In addition to locating and invoking the appropriate transformation, <code>Transformer</code> provides
 * basic facilities for the framework such as object caching.  Caching is an optional feature that can be used to 
 * ensure that multiple references to a source object instance are consistently transformed to the same target object.
 * Global settings for caching are configured on the <code>Transformer</code> instance.  Transformation-specific settings
 * can be controlled using the {@link org.simpliccity.sst.transform.annotation.Transformation#cache()} attribute.</p>
 * 
 * 
 * @author Kevin Fox
 * 
 * @see org.simpliccity.sst.bean.annotation.PropertiesDelegateAnnotationBeanNameGenerator
 * @see org.simpliccity.sst.transform.config.TransformerBeanDefinitionParser
 * @see org.simpliccity.sst.transform.cache.TransformationCacheManager
 *
 */
public class Transformer
{
	private Log logger = LogFactory.getLog(this.getClass());
	
	// --- UserGuide beanMetadataLookupManager ---
	@Autowired
	private BeanMetadataLookupManager lookupManager;
	// --- UserGuide beanMetadataLookupManager ---
	
	private TransformationCacheManager cacheManager;

	private boolean surfaceExceptions;

	private BeanMetadataLookupNameMapper nameMapper = new TransformationAnnotationBeanNameGenerator();
	
	private Map<String, TransformationDescriptor> transformations = new HashMap<>();
	
	/**
	 * Returns the cache manager used by this class.
	 * 
	 * @since 0.3.0
	 * 
	 * @return An instance of {@link TransformationCacheManager} used to manage object caching.
	 */
	public TransformationCacheManager getCacheManager() 
	{
		return cacheManager;
	}

	/**
	 * Specifies the cache manager used by this class.
	 * 
	 * @since 0.3.0
	 * 
	 * @param cacheManager An instance of {@link TransformationCacheManager} used to manage object caching.
	 */
	public void setCacheManager(TransformationCacheManager cacheManager) 
	{
		this.cacheManager = cacheManager;
	}
	
 	/**
	 * Returns a flag indicating whether exceptions thrown when invoking methods on
	 * the transformation bean should be rethrown.
	 * 
	 * @since 0.3.2
	 * 
	 * @return <code>true</code> if exceptions should be rethrown; <code>false</code> otherwise.
	 */
	public boolean isSurfaceExceptions() 
	{
		return surfaceExceptions;
	}

	/**
	 * Sets a flag indicating whether exceptions thrown when invoking methods on the transformation bean should
	 * be rethrown.
	 * 
	 * @since 0.3.2
	 * 
	 * @param surfaceExceptions <code>true</code> if exceptions should be rethrown; <code>false</code> otherwise.
	 */
	public void setSurfaceExceptions(boolean surfaceExceptions) 
	{
		this.surfaceExceptions = surfaceExceptions;
	}

	/**
	 * A convenience method that performs the transformation without flushing the object cache.
	 * 
	 * @param source The instance to be transformed.
	 * @param targetClass The class to transform to.
	 * @param <T> The resulting type of the transformation.
	 * @return An instance of the target class produced by applying a transformation to the source instance;
	 * <code>null</code> if the transformation cannot be performed.
	 * @throws TransformationException if a transformation throws an exception and {@link #isSurfaceExceptions()}
	 * is <code>true</code>.
	 * 
	 * @see #transform(Object, Class, boolean)
	 */
	public <T> T transform(Object source, Class<T> targetClass)
	{
		return transform(source, targetClass, false);
	}
	
	/**
	 * <p>Transforms an object to an instance of another type.  This method coordinates the transformation process, delegating
	 * the actual work to a class marked with the {@link org.simpliccity.sst.transform.annotation.Transformation} annotation.
	 * The appropriate transformation is selected based on the class of the source instance and the target class specified.</p>
	 * 
	 * <p>If enabled, object caching will be employed.  Without caching, a call to this method will result in 
	 * creating a new instance of the target class, even if the source has previously been transformed.  With caching,
	 * the source object is used as a key to retrieve an existing instance of the target class, if any.  Upon completion, any
	 * newly-generated target instance is added to the cache.</p>
	 * 
	 * <p>Cache behavior is controlled by a combination of settings on the {@link TransformationCacheManager} and the configuration
	 * of the specific transformation involved.  In addition to these, the <code>forceFlush</code> parameter can be used to flush
	 * the cache upon completion of this transformation.  While use of <code>forceFlush</code> makes caching pointless for a shallow 
	 * transformation, it can be very helpful in ensuring the desired caching behavior for a deep transformation,  which will call this
	 * method recursively.</p>
	 * 
	 * @since 0.2.0
	 * 
	 * @param source The instance to be transformed.
	 * @param targetClass The class to transform to.
	 * @param forceFlush A flag indicating whether to flush the object cache upon completion of this transformation.
	 * @param <T> The resulting type of the transformation.
	 * @return An instance of the target class produced by applying a transformation to the source instance;
	 * <code>null</code> if the transformation cannot be performed.
	 * @throws TransformationException if a transformation throws an exception and {@link #isSurfaceExceptions()}
	 * is <code>true</code>.
	 * 
	 * @see org.simpliccity.sst.transform.annotation.Transformation
	 * @see org.simpliccity.sst.transform.annotation.TransformationCacheMode
	 * @see TransformationCacheManager#flushCache()
	 */
	public <T> T transform(Object source, Class<T> targetClass, boolean forceFlush)
	{
		if (source == null)
		{
			logger.debug("Source is null.  Nothing to transform.");
			return null;
		}
		
		logger.debug("Attempting to transform " + source + " to " + targetClass + " with cache flush required = " + forceFlush + ".");
		
		T result = null;
		
		@SuppressWarnings("rawtypes")
		Class sourceClass = source.getClass();
		
		TransformationDescriptor transform = getTransformation(sourceClass, targetClass);
		
		if (transform != null)
		{
			logger.debug("Found transformer bean class: [" + transform.getTransformationClassName() + "].");
			
			result = transform.performTransformation(source, targetClass, forceFlush);
		}
		
		return result;
	}

	@SuppressWarnings("rawtypes")
	private TransformationDescriptor getTransformation(Class sourceClass, Class targetClass)
	{
		// Get the transformation from the local cache
		TransformationDescriptor result = getTransformationDescriptor(sourceClass, targetClass);
		
		// If none found (one time event),...
		if (result == null)
		{
			// ...initialize the set of related transformations...
			loadRelatedTransformations(sourceClass, targetClass);
			
			// ...and try again
			result = getTransformationDescriptor(sourceClass, targetClass);
		}
		
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	private TransformationDescriptor getTransformationDescriptor(Class sourceClass, Class targetClass)
	{
		// Retrieve from cache
		String transformationName = generateTransformationName(sourceClass, targetClass);
		
		return transformations.get(transformationName);
	}
	
	@SuppressWarnings("rawtypes")
	private String generateTransformationName(Class sourceClass, Class targetClass)
	{
		// Setup the metadata
		TransformationBeanMetadataLookupContainer container = new TransformationBeanMetadataLookupContainer();
		container.setSource(sourceClass.getCanonicalName());
		container.setTarget(targetClass.getCanonicalName());
		
		// Use the name mapper to build the name
		return nameMapper.generateNameFromMetadata(container);		
	}
	
	@SuppressWarnings("rawtypes")
	private void loadRelatedTransformations(Class sourceClass, Class targetClass)
	{
		// Process any transformation found for the specified source, target
		processTransformationProxy(sourceClass, targetClass);
		
		// Process the inverse transformation as well (might be bidirectional) - unnecessary if source and target are the same
		if (!sourceClass.equals(targetClass))
		{
			processTransformationProxy(targetClass, sourceClass);
		}
		
		// Load a NOOP transformation for the specified source, target, if necessary
		processGap(sourceClass, targetClass);
		
		// Load a NOOP transformation for the inverse, if necessary - skip if source and target are the same
		if (!sourceClass.equals(targetClass))
		{
			processGap(targetClass, sourceClass);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void processTransformationProxy(Class sourceClass, Class targetClass)
	{
		// Get the transformation for the specified source, target
		TransformationProxy proxy = getTransformationProxy(sourceClass, targetClass);
		
		// If one is available...
		if (proxy != null)
		{
			// ...determine which direction it applies, ...
			Transformation annotation = BeanAnnotationUtils.getAnnotationFromInstance(Transformation.class, proxy.getTransformer());
			TransformationType type = annotation.direction();
			
			// ...create appropriate descriptors,...
			List<TransformationDescriptor> transforms = new ArrayList<>();
			switch (type)
			{
				case OUT:
					// Specified source, target only
					transforms.add(new TransformationDescriptor(generateTransformationName(sourceClass, targetClass), proxy, TransformationType.OUT));
					break;
				case IN:
					// Inverse only
					transforms.add(new TransformationDescriptor(generateTransformationName(targetClass, sourceClass), proxy, TransformationType.IN));
					break;
				case BIDIRECTIONAL:
					// Specified and inverse
					transforms.add(new TransformationDescriptor(generateTransformationName(sourceClass, targetClass), proxy, TransformationType.OUT));
					transforms.add(new TransformationDescriptor(generateTransformationName(targetClass, sourceClass), proxy, TransformationType.IN));
					break;
				default:
					break;
			}
			
			// ...and add them to the cache.
			for (TransformationDescriptor transform : transforms)
			{
				transformations.put(transform.getName(), transform);
			}
			
			logger.debug("Initialized transformation [" + proxy.getTransformer().getClass().getName() + "]: source = " + sourceClass.getName() + ", target = " + targetClass.getName() + ", direction = " + type);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private TransformationProxy getTransformationProxy(Class sourceClass, Class targetClass)
	{
		TransformationProxy result = null;

		// --- UserGuide lookupBeanByMetadata ---
		// Construct metadata to use to lookup transformation bean
		TransformationBeanMetadataLookupContainer container = new TransformationBeanMetadataLookupContainer();
		container.setSource(sourceClass.getCanonicalName());
		container.setTarget(targetClass.getCanonicalName());
		
		// Lookup the transformation bean using the specified metadata...
		Object bean = lookupManager.lookupBeanByMetadata(container);
		// --- UserGuide lookupBeanByMetadata ---
		
		// ...and create a proxy from it.
		if (bean != null)
		{
			result = new TransformationProxy(cacheManager, bean, isSurfaceExceptions());
			logger.debug("Created new transformation proxy from transformation bean [" + bean.getClass().getName() + "].");
		}
		
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	private void processGap(Class sourceClass, Class targetClass)
	{
		// Check to see whether a descriptor already exists for this source, target
		if (getTransformationDescriptor(sourceClass, targetClass) == null)
		{
			// If not, create a NOOP descritor...
			String transformName = generateTransformationName(sourceClass, targetClass);
			TransformationDescriptor noopDescriptor = new TransformationDescriptor(transformName, null, TransformationType.NONE);
			
			// ...and add it to the cache
			transformations.put(transformName, noopDescriptor);
		}
	}
}
