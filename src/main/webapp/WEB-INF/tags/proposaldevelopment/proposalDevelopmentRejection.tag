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

<%--<c:set var="proposalDevelopmentRejectionBeanAttributes" value="${DataDictionary.ProposalDevelopmentRejectionBean.attributes}" />
<c:set var="proposalDevelopmentAttributes" value="${DataDictionary.DevelopmentProposal.attributes}" />
--%>
<c:set var="proposalDevelopmentRejectionBeanAttributes" value="${DataDictionary.ProposalDevelopmentRejectionBean.attributes}" />

<kul:tabTop tabTitle="Proposal Developement Rejection Confirmation" defaultOpen="true" tabErrorKey="ProposalDevelopmentRejectionBean">
	<div class="tab-container" align="center">
	<table cellpadding=0 cellspacing=0 summary="">
		<tr>
			<th colspan="2">Are you sure you want to reject this document?</th>
		</tr>
		<tr>
			<th>
				<kul:htmlAttributeLabel attributeEntry="${proposalDevelopmentRejectionBeanAttributes.rejectReason}"/>
			</th>
			<td>
				<kul:htmlControlAttribute property="proposalDevelopmentRejectionBean.rejectReason" attributeEntry="${proposalDevelopmentRejectionBeanAttributes.rejectReason}" readOnly="${false}" />
			</td>
		</tr>
		<%--<tr>
			<th>
				Attachment:
			</th>
			<td>
				<kra:file property="proposalDevelopmentRejectionBean.rejectFile" />
				
				
				<div id = "templateFileDiv" class="addsection" style="${addStyle}">
	                        <%--<html:file property="templateFile" size="50" onchange="showViewFile(this)"/>
	                        <kra:file property="proposalDevelopmentRejectionBean.rejectFile" />
	                    </div>
                        <div id = "fileNameDiv" class="viewsection" style="${viewStyle}">
	                         ${KualiForm.proposalDevelopmentRejectionBean.rejectFile}
	                    </div>
			</td>
		</tr>--%>
		<tr>
            <td colspan="2" align="center">
            	<html:image property="methodToCall.rejectYes" src="/kc-dev/kr/static/images/buttonsmall_Yes.gif" title="Do Reject" alt="Do Reject" styleClass="tinybutton"/>
            	<html:image property="methodToCall.rejectNo" src="/kc-dev/kr/static/images/buttonsmall_No.gif" title="Dont Reject" alt="Don't Reject" styleClass="tinybutton"/>
	
	                <%--<input type="image" name="methodToCall.rejectYes" src="/kc-dev/kr/static/images/buttonsmall_Yes.gif"/>
	            
	                <input type="image" name="methodToCall.rejectNo" src="/kc-dev/kr/static/images/buttonsmall_No.gif"/>
	                <html:image property="methodToCall.rejectNo" src="${ConfigProperties.kr.externalizable.images.url}tinygrey-cancel.gif" title="Dont Reject" alt="Don't Reject" styleClass="tinybutton"/> --%>
	                
            </td>
        </tr>
	</table>
	</div>
</kul:tabTop>