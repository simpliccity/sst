/*
 *    Copyright 2014 Information Control Corporation
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

package org.simpliccity.sst.property.source;

import javax.sql.DataSource;

import org.simpliccity.sst.property.JdbcPropertyLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactoryBean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

public class JdbcPropertyLoaderBuilder 
{
	private EmbeddedDatabaseFactoryBean dbFactory;

	public JdbcPropertyLoaderBuilder()
	{
		// Create and configure database populator
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("sql/PropertiesTest.sql"));
		
		// Create and configure database factory
		dbFactory = new EmbeddedDatabaseFactoryBean();
		dbFactory.setDatabaseType(EmbeddedDatabaseType.HSQL);
		dbFactory.setDatabaseName("propertiesDb");
		dbFactory.setDatabasePopulator(populator);
		dbFactory.afterPropertiesSet();		
	}
	
	public JdbcPropertyLoader getPropertyLoader()
	{
		JdbcPropertyLoader jdbcLoader = null;
		
		// Skip this if db factory has not been initialized (impossible) or has been cleaned
		if (dbFactory != null)
		{
			// Create and configure JDBC property loader
			jdbcLoader = new JdbcPropertyLoader();
			jdbcLoader.setJdbcTemplate(getJdbcTemplate());
			jdbcLoader.setKeyColumnName("property_name");
			jdbcLoader.setValueColumnName("property_value");
			jdbcLoader.setSqlQuery("select property_name, property_value from sst_properties;");
		}

		return jdbcLoader;
	}
	
	public void cleanup()
	{
		if (dbFactory != null)
		{
			dbFactory.destroy();
			dbFactory = null;
		}
	}

	private JdbcTemplate getJdbcTemplate()
	{
		// Create and configure JDBC template
		JdbcTemplate template = new JdbcTemplate();
		DataSource dataSource = dbFactory.getObject();
		template.setDataSource(dataSource);
		
		return template;
	}
}
