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
<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>
<kul:documentPage
	showDocumentInfo="true"
	htmlFormAction="awardPaymentReportsAndTerms"
	documentTypeName="AwardDocument"
	renderMultipart="false"
	showTabButtons="true"
	auditCount="0"
  	headerDispatch="${KualiForm.headerDispatch}"
  	headerTabActive="paymentReportsAndTerms">
 	
  	
This is the Award Payments, Reports & Terms - Under Construction

<div align="right"><kul:help documentTypeName="AwardDocument" pageName="PaymentReportsAndTerms" /></div>

<kra-a:awardPaymentAndInvoices />
<kra-a:awardReports />
<kra-a:awardReportingRequirements />
<kra-a:awardTerms />
<kra-a:awardSpecialApproval />
<kra-a:awardProposalDue />
<kra-a:awardCloseout />

<kul:panelFooter />

<SCRIPT type="text/javascript">
var kualiForm = document.forms['KualiForm'];
var kualiElements = kualiForm.elements;
</SCRIPT>
<script language="javascript" src="scripts/kuali_application.js"></script>


<kul:documentControls transactionalDocument="true" suppressRoutingControls="true" />

</kul:documentPage>