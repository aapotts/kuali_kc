/*
 * Copyright 2006-2009 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.osedu.org/licenses/ECL-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kra.budget.calculator;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.kra.KraTestBase;
import org.kuali.kra.bo.InstituteLaRate;
import org.kuali.kra.bo.InstituteRate;
import org.kuali.kra.budget.BudgetDecimal;
import org.kuali.kra.budget.core.Budget;
import org.kuali.kra.budget.document.BudgetDocument;
import org.kuali.kra.budget.nonpersonnel.BudgetLineItem;
import org.kuali.kra.budget.nonpersonnel.BudgetLineItemCalculatedAmount;
import org.kuali.kra.budget.parameters.BudgetPeriod;
import org.kuali.kra.budget.personnel.BudgetPersonnelDetails;
import org.kuali.kra.budget.rates.BudgetLaRate;
import org.kuali.kra.budget.rates.BudgetRate;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.proposaldevelopment.service.ProposalDevelopmentService;
import org.kuali.rice.kns.UserSession;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.service.DocumentService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.test.data.PerTestUnitTestData;
import org.kuali.rice.test.data.UnitTestData;
import org.kuali.rice.test.data.UnitTestFile;
import org.kuali.rice.test.data.UnitTestSql;

/**
 * This class is for testing Line item calculations
 */
@PerTestUnitTestData(
        @UnitTestData(order = { 
                UnitTestData.Type.SQL_STATEMENTS, UnitTestData.Type.SQL_FILES }, 
        sqlStatements = {
                      @UnitTestSql("delete from VALID_CE_RATE_TYPES where cost_element like '900%'"),
                      @UnitTestSql("delete from cost_element where cost_element like '900%'")
                      ,@UnitTestSql("commit")
                      }, 
        sqlFiles = {
                @UnitTestFile(filename = "classpath:sql/dml/load_calc_service_test_cost_element.sql", delimiter = ";")
                ,@UnitTestFile(filename = "classpath:sql/dml/load_calc_service_test_valid_ce_rate_types.sql", delimiter = ";")
                })
        )

public class BudgetCalculatorServiceTest extends KraTestBase {
    private static final org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(BudgetCalculatorServiceTest.class);
    private DocumentService documentService = null;
    private BusinessObjectService bos;
    private ProposalDevelopmentService proposalDevelopmentService;
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
        GlobalVariables.setUserSession(new UserSession("quickstart"));
        documentService = KNSServiceLocator.getDocumentService();
        bos = KraServiceLocator.getService(BusinessObjectService.class);
        proposalDevelopmentService = KraServiceLocator.getService(ProposalDevelopmentService.class);
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
        
        proposalDevelopmentService.initializeUnitOrganizationLocation(document);
        proposalDevelopmentService.initializeProposalSiteNumbers(document);

        documentService.saveDocument(document);
        BudgetDocument bd = (BudgetDocument)documentService.getNewDocument("BudgetDocument");
        setBaseDocumentFields(bd,document.getDevelopmentProposal().getProposalNumber());
        documentService.saveDocument(bd);
        BudgetDocument savedBudgetDocument = (BudgetDocument)documentService.getByDocumentHeaderId(bd.getDocumentNumber());
        populateDummyRates(savedBudgetDocument.getBudget());
        assertNotNull("Budget document not saved",savedBudgetDocument);
        return savedBudgetDocument;
    }
    @Test
    public void calculateLineItemWithoutRatesTest() throws Exception{
        List<String> errors = new ArrayList<String>();
        Budget bd = createBudgetDocument().getBudget();
        assertNotNull("Budget document not saved",bd);
        
        BudgetPeriod bp = getBudgetPeriod(bd,1,"2004-01-01","2005-12-31");
        bd.getBudgetPeriods().add(bp);
        BudgetLineItem bli = getLineItem(bp, 1, "900000",java.sql.Date.valueOf("2005-01-01"),
                java.sql.Date.valueOf("2005-12-31"),10000.00d,100.00d);
        bp.getBudgetLineItems().add(bli);
        BudgetCalculationService bcs = getService(BudgetCalculationService.class);
        bcs.calculateBudgetLineItem(bd,bli);
        List<BudgetLineItemCalculatedAmount> calcAmounts = bli.getBudgetLineItemCalculatedAmounts();
        assertTrue(calcAmounts.isEmpty());
        for (BudgetLineItemCalculatedAmount budgetLineItemCalculatedAmount : calcAmounts) {
            LOG.info(budgetLineItemCalculatedAmount);
        }
        BudgetDecimal directCost = bli.getDirectCost();
        assertEquals(new BudgetDecimal(10000.00),directCost );
    }
    @Test
    public void calculateLineItemWithMTDCTest() throws Exception{
        List<String> errors = new ArrayList<String>();
        Budget bd = createBudgetDocument().getBudget();
        assertNotNull("Budget document not saved",bd);
        
        BudgetPeriod bp = getBudgetPeriod(bd,1,"2004-01-01","2005-12-31");
        bd.getBudgetPeriods().add(bp);
        BudgetLineItem bli = getLineItem(bp, 1, "900001",java.sql.Date.valueOf("2000-01-01"),
                java.sql.Date.valueOf("2000-06-30"),10000.00d,100.00d);
        bp.getBudgetLineItems().add(bli);
        BudgetCalculationService bcs = getService(BudgetCalculationService.class);
        bcs.calculateBudgetLineItem(bd,bli);
        List<BudgetLineItemCalculatedAmount> calcAmounts = bli.getBudgetLineItemCalculatedAmounts();
        assertTrue(calcAmounts.size()==1);
        for (BudgetLineItemCalculatedAmount budgetLineItemCalculatedAmount : calcAmounts) {
            LOG.info(budgetLineItemCalculatedAmount);
        }
        BudgetDecimal directCost = bli.getDirectCost();
        assertEquals(new BudgetDecimal(10000.00),directCost );
        BudgetDecimal indirectCost = bli.getIndirectCost();
        assertEquals(new BudgetDecimal(6350.00),indirectCost );
        
    }

    private BudgetPersonnelDetails getPersonnelLineItem(BudgetPeriod bp, int lineItemNumber, 
            String costElement,Date startDate,Date endDate,
            double lineItemCost,double costSharingAmount, 
            int personNumber,int personSequenceNumber,String personId,String jobCode,double effort,double charged) {
        BudgetPersonnelDetails bli = new BudgetPersonnelDetails();
//        bli.setProposalNumber(bp.getProposalNumber());
//        bli.setBudgetVersionNumber(bp.getBudgetVersionNumber());
        bli.setBudgetId(bp.getBudgetId());
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

    private void populateDummyRates(Budget bd) {
        List<BudgetRate> budgetRates = bd.getBudgetRates();
        List<InstituteRate> instRates = (List)bos.findAll(InstituteRate.class);
        for (InstituteRate instituteRate : instRates) {
            BudgetRate bpr = new BudgetRate();
            bpr.setBudgetId(bd.getBudgetId());
            bpr.setActivityTypeCode(instituteRate.getActivityTypeCode());
            bpr.setFiscalYear(instituteRate.getFiscalYear());
            bpr.setOnOffCampusFlag(instituteRate.getOnOffCampusFlag());
            bpr.setRateClassCode(instituteRate.getRateClassCode());
            bpr.setRateTypeCode(instituteRate.getRateTypeCode());
            bpr.setStartDate(instituteRate.getStartDate());
            bpr.setUnitNumber(instituteRate.getUnitNumber());
            bpr.setInstituteRate(instituteRate.getInstituteRate());
            bpr.setApplicableRate(bpr.getInstituteRate());
            budgetRates.add(bpr);
        }
        List<BudgetLaRate> budgetLaRates = bd.getBudgetLaRates();
        List<InstituteLaRate> instLaRates = (List)bos.findAll(InstituteLaRate.class);
        
        for (InstituteLaRate instituteLaRate : instLaRates) {
            BudgetLaRate bpr = new BudgetLaRate();
            bpr.setBudgetId(bd.getBudgetId());
            bpr.setFiscalYear(instituteLaRate.getFiscalYear());
            bpr.setOnOffCampusFlag(instituteLaRate.getOnOffCampusFlag());
            bpr.setRateClassCode(instituteLaRate.getRateClassCode());
            bpr.setRateTypeCode(instituteLaRate.getRateTypeCode());
            bpr.setStartDate(instituteLaRate.getStartDate());
            bpr.setUnitNumber(instituteLaRate.getUnitNumber());
            bpr.setInstituteRate(instituteLaRate.getInstituteRate());
            bpr.setApplicableRate(bpr.getInstituteRate());
            budgetLaRates.add(bpr);
        }
        
    }

    private BudgetPeriod getBudgetPeriod(Budget bd, int period, String startDate, String endDate) {
        BudgetPeriod bp = new BudgetPeriod();
//        bp.setProposalNumber(bd.getBudgetDocument().getParentDocument().getDevelopmentProposal().getProposalNumber().toString());
//        bp.setBudgetVersionNumber(bd.getBudgetVersionNumber());
        bp.setBudget(bd);
        bp.setBudgetPeriod(period);
        bp.setStartDate(java.sql.Date.valueOf(startDate));
        bp.setEndDate(java.sql.Date.valueOf(endDate));
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
//        bli.setProposalNumber(bp.getProposalNumber());
//        bli.setBudgetVersionNumber(bp.getBudgetVersionNumber());
        bli.setBudgetId(bp.getBudgetId());
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

    private void setBaseDocumentFields(BudgetDocument bd,String proposalNumber) {
        bd.getDocumentHeader().setDocumentDescription("Test budget calculation");
//        bd.setDocumentNumber(bd.getDocumentNumber());
        Budget budget = bd.getBudget();
        budget.setBudgetId(budget.getBudgetId());
        budget.setBudgetVersionNumber(1);
        budget.setStartDate(java.sql.Date.valueOf("2002-01-01"));
        budget.setEndDate(java.sql.Date.valueOf("2008-12-31"));
        budget.setUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
        budget.setUpdateUser("KRADEV");
        budget.setOhRateClassCode("1");
        budget.setUrRateClassCode("1");
        budget.setModularBudgetFlag(false);
        budget.setActivityTypeCode("1");
    }
    
    
    
    private void setBaseDocumentFields(ProposalDevelopmentDocument document, String description, String sponsorCode, String title, Date requestedStartDateInitial, Date requestedEndDateInitial, String activityTypeCode, String proposalTypeCode, String ownedByUnit) {
        document.getDocumentHeader().setDocumentDescription(description);
        document.getDevelopmentProposal().setSponsorCode(sponsorCode);
        document.getDevelopmentProposal().setTitle(title);
        document.getDevelopmentProposal().setRequestedStartDateInitial(requestedStartDateInitial);
        document.getDevelopmentProposal().setRequestedEndDateInitial(requestedEndDateInitial);
        document.getDevelopmentProposal().setActivityTypeCode(activityTypeCode);
        document.getDevelopmentProposal().setProposalTypeCode(proposalTypeCode);
        document.getDevelopmentProposal().setOwnedByUnitNumber(ownedByUnit);
    }
}
