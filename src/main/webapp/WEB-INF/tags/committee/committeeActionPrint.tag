<%--
 Copyright 2006-2010 The Kuali Foundation
 
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

<div id="workarea">
    <kul:tab tabTitle="Print" 
             tabErrorKey="committeeHelper.committeeActionsHelper.printType"
             auditCluster="requiredFieldsAuditErrors"  
             defaultOpen="false"
             useCurrentTabIndexAsKey="true"  
             transparentBackground="true">
        <div class="tab-container" align="center">
            <h3>
                <span class="subhead-left">Print</span>
                <span class="subhead-right"><kul:help documentTypeName="CommitteeDocument" pageName="Committee Actions" /></span>
            </h3>
            
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td><div align="left">Roster</div></td> 
                    <td><div align="center"><html:radio property="committeeHelper.committeeActionsHelper.printType" value="roster" styleClass="radio" /></div></td>
                </tr>     
                <tr>
                    <td><div align="left">Future Scheduled Meetings</div></td> 
                    <td><div align="center"><html:radio property="committeeHelper.committeeActionsHelper.printType" value="futureScheduledMeetings" styleClass="radio" /></div></td>
                </tr>     
                <tr>
                    <td class="infoline"><div align="left">&nbsp;</div></td> 
                    <td class="infoline">
                        <div align="center">                    
                            <html:image property="methodToCall.printCommitteeDocument"
                                        src='${ConfigProperties.kra.externalizable.images.url}tinybutton-printsel.gif' 
                                        styleClass="tinybutton"/>                         
                        </div>
                    </td>
                </tr>     
            </table>
        </div> 
    </kul:tab>
    <kul:panelFooter />
</div>