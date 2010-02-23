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
<%@ page language="java" %>

<c:choose>
	<c:when test="${ empty(KualiForm.budgetToSummarize) }">
		Cannot locate summary for ${ KualiForm.proposalNumberToSummarize }
	</c:when>
	<c:otherwise>

		<c:set var="budget" value="${ KualiForm.budgetToSummarize }" />
		<table cellpadding=0 cellspacing=0 summary="">
			<tbody>
				<tr>
					<th style="text-align: right;">Proposal Number:</th>
					<td>${budget.budgetDocument.parentDocument.developmentProposal.proposalNumber}</td>
					<th style="text-align: right;">Budget Status:</th>
					<td>${ budget.budgetStatus }</td>
				</tr>
				<tr>
					<th style="text-align: right;">Version Number:</th>
					<td>${ budget.budgetVersionNumber }</td>
					<th style="text-align: right;">Final:</th>
					<td>${ budget.finalVersionFlag }</td>
				</tr>
				<tr>
					<th style="text-align: right;">Budget Start Date:</th>
					<td>${ budget.startDate }</td>
					<th style="text-align: right;">Budget End date:</th>
					<td>${ budget.endDate }</td>
				</tr>
				<tr>
					<th style="text-align: right;">Total Cost:</th>
					<td>${ budget.totalCost }</td>
					<th style="text-align: right;">Total Cost Limit:</th>
					<td>${ budget.totalCostLimit }</td>
				</tr>
				<tr>
					<th style="text-align: right;">Total Direct Cost:</th>
					<td>${ budget.totalDirectCost }</td>
					<th style="text-align: right;">Total F&amp;A Cost:</th>
					<td>${ budget.totalIndirectCost }</td>
				</tr>
				<tr>
					<th style="text-align: right;">Unrecovered F&amp;A:</th>
					<td>${ budget.underrecoveryAmount }</td>
					<th style="text-align: right;">Cost Sharing:</th>
					<td>${ budget.costSharingAmount }</td>
				</tr>
				<tr>
					<th style="text-align: right;">F&amp;A Rate Type:</th>
					<td>${ budget.ohRateClassCode }</td>
					<th style="text-align: right;">Unrecovered F&amp;A Rate Type:</th>
					<td>${ budget.urRateClassCode }</td>
				</tr>
				<tr>
					<th style="text-align: right;">Comments:</th>
					<td colspan="3">${ budget.comments }&nbsp;</td>
				</tr>
			</tbody>
		</table>


		<c:set var="numOfCols" value="${ fn:length(budget.budgetPeriods) + 2 }" />
		<jsp:useBean id="personnelSubTotalsMap" class="java.util.HashMap" scope="request" />
		<jsp:useBean id="nonPersonnelSubTotalsMap" class="java.util.HashMap" scope="request" />
		<c:forEach var="period" items="${budget.budgetPeriods}" varStatus="status">
			<c:set var="periodTotalVar" value="period${status.index}" />
			<c:set target="${nonPersonnelSubTotalsMap}" property="${periodTotalVar}" value="0.00" />
			<c:set target="${personnelSubTotalsMap}" property="${periodTotalVar}" value="0.00" />
		</c:forEach>
		
		<table cellspacing="0" style="width: 100%; padding: 0px;">
			<tbody>
				<tr>
					<th style="text-align: center; height: 25px;">Budget Category</th>
					<c:forEach var="period" items="${budget.budgetPeriods}" varStatus="status">
						<th style="text-align: center;">
							<div>Period ${period.budgetPeriod}<br />
								<span class="fineprint">
									<fmt:formatDate value="${period.startDate}" pattern="MM/dd/yyyy" /> -<br /><fmt:formatDate value="${period.endDate}" pattern="MM/dd/yyyy" />
								</span>
							</div>
						</th>
					</c:forEach>
					<th style="text-align: center;">Total</th>
				</tr>
            <tr>
                  <td colspan="${numOfCols}" class="subhead"><span class="subhead-left"> Personnel&nbsp;</span> </td>
            </tr>
            <tr>
              <td class="tab-subhead" >Salary</td>
              <c:set var="personnelSalaryTotals" value="${budget.budgetSummaryTotals['personnelSalaryTotals']}" />
              <c:set var="personnelSalaryCumulativeTotals" value="0.00" />
              <c:forEach var="period" items="${budget.budgetPeriods}" varStatus="status">
                	<c:set var="periodTotalVar" value="period${status.index}" />
               		<c:set target="${personnelSubTotalsMap}" property="${periodTotalVar}" value="${personnelSubTotalsMap[periodTotalVar] + personnelSalaryTotals[period.budgetPeriod-1]}" />
		           		<td class="tab-subhead" >
		           			<div align="right">
		           				<fmt:formatNumber value="${0.00 + personnelSalaryTotals[period.budgetPeriod-1]}" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;
		           			</div>
		           		</td>
	           		<c:set var="personnelSalaryCumulativeTotals" value = "${personnelSalaryCumulativeTotals + personnelSalaryTotals[period.budgetPeriod-1] }" />
	          </c:forEach>
              <td  align="right" class="tab-subhead">
				<div align="right">
					<strong><fmt:formatNumber value="${personnelSalaryCumulativeTotals}" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;</strong>
				</div>
			 </td>
 			 </tr>
 			 <tr>
              <td class="tab-subhead" >Fringe</td>
              <c:set var="personnelFringeTotals" value="${budget.budgetSummaryTotals['personnelFringeTotals']}" />
              <c:set var="personnelFringeCumulativeTotals" value="0.00" />
              <c:forEach var="period" items="${budget.budgetPeriods}" varStatus="status" >
                	<c:set var="periodTotalVar" value="period${status.index}" />
               		<c:set target="${personnelSubTotalsMap}" property="${periodTotalVar}" value="${personnelSubTotalsMap[periodTotalVar] + personnelFringeTotals[period.budgetPeriod-1]}" />
		           		<td class="tab-subhead" >
		           			<div align="right">
		           				<fmt:formatNumber value="${0.00 + personnelFringeTotals[period.budgetPeriod-1]}" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;
		           			</div>
		           		</td>
		          <c:set var="personnelFringeCumulativeTotals" value = "${personnelFringeCumulativeTotals + personnelFringeTotals[period.budgetPeriod-1] }" />
	          </c:forEach>
	          
              <td  align="right" class="tab-subhead">
					<div align="right">
						<strong><fmt:formatNumber value="${personnelFringeCumulativeTotals}" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;</strong>
					</div>
				</td>
            </tr>
			<tr>
				<td class="tab-subhead" >Calculated Personnel Direct Costs</td>
				<c:set var="personnelCalculatedExpenseSummaryTotals" value="${budget.budgetSummaryTotals['personnelCalculatedExpenseSummaryTotals']}" />
				<c:set var="personnelCalculatedExpenseSummaryCumulativeTotals" value="0.00" />
              	<c:forEach var="period" items="${budget.budgetPeriods}" varStatus="status" >
                	<c:set var="periodTotalVar" value="period${status.index}" />
               		<c:set target="${personnelSubTotalsMap}" property="${periodTotalVar}" value="${personnelSubTotalsMap[periodTotalVar] + personnelCalculatedExpenseSummaryTotals[period.budgetPeriod-1]}" />
		           		<td class="tab-subhead" >
		           			<div align="right">
		           				<fmt:formatNumber value="${0.00 + personnelCalculatedExpenseSummaryTotals[period.budgetPeriod-1]}" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;
		           			</div>
		           		</td>
	           		<c:set var="personnelCalculatedExpenseSummaryCumulativeTotals" value = "${personnelCalculatedExpenseSummaryCumulativeTotals + personnelCalculatedExpenseSummaryTotals[period.budgetPeriod-1] }" />
	          	</c:forEach>
				<td  align="right" class="tab-subhead">
					<div align="right">
						<strong><fmt:formatNumber value="${personnelCalculatedExpenseSummaryCumulativeTotals}" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;</strong>
					</div>
				</td>
		   </tr>
			<tr>
                <td><strong>Personnel Subtotal</strong></td>
                <c:set var="cumPersonnelTotal" value="0.00" />
        	    <c:forEach var="period" items="${budget.budgetPeriods}" varStatus="status" >
        	    	<c:set var="periodTotalVar" value="period${status.index}" />
						<td><div align="right"><i><fmt:formatNumber value="${personnelSubTotalsMap[periodTotalVar]}" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;</i></div></td>
					<c:set var="cumPersonnelTotal" value = "${cumPersonnelTotal + personnelSubTotalsMap[periodTotalVar] }" />
				</c:forEach>    
                <td>
                	<div align="right">  	
                      <i>
                		<fmt:formatNumber value="${cumPersonnelTotal}" type="currency" currencySymbol="" maxFractionDigits="2" />
                      </i>
                	</div>
                </td>
        	 </tr>
            <tr>
                  <td colspan="${numOfCols}" class="subhead"><span class="subhead-left"> Non-personnel&nbsp;</span> </td>
            </tr>
            
            <c:forEach var="objectCodeMapEntry" items="${budget.objectCodeListByBudgetCategoryType}" varStatus="mapIndex">
            <c:set var="categoryType" value="${objectCodeMapEntry.key}" /> 
	            <c:if test="${categoryType.budgetCategoryTypeCode ne 'P'}" >
			            <tr>
			              <td class="tab-subhead" >${categoryType.description}</td>
			              <c:set var="nonPersonnelSummaryTotals" value="${budget.budgetSummaryTotals[categoryType.budgetCategoryTypeCode]}" />
			              <c:set var="nonPersonnelCumulativeTotals" value="0.00" />
			              <c:forEach var="period" items="${budget.budgetPeriods}" varStatus="status" >
                	<c:set var="periodTotalVar" value="period${status.index}" />
               		<c:set target="${nonPersonnelSubTotalsMap}" property="${periodTotalVar}" value="${nonPersonnelSubTotalsMap[periodTotalVar] + nonPersonnelSummaryTotals[period.budgetPeriod-1]}" />
						           	<td class="tab-subhead" >
										<div align="right">
											<fmt:formatNumber value="${0.00 + nonPersonnelSummaryTotals[period.budgetPeriod-1]}" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;
										</div>
									</td>
								<c:set var="nonPersonnelCumulativeTotals" value = "${nonPersonnelCumulativeTotals + nonPersonnelSummaryTotals[period.budgetPeriod-1] }" />
				          </c:forEach>
			              <td  align="right" class="tab-subhead">
							  <div align="right">
									<strong><fmt:formatNumber value="${nonPersonnelCumulativeTotals}" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;</strong>
							  </div>
						  </td>
			            </tr>
			         </c:if>
			    </c:forEach>
            
          <tr>
				<td class="tab-subhead" >Calculated Non-personnel Direct Costs</td>
				<c:set var="nonPersonnelCalculatedExpenseSummaryTotals" value="${budget.budgetSummaryTotals['nonPersonnelCalculatedExpenseSummaryTotals']}" />
				<c:set var="nonPersonnelCalculatedExpenseSummaryCumulativeTotals" value="0.00" />
              	<c:forEach var="period" items="${budget.budgetPeriods}" varStatus="status" >
                	<c:set var="periodTotalVar" value="period${status.index}" />
               		<c:set target="${nonPersonnelSubTotalsMap}" property="${periodTotalVar}" value="${nonPersonnelSubTotalsMap[periodTotalVar] + nonPersonnelCalculatedExpenseSummaryTotals[period.budgetPeriod-1]}" />
		           		<td class="tab-subhead" >
		           			<div align="right">
		           				<fmt:formatNumber value="${0.00 + nonPersonnelCalculatedExpenseSummaryTotals[period.budgetPeriod-1]}" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;
		           			</div>
		           		</td>
	           		<c:set var="nonPersonnelCalculatedExpenseSummaryCumulativeTotals" value = "${nonPersonnelCalculatedExpenseSummaryCumulativeTotals + nonPersonnelCalculatedExpenseSummaryTotals[period.budgetPeriod-1] }" />
	          	</c:forEach>
				<td  align="right" class="tab-subhead">
					<div align="right">
						<strong><fmt:formatNumber value="${nonPersonnelCalculatedExpenseSummaryCumulativeTotals}" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;</strong>
					</div>
				</td>
		   </tr>
			<tr>
                <td width="20%"><strong>Non-Personnel Subtotal</strong></td>
                <c:set var="cumNonPersonnelTotal" value="0.00" />
        	    <c:forEach var="period" items="${budget.budgetPeriods}" varStatus="status" >
        	    	<c:set var="periodTotalVar" value="period${status.index}" />
						<td><div align="right"><i><fmt:formatNumber value="${nonPersonnelSubTotalsMap[periodTotalVar]}" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;</i></div></td>
					<c:set var="cumNonPersonnelTotal" value = "${cumNonPersonnelTotal + nonPersonnelSubTotalsMap[periodTotalVar] }" />
				</c:forEach>    
                <td>
                	<div align="right">  	
                      <i>
                		<fmt:formatNumber value="${cumNonPersonnelTotal}" type="currency" currencySymbol="" maxFractionDigits="2" />
                      </i>
                	</div>
                </td>
        	 </tr>
            <tr>
                  <td colspan="${numOfCols}" class="subhead"><span class="subhead-left"> Totals&nbsp;</span> </td>
            </tr>
		   
          
			</tbody>
		</table>
	</c:otherwise>
</c:choose>
