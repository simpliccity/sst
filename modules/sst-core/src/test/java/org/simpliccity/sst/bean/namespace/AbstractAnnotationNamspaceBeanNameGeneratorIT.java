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

package org.simpliccity.sst.bean.namespace;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpliccity.sst.bean.annotation.BeanCounterListener;
import org.simpliccity.sst.bean.annotation.BeanNameTrackerListener;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/AbstractAnnotationNamespaceBeanNameGeneratorIT.xml" })
public class AbstractAnnotationNamspaceBeanNameGeneratorIT implements ApplicationContextAware
{
	private Log logger = LogFactory.getLog(getClass());
	private ApplicationContext context;
	
	/**
	 * Tests that a BeanNameGeneratorListener to count the number of beans processed was called correctly.
	 */
	@Test
	public void testParticipantCount()
	{
		assertEquals("Number of test participant beans processed", 2, BeanCounterListener.getCount());
	}
	
	/**
	 * Test that a BeanNameGeneratorListener to track the names of beans processed was called correctly and, correspondingly,
	 * that the appropriate beans exist.
	 */
	@Test
	public void testParticipantsExist()
	{
		String[] beanNames = BeanNameTrackerListener.getBeanNames();
		
		for (String name : beanNames)
		{
			logger.debug("Checking existence of bean [" + name + "]");
			assertNotNull("Bean found by name", context.getBean(name));
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException 
	{
		this.context = applicationContext;		
	}
}
