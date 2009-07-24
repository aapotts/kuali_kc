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
package org.kuali.kra.s2s.generator.impl;

import org.kuali.kra.bo.Organization;
import org.kuali.kra.bo.Rolodex;
import org.kuali.kra.bo.Sponsor;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.s2s.generator.S2STestBase;

public class SFLLLV1_1GeneratorTest extends S2STestBase<SFLLLV1_1Generator> {

    @Override
    protected Class<SFLLLV1_1Generator> getFormGeneratorClass() {
        return SFLLLV1_1Generator.class;
    }

    @Override
    protected void prepareData(ProposalDevelopmentDocument document) throws Exception {

        Sponsor sponsor = new Sponsor();
        sponsor.setSponsorName("KUALI");
        document.getDevelopmentProposal().setSponsor(sponsor);
        document.getDevelopmentProposal().setProgramAnnouncementTitle("Testing for SFLLL");
        document.getDevelopmentProposal().setCfdaNumber("565645");
        Organization organization = new Organization();
        organization.setOrganizationName("MIT");
        organization.setCongressionalDistrict("OHIO");
        Rolodex rolodex = new Rolodex();
        rolodex.setTitle("Testing for MS-6");
        rolodex.setFirstName("Michael");
        rolodex.setLastName("Jordan");
        rolodex.setMiddleName("K");
        rolodex.setAddressLine1("addressLine1");
        rolodex.setCity("NewYork");
        rolodex.setState("AL");
        rolodex.setPostalCode("465456546");
        rolodex.setRolodexId(1234);
        organization.setRolodex(rolodex);
        document.getDevelopmentProposal().setOrganization(organization);
        Rolodex rolodex1 = new Rolodex();
        rolodex1.setAddressLine1("#272/B");
        rolodex1.setAddressLine2("15 th Main");
        rolodex1.setPostalCode("556411");
        rolodex1.setCity("LA");
        rolodex1.setState("AL");
        rolodex1.setRolodexId(123);
        document.getDevelopmentProposal().setRolodex(rolodex1);
    }
}
