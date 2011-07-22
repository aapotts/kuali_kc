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
package org.kuali.kra.external.budget.impl;
/**
 * This client connects to the Financial Budget Adjustment Service and creates a Budget Adjustment
 * Document. 
 */



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;







import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.kfs.integration.cg.budgetService.BudgetAdjustmentCreationStatusDTO;
import org.kuali.kfs.integration.cg.budgetService.BudgetAdjustmentParametersDTO;
import org.kuali.kfs.integration.cg.budgetService.BudgetAdjustmentService;
import org.kuali.kfs.integration.cg.budgetService.Details;
import org.kuali.kra.award.budget.AwardBudgetExt;
import org.kuali.kra.award.budget.AwardBudgetVersionOverviewExt;
import org.kuali.kra.award.budget.document.AwardBudgetDocument;
import org.kuali.kra.award.document.AwardDocument;
import org.kuali.kra.award.home.Award;
import org.kuali.kra.budget.BudgetDecimal;
import org.kuali.kra.budget.calculator.BudgetCalculationService;
import org.kuali.kra.budget.core.Budget;
import org.kuali.kra.budget.core.CostElement;
import org.kuali.kra.budget.rates.RateType;
import org.kuali.kra.budget.versions.BudgetDocumentVersion;
import org.kuali.kra.external.budget.BudgetAdjustmentClient;
import org.kuali.kra.external.budget.BudgetAdjustmentServiceHelper;
import org.kuali.kra.external.budget.FinancialObjectCodeMapping;
import org.kuali.kra.external.budget.RateClassRateType;
import org.kuali.kra.external.unit.service.InstitutionalUnitService;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.service.DocumentService;
import org.kuali.rice.kns.service.ParameterService;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.util.KNSConstants;
import org.kuali.rice.kns.util.ObjectUtils;


/**
 * This is the base class for the client that connects to the Budget Adjustment Service on the
 * financial system.
 * 
 */

public abstract class BudgetAdjustmentClientBase implements BudgetAdjustmentClient {

   
    private DocumentService documentService;
    private ParameterService parameterService;
    private BudgetCalculationService budgetCalculationService;
    
    private static final int JULY_INDEXING = 5;
    protected static final QName SERVICE_NAME = new QName("KFS", "budgetAdjustmentServiceSOAP");

    private static final Log LOG = LogFactory.getLog(BudgetAdjustmentClientBase.class);
    private Calendar calendar = Calendar.getInstance();
    AwardBudgetDocument awardBudgetDocument;
    Budget previousBudget;
    BudgetAdjustmentParametersDTO budgetAdjustmentParametersDTO;

    Map<String, BudgetDecimal> accountingLines;
    BudgetAdjustmentServiceHelper helper;
    private BusinessObjectService businessObjectService;
    private InstitutionalUnitService institutionalUnitService;
    
    /*
     * gets the 
     */
    protected abstract BudgetAdjustmentService getServiceHandle();

    public void createBudgetAdjustmentDocument() throws Exception {
        setAwardBudgetDocument(awardBudgetDocument);
        boolean complete = setBudgetAdjustmentParameters();     
        if (complete) {           
            try {
                BudgetAdjustmentService port = getServiceHandle();            
                LOG.info("Invoking createBudgetAdjustment...");           
                BudgetAdjustmentCreationStatusDTO budgetAdjustmentStatus = port.createBudgetAdjustment(budgetAdjustmentParametersDTO);
            
                if (budgetAdjustmentStatus.getStatus().equalsIgnoreCase("success")) {
                    if (budgetAdjustmentStatus.getDocumentNumber() == null) {
                        GlobalVariables.getMessageMap().putError(KNSConstants.GLOBAL_MESSAGES, KeyConstants.DOCUMENT_NUMBER_NULL);
                        awardBudgetDocument.refresh();
                        LOG.warn("Document number returned from KFS budget adjustment service is null.");
                   
                    } else {
                        awardBudgetDocument.getBudget().setBudgetAdjustmentDocumentNumber(budgetAdjustmentStatus.getDocumentNumber());
                        documentService.saveDocument(awardBudgetDocument);
                    }
                } else {
                    String completeErrorMessage = "";
                    List<String> errorMessages = budgetAdjustmentStatus.getErrorMessages();
                    for (String errorMessage : errorMessages) {
                        completeErrorMessage += errorMessage;
                    }
                    GlobalVariables.getMessageMap().putError(KNSConstants.GLOBAL_ERRORS, 
                                                             KeyConstants.BUDGET_ADJUSTMENT_DOCUMENT_NOT_CREATED, completeErrorMessage); 
                }
            } catch (WebServiceException e) {
                String errorMessage = "Cannot connect to the service. The service may be down, please try again later.";
                GlobalVariables.getMessageMap().putError(KNSConstants.GLOBAL_ERRORS, KeyConstants.CANNOT_CONNECT_TO_SERVICE);
                LOG.error(errorMessage + e.getMessage(), e);
            } 
        } 
    }
   
    public boolean setBudgetAdjustmentParameters() throws Exception {
        boolean complete = true;    
        complete &= createBudgetAdjustmentDocumentHeader();
                       
        budgetCalculationService.calculateBudgetSummaryTotals(getAwardBudgetDocument().getAwardBudget());
        accountingLines = new HashMap<String, BudgetDecimal>();
        
        complete &= setNonPersonnelAccountingLines();
        
        // non personnel calculated direct cost
        complete &= setNonPersonnelCalculatedDirectCostAccountingLines();
        
        // salary
        complete &= setPersonnelSalaryAccountingLines();
       
        // calculated direct cost
        complete &= setPersonnnelCalculatedDirectCost();
        
        // Indirect cost 
        complete &= setIndirectCostAccountingLine();    
                
        // fringe
        complete &= setPersonnelFringeAccountingLines();
        
        createAccountingLines();

        return complete;
    }
   
    
    private boolean setPersonnnelCalculatedDirectCost() {
        boolean complete = true;
        Map<RateClassRateType, BudgetDecimal> netPersonnelCalculatedDirectCost = getHelper().getPersonnelCalculatedDirectCost();

        for (RateClassRateType rate : netPersonnelCalculatedDirectCost.keySet()) {
           
            String financialObjectCode = getFinancialObjectCode(rate.getRateClass(), rate.getRateType());
            if (ObjectUtils.isNull(financialObjectCode)) {
                complete &= false;
            } else {
                if (!accountingLines.containsKey(financialObjectCode)) {
                    accountingLines.put(financialObjectCode, netPersonnelCalculatedDirectCost.get(rate));
                } else {
                    accountingLines.put(financialObjectCode, 
                                        accountingLines.get(financialObjectCode).add(netPersonnelCalculatedDirectCost.get(rate)));
                }
            }
        }
        return complete;
    }

    protected boolean setIndirectCostAccountingLine() {
        boolean complete = true;
        Map<RateClassRateType, BudgetDecimal> netIndirectCost = getHelper().getIndirectCost();  
        for (RateClassRateType rate : netIndirectCost.keySet()) {
            Details details = new Details();
            details.setCurrentAmount(netIndirectCost.get(rate).toString());
            String financialObjectCode = getFinancialObjectCode(rate.getRateClass(), rate.getRateType());
            if (ObjectUtils.isNull(financialObjectCode)) {
               complete &= false; 
            } else {
                if (!accountingLines.containsKey(financialObjectCode)) {
                    accountingLines.put(financialObjectCode, netIndirectCost.get(rate));
                } else {
                    accountingLines.put(financialObjectCode, 
                                        accountingLines.get(financialObjectCode).add(netIndirectCost.get(rate)));
                }
            }
            details.setObjectCode(financialObjectCode);
            details.setChart(getAwardChart(getAwardBudgetDocument()));
            details.setAccount(getAwardAccount(getAwardBudgetDocument()));
            details.setProjectCode("");
            details.setSubAccount("");
            //budgetAdjustmentParametersDTO.getDetails().add(details);
            LOG.info("Details: Account: " + details.getAccount()
                    + "Chart: " + details.getChart() 
                    + "CurrentBudgetAdjustment: " + details.getCurrentAmount() 
                    + "ObjectCode: " + details.getObjectCode()
                    + "ProjectCode: " + details.getProjectCode());
        }
        return complete;
    }
    
    protected boolean setPersonnelFringeAccountingLines() {
        boolean complete = true;
        Map<RateClassRateType, BudgetDecimal> netFringeCost = getHelper().getPersonnelFringeCost();
        for (RateClassRateType rate : netFringeCost.keySet()) {
           
            String financialObjectCode = getFinancialObjectCode(rate.getRateClass(), rate.getRateType());
            if (ObjectUtils.isNull(financialObjectCode)) {
                complete &= false;
            } else {
                if (!accountingLines.containsKey(financialObjectCode)) {
                    accountingLines.put(financialObjectCode, netFringeCost.get(rate));
                } else {
                    accountingLines.put(financialObjectCode, 
                                        accountingLines.get(financialObjectCode).add(netFringeCost.get(rate)));
                }
            }
        }
        return complete;
    }
    
    protected boolean  setPersonnelSalaryAccountingLines() throws Exception {
        boolean complete = true;
        SortedMap<String, BudgetDecimal> netCost = getHelper().getPersonnelSalaryCost();
        for (String name : netCost.keySet()) {
          
            String financialObjectCode = getFinancialObjectCode(name);
           
            if (ObjectUtils.isNull(financialObjectCode)) {
                complete &= false;
            } else {
                if (!accountingLines.containsKey(financialObjectCode)) {
                    accountingLines.put(financialObjectCode, netCost.get(name));
                } else {
                    accountingLines.put(financialObjectCode, 
                                        accountingLines.get(financialObjectCode).add(netCost.get(name)));
                }
            }
         }    
        return complete;
    }   
    
    protected String[] getElements(String person) throws Exception {
        if (person.contains(",")) {
            String[] personElements = person.split(",");
            return personElements;
        }
        LOG.error("The string is not in the format objectCode,personId  . Unable to retrieve object code.");
        throw new Exception("The string " + person + "is not in the format objectCode,personId  . Unable to retrieve object code.");
    }
    
    protected boolean setNonPersonnelCalculatedDirectCostAccountingLines() {
        boolean complete = true;
        SortedMap<RateType, BudgetDecimal> netExpense = getHelper().getNonPersonnelCalculatedDirectCost();
        // TODO Auto-generated method stub
        SortedMap<RateType, List<BudgetDecimal>> currentNonPersonnelCalcDirectCost = getAwardBudgetDocument().getAwardBudget().getNonPersonnelCalculatedExpenseTotals();
        
        for (RateType rateType : netExpense.keySet()) {
            if (!rateType.getDescription().equalsIgnoreCase("MTDC")) {
                List<BudgetDecimal> expenses = currentNonPersonnelCalcDirectCost.get(rateType); 
                Details details = new Details();
                details.setCurrentAmount(netExpense.get(rateType).toString());
                // only need abs value of amount
                String financialObjectCode = getFinancialObjectCode(rateType.getRateClassCode(), rateType.getRateTypeCode());
                if (ObjectUtils.isNull(financialObjectCode)) {
                    complete &= false;
                } else {
                    if (!accountingLines.containsKey(financialObjectCode)) {
                        accountingLines.put(financialObjectCode, netExpense.get(rateType));
                    } else {
                        accountingLines.put(financialObjectCode, 
                                            accountingLines.get(financialObjectCode).add(netExpense.get(rateType)));
                    }
                }
               
            }
        }
        return complete;
    }
    

    private boolean setNonPersonnelAccountingLines() {
        HashMap<String, BudgetDecimal> nonPersonnelCost = getHelper().getNonPersonnelCost();
        boolean complete = true;
        for (String costElement : nonPersonnelCost.keySet()) {
            if (ObjectUtils.isNotNull(getFinancialObjectCode(costElement))) {
                BudgetDecimal currentAmount = nonPersonnelCost.get(costElement).abs();
                // only add line item if amount is non-zero
                if (currentAmount.isNonZero()) {
                   
                    String financialObjectCode = getFinancialObjectCode(costElement);
                    if (!accountingLines.containsKey(financialObjectCode)) {
                        accountingLines.put(financialObjectCode, nonPersonnelCost.get(costElement));
                    } else {
                        accountingLines.put(financialObjectCode, 
                                            accountingLines.get(financialObjectCode).add(nonPersonnelCost.get(costElement)));
                    }
                }
            } else {
                // remove this when code is fixed
                GlobalVariables.getMessageMap().putError(KNSConstants.GLOBAL_ERRORS, KeyConstants.FINANCIAL_OBJECT_CODE_MAPPING_NOT_FOUND, "Object Code: " + costElement); 
                LOG.error("No financial system object code mapped to object code" + costElement + "" );
                complete &= false;
            }
        } 
        return complete;
    }
    
    protected AwardBudgetExt getPrevBudget() {
        int currentVersionNumber = awardBudgetDocument.getBudget().getBudgetVersionNumber();
        AwardBudgetExt prevBudget = getPreviousBudget(getAwardBudgetDocument().getParentDocument().getBudgetParent().getAwardDocument());
        if (ObjectUtils.isNotNull(prevBudget.getBudgetVersionNumber()) && prevBudget.getBudgetVersionNumber() < currentVersionNumber) {
            budgetCalculationService.calculateBudgetSummaryTotals(prevBudget);
            return prevBudget;
        }
        return null;  
    }
    
    protected String getParameterValue(String awardBudgetParameter) {
        return  parameterService.getParameterValue(AwardBudgetDocument.class, awardBudgetParameter);
    }
    
    protected String getPostedBudgetStatus() {
        return getParameterValue(KeyConstants.AWARD_BUDGET_STATUS_POSTED);
    }
    
    protected AwardBudgetExt getPreviousBudget(AwardDocument awardDocument) {
        return getNewestBudgetByStatus(awardDocument, Arrays.asList(new String[]{getPostedBudgetStatus()}));
    }         
    
    protected AwardBudgetExt getNewestBudgetByStatus(AwardDocument awardDocument, List<String> statuses) { 
        AwardBudgetVersionOverviewExt budgetVersion = null;
        for (BudgetDocumentVersion version : awardDocument.getBudgetDocumentVersions()) {
            AwardBudgetVersionOverviewExt curVersion = (AwardBudgetVersionOverviewExt) version.getBudgetVersionOverview();
            if (statuses.contains(curVersion.getAwardBudgetStatusCode())) {
                if (budgetVersion == null || curVersion.getBudgetVersionNumber() > budgetVersion.getBudgetVersionNumber()) {
                    budgetVersion = curVersion;
                }
            }
        }
        AwardBudgetExt result = null;
        if (budgetVersion != null) {
            result = getBusinessObjectService().findBySinglePrimaryKey(AwardBudgetExt.class, budgetVersion.getBudgetId());
        }
        if (result == null) {
            result = new AwardBudgetExt();
        }
        return result;        
    }

    protected String getIncomeObjectCode() {
        Budget awardBudget = getAwardBudgetDocument().getAwardBudget();
        Award award = awardBudgetDocument.getParentDocument().getBudgetParent();
        //return getParameterValue(INCOME_OBJECT_CODE_PARAMETER, award.getSponsorCode());
        return "4479";
    }
    
    protected BudgetDecimal getBudgetChangeCostLimit() {
        Budget awardBudget = getAwardBudgetDocument().getAwardBudget();
        return awardBudget.getTotalCostLimit();
        
    }

    protected boolean createBudgetAdjustmentDocumentHeader() {
        budgetAdjustmentParametersDTO = new BudgetAdjustmentParametersDTO();
        // TODO Auto-generated method stub
        //use award doc number
        budgetAdjustmentParametersDTO.setOrgDocNumber("");   
        // budget version number
        Award award = awardBudgetDocument.getParentDocument().getBudgetParent();
        budgetAdjustmentParametersDTO.setSponsorType(award.getSponsor().getSponsorTypeCode());
        budgetAdjustmentParametersDTO.setDescription("Test");
        //Just logging message - creating a new budget adjustment document from KC
        String COMMENT = "Automatically generated from posted Award document " 
                         + awardBudgetDocument.getDocumentNumber();

        // posting year  
        budgetAdjustmentParametersDTO.setPostingFiscalYear(getPostingYear());
        // KFS is not able to authenticate this person now. So until the databases are merged, 
        // use khuntley as principal id
        //budgetAdjustmentParametersDTO.setPrincipalId(UserSession.getAuthenticatedUser().getPrincipalId());
        // khuntley principal id 
        budgetAdjustmentParametersDTO.setPrincipalId("6162502038");
        // posting period code
        return true;
    }

    protected String getPostingYear() {
        // TODO Auto-generated method stub
        Integer year = calendar.get(Calendar.YEAR);
        return year.toString();
    }

    protected String getPostingPeriodCode() {
        // TODO Auto-generated method stub
        // posting period code
        // indexing starts at July
        Integer month = calendar.get(Calendar.MONTH) - JULY_INDEXING;

        if (month.toString().length() < 2) {
            return "0" + month;
        } else {
            return month.toString();
        }
    }

    protected void createAccountingLines() {
        // TODO Auto-generated method stub
        LOG.info("PostingYear: " + budgetAdjustmentParametersDTO.getPostingFiscalYear()
                + "PrincipalId: " + budgetAdjustmentParametersDTO.getPrincipalId());
        for (String objectCode : accountingLines.keySet()) {
            if (accountingLines.get(objectCode).isNonZero()) {
                Details details = new Details();
                details.setCurrentAmount(accountingLines.get(objectCode).toString());
                details.setObjectCode(objectCode);
                details.setChart(getAwardChart(getAwardBudgetDocument()));
                details.setAccount(getAwardAccount(getAwardBudgetDocument()));
                details.setProjectCode("");
                details.setSubAccount("");
                budgetAdjustmentParametersDTO.getDetails().add(details);
                LOG.info("ObjectCode: " + objectCode + "Amount: " + accountingLines.get(objectCode));
            }
        }
        
    }

    /**
     * This method gets the parameter list which is in the form key=financialSystemCode, checks if the kcCode
     * is found and returns the corresponding financial code.
     * @param parameterName
     * @param kcCode
     * @return
     * @throws ParseException 
     */
    protected String getParameterValue(String parameterName, String key) {
        List<String> parameterValues = parameterService.getParameterValues(
                                                            Constants.PARAMETER_MODULE_AWARD, 
                                                            Constants.PARAMETER_COMPONENT_DOCUMENT, 
                                                            parameterName);
        String REGEX = "(.+)\\s*=\\s*(.+)";
        String parameterValue = "";
        Pattern pattern = Pattern.compile(REGEX);
        for (String value : parameterValues) {
            Matcher matcher = pattern.matcher(value);
            if (matcher.find()) {
                if (matcher.group(1).equalsIgnoreCase(key)) {
                    parameterValue = matcher.group(2);
                }
            } else {
                // Throw error on console too
                LOG.error("Did not find = in the parameter values. Invalid parameter values.");
            }
        }   
        return parameterValue;
    }
  
    
    protected String getAwardChart(AwardBudgetDocument awardBudgetDocument) {
        Award award = awardBudgetDocument.getParentDocument().getBudgetParent();
        return award.getFinancialChartOfAccountsCode();
    }
    
    protected String getProjectCode() {
        return "";    
    }
    

    /*
     * This is obtained using the mapping table.
     */
    protected String getFinancialObjectCode(String rateClassCode, String rateTypeCode) {
       // criteria.put("activityTypeCode", activityTypeCode);
        // Do not use activity type in criteria, it is not required.
        Award award = awardBudgetDocument.getParentDocument().getBudgetParent();
        String activityTypeCode = award.getActivityTypeCode();
        String awardUnitNumber = award.getUnitNumber();
        List<FinancialObjectCodeMapping> results = getFinancialObjectCodesFromMappingTable(rateClassCode, rateTypeCode, awardUnitNumber);
        
        if (results.isEmpty()) {
            //if not, go up the unit hierarchy and check to see if something is listed there
            List<String> parentUnits = institutionalUnitService.getParentUnits(awardUnitNumber);
            for (String currentUnitNumber : parentUnits) {                
                List<FinancialObjectCodeMapping> parentUnitResults = getFinancialObjectCodesFromMappingTable(
                                                                        rateClassCode, rateTypeCode, currentUnitNumber
                                                                        );
                if (!parentUnitResults.isEmpty()) {
                    return parentUnitResults.get(0).getFinancialObjectCode();
                }
            }
           
            GlobalVariables.getMessageMap().putError(KNSConstants.GLOBAL_ERRORS,  KeyConstants.FINANCIAL_OBJECT_CODE_MAPPING_NOT_FOUND, 
                                                    " Rate Class Code: " + rateClassCode + " Rate Type Code: " + rateTypeCode +
                                                    " Unit: " + award.getUnitNumber() + " Activity Type: " + award.getActivityTypeCode()); 

            LOG.error("Mapping not found for rateClasssCode: " +rateClassCode + "rateTypeCode: " + rateTypeCode + "unitnumber: " );
            return null;
        } else {
            // if results were returned, check if award activityType is in it
            for (FinancialObjectCodeMapping result : results) {
                if (result.getActivityTypeCode().equalsIgnoreCase(activityTypeCode)) {
                    return result.getFinancialObjectCode();
                }
            }
            // if the correct activity type was not in it, just send any one 
            // (there should really just be one result for the combination)
            return results.get(0).getFinancialObjectCode();
        }
    }
    
    /*
     * This is obtained from the cost element table itself
     */
    protected String getFinancialObjectCode(String costElementName) {
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("costElement", costElementName);
        CostElement costElement = (CostElement) getBusinessObjectService().findByPrimaryKey(CostElement.class, criteria);
        if (ObjectUtils.isNull(costElement.getFinancialObjectCode())) {
            GlobalVariables.getMessageMap().putError(KNSConstants.GLOBAL_ERRORS, KeyConstants.FINANCIAL_OBJECT_CODE_MAPPING_NOT_FOUND, "Object Code: " + costElementName);
            LOG.error("No financial system object code mapped to object code " + costElementName + " ." );
        }
        return costElement.getFinancialObjectCode();
    }
    
    protected List<FinancialObjectCodeMapping> getFinancialObjectCodesFromMappingTable(String rateClassCode, String rateTypeCode, String unitNumber) {
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("rateClassCode", rateClassCode);
        criteria.put("rateTypeCode", rateTypeCode);
        criteria.put("unitNumber", unitNumber);
        // Do not use activity type in criteria, it is not required.
        List<FinancialObjectCodeMapping> results = new ArrayList<FinancialObjectCodeMapping>(
                businessObjectService.findMatching(FinancialObjectCodeMapping.class, criteria));
        return results;
    }
    
    protected String getAwardAccount(AwardBudgetDocument awardBudgetDocument) {
        Award award = awardBudgetDocument.getParentDocument().getBudgetParent();
        return award.getAccountNumber();
    }
    
    protected BudgetAdjustmentServiceHelper getHelper() {
        if (helper != null) {
            return helper;
        }
        return new BudgetAdjustmentServiceHelper(getAwardBudgetDocument().getAwardBudget(), getPrevBudget());
    }
    
    public void setAwardBudgetDocument(AwardBudgetDocument awardBudgetDocument) {
        this.awardBudgetDocument = awardBudgetDocument;
    }
    
    protected AwardBudgetDocument getAwardBudgetDocument() {
        return awardBudgetDocument;
    }
    
    public BudgetAdjustmentParametersDTO getBudgetAdjustmentParameters() {
        return budgetAdjustmentParametersDTO;
    }
    
    public void setInstitutionalUnitService(InstitutionalUnitService institutionalUnitService) {
        this.institutionalUnitService = institutionalUnitService;
    }
    
    protected BusinessObjectService getBusinessObjectService() {
        return KraServiceLocator.getService(BusinessObjectService.class);   
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
    /*
     * 
     */
    public void setBudgetCalculationService(BudgetCalculationService budgetCalculationService) {
        this.budgetCalculationService = budgetCalculationService;
    }
    
    /*
     * Sets the parameterService attribute value. Injected by Spring.
     * 
     * @param parameterService The parameterService to set.
     */
    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }
    
    /**
     * Sets the documentService attribute value. Injected by Spring.
     * 
     * @param documentService The documentService to set.
     */
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }
}


