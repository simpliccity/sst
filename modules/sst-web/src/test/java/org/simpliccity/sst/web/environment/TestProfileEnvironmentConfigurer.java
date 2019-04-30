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

package org.simpliccity.sst.web.environment;

import org.simpliccity.sst.environment.EnvironmentConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;

public class TestProfileEnvironmentConfigurer implements EnvironmentConfigurer
{
	public static final String TEST_PROFILE = "sstEnvironmentConfigurerTestProfile";
	
	@Override
	public ConfigurableEnvironment configureEnvironment(ConfigurableEnvironment environment) 
	{
		environment.addActiveProfile(TEST_PROFILE);
		
		return environment;
	}
	
	public static boolean hasProfile(ConfigurableEnvironment environment)
	{
		boolean result = false;
		
		String[] profiles = environment.getActiveProfiles();
		for (String profile : profiles)
		{
			if (TEST_PROFILE.equals(profile))
			{
				result = true;
				break;
			}
		}
		
		return result;
	}
}
