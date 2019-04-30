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

package org.simpliccity.sst.flow.util;

import org.springframework.webflow.core.FlowException;

/**
 * A runtime exception indicating a problem executing within the context of a Spring Web Flow flow.
 * 
 * @author Kevin Fox
 * @since 1.0.0
 *
 */
public class SstFlowException extends FlowException 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new SstFlowException with a message.
	 * 
	 * @param msg The message describing the exception.
	 */
	public SstFlowException(String msg) 
	{
		super(msg);
	}

	/**
	 * Creates a new SstFlowException with a message and an
	 * underlying cause.
	 * 
	 * @param msg The message describing the exception.
	 * @param cause The exception that caused the SstFlowException.
	 */
	public SstFlowException(String msg, Throwable cause) 
	{
		super(msg, cause);
	}
}
