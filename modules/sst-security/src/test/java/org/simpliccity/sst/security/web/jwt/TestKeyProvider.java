package org.simpliccity.sst.security.web.jwt;

import java.util.HashMap;
import java.util.Map;

import org.simpliccity.sst.jwt.key.AbstractEncodedMapKeyProvider;
import org.simpliccity.sst.jwt.key.annotation.KeySource;

@KeySource(TestKeyProvider.ALGORITHM)
public class TestKeyProvider extends AbstractEncodedMapKeyProvider 
{
	public static final String ALGORITHM = "HS512";
	
	public static final String JCA_ALGORITHM = "HmacSHA512";
	
	public TestKeyProvider()
	{
		Map<String, String> keyMap = new HashMap<String, String>();
		keyMap.put("SST", "1234");
		keyMap.put("ALT", "5678");
		keyMap.put("UNKNOWN", "5678");
		
		setKeyMap(keyMap);
	}
	
	@Override
	protected String getJcaAlgorithm() 
	{
		return JCA_ALGORITHM;
	}
}
