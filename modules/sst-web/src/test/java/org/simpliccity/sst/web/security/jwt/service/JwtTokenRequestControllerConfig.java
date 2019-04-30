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

package org.simpliccity.sst.web.security.jwt.service;

import org.simpliccity.sst.bean.annotation.PropertiesDelegateAnnotationBeanNameGenerator;
import org.simpliccity.sst.jwt.JwtTokenBuilder;
import org.simpliccity.sst.jwt.JwtTokenReader;
import org.simpliccity.sst.jwt.jjwt.JjwtSimpleJwtTokenBuilder;
import org.simpliccity.sst.jwt.jjwt.JjwtSimpleJwtTokenReader;
import org.simpliccity.sst.web.security.jwt.service.annotation.EnableSstJwtTokenGenerationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan(basePackages="org.simpliccity.sst", nameGenerator=PropertiesDelegateAnnotationBeanNameGenerator.class)
@Profile("jwtTokenRequestService")
@EnableWebMvc
@EnableSstJwtTokenGenerationService
public class JwtTokenRequestControllerConfig 
{	
	@Bean 
	public JwtTokenReader jwtTokenReader()
	{
		return new JjwtSimpleJwtTokenReader();
	}
	
	@Bean
	public JwtTokenBuilder jwtTokenBuilder()
	{
		return new JjwtSimpleJwtTokenBuilder();
	}
	
	@Bean
	public MappingJackson2HttpMessageConverter mappingJacksonConverter()
	{
		return new MappingJackson2HttpMessageConverter();
	}
}
