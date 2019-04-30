package org.simpliccity.sst.security.web.jwt;

import java.io.Serializable;

public class CapitalizedString implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private String originalString;
	private String capitalizedString;
	
	public CapitalizedString(String originalString)
	{
		this.originalString = originalString;
		this.capitalizedString = this.originalString.toUpperCase();
	}
	
	public String getOriginalString() 
	{
		return originalString;
	}
	
	public void setOriginalString(String originalString) 
	{
		this.originalString = originalString;
	}
	
	public String getCapitalizedString() 
	{
		return capitalizedString;
	}
	
	public void setCapitalizedString(String capitalizedString) 
	{
		this.capitalizedString = capitalizedString;
	}
}
