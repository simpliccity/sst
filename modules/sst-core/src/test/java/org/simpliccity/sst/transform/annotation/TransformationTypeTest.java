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

package org.simpliccity.sst.transform.annotation;

import junit.framework.TestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/*
 * @author vSomisetty
 * @Created on : 03/09/2010 
 * @Description - This JUnit is used to test the TransformationType Enumerator.
 * 
 */
public class TransformationTypeTest extends TestCase 
{
	@Test
	public void testTransformationTypeEnum()
	{
		 assertThat(TransformationType.valueOf("OUT"), is(notNullValue()));
		 assertThat(TransformationType.valueOf("IN"), is(notNullValue()));
		 assertThat(TransformationType.valueOf("BIDIRECTIONAL"), is(notNullValue()));
	}
}
