/*
 * Copyright 2006-2008 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kra.committee.web.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.kra.bo.ResearchArea;
import org.kuali.kra.committee.bo.Committee;
import org.kuali.kra.committee.document.CommitteeDocument;
import org.kuali.kra.committee.document.authorization.CommitteeTask;
import org.kuali.kra.committee.rule.event.AddCommitteeResearchAreaEvent;
import org.kuali.kra.committee.service.CommitteeService;
import org.kuali.kra.committee.web.struts.form.CommitteeForm;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.infrastructure.TaskName;
import org.kuali.rice.kns.util.KNSConstants;

/**
 * The CommitteeCommitteeAction corresponds to the Committee tab (web page).  It is
 * responsible for handling all user requests from that tab (web page).
 */
public class CommitteeCommitteeAction extends CommitteeAction {
    
    private static final String COMMITTEE_ID = "committeeId";

    @SuppressWarnings("unused")
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(CommitteeCommitteeAction.class);
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionForward actionForward = super.execute(mapping, form, request, response);
        // Following is for committee lookup - edit committee 
        String commandParam = request.getParameter(KNSConstants.PARAMETER_COMMAND);
        if (StringUtils.isNotBlank(commandParam) && commandParam.equals("initiate") && StringUtils.isNotBlank(request.getParameter(COMMITTEE_ID))) {
            ((CommitteeForm)form).getCommitteeDocument().setCommittee(getCommitteeService().getCommitteeById(request.getParameter(COMMITTEE_ID)));
        }

        ((CommitteeForm) form).getCommitteeHelper().prepareView();
        ((CommitteeForm)form).getMembershipHelper().prepareView();
        
        return actionForward;
    }
    
    /**
     * Add a Research Area to a Committee.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward addResearchArea(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
        
        CommitteeForm committeeForm = (CommitteeForm) form;
        CommitteeDocument committeeDocument = committeeForm.getCommitteeDocument();
        Committee committee = committeeDocument.getCommittee();
        
        CommitteeTask task = new CommitteeTask(TaskName.MODIFY_COMMITTEE, committee);
        if (isAuthorized(task)) {
            ResearchArea newResearchArea = committeeForm.getCommitteeHelper().getNewResearchArea();
            boolean rulesPassed = applyRules(new AddCommitteeResearchAreaEvent(committeeDocument, newResearchArea.getResearchAreaCode()));
            if (rulesPassed) {
                getCommitteeService().addResearchArea(committee, newResearchArea.getResearchAreaCode());
                committeeForm.getCommitteeHelper().setNewResearchArea(new ResearchArea());
            }
        }
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Delete a Research Area from a Committee.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deleteResearchArea(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
        
        CommitteeForm committeeForm = (CommitteeForm) form;
        CommitteeDocument committeeDocument = committeeForm.getCommitteeDocument();
        Committee committee = committeeDocument.getCommittee();
        
        CommitteeTask task = new CommitteeTask(TaskName.MODIFY_COMMITTEE, committee);
        if (isAuthorized(task)) {   
            committee.getCommitteeResearchAreas().remove(getLineToDelete(request));
        }
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    private CommitteeService getCommitteeService() {
        return (CommitteeService) KraServiceLocator.getService(CommitteeService.class);
    }

}
