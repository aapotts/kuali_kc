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

<c:set var="proposalDevelopmentAttributes" value="${DataDictionary.DevelopmentProposal.attributes}" />
<c:set var="budgetAttributes" value="${DataDictionary.Budget.attributes}" />
<c:set var="awardBudgetAttributes" value="${DataDictionary.AwardBudgetExt.attributes}" />
<c:set var="textAreaFieldName" value="document.budget.comments" />
<c:set var="action" value="budgetAction" />
<c:set var="KRAConst" value="${org.kuali.kra.infrastructure.Constants}"/>

<input type="hidden" id="updateFinalVersion" name="updateFinalVersion" value='<bean:write name="KualiForm" property="updateFinalVersion"/>' />

<c:forEach var="budgetDocumentVersions" items="${KualiForm.document.parentDocument.budgetDocumentVersions}" varStatus="status">
	<c:if test="${status.index + 1 != KualiForm.document.budget.budgetVersionNumber}">
		<input type="hidden" id="budgetStatus${status.index}" name="KualiForm" property="document.budget.awardBudgetStatusCode" value='<bean:write name="KualiForm" property="document.budget.awardBudgetExt.awardBudgetStatusCode"/>' />
	</c:if> 
</c:forEach>
<kul:tabTop tabTitle="Budget Overview" defaultOpen="true" tabErrorKey="budgetParameters*,document.budget.totalCostLimit" auditCluster="budgetParametersOverviewWarnings" tabAuditKey="document.budget.totalCostLimit">
	<div class="tab-container" align="center">
    	<h3>Budget Overview</h3>
        <table cellpadding=0 cellspacing=0 summary="">
        	<tr>
                <th><div align="right"><kul:htmlAttributeLabel attributeEntry="${budgetAttributes.startDate}" /></div></th>
                <td align="left" valign="middle">
                	<fmt:formatDate value="${KualiForm.document.budget.startDate}" pattern="MM/dd/yyyy" />
                </td>
           		<th><div align="right"><kul:htmlAttributeLabel attributeEntry="${budgetAttributes.totalCostLimit}" /></div></th>
           		<td>
           			<bean:write name="KualiForm" property="document.budget.totalCostLimit"/> 
           		</td>
            </tr>
        	<tr>
				<th><div align="right"><kul:htmlAttributeLabel attributeEntry="${budgetAttributes.endDate}" /></div></th>
                <td>
                	<fmt:formatDate value="${KualiForm.document.budget.endDate}" pattern="MM/dd/yyyy" />
                </td>
		        <input type="hidden" name="prevOnOffCampusFlag" value="${KualiForm.document.budget.onOffCampusFlag}">
				<th><div align="right"><kul:htmlAttributeLabel attributeEntry="${budgetAttributes.onOffCampusFlag}" /></div></th>
                <td colspan="3">
                	<kul:htmlControlAttribute property="document.budget.onOffCampusFlag" attributeEntry="${budgetAttributes.onOffCampusFlag}" readOnlyAlternateDisplay="${KualiForm.document.budget.onOffCampusFlagDescription}"/>
                </td>           		
        	</tr>
			<tr>
				<th><div align="right"><kul:htmlAttributeLabel attributeEntry="${awardBudgetAttributes.awardBudgetStatusCode}" /></div></th>
			    <td>
			         <kul:htmlControlAttribute property="document.budget.awardBudgetStatusCode" attributeEntry="${awardBudgetAttributes.awardBudgetStatusCode}" readOnly="${readOnly}" disabled="${viewOnly}"/>
                </td>
           		<th><div align="right"><kul:htmlAttributeLabel attributeEntry="${budgetAttributes.urRateClassCode}" /></div></th>
                <td>
                	<kra:kraControlAttribute property="document.budget.urRateClassCode" readOnly="${readOnly}" attributeEntry="${budgetAttributes.urRateClassCode}"  styleClass="fixed-size-200-select"/>
                	<input type="hidden" name="urRateClassCodePrevValue" value="${KualiForm.document.budget.urRateClassCode}">
                </td>
        	</tr>
        	<tr>
				<th><div align="right"><kul:htmlAttributeLabel attributeEntry="${budgetAttributes.comments}" /></div></th>
                <td>
                	<kul:htmlControlAttribute property="document.budget.comments" attributeEntry="${budgetAttributes.comments}"/>
                    <kul:expandedTextArea textAreaFieldName="${textAreaFieldName}" action="${action}" textAreaLabel="${budgetAttributes.comments.label}" />
                </td>
           		<th><div align="right"><kul:htmlAttributeLabel attributeEntry="${budgetAttributes.ohRateClassCode}" /></div></th>
           		<td>
           			<kra:kraControlAttribute property="document.budget.ohRateClassCode" readOnly="${readOnly}" attributeEntry="${budgetAttributes.ohRateClassCode}"  styleClass="fixed-size-200-select"/>
           			<input type="hidden" name="ohRateClassCodePrevValue" value="${KualiForm.document.budget.ohRateClassCode}">
           		</td>
     		</tr>
        </table>
    </div>
</kul:tabTop>
<%-- initialize status of final checkbox and budget status. --%>
<img src="${ConfigProperties.kr.externalizable.images.url}pixel_clear.gif" onLoad="setupBudgetStatusSummary(document);" />
