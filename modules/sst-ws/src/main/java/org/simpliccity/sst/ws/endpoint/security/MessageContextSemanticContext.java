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

package org.simpliccity.sst.ws.endpoint.security;

import org.simpliccity.sst.service.security.ServiceSemanticContext;
import org.springframework.ws.context.MessageContext;

public class MessageContextSemanticContext implements ServiceSemanticContext<MessageContext>
{
	private MessageContext context;
	
	public MessageContextSemanticContext(MessageContext context)
	{
		this.context = context;
	}

	@Override
	public MessageContext getContext() 
	{
		return context;
	}

	@Override
	public Object getRequest() 
	{
		return context.getRequest();
	}

	@Override
	public Object getPayload() 
	{
		return context.getRequest().getPayloadSource();
	}

	@Override
	public Object[] getParameter(String name) 
	{
		return null;
	}

	@Override
	public String getAddress() 
	{
		// Unsupported operation
		return null;
	}
}
