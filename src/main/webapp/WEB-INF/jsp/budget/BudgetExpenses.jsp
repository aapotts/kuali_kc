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
<c:set var="extraButtons" value="${KualiForm.extraExpensesButtons}" scope="request"/>
<c:set var="readOnly" value="${not KualiForm.editingMode['modifyBudgets']}" scope="request" />
<c:if test="${readOnly}">
	<c:set var="extraButtons" value="" scope="request"/>
</c:if>

<kul:documentPage
	showDocumentInfo="true"
	htmlFormAction="budgetExpenses"
	documentTypeName="BudgetDocument"
  	headerDispatch="${KualiForm.headerDispatch}"
  	showTabButtons="true"
  	headerTabActive="expenses"
  	extraTopButtons="${KualiForm.extraTopButtons}">
		
	<kra-b:budgetExpenses /> 
	<kul:panelFooter />
		
<kul:documentControls 
		transactionalDocument="true" 
		suppressRoutingControls="true" 
		extraButtons="${extraButtons}"
		viewOnly="${KualiForm.editingMode['viewOnly']}"
/>	

</kul:documentPage>