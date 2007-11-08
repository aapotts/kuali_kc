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

package org.kuali.kra.infrastructure;

public interface Constants {
    public static final String KRA_SESSION_KEY = "kra.session";
    public static final String DATASOURCE = "kraDataSource";
    public static final String DATA_DICTIONARY_SERVICE_NAME = "dataDictionaryService";
    public static final String DATE_TIME_SERVICE_NAME = "dateTimeService";
    public static final String BUSINESS_OBJECT_DAO_NAME = "businessObjectDao";
    public static final String HTML_FORM_ACTION = "htmlFormAction";
    public static final String TEXT_AREA_FIELD_NAME = "textAreaFieldName";
    public static final String TEXT_AREA_FIELD_LABEL = "textAreaFieldLabel";
    public static final String TEXT_AREA_FIELD_ANCHOR = "textAreaFieldAnchor";
    public static final Integer APPROVAL_STATUS = 2;
    public static final String MAINTENANCE_NEW_ACTION = "New";

    public static final String PRINCIPAL_INVESTIGATOR_ROLE = "PI";
    public static final String CO_INVESTIGATOR_ROLE = "COI";
    public static final String MULTIPLE_VALUE = "multipleValues";
    public static final String KEYWORD_PANEL_DISPLAY = "proposaldevelopment.displayKeywordPanel";

    public static final String MAPPING_BASIC = "basic";
    public static final String NEW_PROPOSAL_PERSON_PROPERTY_NAME = "newProposalPerson";
    public static final String NEW_PERSON_LOOKUP_FLAG = "newPersonLookupFlag";
    public static final String MAPPING_CLOSE_PAGE = "closePage";
    public static final String MAPPING_NARRATIVE_ATTACHMENT_RIGHTS_PAGE = "attachmentRights";

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

    public static final String PROPOSAL_PERSON_ROLE_PARAMETER_PREFIX = "proposaldevelopment.personrole.";

    public static final String PARAMETER_MODULE_PROPOSAL_DEVELOPMENT = "KRA-PD";
    public static final String PARAMETER_COMPONENT_DOCUMENT = "D";
    public static final String INSTITUTE_NARRATIVE_TYPE_GROUP_CODE = "O";
    public static final String NARRATIVE_NARRATIVE_TYPE_GROUP_CODE = "P";
    public static final String NARRATIVE_MODULE_STATUS_COMPLETE = "C";

    public static final String ABSTRACTS_PROPERTY_KEY = "document.proposalAbstracts";
    public static final String SPONSOR_PROPOSAL_NUMBER_PROPERTY_KEY = "sponsorProgramNumber";
    public static final String DEADLINE_DATE_KEY = "document.deadlineDate";
    public static final String SPONSOR_PROPOSAL_NUMBER_LABEL = "Sponsor Proposal ID";

    public static final String AUDIT_WARNINGS = "Warnings";

    public static final String PROPOSAL_PAGE = "proposal";
    public static final String SPONSOR_PROGRAM_INFORMATION_PANEL_ANCHOR = "SponsorProgramInformation";
    public static final String SPONSOR_PROGRAM_INFORMATION_PANEL_NAME = "Sponsor & Program Information";
    public static final String PROPOSAL_PERSON_INVESTIGATOR = "investigator";

}
