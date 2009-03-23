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
package org.kuali.kra.committee.web.struts.form;

import org.kuali.core.bo.user.UniversalUser;
import org.kuali.core.util.GlobalVariables;
import org.kuali.kra.bo.ResearchArea;
import org.kuali.kra.committee.bo.Committee;
import org.kuali.kra.committee.document.authorization.CommitteeTask;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.infrastructure.TaskName;
import org.kuali.kra.irb.document.authorization.ProtocolTask;
import org.kuali.kra.service.TaskAuthorizationService;

/**
 * The CommitteeHelper corresponds to the Committee tab web page.
 */
public class CommitteeHelper {
    
    @SuppressWarnings("unused")
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(CommitteeHelper.class);
    
    private CommitteeForm committeeForm;
    private boolean modifyCommittee = false;
    private ResearchArea newResearchArea;
    
    public CommitteeHelper(CommitteeForm committeeForm) {
        this.committeeForm = committeeForm;
        setNewResearchArea(new ResearchArea());
    }
    
    public Committee getCommittee() {
        return committeeForm.getCommitteeDocument().getCommittee();
    }
    
    /**
     * This method is UI view hook.
     */
    public void prepareView() {
        modifyCommittee = canModifyCommittee();
    }
    
    public boolean canModifyCommittee() {
        CommitteeTask task = new CommitteeTask(TaskName.MODIFY_COMMITTEE, getCommittee());
        return getTaskAuthorizationService().isAuthorized(getUserName(), task);
    }

    public ResearchArea getNewResearchArea() {
        return newResearchArea;
    }

    public void setNewResearchArea(ResearchArea newResearchArea) {
        this.newResearchArea = newResearchArea;
    }
    
    protected TaskAuthorizationService getTaskAuthorizationService() {
        return KraServiceLocator.getService(TaskAuthorizationService.class);
    }

    /**
     * Get the userName of the user for the current session.
     * @return the current session's userName
     */
    protected String getUserName() {
         UniversalUser user = GlobalVariables.getUserSession().getUniversalUser();
         return user.getPersonUserIdentifier();
    }

    public boolean getModifyCommittee() {
        return modifyCommittee;
    }

    public void setModifyCommittee(boolean modifyCommittee) {
        this.modifyCommittee = modifyCommittee;
    }
}
