<%--
 Copyright 2006-2009 The Kuali Foundation
 
 Licensed under the Educational Community License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.osedu.org/licenses/ECL-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>
<%@ page
	import="org.kuali.kra.infrastructure.Constants,org.kuali.core.service.DataDictionaryService,org.kuali.kra.infrastructure.KraServiceLocator"%>
<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>
<html>

<%
	String textAreaFieldLabel = null;
	if (request.getParameter(Constants.TEXT_AREA_FIELD_LABEL) == null
			|| request.getParameter(Constants.TEXT_AREA_FIELD_LABEL).trim().equals("")) {
		textAreaFieldLabel = (String) request.getAttribute(Constants.TEXT_AREA_FIELD_LABEL);
	} else {
		textAreaFieldLabel = request.getParameter(Constants.TEXT_AREA_FIELD_LABEL);
	}

%>

<link href="kr/css/kuali.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="scripts/kuali_application.js"></script>
<body onload="setTextArea()">
<div class="headerarea" id="headerarea-small">
<h1><%=textAreaFieldLabel%></h1>
</div>

<c:set var="kraAttributeReferenceDummyAttributes"
	value="${DataDictionary.KraAttributeReferenceDummy.attributes}" />
<c:if test="${empty textAreaFieldName}">
	<c:set var="textAreaFieldName"
		value="<%=request.getParameter(\"textAreaFieldName\")%>" />
</c:if>
<c:if test="${empty htmlFormAction}">
	<c:set var="htmlFormAction"
		value="<%=request.getParameter(\"htmlFormAction\")%>" />
</c:if>

<html:form styleId="kualiForm" method="post"
	action="/${htmlFormAction}.do" enctype=""
	onsubmit="return hasFormAlreadyBeenSubmitted();">
	<table align="center">
		<tr>
			<td><kul:htmlControlAttribute property="${textAreaFieldName}"
				attributeEntry="${kraAttributeReferenceDummyAttributes.bigDescription}" />

			</td>
		</tr>

		<tr>
			<td>
			<div id="globalbuttons" class="globalbuttons"><input
				type="image" name="methodToCall.postTextAreaToParent.anchor${textAreaFieldAnchor}"
				onclick='javascript:postValueToParentWindow();return false'
				src="${ConfigProperties.kra.externalizable.images.url}buttonsmall_return.gif"
				class="globalbuttons" title="return" alt="return"></div>
			</td>
		</tr>
	</table>
		<c:choose>
		<c:when test="${KualiForm.document.sessionDocument}" >
			<html:hidden property="documentWebScope" value="session"/>	
			<html:hidden property="formKey" value="${KualiForm.formKey}"/>	
			<html:hidden property="docFormKey" value="${KualiForm.formKey}"/>	
		</c:when>
		<c:otherwise>
			<html:hidden property="documentWebScope" value="request"/>	
		</c:otherwise>
	</c:choose>
	
</html:form>
</body>

</html>
