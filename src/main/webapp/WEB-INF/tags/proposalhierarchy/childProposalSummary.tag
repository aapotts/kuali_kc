<%--
 Copyright 2006-2009 The Kuali Foundation
 
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
<%@ attribute name="summaryIndex" required="true" %>
<c:choose>
	<c:when test="${KualiForm.hierarchyProposalSummaries[summaryIndex].synced}">
		<c:set var="syncLabel" value="Synced" />
	</c:when>
	<c:otherwise>
		<c:set var="syncLabel" value="Not synced" />
	</c:otherwise>
</c:choose>	
<c:set var="proposalNumber" value="${KualiForm.hierarchyProposalSummaries[summaryIndex].proposalNumber}"/>
<kul:tab tabTitle="Child (Proposal # ${proposalNumber})" tabDescription="${syncLabel}" defaultOpen="false" >
	<kra-ph:proposalSummaryBody summaryIndex="${summaryIndex}" parentTabTitle="Child (Proposal # ${proposalNumber})" />
</kul:tab>