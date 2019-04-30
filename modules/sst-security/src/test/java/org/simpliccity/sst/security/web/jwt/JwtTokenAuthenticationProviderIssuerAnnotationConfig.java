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

import org.simpliccity.sst.bean.annotation.PropertiesDelegateAnnotationBeanNameGenerator;
import org.simpliccity.sst.jwt.UserJwtTokenGenerator;
import org.simpliccity.sst.security.web.token.config.annotation.EnableSstJwtTokenAuth;
import org.simpliccity.sst.security.web.token.config.annotation.IssuerKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ComponentScan(basePackages="org.simpliccity.sst", nameGenerator=PropertiesDelegateAnnotationBeanNameGenerator.class)
@Profile("jwtTokenIssuer")
@EnableSstJwtTokenAuth(knownIssuers={@IssuerKey(issuer="SST", keyAlias="SST"), @IssuerKey(issuer="ALT", keyAlias="ALT")})
public class JwtTokenAuthenticationProviderIssuerAnnotationConfig extends JwtTokenAuthenticationProviderAnnotationConfigSupport
{
	@Bean
	public UserJwtTokenGenerator sstJwtTokenGenerator()
	{
		return configureJwtTokenGenerator("SST");
	}
	
	@Bean
	public UserJwtTokenGenerator altJwtTokenGenerator()
	{
		return configureJwtTokenGenerator("ALT");
	}

	@Bean
	public UserJwtTokenGenerator unknownJwtTokenGenerator()
	{
		return configureJwtTokenGenerator("UNKNOWN");
	}
}
