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

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kuali.core.service.BusinessObjectService;
import org.kuali.core.service.DocumentService;
import org.kuali.core.util.GlobalVariables;
import org.kuali.kra.bo.InstituteLaRate;
import org.kuali.kra.bo.InstituteRate;
import org.kuali.kra.bo.Unit;
import org.kuali.kra.budget.bo.BudgetPeriod;
import org.kuali.kra.budget.bo.BudgetProposalLaRate;
import org.kuali.kra.budget.bo.BudgetProposalRate;
import org.kuali.kra.budget.bo.RateClass;
import org.kuali.kra.budget.bo.RateClassType;
import org.kuali.kra.budget.document.BudgetDocument;
import org.kuali.kra.budget.service.BudgetRatesService;
import org.kuali.kra.budget.web.struts.form.BudgetForm;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;

public class BudgetRatesServiceImpl implements BudgetRatesService{
    private BusinessObjectService businessObjectService;
    private Date projectStartDate;
    private Date projectEndDate;
    private static final String PERIOD_SEARCH_SEPARATOR = "|";
    private static final String PERIOD_DISPLAY_SEPARATOR = ",";
    private static final org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(BudgetRatesServiceImpl.class);


    /* get all institute rates - based on activity type
     * and unit number 
     * */
    private Collection<InstituteRate> getInstituteRates(BudgetDocument budgetDocument) {
        String unitNumber = budgetDocument.getProposal().getOwnedByUnitNumber();
        String activityTypeCode = budgetDocument.getProposal().getActivityTypeCode();
        Unit currentUnit = budgetDocument.getProposal().getOwnedByUnit();
        //Unit parentUnit = ownedByUnit.getParentUnit();
        Collection<InstituteRate> allInstituteRates = new ArrayList();
        do {
            Map rateFilterMap = new HashMap();
            rateFilterMap.put("unitNumber", unitNumber);
            rateFilterMap.put("activityTypeCode", activityTypeCode);
            allInstituteRates = businessObjectService.findMatching(InstituteRate.class, rateFilterMap);
            currentUnit = currentUnit.getParentUnit();
            if(currentUnit != null) {
                unitNumber = currentUnit.getUnitNumber();
            }
        }while(allInstituteRates.size() == 0);

        return allInstituteRates;
    }

    /* get all institute rates - based on 
     * and unit number 
     * */
    private Collection<InstituteLaRate> getInstituteLaRates(BudgetDocument budgetDocument) {
        String unitNumber = budgetDocument.getProposal().getOwnedByUnitNumber();
        Unit currentUnit = budgetDocument.getProposal().getOwnedByUnit();
        
        Collection<InstituteLaRate> allInstituteLaRates = new ArrayList();
        do {
            Map rateFilterMap = new HashMap();
            rateFilterMap.put("unitNumber", unitNumber);
            allInstituteLaRates = businessObjectService.findMatching(InstituteLaRate.class, rateFilterMap);
            currentUnit = currentUnit.getParentUnit();
            if(currentUnit != null) {
                unitNumber = currentUnit.getUnitNumber();
            }
        }while(allInstituteLaRates.size() == 0);
        return allInstituteLaRates;
    }

    /* get all LA rates within project start and end date rang 
     *  
     * */
    private List<InstituteLaRate> getLaRatesForProjectDates(Collection<InstituteLaRate> allInstituteRates) {
        List<InstituteLaRate> instituteRates = new ArrayList();
        for(InstituteLaRate instituteRate : allInstituteRates) {
            Date rateStartDate = instituteRate.getStartDate();
            if(rateStartDate.compareTo(getProjectStartDate()) >= 0 && rateStartDate.compareTo(getProjectEndDate()) <=0) {
                instituteRates.add(instituteRate);
            }
            
        }
        return instituteRates;
    }

    /* get all rates within project start and end date range 
     *  
     * */
    private List<InstituteRate> getRatesForProjectDates(Collection<InstituteRate> allInstituteRates) {
        List<InstituteRate> instituteRates = new ArrayList();
        for(InstituteRate instituteRate : allInstituteRates) {
            Date rateStartDate = instituteRate.getStartDate();
            if(rateStartDate.compareTo(getProjectStartDate()) >= 0 && rateStartDate.compareTo(getProjectEndDate()) <=0 ) {
                instituteRates.add(instituteRate);
            }
            
        }
        return instituteRates;
    }
    
    /* get applicable rates before project start date  
     * get the latest 
     * */
    private List<InstituteRate> getApplicableRates(Collection<InstituteRate> allInstituteRates) {
        List<InstituteRate> instituteRates = new ArrayList();
        HashMap instRates = new HashMap();
        List<String> sameAsProjectStartDateRatesKeys = new ArrayList<String>();
        for(InstituteRate instituteRate : allInstituteRates) {
            Date rateStartDate = instituteRate.getStartDate();
            if(rateStartDate.compareTo(getProjectStartDate()) <= 0) {
                String instRateClassCode = instituteRate.getRateClassCode();
                String instRateTypeCode = instituteRate.getRateTypeCode();
                String onOffFlag = instituteRate.getOnOffCampusFlag() ? Constants.ON_CAMUS_FLAG :Constants.OFF_CAMUS_FLAG;
                String hKey = instRateClassCode + instRateTypeCode + onOffFlag;
                if (rateStartDate.compareTo(getProjectStartDate()) == 0) {
                    sameAsProjectStartDateRatesKeys.add(hKey);
                }
                InstituteRate instRate = (InstituteRate)instRates.get(hKey);
                if((instRate != null) && (instRate.getStartDate().compareTo(rateStartDate) <=0 )) {
                    Date currentStartDate = instRate.getStartDate();
                    if(currentStartDate.compareTo(rateStartDate) <= 0) {
                        instRates.remove(hKey);
                    }
                }
                if (!instRates.keySet().contains(hKey) && !sameAsProjectStartDateRatesKeys.contains(hKey)) {
                    instRates.put(hKey, instituteRate);
                }
            }
            
        }
        instituteRates.addAll(instRates.values());
        return instituteRates;
    }

    /* get applicable rates before project start date  
     * get the latest 
     * */
    private List<InstituteLaRate> getApplicableLaRates(Collection<InstituteLaRate> allInstituteRates) {
        List<InstituteLaRate> instituteRates = new ArrayList();
        HashMap instRates = new HashMap();
        List<String> sameAsProjectStartDateRatesKeys = new ArrayList<String>();
        for(InstituteLaRate instituteRate : allInstituteRates) {
            Date rateStartDate = instituteRate.getStartDate();
            if(rateStartDate.compareTo(getProjectStartDate()) <= 0) {
                String instRateClassCode = instituteRate.getRateClassCode();
                String instRateTypeCode = instituteRate.getRateTypeCode();
                String onOffFlag = instituteRate.getOnOffCampusFlag() ? Constants.ON_CAMUS_FLAG :Constants.OFF_CAMUS_FLAG;
                String hKey = instRateClassCode + instRateTypeCode + onOffFlag;
                if (rateStartDate.compareTo(getProjectStartDate()) == 0) {
                    sameAsProjectStartDateRatesKeys.add(hKey);
                }
                InstituteLaRate instRate = (InstituteLaRate)instRates.get(hKey);
                if((instRate != null) && (instRate.getStartDate().compareTo(rateStartDate) <= 0)) {
                    Date currentStartDate = instRate.getStartDate();
                    if(currentStartDate.compareTo(rateStartDate) <= 0) {
                        instRates.remove(hKey);
                    }
                }
                if (!instRates.keySet().contains(hKey) && !sameAsProjectStartDateRatesKeys.contains(hKey)) {
                    instRates.put(hKey, instituteRate);
                }
            }
            
        }
        instituteRates.addAll(instRates.values());
        return instituteRates;
    }
    
    /* filter institute rates - get rates applicable for 
     * the project 
     * */
    private List<InstituteRate> getFilteredInstituteRates(BudgetDocument budgetDocument, Collection<InstituteRate> allInstituteRates) {

        List<InstituteRate> instituteRates = new ArrayList();
        
        /* get rates for the project start and end date range */
        instituteRates.addAll(getRatesForProjectDates(allInstituteRates));

        /* get applicable rates before project start date */
        instituteRates.addAll(getApplicableRates(allInstituteRates));
        
        return instituteRates;
    }

    /* filter institute LA rates - get rates applicable for 
     * the project 
     * */
    private List<InstituteLaRate> getFilteredInstituteLaRates(BudgetDocument budgetDocument, Collection<InstituteLaRate> allInstituteLaRates) {

        List<InstituteLaRate> instituteLaRates = new ArrayList();
        
        /* get rates for the project start and end date range */
        instituteLaRates.addAll(getLaRatesForProjectDates(allInstituteLaRates));

        /* get applicable rates before project start date */
        instituteLaRates.addAll(getApplicableLaRates(allInstituteLaRates));
        
        return instituteLaRates;
    }
    
    /* reset all budget rates 
     *  
     * */
    public void resetAllBudgetRates(BudgetDocument budgetDocument) {
        List<BudgetProposalRate> budgetProposalRates = budgetDocument.getBudgetProposalRates();
        List<BudgetProposalLaRate> budgetProposalLaRates = budgetDocument.getBudgetProposalLaRates();
        for(BudgetProposalRate budgetProposalRate: budgetProposalRates) {
            budgetProposalRate.setApplicableRate(budgetProposalRate.getInstituteRate()); 
        }
        /* reset la rates */
        for(BudgetProposalLaRate budgetProposalLaRate: budgetProposalLaRates) {
            budgetProposalLaRate.setApplicableRate(budgetProposalLaRate.getInstituteRate()); 
        }
    }
    
    /* reset budget rates for a panel
     * each panel is based on rate class type 
     * */
    public void resetBudgetRatesForRateClassType(String rateClassType, BudgetDocument budgetDocument) {
        List<BudgetProposalRate> budgetProposalRates = budgetDocument.getBudgetProposalRates();
        List<RateClass> rateClasses = budgetDocument.getRateClasses();
        List<BudgetProposalLaRate> budgetProposalLaRates = budgetDocument.getBudgetProposalLaRates();
        for(RateClass rateClass: rateClasses) {
            if(rateClass.getRateClassType().equalsIgnoreCase(rateClassType)) {
                for(BudgetProposalRate budgetProposalRate: budgetProposalRates) {
                    if(budgetProposalRate.getRateClassCode().equalsIgnoreCase(rateClass.getRateClassCode())) {
                        budgetProposalRate.setApplicableRate(budgetProposalRate.getInstituteRate()); 
                    }
                }
                /* reset la rates */
                for(BudgetProposalLaRate budgetProposalLaRate: budgetProposalLaRates) {
                    if(budgetProposalLaRate.getRateClassCode().equalsIgnoreCase(rateClass.getRateClassCode())) {
                        budgetProposalLaRate.setApplicableRate(budgetProposalLaRate.getInstituteRate()); 
                    }
                }
            }
        }
    }

    /* load institute rates to hashmap
     *  
     * */
    private HashMap getInstituteRateMap(Collection<InstituteRate> allInstituteRates) {
        HashMap instituteRates = new HashMap();
        for(InstituteRate instituteRate: allInstituteRates) {
            String instRateClassCode = instituteRate.getRateClassCode();
            String instRateTypeCode = instituteRate.getRateTypeCode();
            String onOffFlag = instituteRate.getOnOffCampusFlag() ? Constants.ON_CAMUS_FLAG :Constants.OFF_CAMUS_FLAG;
            String startDate = instituteRate.getStartDate().toString();
            String hKey = instRateClassCode + instRateTypeCode + startDate + onOffFlag;
            instituteRates.put(hKey, instituteRate);
        }
        return instituteRates;
    }
    
    /* load institute LA rates to hashmap
     *  
     * */
    private HashMap getInstituteLaRateMap(Collection<InstituteLaRate> allInstituteLaRates) {
        HashMap instituteLaRates = new HashMap();
        for(InstituteLaRate instituteLaRate: allInstituteLaRates) {
            String instRateClassCode = instituteLaRate.getRateClassCode();
            String instRateTypeCode = instituteLaRate.getRateTypeCode();
            String onOffFlag = instituteLaRate.getOnOffCampusFlag() ? Constants.ON_CAMUS_FLAG :Constants.OFF_CAMUS_FLAG;
            String startDate = instituteLaRate.getStartDate().toString();
            String hKey = instRateClassCode + instRateTypeCode + startDate + onOffFlag;
            instituteLaRates.put(hKey, instituteLaRate);
        }
        return instituteLaRates;
    }

    /* sync all budget rates
     *  
     * */
    public void syncAllBudgetRates(BudgetDocument budgetDocument) {
        List<BudgetProposalRate> budgetProposalRates = budgetDocument.getBudgetProposalRates();
        List<BudgetProposalLaRate> budgetProposalLaRates = budgetDocument.getBudgetProposalLaRates();
        Collection<InstituteRate> allInstituteRates = getInstituteRates(budgetDocument);
        Collection<InstituteLaRate> allInstituteLaRates = getInstituteLaRates(budgetDocument);
        HashMap instRateMap = getInstituteRateMap(allInstituteRates);
        HashMap instLaRateMap = getInstituteLaRateMap(allInstituteLaRates);
        for(BudgetProposalRate budgetProposalRate: budgetProposalRates) {
            String instRateClassCode = budgetProposalRate.getRateClassCode();
            String instRateTypeCode = budgetProposalRate.getRateTypeCode();
            String onOffFlag = budgetProposalRate.getOnOffCampusFlag() ? Constants.ON_CAMUS_FLAG :Constants.OFF_CAMUS_FLAG;
            String startDate = budgetProposalRate.getStartDate().toString();
            String hKey = instRateClassCode + instRateTypeCode + startDate + onOffFlag;
            InstituteRate instituteRate = (InstituteRate)instRateMap.get(hKey);
            budgetProposalRate.setInstituteRate(instituteRate.getInstituteRate()); 
            budgetProposalRate.setApplicableRate(instituteRate.getInstituteRate()); 
        }
        
        /* sync la rates */
        for(BudgetProposalLaRate budgetProposalLaRate: budgetProposalLaRates) {
            String instRateClassCode = budgetProposalLaRate.getRateClassCode();
            String instRateTypeCode = budgetProposalLaRate.getRateTypeCode();
            String onOffFlag = budgetProposalLaRate.getOnOffCampusFlag() ? Constants.ON_CAMUS_FLAG :Constants.OFF_CAMUS_FLAG;
            String startDate = budgetProposalLaRate.getStartDate().toString();
            String hKey = instRateClassCode + instRateTypeCode + startDate + onOffFlag;
            InstituteLaRate instituteLaRate = (InstituteLaRate)instLaRateMap.get(hKey);
            budgetProposalLaRate.setInstituteRate(instituteLaRate.getInstituteRate()); 
            budgetProposalLaRate.setApplicableRate(instituteLaRate.getInstituteRate()); 
        }
    }

    /* update view - location
     *  
     * */
    public void viewLocation(String viewLocation, Integer budgetPeriod, BudgetDocument budgetDocument) {
        List<BudgetProposalRate> budgetProposalRates = budgetDocument.getBudgetProposalRates();
        List<BudgetProposalLaRate> budgetProposalLaRates = budgetDocument.getBudgetProposalLaRates();
        for(BudgetProposalRate budgetProposalRate: budgetProposalRates) {
            boolean displayRate = true;

            /* check view location */
            String onOffCampusFlag = budgetProposalRate.getOnOffCampusFlag() ? Constants.ON_CAMUS_FLAG : Constants.OFF_CAMUS_FLAG;
            if(viewLocation == null || (viewLocation.equalsIgnoreCase(onOffCampusFlag))) {
                displayRate = true; 
            }else { 
                displayRate  = false;
            }
            
            /* check budget Period */
            if(displayRate && budgetPeriod != null) {
                String trackAffectedPeriod = budgetProposalRate.getTrackAffectedPeriod();
                String formattedBudgetPeriod = PERIOD_SEARCH_SEPARATOR + budgetPeriod + PERIOD_SEARCH_SEPARATOR;
                if(trackAffectedPeriod == null || (trackAffectedPeriod.indexOf(formattedBudgetPeriod) < 0)) {
                    displayRate  = false;
                }
            }
            budgetProposalRate.setDisplayLocation(displayRate); 
        }
        
        /* la rates */
        for(BudgetProposalLaRate budgetProposalLaRate: budgetProposalLaRates) {
            boolean displayRate = true;

            /* check view location */
            String onOffCampusFlag = budgetProposalLaRate.getOnOffCampusFlag() ? Constants.ON_CAMUS_FLAG : Constants.OFF_CAMUS_FLAG;
            if(viewLocation == null || (viewLocation.equalsIgnoreCase(onOffCampusFlag))) {
                displayRate = true; 
            }else { 
                displayRate  = false;
            }
            
            /* check budget Period */
            if(displayRate && budgetPeriod != null) {
                String trackAffectedPeriod = budgetProposalLaRate.getTrackAffectedPeriod();
                String formattedBudgetPeriod = PERIOD_SEARCH_SEPARATOR + budgetPeriod + PERIOD_SEARCH_SEPARATOR;
                if(trackAffectedPeriod == null || (trackAffectedPeriod.indexOf(formattedBudgetPeriod) < 0)) {
                    displayRate  = false;
                }
            }
            budgetProposalLaRate.setDisplayLocation(displayRate); 
        }
    
    }
    
    /* sync budget rates for a panel
     * each panel is based on rate class type 
     * */
    public void syncBudgetRatesForRateClassType(String rateClassType, BudgetDocument budgetDocument) {
        List<BudgetProposalRate> budgetProposalRates = budgetDocument.getBudgetProposalRates();
        List<BudgetProposalLaRate> budgetProposalLaRates = budgetDocument.getBudgetProposalLaRates();
        List<RateClass> rateClasses = budgetDocument.getRateClasses();
        Collection<InstituteRate> allInstituteRates = getInstituteRates(budgetDocument);
        Collection<InstituteLaRate> allInstituteLaRates = getInstituteLaRates(budgetDocument);
        HashMap instRateMap = getInstituteRateMap(allInstituteRates);
        HashMap instLaRateMap = getInstituteLaRateMap(allInstituteLaRates);
        for(RateClass rateClass: rateClasses) {
            if(rateClass.getRateClassType().equalsIgnoreCase(rateClassType)) {
                for(BudgetProposalRate budgetProposalRate: budgetProposalRates) {
                    if(budgetProposalRate.getRateClassCode().equalsIgnoreCase(rateClass.getRateClassCode())) {
                        String instRateClassCode = budgetProposalRate.getRateClassCode();
                        String instRateTypeCode = budgetProposalRate.getRateTypeCode();
                        String onOffFlag = budgetProposalRate.getOnOffCampusFlag() ? Constants.ON_CAMUS_FLAG :Constants.OFF_CAMUS_FLAG;
                        String startDate = budgetProposalRate.getStartDate().toString();
                        String hKey = instRateClassCode + instRateTypeCode + startDate + onOffFlag;
                        InstituteRate instituteRate = (InstituteRate)instRateMap.get(hKey);
                        budgetProposalRate.setInstituteRate(instituteRate.getInstituteRate()); 
                        budgetProposalRate.setApplicableRate(instituteRate.getInstituteRate()); 
                    }
                }
                /* la rates */
                for(BudgetProposalLaRate budgetProposalLaRate: budgetProposalLaRates) {
                    if(budgetProposalLaRate.getRateClassCode().equalsIgnoreCase(rateClass.getRateClassCode())) {
                        String instRateClassCode = budgetProposalLaRate.getRateClassCode();
                        String instRateTypeCode = budgetProposalLaRate.getRateTypeCode();
                        String onOffFlag = budgetProposalLaRate.getOnOffCampusFlag() ? Constants.ON_CAMUS_FLAG :Constants.OFF_CAMUS_FLAG;
                        String startDate = budgetProposalLaRate.getStartDate().toString();
                        String hKey = instRateClassCode + instRateTypeCode + startDate + onOffFlag;
                        InstituteLaRate instituteLaRate = (InstituteLaRate)instLaRateMap.get(hKey);
                        budgetProposalLaRate.setInstituteRate(instituteLaRate.getInstituteRate()); 
                        budgetProposalLaRate.setApplicableRate(instituteLaRate.getInstituteRate()); 
                    }
                }
            }
        }
    }

    /* get all budget periods */
    public List<BudgetPeriod> getBudgetPeriods(){
        DocumentService documentService = KraServiceLocator.getService(DocumentService.class);
        BudgetForm budgetForm = (BudgetForm) GlobalVariables.getKualiForm();
        BudgetDocument budgetDocument  = budgetForm.getBudgetDocument();
        List<BudgetPeriod> budgetPeriods = budgetDocument.getBudgetPeriods();
        return budgetPeriods;
    }
    
    
    private String getFormattedAffectedBudgetPeriod(String periodAffected) {
        String budgetPeriodAffected = periodAffected;
        if(budgetPeriodAffected != null) {
            budgetPeriodAffected = budgetPeriodAffected.trim();
            budgetPeriodAffected = budgetPeriodAffected.replace(PERIOD_SEARCH_SEPARATOR, PERIOD_DISPLAY_SEPARATOR);
            budgetPeriodAffected = budgetPeriodAffected.substring(1, budgetPeriodAffected.length()- 1);
        }
        return budgetPeriodAffected;
    }

    /* get budget LA rates applicable for the proposal - based on 
     * unit number 
     * */
    public void getBudgetLaRates(List<RateClassType> rateClassTypes, BudgetDocument budgetDocument) {
        List<InstituteLaRate> instituteLaRates = budgetDocument.getInstituteLaRates();
        setProjectStartDate(budgetDocument.getStartDate());
        setProjectEndDate(budgetDocument.getEndDate());
        String unitNumber = budgetDocument.getProposal().getOwnedByUnitNumber();
        String activityTypeCode = budgetDocument.getProposal().getActivityTypeCode();
        List<RateClass> rateClasses = budgetDocument.getRateClasses();
        List<BudgetProposalLaRate> budgetProposalLaRates = budgetDocument.getBudgetProposalLaRates();
        boolean isBudgetProposalRatesEmpty = true;
        Map rateClassMap = new HashMap();
        Map rateClassTypeMap = new HashMap();
        
        Collection<InstituteLaRate> allInstituteLaRates = getInstituteLaRates(budgetDocument);
        /* check budget proposal LA rates exists. If not get institute rates to initialize
         * proposal rates.
         */
        if(budgetProposalLaRates.size() > 0) {
            isBudgetProposalRatesEmpty = false;
        }
        instituteLaRates.clear();
        instituteLaRates = getFilteredInstituteLaRates(budgetDocument, allInstituteLaRates);
        for(InstituteLaRate instituteRate: instituteLaRates) {
            String rateClassCode = instituteRate.getRateClassCode();
            String rateClassType = instituteRate.getRateClass().getRateClassType();
            /* Add applicable rate class */
            if(rateClassMap.get(rateClassCode) == null) {
                RateClass rateClass = instituteRate.getRateClass();
                rateClassMap.put(rateClassCode, instituteRate.getRateClass());
            }
            /* Add applicable rate class types */
            if(rateClassTypeMap.get(rateClassType) == null) {
                RateClass rateClass = instituteRate.getRateClass();
                rateClassTypeMap.put(rateClassType, instituteRate.getRateClass().getRateClassTypeT());
            }
            if(isBudgetProposalRatesEmpty) {
                /* initialize budget proposal rates */
                BudgetProposalLaRate budgetProposalRate = new BudgetProposalLaRate();
                budgetProposalRate.setApplicableRate(instituteRate.getInstituteRate());
                budgetProposalRate.setFiscalYear(instituteRate.getFiscalYear());
                budgetProposalRate.setInstituteRate(instituteRate.getInstituteRate());
                budgetProposalRate.setOnOffCampusFlag(instituteRate.getOnOffCampusFlag());
                budgetProposalRate.setRateClass(instituteRate.getRateClass());
                budgetProposalRate.setRateClassCode(rateClassCode);
                budgetProposalRate.setRateType(instituteRate.getRateType());
                budgetProposalRate.setRateTypeCode(instituteRate.getRateTypeCode());
                budgetProposalRate.setStartDate(instituteRate.getStartDate());
                budgetProposalRate.setUnitNumber(unitNumber);
                budgetProposalRate.setOldApplicableRate(instituteRate.getInstituteRate());
                //if (!ObjectUtils.collectionContainsObjectWithIdentitcalKey(budgetProposalLaRates,budgetProposalRate)) {
                    budgetProposalLaRates.add(budgetProposalRate);
               // }
            }
        }
        rateClasses.addAll(rateClassMap.values());
        rateClassTypes.addAll(rateClassTypeMap.values());
        updateRatesForEachPeriod(budgetDocument);
        Collections.sort(budgetDocument.getBudgetProposalLaRates());
        
    }
    

    /* get budget rates applicable for the proposal - based on activity type
     * and unit number 
     * */
    public void getBudgetRates(List<RateClassType> rateClassTypes, BudgetDocument budgetDocument) {
        setProjectStartDate(budgetDocument.getStartDate());
        setProjectEndDate(budgetDocument.getEndDate());
        String unitNumber = budgetDocument.getProposal().getOwnedByUnitNumber();
        String activityTypeCode = budgetDocument.getProposal().getActivityTypeCode();
        List<RateClass> rateClasses = budgetDocument.getRateClasses();
        List<InstituteRate> instituteRates = budgetDocument.getInstituteRates();
        List<BudgetProposalRate> budgetProposalRates = budgetDocument.getBudgetProposalRates();
        boolean isBudgetProposalRatesEmpty = true;
        Map rateClassMap = new HashMap();
        Map rateClassTypeMap = new HashMap();
        
        Collection<InstituteRate> allInstituteRates = getInstituteRates(budgetDocument);
        
        /* check budget proposal rates exists. If not get institute rates to initialize
         * proposal rates.
         */
        if(budgetProposalRates.size() > 0) {
            isBudgetProposalRatesEmpty = false;
        }
        instituteRates.clear();
        instituteRates = getFilteredInstituteRates(budgetDocument, allInstituteRates);
        for(InstituteRate instituteRate: instituteRates) {
            String rateClassCode = instituteRate.getRateClassCode();
            if(instituteRate.getRateClass()==null){
                continue;
            }
            String rateClassType = instituteRate.getRateClass().getRateClassType();
            /* Add applicable rate class */
            if(rateClassMap.get(rateClassCode) == null) {
                RateClass rateClass = instituteRate.getRateClass();
                rateClassMap.put(rateClassCode, instituteRate.getRateClass());
            }
            /* Add applicable rate class types */
            if(rateClassTypeMap.get(rateClassType) == null) {
                RateClass rateClass = instituteRate.getRateClass();
                rateClassTypeMap.put(rateClassType, instituteRate.getRateClass().getRateClassTypeT());
            }
            if(isBudgetProposalRatesEmpty) {
                /* initialize budget proposal rates */
                BudgetProposalRate budgetProposalRate = new BudgetProposalRate();
                budgetProposalRate.setActivityTypeCode(activityTypeCode);
                budgetProposalRate.setApplicableRate(instituteRate.getInstituteRate());
                budgetProposalRate.setFiscalYear(instituteRate.getFiscalYear());
                budgetProposalRate.setInstituteRate(instituteRate.getInstituteRate());
                budgetProposalRate.setOnOffCampusFlag(instituteRate.getOnOffCampusFlag());
                budgetProposalRate.setRateClass(instituteRate.getRateClass());
                budgetProposalRate.setRateClassCode(rateClassCode);
                budgetProposalRate.setRateType(instituteRate.getRateType());
                budgetProposalRate.setRateTypeCode(instituteRate.getRateTypeCode());
                budgetProposalRate.setStartDate(instituteRate.getStartDate());
                budgetProposalRate.setUnitNumber(unitNumber);
                budgetProposalRate.setOldApplicableRate(instituteRate.getInstituteRate());
                //if (!ObjectUtils.collectionContainsObjectWithIdentitcalKey(budgetProposalRates,budgetProposalRate)) {
                    budgetProposalRates.add(budgetProposalRate);
                //}
            }
        }
        rateClasses.addAll(rateClassMap.values());
        rateClassTypes.addAll(rateClassTypeMap.values());
        updateRatesForEachPeriod(budgetDocument);
        Collections.sort(budgetDocument.getBudgetProposalRates());
        getBudgetLaRates(rateClassTypes, budgetDocument);
        checkActivityPrefixForRateClassTypes(rateClassTypes, budgetDocument);
    }

    /* verify and add activity type prefix if required for rate class type description
     * 
     * */
    public void checkActivityPrefixForRateClassTypes(List<RateClassType> rateClassTypes, BudgetDocument budgetDocument) {
        String activityTypeDescription = budgetDocument.getProposal().getActivityType().getDescription().concat(" ");
        List<BudgetProposalRate> budgetProposalRates = budgetDocument.getBudgetProposalRates();
        List<BudgetProposalLaRate> budgetProposalLaRates = budgetDocument.getBudgetProposalLaRates();
        Map rateClassTypeMap = new HashMap();
        for(RateClassType rateClassType : rateClassTypes) {
            if(rateClassType.getPrefixActivityType()) {
                String newRateClassTypeDescription = activityTypeDescription.concat(rateClassType.getDescription()); 
                rateClassType.setDescription(newRateClassTypeDescription);
                rateClassType.setPrefixActivityType(false);
                /* set in proposal rates reference */
                for(BudgetProposalRate budgetProposalRate: budgetProposalRates) {
                    RateClassType BPRateClassType = budgetProposalRate.getRateClass().getRateClassTypeT();
                    if(rateClassType.getRateClassType().equalsIgnoreCase(BPRateClassType.getRateClassType())) {
                        BPRateClassType.setDescription(newRateClassTypeDescription);
                    }
                }
                /* set in proposal LA rates reference */
                for(BudgetProposalLaRate budgetProposalLaRate: budgetProposalLaRates) {
                    RateClassType BPLRateClassType = budgetProposalLaRate.getRateClass().getRateClassTypeT();
                    if(rateClassType.getRateClassType().equalsIgnoreCase(BPLRateClassType.getRateClassType())) {
                        BPLRateClassType.setDescription(newRateClassTypeDescription);
                    }
                }
            }
        }
    }
    
    /**
     * Build rates for each period.
     * @return .
     */
    private void updateRatesForEachPeriod(BudgetDocument budgetDocument) {
        List<BudgetProposalRate> budgetProposalRates = budgetDocument.getBudgetProposalRates();
        List<BudgetProposalLaRate> budgetProposalLaRates = budgetDocument.getBudgetProposalLaRates();
        List<BudgetPeriod> budgetPeriods = budgetDocument.getBudgetPeriods();

        for(BudgetPeriod budgetPeriod: budgetPeriods) {
            for(BudgetProposalRate budgetProposalRate: budgetProposalRates) {
                if(budgetProposalRate.getStartDate().compareTo(budgetPeriod.getEndDate()) <= 0) {
                    String dispBudgetPeriod = budgetPeriod.getBudgetPeriod().toString();
                    String formattedPeriod = dispBudgetPeriod.concat(PERIOD_SEARCH_SEPARATOR);
                    String currBudgetPeriod = budgetProposalRate.getTrackAffectedPeriod(); //(String)ratesForEachPeriod.get(budgetPeriod.getBudgetPeriod());
                    if(currBudgetPeriod == null) {
                        currBudgetPeriod = PERIOD_SEARCH_SEPARATOR.concat(formattedPeriod);
                        budgetProposalRate.setTrackAffectedPeriod(currBudgetPeriod);
                    }else {
                        if(currBudgetPeriod.indexOf(formattedPeriod) < 0) {
                            currBudgetPeriod = currBudgetPeriod.concat(formattedPeriod); 
                            budgetProposalRate.setTrackAffectedPeriod(currBudgetPeriod);
                        }
                    }
                    budgetProposalRate.setAffectedBudgetPeriod(getFormattedAffectedBudgetPeriod(currBudgetPeriod));
                }
            }
            for(BudgetProposalLaRate budgetProposalLaRate: budgetProposalLaRates) {
                if(budgetProposalLaRate.getStartDate().compareTo(budgetPeriod.getEndDate()) <= 0) {
                    String dispBudgetPeriod = budgetPeriod.getBudgetPeriod().toString();
                    String formattedPeriod = dispBudgetPeriod.concat(PERIOD_SEARCH_SEPARATOR);
                    String currBudgetPeriod = budgetProposalLaRate.getTrackAffectedPeriod(); //(String)ratesForEachPeriod.get(budgetPeriod.getBudgetPeriod());
                    if(currBudgetPeriod == null) {
                        currBudgetPeriod = PERIOD_SEARCH_SEPARATOR.concat(formattedPeriod);
                        budgetProposalLaRate.setTrackAffectedPeriod(currBudgetPeriod);
                    }else {
                        if(currBudgetPeriod.indexOf(formattedPeriod) < 0) {
                            currBudgetPeriod = currBudgetPeriod.concat(formattedPeriod); 
                            budgetProposalLaRate.setTrackAffectedPeriod(currBudgetPeriod);
                        }
                    }
                    budgetProposalLaRate.setAffectedBudgetPeriod(getFormattedAffectedBudgetPeriod(currBudgetPeriod));
                }
            }
        }
    }
        
    /**
     * Gets the businessObjectService attribute.
     * @return Returns the businessObjectService.
     */
    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }

    /**
     * Sets the businessObjectService attribute value.
     * @param businessObjectService The businessObjectService to set.
     */
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public Date getProjectStartDate() {
        return projectStartDate;
    }

    public void setProjectStartDate(Date projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public Date getProjectEndDate() {
        return projectEndDate;
    }

    public void setProjectEndDate(Date projectEndDate) {
        this.projectEndDate = projectEndDate;
    }
    
}
