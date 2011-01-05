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
<c:set var="awardAttributes" value="${DataDictionary.Award.attributes}" />
<c:set var="awardAmountInfoAttributes" value="${DataDictionary.AwardAmountInfo.attributes}" />

<kul:tab tabTitle="Summary (${KualiForm.awardForSummaryPanelDisplay.awardNumber})" defaultOpen="false" tabErrorKey="">
	<div class="tab-container" align="center">
    	<h3>
    		<span class="subhead-left">Summary</span>
    		<span class="subhead-right"><kul:help businessObjectClassName="org.kuali.kra.timeandmoney.transactions.PendingTransaction" altText="help"/></span>
        </h3>
        
        <table cellpAdding="0" cellspacing="0" summary="">
		  	<tr>
		    	<th><div align="right">*Award ID:</div></th>
		    	<td>${KualiForm.awardForSummaryPanelDisplay.awardNumber}&nbsp;</td>
		    	<th>
		    		<div align="right"><kul:htmlAttributeLabel attributeEntry="${awardAttributes.awardTypeCode}" /></div>
		      	</th>
		    	<td>
		    		<c:out value="${KualiForm.awardForSummaryPanelDisplay.awardType.description}" />
		    		<!--<kul:htmlControlAttribute property="awardForSummaryPanelDisplay.awardTypeCode" attributeEntry="${awardAttributes.awardTypeCode}" readOnlyAlternateDisplay="${KualiForm.awardForSummaryPanelDisplay.awardType.description}" readOnly="true" />-->
		      	</td>		    	
		  	</tr>
		  	<tr>
		    	<th>
		    		<div align="right"><kul:htmlAttributeLabel attributeEntry="${awardAttributes.sponsorCode}" /></div>
		      	</th>
		    	<td>
		    		<c:out value="${KualiForm.awardForSummaryPanelDisplay.sponsor.sponsorName}" />
		    		<!--<kul:htmlControlAttribute property="awardForSummaryPanelDisplay.sponsorCode" attributeEntry="${awardAttributes.sponsorCode}" readOnlyAlternateDisplay="${KualiForm.awardForSummaryPanelDisplay.sponsor.sponsorName}"readOnly="true" />-->
		    	</td>
		    	<th>
		    		<div align="right"><kul:htmlAttributeLabel attributeEntry="${awardAttributes.activityTypeCode}" /></div>
		    	</th>
		    	<td>
		    		<c:out value="${KualiForm.awardForSummaryPanelDisplay.activityType.description}" />
		    		<!--<kul:htmlControlAttribute property="awardForSummaryPanelDisplay.activityTypeCode" attributeEntry="${awardAttributes.activityTypeCode}" readOnlyAlternateDisplay="${KualiForm.awardForSummaryPanelDisplay.activityType.description}"readOnly="true" />-->
				</td>
		  	</tr>
		  	<tr>
		    	<th>
		    		<div align="right"><kul:htmlAttributeLabel attributeEntry="${awardAttributes.accountNumber}" /></div>
		      	</th>
		    	<td align="left" valign="middle">
		    		<c:out value="${KualiForm.awardForSummaryPanelDisplay.accountNumber}" />
		    		<!--<kul:htmlControlAttribute property="awardForSummaryPanelDisplay.accountNumber" attributeEntry="${awardAttributes.accountNumber}" readOnly="true" />-->
		    	</td>
		    	<th>
		    		<div align="right"><kul:htmlAttributeLabel attributeEntry="${awardAttributes.accountTypeCode}" /></div>
				</th>
		    	<td>
		    		<c:out value="${KualiForm.awardForSummaryPanelDisplay.accountTypeDescription}" />
		    		<!--<kul:htmlControlAttribute property="awardForSummaryPanelDisplay.accountTypeCode" attributeEntry="${awardAttributes.accountTypeCode}" readOnlyAlternateDisplay="${KualiForm.awardForSummaryPanelDisplay.accountTypeDescription}" readOnly="true" />-->
				</td>
		  	</tr>
		  	<tr>
		    	<th>
		    		<div align="right">
		        		<kul:htmlAttributeLabel attributeEntry="${awardAttributes.statusCode}" />
		      		</div>
		      	</th>
		    	<td>
		    		<c:out value="${KualiForm.awardForSummaryPanelDisplay.awardStatus.description}" />
		    		<!--<kul:htmlControlAttribute property="awardForSummaryPanelDisplay.statusCode" attributeEntry="${awardAttributes.statusCode}" readOnlyAlternateDisplay="${KualiForm.awardForSummaryPanelDisplay.awardStatus.description}" readOnly="true" />-->
		      	</td>
		    	<th>&nbsp;</th>
		    	<td align="left" valign="middle">&nbsp;</td>
		  	</tr>
		  	<tr>
		    	<th>
		    		<div align="right">
		        		<kul:htmlAttributeLabel attributeEntry="${awardAttributes.title}" />
		      		</div>
		      	</th>
		    	<td colspan="3">
		        	<table style="border:none; width:100%;">
		        		<tr>
		            		<td style="border:none; width:100%;">
		            			<c:out value="${KualiForm.awardForSummaryPanelDisplay.title}" />
		            			<!--<kul:htmlControlAttribute property="awardForSummaryPanelDisplay.title" attributeEntry="${awardAttributes.title}"  readOnly="true"/>-->		                    	
		        			</td>
		            	</tr>
		        	</table>
		    	</td>
		  	</tr>
		</table>
        
        <h3>
    		<span class="subhead-left">Dates &amp; Amounts</span>
    		<span class="subhead-right"><kul:help businessObjectClassName="org.kuali.kra.timeandmoney.transactions.PendingTransaction" altText="help"/></span>
        </h3>
        <table cellpadding="0" cellspacing="0" summary="">
	    	<tr>
	        	<th>
	            	<div align="right"><kul:htmlAttributeLabel attributeEntry="${awardAttributes.sponsorCode}" /></div>
	        	</th>
	        	<td colspan="3">
	        		<c:out value="${KualiForm.awardForSummaryPanelDisplay.sponsorCode}" />
	        		<!--<kul:htmlControlAttribute property="awardForSummaryPanelDisplay.sponsorCode" attributeEntry="${awardAttributes.sponsorCode}" readOnly="true"/>-->
	        		<c:out value=" - ${KualiForm.awardForSummaryPanelDisplay.sponsor.sponsorName}" />
	        	</td>	        	       		
	        </tr>	        	
				<th>
					<div align="right"><kul:htmlAttributeLabel attributeEntry="${awardAttributes.beginDate}" /></div>
        		</th>
        		<td align="left" valign="middle">
        			<c:out value="${KualiForm.awardForSummaryPanelDisplay.beginDate}" />
        			<!--<kul:htmlControlAttribute property="awardForSummaryPanelDisplay.beginDate" attributeEntry="${awardAttributes.beginDate}" readOnly="true" />-->
				</td>
				<th>
					<div align="right"><kul:htmlAttributeLabel attributeEntry="${awardAmountInfoAttributes.currentFundEffectiveDate}" /></div>
        		</th>
        		<td align="left" valign="middle">
        			<c:out value="${KualiForm.awardForSummaryPanelDisplay.awardAmountInfos[KualiForm.awardForSummaryPanelDisplay.indexOfAwardAmountInfoForDisplay].currentFundEffectiveDate}" />
        			<!--<kul:htmlControlAttribute property="awardForSummaryPanelDisplay.awardAmountInfos[${KualiForm.awardForSummaryPanelDisplay.indexOfAwardAmountInfoForDisplay}].currentFundEffectiveDate" attributeEntry="${awardAmountInfoAttributes.currentFundEffectiveDate}" readOnly="true" />-->
        		</td>
	        <tr>	        	
				<th>
					<div align="right"><kul:htmlAttributeLabel attributeEntry="${awardAmountInfoAttributes.finalExpirationDate}" /></div>
        		</th>
        		<td align="left" valign="middle">
        			<c:out value="${KualiForm.awardForSummaryPanelDisplay.awardAmountInfos[KualiForm.awardForSummaryPanelDisplay.indexOfAwardAmountInfoForDisplay].finalExpirationDate}" />
        			<!--<kul:htmlControlAttribute property="awardForSummaryPanelDisplay.awardAmountInfos[${KualiForm.awardForSummaryPanelDisplay.indexOfAwardAmountInfoForDisplay}].finalExpirationDate" attributeEntry="${awardAmountInfoAttributes.finalExpirationDate}" readOnly="true" />-->
        		</td>
        		<th>
					<div align="right"><kul:htmlAttributeLabel attributeEntry="${awardAmountInfoAttributes.obligationExpirationDate}" /></div>
        		</th>
        		<td align="left" valign="middle">
        			<c:out value="${KualiForm.awardForSummaryPanelDisplay.awardAmountInfos[KualiForm.awardForSummaryPanelDisplay.indexOfAwardAmountInfoForDisplay].obligationExpirationDate}" />
        			<!--<kul:htmlControlAttribute property="awardForSummaryPanelDisplay.awardAmountInfos[${KualiForm.awardForSummaryPanelDisplay.indexOfAwardAmountInfoForDisplay}].obligationExpirationDate" attributeEntry="${awardAmountInfoAttributes.obligationExpirationDate}" readOnly="true" />-->
        		</td>	        	
	        </tr>
	        <tr>	        	
	        	<th>
					<div align="right"><kul:htmlAttributeLabel attributeEntry="${awardAmountInfoAttributes.anticipatedTotalAmount}" /></div>
        		</th>
        		<td align="left" valign="middle">
        			<c:out value="${KualiForm.awardForSummaryPanelDisplay.awardAmountInfos[KualiForm.awardForSummaryPanelDisplay.indexOfAwardAmountInfoForDisplay].anticipatedTotalAmount}" />
        			<!--<kul:htmlControlAttribute property="awardForSummaryPanelDisplay.awardAmountInfos[${KualiForm.awardForSummaryPanelDisplay.indexOfAwardAmountInfoForDisplay}].anticipatedTotalAmount" attributeEntry="${awardAmountInfoAttributes.anticipatedTotalAmount}" readOnly="true" />-->
        		</td>
        		<th>
					<div align="right"><kul:htmlAttributeLabel attributeEntry="${awardAmountInfoAttributes.amountObligatedToDate}" /></div>
        		</th>
        		<td align="left" valign="middle">
        			<c:out value="${KualiForm.awardForSummaryPanelDisplay.awardAmountInfos[KualiForm.awardForSummaryPanelDisplay.indexOfAwardAmountInfoForDisplay].amountObligatedToDate}" />
        			<!--<kul:htmlControlAttribute property="awardForSummaryPanelDisplay.awardAmountInfos[${KualiForm.awardForSummaryPanelDisplay.indexOfAwardAmountInfoForDisplay}].amountObligatedToDate" attributeEntry="${awardAmountInfoAttributes.amountObligatedToDate}" readOnly="true" />-->
        		</td>
	        </tr>
        </table>	
        
        <h3>
    		<span class="subhead-left">Award Details Recorded</span>
    		<span class="subhead-right"><kul:help businessObjectClassName="org.kuali.kra.timeandmoney.transactions.PendingTransaction" altText="help"/></span>
        </h3>
        <table cellpadding="0" cellspacing="0" summary="">
	    	<tr>
	        	<th>
	            	<div align="right">Approved Subaward?</div>
	        	</th>
	        	<td>
	        		<div align="left">
	        		<c:choose>
	        			<c:when test="${fn:length(KualiForm.awardForSummaryPanelDisplay.awardApprovedSubawards)==0}">
	        				N
	        			</c:when>
	        			<c:otherwise>
	        				Y
	        			</c:otherwise>
	        		</c:choose>
	        		</div>
	        	</td>
	        	<th>
					<div align="right">Payment Schedule?</div>
        		</th>
        		<td>
        			<div align="left">
        			<c:choose>
	        			<c:when test="${fn:length(KualiForm.awardForSummaryPanelDisplay.paymentScheduleItems)==0}">
	        				N
	        			</c:when>
	        			<c:otherwise>
	        				Y
	        			</c:otherwise>
	        		</c:choose>
	        		</div>
	        	</td>	        	       		
	        </tr>	        	
				<th>
	            	<div align="right">Approved Equipment?</div>
	        	</th>
	        	<td>
	        		<div align="left">
					<c:choose>
	        			<c:when test="${fn:length(KualiForm.awardForSummaryPanelDisplay.approvedEquipmentItems)==0}">
	        				N
	        			</c:when>
	        			<c:otherwise>
	        				Y
	        			</c:otherwise>
	        		</c:choose>
					</div>
	        	</td>
	        	<th>
					<div align="right">Sponsor Funding Transferred?</div>
        		</th>
        		<td>
       				<div align="left">
       				<c:choose>
	        			<c:when test="${fn:length(KualiForm.awardForSummaryPanelDisplay.awardTransferringSponsors)==0}">
	        				N
	        			</c:when>
	        			<c:otherwise>
	        				Y
	        			</c:otherwise>
	        		</c:choose>
       				</div>
	        	</td>
	        <tr>	        	
				<th>
	            	<div align="right">Approved Foreign Travel?</div>
	        	</th>
	        	<td>
	        		<div align="left">
	        		<c:choose>
	        			<c:when test="${fn:length(KualiForm.awardForSummaryPanelDisplay.approvedForeignTravelTrips)==0}">
	        				N
	        			</c:when>
	        			<c:otherwise>
	        				Y
	        			</c:otherwise>
	        		</c:choose>
	        		</div>
	        	</td>
	        	<th>
					<div align="right">Cost Share?</div>
        		</th>
        		<td>
        			<div align="left">
        			<c:choose>
	        			<c:when test="${fn:length(KualiForm.awardForSummaryPanelDisplay.awardCostShares)==0}">
	        				N
	        			</c:when>
	        			<c:otherwise>
	        				Y
	        			</c:otherwise>
	        		</c:choose>
	        		</div>
	        	</td>	        	
	        </tr>
	        <tr>	        	
	        	<th>
	            	<div align="right">F&A?</div>
	        	</th>
	        	<td colspan="3">
	        		<div align="left">
	        		<c:choose>
	        			<c:when test="${fn:length(KualiForm.awardForSummaryPanelDisplay.awardFandaRate)==0}">
	        				N
	        			</c:when>
	        			<c:otherwise>
	        				Y
	        			</c:otherwise>
	        		</c:choose>
	        		</div>
	        	</td>
	        </tr>
        </table>
        
        <h3>
    		<span class="subhead-left">Investigators</span>
    		<span class="subhead-right"><kul:help businessObjectClassName="org.kuali.kra.timeandmoney.transactions.PendingTransaction" altText="help"/></span>
        </h3>
        <table cellpadding="0" cellspacing="0" summary="">
	    	<tr>
	        	<th>
	            	Investigators
	        	</th>
	        	<th >
	        		Units
	        	</th>
	        </tr>
	        <tr>
	        	<td>
	        		<c:if test="${KualiForm.awardForSummaryPanelDisplay.principalInvestigatorName != null}" >
	        			<c:out value="${KualiForm.awardForSummaryPanelDisplay.principalInvestigatorName} (Principal Investigator)" />
	        		</c:if>
	        		&nbsp;
	        	</td>
	        	<td>
	        		<c:forEach var="leadUnit" items="${KualiForm.awardForSummaryPanelDisplay.principalInvestigator.units}" varStatus="status">
	        			<c:out value="${KualiForm.awardForSummaryPanelDisplay.principalInvestigator.units[status.index].unit.unitName}" />	        			
        				<c:if test="${KualiForm.awardForSummaryPanelDisplay.principalInvestigator.units[status.index].leadUnit}" >
        					(Lead Unit)
        				</c:if>
	        		</c:forEach>
	        	</td>
	        </tr>
	        
	        	<c:forEach var="coInvestigator" items="${KualiForm.awardForSummaryPanelDisplay.coInvestigators}" varStatus="status">
	        	<tr>
	        		<td>
	        			<c:out value="${KualiForm.awardForSummaryPanelDisplay.coInvestigators[status.index].fullName} (Co-Investigator)" />&nbsp;
        			</td>
        			<td>
	        			<c:forEach var="unit" items="${coInvestigator.units}" varStatus="status">
	        				<c:out value="${coInvestigator.units[status.index].unit.unitName}" /><br>	        			
	        			</c:forEach>
        			</td>
	        	</tr>
	        	</c:forEach>
	        	
	        	<c:forEach var="keyPerson" items="${KualiForm.awardForSummaryPanelDisplay.keyPersons}" varStatus="status">
	        	<tr>
	        		<td>
	        			<c:out value="${KualiForm.awardForSummaryPanelDisplay.keyPersons[status.index].fullName} (Key Person)" />&nbsp;
        			</td>
        			<td>
	        			<c:forEach var="unit" items="${keyPerson.units}" varStatus="status">
	        				<c:out value="${keyPerson.units[status.index].unit.unitName}" /><br>	        			
	        			</c:forEach>
        			</td>
	        	</tr>
	        	</c:forEach>
	        
	        	
	        	
	        </tr>	
	    </table>    	
   				<html:image property="methodToCall.goToPreviousAward.anchor${tabKey}"
						src='${ConfigProperties.kra.externalizable.images.url}tinybutton-prev.gif' styleClass="tinybutton" disabled="${KualiForm.rootNode}"/>
	
				<html:image property="methodToCall.goToNextAward.anchor${tabKey}"
						src='${ConfigProperties.kra.externalizable.images.url}tinybutton-next.gif' styleClass="tinybutton" disabled="${KualiForm.lastNode}"/>
	
	</div>   
</kul:tab>
