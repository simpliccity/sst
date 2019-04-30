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

package org.simpliccity.sst.config;

import java.util.HashSet;
import java.util.Set;

/**
 * A cache of the configured "enabler" annotations.  This information is maintained
 * by {@link IfEnabledCondition} during the loading of the application context.
 * 
 * @author Kevin Fox
 * @since 1.0.0
 * 
 * @see IfEnabledCondition
 * @see IfEnabled
 *
 */
public final class EnablerRegistry 
{
	private static Set<String> enablers = new HashSet<>();
	
	private EnablerRegistry()
	{
		super();
	}
	
	/**
	 * Adds the class name of an "enabler" annotation to the cache.
	 * 
	 * @param name The class name of an "enabler" annotation.
	 */
	public static void addEnabler(String name)
	{
		enablers.add(name);
	}
	
	/**
	 * Determines whether an "enabler" annotation class name exists in the cache.
	 * 
	 * @param name The class name of the "enabler" annotation for which to check.
	 * @return <code>true</code> if the class name exists in the cache; <code>false</code> otherwise.
	 */
	public static boolean contains(String name)
	{
		return enablers.contains(name);
	}
	
	/**
	 * Clears all entries from the cache.  This would be necessary if the application context is reloaded.
	 * 
	 * @see EnablerRegistryEventListener
	 */
	public static void reset()
	{
		enablers.clear();
	}
}
