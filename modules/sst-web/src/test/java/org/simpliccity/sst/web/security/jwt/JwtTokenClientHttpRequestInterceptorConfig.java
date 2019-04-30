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

package org.simpliccity.sst.web.security.jwt;

import java.util.ArrayList;
import java.util.List;

import org.simpliccity.sst.bean.annotation.PropertiesDelegateAnnotationBeanNameGenerator;
import org.simpliccity.sst.bean.lookup.config.annotation.EnableSstBeanLookup;
import org.simpliccity.sst.jwt.JwtClaimsGenerator;
import org.simpliccity.sst.jwt.JwtTokenBuilder;
import org.simpliccity.sst.jwt.PrincipalSubjectJwtClaimsGenerator;
import org.simpliccity.sst.jwt.UserJwtTokenGenerator;
import org.simpliccity.sst.jwt.jjwt.JjwtSignedJwtTokenBuilder;
import org.simpliccity.sst.jwt.key.KeyManager;
import org.simpliccity.sst.web.security.token.HttpRequestTokenHandler;
import org.simpliccity.sst.web.security.token.XAuthHeaderTokenHandler;
import org.simpliccity.sst.service.security.ServiceSecurityHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages="org.simpliccity.sst", nameGenerator=PropertiesDelegateAnnotationBeanNameGenerator.class)
@Profile("jwtClientInterceptor")
@EnableSstBeanLookup
public class JwtTokenClientHttpRequestInterceptorConfig 
{
	@Bean
	public ServiceSecurityHandler testSecurityHandler()
	{
		return new TestUserParameterServiceSecurityHandler();
	}
	
	@Bean
	public UserJwtTokenGenerator jwtGenerator()
	{
		UserJwtTokenGenerator generator = new UserJwtTokenGenerator();
		
		generator.setTokenBuilder(jwtTokenBuilder());
		generator.setIssuer("SST");
		generator.setClaimsGenerators(new JwtClaimsGenerator[] {new PrincipalSubjectJwtClaimsGenerator()});
		
		return generator;
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
	
	@Bean
	public HttpRequestTokenHandler tokenHandler()
	{
		return new XAuthHeaderTokenHandler();
	}
	
	@Bean
	public JwtTokenClientHttpRequestInterceptor jwtTokenInterceptor()
	{
		JwtTokenClientHttpRequestInterceptor interceptor = new JwtTokenClientHttpRequestInterceptor();
		interceptor.setSecurityHandler(testSecurityHandler());
		interceptor.setTokenGenerator(jwtGenerator());
		interceptor.setTokenHandler(tokenHandler());
		
		return interceptor;
	}
	
	@Bean
	public RestTemplate restTemplate()
	{
		RestTemplate template = new RestTemplate();
		
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
		interceptors.add(jwtTokenInterceptor());
		
		template.setInterceptors(interceptors);
		
		return template;
	}
}
