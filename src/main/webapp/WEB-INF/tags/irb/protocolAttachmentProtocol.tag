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

<c:set var="protocolAttachmentProtocolAttributes" value="${DataDictionary.ProtocolAttachmentProtocol.attributes}" />
<c:set var="attachmentFileAttributes" value="${DataDictionary.AttachmentFile.attributes}" />
<c:set var="attachmentsHelper" value="${KualiForm.attachmentsHelper}" />
<c:set var="modify" value="${KualiForm.attachmentsHelper.modifyAttachments}" />
<c:set var="action" value="protocolNoteAndAttachment" />
<c:set var="attachmentProtocols" value="${KualiForm.document.protocolList[0].attachmentProtocols}"/>

<kul:tab tabTitle="Protocol Attachments" tabItemCount="${fn:length(KualiForm.document.protocolList[0].activeAttachmentProtocols)}" defaultOpen="false" tabErrorKey="attachmentsHelper.newAttachmentProtocol.*" transparentBackground="true" tabAuditKey="document.protocolList[0].attachmentProtocols*">
	<div class="tab-container" align="center">
   		<kra:permission value="${modify}">
	   		<h3>
	   			<span class="subhead-left">Add Protocol Attachment</span>
	   			<span class="subhead-right"><kul:help businessObjectClassName="org.kuali.kra.irb.noteattachment.ProtocolAttachmentProtocol" altText="help"/></span>
	       </h3>
	       <table cellpadding="4" cellspacing="0" summary="">
	         	<tr>
	         		<th>
	         			<div align="right">
	         				<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes['typeCode']}" noColon="false"/>
	         			</div>
	         		</th>
	         		<td align="left" valign="middle" colspan="3">
	                	<div align="left">
	                		<c:set var="property" value="attachmentsHelper.newAttachmentProtocol.typeCode" />
	                	
	               			<%-- attachment type finder logic start--%>
								<jsp:useBean id="typeParamsType" class="java.util.HashMap"/>
								<c:set target="${typeParamsType}" property="groupCode" value="${attachmentsHelper.newAttachmentProtocol.groupCode}" />
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
	         	</tr>
	         	<tr>
	         		<th>
	         			<div align="right">
	         				<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes['statusCode']}" noColon="false"/>
	         			</div>
	         		</th>
	         		<td align="left" valign="middle">
	                	<div align="left">
	                		<kul:htmlControlAttribute property="attachmentsHelper.newAttachmentProtocol.statusCode" attributeEntry="${protocolAttachmentProtocolAttributes['statusCode']}"/>
		            	</div>
					</td>
					<th>
						<div align="right">
							<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.contactName}" noColon="false" />
						</div>
					</th>
	         		<td align="left" valign="middle">
	                	<div align="left">
	                		<kul:htmlControlAttribute property="attachmentsHelper.newAttachmentProtocol.contactName" attributeEntry="${protocolAttachmentProtocolAttributes.contactName}"/>
		            	</div>
					</td>
	         	</tr>
	         	<tr>
	         		<th>
	         			<div align="right">
	         				<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.updateUser}" noColon="false" />
	         			</div>
	         		</th>
	         		<td align="left" valign="middle">
	                	<div align="left">
	                		<kul:htmlControlAttribute property="attachmentsHelper.newAttachmentProtocol.updateUser" attributeEntry="${protocolAttachmentProtocolAttributes.updateUser}" readOnly="true"/>
		            	</div>
					</td>
					<th>
						<div align="right">
							<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.contactEmailAddress}" noColon="false" />
						</div>
					</th>
	         			<td align="left" valign="middle">
	                	<div align="left">
	                		<kul:htmlControlAttribute property="attachmentsHelper.newAttachmentProtocol.contactEmailAddress" attributeEntry="${protocolAttachmentProtocolAttributes.contactEmailAddress}"/>
		            	</div>
					</td>
	         	</tr>
	         	<tr>
	         		<th>
	         			<div align="right">
	         				<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.updateTimestamp}" noColon="false" />
	         			</div>
	         		</th>
	         		<td align="left" valign="middle">
	                	<div align="left">
	                		<kul:htmlControlAttribute property="attachmentsHelper.newAttachmentProtocol.updateTimestamp" attributeEntry="${protocolAttachmentProtocolAttributes.updateTimestamp}" readOnly="true"/>
		            	</div>
					</td>
					<th>
						<div align="right">
							<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.contactPhoneNumber}" noColon="false" />
						</div>
					</th>
	         		<td align="left" valign="middle">
	                	<div align="left">
	                		<kul:htmlControlAttribute property="attachmentsHelper.newAttachmentProtocol.contactPhoneNumber" attributeEntry="${protocolAttachmentProtocolAttributes.contactPhoneNumber}"/>
		            	</div>
					</td>
	         	</tr>
	         	<tr>
	         		<th>
	         			<div align="right">
	         				<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.comments}" noColon="false" />
	         			</div>
	         		</th>
	         		<td align="left" valign="middle">
	                	<div align="left">
	                		<kul:htmlControlAttribute property="attachmentsHelper.newAttachmentProtocol.comments" attributeEntry="${protocolAttachmentProtocolAttributes.comments}"/>
		            	</div>
					</td>
					<th>
						<div align="right">
							<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.description}" noColon="false"/>
						</div>
					</th>
	         		<td align="left" valign="middle">
	                	<div align="left">
	                		<kul:htmlControlAttribute property="attachmentsHelper.newAttachmentProtocol.description" attributeEntry="${protocolAttachmentProtocolAttributes.description}"/>
		            	</div>
					</td>
	         	</tr>
	         	<tr>
	         		<th>
						<div align="right">
							<kul:htmlAttributeLabel attributeEntry="${attachmentFileAttributes['name']}" noColon="false" />
						</div>
					</th>
	       			<td align="left" valign="middle" colspan="3">
	              		<div align="left">
	              			<c:set var="property" value="attachmentsHelper.newAttachmentProtocol.newFile" />
	              		
	              		    <%-- attachment file error handling logic start--%>
	               				<kul:checkErrors keyMatch="${property}" auditMatch="${property}"/>
	               				<%-- highlighting does not work in firefox but does in ie... --%>
	               				<c:set var="textStyle" value="${hasErrors == true ? 'background-color:#FFD5D5' : ''}"/>
	               			<%-- attachment file error handling logic start--%>
	              		
	              			<html:file property="${property}" style="${textStyle}" size="50"/>
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
		</kra:permission>
		
		<c:forEach var="attachmentProtocol" items="${attachmentProtocols}" varStatus="itrStatus">
		  <c:choose>
		    <c:when test="${attachmentProtocol.active}">
		             <c:set var="modify" value="${KualiForm.attachmentsHelper.modifyAttachments and attachmentProtocol.documentStatusCode != '3'}" />
		    
		    			<kra:innerTab tabTitle="${attachmentProtocol.type.description} - ${attachmentProtocol.status.description}" parentTab="Protocol Attachments(${size})" defaultOpen="false" tabErrorKey="document.protocolList[0].attachmentProtocols[${itrStatus.index}]*,document.protocolList[0].attachmentProtocols[${itrStatus.index}]*" useCurrentTabIndexAsKey="true" tabAuditKey="document.protocolList[0].attachmentProtocols[${itrStatus.index}]*" auditCluster="NoteAndAttachmentAuditErrors">
				<div class="innerTab-container" align="left">
            		<table class=tab cellpadding=0 cellspacing="0" summary="">
						<tr>
			         		<th>
			         			<div align="right">
			         				<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes['typeCode']}" noColon="false" />
			         			</div>
			         		</th>
			         		<td align="left" valign="middle" colspan="3">
			                	<div align="left">
			                		<kul:htmlControlAttribute property="document.protocolList[0].attachmentProtocols[${itrStatus.index}].typeCode" attributeEntry="${protocolAttachmentProtocolAttributes['typeCode']}" readOnly="true" readOnlyAlternateDisplay ="${attachmentProtocol.type.description}" />
				            	</div>
							</td>
			         	</tr>
			         	<tr>
			         		<th>
			         			<div align="right">
			         				<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes['statusCode']}" noColon="false"/>
			         			</div>
			         		</th>
			         		<td align="left" valign="middle">
			                	<div align="left">
			                		<kul:htmlControlAttribute property="document.protocolList[0].attachmentProtocols[${itrStatus.index}].statusCode" attributeEntry="${protocolAttachmentProtocolAttributes['statusCode']}" readOnly="${!modify}"/>
				            	</div>
							</td>
							<th>
								<div align="right">
									<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.contactName}" noColon="false" />
								</div>
							</th>
			         		<td align="left" valign="middle">
			                	<div align="left">
			                		<kul:htmlControlAttribute property="document.protocolList[0].attachmentProtocols[${itrStatus.index}].contactName" attributeEntry="${protocolAttachmentProtocolAttributes.contactName}" readOnly="${!modify}"/>
				            	</div>
							</td>
			         	</tr>
			         	<tr>
			         		<th>
			         			<div align="right">
			         				<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.updateUser}" noColon="false" />
			         			</div>
			         		</th>
			         		<td align="left" valign="middle">
			                	<div align="left">
			                		<kul:htmlControlAttribute property="document.protocolList[0].attachmentProtocols[${itrStatus.index}].updateUser" attributeEntry="${protocolAttachmentProtocolAttributes.updateUser}" readOnly="true"/>
				            	</div>
							</td>
							<th>
								<div align="right">
									<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.contactEmailAddress}" noColon="false" />
								</div>
							</th>
			         			<td align="left" valign="middle">
			                	<div align="left">
			                		<kul:htmlControlAttribute property="document.protocolList[0].attachmentProtocols[${itrStatus.index}].contactEmailAddress" attributeEntry="${protocolAttachmentProtocolAttributes.contactEmailAddress}" readOnly="${!modify}"/>
				            	</div>
							</td>
			         	</tr>
			         	<tr>
			         		<th>
			         			<div align="right">
			         				<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.updateTimestamp}" noColon="false" />
			         			</div>
			         		</th>
			         		<td align="left" valign="middle">
			                	<div align="left">
			                	 	     <kul:htmlControlAttribute property="document.protocolList[0].attachmentProtocols[${itrStatus.index}].updateTimestamp" attributeEntry="${protocolAttachmentProtocolAttributes.updateTimestamp}" readOnly="true"/>  
				            	</div>
							</td>
							<th>
								<div align="right">
									<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.contactPhoneNumber}" noColon="false" />
								</div>
							</th>
			         		<td align="left" valign="middle">
			                	<div align="left">
			                		<kul:htmlControlAttribute property="document.protocolList[0].attachmentProtocols[${itrStatus.index}].contactPhoneNumber" attributeEntry="${protocolAttachmentProtocolAttributes.contactPhoneNumber}" readOnly="${!modify}"/>
				            	</div>
							</td>
			         	</tr>
			         	<tr>
			         		<th>
			         			<div align="right">
			         				<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.comments}" noColon="false" />
			         			</div>
			         		</th>
			         		<td align="left" valign="middle">
			                	<div align="left">
			                		<kul:htmlControlAttribute property="document.protocolList[0].attachmentProtocols[${itrStatus.index}].comments" attributeEntry="${protocolAttachmentProtocolAttributes.comments}" readOnly="${!modify}"/>
				            	</div>
							</td>
							<th>
								<div align="right">
									<kul:htmlAttributeLabel attributeEntry="${protocolAttachmentProtocolAttributes.description}" noColon="false"/>
								</div>
							</th>
			         		<td align="left" valign="middle">
			                	<div align="left">
			                		<kul:htmlControlAttribute property="document.protocolList[0].attachmentProtocols[${itrStatus.index}].description" attributeEntry="${protocolAttachmentProtocolAttributes.description}" readOnly="${!modify}"/>
				            	</div>
							</td>
			         	</tr>
			         	<tr>
			         	<th>
								<div align="right">
									<kul:htmlAttributeLabel attributeEntry="${attachmentFileAttributes['name']}" noColon="false" />
								</div>
							</th>
			       			<td align="left" valign="middle" colspan="3">
			              		<div align="left" style="display: none;" id="attachmentProtocolFile${itrStatus.index}">
			              			<html:file property="document.protocolList[0].attachmentProtocols[${itrStatus.index}].newFile" size="50" />
			           			</div>
			           			<div align="left" id="attachmentProtocolFileName${itrStatus.index}">
			           			   <c:if test="${attachmentProtocol.documentStatusCode == '3'}">
			           			      <font color="red">Deleted -&nbsp;</font>
			           			   </c:if>
			              			${attachmentProtocol.file.name}
			           			</div>
			           			
			           			<%-- this assumes that the versions collection is sorted descending by sequence number --%>
			           			<c:set var="doVersionsExist" value="${fn:length(attachmentProtocol.versions) > 0}" />
			           			<c:if test="${doVersionsExist}">
				           			<kra:innerTab tabTitle="File Versions" parentTab="${attachmentProtocol.type.description} - ${attachmentProtocol.status.description} - ${itrStatus.index}" defaultOpen="false">
										<div class="innerTab-container" align="left">
				         					<table class=tab cellpadding=0 cellspacing="0" summary="" width="100%">
			         							<tr>
					         						<th style="width: 20%">
					         							Last Modifier
					         						</th>
					         						<th style="width: 20%">
					         							Created Date
					         						</th>
					         						<th style="width: 20%">
					         							Last Modified Date
					         						</th>
					         						<th style="width: 60%">
					         							Comments
					         						</th>
					         					</tr>
							         			<c:forEach var="attachmentProtocolVersion" items="${attachmentProtocol.versions}" varStatus="innerItrStatus">
						         					<tr>
						         						<td style="width: 20%">
						         							${attachmentProtocolVersion.authorPersonName}
						         						</td>
						         						<td style="width: 20%">
	                                                       <fmt:formatDate value="${attachmentProtocolVersion.createTimestamp}" pattern="MM/dd/yyyy HH:mm" />
						         							
						         						</td>
						         						<td style="width: 20%">
	                                                       <fmt:formatDate value="${attachmentProtocolVersion.updateTimestamp}" pattern="MM/dd/yyyy HH:mm" />
						         							
						         						</td>
														<td style="width: 60%">
														   <div align="left">
						         							${attachmentProtocolVersion.comments}
						         							</div>
						         						</td>
						         					</tr>
							         			</c:forEach>
						         			</table>
					         			</div>
				         			</kra:innerTab>
				         		</c:if>
							</td>
			         	</tr>
						<tr>
			         		<td colspan="4" class="infoline">
								<div align="center">
									<html:image property="methodToCall.viewAttachmentProtocol.line${itrStatus.index}.anchor${currentTabIndex}"
										src='${ConfigProperties.kra.externalizable.images.url}tinybutton-view.gif' styleClass="tinybutton"
										alt="View Protocol Attachment" onclick="excludeSubmitRestriction = true;"/>
									<kra:permission value="${KualiForm.attachmentsHelper.modifyAttachments}">
										<input class="tinybutton" type="image"
											src='${ConfigProperties.kra.externalizable.images.url}tinybutton-replace.gif'
											alt="Replace Protocol Attachment"
											onclick="document.getElementById('attachmentProtocolFile${itrStatus.index}').style.display = 'block';
											document.getElementById('attachmentProtocolFileName${itrStatus.index}').style.display = 'none';
											return false;"/>
									    <c:if test="${modify}">
										    <html:image property="methodToCall.deleteAttachmentProtocol.line${itrStatus.index}.anchor${currentTabIndex}"
											    src='${ConfigProperties.kra.externalizable.images.url}tinybutton-delete1.gif' styleClass="tinybutton"
											    alt="Delete Protocol Attachment"/>
			           			        </c:if>											
									</kra:permission>
								</div>
							</td>
			         	</tr>
         			</table>
         		</div>
         	</kra:innerTab>
		    
		    
		    </c:when>
		    <c:otherwise>
		      <html:hidden property="document.protocolList[0].attachmentProtocols[${itrStatus.index}].typeCode" value="${KualiForm.document.protocolList[0].attachmentProtocols[itrStatus.index].typeCode}" />
		      <html:hidden property="document.protocolList[0].attachmentProtocols[${itrStatus.index}].statusCode" value="${KualiForm.document.protocolList[0].attachmentProtocols[itrStatus.index].statusCode}" />
		      <html:hidden property="document.protocolList[0].attachmentProtocols[${itrStatus.index}].contactName" value="${KualiForm.document.protocolList[0].attachmentProtocols[itrStatus.index].contactName}" />
		      <html:hidden property="document.protocolList[0].attachmentProtocols[${itrStatus.index}].contactEmailAddress" value="${KualiForm.document.protocolList[0].attachmentProtocols[itrStatus.index].contactEmailAddress}" />
		      <html:hidden property="document.protocolList[0].attachmentProtocols[${itrStatus.index}].contactPhoneNumber" value="${KualiForm.document.protocolList[0].attachmentProtocols[itrStatus.index].contactPhoneNumber}" />
		      <html:hidden property="document.protocolList[0].attachmentProtocols[${itrStatus.index}].comments" value="${KualiForm.document.protocolList[0].attachmentProtocols[itrStatus.index].comments}" />
		      <html:hidden property="document.protocolList[0].attachmentProtocols[${itrStatus.index}].description" value="${KualiForm.document.protocolList[0].attachmentProtocols[itrStatus.index].description}" />
		      <html:hidden property="document.protocolList[0].attachmentProtocols[${itrStatus.index}].file.name" value="${KualiForm.document.protocolList[0].attachmentProtocols[itrStatus.index].file.name}" />
		    </c:otherwise>
		  </c:choose>
		</c:forEach>
     </div>		
</kul:tab>
