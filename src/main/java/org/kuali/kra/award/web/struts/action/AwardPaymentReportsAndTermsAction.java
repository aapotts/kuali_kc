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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.kra.award.bo.Award;
import org.kuali.kra.award.bo.AwardReportTerm;
import org.kuali.kra.award.bo.AwardReportTermRecipient;
import org.kuali.kra.award.document.AwardDocument;
import org.kuali.kra.award.rule.event.AddAwardReportTermEvent;
import org.kuali.kra.award.rule.event.AddAwardReportTermRecipientEvent;
import org.kuali.kra.award.web.struts.form.AwardForm;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.service.AwardReportsService;
import org.kuali.rice.kns.util.KNSConstants;

/**
 * 
 * This class represents the Struts Action for Payments, 
 * Reports & Terms page(AwardPaymentsReportsAndTerms.jsp)
 */
public class AwardPaymentReportsAndTermsAction extends AwardAction {
        
    public ActionForward refreshPulldownOptions(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        return mapping.findForward(Constants.MAPPING_AWARD_BASIC);
    }
    
    /**
     * 
     * @see org.kuali.core.web.struts.action.KualiDocumentActionBase#reload(
     * org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, 
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward reload(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        AwardForm awardForm = (AwardForm) form;
        
        ActionForward actionForward = super.reload(mapping, form, request, response);
        
        AwardReportsService awardReportsService = KraServiceLocator.getService(AwardReportsService.class);
        awardReportsService.doPreparations(awardForm);
        
        return actionForward;        
    }
    
    /**
     * 
     * This method adds a new AwardReportTerm object to the list of AwardReportTerm objects
     * inside Award.
     * For every added AwardReportTerm object; we are adding an empty AwardReportTerm object to
     * AwardForm.newAwardReportTermRecipients list - for recipients to be added.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward addAwardReportTerm(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        AwardForm awardForm = (AwardForm) form;
        AwardDocument awardDocument= (AwardDocument) awardForm.getAwardDocument();
        
        awardForm.setAwardReportTermPanelNumber(new Integer(getReportClassCodeIndex(request)).toString());
        
        AwardReportTerm newAwardReportTerm = 
            awardForm.getNewAwardReportTerm().get(getReportClassCodeIndex(request));
        
        if(getKualiRuleService().applyRules(new AddAwardReportTermEvent(Constants.EMPTY_STRING,
                awardForm.getAwardDocument(), newAwardReportTerm))){
            
            awardDocument.getAward().setAwardReportTerms(addAwardReportTermToAward(
                    awardDocument.getAward(),newAwardReportTerm, getReportClass(request)));            
            awardForm.getNewAwardReportTerm().set(
                    getReportClassCodeIndex(request),new AwardReportTerm());            
            awardForm.getNewAwardReportTermRecipient().add(new AwardReportTermRecipient());
        }
        
        return mapping.findForward(Constants.MAPPING_AWARD_BASIC);
    }
    
    /**
     * 
     * This method adds the newAwardReportTerm to the list of <code>AwardReportTerm</code> objects.
     * @param award
     * @param newAwardReportTerm
     * @param reportClass
     * @return
     */
    protected List<AwardReportTerm> addAwardReportTermToAward(
            Award award, AwardReportTerm newAwardReportTerm, String reportClass){
        newAwardReportTerm.setReportClassCode(reportClass);
        newAwardReportTerm.setAwardNumber(award.getAwardNumber());
        newAwardReportTerm.setSequenceNumber(award.getSequenceNumber());
        award.getAwardReportTerms().add(newAwardReportTerm);
        return award.getAwardReportTerms();
    }
    
    /**
     * 
     * This method deletes a AwardReportTerm from the list of AwardReportTerm objects.
     * This method also calls another method which fetches a list of recipients that need to be deleted;
     * since all the recipients associated with the AwardReportTerm also need to be deleted and 
     * then all those objects are deleted.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deleteAwardReportTerm(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        AwardForm awardForm = (AwardForm) form;
        AwardDocument awardDocument = awardForm.getAwardDocument();
        
        awardDocument.getAward().getAwardReportTerms().remove(getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_AWARD_BASIC);
    }
    
    /**
     * 
     * This method gets the newAwardReportTerm object from the form, evaluates the rules and adds it
     * to the list of AwardReportTerm objects.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward addRecipient(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        AwardForm awardForm = (AwardForm) form;
        
        awardForm.setAwardReportTermPanelNumber(new Integer(getAwardReportTermIndex(request)).toString());
        
        AwardReportTermRecipient newAwardReportTermRecipient = awardForm.getNewAwardReportTermRecipient().
                                                get(getAwardReportTermIndex(request));        
        
        AwardDocument awardDocument = awardForm.getAwardDocument();
        if(getKualiRuleService().applyRules(new AddAwardReportTermRecipientEvent(Constants.EMPTY_STRING,
                awardDocument, newAwardReportTermRecipient))){
            awardDocument.getAward().getAwardReportTerms().get(getAwardReportTermIndex(request)).
                    getAwardReportTermRecipients().add(newAwardReportTermRecipient);
            awardForm.getNewAwardReportTermRecipient().set(getAwardReportTermIndex(request), 
                                                            new AwardReportTermRecipient());
                      
       }
        
        return mapping.findForward(Constants.MAPPING_AWARD_BASIC);
    }
    
    /**
     * 
     * This method deletes a recipient.
     * It reads the recipientsSize from the request; it deletes a AwardReportTerm object from the list
     * if the size is more than 1, otherwise it sets the contactTypeCode, rolodexId and numberOfCopies
     * to null.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deleteRecipient(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        AwardForm awardForm = (AwardForm) form;
        AwardDocument awardDocument = awardForm.getAwardDocument();
        awardDocument.getAward().getAwardReportTerms().get(getAwardReportTermIndex(request)).
            getAwardReportTermRecipients().remove(getLineToDelete(request));
        return mapping.findForward(Constants.MAPPING_AWARD_BASIC);
    }
    
    /**
     * 
     * This method reads the reportClass from the request.
     * @param request
     * @return
     */
    protected String getReportClass(HttpServletRequest request) {
        int reportClass = -1;
        String parameterName = (String) request.getAttribute(KNSConstants.METHOD_TO_CALL_ATTRIBUTE);
        if (StringUtils.isNotBlank(parameterName)) {
            String reportClassString = StringUtils.substringBetween(parameterName, ".reportClass", ".");
            reportClass = Integer.parseInt(reportClassString);
        }

        return new Integer(reportClass).toString();
    }
    
    /**
     * 
     * This method reads the reportClassCodeIndex from the request.
     * It is specified in the tag file and is used for showing the validation errors while adding
     * a new AwardReportTerm object.
     * @param request
     * @return
     */
    protected int getReportClassCodeIndex(HttpServletRequest request) {
        int reportClassIndex = -1;
        String parameterName = (String) request.getAttribute(KNSConstants.METHOD_TO_CALL_ATTRIBUTE);
        if (StringUtils.isNotBlank(parameterName)) {
            String reportClassIndexString = StringUtils.substringBetween(
                    parameterName, ".reportClassIndex", ".");
            reportClassIndex = Integer.parseInt(reportClassIndexString);
        }

        return reportClassIndex;
    }
    
    /**
     * 
     * This method reads the recipientIndex from the request.
     * It is specified in the tag file and is used for showing the validation errors while adding
     * a new AwardReportTerm object as a recipient. 
     * @param request
     * @return
     */
    protected int getAwardReportTermIndex(HttpServletRequest request) {
        int awardReportTermIndex = -1;
        String parameterName = (String) request.getAttribute(KNSConstants.METHOD_TO_CALL_ATTRIBUTE);
        if (StringUtils.isNotBlank(parameterName)) {
            String awardReportTermIndexString = StringUtils.substringBetween(parameterName, ".awardReportTerm", ".");
            awardReportTermIndex= Integer.parseInt(awardReportTermIndexString);
        }

        return awardReportTermIndex;
    }
    
}