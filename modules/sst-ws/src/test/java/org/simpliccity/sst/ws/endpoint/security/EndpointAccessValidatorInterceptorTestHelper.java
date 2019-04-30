package org.simpliccity.sst.ws.endpoint.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Arrays;

import javax.xml.transform.Source;

import org.springframework.core.io.ClassPathResource;
import org.springframework.xml.transform.StringSource;

public class EndpointAccessValidatorInterceptorTestHelper 
{
	private static final int BUFFER_SIZE = 1024;
	private static final String RESOURCE_TEMPLATE = "SendGreetingRequestTemplate.xml";
	private static final String RESOURCE_WAVE_TEMPLATE = "WaveRequestTemplate.xml";
	private static final String RESOURCE_MULTIPLE_TEMPLATE = "SendMultipleGreetingsRequestTemplate.xml";
	private static final String RESOURCE_SHOUTOUT_TEMPLATE = "ShoutOutRequestTemplate.xml";
	private static final String RESOURCE_GREETING_TEMPLATE = "GreetingTemplate.xml";
	private static final String RESOURCE_RESULT = "SendGreetingResult.xml";
	private static final String RESOURCE_WAVE_RESULT = "WaveResult.xml";
	private static final String RESOURCE_MULTIPLE_RESULT = "SendMultipleGreetingsResult.xml";
	private static final String RESOURCE_SHOUTOUT_RESULT = "ShoutOutResult.xml";
	private static final String TEMPLATE_NAME = "<sgr:{0}>{1}</sgr:{0}>";
	
	public static final String GENERAL_GREETING = "How are things?";

	private static String templateText;
	
	private static String waveTemplateText;
	
	private static String multipleTemplateText;
	
	private static String shoutoutTemplateText;
	
	private static String greetingTemplateText;
	
	private static String resultText;
	
	private static String waveResultText;
	
	private static String multipleResultText;
	
	private static String shoutoutResultText;
	
	static
	{
		templateText = loadTemplateResource(RESOURCE_TEMPLATE);
		
		resultText = loadTemplateResource(RESOURCE_RESULT);
		
		multipleTemplateText = loadTemplateResource(RESOURCE_MULTIPLE_TEMPLATE);
		
		shoutoutTemplateText = loadTemplateResource(RESOURCE_SHOUTOUT_TEMPLATE);
		
		greetingTemplateText = loadTemplateResource(RESOURCE_GREETING_TEMPLATE);
		
		waveTemplateText = loadTemplateResource(RESOURCE_WAVE_TEMPLATE);
		
		waveResultText = loadTemplateResource(RESOURCE_WAVE_RESULT);
		
		multipleResultText = loadTemplateResource(RESOURCE_MULTIPLE_RESULT);
		
		shoutoutResultText = loadTemplateResource(RESOURCE_SHOUTOUT_RESULT);
	}

	public static Source generateSendGreetingMessage(String user, String password, String from, String to, String message)
	{
		return generateMessage(templateText, user, password, from, to, message);
	}
	
	public static Source generateSendGreetingResultMessage(String user, String password, String from, String to, String message)
	{
		String greeting = MessageFormat.format(greetingTemplateText, user, password, from, message, to);
		return generateMessage(resultText, greeting);
	}
	
	public static Source generateWaveMessage(String user, String password, String from, String[] names)
	{
		String formattedNames = formatNames("name", names);
		return generateMessage(waveTemplateText, user, password, from, formattedNames);
	}
	
	public static Source generateWaveResultMessage(String name)
	{
		return generateMessage(waveResultText, name);
	}
	
	public static Source generateSendMultipleGreetingsMessage(String user, String password, String from, String[] to, String message)
	{
		String formattedTo = formatNames("to", to);
		return generateMessage(multipleTemplateText, user, password, from, formattedTo, message);
	}
	
	public static Source generateSendMultipleGreetingsResultMessage(String user, String password, String from, String[] to, String message)
	{
		String greetings = formatMultiple(greetingTemplateText, new String[] {user, password, from, message}, (Object[]) to);
		return generateMessage(multipleResultText, greetings);
	}
	
	public static Source generateShoutOutMessage(String user, String password, String name)
	{
		return generateMessage(shoutoutTemplateText, user, password, name);		
	}

	public static Source generateShoutOutResultMessage(String name)
	{
		return generateMessage(shoutoutResultText, name);		
	}

	private static Source generateMessage(String template, Object... parms)
	{
		String soapMessage = MessageFormat.format(template, parms);
		
		return new StringSource(soapMessage);
	}
	
	private static String formatNames(String tag, String[] names)
	{
		return formatMultiple(TEMPLATE_NAME, new String[] {tag}, (Object[]) names);
	}
	
	private static String formatMultiple(String template, String[] fixed, Object... parms)
	{
		MessageFormat format = new MessageFormat(template);
		StringBuffer result = new StringBuffer();
		Object[] workingParms = Arrays.copyOf((Object[]) fixed, fixed.length + 1);
		for (Object parm : parms)
		{
			workingParms[fixed.length] = parm;
			result = format.format(workingParms, result, null);
		}
		
		return result.toString();
	}

    private static String loadTemplateResource(String resourceName)
    {
          StringBuffer pattern = new StringBuffer(BUFFER_SIZE);
          BufferedReader reader = null;
          try
          {
                ClassPathResource resource = new ClassPathResource(resourceName);
                reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

                char[] readBuffer = new char[BUFFER_SIZE];
                int count;
                while((count = reader.read(readBuffer)) >= 0)
                {
                      pattern.append(readBuffer, 0, count);
                }
          }
          catch (IOException e)
          {
        	  // Nothing useful to do
          }
          finally
          {
                if (reader != null)
                {
                	try
                	{
                		reader.close();
                	}
                	catch (IOException e)
                	{
                		// Still nothing useful to do
                	}
                }
          }
         
          return pattern.toString();
    }
}
