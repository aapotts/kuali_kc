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
package org.kuali.kra.irb.actions.decision;


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommitteeDecisionRecuserRuleTest extends CommitteeDecisionRuleBase {
    
    CommitteeDecisionRecuserRule rule;

    @Before
    public void setUp() throws Exception {
        rule = new CommitteeDecisionRecuserRule();
    }

    @After
    public void tearDown() throws Exception {
        rule = null;
    }
    
    @Test
    public void testProccessCommitteeDecisionRule1() {
        CommitteeDecision decision = buildValidCommitteeDecision();
        //add a new recuser
        decision.setNewRecused(getBasicPerson());
        assertTrue(rule.proccessCommitteeDecisionRecuserRule(null, decision));
    }
    
    @Test
    public void testProccessCommitteeDecisionRule2() {
        CommitteeDecision decision = buildValidCommitteeDecision();
        //add a new recuser that is already in the recused list
        decision.setNewRecused(getBasicRescuser());
        assertFalse(rule.proccessCommitteeDecisionRecuserRule(null, decision));
    }
    
    @Test
    public void testProccessCommitteeDecisionRule3() {
        CommitteeDecision decision = buildValidCommitteeDecision();
        //add a new recuser that is already in the abstainer list
        decision.setNewRecused(getBasicAbstainer());
        assertFalse(rule.proccessCommitteeDecisionRecuserRule(null, decision));
    }
    @Test
    public void testProccessCommitteeDecisionRule4() {
        CommitteeDecision decision = buildValidCommitteeDecision();
        //add a new recuser that is invalid
        decision.setNewRecused(new CommitteePerson());
        assertFalse(rule.proccessCommitteeDecisionRecuserRule(null, decision));
    }
}
