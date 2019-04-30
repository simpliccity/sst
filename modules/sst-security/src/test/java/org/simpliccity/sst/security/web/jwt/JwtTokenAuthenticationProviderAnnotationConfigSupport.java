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

package org.simpliccity.sst.security.web.jwt;

import org.simpliccity.sst.jwt.JwtClaimsGenerator;
import org.simpliccity.sst.jwt.JwtTokenBuilder;
import org.simpliccity.sst.jwt.PrincipalSubjectJwtClaimsGenerator;
import org.simpliccity.sst.jwt.UserJwtTokenGenerator;
import org.simpliccity.sst.jwt.jjwt.JjwtSignedJwtTokenBuilder;
import org.simpliccity.sst.jwt.key.KeyManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Profile({"jwtToken", "jwtTokenIssuer"})
@EnableWebMvc
@EnableGlobalMethodSecurity(securedEnabled=true)
public class JwtTokenAuthenticationProviderAnnotationConfigSupport 
{
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() 
	{
	    return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean
	public JwtTokenBuilder jwtTokenBuilder()
	{
		JjwtSignedJwtTokenBuilder builder = new JjwtSignedJwtTokenBuilder();
		builder.setKeyManager(keyManager());
		
		return builder;
	}
	
	@Bean
	public KeyManager keyManager()
	{
		return new KeyManager();
	}
	
	protected UserJwtTokenGenerator configureJwtTokenGenerator(String issuer)
	{
		UserJwtTokenGenerator generator = new UserJwtTokenGenerator();
		generator.setTokenBuilder(jwtTokenBuilder());
		generator.setIssuer(issuer);
		generator.setClaimsGenerators(new JwtClaimsGenerator[] {new PrincipalSubjectJwtClaimsGenerator(), new GrantedAuthorityJwtClaimsGenerator()});
		
		return generator;		
	}
}
