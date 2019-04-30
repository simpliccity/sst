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

public final class JwtReservedClaimNames 
{
	public static final String AUDIENCE = "aud";
	
	public static final String EXPIRATION = "exp";
	
	public static final String ISSUED_AT = "iat";
	
	public static final String ISSUER = "iss";
	
	public static final String JWT_ID = "jti";
	
	public static final String NOT_BEFORE = "nbf";
	
	public static final String PRINCIPAL = "prn";
	
	public static final String SUBJECT = "sub";
	
	public static final String TYPE = "typ";

	// Private constructor to hide default
	private JwtReservedClaimNames()
	{
		super();
	}	
}
