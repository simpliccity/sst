<!--

    Copyright 2010 Information Control Corporation

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 
-->
<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ tag import="org.simpliccity.sst.web.property.PropertyConfigurator.Entry" %>
<%@ attribute name="items" required="true" rtexprvalue="true" type="java.util.List" %>
<%@ attribute name="bindingProperty" required="true" rtexprvalue="true" type="java.lang.String" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>	
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:forEach items="${items}" var="entry" varStatus="status">									
	<div class="sst-editProperties-row"> 
		<div class="sst-editProperties-title">${entry.name}</div> 
		<div class="sst-editProperties-value"><form:input path="${bindingProperty}[${status.index}].value" /></div>
		<c:if test="${entry.hint != null}"><div class="sst-editProperties-hint">(${entry.hint})</div></c:if> 
		<c:if test="${entry.description != null}">
			<div class="sst-editProperties-description">${entry.description}</div> 
		</c:if>	
		<div class="sst-editProperties-clear"></div>																							
	</div>
	<div class="sst-editProperties-row-separator"></div> 
</c:forEach>
