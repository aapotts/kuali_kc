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
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.rule.BusinessRuleInterface;
import org.kuali.kra.rules.ErrorReporter;
import org.kuali.kra.rules.ResearchDocumentRuleBase;

/**
 * 
 * This class implements the business when adding committee schedule minute.
 */
public class MeetingAddMinuteRule extends ResearchDocumentRuleBase implements BusinessRuleInterface<MeetingAddMinuteEvent> {

    private static final String PROTOCOL_ENTRY_TYPE = "3";

    private static final String NEW_COMM_SCHD_MINUTE_PROTOCOL = "meetingHelper.newCommitteeScheduleMinute.protocolIdFk";
    private static final String NEW_COMM_SCHD_MINUTE_PROTOCOL_CONTINGENCY = "meetingHelper.newCommitteeScheduleMinute.protocolContingencyCode";

    private ErrorReporter errorReporter;

    /**
     * 
     * This method is to validate new committee schedule minute. Make sure entry type of 'protocol' selection is ok.
     * if entry type is Protocol then 'protocol' is selected. and if protocol contingency code is entered, then
     * verify this code does exist.
     * @param event
     * @return
     */
    public boolean processRules(MeetingAddMinuteEvent event) {
        boolean rulePassed = true;
        errorReporter = new ErrorReporter();
        CommitteeScheduleMinute committeeScheduleMinute = event.getMeetingHelper().getNewCommitteeScheduleMinute();
        if (StringUtils.isNotBlank(committeeScheduleMinute.getMinuteEntryTypeCode())
                && committeeScheduleMinute.getMinuteEntryTypeCode().equals(PROTOCOL_ENTRY_TYPE)) {
            if (committeeScheduleMinute.getProtocolIdFk() == null) {
                errorReporter.reportError(NEW_COMM_SCHD_MINUTE_PROTOCOL, KeyConstants.ERROR_EMPTY_PROTOCOL);
                rulePassed = false;
            }
            if (StringUtils.isNotBlank(committeeScheduleMinute.getProtocolContingencyCode())) {
                Map<String, String> fieldValues = new HashMap<String, String>();
                fieldValues.put("protocolContingencyCode", committeeScheduleMinute.getProtocolContingencyCode());
                if (getBusinessObjectService().findByPrimaryKey(ProtocolContingency.class, fieldValues) == null) {
                    errorReporter.reportError(NEW_COMM_SCHD_MINUTE_PROTOCOL_CONTINGENCY,
                            KeyConstants.ERROR_EMPTY_PROTOCOL_CONTINGENCY);
                    rulePassed = false;
                }
            }
        }
        else if (StringUtils.isNotBlank(committeeScheduleMinute.getMinuteEntryTypeCode())
                && !committeeScheduleMinute.getMinuteEntryTypeCode().equals(PROTOCOL_ENTRY_TYPE)
                && committeeScheduleMinute.getProtocolIdFk() != null) {
            errorReporter.reportError(NEW_COMM_SCHD_MINUTE_PROTOCOL, KeyConstants.ERROR_NON_EMPTY_PROTOCOL);
            rulePassed = false;

        }
        return rulePassed;
    }
}
