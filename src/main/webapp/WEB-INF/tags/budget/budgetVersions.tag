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

<%@ attribute name="budgetVersionOverviews" required="true" type="java.util.List"%>
<%@ attribute name="pathToVersions" required="true"%>
<%@ attribute name="requestedStartDateInitial" required="true" %>
<%@ attribute name="requestedEndDateInitial" required="true" %>
<%@ attribute name="errorKey" required="false"%>

<c:set var="budgetAttributes" value="${DataDictionary.BudgetDocument.attributes}" />
<c:set var="proposalDevelopmentAttributes" value="${DataDictionary.ProposalDevelopmentDocument.attributes}" />
<c:set var="budgetVersionOverviewAttributes" value="${DataDictionary.BudgetVersionOverview.attributes}" />
<c:set var="javascriptEnabled" value="true" />

<kul:tabTop tabTitle="Budget Versions (${KualiForm.formattedStartDate} - ${KualiForm.formattedEndDate})" defaultOpen="true" tabErrorKey="document.proposal.budgetVersion*,${Constants.DOCUMENT_ERRORS},${errorKey}" auditCluster="budgetVersionErrors" tabAuditKey="document.budgetVersionOverview">
	<div class="tab-container" align="center">
    	<div class="h2-container">
    		<span class="subhead-left"><h2>Budget Versions</h2></span>
    		<span class="subhead-right"><kul:help businessObjectClassName="org.kuali.kra.proposaldevelopment.bo.BudgetStatus" altText="help"/></span>
        </div>
        <table id="budget-versions-table" cellpadding="0" cellspacing="0" summary="Budget Versions">
			<tr>
				<th scope="row">&nbsp;</th>
				<th><div align="left"><kul:htmlAttributeLabel attributeEntry="${budgetVersionOverviewAttributes.documentDescription}" useShortLabel="true" noColon="true" /></div></th>
				<th><kul:htmlAttributeLabel attributeEntry="${budgetAttributes.budgetVersionNumber}" useShortLabel="true" noColon="true" /></th>
				<th><kul:htmlAttributeLabel attributeEntry="${budgetAttributes.totalDirectCost}" useShortLabel="true" noColon="true"/></th>
				<th><kul:htmlAttributeLabel attributeEntry="${budgetAttributes.totalIndirectCost}" useShortLabel="true" noColon="true"/></th>
				<th>Total</th>
				<th>Budget Status</th>
				<th><kul:htmlAttributeLabel attributeEntry="${budgetAttributes.finalVersionFlag}" useShortLabel="true" noColon="true"/></th>
				<kra:section permission="openBudgets">
				    <th><div align="center">Actions</div></th>
				</kra:section>
			</tr>
			
			<kra:section permission="addBudget">
			<tr>
            	<th width="50" align="right" scope="row"><div align="right">Add:</div></th>
            	<td class="infoline"><label><html:text name="KualiForm" property="newBudgetVersionName" size="16"/></label></td>
	            <td class="infoline">&nbsp;</td>
	            <td class="infoline">&nbsp;</td>
	            <td class="infoline">&nbsp;</td>
	            <td class="infoline">&nbsp;</td>
	            <td class="infoline">&nbsp;</td>
	            <td class="infoline">&nbsp;</td>
	            <td class="infoline">
            		<div align=center>
            			<html:image property="methodToCall.addBudgetVersion" src='${ConfigProperties.kra.externalizable.images.url}tinybutton-add1.gif' />
					</div>
				</td>
          	</tr>
          	</kra:section>
          	
          	<c:forEach var="budgetVersion" items="${budgetVersionOverviews}" varStatus="status">
          		<c:set var="version" value="${pathToVersions}.budgetVersionOverview[${status.index}]" />
          		<bean:define id="descriptionUpdatable" name="KualiForm" property="${pathToVersions}.budgetVersionOverview[${status.index}].descriptionUpdatable" />
          		<c:set var="currentTabIndex" value="${KualiForm.currentTabIndex}" scope="request"/>
          		<c:set var="parentTab" value="Budget Versions" scope="request"/>
          		<c:set var="tabTitle" value="${status.index}" scope="request"/>
          		<c:set var="tabKey" value="${kfunc:generateTabKey(parentTab)}:${kfunc:generateTabKey(tabTitle)}" scope="request"/>
          		<c:set var="currentTab" value="${kfunc:getTabState(KualiForm, tabKey)}"/>
          		<c:choose>
    				<c:when test="${empty currentTab}">
        				<c:set var="isOpen" value="false" />
    				</c:when>
    				<c:when test="${!empty currentTab}" >
        				<c:set var="isOpen" value="${currentTab == 'OPEN'}" />
    				</c:when>
				</c:choose>
				<c:if test="${isOpen == 'true' || isOpen == 'TRUE'}">
					<c:set var="displayStyle" value="display: table-row-group;"/>
				</c:if>
				<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
					<c:set var="displayStyle" value="display: none;"/>
				</c:if>
          		<tr>
           			<td align="right" class="tab-subhead1" scope="row">
           				<div align="center">
           					<c:if test="${isOpen == 'true' || isOpen == 'TRUE'}">
                 				<html:image property="methodToCall.toggleTab.tab${tabKey}" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-hide.gif" title="close ${tabTitle}" alt="close ${tabTitle}" styleClass="tinybutton"  styleId="tab-${tabKey}-imageToggle" onclick="javascript: return toggleTab(document, '${tabKey}'); " />
               				</c:if>
               				<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
                 				<html:image  property="methodToCall.toggleTab.tab${tabKey}" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-show.gif" title="open ${tabTitle}" alt="open ${tabTitle}" styleClass="tinybutton" styleId="tab-${tabKey}-imageToggle" onclick="javascript: return toggleTab(document, '${tabKey}'); " />
               				</c:if>
           				</div>
           			</td>
           			<td class="tab-subhead1"><kul:htmlControlAttribute property="${version}.documentDescription" attributeEntry="${budgetVersionOverviewAttributes.documentDescription}" readOnly="${descriptionUpdatable != 'Yes'}"/></td>
	            	<td class="tab-subhead1"><div align="center">${budgetVersion.budgetVersionNumber}</div></td>
		            <td class="tab-subhead1"><div align="right">&nbsp;<kul:htmlControlAttribute property="${version}.totalDirectCost" attributeEntry="${budgetVersionOverviewAttributes.totalDirectCost}" styleClass="amount" readOnly="true"/></div></td>
		            <td class="tab-subhead1"><div align="right">&nbsp;<kul:htmlControlAttribute property="${version}.totalIndirectCost" attributeEntry="${budgetVersionOverviewAttributes.totalIndirectCost}" styleClass="amount" readOnly="true"/></div></td>
		            <td class="tab-subhead1"><div align="right">&nbsp;<kul:htmlControlAttribute property="${version}.totalCost" attributeEntry="${budgetVersionOverviewAttributes.totalCost}" styleClass="amount" readOnly="true"/></div></td>
		            <td class="tab-subhead1">
		            	<div align="center">
		            		<!--  This field is to hold select status if it's disabled by javascript -->
		            		<html:hidden name="KualiForm" property="${version}.budgetStatus" disabled="true" />
		            		<kul:htmlControlAttribute property="${version}.budgetStatus" attributeEntry="${proposalDevelopmentAttributes.budgetStatus}" onchange="javascript: toggleFinalCheckboxes(document)" />
		            	</div>
            		</td>
	            	<td class="tab-subhead1">
	            		<div align="center">
	            			<kul:htmlControlAttribute property="${version}.finalVersionFlag" attributeEntry="${budgetAttributes.finalVersionFlag}" onclick="javascript: enableBudgetStatus(document, '${status.index}')" />
	            			<!--  This field is to hold checkbox status if it's disabled by javascript -->
	            			<html:hidden name="KualiForm" property="${version}.finalVersionFlag" disabled="true" />
	            		</div>
	            	</td>
	            	<kra:section permission="openBudgets">
	           			<td nowrap class="tab-subhead1">
	           				<div align=center>
	           					<html:image property="methodToCall.openBudgetVersion.line${status.index}.x" src='${ConfigProperties.kra.externalizable.images.url}tinybutton-open.gif' alt="open budget" />
	           					<kra:section permission="addBudget">
	           					    <html:image property="methodToCall.copyBudgetVersion.line${status.index}.x" src='${ConfigProperties.kra.externalizable.images.url}tinybutton-copy2.gif' alt="copy budget" />
	           				    </kra:section>
	           				</div>
	           			</td>
	           		</kra:section>
         		</tr>
         		<tbody style="${displayStyle}">
         		<tr>
            		<th align="right" scope="row">&nbsp;</th>
            		<td colspan="8" style="padding:0px; border-left:none">
            			<table cellpadding="0" cellspacing="0" summary="" style="width:100%;">
                			<tr>
	                    		<th width="1%" nowrap><div align="right">Residual Funds:</div></th>
	                    		<td align="left" width="12%">${budgetVersion.residualFunds}&nbsp;</td>
	                    		<th width="40%" nowrap><div align="right">OH Rate Type:</div></th>
	                    		<td align="left" width="99%">${rateClassMap[budgetVersion.ohRateTypeCode].description}&nbsp;</td>
                  			</tr>
	                  		<tr>
	                    		<th nowrap><div align="right">Cost Sharing:</div></th>
	                    		<td align="left">${budgetVersion.costSharingAmount}&nbsp;</td>
	                    		<th nowrap><div align="right">Last Updated:</div></th>
	                    		<td align="left"><fmt:formatDate value="${budgetVersion.updateTimestamp}" type="both" />&nbsp;</td>
	                  		</tr>
				            <tr>
				                <th nowrap><div align="right">Unrecovered F&amp;A:</div></th>
				                <td align="left">${budgetVersion.underrecoveryAmount}&nbsp;</td>
				                <th nowrap><div align="right">Last Updated By:</div></th>
				                <td align="left">${budgetVersion.updateUser}&nbsp;</td>
				            </tr>
                 			<tr>
                   				<th nowrap><div align="right">Comments:</div></th>
                   				<td colspan="3" align="left">${budgetVersion.comments}&nbsp;</td>
                 			</tr>
           				</table>
           			</td>
         		</tr>
         		</tbody>
          	</c:forEach>
        </table>
	</div>
</kul:tabTop>
<kul:panelFooter />
<img src="${ConfigProperties.kr.externalizable.images.url}pixel_clear.gif" onLoad="toggleFinalCheckboxes(document); setupBudgetStatuses(document);" />
