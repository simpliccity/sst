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

package org.simpliccity.sst.ws.endpoint.security.config.annotation;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpliccity.sst.bean.lookup.config.annotation.EnableSstBeanLookup;
import org.simpliccity.sst.config.IfEnabled;
import org.simpliccity.sst.service.security.ServiceSecurityHandler;
import org.simpliccity.sst.ws.endpoint.security.EndpointAccessValidatorInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;

@Configuration
@IfEnabled(EnableSstWsAccessControl.class)
@EnableSstBeanLookup
public class WsAccessControlConfiguration extends WsConfigurerAdapter
{
	public static final String BEAN_NAME_INTERCEPTOR = "sstWebAccessControlInterceptor";

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired(required=false)
	private ServiceSecurityHandler securityHandler;
	
	@Bean(name=BEAN_NAME_INTERCEPTOR)
	public EndpointAccessValidatorInterceptor interceptor()
	{
		EndpointAccessValidatorInterceptor interceptor = new EndpointAccessValidatorInterceptor();
		
		interceptor.setSecurityHandler(securityHandler);
		logger.debug("Defined service access control interceptor bean.");
		
		return interceptor;
	}

	@Override
	public void addInterceptors(List<EndpointInterceptor> interceptors) 
	{
		interceptors.add(interceptor());
	}
}
