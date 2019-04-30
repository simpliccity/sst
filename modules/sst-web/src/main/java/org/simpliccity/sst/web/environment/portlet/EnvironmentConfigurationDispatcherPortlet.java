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

import javax.portlet.PortletConfig;

import org.simpliccity.sst.environment.EnvironmentConfigurer;
import org.springframework.beans.BeanUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.ClassUtils;
import org.springframework.web.portlet.DispatcherPortlet;

public class EnvironmentConfigurationDispatcherPortlet extends DispatcherPortlet 
{
	public static final String PARAM_ENV_CONFIGURER = "environmentConfigurer";

	@Override
	protected ConfigurableEnvironment createEnvironment()
	{
		ConfigurableEnvironment environment = super.createEnvironment();
		
		EnvironmentConfigurer configurer = getConfigurerInstance();
		
		if (configurer != null)
		{
			environment = configurer.configureEnvironment(environment);
			logger.debug("Executed environment configuration using configurer class [" + configurer.getClass().getName() + "].");
		}
		
		return environment;
	}
	
	private EnvironmentConfigurer getConfigurerInstance()
	{
		EnvironmentConfigurer result = null;
		
		PortletConfig config = getPortletConfig();
		
		String parameter = config.getInitParameter(PARAM_ENV_CONFIGURER);
		
		Class<?> configurerClass = null;
		try 
		{
			logger.debug("Attempting to load configurer class [" + parameter + "].");
			configurerClass = ClassUtils.forName(parameter, null);
		} 
		catch (ClassNotFoundException | LinkageError e) 
		{
			logger.error("Unable to load configurer class [" + parameter + "].", e);
		}
		
		if (configurerClass != null && EnvironmentConfigurer.class.isAssignableFrom(configurerClass))
		{
			result = (EnvironmentConfigurer) BeanUtils.instantiate(configurerClass);
			logger.debug("Successfully created an instance of the specified configurer class [" + parameter + "].");
		}
		else
		{
			logger.warn("Unable to create an instance of the specified configurer class [" + parameter + "].");
		}
		
		return result;
	}
}
