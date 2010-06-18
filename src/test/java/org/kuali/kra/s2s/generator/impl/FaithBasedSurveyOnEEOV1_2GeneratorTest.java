/*
 * Copyright 2005-2010 The Kuali Foundation.
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
package org.kuali.kra.s2s.generator.impl;

import org.kuali.kra.bo.Organization;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.s2s.bo.S2sOpportunity;
import org.kuali.kra.s2s.generator.S2STestBase;

/**
 * This class tests the FaithBasedSurveyOnEEOV1_2Generator
 */

public class FaithBasedSurveyOnEEOV1_2GeneratorTest extends S2STestBase<FaithBasedSurveyOnEEOV1_2Generator> {

    @Override
    protected Class<FaithBasedSurveyOnEEOV1_2Generator> getFormGeneratorClass() {
        return FaithBasedSurveyOnEEOV1_2Generator.class;
    }

    @Override
    protected void prepareData(ProposalDevelopmentDocument document) throws Exception {

        Organization organization = new Organization();
        organization.setOrganizationName("MIT");
        organization.setDunsNumber("564464647");
        document.getDevelopmentProposal().setApplicantOrgFromOrganization(organization);
        S2sOpportunity s2sOpportunity = new S2sOpportunity();
        s2sOpportunity.setOpportunityTitle("OpportunityTitle for faith based survey");
        s2sOpportunity.setCfdaNumber("00.000");
        s2sOpportunity.setProposalNumber(document.getDevelopmentProposal().getProposalNumber());
        document.getDevelopmentProposal().setS2sOpportunity(s2sOpportunity);
    }
}
