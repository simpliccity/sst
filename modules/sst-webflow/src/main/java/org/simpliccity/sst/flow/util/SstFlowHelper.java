/*
 *    Copyright 2010 Information Control Corporation
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

package org.simpliccity.sst.flow.util;

import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.stereotype.Component;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

/**
 * <p>A utility class that provides various convenience methods to simplify
 * working with Spring Web Flow.</p>
 * 
 * @author Kevin Fox
 *
 */
@Component
public class SstFlowHelper 
{
	/**
	 * <p>A convenience method that can be used to set the value of a flow variable to null.</p>
	 * 
	 * <p>Usage:</p>
	 * 
	 * <pre>
	 * &lt;evaluate expression="sstFlowHelper.generateNull()" result="flowScope.targetState" /&gt;
	 * </pre>
	 * 
	 * @return null
	 */
	public Object generateNull()
	{
		return null;
	}
	
	/**
	 * A convenience method to add a session attribute from a flow in a way that does not disrupt a transition.
	 * 
	 * 
	 * @param name The name of the session attribute to add.
	 * @param value The value of the session attribute to add.
	 */
	public void addSessionAttribute(String name, Object value)
	{
		ExternalContext externalContext = getRequestContext().getExternalContext();
		externalContext.getSessionMap().put(name, value);
	}
	
	/**
	 * A convenience method for adding an informational message to the {@link org.springframework.binding.message.MessageContext}
	 * of a web flow.  The message is available for display through the configured view technology.
	 * 
	 * @param messageKey The key for the message in the resource bundle.
	 * @param source The source of the message.
	 * @param parm Substitutable arguments for the message text.
	 */
	public void addInfoMessage(String messageKey, Object source, Object...parm)
	{
		MessageBuilder info = new MessageBuilder().info();
		addMessage(info, messageKey, source, parm);
	}

	/**
	 * A convenience method for adding a warning message to the {@link org.springframework.binding.message.MessageContext}
	 * of a web flow.  The message is available for display through the configured view technology.
	 * 
	 * @param messageKey The key for the message in the resource bundle.
	 * @param source The source of the message.
	 * @param parm Substitutable arguments for the message text.
	 */
	public void addWarnMessage(String messageKey, Object source, Object...parm)
	{
		MessageBuilder warn = new MessageBuilder().warning();
		addMessage(warn, messageKey, source, parm);
	}

	/**
	 * A convenience method for adding an error message to the {@link org.springframework.binding.message.MessageContext}
	 * of a web flow.  The message is available for display through the configured view technology.
	 * 
	 * @param messageKey The key for the message in the resource bundle.
	 * @param source The source of the message.
	 * @param parm Substitutable arguments for the message text.
	 */
	public void addErrorMessage(String messageKey, Object source, Object...parm)
	{
		MessageBuilder error = new MessageBuilder().error();
		addMessage(error, messageKey, source, parm);
	}

	/**
	 * A convenience method for adding a generic message to the {@link org.springframework.binding.message.MessageContext}
	 * of a web flow.  The message is available for display through the configured view technology.
	 * 
	 * @param builder The {@link org.springframework.binding.message.MessageBuilder} to
	 * use to generate the message.
	 * @param messageKey The key for the message in the resource bundle.
	 * @param source The source of the message.
	 * @param parm Substitutable arguments for the message text.
	 */	
	public void addMessage(MessageBuilder builder, String messageKey, Object source, Object...parm)
	{
		MessageContext messageContext = getRequestContext().getMessageContext();
		
		MessageBuilder message = builder.source(source).code(messageKey);
		
		if (parm != null)
		{
			message = message.resolvableArgs(parm);
		}
		
		messageContext.addMessage(message.build());
	}
	
	private RequestContext getRequestContext()
	{
		RequestContext context = RequestContextHolder.getRequestContext();
		if (context == null)
		{
			throw new SstFlowException("Unable to retrieve the current request context. May not be executing within the context of a flow.");
		}
		
		return context;
	}
}
