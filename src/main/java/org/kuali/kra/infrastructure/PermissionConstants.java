/*
 * Copyright 2006-2009 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.osedu.org/licenses/ECL-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kra.infrastructure;

/**
 * The set of all Permissions used by KRA.
 *
 * @author Kuali Research Administration Team (kualidev@oncourse.iu.edu)
 */
public interface PermissionConstants {
    
    /* Proposal/Budget Permissions */
    
    public static final String CREATE_PROPOSAL = "Create ProposalDevelopmentDocument";
    public static final String MODIFY_PROPOSAL = "Modify ProposalDevelopmentDocument";
    public static final String VIEW_PROPOSAL = "View Proposal";
    public static final String MODIFY_NARRATIVE = "Modify Narrative";
    public static final String VIEW_NARRATIVE = "View Narratives";
    public static final String MODIFY_BUDGET = "Modify Budget";
    public static final String VIEW_BUDGET = "View Budget";
    public static final String MAINTAIN_PROPOSAL_ACCESS = "Modify ProposalPermissions";
    public static final String CERTIFY = "Certify";
    public static final String PRINT_PROPOSAL = "Print Proposal";
    public static final String ALTER_PROPOSAL_DATA = "Alter Proposal Data";
    public static final String SUBMIT_TO_SPONSOR = "Submit to Sponsor";
    public static final String SUBMIT_PROPOSAL = "Submit ProposalDevelopmentDocument";
    public static final String ADD_PROPOSAL_VIEWER = "Add Proposal Viewer";
    
    /* IRB Permissions */
    
    public static final String CREATE_PROTOCOL = "Create ProtocolDocument";
    public static final String MODIFY_PROTOCOL = "Modify Protocol";
    public static final String VIEW_PROTOCOL = "View Protocol";
    public static final String SUBMIT_PROTOCOL = "Submit Protocol";
    public static final String MAINTAIN_PROTOCOL_ACCESS = "Modify ProtocolPermissions";
    public static final String ADD_PROTOCOL_NOTES = "Add Notes";
    public static final String CREATE_AMMENDMENT = "Create Ammendment";
    public static final String CREATE_RENEWAL = "Create Renewal";
    public static final String MAINTAIN_PROTOCOL_RELATED_PROJ = "MAINTAIN_PROTOCOL_RELATED_PROJ";
    public static final String EDIT_PROTOCOL_BILLABLE = "EDIT_PROTOCOL_BILLABLE";
    public static final String ADMINSTRATIVE_CORRECTION = "Administrative Correction";
    public static final String MAINTAIN_IRB_CORRESP_TEMPLATE = "MAINTAIN_IRB_CORRESP_TEMPLATE";
    public static final String MAINTAIN_PROTOCOL_SUBMISSIONS = "Maintain Protocol Submissions";
    public static final String MAINTAIN_PROTO_REVIEW_COMMENTS = "MAINTAIN_PROTO_REVIEW_COMMENTS";
    public static final String PERFORM_IRB_ACTIONS_ON_PROTO = "Perform IRB Actions on a Protocol";
    public static final String VIEW_RESTRICTED_NOTES = "VIEW_RESTRICTED_NOTES";
    
    /*
     * Committee Permissions
     */
    public static final String ADD_COMMITTEE = "Create CommitteeDocument";
    public static final String VIEW_COMMITTEE = "View Committee";
    public static final String MODIFY_COMMITTEE = "Modify Committee";
    public static final String GENERATE_MINUTES = "Generate Minutes";
    public static final String GENERATE_SCHEDULE = "Generate Schedule";
    public static final String MODIFY_SCHEDULE = "Modify Schedule";
    public static final String MAINTAIN_MEMBERSHIPS = "Maintain Memberships";
    public static final String MAINTAIN_MINUTES = "Maintain Minutes";
    public static final String GENERATE_AGENDA = "Generate Agenda";
    public static final String VIEW_SCHEDULE = "VIEW_SCHEDULE";

    
    /*
     * Questionnaire Permissions
     */
    public static final String VIEW_QUESTION = "VIEW_QUESTION";
    public static final String MODIFY_QUESTION = "MODIFY_QUESTION";
    public static final String VIEW_QUESTIONNAIRE = "VIEW_QUESTIONNAIRE";
    public static final String MODIFY_QUESTIONNAIRE = "MODIFY_QUESTIONNAIRE";
}
