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

import java.util.ArrayList;
import java.util.List;

import org.simpliccity.sst.bean.BeanNameGeneratorEvent;
import org.simpliccity.sst.bean.BeanNameGeneratorListener;

public class BeanNameTrackerListener implements BeanNameGeneratorListener 
{
	private static List<String> beans = new ArrayList<String>();
	
	public static String[] getBeanNames()
	{
		return beans.toArray(new String[beans.size()]);
	}
	
	@Override
	public void beanNameGenerated(BeanNameGeneratorEvent event) 
	{
		beans.add(event.getBeanName());
	}
}
