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

<c:set var="attributes" value="${DataDictionary.ProtocolGenericActionBean.attributes}" />
<c:set var="minutesAttributes" value="${DataDictionary.CommitteeScheduleMinute.attributes}" />
<c:set var="action" value="protocolProtocolActions" />
<c:set var="textCloseComments" value="actionHelper.protocolCloseBean.comments" />

<kra:permission value="${KualiForm.actionHelper.canClose}">

<kra:innerTab tabTitle="Close" parentTab="" defaultOpen="false" tabErrorKey="actionHelper.protocolCloseBean*">
   
    <div style="padding-left: 56px" >
        <table class="tab" cellpadding="0" cellspacing="0" summary=""> 
            <tbody>
                
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
                            <kul:htmlControlAttribute property="actionHelper.protocolCloseBean.comments" attributeEntry="${attributes.comments}" />
                            <kul:expandedTextArea textAreaFieldName="${textCloseComments}" action="${action}" textAreaLabel="${attributes.comments.label}" />
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
                            <kul:htmlControlAttribute property="actionHelper.protocolCloseBean.actionDate" attributeEntry="${attributes.actionDate}" datePicker="true" />
                        </nobr>
                    </td>
                </tr>
                
                <tr>
                    <td colspan="2">
                        <kra-irb-action:reviewComments bean="${KualiForm.actionHelper.protocolCloseBean.reviewComments}"
                                                       property="actionHelper.protocolCloseBean.reviewComments"
                                                       action="${action}"
                                                       actionName="Close" />
                   </td>
                </tr>
                
                <tr>
                    <td align="center" colspan="2">
                        <div align="center">
                            <html:image property="methodToCall.close.anchor${tabKey}"
                                        src='${ConfigProperties.kra.externalizable.images.url}tinybutton-submit.gif' styleClass="tinybutton"/>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>       
    </div>
    
</kra:innerTab>

</kra:permission>
