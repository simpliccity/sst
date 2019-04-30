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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * A Spring {@link EventListener} used to clear the cache of discovered "enabler" annotations whenever
 * the application context could be reinitialized.
 * 
 * @author Kevin Fox
 * @since 1.0.0
 * 
 * @see IfEnabledCondition
 * @see EnablerRegistry
 *
 */
@Component
public class EnablerRegistryEventListener 
{
	private Log logger = LogFactory.getLog(this.getClass());
	
	@EventListener({ContextRefreshedEvent.class, ContextStartedEvent.class, ContextStoppedEvent.class, ContextClosedEvent.class})
	public void resetEnablerRegistry(ApplicationContextEvent event)
	{
		EnablerRegistry.reset();
		logger.debug("Reset EnablerRegistry due to monitored application context event [" + event.getClass().getSimpleName() + "]." );
	}
}
