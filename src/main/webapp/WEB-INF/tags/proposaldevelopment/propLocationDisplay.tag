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
<%@ include file="/kr/WEB-INF/jsp/tldHeader.jsp"%>

<%@ attribute name="index" required="true"%>
<%@ attribute name="docLocation" required="true"%>
<%@ attribute name="locationIter" required="true" type="org.kuali.kra.proposaldevelopment.bo.ProposalLocation"%>
<c:set var="propLocationAttributes"
	value="${DataDictionary.ProposalLocation.attributes}" />

<tr>
	<th class="infoline" width="10%">
		<c:choose>
			<c:when test="${index == -1}">
			        <c:set var="class" value="infoline" />
					Add:
			</c:when>
			<c:otherwise>
			    <c:set var="class" value="" />
				${index+1}
			</c:otherwise>
			 
		</c:choose>
		<input type="hidden" name="${docLocation}.proposalNumber" value="${KualiForm.document.developmentProposalList[0].proposalNumber}">
	</th>
	<td class="${class}" width="20%">
		<kul:htmlControlAttribute
			property="${docLocation}.location"
			attributeEntry="${propLocationAttributes.location}" />
	</td>
	<td class="${class}" width="45%">
	
		<kul:htmlControlAttribute
			property="${docLocation}.rolodexId"
			attributeEntry="${propLocationAttributes.rolodexId}" />
		<c:choose>
			<c:when test="${empty locationIter.rolodexId}">
				<c:out value="(Select)" />
			</c:when>
			<c:otherwise>
				<c:out value="${locationIter.rolodex.addressLine1}" />
			</c:otherwise>
		</c:choose>
		<c:if test="${!empty locationIter.rolodexId}">
			<input type="hidden"
				name="${docLocation}.rolodex.rolodexId"
				value="${locationIter.rolodexId}">
		</c:if>
		
	<c:choose>
			<c:when test="${index == -1}">
		(select)<kul:lookup boClassName="org.kuali.kra.bo.Rolodex" fieldConversions="rolodexId:${docLocation}.rolodexId,postalCode:${docLocation}.rolodex.postalCode,addressLine1:${docLocation}.rolodex.addressLine1,addressLine2:${docLocation}.rolodex.addressLine2,addressLine3:${docLocation}.rolodex.addressLine3,city:${docLocation}.rolodex.city,state:${docLocation}.rolodex.state"  anchor="${currentTabIndex}"/>
	</c:when>
	<c:otherwise>
		<kul:lookup boClassName="org.kuali.kra.bo.Rolodex" fieldConversions="rolodexId:${docLocation}.rolodexId,postalCode:${docLocation}.rolodex.postalCode,addressLine1:${docLocation}.rolodex.addressLine1,addressLine2:${docLocation}.rolodex.addressLine2,addressLine3:${docLocation}.rolodex.addressLine3,city:${docLocation}.rolodex.city,state:${docLocation}.rolodex.state"  anchor="${currentTabIndex}"/>
			</c:otherwise>
		</c:choose>
		
		<c:if test="${index != -1}">
            <kul:directInquiry boClassName="org.kuali.kra.bo.Rolodex" inquiryParameters="${docLocation}.rolodexId:rolodexId" anchor="${currentTabIndex}"/>
		</c:if>
		<br>
		<c:if test="${!empty locationIter.rolodex.addressLine2}">
			<c:out value="${locationIter.rolodex.addressLine2}" />
			<br />
		</c:if>
		<c:if test="${!empty locationIter.rolodex.addressLine3}">
			<c:out value="${locationIter.rolodex.addressLine3}" />
			<br />
		</c:if>
		<c:if test="${!empty locationIter.rolodex.city || !empty locationIter.rolodex.state || !empty locationIter.rolodex.postalCode}">
			<c:out value="${locationIter.rolodex.city}," />&nbsp
            <c:out value="${locationIter.rolodex.state}" />&nbsp
            <c:out value="${locationIter.rolodex.postalCode}" />
		</c:if>
	</td>
	<td class="${class}" width="25%">
	
	<kra:section permission="modifyProposal">
		<c:choose>
			<c:when test="${index == -1}">
				<div align=center>
					<html:image property="methodToCall.addLocation.anchor${currentTabIndex}"
					src='${ConfigProperties.kra.externalizable.images.url}tinybutton-add1.gif' styleClass="tinybutton"/>
				</div>
			</c:when>
			<c:otherwise>
				<div align=center>
				
					<html:image property="methodToCall.clearAddress.line${index}.anchor${currentTabIndex}"
						src='${ConfigProperties.kra.externalizable.images.url}tinybutton-clraddress.gif' styleClass="tinybutton"/>
					<html:image property="methodToCall.deleteLocation.line${index}.anchor${currentTabIndex}"
						src='${ConfigProperties.kra.externalizable.images.url}tinybutton-delete1.gif' styleClass="tinybutton"/>
				</div>
			</c:otherwise>
		</c:choose>
	</kra:section>
	</td>

</tr>
