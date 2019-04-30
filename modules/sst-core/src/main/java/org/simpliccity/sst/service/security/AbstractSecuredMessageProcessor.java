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

package org.simpliccity.sst.service.security;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.bean.lookup.BeanMetadataLookupManager;
import org.simpliccity.sst.service.security.annotation.ApplyWhen;
import org.simpliccity.sst.service.security.annotation.SecuredMessage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * <p>An abstract base class that incorporates the primary message processing logic and extension points for the 
 * <code>access</code> framework.  Subclasses of this class can be used to adapt the <code>access</code>
 * framework for use with various service processing frameworks.  Such subclasses would generally leverage
 * interceptor type functionality within the service framework to ensure that constraints are applied to 
 * service requests in a cross-cutting fashion, without requiring changes to existing application code.</p>
 * 
 *  <p>A concrete subclass must call the {@link #evaluateAccessControl(ServiceSemanticContext, Object, ApplyWhen)}
 *  method to process the access control decision at the appropriate point in the service request lifecycle.  The
 *  subclass is responsible for enforcing the access control result by returning an appropriate error response
 *  if access is denied.</p>
 *  
 * <p>The evaluate method will publish the result of the access control decision as a Spring application
 * event.  An {@link org.springframework.context.ApplicationListener} for {@link ServiceAccessControlEvent}
 * can be used to process these events, for example to maintain an audit log.</p>
 * 
 * @author Kevin Fox
 * @since 1.0.0
 *
 */
public abstract class AbstractSecuredMessageProcessor implements ApplicationEventPublisherAware, InitializingBean
{
	private Log logger = LogFactory.getLog(this.getClass());
	
	private ApplicationEventPublisher eventPublisher;
	
	private ServiceSecurityHandler securityHandler;
	
	private Map<Object, SecuredMessageEvaluator> controls = new HashMap<>();

	@Autowired
	private BeanMetadataLookupManager lookupManager;
	
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher)
	{
		this.eventPublisher = applicationEventPublisher;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception 
	{
		if (getSecurityHandler() == null)
		{
			throw new ServiceAccessException("Security handler bean not specified.");
		}
	}
	
	/**
	 * Retrieves the {@link ServiceSecurityHandler} bean configured to integrate with the underlying
	 * security framework.
	 * 
	 * @return The configured {@link ServiceSecurityHandler}.
	 */
	public ServiceSecurityHandler getSecurityHandler() 
	{
		return securityHandler;
	}

	/**
	 * Specifies the {@link ServiceSecurityHandler} bean which integrates with the underlying
	 * security framework.
	 * 
	 * @param securityHandler The {@link ServiceSecurityHandler} bean which integrates with the 
	 * underlying security framework.
	 */
	public void setSecurityHandler(ServiceSecurityHandler securityHandler) 
	{
		this.securityHandler = securityHandler;
	}
	
	/**
	 * <p>Retrieves the {@code @SecuredMessage} annotation associated with the service request.  
	 * {@link SecuredMessage} provides the details of the security constraint defined for the
	 * service request.</p>
	 * 
	 * <p>Since the specifics of how a particular request is mapped to an element that can be annotated 
	 * (e.g. class, method) are managed by the underlying service framework, the implementation of this
	 * method must be provided by the concrete message processor subclass for the service framework.</p>
	 *  
	 * @param controlledObject The annotated object, as provided by {@link #getControlledObject(Object, ServiceSemanticContext)}.
	 * @param semanticContext The context of a service request.
	 * @return The {@link SecuredMessage} annotation that defines the security constraint(s) applicable to
	 * the service request.
	 */
	protected abstract SecuredMessage getAnnotation(Object controlledObject, ServiceSemanticContext<?> semanticContext);
	
	/**
	 * Retrieves the element associated with the service request that is annotated with the {@code @SecuredMessage} 
	 * annotation that specifies the security constraint(s) to be applied.  The default behavior returns the
	 * specified service object.  Concrete subclasses can override this method if further logic is needed to
	 * extract the actual annotated element.
	 * 
	 * @param service The service framework-specific representation of the service endpoint processing
	 * a request.
	 * @param semanticContext The context of a service request.
	 * @return The element marked with the {@link SecuredMessage} annotation for the specified request.
	 */
	protected Object getControlledObject(Object service, ServiceSemanticContext<?> semanticContext)
	{
		// Default implementation
		return service;
	}
	
	/**
	 * Returns the {@link Log} instance used by the class to log messages.
	 * 
	 * @return The {@link Log} instance for this class.
	 */
	protected final Log getLogger()
	{
		return logger;
	}
	
	/**
	 * Retrieves the {@link ApplicationEventPublisher} bean for the Spring context.
	 * 
	 * @return The {@link ApplicationEventPublisher} bean for the Spring context.
	 */
	protected final ApplicationEventPublisher getEventPublisher()
	{
		return eventPublisher;
	}
	
	/**
	 * Retrieves the {@link BeanMetadataLookupManager} bean used to retrieve Spring
	 * beans by metadata.
	 * 
	 * @return The configured {@link BeanMetadataLookupManager} bean.
	 */
	protected final BeanMetadataLookupManager getLookupManager()
	{
		return lookupManager;
	}

	/**
	 * <p>Applies the access controls to a secured service request.  The concrete subclass for a 
	 * particular service framework should directly call this method either before or after 
	 * (as indicated by the {@link ApplyWhen} parameter) executing the service logic for the 
	 * request.</p>
	 * 
	 * <p>If this method returns <code>false</code>, the service request should fail
	 * with an appropriate error response.  If the return is {@code false} and the {@link ApplyWhen} 
	 * parameter has a value of {@code PRE}, the service logic should not be executed.  The method
	 * may also throw an exception if it is unable to process the access controls.  The concrete
	 * subclass must decide on the appropriate behavior in this case.</p>
	 * 
	 * <p>This method relies on {@link SecuredMessageEvaluator} to evaluate the constraint(s) for
	 * a service request.  The appropriate evaluator for a specific request is retrieved using
	 * {@link #getControl(Object, ServiceSemanticContext, ApplyWhen)}.</p>
	 * 
	 * <p>This method will publish the result of the access control decision as a Spring application
	 * event.  An {@link org.springframework.context.ApplicationListener} for {@link ServiceAccessControlEvent}
	 * can be used to process these events, for example to maintain an audit log.</p>
	 * 
	 * @param semanticContext The context of a service request.
	 * @param service The service framework-specific representation of the service endpoint processing
	 * a request.
	 * @param apply An indication of when the access control is being applied, either before ({@code ApplyWhen.PRE})
	 * or after ({@code ApplyWhen.POST}) the service logic has been executed.
	 * @return {@code true} if access is allowed; {@code false} otherwise.
	 * @throws ServiceAccessException If the framework cannot process the access controls.
	 * 
	 * @see SecuredMessageEvaluator
	 */
	protected final boolean evaluateAccessControl(ServiceSemanticContext<?> semanticContext, Object service, ApplyWhen apply) throws ServiceAccessException
	{
		boolean allow = true;
		
		// Get access control definition (will shortcut check if none specified)
		SecuredMessageEvaluator control = getControl(service, semanticContext, apply);

		// If access control has been defined...
		if (control != null)
		{
			// ...evaluate it
			allow = control.evaluate(semanticContext);			
			logger.debug("Access " + (allow ? "allowed" : "denied") + " for service implementation [" + service + "]");
		}
		
		// Publish event indicating the access control decision - useful for auditing purposes
		getEventPublisher().publishEvent(getEvent(service, semanticContext, allow));
		
		return allow;
	}
	
	/**
	 * Retrieves the {@link SecuredMessageEvaluator} used to process the access controls for a service request.
	 * To improve performance, this class maintains an internal cache of evaluators.  The method will retrieve
	 * an evaluator from the cache, if available.  Otherwise, it will construct one from the {@code @SecuredMessage}
	 * annotation on the controlled object (see {@link #getControlledObject(Object, ServiceSemanticContext)}) associated
	 * with the request.
	 * 
	 * @param service The service framework-specific representation of the service endpoint processing a request.
	 * @param semanticContext The context of a service request.
	 * @param when An indication of when the access control is being applied, either before ({@code ApplyWhen.PRE})
	 * or after ({@code ApplyWhen.POST}) the service logic has been executed.
	 * @return The {@link SecuredMessageEvaluator} used to evaluate the access controls specified for the service request.
	 */
	protected final SecuredMessageEvaluator getControl(Object service, ServiceSemanticContext<?> semanticContext, ApplyWhen when)
	{
		// Get the actual controlled object for the service (this maybe a dependent or derivative object)
		Object controlledObject = getControlledObject(service, semanticContext);
		
		// Look for a payload control evaluator in the cache
		SecuredMessageEvaluator control = controls.get(controlledObject);
		
		// If none is found...
		if (control == null)
		{
			logger.debug("Creating new secured message evaluator for service implementation [" + controlledObject + "].");
			
			// ...get the payload control annotation from the endpoint
			SecuredMessage annotation = getAnnotation(controlledObject, semanticContext);
			
			// If the annotation information is found and applies ...
			if (annotation != null && annotation.apply() == when)
			{
				// ...create a corresponding evaluator and add it to the cache
				control = new SecuredMessageEvaluator(annotation, getSecurityHandler(), getLookupManager());
				controls.put(controlledObject, control);
			}
		}
			
		if (control != null && control.applies(when))
		{
			logger.debug("Access control defined for endpoint.");
		}
		else
		{
			control = null;
			logger.debug("No access control defined for endpoint.");
		}
		
		return control;
	}
	
	/**
	 * Generates an application event for the access control decision applied to the service request.
	 * 
	 * @param service The service framework-specific representation of the service endpoint processing a request.
	 * @param semanticContext The context of a service request.
	 * @param allow The access control decision generated by {@link #evaluateAccessControl(ServiceSemanticContext, Object, ApplyWhen)}.
	 * @return A {@link ServiceAccessControlEvent} application event.
	 */
	protected final ServiceAccessControlEvent getEvent(Object service, ServiceSemanticContext<?> semanticContext, boolean allow)
	{
		return new ServiceAccessControlEvent(service, semanticContext, getSecurityHandler().getPrincipal(semanticContext), allow);
	}
}
