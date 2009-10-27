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
package org.kuali.kra.proposaldevelopment.hierarchy.service;

import java.util.List;

import org.kuali.kra.proposaldevelopment.bo.DevelopmentProposal;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.proposaldevelopment.hierarchy.ProposalHierarchyErrorDto;
import org.kuali.kra.proposaldevelopment.hierarchy.ProposalHierarchyException;
import org.kuali.kra.proposaldevelopment.hierarchy.bo.HierarchyProposalSummary;
import org.kuali.rice.kns.bo.DocumentHeader;
import org.kuali.rice.kns.workflow.service.KualiWorkflowDocument;



/**
 * This class...
 */

public interface ProposalHierarchyService {

    //Constants for Proposal Hierarchy Child Routing
    public static final String PROPOSAL_HIERARCHY_PARENT_ENROUTE="Parent Enroute";
    public static final String PROPOSAL_HIERARCHY_PARENT_CANCEL="Parent Cancel";
    public static final String PROPOSAL_HIERARCHY_PARENT_DISAPPROVE="Parent Disapproved";
    public static final String PROPOSAL_HIERARCHY_PARENT_FINAL="Parent Final";

    /**
     * This method takes a proposal, creates a Hierarchy
     * and links the proposal as the initial child.
     * 
     * @param initialChildProposal 
     * @return the proposal number of the new hierarchy
     * @throws ProposalHierarchyException if the proposal is already a member of a hierarchy
     */
    public String createHierarchy(DevelopmentProposal initialChild) throws ProposalHierarchyException;

    /**
     * This method links a proposal to a Hierarchy.
     * 
     * @param hierarchyProposal the hierarchy to link the new child to
     * @param newChildProposal the proposal to link to the hierarchy
     * @throws ProposalHierarchyException if hierarchyProposal is not a valid Hierarchy
     * or if newChildProposal is already a member of a hierarchy or does not exist
     */
    public void linkToHierarchy(DevelopmentProposal hierarchyProposal, DevelopmentProposal newChildProposal) throws ProposalHierarchyException;

    /**
     * This method removes childProposal from the hierarchy of which it is a member
     * 
     * @param childProposal the proposal to remove
     * @throws ProposalHierarchyException if childProposal is not a member of a hierarchy
     */
    public void removeFromHierarchy(DevelopmentProposal childProposal) throws ProposalHierarchyException;

    /**
     * This method synchronizes the contents of one child into its hierarchy.  If the child has changed since its last synchronization, the parent is reaggregated.
     * @param childProposal the child proposal in question
     * @throws ProposalHierarchyException if childProposal is not a member of a hierarchy
     */
    public void synchronizeChild(DevelopmentProposal childProposal) throws ProposalHierarchyException;
    
    /**
     * This method synchronizes the contents of all children into the hierarchy.  If any child has changed since its last synchronization, the parent is reaggregated.
     * @param hierarchyProposal the hierarchy in question
     * @throws ProposalHierarchyException if hierarchyProposal is not a valid Hierarchy
     */
    public void synchronizeAllChildren(DevelopmentProposal hierarchyProposal) throws ProposalHierarchyException;

    public DevelopmentProposal getDevelopmentProposal(String proposalNumber);
    public DevelopmentProposal lookupParent(DevelopmentProposal childProposal) throws ProposalHierarchyException;
    public List<HierarchyProposalSummary> getProposalSummaries(String proposalNumber) throws ProposalHierarchyException;
    public ProposalHierarchyErrorDto validateChildBudgetPeriods(DevelopmentProposal hierarchyProposal, DevelopmentProposal childProposal) throws ProposalHierarchyException;
    
    
    /**
     * Get the parent workflow document of the hierarchy child document.
     * This is a utility method.
     * 
     * @param doc The child in question
     * @return The KualiWorklowDocument of the child's parent.
     * @throws ProposalHierarchyException if the provided proposal is not in a hierarchy.
     */
    public KualiWorkflowDocument getParentWorkflowDocument( ProposalDevelopmentDocument doc ) throws ProposalHierarchyException;

    /**
     * Calculate the AppDocStatus that should be applied to children of 
     * a parent moving from oldStatus to newStatus.
     * 
     * @param oldStatus The old workflow status of the parent document.
     * @param newStatus The new workflow status of the parent document.
     * 
     * @return The AppWorkDocStatus that should be set in a children.
     */
    public String getHierarchyChildRouteCode( String oldStatus, String newStatus );

}
