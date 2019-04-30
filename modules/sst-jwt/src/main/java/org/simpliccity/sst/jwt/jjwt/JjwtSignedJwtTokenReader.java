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
import java.util.HashMap;
import java.util.Map;

import org.simpliccity.sst.jwt.AbstractKeyManagerJwtTokenReader;
import org.simpliccity.sst.jwt.ClaimsUtils;
import org.simpliccity.sst.jwt.IssuerConfig;
import org.simpliccity.sst.jwt.JwtContent;
import org.simpliccity.sst.jwt.key.KeyManager;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.SigningKeyResolverAdapter;

public class JjwtSignedJwtTokenReader extends AbstractKeyManagerJwtTokenReader
{
	private SigningKeyResolver keyResolver;
	
	@Override
	protected JwtContent parseAndValidateToken(String token) throws Exception
	{
		Jws<Claims> parsedToken = Jwts.parser().setSigningKeyResolver(keyResolver).parseClaimsJws(token);
		
		String signingAlgorithm = parsedToken.getHeader().getAlgorithm();
		Claims claims = parsedToken.getBody();
		
		return new JwtContent(signingAlgorithm, ClaimsUtils.cleanseClaims(claims));
	}

	@Override
	protected void initialize() 
	{
		this.keyResolver = new MappedIssuerSigningKeyResolver(getKeyManager(), getKnownIssuers());
	}
}

class MappedIssuerSigningKeyResolver extends SigningKeyResolverAdapter
{
	private KeyManager keyManager;
	
	private Map<String, IssuerConfig> issuers;
	
	MappedIssuerSigningKeyResolver(KeyManager keyManager, Map<String, IssuerConfig> issuers)
	{
		this.keyManager = keyManager;
		
		this.issuers = (issuers == null) ? new HashMap<String, IssuerConfig>() : new HashMap<String, IssuerConfig>(issuers);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Key resolveSigningKey(JwsHeader header, Claims claims)
	{
		String issuer = claims.getIssuer();
		
		String alias = issuers.containsKey(issuer) ? issuers.get(issuer).getKeyAlias() : issuer;
		
		return keyManager.getSigningKey(header.getAlgorithm(), alias);
	}
}