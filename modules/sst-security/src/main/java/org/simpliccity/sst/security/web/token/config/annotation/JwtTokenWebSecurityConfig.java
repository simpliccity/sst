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

package org.simpliccity.sst.security.web.token.config.annotation;

import java.util.HashMap;
import java.util.Map;

import org.simpliccity.sst.bean.lookup.config.annotation.EnableSstBeanLookup;
import org.simpliccity.sst.config.ConfigurationAnnotationUtils;
import org.simpliccity.sst.config.IfEnabled;
import org.simpliccity.sst.jwt.IssuerConfig;
import org.simpliccity.sst.jwt.JwtTokenReader;
import org.simpliccity.sst.jwt.jjwt.JjwtSignedJwtTokenReader;
import org.simpliccity.sst.jwt.key.KeyManager;
import org.simpliccity.sst.security.web.jwt.JwtTokenAuthenticationProvider;
import org.simpliccity.sst.security.web.token.StatelessTokenAssertionAuthenticationFilter;
import org.simpliccity.sst.web.security.token.HttpRequestTokenHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.util.MultiValueMap;

@Configuration
@EnableWebSecurity
@EnableSstBeanLookup
@IfEnabled(EnableSstJwtTokenAuth.class)
public class JwtTokenWebSecurityConfig extends WebSecurityConfigurerAdapter implements ImportAware, EnvironmentAware
{
	private static final String ATTRIB_KNOWN_ISSUERS = "knownIssuers";
	
	private static final String ATTRIB_AUTH_CLAIM = "authorityClaim";
	
	private static final String ATTRIB_ROLE_PREFIX = "rolePrefix";
	
	private static final String ATTRIB_STRICT = "strict";
		
	private static final String ATTRIB_TOKEN_HANDLER = "tokenHandler";
	
	private static final String ATTRIB_IK_ISSUER = "issuer";
	
	private static final String ATTRIB_IK_KEY_ALIAS = "keyAlias";
	
	private static final String ATTRIB_IK_ALGORITHM = "signingAlgorithm";
	
	private Environment environment;
	
	private MultiValueMap<String, Object> jwtProviderConfig;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
	{
		auth.authenticationProvider(jwtTokenAuthenticationProvider());
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception
	{
		http.antMatcher("/**");
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.exceptionHandling().authenticationEntryPoint(forbiddenEntryPoint());
		http.csrf().disable();
		http.addFilterAfter(statelessTokenAssertionFilter(authenticationManagerBean()), AbstractPreAuthenticatedProcessingFilter.class);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception
	{
		return super.authenticationManagerBean();
	}

	@Bean
	public JwtTokenAuthenticationProvider jwtTokenAuthenticationProvider()
	{
		JwtTokenAuthenticationProvider provider = new JwtTokenAuthenticationProvider();
		
		Map<String, IssuerConfig> issuers = convertKnownIssuers((AnnotationAttributes[]) jwtProviderConfig.getFirst(ATTRIB_KNOWN_ISSUERS));
		
		provider.setTokenReader(jwtTokenReader(issuers));
		provider.setKnownIssuers(issuers);
		provider.setAuthorityClaim((String) jwtProviderConfig.getFirst(ATTRIB_AUTH_CLAIM));
		provider.setRolePrefix((String) jwtProviderConfig.getFirst(ATTRIB_ROLE_PREFIX));
		provider.setStrict((boolean) jwtProviderConfig.getFirst(ATTRIB_STRICT));
		
		return provider;
	}
	
	@Bean
	public KeyManager jwtReaderKeyManager()
	{
		return new KeyManager();
	}
	
	@Bean
	public JwtTokenReader jwtTokenReader(Map<String, IssuerConfig> issuers)
	{
		JjwtSignedJwtTokenReader reader = new JjwtSignedJwtTokenReader();
		reader.setKeyManager(jwtReaderKeyManager());
		reader.setKnownIssuers(issuers);
		
		return reader;
	}
	
	@SuppressWarnings("unchecked")
	@Bean
	public StatelessTokenAssertionAuthenticationFilter statelessTokenAssertionFilter(AuthenticationManager authManager)
	{
		StatelessTokenAssertionAuthenticationFilter filter = new StatelessTokenAssertionAuthenticationFilter();
		
		Class<? extends HttpRequestTokenHandler> clazz = (Class<? extends HttpRequestTokenHandler>) jwtProviderConfig.getFirst(ATTRIB_TOKEN_HANDLER);
		HttpRequestTokenHandler tokenHandler = BeanUtils.instantiate(clazz);
		filter.setTokenHandler(tokenHandler);
		
		filter.setAuthenticationManager(authManager);
		
		return filter;
	}

	@Bean
	public Http403ForbiddenEntryPoint forbiddenEntryPoint()
	{
		return new Http403ForbiddenEntryPoint();
	}

	@Override
	public void setEnvironment(Environment environment) 
	{
		this.environment = environment;
	}

	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) 
	{
		jwtProviderConfig = ConfigurationAnnotationUtils.getResolvedAttributes(environment, importMetadata, EnableSstJwtTokenAuth.class.getName());
	}
	
	private Map<String, IssuerConfig> convertKnownIssuers(AnnotationAttributes[] keys)
	{
		Map<String, IssuerConfig> issuerMap = new HashMap<>();
		
		for (AnnotationAttributes key : keys)
		{
			IssuerConfig config = new IssuerConfig(key.getString(ATTRIB_IK_ISSUER), key.getString(ATTRIB_IK_ALGORITHM), key.getString(ATTRIB_IK_KEY_ALIAS));
			issuerMap.put(key.getString(ATTRIB_IK_ISSUER), config);
		}
		
		return issuerMap;
	}
}
