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

package org.simpliccity.sst.jwt.key;

import java.util.HashMap;
import java.util.Map;

import org.simpliccity.sst.jwt.key.AbstractEncodedMapKeyProvider;
import org.simpliccity.sst.jwt.key.annotation.KeySource;

@KeySource(PS256TestKeyProvider.ALGORITHM)
public class PS256TestKeyProvider extends AbstractEncodedMapKeyProvider 
{
	public static final String ALGORITHM = "PS256";
	
	public static final String JCA_ALGORITHM = "SHA256withRSAandMGF1";
	
	public PS256TestKeyProvider()
	{
		Map<String, String> keyMap = new HashMap<String, String>();
		keyMap.put("SST", "ABCDEFGHIJ");
		keyMap.put("ALT", "JIHGFEDCBA");
		
		setKeyMap(keyMap);
	}
	
	@Override
	protected String getJcaAlgorithm() 
	{
		return JCA_ALGORITHM;
	}
}
