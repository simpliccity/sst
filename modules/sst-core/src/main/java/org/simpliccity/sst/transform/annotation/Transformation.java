/*
 *    Copyright 2009 Information Control Corporation
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

package org.simpliccity.sst.transform.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * <p>Marks a class for use as a transformation managed by {@link org.simpliccity.sst.transform.Transformer}.
 * Classes annotated as a transformation will be automatically recognized and loaded by Spring's
 * annotation scanning.</p>
 * 
 * <p>{@link org.simpliccity.sst.transform.Transformer} will use the element values of this annotation to 
 * determine how to apply the transformation.  However, the implementation of the transformation is 
 * completely determined by the annotated class, itself.  In particular, whether the transformation is 
 * deep or shallow is managed by the implementation.  A deep transformation can use 
 * {@link org.simpliccity.sst.transform.Transformer} to recursively transform its complex members.</p>
 * 
 * @author Kevin Fox
 *
 */
// --- UserGuide customAnnotation ---
@Documented
@Target(TYPE)
@Retention(RUNTIME)
// -ughighlight-
@Component
// -ughighlight-
public @interface Transformation 
{
// --- UserGuide customAnnotation ---
	/**
	 * Specifies the class of the source object to be processed by this transformation.
	 * (Note that the designation as the source class is for purposes of specification only.
	 * Whether this class will be accepted as input or produced as output is dependent on
	 * the direction specified for the transformation.)
	 * 
	 * @return The source object {@link java.lang.Class}.
	 */
	@SuppressWarnings("rawtypes")
	Class source();
	
	/**
	 * Specifies the class of the target object to be processed by this transformation.
	 * (Note that the designation as the target class is for purposes of specification only.
	 * Whether this class will be accepted as input or produced as output is dependent on
	 * the direction specified for the transformation.)
	 * 
	 * @return The target object {@link java.lang.Class}.
	 */
	@SuppressWarnings("rawtypes")
	Class target();
	
	/**
	 * <p>Specifies the direction in which the transformation will be applied.  Options are:</p>
	 * <ul>
	 * <li>OUT - A method designated with {@link OutTransform} will accept an instance of
	 * the source class as input and produce a corresponding instance of the target class
	 * as output.</li>
	 * <li>IN - A method designated with {@link InTransform} will accept an instance of the
	 * target class as input and produce a corresponding instance of the source class as
	 * output.</li>
	 * <li>BIDIRECTIONAL - Two methods, designated with {@link OutTransform} and {@link InTransform},
	 * will provide for transformation between instances of the source and target classes.</li>
	 * </ul>
	 * 
	 * @return A constant of the {@link TransformationType} enum designating the direction
	 * of the transformation.
	 */
	TransformationType direction();
	
	/**
	 * Specifies how this transformation participates in object caching (if any) provided by
	 * {@link org.simpliccity.sst.transform.Transformer}.  This setting overrides the default
	 * caching mode and applies globally for all uses of this transformation.
	 * 
	 * @since 0.2.0
	 * 
	 * @return A constant of the (@link TransformationCacheMode} enum designating the caching
	 * mode that applies to this transformation.
	 */
	TransformationCacheMode cache() default TransformationCacheMode.DEFAULT;
}
