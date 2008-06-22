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
package org.kuali.kra.budget.service;


import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kuali.core.util.DateUtils;
import org.kuali.kra.bo.AbstractInstituteRate;
import org.kuali.kra.bo.InstituteLaRate;
import org.kuali.kra.bo.InstituteRate;
import org.kuali.kra.bo.Unit;
import org.kuali.kra.budget.BudgetDecimal;
import org.kuali.kra.budget.bo.AbstractBudgetRate;
import org.kuali.kra.budget.bo.BudgetLineItem;
import org.kuali.kra.budget.bo.BudgetPeriod;
import org.kuali.kra.budget.bo.BudgetPersonnelDetails;
import org.kuali.kra.budget.bo.BudgetProposalLaRate;
import org.kuali.kra.budget.bo.BudgetProposalRate;
import org.kuali.kra.budget.bo.RateClass;
import org.kuali.kra.budget.bo.RateClassType;
import org.kuali.kra.budget.bo.RateType;
import org.kuali.kra.budget.document.BudgetDocument;
import org.kuali.kra.budget.service.impl.BudgetRatesServiceImpl;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.proposaldevelopment.bo.ActivityType;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;

public class BudgetRatesServiceTest {
    private static final String TRACK_AFFECTED_PERIOD_1 = "|1|";
    private static final String TRACK_AFFECTED_PERIOD_2 = "|2|";
    private static final double DOUBLE_VALUE_ERROR_LIMIT = 0.01;
    private static final double TEST_INSTITUTE_LA_RATE = 2.5;
    private static final double TEST_INSTITUTE_RATE = 1.5;
    private static final double OLD_APPLICABLE_RATE = 5.75;
    private static final double INSTITUTE_RATE = 7.75;
    
    private static Map<String, ActivityType> activityTypes;
    private BudgetRatesService budgetRatesService;
    private BudgetDocument budgetDocument;
    private List<InstituteRate> instituteRates;
    private List<InstituteLaRate> instituteLaRates;
    
    private Date referenceStartDate = DateUtils.newDate(2006, Calendar.JANUARY, 1);
    private Date budgetPeriod1Start = DateUtils.newDate(2008, Calendar.JULY, 1);
    private Date budgetPeriod1End = DateUtils.newDate(2009, Calendar.JUNE, 30);
    private Date budgetPeriod2Start = DateUtils.newDate(2009, Calendar.JULY, 1);
    private Date budgetPeriod2End = DateUtils.newDate(2010, Calendar.JUNE, 30);
    
    private static final String RATE_CLASS_CODE_1 = "1";
    private static final String RATE_CLASS_CODE_2 = "2";
    private static final String RATE_CLASS_CODE_3 = "3";
    
    private static final String RATE_TYPE_CODE_1 = "1";
    private static final String RATE_TYPE_CODE_2 = "2";
    private static final String RATE_TYPE_CODE_3 = "3";
    
    private static final String UNIT_NO = "1";
    
    private static final String RESEARCH_ACTIVITY_CODE = "1";
    private static final String INSTRUCTION_ACTIVITY_CODE = "2";
    private static final String PUBLIC_SERVICE_ACTIVITY_CODE = "3";

    private static final String RESEARCH_ACTIVITY_DESCRIPTION = "Research";
    private static final String INSTRUCTION_ACTIVITY_DESCRIPTION = "Instruction";
    private static final String PUBLIC_SERVICE_ACTIVITY_DESCRIPTION = "Public Service";
    private List<RateClass> rateClasses;
    
    @Test
    public void testResetAllBudgetRates() throws Exception {
        setRates(budgetDocument.getBudgetProposalRates());
        setRates(budgetDocument.getBudgetProposalLaRates());
        budgetRatesService.resetAllBudgetRates(budgetDocument);
        checkApplicableRateEqualsInstituteRate(budgetDocument.getBudgetProposalRates());
        checkApplicableRateEqualsInstituteRate(budgetDocument.getBudgetProposalLaRates());        
    }
    

    @Test
    public void testResetBudgetRatesForRateClassType() throws Exception {
        setRates(budgetDocument.getBudgetProposalRates());
        setRates(budgetDocument.getBudgetProposalLaRates());
        budgetRatesService.resetBudgetRatesForRateClassType(RATE_CLASS_CODE_3, budgetDocument);
        checkApplicableRateEqualsInstituteRateForRateClass(RATE_CLASS_CODE_3, budgetDocument.getBudgetProposalRates());
        checkApplicableRateEqualsInstituteRateForRateClass(RATE_CLASS_CODE_3, budgetDocument.getBudgetProposalLaRates());
    }


    @Test
    public void testSyncAllBudgetRates_ChangingRatesNotProposalActivityType() throws Exception {
        changeInstituteRates();        
        budgetRatesService.syncAllBudgetRates(budgetDocument);
        String activityTypeCode = budgetDocument.getProposal().getActivityTypeCode();
        Assert.assertEquals(countInstituteRatesForActivityTypeCode(activityTypeCode), budgetDocument.getBudgetProposalRates().size());
        checkSyncedRates();        
    }
    
    @Test
    public void testSyncAllBudgetRates_ChangeProposalActivityType() throws Exception {
        budgetDocument.getProposal().setActivityTypeCode(INSTRUCTION_ACTIVITY_CODE);
        changeInstituteRates();
        budgetRatesService.syncAllBudgetRates(budgetDocument);
        String activityTypeCode = budgetDocument.getProposal().getActivityTypeCode();
        Assert.assertEquals(countInstituteRatesForActivityTypeCode(activityTypeCode), budgetDocument.getBudgetProposalRates().size());
        checkSyncedRates();        
    }    

    @Test
    public void testViewLocation() throws Exception {
        testViewLocation(budgetDocument.getBudgetProposalRates());
        testViewLocation(budgetDocument.getBudgetProposalLaRates());
    }
    
    @BeforeClass
    public static void init() {
        initializeActivityTypes();
    }    
    
    @Before
    public void setUp() {
        budgetRatesService = new BudgetRatesServiceImpl();
        ((BudgetRatesServiceImpl)budgetRatesService).setBusinessObjectService(new MockBusinessObjectService());
        initializeBudgetDocument();
        initializeBudgetProposalRates();
        initializeInstituteRates();
        initializeRateClasses();
    }

    private void initializeBudgetDocument() {
        budgetDocument = new BudgetDocument() {

            @Override
            public BudgetRatesService getBudgetRatesService() {
                return budgetRatesService;
            }

            @Override
            public BudgetSummaryService getBudgetSummaryService() {
                return new MockBudgetSummaryService();            
            }            
            
        };
        budgetDocument.setBudgetVersionNumber(1);
        budgetDocument.setProposal(initializeProposalDevelopmentDocument());
        budgetDocument.setStartDate(budgetDocument.getProposal().getRequestedStartDateInitial());
        budgetDocument.setEndDate(budgetDocument.getProposal().getRequestedEndDateInitial());
        budgetDocument.add(generateBudgetPeriod(1, budgetPeriod1Start, budgetPeriod1End));
        budgetDocument.add(generateBudgetPeriod(2, budgetPeriod2Start, budgetPeriod2End));
    }

    private BudgetPeriod generateBudgetPeriod(int periodNo, Date start, Date end) {
        BudgetPeriod bp = new BudgetPeriod();
        bp.setStartDate(start);
        bp.setEndDate(end);
        bp.setBudgetPeriod(periodNo);
        return bp;
    }

    private ProposalDevelopmentDocument initializeProposalDevelopmentDocument() {
        ProposalDevelopmentDocument proposal = new ProposalDevelopmentDocument();
        proposal.setActivityTypeCode(RESEARCH_ACTIVITY_CODE);
        proposal.setActivityType(activityTypes.get(RESEARCH_ACTIVITY_CODE));
        proposal.setOwnedByUnitNumber(UNIT_NO);
        Unit unit = new Unit();
        unit.setUnitNumber(UNIT_NO);
        proposal.setOwnedByUnit(unit);
        proposal.setRequestedStartDateInitial(budgetPeriod1Start);
        proposal.setRequestedEndDateInitial(budgetPeriod2End);
        return proposal;
    }

    @After
    public void tearDown() {
        budgetRatesService = null;
        budgetDocument = null;
        instituteRates = null;
        instituteLaRates = null;
    }

    private static void initializeActivityTypes() {
        activityTypes = new TreeMap<String, ActivityType>();
        activityTypes.put(RESEARCH_ACTIVITY_CODE, generateMinimalActivityType(RESEARCH_ACTIVITY_CODE, RESEARCH_ACTIVITY_DESCRIPTION));
        activityTypes.put(INSTRUCTION_ACTIVITY_CODE, generateMinimalActivityType(INSTRUCTION_ACTIVITY_CODE, INSTRUCTION_ACTIVITY_DESCRIPTION));
        activityTypes.put(PUBLIC_SERVICE_ACTIVITY_CODE, generateMinimalActivityType(PUBLIC_SERVICE_ACTIVITY_CODE, PUBLIC_SERVICE_ACTIVITY_DESCRIPTION));
    }

    private static ActivityType generateMinimalActivityType(String activityTypeCode, String description) {
        ActivityType activityType = new ActivityType();
        activityType.setActivityTypeCode(activityTypeCode);
        activityType.setDescription(description);
        return activityType;
    }
    
    private void changeInstituteRates() {
        for(AbstractInstituteRate instituteRate: instituteRates) {
            instituteRate.setInstituteRate(new BudgetDecimal(TEST_INSTITUTE_RATE));
        }
        for(AbstractInstituteRate instituteRate: instituteLaRates) {
            instituteRate.setInstituteRate(new BudgetDecimal(TEST_INSTITUTE_LA_RATE));
        }
    }
    
    private void checkSyncedRates() {
        for(BudgetProposalRate budgetProposalRate: budgetDocument.getBudgetProposalRates()) {
            Assert.assertEquals(TEST_INSTITUTE_RATE, budgetProposalRate.getInstituteRate().doubleValue(), DOUBLE_VALUE_ERROR_LIMIT);
            Assert.assertEquals(TEST_INSTITUTE_RATE, budgetProposalRate.getApplicableRate().doubleValue(), DOUBLE_VALUE_ERROR_LIMIT);
        }
        
        for(BudgetProposalLaRate budgetProposalLaRate: budgetDocument.getBudgetProposalLaRates()) {
            Assert.assertEquals(TEST_INSTITUTE_LA_RATE, budgetProposalLaRate.getInstituteRate().doubleValue(), DOUBLE_VALUE_ERROR_LIMIT);
            Assert.assertEquals(TEST_INSTITUTE_LA_RATE, budgetProposalLaRate.getApplicableRate().doubleValue(), DOUBLE_VALUE_ERROR_LIMIT);
        }
    }
    
    private int countInstituteRatesForActivityTypeCode(String activityTypeCode) {
        int count = 0;
        for(InstituteRate rate: instituteRates) {
            if(rate.getActivityTypeCode().equals(activityTypeCode)) {
                count++;
            }
        }
        return count;
    }
    
    private void initializeBudgetProposalRates() {
        budgetDocument.add(generateBudgetProposalRate(RESEARCH_ACTIVITY_CODE, "1", "1", referenceStartDate, Constants.ON_CAMUS_FLAG));
        budgetDocument.add(generateBudgetProposalRate(RESEARCH_ACTIVITY_CODE, "2", "1", referenceStartDate, Constants.ON_CAMUS_FLAG));
        budgetDocument.add(generateBudgetProposalRate(RESEARCH_ACTIVITY_CODE, "2", "2", referenceStartDate, Constants.ON_CAMUS_FLAG));
        budgetDocument.add(generateBudgetProposalRate(RESEARCH_ACTIVITY_CODE, "3", "1", referenceStartDate, Constants.ON_CAMUS_FLAG));
        budgetDocument.add(generateBudgetProposalRate(RESEARCH_ACTIVITY_CODE, "3", "2", referenceStartDate, Constants.ON_CAMUS_FLAG));
        budgetDocument.add(generateBudgetProposalRate(RESEARCH_ACTIVITY_CODE, "3", "3", referenceStartDate, Constants.ON_CAMUS_FLAG));
    }
    
    private void initializeRateClasses() {
        rateClasses = new ArrayList<RateClass>();
        rateClasses.add(generateRateClass(RATE_TYPE_CODE_1, RATE_CLASS_CODE_1));
        rateClasses.add(generateRateClass(RATE_TYPE_CODE_2, RATE_CLASS_CODE_2));
        rateClasses.add(generateRateClass(RATE_TYPE_CODE_3, RATE_CLASS_CODE_3));
        budgetDocument.setRateClasses(rateClasses);
    }

    private RateClass generateRateClass(String rateClassType, String rateClassCode) {
        RateClass rateClass = new RateClass();
        rateClass.setRateClassType(rateClassType);
        rateClass.setRateClassCode(rateClassCode);
        return rateClass;
    }
    
    private void initializeInstituteRates() {
        instituteRates = new ArrayList<InstituteRate>();
        instituteLaRates = new ArrayList<InstituteLaRate>();
        
        generateInstituteRate(RESEARCH_ACTIVITY_CODE, RATE_CLASS_CODE_1, RATE_TYPE_CODE_1);
        generateInstituteRate(RESEARCH_ACTIVITY_CODE, RATE_CLASS_CODE_2, RATE_TYPE_CODE_1);
        generateInstituteRate(RESEARCH_ACTIVITY_CODE, RATE_CLASS_CODE_2, RATE_TYPE_CODE_2);
        generateInstituteRate(RESEARCH_ACTIVITY_CODE, RATE_CLASS_CODE_3, RATE_TYPE_CODE_1);
        generateInstituteRate(RESEARCH_ACTIVITY_CODE, RATE_CLASS_CODE_3, RATE_TYPE_CODE_2);
        generateInstituteRate(RESEARCH_ACTIVITY_CODE, RATE_CLASS_CODE_3, RATE_TYPE_CODE_3);
        
        generateInstituteRate(INSTRUCTION_ACTIVITY_CODE, RATE_CLASS_CODE_1, RATE_TYPE_CODE_1);
        generateInstituteRate(INSTRUCTION_ACTIVITY_CODE, RATE_CLASS_CODE_2, RATE_TYPE_CODE_2);
        generateInstituteRate(INSTRUCTION_ACTIVITY_CODE, RATE_CLASS_CODE_3, RATE_TYPE_CODE_3);
        
        generateInstituteRate(PUBLIC_SERVICE_ACTIVITY_CODE, RATE_CLASS_CODE_1, RATE_TYPE_CODE_1);
        generateInstituteRate(PUBLIC_SERVICE_ACTIVITY_CODE, RATE_CLASS_CODE_1, RATE_TYPE_CODE_2);
        generateInstituteRate(PUBLIC_SERVICE_ACTIVITY_CODE, RATE_CLASS_CODE_3, RATE_TYPE_CODE_3);
        generateInstituteRate(PUBLIC_SERVICE_ACTIVITY_CODE, RATE_CLASS_CODE_2, RATE_TYPE_CODE_1);
        generateInstituteRate(PUBLIC_SERVICE_ACTIVITY_CODE, RATE_CLASS_CODE_2, RATE_TYPE_CODE_2);
        generateInstituteRate(PUBLIC_SERVICE_ACTIVITY_CODE, RATE_CLASS_CODE_2, RATE_TYPE_CODE_3);
        generateInstituteRate(PUBLIC_SERVICE_ACTIVITY_CODE, RATE_CLASS_CODE_3, RATE_TYPE_CODE_1);
        generateInstituteRate(PUBLIC_SERVICE_ACTIVITY_CODE, RATE_CLASS_CODE_3, RATE_TYPE_CODE_2);
        generateInstituteRate(PUBLIC_SERVICE_ACTIVITY_CODE, RATE_CLASS_CODE_3, RATE_TYPE_CODE_3);
    }
    
    private BudgetDecimal generateDefaultRate(AbstractInstituteRate instituteRate) {
        return new BudgetDecimal(new StringBuilder(instituteRate.getRateClassCode()).append(".").append(instituteRate.getRateTypeCode()).toString());
    }
    
    /**
     * This method creates InstituteRate and InstituteLaRates
     * @param activityTypeCode
     * @param rateClassCode
     * @param rateTypeCode
     */
    private void generateInstituteRate(String activityTypeCode, String rateClassCode, String rateTypeCode) {
        InstituteRate instituteRate = new InstituteRate();
        initializeInstitutionRate(instituteRate, activityTypeCode, rateClassCode, rateTypeCode);
        instituteRates.add(instituteRate);
        
        InstituteLaRate instituteLaRate = new InstituteLaRate();        
        initializeInstitutionRate(instituteLaRate, activityTypeCode, rateClassCode, rateTypeCode);
        instituteLaRates.add(instituteLaRate);
    }
    
    private void initializeInstitutionRate(AbstractInstituteRate instituteRate, String activityTypeCode, String rateClassCode, String rateTypeCode) {
        instituteRate.setUnitNumber(UNIT_NO);
        if(instituteRate instanceof InstituteRate) {
            InstituteRate instRate = (InstituteRate) instituteRate;
            instRate.setActivityTypeCode(activityTypeCode);
            instRate.setActivityType(activityTypes.get(activityTypeCode));
        }
        
        instituteRate.setRateClassCode(rateClassCode);
        RateClass rateClass = new RateClass();
        RateClassType rateClassType = new RateClassType();
        rateClassType.setRateClassType("O");
        rateClassType.setPrefixActivityType(false);
        rateClassType.setSortId("1");
        rateClass.setRateClassType("O");
        rateClass.setRateClassTypeT(rateClassType);
        instituteRate.setRateClass(rateClass);
        instituteRate.setRateTypeCode(rateTypeCode);
        RateType rateType = new RateType();
        rateType.setDescription(rateTypeCode);
        instituteRate.setRateType(rateType);
        instituteRate.setStartDate(referenceStartDate);
        instituteRate.setOnOffCampusFlag(true);
        instituteRate.setInstituteRate(generateDefaultRate(instituteRate));
        instituteRate.setFiscalYear("2006");
    }
    
    private BudgetProposalRate generateBudgetProposalRate(String activityTypeCode, String rateClassCode, String rateTypeCode, Date startDate, String onOffCampusFlag) {
        BudgetProposalRate budgetProposalRate = new BudgetProposalRate();
        budgetProposalRate.setActivityTypeCode(activityTypeCode);
        budgetProposalRate.setRateClassCode(rateClassCode);
        budgetProposalRate.setRateTypeCode(rateTypeCode);
        RateType rateType = new RateType();
        rateType.setDescription(rateTypeCode);
        budgetProposalRate.setRateType(rateType);
        budgetProposalRate.setStartDate(referenceStartDate);
        budgetProposalRate.setOnOffCampusFlag(Constants.ON_CAMUS_FLAG.equals(onOffCampusFlag));
        budgetProposalRate.setFiscalYear("2008");        
        return budgetProposalRate;
    }
    
    @SuppressWarnings("unchecked")
    private void checkApplicableRateEqualsInstituteRate(List rates) {
        List<AbstractBudgetRate> abstractBudgetRates = (List<AbstractBudgetRate>) rates; 
        for(AbstractBudgetRate budgetRate: abstractBudgetRates) {
            Assert.assertEquals(budgetRate.getInstituteRate(), budgetRate.getApplicableRate());
        }
    }

    @SuppressWarnings("unchecked")
    private void checkApplicableRateEqualsInstituteRateForRateClass(String rateClassType, List rates) {
        List<AbstractBudgetRate> abstractBudgetRates = (List<AbstractBudgetRate>) rates; 
        for(AbstractBudgetRate budgetRate: abstractBudgetRates) {
            if(budgetRate.getRateClass().getRateClassType().equals(rateClassType)) {
                Assert.assertEquals(budgetRate.getInstituteRate(), budgetRate.getApplicableRate());
            } else {
                Assert.assertEquals(OLD_APPLICABLE_RATE, budgetRate.getApplicableRate().doubleValue(), DOUBLE_VALUE_ERROR_LIMIT);
            }
        }
    }
    
    private void setRates(List rateList) {
        List<AbstractBudgetRate> abstractBudgetRates = (List<AbstractBudgetRate>) rateList;         
        for(AbstractBudgetRate budgetRate: abstractBudgetRates) {
            budgetRate.setApplicableRate(new BudgetDecimal(OLD_APPLICABLE_RATE));
            budgetRate.setInstituteRate(new BudgetDecimal(INSTITUTE_RATE));
            budgetRate.setRateClass(rateClasses.get(Integer.valueOf(budgetRate.getRateClassCode()) - 1));
            budgetRate.setRateClassCode(rateClasses.get(Integer.valueOf(budgetRate.getRateClassCode()) - 1).getRateClassCode());
        }
    }
    
    private void testViewLocation(List budgetRates) throws Exception {
        List<AbstractBudgetRate> abstractBudgetRates = (List<AbstractBudgetRate>) budgetRates; 
        testViewLocation_DisplayLocation(abstractBudgetRates, TRACK_AFFECTED_PERIOD_1, 2, false);
        testViewLocation_DisplayLocation(abstractBudgetRates, TRACK_AFFECTED_PERIOD_2, 2, true);        
    }
    
    private void testViewLocation_DisplayLocation(List<AbstractBudgetRate> abstractBudgetRates, String trackAffectedPeriod, Integer budgetPeriod, 
                                                    boolean expectedResults) throws Exception {
        for(AbstractBudgetRate rate: abstractBudgetRates) {
            rate.setTrackAffectedPeriod(trackAffectedPeriod);
        }
        budgetRatesService.viewLocation(Constants.ON_CAMUS_FLAG, budgetPeriod, budgetDocument);
        for(AbstractBudgetRate rate: abstractBudgetRates) {
            Assert.assertEquals(expectedResults, rate.isDisplayLocation());
        }
    }
    
    @SuppressWarnings("unchecked")
    public class MockBusinessObjectService extends BusinessObjectServiceAdapter {        
        
        @Override
        public Collection findMatching(Class clazz, Map fieldValues) {
            Collection results;
            if(clazz.equals(InstituteRate.class)) {
                results = findMatchingInstituteRates(fieldValues);
            } else if(clazz.equals(InstituteLaRate.class)) {
                results = findMatchingInstituteLaRates(fieldValues);
            } else {
                results = null;
            }
            
            return results;
            
        }

        private Collection findMatchingInstituteRates(Map fieldValues) {
            String activityTypeCode = (String) fieldValues.get(BudgetRatesServiceImpl.ACTIVITY_TYPE_CODE_KEY);
            String unitNumber = (String) fieldValues.get(BudgetRatesServiceImpl.UNIT_NUMBER_KEY);
            
            List<InstituteRate> filteredInstituteRates = new ArrayList<InstituteRate>();
            for(InstituteRate testRate: instituteRates) {
                if(testRate.getUnitNumber().equals(unitNumber) && testRate.getActivityTypeCode().equals(activityTypeCode)) {
                    filteredInstituteRates.add(testRate);
                }
            }
            
            return filteredInstituteRates;
        }
        
        private Collection findMatchingInstituteLaRates(Map fieldValues) {
            String unitNumber = (String) fieldValues.get(BudgetRatesServiceImpl.UNIT_NUMBER_KEY);
            
            List<InstituteLaRate> filteredInstituteLaRates = new ArrayList<InstituteLaRate>();
            for(InstituteLaRate testRate: instituteLaRates) {
                if(testRate.getUnitNumber().equals(unitNumber)) {
                    filteredInstituteLaRates.add(testRate);
                }
            }
            
            return filteredInstituteLaRates;
        }        
    }
    
    public class MockBudgetSummaryService implements BudgetSummaryService {
        public void addBudgetPeriod(BudgetDocument budgetDocument, BudgetPeriod newBudgetPeriod) { }
        public boolean budgetLineItemExists(BudgetDocument budgetDocument, Integer budgetPeriod) { return false; }
        public void calculateBudget(BudgetDocument budgetDocument) { }
        public void deleteBudgetPeriod(BudgetDocument budgetDocument, int delPeriod) { }
        public void generateAllPeriods(BudgetDocument budgetDocument) { }
        public void generateBudgetPeriods(List<BudgetPeriod> budgetPeriods, Date projectStartDate, Date projectEndDate) { }
        public Collection<BudgetLineItem> getBudgetLineItemForPeriod(BudgetDocument budgetDocument, int budgetPeriodNumber) { return null; }
        public Collection<BudgetPersonnelDetails> getBudgetPersonnelDetailsForPeriod(BudgetDocument budgetDocument, int budgetPeriodNumber) { return null; }
        public void updateOnOffCampusFlag(BudgetDocument budgetDocument, String onOffCampusFlag) {}
        public void adjustStartEndDatesForLineItems(BudgetDocument budgetDocument) { }
        public void setupOldStartEndDate(BudgetDocument budgetDocument, boolean resetAll) { }        
        public void setupOldStartEndDate(List <BudgetLineItem > budgetLineItems) { }        
        public List<Date> getNewStartEndDates(List<Date> startEndDates, int gap, int duration, Date prevDate) { return new ArrayList <Date>();}
    }
}
