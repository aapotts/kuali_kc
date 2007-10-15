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

<c:set var="propPersonBioAttributes" value="${DataDictionary.PropPersonBio.attributes}" />
<c:set var="propPerDocTypeAttributes" value="${DataDictionary.PropPerDocType.attributes}" />
<kul:tab tabTitle="Personnel Attachments" defaultOpen="true" tabErrorKey="">
	<div class="tab-container" align="center">
    	<div class="h2-container">
    		<span class="subhead-left"><h2>Personnel Attachments</h2></span>
    		<span class="subhead-right"><kul:help businessObjectClassName="fillMeIn" altText="help"/></span>
        </div>
        
        <table cellpadding=0 cellspacing=0 summary="">
          	<tr>
          	    <th><div align="left">&nbsp</div></th> 
          		<th><div align="left"><kul:htmlAttributeLabel attributeEntry="${propPersonBioAttributes.updateTimestamp}" skipHelpUrl="true" noColon="true" /></div></th>
          		<th><div align="left"><kul:htmlAttributeLabel attributeEntry="${propPersonBioAttributes.updateUser}" skipHelpUrl="true" noColon="true" /></div></th>
          		<th><div align="left"><kul:htmlAttributeLabel attributeEntry="${propPersonBioAttributes.documentTypeCode}" skipHelpUrl="true" noColon="true" /></div></th>
          		<th><div align="left"><kul:htmlAttributeLabel attributeEntry="${propPersonBioAttributes.personId}" skipHelpUrl="true" noColon="true" /></div></th>
          		<th><div align="left"><kul:htmlAttributeLabel attributeEntry="${propPersonBioAttributes.description}" skipHelpUrl="true" noColon="true" /></div></th>
          		<th><div align="left"><kul:htmlAttributeLabel attributeEntry="${propPersonBioAttributes.fileName}" skipHelpUrl="true" noColon="true" /></div></th>
              	<kul:htmlAttributeHeaderCell literalLabel="Action" scope="col"/>
	  			             		
          	</tr>        
          	<tr>
				<th class="infoline">
					Add:
				</th>

                <td align="left" valign="middle">                
                	<kul:htmlControlAttribute property="newPropPersonBio.updateTimestamp" attributeEntry="${propPersonBioAttributes.updateTimestamp}" readOnly="true" />	            
				</td>
                <td>
                	<kul:htmlControlAttribute property="newPropPersonBio.updateUser" attributeEntry="${propPersonBioAttributes.updateUser}" readOnly="true" />
                </td>
                <td>                	
                	<kul:htmlControlAttribute property="newPropPersonBio.documentTypeCode" attributeEntry="${propPersonBioAttributes.documentTypeCode}" />
				</td>
                <td>          	
          		       <html:select property="newPropPersonBio.personId">
  		                    <c:set var="proposalPersons" value="${KualiForm.document.proposalPersons}"/>
	    		            <html:options collection="proposalPersons" property="personId" labelProperty="fullName"/>
	  			        </html:select>
                </td>
                <td align="left" valign="middle">
                	<kul:htmlControlAttribute property="newPropPersonBio.description" attributeEntry="${propPersonBioAttributes.description}" />
				</td>
                
                <td align="left" valign="middle">
                	<html:file property="newPropPersonBio.personnelAttachmentFile" />
                </td>
				<td>
					<div align=center>
						<html:image property="methodToCall.addPersonnelAttachment.anchor${tabKey}"
						src='${ConfigProperties.kra.externalizable.images.url}tinybutton-add1.gif' />
					</div>
                </td>
            </tr>

        	<c:forEach var="propPersonBio" items="${KualiForm.document.propPersonBios}" varStatus="status">
	             <tr>
					<th class="infoline">
						${status.index+1}
					</th>
	                <td align="left" valign="middle">
                	    <kul:htmlControlAttribute property="document.propPersonBios[${status.index}].updateTimestamp" readOnly="true" attributeEntry="${propPersonBioAttributes.updateTimestamp}" />
					</td>
	                <td>
                	    <kul:htmlControlAttribute property="document.propPersonBios[${status.index}].updateUser" readOnly="true" attributeEntry="${propPersonBioAttributes.updateUser}" />
                	    ${propPersonBio.authorPersonName}
	                </td>
	                <td>   
	                    ${propPersonBio.propPerDocType.description}           	
	                    <!-- <kul:htmlControlAttribute property="document.propPersonBios[${status.index}].propPerDocType.description" attributeEntry="${propPersonBioAttributes.propPerDocType.description}" /> -->  
					</td>
	                <td align="left" valign="middle">
        			   <input type="hidden" name="document.propPersonBios[${status.index}].personId" value="000000001"> 
	                	<!--<kul:htmlControlAttribute property="document.propPersonBios[${status.index}].personId" readOnly="true" attributeEntry="${propPersonBioAttributes.personId}" /> -->
	                </td>
	                <td align="left" valign="middle">
	                	<kul:htmlControlAttribute property="document.propPersonBios[${status.index}].description" readOnly="true" attributeEntry="${propPersonBioAttributes.description}" />
					</td>
	                <td align="left" valign="middle">
	                    <kul:htmlControlAttribute property="document.propPersonBios[${status.index}].fileName" readOnly="true" attributeEntry="${propPersonBioAttributes.fileName}" />
	                </td>
	                <td align="left" valign="middle">
					<div align=center>
						<html:image property="methodToCall.viewAttachmentFile.line${status.index}.anchor${currentTabIndex}"
									src='${ConfigProperties.kra.externalizable.images.url}tinybutton-view.gif' 
									onclick="javascript: openNewWindow('proposalDevelopmentAbstractsAttachments','viewPersonnelAttachment',${status.index}); return false" />
						<html:image property="methodToCall.deletePersonnelAttachment.line${status.index}.anchor${currentTabIndex}"
							src='${ConfigProperties.kra.externalizable.images.url}tinybutton-delete1.gif' />
					</div>
	                </td>
	            </tr>
        	</c:forEach>        

          	
        </table>
    </div>
</kul:tab>
