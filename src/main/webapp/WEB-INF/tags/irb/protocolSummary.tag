<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>
          
<kra:innerTab tabTitle="Summary" parentTab="" defaultOpen="false">
    <div class="tab-container">
	<table cellpadding="0" cellspacing="0">
    	<tbody>
            <tr>
                <th style="text-align:right; width:135px;">Protocol Number:</th>
                <td>${KualiForm.actionHelper.protocolSummary.protocolNumber}&nbsp;</td>
                <th style="text-align:right; width:135px">Application Date:</th>
                <td>${KualiForm.actionHelper.protocolSummary.applicationDate}&nbsp;</td>
			</tr>
			
			<tr>
                <th style="text-align:right; width:135px;">Approval Date:</th>
                <td>${KualiForm.actionHelper.protocolSummary.approvalDate}&nbsp;</td>
                <th style="text-align:right; width:135px;">Expiration Date:</th>
                <td>${KualiForm.actionHelper.protocolSummary.expirationDate}&nbsp;</td>
			</tr>
			
			<tr>
                <th style="text-align:right; width:135px;">Last Approval Date:</th>
                <td>${KualiForm.actionHelper.protocolSummary.lastApprovalDate}&nbsp;</td>
                <th style="text-align:right; width:135px">Type:</th>
                <td>${KualiForm.actionHelper.protocolSummary.type}&nbsp;</td>
			</tr>
			
			<tr>
                <th style="text-align:right; width:135px;">PI:</th>
                <td>
                    ${KualiForm.actionHelper.protocolSummary.piName}&nbsp;
                    <input type="hidden" name="actionHelper.protocolSummary.piProtocolPersonId"
                           value="${KualiForm.actionHelper.protocolSummary.piProtocolPersonId}" />
                    <kul:directInquiry boClassName="org.kuali.kra.irb.personnel.ProtocolPerson"
	                     inquiryParameters="actionHelper.protocolSummary.piProtocolPersonId:protocolPersonId" 
	                     anchor="${currentTabIndex}" />
                </td>
                <th style="text-align:right; width:135px">Status:</th>
                <td>${KualiForm.actionHelper.protocolSummary.status}&nbsp;</td>
			</tr>
			
			<tr>
                <th style="text-align:right; width:135px;">Title:</th>
                <td colspan="3">${KualiForm.actionHelper.protocolSummary.title}&nbsp;</td>
			</tr>
		</tbody>                  
    </table>
    
    <table cellpadding="0" cellspacing="0">
    	<tbody>
            <tr>
                <td style="background-color: rgb(195, 195, 195); font-weight: bold;" colspan="5">Personnel:</td>
            </tr>
            
            <tr>
                <th style="width: 50px;">&nbsp;</th>
                <td class="infoline fineprint" style="font-weight: bold; text-align: center; color: rgb(51, 51, 51);">Name</td>
                <td class="infoline fineprint" style="font-weight: bold; text-align: center; color: rgb(51, 51, 51);">Role</td>
                <td class="infoline fineprint" style="font-weight: bold; text-align: center; color: rgb(51, 51, 51);">Affiliation</td>
                <td class="infoline fineprint" style="font-weight: bold; text-align: center; color: rgb(51, 51, 51);">Unit(s)</td>              
            </tr>
            
            <c:forEach items="${KualiForm.actionHelper.protocolSummary.persons}" var="person" varStatus="status">
                <tr>
                    <th style="width:50px">${status.index + 1}</th>
                    <td class="changed">${person.name}</td>
                    <td class="changed">${person.roleName}</td>
                    <td class="changed">${person.affiliation}&nbsp;</td>
                    <td class="changed">
                        <c:forEach items="${person.units}" var="unit" varStatus="status">
           					${unit.unitNumber} : ${unit.unitName}<br />
           				</c:forEach>
                  	</td>
                </tr>
            </c:forEach>
    	</tbody>
    </table>
    
    <table cellpadding="0" cellspacing="0">
    	<tbody>
            <tr>
                <td style="background-color: rgb(195, 195, 195); font-weight: bold;" colspan="2">Areas of Research:</td>
            </tr>
            <c:forEach items="${KualiForm.actionHelper.protocolSummary.researchAreas}" var="researchArea" varStatus="status">
                <tr>
                    <th style="width:50px">${status.index + 1}</th>
                    <td class="changed">${researchArea.researchAreaCode} : ${researchArea.description}</td>
               </tr>
            </c:forEach>
    	</tbody>
    </table>
    
    <table cellpadding="0" cellspacing="0">
    	<tbody>
            <tr>
                <td style="background-color: rgb(195, 195, 195); font-weight: bold;" colspan="3">Attachments:</td>
            </tr>
            <c:forEach items="${KualiForm.actionHelper.protocolSummary.attachments}" var="attachment" varStatus="status">
                <tr>
                    <th style="width:50px">${status.index + 1}</th>
                    <td class="changed">${attachment.fileName}</td>
                    <td style="width:90%">
                        <html:image property="methodToCall.viewAttachmentProtocol.line${status.index}.anchor${currentTabIndex}"
						  	        src='${ConfigProperties.kra.externalizable.images.url}tinybutton-view.gif' styleClass="tinybutton"
						     	    alt="View Protocol Attachment" onclick="excludeSubmitRestriction = true;"/>
                    </td>
               </tr>
            </c:forEach>
    	</tbody>
    </table>
    
    <table cellpadding="0" cellspacing="0">
    	<tbody>
            <tr>
                <td style="background-color: rgb(195, 195, 195); font-weight: bold;" colspan="5">Funding Source:</td>
            </tr>
            
            <tr>
                <th style="width: 50px;">&nbsp;</th>
                <td class="infoline fineprint" style="font-weight: bold; text-align: center; color: rgb(51, 51, 51);">Funding Type</td>
                <td class="infoline fineprint" style="font-weight: bold; text-align: center; color: rgb(51, 51, 51);">Funding ID</td>
                <td class="infoline fineprint" style="font-weight: bold; text-align: center; color: rgb(51, 51, 51);">Source</td>
                <td class="infoline fineprint" style="font-weight: bold; text-align: center; color: rgb(51, 51, 51);">Title</td>              
            </tr>
            
            <c:forEach items="${KualiForm.actionHelper.protocolSummary.fundingSources}" var="fundingSource" varStatus="status">
                <tr>
                    <th style="width:50px">${status.index + 1}</th>
                    <td class="changed">${fundingSource.fundingType}</td>
                    <td class="changed">${fundingSource.fundingId}</td>
                    <td class="changed">${fundingSource.fundingSource}</td>
                    <td class="changed">${fundingSource.title}&nbsp;</td>
               </tr>
            </c:forEach>
    	</tbody>
    </table>
    
    <table cellpadding="0" cellspacing="0">
    	<tbody>
            <tr>
                <td style="background-color: rgb(195, 195, 195); font-weight: bold;" colspan="3">Participant Types:</td>
            </tr>
            
            <tr>
                <th style="width: 50px;">&nbsp;</th>
                <td class="infoline fineprint" style="font-weight: bold; text-align: center; color: rgb(51, 51, 51);">Description</td>
                <td class="infoline fineprint" style="font-weight: bold; text-align: center; color: rgb(51, 51, 51);">Count</td>
            </tr>
            
            <c:forEach items="${KualiForm.actionHelper.protocolSummary.participants}" var="participant" varStatus="status">
                <tr>
                    <th style="width:50px">${status.index + 1}</th>
                    <td class="changed">${participant.description}</td>
                    <td class="changed">${participant.count}</td>
               </tr>
            </c:forEach>
    	</tbody>
    </table>
    
    <table cellpadding="0" cellspacing="0">
    	<tbody>
            <tr>
                <td style="background-color: rgb(195, 195, 195); font-weight: bold;" colspan="5">Organization:</td>
            </tr>
            
            <tr>
                <th style="width: 50px;">&nbsp;</th>
                <td class="infoline fineprint" style="font-weight: bold; text-align: center; color: rgb(51, 51, 51);">Organization ID</td>
                <td class="infoline fineprint" style="font-weight: bold; text-align: center; color: rgb(51, 51, 51);">Organization Type</td>
                <td class="infoline fineprint" style="font-weight: bold; text-align: center; color: rgb(51, 51, 51);">Contact</td>
                <td class="infoline fineprint" style="font-weight: bold; text-align: center; color: rgb(51, 51, 51);">FWA Number</td>
            </tr>
            
            <c:forEach items="${KualiForm.actionHelper.protocolSummary.organizations}" var="organization" varStatus="status">
                <tr>
                    <th style="width:50px">${status.index + 1}</th>
                    <td class="changed">
                        ${organization.id}
                        <input type="hidden" name="actionHelper.protocolSummary.organizations[${status.index}].organizationId"
                               value="${organization.organizationId}" />
                        <kul:directInquiry boClassName="org.kuali.kra.bo.Organization"
                             inquiryParameters="actionHelper.protocolSummary.organizations[${status.index}].organizationId:organizationId" 
                             anchor="${currentTabIndex}" />
                        <br /><span class="fineprint">${organization.name}</span>
                    </td>
                    <td class="changed">${organization.type}</td>
                    <td class="changed">
                        ${organization.contact}
                        <input type="hidden" name="actionHelper.protocolSummary.organizations[${status.index}].contactId"
                               value="${organization.contactId}" />
                        <kul:directInquiry boClassName="org.kuali.kra.bo.Rolodex"
                             inquiryParameters="actionHelper.protocolSummary.organizations[${status.index}].contactId:rolodexId" 
                             anchor="${currentTabIndex}" />
                    </td>
                    <td class="changed">${organization.fwaNumber}&nbsp;</td>
               </tr>
            </c:forEach>
    	</tbody>
    </table>
    
    <table cellpadding="0" cellspacing="0">
    	<tbody>
            <tr>
                <td style="background-color: rgb(195, 195, 195); font-weight: bold;" colspan="8">Special Reviews:</td>
            </tr>
            
             <tr>
                <th style="width: 50px;">&nbsp;</th>
                <td class="infoline fineprint" style="font-weight: bold; text-align: center; color: rgb(51, 51, 51);">Type</td>
                <td class="infoline fineprint" style="font-weight: bold; text-align: center; color: rgb(51, 51, 51);">Approval Status</td>
                <td class="infoline fineprint" style="font-weight: bold; text-align: center; color: rgb(51, 51, 51);">Protocol #</td>
                <td class="infoline fineprint" style="font-weight: bold; text-align: center; color: rgb(51, 51, 51);">Application Date</td>
                <td class="infoline fineprint" style="font-weight: bold; text-align: center; color: rgb(51, 51, 51);">Approval Date</td>
                <td class="infoline fineprint" style="font-weight: bold; text-align: center; color: rgb(51, 51, 51);">Expiration Date</td>
                <td class="infoline fineprint" style="font-weight: bold; text-align: center; color: rgb(51, 51, 51);">Exemption #</td>
            </tr>
            
            <c:forEach items="${KualiForm.actionHelper.protocolSummary.specialReviews}" var="specialReview" varStatus="status">
                <tr>
                    <th rowspan="2" style="width:50px">${status.index + 1}</th>
                    <td class="changed">${specialReview.type}</td>
                    <td class="changed">${specialReview.approvalStatus}</td>
                    <td class="changed">${specialReview.protocolNumber}&nbsp;</td>
                    <td class="changed">${specialReview.applicationDate}&nbsp;</td>
                    <td class="changed">${specialReview.approvalDate}&nbsp;</td>
                    <td class="changed">${specialReview.expirationDate}&nbsp;</td>
                    <td class="changed">${specialReview.exemptionNumbers}&nbsp;</td>
               </tr>
               
               <tr>
                   <td colspan="7">
                       <span class="fineprint">
                           <c:choose>
                               <c:when test="${specialReview.comment == null || specialReviewComment == ''}">
                                   Comment: <i>This is no comment for this record.</i>
                               </c:when>
                               <c:otherwise>
                                   ${specialReview.comment}
                               </c:otherwise>
                           </c:choose>
                       </span>
                   </td>
               </tr>
            </c:forEach>
    	</tbody>
    </table>
    
    <table cellpadding="0" cellspacing="0">
    	<tbody>
            <tr>
                <td style="background-color: rgb(195, 195, 195); font-weight: bold;" colspan="5">Additional Information:</td>
            </tr>
            
            <tr>
                <th style="text-align:right; width:135px;">FDA IND or IDE #:</th>
                <td>${KualiForm.actionHelper.protocolSummary.additionalInfo.fdaApplicationNumber}&nbsp;</td>
                <th style="text-align:right; width:135px">Billable:</th>
                <td>${KualiForm.actionHelper.protocolSummary.additionalInfo.billable}&nbsp;</td>
            </tr>
            
            <tr>
                <th style="text-align:right; width:135px;">Reference ID1:</th>
                <td>${KualiForm.actionHelper.protocolSummary.additionalInfo.referenceId1}&nbsp;</td>
                <th style="text-align:right; width:135px;">Reference ID2:</th>
                <td>${KualiForm.actionHelper.protocolSummary.additionalInfo.referenceId1}&nbsp;</td>
            </tr>
            
            <tr>
                <th style="text-align:right; width:135px;">Summary/Keywords:</th>
                <td colspan="3">${KualiForm.actionHelper.protocolSummary.additionalInfo.description}&nbsp;</td>
            </tr>
    	</tbody>
    </table>
    </div>
</kra:innerTab>
