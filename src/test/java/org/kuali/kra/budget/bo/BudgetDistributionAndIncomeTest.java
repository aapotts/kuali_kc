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
package org.kuali.kra.budget.bo;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.kuali.kra.budget.BudgetDecimal;
import org.kuali.kra.budget.document.BudgetDocument;

public class BudgetDistributionAndIncomeTest {
    protected static final BudgetDecimal TEST_AMOUNT_100 = new BudgetDecimal(100.0);
    protected static final BudgetDecimal TEST_AMOUNT_250 = new BudgetDecimal(250.0);
    protected static final int DAY_1 = 1;
    protected static final int DAY_2 = 2;
    protected static final int DAY_30 = 30;
    protected static final int YEAR_2000 = 2000;
    protected static final int YEAR_2007 = 2007;
    protected static final int YEAR_2008 = 2008;
    protected static final int YEAR_2009 = 2009;
    protected static final BudgetDecimal ZERO_AMOUNT = BudgetDecimal.ZERO;
    
    protected BudgetDecimal[] costShareAmounts = { TEST_AMOUNT_100, ZERO_AMOUNT, TEST_AMOUNT_100, TEST_AMOUNT_100, TEST_AMOUNT_100, TEST_AMOUNT_100 };
    protected BudgetDecimal[] unrecoveredFandAAmounts = { TEST_AMOUNT_250, ZERO_AMOUNT, TEST_AMOUNT_100, TEST_AMOUNT_250, TEST_AMOUNT_250, TEST_AMOUNT_100 };
    
    protected BudgetDocument budgetDocument;
    protected Calendar calendar;
    private Date fiscalYearStartArtifact;    
    
    @Before
    public void setUp() {
        calendar = GregorianCalendar.getInstance();
        fiscalYearStartArtifact = getDate(YEAR_2000, Calendar.OCTOBER, DAY_1);
        budgetDocument = new BudgetDocument_CostShareAndUnrecoveredFandAApplicable();        
    }
    
    @After
    public void tearDown() {
        budgetDocument = null;
        calendar = null;
    }
    
    protected BudgetPeriod createAndAddBudgetPeriod(Date startDate, Date endDate) {
        BudgetPeriod budgetPeriod = new BudgetPeriod();
        budgetPeriod.setStartDate(startDate);
        budgetPeriod.setEndDate(endDate);
        budgetDocument.add(budgetPeriod);
        return budgetPeriod;
    }
    
    protected Date getDate(int year, int month, int date) {
        calendar.set(year, month, date);
        return new Date(calendar.getTimeInMillis());
    }

    protected BudgetPeriod createAndAddBudgetPeriod() {
        BudgetPeriod budgetPeriod = new BudgetPeriod();
        budgetDocument.add(budgetPeriod);
        return budgetPeriod;
    }

    protected void createBudgetPeriodsForThreeFiscalYears() {
        //FY 2007
        createAndAddBudgetPeriod(getDate(YEAR_2007, Calendar.JANUARY, DAY_1), getDate(YEAR_2007, Calendar.MARCH, DAY_30));        
        createAndAddBudgetPeriod(getDate(YEAR_2007, Calendar.APRIL, DAY_1), getDate(YEAR_2007, Calendar.JUNE, DAY_30));        
        createAndAddBudgetPeriod(getDate(YEAR_2007, Calendar.JULY, DAY_1), getDate(YEAR_2007, Calendar.SEPTEMBER, DAY_30));
        
        //FY 2008
        createAndAddBudgetPeriod(getDate(YEAR_2007, Calendar.NOVEMBER, DAY_1), getDate(YEAR_2007, Calendar.DECEMBER, DAY_30));
        
        //FY 2009
        createAndAddBudgetPeriod(getDate(YEAR_2008, Calendar.OCTOBER, DAY_2), getDate(YEAR_2008, Calendar.DECEMBER, DAY_30));
        createAndAddBudgetPeriod(getDate(YEAR_2008, Calendar.OCTOBER, DAY_1), getDate(YEAR_2009, Calendar.OCTOBER, DAY_30));
        
        
        int i = 0;
        for(BudgetPeriod bp: budgetDocument.getBudgetPeriods()) {
            bp.setCostSharingAmount(costShareAmounts[i]);
            bp.setUnderrecoveryAmount(unrecoveredFandAAmounts[i]);
            i++;
        }        
    }
    
    protected class BudgetDocument_CostShareAndUnrecoveredFandAApplicable extends BudgetDocument {
        private static final long serialVersionUID = 1L;
                
        @Override
        protected Boolean loadCostSharingApplicability() {
           return Boolean.TRUE;
        }

        @Override
        protected Date loadFiscalYearStart() {
            return fiscalYearStartArtifact;
        }

        @Override
        protected Boolean loadUnrecoveredFandAApplicability() {
            return Boolean.TRUE;
        }
        
        protected void setCostShareApplicability(Boolean value) {
            
        }
    }
    
    protected class BudgetDocument_CostShareAndUnrecoveredFandANotApplicable extends BudgetDocument {
        private static final long serialVersionUID = 1L;
                
        @Override
        protected Boolean loadCostSharingApplicability() {
           return Boolean.FALSE;
        }

        @Override
        protected Date loadFiscalYearStart() {
            return fiscalYearStartArtifact;
        }

        @Override
        protected Boolean loadUnrecoveredFandAApplicability() {
            return Boolean.FALSE;
        }
    }
}