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
<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<c:set var="propPersonBioAttributes" value="${DataDictionary.ProposalPersonBiography.attributes}" />
<c:set var="propPerDocTypeAttributes" value="${DataDictionary.PropPerDocType.attributes}" />
<c:set var="textAreaFieldName" value="newPropPersonBio.description" />
<c:set var="action" value="proposalDevelopmentAbstractsAttachments" />
<kul:tab tabTitle="Personnel Attachments (${fn:length(KualiForm.document.propPersonBios)})" defaultOpen="true" tabErrorKey="document.propPersonBio*,newPropPersonBio*">
	<div class="tab-container" align="center">
    	<div class="h2-container">
    		<span class="subhead-left"><h2>Add Personnel Attachments</h2></span>
    		<span class="subhead-right"><kul:help businessObjectClassName="fillMeIn" altText="help"/></span>
        </div>
        
        <table cellpadding=0 cellspacing=0 summary="">
          	<tr>
          	    <th><div align="left">&nbsp</div></th> 
          		<th><div align="left"><kul:htmlAttributeLabel attributeEntry="${propPersonBioAttributes.updateTimestamp}" noColon="true" /></div></th>
          		<th><div align="left"><kul:htmlAttributeLabel attributeEntry="${propPersonBioAttributes.updateUser}" noColon="true" /></div></th>
          		<th><div align="left"><kul:htmlAttributeLabel attributeEntry="${propPersonBioAttributes.proposalPersonNumber}" noColon="true" /></div></th>
          		<th><div align="left"><kul:htmlAttributeLabel attributeEntry="${propPersonBioAttributes.documentTypeCode}" noColon="true" /></div></th>
          		<th><div align="left"><kul:htmlAttributeLabel attributeEntry="${propPersonBioAttributes.description}" noColon="true" /></div></th>
          		<th><div align="left"><kul:htmlAttributeLabel attributeEntry="${propPersonBioAttributes.fileName}" noColon="true" /></div></th>
              	<kul:htmlAttributeHeaderCell literalLabel="Action" scope="col"/>
	  			             		
          	</tr>        
          	<tr>
          	  <c:set var="personSelectStyle" value="" scope="request"/>
          	  
			     <c:forEach items="${ErrorPropertyList}" var="key">
				    <c:if test="${key eq 'newPropPersonBio.proposalPersonNumber'}">
					  <c:set var="personSelectStyle" value="background-color:#FFD5D5" scope="request"/>
				    </c:if>
			     </c:forEach>
          	    
				<th class="infoline">
					Add:
				</th>

                <td class="infoline">                
                	<kul:htmlControlAttribute property="newPropPersonBio.updateTimestamp" attributeEntry="${propPersonBioAttributes.updateTimestamp}" readOnly="true" />	            
				</td>
                <td class="infoline">
                	<kul:htmlControlAttribute property="newPropPersonBio.updateUser" attributeEntry="${propPersonBioAttributes.updateUser}" readOnly="true" />
                </td>
                <td class="infoline">          	
          		       <html:select property="newPropPersonBio.proposalPersonNumber" style="${personSelectStyle}">
  		                    <c:set var="proposalPersons" value="${KualiForm.document.proposalPersons}"/>
  		                    <option value="">select:</option>
	    		            <html:options collection="proposalPersons" property="proposalPersonNumber" labelProperty="fullName"/>
	  			        </html:select>
                </td>
                <td class="infoline">                	
                	<kul:htmlControlAttribute property="newPropPersonBio.documentTypeCode" attributeEntry="${propPersonBioAttributes.documentTypeCode}" styleClass="fixed-size-select"/>
				</td>
                <td class="infoline">
                	<kul:htmlControlAttribute property="newPropPersonBio.description" attributeEntry="${propPersonBioAttributes.description}" />
                    <kra:expandedTextArea textAreaFieldName="${textAreaFieldName}" action="${action}" textAreaLabel="${propPersonBioAttributes.description.label}" />
				</td>
                
                <td class="infoline">
                	<html:file property="newPropPersonBio.personnelAttachmentFile" />
                </td>
				<td class="infoline">
					<div align=center>
						<html:image property="methodToCall.addPersonnelAttachment.anchor${tabKey}"
						src='${ConfigProperties.kra.externalizable.images.url}tinybutton-add1.gif' />
					</div>
                </td>
            </tr>

        	<c:forEach var="propPersonBio" items="${KualiForm.document.propPersonBios}" varStatus="status">
	             <tr>
					<th class="infoline" align="right">
						${status.index+1}:
					</th>
	                <td>
                	    <kul:htmlControlAttribute property="document.propPersonBio[${status.index}].updateTimestamp" readOnly="true" attributeEntry="${propPersonBioAttributes.updateTimestamp}" />
					</td>
	                <td>
                	    ${propPersonBio.authorPersonName}
	                </td>
	                <td>
        			    <input type="hidden" name="document.propPersonBio[${status.index}].proposalPersonNumber" value="${propPersonBio.proposalPersonNumber}" /> 
	                	<!--<kul:htmlControlAttribute property="document.propPersonBio[${status.index}].personId" attributeEntry="${propPersonBioAttributes.personId}" />--> 
        				<c:forEach var="keyPerson" items="${KualiForm.document.proposalPersons}" varStatus="idx">
        				   <c:if test="${keyPerson.proposalPersonNumber == propPersonBio.proposalPersonNumber}" >
        				       ${keyPerson.fullName}
		        			    <input type="hidden" name="document.propPersonBio[${status.index}].personId" value="${keyPerson.personId}" /> 
			    			    <input type="hidden" name="document.propPersonBio[${status.index}].rolodexId" value="${keyPerson.rolodexId}" /> 
        				   </c:if>
						</c:forEach>
	                </td>
	                <td>   
	                    ${propPersonBio.propPerDocType.description}          	
					</td>
	                <td>
	                	${propPersonBio.description} 
					</td>
	                <td>
	                    ${propPersonBio.fileName}
	                </td>
	                <td>
					<div align=center>
						<html:image property="methodToCall.viewPersonnelAttachment.line${status.index}.anchor${currentTabIndex}"
									src='${ConfigProperties.kra.externalizable.images.url}tinybutton-view.gif' 
									onclick="javascript: openNewWindow('proposalDevelopmentAbstractsAttachments','viewPersonnelAttachment',${status.index},${KualiForm.formKey},'${KualiForm.document.sessionDocument}'); return false" />
						<html:image property="methodToCall.deletePersonnelAttachment.line${status.index}.anchor${currentTabIndex}"
							src='${ConfigProperties.kra.externalizable.images.url}tinybutton-delete1.gif' />
					</div>
	                </td>
	            </tr>
        	</c:forEach>        

          	
        </table>
    </div>
</kul:tab>
