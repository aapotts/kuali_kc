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
--%>/
<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<c:set var="awardHierarchyNodeAttributes" value="${DataDictionary.AwardHierarchyNode.attributes}" />
<c:set var="awardAmountTransactionAttributes" value="${DataDictionary.AwardAmountTransaction.attributes}" />

<kul:tab tabTitle="Award Hierarchy" defaultOpen="true" tabErrorKey="" auditCluster="reportsAuditErrors" tabAuditKey="document.reportTermsAuditRules*" useRiceAuditMode="true">
	<div class="tab-container" align="right">
    	<h3>
    		<span class="subhead-left">Hierarchy</span>
    		<span class="subhead-right">
    			<kul:help businessObjectClassName="org.kuali.kra.award.bo.AwardFandaRate" altText="help"/>
			</span>
        </h3>        
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
    
    <table cellpadding="0" cellspacing="0" summary="">	
		<tr>
			<th>
				<div align="left">
					Go To:
					<html:text property="goToAwardNumber" size="12" maxlength="12" />
					<html:image src="${ConfigProperties.kra.externalizable.images.url}tinybutton-go.gif" styleClass="globalbuttons" alt="Go" property="methodToCall.switchAward" />
				</div>
			</th>			
		</tr>
	</table>
	
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
              		<c:out value ="${KualiForm.document.awardHierarchyNodes[order].currentFundEffectiveDate}" />
			</div>
		  </td>
		  <td align="left" valign="middle">
			<div align="center">
              		<c:out value ="${KualiForm.document.awardHierarchyNodes[order].obligationExpirationDate}" />
			</div>
  		  </td>
  		  <td align="left" valign="middle">
			<div align="center">
              		<c:out value ="${KualiForm.document.awardHierarchyNodes[order].finalExpirationDate}" />
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

