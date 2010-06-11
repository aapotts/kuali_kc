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
<c:set var="onlineReviewAttributes" value="${DataDictionary.ProtocolOnlineReview.attributes}" />
<c:set var="readOnly" value = "false"/>

<c:set var="docHeaderAttributes" value="${DataDictionary.DocumentHeader.attributes}" />
<c:set var="documentTypeName" value="${KualiForm.docTypeName}" />
<c:set var="documentEntry" value="${DataDictionary[documentTypeName]}" />



<kul:tabTop tabTitle="Create New Online Review" defaultOpen="true" tabErrorKey="${Constants.DOCUMENT_ERRORS}" >
	<div class="tab-container" align=center>
		  <!-- DOC OVERVIEW TABLE -->
					
		  <!-- DOC OVERVIEW TABLE -->
		  	
		 	<h3>Create New Online Review</h3>
		    <table cellpadding="0" cellspacing="0" class="datatable" title="view/edit document overview information" summary="view/edit document overview information">
	
              <tr>
                <th class="grid"><div align="right">*Reviewer:</div></th>
                <td nowrap class="grid">
					<html:select property="onlineReviewActionHelper.newReviewCommitteeMembershipId" tabindex="0">
						<option value = "">Select...</option>
						<c:forEach items = "${KualiForm.onlineReviewActionHelper.availableCommitteeMembersForCurrentSubmission}" var = "member">
							<option value = "${member.committeeMembershipId}" <c:if test = "${KualiForm.onlineReviewActionHelper.newReviewCommitteeMembershipId eq member.committeeMembershipId}">selected="true"</c:if>>
								${member.personName}
							</option>
						</c:forEach>
					</html:select>
				
				</td>
                <th class="grid"><div align="right"><kul:htmlAttributeLabel attributeEntry="${onlineReviewAttributes.dateRequested}" noColon="false" /></div></th>
                <td class="grid" >
					<kul:htmlControlAttribute property="onlineReviewActionHelper.newReviewDateRequested" attributeEntry="${onlineReviewAttributes.dateRequested}" datePicker="true" readOnly="${readOnly}" />
                </td>
              </tr>
              <tr>
              <th class="grid"></th>
              <td class = "grid"></td>
              
                <th class="grid"><div align="right"><kul:htmlAttributeLabel attributeEntry="${onlineReviewAttributes.dateDue}" noColon="false" /></div></th>
                <td class="grid" >
					<kul:htmlControlAttribute property="onlineReviewActionHelper.newReviewDateDue" attributeEntry="${onlineReviewAttributes.dateDue}" datePicker="true" readOnly="${readOnly}" />
                </td>
              
              </tr>
              
         
           	<tr>
		      <kul:htmlAttributeHeaderCell
		          labelFor="document.documentHeader.documentDescription"
		          attributeEntry="${docHeaderAttributes.documentDescription}"
		          horizontal="true"
		          />
		      <td align="left" valign="middle">
		      	<kul:htmlControlAttribute property="onlineReviewActionHelper.newReviewDocumentDescription" attributeEntry="${docHeaderAttributes.documentDescription}" readOnly="${readOnly}"/>
		      </td>
		      <kul:htmlAttributeHeaderCell
                  labelFor="document.documentHeader.explanation"
                  attributeEntry="${docHeaderAttributes.explanation}"
                  horizontal="true"
		          rowspan="2"
                  />
		      <td align="left" valign="middle" rowspan="2">
                  <kul:htmlControlAttribute
                      property="onlineReviewActionHelper.newReviewExplanation"
                      attributeEntry="${docHeaderAttributes.explanation}"
                      readOnly="${readOnly}"
                      readOnlyAlternateDisplay="${fn:replace(fn:escapeXml(onlineReviewActionHelper.newReviewExplanation), Constants.NEWLINE, '<br/>')}"
                      />
              </td>
		    </tr>
		    <tr>
			  <kul:htmlAttributeHeaderCell
		        labelFor="document.documentHeader.organizationDocumentNumber"
		        attributeEntry="${docHeaderAttributes.organizationDocumentNumber}"
		        horizontal="true"
		      />			  
              <td align="left" valign="middle">
              	<kul:htmlControlAttribute property="onlineReviewActionHelper.newReviewOrganizationDocumentNumber" attributeEntry="${docHeaderAttributes.organizationDocumentNumber}" readOnly="${readOnly}"/>
              </td>
            </tr>
			</table>   
         
			<br/>
	
			<html:image property="methodToCall.testCreateOnlineReview.anchor${tabKey}"
							            src='${ConfigProperties.kra.externalizable.images.url}tinybutton-create.gif' styleClass="tinybutton"/>

			
			
        </div>
		</kul:tabTop>



