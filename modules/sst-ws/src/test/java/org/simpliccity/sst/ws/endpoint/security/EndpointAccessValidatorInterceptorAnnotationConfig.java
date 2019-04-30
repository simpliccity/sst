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

package org.simpliccity.sst.ws.endpoint.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.simpliccity.sst.bean.annotation.PropertiesDelegateAnnotationBeanNameGenerator;
import org.simpliccity.sst.security.service.SpringSecurityServiceSecurityHandler;
import org.simpliccity.sst.service.security.ServiceSecurityHandler;
import org.simpliccity.sst.ws.endpoint.security.config.annotation.EnableSstWsAccessControl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.endpoint.adapter.method.MarshallingPayloadMethodProcessor;
import org.springframework.ws.server.endpoint.adapter.method.MethodArgumentResolver;
import org.springframework.ws.server.endpoint.adapter.method.MethodReturnValueHandler;
import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j.callback.SpringSecurityPasswordValidationCallbackHandler;

@SuppressWarnings("deprecation")
@Configuration
@Profile("wsAccess")
@ComponentScan(basePackages="org.simpliccity.sst", nameGenerator=PropertiesDelegateAnnotationBeanNameGenerator.class)
@EnableWs
@EnableSstWsAccessControl
public class EndpointAccessValidatorInterceptorAnnotationConfig extends WsConfigurerAdapter
{
	@Override
	public void addInterceptors(List<EndpointInterceptor> interceptors)
	{
		interceptors.add(securityInterceptor(validationCallback(userDetailsService())));
	}
	
	@Override
	public void addArgumentResolvers(List<MethodArgumentResolver> argumentResolvers)
	{
		argumentResolvers.add(methodProcessor());
	}
	
	@Override
	public void addReturnValueHandlers(List<MethodReturnValueHandler> returnValueHandlers)
	{
		returnValueHandlers.add(methodProcessor());
	}

	@Bean
	public ServiceSecurityHandler serviceSecurityHandler()
	{
		return new SpringSecurityServiceSecurityHandler();
	}
	
	@Bean
	public Jaxb2Marshaller marshaller()
	{
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("org.simpliccity.sst.ws.endpoint.security.schema");
		return marshaller;
	}
	
	@Bean
	public MarshallingPayloadMethodProcessor methodProcessor() 
	{
		return new MarshallingPayloadMethodProcessor(marshaller());
	}
	
	@Bean
	public SpringSecurityPasswordValidationCallbackHandler validationCallback(UserDetailsService usd)
	{
		SpringSecurityPasswordValidationCallbackHandler callback = new SpringSecurityPasswordValidationCallbackHandler();
		callback.setUserDetailsService(usd);
		
		return callback;
	}
	
	@Bean
	public Wss4jSecurityInterceptor securityInterceptor(SpringSecurityPasswordValidationCallbackHandler handler)
	{
		Wss4jSecurityInterceptor interceptor = new Wss4jSecurityInterceptor();
		interceptor.setValidationActions("UsernameToken");
		interceptor.setValidationCallbackHandler(handler);
		
		return interceptor;
	}
	
	@Bean
	public UserDetailsService userDetailsService()
	{
		Collection<SimpleGrantedAuthority> userRole = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
		Collection<SimpleGrantedAuthority> adminRole = Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
		Collection<SimpleGrantedAuthority> untrustedRole = Collections.singleton(new SimpleGrantedAuthority("ROLE_UNTRUSTED"));
		
		Collection<UserDetails> users = new ArrayList<UserDetails>();
		users.add(new User("userA", "password", userRole));
		users.add(new User("userB", "password", userRole));
		users.add(new User("userC", "password", untrustedRole));
		users.add(new User("admin", "password", adminRole));
		
		
		UserDetailsService usd = new InMemoryUserDetailsManager(users);
		
		return usd;
	}
}
