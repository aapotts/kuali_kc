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
package org.kuali.kra.budget.service.impl;

import javax.servlet.http.HttpServletResponse;

import org.kuali.kra.budget.document.BudgetDocument;
import org.kuali.kra.budget.service.BudgetPrintService;
import org.kuali.kra.proposaldevelopment.bo.AttachmentDataSource;

public class BudgetPrintServiceDummyImpl implements BudgetPrintService{

    public void populateBudgetPrintForms(BudgetDocument budgetDocument) {
        // TODO Auto-generated method stub
        throw new RuntimeException("Not supported functionality");
    }

    public boolean printBudgetForms(BudgetDocument budgetDocument, String[] selectedBudgetPrintFormId, HttpServletResponse response) {
        // TODO Auto-generated method stub
        throw new RuntimeException("Not supported functionality");
    }

    public AttachmentDataSource readBudgetPrintStream(BudgetDocument budgetDocument, String selectedBudgetPrintFormId) {
        // TODO Auto-generated method stub
        throw new RuntimeException("Not supported functionality");
    }

}
