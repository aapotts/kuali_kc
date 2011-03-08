package org.kuali.kra.committee.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

public class CommitteeCompleteSeleniumTest extends CommitteeSeleniumTestBase {
    
    private static final String PERSON_ID_TAG = "committeeHelper.newCommitteeMembership.personId";
    private static final String ROLODEX_ID_TAG = "committeeHelper.newCommitteeMembership.rolodexId";
    private static final String RESEARCH_AREAS_TAG = "committeeResearchAreas";
    
    private static final String PERSON_ID_ID = "personId";
    private static final String ROLODEX_ID_ID = "rolodexId";
    private static final String MEMBERSHIP_TYPE_CODE_ID = "document.committeeList[0].committeeMemberships[%d].membershipTypeCode";
    private static final String TERM_START_DATE_ID = "document.committeeList[0].committeeMemberships[%d].termStartDate";
    private static final String TERM_END_DATE_ID = "document.committeeList[0].committeeMemberships[%d].termEndDate";
    private static final String MEMBERSHIP_ROLE_CODE_ID = "committeeHelper.newCommitteeMembershipRoles[%d].membershipRoleCode";
    private static final String START_DATE_ID = "committeeHelper.newCommitteeMembershipRoles[%d].startDate";
    private static final String END_DATE_ID = "committeeHelper.newCommitteeMembershipRoles[%d].endDate";
    private static final String RESEARCH_AREA_CODE_ID = "researchAreaCode";
    private static final String SCHEDULE_START_DATE_ID = "committeeHelper.scheduleData.scheduleStartDate";
    private static final String RECURRENCE_TYPE_ID = "committeeHelper.scheduleData.recurrenceType";
    private static final String SCHEDULE_END_DATE_ID = "committeeHelper.scheduleData.monthlySchedule.scheduleEndDate";
    
    private static final String VOTING_CHAIR_MEMBERSHIP_TYPE = "Voting member";
    private static final String NICHOLAS_MAJORS_PERSON_ID = "10000000004";
    private static final String NICHOLAS_MAJORS_NAME = "Nicholas  Majors";
    private static final String CHAIR_MEMBERSHIP_ROLE = "Chair";
    private static final String PERSONAL_CULINARY_SERVICES_OTHER_RESEARCH_AREA_CODE = "12.99";
    private static final String PERSONAL_CULINARY_SERVICES_OTHER_NAME = "12.99 Personal and Culinary Services, Other";
    private static final String ALLYSON_CATE_PERSON_ID = "10000000008";
    private static final String ALLYSON_CATE_NAME = "Allyson  Cate";
    private static final String IRB_ADMINISTRATOR_MEMBERSHIP_ROLE = "IRB Administrator";
    private static final String AGRICULTURAL_PRODUCTION_OPERATIONS_RESEARCH_AREA_CODE = "01.03";
    private static final String AGRICULTURAL_PRODUCTION_OPERATIONS_NAME = "01.03 Agricultural Production Operations";
    private static final String LEE_VAN_LENTEN_PERSON_ID = "328";
    private static final String LEE_VAN_LENTEN_NAME = "Van Lenten, Lee";
    private static final String ALTERNATE_MEMBERSHIP_ROLE = "Alternate";
    private static final String GENERAL_EDUCATION_RESEARCH_AREA_CODE = "13.01";
    private static final String GENERAL_EDUCATION_NAME = "13.01 Education, General";
    private static final String RECURRENCE_TYPE = "MONTHLY";
    
    private static final String ADD_MEMBERSHIP_BUTTON = "methodToCall.addCommitteeMembership";
    private static final String ADD_MEMBERSHIP_ROLE_BUTTON = "methodToCall.addCommitteeMembershipRole.document.committeeList[0].committeeMemberships[%d].line%d";
    private static final String ADD_EVENT_BUTTON = "methodToCall.addEvent";
    private static final String SHOW_ALL_MEMBERS_BUTTON = "methodToCall.showAllMembers";
    
    private String startDate;
    private String endDate;
    private String firstScheduleDate;
    private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    @Before
    public void setUp() throws Exception {
        super.setUp();
        setupDates();
    }

    @Test
    public void testCommitteeComplete() throws Exception {
        createCommittee();

        addMembers();
        
        addSchedules();
        
        routeDocument();
        
        assertMembers();
        assertSchedule();
        
        String documentNumber = getDocumentNumber();
        docSearch(documentNumber);
        
        assertMembers();
        assertSchedule();
    }

    private void addMembers() {
        clickCommitteeMembersPage();
        
        addMember(0, true, NICHOLAS_MAJORS_PERSON_ID, CHAIR_MEMBERSHIP_ROLE, PERSONAL_CULINARY_SERVICES_OTHER_RESEARCH_AREA_CODE);
        addMember(1, true, ALLYSON_CATE_PERSON_ID, IRB_ADMINISTRATOR_MEMBERSHIP_ROLE, AGRICULTURAL_PRODUCTION_OPERATIONS_RESEARCH_AREA_CODE);        
        addMember(2, false, LEE_VAN_LENTEN_PERSON_ID, ALTERNATE_MEMBERSHIP_ROLE, GENERAL_EDUCATION_RESEARCH_AREA_CODE);
    }
    
    private void addMember(int index, boolean isEmployee, String personId, String role, String researchAreaCode) {
        if (isEmployee) {
            lookup(PERSON_ID_TAG, PERSON_ID_ID, personId);
        } else {
            lookup(ROLODEX_ID_TAG, ROLODEX_ID_ID, personId);
        }
        
        click(ADD_MEMBERSHIP_BUTTON);
        
        openTab(0);
        
        openTab(1);
        set(String.format(MEMBERSHIP_TYPE_CODE_ID, index), VOTING_CHAIR_MEMBERSHIP_TYPE);
        set(String.format(TERM_START_DATE_ID, index), startDate);
        set(String.format(TERM_END_DATE_ID, index), endDate);
        
        openTab(3);
        set(String.format(MEMBERSHIP_ROLE_CODE_ID, index), role);
        set(String.format(START_DATE_ID, index), startDate);
        set(String.format(END_DATE_ID, index), endDate);
        click(String.format(ADD_MEMBERSHIP_ROLE_BUTTON, index, index));

        openTab(4);
        multiLookup(RESEARCH_AREAS_TAG, RESEARCH_AREA_CODE_ID, researchAreaCode);
        
        saveDocument();
        assertSave();
    }
    
    private void addSchedules() {
        clickCommitteeSchedulePage();
        
        set(SCHEDULE_START_DATE_ID, startDate);
        set(RECURRENCE_TYPE_ID, RECURRENCE_TYPE);
        set(SCHEDULE_END_DATE_ID, endDate);
        click(ADD_EVENT_BUTTON);
        saveDocument();
        assertSave();
    }
    
    private void assertMembers() {
        clickCommitteeMembersPage();
        
        click(SHOW_ALL_MEMBERS_BUTTON);
        clickExpandAll();
        
        assertMember(NICHOLAS_MAJORS_NAME, CHAIR_MEMBERSHIP_ROLE, PERSONAL_CULINARY_SERVICES_OTHER_NAME);
        assertMember(ALLYSON_CATE_NAME, IRB_ADMINISTRATOR_MEMBERSHIP_ROLE, AGRICULTURAL_PRODUCTION_OPERATIONS_NAME);
        assertMember(LEE_VAN_LENTEN_NAME, ALTERNATE_MEMBERSHIP_ROLE, GENERAL_EDUCATION_NAME);
    }
    
    private void assertMember(String name, String role, String researchArea) {
        assertPageContains(name);
        assertPageContains(role);
        assertPageContains(researchArea);
    }
    
    private void assertSchedule() {
        clickCommitteeSchedulePage();
        
        assertPageContains(firstScheduleDate);
    }
    
    private void setupDates() {
        Calendar cl = new GregorianCalendar();
        cl.setTime(new Date());        
        cl.get(Calendar.DAY_OF_MONTH);
        int month = cl.get(Calendar.MONTH);
        int year = cl.get(Calendar.YEAR);
        if (cl.get(Calendar.DAY_OF_MONTH) <= 6) {
            startDate = dateFormat.format(DateUtils.addDays(new Date(), 6));
            endDate = dateFormat.format(DateUtils.addDays(new Date(), 371));
        } else {
            startDate = dateFormat.format(new Date());
            endDate = dateFormat.format(DateUtils.addDays(new Date(), 365));
        }
        if (month == 11) {
            year = year + 1;
            month = 1;
        } else {
            month = month + 2;
        }
        if (month < 10) {
            firstScheduleDate = "0" + month + "/06/" + year;
        } else {
            firstScheduleDate =  month + "/06/" + year;
        }
    }
    
}