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
package org.kuali.kra.common.committee.print;

import org.kuali.kra.bo.KcPerson;
import org.kuali.kra.common.committee.bo.CommitteeSchedule;
import org.kuali.kra.iacuc.actions.submit.IacucProtocolSubmission;
import org.kuali.kra.protocol.personnel.ProtocolPerson;
import org.kuali.kra.protocol.personnel.ProtocolPersonRolodex;

import edu.mit.coeus.xml.iacuc.PersonType;
import edu.mit.coeus.xml.iacuc.ProtocolSubmissionType;
import edu.mit.coeus.xml.iacuc.ProtocolType.Submissions;
import edu.mit.coeus.xml.iacuc.ScheduleType;
import edu.mit.coeus.xml.iacuc.SubmissionDetailsType;

/**
 * This class has different helper methods to populate data for Person XML data.
 */
public interface PrintXmlUtilService {
    
    public void setPersonXml(KcPerson person, PersonType personType);
    public void setPersonXml(ProtocolPersonRolodex rolodex, PersonType personType);
    public void setPersonRolodexType(ProtocolPerson protocolPerson, PersonType personType);   
    
    public void setProtocolSubmissionAction(IacucProtocolSubmission protocolSubmission,
            SubmissionDetailsType protocolSubmissionDetail);
    public void setSubmissionCheckListinfo(org.kuali.kra.protocol.actions.submit.ProtocolSubmission protocolSubmission,
            SubmissionDetailsType protocolSubmissionDetail);
    public void setMinutes(CommitteeSchedule scheduleDetailsBean, ScheduleType schedule);
    public void setProcotolMinutes(CommitteeSchedule committeeSchedule, 
            org.kuali.kra.protocol.actions.submit.ProtocolSubmission protocolSubmission, ProtocolSubmissionType protocolSubmissionType);
    public void setProcotolSubmissionMinutes(CommitteeSchedule committeeSchedule,
            ProtocolSubmissionType protocolSubmission, Submissions submissionsType);
    
    public void setProtocolReviewMinutes(CommitteeSchedule committeeSchedule,
            org.kuali.kra.protocol.actions.submit.ProtocolSubmission protocolSubmission, Submissions submissionsType);
    
}
