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
package org.kuali.kra.committee.web.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.kra.committee.bo.Committee;
import org.kuali.kra.committee.service.CommitteeScheduleService;
import org.kuali.kra.committee.web.struts.form.CommitteeForm;
import org.kuali.kra.committee.web.struts.form.schedule.ScheduleData;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.irb.service.ProtocolReferenceService;
import org.kuali.kra.irb.web.struts.form.ProtocolForm;

public class CommitteeScheduleAction extends CommitteeAction {
    
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(CommitteeScheduleAction.class);
    
/*  TODO check if required  
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionForward actionForward = super.execute(mapping, form, request, response);
        
        ((CommitteeForm)form).getCommitteeHelper().prepareView();
        
        return actionForward;
    }*/
    
    public ActionForward addEvent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        CommitteeForm committeeForm = (CommitteeForm) form;
        committeeForm.getScheduleData().printf();
        CommitteeScheduleService service  = KraServiceLocator.getService(CommitteeScheduleService.class);
        service.addSchedule(committeeForm.getScheduleData(), committeeForm.getCommitteeDocument().getCommittee());
        //TODO comment it: Changes style class selection, which will trigger selected type of recurrence
        committeeForm.getScheduleData().populateStyleClass();
        return mapping.findForward(Constants.MAPPING_BASIC );
    }
    
    public ActionForward deleteCommitteeSchedule(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        CommitteeForm committeeForm = (CommitteeForm) form;       
        int lineToDelete = getLineToDelete(request);
        committeeForm.getCommitteeDocument().getCommittee().getCommitteeSchedules().remove(lineToDelete);   
        return mapping.findForward(Constants.MAPPING_BASIC );
    }    
    
    public ActionForward filterCommitteeScheduleDates(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        CommitteeForm committeeForm = (CommitteeForm) form;        
        CommitteeScheduleService service  = KraServiceLocator.getService(CommitteeScheduleService.class); 
        service.filterCommitteeScheduleDates(committeeForm.getScheduleData(), committeeForm.getCommitteeDocument().getCommittee());
        return mapping.findForward(Constants.MAPPING_BASIC );
    }

    public ActionForward resetCommitteeScheduleDates(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        CommitteeForm committeeForm = (CommitteeForm) form;        
        CommitteeScheduleService service  = KraServiceLocator.getService(CommitteeScheduleService.class); 
        service.resetCommitteeScheduleDates(committeeForm.getCommitteeDocument().getCommittee());
        return mapping.findForward(Constants.MAPPING_BASIC );
    } 
    
    public ActionForward reload(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        CommitteeForm committeeForm = (CommitteeForm) form;
        //TODO comment it: Changes style class selection, which will trigger selected type of recurrence
        committeeForm.getScheduleData().populateStyleClass();
        return mapping.findForward(Constants.MAPPING_BASIC );
    }    
}
