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

package org.simpliccity.sst.ws.endpoint.security.schema;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name="", propOrder={"from", "to", "message"})
@XmlRootElement(name="SendMultipleGreetingsRequest")
public class SendMultipleGreetingsRequest 
{
	private String from;
	private List<String> to;
	private String message;
	
	public String getFrom() 
	{
		return from;
	}
	
	@XmlElement(required = true)
	public void setFrom(String from) 
	{
		this.from = from;
	}

	public List<String> getTo() 
	{
		return to;
	}

	@XmlElement(required = true)
	public void setTo(List<String> to) 
	{
		this.to = to;
	}

	public String getMessage() 
	{
		return message;
	}

	@XmlElement(required = true)
	public void setMessage(String message) 
	{
		this.message = message;
	}
}
