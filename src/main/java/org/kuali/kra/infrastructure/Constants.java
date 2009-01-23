/*
 * Copyright 2006-2008 The Kuali Foundation
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

public interface Constants {
    public static final String KRA_SESSION_KEY = "kra.session";
    public static final String APP_CONTEXT_KEY = "app.context.name";
    public static final String DATASOURCE = "kraDataSource";
    public static final String DATA_DICTIONARY_SERVICE_NAME = "dataDictionaryService";
    public static final String BUSINESS_OBJECT_DICTIONARY_SERVICE_NAME = "businessObjectDictionaryService";
    public static final String DATE_TIME_SERVICE_NAME = "dateTimeService";
    public static final String BUSINESS_OBJECT_DAO_NAME = "businessObjectDao";
    public static final String HTML_FORM_ACTION = "htmlFormAction";
    public static final String TEXT_AREA_FIELD_NAME = "textAreaFieldName";
    public static final String TEXT_AREA_FIELD_LABEL = "textAreaFieldLabel";
    public static final String TEXT_AREA_FIELD_ANCHOR = "textAreaFieldAnchor";
    public static final Integer APPROVAL_STATUS = 2;
    public static final String MAINTENANCE_NEW_ACTION = "New";

    public static final String KEY_PERSON_ROLE = "KP";
    public static final String PRINCIPAL_INVESTIGATOR_ROLE = "PI";
    public static final String CO_INVESTIGATOR_ROLE = "COI";
    public static final String MULTIPLE_VALUE = "multipleValues";
    public static final String KEYWORD_PANEL_DISPLAY = "proposaldevelopment.displayKeywordPanel";

    public static final String MAPPING_BASIC = "basic";
    public static final String MAPPING_ERROR = "error";
    public static final String MAPPING_PROPOSAL_ACTIONS = "actions";
    public static final String NEW_PROPOSAL_PERSON_PROPERTY_NAME = "newProposalPerson";
    public static final String NEW_PERSON_LOOKUP_FLAG = "newPersonLookupFlag";
    public static final String MAPPING_CLOSE_PAGE = "closePage";
    public static final String MAPPING_NARRATIVE_ATTACHMENT_RIGHTS_PAGE = "attachmentRights";
    public static final String MAPPING_INSTITUTE_ATTACHMENT_RIGHTS_PAGE = "instituteAttachmentRights";
 
    public static final String CREDIT_SPLIT_ENABLED_RULE_NAME = "proposaldevelopment.creditsplit.enabled";
    public static final String CREDIT_SPLIT_ENABLED_FLAG = "creditSplitEnabledFlag";

    public static final String NARRATIVE_MODULE_NUMBER = "proposalDevelopment.narrative.moduleNumber";
    public static final String NARRATIVE_MODULE_SEQUENCE_NUMBER = "proposalDevelopment.narrative.moduleSequenceNumber";
    public static final String PROP_PERSON_BIO_NUMBER = "proposalDevelopment.proposalPersonBiography.biographyNumber";
    public static final String PROPOSAL_LOCATION_SEQUENCE_NUMBER = "proposalDevelopment.proposalLocation.locationSequenceNumber";
    public static final String PROPOSAL_SPECIALREVIEW_NUMBER = "proposalDevelopment.proposalSpecialReview.specialReviewNumber";
    public static final String PROPOSAL_PERSON_DEGREE_SEQUENCE_NUMBER = "proposalDevelopment.proposalPerson.degree.degreeSequenceNumber";
    public static final String PROPOSAL_PERSON_NUMBER = "proposalDevelopment.proposalPerson.proposalPersonNumber";
    public static final String PROPOSAL_NARRATIVE_TYPE_GROUP = "proposalNarrativeTypeGroup";
    public static final String INSTITUTE_NARRATIVE_TYPE_GROUP = "instituteNarrativeTypeGroup";
    public static final String INSTITUTIONAL_ATTACHMENT_TYPE_NAME = "Institutional Attachment";
    public static final String PERSONNEL_ATTACHMENT_TYPE_NAME = "Personnel Attachment";
    public static final String PROPOSAL_ATTACHMENT_TYPE_NAME = "Proposal Attachment";
    public static final String NEW_NARRATIVE_USER_RIGHTS_PROPERTY_KEY = "newNarrativeUserRight";

    public static final String PROPOSAL_PERSON_ROLE_PARAMETER_PREFIX = "proposaldevelopment.personrole.";
    public static final String NIH_SPONSOR_ACRONYM = "NIH";
    public static final String NIH_SPONSOR_CODE = "000340";
    public static final String SPONSOR_LEVEL_HIERARCHY = "sponsorLevelHierarchy";
    public static final String SPONSOR_HIERARCHY_NAME = "sponsorGroupHierarchyName";
    
    public static final String PARAMETER_MODULE_PROTOCOL = "KC-PROTOCOL";
    public static final String PARAMETER_MODULE_PROTOCOL_REFERENCEID1 = "irb.protocol.referenceID1";
    public static final String PARAMETER_MODULE_PROTOCOL_REFERENCEID2 = "irb.protocol.referenceID2";
    public static final String PARAMETER_MODULE_PROPOSAL_DEVELOPMENT = "KRA-PD";
    public static final String PARAMETER_COMPONENT_DOCUMENT = "D";
    public static final String INSTITUTE_NARRATIVE_TYPE_GROUP_CODE = "O";
    public static final String NARRATIVE_MODULE_STATUS_COMPLETE = "C";

    public static final String ABSTRACTS_PROPERTY_KEY = "newProposalAbstract";
    public static final String SPONSOR_PROPOSAL_NUMBER_PROPERTY_KEY = "sponsorProposalNumber";
    public static final String DEADLINE_DATE_KEY = "document.deadlineDate";
    public static final String PROJECT_TITLE_KEY = "document.title";
    public static final String SPONSOR_PROPOSAL_NUMBER_LABEL = "Sponsor Proposal ID";

    public static final String AUDIT_ERRORS = "Validation Errors";
    public static final String AUDIT_WARNINGS = "Warnings";
    public static final String GRANTSGOV_ERRORS= "Grants.Gov Errors";
    
    public static final String PROPOSAL_PAGE = "proposal";
    public static final String CUSTOM_ATTRIBUTES_PAGE = "customData";
    public static final String QUESTIONS_PAGE = "questions";
    public static final String PERMISSIONS_PAGE = "permissions";
    public static final String PROPOSAL_ACTIONS_PAGE = "actions";
    public static final String ATTACHMENTS_PAGE = "abstractsAttachments";
    public static final String SPONSOR_PROGRAM_INFORMATION_PANEL_ANCHOR = "SponsorProgramInformation";
    public static final String SPONSOR_PROGRAM_INFORMATION_PANEL_NAME = "Sponsor & Program Information";
    public static final String REQUIRED_FIELDS_PANEL_ANCHOR = "RequiredFieldsforSavingDocument";
    public static final String REQUIRED_FIELDS_PANEL_NAME = "Required Fields for Saving Document ";
    
    public static final String BUDGET_EXPENSES_PAGE = "budgetExpenses";
    public static final String BUDGET_EXPENSES_OVERVIEW_PANEL_ANCHOR = "BudgetOvervieV";
    public static final String BUDGET_EXPENSES_OVERVIEW_PANEL_NAME = "Budget Overview (Period ";
    // Proposal Document Property Constants
    public static final String PROPOSAL_NUMBER = "proposalNumber";
    public static final String BUDGET_VERSION_PANEL_NAME = "Budget Versions";
    public static final String BUDGET_VERSION_OVERVIEWS = "budgetVersionOverviews";
    
    // Budget Document Property Constants
    public static final String BUDGET_VERSION_NUMBER = "budgetVersionNumber";

    // Key Personnel Mojo
    public static final String KEY_PERSONNEL_PAGE = "keyPersonnel";
    public static final String PROPOSAL_PERSON_INVESTIGATOR = "investigator";
    public static final String KEY_PERSONNEL_PANEL_ANCHOR = "KeyPersonnel";
    public static final String KEY_PERSONNEL_PANEL_NAME = "Key Personnel Information";
    public static final String PERSON_EDITABLE_FIELD_NAME_PROPERTY_KEY = "fieldName";
    public static final String INVESTIGATOR_CREDIT_TYPE_CODE_PROPERTY_KEY = "invCreditTypeCode";
    public static final String EMPTY_STRING = "";
    public static final String PROPOSAL_PERSON_KEY = "document.proposalPerson*";
    public static final String PRINCIPAL_INVESTIGATOR_KEY = "newProposalPerson*";
    public static final String CREDIT_SPLIT_KEY = "document.creditSplit";

   
    /* set values for ynq */
    public static final Integer ANSWER_YES_NO = 2;
    public static final Integer ANSWER_YES_NO_NA = 3;
    public static final String QUESTION_TYPE_PROPOSAL = "P";
    public static final String QUESTION_TYPE_INDIVIDUAL = "I"; // Investigator - Certification questions
    public static final String YNQ_EXPLANATION_REQUIRED = "Explanation required: if answer = ";
    public static final String YNQ_REVIEW_DATE_REQUIRED = "Date required: if answer = ";
    public static final String QUESTION_STATUS_ACTIVE = "A";
    public static final String ANSWER_NA = "X";

    public static final String DEFAULT_DATE_FORMAT_PATTERN = "MM/dd/yyyy";
    public static final String PARAMETER_MODULE_BUDGET = "KRA-B";
    
    // Budget Versions Constants
    public static final String BUDGET_STATUS_COMPLETE_CODE = "budgetStatusCompleteCode";
    public static final String BUDGET_STATUS_INCOMPLETE_CODE = "budgetStatusIncompleteCode";
    public static final String PROPOSAL_BUDGET_VERSION_NUMBER = "proposalDevelopment.budget.versionNumber";
    public static final String BUDGET_DEFAULT_OVERHEAD_RATE_CODE = "defaultOverheadRateClassCode";
    public static final String BUDGET_DEFAULT_OVERHEAD_RATE_TYPE_CODE = "defaultOverheadRateTypeCode";
    public static final String BUDGET_DEFAULT_UNDERRECOVERY_RATE_CODE = "defaultUnderrecoveryRateClassCode";
    public static final String BUDGET_DEFAULT_MODULAR_FLAG = "defaultModularFlag";
    
    // Budget Distribution And Income
    public static final String BUDGET_CURRENT_FISCAL_YEAR = "budgetCurrentFiscalYear";
    public static final String BUDGET_COST_SHARING_APPLICABILITY_FLAG = "budgetCostSharingApplicabilityFlag";
    public static final String BUDGET_UNRECOVERED_F_AND_A_APPLICABILITY_FLAG = "budgetUnrecoveredFandAApplicabilityFlag";
    
    // Budget Personnel
    public static final String BUDGET_PERSON_DEFAULT_JOB_CODE = "budgetPersonDefaultJobCode";
    public static final String BUDGET_PERSON_DEFAULT_APPOINTMENT_TYPE = "budgetPersonDefaultAppointmentType";
    public static final String BUDGET_PERSON_DEFAULT_PERIOD_TYPE = "budgetPersonDefaultPeriodType";
    public static final String BUDGET_PERSON_DEFAULT_CALCULATION_BASE = "budgetPersonDefaultCalculationBase";
    public static final String BUDGET_PERSON_DEFAULT_EFFECTIVE_DATE = "budgetPersonDefaultEffectiveDate";
    public static final String PERSON_SEQUENCE_NUMBER = "personSequenceNumber";
    public static final String BUDGET_PERSONNEL_PAGE = "personnel";
    public static final String JOB_CODE = "jobCode";
    
    // KIM Authorization Namespace for KRA
    public static final String KRA_NAMESPACE = "KRA";
    
    // Key Permissions Info
    public static final String CONFIRM_DELETE_PROPOSAL_USER_KEY = "confirmDeleteProposalUser";
    public static final String PERMISSION_USERS_PROPERTY_KEY = "newProposalUser";
    public static final String EDIT_ROLES_PROPERTY_KEY = "proposalUserEditRole";
    public static final String MAPPING_PERMISSIONS_ROLE_RIGHTS_PAGE = "permissionsRoleRights";
    public static final String MAPPING_PERMISSIONS_EDIT_ROLES_PAGE = "permissionsEditRoles";
    public static final String MAPPING_PERMISSIONS_CLOSE_EDIT_ROLES_PAGE = "permissionsCloseEditRoles";
    
    // Task Authorization
    public static final String TASK_AUTHORIZATION = "taskAuthorization";

    //Budget Rates
    public static final String ON_CAMUS_FLAG = "N";
    public static final String OFF_CAMUS_FLAG = "F";
    public static final String DEFALUT_CAMUS_FLAG = "D";
    
    //Budget Expenses
    public static final String BUDGET_LINEITEM_NUMBER = "budget.budgetLineItem.lineItemNumber";
    public static final String BUDGET_EXPENSE_LOOKUP_MESSAGE1 = "budget.expense.lookup.message1";
    public static final String BUDGET_EXPENSE_LOOKUP_MESSAGE2 = "budget.expense.lookup.message2";
    public static final String PERCENT_EFFORT_FIELD = "Percent Effort";
    public static final String PERCENT_CHARGED_FIELD = "Percent Charged";
    
    //Grants.gov
    public static final String S2S_SUBMISSIONTYPE_CODE_KEY="document.s2sOpportunity.s2sSubmissionTypeCode";
    public static final String GRANTS_GOV_PANEL_ANCHOR  = "Opportunity";
    public static final String GRANTS_GOV_OPPORTUNITY_PANEL = "GrantsGov";
    public static final String OPPORTUNITY_ID_KEY="document.programAnnouncementNumber";
    public static final String OPPORTUNITY_TITLE_KEY="document.programAnnouncementTitle";
    public static final String CFDA_NUMBER_KEY="document.cfdaNumber";
    public static final String GRANTS_GOV_PAGE = "grantsGov";
    public static final String ORIGINAL_PROPOSAL_ID_KEY = "document.sponsorProposalNumber";
    public static final String CFDA_NUMBER = "cfdaNumber";
    public static final String OPPORTUNITY_ID= "opportunityId";
    public static final String NO_FIELD= "noField";
    public static final String GRANTS_GOV_LINK="message.grantsgov.link";
    public static final String GRANTS_GOV_GENERIC_ERROR_KEY= "error.grantsgov.schemavalidation.generic.errorkey";
    public static final String GRANTS_GOV_SUBMISSION_SUCCESSFUL_MESSAGE = "message.grantsGov.submission.successful";
    
    
    // custom attribute
    public static final String CUSTOM_ATTRIBUTE_ID = "customAttributeId";
    public static final String DOCUMENT_NEWMAINTAINABLEOBJECT_ACTIVE = "document.newMaintainableObject.active";
    public static final String DOCUMENT_NEWMAINTAINABLEOBJECT_LOOKUPRETURN = "document.newMaintainableObject.lookupReturn";
    public static final String LOOKUP_RETURN_FIELDS = "lookupReturnFields";
    public static final String LOOKUP_CLASS_NAME = "lookupClassName";

    public static final String MAPPING_PERSONNEL_BUDGET = "personnelBudget";
    public static final String MAPPING_EXPENSES_BUDGET = "expenses";
    public static final String BUDGET_PERSON_LINE_NUMBER = "budget.budgetPersonnelDetails.personNumber";
    public static final String BUDGET_PERSON_LINE_SEQUENCE_NUMBER = "budget.budgetPersonnelDetails.sequenceNumber";
    
    public static final String DATA_TYPE_STRING = "String - Any Character";
    public static final String DATA_TYPE_NUMBER = "Number - [0-9]";
    public static final String DATA_TYPE_DATE = "Date - [xx/xx/xxxx]";
    
   
    // Change Password
    public static final String CHANGE_PASSWORD_PROPERTY_KEY = "changePassword";
    
    public static final String TRUE_FLAG = "Y";
    public static final String FALSE_FLAG = "N";
    public static final String PROPOSAL_SPECIAL_REVIEW_KEY = "document.proposalSpecialReview*";
    public static final String SPECIAL_REVIEW_PAGE = "specialReview";
    public static final String SPECIAL_REVIEW_PANEL_ANCHOR = "SpecialReview";
    public static final String SPECIAL_REVIEW_PANEL_NAME = "Special Review Information";
    public static final String BUDGET_PERIOD_PANEL_NAME = "Budget Period And Totals Information";
    public static final String BUDGET_RATE_PANEL_NAME = "Budget Rate";
    public static final String BUDGET_PERIOD_PAGE = "summary";
    public static final String BUDGET_RATE_PAGE = "budgetRate";
    public static final String BUDGET_VERSIONS_PAGE = "budgetVersions";
    public static final String PD_BUDGET_VERSIONS_PAGE = "budgetVersions";
    public static final String BUDGET_PERIOD_PANEL_ANCHOR = "BudgetPeriodsAmpTotals";
    public static final String BUDGET_RATE_PANEL_ANCHOR = "BudgetProposalRate";
    public static final String BUDGET_VERSIONS_PANEL_ANCHOR = "BudgetVersions";
    public static final String BUDGET_PERIOD_KEY = "document.budgetPeriod*";
    
    // Copy proposal
    public static final String COPY_PROPOSAL_PROPERTY_KEY = "copyProposal";
    
    public static final String MAPPING_COPY_PROPOSAL_PAGE = "copyProposal";
    public static final String HEADER_TAB = "headerTab";
    
    public static final String ON_OFF_CAMPUS_FLAG = "onOffCampusFlag";

    // Budget Rates
    public static final int APPLICABLE_RATE_PRECISION = 3;
    public static final int APPLICABLE_RATE_SCALE = 2;
    public static final String APPLICABLE_RATE_LIMIT = "999.99";
    public static final String APPLICABLE_RATE_DECIMAL_CHAR = ".";
    
    // Modular Budget
    public static final String PARAMETER_FNA_COST_ELEMENTS = "consortiumFnaCostElements";
    public static final String PARAMETER_FNA_RATE_CLASS_TYPE = "fnaRateClassTypeCode";
    
    public static final String PROPOSALDATA_OVERRIDE_PROPERTY_KEY = "newProposalChangedData";
    public static final String PROPOSALDATA_CHANGED_VAL_KEY = "newProposalChangedData.changedValue";
    public static final String PROPOSALDATA_DISPLAY_VAL_KEY = "newProposalChangedData.displayValue";
    public static final String PROPOSALDATA_CURRENT_DISPLAY_KEY = "newProposalChangedData.oldDisplayValue";
    public static final String PROPOSALDATA_COMMENTS_KEY = "newProposalChangedData.comments";

    public static final String BUDGET_SALARY_REPORT = "ProposalBudget/Salaries";
    public static final String PERSONNEL_BUDGET_PANEL_NAME = "Personnel Budget";
    public static final String INITIAL_UNIT_HIERARCHY_LOAD_DEPTH = "initialUnitLoadDepth";
    public static final String NUMBER_PER_SPONSOR_HIERARCHY_GROUP = "numberPerSponsorHierarchyGroup";
    
    public static final String PROPOSAL_EDITABLECOLUMN_DATATYPE = "document.newMaintainableObject.dataType";
    public static final String PROPOSAL_EDITABLECOLUMN_DATALENGTH = "document.newMaintainableObject.dataLength";
    public static final String PROPOSAL_EDITABLECOLUMN_LOOKUPRETURN = "document.newMaintainableObject.lookupReturn";

    public static final String PDF_REPORT_CONTENT_TYPE = "application/pdf";
    public static final String PDF_FILE_EXTENSION = ".pdf";
    public static final String GENERIC_SPONSOR_CODE = "GENERIC_SPONSOR_CODE";
    public static final String CUSTOM_ERROR = "error.custom";
    public static final String SUBAWARD_FILE_REQUIERED = "newSubAward.subAwardFile.required";
    public static final String SUBAWARD_FILE = "newSubAward.subAwardFile";
    public static final String SUBAWARD_FILE_NOT_POPULATED = "newSubAward.subAwardFile.notExtracted";
    public static final String SUBAWARD_ORG_NAME = "newSubAward.organizationName";
    public static final String SUBAWARD_ORG_NAME_REQUIERED = "newSubAward.organizationName.required";

    // sponsor hierarchy
    public static final String HIERARCHY_NAME = "hierarchyName";
    public static final String SPONSOR_CODE = "sponsorCode";
    public static final String SPONSOR_HIERARCHY_SEPARATOR_C1C = ";1;";
    public static final String SPONSOR_HIERARCHY_SEPARATOR_P1P = "#1#";
    
    //Award
    public static final String MAPPING_AWARD_BASIC = "basic";
    public static final String MAPPING_AWARD_HOME_PAGE = "home";
    public static final String MAPPING_AWARD_CONTACTS_PAGE = "contacts";
    public static final String MAPPING_AWARD_TIME_AND_MONEY_PAGE = "timeAndMoney";
    public static final String MAPPING_AWARD_PAYMENT_REPORTS_AND_TERMS_PAGE = "paymentReportsAndTerms";
    public static final String MAPPING_AWARD_SPECIAL_REVIEW_PAGE = "specialReview";
    public static final String MAPPING_AWARD_CUSTOM_DATA_PAGE = "customData";
    public static final String MAPPING_AWARD_QUESTIONS_PAGE = "questions";
    public static final String MAPPING_AWARD_PERMISSIONS_PAGE = "permissions";
    public static final String MAPPING_AWARD_NOTES_AND_ATTACHMENTS_PAGE = "notesAndAttachments";
    public static final String MAPPING_AWARD_ACTIONS_PAGE = "awardActions";
    
    public static final Integer COST_SHARE_COMMENT_TYPE_CODE = 9;
    public static final Integer FANDA_RATE_COMMENT_TYPE_CODE = 8;
    public static final Integer PREAWARD_SPONSOR_AUTHORIZATION_COMMENT_TYPE_CODE = 18;
    public static final Integer PREAWARD_INSTITUTIONAL_AUTHORIZATION_COMMENT_TYPE_CODE = 19;
    public static final Integer MIN_FISCAL_YEAR = 1900;
    public static final Integer MAX_FISCAL_YEAR = 2499;
    
    public static final String PARAMETER_MODULE_AWARD = "KC-AWARD";
    public static final String AWARD_SPECIALREVIEW_NUMBER = "AWARD_SPECIALREVIEW_NUMBER";

    // IRB
    public static final String PARTICIPANTS_PROPERTY_KEY = "newProtocolParticipant";
    public static final String DEFAULT_PROTOCOL_ORGANIZATION_TYPE_CODE = "1";
    public static final String DEFAULT_PROTOCOL_ORGANIZATION_ID = "000001";
    public static final String DEFAULT_PROTOCOL_STATUS_CODE = "100";
    public static final String PROPERTY_PROTOCOL_STATUS = "protocolStatus";

    public static final String VIEW_ONLY = "viewOnly";

    // Committee
    public static final String COMMITTEE_PROPERTY_KEY = "committee";
}
