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
package org.kuali.kra.budget.calculator;

import java.sql.Timestamp;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.core.UserSession;
import org.kuali.core.service.BusinessObjectService;
import org.kuali.core.service.DateTimeService;
import org.kuali.core.service.DocumentService;
import org.kuali.core.util.GlobalVariables;
import org.kuali.kra.KraTestBase;
import org.kuali.kra.bo.AbstractInstituteRate;
import org.kuali.kra.bo.InstituteLaRate;
import org.kuali.kra.bo.InstituteRate;
import org.kuali.kra.budget.BudgetDecimal;
import org.kuali.kra.budget.bo.BudgetLineItem;
import org.kuali.kra.budget.bo.BudgetLineItemCalculatedAmount;
import org.kuali.kra.budget.bo.BudgetPeriod;
import org.kuali.kra.budget.bo.BudgetPerson;
import org.kuali.kra.budget.bo.BudgetPersonnelDetails;
import org.kuali.kra.budget.bo.AbstractBudgetRate;
import org.kuali.kra.budget.bo.BudgetProposalLaRate;
import org.kuali.kra.budget.bo.BudgetProposalRate;
import org.kuali.kra.budget.calculator.query.And;
import org.kuali.kra.budget.calculator.query.Equals;
import org.kuali.kra.budget.document.BudgetDocument;
import org.kuali.kra.budget.service.BudgetCalculationService;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.rice.KNSServiceLocator;

/**
 * This class is for testing Line item calculations
 */
//@PerTestUnitTestData(
//        @UnitTestData(order = { 
//                UnitTestData.Type.SQL_STATEMENTS, UnitTestData.Type.SQL_FILES }, 
//        sqlStatements = {
//                @UnitTestSql("delete from EPS_PROPOSAL where proposal_number = 9999999"),
//                @UnitTestSql("delete from budget where proposal_number = 9999999"),
//                @UnitTestSql("delete from budget_periods where proposal_number = '9999999'"),
//                @UnitTestSql("delete from budget_details where proposal_number = '9999999'")
//                }, 
//        sqlFiles = {
//                @UnitTestFile(filename = "classpath:insertBudgetTestData.sql", delimiter = ";")
//                })
//        )

public class LineItemCalculatorTest extends KraTestBase {
    private static final org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(LineItemCalculatorTest.class);
    private DocumentService documentService = null;
    private DateTimeService dateTimeService = null;
    private BusinessObjectService bos;
    @Before
    public void setUp() throws Exception {
        super.setUp();
        GlobalVariables.setUserSession(new UserSession("quickstart"));
        documentService = KNSServiceLocator.getDocumentService();
        bos = KraServiceLocator.getService(BusinessObjectService.class);
        dateTimeService = KNSServiceLocator.getDateTimeService();
    }

    @After
    public void tearDown() throws Exception {
        GlobalVariables.setUserSession(null);
        documentService = null;
        super.tearDown();
    }
    private BudgetDocument createBudgetDocument() throws Exception{
        ProposalDevelopmentDocument document = (ProposalDevelopmentDocument) documentService.getNewDocument("ProposalDevelopmentDocument");
        Date requestedStartDateInitial = new Date(System.currentTimeMillis());
        Date requestedEndDateInitial = new Date(System.currentTimeMillis());
        setBaseDocumentFields(document, "ProposalDevelopmentDocumentTest test doc", "005770", "project title", requestedStartDateInitial, requestedEndDateInitial, "1", "1", "000001");
        documentService.saveDocument(document);
        BudgetDocument bd = (BudgetDocument)documentService.getNewDocument("BudgetDocument");
        setBaseDocumentFields(bd,document.getProposalNumber());
        documentService.saveDocument(bd);
        BudgetDocument savedBudgetDocument = (BudgetDocument)documentService.getByDocumentHeaderId(bd.getDocumentNumber());
        populateDummyRates(savedBudgetDocument);
        assertNotNull("Budget document not saved",savedBudgetDocument);
        return savedBudgetDocument;
    }
    @Test
    public void filterRatesTest() throws Exception{
        BudgetDocument bd = createBudgetDocument();
        LineItemCalculator lic = new LineItemCalculator(bd,getLineItem(getBudgetPeriod(bd,1,"2004-01-01","2005-12-31"), 1, "400250",dateTimeService.convertToSqlDate("2005-01-01"),
                dateTimeService.convertToSqlDate("2005-12-31"),10000.00d,100.00d));
        lic.populateCalculatedAmountLineItems();
        QueryList<BudgetProposalRate> rates = lic.filterRates(bd.getBudgetProposalRates());
        Equals equalsRC = new Equals("rateClassCode", "1");
        Equals equalsRT = new Equals("rateTypeCode", "1");
        Equals equalsOnOff = new Equals("onOffCampusFlag", Boolean.TRUE);
        Equals eActType = new Equals("activityTypeCode", "1");
        Equals eStartDate = new Equals("startDate", dateTimeService.convertToSqlDate("2005-07-01"));
        And RCandRT = new And(equalsRC, equalsRT);
        And RCRTandOnOff = new And(RCandRT, equalsOnOff);
        And RCRTOnOffandAT = new And(RCRTandOnOff, eActType);
        And RCRTOnOffATandSD = new And(RCRTOnOffandAT, eStartDate);
        rates = rates.filter(RCRTOnOffATandSD);
        assertTrue("MTDC rate is not correct",rates.size()==1);
        for (BudgetProposalRate budgetProposalRate : rates) {
            LOG.info(budgetProposalRate);
        }
    }

    @Test
    public void calculateLineItemTest() throws Exception{
        List<String> errors = new ArrayList<String>();
        BudgetDocument bd = createBudgetDocument();
        assertNotNull("Budget document not saved",bd);
        
        List<BudgetPeriod> periods = bd.getBudgetPeriods();
        BudgetPeriod bp = getBudgetPeriod(bd,1,"2004-01-01","2005-12-31");
        bd.getBudgetPeriods().add(bp);
        BudgetLineItem bli = getLineItem(bp, 1, "400250",dateTimeService.convertToSqlDate("2005-01-01"),
                dateTimeService.convertToSqlDate("2005-12-31"),10000.00d,100.00d);
        bp.getBudgetLineItems().add(bli);
        BudgetCalculationService bcs = getService(BudgetCalculationService.class);
        bcs.calculateBudgetLineItem(bd,bli);
        List<BudgetLineItemCalculatedAmount> calcAmounts = bli.getBudgetLineItemCalculatedAmounts();
//        for (BudgetLineItemCalculatedAmount budgetLineItemCalculatedAmount : calcAmounts) {
//            LOG.info(budgetLineItemCalculatedAmount);
//        }
        BudgetDecimal directCost = bli.getDirectCost();
        try{
            assertEquals("Direct cost for 400250 is not correct",new BudgetDecimal(15932.50),directCost );
        }catch (AssertionError e) {
            errors.add(e.getMessage());
        }
        try{
            assertEquals("Indirect cost for 400250 is not correct",new BudgetDecimal(8208.61),bli.getIndirectCost() );
        }catch (AssertionError e) {
            errors.add(e.getMessage());
        }

        bd.getBudgetPeriods().add(bp);
        
        BudgetLineItem bli1 = getLineItem(bp, 1, "400025",dateTimeService.convertToSqlDate("2005-01-01"),
                dateTimeService.convertToSqlDate("2005-12-31"),10000.00d,100.00d);
        bp.getBudgetLineItems().add(bli1);
        bcs.calculateBudgetLineItem(bd,bli1);
        BudgetDecimal directCost1 = bli1.getDirectCost();
        try{
            assertEquals("Direct cost for 400025 is not correct",new BudgetDecimal(15081.68),directCost1);
        }catch (AssertionError e) {
            errors.add(e.getMessage());
        }
        try{
            assertEquals("Direct cost for 400025 is not correct",new BudgetDecimal(7688.54),bli1.getIndirectCost());
        }catch (AssertionError e) {
            errors.add(e.getMessage());
        }

        bcs.calculateBudgetPeriod(bd,bp);
        try{
            assertEquals("Total direct cost for 400250 is not correct",new BudgetDecimal(31014.18),bp.getTotalDirectCost());
        }catch (AssertionError e) {
            errors.add(e.getMessage());
        }
        try{
            assertEquals("Total indirect cost for 400250 is not correct",new BudgetDecimal(15897.15),bp.getTotalIndirectCost());
        }catch (AssertionError e) {
            errors.add(e.getMessage());
        }
        try{
            assertEquals("Total cost for 400250 is not correct",new BudgetDecimal(46911.33),bp.getTotalCost());
        }catch (AssertionError e) {
            errors.add(e.getMessage());
        }
        if(!errors.isEmpty()){
            throw new AssertionError(errors.toString());
        }
        
    }

    @Test
    public void calculateUnderrecoveryTest() throws Exception{
        List<String> errors = new ArrayList<String>();
        BudgetDocument bd = createBudgetDocument();
        assertNotNull("Budget document not saved",bd);
        bd.setUrRateClassCode("2");
        
        List<BudgetPeriod> periods = bd.getBudgetPeriods();
        BudgetPeriod bp = getBudgetPeriod(bd,1,"2004-01-01","2005-12-31");
        BudgetLineItem bli = getLineItem(bp, 1, "400250",dateTimeService.convertToSqlDate("2005-01-01"),
                dateTimeService.convertToSqlDate("2005-12-31"),10000.00d,100.00d);
        BudgetCalculationService bcs = getService(BudgetCalculationService.class);
        bcs.calculateBudgetLineItem(bd,bli);
        try{
            assertEquals("Direct cost for 400250 is not correct",new BudgetDecimal(15932.50),bli.getDirectCost() );
        }catch (AssertionError e) {
            errors.add(e.getMessage());
        }try{
            assertEquals("Uderrecovery for 400250 is not correct",new BudgetDecimal(540.31),bli.getUnderrecoveryAmount());
        }catch (AssertionError e) {
            errors.add(e.getMessage());
        }
        if(!errors.isEmpty()){
            throw new AssertionError(errors.toString());
        }
        
//        LOG.info(bli.getDirectCost());
//        LOG.info(bli.getUnderrecoveryAmount());
        
    }
    @Test
    public void calculatePersonnelLineItemTest() throws Exception{
        List<String> errors = new ArrayList<String>();
        BudgetDocument bd = createBudgetDocument();
        assertNotNull("Budget document not saved",bd);
        
        List<BudgetPeriod> periods = bd.getBudgetPeriods();
        BudgetPeriod bp = getBudgetPeriod(bd,1,"2005-01-01","2005-12-31");
        bd.getBudgetPeriods().add(bp);
        BudgetLineItem bli = getLineItem(bp, 1, "400250",dateTimeService.convertToSqlDate("2005-01-01"),
                dateTimeService.convertToSqlDate("2005-12-31"),10000.00d,100.00d);

        BudgetPerson bper = getBudgetPerson("8888888",1,2000,"7","TJC","2005-01-01");
        bd.getBudgetPersons().add(bper);
        BudgetPerson bper1 = getBudgetPerson("9999999",2,4000,"7","TJC","2005-01-01");
        bd.getBudgetPersons().add(bper1);
        
        BudgetCalculationService bcs = getService(BudgetCalculationService.class);
        
        BudgetPersonnelDetails bpli = getPersonnelLineItem(bp, 1, "400250",dateTimeService.convertToSqlDate("2005-01-01"),
                dateTimeService.convertToSqlDate("2005-12-31"),10000.00d,100.00d,1,2,"9999999","TJC",100.00d,50.00d);
        bli.getBudgetPersonnelDetailsList().add(bpli);
        bcs.calculateBudgetLineItem(bd,bpli);
        try{
            assertEquals("Salary requested is not correct : ",new BudgetDecimal(2030.00), bpli.getSalaryRequested());
        }catch (AssertionError e) {
            errors.add(e.getMessage());
        }try{
            assertEquals("Costshare percentage is not correct",new BudgetDecimal(50.00), bpli.getCostSharingPercent());
        }catch (AssertionError e) {
            errors.add(e.getMessage());
        }try{
            assertEquals("Costshare amount is not correct",new BudgetDecimal(2030.00), bpli.getCostSharingAmount());
        }catch (AssertionError e) {
            errors.add(e.getMessage());
        }try{
            assertEquals("Direct cost for 400250 is not correct",new BudgetDecimal(3233.89), bpli.getDirectCost());
        }catch (AssertionError e) {
            errors.add(e.getMessage());
        }try{
            assertEquals("Indirect cost for 400250 is not correct",new BudgetDecimal(1666.69), bpli.getIndirectCost());
        }catch (AssertionError e) {
            errors.add(e.getMessage());
        }
//        LOG.info("Salary requested=> "+bpli.getSalaryRequested());
//        LOG.info("Cost sharing percentage=>"+bpli.getCostSharingPercent());
//        LOG.info("Cost sharing amount =>"+bpli.getCostSharingAmount());
//        LOG.info("Direct cost =>"+bpli.getDirectCost());
//        LOG.info("Indirect cost =>"+bpli.getIndirectCost());
        
        BudgetPersonnelDetails bpli1 = getPersonnelLineItem(bp, 1, "400250",dateTimeService.convertToSqlDate("2005-01-01"),
                dateTimeService.convertToSqlDate("2005-12-31"),10000.00d,100.00d,1,1,"8888888","TJC",100.00d,100.00d);
        bli.getBudgetPersonnelDetailsList().add(bpli1);

        bcs.calculateBudgetLineItem(bd,bpli1);
        try{
            assertEquals("Salary requested is not correct",new BudgetDecimal(2030.00), bpli1.getSalaryRequested());
        }catch (AssertionError e) {
            errors.add(e.getMessage());
        }try{
            assertEquals("Costshare percentage is not correct",new BudgetDecimal(0.00), bpli1.getCostSharingPercent());
        }catch (AssertionError e) {
            errors.add(e.getMessage());
        }try{
            assertEquals("Costshare amount is not correct",new BudgetDecimal(0.00), bpli1.getCostSharingAmount());
        }catch (AssertionError e) {
            errors.add(e.getMessage());
        }try{
            assertEquals("Direct cost for 400250 is not correct",new BudgetDecimal(3233.89), bpli1.getDirectCost());
        }catch (AssertionError e) {
            errors.add(e.getMessage());
        }try{
            assertEquals("Indirect cost for 400250 is not correct",new BudgetDecimal(1666.69), bpli1.getIndirectCost());
        }catch (AssertionError e) {
            errors.add(e.getMessage());
        }
        if(!errors.isEmpty()){
            throw new AssertionError(errors.toString());
        }
//        LOG.info("Salary requested=> "+bpli1.getSalaryRequested());
//        LOG.info("Cost sharing percentage=>"+bpli1.getCostSharingPercent());
//        LOG.info("Cost sharing percentage amount =>"+bpli1.getCostSharingAmount());
//        LOG.info("Direct cost =>"+bpli1.getDirectCost());
//        LOG.info("Indirect cost =>"+bpli1.getIndirectCost());
        
//        LOG.info(bp.toString());
    }

    @Test
    public void calculateBudgetTest() throws Exception{
        List<String> errors = new ArrayList<String>();
        BudgetDocument bd = createBudgetDocument();
        assertNotNull("Budget document not saved",bd);
        
//        List<BudgetPeriod> periods = bd.getBudgetPeriods();
        BudgetPeriod bp = getBudgetPeriod(bd,1,"2005-01-01","2005-12-31");
        bd.getBudgetPeriods().add(bp);
        BudgetLineItem bli = getLineItem(bp, 1, "400250",dateTimeService.convertToSqlDate("2005-01-01"),
                dateTimeService.convertToSqlDate("2005-12-31"),10000.00d,100.00d);
        bd.getBudgetLineItems().add(bli);
        BudgetCalculationService bcs = getService(BudgetCalculationService.class);
        bcs.calculateBudget(bd);
        LOG.info(bd);
    }
    private BudgetPerson getBudgetPerson(String personId, int personSequenceNumber, 
            int calcBase, String appointTypeCode, String jobCode, String effectiveDate) throws Exception{
        BudgetPerson bper = new BudgetPerson();
        bper.setPersonId(personId);
        bper.setPersonSequenceNumber(personSequenceNumber);
        bper.setCalculationBase(new BudgetDecimal(calcBase));
        bper.setAppointmentTypeCode(appointTypeCode);
        bper.setJobCode(jobCode);
        bper.setEffectiveDate(dateTimeService.convertToSqlDate(effectiveDate));
        return bper;
    }

    @Test
    public void calculateSalaryTest() throws Exception{
        BudgetDocument bd = createBudgetDocument();
        assertNotNull("Budget document not saved",bd);
        
        List<BudgetPeriod> periods = bd.getBudgetPeriods();
        BudgetPeriod bp = getBudgetPeriod(bd,1,"2005-01-01","2005-12-31");
        bd.getBudgetPeriods().add(bp);
        BudgetLineItem bli = getLineItem(bp, 1, "400250",dateTimeService.convertToSqlDate("2005-01-01"),
                dateTimeService.convertToSqlDate("2005-12-31"),10000.00d,100.00d);
        
        BudgetPerson bper = getBudgetPerson("8888888",1,2000,"7","TJC","2005-01-01");
        bd.getBudgetPersons().add(bper);
        BudgetPerson bper1 = getBudgetPerson("9999999",2,4000,"7","TJC","2005-01-01");
        bd.getBudgetPersons().add(bper1);
        
//        bp.getBudgetLineItems().add(bli);
        
        BudgetPersonnelDetails bpli = getPersonnelLineItem(bp, 1, "400250",dateTimeService.convertToSqlDate("2005-01-01"),
                dateTimeService.convertToSqlDate("2005-12-31"),10000.00d,100.00d,1,2,"9999999","TJC",100.00d,100.00d);
        bli.getBudgetPersonnelDetailsList().add(bpli);
        
        BudgetPersonnelDetails bpli1 = getPersonnelLineItem(bp, 1, "400250",dateTimeService.convertToSqlDate("2005-01-01"),
                dateTimeService.convertToSqlDate("2005-12-31"),10000.00d,100.00d,1,1,"8888888","TJC",100.00d,100.00d);
        bli.getBudgetPersonnelDetailsList().add(bpli1);
        List<String> errors = new ArrayList<String>();
        BudgetCalculationService bcs = getService(BudgetCalculationService.class);
        bcs.calculateBudgetLineItem(bd,bli);
        try{
        assertEquals(new BudgetDecimal(4060.00), bpli.getSalaryRequested());
        }catch (AssertionError e) {
            errors.add(e.getMessage());
        }
        try{
        assertEquals(new BudgetDecimal(2030.00), bpli1.getSalaryRequested());
        }catch (AssertionError e) {
            errors.add(e.getMessage());
        }
        if(!errors.isEmpty()){
            throw new AssertionError(errors.toString());
        }

//        LOG.info("Salary requested=>"+bpli.getSalaryRequested());
//        LOG.info("Salary requested=>"+bpli1.getSalaryRequested());
//        
//        LOG.info("Direct cost =>"+bli.getDirectCost());
//        LOG.info("Indirect cost =>"+bli.getIndirectCost());
    }

    @Test
    public void populateCalculatedAmountsTest() throws Exception{
        BudgetDocument bd = createBudgetDocument();
        assertNotNull("Budget document not saved",bd);
        List<BudgetPeriod> periods = bd.getBudgetPeriods();
        BudgetPeriod bp = getBudgetPeriod(bd,1,"2005-01-01","2005-12-31");
        bd.getBudgetPeriods().add(bp);
        BudgetLineItem bli = getLineItem(bp, 1, "400250",dateTimeService.convertToSqlDate("2005-01-01"),
                dateTimeService.convertToSqlDate("2005-12-31"),10000.00d,100.00d);
        BudgetCalculationService bcs = getService(BudgetCalculationService.class);
        bcs.populateCalculatedAmount(bd,bli);
        List<BudgetLineItemCalculatedAmount> calcAmounts = bli.getBudgetLineItemCalculatedAmounts();
        assertEquals(8, calcAmounts.size());
        List<String> possibleRateClassRateTypes = new ArrayList<String>();
        possibleRateClassRateTypes.add("1,1");
        possibleRateClassRateTypes.add("10,1");
        possibleRateClassRateTypes.add("11,1");
        possibleRateClassRateTypes.add("12,1");
        possibleRateClassRateTypes.add("5,1");
        possibleRateClassRateTypes.add("8,1");
        possibleRateClassRateTypes.add("5,3");
        possibleRateClassRateTypes.add("8,2");
        for (BudgetLineItemCalculatedAmount calcAmt : calcAmounts) {
            assertTrue("Unexpected rateclass ratetype combination "+calcAmt, 
                    possibleRateClassRateTypes.contains(calcAmt.getRateClassCode()+","+calcAmt.getRateTypeCode()));
        }
    }

    private BudgetPersonnelDetails getPersonnelLineItem(BudgetPeriod bp, int lineItemNumber, 
            String costElement,Date startDate,Date endDate,
            double lineItemCost,double costSharingAmount, int personNumber,int personSequenceNumber,String personId,String jobCode,double effort,double charged) {
        BudgetPersonnelDetails bli = new BudgetPersonnelDetails();
        bli.setProposalNumber(bp.getProposalNumber());
        bli.setBudgetVersionNumber(bp.getBudgetVersionNumber());
        bli.setBudgetPeriod(bp.getBudgetPeriod());
        bli.setStartDate(startDate);
        bli.setEndDate(endDate);
        bli.setApplyInRateFlag(true);
        bli.setOnOffCampusFlag(true);
        bli.setLineItemNumber(lineItemNumber);
        bli.setBudgetCategoryCode("1");
        bli.setCostElement(costElement);
        bli.setLineItemCost(new BudgetDecimal(lineItemCost));
        bli.setCostSharingAmount(new BudgetDecimal(costSharingAmount));
        bli.setUpdateTimestamp(bp.getUpdateTimestamp());
        bli.setUpdateUser(bp.getUpdateUser());
        bli.setPersonNumber(personNumber);
        bli.setPersonSequenceNumber(personSequenceNumber);
        bli.setPersonId(personId);
        bli.setJobCode(jobCode);
        bli.setPercentEffort(new BudgetDecimal(effort));
        bli.setPercentCharged(new BudgetDecimal(charged));
        return bli;
    }

    private void populateDummyRates(BudgetDocument bd) {
        List<BudgetProposalRate> budgetProposalRates = bd.getBudgetProposalRates();
        List<InstituteRate> instRates = (List)bos.findAll(InstituteRate.class);
        for (InstituteRate instituteRate : instRates) {
            BudgetProposalRate bpr = new BudgetProposalRate();
            bpr.setProposalNumber(bd.getProposalNumber().toString());
            bpr.setProposalNumber(bd.getProposalNumber().toString());
            bpr.setBudgetVersionNumber(bd.getBudgetVersionNumber());
            bpr.setActivityTypeCode(instituteRate.getActivityTypeCode());
            bpr.setFiscalYear(instituteRate.getFiscalYear());
            bpr.setOnOffCampusFlag(instituteRate.getOnOffCampusFlag());
            bpr.setRateClassCode(instituteRate.getRateClassCode());
            bpr.setRateTypeCode(instituteRate.getRateTypeCode());
            bpr.setStartDate(instituteRate.getStartDate());
            bpr.setUnitNumber(instituteRate.getUnitNumber());
            bpr.setInstituteRate(instituteRate.getInstituteRate());
            bpr.setApplicableRate(bpr.getInstituteRate());
            budgetProposalRates.add(bpr);
        }
        List<BudgetProposalLaRate> budgetProposalLaRates = bd.getBudgetProposalLaRates();
        List<InstituteLaRate> instLaRates = (List)bos.findAll(InstituteLaRate.class);
        
        for (InstituteLaRate instituteLaRate : instLaRates) {
            BudgetProposalLaRate bpr = new BudgetProposalLaRate();
            bpr.setProposalNumber(bd.getProposalNumber().toString());
            bpr.setProposalNumber(bd.getProposalNumber().toString());
            bpr.setBudgetVersionNumber(bd.getBudgetVersionNumber());
            bpr.setFiscalYear(instituteLaRate.getFiscalYear());
            bpr.setOnOffCampusFlag(instituteLaRate.getOnOffCampusFlag());
            bpr.setRateClassCode(instituteLaRate.getRateClassCode());
            bpr.setRateTypeCode(instituteLaRate.getRateTypeCode());
            bpr.setStartDate(instituteLaRate.getStartDate());
            bpr.setUnitNumber(instituteLaRate.getUnitNumber());
            bpr.setInstituteRate(instituteLaRate.getInstituteRate());
            bpr.setApplicableRate(bpr.getInstituteRate());
            budgetProposalLaRates.add(bpr);
        }
        
    }

    private BudgetPeriod getBudgetPeriod(BudgetDocument bd, 
                int period, String startDate, String endDate) throws Exception{
        BudgetPeriod bp = new BudgetPeriod();
        bp.setProposalNumber(bd.getProposalNumber().toString());
        bp.setBudgetVersionNumber(bd.getBudgetVersionNumber());
        bp.setBudgetPeriod(period);
        bp.setStartDate(dateTimeService.convertToSqlDate(startDate));
        bp.setEndDate(dateTimeService.convertToSqlDate(endDate));
        bp.setUpdateUser(bd.getUpdateUser());
        bp.setUpdateTimestamp(bd.getUpdateTimestamp());
        return bp;
//        addLineItem(bd,1,1,1,"400250");
        
//        bd.getBudgetPeriods().add(bp);
    }

    private BudgetLineItem getLineItem(BudgetPeriod bp, int lineItemNumber, 
                String costElement,Date startDate,Date endDate,
                double lineItemCost,double costSharingAmount) {
        BudgetLineItem bli = new BudgetLineItem();
        bli.setProposalNumber(bp.getProposalNumber());
        bli.setBudgetVersionNumber(bp.getBudgetVersionNumber());
        bli.setBudgetPeriod(bp.getBudgetPeriod());
        bli.setStartDate(startDate);
        bli.setEndDate(endDate);
        bli.setApplyInRateFlag(true);
        bli.setOnOffCampusFlag(true);
        bli.setLineItemNumber(lineItemNumber);
        bli.setBudgetCategoryCode("1");
        bli.setCostElement(costElement);
        bli.setLineItemCost(new BudgetDecimal(lineItemCost));
        bli.setCostSharingAmount(new BudgetDecimal(costSharingAmount));
        bli.setUpdateTimestamp(bp.getUpdateTimestamp());
        bli.setUpdateUser(bp.getUpdateUser());
        return bli;
        
    }

    private void setBaseDocumentFields(BudgetDocument bd,String proposalNumber) throws Exception{
        bd.getDocumentHeader().setFinancialDocumentDescription("Test budget calculation");
//        bd.setDocumentNumber(bd.getDocumentNumber());
        bd.setProposalNumber(proposalNumber);
        bd.setBudgetVersionNumber(1);
        bd.setStartDate(dateTimeService.convertToSqlDate("2002-01-01"));
        bd.setEndDate(dateTimeService.convertToSqlDate("2008-12-31"));
        bd.setUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
        bd.setUpdateUser("KRADEV");
        bd.setOhRateClassCode("1");
        bd.setUrRateClassCode("1");
        bd.setModularBudgetFlag(false);
        bd.setActivityTypeCode("1");
    }
    
    
    
    private void setBaseDocumentFields(ProposalDevelopmentDocument document, String description, String sponsorCode, String title, Date requestedStartDateInitial, Date requestedEndDateInitial, String activityTypeCode, String proposalTypeCode, String ownedByUnit) {
        document.getDocumentHeader().setFinancialDocumentDescription(description);
        document.setSponsorCode(sponsorCode);
        document.setTitle(title);
        document.setRequestedStartDateInitial(requestedStartDateInitial);
        document.setRequestedEndDateInitial(requestedEndDateInitial);
        document.setActivityTypeCode(activityTypeCode);
        document.setProposalTypeCode(proposalTypeCode);
        document.setOwnedByUnitNumber(ownedByUnit);
    }
}
