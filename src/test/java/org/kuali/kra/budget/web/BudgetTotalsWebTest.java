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
package org.kuali.kra.budget.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.core.service.BusinessObjectService;
import org.kuali.core.service.DocumentService;
import org.kuali.kra.budget.document.BudgetDocument;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.proposaldevelopment.web.ProposalDevelopmentWebTestBase;
import org.kuali.rice.KNSServiceLocator;
import org.kuali.rice.test.SQLDataLoader;
import org.kuali.rice.test.data.PerTestUnitTestData;
import org.kuali.rice.test.data.UnitTestData;
import org.kuali.rice.test.data.UnitTestFile;
import org.kuali.rice.test.data.UnitTestSql;
import org.kuali.rice.testharness.TransactionalLifecycle;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

@PerTestUnitTestData(
        @UnitTestData(
sqlStatements = {
      @UnitTestSql("delete from BUDGET_DETAILS where proposal_number = 999999"),
      @UnitTestSql("delete from BUDGET_DETAILS_CAL_AMTS where proposal_number = 999999")
      }, 
sqlFiles = {
        @UnitTestFile(filename = "classpath:sql/dml/load_budget_line_item_for_total.sql", delimiter = ";"),
        @UnitTestFile(filename = "classpath:sql/dml/load_budget_details_cam_amts.sql", delimiter = ";")
      })
)


public class BudgetTotalsWebTest extends ProposalDevelopmentWebTestBase {
    private static final String PDDOC_BUDGET_VERSIONS_LINK_NAME = "methodToCall.headerTab.headerDispatch.save.navigateTo.budgetVersions.x";
    private static final String BDOC_BUDGET_TOTALS_LINK_NAME = "methodToCall.headerTab.headerDispatch.save.navigateTo.totals.x";
    private static final String NEW_BUDGET_VERSION_NAME = "newBudgetVersionName";
    private static final String ADD_BUDGET_VERSION_BUTTON = "methodToCall.addBudgetVersion";

    private static final String DEFAULT_DOCUMENT_DESCRIPTION = "Proposal Development Web Test";
    private static final String DEFAULT_PROPOSAL_SPONSOR_CODE = "005894";
    private static final String DEFAULT_PROPOSAL_TITLE = "Project title";
    private static final String DEFAULT_PROPOSAL_REQUESTED_START_DATE = "09/01/2008";
    private static final String DEFAULT_PROPOSAL_REQUESTED_END_DATE = "08/31/2012";
    private static final String DEFAULT_PROPOSAL_ACTIVITY_TYPE = "1";
    private static final String DEFAULT_PROPOSAL_TYPE_CODE = "1";
    private static final String DEFAULT_PROPOSAL_OWNED_BY_UNIT = "IN-CARD";

    private static final String  TOTAL_HEAHER = "Totals Expenses Object Code Object Code Name Period 1 Period 2 Period 3 Period 4 Total"; 
    private static final String  OBJECTCODE_LINE_400025 = "400025 Faculty Salaries Tenured - On 160000.00 160000.00 160000.00 160000.00 640000.0"; 
    private static final String  OBJECTCODE_LINE_400390 = "400390 Post-Doctoral Staff 45000.00 45000.00 45000.00 45000.00 180000.0"; 
    private static final String  OBJECTCODE_LINE_400700 = "400700 Graduate Student Staff - On 50000.00 50000.00 50000.00 50000.00 200000.0"; 
    private static final String  OBJECTCODE_LINE_420050 = "420050 Travel Expenses 5000.00 5000.00 5000.00 5000.00 20000.0"; 
    private static final String  OBJECTCODE_LINE_420258 = "420258 Office Supplies 1000.00 1000.00 1000.00 1000.00 4000.0"; 
    private static final String  OBJECTCODE_LINE_420600 = "420600 Subcontracts (first 25K) - Subject to F\\&A 60000.00 60000.00 60000.00 60000.00 240000.0"; 
    private static final String  OBJECTCODE_LINE_420710 = "420710 Consultants 10000.00 10000.00 10000.00 10000.00 40000.0"; 
    private static final String  OH_MTDC_LINE = "Calculated Expenses F & A - MTDC 12000.00 24000.00 36000.00 48000.00 120000.0"; 
    private static final String  OH_TDC_LINE = "F & A - TDC 1000.00 0.00 0.00 0.00 1000.0 "; 
    private static final String  EMP_BENEFIT_LINE = "Fringe Benefits - Research Rate 7000.00 14000.00 21000.00 28000.00 70000.0"; 
    private static final String  VACATION_LINE = "Vacation - Vacation 1000.00 2000.00 3000.00 4000.00 10000.0";
    private static final String  TOTALS_LINE = "Totals 352000.00 371000.00 391000.00 411000.00 1525000.0";
    
    private TransactionalLifecycle transactionalLifecycle;
    private DocumentService documentService = null;
    private String documentNumber;
    private String proposalNumber;
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
        documentService = KNSServiceLocator.getDocumentService();
        transactionalLifecycle = new TransactionalLifecycle();
        transactionalLifecycle.start();
    }

    @After
    public void tearDown() throws Exception {
        transactionalLifecycle.stop();
        super.tearDown();
        documentService = null;
    }


    
    @Test
    public void testBudgetTotals() throws Exception {
        /* get budget version page in proposal development module */
        HtmlPage proposalBudgetVersionsPage = getBudgetVersionsPage();
        /* add new version and open budget version page in budget module */
        addBudgetVersion(proposalBudgetVersionsPage);
        //ProposalDevelopmentDocument pd = (ProposalDevelopmentDocument) documentService.getByDocumentHeaderId(documentNumber);

        // set up the details/detailscalamts properly
        SQLDataLoader sqlDataLoader = new SQLDataLoader("update budget_details set proposal_number ="+ proposalNumber +" where proposal_number=999999");
        sqlDataLoader.runSql();
        sqlDataLoader = new SQLDataLoader("update budget_details_cal_amts set proposal_number ="+ proposalNumber +" where proposal_number=999999");
        sqlDataLoader.runSql();
        sqlDataLoader = new SQLDataLoader("commit");
        sqlDataLoader.runSql();

        // docsearch is hung for proposal page, so try budget version page
        Map fieldValues = new HashMap();
        fieldValues.put("proposalNumber", proposalNumber);
        Collection budgetDocuments = (Collection)KraServiceLocator.getService(BusinessObjectService.class).findMatching(BudgetDocument.class, fieldValues);
        assertNotNull(budgetDocuments);
        HtmlPage budgetVersionsPage = docSearch(((BudgetDocument)budgetDocuments.iterator().next()).getDocumentNumber());
        //HtmlPage budgetVersionsPage = docSearch(Integer.toString(Integer.parseInt(documentNumber)+1));
        /* get budget totals page */
        HtmlPage budgetTotalsPage = clickOn(budgetVersionsPage, BDOC_BUDGET_TOTALS_LINK_NAME);

        
        /* check budget totals page.
         * The numbers displayed in 'asText()' are not formatted
         *  */
        assertContains(budgetTotalsPage, TOTAL_HEAHER);
        assertContains(budgetTotalsPage, OBJECTCODE_LINE_400025);
        assertContains(budgetTotalsPage, OBJECTCODE_LINE_400390);
        assertContains(budgetTotalsPage, OBJECTCODE_LINE_400700);
        assertContains(budgetTotalsPage, OBJECTCODE_LINE_420050);
        assertContains(budgetTotalsPage, OBJECTCODE_LINE_420258);
        assertContains(budgetTotalsPage, OBJECTCODE_LINE_420600);
        assertContains(budgetTotalsPage, OBJECTCODE_LINE_420710);
        assertContains(budgetTotalsPage, OH_MTDC_LINE);
        assertContains(budgetTotalsPage, OH_TDC_LINE);
        assertContains(budgetTotalsPage, EMP_BENEFIT_LINE);
        assertContains(budgetTotalsPage, VACATION_LINE);
        assertContains(budgetTotalsPage, TOTALS_LINE);
        
        // remove details test data
        sqlDataLoader = new SQLDataLoader("delete from budget_details where proposal_number ="+ proposalNumber);
        sqlDataLoader.runSql();
        sqlDataLoader = new SQLDataLoader("delete from budget_details_cal_amts where proposal_number ="+ proposalNumber);
        sqlDataLoader.runSql();
        sqlDataLoader = new SQLDataLoader("commit");
        sqlDataLoader.runSql();

        
    }
    
    private HtmlPage getBudgetVersionsPage() throws Exception {
        HtmlPage proposalPage = this.getProposalDevelopmentPage();
        documentNumber = getFieldValue(proposalPage, "document.documentHeader.documentNumber");

        setRequiredFields(proposalPage, DEFAULT_DOCUMENT_DESCRIPTION,
                DEFAULT_PROPOSAL_SPONSOR_CODE,
                DEFAULT_PROPOSAL_TITLE,
                DEFAULT_PROPOSAL_REQUESTED_START_DATE,
                DEFAULT_PROPOSAL_REQUESTED_END_DATE,
                DEFAULT_PROPOSAL_ACTIVITY_TYPE,
                DEFAULT_PROPOSAL_TYPE_CODE,
                DEFAULT_PROPOSAL_OWNED_BY_UNIT);
        
        proposalPage = this.saveDoc(proposalPage);
        proposalNumber = getProposalNumber(proposalPage);
        
        HtmlPage budgetVersionsPage = clickOn(proposalPage, PDDOC_BUDGET_VERSIONS_LINK_NAME);
        return budgetVersionsPage;
    }
    
    private String getProposalNumber(HtmlPage proposalPage) {
        System.out.println(proposalPage.asXml());
        HtmlTable table = this.getTable(proposalPage, "tab-RequiredFieldsforSavingDocument-div");
        return table.getRow(0).getCell(1).asText().trim();
    }


    private HtmlPage addBudgetVersion(HtmlPage budgetVersionsPage) throws Exception {
        setFieldValue(budgetVersionsPage, NEW_BUDGET_VERSION_NAME, "Test Budget Version - 1");
        HtmlElement addBtn = getElementByName(budgetVersionsPage, ADD_BUDGET_VERSION_BUTTON, true);
        budgetVersionsPage = clickOn(addBtn);
        return budgetVersionsPage;
    }

}
