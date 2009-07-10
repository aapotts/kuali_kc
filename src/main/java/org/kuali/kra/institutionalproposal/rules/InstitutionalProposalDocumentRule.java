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
package org.kuali.kra.institutionalproposal.rules;

import org.kuali.kra.award.document.AwardDocument;
import org.kuali.kra.institutionalproposal.customdata.InstitutionalProposalCustomDataRuleImpl;
import org.kuali.kra.institutionalproposal.customdata.InstitutionalProposalSaveCustomDataRuleEvent;
import org.kuali.kra.institutionalproposal.document.InstitutionalProposalDocument;
import org.kuali.kra.rules.ResearchDocumentRuleBase;
import org.kuali.rice.kns.document.Document;
import org.kuali.rice.kns.util.ErrorMap;
import org.kuali.rice.kns.util.GlobalVariables;

/**
 * This class...
 */
public class InstitutionalProposalDocumentRule extends ResearchDocumentRuleBase {
    
    public static final String DOCUMENT_ERROR_PATH = "document";
    public static final String INSTITUTIONAL_PROPOSAL_ERROR_PATH = "institutionalProposalList[0]";
    
    public static final boolean VALIDATION_REQUIRED = true;
    public static final boolean CHOMP_LAST_LETTER_S_FROM_COLLECTION_NAME = false;
    
    /**
     * 
     * @see org.kuali.core.rules.DocumentRuleBase#processCustomSaveDocumentBusinessRules(
     * org.kuali.rice.kns.document.Document)
     */
    @Override
    protected boolean processCustomSaveDocumentBusinessRules(Document document) {
        boolean retval = true;
        ErrorMap errorMap = GlobalVariables.getErrorMap();
        if (!(document instanceof InstitutionalProposalDocument)) {
            return false;
        }
        
        errorMap.addToErrorPath(DOCUMENT_ERROR_PATH);
        getDictionaryValidationService().validateDocumentAndUpdatableReferencesRecursively(
                document, getMaxDictionaryValidationDepth(),
                VALIDATION_REQUIRED, CHOMP_LAST_LETTER_S_FROM_COLLECTION_NAME);
        errorMap.removeFromErrorPath(DOCUMENT_ERROR_PATH);
        
        
        retval &= processSaveInstitutionalProposalCustomDataBusinessRules(document);
        
        return retval;
    }    
    
    /**
    *
    * process save Custom Data Business Rules.
    * @param institutionalProposalDocument
    * @return
    */
    private boolean processSaveInstitutionalProposalCustomDataBusinessRules(Document document) {
        boolean valid = true;
        ErrorMap errorMap = GlobalVariables.getErrorMap();
        InstitutionalProposalDocument institutionalProposalDocument = (InstitutionalProposalDocument) document;
        errorMap.addToErrorPath(DOCUMENT_ERROR_PATH);
        errorMap.addToErrorPath(INSTITUTIONAL_PROPOSAL_ERROR_PATH);
        String errorPath = "institutionalProposalCustomData";
        errorMap.addToErrorPath(errorPath);
        InstitutionalProposalSaveCustomDataRuleEvent event = new InstitutionalProposalSaveCustomDataRuleEvent(errorPath, 
                                                               institutionalProposalDocument);
        valid &= new InstitutionalProposalCustomDataRuleImpl().processSaveInstitutionalProposalCustomDataBusinessRules(event);
        errorMap.removeFromErrorPath(errorPath);
        errorMap.removeFromErrorPath(INSTITUTIONAL_PROPOSAL_ERROR_PATH);
        errorMap.removeFromErrorPath(DOCUMENT_ERROR_PATH);
        return valid;
    }
    

}
