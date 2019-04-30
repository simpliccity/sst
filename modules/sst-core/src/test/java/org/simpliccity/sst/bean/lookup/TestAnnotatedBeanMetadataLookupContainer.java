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

package org.simpliccity.sst.bean.lookup;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

public class TestAnnotatedBeanMetadataLookupContainer extends AbstractBeanMetadataLookupContainer<Service> 
{
	private static final long serialVersionUID = 1L;
	
	private static final String KEY_ATTRIBUTE_VALUE = "value";
	
	private String value;
	
	public TestAnnotatedBeanMetadataLookupContainer()
	{
		super(Service.class);
	}

	public TestAnnotatedBeanMetadataLookupContainer(Service svc)
	{
		super(Service.class);
		
		this.value = svc.value();
	}
	
	public String getValue() 
	{
		return value;
	}

	public void setValue(String value) 
	{
		this.value = value;
	}

	@Override
	public Map<String, Object> getMetadataAttributes() 
	{
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put(KEY_ATTRIBUTE_VALUE, getValue());
		
		return result;
	}
}
