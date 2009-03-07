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
package org.kuali.kra.committee.rules;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.kuali.core.util.ErrorMessage;
import org.kuali.core.util.GlobalVariables;
import org.kuali.core.util.TypedArrayList;
import org.kuali.kra.KraTestBase;
import org.kuali.kra.committee.document.CommitteeDocument;
import org.kuali.kra.committee.rule.event.AddCommitteeScheduleStartAndEndDateEvent;
import org.kuali.kra.committee.web.struts.form.schedule.DailyScheduleDetails;
import org.kuali.kra.committee.web.struts.form.schedule.MonthlyScheduleDetails;
import org.kuali.kra.committee.web.struts.form.schedule.ScheduleData;
import org.kuali.kra.committee.web.struts.form.schedule.StyleKey;
import org.kuali.kra.committee.web.struts.form.schedule.WeeklyScheduleDetails;
import org.kuali.kra.committee.web.struts.form.schedule.YearlyScheduleDetails;
import org.kuali.kra.infrastructure.KeyConstants;

public class CommitteeScheduleStartAndEndDateRuleTest extends KraTestBase {
    
    private CommitteeDocument document;
    
    private ScheduleData scheduleData;
    
    private AddCommitteeScheduleStartAndEndDateEvent event;
    
    public static String DAILY = "scheduleData.dailySchedule.scheduleEndDate";
    
    public static String WEEKLY = "scheduleData.weeklySchedule.scheduleEndDate";
    
    public static String MONTHLY = "scheduleData.monthlySchedule.scheduleEndDate";
    
    public static String YEARLY = "scheduleData.yearlySchedule.scheduleEndDate";
    
    public static String START_DATE = "scheduleData.scheduleStartDate";
    
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }
    
    @Test
    public void testNever() throws Exception {
        
        prerequisite(StyleKey.NEVER);
        
        testAssertTrue();
    }
    
    @Test
    public void testDailyForTrue() throws Exception {
        
        prerequisite(StyleKey.DAILY);
        
        scheduleData.setDailySchedule(new DailyScheduleDetails());
        Date dt = DateUtils.addDays(new Date(), 1);
        scheduleData.getDailySchedule().setScheduleEndDate(new java.sql.Date(dt.getTime()));
        
        testAssertTrue();
    }
    
    @Test
    public void testDailyForFalse() throws Exception {
        
        prerequisite(StyleKey.DAILY);
        
        scheduleData.setDailySchedule(new DailyScheduleDetails());
        scheduleData.getDailySchedule().setScheduleEndDate(scheduleData.getScheduleStartDate());       
        
        testAssertFalse();
        assertMessages(DAILY);
    }
    
    @Test
    public void testWeeklyForTrue() throws Exception {
        
        prerequisite(StyleKey.WEEKLY);
        
        scheduleData.setWeeklySchedule(new WeeklyScheduleDetails());
        Date dt = DateUtils.addDays(new Date(), 1);
        scheduleData.getWeeklySchedule().setScheduleEndDate(new java.sql.Date(dt.getTime()));
        
        testAssertTrue();
    }

    @Test
    public void testWeeklyForFalse() throws Exception {
        
        prerequisite(StyleKey.WEEKLY);
        
        scheduleData.setWeeklySchedule(new WeeklyScheduleDetails());
        scheduleData.getWeeklySchedule().setScheduleEndDate(scheduleData.getScheduleStartDate());
        
        testAssertFalse();
        assertMessages(WEEKLY);
    }

    @Test
    public void testMonthlyForTrue() throws Exception {
        
        prerequisite(StyleKey.MONTHLY);
        
        scheduleData.setMonthlySchedule(new MonthlyScheduleDetails());
        Date dt = DateUtils.addDays(new Date(), 1);
        scheduleData.getMonthlySchedule().setScheduleEndDate(new java.sql.Date(dt.getTime()));
        
        testAssertTrue();
    }

    @Test
    public void testMonthlyForFalse() throws Exception {
        
        prerequisite(StyleKey.MONTHLY);
        
        scheduleData.setMonthlySchedule(new MonthlyScheduleDetails());
        scheduleData.getMonthlySchedule().setScheduleEndDate(scheduleData.getScheduleStartDate());
        
        testAssertFalse();
        assertMessages(MONTHLY);
    }    

    @Test
    public void testYearlyForTrue() throws Exception {
        
        prerequisite(StyleKey.YEARLY);
        
        scheduleData.setYearlySchedule(new YearlyScheduleDetails());
        Date dt = DateUtils.addDays(new Date(), 1);
        scheduleData.getYearlySchedule().setScheduleEndDate(new java.sql.Date(dt.getTime()));
        
        testAssertTrue();
    }

    @Test
    public void testYearlyForFalse() throws Exception {
        
        prerequisite(StyleKey.YEARLY);
        
        scheduleData.setYearlySchedule(new YearlyScheduleDetails());
        scheduleData.getYearlySchedule().setScheduleEndDate(scheduleData.getScheduleStartDate());
        
        testAssertFalse();

        assertMessages(YEARLY);
    } 
    
    private void assertMessages(String key) {
        TypedArrayList errors = GlobalVariables.getErrorMap().getMessages(START_DATE);
        assertTrue(errors.size() == 1);    
        ErrorMessage message = (ErrorMessage) errors.get(0);
        assertEquals(message.getErrorKey(), KeyConstants.ERROR_COMMITTEESCHEDULE_SCHEDULEDATES);
        
        errors = GlobalVariables.getErrorMap().getMessages(key);
        assertTrue(errors.size() == 1);
        message = (ErrorMessage) errors.get(0);
        assertEquals(message.getErrorKey(), KeyConstants.ERROR_COMMITTEESCHEDULE_BLANK);
    }
    
    private void testAssertTrue() {
        boolean val = new CommitteeScheduleStartAndEndDateRule().processAddCommitteeScheduleRuleBusinessRules(event);
        assertTrue(val);
    }
    
    private void testAssertFalse() {
        boolean val = new CommitteeScheduleStartAndEndDateRule().processAddCommitteeScheduleRuleBusinessRules(event);
        assertFalse(val);
    }
    
    private void prerequisite(StyleKey key) {
        scheduleData = new ScheduleData();
        scheduleData.setRecurrenceType(key.toString());
        scheduleData.setScheduleStartDate(new java.sql.Date(new Date().getTime()));
        event = new AddCommitteeScheduleStartAndEndDateEvent("", (CommitteeDocument)document, scheduleData, null, null);
    }
}
