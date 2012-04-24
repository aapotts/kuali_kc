/*
 * 
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
package org.kuali.kra.subaward.subawardrule;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.kuali.kra.award.home.Award;
import org.kuali.kra.award.home.AwardService;
import org.kuali.kra.bo.CustomAttribute;
import org.kuali.kra.bo.CustomAttributeDocument;
import org.kuali.kra.bo.NonOrganizationalRolodex;
import org.kuali.kra.bo.Unit;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.rule.event.KraDocumentEventBaseExtension;
import org.kuali.kra.rule.event.SaveCustomAttributeEvent;
import org.kuali.kra.rules.ResearchDocumentRuleBase;
import org.kuali.kra.subaward.bo.SubAward;
import org.kuali.kra.subaward.bo.SubAwardAmountInfo;
import org.kuali.kra.subaward.bo.SubAwardAmountReleased;
import org.kuali.kra.subaward.bo.SubAwardCloseout;
import org.kuali.kra.subaward.bo.SubAwardContact;
import org.kuali.kra.subaward.bo.SubAwardFundingSource;
import org.kuali.kra.subaward.customdata.SubAwardCustomData;
import org.kuali.kra.subaward.document.SubAwardDocument;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.MessageMap;

/**
 * This class is for rule validation while
 * subAwardDocumentRule is used...
 */
public class SubAwardDocumentRule extends
ResearchDocumentRuleBase implements SubAwardRule,
SubAwardAmountInfoRule,
SubAwardAmountReleasedRule,
SubAwardContactRule,
SubAwardCloseoutRule,
SubAwardFundingSourceRule {

    private static final String STATUS_CODE = ".statusCode";
    private static final String SUBAWARD_TYPE_CODE = ".subAwardTypeCode";
    private static final String REQUISITIONER = ".requisitionerUserName";
    private static final String REQUISITIONER_UNIT = ".requisitionerUnit";
    private static final String PURCHASE_ORDER_NUM= ".purchaseOrderNum";
    private static final String SUBCONTRACTOR_ID = ".organizationId";
    private static final String NEW_SUBAWARD = "document.subAwardList[0]";
    private static final String SUBAWARD_START_DATE =".startDate";
    private static final String SITEINVESTIGATOR =".siteInvestigatorId";
    private static final String AMOUNT_INFO_EFFECTIVE_DATE = "newSubAwardAmountInfo.effectiveDate";
    private static final String AMOUNT_INFO_OBLIGATED_AMOUNT = "newSubAwardAmountInfo.obligatedChange";
    private static final String AMOUNT_INFO_ANTICIPATED_AMOUNT = "newSubAwardAmountInfo.anticipatedChange";

    private static final String AMOUNT_RELEASED_EFFECTIVE_DATE = "newSubAwardAmountReleased.effectiveDate";
    private static final String INVOICE_NUMBER = "newSubAwardAmountReleased.invoiceNumber";
    private static final String START_DATE = "newSubAwardAmountReleased.startDate";
    private static final String END_DATE = "newSubAwardAmountReleased.endDate";
    private static final String AMOUNT_RELEASED="newSubAwardAmountReleased.amountReleased";
    
    private static final String ROLODEX_ID="newSubAwardContact.rolodex.fullName";
    private static final String CONTACT_TYPE_CODE="newSubAwardContact.contactTypeCode";
    
    private static final String CLOSEOUT_TYPE_CODE="newSubAwardCloseout.closeoutTypeCode";
    private static final String DATE_REQUESTED = "newSubAwardCloseout.dateRequested";
    
    private static final String AWARD_NUMBER="newSubAwardFundingSource.award.awardNumber";

    /**.
     * This method is for AddSubAwardBusinessRules
     * @param subAward
     * @return rulePassed boolean...
     */
    public boolean processAddSubAwardBusinessRules(SubAward subAward) {

        boolean rulePassed = true;
        rulePassed &= processSaveSubAwardBusinessRules(subAward, NEW_SUBAWARD);
        return rulePassed;
}
    /**.
     * This method is for SaveSubAwardBusinessRules
     * @param subAward
     * @param propertyPrefix
     * @return  boolean...
     */
    protected boolean  processSaveSubAwardBusinessRules(
    SubAward subAward,String propertyPrefix){
     
        boolean rulePassed = true;

        if (subAward.getOrganizationId() == null ){ 
            rulePassed = false;
            reportError(propertyPrefix+SUBCONTRACTOR_ID
                    , KeyConstants.ERROR_REQUIRED_SUBRECIPIENT_ID);
        }   
        
        if(subAward.getStatusCode()==null ){ 
            rulePassed = false;            
            reportError(propertyPrefix+STATUS_CODE
                    , KeyConstants.ERROR_REQUIRED_STATUS);          
        }  
        if(subAward.getSubAwardTypeCode()==null ){ 
            rulePassed = false;            
            reportError(propertyPrefix+SUBAWARD_TYPE_CODE
                    , KeyConstants.ERROR_REQUIRED_SUBAWARD_TYPE);
        }  
        if(subAward.getRequisitionerId()==null ){ 
            rulePassed = false;            
            if (subAward.getRequisitionerUserName() == null) {
                reportError(propertyPrefix + REQUISITIONER
                        , KeyConstants.ERROR_REQUIRED_REQUISITIONER);
            }
            else {
                reportError(propertyPrefix + REQUISITIONER,
                        KeyConstants.ERROR_INVALID_REQUISITIONER, new String[] {subAward.getRequisitionerUserName()});
            }
        }  
        if(subAward.getRequisitionerUnit() != null){
            Unit leadUnit = (Unit) getBusinessObjectService().findByPrimaryKey(Unit.class, Collections.singletonMap("unitNumber", subAward.getRequisitionerUnit()));
            if(leadUnit == null)
                reportError(propertyPrefix+REQUISITIONER_UNIT
                        , KeyConstants.ERROR_REQUIRED_REQUISITIONER_UNIT); 
        }
        if(subAward.getPurchaseOrderNum()==null ){
            rulePassed = false;
            
            reportError(propertyPrefix+PURCHASE_ORDER_NUM
                    , KeyConstants.ERROR_REQUIRED_PURCHASE_ORDER_NUM); 
        }      
        if(subAward.getStartDate()!=null && subAward.getEndDate()!=null){
            if(subAward.getStartDate().after(subAward.getEndDate())){
                rulePassed = false;
                reportError(propertyPrefix+SUBAWARD_START_DATE
                        , KeyConstants.SUBAWARD_ERROR_END_DATE_GREATER_THAN_START); 
            }
        }
        if (subAward.getSiteInvestigator() == null && subAward.getSiteInvestigatorId() != null) {
            rulePassed = false;               
            reportError(propertyPrefix + SITEINVESTIGATOR, 
                    KeyConstants.ERROR_INVALID_SITEINVESTIGATOR_ID, new String[] {subAward.getSiteInvestigatorId()});          
        }
        if (subAward.getOrganizationId() != null) { 
            if (subAward.getOrganization() == null) {
                rulePassed = false;               
                reportError(propertyPrefix + SUBCONTRACTOR_ID, KeyConstants.ERROR_INVALID_SUBRECIPIENT_ID, new String[] {subAward.getOrganizationId()});
            }           
        }
        return rulePassed;
    }
    
    public boolean processSaveSubAwardAmountInfoBusinessRule(SubAward subAward,SubAwardAmountInfo amountInfo) {
        boolean rulePassed = true; 
        
        rulePassed &= processSubAwardAmountInfoBusinessRule(subAward,amountInfo);
        
        return rulePassed;
    } 
   protected boolean  processSubAwardAmountInfoBusinessRule(SubAward subAward,SubAwardAmountInfo amountInfo){  
     
       boolean rulePassed = true;
         
       KualiDecimal obligatedAmount=KualiDecimal.ZERO;
        if( obligatedAmount!=null){
            int count =0;
            for(SubAwardAmountInfo subAwardAmountInfo :subAward.getSubAwardAmountInfoList()){  
                count=count+1;
                if(subAwardAmountInfo.getObligatedChange()!=null){
                    obligatedAmount = obligatedAmount .add(subAwardAmountInfo.getObligatedChange());
                }
                else
                {
                    rulePassed = false;
                    reportError("document.subAwardList[0].subAwardAmountInfoList["+Integer.toString(count-1)+"].obligatedChange"
                        , KeyConstants.ERROR_REQUIRED_OBLIGATED_AMOUNT);  
                }
            }
        }
        if(obligatedAmount !=null){
            if(obligatedAmount .isNegative()){
                rulePassed = false; 
                reportError(AMOUNT_INFO_OBLIGATED_AMOUNT
                        , KeyConstants.ERROR_AMOUNT_INFO_OBLIGATED_AMOUNT_NEGATIVE); 
            }
        }
        KualiDecimal anticipatedAmount=KualiDecimal.ZERO;
        if(anticipatedAmount!=null){
            int count=0;
            for(SubAwardAmountInfo subAwardAmountInfo :subAward.getSubAwardAmountInfoList()){
                count=count+1;
                if(subAwardAmountInfo.getAnticipatedChange()!=null){
                    anticipatedAmount = anticipatedAmount .add(subAwardAmountInfo.getAnticipatedChange());
                }
                else
                {
                    rulePassed = false;
                    reportError("document.subAwardList[0].subAwardAmountInfoList["+Integer.toString(count-1)+"].anticipatedChange"
                            , KeyConstants.ERROR_REQUIRED_ANTICIPATED_AMOUNT);  
                }
            }
        }
        if(anticipatedAmount!=null){
            if(anticipatedAmount .isNegative()){
                rulePassed = false; 
                reportError(AMOUNT_INFO_ANTICIPATED_AMOUNT
                        , KeyConstants.ERROR_AMOUNT_INFO_ANTICIPATED_AMOUNT_NEGATIVE); 
            }
        } 
        KualiDecimal amountReleased=KualiDecimal.ZERO;
        if( amountReleased!=null && obligatedAmount!=null){
            int count =0;
            for(SubAwardAmountReleased subAwardAmountReleased :subAward.getSubAwardAmountReleasedList()){  
                if(subAwardAmountReleased.getAmountReleased()!=null){
                    count=count+1; 
                    amountReleased = amountReleased .add(subAwardAmountReleased.getAmountReleased());
                    if(amountReleased.isGreaterThan(obligatedAmount)){
                        rulePassed = false;
                        reportError("document.subAwardList[0].subAwardAmountReleasedList["+Integer.toString(count-1)+"].amountReleased"
                                , KeyConstants.ERROR_SUBAWARD_AMOUNT_RELEASED_GREATER_OBLIGATED_AMOUNT );  
                    }
                }
            } 
        }
        if( amountReleased!=null){
            int count =0;
            for(SubAwardAmountReleased subAwardAmountReleased :subAward.getSubAwardAmountReleasedList()){
                if(subAwardAmountReleased.getAmountReleased()!=null){
                    count=count+1; 
                    if(subAwardAmountReleased.getAmountReleased().isNegative()){
                        rulePassed = false;
                        reportError("document.subAwardList[0].subAwardAmountReleasedList["+Integer.toString(count-1)+"].amountReleased"
                                , KeyConstants.ERROR_SUBAWARD_AMOUNT_RELEASED_NEGATIVE ); 
                    }
                }
            } 
        }
        if( obligatedAmount!=null){
            int count =0;
            for(SubAwardAmountReleased subAwardAmountReleased :subAward.getSubAwardAmountReleasedList()){
                if(subAwardAmountReleased.getStartDate()!=null && subAwardAmountReleased.getEndDate()!=null ){
                    count=count+1;                
                    if(subAwardAmountReleased.getStartDate().after(subAwardAmountReleased.getEndDate())){
                        rulePassed = false;   
                        reportError("document.subAwardList[0].subAwardAmountReleasedList["+Integer.toString(count-1)+"].startDate"
                                , KeyConstants.SUBAWARD_ERROR_END_DATE_GREATER_THAN_START);
                    }
                }
            }
        }
       
        if(obligatedAmount != null && anticipatedAmount != null) {
            if(obligatedAmount.isGreaterThan(anticipatedAmount)) {
                rulePassed = false;
                reportError(AMOUNT_INFO_ANTICIPATED_AMOUNT
                        , KeyConstants.ERROR_AMOUNT_INFO_OBLIGATED_AMOUNT_GREATER_THAN_ANTICIPATED_AMOUNT); 
            }
        }
        
        if( obligatedAmount!=null){
            int count =0;
            List<String> awardAmountReleased = new ArrayList<String>();
            for(SubAwardAmountReleased subAwardAmountReleased :subAward.getSubAwardAmountReleasedList()){
                if(subAwardAmountReleased.getInvoiceNumber()!=null){
                    count=count+1;
                    if(awardAmountReleased.contains(subAwardAmountReleased.getInvoiceNumber())){
                        rulePassed = false;
                        reportError("document.subAwardList[0].subAwardAmountReleasedList["+Integer.toString(count-1)+"].invoiceNumber"
                                , KeyConstants.ERROR_SUBAWARD_INVOICE_NUMBER_SHOULD_BE_UNIQUE);
                    }
                    else
                    {
                        awardAmountReleased .add(subAwardAmountReleased.getInvoiceNumber());
                    }
                }
            }
        }
        
        return rulePassed;
    }

    public boolean processAddSubAwardAmountInfoBusinessRules(SubAwardAmountInfo amountInfo,SubAward subAward) {
        boolean rulePassed = true; 
        
        rulePassed &= processSaveSubAwardAmountInfoBusinessRules(amountInfo,subAward);
        
        return rulePassed;
    }    
    
    protected boolean  processSaveSubAwardAmountInfoBusinessRules(SubAwardAmountInfo amountInfo,SubAward subAward){    
        
        boolean rulePassed = true;   
        
        if(amountInfo==null 
                || amountInfo.getEffectiveDate()==null){
            rulePassed = false;
            
            reportError(AMOUNT_INFO_EFFECTIVE_DATE
                    , KeyConstants.ERROR_REQUIRED_EFFECTIVE_DATE);            
            
        }  
       
        if(amountInfo==null 
                || amountInfo.getAnticipatedChange()==null){
            rulePassed = false;            
            reportError(AMOUNT_INFO_ANTICIPATED_AMOUNT
                    , KeyConstants.ERROR_REQUIRED_ANTICIPATED_AMOUNT);
        } 
 
        if(amountInfo==null 
                || amountInfo.getObligatedChange()==null){
            rulePassed = false;            
            reportError(AMOUNT_INFO_OBLIGATED_AMOUNT
                    , KeyConstants.ERROR_REQUIRED_OBLIGATED_AMOUNT);
        }      
         
        KualiDecimal obligatedAmount=amountInfo.getObligatedChange();
        for(SubAwardAmountInfo subAwardAmountInfo :subAward.getSubAwardAmountInfoList()){   
            if( obligatedAmount!=null && subAwardAmountInfo.getObligatedChange()!=null ){
                obligatedAmount = obligatedAmount .add(subAwardAmountInfo.getObligatedChange());
            }
        } 
        if(obligatedAmount !=null){
            if(obligatedAmount .isNegative()){
                rulePassed = false; 
                reportError(AMOUNT_INFO_OBLIGATED_AMOUNT
                        , KeyConstants.ERROR_AMOUNT_INFO_OBLIGATED_AMOUNT_NEGATIVE); 
            }
        }
        KualiDecimal anticipatedAmount=amountInfo.getAnticipatedChange();
        for(SubAwardAmountInfo subAwardAmountInfo :subAward.getSubAwardAmountInfoList()){  
            if(anticipatedAmount!=null && subAwardAmountInfo.getAnticipatedChange()!=null){
                anticipatedAmount = anticipatedAmount .add(subAwardAmountInfo.getAnticipatedChange()); 
            }
        }
        if(anticipatedAmount!=null){
            if(anticipatedAmount .isNegative()){
                rulePassed = false; 
                reportError(AMOUNT_INFO_ANTICIPATED_AMOUNT
                        , KeyConstants.ERROR_AMOUNT_INFO_ANTICIPATED_AMOUNT_NEGATIVE); 
            }
        }
        
        if(obligatedAmount != null && anticipatedAmount != null) {
            if(obligatedAmount.isGreaterThan(anticipatedAmount)) {
                rulePassed = false;
                reportError(AMOUNT_INFO_ANTICIPATED_AMOUNT
                        , KeyConstants.ERROR_AMOUNT_INFO_OBLIGATED_AMOUNT_GREATER_THAN_ANTICIPATED_AMOUNT); 
            }
        }
        
        
        if(obligatedAmount !=null && subAward.getTotalAmountReleased()!=null){
            if(obligatedAmount.isLessThan(subAward.getTotalAmountReleased())){
                rulePassed = false;
                reportError(AMOUNT_INFO_OBLIGATED_AMOUNT
                        , KeyConstants.ERROR_SUBAWARD_OBLIGATED_AMOUNT_SHOULD_BE_GREATER_AMOUNT_RELEASED ); 
            }
        }
        
        return rulePassed;
    }
    
    
    public boolean processDeleteSubAwardAmountInfoBusinessRules(SubAwardAmountInfo subAwardAmountInfo,SubAward subAward) {
        boolean rulePassed = true; 
        
        rulePassed &= processSubAwardAmountInfoBusinessRules(subAwardAmountInfo,subAward);
        
        return rulePassed;
    } 
    protected boolean  processSubAwardAmountInfoBusinessRules(SubAwardAmountInfo subAwardAmountInfo,SubAward subAward){    
        
        boolean rulePassed = true;   
        if(subAward.getTotalObligatedAmount()!=null && subAwardAmountInfo.getObligatedChange()!=null && subAward.getTotalAmountReleased()!=null){
            if((subAward.getTotalObligatedAmount().subtract(subAwardAmountInfo.getObligatedChange())).isLessThan(subAward.getTotalAmountReleased())){
                rulePassed = false;
                reportError(AMOUNT_INFO_OBLIGATED_AMOUNT
                        , KeyConstants.ERROR_SUBAWARD_OBLIGATED_AMOUNT_IS_GREATER_AMOUNT_RELEASED ); 
            }
        }
        
        return rulePassed;
    }
    
    
    public boolean processAddSubAwardEffectiveDateRules(SubAwardAmountReleased subAwardAmountReleased,SubAward subAward ) {
        
        boolean rulePassed = true;
        rulePassed &= processSubAwardAmountReleasedBusinessRules(subAwardAmountReleased,subAward);
        
        return rulePassed;
    }
    
    protected boolean processSubAwardAmountReleasedBusinessRules(SubAwardAmountReleased subAwardAmountReleased,SubAward subAward ) {
        
        boolean rulePassed = false;
        
        if(subAwardAmountReleased.getStartDate() != null && subAwardAmountReleased.getEndDate() != null && subAwardAmountReleased.getEffectiveDate() != null) {
            Calendar subAwardInoiceEndDate = new GregorianCalendar();
            Calendar subAwardInoiceStartDate = new GregorianCalendar();
            subAwardInoiceEndDate.setTime(subAwardAmountReleased.getEndDate());
            subAwardInoiceStartDate.setTime(subAwardAmountReleased.getStartDate());
            subAwardInoiceEndDate.add(subAwardInoiceEndDate.DATE, 30);
            subAwardInoiceStartDate.add(subAwardInoiceStartDate.DATE, -30);
            Date endDate=(Date)subAwardInoiceEndDate.getTime();
            Date startDate=(Date)subAwardInoiceStartDate.getTime();
            if(subAwardAmountReleased.getEffectiveDate().before(startDate) ||(subAwardAmountReleased.getEffectiveDate().after(endDate)) ){
                rulePassed = true;
            }
        }
        return rulePassed;
    }
     


    public boolean processAddSubAwardAmountReleasedBusinessRules(SubAwardAmountReleased amountReleased,SubAward subAward ) {
        
        boolean rulePassed = true;
        rulePassed &= processSaveSubAwardAmountReleasedBusinessRules(amountReleased,subAward);
        
        return rulePassed;
        
    }
    
    protected boolean  processSaveSubAwardAmountReleasedBusinessRules(SubAwardAmountReleased amountReleased,SubAward subAward ){    
        
        boolean rulePassed = true;   
        
        if(amountReleased==null 
                || amountReleased.getInvoiceNumber()==null){
            rulePassed = false;            
            reportError(INVOICE_NUMBER
                    , KeyConstants.ERROR_REQUIRED_INVOICE_NUMBER);
        }  
        if(amountReleased==null 
                || amountReleased.getStartDate()==null){
            rulePassed = false;            
            reportError(START_DATE
                    , KeyConstants.ERROR_REQUIRED_AMOUNT_RELEASED_START_DATE);
        }  
        if(amountReleased==null 
                || amountReleased.getEndDate()==null){
            rulePassed = false;            
            reportError(END_DATE
                    , KeyConstants.ERROR_REQUIRED_AMOUNT_RELEASED_END_DATE);
        }  
        if(amountReleased==null 
                || amountReleased.getEffectiveDate()==null){
            rulePassed = false;            
            reportError(AMOUNT_RELEASED_EFFECTIVE_DATE
                    , KeyConstants.ERROR_REQUIRED_AMOUNT_RELEASED_EFFECTIVE_DATE);
        }  
        if(amountReleased==null 
                || amountReleased.getAmountReleased()==null){
            rulePassed = false;            
            reportError(AMOUNT_RELEASED
                    , KeyConstants.ERROR_REQUIRED_AMOUNT_RELEASED);
        }  
        if(amountReleased.getInvoiceNumber()!=null){
            for(SubAwardAmountReleased subAwardAmountReleased :subAward.getSubAwardAmountReleasedList()){
                if(subAwardAmountReleased.getInvoiceNumber()!=null){
                    if(amountReleased.getInvoiceNumber().equals(subAwardAmountReleased.getInvoiceNumber())){
                        rulePassed = false; 
                        reportError(INVOICE_NUMBER
                                , KeyConstants.ERROR_SUBAWARD_INVOICE_NUMBER_SHOULD_BE_UNIQUE);
                    }
                }
            }
        }
        if(amountReleased!=null && amountReleased.getStartDate()!=null && amountReleased.getEndDate()!=null){
            if(amountReleased.getStartDate().after(amountReleased.getEndDate())){
                rulePassed = false;            
                reportError(START_DATE
                        , KeyConstants.SUBAWARD_ERROR_END_DATE_GREATER_THAN_START);
            }
        }
        KualiDecimal totalAmount = KualiDecimal.ZERO;
        if(amountReleased.getAmountReleased()!=null){
            totalAmount=amountReleased.getAmountReleased();
        }
        if(totalAmount!=null){
            for(SubAwardAmountReleased subAwardAmountReleased :subAward.getSubAwardAmountReleasedList()){   
                if(subAwardAmountReleased.getAmountReleased()!=null){
                    totalAmount = totalAmount .add(subAwardAmountReleased.getAmountReleased());
                }
            } 
        }     
        if(totalAmount!=null && totalAmount.isNegative()){
            rulePassed = false;
            reportError(AMOUNT_RELEASED
                    , KeyConstants.ERROR_SUBAWARD_AMOUNT_RELEASED_NEGATIVE ); 
        }
        if(totalAmount!=null && subAward.getTotalObligatedAmount()!=null){
            if(totalAmount.isGreaterThan(subAward.getTotalObligatedAmount())){
                rulePassed = false;
                reportError(AMOUNT_RELEASED
                        , KeyConstants.ERROR_SUBAWARD_AMOUNT_RELEASED_GREATER_OBLIGATED_AMOUNT ); 
            }
        }
        return rulePassed;
    }
    
    
    
    
    

    public boolean processAddSubAwardContactBusinessRules(SubAwardContact subAwardContact,SubAward subAward) {
        boolean rulePassed = true;
        rulePassed &= processSaveSubAwardContactBusinessRules(subAwardContact,subAward);
        
        return rulePassed;
    }
    protected boolean  processSaveSubAwardContactBusinessRules(SubAwardContact subAwardContact,SubAward subAward){ 
        
        boolean rulePassed = true;   
        
        if(subAwardContact==null 
                || subAwardContact.getRolodexId()==null){
            rulePassed = false;            
            reportError(ROLODEX_ID
                    , KeyConstants.ERROR_REQUIRED_SUBAWARD_CONTACT_ROLODEX_ID);
        }  
        if(subAwardContact==null 
                || subAwardContact.getContactTypeCode()==null){
            rulePassed = false;            
            reportError(CONTACT_TYPE_CODE
                    , KeyConstants.ERROR_REQUIRED_SUBAWARD_CONTACT_TYPE_CODE);
        }  
        for(SubAwardContact contact : subAward.getSubAwardContactsList()){
            if(contact.getRolodexId()!=null && subAwardContact.getRolodexId()!=null ){
                if(contact.getRolodexId().equals(subAwardContact.getRolodexId())){
                    rulePassed = false;              
                    String contactName = contact.getRolodex().getFullName();

                    if(contactName == null){
                        contactName = contact.getRolodex().getOrganization();
                    }               
                    reportError(ROLODEX_ID, KeyConstants.ERROR_REQUIRED_SUBAWARD_CONTACT_PERSON_EXIST, new String[] {contactName});  
                }
            }
        }
        return rulePassed;
    }
    
    
    
    
    
    

    public boolean processAddSubAwardCloseoutBusinessRules(SubAwardCloseout subAwardCloseout) {
        boolean rulePassed = true;
        rulePassed &= processSaveSubAwardCloseoutBusinessRules(subAwardCloseout);
        
        return rulePassed;
    }
    protected boolean  processSaveSubAwardCloseoutBusinessRules(SubAwardCloseout subAwardCloseout){
        boolean rulePassed = true;   
        
        if(subAwardCloseout==null 
                || subAwardCloseout.getCloseoutTypeCode()==null){
            rulePassed = false;            
            reportError(CLOSEOUT_TYPE_CODE
                    , KeyConstants.ERROR_REQUIRED_SUBAWARD_CLOSEOUT_TYPE_CODE);
        }  
        if (subAwardCloseout == null || subAwardCloseout.getDateRequested() == null) {
            rulePassed = false;
            reportError(DATE_REQUESTED, KeyConstants.ERROR_REQUIRED_SUBAWARD_DATE_REQUESTED);
        }
        return rulePassed;
    }
    
    
    

    public boolean processAddSubAwardFundingSourceBusinessRules(SubAwardFundingSource subAwardFundingSource,SubAward subAward) {
        boolean rulePassed = true;
        rulePassed &= processSaveSubAwardFundingSourceBusinessRules(subAwardFundingSource,subAward);
        
        return rulePassed;
    }
    protected boolean processSaveSubAwardFundingSourceBusinessRules(SubAwardFundingSource subAwardFundingSource,SubAward subAward){
        boolean rulePassed = true;   
        
        if(subAwardFundingSource==null 
                || subAwardFundingSource.getAwardId()==null){
            rulePassed = false;            
            reportError(AWARD_NUMBER
                    , KeyConstants.ERROR_REQUIRED_SUBAWARD_FUNDING_SOURCE_AWARD_NUMBER);
        }  
        else{
            for(SubAwardFundingSource fundingSource : subAward.getSubAwardFundingSourceList()){
                if(fundingSource.getAwardId().equals(subAwardFundingSource.getAwardId())){
                    rulePassed = false;
                    AwardService awardService = KraServiceLocator.getService(AwardService.class);
                    Award award = awardService.getAward(fundingSource.getAwardId());
                    
                    reportError(AWARD_NUMBER, KeyConstants.ERROR_REQUIRED_SUBAWARD_FUNDING_SOURCE_AWARD_NUMBER_DUPLICATE, new String[] {award.getAwardNumber()});
                }
            }
        }
        return rulePassed;
    }
    
    /**
     * @see org.kuali.rice.kns.rule.DocumentAuditRule#processRunAuditBusinessRules(org.kuali.rice.kns.document.Document)
     */
    @Override
    public boolean processRunAuditBusinessRules(Document document){
        boolean retval = true;
        retval &= new SubAwardAuditRule().processRunAuditBusinessRules(document);
        retval &= new SubAwardCustomDataAuditRule().processRunAuditBusinessRules(document);
        retval &= new SubAwardFinancialAuditRule().processRunAuditBusinessRules(document);
        return retval;
    }
    
    
    
    @Override
    protected boolean processCustomSaveDocumentBusinessRules(Document document) {
        if (!(document instanceof SubAwardDocument)) {
            return false;
        }

        MessageMap errorMap = GlobalVariables.getMessageMap();
        errorMap.addToErrorPath(DOCUMENT_ERROR_PATH);
        getDictionaryValidationService().validateDocumentAndUpdatableReferencesRecursively(
               document, getMaxDictionaryValidationDepth(),
               VALIDATION_REQUIRED, CHOMP_LAST_LETTER_S_FROM_COLLECTION_NAME);
        errorMap.removeFromErrorPath(DOCUMENT_ERROR_PATH);

        boolean valid = true;
        
        valid &= processSaveAwardCustomDataBusinessRules((SubAwardDocument) document);
       
        return valid;
    }
    
    /**
    *
    * process save Custom Data Business Rules.
    * @param subawardDocument
    * @return
    */
    public boolean processSaveAwardCustomDataBusinessRules(Document document) {
        boolean valid = true;
        
        SubAwardDocument subAwardDocument = (SubAwardDocument) document;
        Map<String, CustomAttributeDocument> customAttributeDocuments = subAwardDocument.getCustomAttributeDocuments();
        for (Map.Entry<String, CustomAttributeDocument> customAttributeDocumentEntry : customAttributeDocuments.entrySet()) {
            CustomAttributeDocument customAttributeDocument = customAttributeDocumentEntry.getValue();
            CustomAttribute customAttribute = customAttributeDocument.getCustomAttribute();
            if(subAwardDocument.getSubAward().getSubAwardCustomDataList().size() > 0) {
                int customAttributeId = customAttributeDocument.getCustomAttributeId();
                List<SubAwardCustomData> subAwardCustomDataList = subAwardDocument.getSubAward().getSubAwardCustomDataList();
                for(SubAwardCustomData awardCustomData : subAwardCustomDataList){
                  if(awardCustomData.getCustomAttributeId() == customAttributeId){  
                      customAttribute.setValue(awardCustomData.getValue());
                      break;
                  }
                }
            }
        }
        valid &= processRules(new SaveCustomAttributeEvent(Constants.EMPTY_STRING, subAwardDocument));
        return valid;
    }
    /**
     * @see org.kuali.kra.rule.BusinessRuleInterface#processRules(org.kuali.kra.rule.event.KraDocumentEventBaseExtension)
     */
    public boolean processRules(KraDocumentEventBaseExtension event) {
        boolean retVal = false;
        retVal = event.getRule().processRules(event);
        return retVal;
    }
}