<%--
Copyright 2005-2006 The Kuali Foundation.

Licensed under the Educational Community License, Version 1.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.opensource.org/licenses/ecl1.php

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
	--%>
<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>
<%-- Proposal Actions Page - Submit To Grants.gov Button - Commented Temporarily--%>
<kra:section permission="submitToSponsor">
 <c:set var="extraButtons" value="${KualiForm.extraActionsButtons}" scope="request"/> 
</kra:section>
<kul:documentPage
showDocumentInfo="true"
	htmlFormAction="proposalDevelopmentActions"
		documentTypeName="ProposalDevelopmentDocument"
			renderMultipart="false"
				showTabButtons="true"
					auditCount="0"
						headerDispatch="${KualiForm.headerDispatch}"
							headerTabActive="actions">

<kra-pd:proposalDevelopmentDataValidation /> 
<kra-pd:proposalDevelopmentHierarchy /> 
<kra:section permission="printProposal">
   <kra-pd:proposalDevelopmentPrintForms /> 
</kra:section>
<kra-pd:proposalDevelopmentCopy /> 
<kul:routeLog /> 
<kul:adHocRecipients /> 
<kul:panelFooter />
<c:if test="${not KualiForm.suppressAllButtons}">
          <c:if test="${KualiForm.documentActionFlags.canApprove and KualiForm.reject}">
              <c:set var="extraButtonSource" value="${ConfigProperties.externalizable.images.url}buttonsmall_reject.gif"/>
              <c:set var="extraButtonProperty" value="methodToCall.reject"/>
              <c:set var="extraButtonAlt" value="Reject the document"/>
           </c:if> 

</c:if>

<p>
<kul:documentControls 
transactionalDocument="true"
	extraButtonSource="${extraButtonSource}"
		extraButtonProperty="${extraButtonProperty}"
			extraButtonAlt="${extraButtonAlt}" 
				extraButtons="${extraButtons}" />
</p>

<script language="javascript" src="scripts/kuali_application.js"></script>

</kul:documentPage>