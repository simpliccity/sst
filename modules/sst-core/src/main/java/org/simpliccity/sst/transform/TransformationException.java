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

/**
 * A runtime exception used to wrap an exception thrown by the invocation of a transformer method.
 * 
 * @author Kevin Fox
 * @since 0.3.2
 *
 */
public class TransformationException extends RuntimeException 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new TransformationException with an underlying cause.
	 * 
	 * @param cause The exception that caused the TransformerException.
	 */
	public TransformationException(Throwable cause) 
	{
		super(cause);
	}

	/**
	 * Creates a new TransformationException with a message and an
	 * underlying cause.
	 * 
	 * @param msg The message describing the exception.
	 * @param cause The exception that caused the TransformationException.
	 */
	public TransformationException(String msg, Throwable cause) 
	{
		super(msg, cause);
	}
}
