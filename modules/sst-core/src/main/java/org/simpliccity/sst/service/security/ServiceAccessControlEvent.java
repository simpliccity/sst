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

import java.security.Principal;

import org.springframework.context.ApplicationEvent;

/**
 * A Spring application event that denotes an access control decision made for a service request by the <code>access</code> framework.  Events are published by 
 * {@link AbstractSecuredMessageProcessor#evaluateAccessControl(ServiceSemanticContext, Object, org.simpliccity.sst.service.security.annotation.ApplyWhen)}.
 * 
 * @author Kevin Fox
 * @since 1.0.0
 *
 */
public class ServiceAccessControlEvent extends ApplicationEvent 
{
	private static final long serialVersionUID = 1L;
	
	private String serviceAddress;
	
	private String principal;
	
	private String principalName;
	
	private boolean allowed;
	
	/**
	 * Creates a new instance of the event with all applicable details.
	 * 
	 * @param source The endpoint for processing a service request, in the underlying service framework.
	 * @param serviceContext The context of a service request.
	 * @param principal The authenticated user associated with the service request.
	 * @param allowed The access control decision applied by the <code>access</code> framework.
	 */
	public ServiceAccessControlEvent(Object source, ServiceSemanticContext<?> serviceContext, Principal principal, boolean allowed) 
	{
		super(source);
		
		this.serviceAddress = serviceContext.getAddress();
		this.principal = principal.toString();
		this.principalName = principal.getName();
		this.allowed = allowed;
	}

	/**
	 * Retrieves the address of the service request, as defined in the {@link ServiceSemanticContext}.
	 * 
	 * @return The service request address.
	 */
	public String getServiceAddress() 
	{
		return serviceAddress;
	}

	/**
	 * Specifies the service request address.
	 * 
	 * @param serviceAddress The service request address.
	 */
	public void setServiceAddress(String serviceAddress) 
	{
		this.serviceAddress = serviceAddress;
	}

	/**
	 * Retrieves the authenticated user associated with the service request.
	 * 
	 * @return The authenticated user of the service request.
	 */
	public String getPrincipal() 
	{
		return principal;
	}

	/**
	 * Specifies the authenticated user associated with the service request.
	 * 
	 * @param principal The authenticated user of the service request.
	 */
	public void setPrincipal(String principal) 
	{
		this.principal = principal;
	}

	/**
	 * Retrieves the name of the authenticated user associated with the service request.
	 * 
	 * @return The name of the authenticated user of the service request.
	 */
	public String getPrincipalName() 
	{
		return principalName;
	}

	/**
	 * Specifies the name of the authenticated user associated with the service request.
	 * 
	 * @param principalName The name of the authenticated user of ther service request.
	 */
	public void setPrincipalName(String principalName) 
	{
		this.principalName = principalName;
	}

	/**
	 * Retrieves the access control decision applied to the service request.
	 * 
	 * @return The access control decision for the service request.
	 */
	public boolean isAllowed() 
	{
		return allowed;
	}

	/**
	 * Specifies the access control decision applied to the service request.
	 * 
	 * @param allowed The access control decision for the service request.
	 */
	public void setAllowed(boolean allowed) 
	{
		this.allowed = allowed;
	}
}
