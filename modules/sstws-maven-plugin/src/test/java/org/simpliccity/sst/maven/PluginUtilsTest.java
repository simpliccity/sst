package org.simpliccity.sst.maven;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PluginUtilsTest 
{
	@Mock private MavenProject project;
	@Mock private Log log;
	
	private List<String> compileDependencies = Arrays.asList(new String[] {"/junk/abc.jar", "/junk/def.jar"});
	private List<String> runtimeDependencies = Arrays.asList(new String[] {"/junk/ghi.jar"});
	private List<String> testDependencies = Arrays.asList(new String[] {"/junk/jkl.jar"});

	@Test
	public void testGetClassLoaderFullClasspath() throws DependencyResolutionRequiredException, IOException, MojoExecutionException
	{
		String basePath = initProject(true);

		ClassLoader loader = PluginUtils.getProjectClassloader(project, log, true);
		
		assertTrue("Classloader type", loader instanceof URLClassLoader);
		
		URLClassLoader urlLoader = (URLClassLoader) loader;
		
		assertTrue("Classpath populated", checkClassPath(urlLoader, basePath, true, true));
	}
	
	@Test
	public void testGetClassLoaderNoTestClasspath() throws DependencyResolutionRequiredException, IOException, MojoExecutionException
	{
		String basePath = initProject(true);

		ClassLoader loader = PluginUtils.getProjectClassloader(project, log, false);
		
		assertTrue("Classloader type", loader instanceof URLClassLoader);
		
		URLClassLoader urlLoader = (URLClassLoader) loader;
		
		assertTrue("Classpath populated", checkClassPath(urlLoader, basePath, false, true));
	}
	
	@Test
	public void testGetClassLoaderNoWarClasspath() throws DependencyResolutionRequiredException, IOException, MojoExecutionException
	{
		String basePath = initProject(false);

		ClassLoader loader = PluginUtils.getProjectClassloader(project, log, true);
		
		assertTrue("Classloader type", loader instanceof URLClassLoader);
		
		URLClassLoader urlLoader = (URLClassLoader) loader;
		
		assertTrue("Classpath populated", checkClassPath(urlLoader, basePath, true, false));
	}

	@Test
	public void testGetClassLoaderNoAdditionalClasspath() throws DependencyResolutionRequiredException, IOException, MojoExecutionException
	{
		String basePath = initProject(false);

		ClassLoader loader = PluginUtils.getProjectClassloader(project, log, false);
		
		assertTrue("Classloader type", loader instanceof URLClassLoader);
		
		URLClassLoader urlLoader = (URLClassLoader) loader;
		
		assertTrue("Classpath populated", checkClassPath(urlLoader, basePath, false, false));
	}

	@Test
	public void testGetCompileDependencies() throws MojoExecutionException, DependencyResolutionRequiredException, IOException
	{
		initProject(false);
		
		List<String> result = PluginUtils.getCompileDependencies(project, log);
		
		assertEquals("Compile dependecies", compileDependencies, result);
	}

	@Test(expected=MojoExecutionException.class)
	public void testGetCompileDependenciesException() throws MojoExecutionException, DependencyResolutionRequiredException
	{
		initProjectException();
		
		PluginUtils.getCompileDependencies(project, log);
	}

	@Test
	public void testGetRuntimeDependencies() throws MojoExecutionException, DependencyResolutionRequiredException, IOException
	{		
		initProject(false);
		
		List<String> result = PluginUtils.getRuntimeDependencies(project, log);
		
		assertEquals("Runtime dependecies", runtimeDependencies, result);
	}

	@Test(expected=MojoExecutionException.class)
	public void testGetRuntimeDependenciesException() throws MojoExecutionException, DependencyResolutionRequiredException
	{
		initProjectException();
		
		PluginUtils.getRuntimeDependencies(project, log);
	}

	@Test
	public void testGetTestDependencies() throws MojoExecutionException, DependencyResolutionRequiredException, IOException
	{		
		initProject(false);
		
		List<String> result = PluginUtils.getTestDependencies(project, log);
		
		assertEquals("Runtime dependecies", testDependencies, result);
	}

	@Test(expected=MojoExecutionException.class)
	public void testGetTestDependenciesException() throws MojoExecutionException, DependencyResolutionRequiredException
	{
		initProjectException();
		
		PluginUtils.getTestDependencies(project, log);
	}
	
	@Test
	public void testGetWarDependencies() throws IOException, DependencyResolutionRequiredException
	{
		String basePath = initProject(true);
		
		List<String> result = PluginUtils.getWarDependencies(project, log);
		
		assertEquals("WAR dependencies", basePath + "/src/main/webapp", result.get(0));
	}

	@Test
	public void testGetNoWarDependencies() throws IOException, DependencyResolutionRequiredException
	{
		initProject(false);
		
		List<String> result = PluginUtils.getWarDependencies(project, log);
		
		assertEquals("No WAR dependencies", 0, result.size());
	}
	
	private String initProject(boolean isWar) throws DependencyResolutionRequiredException, IOException
	{
		// Classpath elements
		when(project.getCompileClasspathElements()).thenReturn(compileDependencies);
		when(project.getRuntimeClasspathElements()).thenReturn(runtimeDependencies);		
		when(project.getTestClasspathElements()).thenReturn(testDependencies);
		
		// Base path
		File basePath = getBasePath();
		when(project.getBasedir()).thenReturn(basePath);
		
		// Project packaging
		when(project.getPackaging()).thenReturn(isWar ? "WAR" : "JAR");
		
		return basePath.getAbsolutePath();
	}

	private void initProjectException() throws DependencyResolutionRequiredException
	{
		when(project.getCompileClasspathElements()).thenThrow(DependencyResolutionRequiredException.class);
		when(project.getRuntimeClasspathElements()).thenThrow(DependencyResolutionRequiredException.class);			
		when(project.getTestClasspathElements()).thenThrow(DependencyResolutionRequiredException.class);			
	}

	private File getBasePath() throws IOException
	{
		File tempFile = File.createTempFile("Test", null);
		File tempLocation = tempFile.getParentFile();
		
		return tempLocation;
	}

	private boolean checkClassPath(URLClassLoader classloader, String basePath, boolean includeTest, boolean isWar) throws IOException
	{
		boolean result = false;
		
		List<URL> urls = Arrays.asList(classloader.getURLs());
		
		List<URL> expectedUrls = getExpectedUrls(basePath, includeTest, isWar);
		
		result = urls.containsAll(expectedUrls);
		
		return result;
	}
	
	private List<URL> getExpectedUrls(String basePath, boolean includeTest, boolean isWar) throws IOException
	{
		List<String> dependencies = new ArrayList<String>();
		
		dependencies.addAll(compileDependencies);
		dependencies.addAll(runtimeDependencies);
		
		if (includeTest)
		{
			dependencies.addAll(testDependencies);
		}
		
		if (isWar)
		{
			dependencies.add(basePath + "/src/main/webapp");
		}
		
		List<File> files = new ArrayList<File>();
		
		for (String dependency : dependencies)
		{
			File file = new File(dependency);
			files.add(file);
		}
		URL[] urls = FileUtils.toURLs(files.toArray(new File[files.size()]));
		return Arrays.asList(urls);
	}
}
