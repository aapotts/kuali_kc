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
package org.kuali.kra.irb.specialreview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.irb.ProtocolAction;
import org.kuali.kra.irb.ProtocolDocument;
import org.kuali.kra.irb.ProtocolForm;
import org.kuali.kra.rule.event.AddSpecialReviewEvent;
import org.kuali.kra.rule.event.SaveSpecialReviewEvent;

/**
 * This class represents the Struts Action for Special Review page(ProtocolSpecialReview.jsp).
 */
public class ProtocolSpecialReviewAction extends ProtocolAction {
    
    private static final String ADD_SPECIAL_REVIEW_FIELD = "specialReviewHelper.newSpecialReview";
    private static final String SAVE_SPECIAL_REVIEW_FIELD = "document.protocolList[0].specialReview";

    
    /**
     * {@inheritDoc}
     * @see org.kuali.kra.web.struts.action.KraTransactionalDocumentActionBase#execute(org.apache.struts.action.ActionMapping, 
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        ActionForward actionForward = super.execute(mapping, form, request, response);

        ((ProtocolForm) form).getSpecialReviewHelper().prepareView();
        
        return actionForward;
    }
    
    /**
     * 
     * @see org.kuali.kra.irb.ProtocolAction#preSave(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void preSave(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolDocument document = protocolForm.getDocument();
        for (ProtocolSpecialReview review : document.getProtocol().getSpecialReviews()) {
            if (review.getExemptionTypeCodes() == null || review.getExemptionTypeCodes().size() == 0) {
                review.setExemptionTypeCodes(new ArrayList<String>());
                //delete the codes for this review
                if (review.getProtocolSpecialReviewId() != null) {
                    Map values = new HashMap();
                    values.put("PROTOCOL_SPECIAL_REVIEW_ID", review.getProtocolSpecialReviewId());
                    this.getBusinessObjectService().deleteMatching(ProtocolSpecialReviewExemption.class, values);
                }
            }
        }
    }
    
    /**
     * This method is for adding ProtocolSpecialReview to the list.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws Exception
     */
    public ActionForward addSpecialReview(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolDocument document = protocolForm.getDocument();
        ProtocolSpecialReview newSpecialReview = protocolForm.getSpecialReviewHelper().getNewSpecialReview();
        
        if (applyRules(new AddSpecialReviewEvent<ProtocolSpecialReview>(ADD_SPECIAL_REVIEW_FIELD, document, newSpecialReview))) {
            newSpecialReview.setSpecialReviewNumber(document.getDocumentNextValue(Constants.SPECIAL_REVIEW_NUMBER));
            document.getProtocol().getSpecialReviews().add(newSpecialReview);
            protocolForm.getSpecialReviewHelper().setNewSpecialReview(new ProtocolSpecialReview());
        }
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * This method deletes the SpecialReview from the list.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deleteSpecialReview(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
        throws Exception {
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolDocument document = protocolForm.getDocument();
        
        document.getProtocol().getSpecialReviews().remove(getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * {@inheritDoc}
     * @see org.kuali.kra.irb.ProtocolAction#isValidSave(org.kuali.kra.irb.ProtocolForm)
     */
    @Override
    protected boolean isValidSave(ProtocolForm protocolForm) {
        ProtocolDocument document = protocolForm.getDocument();
        List<ProtocolSpecialReview> specialReviews = document.getProtocol().getSpecialReviews();

        return applyRules(new SaveSpecialReviewEvent<ProtocolSpecialReview>(SAVE_SPECIAL_REVIEW_FIELD, document, specialReviews));
    }
    
}