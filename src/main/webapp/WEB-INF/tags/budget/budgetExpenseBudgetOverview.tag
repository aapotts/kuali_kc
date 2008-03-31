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

<c:set var="budgetPeriodAttributes" value="${DataDictionary.BudgetPeriod.attributes}" />

<c:set var="action" value="budgetExpensesAction" />

	<div class="tab-container" align="center">
   	<div class="h2-container">
   		<span class="subhead-left"><h2>Budget Overview (Period ${KualiForm.viewBudgetPeriod})</h2></span>
	   	<span class="subhead-right"><kul:help businessObjectClassName="fillMeIn" altText="help"/></span>
    </div>
    <table cellpadding=0 cellspacing=0 summary="">
    	<c:choose>
    	<c:when test="${!(KualiForm.viewBudgetPeriod == 2 || KualiForm.viewBudgetPeriod == 1)}" >
    		<tr>
	    		<th width="25%"><div align="right"><kul:htmlAttributeLabel attributeEntry="${budgetPeriodAttributes.startDate}" noColon="true" /></div></th>
	    		<td><div align="left"><fmt:formatDate value="${KualiForm.document.summaryPeriodStartDate}" pattern="MM/dd/yyyy" /></div></td>
	    		<th>Period Cost Limit - TBD</th>
	    		<td><div align="left">Period Cost Limit - TBD</div></td>
	    	</tr>
	    	<tr>
	    		<th width="25%"><div align="right"><kul:htmlAttributeLabel attributeEntry="${budgetPeriodAttributes.endDate}" noColon="true" /></div></th>
	    		<td><div align="left"><fmt:formatDate value="${KualiForm.document.summaryPeriodEndDate}" pattern="MM/dd/yyyy" /></div></td>
	    		<th width="25%"><div align="right"><kul:htmlAttributeLabel attributeEntry="${budgetPeriodAttributes.totalCostLimit}" noColon="true" /></div></th>
	    		<td><div align="left">Total Cost Limit - TBD</div></td>
	    	</tr>
	    	<tr>
				<th width="25%"><div align="right"><kul:htmlAttributeLabel attributeEntry="${budgetPeriodAttributes.totalDirectCost}" noColon="true" /></div></th>          		
	    		<td><div align="left"><strong> ${KualiForm.document.totalDirectCost} </strong></div></td>
	    		<th width="25%"><div align="right"><kul:htmlAttributeLabel attributeEntry="${budgetPeriodAttributes.underrecoveryAmount}" noColon="true" /></div></th>          		
	    		<td><div align="left"><strong> ${KualiForm.document.underrecoveryAmount} </strong></div></td>
	    	</tr>
	    	<tr>
	    		<th width="25%"><div align="right"><kul:htmlAttributeLabel attributeEntry="${budgetPeriodAttributes.totalIndirectCost}"noColon="true" /></div></th>
	    		<td><div align="left"><strong> ${KualiForm.document.totalIndirectCost} </strong></div></td>
	    		<th width="25%"><div align="right"><kul:htmlAttributeLabel attributeEntry="${budgetPeriodAttributes.costSharingAmount}" noColon="true" /></div></th>	        		        		
	    		<td><div align="left"><strong> ${KualiForm.document.costSharingAmount} </strong></div></td>
	    	</tr>
	    	<tr>
	    		<th width="25%"><div align="right"><kul:htmlAttributeLabel attributeEntry="${budgetPeriodAttributes.totalCost}" noColon="true" /></div></th>
	    		<td><div align="left"><strong> ${KualiForm.document.totalCost} </strong></div></td>
	    	</tr>
    	</c:when>
    	<c:otherwise>
	    	<tr>
	    		<th width="25%"><div align="right"><kul:htmlAttributeLabel attributeEntry="${budgetPeriodAttributes.startDate}" noColon="true" /></div></th>
	    		<td><div align="left"><kul:htmlControlAttribute property="document.budgetPeriod[${KualiForm.viewBudgetPeriod - 1}].startDate" attributeEntry="${budgetPeriodAttributes.startDate}" datePicker="true"/></div></td>
	    		<th>Period Cost Limit - TBD</th>
	    		<td><div align="left">Period Cost Limit - TBD</div></td>
	    	</tr>
	    	<tr>
	    		<th width="25%"><div align="right"><kul:htmlAttributeLabel attributeEntry="${budgetPeriodAttributes.endDate}" noColon="true" /></div></th>
	    		<td><div align="left"><kul:htmlControlAttribute property="document.budgetPeriod[${KualiForm.viewBudgetPeriod - 1}].endDate" attributeEntry="${budgetPeriodAttributes.endDate}" datePicker="true"/></div></td>
	    		<th width="25%"><div align="right"><kul:htmlAttributeLabel attributeEntry="${budgetPeriodAttributes.totalCostLimit}" noColon="true" /></div></th>
	    		<td><div align="left"><kul:htmlControlAttribute property="document.budgetPeriod[${KualiForm.viewBudgetPeriod - 1}].totalCostLimit" attributeEntry="${budgetPeriodAttributes.totalCostLimit}" datePicker="true" readOnly="true"/></div></td>
	    	</tr>
	    	<tr>
				<th width="25%"><div align="right"><kul:htmlAttributeLabel attributeEntry="${budgetPeriodAttributes.totalDirectCost}" noColon="true" /></div></th>          		
	    		<td><div align="left"><kul:htmlControlAttribute property="document.budgetPeriod[${KualiForm.viewBudgetPeriod - 1}].totalDirectCost" attributeEntry="${budgetPeriodAttributes.totalDirectCost}" styleClass="amount" readOnly="true"/></div></td>
	    		<th width="25%"><div align="right"><kul:htmlAttributeLabel attributeEntry="${budgetPeriodAttributes.underrecoveryAmount}" noColon="true" /></div></th>          		
	    		<td><div align="left"><kul:htmlControlAttribute property="document.budgetPeriod[${KualiForm.viewBudgetPeriod - 1}].underrecoveryAmount" attributeEntry="${budgetPeriodAttributes.underrecoveryAmount}" styleClass="amount" readOnly="true"/></div></td>
	    	</tr>
	    	<tr>
	    		<th width="25%"><div align="right"><kul:htmlAttributeLabel attributeEntry="${budgetPeriodAttributes.totalIndirectCost}"noColon="true" /></div></th>
	    		<td><div align="left"><kul:htmlControlAttribute property="document.budgetPeriod[${KualiForm.viewBudgetPeriod - 1}].totalIndirectCost" attributeEntry="${budgetPeriodAttributes.totalIndirectCost}" styleClass="amount" readOnly="true"/></div></td>
	    		<th width="25%"><div align="right"><kul:htmlAttributeLabel attributeEntry="${budgetPeriodAttributes.costSharingAmount}" noColon="true" /></div></th>	        		        		
	    		<td><div align="left"><kul:htmlControlAttribute property="document.budgetPeriod[${KualiForm.viewBudgetPeriod - 1}].costSharingAmount" attributeEntry="${budgetPeriodAttributes.costSharingAmount}" styleClass="amount" readOnly="true"/></div></td>
	    	</tr>
	    	<tr>
	    		<th width="25%"><div align="right"><kul:htmlAttributeLabel attributeEntry="${budgetPeriodAttributes.totalCost}" noColon="true" /></div></th>
	    		<td><div align="left"><kul:htmlControlAttribute property="document.budgetPeriod[${KualiForm.viewBudgetPeriod - 1}].totalCost" attributeEntry="${budgetPeriodAttributes.totalCost}" readOnly="true"/></div></td>
	    	</tr>
    	</c:otherwise>

    	</c:choose>

    </table>