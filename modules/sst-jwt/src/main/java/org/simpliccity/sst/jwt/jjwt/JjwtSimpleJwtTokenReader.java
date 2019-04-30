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

package org.simpliccity.sst.jwt.jjwt;

import org.simpliccity.sst.jwt.ClaimsUtils;
import org.simpliccity.sst.jwt.InvalidJwtTokenException;
import org.simpliccity.sst.jwt.JwtContent;
import org.simpliccity.sst.jwt.JwtTokenReader;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;

public class JjwtSimpleJwtTokenReader implements JwtTokenReader 
{
	public static final String ALGORITHM_UNSIGNED = "UNSIGNED";
	
	@SuppressWarnings("rawtypes")
	@Override
	public JwtContent parseToken(String token) throws InvalidJwtTokenException 
	{
		Jwt<Header, Claims> parsedToken = Jwts.parser().parseClaimsJwt(token);
		
		Claims claims = parsedToken.getBody();
		
		return new JwtContent(ALGORITHM_UNSIGNED, ClaimsUtils.cleanseClaims(claims));
	}
}
