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
package org.kuali.kra.budget.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.kuali.core.util.ObjectUtils;
import org.kuali.kra.budget.BudgetDecimal;
import org.kuali.kra.budget.bo.BudgetLineItem;
import org.kuali.kra.budget.bo.BudgetLineItemCalculatedAmount;
import org.kuali.kra.budget.bo.BudgetModular;
import org.kuali.kra.budget.bo.BudgetModularIdc;
import org.kuali.kra.budget.bo.BudgetModularSummary;
import org.kuali.kra.budget.bo.BudgetPeriod;
import org.kuali.kra.budget.calculator.LineItemCalculator;
import org.kuali.kra.budget.document.BudgetDocument;
import org.kuali.kra.budget.service.BudgetModularService;

public class BudgetModularServiceImpl implements BudgetModularService {
    
    private static final String consortiumFnaCostElement = "420630";
    
    public void generateModularPeriod(BudgetPeriod budgetPeriod) {

        if (ObjectUtils.isNull(budgetPeriod.getBudgetModular())) {
            // Modular period not initialized yet - create
            BudgetModular budgetModular = 
                new BudgetModular(budgetPeriod.getProposalNumber(), budgetPeriod.getBudgetVersionNumber(), budgetPeriod.getBudgetPeriod());
            budgetPeriod.setBudgetModular(budgetModular);
        }
        
        budgetPeriod.getBudgetModular().calculateAllTotals();
    }
    
    public BudgetModularSummary generateModularSummary(BudgetDocument budgetDocument) {
        BudgetModularSummary modularSummary = new BudgetModularSummary();
        BudgetDecimal directCostLessConsortiumFna = new BudgetDecimal(0);
        BudgetDecimal consortiumFna = new BudgetDecimal(0);
        BudgetDecimal totalDirectCost = new BudgetDecimal(0);
        BudgetDecimal totalFnaRequested = new BudgetDecimal(0);
        BudgetDecimal totalRequestedCost = new BudgetDecimal(0);
        List<BudgetModularIdc> budgetModularIdcs = new ArrayList<BudgetModularIdc>();
        
        for (BudgetPeriod budgetPeriod: budgetDocument.getBudgetPeriods()) {
            BudgetModular budgetModular = budgetPeriod.getBudgetModular();
            if (!ObjectUtils.isNull(budgetModular)) {
                budgetModular.calculateAllTotals();
                directCostLessConsortiumFna = directCostLessConsortiumFna.add(budgetModular.getDirectCostLessConsortiumFna());
                consortiumFna = consortiumFna.add(budgetModular.getConsortiumFna());
                totalDirectCost = totalDirectCost.add(budgetModular.getTotalDirectCost());
                totalFnaRequested = totalFnaRequested.add(budgetModular.getTotalFnaRequested());
                totalRequestedCost = totalRequestedCost.add(budgetModular.getTotalRequestedCost());
                budgetModularIdcs.addAll(budgetModular.getBudgetModularIdcs());
            }
        }
        
        modularSummary.setDirectCostLessConsortiumFna(directCostLessConsortiumFna);
        modularSummary.setConsortiumFna(consortiumFna);
        modularSummary.setTotalDirectCost(totalDirectCost);
        modularSummary.setTotalFnaRequested(totalFnaRequested);
        modularSummary.setTotalRequestedCost(totalRequestedCost);
        modularSummary.setBudgetModularIdcs(budgetModularIdcs);
        
        return modularSummary;
    }
    
    public void synchModularBudget(BudgetDocument budgetDocument) {
        for (BudgetPeriod budgetPeriod: budgetDocument.getBudgetPeriods()) {
            
            if (ObjectUtils.isNull(budgetPeriod.getBudgetModular())) {
                // Modular period not initialized yet - create
                budgetPeriod.setBudgetModular(
                        new BudgetModular(budgetPeriod.getProposalNumber(), budgetPeriod.getBudgetVersionNumber(), budgetPeriod.getBudgetPeriod()));
            }
            
            BudgetModular budgetModular = budgetPeriod.getBudgetModular();
            budgetModular.setBudgetModularIdcs(new ArrayList<BudgetModularIdc>());
            BudgetDecimal directCostLessConsortiumFna = new BudgetDecimal(0);
            BudgetDecimal consortiumFna = new BudgetDecimal(0);
            
            for (BudgetLineItem budgetLineItem: budgetPeriod.getBudgetLineItems()) {
                new LineItemCalculator(budgetDocument, budgetLineItem).calculate();
//              TODO use configuration service
                if (budgetLineItem.getCostElement().equals("420630") || budgetLineItem.getCostElement().equals("420610")) {
                    // Consortium F&A
                    consortiumFna = consortiumFna.add(budgetLineItem.getDirectCost());
                    break;
                }
                // Loop through line item calculated amounts
                BudgetDecimal rateMultiplier = new BudgetDecimal(100);
                for (Iterator iter = budgetLineItem.getBudgetLineItemCalculatedAmounts().iterator(); iter.hasNext();) {
                    
                    BudgetLineItemCalculatedAmount budgetLineItemCalculatedAmount = (BudgetLineItemCalculatedAmount) iter.next();
                    budgetLineItemCalculatedAmount.refreshReferenceObject("rateClass");
                    if (budgetLineItemCalculatedAmount.getRateClass().getRateClassType().equals("O")) {
                        // F&A
                        BudgetModularIdc budgetModularIdc = new BudgetModularIdc();
                        //budgetDocument.refreshReferenceObject("documentNextvalues");
                        budgetModularIdc.setRateNumber(budgetDocument.getHackedDocumentNextValue("rateNumber"));
                        budgetModularIdc.setFundsRequested(budgetLineItemCalculatedAmount.getCalculatedCost());
                        budgetModularIdc.setDescription(budgetLineItemCalculatedAmount.getRateClassCode());
                        budgetModularIdc.setIdcBase(budgetLineItem.getLineItemCost());
                        budgetModularIdc.setIdcRate(
                                budgetModularIdc.getFundsRequested().divide(budgetModularIdc.getIdcBase()).multiply(rateMultiplier));
                        budgetModular.addNewBudgetModularIdc(budgetModularIdc);
                    }
                }
                directCostLessConsortiumFna = directCostLessConsortiumFna.add(budgetLineItem.getDirectCost());
            }
            BudgetDecimal modularTdc = new BudgetDecimal(0);
            while (directCostLessConsortiumFna.isGreaterThan(modularTdc)) {
                modularTdc = modularTdc.add(new BudgetDecimal(25000));
            }
            budgetModular.setDirectCostLessConsortiumFna(modularTdc);
            budgetModular.setConsortiumFna(consortiumFna);
            budgetModular.calculateAllTotals();
        }
    }
}
