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
package org.kuali.kra.timeandmoney.rules;

import java.sql.Date;
import java.util.Map.Entry;

import org.kuali.kra.award.AwardAmountInfoService;
import org.kuali.kra.award.AwardDateRulesHelper;
import org.kuali.kra.award.home.Award;
import org.kuali.kra.award.home.AwardAmountInfo;
import org.kuali.kra.award.version.service.AwardVersionService;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.rules.ResearchDocumentRuleBase;
import org.kuali.kra.timeandmoney.AwardHierarchyNode;
import org.kuali.kra.timeandmoney.document.TimeAndMoneyDocument;
import org.kuali.kra.timeandmoney.rule.event.TimeAndMoneyAwardDateSaveEvent;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.MessageMap;

/**
 * This class...
 */
public class TimeAndMoneyAwardDateSaveRuleImpl extends ResearchDocumentRuleBase implements TimeAndMoneyAwardDateSaveRule {

    private static final String OBLIGATED_DATE_PROPERTY = "obligatedDate";
    private static final String OBLIGATED_END_DATE_PROPERTY = "obligatedEndDate";
    private static final String OBLIGATED_START_DATE_PROPERTY = "obligatedStartDate";
    private static final String FINAL_EXPIRATION_DATE_PROPERTY = "finalExpirationDate";
    private static final String AWARD_EFFECTIVE_DATE_PROPERTY = "document.awardList[0].awardEffectiveDate";


    
    public boolean processSaveAwardDatesBusinessRules(TimeAndMoneyAwardDateSaveEvent timeAndMoneyAwardDateSaveEvent) {
        TimeAndMoneyDocument timeAndMoneyDocument = (TimeAndMoneyDocument) timeAndMoneyAwardDateSaveEvent.getDocument();
        
        //return validateObligatedDates(timeAndMoneyDocument) && validateDatesNotNull(timeAndMoneyDocument) && validateDatesAgainstProjectStartDate(timeAndMoneyDocument);
        //return validateObligatedDates(timeAndMoneyDocument) && validateDatesAgainstProjectStartDate(timeAndMoneyDocument);
        return validateObligatedDates(timeAndMoneyDocument);
    }
    
    private boolean validateObligatedDates(TimeAndMoneyDocument document) {
        boolean valid = true;
        int i = 0;
        for(Entry<String, AwardHierarchyNode> awardHierarchyNode : document.getAwardHierarchyNodes().entrySet()) {
            Award award = getAwardVersionService().getWorkingAwardVersion(awardHierarchyNode.getValue().getAwardNumber());
            AwardAmountInfo aai = getAwardAmountInfoService().fetchAwardAmountInfoWithHighestTransactionId(award.getAwardAmountInfos());
            Date obligatedStartDate = awardHierarchyNode.getValue().getCurrentFundEffectiveDate();
            Date obligatedEndDate = awardHierarchyNode.getValue().getObligationExpirationDate();
            Date projectEndDate = awardHierarchyNode.getValue().getFinalExpirationDate();
            Date projectStartDate = aai.getAward().getAwardEffectiveDate();
            KualiDecimal obligatedTotal = awardHierarchyNode.getValue().getAmountObligatedToDate();
            
            MessageMap errorMap = GlobalVariables.getMessageMap();
            String fieldStarter =  "awardHierarchyNodeItems[" + i;
            String awardId = award.getAwardNumber();
            valid = AwardDateRulesHelper.validateProjectStartBeforeProjectEnd(errorMap, projectStartDate, projectEndDate, fieldStarter + "].finalExpirationDate", awardId) && valid;
            valid = AwardDateRulesHelper.validateObligationStartBeforeObligationEnd(errorMap, obligatedStartDate, obligatedEndDate, fieldStarter + "].obligationExpirationDate", awardId) && valid;
            valid = AwardDateRulesHelper.validateProjectStartBeforeObligationStart(errorMap, projectStartDate, obligatedStartDate, fieldStarter + "].currentFundEffectiveDate", awardId) && valid;
            valid = AwardDateRulesHelper.validateProjectStartBeforeObligationEnd(errorMap, projectStartDate, obligatedEndDate, fieldStarter + "].obligationExpirationDate", awardId) && valid;
            valid = AwardDateRulesHelper.validateObligationStartBeforeProjectEnd(errorMap, obligatedStartDate, projectEndDate, fieldStarter + "].currentFundEffectiveDate", awardId) && valid;
            valid = AwardDateRulesHelper.validateObligationEndBeforeProjectEnd(errorMap, obligatedEndDate, projectEndDate, fieldStarter + "].obligationExpirationDate", awardId) && valid;
            
            if (obligatedTotal != null && obligatedTotal.isGreaterThan(new KualiDecimal(0))) {
                if (obligatedStartDate == null) {
                    String field = fieldStarter + "].currentFundEffectiveDate"; 
                    reportError(field, KeyConstants.ERROR_AWARD_EFFECTIVE_DATE);
                    valid = false;
                }
                if (obligatedEndDate == null) {
                    String field = fieldStarter + "].obligationExpirationDate"; 
                    reportError(field, KeyConstants.ERROR_OBLIGATION_EXPIRATION_DATE);
                    valid = false;
                }
            }
            i++;
        }
        return valid;
    }
    
    
    private AwardAmountInfoService getAwardAmountInfoService() {
        return KraServiceLocator.getService(AwardAmountInfoService.class);
    }
    
    public AwardVersionService getAwardVersionService() {
        return KraServiceLocator.getService(AwardVersionService.class);
    }
    
    /* Leaving this code in here temporarily for quick look up purposes.  If everything tests fine, then this can be removed shortly 11/27/2012
    private boolean validateDatesAgainstProjectStartDate(TimeAndMoneyDocument document) {
        boolean valid = true;
        for(Entry<String, AwardHierarchyNode> awardHierarchyNode : document.getAwardHierarchyNodes().entrySet()){
            Date obligatedStartDate = awardHierarchyNode.getValue().getCurrentFundEffectiveDate();
            Date obligatedEndDate = awardHierarchyNode.getValue().getObligationExpirationDate();
            Date projectEndDate = awardHierarchyNode.getValue().getFinalExpirationDate();
            Date projectStartDate = awardHierarchyNode.getValue().getProjectStartDate();     
            if(projectStartDate != null && obligatedStartDate != null && obligatedStartDate.before(projectStartDate)) {
                valid = false;
                reportError(OBLIGATED_START_DATE_PROPERTY, KeyConstants.ERROR_OBLIGATED_START_BEFORE_PROJECT_START, 
                        awardHierarchyNode.getValue().getAwardNumber());
            }
            if(projectStartDate != null && obligatedEndDate != null  &&  (obligatedEndDate.before(projectStartDate) || obligatedEndDate.equals(projectStartDate))) {
                valid = false;
                reportError(OBLIGATED_END_DATE_PROPERTY, KeyConstants.ERROR_OBLIGATED_END_BEFORE_PROJECT_START, 
                        awardHierarchyNode.getValue().getAwardNumber());
            }
            if(projectStartDate != null && obligatedEndDate != null &&  (projectEndDate.before(projectStartDate) || obligatedEndDate.equals(projectStartDate))) {
                valid = false;
                reportError(FINAL_EXPIRATION_DATE_PROPERTY, KeyConstants.ERROR_PROJECT_END_BEFORE_PROJECT_START, 
                        awardHierarchyNode.getValue().getAwardNumber());
                }
            }   
          return valid;  
        }
    
    */
    
    
    public boolean enforceAwardStartDatePopulated(Award award) {
        boolean valid = true;
        if(award.getAwardEffectiveDate() == null) {
            reportError(AWARD_EFFECTIVE_DATE_PROPERTY, KeyConstants.ERROR_AWARD_EFFECTIVE_DATE_TIME_AND_MONEY);
            valid = false;
        }
        return valid;
    }
    }
    
       
