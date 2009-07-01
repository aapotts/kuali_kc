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
<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>
<%@ page import="java.util.HashMap" %>

<c:set var="proposalDevelopmentAttributes" value="${DataDictionary.ProposalDevelopmentDocument.attributes}" />
<c:set var="proposalPersonAttributes" value="${DataDictionary.ProposalPerson.attributes}" />
<c:set var="readOnly" value="${not KualiForm.editingMode['modifyProposal']}" scope="request" />

<jsp:useBean id="newMap" class="java.util.HashMap" scope="request" />

<kul:documentPage
	showDocumentInfo="true"
	htmlFormAction="proposalDevelopmentKeyPersonnel"
	documentTypeName="ProposalDevelopmentDocument"
	renderMultipart="false"
	showTabButtons="true"
	auditCount="0"
  	headerDispatch="${KualiForm.headerDispatch}"
  	headerTabActive="keyPersonnel">
  	
  	<div align="right"><kul:help documentTypeName="ProposalDevelopmentDocument" pageName="Key Personnel" /></div>

<c:set var="viewOnly" value="${not KualiForm.editingMode['modifyProposal']}" />
<kra:section permission="modifyProposal">
    <kul:uncollapsable tabTitle="Add Key Personnel" tabErrorKey="newProposalPerson.*" auditCluster="keyPersonnelAuditErrors" tabAuditKey="newProposalPerson*">
          <div align="center">
            <table  cellpadding="0" cellspacing="0" class="grid" summary="">
              <tr>
                <th class="grid"><div align="right">*Person:</div></th>
                <td nowrap class="grid">
<c:choose>                  
  <c:when test="${empty KualiForm.newPersonId && empty KualiForm.newRolodexId}">
                <label> Employee Search</label>
                  <label><kul:lookup boClassName="org.kuali.kra.bo.Person" 
                                fieldConversions="personId:newPersonId" /></label><br>
                  <label>Non-employee Search</label> 
                  <label><kul:lookup boClassName="org.kuali.kra.bo.NonOrganizationalRolodex" 
                                fieldConversions="rolodexId:newRolodexId" /></label></td>
  </c:when>
  <c:otherwise>
                  <label><kul:htmlControlAttribute property="newProposalPerson.fullName" attributeEntry="${proposalPersonAttributes.fullName}" readOnly="true"/></label><br/>
  </c:otherwise>
</c:choose>
                </td>
                <th class="grid"><div align="right">*Proposal Role:</div></th>
                <td class="grid" >
<c:set var="roleIdAttribute" value="${proposalPersonAttributes.proposalPersonRoleId}" />
<c:if test="${KualiForm.document.sponsor.acronym == 'NIH'}">
  <c:set var="roleIdAttribute" value="${proposalPersonAttributes.nonNihProposalPersonRoleId}" />
</c:if>
                  <kul:htmlControlAttribute property="newProposalPerson.proposalPersonRoleId" attributeEntry="${proposalPersonAttributes.proposalPersonRoleId}" />
                </td>
              </tr>
            </table>
           
            <br>
            <html:image property="methodToCall.clearProposalPerson" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-clear1.gif" title="Clear Fields" alt="Clear Fields" styleClass="tinybutton"/>
            <html:image property="methodToCall.insertProposalPerson" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-addpers.gif" title="Add Proposal Person" alt="Add Proposal Person" styleClass="tinybutton"/>
            <c:choose>
            <c:when test="${KualiForm.document.nih}">
            <br>
             <strong>PI/Contact is a required Proposal Role prior to submission. Only one PI/Contact is allowed. 
            For single PI submissions, please designate the lead investigator as PI/Contact & other senior personnel as Key Persons. 
            For multiple PI submissions, please designate one PI/Contact, additional lead investigators as PI/Multiple, & other senior personnel as Key Persons. </strong>
            <br>
            </c:when>
            <c:otherwise>
            <br>
            <strong>Principal Investigator is a required field prior to submission. Only one PI is allowed.</strong>
            </c:otherwise>
            </c:choose>
          </div>
    </kul:uncollapsable>
</kra:section>

    <br/>

    <kra-pd:keyPersons/>

  <c:if test="${not empty viewOnly && ! viewOnly and fn:length(KualiForm.document.proposalPersons) > 0}">
  	<c:set var="extraButtonSource" value="${ConfigProperties.externalizable.images.url}buttonsmall_deletesel.gif"/>
  	<c:set var="extraButtonProperty" value="methodToCall.deletePerson"/>
  	<c:set var="extraButtonAlt" value="Delete a Key Person"/>
  </c:if>  
  
  	<p>
	<kul:documentControls 
		transactionalDocument="false"
		suppressRoutingControls="true"
		extraButtonSource="${extraButtonSource}"
		extraButtonProperty="${extraButtonProperty}"
		extraButtonAlt="${extraButtonAlt}"
		viewOnly="${viewOnly}"
		/>
	</p>

<SCRIPT type="text/javascript">
var kualiForm = document.forms['KualiForm'];
var kualiElements = kualiForm.elements;
</SCRIPT>
<script language="javascript" src="scripts/kuali_application.js"></script>
<script language="javascript" src="dwr/interface/UnitService.js"></script>

</kul:documentPage>
