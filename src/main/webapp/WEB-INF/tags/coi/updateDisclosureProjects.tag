<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>
<c:set var="masterDisclosure" value="${KualiForm.disclosureHelper.masterDisclosureBean}" />
<c:if test="${fn:length(masterDisclosure.manualAwardProjects) > 0 or fn:length(masterDisclosure.manualProposalProjects) > 0 or  fn:length(masterDisclosure.manualProtocolProjects) > 0 or  fn:length(masterDisclosure.manualTravelProjects) > 0}" >
<kul:tab defaultOpen="false" tabTitle="Manual Projects" auditCluster="financialEntityDiscAuditErrors" tabAuditKey="disclosureHelper.masterDisclosureBean.manualAwardProjects[*,disclosureHelper.masterDisclosureBean.manualProtocolProjects[*,disclosureHelper.masterDisclosureBean.manualProposalProjects[*,disclosureHelper.masterDisclosureBean.manualTravelProjects[*" useRiceAuditMode="true"
    tabErrorKey="disclosureHelper.newCoiDisclProject.*" >
    <div class="tab-container" align="center">
              

    <c:if test="${fn:length(masterDisclosure.manualAwardProjects) > 0}" >
        <kra-coi:masterManualProject masterDisclosureProjects="${masterDisclosure.manualAwardProjects}" projectDivNamePrefix="masterManualAwardFE" projectListName="manualAwardProjects"/>
    </c:if>
    <c:if test="${fn:length(masterDisclosure.manualProposalProjects) > 0}" >
        <kra-coi:masterManualProject masterDisclosureProjects="${masterDisclosure.manualProposalProjects}" projectDivNamePrefix="masterManualProposalFE" projectListName="manualProposalProjects"/>
    </c:if>
    <c:if test="${fn:length(masterDisclosure.manualProtocolProjects) > 0}" >
        <kra-coi:masterManualProject masterDisclosureProjects="${masterDisclosure.manualProtocolProjects}" projectDivNamePrefix="masterManualProtocolFE" projectListName="manualProtocolProjects"/>
    </c:if>
    <c:if test="${fn:length(masterDisclosure.manualTravelProjects) > 0}" >
        <kra-coi:masterManualProject masterDisclosureProjects="${masterDisclosure.manualTravelProjects}" projectDivNamePrefix="masterManualTravelFE" projectListName="manualTravelProjects"/>
    </c:if>
       </div>
</kul:tab>
</c:if>    
<c:if test="${fn:length(masterDisclosure.awardProjects) > 0}" >
    <%-- <kra-coi:proposalProjects /> --%>
    <kra-coi:masterAward masterDisclosureProjects="${masterDisclosure.awardProjects}"/>
</c:if>
<c:if test="${fn:length(masterDisclosure.proposalProjects) > 0}" >
    <%-- <kra-coi:proposalProjects /> --%>
    <kra-coi:masterProposal masterDisclosureProjects="${masterDisclosure.proposalProjects}"/>
</c:if>
<c:if test="${fn:length(masterDisclosure.protocolProjects) > 0}" >
    <%-- <kra-coi:proposalProjects /> --%>
    <kra-coi:masterProtocol masterDisclosureProjects="${masterDisclosure.protocolProjects}"/>
</c:if>
