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

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.simpliccity.sst.jwt.AbstractKeyManagerJwtTokenBuilder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;

public class JjwtSignedJwtTokenBuilder extends AbstractKeyManagerJwtTokenBuilder 
{
	@Override
	public String buildAndSignToken(Key key, String issuer, Map<String, Object> claims, String signingAlgorithm, Date issued, Date expiration) 
	{
		Claims jwtClaims = new DefaultClaims(claims);
		
		SignatureAlgorithm algorithm = SignatureAlgorithm.forName(signingAlgorithm);
				
		// Be sure to perform additional sets after setting claims, otherwise they will be overwritten
		return Jwts.builder().setClaims(jwtClaims).setIssuer(issuer).setIssuedAt(issued).setNotBefore(issued).setExpiration(expiration).signWith(algorithm, key).compact();
	}
}
