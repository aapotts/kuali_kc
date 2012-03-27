<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>
<%@ attribute name="masterDisclosureProjects" required="true" type="java.util.List" description="A List of active or inactive FE" %>
<c:set var="coiDiscDetailAttributes" value="${DataDictionary.CoiDiscDetail.attributes}" />
<c:set var="hidden" value="${not masterDisclosureProjects[0].coiDisclProject.coiDisclosure.approvedDisclosure and masterDisclosureProjects[0].coiDisclProject.coiDisclosureEventType.excludeFromMasterDisclosure}" />
<kul:tab defaultOpen="false" tabTitle="Awards" auditCluster="financialEntityDiscAuditErrors" tabAuditKey="disclosureHelper.masterDisclosureBean.awardProjects[*" useRiceAuditMode="true"
    tabErrorKey="disclosureHelper.newCoiDisclProject.*" hidden="${hidden}">
	<div class="tab-container" align="center">
              
              
                                  
            <%-- New data --%>
            
            <%-- Existing data --%>

        	<c:forEach var="disclProjectBean" items="${masterDisclosureProjects}" varStatus="status">
                     <kra-coi:projectStyle disclProjectBean="${disclProjectBean}"/>                    
                     <kra-coi:awardHeader disclProject="${disclProjectBean.coiDisclProject}" />                    
                     <kra-coi:masterProjectQuestionnaires disclProjectBean="${disclProjectBean}" parentTab="Awards" />                    
                     <kra-coi:masterProjectFE disclProjectBean="${disclProjectBean}" projectDivNamePrefix="masterAwardFE"  idx="${status.index}" projectListName="awardProjects"/>                    
        	</c:forEach> 
            <%-- Existing data --%>
       </div>
</kul:tab>
