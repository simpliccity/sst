/*
 *    Copyright 2014 Information Control Corporation
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

import java.security.Principal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.bean.lookup.BeanMetadataLookupManager;
import org.simpliccity.sst.service.security.annotation.SemanticConstraint;
import org.simpliccity.sst.service.security.annotation.ServiceAccessValidatorBeanMetadataLookupContainer;
import org.simpliccity.sst.service.security.annotation.ServiceContentExtractorBeanMetadataLookupContainer;

/**
 * Evaluates an individual semantic constraint, as defined in {@link org.simpliccity.sst.service.security.annotation.SecuredMessage#restrictions()},
 * for a service request.  The details of the constraint are specified in an {@code @SemanticConstraint} annotation.
 * 
 * @author Kevin Fox
 * @since 1.0.0
 * 
 * @see SecuredMessageEvaluator
 *
 */
public class SemanticConstraintEvaluator 
{
	private Log logger = LogFactory.getLog(SemanticConstraintEvaluator.class);
	
	private SemanticConstraint constraint;
	private BeanMetadataLookupManager lookupManager;
	private SemanticContentExtractor extractor;
	private ServiceAccessControlRule validation;
	
	/**
	 * Creates a new instance of SemanticConstraintEvaluator initialized with the message constraint definition and the bean metadata
	 * lookup manager.
	 * 
	 * @param constraint The message constraint definition.
	 * @param lookupManager The manager used to lookup beans via metadata.
	 */
	public SemanticConstraintEvaluator(SemanticConstraint constraint, BeanMetadataLookupManager lookupManager) 
	{
		this.constraint = constraint;
		this.lookupManager = lookupManager;
		
		initValidator();
		initExtractor();
	}
	
	/**
	 * Evaluates whether or not the service request made by the specified user passes the message
	 * constraint defined by the associated {@link SemanticConstraint} annotation.
	 * 
	 * @param semanticContext The context of a service request.
	 * @param user The authenticated user placing the service call.
	 * @return <code>true</code> if the access control constraint passes; <code>false</code> otherwise.
	 */
	public boolean evaluate(ServiceSemanticContext<?> semanticContext, Principal user)
	{
		logger.debug("Processing restriction: user [" + user.getName() + "], check [" + constraint.semanticRule() + "], extractor [" + constraint.contentExtractor() + "], spec [" + constraint.contentSpec() + "]");

		// Get the specified message content (possibly multiple values)
		Object[] content = extractor.extractContent(semanticContext, constraint);
		
		// Use the specified rule to check access for each value in the content
		boolean result = false;
		for (Object value : content)
		{
			result = validation.allowAccess(user, value);
			
			// If access fails for any value, the entire constraint fails
			if (!result)
			{
				break;
			}
		}
		
		logger.debug("Restriction " + (result ? "passed" : "failed") + ": user [" + user.getName() + "], check [" + constraint.semanticRule() + "], extractor [" + constraint.contentExtractor() + "], spec [" + constraint.contentSpec() + "]");
		
		return result;
	}
	
	/**
	 * Determines whether this constraint applies for the service request, based on whether the authenticated
	 * user is in any of the roles specified in {@link SemanticConstraint#applicableRoles()}.
	 * 
	 * @param handler An instance of {@link ServiceSecurityHandler} used as an adapter for the underlying security implementation.
	 * @param semanticContext The context of a service request.
	 * @return <code>true</code> if the authenticated user has any of the specified roles or no roles have been specified; 
	 * <code>false</code> otherwise.
	 */
	public boolean isApplicable(ServiceSecurityHandler handler, ServiceSemanticContext<?> semanticContext)
	{
		// A constraint without applicable roles always applies
		return (constraint.applicableRoles().length == 0) ? true : handler.inApplicableRole(semanticContext, constraint.applicableRoles());
	}

	private void initValidator()
	{
		// Construct metadata used to lookup access validator bean
		ServiceAccessValidatorBeanMetadataLookupContainer container = new ServiceAccessValidatorBeanMetadataLookupContainer();
		container.setValue(constraint.semanticRule());
		
		// Lookup the bean using the specified metadata
		validation = (ServiceAccessControlRule) lookupManager.lookupBeanByMetadata(container);
		
		// To avoid an error, return the most restrictive checker if no other is found
		validation =  validation == null ? new RestrictiveServiceAccessControlRule() : validation;
		
		logger.debug("Returning an instance of " + validation.getClass().getName() + " for the specified access control rule [" + constraint.semanticRule() + "].");
	}
	
	private void initExtractor()
	{
		// Construct metadata used to lookup content extractor bean
		ServiceContentExtractorBeanMetadataLookupContainer container = new ServiceContentExtractorBeanMetadataLookupContainer();
		container.setValue(constraint.contentExtractor());
		
		// Lookup the bean using the specified metadata
		extractor = (SemanticContentExtractor) lookupManager.lookupBeanByMetadata(container);
		
		// To avoid an error, return the most restrictive checker if no other is found
		extractor =  extractor == null ? new EmptySemanticContentExtractor() : extractor;
		
		logger.debug("Returning an instance of " + extractor.getClass().getName() + " for the specified content extractor [" + constraint.contentExtractor() + "].");
	}
}

/**
 * Default access control rule to apply if none specified.  Denies access to all users.
 * 
 * @author Kevin Fox
 *
 */
class RestrictiveServiceAccessControlRule implements ServiceAccessControlRule
{
	@Override
	public boolean allowAccess(Principal user, Object content) 
	{
		return false;
	}
}

class EmptySemanticContentExtractor implements SemanticContentExtractor
{
	@Override
	public Object[] extractContent(ServiceSemanticContext<?> semanticContext, SemanticConstraint constraint) 
	{
		return new Object[0];
	}
}
