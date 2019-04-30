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

package org.simpliccity.sst.sstws;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.simpliccity.sst.maven.PluginUtils;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ws.wsdl.WsdlDefinition;

/**
 * <p>Goal which generates a WSDL file from a Spring Web Services WsdlDefinition.</p>
 * 
 * <p><i>This Mojo is based on the implementation of the <b>springws-maven-plugin</b>
 * from Codehaus (groupId:org.codehaus.mojo, artifactId:springws-maven-plugin).  It
 * addresses a number of outstanding issues with that plugin, which has remained in
 * alpha status.</i></p>
 * 
 */
@Mojo(name="generateWSDL", defaultPhase=LifecyclePhase.PREPARE_PACKAGE, requiresDependencyResolution=ResolutionScope.TEST)
public class GenerateWsdlMojo extends AbstractMojo
{
    /**
     * Current Maven project.
     */
    @Parameter(property="project", required=true, readonly=true )
	private MavenProject project;
	
    /**
     * Output directory for generated WSDL file(s).
     */
	@Parameter(defaultValue="${project.build.directory}/wsdl", required=true)
    private File outputDirectory;
    
    /**
     * A list of files that define the Spring configuration containing the WsdlDefinition.
     * The files will be loaded using the Spring ClassPathXmlApplicationContext and
     * must follow the rules for resource naming employed by this class.
     */
	@Parameter(required=true)
    private String[] springContextConfigurations;
    
    /**
     * A mapping associating the WsdlDefinition entries for which a WSDL file should
     * be generated with the name of the generated file.
     */
	@Parameter(required=true)
    private Map<String, String> wsdlDefinitions;
	
	/**
	 * Flag indicating whether to include test dependencies in the classpath when 
	 * resolving the location of Spring configuration files.
	 * 
	 * @since 0.3.0
	 * 
	 * @see #springContextConfigurations
	 */
	@Parameter(defaultValue="false", required=true)
	private boolean includeTestDependencies;
    
    private Transformer transformer;
    

    /**
     * Creates a new instance of GenerateWsdlMojo.
     * 
     * @throws TransformerConfigurationException If cannot create a new javax.xml.transform.Transformer instance.
     */
    public GenerateWsdlMojo() throws TransformerConfigurationException
    {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
    }
    
	@Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
		ClassLoader originalLoader = Thread.currentThread().getContextClassLoader();
		
    	// Create output directory
    	createOutputDirectory();
    	
    	// Get Spring application context
    	AbstractApplicationContext context = initializeContext();
    	
    	// Generate a WSDL file from each specified Spring WsdlDefinition bean
    	for(String definitionName : getWsdlDefinitions().keySet())
    	{
    		// Retrieve a WsdlDefinition from the application context
    		WsdlDefinition definitionBean = (WsdlDefinition) context.getBean(definitionName);
    		
    		// If found, generate WSDL output
    		if (definitionBean == null)
    		{
    			getLog().warn("No such WSDL definition found [" + definitionName + "]");
     		}
    		else
    		{
    			generateWsdlOutput(definitionName, definitionBean);
    		}
    	}
    	
    	finalizeContext(context, originalLoader);
    }

    /**
     * Returns the current Maven project.
     * 
     * @return The current Maven project.
     */
	public MavenProject getProject() 
	{
		return project;
	}

	/**
	 * Sets the current Maven project.
	 * 
	 * @param project The current Maven project.
	 */
	public void setProject(MavenProject project) 
	{
		this.project = project;
	}

	/**
	 * Returns the output directory for generated WSDL file(s).
	 * 
	 * @return The output directory for generated WSDL file(s).
	 * 
	 * @see GenerateWsdlMojo#outputDirectory
	 */
	public File getOutputDirectory() 
	{
		return outputDirectory;
	}

	/**
	 * Sets the output directory for generated WSDL file(s).
	 * 
	 * @param outputDirectory The output directory for generated WSDL file(s).
	 * 
	 * @see #outputDirectory
	 */
	public void setOutputDirectory(File outputDirectory) 
	{
		this.outputDirectory = outputDirectory;
	}

	/**
	 * Returns the list of Spring configuration files to process.
	 * 
	 * @return An array of file names to process.
	 * 
	 * @see #springContextConfigurations
	 */
	public String[] getSpringContextConfigurations() 
	{
		// Returns copy of array to avoid external manipulation of the array itself
		return (springContextConfigurations == null) ? null : Arrays.copyOf(springContextConfigurations, springContextConfigurations.length);
	}

	/**
	 * Sets the list of Spring configuration files to process.
	 * 
	 * @param springContextConfigurations An array of file names to process.
	 * 
	 * @see #springContextConfigurations
	 */
	public void setSpringContextConfigurations(String[] springContextConfigurations) 
	{
		// Makes copy of input array to avoid external manipulation of the array itself
		this.springContextConfigurations = (springContextConfigurations == null) ? null : Arrays.copyOf(springContextConfigurations, springContextConfigurations.length);
	}

	/**
	 * Returns the association of WSDL definitions to WSDL file names.
	 * 
	 * @return A map associating WSDL definitions to file names.
	 * 
	 * @see #wsdlDefinitions
	 */
	public Map<String, String> getWsdlDefinitions() 
	{
		return wsdlDefinitions;
	}

	/**
	 * Sets the association of WSDL definitions to WSDL file names.
	 * 
	 * @param wsdlDefinitions A map associating WSDL definitions to file names.
	 * 
	 * @see #wsdlDefinitions
	 */
	public void setWsdlDefinitions(Map<String, String> wsdlDefinitions) 
	{
		this.wsdlDefinitions = wsdlDefinitions;
	}
	
	/**
	 * Indicates whether test dependencies should be included in the classpath when resolving
	 * Spring configuration files.
	 * 
	 * @return <code>true</code> if test dependencies should be included; <code>false</code> otherwise.
	 * 
	 * @see #includeTestDependencies
	 */
	public boolean isIncludeTestDependencies() 
	{
		return includeTestDependencies;
	}

	/**
	 * Specify whether test dependencies should be included in the classpath when resolving
	 * Spring configuration files.
	 * 
	 * @param includeTestDependencies <code>true</code> if test dependencies should be included; <code>false</code> otherwise.
	 * 
	 * @see #includeTestDependencies
	 */
	public void setIncludeTestDependencies(boolean includeTestDependencies) 
	{
		this.includeTestDependencies = includeTestDependencies;
	}

	private void createOutputDirectory() throws MojoFailureException
	{
    	// Create output directory, if necessary
    	if (!getOutputDirectory().exists())
    	{
	    	boolean createDirSuccess = getOutputDirectory().mkdirs();
	    	if (!createDirSuccess)
	    	{
	    		throw new MojoFailureException("Unable to create output directory.");
	    	}
    	}		
	}

	private AbstractApplicationContext initializeContext() throws MojoExecutionException
	{
    	// Prepare the Spring application context using the specified configuration files
    	// and a classloader that includes all dependencies and the project output
    	Thread.currentThread().setContextClassLoader(PluginUtils.getProjectClassloader(getProject(), getLog(), isIncludeTestDependencies()));
    	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(getSpringContextConfigurations());
    	
    	try 
    	{
        	context.refresh();    		
    	}
    	catch (Exception e)
    	{
    		// Cleanup application context before throwing exception
    		context.close();
    		getLog().error("Unable to load the specified Spring context configuration files", e);
    		throw new MojoExecutionException("Unable to load specified Spring configuration files", e);
    	}

    	return context;
	}
	
	private void generateWsdlOutput(String definitionName, WsdlDefinition definitionBean) throws MojoFailureException
	{
		// Get name of output file
		String filename = getWsdlDefinitions().get(definitionName);
		
		// Get XML source for WSDL definition
		Source wsdlSource = definitionBean.getSource();
		
		// Create output file and XML result
		File outputFile = new File(getOutputDirectory(), filename);
		Result outputResult = new StreamResult(outputFile);
		
		// Transform source to result
		try
		{
			transformer.transform(wsdlSource, outputResult);					
			getLog().info("Successfully output WSDL content to " + outputFile.getAbsolutePath());
		}
		catch (TransformerException e)
		{
			getLog().error("Unable to generate output file for WSDL definition [" + definitionName + "].", e);
			throw new MojoFailureException("Unable to generate output file for WSDL definition [" + definitionName + "].", e);
		}
	}
	
	private void finalizeContext(AbstractApplicationContext context, ClassLoader originalLoader)
	{
		// Clean up application context
		context.close();
		
		// Clean up thread context class loader
		
		Thread currentThread = Thread.currentThread();
		// First get project class loader
		ClassLoader projectLoader = currentThread.getContextClassLoader();
		// Then put back the original class loader
		currentThread.setContextClassLoader(originalLoader);
		// Then close the project class loader, if applicable
		if (projectLoader instanceof Closeable)
		{
			try 
			{
				((Closeable) projectLoader).close();
			} 
			catch (IOException e) 
			{
				getLog().warn("Unable to cleanup project class loader.", e);
			}
		}
	}
}
