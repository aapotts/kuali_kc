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
package org.kuali.kra.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.core.bo.Parameter;
import org.kuali.core.service.KualiConfigurationService;
import org.kuali.kra.award.bo.Award;
import org.kuali.kra.award.bo.AwardReportTerm;
import org.kuali.kra.award.bo.Frequency;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.scheduling.sequence.XMonthlyScheduleSequence;
import org.kuali.kra.scheduling.service.ScheduleService;
import org.kuali.kra.scheduling.util.Time24HrFmt;

/**
 * 
 * This class tests the helper methods in AwardPaymentScheduleGenerationServiceImpl
 */
@RunWith(JMock.class)
public class AwardPaymentScheduleGenerationServiceImplTest {
    
    public static final int START_DATE_YEAR_2009 = 2009;
    public static final int START_DATE_YEAR_2011 = 2011;
    public static final int START_DATE_MONTH_APRIL = 3;
    public static final int START_DATE_MONTH_MAY = 4;
    public static final int START_DATE_MONTH_JULY = 6;
    public static final int START_DATE_MONTH_AUGUST = 7;
    public static final int FIRST_DAY_OF_MONTH = 1;
    public static final int ZERO =0;
    public static final int THIRTY_DAYS = 30;
    public static final int THREE_MONTHS = 3;
    public static final String ZERO_HOURS = "00:00";
    public static final String FREQUENCY_BASE_CODE_ONE = "1";
    public static final String FREQUENCY_BASE_CODE_FOUR = "4";
    public static final String FREQUENCY_BASE_CODE_TWO = "2";
    public static final String REPORT_CLASS_CODE_CODE_SIX = "6";
    public static final int PERIOD_IN_YEARS = 1;
    
    Award award;
    List<AwardReportTerm> awardReportTerms;
    AwardReportTerm newAwardReportTerm;
    Frequency frequency;
    AwardPaymentScheduleGenerationServiceImpl awardPaymentScheduleGenerationServiceImpl;
    Calendar calendar;
    Calendar calendar1;
    
    private Mockery context = new JUnit4Mockery();
    
    @Before
    public void setUp() throws Exception {
        award = new Award();
        frequency = new Frequency();        
        awardReportTerms = new ArrayList<AwardReportTerm>();        
        newAwardReportTerm = new AwardReportTerm();        
        awardPaymentScheduleGenerationServiceImpl = new AwardPaymentScheduleGenerationServiceImpl();
        calendar = new GregorianCalendar();
        calendar1 = new GregorianCalendar();
    }

    @After
    public void tearDown() throws Exception {
        frequency = null;
        newAwardReportTerm = null;
        awardReportTerms = null;
        awardPaymentScheduleGenerationServiceImpl = null;
        newAwardReportTerm = null;
        calendar = null;
        calendar1 = null;
    }
    
    @Test
    public final void testGetStartDate(){
        newAwardReportTerm.setFrequencyBaseCode(FREQUENCY_BASE_CODE_ONE);
        calendar.clear();
        calendar.set(START_DATE_YEAR_2009, START_DATE_MONTH_APRIL, FIRST_DAY_OF_MONTH);
        newAwardReportTerm.setFrequency(frequency);
        java.util.Date startDate = awardPaymentScheduleGenerationServiceImpl.getStartDate(newAwardReportTerm);
        
        Assert.assertEquals(calendar.getTime(), startDate);
        
    }
    
    @Test
    public final void testGetEndDate(){
        calendar.clear();
        calendar.set(START_DATE_YEAR_2009, START_DATE_MONTH_JULY, FIRST_DAY_OF_MONTH,ZERO,ZERO,ZERO);
        awardPaymentScheduleGenerationServiceImpl.setPeriodInYears(PERIOD_IN_YEARS);
        java.util.Date endDate = awardPaymentScheduleGenerationServiceImpl.getEndDate(FREQUENCY_BASE_CODE_FOUR, calendar.getTime());
        calendar.add(Calendar.YEAR, 1);        
        Assert.assertEquals(calendar.getTime(),endDate);
        endDate = awardPaymentScheduleGenerationServiceImpl.getEndDate(FREQUENCY_BASE_CODE_TWO, calendar.getTime());
        calendar.clear();
        calendar.set(START_DATE_YEAR_2011, START_DATE_MONTH_MAY, FIRST_DAY_OF_MONTH,ZERO,ZERO,ZERO);
        Assert.assertEquals(calendar.getTime(),endDate);
    }
    
    @Test
    public final void testGetUpdatedStartDate(){
        calendar.clear();
        calendar.set(START_DATE_YEAR_2009, START_DATE_MONTH_JULY, FIRST_DAY_OF_MONTH,ZERO,ZERO,ZERO);
        frequency.setNumberOfDays(null);
        frequency.setNumberOfMonths(null);
        frequency.setAdvanceNumberOfDays(null);
        frequency.setAdvanceNumberOfMonths(null);
        java.util.Date startDate = awardPaymentScheduleGenerationServiceImpl.getStartDateFromTheBaseDate(calendar.getTime(), frequency);
        Assert.assertEquals(calendar.getTime(),startDate);
        
        frequency.setNumberOfDays(THIRTY_DAYS);
        frequency.setNumberOfMonths(null);
        frequency.setAdvanceNumberOfDays(null);
        frequency.setAdvanceNumberOfMonths(null);
        startDate = awardPaymentScheduleGenerationServiceImpl.getStartDateFromTheBaseDate(calendar.getTime(), frequency);
        calendar.add(Calendar.DAY_OF_YEAR, THIRTY_DAYS);
        Assert.assertEquals(calendar.getTime(),startDate);
        
        frequency.setNumberOfDays(null);
        frequency.setNumberOfMonths(null);
        frequency.setAdvanceNumberOfDays(THIRTY_DAYS);
        frequency.setAdvanceNumberOfMonths(null);
        startDate = awardPaymentScheduleGenerationServiceImpl.getStartDateFromTheBaseDate(calendar.getTime(), frequency);
        calendar.add(Calendar.DAY_OF_YEAR, -THIRTY_DAYS);
        Assert.assertEquals(calendar.getTime(),startDate);
        
        frequency.setNumberOfDays(null);
        frequency.setNumberOfMonths(null);
        frequency.setAdvanceNumberOfDays(null);
        frequency.setAdvanceNumberOfMonths(THREE_MONTHS);
        startDate = awardPaymentScheduleGenerationServiceImpl.getStartDateFromTheBaseDate(calendar.getTime(), frequency);
        calendar.add(Calendar.MONTH, -THREE_MONTHS);
        Assert.assertEquals(calendar.getTime(),startDate);
    }
    
    @Test
    public final void testAddOffSetPeriodToStartDate(){
        frequency.setNumberOfDays(THIRTY_DAYS);
        frequency.setNumberOfMonths(null);
        frequency.setAdvanceNumberOfDays(null);
        frequency.setAdvanceNumberOfMonths(null);
        calendar.clear();
        calendar.set(START_DATE_YEAR_2011, START_DATE_MONTH_AUGUST, FIRST_DAY_OF_MONTH,ZERO,ZERO,ZERO);
        calendar1.clear();
        calendar1.set(START_DATE_YEAR_2011, START_DATE_MONTH_AUGUST, FIRST_DAY_OF_MONTH,ZERO,ZERO,ZERO);
        frequency.setNumberOfDays(THIRTY_DAYS);
        awardPaymentScheduleGenerationServiceImpl.addOffSetPeriodToStartDate(frequency, calendar);        
        calendar1.add(Calendar.DAY_OF_YEAR, THIRTY_DAYS);        
        Assert.assertEquals(calendar1.getTime(),calendar.getTime());
        
        frequency.setNumberOfDays(null);
        frequency.setNumberOfMonths(null);
        frequency.setAdvanceNumberOfDays(THIRTY_DAYS);
        frequency.setAdvanceNumberOfMonths(null);
        calendar.clear();
        calendar.set(START_DATE_YEAR_2011, START_DATE_MONTH_AUGUST, FIRST_DAY_OF_MONTH,ZERO,ZERO,ZERO);
        calendar1.clear();
        calendar1.set(START_DATE_YEAR_2011, START_DATE_MONTH_AUGUST, FIRST_DAY_OF_MONTH,ZERO,ZERO,ZERO);
        frequency.setAdvanceNumberOfDays(THIRTY_DAYS);
        awardPaymentScheduleGenerationServiceImpl.addOffSetPeriodToStartDate(frequency, calendar);        
        calendar1.add(Calendar.DAY_OF_YEAR, -THIRTY_DAYS);        
        Assert.assertEquals(calendar1.getTime(),calendar.getTime());
        
        frequency.setNumberOfDays(null);
        frequency.setNumberOfMonths(null);
        frequency.setAdvanceNumberOfDays(null);
        frequency.setAdvanceNumberOfMonths(THREE_MONTHS);
        calendar.clear();
        calendar.set(START_DATE_YEAR_2011, START_DATE_MONTH_AUGUST, FIRST_DAY_OF_MONTH,ZERO,ZERO,ZERO);
        calendar1.clear();
        calendar1.set(START_DATE_YEAR_2011, START_DATE_MONTH_AUGUST, FIRST_DAY_OF_MONTH,ZERO,ZERO,ZERO);
        frequency.setAdvanceNumberOfMonths(THREE_MONTHS);
        awardPaymentScheduleGenerationServiceImpl.addOffSetPeriodToStartDate(frequency, calendar);        
        calendar1.add(Calendar.MONTH, -THREE_MONTHS);        
        Assert.assertEquals(calendar1.getTime(),calendar.getTime());
        
        frequency.setNumberOfDays(null);
        frequency.setNumberOfMonths(null);
        frequency.setAdvanceNumberOfDays(null);
        frequency.setAdvanceNumberOfMonths(null);
        calendar.clear();
        calendar.set(START_DATE_YEAR_2011, START_DATE_MONTH_AUGUST, FIRST_DAY_OF_MONTH,ZERO,ZERO,ZERO);
        calendar1.clear();
        calendar1.set(START_DATE_YEAR_2011, START_DATE_MONTH_AUGUST, FIRST_DAY_OF_MONTH,ZERO,ZERO,ZERO);
        
        awardPaymentScheduleGenerationServiceImpl.addOffSetPeriodToStartDate(frequency, calendar);
        Assert.assertEquals(calendar1.getTime(),calendar.getTime());
    }
    
    @Test
    public void testAddNumberOfMonthsToStartDate(){
        frequency.setNumberOfDays(null);
        frequency.setNumberOfMonths(THREE_MONTHS);
        frequency.setAdvanceNumberOfDays(null);
        frequency.setAdvanceNumberOfMonths(null);
        calendar.clear();
        calendar.set(START_DATE_YEAR_2011, START_DATE_MONTH_AUGUST, FIRST_DAY_OF_MONTH,ZERO,ZERO,ZERO);
        calendar1.clear();
        calendar1.set(START_DATE_YEAR_2011, START_DATE_MONTH_AUGUST, FIRST_DAY_OF_MONTH,ZERO,ZERO,ZERO);
        awardPaymentScheduleGenerationServiceImpl.addNumberOfMonthsToStartDate(frequency, calendar);
        calendar1.add(Calendar.MONTH, THREE_MONTHS);
        Assert.assertEquals(calendar1.getTime(),calendar.getTime());
        
        frequency.setNumberOfDays(null);
        frequency.setNumberOfMonths(null);
        frequency.setAdvanceNumberOfDays(null);
        frequency.setAdvanceNumberOfMonths(null);
        calendar.clear();
        calendar.set(START_DATE_YEAR_2011, START_DATE_MONTH_AUGUST, FIRST_DAY_OF_MONTH,ZERO,ZERO,ZERO);
        calendar1.clear();
        calendar1.set(START_DATE_YEAR_2011, START_DATE_MONTH_AUGUST, FIRST_DAY_OF_MONTH,ZERO,ZERO,ZERO);
        awardPaymentScheduleGenerationServiceImpl.addNumberOfMonthsToStartDate(frequency, calendar);        
        Assert.assertEquals(calendar1.getTime(),calendar.getTime());
    }
    
    @Test
    public void testGetScheduledDatesSuccessCaseWhenRepeatFlagIsTrue() throws ParseException{        
        calendar.clear();
        calendar.set(START_DATE_YEAR_2011, START_DATE_MONTH_AUGUST, FIRST_DAY_OF_MONTH,ZERO,ZERO,ZERO);
        final java.util.Date START_DATE = calendar.getTime();
        final int DAY_OF_MONTH = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.YEAR, 1);
        final java.util.Date END_DATE = calendar.getTime();
        final List<java.util.Date> DATES = new ArrayList<java.util.Date>();
        DATES.add(START_DATE);
        
        frequency.setRepeatFlag(true);
        frequency.setNumberOfMonths(THREE_MONTHS);
        
        newAwardReportTerm = new AwardReportTerm();
        newAwardReportTerm.setFrequency(frequency);
        newAwardReportTerm.setReportClassCode(REPORT_CLASS_CODE_CODE_SIX);
        newAwardReportTerm.setFrequencyBaseCode(FREQUENCY_BASE_CODE_FOUR);
        awardReportTerms.add(newAwardReportTerm);
        award.setAwardReportTerms(awardReportTerms);
        
        final ScheduleService scheduleService = context.mock(ScheduleService.class);
        final KualiConfigurationService kualiConfigurationService = context.mock(KualiConfigurationService.class);
        
        final Parameter parameter = new Parameter();
        parameter.setParameterName(KeyConstants.REPORT_CLASS_FOR_PAYMENTS_AND_INVOICES);
        parameter.setParameterValue(REPORT_CLASS_CODE_CODE_SIX);
        
        context.checking(new Expectations() {{
            one(scheduleService).getScheduledDates(with(equal(START_DATE)), with(equal(END_DATE)), with(equal(new Time24HrFmt(ZERO_HOURS)))
                    , with(any(XMonthlyScheduleSequence.class)), with(equal(DAY_OF_MONTH)));will(returnValue(DATES));
        }});
        
        context.checking(new Expectations(){{
            one(kualiConfigurationService).getParameter(Constants.PARAMETER_MODULE_AWARD,Constants.PARAMETER_COMPONENT_DOCUMENT
                    ,KeyConstants.REPORT_CLASS_FOR_PAYMENTS_AND_INVOICES);will(returnValue(parameter));
        }});
        
        awardPaymentScheduleGenerationServiceImpl.setScheduleService(scheduleService);
        awardPaymentScheduleGenerationServiceImpl.setKualiConfigurationService(kualiConfigurationService);
        awardPaymentScheduleGenerationServiceImpl.setPeriodInYears(PERIOD_IN_YEARS);
        
        Assert.assertEquals(DATES
                , awardPaymentScheduleGenerationServiceImpl.generateSchedules(award, awardReportTerms));
    }
    
    @Test
    public void testGetScheduledDatesSuccessCaseWhenRepeatFlagIsFalse() throws ParseException{
        
        calendar.clear();
        calendar.set(START_DATE_YEAR_2009, START_DATE_MONTH_JULY, FIRST_DAY_OF_MONTH,ZERO,ZERO,ZERO);
        final java.util.Date START_DATE = calendar.getTime();
        final int DAY_OF_MONTH = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.YEAR, 1);
        final java.util.Date END_DATE = calendar.getTime();
        final List<java.util.Date> DATES = new ArrayList<java.util.Date>();
        DATES.add(START_DATE);
        
        frequency.setRepeatFlag(false);
        frequency.setNumberOfMonths(THREE_MONTHS);
        
        newAwardReportTerm = new AwardReportTerm();
        newAwardReportTerm.setFrequency(frequency);
        newAwardReportTerm.setReportClassCode(REPORT_CLASS_CODE_CODE_SIX);
        newAwardReportTerm.setFrequencyBaseCode(FREQUENCY_BASE_CODE_ONE);
        awardReportTerms.add(newAwardReportTerm);
        award.setAwardReportTerms(awardReportTerms);
        
        final ScheduleService scheduleService = context.mock(ScheduleService.class);
        final KualiConfigurationService kualiConfigurationService = context.mock(KualiConfigurationService.class);
        
        final Parameter parameter = new Parameter();
        parameter.setParameterName(KeyConstants.REPORT_CLASS_FOR_PAYMENTS_AND_INVOICES);
        parameter.setParameterValue(REPORT_CLASS_CODE_CODE_SIX);
        
        context.checking(new Expectations() {{
            never(scheduleService).getScheduledDates(START_DATE, END_DATE, new Time24HrFmt(ZERO_HOURS)
                , new XMonthlyScheduleSequence(frequency.getNumberOfMonths()), DAY_OF_MONTH);will(returnValue(DATES));
        }});
        
        context.checking(new Expectations(){{
            one(kualiConfigurationService).getParameter(Constants.PARAMETER_MODULE_AWARD,Constants.PARAMETER_COMPONENT_DOCUMENT
                    ,KeyConstants.REPORT_CLASS_FOR_PAYMENTS_AND_INVOICES);will(returnValue(parameter));
        }});
        
        awardPaymentScheduleGenerationServiceImpl.setScheduleService(scheduleService);
        awardPaymentScheduleGenerationServiceImpl.setKualiConfigurationService(kualiConfigurationService);
        awardPaymentScheduleGenerationServiceImpl.setPeriodInYears(PERIOD_IN_YEARS);
        
        Assert.assertEquals(DATES, awardPaymentScheduleGenerationServiceImpl.generateSchedules(award, awardReportTerms));
    }
}
