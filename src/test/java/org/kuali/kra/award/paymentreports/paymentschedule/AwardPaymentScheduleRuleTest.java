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
package org.kuali.kra.award.paymentreports.paymentschedule;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kuali.core.util.ErrorMap;
import org.kuali.core.util.GlobalVariables;
import org.kuali.kra.award.bo.Award;
import org.kuali.kra.rules.SoftError;

/**
 * This class tests AwardApprovedEquipmentRuleImpl behavior
 */
public class AwardPaymentScheduleRuleTest {
    private static final Calendar calendar = new GregorianCalendar();
    private AwardPaymentScheduleRuleImpl awardPaymentScheduleRule;
    private Award award;
    
    @Before
    public void setUp() throws Exception {
        awardPaymentScheduleRule = prepareTestReadyAwardPaymentScheduleRuleImpl();
        award = new Award();
        award.setAwardId(1L);
        award.setAwardNumber("X1000");
        award.setSequenceNumber(1);
        calendar.set(2009, 4, 1);
        GlobalVariables.setErrorMap(new ErrorMap());
    }
    
    @After
    public void tearDown() {
        award = null;
        awardPaymentScheduleRule = null;
    }
    
    @Test
    public void testRequiredFieldPresent() {
        AwardPaymentSchedule paymentScheduleItem = createPaymentScheduleItem(new Date(calendar.getTimeInMillis()), 
                                                        award.getAwardNumber(), award.getSequenceNumber());
        Assert.assertTrue(awardPaymentScheduleRule.areRequiredFieldsComplete(paymentScheduleItem));
        
        paymentScheduleItem.setAmount(null);
        Assert.assertTrue(awardPaymentScheduleRule.areRequiredFieldsComplete(paymentScheduleItem));
        
        paymentScheduleItem.setStatus(null);
        Assert.assertTrue(awardPaymentScheduleRule.areRequiredFieldsComplete(paymentScheduleItem));
        
        paymentScheduleItem.setStatusDescription(null);
        Assert.assertTrue(awardPaymentScheduleRule.areRequiredFieldsComplete(paymentScheduleItem));
        
        paymentScheduleItem.setSubmitDate(null);
        Assert.assertTrue(awardPaymentScheduleRule.areRequiredFieldsComplete(paymentScheduleItem));
        
        paymentScheduleItem.setSubmittedBy(null);
        Assert.assertTrue(awardPaymentScheduleRule.areRequiredFieldsComplete(paymentScheduleItem));
        
        paymentScheduleItem.setDueDate(null);
        Assert.assertFalse(awardPaymentScheduleRule.areRequiredFieldsComplete(paymentScheduleItem));
    }
    
    @Test
    public void testIsUnique() {
        AwardPaymentSchedule paymentScheduleItem1 = createPaymentScheduleItem(new Date(calendar.getTimeInMillis()), 
                                                        award.getAwardNumber(), award.getSequenceNumber());
        AwardPaymentSchedule paymentScheduleItem2 = createPaymentScheduleItem(new Date(calendar.getTimeInMillis()), 
                                                        award.getAwardNumber(), award.getSequenceNumber());
                
        checkAddingNewItemToEmptyList(paymentScheduleItem1);
        award.add(paymentScheduleItem1);
        checkExistingEntriesDoesntTriggerErrorOnSave();
        addPaymentScheduleToAward(paymentScheduleItem1, paymentScheduleItem2);
        checkAddingDuplicateToCollection();
        checkEditingItemResultingInDuplicate(paymentScheduleItem2);
    }

    private void addPaymentScheduleToAward(AwardPaymentSchedule paymentScheduleItem1, 
            AwardPaymentSchedule paymentScheduleItem2) {
        paymentScheduleItem1.setAwardPaymentScheduleId(1L);
        award.add(paymentScheduleItem1);
        paymentScheduleItem2.setAwardPaymentScheduleId(2L);
        award.add(paymentScheduleItem2);
    }

    private void checkAddingDuplicateToCollection() {
        AwardPaymentSchedule item1Duplicate = createPaymentScheduleItem(new Date(calendar.getTimeInMillis()), award.getAwardNumber(), 
                                                    award.getSequenceNumber());
        Assert.assertFalse(awardPaymentScheduleRule.isUnique(award.getPaymentScheduleItems(), item1Duplicate));
    }
    
    private void checkAddingNewItemToEmptyList(AwardPaymentSchedule paymentScheduleItem1) {
        Assert.assertTrue(awardPaymentScheduleRule.isUnique(new ArrayList<AwardPaymentSchedule>(), 
                                                            paymentScheduleItem1));
    }

    private void checkExistingEntriesDoesntTriggerErrorOnSave() {
        List<AwardPaymentSchedule> items = award.getPaymentScheduleItems();
        for(AwardPaymentSchedule item : items) {
            Assert.assertTrue(awardPaymentScheduleRule.isUnique(items, item));
        }
    }
    
    private void checkEditingItemResultingInDuplicate(AwardPaymentSchedule paymentScheduleItem2) {        
        Assert.assertFalse(awardPaymentScheduleRule.isUnique(award.getPaymentScheduleItems(), paymentScheduleItem2));
    }
    
    private AwardPaymentSchedule createPaymentScheduleItem(Date dueDate, String awardNumber, Integer sequenceNumber) {
        return new AwardPaymentSchedule(dueDate, awardNumber, sequenceNumber);
    }
    
    private AwardPaymentScheduleRuleImpl prepareTestReadyAwardPaymentScheduleRuleImpl() {
        AwardPaymentScheduleRuleImpl approvedEquipmentRule = new AwardPaymentScheduleRuleImpl() {
            private Map<String, Collection<SoftError>> softErrors = new HashMap<String, Collection<SoftError>>();
            @Override
            public Map<String, Collection<SoftError>> getSoftErrors() { 
                return softErrors; 
            }
        };
        return approvedEquipmentRule;
    }
}
