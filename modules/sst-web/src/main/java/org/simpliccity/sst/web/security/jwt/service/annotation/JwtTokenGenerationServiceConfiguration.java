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

package org.simpliccity.sst.web.security.jwt.service.annotation;

import org.simpliccity.sst.bean.lookup.config.annotation.EnableSstBeanLookup;
import org.simpliccity.sst.config.IfEnabled;
import org.simpliccity.sst.jwt.JwtTokenBuilder;
import org.simpliccity.sst.jwt.JwtTokenReader;
import org.simpliccity.sst.jwt.jjwt.JjwtSignedJwtTokenBuilder;
import org.simpliccity.sst.jwt.jjwt.JjwtSignedJwtTokenReader;
import org.simpliccity.sst.jwt.key.KeyManager;
import org.simpliccity.sst.web.security.jwt.service.JwtTokenRequestController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@IfEnabled(EnableSstJwtTokenGenerationService.class)
@EnableSstBeanLookup
public class JwtTokenGenerationServiceConfiguration 
{
	@Bean
	public KeyManager jwtBuilderKeyManager()
	{
		return new KeyManager();
	}
	
	@Bean
	public JwtTokenBuilder jwtTokenBuilder()
	{
		JjwtSignedJwtTokenBuilder builder = new JjwtSignedJwtTokenBuilder();
		builder.setKeyManager(jwtBuilderKeyManager());
		
		return builder;
	}
	
	@Bean
	public JwtTokenReader jwtTokenReader()
	{
		JjwtSignedJwtTokenReader reader = new JjwtSignedJwtTokenReader();
		reader.setKeyManager(jwtBuilderKeyManager());
		
		return reader;
	}
	
	@Bean
	public JwtTokenRequestController jwtTokenRequestController()
	{
		JwtTokenRequestController controller = new JwtTokenRequestController();
		controller.setTokenBuilder(jwtTokenBuilder());
		controller.setTokenReader(jwtTokenReader());
		
		return controller;
	}
}
