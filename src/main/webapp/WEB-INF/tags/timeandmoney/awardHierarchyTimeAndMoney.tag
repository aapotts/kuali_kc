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

<c:set var="awardHierarchyNodeAttributes" value="${DataDictionary.AwardHierarchyNode.attributes}" />
<c:set var="awardAmountTransactionAttributes" value="${DataDictionary.AwardAmountTransaction.attributes}" />

<kul:tab tabTitle="Award Hierarchy" defaultOpen="true" tabErrorKey="" auditCluster="reportsAuditErrors" tabAuditKey="document.reportTermsAuditRules*" useRiceAuditMode="true">
	<div class="tab-container" align="right">    	        
    <table cellpadding="0" cellspacing="0" summary="">
    	<tr>
    		<kul:htmlAttributeHeaderCell attributeEntry="${awardAmountTransactionAttributes.transactionTypeCode}" scope="col" /></div></th>
    		<kul:htmlAttributeHeaderCell attributeEntry="${awardAmountTransactionAttributes.noticeDate}" scope="col" /></div></th>
    		<kul:htmlAttributeHeaderCell attributeEntry="${awardAmountTransactionAttributes.comments}" scope="col" /></div></th>
    	</tr>
    	<tr>
    		<c:choose>
    			<c:when test="${fn:length(KualiForm.document.awardAmountTransactions) > 0}" >
    				<td align="left" valign="middle">					
		    		<div align="center">            
		            	<kul:htmlControlAttribute property="document.awardAmountTransactions[0].transactionTypeCode" attributeEntry="${awardAmountTransactionAttributes.transactionTypeCode}" />
					</div>
					</td>
					<td align="left" valign="middle">					
		    		<div align="center">            
		            	<kul:htmlControlAttribute property="document.awardAmountTransactions[0].noticeDate" attributeEntry="${awardAmountTransactionAttributes.noticeDate}" />
					</div>
					</td>
					<td align="left" valign="middle">					
		    		<div align="center">            
		            	<kul:htmlControlAttribute property="document.awardAmountTransactions[0].comments" attributeEntry="${awardAmountTransactionAttributes.comments}" />
					</div>
					</td>
    			</c:when>
    			<c:otherwise>
    				<td align="left" valign="middle">					
		    		<div align="center">            
		            	<kul:htmlControlAttribute property="document.newAwardAmountTransaction.transactionTypeCode" attributeEntry="${awardAmountTransactionAttributes.transactionTypeCode}" />
					</div>
					</td>
					<td align="left" valign="middle">					
		    		<div align="center">            
		            	<kul:htmlControlAttribute property="document.newAwardAmountTransaction.noticeDate" attributeEntry="${awardAmountTransactionAttributes.noticeDate}" />
					</div>
					</td>
					<td align="left" valign="middle">					
		    		<div align="center">            
		            	<kul:htmlControlAttribute property="document.newAwardAmountTransaction.comments" attributeEntry="${awardAmountTransactionAttributes.comments}" />
					</div>
					</td>    			
    			</c:otherwise>
    		</c:choose>
    		
    	</tr>
    </table>
    <div>
    <table cellpadding="0" cellspacing="0" summary="">	
		<tr>
			<th>
				<div align="left">
					Go To:
					<html:text property="goToAwardNumber" size="12" maxlength="12" />
					<html:image src="${ConfigProperties.kra.externalizable.images.url}tinybutton-go.gif" styleClass="globalbuttons" alt="Go" property="methodToCall.switchAward" />
				</div>
			</th>
			<td style="text-align: center; background-color: rgb(195, 195, 195); font-weight: bold; width: 170px;">
				<input class="nobord" type="radio" checked="checked" value="0" name="when01"/>
					current
				<input class="nobord" type="radio" value="1" name="when01"/>
					pending
			</td>
			<td style="text-align: center; background-color: rgb(195, 195, 195); font-weight: bold; width: 185px;">
				<select id="controlForAwardHierarchyView" name="controlForAwardHierarchyView" >
					<option value="0">Dates Only</option>
					<option value="1">Distributed/Distributable</option>
					<option value="2">Totals</option>
				</select>
			</td>
			<td style="text-align: center; background-color: rgb(195, 195, 195); width: 60px;">
				<input class="tinybutton" type="image" alt="refresh" src="${ConfigProperties.kra.externalizable.images.url}tinybutton-refresh.gif" name="x"/>
			</td>			
		</tr>
	</table>
	</div>    
    <div class="divAHT"><div style="padding-right: 47px;">
	<table style="border: medium none ; width: 100%; border-collapse: collapse;">
	<tbody><tr>
		<td style="border: medium none ; border-collapse: collapse; background-color: rgb(234, 233, 234);">
		<span style="display: inline;" id="treecontrol">
		20 Nodes: 
		<a href="#" title="Collapse the entire tree below"><img src="../BFN/images/minus.gif"/> Collapse All</a>
		<a href="#" title="Expand the entire tree below"><img src="../BFN/images/plus.gif"/> Expand All</a>
		</span>
		</td>
		<td style="border: 1px solid rgb(153, 153, 153); padding: 1px; text-align: center; width: 108px; border-collapse: collapse; font-weight: bold; background-color: rgb(195, 195, 195);">
		Oblg. Start
		</td>
		<td style="border: 1px solid rgb(153, 153, 153); text-align: center; width: 108px; border-collapse: collapse; font-weight: bold; background-color: rgb(195, 195, 195);">
		Oblg. End
		</td>
		<td style="border: 1px solid rgb(153, 153, 153); text-align: center; width: 108px; border-collapse: collapse; font-weight: bold; background-color: rgb(195, 195, 195);">
		Project End
		</td>
		<td style="border: 1px solid rgb(153, 153, 153); text-align: right; width: 100px; border-collapse: collapse; font-weight: bold; background-color: rgb(195, 195, 195);">
		Obligated
		</td>
		<td style="border: 1px solid rgb(153, 153, 153); text-align: right; width: 101px; border-collapse: collapse; font-weight: bold; background-color: rgb(195, 195, 195);">
		Anticipated
		</td>
	</tr>
	</tbody></table></div>
	</div>
    <div align="left">    
                                            <%-- <div id="treecontrol" style="display:inline;">
                                                &nbsp;&nbsp;&nbsp;&nbsp;<a title="Collapse the entire tree below" href="#"><img src="static/images/jquery/minus.gif" /> Collapse All</a>
                                                &nbsp;&nbsp;&nbsp;&nbsp;<a title="Expand the entire tree below" href="#"><img src="static/images/jquery/plus.gif" /> Expand All</a>
                                            </div> --%>
                                            
	${TimeAndMoneyForm.awardHierarchy}
     <div style = "background:#e4e4e4" >     
  <ul id="awardhierarchy" class="filetree stripeli treeview"  >
        <%-- <li><span class="folder">00000</span>
        </li> --%>
    </ul>
   </div> 
    </div>
    
	<table cellpadding="0" cellspacing="0" summary="">
    	<%-- Header --%>
    	
  		<tr>
       		<kul:htmlAttributeHeaderCell literalLabel="&nbsp;" scope="col" />
       		<kul:htmlAttributeHeaderCell attributeEntry="${awardHierarchyNodeAttributes.awardNumber}" scope="col" /></div></th>
       		<kul:htmlAttributeHeaderCell attributeEntry="${awardHierarchyNodeAttributes.parentAwardNumber}" scope="col" /></div></th>
       		<kul:htmlAttributeHeaderCell attributeEntry="${awardHierarchyNodeAttributes.currentFundEffectiveDate}" scope="col" /></div></th>
       		<kul:htmlAttributeHeaderCell attributeEntry="${awardHierarchyNodeAttributes.obligationExpirationDate}" scope="col" /></div></th>
       		<kul:htmlAttributeHeaderCell attributeEntry="${awardHierarchyNodeAttributes.finalExpirationDate}" scope="col" /></div></th>
       		<kul:htmlAttributeHeaderCell attributeEntry="${awardHierarchyNodeAttributes.amountObligatedToDate}" scope="col" /></div></th>
       		<kul:htmlAttributeHeaderCell attributeEntry="${awardHierarchyNodeAttributes.obliDistributableAmount}" scope="col" /></div></th>
       		<kul:htmlAttributeHeaderCell attributeEntry="${awardHierarchyNodeAttributes.anticipatedTotalAmount}" scope="col" /></div></th>
       		<kul:htmlAttributeHeaderCell attributeEntry="${awardHierarchyNodeAttributes.antDistributableAmount}" scope="col" /></div></th>       		
       	</tr>
       	
		<c:forEach var="order" items="${KualiForm.order}" varStatus="status">
          <tr>
			<th class="infoline">
				<c:out value="${status.index+1}" />
			</th>            
          <td align="left" valign="middle">
			<div align="center">
              	<c:out value ="${KualiForm.document.awardHierarchyNodes[order].awardNumber}" /> : <c:out value ="${KualiForm.document.awardHierarchyNodes[order].leadUnitName}" /> : <c:out value ="${KualiForm.document.awardHierarchyNodes[order].principalInvestigatorName}" />
			</div>
		  </td>
		  <td align="left" valign="middle">
			<div align="center">
              	<c:out value ="${KualiForm.document.awardHierarchyNodes[order].parentAwardNumber}" />
			</div>
		  </td>
          <td align="left" valign="middle">
			<div align="center">              	
              	<kul:htmlControlAttribute property="obligationStartDates[${status.index}]" attributeEntry="${awardHierarchyNodeAttributes.currentFundEffectiveDate}" datePicker="true" />
			</div>
		  </td>
		  <td align="left" valign="middle">
			<div align="center">            	
            	<kul:htmlControlAttribute property="obligationExpirationDates[${status.index}]" attributeEntry="${awardHierarchyNodeAttributes.obligationExpirationDate}" datePicker="true" />
			</div>
  		  </td>
  		  <td align="left" valign="middle">
			<div align="center">              	
              	<kul:htmlControlAttribute property="finalExpirationDates[${status.index}]" attributeEntry="${awardHierarchyNodeAttributes.obligationExpirationDate}" datePicker="true" />              	
			</div>
  		  </td>
  		  <td align="left" valign="middle">
			<div align="center">
              		<c:out value ="${KualiForm.document.awardHierarchyNodes[order].amountObligatedToDate}" />
			</div>
  		  </td>
  		  <td align="left" valign="middle">
			<div align="center">
              		<c:out value ="${KualiForm.document.awardHierarchyNodes[order].obliDistributableAmount}" />
			</div>
  		  </td>
  		  <td align="left" valign="middle">
			<div align="center">
              		<c:out value ="${KualiForm.document.awardHierarchyNodes[order].anticipatedTotalAmount}" />              		
			</div>
  		  </td>
  		  <td align="left" valign="middle">
			<div align="center">
              		<c:out value ="${KualiForm.document.awardHierarchyNodes[order].antDistributableAmount}" />
			</div>
  		  </td>		  
           </tr>
      	</c:forEach>    
      </table>	
    
    </div>
</kul:tab>
