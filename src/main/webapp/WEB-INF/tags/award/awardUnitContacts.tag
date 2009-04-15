<%--
 Copyright 2006-2008 The Kuali Foundation
 
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
<%-- member of AwardContacts.jsp --%>

<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<c:set var="awardContactAttributes" value="${DataDictionary.AwardContact.attributes}" />

<%-- kra:section permission="modifyAward" --%>
<kul:tab defaultOpen="false" tabItemCount="${KualiForm.unitContactsBean.unitContactsCount}" 
				tabTitle="Unit Contacts" tabErrorKey="newAwardContact*,document.awardList[0].awardContactsCount*" >
	<div class="tab-container" align="center">
		<h3>
			<span class="subhead-left">Unit Contacts</span>
			<span class="subhead-right"><kul:help businessObjectClassName="org.kuali.kra.award.bo.AwardContact" altText="help"/></span>
		</h3>
	    <table id="contacts-table" cellpadding="0" cellspacing="0" summary="Unit Contacts">
			<tr>
				<th scope="row" width="5%">&nbsp;</th>
				<th width="15%">Person</th>
				<th width="15%">Unit</th>
				<th width="20%">Project Role</th>
				<th width="15%">Office Phone</th>
				<th width="15%">Email</th>
				<th width="15%"><div align="center">Actions</div></th>
			</tr>
			
			<tr>
				<th class="infoline" scope="row">Add</th>
				<td nowrap class="grid" class="infoline">
					<c:choose>                  
						<c:when test="${empty KualiForm.unitContactsBean.newAwardContact.contact.identifier}">
							<div>
	        					<input type="text" size="20" value="" readonly="true"/>
	              				<label>
	              					<kul:lookup boClassName="org.kuali.kra.bo.Person" fieldConversions="personId:unitContactsBean.personId" anchor="${tabKey}" 
		  	 									lookupParameters="unitContactsBean.personId:personId"/>
		  	 					</label>
		  	 				</div>
						</c:when>
						<c:otherwise>
							<div align="center">
	              				<label><kul:htmlControlAttribute property="unitContactsBean.newAwardContact.fullName" 
	              							attributeEntry="${awardContactAttributes.fullName}" readOnly="true"/></label>
																				            			
							</div>
						</c:otherwise>
					</c:choose>
        		</td>
	        	<td class="infoline">
	        		<div align="center">
	        			<c:out value="${KualiForm.unitContactsBean.newAwardContact.contactOrganizationName}" />&nbsp;
	        		</div>
				</td>
	        	<td class="infoline" style="font-size: 80%">
	        		<div align="center">
		        		<html:select property="unitContactsBean.contactRoleCode" styleClass="fixed-size-300-select">
		        			<c:forEach var="role" items="${KualiForm.unitContactsBean.contactRoles}">
		        				<option value="${role.roleCode}"><c:out value="${role.roleDescription}" /></option>
		        			</c:forEach>                		
						</html:select>
					</div>
	        	</td>
	        	<td class="infoline">
	        		<c:out value="${KualiForm.unitContactsBean.newAwardContact.contact.phoneNumber}" />&nbsp;
	        	</td>
	        	<td class="infoline">
	        		<c:out value="${KualiForm.unitContactsBean.newAwardContact.contact.emailAddress}" />&nbsp;
	        	</td>
	        	<td class="infoline">
	        		<c:choose>
		        		<c:when test="${not empty KualiForm.unitContactsBean.newAwardContact.contact.identifier}">
			        		<div align="center">	        			
			        			<html:image property="methodToCall.addUnitContact" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-add1.gif" title="Add Contact" alt="Add Contact" styleClass="tinybutton" />
			        			<html:image property="methodToCall.clearNewUnitContact" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-clear1.gif" title="Clear Fields" alt="Clear Fields" styleClass="tinybutton" />
			        		</div>
			        	</c:when>
			        	<c:otherwise>&nbsp;</c:otherwise>
			        </c:choose>
	        	</td>
			</tr>
				
			<c:forEach var="awardContact" items="${KualiForm.unitContactsBean.unitContacts}" varStatus="awardContactRowStatus">
				<tr>
					<th class="infoline" scope="row">
						<c:out value="${awardContactRowStatus.index + 1}" />
					</th>
	                <td valign="middle">
	                	<div align="center">
	                		${awardContact.fullName}&nbsp;
	                		<c:choose>
		                		<c:when test="${awardContact.employee}">
		                			<kul:directInquiry boClassName="org.kuali.kra.bo.Person" inquiryParameters="'${awardContact.contact.identifier}':personId" anchor="${tabKey}" />
		                		</c:when>
		                		<c:otherwise>
		                			<kul:directInquiry boClassName="org.kuali.kra.bo.NonOrganizationalRolodex" inquiryParameters="'${awardContact.contact.identifier}':rolodexId" anchor="${tabKey}" />
		                		</c:otherwise>
		                	</c:choose>
						</div>
					</td>
	                <td valign="middle">
	                	<div align="center">
							${awardContact.contactOrganizationName}&nbsp;
						</div>
					</td>
	                <td valign="middle">
	                	<div align="center">
	                	<html:select name="awardContact" property="contactRoleCode" styleClass="fixed-size-300-select">
	                		<c:forEach var="role" items="${KualiForm.unitContactsBean.contactRoles}">
		        				<c:if test="${awardContact.contactRoleCode != role.roleCode}">
		        					<option value="${role.roleCode}"><c:out value="${role.roleDescription}" /></option>
		        				</c:if>
		        				<c:if test="${awardContact.contactRoleCode == role.roleCode}">
		        					<option value="${role.roleCode}" selected="true"><c:out value="${role.roleDescription}" /></option>
		        				</c:if>
		        			</c:forEach>                		
						</html:select>
					</div>
					</td>
					<td valign="middle">
						<div align="center">
	                		${awardContact.phoneNumber}&nbsp;
	                	</div> 
					</td>
	                <td valign="middle">
	                	<div align="center">                	
							${awardContact.emailAddress}&nbsp;
						</div> 
					</td>
	                
					<td class="infoline">
						<div align="center">
							<html:image property="methodToCall.deleteUnitContact.line${awardContactRowStatus.index}.anchor${currentTabIndex}"
							src='${ConfigProperties.kra.externalizable.images.url}tinybutton-delete1.gif' styleClass="tinybutton"/>
						</div>
	                </td>
	            </tr>
    		</c:forEach>	    	
    	</table>
	</div>
</kul:tab>