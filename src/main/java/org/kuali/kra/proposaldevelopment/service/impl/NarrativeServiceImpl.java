/*
 * Copyright 2007 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kra.proposaldevelopment.service.impl;

import static org.kuali.kra.infrastructure.Constants.NARRATIVE_MODULE_NUMBER;
import static org.kuali.kra.infrastructure.Constants.NARRATIVE_MODULE_SEQUENCE_NUMBER;
import static org.kuali.kra.infrastructure.KraServiceLocator.getService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.core.service.BusinessObjectService;
import org.kuali.core.service.DateTimeService;
import org.kuali.core.util.GlobalVariables;
import org.kuali.kra.bo.KraPersistableBusinessObjectBase;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.infrastructure.NarrativeRight;
import org.kuali.kra.proposaldevelopment.bo.Narrative;
import org.kuali.kra.proposaldevelopment.bo.NarrativeAttachment;
import org.kuali.kra.proposaldevelopment.bo.NarrativeUserRights;
import org.kuali.kra.proposaldevelopment.bo.ProposalUserRoles;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.proposaldevelopment.service.NarrativeAuthZService;
import org.kuali.kra.proposaldevelopment.service.NarrativeService;
import org.kuali.kra.proposaldevelopment.service.ProposalPersonService;

/**
 * This class...
 */
public class NarrativeServiceImpl implements NarrativeService {
    private NarrativeAuthZService narrativeAuthZService;
    private ProposalPersonService proposalPersonService;
    private BusinessObjectService businessObjectService;
    private DateTimeService dateTimeService;
    
    /**
     * 
     * Method to add a new narrative to narratives list
     * @param narrative
     */
    public void addNarrative(ProposalDevelopmentDocument proposaldevelopmentDocument,Narrative narrative) {
        narrative.setProposalNumber(proposaldevelopmentDocument.getProposalNumber());
        narrative.setModuleNumber(proposaldevelopmentDocument.getProposalNextValue(NARRATIVE_MODULE_NUMBER));
        narrative.setModuleSequenceNumber(proposaldevelopmentDocument.getProposalNextValue(NARRATIVE_MODULE_SEQUENCE_NUMBER));
        updateUserTimestamp(narrative);
        narrative.setModifyAttachment(true);
        narrative.refreshReferenceObject("narrativeType");
        narrative.refreshReferenceObject("narrativeStatus");
        narrative.populateAttachment();
        addNarrativeUserRights(proposaldevelopmentDocument,narrative);
        if (isProposalAttachmentType(narrative)) {
            proposaldevelopmentDocument.getNarratives().add(narrative);
            
        } else {
            proposaldevelopmentDocument.getInstitutes().add(narrative);
        }
    }
    /**
     * 
     * This method used to add narrative user rights to narrative object
     * It looks for proposal persons who has narrative rights and add it to narrative
     * @param narrative
     */
    private void addNarrativeUserRights(ProposalDevelopmentDocument proposaldevelopmentDocument,Narrative narrative) {
        List<ProposalUserRoles> proposalUserRoles = proposaldevelopmentDocument.getProposalUserRoles();
        List<NarrativeUserRights> narrativeUserRights = narrative.getNarrativeUserRights();
        for (ProposalUserRoles proposalUserRole : proposalUserRoles) {
            NarrativeRight narrativeRight = narrativeAuthZService.getNarrativeRight(proposalUserRole.getRoleId());
            String personName = proposalPersonService.getPersonName(proposaldevelopmentDocument, proposalUserRole.getUserId());
            NarrativeUserRights narrUserRight = new NarrativeUserRights();
            narrUserRight.setProposalNumber(narrative.getProposalNumber());
            narrUserRight.setModuleNumber(narrative.getModuleNumber());
            narrUserRight.setUserId(proposalUserRole.getUserId());
            narrUserRight.setAccessType(narrativeRight.getAccessType());
            narrUserRight.setPersonName(personName);
            updateUserTimestamp(narrUserRight);
            narrativeUserRights.add(narrUserRight);
        }
    }

    public void deleteProposalAttachment(ProposalDevelopmentDocument proposaldevelopmentDocument,int lineToDelete) {
        deleteAttachment(proposaldevelopmentDocument.getNarratives(), lineToDelete);
    }

    public void deleteInstitutionalAttachment(ProposalDevelopmentDocument proposaldevelopmentDocument,int lineToDelete) {
        deleteAttachment(proposaldevelopmentDocument.getInstitutes(), lineToDelete);
    }

    private void deleteAttachment(List<Narrative> narratives, int lineToDelete) {
        Narrative narrative = narratives.get(lineToDelete);
        NarrativeAttachment narrAtt = new NarrativeAttachment();
        narrAtt.setProposalNumber(narrative.getProposalNumber());
        narrAtt.setModuleNumber(narrative.getModuleNumber());
        if (narrative.getNarrativeAttachmentList().isEmpty())
            narrative.getNarrativeAttachmentList().add(narrAtt);
        else
            narrative.getNarrativeAttachmentList().set(0, narrAtt);
        narratives.remove(lineToDelete);

    }
    
    public void populatePersonNameForNarrativeUserRights(ProposalDevelopmentDocument proposaldevelopmentDocument,Narrative narrative) {
//        Narrative narrative = getNarratives().get(lineNumber);
        List<NarrativeUserRights> narrativeUserRights = narrative.getNarrativeUserRights();
        for (NarrativeUserRights narrativeUserRight : narrativeUserRights) {
            String personName = proposalPersonService.getPersonName(proposaldevelopmentDocument, narrativeUserRight.getUserId());
            narrativeUserRight.setPersonName(personName);
        }
    }

    public void replaceAttachment(Narrative narrative) {
//        Narrative narrative = proposaldevelopmentDocument.getNarratives().get(selectedLine);
        NarrativeAttachment narrativeAttachment =  findNarrativeAttachment(narrative);
        if(narrativeAttachment!=null)
            if (narrative.getNarrativeAttachmentList().isEmpty())
                narrative.getNarrativeAttachmentList().add(narrativeAttachment);
            else
                narrative.getNarrativeAttachmentList().set(0, narrativeAttachment);
        narrative.populateAttachment();
    }
    /**
     * 
     * This method used to find the narrative attachment for a narrative
     * @param narrative
     * @return NarrativeAttachment
     */
    private NarrativeAttachment findNarrativeAttachment(Narrative narrative){
        Map<String,Integer> narrativeAttachemntMap = new HashMap<String,Integer>();
        narrativeAttachemntMap.put("proposalNumber", narrative.getProposalNumber());
        narrativeAttachemntMap.put("moduleNumber", narrative.getModuleNumber());
        return (NarrativeAttachment)businessObjectService.findByPrimaryKey(NarrativeAttachment.class, narrativeAttachemntMap);
    }

    public void populateNarrativeRightsForLoggedinUser(ProposalDevelopmentDocument proposaldevelopmentDocument) {
        List<Narrative> narrativeList = proposaldevelopmentDocument.getNarratives();
        //Have to get person id of logged in user for the time being, its been hard coded
        String updateUser = GlobalVariables.getUserSession().getLoggedInUserNetworkId();
        String loggedInUserPersonId = "000000002";//get person id for looged in user
        for (Narrative narrative : narrativeList) {
            narrative.setModifyAttachment(false);
            narrative.setViewAttachment(false);
            List<NarrativeUserRights> narrativeUserRights = narrative.getNarrativeUserRights();
            for (NarrativeUserRights narrativeRight : narrativeUserRights) {
                if (StringUtils.equals(narrativeRight.getUserId(),loggedInUserPersonId)) {
                    narrative.setViewAttachment(narrativeRight.getAccessType().equals(
                            NarrativeRight.VIEW_NARRATIVE_RIGHT.getAccessType()));
                    narrative.setModifyAttachment(narrativeRight.getAccessType().equals(
                            NarrativeRight.MODIFY_NARRATIVE_RIGHT.getAccessType()));
                    break;
                }
            }
        }
    }

    /**
     * Update the User and Timestamp for the business object.
     * @param doc the business object
     */
    private void updateUserTimestamp(KraPersistableBusinessObjectBase bo) {
        String updateUser = GlobalVariables.getUserSession().getLoggedInUserNetworkId();
    
        // Since the UPDATE_USER column is only VACHAR(8), we need to truncate this string if it's longer than 8 characters
        if (updateUser.length() > 8) {
            updateUser = updateUser.substring(0, 8);
        }
        bo.setUpdateTimestamp(getDateTimeService().getCurrentTimestamp());
        bo.setUpdateUser(updateUser);
    }
    /**
     * Gets the narrativeAuthZService attribute. 
     * @return Returns the narrativeAuthZService.
     */
    public NarrativeAuthZService getNarrativeAuthZService() {
        return narrativeAuthZService;
    }
    /**
     * Sets the narrativeAuthZService attribute value.
     * @param narrativeAuthZService The narrativeAuthZService to set.
     */
    public void setNarrativeAuthZService(NarrativeAuthZService narrativeAuthZService) {
        this.narrativeAuthZService = narrativeAuthZService;
    }
    /**
     * Gets the proposalPersonService attribute. 
     * @return Returns the proposalPersonService.
     */
    public ProposalPersonService getProposalPersonService() {
        return proposalPersonService;
    }
    /**
     * Sets the proposalPersonService attribute value.
     * @param proposalPersonService The proposalPersonService to set.
     */
    public void setProposalPersonService(ProposalPersonService proposalPersonService) {
        this.proposalPersonService = proposalPersonService;
    }
    /**
     * Gets the businessObjectService attribute. 
     * @return Returns the businessObjectService.
     */
    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }
    /**
     * Sets the businessObjectService attribute value.
     * @param businessObjectService The businessObjectService to set.
     */
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
    /**
     * Gets the dateTimeService attribute. 
     * @return Returns the dateTimeService.
     */
    public DateTimeService getDateTimeService() {
        if(dateTimeService==null){
            dateTimeService = (DateTimeService)KraServiceLocator.getService(Constants.DATE_TIME_SERVICE_NAME);
        }
        return dateTimeService;
    }
    /**
     * Sets the dateTimeService attribute value.
     * @param dateTimeService The dateTimeService to set.
     */
    public void setDateTimeService(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }
    
    private boolean isProposalAttachmentType(Narrative narrative) {
        return Constants.NARRATIVE_NARRATIVE_TYPE_GROUP_CODE.equals(narrative.getNarrativeType().getNarrativeTypeGroup());
    }
}
