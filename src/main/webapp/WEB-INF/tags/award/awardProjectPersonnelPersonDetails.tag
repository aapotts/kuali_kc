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
<%-- Member of awardProjectPersonnel.tag --%>

<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<%@ attribute name="awardContact" required="true" type="org.kuali.kra.award.contacts.AwardPerson" %>
<%@ attribute name="awardContactRowStatusIndex" required="true" %>
<c:set var="keypersonrole" value="<%=org.kuali.kra.infrastructure.Constants.KEY_PERSON_ROLE%>" />

<c:set var="awardPersonAttributes" value="${DataDictionary.AwardPerson.attributes}" />

<kra:innerTab tabTitle="Person Details" parentTab="${awardContact.fullName}" defaultOpen="false" tabErrorKey="document.award.projectPersons*">
	<table>
		<tr>
			<th class="infoline">
				<div align="right">
					<kul:htmlAttributeLabel attributeEntry="${awardPersonAttributes.faculty}" useShortLabel="true" noColon="false" />
				</div>
			</th>
			<td>
				<kul:htmlControlAttribute property="document.awardList[0].projectPersons[${awardContactRowStatusIndex}].faculty" 
											attributeEntry="${awardPersonAttributes.faculty}" />
			</td>
			<th class="infoline">
				<div align="right">
					<kul:htmlAttributeLabel attributeEntry="${awardPersonAttributes.academicYearEffort}" useShortLabel="true" noColon="false" />
				</div>
			</th>
			<td>
				<kul:htmlControlAttribute property="document.awardList[0].projectPersons[${awardContactRowStatusIndex}].academicYearEffort" 
											attributeEntry="${awardPersonAttributes.academicYearEffort}" styleClass="amount"/>
			</td>
		</tr>
		<tr>
			<th class="infoline">
				<div align="right">
					<kul:htmlAttributeLabel attributeEntry="${awardPersonAttributes.totalEffort}" useShortLabel="true" noColon="false" />
				</div>
			</th>
			<td>
				<kul:htmlControlAttribute property="document.awardList[0].projectPersons[${awardContactRowStatusIndex}].totalEffort" 
											attributeEntry="${awardPersonAttributes.totalEffort}" styleClass="amount"/>
			</td>
			<th class="infoline">
				<div align="right">
					<kul:htmlAttributeLabel attributeEntry="${awardPersonAttributes.summerEffort}" useShortLabel="true" noColon="false" />
				</div>
			</th>
			<td>
				<kul:htmlControlAttribute property="document.awardList[0].projectPersons[${awardContactRowStatusIndex}].summerEffort" 
											attributeEntry="${awardPersonAttributes.summerEffort}" styleClass="amount"/>
			</td>
		</tr>
		<tr>
		  <c:choose>
		   <c:when test="${KualiForm.document.awardList[0].projectPersons[awardContactRowStatusIndex].contactRole.roleCode == keypersonrole}">
		    <th class="infoline">
		    	<div align="right">
					*<kul:htmlAttributeLabel attributeEntry="${awardPersonAttributes.keyPersonRole}" useShortLabel="true" noColon="false" />
				</div>
		    </th> 
		    <td>
				<kul:htmlControlAttribute property="document.awardList[0].projectPersons[${awardContactRowStatusIndex}].keyPersonRole" 
										attributeEntry="${awardPersonAttributes.keyPersonRole}"/>		    
		    </td>
		   </c:when><c:otherwise>
			<th class="infoline">&nbsp;</th>
			<td>&nbsp;</td>
		   </c:otherwise>
		  </c:choose>
			<th class="infoline">
				<div align="right">
					<kul:htmlAttributeLabel attributeEntry="${awardPersonAttributes.calendarYearEffort}" useShortLabel="true" noColon="false" />
				</div>
			</th>
			<td>
				<kul:htmlControlAttribute property="document.awardList[0].projectPersons[${awardContactRowStatusIndex}].calendarYearEffort" 
											attributeEntry="${awardPersonAttributes.calendarYearEffort}" styleClass="amount"/>
			</td>
		</tr>	            				
	</table>
</kra:innerTab>
