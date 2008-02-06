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
package org.kuali.kra.proposaldevelopment.web.struts.action;

import static org.kuali.kra.infrastructure.KraServiceLocator.getService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.core.service.KualiRuleService;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.proposaldevelopment.bo.ProposalSpecialReview;
import org.kuali.kra.proposaldevelopment.rule.event.AddProposalSpecialReviewEvent;
import org.kuali.kra.proposaldevelopment.web.struts.form.ProposalDevelopmentForm;

public class ProposalDevelopmentSpecialReviewAction extends ProposalDevelopmentAction {
    private static final Log LOG = LogFactory.getLog(ProposalDevelopmentSpecialReviewAction.class);
    public ActionForward addSpecialReview(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProposalDevelopmentForm proposalDevelopmentForm = (ProposalDevelopmentForm) form;
        ProposalSpecialReview newProposalSpecialReview = proposalDevelopmentForm.getNewPropSpecialReview();
        if(getKualiRuleService().applyRules(new AddProposalSpecialReviewEvent(Constants.EMPTY_STRING, proposalDevelopmentForm.getProposalDevelopmentDocument(), newProposalSpecialReview))){
            newProposalSpecialReview.setSpecialReviewNumber(proposalDevelopmentForm.getProposalDevelopmentDocument().getDocumentNextValue(Constants.PROPOSAL_SPECIALREVIEW_NUMBER));
            proposalDevelopmentForm.getProposalDevelopmentDocument().getPropSpecialReviews().add(newProposalSpecialReview);
            proposalDevelopmentForm.setNewPropSpecialReview(new ProposalSpecialReview());
        }
        return mapping.findForward("basic");
    }
    public ActionForward deleteSpecialReview(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProposalDevelopmentForm proposalDevelopmentForm = (ProposalDevelopmentForm) form;
        proposalDevelopmentForm.getProposalDevelopmentDocument().getPropSpecialReviews().remove(getLineToDelete(request));
        return mapping.findForward("basic");
    }

    // TODO : move this method up?
    private KualiRuleService getKualiRuleService() {
        return getService(KualiRuleService.class);
    }

}
