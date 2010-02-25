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
package org.kuali.kra.award.maintenance;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.award.home.AwardTemplate;
import org.kuali.kra.award.home.AwardTemplateReportTerm;
import org.kuali.kra.award.home.AwardTemplateTerm;
import org.kuali.kra.award.home.ValidBasisMethodPayment;
import org.kuali.kra.award.paymentreports.ValidClassReportFrequency;
import org.kuali.kra.award.paymentreports.ValidFrequencyBase;
import org.kuali.kra.bo.SponsorTerm;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.institutionalproposal.proposallog.ProposalLog;
import org.kuali.kra.maintenance.KraMaintainableImpl;
import org.kuali.kra.rules.ErrorReporter;
import org.kuali.rice.kns.bo.PersistableBusinessObject;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.util.KNSConstants;
import org.kuali.rice.kns.util.ObjectUtils;

/**
 * This class is for adding validation rules to maintain Award Template
 */
public class AwardTemplateMaintainableImpl extends KraMaintainableImpl {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -3368480537790330757L;
    
    private static final String PERSON_OBJECT_REFERENCE = "person";
    
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(AwardTemplateMaintainableImpl.class); 

    
    public void addMultipleValueLookupResults(MaintenanceDocument document, String collectionName, Collection<PersistableBusinessObject> rawValues, boolean needsBlank, PersistableBusinessObject bo) {
        Collection maintCollection = (Collection) ObjectUtils.getPropertyValue(bo, collectionName);
        String docTypeName = document.getDocumentHeader().getWorkflowDocument().getDocumentType();
        BusinessObjectService businessObjectService = KraServiceLocator.getService(BusinessObjectService.class);
        
        
        if( StringUtils.equals( collectionName, "templateTerms") ) {
            for (PersistableBusinessObject nextBo : rawValues) {
                SponsorTerm aTerm = (SponsorTerm)nextBo;
                aTerm.refreshNonUpdateableReferences();
            }
        }
        super.addMultipleValueLookupResults(document, collectionName, rawValues, needsBlank, bo);
        
    }
    
    /**
     * This method is for performing any new validations while adding new items to the list.
     */
    @Override
    public void addNewLineToCollection(String collectionName) {
        if(collectionName.equals("templateReportTerms")){
            ErrorReporter errorReporter = new ErrorReporter();
            AwardTemplateReportTerm awardTemplateReportTerm = (AwardTemplateReportTerm)newCollectionLines.get( collectionName );
            if ( awardTemplateReportTerm != null ) {
                if(!isValid(awardTemplateReportTerm)){
                    reportReportTermError(errorReporter, awardTemplateReportTerm);
                }else{
                    super.addNewLineToCollection(collectionName);
                }
                
            }
        } else {
            super.addNewLineToCollection(collectionName);
        }
    }
   
    @Override
    public void prepareForSave() {
        AwardTemplate awardTemplate = (AwardTemplate)this.businessObject;
        if(!isValid(awardTemplate.getBasisOfPaymentCode(),awardTemplate.getMethodOfPaymentCode())){
            reportInvalidAwardBasisError(awardTemplate);
        }
        super.prepareForSave();
    }
    
    private void reportInvalidAwardBasisError(AwardTemplate awardTemplate) {
        ErrorReporter errorReporter = new ErrorReporter();
        awardTemplate.refreshNonUpdateableReferences();
        errorReporter.reportError("document.newMaintainableObject.basisOfPaymentCode", 
                        KeyConstants.INVALID_BASIS_PAYMENT,
                        new String[]{awardTemplate.getAwardBasisOfPayment()==null?"":
                                        awardTemplate.getAwardBasisOfPayment().getDescription()});
        errorReporter.reportError("document.newMaintainableObject.methodOfPaymentCode", 
                        KeyConstants.INVALID_METHOD_PAYMENT,
                        new String[]{awardTemplate.getAwardMethodOfPayment()==null?"":
                                        awardTemplate.getAwardMethodOfPayment().getDescription()});
    }
    private boolean isValid(String basisOfPaymentCode, String methodOfPaymentCode) {
        BusinessObjectService businessObjectService = KraServiceLocator.getService(BusinessObjectService.class);
        Map<String, String> validBasisOfPaymentsParams = new HashMap<String, String>();
        validBasisOfPaymentsParams.put("basisOfPaymentCode", basisOfPaymentCode);
        validBasisOfPaymentsParams.put("methodOfPaymentCode", methodOfPaymentCode);
        Collection<ValidBasisMethodPayment> validBasisMethodPayments = businessObjectService.findMatching(ValidBasisMethodPayment.class, validBasisOfPaymentsParams);
        return !validBasisMethodPayments.isEmpty();
    }
    /**
     * This method is to report error on the GlobalError
     * @param errorReporter
     * @param awardTemplateReportTerm
     */
    private void reportReportTermError(ErrorReporter errorReporter, AwardTemplateReportTerm awardTemplateReportTerm) {
        awardTemplateReportTerm.refreshNonUpdateableReferences();
        errorReporter.reportError("document.newMaintainableObject.add.templateReportTerms", KeyConstants.INVALID_REPORT_FREQUENCY,
                new String[]{awardTemplateReportTerm.getReportClass().getDescription(),awardTemplateReportTerm.getReport().getDescription(),
                awardTemplateReportTerm.getFrequency().getDescription(),awardTemplateReportTerm.getFrequencyBase().getDescription()});
    }
    /**
     * 
     * This method is to check whether the selected values for ReportTerm 
     * @param awardTemplateReportTerm
     * @return
     */
    private boolean isValid(AwardTemplateReportTerm awardTemplateReportTerm) {
        BusinessObjectService businessObjectService = KraServiceLocator.getService(BusinessObjectService.class);
        Map<String, String> classReportFreqParams = new HashMap<String, String>();
        classReportFreqParams.put("reportClassCode", awardTemplateReportTerm.getReportClassCode());
        classReportFreqParams.put("reportCode", awardTemplateReportTerm.getReportCode());
        classReportFreqParams.put("frequencyCode", awardTemplateReportTerm.getFrequencyCode());
        Collection<ValidClassReportFrequency> coll = businessObjectService.findMatching(ValidClassReportFrequency.class, classReportFreqParams);
        Map<String, String> freqBaseParams = new HashMap<String, String>();
        freqBaseParams.put("frequencyBaseCode", awardTemplateReportTerm.getFrequencyBaseCode());
        freqBaseParams.put("frequencyCode", awardTemplateReportTerm.getFrequencyCode());
        Collection<ValidFrequencyBase> validFrequencyBases = businessObjectService.findMatching(ValidFrequencyBase.class, freqBaseParams);
        
        return !coll.isEmpty() && !validFrequencyBases.isEmpty();
    }
    
    /**
     * @see org.kuali.rice.kns.maintenance.Maintainable#refresh(String refreshCaller, Map fieldValues, MaintenanceDocument document)
     */
    @Override
    @SuppressWarnings("unchecked")
    public void refresh(String refreshCaller, Map fieldValues, MaintenanceDocument document) {
        super.refresh(refreshCaller, fieldValues, document);
//        LOG.warn("refresh called.");
//        // If a person has been selected, lead unit should default to the person's home unit.
//        String referencesToRefresh = (String) fieldValues.get(KNSConstants.REFERENCES_TO_REFRESH);
//        
//        if (referencesToRefresh != null && referencesToRefresh.contains(PERSON_OBJECT_REFERENCE)) {
//            LOG.info( "*********" + referencesToRefresh );
//            ProposalLog proposalLog = (ProposalLog) this.getBusinessObject();
//            if (proposalLog.getkcPerson() != null) {
//                proposalLog.setLeadUnit(proposalLog.getkcPerson().getContactOrganizationName());
//            }
//        }
    }
   
}
