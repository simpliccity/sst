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

package org.simpliccity.sst.bean.annotation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.bean.BeanNameGeneratorEvent;
import org.simpliccity.sst.bean.BeanNameGeneratorListener;

public class BeanCounterListener implements BeanNameGeneratorListener 
{
	private static int count = 0;
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	public BeanCounterListener()
	{
		logger.debug("Initialized listener.");
		// Kludgy workaround for fact that a unit test can only access the counter statically
		// and multiple tests may get run where the listener is indirectly used
		count = 0;
	}
	
	public static int getCount() 
	{
		return count;
	}

	@Override
	public void beanNameGenerated(BeanNameGeneratorEvent event) 
	{
		count++;
		logger.debug("Processed event for bean [" + event.getBeanName() + "], current event count = " + count);
	}
}
