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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.simpliccity.sst.security.web.jwt.JwtTokenAuthenticationProvider;
import org.simpliccity.sst.web.security.token.HttpRequestTokenHandler;
import org.simpliccity.sst.web.security.token.XAuthHeaderTokenHandler;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(JwtTokenWebSecurityConfig.class)
public @interface EnableSstJwtTokenAuth 
{
	IssuerKey[] knownIssuers() default {};
	
	String authorityClaim() default JwtTokenAuthenticationProvider.DEFAULT_AUTHORITY_CLAIM;
	
	String rolePrefix() default JwtTokenAuthenticationProvider.DEFAULT_ROLE_PREFIX;
	
	boolean strict() default true;
	
	Class<? extends HttpRequestTokenHandler> tokenHandler() default XAuthHeaderTokenHandler.class;
}
