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

<c:set var="attributes" value="${DataDictionary.ProtocolApproveBean.attributes}" />
<c:set var="action" value="protocolProtocolActions" />
<c:set var="openForFollowup" value="${KualiForm.actionHelper.isApproveOpenForFollowup}" />
<c:set var="datesReadOnly" value="${KualiForm.actionHelper.protocol.amendment and not KualiForm.actionHelper.protocol.renewal}" />

<kra:permission value="${KualiForm.actionHelper.canApprove}">

<kul:innerTab tabTitle="Approve Action" parentTab="" defaultOpen="${openForFollowup}" tabErrorKey="actionHelper.protocolApproveBean*">
   
   <kra-irb-action:padLeft>
        <table class="tab" cellpadding="0" cellspacing="0" summary=""> 
            <tbody>
            
                <tr>
                    <th width="15%"> 
                        <div align="right">
                            <nobr>
                                <kul:htmlAttributeLabel attributeEntry="${attributes.approvalDate}" />
                            </nobr>
                        </div>
                    </th>
                    <td>
                        <nobr>
                            <kul:htmlControlAttribute property="actionHelper.protocolApproveBean.approvalDate" 
                                                      attributeEntry="${attributes.approvalDate}" 
                                                      readOnly="${datesReadOnly}" />
                        </nobr>
                    </td>
                </tr>
                
                <tr>
                    <th width="15%"> 
                        <div align="right">
                            <nobr>
                                <kul:htmlAttributeLabel attributeEntry="${attributes.expirationDate}" />
                            </nobr>
                        </div>
                    </th>
                    <td>
                        <nobr>
                            <kul:htmlControlAttribute property="actionHelper.protocolApproveBean.expirationDate" 
                                                      attributeEntry="${attributes.expirationDate}" 
                                                      readOnly="${datesReadOnly}" />
                        </nobr>
                    </td>
                </tr>
                
                <tr>
                    <th width="15%"> 
                        <div align="right">
                            <nobr>
                                <kul:htmlAttributeLabel attributeEntry="${attributes.comments}" />
                            </nobr>
                        </div>
                    </th>
                    <td>
                        <nobr>
                            <kul:htmlControlAttribute property="actionHelper.protocolApproveBean.comments" attributeEntry="${attributes.comments}" />
                        </nobr>
                    </td>
                </tr>
                
                <tr>
                    <th width="15%"> 
                        <div align="right">
                            <nobr>
                                <kul:htmlAttributeLabel attributeEntry="${attributes.actionDate}" />
                            </nobr>
                        </div>
                    </th>
                    <td>
                        <nobr>
                            <kul:htmlControlAttribute property="actionHelper.protocolApproveBean.actionDate" attributeEntry="${attributes.actionDate}"  />
                        </nobr>
                    </td>
                </tr>
                
                <tr>
                    <td colspan="2">
                        <kra-irb-action:riskLevel bean="${KualiForm.actionHelper.protocolApproveBean.protocolRiskLevelBean}"
                                                  property="actionHelper.protocolApproveBean.protocolRiskLevelBean"
                                                  action="${action}" 
                                                  actionName="Approve" />
                    </td>
                </tr>
                
                <tr>
                    <td colspan="2">
                        <kra-irb-action:reviewComments bean="${KualiForm.actionHelper.protocolApproveBean.reviewCommentsBean}"
                                                       property="actionHelper.protocolApproveBean.reviewCommentsBean"
                                                       action="${action}"
                                                       actionName="ApproveAction" />
                   </td>
                </tr>
                
                <tr>
                    <td align="center" colspan="2">
                        <div align="center">
                            <html:image property="methodToCall.approveAction.anchor${tabKey}"
                                        src='${ConfigProperties.kra.externalizable.images.url}tinybutton-submit.gif' styleClass="tinybutton"/>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>       
   </kra-irb-action:padLeft>
    
</kul:innerTab>

</kra:permission>
