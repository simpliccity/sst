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

/**
 * The available types of transformations managed by {@link org.simpliccity.sst.transform.Transformer}.
 * Used in the {@link Transformation#direction()} annotation element.
 * 
 * @author Kevin Fox
 * 
 * @see Transformation
  */
public enum TransformationType 
{
	IN, OUT, BIDIRECTIONAL, NONE;
}
