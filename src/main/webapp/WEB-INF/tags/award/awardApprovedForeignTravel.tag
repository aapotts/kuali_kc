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
<%-- member of awardSpecialApproval.tag --%>

<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>
 
<kul:innerTab parentTab="Special Approval" tabItemCount="${KualiForm.document.awardList[0].approvedForeignTravelTripCount}" defaultOpen="false" 
				tabTitle="Approved Foreign Travel" tabErrorKey="newAwardAwardApprovedForeignTravel*,document.awardList[0].approvedForeignTravelTrips*" >
				
	<c:set var="approvedForeignTravelAttributes" value="${DataDictionary.AwardApprovedForeignTravel.attributes}" />
			
    <table id="approved-foreignTravel-table" cellpadding="0" cellspacing="0" summary="Approved Foreign Travel">
		<tr>
			<th scope="row">&nbsp;</th>
			<th><kul:htmlAttributeLabel attributeEntry="${approvedForeignTravelAttributes.travelerName}" useShortLabel="true" noColon="true" /></th>
			<th><kul:htmlAttributeLabel attributeEntry="${approvedForeignTravelAttributes.destination}" useShortLabel="true" noColon="true" /></th>
			<th><kul:htmlAttributeLabel attributeEntry="${approvedForeignTravelAttributes.startDate}" useShortLabel="true" noColon="true"/></th>
			<th><kul:htmlAttributeLabel attributeEntry="${approvedForeignTravelAttributes.endDate}" useShortLabel="true" noColon="true"/></th>
			<th><kul:htmlAttributeLabel attributeEntry="${approvedForeignTravelAttributes.amount}" useShortLabel="true" noColon="true"/></th>
			<th><div align="center">Actions</div></th>
		</tr>
		
		<tr>
        	<th width="50" align="center" scope="row"><div align="right">Add:</div></th>
        	<td class="infoline">
        	  	<div align="center">
        	  	 	<kul:htmlControlAttribute property="approvedForeignTravelBean.newApprovedForeignTravel.travelerName" attributeEntry="${approvedForeignTravelAttributes.travelerName}" readOnly="true" />
        	  	 	<kul:lookup boClassName="org.kuali.kra.bo.Person" fieldConversions="personId:approvedForeignTravelBean.newApprovedForeignTravel.travelerId" anchor="${tabKey}" 
        	  	 				lookupParameters="approvedForeignTravelBean.newApprovedForeignTravel.travelerId:personId" />
        	 	</div>
        	</td>
            <td class="infoline">
              	<div align="center">
            		<kul:htmlControlAttribute property="approvedForeignTravelBean.newApprovedForeignTravel.destination" attributeEntry="${approvedForeignTravelAttributes.destination}" />
              	</div>
            </td>
            <td class="infoline">
            	<div align="center">
        	    	<kul:htmlControlAttribute property="approvedForeignTravelBean.newApprovedForeignTravel.startDate" 
        	    								attributeEntry="${approvedForeignTravelAttributes.startDate}" datePicker="true" />
        	  	</div>
            </td>
            <td class="infoline">
            	<div align="center">
        	    	<kul:htmlControlAttribute property="approvedForeignTravelBean.newApprovedForeignTravel.endDate" 
        	    								attributeEntry="${approvedForeignTravelAttributes.endDate}" datePicker="true"/>
        	  	</div>
            </td>
            <td class="infoline">
            	<div align="center">
        	    	<kul:htmlControlAttribute property="approvedForeignTravelBean.newApprovedForeignTravel.amount" attributeEntry="${approvedForeignTravelAttributes.amount}"/>
        	  	</div>
            </td>
            <td class="infoline">
            	<div align="center">
					<html:image property="methodToCall.addApprovedForeignTravel.anchor${tabKey}"
					src='${ConfigProperties.kra.externalizable.images.url}tinybutton-add1.gif' styleClass="tinybutton"/>
				</div>
            </td>
      	</tr>
		
		<c:forEach var="approvedForeignTravelTrip" items="${KualiForm.document.awardList[0].approvedForeignTravelTrips}" varStatus="status">
             <tr>
				<th width="10%" class="infoline">
					<c:out value="${status.index+1}" />
				</th>
                <td width="15%" valign="middle">
                	<div align="center">
                		<kul:htmlControlAttribute property="document.awardList[0].approvedForeignTravelTrips[${status.index}].travelerName" 
                								attributeEntry="${approvedForeignTravelAttributes.travelerName}" readOnly="true"/>
                		<kul:lookup boClassName="org.kuali.kra.bo.Person" 
                				fieldConversions="personId:document.awardList[0].approvedForeignTravelTrips[${status.index}].travelerId" anchor="${tabKey}" 
        	  	 				lookupParameters="document.awardList[0].approvedForeignTravelTrips[${status.index}].travelerId:personId" />
					</div>
				</td>
                <td width="15%" valign="middle">
                	<div align="center">
						<kul:htmlControlAttribute property="document.awardList[0].approvedForeignTravelTrips[${status.index}].destination" attributeEntry="${approvedForeignTravelAttributes.destination}"/>
					</div>
				</td>
                <td width="15%" valign="middle">
                	<div align="center">                	
						<kul:htmlControlAttribute property="document.awardList[0].approvedForeignTravelTrips[${status.index}].startDate" attributeEntry="${approvedForeignTravelAttributes.startDate}"/>
					</div> 
				</td>
				<td width="15%" valign="middle">
                	<div align="center">                	
						<kul:htmlControlAttribute property="document.awardList[0].approvedForeignTravelTrips[${status.index}].endDate" attributeEntry="${approvedForeignTravelAttributes.endDate}"/>
					</div> 
				</td>
                <td width="15%" valign="middle">
                	<div align="center">                	
						<kul:htmlControlAttribute property="document.awardList[0].approvedForeignTravelTrips[${status.index}].amount" 
								attributeEntry="${approvedForeignTravelAttributes.amount}" styleClass="amount" />
					</div> 
				</td>
                
				<td width="15%" class="infoline">
					<div align="center">
						<html:image property="methodToCall.deleteApprovedForeignTravelTrip.line${status.index}.anchor${currentTabIndex}"
						src='${ConfigProperties.kra.externalizable.images.url}tinybutton-delete1.gif' styleClass="tinybutton"/>
					</div>
                </td>
            </tr>
    	</c:forEach>
    	<tr>
      		<th align="center" scope="row"><div>Totals</div></th>
      		<th colspan="4" scope="row">&nbsp;</th>
      		<th align="right">
      			<div align="right">
      				<fmt:formatNumber type="CURRENCY" value="${KualiForm.document.award.totalApprovedApprovedForeignTravelAmount}" />
                </div>
         	</th>
         	<th scope="row">
         		<div align="center">
					<html:image property="methodToCall.recalculateSpecialApprovalTotals.anchor${tabKey}" 
								src='${ConfigProperties.kra.externalizable.images.url}tinybutton-recalculate.gif' styleClass="tinybutton" />
				</div>
			</th>
		</tr>
		
    </table>
</kul:innerTab>