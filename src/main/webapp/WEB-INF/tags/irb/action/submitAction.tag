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

<c:set var="attributes" value="${DataDictionary.ProtocolSubmitAction.attributes}" />
<c:set var="action" value="protocolProtocolAction" />

<kul:innerTab tabTitle="Submit for Review" parentTab="" defaultOpen="false" tabErrorKey="">
    <div class="innerTab-container" align="left">
        <table class="tab" cellpadding="0" cellspacing="0" summary=""> 
            <tbody>
                <tr>
                    <th width="15%"> 
                        <div align="right">
                            <nobr>
                            <kul:htmlAttributeLabel attributeEntry="${attributes.submissionTypeCode}" />
                            </nobr>
                        </div>
                    </th>
                    <td>
                        <kul:htmlControlAttribute property="actionHelper.protocolSubmitAction.submissionTypeCode" attributeEntry="${attributes.submissionTypeCode}" />
                    </td>
                    <th width="20%"> 
                        <div align="right">
                            <nobr>
                            <kul:htmlAttributeLabel attributeEntry="${attributes.protocolReviewTypeCode}" />
                            </nobr>
                        </div>
                    </th>
                    <td>
                        <nobr>
                        <kul:htmlControlAttribute property="actionHelper.protocolSubmitAction.protocolReviewTypeCode" attributeEntry="${attributes.protocolReviewTypeCode}" />
                        <html:image property="methodToCall.refreshScheduleDates.anchor${tabKey}"
								        src='${ConfigProperties.kra.externalizable.images.url}tinybutton-refresh.gif' styleClass="tinybutton"/>
                        </nobr>
                    </td>
                </tr>
                
                <tr>
                    <th width="15%"> 
                        <div align="right">
                            <kul:htmlAttributeLabel attributeEntry="${attributes.submissionQualifierTypeCode}" />
                        </div>
                    </th>
                    <td colspan="3" width="100%">
                        <kul:htmlControlAttribute property="actionHelper.protocolSubmitAction.submissionQualifierTypeCode" attributeEntry="${attributes.submissionQualifierTypeCode}" />
                    </td>
                </tr>
                
                <tr>
                	<th width="15%"> 
                        <div align="right">
                            <kul:htmlAttributeLabel attributeEntry="${attributes.committeeId}" />
                        </div>
                    </th>
                    <td>
                        <kul:htmlControlAttribute property="actionHelper.protocolSubmitAction.committeeId" attributeEntry="${attributes.committeeId}" />
                    </td>
                    <th width="20%"> 
                        <div align="right">
                              <kul:htmlAttributeLabel attributeEntry="${attributes.scheduleId}" />
                        </div>
                    </th>
                    <td>
                        <nobr>
                        <kul:htmlControlAttribute property="actionHelper.protocolSubmitAction.scheduleId" attributeEntry="${attributes.scheduleId}" />
                    	<html:image property="methodToCall.refreshScheduleDates.anchor${tabKey}"
								        src='${ConfigProperties.kra.externalizable.images.url}arrow_refresh.png' styleClass="tinybutton"/>
                        </nobr>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</kul:innerTab>
