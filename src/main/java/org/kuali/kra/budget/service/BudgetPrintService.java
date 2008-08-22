/*
 * Copyright 2006-2008 The Kuali Foundation
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
package org.kuali.kra.budget.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.kuali.kra.budget.bo.BudgetPrintForm;
import org.kuali.kra.budget.document.BudgetDocument;
import org.kuali.kra.proposaldevelopment.bo.AttachmentDataSource;

/**
 * This class...
 */
public interface BudgetPrintService {
    public void populateBudgetPrintForms(BudgetDocument budgetDocument);
    public AttachmentDataSource readBudgetPrintStream(BudgetDocument budgetDocument, String selectedBudgetPrintFormId);
    
    public void printBudgetForms(BudgetDocument budgetDocument, String[] selectedBudgetPrintFormId, HttpServletResponse response);

}
