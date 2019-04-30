/*
 *    Copyright 2013 Information Control Corporation
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

package org.simpliccity.sst.bean.lookup;

import org.simpliccity.sst.bean.lookup.annotation.ComponentMetadata;
import org.simpliccity.sst.bean.lookup.annotation.DynamicTypedComponent;

// --- UserGuide dynamicTypedComponent ---
@DynamicTypedComponent
(
	// -ughighlight-
	type=TestableComponent.class, 
	// -ughighlight-
	metadata={@ComponentMetadata(name="ResultType", value="Success")}
)
public class PassingTestableComponent implements /* -ughighlight- */ TestableComponent /* -ughighlight- */
{
	// --- UserGuide dynamicTypedComponent ---
	public static final String RESULT_PASS = "Pass";
	
	@Override
	public String getTestResult() 
	{
		return RESULT_PASS;
	}
}
