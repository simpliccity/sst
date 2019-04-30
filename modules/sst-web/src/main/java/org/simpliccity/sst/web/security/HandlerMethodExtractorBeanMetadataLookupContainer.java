/*
 *    Copyright 2016 Information Control Corporation
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

package org.simpliccity.sst.web.security;

import java.util.HashMap;
import java.util.Map;

import org.simpliccity.sst.bean.lookup.AbstractBeanMetadataLookupContainer;

public class HandlerMethodExtractorBeanMetadataLookupContainer extends AbstractBeanMetadataLookupContainer<HandlerMethodExtractor> 
{
	private static final long serialVersionUID = 1L;

	private String target;
	
	public HandlerMethodExtractorBeanMetadataLookupContainer() 
	{
		super(HandlerMethodExtractor.class);
	}
	
	public HandlerMethodExtractorBeanMetadataLookupContainer(String target)
	{
		super(HandlerMethodExtractor.class);

		this.target = target;
	}

	@Override
	public Map<String, Object> getMetadataAttributes() 
	{
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("target", getTarget());
		
		return attributes;
	}

	public String getTarget() 
	{
		return target;
	}

	public void setTarget(String target) 
	{
		this.target = target;
	}
}
