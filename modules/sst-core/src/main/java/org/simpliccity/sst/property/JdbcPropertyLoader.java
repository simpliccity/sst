/*
 *    Copyright 2011 Information Control Corporation
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

package org.simpliccity.sst.property;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.SQLExceptionSubclassTranslator;

/**
 * An implementation of {@link PropertyLoader} that loads application properties from a database
 * query. This class relies on Spring's JDBC support to execute the query.
 * 
 * @author Kevin Fox
 * 
 * @see org.springframework.jdbc.core.JdbcTemplate
 *
 */
public class JdbcPropertyLoader implements PropertyLoader 
{	
	private Log logger = LogFactory.getLog(getClass());
	
	private JdbcTemplate jdbcTemplate;
	
	private String sqlQuery;
	
	private String keyColumnName;
	
	private String valueColumnName;
	
	/**
	 * Returns the {@link org.apache.commons.logging.Log} instance used to
	 * log application messages related to this class.
	 *  
	 * @return The class-specific {@link org.apache.commons.logging.Log} instance.
	 */
	protected Log getLogger() 
	{
		return logger;
	}

	/**
	 * Returns the Spring {@link org.springframework.jdbc.core.JdbcTemplate} used to execute
	 * the database query that returns the application properties.
	 * 
	 * @return The {@link org.springframework.jdbc.core.JdbcTemplate} used for database access.
	 */
	public JdbcTemplate getJdbcTemplate() 
	{
		return jdbcTemplate;
	}

	/**
	 * Specifies the Spring {@link org.springframework.jdbc.core.JdbcTemplate} used to execute
	 * the database query that returns the application properties.
	 * 
	 * @param jdbcTemplate The {@link org.springframework.jdbc.core.JdbcTemplate} used for database access.
	 */
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) 
	{
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Returns the SQL query used to retrieve application properties from a database.  The query must return
	 * at least the two columns defined for the property key and value, as specified using the {@link #setKeyColumnName(String)}
	 * and {@link #setValueColumnName(String)} methods, respectively.
	 * 
	 * @return The SQL query to be executed to retrieve properties from a database.
	 */
	public String getSqlQuery() 
	{
		return sqlQuery;
	}

	/**
	 * Specifies the SQL query used to retrieve application properties from a database.  The query must return
	 * at least the two columns defined for the property key and value, as specified using the {@link #setKeyColumnName(String)}
	 * and {@link #setValueColumnName(String)} methods, respectively.
	 * 
	 * @param sqlQuery The SQL query to be executed to retrieve properties from a database.
	 */
	public void setSqlQuery(String sqlQuery) 
	{
		this.sqlQuery = sqlQuery;
	}

	/**
	 * Returns the name of the column in the query specified by {@link #setSqlQuery(String)} that designates the
	 * property key.
	 * 
	 * @return The name of key column.
	 */
	public String getKeyColumnName() 
	{
		return keyColumnName;
	}

	/**
	 * Specifies the name of the column in the query specified by {@link #setSqlQuery(String)} that designates the
	 * property key.
	 * 
	 * @param keyColumnName The name of the key column.
	 */
	public void setKeyColumnName(String keyColumnName) 
	{
		this.keyColumnName = keyColumnName;
	}

	/**
	 * Returns the name of the column in the query specified by {@link #setSqlQuery(String)} that designates the
	 * property value.
	 * 
	 * @return The name of value column.
	 */
	public String getValueColumnName() 
	{
		return valueColumnName;
	}

	/**
	 * Specifies the name of the column in the query specified by {@link #setSqlQuery(String)} that designates the
	 * property value.
	 * 
	 * @param valueColumnName The name of value column.
	 */
	public void setValueColumnName(String valueColumnName) 
	{
		this.valueColumnName = valueColumnName;
	}

	@Override
	public void loadProperties(final Properties props) throws IOException
	{
		getLogger().debug("Loading properties for bean configuration.");
		
		try 
		{
			getJdbcTemplate().query(getSqlQuery(), new PropertyQueryRowCallbackHandler(this, props));
		}
		catch (DataAccessException e)
		{
			throw new IOException("Unable to read properties from database.", e);
		}
	}	
}

/**
 * Inner class used to process rows retrieved from the database by the
 * query specified for {@link JdbcPropertyLoader}.  This class implements
 * the {@link org.springframework.jdbc.core.RowCallbackHandler} interface
 * to support the query functionality of {@link org.springframework.jdbc.core.JdbcTemplate}.
 * 
 * @author Kevin Fox
 *
 */
class PropertyQueryRowCallbackHandler implements RowCallbackHandler
{
	private JdbcPropertyLoader loader;
	private Properties props;
	private SQLExceptionSubclassTranslator translator = new SQLExceptionSubclassTranslator();
	
	/**
	 * Constructor that accepts the {@link JdbcPropertyLoader} for which this row
	 * callback handler will be used and the {@link java.util.Properties} instance
	 * it will populate.
	 * 
	 * @param loader The parent {@link JdbcPropertyLoader}.
	 * @param props The {@link java.util.Properties} instance to populate.
	 */
	public PropertyQueryRowCallbackHandler(JdbcPropertyLoader loader, Properties props)
	{
		this.loader = loader;
		this.props = props;
	}
	
	@Override
	public void processRow(ResultSet rs)
	{
		try 
		{
			// Get the values from the specified key and value columns
			String key = rs.getString(loader.getKeyColumnName());
			String value = rs.getString(loader.getValueColumnName());
			
			// Add a property using the specified key and value
			props.setProperty(key, value);
			loader.getLogger().debug("Set property [" + key + "]: " + value);
		}
		catch (SQLException e)
		{
			loader.getLogger().error("Unable to read properties from database.", e);
			throw translator.translate("Reading properties from database", loader.getSqlQuery(), e);
		}
	}
}

