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
package org.kuali.kra.irb.actions.assignagenda;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.irb.Protocol;
import org.kuali.kra.irb.actions.ActionHelper;
import org.kuali.kra.irb.actions.assigncmtsched.*;

/**
 * This class is really just a "form" for assigning a protocol
 * to a committee and schedule.
 */
@SuppressWarnings("serial")
public class ProtocolAssignToAgendaBean implements Serializable{
    
    private ActionHelper actionHelper;
    
    private String committeeId = "";
    private String committeName = "";
    private Date scheduleDate = null;
    private boolean protocolAssigned = false;
    private String comments = "";
    
    public ProtocolAssignToAgendaBean(ActionHelper actionHelper) {
        System.err.println("Made a ProtocolAssignToAgendaBean.");
        this.actionHelper = actionHelper;
        //init();
        
    }
    
    
    public void init() {
        System.err.println("Start Init");
        String committeeId = getProtocolAssigntoAgendaService().getAssignedCommitteeId(getProtocol());
        System.err.println("tried to set committee id");
        if (committeeId != null) {
            this.committeeId = committeeId;
            
            //String scheduleId = getProtocolAssigntoAgendaService().getAssignedScheduleId(getProtocol());
            //if (scheduleId != null) {
                
            //}
            
            this.scheduleDate = getProtocolAssigntoAgendaService().getAssignedScheduleDate(getProtocol());
        }
    }
    
    
    private ProtocolAssignToAgendaService getProtocolAssigntoAgendaService() {
        return KraServiceLocator.getService(ProtocolAssignToAgendaService.class);
    }

    private Protocol getProtocol() {
        return actionHelper.getProtocolForm().getProtocolDocument().getProtocol();
    }


    public ActionHelper getActionHelper() {
        return actionHelper;
    }


    public String getCommitteeId() {
        return committeeId;
        //return "12345";
    }


    public String getCommitteName() {
        return committeName;
        //return "really awesome committee";
    }


    public Date getScheduleDate() {
        return scheduleDate;
        //return new Date();
    }


    public boolean isProtocolAssigned() {
        return protocolAssigned;
        //return true;
    }


    public String getComments() {
        return comments;
        //return "Comments can be cool \n \n \n \n and so are text boxes";
    }
    
    /**
     * Prepare the Assign to Committee and Schedule for rendering with JSP.
     */
    public void prepareView() {
        /*
         * The Assign to Agenda has to work with and without JavaScript.
         * When JavaScript is enabled, the newly selected committee and schedule
         * are what we want to continue to display.  When JavaScript is disabled,
         * we have to change the schedule dates that we display if the committee
         * has changed.
         */
        if (actionHelper.getProtocolForm().isJavaScriptEnabled()) {
           // committeeId = newCommitteeId;
            //scheduleId = newScheduleId;
        } 
        else {
          //  if (!StringUtils.equals(committeeId, newCommitteeId)) {
              //  committeeId = newCommitteeId;
                //scheduleId = "";
            //}
            //else if (!StringUtils.equals(scheduleId, newScheduleId)) {
                //scheduleId = newScheduleId;
            //}
        }
    }
}