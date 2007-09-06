<%--
 Copyright 2007 The Kuali Foundation.
 
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
<%@ include file="/kr/WEB-INF/jsp/tldHeader.jsp"%>

<c:set var="proposalDevelopmentAttributes" value="${DataDictionary.ProposalDevelopmentDocument.attributes}" />
<c:set var="proSpecialAttriburesAttributes" value="${DataDictionary.PropSpecialReview.attributes}" />
<c:set var="action" value="proposalDevelopmentSpecialReview" />
<kul:tabTop tabTitle="Special Review" defaultOpen="true" tabErrorKey="document.propSpecialReviews*">
	<div class="tab-container" align="center">
    	<div class="h2-container">
    		<span class="subhead-left"><h2>Special Review</h2></span>
    		<span class="subhead-right"><kul:help businessObjectClassName="fillMeIn" altText="help"/></span>
        </div>
        
        <table cellpadding=0 cellspacing=0 summary="">
          	<tr>
          		<th><div align="left">&nbsp</div></th> 
          		<th><div align="left"><kul:htmlAttributeLabel attributeEntry="${proSpecialAttriburesAttributes.specialReviewCode}" skipHelpUrl="true" noColon="true" /></div></th>
          		<th><div align="left"><kul:htmlAttributeLabel attributeEntry="${proSpecialAttriburesAttributes.approvalTypeCode}" skipHelpUrl="true" noColon="true" /></div></th>
          		<th><div align="left"><kul:htmlAttributeLabel attributeEntry="${proSpecialAttriburesAttributes.protocolNumber}" skipHelpUrl="true" noColon="true" /></div></th>
          		<th><div align="left"><kul:htmlAttributeLabel attributeEntry="${proSpecialAttriburesAttributes.applicationDate}" skipHelpUrl="true" noColon="true" /></div></th>
          		<th><div align="left"><kul:htmlAttributeLabel attributeEntry="${proSpecialAttriburesAttributes.approvalDate}" skipHelpUrl="true" noColon="true" /></div></th>
          		<th><div align="left"><kul:htmlAttributeLabel attributeEntry="${proSpecialAttriburesAttributes.comments}" skipHelpUrl="true" noColon="true" /></div></th>
              	<kul:htmlAttributeHeaderCell literalLabel="Action" scope="col"/>
          	
          	</tr>        
             <tr>
                <c:set var="textAreaFieldName" value="newPropSpecialReview.comments" />
				<th class="infoline">
					<c:out value="Add:" />
				</th>

                <td align="left" valign="middle">
                	<kul:htmlControlAttribute property="newPropSpecialReview.specialReviewCode" attributeEntry="${proSpecialAttriburesAttributes.specialReviewCode}" />
				</td>
                <td>
                	<kul:htmlControlAttribute property="newPropSpecialReview.approvalTypeCode" attributeEntry="${proSpecialAttriburesAttributes.approvalTypeCode}" />
                </td>
                <td>                	
                  <kul:htmlControlAttribute property="newPropSpecialReview.protocolNumber" attributeEntry="${proSpecialAttriburesAttributes.protocolNumber}" />
                 <kul:lookup boClassName="org.kuali.kra.bo.Protocol" 
                    fieldConversions="protocolNumber:newPropSpecialReview.protocolNumber" /> 
				</td>
                <td align="left" valign="middle">
                	<kul:htmlControlAttribute property="newPropSpecialReview.applicationDate" attributeEntry="${proSpecialAttriburesAttributes.applicationDate}" datePicker="true"/>
                </td>
                <td align="left" valign="middle">
                	<kul:htmlControlAttribute property="newPropSpecialReview.approvalDate" attributeEntry="${proSpecialAttriburesAttributes.approvalDate}" datePicker="true"/>
                </td>
                <td align="left" valign="middle">
                	<kul:htmlControlAttribute property="newPropSpecialReview.comments" attributeEntry="${proSpecialAttriburesAttributes.comments}" />
                    <html:image property="methodToCall.updateTextArea.((#${textAreaFieldName}:${action}:${proSpecialAttriburesAttributes.comments.label}#))" src='${ConfigProperties.kra.externalizable.images.url}pencil_add.png' onclick="javascript: textAreaPop(document.getElementById('${textAreaFieldName}').value,'${textAreaFieldName}','proposalDevelopment','${proSpecialAttriburesAttributes.comments.label}');return false"/>
                </td>
				<td>
					<div align=center>
						<html:image property="methodToCall.addSpecialReview"
						src='${ConfigProperties.kra.externalizable.images.url}tinybutton-add1.gif' />
					</div>
                </td>
            </tr>
            
        	<c:forEach var="specialReview" items="${KualiForm.document.propSpecialReviews}" varStatus="status">
	             <tr>
	                <c:set var="textAreaFieldName" value="document.propSpecialReviews[${status.index}].comments" />
					<th class="infoline">
						<c:out value="${status.index+1}" />
					</th>
        			<input type="hidden" name="document.propSpecialReviews[${status.index}].specialReviewNumber" value="${status.index+1}">
	                <td align="left" valign="middle">
	                	<kul:htmlControlAttribute property="document.propSpecialReviews[${status.index}].specialReviewCode" attributeEntry="${proSpecialAttriburesAttributes.specialReviewCode}" />
					</td>
	                <td>
	                	<kul:htmlControlAttribute property="document.propSpecialReviews[${status.index}].approvalTypeCode" attributeEntry="${proSpecialAttriburesAttributes.approvalTypeCode}" />
	                </td>
	                <td>                	
	                  <kul:htmlControlAttribute property="document.propSpecialReviews[${status.index}].protocolNumber" attributeEntry="${proSpecialAttriburesAttributes.protocolNumber}" />
	                 <kul:lookup boClassName="org.kuali.kra.bo.Protocol" 
	                    fieldConversions="protocolNumber:document.propSpecialReviews[${status.index}].protocolNumber" /> 
					</td>
	                <td align="left" valign="middle">
	                	<kul:htmlControlAttribute property="document.propSpecialReviews[${status.index}].applicationDate" attributeEntry="${proSpecialAttriburesAttributes.applicationDate}" datePicker="true"/>
	                </td>
	                <td align="left" valign="middle">
	                	<kul:htmlControlAttribute property="document.propSpecialReviews[${status.index}].approvalDate" attributeEntry="${proSpecialAttriburesAttributes.approvalDate}" datePicker="true"/>
	                </td>
	                <td align="left" valign="middle">
	                	<kul:htmlControlAttribute property="document.propSpecialReviews[${status.index}].comments" attributeEntry="${proSpecialAttriburesAttributes.comments}" />
	                    <html:image property="methodToCall.updateTextArea.((#${textAreaFieldName}:${action}:${proSpecialAttriburesAttributes.comments.label}#))" src='${ConfigProperties.kra.externalizable.images.url}pencil_add.png' onclick="javascript: textAreaPop(document.getElementById('${textAreaFieldName}').value,'${textAreaFieldName}','proposalDevelopment','${proSpecialAttriburesAttributes.comments.label}');return false"/>
	                </td>
					<td>
					<div align=center>
						<html:image property="methodToCall.deleteSpecialReview.line${status.index}."
							src='${ConfigProperties.kra.externalizable.images.url}tinybutton-delete1.gif' />
					</div>
	                </td>
	            </tr>
        	</c:forEach>        

            
        </table>
    </div> 
</kul:tabTop>
