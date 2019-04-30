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

package org.simpliccity.sst.service.security;

/**
 * The names of the available {@link SemanticContentExtractor} implementations provided in 
 * the <code>core</code> project.  For use with the {@code @ServiceContentExtractor} and 
 * {@code @SemanticConstraint} annotations.
 * 
 * @author Kevin Fox
 * @since 1.0.0
 *
 * @see org.simpliccity.sst.service.security.annotation.ServiceContentExtractor
 * @see org.simpliccity.sst.service.security.annotation.SemanticConstraint
 */
public final class ExtractorTypes 
{
	public static final String PARAMETER = "parameter";
	
	public static final String PAYLOAD_SPEL = "spelPayload";

	// Private constructor to hide default
	private ExtractorTypes()
	{
		super();
	}	
}
