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

package org.simpliccity.sst.web.security;

import java.util.HashMap;
import java.util.Map;

import org.simpliccity.sst.bean.annotation.PropertiesDelegateAnnotationBeanNameGenerator;
import org.simpliccity.sst.jwt.IssuerConfig;
import org.simpliccity.sst.jwt.JwtTokenBuilder;
import org.simpliccity.sst.jwt.JwtTokenReader;
import org.simpliccity.sst.jwt.jjwt.JjwtSignedJwtTokenBuilder;
import org.simpliccity.sst.jwt.jjwt.JjwtSignedJwtTokenReader;
import org.simpliccity.sst.jwt.key.KeyManager;
import org.simpliccity.sst.service.security.ServiceSecurityHandler;
import org.simpliccity.sst.web.security.config.annotation.EnableSstWebAccessControl;
import org.simpliccity.sst.web.security.token.HttpRequestTokenHandler;
import org.simpliccity.sst.web.security.token.XAuthHeaderTokenHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan(basePackages="org.simpliccity.sst", nameGenerator=PropertiesDelegateAnnotationBeanNameGenerator.class)
@Profile("serviceAccessControl")
@EnableWebMvc
@EnableSstWebAccessControl
public class ServiceAccessControlHandlerInterceptorConfig 
{	
	@Bean
	public ServiceSecurityHandler serviceSecurityHandler()
	{
		TestJwtTokenServiceSecurityHandler securityHandler = new TestJwtTokenServiceSecurityHandler();
		securityHandler.setJwtTokenReader(jwtTokenReader());
		securityHandler.setTokenHandler(tokenHandler());
		
		return securityHandler;
	}
	
	@Bean 
	public JwtTokenReader jwtTokenReader()
	{
		JjwtSignedJwtTokenReader reader = new JjwtSignedJwtTokenReader();
		reader.setKeyManager(keyManager());

		Map<String, IssuerConfig> issuers = new HashMap<String, IssuerConfig>();
		issuers.put("SST", new IssuerConfig("SST", "HS512", "SST"));
		reader.setKnownIssuers(issuers);
		
		return reader;
	}
	
	@Bean
	public KeyManager keyManager()
	{
		return new KeyManager();
	}
	
	@Bean
	public HttpRequestTokenHandler tokenHandler()
	{
		return new XAuthHeaderTokenHandler();
	}
	
	@Bean
	public JwtTokenBuilder jwtTokenBuilder()
	{
		JjwtSignedJwtTokenBuilder builder = new JjwtSignedJwtTokenBuilder();
		builder.setKeyManager(keyManager());
		
		return builder;
	}
}
