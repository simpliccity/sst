/*
 *    Copyright 2016 Information Control Company
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

package org.simpliccity.sst.web.environment.portlet;

import static org.junit.Assert.assertTrue;

import javax.portlet.PortletException;

import org.junit.Before;
import org.junit.Test;
import org.simpliccity.sst.web.environment.TestProfileEnvironmentConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.mock.web.portlet.MockPortletConfig;
import org.springframework.mock.web.portlet.MockPortletContext;
import org.springframework.web.portlet.DispatcherPortlet;
import org.springframework.web.portlet.context.StaticPortletApplicationContext;

public class EnvironmentConfigurationDispatcherPortletTest 
{
	private MockPortletConfig portletConfig = new MockPortletConfig(new MockPortletContext());
	
	private DispatcherPortlet portlet;
	
	@Before
	public void init()
	{
		portlet = new EnvironmentConfigurationDispatcherPortlet();
		portlet.setContextClass(StaticPortletApplicationContext.class);
	}
	
	@Test
	public void testValidConfigurer() throws PortletException
	{
		portletConfig.addInitParameter(EnvironmentConfigurationDispatcherPortlet.PARAM_ENV_CONFIGURER, "org.simpliccity.sst.web.environment.TestProfileEnvironmentConfigurer");
		portlet.init(portletConfig);
		
		ConfigurableEnvironment environment = portlet.getEnvironment();
		assertTrue("Added active profile.", TestProfileEnvironmentConfigurer.hasProfile(environment));
	}
}
