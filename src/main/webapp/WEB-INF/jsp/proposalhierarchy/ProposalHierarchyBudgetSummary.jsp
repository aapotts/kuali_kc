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
					<td><fmt:formatNumber value="${ budget.totalCost }" type="currency" currencySymbol="" maxFractionDigits="2" /></td>
					<th style="text-align: right;">Total Cost Limit:</th>
					<td><fmt:formatNumber value="${ budget.totalCostLimit }" type="currency" currencySymbol="" maxFractionDigits="2" /></td>
				</tr>
				<tr>
					<th style="text-align: right;">Total Direct Cost:</th>
					<td><fmt:formatNumber value="${ budget.totalDirectCost }" type="currency" currencySymbol="" maxFractionDigits="2" /></td>
					<th style="text-align: right;">Total F&amp;A Cost:</th>
					<td><fmt:formatNumber value="${ budget.totalIndirectCost }" type="currency" currencySymbol="" maxFractionDigits="2" /></td>
				</tr>
				<tr>
					<th style="text-align: right;">Unrecovered F&amp;A:</th>
					<td><fmt:formatNumber value="${ budget.underrecoveryAmount }" type="currency" currencySymbol="" maxFractionDigits="2" /></td>
					<th style="text-align: right;">Cost Sharing:</th>
					<td><fmt:formatNumber value="${ budget.costSharingAmount }" type="currency" currencySymbol="" maxFractionDigits="2" /></td>
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
		<jsp:useBean id="indirectCostMap" class="java.util.HashMap" scope="request" />
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
               		<%-- The following sets and scriptlets is a temporary fix before Jetty fixes the type coercion bug --%>
                	<c:set var="testVar1" value="${personnelSubTotalsMap[periodTotalVar]}" />
                	<c:set var="testVar2" value="${personnelSalaryTotals[period.budgetPeriod-1]}" />
                	<%
                		double var1 = Double.valueOf(pageContext.getAttribute("testVar1").toString()).doubleValue();
                		double var2 = Double.valueOf(pageContext.getAttribute("testVar2").toString()).doubleValue();
                        double var3= var1 + var2;
                        double var4 = 0.00 + var2;
                	%> 
                	<%-- this line is replaced by the following line temporarily. Need to put it back and delete the following line after Jetty fixes the type coercion issue
                	<c:set target="${personnelSubTotalsMap}" property="${periodTotalVar}" value="${personnelSubTotalsMap[periodTotalVar] + personnelSalaryTotals[period.budgetPeriod-1]}" />
                	--%>
                	<c:set target="${personnelSubTotalsMap}" property="${periodTotalVar}" value="<%=var3%>" />
	           		<td class="tab-subhead" >
	           			<div align="right">
	           				<%-- this line is replaced by the following line temporarily. Need to put it back and delete the following line after Jetty fixes the type coercion issue 
	           				<fmt:formatNumber value="${0.00 + personnelSalaryTotals[period.budgetPeriod-1]}" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;
	           				 --%>
	           				<fmt:formatNumber value="<%=var4%>" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;
	           			</div>
	           		</td>
	           		<%-- The following sets and scriptlets is a temporary fix before Jetty fixes the type coercion bug --%>
		           	<%
		           		double var5 = Double.valueOf(pageContext.getAttribute("personnelSalaryCumulativeTotals").toString()).doubleValue();
		           		double var6 = var5 + var2;
		           	%>
		           	<%-- this line is replaced by the following line temporarily. Need to put it back and delete the following line after Jetty fixes the type coercion issue
           			<c:set var="personnelSalaryCumulativeTotals" value = "${personnelSalaryCumulativeTotals + personnelSalaryTotals[period.budgetPeriod-1] }" />
           			--%>
		           	<c:set var="personnelSalaryCumulativeTotals" value = "<%=var6%>" />
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
                	<%-- The following sets and scriptlets is a temporary fix before Jetty fixes the type coercion bug --%>
                	<c:set var="testVar1" value="${personnelSubTotalsMap[periodTotalVar]}" />
                	<c:set var="testVar2" value="${personnelFringeTotals[period.budgetPeriod-1]}" />
                	<%
                		double var1 = Double.valueOf(pageContext.getAttribute("testVar1").toString()).doubleValue();
                		double var2 = Double.valueOf(pageContext.getAttribute("testVar2").toString()).doubleValue();
                		double var3 = var1 + var2;
                		double var4 = 0.00 + var2;
                	%>
                	<%-- this line is replaced by the following line temporarily. Need to put it back and delete the following line after Jetty fixes the type coercion issue
		            <c:set target="${personnelSubTotalsMap}" property="${periodTotalVar}" value="${personnelSubTotalsMap[periodTotalVar] + personnelFringeTotals[period.budgetPeriod-1]}" />
		             --%>
		            <c:set target="${personnelSubTotalsMap}" property="${periodTotalVar}" value="<%=var3%>" />
	           		<td class="tab-subhead" >
	           			<div align="right">
	           			<%-- this line is replaced by the following line temporarily. Need to put it back and delete the following line after Jetty fixes the type coercion issue
	           			<fmt:formatNumber value="${0.00 + personnelFringeTotals[period.budgetPeriod-1]}" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;
	           			 --%>
	           			 <fmt:formatNumber value="<%=var4%>" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;
	           			</div>
	           		</td>
		         	<%-- The following sets and scriptlets is a temporary fix before Jetty fixes the type coercion bug --%>
		           	<%
		           		double var5 = Double.valueOf(pageContext.getAttribute("personnelFringeCumulativeTotals").toString()).doubleValue();
		           		double var6 = var5 + var2;
		           	%>
		           	<%-- this line is replaced by the following line temporarily. Need to put it back and delete the following line after Jetty fixes the type coercion issue
		           	<c:set var="personnelFringeCumulativeTotals" value = "${personnelFringeCumulativeTotals + personnelFringeTotals[period.budgetPeriod-1] }" />
		           	--%>
		           	<c:set var="personnelFringeCumulativeTotals" value = "<%=var6%>" />
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
                	<%-- The following sets and scriptlets is a temporary fix before Jetty fixes the type coercion bug --%>
                	<c:set var="testVar1" value="${personnelSubTotalsMap[periodTotalVar]}" />
                	<c:set var="testVar2" value="${personnelCalculatedExpenseSummaryTotals[period.budgetPeriod-1]}" />
                	<%
                		double var1 = Double.valueOf(pageContext.getAttribute("testVar1").toString()).doubleValue();
                		double var2 = Double.valueOf(pageContext.getAttribute("testVar2").toString()).doubleValue();
                		double var3 = var1 + var2;
                		double var4 = 0.00 + var2;
                	%>
                	<%-- this line is replaced by the following line temporarily. Need to put it back and delete the following line after Jetty fixes the type coercion issue
                	<c:set target="${personnelSubTotalsMap}" property="${periodTotalVar}" value="${personnelSubTotalsMap[periodTotalVar] + personnelCalculatedExpenseSummaryTotals[period.budgetPeriod-1]}" />
                	--%>
               		<c:set target="${personnelSubTotalsMap}" property="${periodTotalVar}" value="<%=var3%>" />
	           		<td class="tab-subhead" >
	           			<div align="right">
	           				<%-- this line is replaced by the following line temporarily. Need to put it back and delete the following line after Jetty fixes the type coercion issue
	           				<fmt:formatNumber value="${0.00 + personnelCalculatedExpenseSummaryTotals[period.budgetPeriod-1]}" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;
	           				--%>
	           				<fmt:formatNumber value="<%=var4%>" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;
	           			</div>
	           		</td>
	           		<%-- The following sets and scriptlets is a temporary fix before Jetty fixes the type coercion bug --%>
		            <%
		             	double var5 = Double.valueOf(pageContext.getAttribute("personnelCalculatedExpenseSummaryCumulativeTotals").toString()).doubleValue();
		             	double var6 = var5 + var2;
		            %>
		            <%-- this line is replaced by the following line temporarily. Need to put it back and delete the following line after Jetty fixes the type coercion issue
	           		<c:set var="personnelCalculatedExpenseSummaryCumulativeTotals" value = "${personnelCalculatedExpenseSummaryCumulativeTotals + personnelCalculatedExpenseSummaryTotals[period.budgetPeriod-1] }" />
	           		--%>
		            <c:set var="personnelCalculatedExpenseSummaryCumulativeTotals" value = "<%=var6%>" />
	          	</c:forEach>
				<td  align="right" class="tab-subhead">
					<div align="right">
						<strong><fmt:formatNumber value="${personnelCalculatedExpenseSummaryCumulativeTotals}" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;</strong>
					</div>
				</td>
		   </tr>
		   	     <c:forEach var="calculatedExpenseTotal" items="${budget.personnelCalculatedExpenseTotals}" >
	           		<c:if test="${not empty calculatedExpenseTotal.key.rateClass.rateClassType && calculatedExpenseTotal.key.rateClass.rateClassType eq 'O'}">
		                <c:forEach var="periodTotal" items="${calculatedExpenseTotal.value}" varStatus="status">
		                	<c:set var="calculatedIndirectExpenseVar" value="calculatedIndirectExpense${status.index}" />
		                	<c:set target="${indirectCostMap}" property="calculatedIndirectExpense${status.index}" value="0.00" />
		                	
		                	<%-- The following sets and scriptlets is a temporary fix before Jetty fixes the type coercion bug --%>
		                	<c:set var="testVar1" value="${indirectCostMap[calculatedIndirectExpenseVar]}" />
		                	<%
				             	double var1 = Double.valueOf(pageContext.getAttribute("testVar1").toString()).doubleValue();
		                		double var2 = Double.valueOf(pageContext.getAttribute("periodTotal").toString()).doubleValue();
				             	double var3 = var1 + var2;
				            %>
				            <%-- this line is replaced by the following line temporarily. Need to put it back and delete the following line after Jetty fixes the type coercion issue
			                <c:set target="${indirectCostMap}" property="calculatedIndirectExpense${status.index}" value="${indirectCostMap[calculatedIndirectExpenseVar] + periodTotal}" />
			                --%>
			                <c:set target="${indirectCostMap}" property="calculatedIndirectExpense${status.index}" value="<%=var3%>" />
		        	    </c:forEach> 
		        	   </c:if>
		        	   </c:forEach>
		   
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
		                	
		                	<%-- The following sets and scriptlets is a temporary fix before Jetty fixes the type coercion bug --%>
		                	<c:set var="testVar1" value="${nonPersonnelSubTotalsMap[periodTotalVar]}" />
		                	<c:set var="testVar2" value="${nonPersonnelSummaryTotals[period.budgetPeriod-1]}" />
		                	<%
				             	double var1 = Double.valueOf(pageContext.getAttribute("testVar1").toString()).doubleValue();
		                		double var2 = Double.valueOf(pageContext.getAttribute("testVar2").toString()).doubleValue();
				             	double var3 = var1 + var2;
				             	double var4 = 0.00 + var2;
				            %>
		                	<%-- this line is replaced by the following line temporarily. Need to put it back and delete the following line after Jetty fixes the type coercion issue
		               		<c:set target="${nonPersonnelSubTotalsMap}" property="${periodTotalVar}" value="${nonPersonnelSubTotalsMap[periodTotalVar] + nonPersonnelSummaryTotals[period.budgetPeriod-1]}" />
		               		--%>
		               		<c:set target="${nonPersonnelSubTotalsMap}" property="${periodTotalVar}" value="<%=var3%>" />
						           	<td class="tab-subhead" >
										<div align="right">
											<%-- this line is replaced by the following line temporarily. Need to put it back and delete the following line after Jetty fixes the type coercion issue
											<fmt:formatNumber value="${0.00 + nonPersonnelSummaryTotals[period.budgetPeriod-1]}" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;
											--%>
											<fmt:formatNumber value="<%=var4%>" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;
										</div>
									</td>
								<%-- The following scriptlets is a temporary fix before Jetty fixes the type coercion bug --%>
								<%
					             	double var5 = Double.valueOf(pageContext.getAttribute("nonPersonnelCumulativeTotals").toString()).doubleValue();
					             	double var6 = var5 + var2;
					            %>
					            <%-- this line is replaced by the following line temporarily. Need to put it back and delete the following line after Jetty fixes the type coercion issue
								<c:set var="nonPersonnelCumulativeTotals" value = "${nonPersonnelCumulativeTotals + nonPersonnelSummaryTotals[period.budgetPeriod-1] }" />
								--%>
								<c:set var="nonPersonnelCumulativeTotals" value = "<%=var6%>" />
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
                	<%-- The following sets and scriptlets is a temporary fix before Jetty fixes the type coercion bug --%>
                	<c:set var="testVar1" value="${nonPersonnelSubTotalsMap[periodTotalVar]}" />
	               	<c:set var="testVar2" value="${nonPersonnelCalculatedExpenseSummaryTotals[period.budgetPeriod-1]}" />
	               	<%
	               		double var1 = Double.valueOf(pageContext.getAttribute("testVar1").toString()).doubleValue();
	               		double var2 = Double.valueOf(pageContext.getAttribute("testVar2").toString()).doubleValue();
	               		double var3 = var1 + var2;
	               		double var4 = 0.00 + var2;
	               	%>
                	<%-- this line is replaced by the following line temporarily. Need to put it back and delete the following line after Jetty fixes the type coercion issue
	               	<c:set target="${nonPersonnelSubTotalsMap}" property="${periodTotalVar}" value="${nonPersonnelSubTotalsMap[periodTotalVar] + nonPersonnelCalculatedExpenseSummaryTotals[period.budgetPeriod-1]}" />
	               	 --%>
               		<c:set target="${nonPersonnelSubTotalsMap}" property="${periodTotalVar}" value="<%=var3%>" />
		           		<td class="tab-subhead" >
		           			<div align="right">
								<%-- this line is replaced by the following line temporarily. Need to put it back and delete the following line after Jetty fixes the type coercion issue
								<fmt:formatNumber value="${0.00 + nonPersonnelCalculatedExpenseSummaryTotals[period.budgetPeriod-1]}" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;
								--%>
		           				<fmt:formatNumber value="<%=var4%>" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;
		           			</div>
		           		</td>
		            <%-- The following sets and scriptlets is a temporary fix before Jetty fixes the type coercion bug --%>
		             <%
		             	double var5 = Double.valueOf(pageContext.getAttribute("nonPersonnelCalculatedExpenseSummaryCumulativeTotals").toString()).doubleValue();
		             	double var6 = var5 + var2;
		             %>
		             <%-- this line is replaced by the following line temporarily. Need to put it back and delete the following line after Jetty fixes the type coercion issue
	           		<c:set var="nonPersonnelCalculatedExpenseSummaryCumulativeTotals" value = "${nonPersonnelCalculatedExpenseSummaryCumulativeTotals + nonPersonnelCalculatedExpenseSummaryTotals[period.budgetPeriod-1] }" />
	           		 --%>
		             <c:set var="nonPersonnelCalculatedExpenseSummaryCumulativeTotals" value = "<%=var6%>" /> 
		             
	          	</c:forEach>
				<td  align="right" class="tab-subhead">
					<div align="right">
						<strong><fmt:formatNumber value="${nonPersonnelCalculatedExpenseSummaryCumulativeTotals}" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;</strong>
					</div>
				</td>
		   </tr>
		   	    <c:forEach var="calculatedExpenseTotal" items="${budget.nonPersonnelCalculatedExpenseTotals}" >
	           		<c:if test="${not empty calculatedExpenseTotal.key.rateClass.rateClassType && calculatedExpenseTotal.key.rateClass.rateClassType eq 'O'}">
		               <c:forEach var="periodTotal" items="${calculatedExpenseTotal.value}" varStatus="status">
		               		<c:set var="calculatedIndirectExpenseVar" value="calculatedIndirectExpense${status.index}" />
		               		<%-- The following sets and scriptlets is a temporary fix before Jetty fixes the type coercion bug --%>
		                	<c:set var="testVar1" value="${indirectCostMap[calculatedIndirectExpenseVar]}" />
			               	<%
			               		double var1 = Double.valueOf(pageContext.getAttribute("testVar1").toString()).doubleValue();
			               		double var2 = Double.valueOf(pageContext.getAttribute("periodTotal").toString()).doubleValue();
			               		double var3 = 0.00 + var1 + var2;
			               	%>
							<%-- this line is replaced by the following line temporarily. Need to put it back and delete the following line after Jetty fixes the type coercion issue
		               		<c:set target="${indirectCostMap}" property="calculatedIndirectExpense${status.index}" value="${0.00 + indirectCostMap[calculatedIndirectExpenseVar] + periodTotal}" />
		               		--%>
		               		<c:set target="${indirectCostMap}" property="calculatedIndirectExpense${status.index}" value="<%=var3%>" />
						</c:forEach>
					</c:if>
				</c:forEach>
		   
			<c:set var="nonPersonnelObjectCodes" value="${budget.objectCodeListByBudgetCategoryType[categoryType]}" />
		   		<c:forEach var="nonPersonnelObjectCode" items="${nonPersonnelObjectCodes}" varStatus="objStatus" >
		   		<c:if test="${nonPersonnelObjectCode.costElement eq KualiForm.proposalHierarchyIndirectObjectCode}">
	        	 	 <c:forEach var="periodTotal" items="${budget.objectCodeTotals[nonPersonnelObjectCode]}" varStatus="objPeriodStatus" >
	               		<c:set var="calculatedIndirectExpenseVar" value="calculatedIndirectExpense${objPeriodStatus.index}" />
	               		<%-- The following sets and scriptlets is a temporary fix before Jetty fixes the type coercion bug --%>
	                	<c:set var="testVar1" value="${indirectCostMap[calculatedIndirectExpenseVar]}" />
		               	<%
		               		double var1 = Double.valueOf(pageContext.getAttribute("testVar1").toString()).doubleValue();
		               		double var2 = Double.valueOf(pageContext.getAttribute("periodTotal").toString()).doubleValue();
		               		double var3 = 0.00 + var1 + var2;
		               	%>
						<%-- this line is replaced by the following line temporarily. Need to put it back and delete the following line after Jetty fixes the type coercion issue
	               		<c:set target="${indirectCostMap}" property="calculatedIndirectExpense${objPeriodStatus.index}" value="${0.00 + indirectCostMap[calculatedIndirectExpenseVar] + periodTotal}" />
	               		--%>
	               		<c:set target="${indirectCostMap}" property="calculatedIndirectExpense${objPeriodStatus.index}" value="<%=var3%>" />
	        	 	 </c:forEach>
	        	 </c:if>
				</c:forEach>
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


        	<tr>
                <td class="infoline"><strong>TOTAL DIRECT COSTS</strong></td>
                <c:set var="cumTotal" value="0.00" />
        	    <c:forEach var="period" items="${budget.budgetPeriods}" varStatus="status" >
        	    	<c:set var="periodTotalVar" value="period${status.index}" />
						<td class="infoline"><div align="right"><strong><fmt:formatNumber value="${personnelSubTotalsMap[periodTotalVar] + nonPersonnelSubTotalsMap[periodTotalVar]}" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;</strong></div></td>
					<c:set var="cumTotal" value = "${cumTotal + personnelSubTotalsMap[periodTotalVar] + nonPersonnelSubTotalsMap[periodTotalVar]}" />
				</c:forEach>    
                <td class="infoline">
                	<div align="right">  	
                      <strong>
                		<fmt:formatNumber value="${cumTotal}" type="currency" currencySymbol="" maxFractionDigits="2" />
                      </strong>
                	</div>
                </td>
        	 </tr>
        	 
        	 <tr>
                <td class="infoline">
                	<strong>TOTAL F&amp;A COSTS</strong>
                </td>
                <c:set var="cumTotal" value="0.00" />
        	    <c:forEach var="period" items="${budget.budgetPeriods}" varStatus="status" >
        	    	<c:set var="calculatedIndirectExpenseVar" value="calculatedIndirectExpense${status.index}" />
						<td class="infoline">
						<div align="right">
							<strong><fmt:formatNumber value="${indirectCostMap[calculatedIndirectExpenseVar]}" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;</strong>
						</div>
						</td>
					<c:set var="cumTotal" value = "${cumTotal + indirectCostMap[calculatedIndirectExpenseVar]}" />
				</c:forEach>    
                <td class="infoline">
                	<div align="right">  	
                      <strong>
                		<fmt:formatNumber value="${cumTotal}" type="currency" currencySymbol="" maxFractionDigits="2" />
                      </strong>
                	</div>
                </td>
        	 </tr>
        	 
        	 <tr>
                <td class="infoline"><strong>TOTAL COSTS</strong></td>
                <c:set var="cumTotal" value="0.00" />
        	    <c:forEach var="period" items="${budget.budgetPeriods}" varStatus="status" >
        	    	<c:set var="periodTotalVar" value="period${status.index}" />
        	    	<c:set var="calculatedIndirectExpenseVar" value="calculatedIndirectExpense${status.index}" />
						<td class="infoline"><div align="right"><strong><fmt:formatNumber value="${personnelSubTotalsMap[periodTotalVar] + nonPersonnelSubTotalsMap[periodTotalVar] + indirectCostMap[calculatedIndirectExpenseVar]}" type="currency" currencySymbol="" maxFractionDigits="2" />&nbsp;</strong></div></td>
					<c:set var="cumTotal" value = "${cumTotal + personnelSubTotalsMap[periodTotalVar] + nonPersonnelSubTotalsMap[periodTotalVar] + indirectCostMap[calculatedIndirectExpenseVar]}" />
				</c:forEach>    
                <td class="infoline">
                	<div align="right">  	
                      <strong>
                		<fmt:formatNumber value="${cumTotal}" type="currency" currencySymbol="" maxFractionDigits="2" />
                      </strong>
                	</div>
                </td>
        	 </tr>

			</tbody>
		</table>
	</c:otherwise>
</c:choose>
