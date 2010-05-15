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
package org.kuali.kra.award.contacts;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kuali.kra.award.home.Award;
import org.kuali.kra.bo.KcPerson;
import org.kuali.kra.bo.KcPersonFixtureFactory;
import org.kuali.kra.bo.NonOrganizationalRolodex;
import org.kuali.kra.service.impl.adapters.BusinessObjectServiceAdapter;
import org.kuali.kra.service.impl.adapters.IdentityServiceAdapter;
import org.kuali.rice.kim.bo.entity.dto.KimEntityInfo;
import org.kuali.rice.kim.service.IdentityService;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.util.MessageMap;

import javax.jws.WebParam;

/**
 * This class tests AddAwardProjectPersonRuleImpl
 */
public class AwardProjectPersonAddRuleImplTest {

    private static final int ROLODEX_ID = 1002;
    private Award award;
    private AwardProjectPersonAddRuleImpl rule;
    private KcPerson person1;
    private static final String PERSON_ID = "1001";

    @Before
    public void setUp() {
        rule = new AwardProjectPersonAddRuleImpl();
        award = new Award();

        person1 = KcPersonFixtureFactory.createKcPerson(PERSON_ID);

        NonOrganizationalRolodex person2 = new NonOrganizationalRolodex();
        person2.setRolodexId(ROLODEX_ID);
        
        award.add(new AwardPerson(person1, ContactRoleFixtureFactory.MOCK_PI));
        award.add(new AwardPerson(person2, ContactRoleFixtureFactory.MOCK_COI));
        
        
        GlobalVariables.setMessageMap(new MessageMap());
    }

    @After
    public void tearDown() {
        rule = null;
        award = null;
    }

    @Test
    public void testCheckForExistingPI_DuplicateFound() {
        AwardPerson newPerson = new AwardPerson(new KcPerson(), ContactRoleFixtureFactory.MOCK_PI);
        Assert.assertFalse("Duplicate PI not identified", rule.checkForExistingPrincipalInvestigators(award, newPerson));
    }

    @Test
    public void testCheckForExistingPI_NoDuplicateFound() {
        AwardPerson newPerson = new AwardPerson(new KcPerson(), ContactRoleFixtureFactory.MOCK_KEY_PERSON);
        Assert.assertTrue("Duplicate PI misidentified", rule.checkForExistingPrincipalInvestigators(award, newPerson));
    }

    @Test
    public void testCheckForDuplicateContact_DuplicatePersonFound() {
        KcPerson duplicatePerson = KcPersonFixtureFactory.createKcPerson(PERSON_ID);
        duplicatePerson.setPersonId(person1.getPersonId());
        AwardPerson newPerson = new AwardPerson(duplicatePerson, ContactRoleFixtureFactory.MOCK_KEY_PERSON);
        Assert.assertFalse("Duplicate Person not identified", rule.checkForDuplicatePerson(award, newPerson));
    }


    @Test
    public void testCheckForDuplicateContact_DuplicateRolodexFound() {
        NonOrganizationalRolodex duplicatePerson = new NonOrganizationalRolodex ();
        duplicatePerson.setRolodexId(ROLODEX_ID);
        AwardPerson newPerson = new AwardPerson(duplicatePerson, ContactRoleFixtureFactory.MOCK_KEY_PERSON);
        Assert.assertFalse("Duplicate Rolodex not identified", rule.checkForDuplicatePerson(award, newPerson));
    }
}
