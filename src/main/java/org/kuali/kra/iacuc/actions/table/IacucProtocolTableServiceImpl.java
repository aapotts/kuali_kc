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
package org.kuali.kra.iacuc.actions.table;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kuali.kra.common.committee.bo.CommonCommittee;
import org.kuali.kra.common.committee.bo.CommitteeSchedule;
import org.kuali.kra.common.committee.meeting.CommitteeScheduleMinute;
import org.kuali.kra.iacuc.IacucProtocol;
import org.kuali.kra.iacuc.actions.IacucProtocolAction;
import org.kuali.kra.iacuc.actions.IacucProtocolActionType;
import org.kuali.kra.iacuc.actions.submit.IacucProtocolSubmission;
import org.kuali.kra.protocol.actions.submit.ProtocolActionService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;

public class IacucProtocolTableServiceImpl implements IacucProtocolTableService {
    
    private ProtocolActionService protocolActionService;
    private BusinessObjectService businessObjectService;
    private DocumentService documentService;

    
    public DocumentService getDocumentService() {
        return documentService;
    }
    
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }
    
    
    
    public ProtocolActionService getProtocolActionService() {
        return protocolActionService;
    }
    
    public void setProtocolActionService(ProtocolActionService protocolActionService) {
        this.protocolActionService = protocolActionService;
    }


    
    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }
        
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
    
    
    
    @Override
    public CommitteeSchedule getNextScheduleForCommittee(CommonCommittee committee, CommitteeSchedule schedule) {
        CommitteeSchedule retVal = null;
        
        if((null != committee) && (null != schedule)) {
            List<CommitteeSchedule> schedules = committee.getCommitteeSchedules();
            if(null != schedules) {
                // sort will use the schedule's comparison method which orders by date
                Collections.sort(schedules);
                int indexOfSchedule = schedules.indexOf(schedule);
                // check if next schedule exists, and if so get it
                if( (indexOfSchedule != -1) && ((indexOfSchedule + 1) < schedules.size()) ) {
                    retVal = schedules.get(indexOfSchedule + 1);
                }
            }
        }
        
        return retVal;
    }
    
    
    
    // bump the submission to the next schedule (if any) for the same committee, and move the associated minutes (if any)
    private void bumpSubmissionToNextSchedule(IacucProtocolSubmission submission){
        CommitteeSchedule originalSchedule = submission.getCommitteeSchedule();
        CommitteeSchedule nextSchedule = getNextScheduleForCommittee(submission.getCommittee(), originalSchedule);
        if(null != nextSchedule) {
            // update submission's links to point to next schedule
            submission.setScheduleId(nextSchedule.getScheduleId());
            submission.setScheduleIdFk(nextSchedule.getId());
            submission.setCommitteeSchedule(nextSchedule);
        
            // the minutes if any for this protocol in the original schedule should now link to next schedule 
            Map<String, String> fieldValues = new HashMap<String, String>();
            fieldValues.put("protocolIdFk", submission.getProtocolId().toString());
            fieldValues.put("scheduleIdFk", originalSchedule.getId().toString());
            List<CommitteeScheduleMinute> minutes = (List<CommitteeScheduleMinute>) businessObjectService.findMatching(CommitteeScheduleMinute.class, fieldValues);
            if (!minutes.isEmpty()) {
                // update the schedule link (foreign key) for the minutes and save them
                for (CommitteeScheduleMinute minute : minutes) {
                    minute.setScheduleIdFk(submission.getScheduleIdFk());
                }
                getBusinessObjectService().save(minutes);
            }
        }
    }
    

    @Override
    public void tableProtocol(IacucProtocol protocol, IacucProtocolTableBean actionBean) throws Exception {
        IacucProtocolSubmission submission = (IacucProtocolSubmission) protocol.getProtocolSubmission();
        // bump to next schedule
        bumpSubmissionToNextSchedule(submission);
        // add a new protocol action for "tabled", update protocol status and save
        IacucProtocolAction protocolAction = new IacucProtocolAction(protocol, submission, IacucProtocolActionType.TABLED);
        protocolAction.setComments(actionBean.getComments());
        protocolAction.setActionDate(new Timestamp(actionBean.getActionDate().getTime()));
        protocol.getProtocolActions().add(protocolAction);
        getProtocolActionService().updateProtocolStatus(protocolAction, protocol);
        getDocumentService().saveDocument(protocol.getProtocolDocument());    
        
    }
    

}
