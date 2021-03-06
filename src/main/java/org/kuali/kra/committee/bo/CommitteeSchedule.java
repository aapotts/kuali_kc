/*
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.kra.committee.bo;

import org.kuali.kra.common.committee.bo.CommitteeScheduleBase;
import org.kuali.kra.irb.actions.submit.ProtocolSubmission;
import org.kuali.kra.meeting.CommitteeScheduleMinute;

/**
 * This is BO class to support CommitteeScheulde. It has three transient field to support UI.
 */
public class CommitteeSchedule extends CommitteeScheduleBase<CommitteeSchedule, Committee, ProtocolSubmission, CommitteeScheduleMinute> { 
    
    private static final long serialVersionUID = -360139608123017188L;
    
    private Committee committee;


//    public static final Long DEFAULT_SCHEDULE_ID = 9999999999L;
//    
//    private Time12HrFmt viewTime;
//    
//    private boolean filter = true;
//    private boolean delete = false;
//    private transient boolean selected = false;
//    private Long id; 
//    private String scheduleId; 
//    private Date scheduledDate;
//    private String place;
//    private Timestamp time;
//
//    private Date protocolSubDeadline;
//    private Integer scheduleStatusCode;
//    private Date meetingDate;
//    private Timestamp startTime;
//    private Timestamp endTime;
//    private Date agendaProdRevDate;
//    private Integer maxProtocols;
//    private String comments; 
//    private boolean availableToReviewers;
//	
//// TODO : recursive reference    
//	private Committee committee; 
//    private ScheduleStatus scheduleStatus;
//    
//    //TODO revisit required during meeting management to map Protocol
//    @SkipVersioning
//    private List<Protocol> protocols;
//    private Time12HrFmt viewStartTime;
//    private Time12HrFmt viewEndTime;
//
//    // Following are related to meeting data.  They will not be serialized to doc content.
//    // So, keep the "transient". Also, they are not versioned with version service.   
//    // Merging these data from old version committee to the new version committee at the time of "approval" of new version committee.
//    private transient List<CommitteeScheduleAttendance> committeeScheduleAttendances;        
//    private transient List<CommitteeScheduleMinute> committeeScheduleMinutes;  
//    private transient List<CommitteeScheduleAttachments> committeeScheduleAttachments;
//    @SkipVersioning
//    private transient List<ProtocolSubmission> protocolSubmissions;        
//    private transient List<CommScheduleActItem>  commScheduleActItems;
//    private transient List<CommScheduleMinuteDoc> minuteDocs;        
//    private transient List<ScheduleAgenda> scheduleAgendas;        

    
//    public CommitteeSchedule() { 
//        setCommitteeScheduleAttendances(new ArrayList<CommitteeScheduleAttendance>()); 
//        setCommScheduleActItems(new ArrayList<CommScheduleActItem>()); 
//        setProtocolSubmissions(new ArrayList<ProtocolSubmission>()); 
//        setCommitteeScheduleMinutes(new ArrayList<CommitteeScheduleMinute>()); 
//        setMinuteDocs(new ArrayList<CommScheduleMinuteDoc>()); 
//        setScheduleAgendas(new ArrayList<ScheduleAgenda>()); 
//        setCommitteeScheduleAttachments(new ArrayList<CommitteeScheduleAttachments>());
//	} 
//	
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }	
//	
//	public String getScheduleId() {
//		return scheduleId;
//	}
//
//	public void setScheduleId(String scheduleId) {
//		this.scheduleId = scheduleId;
//	}
//
//	public Date getScheduledDate() {
//		return scheduledDate;
//	}
//
//	public void setScheduledDate(Date scheduledDate) {
//		this.scheduledDate = scheduledDate;
//	}
//
//	public String getPlace() {
//		return place;
//	}
//
//	public void setPlace(String place) {
//		this.place = place;
//	}
//
//	/**
//	 * This method is BO persistent accessor method, which adds Time to Date on each call. 
//	 * In support to UI.
//	 * @return
//	 */
//	public Timestamp getTime() {
//	    java.util.Date dt = new java.util.Date(this.time.getTime());
//	    dt = DateUtils.round(dt, Calendar.DAY_OF_MONTH);
//	    if (viewTime != null) {
//            dt = new java.util.Date(0); // 12/31/1969 19:00:00
//            dt = DateUtils.round(dt, Calendar.DAY_OF_MONTH);
//	        dt = DateUtils.addMinutes(dt, viewTime.findMinutes()); // to set it to 1970-01-01
//            //dt = DateUtils.addMinutes(dt, viewTime.findMinutes());
//            //dt = DateUtils.addMinutes(dt, getViewTime().findMinutes());
//	        this.time = new Timestamp(dt.getTime());
//	    }
//	    return time;
//	}
//
//	public void setTime(Timestamp time) {
//		this.time = time;
//	}
//	
//	public Timestamp getActualTime() {
//	    return time;
//	}
//
//	public Date getProtocolSubDeadline() {
//		return protocolSubDeadline;
//	}
//
//	public void setProtocolSubDeadline(Date protocolSubDeadline) {
//		this.protocolSubDeadline = protocolSubDeadline;
//	}
//
//	public Integer getScheduleStatusCode() {
//		return scheduleStatusCode;
//	}
//
//	public void setScheduleStatusCode(Integer scheduleStatusCode) {
//		this.scheduleStatusCode = scheduleStatusCode;
//	}
//
//	public Date getMeetingDate() {
//		return meetingDate;
//	}
//
//	public void setMeetingDate(Date meetingDate) {
//		this.meetingDate = meetingDate;
//	}
//
//	public Timestamp getStartTime() {
//        if (startTime == null || startTime.getTime() == 0) {
//            java.util.Date dt = new java.util.Date(0);
//            dt = DateUtils.round(dt, Calendar.DAY_OF_MONTH);
//            if (viewStartTime != null) {
//                dt = DateUtils.addMinutes(dt, viewStartTime.findMinutes());
//                // dt = DateUtils.addMinutes(dt, getViewTime().findMinutes());
//            }
//            this.startTime = new Timestamp(dt.getTime());
//        }
//
//        return startTime;
//	}
//
//	public void setStartTime(Timestamp startTime) {
//		this.startTime = startTime;
//	}
//
//	public Timestamp getEndTime() {
//        if (endTime == null || endTime.getTime() == 0) {
//            java.util.Date dt = new java.util.Date(0); // set to 1969/12/31 19:00 ?
//            dt = DateUtils.round(dt, Calendar.DAY_OF_MONTH); // force it to 1970-01-01
//            if (viewEndTime != null) {
//                dt = DateUtils.addMinutes(dt, viewEndTime.findMinutes());
//                // dt = DateUtils.addMinutes(dt, getViewTime().findMinutes());
//            } 
//            this.endTime = new Timestamp(dt.getTime());
//        }
//        return endTime;
//	}
//
//	public void setEndTime(Timestamp endTime) {
//		this.endTime = endTime;
//	}
//
//	public Date getAgendaProdRevDate() {
//		return agendaProdRevDate;
//	}
//
//	public void setAgendaProdRevDate(Date agendaProdRevDate) {
//		this.agendaProdRevDate = agendaProdRevDate;
//	}
//
//	public Integer getMaxProtocols() {
//        if (maxProtocols == null) {
//            maxProtocols = committee.getMaxProtocols();
//        }
//		return maxProtocols;
//	}
//
//	public void setMaxProtocols(Integer maxProtocols) {
//	    if (maxProtocols == null) {
//	        maxProtocols = 0;
//	    }
//		this.maxProtocols = maxProtocols;
//	}
//
//	public String getComments() {
//		return comments;
//	}
//
//	public void setComments(String comments) {
//		this.comments = comments;
//	}
//
//	public boolean isAvailableToReviewers() {
//        return availableToReviewers;
//    }
//
//    public void setAvailableToReviewers(boolean availableToReviewers) {
//        this.availableToReviewers = availableToReviewers;
//    }

    
    public Committee getParentCommittee() {
        return this.getCommittee();
    }
    
    public Committee getCommittee() {
        if (committee == null && getCommitteeIdFk() == null) {
            committee = new Committee();
        }
        return committee;
    }
    
	public void setCommittee(Committee committee) {
		this.committee = committee;
	}
	
	
//    public ScheduleStatus getScheduleStatus() {
//        return scheduleStatus;
//    }
//
//    public void setScheduleStatus(ScheduleStatus scheduleStatus) {
//        this.scheduleStatus = scheduleStatus;
//    }	
//	
//    public void setDayOfWeek(String dayOfWeek){
//        //Do nothing, struts needs it on refresh
//    }
//    
//    /**
//     * This UI support method to find day Of week from BO's persistent field scheduledDate.
//     * @return
//     */
//    public String getDayOfWeek() {
//        Calendar cl = new GregorianCalendar();
//        cl.setTime(scheduledDate);
//        DayOfWeek dayOfWeek = null;        
//        switch (cl.get(Calendar.DAY_OF_WEEK)) {
//            case Calendar.SUNDAY:
//                dayOfWeek = DayOfWeek.Sunday;
//                break;
//            case Calendar.MONDAY:
//                dayOfWeek = DayOfWeek.Monday;
//                break;
//            case Calendar.TUESDAY:
//                dayOfWeek = DayOfWeek.Tuesday;
//                break;
//            case Calendar.WEDNESDAY:
//                dayOfWeek = DayOfWeek.Wednesday;
//                break;
//            case Calendar.THURSDAY:
//                dayOfWeek = DayOfWeek.Thursday;
//                break;
//            case Calendar.FRIDAY:
//                dayOfWeek = DayOfWeek.Friday;
//                break;
//            case Calendar.SATURDAY:
//                dayOfWeek = DayOfWeek.Saturday;
//                break;
//        }
//        return dayOfWeek.name().toUpperCase();
//    }
//    
//    public void setFilter(boolean filter) {
//        this.filter = filter;
//    }
//
//    public boolean getFilter() {
//        return filter;
//    }    
//    
//    public Time12HrFmt getViewTime() {
//        if(null == this.viewTime)
//            this.viewTime = new Time12HrFmt(time);
//        return viewTime;
//    }
//
//    public void setViewTime(Time12HrFmt viewTime) {
//        this.viewTime = viewTime;
//    }
//    
//    public boolean getDelete() {
//        return delete;
//    }
//
//    public void setDelete(boolean delete) {
//        this.delete = delete;
//    }        
//    
//    public List<Protocol> getProtocols() {
//        return protocols;
//    }
//
//    public void setProtocols(List<Protocol> protocols) {
//        this.protocols = protocols;
//    }
//    
//    public List<CommitteeScheduleAttendance> getCommitteeScheduleAttendances() {
//        return committeeScheduleAttendances;
//    }
//
//    public void setCommitteeScheduleAttendances(List<CommitteeScheduleAttendance> committeeScheduleAttendances) {
//        this.committeeScheduleAttendances = committeeScheduleAttendances;
//    }
//
//    public boolean isSelected() {
//        return selected;
//    }
//
//    public void setSelected(boolean selected) {
//        this.selected = selected;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (obj.getClass() != this.getClass()) {
//            return false;
//        }
//        CommitteeSchedule committeeSchedule = (CommitteeSchedule) obj;
//        if (this.getId() != null && this.getId().equals(committeeSchedule.getId())) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    @Override
//    public int hashCode() {
//        final int PRIME = 31;
//        int result = 1;
//        result = PRIME * result + (this.getId() == null ? 0 : this.getId().hashCode());
//        return result;     
//    }
//    
//    /**
//     * Compares the schedule dates for two instances of <code>CommitteeSchedule</code>
//     * in order to enforce an ordering.
//     * 
//     * @param other The CommitteeSchedule to be compared.
//     * @return the result of comparing this <code>scheduledDate</code> to the other <code>scheduledDate</code>
//     */
//    public int compareTo(CommitteeSchedule other) {
//        int compareResult;
//        
//        if (getScheduledDate() == null) {
//            if (other.getScheduledDate() == null) {
//                compareResult = 0;                
//            } else {
//                compareResult = -1;
//            }
//        }  else {
//            if (other.getScheduledDate() == null) {
//                compareResult = 1;
//            } else {
//                compareResult = getScheduledDate().compareTo(other.getScheduledDate());
//            }
//        }
//        return compareResult;
//    }
//    
//    public void resetPersistenceState() {
//        setId(null);
//    }
//
//    public List<ProtocolSubmission> getProtocolSubmissions() {
//        return protocolSubmissions;
//    }
//
//    public List<ProtocolSubmission> getLatestProtocolSubmissions() {
//        TreeMap<String, ProtocolSubmission> latestSubmissions = new TreeMap<String, ProtocolSubmission>();
//        List<ProtocolSubmission> returnList = new ArrayList<ProtocolSubmission>();
//        for (ProtocolSubmission submission : protocolSubmissions) {
//            // gonna do something a little hacktacular here... in some cases, protocol and/or protocol number might not be set.
//            // in that case, go ahead and pass submissions on to caller
//            if (submission.getProtocol() == null || StringUtils.isEmpty(submission.getProtocol().getProtocolNumber())) {
//                returnList.add(submission);
//            } else {
//                String key = submission.getProtocol().getProtocolNumber();
//                if (submission.getProtocol().isActive()) {
//                    ProtocolSubmission existingSubmission = latestSubmissions.get(key);
//                    if (existingSubmission == null) {
//                        latestSubmissions.put(key, submission);
//                    } else {
//                        int newInt = submission.getSequenceNumber().intValue();
//                        int existInt = existingSubmission.getSequenceNumber().intValue();
//                        int newSubNum = submission.getSubmissionNumber().intValue();
//                        int existSubNum = existingSubmission.getSubmissionNumber().intValue();
//                        if ((newInt > existInt) || ((newInt == existInt) && (newSubNum > existSubNum))){
//                            latestSubmissions.put(key, submission);
//                        }
//                    }
//                }
//            }
//        }
//        returnList.addAll(latestSubmissions.values());
//        return returnList;
//    }
//
//    public void setProtocolSubmissions(List<ProtocolSubmission> protocolSubmissions) {
//        this.protocolSubmissions = protocolSubmissions;
//    }
//    public Time12HrFmt getViewStartTime() {
//        if (null == this.viewStartTime) {
//            this.viewStartTime = new Time12HrFmt(startTime);
//        }
//        return viewStartTime;
//    }
//
//    public void setViewStartTime(Time12HrFmt viewStartTime) {
//        this.viewStartTime = viewStartTime;
//    }
//
//    public Time12HrFmt getViewEndTime() {
//        if (null == this.viewEndTime) {
//            this.viewEndTime = new Time12HrFmt(endTime);
//        }
//        return viewEndTime;
//    }
//
//    public void setViewEndTime(Time12HrFmt viewEndTime) {
//        this.viewEndTime = viewEndTime;
//    }
//
//    public List<CommScheduleActItem> getCommScheduleActItems() {
//        return commScheduleActItems;
//    }
//
//    public void setCommScheduleActItems(List<CommScheduleActItem> commScheduleActItems) {
//        this.commScheduleActItems = commScheduleActItems;
//    }
//
//    public List<CommitteeScheduleMinute> getCommitteeScheduleMinutes() {
//        return committeeScheduleMinutes;
//    }
//
//    public void setCommitteeScheduleMinutes(List<CommitteeScheduleMinute> committeeScheduleMinutes) {
//        this.committeeScheduleMinutes = committeeScheduleMinutes;
//    }
//
//    public List<CommitteeScheduleAttachments> getCommitteeScheduleAttachments() {
//        return committeeScheduleAttachments;
//    }
//
//    public void setCommitteeScheduleAttachments(List<CommitteeScheduleAttachments> committeeScheduleAttachments) {
//        this.committeeScheduleAttachments = committeeScheduleAttachments;
//    }
//
//    public List<CommScheduleMinuteDoc> getMinuteDocs() {
//        return minuteDocs;
//    }
//
//    public void setMinuteDocs(List<CommScheduleMinuteDoc> minuteDocs) {
//        this.minuteDocs = minuteDocs;
//    }
//
//    public List<ScheduleAgenda> getScheduleAgendas() {
//        return scheduleAgendas;
//    }
//
//    public void setScheduleAgendas(List<ScheduleAgenda> scheduleAgendas) {
//        this.scheduleAgendas = scheduleAgendas;
//    }
//
//    //Permissionable interface
//    public String getDocumentKey() {
//        return Permissionable.COMMITTEE_SCHEDULE_KEY;
//    }
//
//    public String getDocumentNumberForPermission() {
//        return getScheduleId();
//    }
//
//    public String getDocumentRoleTypeCode() {
//        return null;
//    }
//
//    public String getLeadUnitNumber() {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    public String getNamespace() {
//        return getCommittee().getNamespace();
//    }
//
//    public List<String> getRoleNames() {
//        List<String> roleNames = new ArrayList<String>();
//        roleNames.add(RoleConstants.IRB_ADMINISTRATOR);
//        roleNames.add(RoleConstants.IRB_REVIEWER);
//        return roleNames;
//    }
//
//    public void populateAdditionalQualifiedRoleAttributes(Map<String, String> qualifiedRoleAttributes) {
//        qualifiedRoleAttributes.put(KcKimAttributes.COMMITTEE, getCommittee().getCommitteeId());
//        qualifiedRoleAttributes.put(KcKimAttributes.COMMITTEESCHEDULE, this.getScheduleId());
//    }
//    
//    
//    /**
//     * This method returns true if the given personId has a membership in the schedule's parent committee that is active 
//     * for the schedule date, and false otherwise. Also returns false if the personId parameter or the 
//     * parent committee of the schedule is null. 
//     * @param personId
//     * @return
//     */
//    public boolean isActiveFor(String personId) {
//        boolean retVal = false;
//        Committee parentCommittee = this.getCommittee();
//        if(parentCommittee != null){
//            CommitteeMembership member = parentCommittee.getCommitteeMembershipFor(personId);
//            if(member != null) {
//                retVal = member.isActive(this.scheduledDate);
//            }
//        }
//        return retVal;        
//    }
//    
//    /**
//     * This method returns true if and only if the schedule date has passed
//     * @return
//     */
//    public boolean isScheduleDateInPast(){
//        boolean retVal = false;
//        Date currentDate = DateUtils.clearTimeFields(new Date(System.currentTimeMillis()));
//        if(this.scheduledDate != null) {
//            retVal = this.scheduledDate.before(currentDate);
//        }
//        return retVal;
//    }

}
