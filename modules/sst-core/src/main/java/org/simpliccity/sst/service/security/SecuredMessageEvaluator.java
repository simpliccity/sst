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
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.bean.lookup.BeanMetadataLookupManager;
import org.simpliccity.sst.service.security.annotation.ApplyWhen;
import org.simpliccity.sst.service.security.annotation.SecuredMessage;
import org.simpliccity.sst.service.security.annotation.SemanticConstraint;

/**
 * <p>Applies the access control check defined by an {@code @SecuredMessage} annotation to a service request.
 * {@link AbstractSecuredMessageProcessor#evaluateAccessControl(ServiceSemanticContext, Object, ApplyWhen)}
 * uses an instance of this class related to a specific service endpoint to make the access control decision
 * for a corresponding service request.</p>
 * 
 * <p>A {@code @SecuredMessage} annotation will include definitions for one or more {@link SemanticConstraint}
 * access control constraints.  Each {@link SemanticConstraint} is evaluated individually by an instance of
 * {@link SemanticConstraintEvaluator}.  This class collects the "votes" from the various constraint
 * evaluators and determines the final result, based on the configured {@link SecuredMessage#pass()}
 * option.</p>
 *  
 * @author Kevin Fox
 * @since 1.0.0
 * 
 * @see SecuredMessage
 *
 */
public class SecuredMessageEvaluator 
{
	private Log logger = LogFactory.getLog(SecuredMessageEvaluator.class);
	
	private SecuredMessage control;
	private ServiceSecurityHandler securityHandler;
	private SemanticConstraintEvaluator[] restrictions;

	/**
	 * Creates a new instance of SecuredMessageEvaluator initialized with the message control definition, the service security
	 * handler and the bean metadata lookup manager.
	 * 
	 * @param control The message control definition.
	 * @param securityHandler An instance of {@link ServiceSecurityHandler} used as an adapter for the underlying security implementation.
	 * @param lookupManager The manager used to lookup beans via metadata.
	 */
	public SecuredMessageEvaluator(SecuredMessage control, ServiceSecurityHandler securityHandler, BeanMetadataLookupManager lookupManager) 
	{
		this.control = control;
		this.securityHandler = securityHandler;
		
		initRestrictions(lookupManager);
	}
	
	/**
	 * Evaluates whether or not the service request passes the message access control check defined
	 * by the associated {@link SecuredMessage} annotation.
	 * 
	 * @param semanticContext The context of a service request.
	 * @return <code>true</code> if the access control check passes; <code>false</code> otherwise.
	 */
	public boolean evaluate(ServiceSemanticContext<?> semanticContext) throws ServiceAccessException
	{
		// See if user in exempt role
		boolean allow = securityHandler.inApplicableRole(semanticContext, control.roleExclusions());
		
		// If user not exempt...
		if (!allow)
		{
			logger.debug("Control has " + restrictions.length + " domain access restriction(s) to process");
			
			Principal user = securityHandler.getPrincipal(semanticContext);
			if (user == null)
			{
				throw new ServiceAccessException("The service security handler is unable to identify an authenticated user for the service call. Access to the serivce is automatically denied.");
			}
			
			// ...evaluate individual restrictions
			int applicableConstraints = 0;
			int met = 0;
			for (SemanticConstraintEvaluator restriction : restrictions)
			{
				// Check whether the current restriction applies,...
				if (restriction.isApplicable(securityHandler, semanticContext))
				{
					// ...keeping track of the total number of applicable restrictions,...
					applicableConstraints++;
					// ...and incrementing the count of met restrictions, if it is.
					met += restriction.evaluate(semanticContext, user) ? 1 : 0;
				}				
			}
			
			// If enough applicable restrictions have been met, pass the check
			allow = met >= getPassingCount(applicableConstraints);
		}
		
		return allow;
	}
	
	/**
	 * Determines whether this access control check is applicable at the specified point in
	 * the service request lifecycle.
	 * 
	 * @param when The desired point in the service request lifecycle to apply the access control
	 * check.
	 * @return <code>true</code> if the access control check is applicable at the desired point
	 * in the service request lifecycle; <code>false</code> otherwise.
	 */
	public boolean applies(ApplyWhen when)
	{
		return control.apply() == when;
	}
	
	private void initRestrictions(BeanMetadataLookupManager lookupManager)
	{
		ArrayList<SemanticConstraintEvaluator> executors = new ArrayList<>();
		
		for (SemanticConstraint restriction : control.restrictions())
		{
			executors.add(new SemanticConstraintEvaluator(restriction, lookupManager));
		}
		
		restrictions = executors.toArray(new SemanticConstraintEvaluator[executors.size()]);
	}

	private int getPassingCount(int total)
	{
		// Determines how many of the semantic constraints must pass, based on the selected pass option
		int required;
		switch (control.pass()) 
		{
			case ANY:
				required = 1;
				break;
			case MAJORITY:
				required = total / 2 + 1;
				break;
			case ALL:
			default:
				required = Math.max(total, 1);
				break;
		}

		return required;
	}
}
