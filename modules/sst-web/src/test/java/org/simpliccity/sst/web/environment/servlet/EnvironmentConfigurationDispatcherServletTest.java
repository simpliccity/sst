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

package org.simpliccity.sst.web.environment.servlet;

import static org.junit.Assert.assertTrue;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;
import org.simpliccity.sst.web.environment.TestProfileEnvironmentConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.StaticWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class EnvironmentConfigurationDispatcherServletTest 
{
	private MockServletConfig servletConfig = new MockServletConfig(new MockServletContext());
	
	private DispatcherServlet servlet;
	
	@Before
	public void init()
	{
		servlet = new EnvironmentConfigurationDispatcherServlet();
		servlet.setContextClass(StaticWebApplicationContext.class);
	}
	
	@Test
	public void testValidConfigurer() throws ServletException
	{
		servletConfig.addInitParameter(EnvironmentConfigurationDispatcherServlet.PARAM_ENV_CONFIGURER, "org.simpliccity.sst.web.environment.TestProfileEnvironmentConfigurer");
		servlet.init(servletConfig);
		
		ConfigurableEnvironment environment = servlet.getEnvironment();
		assertTrue("Added active profile.", TestProfileEnvironmentConfigurer.hasProfile(environment));
	}
}
