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
package org.kuali.kra.proposaldevelopment.budget.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.kuali.kra.budget.core.Budget;
import org.kuali.kra.proposaldevelopment.bo.AttachmentDataSource;
import org.kuali.kra.proposaldevelopment.budget.bo.BudgetPrintForm;
import org.kuali.kra.proposaldevelopment.budget.service.BudgetPrintService;

public class BudgetPrintServiceDummyImpl implements BudgetPrintService{
    public void populateBudgetPrintForms(Budget budget) {
        List<BudgetPrintForm> printForms = new ArrayList<BudgetPrintForm>();
        budget.setBudgetPrintForms(printForms);
    }

    public boolean printBudgetForms(Budget budget, String[] selectedBudgetPrintFormId, HttpServletResponse response) {
        // TODO Auto-generated method stub
        throw new RuntimeException("Not supported functionality");
    }

    public AttachmentDataSource readBudgetPrintStream(Budget budget, String selectedBudgetPrintFormId) {
        // TODO Auto-generated method stub
        throw new RuntimeException("Not supported functionality");
    }

}
