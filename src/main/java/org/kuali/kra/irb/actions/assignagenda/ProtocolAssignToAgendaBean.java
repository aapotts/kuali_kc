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
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.irb.Protocol;
import org.kuali.kra.irb.actions.ActionHelper;

/**
 * This class is really just a "form" for assigning a protocol to an agenda.
 */
@SuppressWarnings("serial")
public class ProtocolAssignToAgendaBean implements Serializable {

    private ActionHelper actionHelper;

    private String committeeId = "";
    private String committeName = "";
    private String scheduleDate = "";
    private boolean protocolAssigned = false;
    private String comments = "";
    
    private transient ProtocolAssignToAgendaService agendaService;

    /**
     * 
     * Constructs a ProtocolAssignToAgendaBean.java.
     * 
     * @param actionHelper an ActionHelper object
     */
    public ProtocolAssignToAgendaBean(ActionHelper actionHelper) {
        this.actionHelper = actionHelper;

    }


    public void setCommitteeId(String committeeId) {
        this.committeeId = committeeId;
    }


    public void setCommitteName(String committeName) {
        this.committeName = committeName;
    }


    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }


    public void setProtocolAssigned(boolean protocolAssigned) {
        this.protocolAssigned = protocolAssigned;
    }


    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * 
     * This method initializes the values of the bean.
     */
    public void init() {
        //try {
            if (getProtocol() != null && getProtocol().getProtocolNumber() != null) {
                String committeeId = getProtocolAssigntoAgendaService().getAssignedCommitteeId(getProtocol());
                if (committeeId != null) {
                    this.committeeId = committeeId;
                    this.committeName = getProtocolAssigntoAgendaService().getAssignedCommitteeName(getProtocol());

                    this.comments = getProtocolAssigntoAgendaService().getAssignToAgendaComments(getProtocol());

                    this.protocolAssigned = getProtocolAssigntoAgendaService().isAssignedToAgenda(getProtocol());

                    this.scheduleDate = getProtocolAssigntoAgendaService().getAssignedScheduleDate(getProtocol());
                }
            }
        //} catch (Exception e) {
            //e.printStackTrace();
            // errors shouldn't happen in real life, but the test cases can throw errors because data isn't complete
            // e.printStackTrace();
        //}
        // the else condition means we can't create this bean
    }


    private ProtocolAssignToAgendaService getProtocolAssigntoAgendaService() {
        if (this.agendaService == null){
            this.agendaService = KraServiceLocator.getService(ProtocolAssignToAgendaService.class);
        }
        return this.agendaService;
    }

    private Protocol getProtocol() {
        return actionHelper.getProtocolForm().getProtocolDocument().getProtocol();
    }


    public ActionHelper getActionHelper() {
        return actionHelper;
    }


    public String getCommitteeId() {
        return committeeId;
        // return "12345";
    }


    public String getCommitteName() {
        return committeName;
        // return "really awesome committee";
    }


    public String getScheduleDate() {
        return scheduleDate;
        // return new Date();
    }


    public boolean isProtocolAssigned() {
        return protocolAssigned;
        // return true;
    }


    public String getComments() {
        return comments;
        // return "Comments can be cool \n \n \n \n and so are text boxes";
    }

    /**
     * Prepare the Assign to Committee and Schedule for rendering with JSP.
     */
    public void prepareView() {
        /*
         * The Assign to Agenda has to work with and without JavaScript. When JavaScript is enabled, the newly selected committee
         * and schedule are what we want to continue to display. When JavaScript is disabled, we have to change the schedule dates
         * that we display if the committee has changed.
         */
        if (actionHelper.getProtocolForm().isJavaScriptEnabled()) {
            // committeeId = newCommitteeId;
            // scheduleId = newScheduleId;
        } else {
            // if (!StringUtils.equals(committeeId, newCommitteeId)) {
            // committeeId = newCommitteeId;
            // scheduleId = "";
            // }
            // else if (!StringUtils.equals(scheduleId, newScheduleId)) {
            // scheduleId = newScheduleId;
            // }
        }
    }
}