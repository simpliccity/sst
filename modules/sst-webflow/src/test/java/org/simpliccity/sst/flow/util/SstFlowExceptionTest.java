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

package org.simpliccity.sst.flow.util;

import org.junit.Assert;
import org.junit.Test;

public class SstFlowExceptionTest 
{
	private static final String TEST_MESSAGE = "Testing";
	
	@Test
	public void testMessageOnlyConstructor()
	{
		Exception e = new SstFlowException(TEST_MESSAGE);
		
		Assert.assertSame("Valid message", TEST_MESSAGE, e.getMessage());
		Assert.assertNull("Null cause", e.getCause());
	}

	@Test
	public void testMessageAndCauseConstructor()
	{
		Exception cause = new Exception();
		Exception e = new SstFlowException(TEST_MESSAGE, cause);
		
		Assert.assertSame("Valid message", TEST_MESSAGE, e.getMessage());
		Assert.assertSame("Valid cause", cause, e.getCause());
	}
}
