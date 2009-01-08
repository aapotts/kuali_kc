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
package org.kuali.kra.award.web.struts.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.kra.award.bo.Award;
import org.kuali.kra.award.bo.AwardScienceKeyword;
import org.kuali.kra.award.bo.AwardCostShare;
import org.kuali.kra.award.bo.AwardApprovedSubaward;
import org.kuali.kra.award.document.AwardDocument;
import org.kuali.kra.award.web.struts.form.AwardForm;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.service.KeywordsService;


/**
 * 
 * This class represents the Struts Action for Award page(AwardHome.jsp) 
 */
public class AwardHomeAction extends AwardAction { 

    /**
     * This method is used to add a new Award Cost Share
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return mapping forward
     * @throws Exception
     */
    public ActionForward addApprovedSubaward(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AwardForm awardForm = (AwardForm) form;
        AwardDocument awardDocument = awardForm.getAwardDocument();
        AwardApprovedSubaward awardApprovedSubaward = awardForm.getNewAwardApprovedSubaward();
        //initialize fields not set on form.
        awardApprovedSubaward.setAward(awardDocument.getAward());
        awardApprovedSubaward.setAwardNumber(awardDocument.getAward().getAwardNumber());
        awardApprovedSubaward.setSequenceNumber(awardDocument.getAward().getSequenceNumber());
       
        awardDocument.getAward().getAwardApprovedSubawards().add(awardApprovedSubaward);
        awardForm.setNewAwardApprovedSubaward(new AwardApprovedSubaward());
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * This method is used to delete an Award Cost Share
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return mapping forward
     * @throws Exception
     */
    public ActionForward deleteApprovedSubaward(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AwardForm awardForm = (AwardForm) form;
        AwardDocument awardDocument = awardForm.getAwardDocument();
        int delApprovedSubaward = getLineToDelete(request);
        awardDocument.getAward().getAwardApprovedSubawards().remove(delApprovedSubaward);
        
        return mapping.findForward(Constants.MAPPING_BASIC);
     
    }
    
    /**
     * This method is used to recalculate the Total Subaward amount in the Subaward panel.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return mapping forward
     * @throws Exception
     */
    public ActionForward recalculateSubawardTotal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
       
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
   

    /**
     * This takes care of populating the ScienceKeywords in keywords list after the selected Keywords returns from <code>multilookup</code>
     * @see org.kuali.core.web.struts.action.KualiDocumentActionBase#refresh(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @SuppressWarnings("unchecked")
    @Override
    public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        super.refresh(mapping, form, request, response);
        AwardForm awardMultiLookupForm = (AwardForm) form;
        Award awardDocument = awardMultiLookupForm.getAwardDocument().getAward();
        getKeywordService().addKeywords(awardDocument, awardMultiLookupForm);
        return mapping.findForward(Constants.MAPPING_BASIC);
    }    
    /**
     * 
     * This method...
     * @return
     */
    @SuppressWarnings("unchecked")
    protected KeywordsService getKeywordService(){
        return KraServiceLocator.getService(KeywordsService.class);
    }
    /**
     * 
     * This method is for selecting all keywords if javascript is disabled on a browser. 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return Basic ActionForward
     * @throws Exception
     */
    public ActionForward selectAllScienceKeyword(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AwardForm awardForm = (AwardForm) form;
        AwardDocument awardDocument = awardForm.getAwardDocument();
        List<AwardScienceKeyword> keywords = awardDocument.getAward().getKeywords();
        for (AwardScienceKeyword awardScienceKeyword : keywords) {
            awardScienceKeyword.setSelectKeyword(true);
        }
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    /**
     * 
     * This method is to delete selected keywords from the keywords list. 
     * It uses {@link KeywordsService} to process the request 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public ActionForward deleteSelectedScienceKeyword(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AwardForm awardForm = (AwardForm) form;
        AwardDocument awardDocument = awardForm.getAwardDocument();
        KeywordsService keywordsService = KraServiceLocator.getService(KeywordsService.class);
        keywordsService.deleteKeyword(awardDocument.getAward()); 
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
}
