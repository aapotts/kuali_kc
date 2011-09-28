/*
 * Copyright 2005-2010 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
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
package org.kuali.kra.negotiations.service;

import java.util.List;

import org.kuali.kra.negotiations.bo.Negotiation;
import org.kuali.kra.negotiations.bo.NegotiationAssociatedDetailBean;
import org.kuali.kra.negotiations.bo.NegotiationAssociationType;
import org.kuali.rice.kns.bo.BusinessObject;

/**
 * Service to help with Negotiation and working with Negotiations.
 */
public interface NegotiationService {
    
    /**
     * Get the in-progress status codes.
     * @return
     */
    List<String> getInProgressStatusCodes();
    
    /**
     * Get the completed status codes.
     * @return
     */
    List<String> getCompletedStatusCodes();
    
    BusinessObject getAssociatedObject(Negotiation negotiation);
    
    /**
     * 
     * This method gets the award, proposal log, institutional proposal or sub award from the negotiation's associated doc ID, and
     * create a NegotiationAssociatedDetailBean to pass back;
     * @param negotiation
     * @return
     */
    NegotiationAssociatedDetailBean buildNegotiationAssociatedDetailBean(Negotiation negotiation);
    
    /**
     * Get any negotiations associated with BO(ProposalLog, Inst Prop, Award, Subaward).
     * User primarly by Medusa.
     * @param bo
     * @return
     */
    List<Negotiation> getAssociatedNegotiations(BusinessObject bo);
    
    /**
     * Retrieve the association type BO.
     * @param associationTypeCode
     * @return
     */
    NegotiationAssociationType getNegotiationAssociationType(String associationTypeCode);

    /**
     * Can a negotiation be linked to a proposal log?
     * @return
     */
    boolean isProposalLogLinkingEnabled();

    /**
     * Can a negotiation be linked to an inst prop?
     * @return
     */
    boolean isInstitutionalProposalLinkingEnabled();
    
    /**
     * Can a negotiation be linked to an award?
     * @return
     */
    boolean isAwardLinkingEnabled();
    
    /**
     * Can a negotiation be linked to a subaward?
     * @return
     */
    boolean isSubawardLinkingEnabled();
    
    /**
     * Can a negotiation be linked to nothing?
     * @return
     */
    boolean isNoModuleLinkingEnabled();
    
    /**
     * If the negotiation is linked to a proposal log that has been promoted to a inst prop, then
     * link the negotiation to the new inst prop.
     */
    void checkForPropLogPromotion(Negotiation negotiation);
    
    /**
     * 
     * This method checks to see if the passed in person id is the PI, CO-PI, or KeyPerson on the associated document.
     * @param negotiation
     * @param personToCheckPersonId
     * @return
     */
    boolean isPersonIsAssociatedPerson(Negotiation negotiation, String personToCheckPersonId);
    
    void findAndLoadNegotiationUnassociatedDetail(Negotiation negotiation, boolean reload);
}
