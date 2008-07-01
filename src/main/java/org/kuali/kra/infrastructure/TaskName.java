/*
 * Copyright 2008 The Kuali Foundation.
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
package org.kuali.kra.infrastructure;

/**
 * Each task that a user can execute and which requires authorization is
 * enumerated here.  It must correspond to the values in the SpringBeans.xml.
 */
public interface TaskName {
    /*
     * Application Tasks.
     */
    public static final String CREATE_PROPOSAL = "createProposal";
    
    /*
     * Proposal Tasks.
     */
    public static final String MODIFY_PROPOSAL = "modifyProposal";
    public static final String VIEW_PROPOSAL = "viewProposal";
    public static final String PRINT_PROPOSAL = "printProposal";
    public static final String SUBMIT_TO_SPONSOR = "submitToSponsor";
    public static final String ADD_BUDGET = "addBudget";
    public static final String OPEN_BUDGETS = "openBudgets";
    public static final String MODIFY_PROPOSAL_ROLES = "modifyProposalRoles";
    public static final String ADD_NARRATIVE = "addNarrative";
    public static final String CERTIFY = "certify";
    public static final String ALTER_PROPOSAL_DATA = "alterProposalData";
    
    /*
     * Narrative Tasks.
     */
    public static final String MODIFY_NARRATIVE_RIGHTS = "modifyNarrativeRights";
    public static final String DOWNLOAD_NARRATIVE = "downloadNarrative";
    public static final String DELETE_NARRATIVE = "deleteNarrative";
    public static final String REPLACE_NARRATIVE = "replaceNarrative";
    
    /*
     * Budget Tasks.
     */
    public static final String MODIFY_BUDGET = "modifyBudget";
    public static final String VIEW_BUDGET = "viewBudget";
}
