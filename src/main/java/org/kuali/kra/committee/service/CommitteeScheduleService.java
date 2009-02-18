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
package org.kuali.kra.committee.service;

import java.text.ParseException;

import org.kuali.kra.committee.bo.Committee;
import org.kuali.kra.committee.bo.CommitteeSchedule;
import org.kuali.kra.committee.web.struts.form.schedule.ScheduleData;


public interface CommitteeScheduleService {
    
    public Boolean isCommitteeScheduleDeleteAllowed(CommitteeSchedule committeeSchedule);
    
    public void addSchedule(ScheduleData scheduleData, Committee committee) throws ParseException;
    
    public void deleteCommitteeSchedule(Committee committee, int lineNumber);
    
    public void filterCommitteeScheduleDates(ScheduleData scheduleData, Committee committee);

    public void resetCommitteeScheduleDates(Committee committee);
}
