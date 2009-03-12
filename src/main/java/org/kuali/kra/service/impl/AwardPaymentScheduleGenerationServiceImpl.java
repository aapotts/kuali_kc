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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.kuali.core.service.KualiConfigurationService;
import org.kuali.core.service.PersistenceService;
import org.kuali.kra.award.bo.Award;
import org.kuali.kra.award.bo.AwardReportTerm;
import org.kuali.kra.award.bo.Frequency;
import org.kuali.kra.award.paymentreports.paymentschedule.AwardPaymentSchedule;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.scheduling.sequence.XMonthlyScheduleSequence;
import org.kuali.kra.scheduling.service.ScheduleService;
import org.kuali.kra.scheduling.util.Time24HrFmt;

/**
 * 
 * This is the AwardPaymentScheduleGenerationService class.
 */

public class AwardPaymentScheduleGenerationServiceImpl implements org.kuali.kra.service.AwardPaymentScheduleGenerationService {
    
    private ScheduleService scheduleService;
    private PersistenceService persistenceService;
    private KualiConfigurationService kualiConfigurationService;
    
    /**
     * 
     * @see org.kuali.kra.service.AwardPaymentScheduleGenerationService#generatePaymentSchedules(org.kuali.kra.award.bo.Award, java.util.List)
     */
    public void generatePaymentSchedules(Award award, List<AwardReportTerm> awardReportTerms) throws ParseException{
        List<Date> dates = new ArrayList<Date>();
        AwardPaymentSchedule newAwardPaymentSchedule;
        refreshAwardReportTerms(awardReportTerms);
        dates = generateSchedules(award,awardReportTerms);
        for(Date date: dates){
            newAwardPaymentSchedule = new AwardPaymentSchedule();
            java.sql.Date sqldate = new java.sql.Date(date.getTime());
            newAwardPaymentSchedule.setDueDate(sqldate);
            newAwardPaymentSchedule.setAward(award);
            
            award.getPaymentScheduleItems().add(newAwardPaymentSchedule);
        }
    }

    /**
     * 
     * This is a helper method. This method calls evaluates the frequency and frequency base and generates dates either by calling the scheduling service or
     * without that.
     * 
     * @param award
     * @param awardReportTerms
     * @return
     * @throws ParseException
     */
    protected List<Date> generateSchedules(Award award, List<AwardReportTerm> awardReportTerms) throws ParseException{
        List<Date> dates = new ArrayList<Date>();
        java.util.Date startDate = null;
        java.util.Date endDate = null;
        
        GregorianCalendar calendar = new GregorianCalendar();
        
        for(AwardReportTerm awardReportTerm: awardReportTerms){
            if(StringUtils.equalsIgnoreCase(awardReportTerm.getReportClassCode(), kualiConfigurationService.getParameter(Constants.PARAMETER_MODULE_AWARD
                    ,Constants.PARAMETER_COMPONENT_DOCUMENT,KeyConstants.REPORT_CLASS_FOR_PAYMENTS_AND_INVOICES).getParameterValue())){
                startDate = getStartDate(awardReportTerm);
                endDate = getEndDate(awardReportTerm.getFrequencyBaseCode(),startDate);
                
                if(startDate!=null){                    
                    calendar.setTime(startDate);
                    if(endDate!=null && awardReportTerm.getFrequency().getRepeatFlag() && awardReportTerm.getFrequency().getNumberOfMonths()!=null){            
                        dates = scheduleService.getScheduledDates(startDate, endDate, new Time24HrFmt("00:00")
                                    , new XMonthlyScheduleSequence(awardReportTerm.getFrequency().getNumberOfMonths()), calendar.get(Calendar.DAY_OF_MONTH));
                    }else{            
                        dates.add(startDate);
                    }                        
                }
                
            }            
        }
        
        return dates;
    }
    
    /**
     * 
     * This method determines and returns the start date based on the frequency base code.
     * 
     * @param awardReportTerm
     * @return
     */
    protected Date getStartDate(AwardReportTerm awardReportTerm){
        GregorianCalendar calendar = new GregorianCalendar();
        java.util.Date baseDate;
        if(awardReportTerm.getFrequencyBaseCode().equals("1")){
            calendar.clear();
            calendar.set(2009, 3, 1);//temp hardcoded award execution date.
        }else if(awardReportTerm.getFrequencyBaseCode().equals("2")){                        
            calendar.clear();
            calendar.set(2009, 4, 1);//temp hardcoded award effective date.
        }else if(awardReportTerm.getFrequencyBaseCode().equals("3")){
            calendar.clear();
            calendar.set(2009, 5, 1);//temp hardcoded award expiration date of obligation.
        }else if(awardReportTerm.getFrequencyBaseCode().equals("4")){
            calendar.clear();
            calendar.set(2011, 4, 1);//temp hardcoded award expiration date.
        }else if(awardReportTerm.getFrequencyBaseCode().equals("5")){
            calendar.clear();
            calendar.set(2009, 7, 1);//temp hardcoded award effective date of obligation.
        }else{
            calendar.clear();
            calendar.setTimeInMillis(awardReportTerm.getDueDate().getTime());
        }
        
        baseDate = calendar.getTime();
        
        return getStartDateFromTheBaseDate(baseDate, awardReportTerm.getFrequency());
    }

    /**
     * 
     * This is a helper method that updates the base date based on frequency if required to get the start date.
     * 
     * @param startDate
     * @param frequency
     * @return
     */
    protected Date getStartDateFromTheBaseDate(Date baseDate, Frequency frequency) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.clear();
        calendar.setTime(baseDate);
        addOffSetPeriodToStartDate(frequency, calendar);
        
        addNumberOfMonthsToStartDate(frequency, calendar);
        
        return calendar.getTime();
    }

    /**
     * numberOfDays,AdvanceNumberOfDays and AdvanceNumberOfMonths fields of Frequency BO represent any offset from the base date, if present.
     * 
     * Only 1 out of the three can be not null for any frequency. This method determines it and adds the required offset to the base date.
     * 
     * @param frequency
     * @param calendar
     */
    protected void addOffSetPeriodToStartDate(Frequency frequency, Calendar calendar) {
        if(frequency!= null && frequency.getNumberOfDays()!=null){
            calendar.add(Calendar.DAY_OF_YEAR,frequency.getNumberOfDays());
        }else if(frequency!= null && frequency.getAdvanceNumberOfDays()!=null){
            calendar.add(Calendar.DAY_OF_YEAR,-frequency.getAdvanceNumberOfDays());
        }else if(frequency!= null && frequency.getAdvanceNumberOfMonths()!=null){
            calendar.add(Calendar.MONTH,-frequency.getAdvanceNumberOfMonths());
        }
    }

    /**
     * If the frequency is x monthly, numberOfMonths field in Frequency BO specifies the same.
     * 
     * This method adds the number of months from Frequency BO to base date to get the first date.
     * 
     * @param frequency
     * @param calendar
     */
    protected void addNumberOfMonthsToStartDate(Frequency frequency, Calendar calendar) {
        if(frequency.getNumberOfMonths()!=null){
            calendar.add(Calendar.MONTH, frequency.getNumberOfMonths());
        }
    }
    
    /**
     * 
     * This method returns the end date based on start date and frequency base code.
     * 
     * If frequency base code is 4(Final Expiration Date), it adds 1 year to the start date and returns it.
     * otherwise it returns the final expiration date itself.
     * 
     * @param frequencyBaseCode
     * @param startDate
     * @return
     */
    protected Date getEndDate(String frequencyBaseCode, Date startDate){
        GregorianCalendar calendar = new GregorianCalendar();
        if(frequencyBaseCode.equals("4")){                        
            calendar.clear();
            calendar.setTime(startDate);   
            calendar.add(Calendar.YEAR, 1);            
        }else{
            calendar.clear();
            calendar.set(2011, 4, 1);//temp hardcoded award expiration date.
        }
        return calendar.getTime();
    }

    /**
     * Gets the scheduleService attribute. 
     * @return Returns the scheduleService.
     */
    public ScheduleService getScheduleService() {
        return scheduleService;
    }

    /**
     * Sets the scheduleService attribute value.
     * @param scheduleService The scheduleService to set.
     */
    public void setScheduleService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }
    
    /**
     * 
     * This method collects all the AwardReportTerm objects in one collection and does a refresh reference object
     * on all of them in one single transaction.
     * 
     * @param awardReportTerms
     */
    protected void refreshAwardReportTerms(List<AwardReportTerm> awardReportTerms) {
        List<AwardReportTerm> persistableObjects = new ArrayList<AwardReportTerm>();
        List<String> referenceObjectNames = new ArrayList<String>();
        
        for(AwardReportTerm awardReportTerm : awardReportTerms){
            persistableObjects.add(awardReportTerm);
            referenceObjectNames.add("frequency");            
        }
        
        if(persistableObjects.size()>0 && referenceObjectNames.size()>0 ){
            getPersistenceService().retrieveReferenceObjects(persistableObjects, referenceObjectNames);
        }
    }
    
    /**
     * Gets the persistenceService attribute. 
     * @return Returns the persistenceService.
     */
    public PersistenceService getPersistenceService() {
        return persistenceService;
    }

    /**
     * Sets the persistenceService attribute value.
     * @param persistenceService The persistenceService to set.
     */
    public void setPersistenceService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    /**
     * Gets the kualiConfigurationService attribute. 
     * @return Returns the kualiConfigurationService.
     */
    public KualiConfigurationService getKualiConfigurationService() {
        return kualiConfigurationService;
    }

    /**
     * Sets the kualiConfigurationService attribute value.
     * @param kualiConfigurationService The kualiConfigurationService to set.
     */
    public void setKualiConfigurationService(KualiConfigurationService kualiConfigurationService) {
        this.kualiConfigurationService = kualiConfigurationService;
    }

}

    
