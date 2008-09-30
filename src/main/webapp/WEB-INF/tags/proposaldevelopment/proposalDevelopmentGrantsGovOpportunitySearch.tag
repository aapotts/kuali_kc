<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<c:set var="proposalDevelopmentAttributes" value="${DataDictionary.ProposalDevelopmentDocument.attributes}" />
<c:set var="s2sOpportunityAttributes" value="${DataDictionary.S2sOpportunity.attributes}" />
<c:set var="textAreaFieldName" value="document.opportunityTitle" />
<c:set var="action" value="proposalDevelopmentProposal" />

<kul:tabTop tabTitle="Grants.gov" defaultOpen="true" tabErrorKey="proposalDevelopmentAttributes.cfdaNumber,proposalDevelopmentAttributes.programAnnouncementNumber ">
    <div class="tab-container" align="center">
        <h3>
            <span class="subhead-left">Opportunity Search</span>
            <span class="subhead-right"><a href="http://www.grants.gov" target="_blank">www.grants.gov</a><kul:help businessObjectClassName="org.kuali.kra.s2s.bo.S2sOpportunity" altText="help"/></span>
        </h3>
        
        <input type="hidden" name="document.cfdaNumber" value="${KualiForm.document.cfdaNumber}">    
        <input type="hidden" name="document.programAnnouncementNumber" value="${KualiForm.document.programAnnouncementNumber}">            
        
        Grants.gov Lookup
        <c:if test="${!readOnly}" >
        	<kul:lookup boClassName="org.kuali.kra.s2s.bo.S2sOpportunity" fieldConversions="opportunityId:document.s2sOpportunity.opportunityId,cfdaNumber:document.s2sOpportunity.cfdaNumber,opportunityTitle:document.s2sOpportunity.opportunityTitle,s2sSubmissionTypeCode:document.s2sOpportunity.s2sSubmissionTypeCode,revisionCode:document.s2sOpportunity.revisionCode,competetionId:document.s2sOpportunity.competetionId,openingDate:document.s2sOpportunity.openingDate,closingDate:document.s2sOpportunity.closingDate,instructionUrl:document.s2sOpportunity.instructionUrl,schemaUrl:document.s2sOpportunity.schemaUrl" anchor="${tabKey}" autoSearch="yes" lookupParameters="document.programAnnouncementNumber:opportunityId,document.cfdaNumber:cfdaNumber" readOnlyFields="yes"/>
        </c:if>	
               
            
        <br/>
        <br/>       
        <c:if test="${!readOnly}" >
        <c:choose>
            <c:when test="${!empty KualiForm.document.s2sOpportunity.opportunityId}" >    
                <html:image src="${ConfigProperties.kr.externalizable.images.url}tinybutton-remvopp.gif" styleClass="globalbuttons" property="methodToCall.removeOpportunity" title="remove opportunity" alt="remove opportunity"/>
            </c:when>    
        </c:choose>            
        </c:if>
        <br/>   
        
        <div align="center">
            <table cellpadding="0" cellspacing="0" summary="">
                <tr>
                    <td style="padding:0px">
	                    <kra-pd:proposalDevelopmentGrantsGovOpportunity />
                    </td>
                </tr>
                <tr>
                    <td style="padding:0px">
	        			<kra-pd:proposalDevelopmentGrantsGovSubmissionDetails />
                    </td>
                </tr>
                <tr>
                    <td style="padding:0px">
	        			<kra-pd:proposalDevelopmentGrantsGovForms />   
                    </td>
                </tr>
                <tr>    
                    <td style="padding:0px">
	        			<kra-pd:proposalDevelopmentSubmissionHistory />   
                    </td>
                </tr>
			</table>
	    </div>  
	   
    </div>
</kul:tabTop>

