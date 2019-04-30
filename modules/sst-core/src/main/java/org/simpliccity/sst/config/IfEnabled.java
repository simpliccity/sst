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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Conditional;

/**
 * An annotation used to conditionally enable processing of a {@code @Configuration} class recognized by Spring 
 * annotation scanning.  
 * 
 * <p>When using the <b>SST</b> framework, it is common for annotation scanning to include classes within 
 * the framework, itself.  By default, the framework classes used to enable Java-based configuration would
 * always be processed (due to the presence of the {@code @Configuration} annotation) when loading the application 
 * context.  This is not the desired behavior.  Instead, these configurations should only be loaded if they
 * have been explicitly enabled by marking a user-provided configuration class with a corresponding "enabler"
 * annotation.  For example, the use of {@code @EnableSstBeanLookup} on a configuration bean should enable
 * loading of the corresponding configuration class needed to configure the <code>lookup</code> framework:</p>
 * 
 * <pre class="code">
 * &#064;Configuration
 * &#064;EnableSstBeanLookup
 * &#064;ComponentScan(basePackages="org.simpliccity.sst", nameGenerator=PropertiesDelegateAnnotationBeanNameGenerator.class)
 * public class MyConfiguration {
 * </pre>
 * 
 * Spring's {@code @Conditional} annotation provides a mechanism to mark classes that should only be processed
 * by annotation scanning when a specified condition is met.  The {@link IfEnabledCondition} defines such a condition
 * that passes when another bean in the application context has been marked with a specified "enabler" annotation.
 * The {@code @IfEnabled} annotation provides a convenient way to both apply the {@link IfEnabledCondition} to a
 * {@code @Configuration} class and to specify the corresponding "enabler" annotation.
 * 
 * <p>For example, the following annotations on the configuration class for the <code>lookup</code> framework define
 * the conditional behavior needed to support the example above:</p>
 * 
 * <pre class="code">
 * &#064;Configuration
 * &#064;IfEnabled(EnableSstBeanLookup.class)
 * public class BeanMetadataLookupManagerConfiguration {
 * </pre>
 * 
 * @author Kevin Fox
 * @since 1.0.0
 * 
 * @see IfEnabledCondition
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Conditional(IfEnabledCondition.class)
public @interface IfEnabled 
{
	/**
	 * The "enabler" annotation class to be sought when applying the {@link IfEnabledCondition} condition.
	 * 
	 * @return The class name of an "enabler" annotation.
	 */
	Class<?> value();
}
