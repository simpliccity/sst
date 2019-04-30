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
 * An exception indicating that a requested transformation cannot be completed.
 * 
 * @author Kevin Fox
 * @since 0.3.0
 * 
 * @see TransformationProxy
 *
 */
public class UnsupportedTransformationException extends Exception 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new UnsupportedTransformationException.
	 */
	public UnsupportedTransformationException() 
	{
		super();
	}

	/**
	 * Creates a new UnsupportedTransformationException with a message.
	 * 
	 * @param msg The message describing the exception.
	 */
	public UnsupportedTransformationException(String msg) 
	{
		super(msg);
	}

	/**
	 * Creates a new UnsupportedTransformationException with an underlying cause.
	 * 
	 * @param cause The exception that caused the UnsupportedTransformationException.
	 */
	public UnsupportedTransformationException(Throwable cause) 
	{
		super(cause);
	}

	/**
	 * Creates a new UnsupportedTransformationException with a message and an
	 * underlying cause.
	 * 
	 * @param msg The message describing the exception.
	 * @param cause The exception that caused the UnsupportedTransformationException.
	 */
	public UnsupportedTransformationException(String msg, Throwable cause) 
	{
		super(msg, cause);
	}
}
