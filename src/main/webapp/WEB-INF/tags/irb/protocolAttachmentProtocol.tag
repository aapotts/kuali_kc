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
<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<c:set var="protocolAttachmentProtocolAttributes" value="${DataDictionary.ProtocolAttachmentProtocol.attributes}" />
<c:set var="notesAndAttachmentsHelper" value="${KualiForm.notesAndAttachmentsHelper}" />
<c:set var="readOnly" value="${!KualiForm.notesAndAttachmentsHelper.modifyProtocol}" />
<c:set var="action" value="protocolNoteAndAttachment" />
<c:set var="attachmentProtocols" value="${KualiForm.document.protocolList[0].attachmentProtocols}"/>

<kul:tab tabTitle="Protocol Attachments(${fn:length(KualiForm.document.protocolList[0].attachmentProtocols)})" defaultOpen="false" tabErrorKey="notesAndAttachmentsHelper.newAttachmentProtocol.*" transparentBackground="true" tabAuditKey="document.protocolList[0].attachmentProtocol*">
	<div class="tab-container" align="center">
   		<h3>
   			<span class="subhead-left">Add Protocol Attachment</span>
   			<span class="subhead-right"><kul:help businessObjectClassName="org.kuali.kra.bo.ProtocolAttachmentProtocol" altText="help"/></span>
       </h3>
       <table cellpadding="4" cellspacing="0" summary="">
         	<tr>
         		<th>
         			<div align="right">
         				<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes['type.code']}" noColon="false"/>
         			</div>
         		</th>
         		<td align="left" valign="middle" class="infoline">
                	<div align="left">
                		<c:set var="property" value="notesAndAttachmentsHelper.newAttachmentProtocol.type.code" />
                	
               			<%-- attachment type finder logic start--%>
							<jsp:useBean id="typeParamsType" class="java.util.HashMap"/>
							<c:set target="${typeParamsType}" property="groupCode" value="${notesAndAttachmentsHelper.newAttachmentProtocol.groupCode}" />
							<c:set var="options" value="${krafn:getOptionList('org.kuali.kra.irb.noteattachment.ProtocolAttachmentTypeByGroupValuesFinder', typeParamsType)}" />
						<%-- attachment type finder logic end --%>
						
               			<%-- attachment type error handling logic start--%>
               				<kul:checkErrors keyMatch="${property}" auditMatch="${property}"/>
               				<c:set var="textStyle" value="${hasErrors == true ? 'background-color:#FFD5D5' : ''}"/>
               			<%-- attachment type error handling logic start--%>
               			
               			<html:select property="${property}" style="${textStyle}">
               				<html:options collection="options" labelProperty="label" property="key" />
               			</html:select>
	            	</div>
				</td>
				<th>
					<div align="right">
						<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes['file.id']}" noColon="false" />
					</div>
				</th>
       			<td align="left" valign="middle" class="infoline">
              		<div align="left">
              			<c:set var="property" value="notesAndAttachmentsHelper.newAttachmentProtocol.newFile" />
              		
              		    <%-- attachment file error handling logic start--%>
               				<kul:checkErrors keyMatch="${property}" auditMatch="${property}"/>
               				<%-- highlighting does not work in firefox but does in ie... --%>
               				<c:set var="textStyle" value="${hasErrors == true ? 'background-color:#FFD5D5' : ''}"/>
               			<%-- attachment file error handling logic start--%>
              		
              			<html:file property="${property}" style="${textStyle}"/>
           			</div>
				</td>
         	</tr>
         	<tr>
         		<th>
         			<div align="right">
         				<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes['status.code']}" noColon="false"/>
         			</div>
         		</th>
         		<td align="left" valign="middle" class="infoline">
                	<div align="left">
                		<kul:htmlControlAttribute property="notesAndAttachmentsHelper.newAttachmentProtocol.status.code" attributeEntry="${protocolAttachmentProtocolAttributes['status.code']}"/>
	            	</div>
				</td>
				<th>
					<div align="right">
						<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.contactName}" noColon="false" />
					</div>
				</th>
         		<td align="left" valign="middle" class="infoline">
                	<div align="left">
                		<kul:htmlControlAttribute property="notesAndAttachmentsHelper.newAttachmentProtocol.contactName" attributeEntry="${protocolAttachmentProtocolAttributes.contactName}"/>
	            	</div>
				</td>
         	</tr>
         	<tr>
         		<th>
         			<div align="right">
         				<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.updateUser}" noColon="false" />
         			</div>
         		</th>
         		<td align="left" valign="middle" class="infoline">
                	<div align="left">
                		<kul:htmlControlAttribute property="notesAndAttachmentsHelper.newAttachmentProtocol.updateUser" attributeEntry="${protocolAttachmentProtocolAttributes.updateUser}" readOnly="true"/>
	            	</div>
				</td>
				<th>
					<div align="right">
						<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.contactEmailAddress}" noColon="false" />
					</div>
				</th>
         			<td align="left" valign="middle" class="infoline">
                	<div align="left">
                		<kul:htmlControlAttribute property="notesAndAttachmentsHelper.newAttachmentProtocol.contactEmailAddress" attributeEntry="${protocolAttachmentProtocolAttributes.contactEmailAddress}"/>
	            	</div>
				</td>
         	</tr>
         	<tr>
         		<th>
         			<div align="right">
         				<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.updateTimestamp}" noColon="false" />
         			</div>
         		</th>
         		<td align="left" valign="middle" class="infoline">
                	<div align="left">
                		<kul:htmlControlAttribute property="notesAndAttachmentsHelper.newAttachmentProtocol.updateTimestamp" attributeEntry="${protocolAttachmentProtocolAttributes.updateTimestamp}" readOnly="true"/>
	            	</div>
				</td>
				<th>
					<div align="right">
						<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.contactPhoneNumber}" noColon="false" />
					</div>
				</th>
         		<td align="left" valign="middle" class="infoline">
                	<div align="left">
                		<kul:htmlControlAttribute property="notesAndAttachmentsHelper.newAttachmentProtocol.contactPhoneNumber" attributeEntry="${protocolAttachmentProtocolAttributes.contactPhoneNumber}"/>
	            	</div>
				</td>
         	</tr>
         	<tr>
         		<th>
         			<div align="right">
         				<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.comments}" noColon="false" />
         			</div>
         		</th>
         		<td align="left" valign="middle" class="infoline">
                	<div align="left">
                		<kul:htmlControlAttribute property="notesAndAttachmentsHelper.newAttachmentProtocol.comments" attributeEntry="${protocolAttachmentProtocolAttributes.comments}"/>
                		<kra:expandedTextArea textAreaFieldName="notesAndAttachmentsHelper.newAttachmentProtocol.comments" action="${action}" textAreaLabel="${protocolAttachmentProtocolAttributes.comments.label}" />
	            	</div>
				</td>
				<th>
					<div align="right">
						<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.description}" noColon="false"/>
					</div>
				</th>
         		<td align="left" valign="middle" class="infoline">
                	<div align="left">
                		<kul:htmlControlAttribute property="notesAndAttachmentsHelper.newAttachmentProtocol.description" attributeEntry="${protocolAttachmentProtocolAttributes.description}"/>
                		<kra:expandedTextArea textAreaFieldName="notesAndAttachmentsHelper.newAttachmentProtocol.description" action="${action}" textAreaLabel="${protocolAttachmentProtocolAttributes.description.label}" />
	            	</div>
				</td>
         	</tr>
         	<tr>
         		<td colspan="4" class="infoline">
					<div align="center">
						<html:image property="methodToCall.addAttachmentProtocol.anchor${tabKey}"
						src="${ConfigProperties.kra.externalizable.images.url}tinybutton-add1.gif" styleClass="tinybutton"/>
					</div>
				</td>
         	</tr>
		</table>
		
		<c:forEach var="attachmentProtocol" items="${KualiForm.document.protocolList[0].attachmentProtocols}" varStatus="itrStatus">
			<kul:innerTab tabTitle="${attachmentProtocol.type.description} - ${attachmentProtocol.status.description}" parentTab="Protocol Attachments(${size})" defaultOpen="false" tabErrorKey="document.protocolList[0].attachmentProtocols[${itrStatus.index}]*" useCurrentTabIndexAsKey="true" tabAuditKey="document.protocolList[0].attachmentProtocol[${itrStatus.index}]*" auditCluster="NoteAndAttachmentAuditErrors">
				<div class="innerTab-container" align="left">
            		<table class=tab cellpadding=0 cellspacing="0" summary="">
						<tr>
			         		<th>
			         			<div align="right">
			         				<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes['type.code']}" noColon="false" />
			         			</div>
			         		</th>
			         		<td align="left" valign="middle" class="infoline">
			                	<div align="left">
			                		<kul:htmlControlAttribute property="document.protocolList[0].attachmentProtocol[${itrStatus.index}].type.code" attributeEntry="${protocolAttachmentProtocolAttributes['type.code']}" readOnly="true" readOnlyAlternateDisplay="${attachmentProtocol.type.description}" />
				            	</div>
							</td>
							<th>
								<div align="right">
									<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes['file.id']}" noColon="false" />
								</div>
							</th>
			       			<td align="left" valign="middle" class="infoline">
			              		<div align="left" style="display: none;" id="attachmentProtocolFile${itrStatus.index}">
			              			<html:file property="document.protocolList[0].attachmentProtocol[${itrStatus.index}].newFile" />
			           			</div>
			           			<div align="left" id="attachmentProtocolFileName${itrStatus.index}">
			              			${attachmentProtocol.file.name}
			           			</div>
							</td>
			         	</tr>
			         	<tr>
			         		<th>
			         			<div align="right">
			         				<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes['status.code']}" noColon="false"/>
			         			</div>
			         		</th>
			         		<td align="left" valign="middle" class="infoline">
			                	<div align="left">
			                		<kul:htmlControlAttribute property="document.protocolList[0].attachmentProtocol[${itrStatus.index}].status.code" attributeEntry="${protocolAttachmentProtocolAttributes['status.code']}"/>
				            	</div>
							</td>
							<th>
								<div align="right">
									<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.contactName}" noColon="false" />
								</div>
							</th>
			         		<td align="left" valign="middle" class="infoline">
			                	<div align="left">
			                		<kul:htmlControlAttribute property="document.protocolList[0].attachmentProtocol[${itrStatus.index}].contactName" attributeEntry="${protocolAttachmentProtocolAttributes.contactName}"/>
				            	</div>
							</td>
			         	</tr>
			         	<tr>
			         		<th>
			         			<div align="right">
			         				<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.updateUser}" noColon="false" />
			         			</div>
			         		</th>
			         		<td align="left" valign="middle" class="infoline">
			                	<div align="left">
			                		<kul:htmlControlAttribute property="document.protocolList[0].attachmentProtocol[${itrStatus.index}].updateUser" attributeEntry="${protocolAttachmentProtocolAttributes.updateUser}" readOnly="true"/>
				            	</div>
							</td>
							<th>
								<div align="right">
									<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.contactEmailAddress}" noColon="false" />
								</div>
							</th>
			         			<td align="left" valign="middle" class="infoline">
			                	<div align="left">
			                		<kul:htmlControlAttribute property="document.protocolList[0].attachmentProtocol[${itrStatus.index}].contactEmailAddress" attributeEntry="${protocolAttachmentProtocolAttributes.contactEmailAddress}"/>
				            	</div>
							</td>
			         	</tr>
			         	<tr>
			         		<th>
			         			<div align="right">
			         				<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.updateTimestamp}" noColon="false" />
			         			</div>
			         		</th>
			         		<td align="left" valign="middle" class="infoline">
			                	<div align="left">
			                		<kul:htmlControlAttribute property="document.protocolList[0].attachmentProtocol[${itrStatus.index}].updateTimestamp" attributeEntry="${protocolAttachmentProtocolAttributes.updateTimestamp}" readOnly="true"/>
				            	</div>
							</td>
							<th>
								<div align="right">
									<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.contactPhoneNumber}" noColon="false" />
								</div>
							</th>
			         		<td align="left" valign="middle" class="infoline">
			                	<div align="left">
			                		<kul:htmlControlAttribute property="document.protocolList[0].attachmentProtocol[${itrStatus.index}].contactPhoneNumber" attributeEntry="${protocolAttachmentProtocolAttributes.contactPhoneNumber}"/>
				            	</div>
							</td>
			         	</tr>
			         	<tr>
			         		<th>
			         			<div align="right">
			         				<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.comments}" noColon="false" />
			         			</div>
			         		</th>
			         		<td align="left" valign="middle" class="infoline">
			                	<div align="left">
			                		<kul:htmlControlAttribute property="document.protocolList[0].attachmentProtocol[${itrStatus.index}].comments" attributeEntry="${protocolAttachmentProtocolAttributes.comments}"/>
			                		<kra:expandedTextArea textAreaFieldName="document.protocolList[0].attachmentProtocol[${itrStatus.index}].comments" action="${action}" textAreaLabel="${protocolAttachmentProtocolAttributes.comments.label}" />
				            	</div>
							</td>
							<th>
								<div align="right">
									<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.description}" noColon="false"/>
								</div>
							</th>
			         		<td align="left" valign="middle" class="infoline">
			                	<div align="left">
			                		<kul:htmlControlAttribute property="document.protocolList[0].attachmentProtocol[${itrStatus.index}].description" attributeEntry="${protocolAttachmentProtocolAttributes.description}"/>
			                		<kra:expandedTextArea textAreaFieldName="document.protocolList[0].attachmentProtocol[${itrStatus.index}].description" action="${action}" textAreaLabel="${protocolAttachmentProtocolAttributes.description.label}" />
				            	</div>
							</td>
			         	</tr>
						<tr>
			         		<td colspan="4" class="infoline">
								<div align="center">
									<html:image property="methodToCall.viewAttachmentProtocol.line${itrStatus.index}.anchor${currentTabIndex}"
										src='${ConfigProperties.kra.externalizable.images.url}tinybutton-view.gif' styleClass="tinybutton"
										alt="View Protocol Attachment" onclick="excludeSubmitRestriction = true;"/>
									<input class="tinybutton" type="image" alt="Replace"
										src='${ConfigProperties.kra.externalizable.images.url}tinybutton-replace.gif' class="tinybutton"
										alt="Replace Protocol Attachment"
										onclick="document.getElementById('attachmentProtocolFile${itrStatus.index}').style.display = 'block';
										document.getElementById('attachmentProtocolFileName${itrStatus.index}').style.display = 'none';
										return false;"/>
									<html:image property="methodToCall.deleteAttachmentProtocol.line${itrStatus.index}.anchor${currentTabIndex}"
										src='${ConfigProperties.kra.externalizable.images.url}tinybutton-delete1.gif' styleClass="tinybutton"
										alt="Delete Protocol Attachment"/>
								</div>
							</td>
			         	</tr>
         			</table>
         		</div>
         	</kul:innerTab>
		</c:forEach>
     </div>		
</kul:tab>