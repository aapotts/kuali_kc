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

<c:set var="protocolAttachmentPersonnelAttributes" value="${DataDictionary.ProtocolAttachmentPersonnel.attributes}" />
<c:set var="protocolAttachmentPersonnelAttributes" value="${DataDictionary.ProtocolAttachmentPersonnel.attributes}" />
<c:set var="protocolAttachmentTypeAttributes" value="${DataDictionary.ProtocolAttachmentType.attributes}" />
<c:set var="notesAndAttachmentsHelper" value="${KualiForm.notesAndAttachmentsHelper}" />
<c:set var="readOnly" value="${!KualiForm.notesAndAttachmentsHelper.modifyProtocol}" />
<c:set var="action" value="protocolNotesAndAttachments" />
<c:set var="size" value="${empty KualiForm.document.protocol.attachmentPersonnels ? '0' : KualiForm.document.protocol.attachmentPersonnels.size}" />

<kul:tab tabTitle="Personnel Attachments(${size})" defaultOpen="true" tabErrorKey="">
	<div class="tab-container" align="center">
   		<h3>
   			<span class="subhead-left">Add Personnel Attachment</span>
   			<span class="subhead-right"><kul:help businessObjectClassName="org.kuali.kra.bo.ProtocolAttachmentPersonnel" altText="help"/></span>
   		</h3>
	    <table id="protocolAttachmentProtocolTableId" cellpadding="0" cellspacing="0" summary="">
	    	<tr>
	    		<td>&nbsp;</td>
	    	</tr>
	    </table>
	</div>
</kul:tab>