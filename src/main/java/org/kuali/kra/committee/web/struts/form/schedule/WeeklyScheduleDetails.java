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
package org.kuali.kra.committee.web.struts.form.schedule;


/**
 * This class holds weekly recurrence UI data.
 */
public class WeeklyScheduleDetails extends ScheduleDetails {
    
    private Integer week;
    
    private String[] daysOfWeek;    
    
    public WeeklyScheduleDetails() {
        super();
        this.setWeek(1);
        this.setDaysOfWeek(new String[7]);
        this.getDaysOfWeek()[0] = DayOfWeek.Monday.name();
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public Integer getWeek() {
        return week;
    }

    public void setDaysOfWeek(String[] daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public String[] getDaysOfWeek() {
        return daysOfWeek;
    }

}
