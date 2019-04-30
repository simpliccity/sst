/*
 *    Copyright 2013 Information Control Corporation
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

package org.simpliccity.sst.property.config;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.beans.factory.parsing.ProblemReporter;
import org.springframework.beans.factory.parsing.ReaderEventListener;
import org.springframework.beans.factory.parsing.SourceExtractor;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.NamespaceHandlerResolver;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.core.io.Resource;
import org.w3c.dom.Element;

@RunWith(MockitoJUnitRunner.class)
public class PropertyBeanDefinitionParserTest 
{
	@Mock private Element element;
	@Mock private Resource resource;
	@Mock private ProblemReporter problemReporter;
	@Mock private ReaderEventListener readerEventListener;
	@Mock private SourceExtractor sourceExtractor;
	@Mock private XmlBeanDefinitionReader reader;
	@Mock private NamespaceHandlerResolver resolver;
	@Mock private BeanDefinitionParserDelegate parserDelegate;
	private XmlReaderContext readerContext;
	private ParserContext parserContext;
	
	@Before
	public void initialize()
	{
		// Setup needed to support ability of PropertyBeanDefinitionParser to generate exception (workaround for mocking issues)
		
		readerContext = new XmlReaderContext(resource, problemReporter, readerEventListener, sourceExtractor, reader, resolver);
		
		parserContext = new ParserContext(readerContext, parserDelegate);
	}
	
	@Test(expected=BeanDefinitionParsingException.class)
	public void testInvalidConfiguration()
	{
		when(element.getAttribute("loader")).thenReturn(null);
		
		PropertyBeanDefinitionParser parser = new PropertyBeanDefinitionParser();
		
		// The call should fail with the expected exception
		parser.parseInternal(element, parserContext);
	}
}
