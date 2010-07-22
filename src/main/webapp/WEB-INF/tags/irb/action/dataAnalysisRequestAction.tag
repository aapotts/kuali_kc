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

<c:set var="attributes" value="${DataDictionary.ProtocolRequestBean.attributes}" />
<c:set var="action" value="protocolProtocolActions" />
                                    
<kra:permission value="${KualiForm.actionHelper.canRequestDataAnalysis}">

<kra:innerTab tabTitle="Request for Data Analysis Only" parentTab="" defaultOpen="false" tabErrorKey="actionHelper.protocolDataAnalysisRequest*">
    <div class="innerTab-container" align="left">
        <table class="tab" cellpadding="0" cellspacing="0" summary="">
            <tbody>
                <tr>
                    <th width="15%"> 
                        <c:if test="${KualiForm.actionHelper.showCommittee}">
	                        <div align="right">
	                            <nobr>
	                            <kul:htmlAttributeLabel attributeEntry="${attributes.committeeId}" />
	                            </nobr>
	                        </div>
                        </c:if>
                    </th>
                    <td>
                        <c:if test="${KualiForm.actionHelper.showCommittee}">
	                        <nobr>
	                        <kul:htmlControlAttribute property="actionHelper.protocolDataAnalysisRequestBean.committeeId" attributeEntry="${attributes.committeeId}" />
	                        </nobr>
                        </c:if>
                    </td>
                    <th>
	          		    <div align="right">
	          		        <kul:htmlAttributeLabel attributeEntry="${attributes.fileName}"/>
	          		    </div>
	          		</th>
	                <td align="left" valign="middle">
	                	<html:file property="actionHelper.protocolDataAnalysisRequestBean.file" />
					</td>
                </tr>
                <tr>
                    <th width="15%"> 
                        <div align="right">
                            <nobr>
                            <kul:htmlAttributeLabel attributeEntry="${attributes.reason}" />
                            </nobr>
                        </div>
                    </th>
                    <td colspan="3">
                        <nobr>
                        <kul:htmlControlAttribute property="actionHelper.protocolDataAnalysisRequestBean.reason" attributeEntry="${attributes.reason}" />
                        </nobr>
                    </td>
                </tr>
                <tr>
					<td align="center" colspan="4">
						<div align="center">
							<html:image property="methodToCall.dataAnalysisOnlyRequestProtocol.anchor${tabKey}"
							            src='${ConfigProperties.kra.externalizable.images.url}tinybutton-submit.gif' styleClass="tinybutton"/>
						</div>
	                </td>
                </tr>
            </tbody>
        </table>
    </div>
    
</kra:innerTab>

</kra:permission>
