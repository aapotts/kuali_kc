 <%--
 Copyright 2005-2010 The Kuali Foundation

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
<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<c:set var="coiDiscDetailAttributes" value="${DataDictionary.CoiDiscDetail.attributes}" />
<c:set var="financialEntityAttributes" value="${DataDictionary.PersonFinIntDisclosure.attributes}" />
<c:set var="proposal" value="${KualiForm.document.coiDisclosureList[0].eventBo}" />
<kul:tab defaultOpen="false" tabTitle="Project & Financial Entity Relationships (${KualiForm.document.coiDisclosureList[0].completeMessage})" auditCluster="financialEntityDiscAuditErrors" tabAuditKey="document.coiDisclosureList[0].coiDiscDetails*" useRiceAuditMode="true"
    tabErrorKey="document.coiDisclosureList[0].coiDiscDetails*" >
	<div class="tab-container" align="center">
	 <div>
    	<h3>
    		<span class="subhead-left">Proposal</span>
    		<span class="subhead-right"><kul:help businessObjectClassName="org.kuali.kra.coi.CoiDiscDetail" altText="help"/></span>
        </h3>
        
        <table id="disclosurefe-table" cellpadding="0" cellspacing="0" summary="">
          	<%-- Header --%>
                                 <tr>
                                    <th><div align="center">Proposal Number</div></th> 
                                    <th><div align="center">Proposal Name</div></th> 
                                    <th><div align="center">Sponsor</div></th> 
                                    <th><div align="center">start Date</div></th> 
                                    <th><div align="center">End Date</div></th> 
                                    <th><div align="center">PI</div></th> 
                                </tr>
                        <%-- Header --%>
                        
                         <%-- New data --%>
                        <%-- kra:permission value="${KualiForm.disclosureHelper.modifyPersonnel}" --%>
                <tr>
                  <td align="left" valign="middle">
					<div align="left">
                		${proposal.proposalNumber}
					</div>
				  </td>
                  <td align="left" valign="middle">
					<div align="left">
                		${proposal.title}
					</div>
				  </td>
                  <td align="left" valign="middle">
					<div align="left">
                		${proposal.sponsor.sponsorName} 
					</div>
				  </td>
                  <td align="left" valign="middle">
					<div align="left">
                 	    ${proposal.requestedStartDateInitial}
					</div>
					</td>
                  <td align="left" valign="middle">
					<div align="left">
                 	    ${proposal.requestedEndDateInitial}
					</div>
					</td>
                  <td align="left" valign="middle">
					<div align="left">
				         ${KualiForm.document.coiDisclosureList[0].disclosurePersons[0].reporter.fullName}
					</div>
				  </td>
	            </tr>
             </table>
             </div>
            <%-- Existing data --%>
	<div>
    	<h3>
    		<span class="subhead-left">Financial Entities</span>
    		<span class="subhead-right"><kul:help businessObjectClassName="org.kuali.kra.coi.CoiDiscDetail" altText="help"/></span>
        </h3>
        
        <table id="disclosurefe-table" cellpadding="0" cellspacing="0" summary="">
        
                  	<tr>
          		<kul:htmlAttributeHeaderCell literalLabel="&nbsp;" scope="col" /> 
          		<kul:htmlAttributeHeaderCell literalLabel="Entity"  scope="col" />
          		<th rowspan="1" colspan="1" scope="col">${KualiForm.disclosureHelper.conflictHeaderLabel}</th>
          		<kul:htmlAttributeHeaderCell attributeEntry="${coiDiscDetailAttributes.comments}" scope="col" />
          		<kul:htmlAttributeHeaderCell literalLabel="Action" scope="col" /> 
          	</tr> 
	             <tr>
					<th class="infoline">
						&nbsp;
					</th>
					<th class="infoline">
                       &nbsp;
					</th>
	
	                <td align="left" valign="middle"  class="infoline">
						<div align="center">
							<html:image property="methodToCall.allConflict.anchor${tabKey}"
							src='${ConfigProperties.kra.externalizable.images.url}tinybutton-all.gif' styleClass="conflict" onclick="$j('select.conflictClass${idx}').val('2');return false;" />
							<html:image property="methodToCall.noneConflict.anchor${tabKey}"
							src='${ConfigProperties.kra.externalizable.images.url}tinybutton-none.gif' styleClass="conflict" onclick="$j('select.conflictClass${idx}').val('1');return false;" />
						</div>
	                </td>
	                <td align="left" valign="middle" class="infoline">
	                	&nbsp;
					</td>
					<th class="infoline">
						<div align="center">
							<html:image property="methodToCall.newFinancialEntity.anchor${tabKey}"
							src='${ConfigProperties.kra.externalizable.images.url}tinybutton-newfinancialentity.gif' styleClass="tinybutton"/>
						</div>
					</th>
	            </tr>
            
        
        
        	<c:forEach var="disclosureDetail" items="${KualiForm.document.coiDisclosureList[0].coiDiscDetails}" varStatus="status">
	             <tr>
					<th class="infoline">
						<c:out value="${status.index+1}" />
					</th>
                  <td align="left" valign="middle">
					<div align="left">
                		<kul:htmlControlAttribute property="document.coiDisclosureList[0].coiDiscDetails[${status.index}].personFinIntDisclosure.entityName" readOnly="true" attributeEntry="${financialEntityAttributes.entityName}" /> 
					</div>
				  </td>
                  <td align="left" valign="middle">
					<div align="left">
                		<kul:htmlControlAttribute property="document.coiDisclosureList[0].coiDiscDetails[${status.index}].entityStatusCode" 
                			readOnly="${readOnly}" attributeEntry="${coiDiscDetailAttributes.entityStatusCode}"  styleClass="conflictClass${idx}"/>
					</div>
				  </td>
                  <td align="left" valign="middle">
					<div align="left">
                		<kul:htmlControlAttribute property="document.coiDisclosureList[0].coiDiscDetails[${status.index}].comments" 
                			readOnly="${readOnly}" attributeEntry="${coiDiscDetailAttributes.comments}" />
					</div>
				  </td>
                  <td align="left" valign="middle">
					<div align="center">
							<c:if test="${KualiForm.disclosureHelper.canViewDisclosureFeHistory}">		
                                <a class="disclosureFeView" id="viewEntitySummary${status.index}" title="${disclosureDetail.personFinIntDisclosure.entityName} Summary" href="${pageContext.request.contextPath}/financialEntityEditList.do?methodToCall=viewFinancialEntity&status=activecoi&index=${disclosureDetail.personFinIntDisclosure.personFinIntDisclosureId}" scrolling="no" noresize>
						 	        <html:image src='${ConfigProperties.kra.externalizable.images.url}tinybutton-view.gif' styleClass="tinybutton" title="View Entity"/>
                    	        </a>   
                    	    </c:if>         
							<c:if test="${KualiForm.disclosureHelper.canEditDisclosureFinancialEntity}">		
							    <html:image property="methodToCall.editFinancialEntity.line${status.index}.anchor${currentTabIndex}"
									src='${ConfigProperties.kra.externalizable.images.url}tinybutton-edit1.gif' styleClass="tinybutton"/>
                    	     </c:if>
							<c:if test="${KualiForm.disclosureHelper.canViewDisclosureFeHistory}">		
						        <a class="disclosureFeHistory" id="history${status.index}" title="${disclosureDetail.personFinIntDisclosure.entityName} History" href="${pageContext.request.contextPath}/financialEntityEditList.do?methodToCall=showFinancialEntityHistory&status=activecoi&index=${disclosureDetail.personFinIntDisclosure.personFinIntDisclosureId}">
							        <html:image property="methodToCall.historyFinancialEntity.line${status.index}.anchor${currentTabIndex}"
									    src='${ConfigProperties.kra.externalizable.images.url}tinybutton-history.gif' styleClass="tinybutton"/>
                    	         </a>
                    	     </c:if>
					</div>
				  </td>
	            </tr>
        	</c:forEach> 
            <%-- Existing data --%>
        </table>
      </div>
    </div>
</kul:tab>

