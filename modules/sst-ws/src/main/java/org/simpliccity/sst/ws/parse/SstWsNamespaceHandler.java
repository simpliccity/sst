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

package org.simpliccity.sst.ws.parse;

import org.simpliccity.sst.ws.endpoint.security.config.AccessValidatorBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * <p>A Spring {@link org.springframework.beans.factory.xml.NamespaceHandler} for the <code>sstws</code>
 * namespace.  The <code>sstws</code> namespace provides a convenient means to configure SSTWS-specific
 * beans in a Spring application context XML configuration file.</p>
 * 
 * <p>The <code>sstws</code> namespace can be referenced in an application context configuration file
 * by including the following namespace declaration in the <code>beans</code> tag:</p>
 * <pre>
 * xmlns:sstws="http://www.simpliccity.org/schema/sstws"
 * </pre>
 * <p>See the SST reference documentation for the corresponding <code>schemaLocation</code> for the current
 * version of the <code>sstws</code> schema.</p>
 * 
 * @author Kevin Fox
 * @since 1.0.0
 *
 */
public class SstWsNamespaceHandler extends NamespaceHandlerSupport 
{
	@Override
	public void init() 
	{
		registerBeanDefinitionParser("access", new AccessValidatorBeanDefinitionParser());
	}
}
