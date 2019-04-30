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

package org.simpliccity.sst.transform.annotation;

/**
 * <p>The available types of transformation cache modes supported by {@link org.simpliccity.sst.transform.Transformer}.
 * Used in the {@link Transformation#cache()} annotation element.</p>
 * 
 * <p>The available values are:</p>
 * <ul>
 * <li><b>DEFAULT</b> - Uses the default cache mode specified for the {@link org.simpliccity.sst.transform.Transformer}</li>
 * <li><b>NONE</b> - The transformation does not participate in caching, either in retrieving or setting the value from/to the cache.</li>
 * <li><b>JOIN</b> - The transformation uses the existing cache context, retrieving or setting the value from/to the cache.</li>
 * <li><b>FLUSH</b> - The transformation uses the existing cache context but flushes it upon completion.</li>
 * </ul>
 * 
 * @author Kevin Fox
 * @since 0.2.0
 * 
 * @see Transformation
 *
 */
public enum TransformationCacheMode 
{
	DEFAULT, NONE, JOIN, FLUSH;
}
