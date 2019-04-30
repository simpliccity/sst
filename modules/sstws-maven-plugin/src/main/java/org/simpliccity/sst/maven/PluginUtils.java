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

package org.simpliccity.sst.maven;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

/**
 * Functions to simplify working with the Maven plugin environment.
 * 
 * @author Kevin Fox
 * @since 1.0.0
 *
 */
public final class PluginUtils 
{
	private PluginUtils() 
	{
		super();
	}

	/**
	 * Provides a {@link ClassLoader} with all of the appropriate Maven project dependencies on the classpath.
	 * This is intended to be used within the context of a Maven build.
	 * 
	 * @param project The Maven project being built.
	 * @param log The Maven plugin logger.
	 * @param includeTestDependencies A flag indicating whether to included test dependencies on the classpath.
	 * @return A {@link ClassLoader}, based on the current thread class loader, with the dependencies on the classpath.
	 * @throws MojoExecutionException If the class loader cannot be retrieved or configured within the context of the build.
	 */
	public static ClassLoader getProjectClassloader(MavenProject project, Log log, boolean includeTestDependencies) throws MojoExecutionException
	{
		// Get list of compile, runtime and, optionally, test dependencies
		List<String> dependencies = new ArrayList<>();
		
		dependencies.addAll(getCompileDependencies(project, log));
		
		dependencies.addAll(getRuntimeDependencies(project, log));
		
		if (includeTestDependencies)
		{
			dependencies.addAll(getTestDependencies(project, log));
		}
		
		dependencies.addAll(getWarDependencies(project, log));
		
		// Convert classpath to URL's
		List<File> classpathFiles = new ArrayList<>();
		for (String classpath : dependencies)
		{
			log.debug("Processing classpath entry: " + classpath);
			File temp = new File(classpath);
			classpathFiles.add(temp);
		}
		
		// Create new classloader
		ClassLoader result = null;
		try
		{
			// Start with context classloader
			ClassLoader pluginClassLoader = Thread.currentThread().getContextClassLoader();

			// Create new classloader with dependencies added to classpath
			result = URLClassLoader.newInstance(FileUtils.toURLs(classpathFiles.toArray(new File[classpathFiles.size()])), pluginClassLoader);
			log.debug("Successfully constructed classpath from project dependencies and build directory.");
		
		}
		catch (IOException e)
		{
			log.error("Unable to construct classpath from project dependencies and build directory.", e);
			throw new MojoExecutionException("Unable to construct classpath from project dependencies and build directory.", e);
		}
						
		return result;
	}

	/**
	 * Provides a list of the compile dependency resources for the project being built.
	 * 
	 * @param project The Maven project being built.
	 * @param log The Maven plugin logger.
	 * @return A list of the resource locations for project compile dependencies.
	 * @throws MojoExecutionException If the specified type of dependencies cannot be resolved.
	 */
	public static List<String> getCompileDependencies(MavenProject project, Log log) throws MojoExecutionException
	{
		List<String> result = null;

		try
		{
			result = project.getCompileClasspathElements();
			log.debug("Adding compile classpath elements: " + result);
		}
		catch (DependencyResolutionRequiredException e) 
		{
			log.error("Unable to determine project compile dependencies when building classpath.", e);
			throw new MojoExecutionException("Unable to determine project compile dependencies when building classpath.", e);
		}
		
		return result;
	}
	
	/**
	 * Provides a list of the runtime dependency resources for the project being built.
	 * 
	 * @param project The Maven project being built.
	 * @param log The Maven plugin logger.
	 * @return A list of the resource locations for project runtime dependencies.
	 * @throws MojoExecutionException If the specified type of dependencies cannot be resolved.
	 */
	public static List<String> getRuntimeDependencies(MavenProject project, Log log) throws MojoExecutionException
	{
		List<String> result = null;

		try
		{
			result = project.getRuntimeClasspathElements();
			log.debug("Adding runtime classpath elements: " + result);
		}
		catch (DependencyResolutionRequiredException e) 
		{
			log.error("Unable to determine project runtime dependencies when building classpath.", e);
			throw new MojoExecutionException("Unable to determine project runtime dependencies when building classpath.", e);
		}
		
		return result;
	}
	
	/**
	 * Provides a list of the test dependency resources for the project being built.
	 * 
	 * @param project The Maven project being built.
	 * @param log The Maven plugin logger.
	 * @return A list of the resource locations for project test dependencies.
	 * @throws MojoExecutionException If the specified type of dependencies cannot be resolved.
	 */
	public static List<String> getTestDependencies(MavenProject project, Log log) throws MojoExecutionException
	{
		List<String> result = null;

		try
		{
			result = project.getTestClasspathElements();
			log.debug("Adding test classpath elements: " + result);
		}
		catch (DependencyResolutionRequiredException e) 
		{
			log.error("Unable to determine project test dependencies when building classpath.", e);
			throw new MojoExecutionException("Unable to determine project test dependencies when building classpath.", e);
		}
		
		return result;
	}
	
	/**
	 * Provides the location of the <code>webapp</code> folder for a WAR project.
	 * 
	 * @param project The Maven project being built.
	 * @param log The Maven plugin logger.
	 * @return A list of the resource locations for project WAR dependencies.
	 */
	public static List<String> getWarDependencies(MavenProject project, Log log)
	{
		List<String> result = new ArrayList<>();

		// Include webapp folder for WAR projects
		if ("WAR".equalsIgnoreCase(project.getPackaging()))
		{
			result.add(project.getBasedir().getAbsolutePath() + "/src/main/webapp");
			log.debug("Adding webapp classpath element: " + result.get(0));
		}
		
		return result;		
	}
}
