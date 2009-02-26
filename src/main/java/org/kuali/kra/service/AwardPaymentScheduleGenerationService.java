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
package org.kuali.kra.service;

import java.text.ParseException;
import java.util.List;

import org.kuali.kra.award.bo.Award;
import org.kuali.kra.award.bo.AwardReportTerm;

/**
 * 
 * This is the AwardPaymentScheduleGenerationService interface.
 */
public interface AwardPaymentScheduleGenerationService {

    public void generateSchedules(Award award, List<AwardReportTerm> awardReportTerms) throws ParseException;
}
