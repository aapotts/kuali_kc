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
package org.kuali.kra.meeting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.committee.bo.CommitteeSchedule;
import org.kuali.kra.committee.web.struts.form.schedule.Time12HrFmt;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.rules.ErrorReporter;
import org.kuali.rice.kns.service.BusinessObjectService;

public class MeetingDetailsRule {
    public static final String MSG1 = "hh:mm";

    public static final String MSG2 = "hh as 1-12 & mm as 0-59";

    public static final String MSG3 = "hh as 1-12";

    public static final String MSG4 = "mm as 0-59";

    public static final String COLON = ":";
    public static final String ALTERNATE_ENTRY_TYPE = "3";

    public static final String ID1 = "meetingHelper.committeeSchedule.viewTime.time";
    public static final String NEWOTHERPRESENT_PERSONNAME = "meetingHelper.newOtherPresentBean.attendance.personName";
    public static final String NEW_COMM_SCHD_MINUTE_PROTOCOL = "meetingHelper.newCommitteeScheduleMinute.protocolIdFk";
    public static final String NEW_COMM_SCHD_MINUTE_PROTOCOL_CONTINGENCY = "meetingHelper.newCommitteeScheduleMinute.protocolContingencyCode";
    private ErrorReporter errorReporter;

    /**
     * 
     * This method is to validate time/start time/end time to make sure format is ok and als end time >= start time
     * @param committeeSchedule
     * @return
     */
    public boolean validateMeetingDetails(CommitteeSchedule committeeSchedule) {

        boolean rulePassed = true;
        errorReporter = new ErrorReporter();
        String time = committeeSchedule.getViewStartTime().getTime();
        rulePassed &= processTime(time, ID1.replace("viewTime", "viewStartTime"));
        time = committeeSchedule.getViewEndTime().getTime();
        rulePassed &= processTime(time, ID1.replace("viewTime", "viewEndTime"));
        if (rulePassed) {
            rulePassed &= checkStartTimeBeforeEndTime(committeeSchedule.getViewStartTime(), committeeSchedule.getViewEndTime());
        }
        time = committeeSchedule.getViewTime().getTime();
        rulePassed &= processTime(time, ID1);
        return rulePassed;
    }

    /**
     * 
     * This method that new person/rolodex is selected, and the selected is not in member present or alternate for.
     * @param meetingHelper
     * @return
     */
    public boolean validateNewOther(MeetingHelper meetingHelper) {

        boolean rulePassed = true;
        errorReporter = new ErrorReporter();
        if (StringUtils.isBlank(meetingHelper.getNewOtherPresentBean().getAttendance().getPersonName())) {
            errorReporter.reportError(NEWOTHERPRESENT_PERSONNAME, KeyConstants.ERROR_EMPTY_PERSON);
            rulePassed = false;
        }
        else {
            for (MemberPresentBean memberPresentBean : meetingHelper.getMemberPresentBeans()) {
                if (isMemberPresent(memberPresentBean, meetingHelper.getNewOtherPresentBean())) {
                    errorReporter.reportError(NEWOTHERPRESENT_PERSONNAME, KeyConstants.ERROR_ADD_MEMBER_PRESENT, meetingHelper.getNewOtherPresentBean()
                            .getAttendance().getPersonName());
                    rulePassed = false;
                }

            }
        }
        return rulePassed;
    }

    /**
     * 
     * This method is to validate that the member absent is not an alternate for
     * @param memberPresentBeans
     * @param memberAbsentBean
     * @return
     */
    public boolean validateNotAlternateFor(List<MemberPresentBean> memberPresentBeans, MemberAbsentBean memberAbsentBean) {

        boolean rulePassed = true;
        errorReporter = new ErrorReporter();
        for (MemberPresentBean memberPresentBean : memberPresentBeans) {
            if (isAlternateFor(memberPresentBean, memberAbsentBean)) {
                errorReporter.reportError("meetingHelper.memberAbsentBean.attendance.personId",
                        KeyConstants.ERROR_PRESENT_MEMBER_ABSENT, memberAbsentBean.getAttendance().getPersonName());
                rulePassed = false;
            }

        }

        return rulePassed;
    }

    /**
     * 
     * This method is to validate that same person is only selected in one alternate for
     * @param memberPresentBeans
     * @return
     */
    public boolean validateDuplicateAlternateFor(List<MemberPresentBean> memberPresentBeans) {

        boolean rulePassed = true;
        errorReporter = new ErrorReporter();
        int i = 0;
        for (MemberPresentBean memberPresentBean : memberPresentBeans) {
            if (StringUtils.isNotBlank(memberPresentBean.getAttendance().getAlternateFor())) {
                int j = 0;
                for (MemberPresentBean memberPresentBean1 : memberPresentBeans) {
                    if (j > i
                            && StringUtils.isNotBlank(memberPresentBean1.getAttendance().getAlternateFor())
                            && memberPresentBean.getAttendance().getAlternateFor().equals(
                                    memberPresentBean1.getAttendance().getAlternateFor())) {
                        errorReporter.reportError("meetingHelper.memberPresentBeans[" + i + "].attendance.alternateFor",
                                KeyConstants.ERROR_DUPLICATE_ALTERNATE_FOR);
                        rulePassed = false;
                    }
                    j++;
                }
            }
            i++;
        }

        return rulePassed;
    }

    /**
     * 
     * This method is to validate new committee schedule minute. Make sure entry type of 'protocol' selection is ok.
     * if entry type is Protocol then 'protocol' is selected. and if protocol contingency code is entered, then
     * verify this code does exist.
     * @param committeeScheduleMinute
     * @return
     */
    public boolean validateProtocolInMinute(CommitteeScheduleMinute committeeScheduleMinute) {

        boolean rulePassed = true;
        errorReporter = new ErrorReporter();
        if (StringUtils.isNotBlank(committeeScheduleMinute.getMinuteEntryTypeCode())
                && committeeScheduleMinute.getMinuteEntryTypeCode().equals(ALTERNATE_ENTRY_TYPE)) {
            if (committeeScheduleMinute.getProtocolIdFk() == null) {
                errorReporter.reportError(NEW_COMM_SCHD_MINUTE_PROTOCOL, KeyConstants.ERROR_EMPTY_PROTOCOL);
                rulePassed = false;
            }
            if (StringUtils.isNotBlank(committeeScheduleMinute.getProtocolContingencyCode())) {
                Map fieldValues = new HashMap();
                fieldValues.put("protocolContingencyCode", committeeScheduleMinute.getProtocolContingencyCode());
                if (KraServiceLocator.getService(BusinessObjectService.class).findByPrimaryKey(ProtocolContingency.class,
                        fieldValues) == null) {
                    errorReporter.reportError(NEW_COMM_SCHD_MINUTE_PROTOCOL_CONTINGENCY, KeyConstants.ERROR_EMPTY_PROTOCOL_CONTINGENCY);
                    rulePassed = false;
                }
            }
        }
        else if (StringUtils.isNotBlank(committeeScheduleMinute.getMinuteEntryTypeCode())
                && !committeeScheduleMinute.getMinuteEntryTypeCode().equals(ALTERNATE_ENTRY_TYPE)
                && committeeScheduleMinute.getProtocolIdFk() != null) {
            errorReporter.reportError(NEW_COMM_SCHD_MINUTE_PROTOCOL, KeyConstants.ERROR_NON_EMPTY_PROTOCOL);
            rulePassed = false;

        }
        return rulePassed;
    }

    /*
     * check if the member in absent panel is selected as alternate for already
     */
    private boolean isAlternateFor(MemberPresentBean memberPresentBean, MemberAbsentBean memberAbsentBean) {
        boolean isPresent = false;

        if (StringUtils.isNotBlank(memberPresentBean.getAttendance().getAlternateFor())
                && StringUtils.isNotBlank(memberAbsentBean.getAttendance().getPersonId())
                // TODO check alternate for is not perfect because there is no idicator that alternatefor is person or rolodex
                // and we can't rule out personid=rolodexid
                && memberPresentBean.getAttendance().getAlternateFor().equals(memberAbsentBean.getAttendance().getPersonId())) {
            isPresent = true;
        }
        return isPresent;
    }

    /*
     * check if the selected person for 'other present' is a member.
     */
    private boolean isMemberPresent(MemberPresentBean memberPresentBean, OtherPresentBean otherPresentBean) {
        boolean isPresent = false;
        if (memberPresentBean.getAttendance().getNonEmployeeFlag() && otherPresentBean.getAttendance().getNonEmployeeFlag()
                && memberPresentBean.getAttendance().getPersonId().equals(otherPresentBean.getAttendance().getPersonId())) {
            isPresent = true;
        }
        else if (!memberPresentBean.getAttendance().getNonEmployeeFlag() && !otherPresentBean.getAttendance().getNonEmployeeFlag()
                && memberPresentBean.getAttendance().getPersonId().equals(otherPresentBean.getAttendance().getPersonId())) {
            isPresent = true;
        }
        else if (StringUtils.isNotBlank(memberPresentBean.getAttendance().getAlternateFor())
                && StringUtils.isNotBlank(otherPresentBean.getAttendance().getPersonId())
                // TODO check alternate for is not perfect because there is no idicator that alternatefor is person or rolodex
                // and we can't rule out personid=rolodexid
                && memberPresentBean.getAttendance().getAlternateFor().equals(otherPresentBean.getAttendance().getPersonId())) {
            isPresent = true;
        }
        // else if (StringUtils.isNotBlank(memberPresentBean.getAlternateFor()) &&
        // StringUtils.isBlank(otherPresentBean.getPersonId())
        // && memberPresentBean.getAlternateFor().equals(otherPresentBean.getRolodexId().toString())) {
        // isPresent = true;
        // }
        return isPresent;
    }

    /*
     * check 'time' format
     */
    private boolean processTime(String time, String id) {
        String prefix = "";
        if (id.contains("viewStartTime")) {
            prefix = "Start";
        }
        else if (id.contains("viewEndTime")) {
            prefix = "End";
        }
        boolean rulePassed = true;

        if (StringUtils.isBlank(time)) {
            rulePassed = createErrorReport(id, prefix, MSG1);
            return rulePassed;
        }

        String[] result = time.split(COLON);
        if (result.length != 2) {
            rulePassed = createErrorReport(id, prefix, MSG1);
            return rulePassed;
        }

        Integer hrs;
        Integer mins;

        try {
            hrs = new Integer(result[0]);
            mins = new Integer(result[1]);

            if (!(hrs >= 1 && hrs <= 12)) {
                rulePassed = createErrorReport(id, prefix, MSG3);
                return rulePassed;
            }


            if (!(mins >= 0 && mins <= 59)) {
                rulePassed = createErrorReport(id, prefix, MSG4);
                return rulePassed;
            }
        }
        catch (NumberFormatException e) {
            rulePassed = createErrorReport(id, prefix, MSG2);
        }
        return rulePassed;
    }

    /*
     * check end time >= start time
     */
    private boolean checkStartTimeBeforeEndTime(Time12HrFmt startTime, Time12HrFmt endTime) {

        boolean rulePassed = true;
        String[] startTimeSplit = startTime.getTime().split(COLON);
        String[] endTimeSplit = endTime.getTime().split(COLON);

        Integer startHrs;
        Integer startMins;
        Integer endHrs;
        Integer endMins;

        if (startTime.getMeridiem().equals("PM") && endTime.getMeridiem().equals("AM")) {
            errorReporter.reportError(ID1.replace("viewTime", "viewEndTime"),
                    KeyConstants.ERROR_COMMITTEESCHEDULE_ENDTIME_BEFORE_STARTTIME);
            rulePassed = false;
        }
        else if (startTime.getMeridiem().equals(endTime.getMeridiem())) {
            try {
                startHrs = new Integer(startTimeSplit[0]);
                startMins = new Integer(startTimeSplit[1]);
                endHrs = new Integer(endTimeSplit[0]);
                endMins = new Integer(endTimeSplit[1]);
                if ((startHrs != 12 && (startHrs > endHrs || endHrs == 12)) || (startHrs.equals(endHrs) && startMins > endMins)) {
                    errorReporter.reportError(ID1.replace("viewTime", "viewEndTime"),
                            KeyConstants.ERROR_COMMITTEESCHEDULE_ENDTIME_BEFORE_STARTTIME);
                    rulePassed = false;
                }
            }
            catch (NumberFormatException e) {
            }
        }
        return rulePassed;
    }

    private boolean createErrorReport(String id, String prefix, String msg) {
        errorReporter.reportError(id, KeyConstants.ERROR_COMMITTEESCHEDULE_VIEWTIME, prefix, msg);
        return false;
    }

}
