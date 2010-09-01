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
package org.kuali.kra.committee.bo;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.kuali.kra.committee.web.struts.form.schedule.Time12HrFmt;
import org.kuali.kra.committee.web.struts.form.schedule.Time12HrFmt.MERIDIEM;

/**
 * This class is Template implementation of BoAttributeTestBase<T> class, to test CommitteeSchedule BO for toStringMapper.
 */

public class CommitteeScheduleTest extends BoAttributeTestBase<CommitteeSchedule> {
    
    private static final int ATTRIBUTE_COUNT = 14;
    
    private static final Date date = new Date(new java.util.Date().getTime());
    private static final Timestamp timestamp = new Timestamp(new java.util.Date().getTime());
        
    private static final String FIELD_ID = "id";
    private static final Long FIELD_ID_VALUE = 1L; 

    private static final String FIELD_SCHEDULEID = "scheduleId"; 
    private static final String FIELD_SCHEDULEID_VALUE = "1"; 

    private static final String FIELD_SCHEDULEDDATE = "scheduledDate";
    private static final Date FIELD_SCHEDULEDDATE_VALUE = date; 

    private static final String FIELD_PLACE = "place";
    private static final String FIELD_PLACE_VALUE = "Davis 103";

    private static final String FIELD_TIME = "time";
    private static Timestamp FIELD_TIME_VALUE;

    private static final String FIELD_PROTOCOLSUBDEADLINE = "protocolSubDeadline";
    private static final Date FIELD_PROTOCOLSUBDEADLINE_VALUE = date;

    private static final String FIELD_SCHEDULESTATUSCODE = "scheduleStatusCode";
    private static final Integer FIELD_SCHEDULESTATUSCODE_VALUE = 1;

    private static final String  FIELD_MEETINGDATE = "meetingDate";
    private static final Date FIELD_MEETINGDATE_VALUE = date;  

    private static final String FIELD_STARTTIME = "startTime";
    private static final Timestamp FIELD_STARTTIME_VALUE = timestamp; 
    
    private static final String FIELD_ENDTIME = "endTime";
    private static final Timestamp FIELD_ENDTIME_VALUE = timestamp; 

    private static final String FIELD_AGENDAPRODREVDATE = "agendaProdRevDate";
    private static final Date FIELD_AGENDAPRODREVDATE_VALUE = date;

    private static final String FIELD_MAXPROTOCOLS = "maxProtocols";
    private static final Integer FIELD_MAXPROTOCOLS_VALUE = 1;

    private static final String FIELD_COMMONTS = "comments";
    private static final String FIELD_COMMONTS_VALUE = "Some comment to test should go here";
    
    private static CommitteeSchedule cm = new CommitteeSchedule();

    /**
     * Constructs a CommitteeScheduleTest.java.
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public CommitteeScheduleTest() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {        
        super(ATTRIBUTE_COUNT, cm);
    }

    /**
     * @see org.kuali.kra.committee.bo.BoAttributeTestBase#getFieldMap()
     */
    @SuppressWarnings("unchecked")
    @Override
    protected Map getFieldMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(FIELD_ID, FIELD_ID_VALUE);
        map.put(FIELD_SCHEDULEID, FIELD_SCHEDULEID_VALUE);
        map.put(FIELD_SCHEDULEDDATE, FIELD_SCHEDULEDDATE_VALUE);
        map.put(FIELD_PLACE, FIELD_PLACE_VALUE);
        map.put(FIELD_TIME, FIELD_TIME_VALUE);
        map.put(FIELD_PROTOCOLSUBDEADLINE, FIELD_PROTOCOLSUBDEADLINE_VALUE);
        map.put(FIELD_SCHEDULESTATUSCODE, FIELD_SCHEDULESTATUSCODE_VALUE);
        map.put(FIELD_MEETINGDATE, FIELD_MEETINGDATE_VALUE);
        map.put(FIELD_STARTTIME, FIELD_STARTTIME_VALUE);
        map.put(FIELD_ENDTIME, FIELD_ENDTIME_VALUE);
        map.put(FIELD_AGENDAPRODREVDATE, FIELD_AGENDAPRODREVDATE_VALUE);
        map.put(FIELD_MAXPROTOCOLS, FIELD_MAXPROTOCOLS_VALUE);
        map.put(FIELD_COMMONTS, FIELD_COMMONTS_VALUE);
        return map;
    }

    /**
     * @see org.kuali.kra.committee.bo.BoAttributeTestBase#getToStringMapper()
     */
    @SuppressWarnings("unchecked")
    @Override
    protected Map<String, Object> getToStringMapper() {
        return getT().toStringMapper();
    }
    
    /**
     * @see org.kuali.kra.committee.bo.BoAttributeTestBase#boPrerequisite()
     */
    @Override
    protected void boPrerequisite(){
        super.boPrerequisite();
        java.util.Date dt = new java.util.Date(0);
        Time12HrFmt time12HrFmt = new Time12HrFmt("10:30",MERIDIEM.AM);
        dt = DateUtils.round(dt, Calendar.DAY_OF_MONTH);
        dt = DateUtils.addMinutes(dt, time12HrFmt.findMinutes());
        FIELD_TIME_VALUE = new java.sql.Timestamp(dt.getTime());
    }
    
    /**
     * @see org.kuali.kra.committee.bo.BoAttributeTestBase#boPostrequisite()
     */
    @Override
    protected void boPostrequisite() {
        super.boPostrequisite();
        cm.setViewTime(new Time12HrFmt("10:30",MERIDIEM.AM));
    }
}
