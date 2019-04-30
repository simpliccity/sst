/*
 *    Copyright 2011 Information Control Corporation
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

package org.simpliccity.sst.bean;

import org.springframework.beans.factory.config.BeanDefinition;

/**
 * <p>An event indicating that a bean name has been generated for a Spring component.  A custom
 * {@link org.springframework.beans.factory.support.BeanNameGenerator} may fire such an event
 * to be processed by any configured {@link BeanNameGeneratorListener}s.</p>
 * 
 * @author Kevin Fox
 *
 */
public class BeanNameGeneratorEvent
{
	private BeanDefinition beanDefinition;
	private String beanName;
	
	/**
	 * Constructs an event indicating that the Spring component
	 * (as indicated by a  {@link org.springframework.beans.factory.config.BeanDefinition})
	 * has been given the specified name.
	 * 
	 * @param beanDefinition Newly named Spring component.
	 * @param beanName Name given to the bean.
	 */
	public BeanNameGeneratorEvent(BeanDefinition beanDefinition, String beanName)
	{
		setBeanDefinition(beanDefinition);
		setBeanName(beanName);
	}
	
	/**
	 * Specifies the Spring component that has been named.
	 * 
	 * @param beanDefinition The {@link org.springframework.beans.factory.config.BeanDefinition}
	 * of the named bean.
	 */
	public final void setBeanDefinition(BeanDefinition beanDefinition) 
	{
		this.beanDefinition = beanDefinition;
	}
	
	/**
	 * Returns the {@link org.springframework.beans.factory.config.BeanDefinition}
	 * of the bean for which a name has been generated.
	 * 
	 * @return The {@link org.springframework.beans.factory.config.BeanDefinition}
	 * of the named bean.
	 */
	public final BeanDefinition getBeanDefinition() 
	{
		return beanDefinition;
	}

	/**
	 * Specifies the name of the Spring component.
	 * 
	 * @param beanName The name given to the bean by the custom
	 * {@link org.springframework.beans.factory.support.BeanNameGenerator}.
	 */
	public final void setBeanName(String beanName) 
	{
		this.beanName = beanName;
	}

	/**
	 * Returns the name given to the named Spring component.
	 * 
	 * @return Name generated for the Spring bean.
	 */
	public final String getBeanName() 
	{
		return beanName;
	}

}
