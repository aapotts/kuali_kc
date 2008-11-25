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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.core.service.KualiRuleService;
import org.kuali.kra.award.bo.Award;
import org.kuali.kra.award.bo.AwardIndirectCostRate;
import org.kuali.kra.award.document.AwardDocument;
import org.kuali.kra.award.rule.event.AddAwardIndirectCostRateEvent;
import org.kuali.kra.award.web.struts.form.AwardForm;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;

/**
 * 
 * This class represents the Struts Action for Time & Money page(AwardTimeAndMoney.jsp)
 */
public class AwardTimeAndMoneyAction extends AwardAction {    
    
    /**
     * 
     * This method adds an awardIndirectCost Rate. It gets called upon add action on F&A Rates Sub-Panel of 
     * Rates Panel
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward addFandaRate(ActionMapping mapping, ActionForm form, HttpServletRequest request
            , HttpServletResponse response) throws Exception {
        AwardForm awardForm = (AwardForm) form;
        AwardIndirectCostRate newAwardIndirectCostRate = awardForm.getNewAwardIndirectCostRate();
        if(getKualiRuleService().applyRules(new AddAwardIndirectCostRateEvent(Constants.EMPTY_STRING, 
                awardForm.getAwardDocument(), newAwardIndirectCostRate))){
            addFandaRateToAward(awardForm.getAwardDocument().getAward(),newAwardIndirectCostRate);            
            awardForm.setNewAwardIndirectCostRate(new AwardIndirectCostRate());
        }
        return mapping.findForward(Constants.MAPPING_AWARD_BASIC);     
    }
    
    boolean addFandaRateToAward(Award award, AwardIndirectCostRate awardIndirectCostRate){
        return award.getAwardIndirectCostRate().add(awardIndirectCostRate);
    }
    
    /**
     * 
     * This method deletes an awardIndirectCostRate; It gets called upon delete action on 
     * F&A Rates Sub-Panel of Rates Panel
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deleteFandaRate(ActionMapping mapping, ActionForm form
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        AwardForm awardForm = (AwardForm) form;
        AwardDocument awardDocument = (AwardDocument) awardForm.getDocument();        
        deleteFandaRateFromAward(awardDocument.getAward(),getLineToDelete(request));
        return mapping.findForward(Constants.MAPPING_AWARD_BASIC);
    }
    
    boolean deleteFandaRateFromAward(Award award, int lineToDelete){
        award.getAwardIndirectCostRate().remove(lineToDelete);
        return true;
    }
    
    /**
     * 
     * This method recalculates the Unrecovered F&A; It gets called upon Recalculate action on 
     * F&A Rates sub-panel
     * of Rates Panel.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward recalculateFandARate(ActionMapping mapping, ActionForm form
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        return mapping.findForward(Constants.MAPPING_AWARD_BASIC);
    }
    
    /**
     * 
     * This method is a helper method to retrieve KualiRuleService.
     * @return
     */
    protected KualiRuleService getKualiRuleService() {
        return KraServiceLocator.getService(KualiRuleService.class);
    }
}
