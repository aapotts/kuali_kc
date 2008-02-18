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
<%@ page import="org.kuali.kra.infrastructure.Constants"%>
<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<kul:documentPage
	showDocumentInfo="true"
	htmlFormAction="proposalDevelopmentGrantsGov"
	documentTypeName="ProposalDevelopmentDocument"
	renderMultipart="false"
	showTabButtons="false"
	auditCount="0"
  	headerDispatch="save"
  	headerTabActive="grantsGov">
<center>Under Construction</center>

<kra-pd:proposalDevelopmentGrantsGovOpportunitySearch />
<kra-pd:proposalDevelopmentGrantsGovOpportunity />
<kra-pd:proposalDevelopmentGrantsGovSubmissionDetails />
<kra-pd:proposalDevelopmentGrantsGovForms />

<kul:panelFooter />
<kul:documentControls transactionalDocument="true" suppressRoutingControls="true" />
<html:image src="${ConfigProperties.kr.externalizable.images.url}buttonsmall_submit.gif" styleClass="globalbuttons" property="methodToCall.submitToGrantsGov" title="Submit" alt="Submit"/>
</kul:documentPage>
