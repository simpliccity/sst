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

package org.simpliccity.sst.ws.endpoint.security;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.simpliccity.sst.service.security.annotation.ServiceAccessValidator;
import org.simpliccity.sst.service.security.annotation.ServiceAccessValidatorBeanMetadataLookupContainer;

public class AccessValidatorBeanMetadataLookupContainerTest 
{
	private static final String TEST_VALUE = "Testing";
	
	@Test
	public void testDiscriminator()
	{
		ServiceAccessValidatorBeanMetadataLookupContainer container = new ServiceAccessValidatorBeanMetadataLookupContainer();

		Assert.assertSame("Valid discriminator", ServiceAccessValidator.class, container.getLookupDiscriminator());
	}
	
	@Test
	public void testAccessors()
	{
		ServiceAccessValidatorBeanMetadataLookupContainer container = new ServiceAccessValidatorBeanMetadataLookupContainer();
		
		container.setValue(TEST_VALUE);
		
		Assert.assertSame("Valid value", TEST_VALUE, container.getValue());
	}
	
	@Test
	public void testAttributes()
	{
		ServiceAccessValidatorBeanMetadataLookupContainer container = new ServiceAccessValidatorBeanMetadataLookupContainer();
		
		container.setValue(TEST_VALUE);

		Map<String, Object> attributes = container.getMetadataAttributes();
		
		Assert.assertSame("Valid value attribute", TEST_VALUE, attributes.get(ServiceAccessValidatorBeanMetadataLookupContainer.KEY_ATTRIBUTE_VALUE));
	}
}
