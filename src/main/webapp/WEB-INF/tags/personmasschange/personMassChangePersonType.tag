<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<c:set var="awardPersonMassChangeAttributes" value="${DataDictionary.AwardPersonMassChange.attributes}" />
<c:set var="institutionalProposalPersonMassChangeAttributes" value="${DataDictionary.InstitutionalProposalPersonMassChange.attributes}" />
<c:set var="proposalDevelopmentPersonMassChangeAttributes" value="${DataDictionary.ProposalDevelopmentPersonMassChange.attributes}" />
<c:set var="proposalLogPersonMassChangeAttributes" value="${DataDictionary.ProposalLogPersonMassChange.attributes}" />
<c:set var="subawardPersonMassChangeAttributes" value="${DataDictionary.SubawardPersonMassChange.attributes}" />
<c:set var="negotiationPersonMassChangeAttributes" value="${DataDictionary.NegotiationPersonMassChange.attributes}" />
<c:set var="committeePersonMassChangeAttributes" value="${DataDictionary.CommitteePersonMassChange.attributes}" />
<c:set var="protocolPersonMassChangeAttributes" value="${DataDictionary.ProtocolPersonMassChange.attributes}" />
<c:set var="schedulePersonMassChangeAttributes" value="${DataDictionary.SchedulePersonMassChange.attributes}" />
<c:set var="unitPersonMassChangeAttributes" value="${DataDictionary.UnitPersonMassChange.attributes}" />
<c:set var="action" value="personMassChangeHome" />
<c:set var="className" value="org.kuali.kra.personmasschange.document.PersonMassChangeDocument" />
<c:set var="readOnly" value="${not KualiForm.editingMode['modify']}"/>

<kul:tabTop tabTitle="Person Type" defaultOpen="true" tabErrorKey="document.personMassChange*">
	<div class="tab-container" align="center">
	    <kul:innerTab parentTab="${parentTab}" tabTitle="Award ${tabTitle}" defaultOpen="false" >
	        <div class="tab-container" align="center">
	            <table cellpadding="4" cellspacing="0" summary="">
	                <tr>
	                    <td><div align="left"><kul:htmlAttributeLabel attributeEntry="${awardPersonMassChangeAttributes.investigator}" /></div></td>
	                    <td><kul:htmlControlAttribute property="document.personMassChange.awardPersonMassChange.investigator" 
	                                                  attributeEntry="${awardPersonMassChangeAttributes.investigator}" readOnly="false" /></td>
	                </tr>
	                <tr>
	                    <td><div align="left"><kul:htmlAttributeLabel attributeEntry="${awardPersonMassChangeAttributes.contactPerson}" /></div></td>
	                    <td><kul:htmlControlAttribute property="document.personMassChange.awardPersonMassChange.contactPerson" 
	                                                  attributeEntry="${awardPersonMassChangeAttributes.contactPerson}" readOnly="false" /></td>
	                </tr>
	                <tr>
	                    <td><div align="left"><kul:htmlAttributeLabel attributeEntry="${awardPersonMassChangeAttributes.foreignTrip}" /></div></td>
	                    <td><kul:htmlControlAttribute property="document.personMassChange.awardPersonMassChange.foreignTrip" 
	                                                  attributeEntry="${awardPersonMassChangeAttributes.foreignTrip}" readOnly="false" /></td>
	                </tr>
	                <tr>
	                    <td><div align="left"><kul:htmlAttributeLabel attributeEntry="${awardPersonMassChangeAttributes.unitAdministrator}" /></div></td>
	                    <td><kul:htmlControlAttribute property="document.personMassChange.awardPersonMassChange.unitAdministrator" 
	                                                  attributeEntry="${awardPersonMassChangeAttributes.unitAdministrator}" readOnly="false" /></td>
	                </tr>
	                <tr>
	                    <td />
	                    <td><div align="left">
	                        <html:image src='${ConfigProperties.kra.externalizable.images.url}tinybutton-selectall.gif' alt="Select All" styleClass="tinybutton" />
	                        <html:image src='${ConfigProperties.kra.externalizable.images.url}tinybutton-selectnone.gif' alt="Select None" styleClass="tinybutton" />
	                    </div></td>
	                </tr>
	            </table>
	        </div>
	    </kul:innerTab>
	    
	    <kul:innerTab parentTab="${parentTab}" tabTitle="Institutional Proposal ${tabTitle}" defaultOpen="false" >
	        <div class="tab-container" align="center">
	            <table cellpadding="4" cellspacing="0" summary="">
	                <tr>
	                    <td><div align="left"><kul:htmlAttributeLabel attributeEntry="${institutionalProposalPersonMassChangeAttributes.investigator}" /></div></td>
	                    <td><kul:htmlControlAttribute property="document.personMassChange.institutionalProposalPersonMassChange.investigator" 
	                                                  attributeEntry="${institutionalProposalPersonMassChangeAttributes.investigator}" readOnly="false" /></td>
	                </tr>
	                <tr>
	                    <td><div align="left"><kul:htmlAttributeLabel attributeEntry="${institutionalProposalPersonMassChangeAttributes.unitAdministrator}" /></div></td>
	                    <td><kul:htmlControlAttribute property="document.personMassChange.institutionalProposalPersonMassChange.unitAdministrator" 
	                                                  attributeEntry="${institutionalProposalPersonMassChangeAttributes.unitAdministrator}" readOnly="false" /></td>
	                </tr>
	                <tr>
	                    <td><div align="left"><kul:htmlAttributeLabel attributeEntry="${institutionalProposalPersonMassChangeAttributes.mailingInformation}" /></div></td>
	                    <td><kul:htmlControlAttribute property="document.personMassChange.institutionalProposalPersonMassChange.mailingInformation" 
	                                                  attributeEntry="${institutionalProposalPersonMassChangeAttributes.mailingInformation}" readOnly="false" /></td>
	                </tr>
	                <tr>
	                    <td><div align="left"><kul:htmlAttributeLabel attributeEntry="${institutionalProposalPersonMassChangeAttributes.ipReviewer}" /></div></td>
	                    <td><kul:htmlControlAttribute property="document.personMassChange.institutionalProposalPersonMassChange.ipReviewer" 
	                                                  attributeEntry="${institutionalProposalPersonMassChangeAttributes.ipReviewer}" readOnly="false" /></td>
	                </tr>
	                <tr>
	                    <td />
	                    <td><div align="left">
	                        <html:image src='${ConfigProperties.kra.externalizable.images.url}tinybutton-selectall.gif' alt="Select All" styleClass="tinybutton" />
	                        <html:image src='${ConfigProperties.kra.externalizable.images.url}tinybutton-selectnone.gif' alt="Select None" styleClass="tinybutton" />
	                    </div></td>
	                </tr>
	            </table>
	        </div>
	    </kul:innerTab>
	    
	    <kul:innerTab parentTab="${parentTab}" tabTitle="Proposal Development ${tabTitle}" defaultOpen="false" >
	        <div class="tab-container" align="center">
	            <table cellpadding="4" cellspacing="0" summary="">
	                <tr>
	                    <td><div align="left"><kul:htmlAttributeLabel attributeEntry="${proposalDevelopmentPersonMassChangeAttributes.investigator}" /></div></td>
	                    <td><kul:htmlControlAttribute property="document.personMassChange.proposalDevelopmentPersonMassChange.investigator" 
	                                                  attributeEntry="${proposalDevelopmentPersonMassChangeAttributes.investigator}" readOnly="false" /></td>
	                </tr>
	                <tr>
	                    <td><div align="left"><kul:htmlAttributeLabel attributeEntry="${proposalDevelopmentPersonMassChangeAttributes.mailingInformation}" /></div></td>
	                    <td><kul:htmlControlAttribute property="document.personMassChange.proposalDevelopmentPersonMassChange.mailingInformation" 
	                                                  attributeEntry="${proposalDevelopmentPersonMassChangeAttributes.mailingInformation}" readOnly="false" /></td>
	                </tr>
	                <tr>
	                    <td><div align="left"><kul:htmlAttributeLabel attributeEntry="${proposalDevelopmentPersonMassChangeAttributes.keyStudyPerson}" /></div></td>
	                    <td><kul:htmlControlAttribute property="document.personMassChange.proposalDevelopmentPersonMassChange.keyStudyPerson" 
	                                                  attributeEntry="${proposalDevelopmentPersonMassChangeAttributes.keyStudyPerson}" readOnly="false" /></td>
	                </tr>
	                <tr>
	                    <td><div align="left"><kul:htmlAttributeLabel attributeEntry="${proposalDevelopmentPersonMassChangeAttributes.budgetPerson}" /></div></td>
	                    <td><kul:htmlControlAttribute property="document.personMassChange.proposalDevelopmentPersonMassChange.budgetPerson" 
	                                                  attributeEntry="${proposalDevelopmentPersonMassChangeAttributes.budgetPerson}" readOnly="false" /></td>
	                </tr>
	                <tr>
	                    <td />
	                    <td><div align="left">
	                        <html:image src='${ConfigProperties.kra.externalizable.images.url}tinybutton-selectall.gif' alt="Select All" styleClass="tinybutton" />
	                        <html:image src='${ConfigProperties.kra.externalizable.images.url}tinybutton-selectnone.gif' alt="Select None" styleClass="tinybutton" />
	                    </div></td>
	                </tr>
	            </table>
	        </div>
	    </kul:innerTab>
	    
	    <kul:innerTab parentTab="${parentTab}" tabTitle="Proposal Log ${tabTitle}" defaultOpen="false" >
	        <div class="tab-container" align="center">
	            <table cellpadding="4" cellspacing="0" summary="">
	                <tr>
	                    <td><div align="left"><kul:htmlAttributeLabel attributeEntry="${proposalLogPersonMassChangeAttributes.principalInvestigator}" /></div></td>
	                    <td><kul:htmlControlAttribute property="document.personMassChange.proposalLogPersonMassChange.principalInvestigator" 
	                                                  attributeEntry="${proposalLogPersonMassChangeAttributes.principalInvestigator}" readOnly="false" /></td>
	                </tr>
	                <tr>
	                    <td />
	                    <td><div align="left">
	                        <html:image src='${ConfigProperties.kra.externalizable.images.url}tinybutton-selectall.gif' alt="Select All" styleClass="tinybutton" />
	                        <html:image src='${ConfigProperties.kra.externalizable.images.url}tinybutton-selectnone.gif' alt="Select None" styleClass="tinybutton" />
	                    </div></td>
	                </tr>
	            </table>
	        </div>
	    </kul:innerTab>
	    
	    <kul:innerTab parentTab="${parentTab}" tabTitle="Subaward ${tabTitle}" defaultOpen="false" >
	        <div class="tab-container" align="center">
	            <table cellpadding="4" cellspacing="0" summary="">
	                <tr>
	                    <td><div align="left"><kul:htmlAttributeLabel attributeEntry="${subawardPersonMassChangeAttributes.contactPerson}" /></div></td>
	                    <td><kul:htmlControlAttribute property="document.personMassChange.subawardPersonMassChange.contactPerson" 
	                                                  attributeEntry="${subawardPersonMassChangeAttributes.contactPerson}" readOnly="false" /></td>
	                </tr>
	                <tr>
	                    <td><div align="left"><kul:htmlAttributeLabel attributeEntry="${subawardPersonMassChangeAttributes.requisitioner}" /></div></td>
	                    <td><kul:htmlControlAttribute property="document.personMassChange.subawardPersonMassChange.requisitioner" 
	                                                  attributeEntry="${subawardPersonMassChangeAttributes.requisitioner}" readOnly="false" /></td>
	                </tr>
	                <tr>
	                    <td />
	                    <td><div align="left">
	                        <html:image src='${ConfigProperties.kra.externalizable.images.url}tinybutton-selectall.gif' alt="Select All" styleClass="tinybutton" />
	                        <html:image src='${ConfigProperties.kra.externalizable.images.url}tinybutton-selectnone.gif' alt="Select None" styleClass="tinybutton" />
	                    </div></td>
	                </tr>
	            </table>
	        </div>
	    </kul:innerTab>
	    
	    <kul:innerTab parentTab="${parentTab}" tabTitle="Negotiation ${tabTitle}" defaultOpen="false" >
	        <div class="tab-container" align="center">
	            <table cellpadding="4" cellspacing="0" summary="">
	                <tr>
	                    <td><div align="left"><kul:htmlAttributeLabel attributeEntry="${negotiationPersonMassChangeAttributes.negotiator}" /></div></td>
	                    <td><kul:htmlControlAttribute property="document.personMassChange.negotiationPersonMassChange.negotiator" 
	                                                  attributeEntry="${negotiationPersonMassChangeAttributes.negotiator}" readOnly="false" /></td>
	                </tr>
	                <tr>
	                    <td />
	                    <td><div align="left">
	                        <html:image src='${ConfigProperties.kra.externalizable.images.url}tinybutton-selectall.gif' alt="Select All" styleClass="tinybutton" />
	                        <html:image src='${ConfigProperties.kra.externalizable.images.url}tinybutton-selectnone.gif' alt="Select None" styleClass="tinybutton" />
	                    </div></td>
	                </tr>
	            </table>
	        </div>
	    </kul:innerTab>
	    
	    <kul:innerTab parentTab="${parentTab}" tabTitle="Committee ${tabTitle}" defaultOpen="false" >
	        <div class="tab-container" align="center">
	            <table cellpadding="4" cellspacing="0" summary="">
	                <tr>
	                    <td><div align="left"><kul:htmlAttributeLabel attributeEntry="${committeePersonMassChangeAttributes.member}" /></div></td>
	                    <td><kul:htmlControlAttribute property="document.personMassChange.committeePersonMassChange.member" 
	                                                  attributeEntry="${committeePersonMassChangeAttributes.member}" readOnly="false" /></td>
	                </tr>
	                <tr>
	                    <td />
	                    <td><div align="left">
	                        <html:image src='${ConfigProperties.kra.externalizable.images.url}tinybutton-selectall.gif' alt="Select All" styleClass="tinybutton" />
	                        <html:image src='${ConfigProperties.kra.externalizable.images.url}tinybutton-selectnone.gif' alt="Select None" styleClass="tinybutton" />
	                    </div></td>
	                </tr>
	            </table>
	        </div>
	    </kul:innerTab>
	    
	    <kul:innerTab parentTab="${parentTab}" tabTitle="Protocol ${tabTitle}" defaultOpen="false" >
	        <div class="tab-container" align="center">
	            <table cellpadding="4" cellspacing="0" summary="">
	                <tr>
	                    <td><div align="left"><kul:htmlAttributeLabel attributeEntry="${protocolPersonMassChangeAttributes.investigator}" /></div></td>
	                    <td><kul:htmlControlAttribute property="document.personMassChange.protocolPersonMassChange.investigator" 
	                                                  attributeEntry="${protocolPersonMassChangeAttributes.investigator}" readOnly="false" /></td>
	                </tr>
	                <tr>
	                    <td><div align="left"><kul:htmlAttributeLabel attributeEntry="${protocolPersonMassChangeAttributes.keyStudyPerson}" /></div></td>
	                    <td><kul:htmlControlAttribute property="document.personMassChange.protocolPersonMassChange.keyStudyPerson" 
	                                                  attributeEntry="${protocolPersonMassChangeAttributes.keyStudyPerson}" readOnly="false" /></td>
	                </tr>
	                <tr>
	                    <td><div align="left"><kul:htmlAttributeLabel attributeEntry="${protocolPersonMassChangeAttributes.correspondents}" /></div></td>
	                    <td><kul:htmlControlAttribute property="document.personMassChange.protocolPersonMassChange.correspondents" 
	                                                  attributeEntry="${protocolPersonMassChangeAttributes.correspondents}" readOnly="false" /></td>
	                </tr>
	                <tr>
	                    <td><div align="left"><kul:htmlAttributeLabel attributeEntry="${protocolPersonMassChangeAttributes.reviewer}" /></div></td>
	                    <td><kul:htmlControlAttribute property="document.personMassChange.protocolPersonMassChange.reviewer" 
	                                                  attributeEntry="${protocolPersonMassChangeAttributes.reviewer}" readOnly="false" /></td>
	                </tr>
	                <tr>
	                    <td />
	                    <td><div align="left">
	                        <html:image src='${ConfigProperties.kra.externalizable.images.url}tinybutton-selectall.gif' alt="Select All" styleClass="tinybutton" />
	                        <html:image src='${ConfigProperties.kra.externalizable.images.url}tinybutton-selectnone.gif' alt="Select None" styleClass="tinybutton" />
	                    </div></td>
	                </tr>
	            </table>
	        </div>
	    </kul:innerTab>
	    
	    <kul:innerTab parentTab="${parentTab}" tabTitle="Schedule ${tabTitle}" defaultOpen="false" >
	        <div class="tab-container" align="center">
	            <table cellpadding="4" cellspacing="0" summary="">
	                <tr>
	                    <td><div align="left"><kul:htmlAttributeLabel attributeEntry="${schedulePersonMassChangeAttributes.attendees}" /></div></td>
	                    <td><kul:htmlControlAttribute property="document.personMassChange.schedulePersonMassChange.attendees" 
	                                                  attributeEntry="${schedulePersonMassChangeAttributes.attendees}" readOnly="false" /></td>
	                </tr>
	                <tr>
	                    <td />
	                    <td><div align="left">
	                        <html:image src='${ConfigProperties.kra.externalizable.images.url}tinybutton-selectall.gif' alt="Select All" styleClass="tinybutton" />
	                        <html:image src='${ConfigProperties.kra.externalizable.images.url}tinybutton-selectnone.gif' alt="Select None" styleClass="tinybutton" />
	                    </div></td>
	                </tr>
	            </table>
	        </div>
	    </kul:innerTab>
	    
	    <kul:innerTab parentTab="${parentTab}" tabTitle="Unit ${tabTitle}" defaultOpen="false" >
	        <div class="tab-container" align="center">
	            <table cellpadding="4" cellspacing="0" summary="">
	                <tr>
	                    <td><div align="left"><kul:htmlAttributeLabel attributeEntry="${unitPersonMassChangeAttributes.administrator}" /></div></td>
	                    <td><kul:htmlControlAttribute property="document.personMassChange.unitPersonMassChange.administrator" 
	                                                  attributeEntry="${unitPersonMassChangeAttributes.administrator}" readOnly="false" /></td>
	                </tr>
	                <tr>
	                    <td />
	                    <td><div align="left">
	                        <html:image src='${ConfigProperties.kra.externalizable.images.url}tinybutton-selectall.gif' alt="Select All" styleClass="tinybutton" />
	                        <html:image src='${ConfigProperties.kra.externalizable.images.url}tinybutton-selectnone.gif' alt="Select None" styleClass="tinybutton" />
	                    </div></td>
	                </tr>
	            </table>
	        </div>
	    </kul:innerTab>
    </div>
</kul:tabTop>