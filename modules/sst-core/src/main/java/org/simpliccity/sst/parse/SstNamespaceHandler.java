/*
 *    Copyright 2012 Information Control Corporation
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

package org.simpliccity.sst.parse;

import org.simpliccity.sst.bean.lookup.config.BeanMetadataLookupManagerBeanDefinitionParser;
import org.simpliccity.sst.property.config.PropertyBeanDefinitionParser;
import org.simpliccity.sst.transform.config.TransformerBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * <p>A Spring {@link org.springframework.beans.factory.xml.NamespaceHandler} for the <code>sst</code>
 * namespace.  The <code>sst</code> namespace provides a convenient means to configure SST-specific
 * beans in a Spring application context XML configuration file.</p>
 * 
 * <p>The <code>sst</code> namespace can be referenced in an application context configuration file
 * by including the following namespace declaration in the <code>beans</code> tag:</p>
 * <pre>
 * xmlns:sst="http://www.simpliccity.org/schema/sst"
 * </pre>
 * <p>See the SST reference documentation for the corresponding <code>schemaLocation</code> for the current
 * version of the <code>sst</code> schema.</p>
 * 
 * @author Kevin Fox
 * @since 0.2.0
 *
 */
public class SstNamespaceHandler extends NamespaceHandlerSupport 
{
	@Override
	public void init() 
	{
		registerBeanDefinitionParser("transformer", new TransformerBeanDefinitionParser());
		registerBeanDefinitionParser("lookup", new BeanMetadataLookupManagerBeanDefinitionParser());
		registerBeanDefinitionParser("property", new PropertyBeanDefinitionParser());
	}
}
