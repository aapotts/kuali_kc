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
package org.kuali.kra.proposaldevelopment.hierarchy.service.impl;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.proposaldevelopment.bo.DevelopmentProposal;
import org.kuali.kra.proposaldevelopment.bo.Narrative;
import org.kuali.kra.proposaldevelopment.bo.PropScienceKeyword;
import org.kuali.kra.proposaldevelopment.bo.ProposalPerson;
import org.kuali.kra.proposaldevelopment.bo.ProposalPersonBiography;
import org.kuali.kra.proposaldevelopment.bo.ProposalSpecialReview;
import org.kuali.kra.proposaldevelopment.hierarchy.ProposalHierarchyException;
import org.kuali.kra.proposaldevelopment.hierarchy.bo.ProposalHierarchy;
import org.kuali.kra.proposaldevelopment.hierarchy.bo.ProposalHierarchyChild;
import org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchyService;
import org.kuali.rice.kns.util.ObjectUtils;

/**
 * This class...
 */
public class ProposalHierarchyServiceImpl implements ProposalHierarchyService {

    /**
     * @see org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchyService#createHierarchy(java.lang.String)
     */
    public String createHierarchy(String initialChildProposalNumber) throws ProposalHierarchyException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchyService#getHierarchyProposal(java.lang.String)
     */
    public String getHierarchyProposal(String childProposalNumber) throws ProposalHierarchyException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchyService#isChild(java.lang.String)
     */
    public boolean isChild(String proposalNumber) throws ProposalHierarchyException {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchyService#isParent(java.lang.String)
     */
    public boolean isParent(String proposalNumber) throws ProposalHierarchyException {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchyService#linkToHierarchy(java.lang.String, java.lang.String)
     */
    public void linkToHierarchy(String hierarchyProposalNumber, String newChildProposalNumber) throws ProposalHierarchyException,
            ProposalHierarchyException {
        // TODO Auto-generated method stub

    }

    /**
     * @see org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchyService#removeFromHierarchy(java.lang.String)
     */
    public void removeFromHierarchy(String childProposalNumber) throws ProposalHierarchyException {
        // TODO Auto-generated method stub

    }


    /**
     * @see org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchySyncService#synchronizeAllChildren(java.lang.String)
     */
    public void synchronizeAllChildren(String hierarchyProposalNumber) throws ProposalHierarchyException {
        // TODO get hierarchy
        ProposalHierarchy hierarchy = null;
        boolean changed = false;
        
        for (ProposalHierarchyChild child : hierarchy.getChildren()) {
            changed &= synchronizeChild(child.getProposalNumber(), false);
        }
        if (changed) aggregateHierarchy(hierarchyProposalNumber);
    }

    /**
     * @see org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchySyncService#synchronizeChild(java.lang.String)
     */
    public void synchronizeChild(String childProposalNumber) throws ProposalHierarchyException {
        synchronizeChild(childProposalNumber, true);

    }
    
    @SuppressWarnings("unchecked")
    private boolean synchronizeChild(String childProposalNumber, boolean performAggregation) throws ProposalHierarchyException {
        String hierarchyProposalNumber = getHierarchyProposal(childProposalNumber);
        // TODO get hierarchy
        ProposalHierarchy hierarchy = null;
        ProposalHierarchyChild hierarchyChild = null;
        
        for (ProposalHierarchyChild child : hierarchy.getChildren()) {
            if (StringUtils.equalsIgnoreCase(child.getProposalNumber(), childProposalNumber)) {
                hierarchyChild = child;
                break;
            }
        }
        if (hierarchyChild == null) throw new ProposalHierarchyException("Error finding child in hierarchy");
        
        // TODO get child Development Proposal
        DevelopmentProposal childProposal = null;
        
        if (childProposal.getUpdateTimestamp().equals(hierarchyChild.getProposalUpdateTimestamp())
                || childProposal.hierarchyChildHashCode() == hierarchyChild.getProposalChildHashCode()) {
            return false;
        }
        
        hierarchyChild.setPropScienceKeywords((List<PropScienceKeyword>) cloneAndUpdateProposalNumber(childProposal.getPropScienceKeywords(), hierarchyProposalNumber));
        hierarchyChild.setPrincipalInvestigator((ProposalPerson) cloneAndUpdateProposalNumber(childProposal.getPrincipalInvestigator(), hierarchyProposalNumber));
        hierarchyChild.setInvestigators((List<ProposalPerson>) cloneAndUpdateProposalNumber(childProposal.getInvestigators(), hierarchyProposalNumber));
        hierarchyChild.setProposalPersons((List<ProposalPerson>) cloneAndUpdateProposalNumber(childProposal.getProposalPersons(), hierarchyProposalNumber));
        hierarchyChild.setPropSpecialReviews((List<ProposalSpecialReview>) cloneAndUpdateProposalNumber(childProposal.getPropSpecialReviews(), hierarchyProposalNumber));
        hierarchyChild.setNarratives((List<Narrative>) cloneAndUpdateProposalNumber(childProposal.getNarratives(), hierarchyProposalNumber));
        removeNonExclusiveNarratives(hierarchyChild.getNarratives());
        
        hierarchyChild.setProposalUpdateTimestamp(new Timestamp(childProposal.getUpdateTimestamp().getTime()));
        hierarchyChild.setProposalChildHashCode(childProposal.hierarchyChildHashCode());
        
        
        if (performAggregation) {
            aggregateHierarchy(hierarchyProposalNumber);
        }
        
        return true;
    }

    /**
     * @see org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchySyncService#aggregateHierarchy(java.lang.String)
     */
    public void aggregateHierarchy(String hierarchyProposalNumber) throws ProposalHierarchyException {
        // TODO get hierarchy
        ProposalHierarchy hierarchy = null;
        
        if (hierarchy == null) throw new ProposalHierarchyException("Hierarchy number " + hierarchyProposalNumber + " not found");

        ProposalPerson oldPrincipalInvestigator = hierarchy.getPrincipalInvestigator();
        hierarchy.setPrincipalInvestigator(null);
        hierarchy.getInvestigators().clear();
        hierarchy.getProposalPersons().clear();
        hierarchy.getPropScienceKeywords().clear();
        removeNonExclusiveNarratives(hierarchy.getNarratives());
        hierarchy.getPropSpecialReviews().clear();
        
        for (Narrative narrative : hierarchy.getHierarchyNarratives()) {
            hierarchy.addNarrative(narrative);
        }
        for (PropScienceKeyword keyword : hierarchy.getHierarchyPropScienceKeywords()) {
            hierarchy.addPropScienceKeyword(keyword);
        }
        for (ProposalSpecialReview review : hierarchy.getHierarchySpecialReviews()) {
            hierarchy.getPropSpecialReviews().add(review);
        }
        
        for (ProposalHierarchyChild child : hierarchy.getChildren()) {
            for (Narrative narrative : child.getNarratives()) {
                hierarchy.addNarrative(narrative);
            }
            for (ProposalSpecialReview review : child.getPropSpecialReviews()) {
                hierarchy.getPropSpecialReviews().add(review);
            }
            for (PropScienceKeyword keyword : child.getPropScienceKeywords()) {
                if (!hierarchy.getPropScienceKeywords().contains(keyword)) {
                    hierarchy.addPropScienceKeyword(keyword);
                }
            }
            if (!hierarchy.getInvestigators().contains(child.getPrincipalInvestigator())) {
                hierarchy.getInvestigators().add(child.getPrincipalInvestigator());
            }
            for (ProposalPerson investigator : child.getInvestigators()) {
                if (!hierarchy.getInvestigators().contains(investigator)) {
                    hierarchy.getInvestigators().add(investigator);
                }
            }
            for (ProposalPerson keyPerson : child.getProposalPersons()) {
                if (!hierarchy.getProposalPersons().contains(keyPerson)) {
                    hierarchy.getProposalPersons().add(keyPerson);
                }
            }
        }
        if (hierarchy.getInvestigators().contains(oldPrincipalInvestigator)) {
            hierarchy.getInvestigators().remove(oldPrincipalInvestigator);
            hierarchy.setPrincipalInvestigator(oldPrincipalInvestigator);
        }
        else if (!hierarchy.getInvestigators().isEmpty()) {
            hierarchy.setPrincipalInvestigator(hierarchy.getInvestigators().remove(0));
        }
        String personId;
        for (int i=hierarchy.getPropPersonBios().size()-1; i>=0; i--) {
            personId = hierarchy.getPropPersonBios().get(i).getPersonId();
            if(!hierarchy.getPrincipalInvestigator().equals(personId) && !hierarchy.getInvestigators().contains(personId)) {
                hierarchy.getPropPersonBios().remove(i);
            }
        }
        
    }
    
    private void removeNonExclusiveNarratives(List<Narrative> narratives) {
        Narrative narrative;
        for (int i = narratives.size()-1; i >= 0; i--) {
            narrative = narratives.get(i);
            if (StringUtils.equalsIgnoreCase(narrative.getNarrativeType().getAllowMultiple(), "Y")) {
                narratives.remove(i);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    private Object cloneAndUpdateProposalNumber(Object o, String proposalNumber) {
        Object newO = ObjectUtils.deepCopy((Serializable)o);
        if (newO instanceof List<?>) {
            List<Object> oList = (List<Object>)newO;
            for (Object obj : oList) {
                updateProposalNumber(obj, proposalNumber); 
            }
        }
        else {
            updateProposalNumber(newO, proposalNumber);
        }
        return newO;
    }
    
    private void updateProposalNumber(Object o, String proposalNumber) {
        Method[] methods = o.getClass().getDeclaredMethods();
        for (Method method : methods) {
            try {
                if (method.getName().equalsIgnoreCase("setProposalNumber")) {
                    method.invoke(o, proposalNumber);
                }
            } catch (Throwable e) {}
        }
    }
}
