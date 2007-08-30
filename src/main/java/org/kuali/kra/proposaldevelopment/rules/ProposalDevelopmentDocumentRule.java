/*
 * Copyright 2007 The Kuali Foundation.
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
package org.kuali.kra.proposaldevelopment.rules;

import org.kuali.core.document.Document;
import org.kuali.core.rules.DocumentRuleBase;
import org.kuali.core.util.ErrorMap;
import org.kuali.core.util.GlobalVariables;
import org.kuali.kra.bo.ValidSpRevApproval;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.proposaldevelopment.bo.PropLocation;
import org.kuali.kra.proposaldevelopment.bo.PropSpecialReview;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.rice.KNSServiceLocator;

/**
 * This class...
 * 
 * @author Kuali Nervous System Team (kualidev@oncourse.iu.edu)
 */
public class ProposalDevelopmentDocumentRule extends DocumentRuleBase {

    @Override
    protected boolean processCustomSaveDocumentBusinessRules(Document document) {
        if (!(document instanceof ProposalDevelopmentDocument)) {
            return false;
        }

        boolean valid = true;

        ProposalDevelopmentDocument proposalDevelopmentDocument = (ProposalDevelopmentDocument) document;

        GlobalVariables.getErrorMap().addToErrorPath("document");

        // changing this to '0' so it doesn't validate reference objects within a list
        KNSServiceLocator.getDictionaryValidationService().validateDocumentRecursively(proposalDevelopmentDocument, 0);
        if (proposalDevelopmentDocument.getOrganizationId()!=null && (proposalDevelopmentDocument.getPropLocations().size()==0 ||
                (proposalDevelopmentDocument.getPropLocations().size()==1 && ((PropLocation)(proposalDevelopmentDocument.getPropLocations().get(0))).getLocationSequenceNumber()==null))) {
            // should have one just added by form
            // this is a business rule, so put it here.  If needed we can always create a new prop location if the last one is deleted in deletelocation action ?
            GlobalVariables.getErrorMap().addToErrorPath("newPropLocation");
            GlobalVariables.getErrorMap().putError("location", KeyConstants.ERROR_REQUIRED_FOR_PROPLOCATION);
            GlobalVariables.getErrorMap().removeFromErrorPath("newPropLocation");
        }
        valid &= processSpecialReviewBusinessRule(proposalDevelopmentDocument);

        GlobalVariables.getErrorMap().removeFromErrorPath("document");


        return valid;
    }

    private boolean processSpecialReviewBusinessRule(ProposalDevelopmentDocument proposalDevelopmentDocument) {
        boolean valid = true;

        ErrorMap errorMap = GlobalVariables.getErrorMap();

        int i = 0;

        for (PropSpecialReview propSpecialReview : proposalDevelopmentDocument.getPropSpecialReviews()) {
            errorMap.addToErrorPath("propSpecialReviews[" + i + "]");
            propSpecialReview.refresh();
            propSpecialReview.refreshReferenceObject("validSpRevApproval");
            if (propSpecialReview.getApprovalTypeCode() != null && propSpecialReview.getSpecialReviewCode() != null) {
                ValidSpRevApproval validSpRevApproval = propSpecialReview.getValidSpRevApproval();
                if (validSpRevApproval != null) {
                    if (validSpRevApproval.isProtocolNumberFlag() && propSpecialReview.getProtocolNumber() == null) {
                        valid = false;
                        errorMap.putError("protocolNumber", KeyConstants.ERROR_REQUIRED_FOR_VALID_SPECIALREVIEW, "Protocol Number",
                                validSpRevApproval.getSpecialReview().getDescription() + "/"
                                        + validSpRevApproval.getSpecialReviewApprovalType().getDescription());
                    }
                    if (validSpRevApproval.isApplicationDateFlag() && propSpecialReview.getApplicationDate() == null) {
                        valid = false;
                        errorMap.putError("applicationDate", KeyConstants.ERROR_REQUIRED_FOR_VALID_SPECIALREVIEW,
                                "Protocol Number", validSpRevApproval.getSpecialReview().getDescription() + "/"
                                        + validSpRevApproval.getSpecialReviewApprovalType().getDescription());
                    }
                    if (validSpRevApproval.isApprovalDateFlag() && propSpecialReview.getApprovalDate() == null) {
                        valid = false;
                        errorMap.putError("approvalDate", KeyConstants.ERROR_REQUIRED_FOR_VALID_SPECIALREVIEW, "Protocol Number",
                                validSpRevApproval.getSpecialReview().getDescription() + "/"
                                        + validSpRevApproval.getSpecialReviewApprovalType().getDescription());
                    }

                }

            }
            // TODO : are all the sp rev business rules defined in valid_sp_rev_approval table too?
            // If they are, then the following are not needed
            if (propSpecialReview.getApprovalTypeCode() != null) {
                if (propSpecialReview.getApprovalTypeCode().intValue() == Constants.APPROVAL_STATUS.intValue()) {
                    if (propSpecialReview.getApplicationDate() == null) {
                        valid = false;
                        errorMap.putError("applicationDate", KeyConstants.ERROR_REQUIRED_FOR_APPROVED_SPECIALREVIEW,
                                "Application Date");
                    }
                    if (propSpecialReview.getApprovalDate() == null) {
                        valid = false;
                        errorMap.putError("approvalDate", KeyConstants.ERROR_REQUIRED_FOR_APPROVED_SPECIALREVIEW, "Approval Date");
                    }
                    if (propSpecialReview.getProtocolNumber() == null) {
                        valid = false;
                        errorMap.putError("protocolNumber", KeyConstants.ERROR_REQUIRED_FOR_APPROVED_SPECIALREVIEW,
                                "Protocol Number");
                    }
                }
                else {
                    if (propSpecialReview.getApprovalDate() != null) {
                        valid = false;
                        errorMap.putError("approvalDate", KeyConstants.ERROR_NOT_APPROVED_SPECIALREVIEW, "Approval Date");
                    }
                }
            }
            errorMap.removeFromErrorPath("propSpecialReviews[" + i++ + "]");
        }
        return valid;
    }

}
