/*
 *    Copyright 2016 Information Control Corporation
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

package org.simpliccity.sst.jwt;

public class IssuerConfig 
{
	public static final String DEFAULT_SIGNING_ALGORITHM = "HS512";
	
	private String issuer;
	
	private String signingAlgorithm;
	
	private String keyAlias;
	
	public IssuerConfig(String issuer, String signingAlgorithm, String keyAlias)
	{
		this.issuer = issuer;
		this.signingAlgorithm = signingAlgorithm;
		this.keyAlias = keyAlias;
	}

	public String getIssuer() 
	{
		return issuer;
	}

	public void setIssuer(String issuer) 
	{
		this.issuer = issuer;
	}

	public String getSigningAlgorithm() 
	{
		return signingAlgorithm;
	}

	public void setSigningAlgorithm(String signingAlgorithm) 
	{
		this.signingAlgorithm = signingAlgorithm;
	}

	public String getKeyAlias() 
	{
		return keyAlias;
	}

	public void setKeyAlias(String keyAlias) 
	{
		this.keyAlias = keyAlias;
	}
	
	public boolean supports(String algorithm)
	{
		return this.signingAlgorithm.equalsIgnoreCase(algorithm);
	}
}
