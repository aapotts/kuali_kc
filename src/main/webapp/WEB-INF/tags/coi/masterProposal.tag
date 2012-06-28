<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>
<%@ attribute name="masterDisclosureProjects" required="true" type="java.util.List" description="A List of active or inactive FE" %>
<c:set var="coiDiscDetailAttributes" value="${DataDictionary.CoiDiscDetail.attributes}" />
<c:set var="hidden" value="${not masterDisclosureProjects[0].coiDisclProject.coiDisclosure.approvedDisclosure and masterDisclosureProjects[0].coiDisclProject.coiDisclosureEventType.excludeFromMasterDisclosure}" />
<kul:tab defaultOpen="false" tabTitle="Proposals" auditCluster="financialEntityDiscAuditErrors" tabAuditKey="disclosureHelper.masterDisclosureBean.proposalProjects[*" useRiceAuditMode="true"
    tabErrorKey="disclosureHelper.newCoiDisclProject.*" hidden="${hidden}">
	<div class="tab-container" align="center">
                                  
            <%-- New data --%>
            
            <%-- Existing data --%>

        	<c:forEach var="disclProjectBean" items="${masterDisclosureProjects}" varStatus="status">
                <kra-coi:projectStyle disclProjectBean="${disclProjectBean}"/>                    
        		<c:choose>
        			<c:when test="${disclProjectBean.coiDisclProject.institutionalProposalEvent}">
						<kra-coi:institutionalProposalHeader disclProject="${disclProjectBean.coiDisclProject}" />                    
        			</c:when>
        			<c:otherwise>
						<kra-coi:proposalHeader disclProject="${disclProjectBean.coiDisclProject}" />                    
        			</c:otherwise>
        		</c:choose>
                <kra-coi:masterProjectQuestionnaires disclProjectBean="${disclProjectBean}" parentTab="Proposals" />                    
                <kra-coi:masterProjectFE disclProjectBean="${disclProjectBean}" projectDivNamePrefix="masterProposalFE" idx="${status.index}" projectListName="proposalProjects"/>                    
        	</c:forEach> 
            <%-- Existing data --%>
       </div>
</kul:tab>
