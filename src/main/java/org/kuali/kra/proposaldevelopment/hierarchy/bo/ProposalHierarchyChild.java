/*
 * Copyright 2006-2008 The Kuali Foundation
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
package org.kuali.kra.proposaldevelopment.hierarchy.bo;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;

import org.kuali.kra.bo.KraPersistableBusinessObjectBase;
import org.kuali.kra.proposaldevelopment.bo.Narrative;
import org.kuali.kra.proposaldevelopment.bo.PropScienceKeyword;
import org.kuali.kra.proposaldevelopment.bo.ProposalPerson;
import org.kuali.kra.proposaldevelopment.bo.ProposalSpecialReview;
import org.kuali.kra.proposaldevelopment.hierarchy.HierarchyChildComparable;

public class ProposalHierarchyChild extends KraPersistableBusinessObjectBase implements HierarchyChildComparable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -6265373411837495670L;

    private String proposalNumber;
    private int proposalChildHashCode;
    private Timestamp proposalUpdateTimestamp;
    private List<PropScienceKeyword> propScienceKeywords;
    private ProposalPerson principalInvestigator;
    private List<ProposalPerson> investigators;
    private List<ProposalPerson> proposalPersons;
    private List<ProposalSpecialReview> propSpecialReviews;
    private List<Narrative> narratives;




    /**
     * Gets the proposalNumber attribute. 
     * @return Returns the proposalNumber.
     */
    public String getProposalNumber() {
        return proposalNumber;
    }




    /**
     * Sets the proposalNumber attribute value.
     * @param proposalNumber The proposalNumber to set.
     */
    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }




    /**
     * Gets the proposalChildHashCode attribute. 
     * @return Returns the proposalChildHashCode.
     */
    public int getProposalChildHashCode() {
        return proposalChildHashCode;
    }




    /**
     * Sets the proposalChildHashCode attribute value.
     * @param proposalChildHashCode The proposalChildHashCode to set.
     */
    public void setProposalChildHashCode(int proposalChildHashCode) {
        this.proposalChildHashCode = proposalChildHashCode;
    }




    /**
     * Gets the proposalUpdateTimestamp attribute. 
     * @return Returns the proposalUpdateTimestamp.
     */
    public Timestamp getProposalUpdateTimestamp() {
        return proposalUpdateTimestamp;
    }




    /**
     * Sets the proposalUpdateTimestamp attribute value.
     * @param proposalUpdateTimestamp The proposalUpdateTimestamp to set.
     */
    public void setProposalUpdateTimestamp(Timestamp proposalUpdateTimestamp) {
        this.proposalUpdateTimestamp = proposalUpdateTimestamp;
    }




    /**
     * Gets the propScienceKeywords attribute. 
     * @return Returns the propScienceKeywords.
     */
    public List<PropScienceKeyword> getPropScienceKeywords() {
        return propScienceKeywords;
    }




    /**
     * Sets the propScienceKeywords attribute value.
     * @param propScienceKeywords The propScienceKeywords to set.
     */
    public void setPropScienceKeywords(List<PropScienceKeyword> propScienceKeywords) {
        this.propScienceKeywords = propScienceKeywords;
    }




    /**
     * Gets the principalInvestigator attribute. 
     * @return Returns the principalInvestigator.
     */
    public ProposalPerson getPrincipalInvestigator() {
        return principalInvestigator;
    }




    /**
     * Sets the principalInvestigator attribute value.
     * @param principalInvestigator The principalInvestigator to set.
     */
    public void setPrincipalInvestigator(ProposalPerson principalInvestigator) {
        this.principalInvestigator = principalInvestigator;
    }




    /**
     * Gets the investigators attribute. 
     * @return Returns the investigators.
     */
    public List<ProposalPerson> getInvestigators() {
        return investigators;
    }




    /**
     * Sets the investigators attribute value.
     * @param investigators The investigators to set.
     */
    public void setInvestigators(List<ProposalPerson> investigators) {
        this.investigators = investigators;
    }




    /**
     * Gets the proposalPersons attribute. 
     * @return Returns the proposalPersons.
     */
    public List<ProposalPerson> getProposalPersons() {
        return proposalPersons;
    }




    /**
     * Sets the proposalPersons attribute value.
     * @param proposalPersons The proposalPersons to set.
     */
    public void setProposalPersons(List<ProposalPerson> proposalPersons) {
        this.proposalPersons = proposalPersons;
    }




    /**
     * Gets the propSpecialReviews attribute. 
     * @return Returns the propSpecialReviews.
     */
    public List<ProposalSpecialReview> getPropSpecialReviews() {
        return propSpecialReviews;
    }




    /**
     * Sets the propSpecialReviews attribute value.
     * @param propSpecialReviews The propSpecialReviews to set.
     */
    public void setPropSpecialReviews(List<ProposalSpecialReview> propSpecialReviews) {
        this.propSpecialReviews = propSpecialReviews;
    }




    /**
     * Gets the narratives attribute. 
     * @return Returns the narratives.
     */
    public List<Narrative> getNarratives() {
        return narratives;
    }




    /**
     * Sets the narratives attribute value.
     * @param narratives The narratives to set.
     */
    public void setNarratives(List<Narrative> narratives) {
        this.narratives = narratives;
    }




    @Override
    protected LinkedHashMap toStringMapper() {
        // TODO Auto-generated method stub
        return null;
    }




    /**
     * @see org.kuali.kra.proposaldevelopment.hierarchy.HierarchyChildComparable#hierarchyChildHashCode()
     */
    public int hierarchyChildHashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((investigators == null) ? 0 : investigators.hashCode());
        result = prime * result + ((narratives == null) ? 0 : narratives.hashCode());
        result = prime * result + ((principalInvestigator == null) ? 0 : principalInvestigator.hashCode());
        result = prime * result + ((propScienceKeywords == null) ? 0 : propScienceKeywords.hashCode());
        result = prime * result + ((propSpecialReviews == null) ? 0 : propSpecialReviews.hashCode());
        result = prime * result + ((proposalNumber == null) ? 0 : proposalNumber.hashCode());
        result = prime * result + ((proposalPersons == null) ? 0 : proposalPersons.hashCode());
        return result;
    }

    

}
