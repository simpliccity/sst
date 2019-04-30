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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.transform.annotation.TransformationType;

/**
 * A class representing an individual type of transformation, using the proxy
 * for a transformation bean.  This class mediates between {@link Transformer},
 * which does not require that a transformation direction be specified, and
 * {@link TransformationProxy} which must invoke the appropriate transformation
 * method for the desired direction.  {@link Transformer} locates the appropriate
 * instance of this class by name, based on the source and target classes for the
 * transformation, and uses it to invoke the corresponding method call to the underlying
 * proxy.
 * 
 * @author Kevin Fox
 * @since 0.3.0
 * 
 * @see Transformer
 *
 */
public class TransformationDescriptor 
{
	private static final String CLASSNAME_NO_PROXY = "UNSUPPORTED TRANSFORMATION";
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	private String name;
	private TransformationProxy proxy;
	private TransformationType direction;
	
	/**
	 * Creates a new TransformationDescriptor instance using the specified name, transformation
	 * proxy and transformation direction.
	 * 
	 * @param name The name used to identify this descriptor.
	 * @param proxy The proxy used to perform the desired transformation.
	 * @param direction The direction of the transformation to be performed.
	 */
	public TransformationDescriptor(String name, TransformationProxy proxy, TransformationType direction)
	{
		this.name = name;
		this.proxy = proxy;
		this.direction = direction;
	}
	
	/**
	 * Returns the name by which this descriptor can be located.
	 * 
	 * @return The name of this descriptor.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Returns the proxy for the desired transformation.
	 * 
	 * @return The transformation proxy.
	 */
	public TransformationProxy getProxy()
	{
		return proxy;
	}
	
	/**
	 * Returns the direction of the desired transformation.
	 * 
	 * @return The transformation direction.
	 */
	public TransformationType getDirection()
	{
		return direction;
	}
	
	/**
	 * Returns the class name of the underlying transformation bean.
	 * 
	 * @return The transformation bean class name.
	 */
	public String getTransformationClassName()
	{
		return (proxy == null) ? CLASSNAME_NO_PROXY : proxy.getTransformer().getClass().getName();
	}
	
	/**
	 * Performs the transformation represented by this descriptor on the source object.
	 * The actual transformation is delegated to the appropriate method on the underlying
	 * proxy.
	 * 
	 * @param source The instance to be transformed.
	 * @param targetClass The class to transform to.
	 * @param <T> The resulting type of the transformation.
	 * @param forceFlush A flag indicating whether to flush the object cache upon completion of this transformation.
	 * @return An instance of the target class produced by applying a transformation to the source instance;
	 * <code>null</code> if the transformation cannot be performed.
	 * 
	 * @see TransformationProxy
	 */
	public <T> T performTransformation(Object source, Class<T> targetClass, boolean forceFlush)
	{
		T result = null;
		
		// Delegate to the appropriate transformation method on the proxy
		try
		{
			if (direction == TransformationType.OUT)
			{
				result = proxy.performOutTransformation(source, targetClass, forceFlush);
			}
			else if (direction == TransformationType.IN)
			{
				result = proxy.performInTransformation(source, targetClass, forceFlush);
			}
			else if (direction == TransformationType.NONE)
			{
				result = null;
			}
		}
		catch (UnsupportedTransformationException e)
		{
			logger.debug("Unsupported transformation.", e);
			result = null;
		}
		
		return result;
	}
}
