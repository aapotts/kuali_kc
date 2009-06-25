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
package org.kuali.kra.irb.actions.submit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.kuali.kra.drools.util.DroolsRuleHandler;
import org.kuali.kra.irb.Protocol;
import org.kuali.kra.irb.ProtocolDao;
import org.kuali.kra.irb.actions.ProtocolAction;
import org.kuali.kra.irb.auth.ProtocolAuthorizationService;
import org.kuali.kra.rice.shim.UniversalUser;
import org.kuali.kra.service.UnitAuthorizationService;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.util.GlobalVariables;


public class ProtocolActionServiceImpl implements ProtocolActionService {

    private static final String LEAD_UNIT = "leadUnit";

    private static final String HOME_UNIT = "homeUnit";

    private static final String DEFAULT_UNIT = "defaultUnit";

    private BusinessObjectService businessObjectService;

    private ProtocolAuthorizationService protocolAuthorizationService;

    private UnitAuthorizationService unitAuthorizationService;

    private ProtocolDao protocolDao;

    String[] actn = { "104", "105", "106", "108", "114", "115", "116", "200", "201", "202", "203", "204", "205", "206", "207",
            "208", "209", "210", "211", "212", "300", "301", "302", "303", "304", "305", "306" };

    private List<String> actions = new ArrayList<String>();

    {
        actions = Arrays.asList(actn);
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public void setProtocolAuthorizationService(ProtocolAuthorizationService protocolAuthorizationService) {
        this.protocolAuthorizationService = protocolAuthorizationService;
    }

    public void setUnitAuthorizationService(UnitAuthorizationService unitAuthorizationService) {
        this.unitAuthorizationService = unitAuthorizationService;
    }

    public void setProtocolDao(ProtocolDao protocolDao) {
        this.protocolDao = protocolDao;
    }

    /**
     * @see org.kuali.kra.irb.actions.submit.ProtocolActionService#isActionAllowed(java.lang.String, org.kuali.kra.irb.Protocol)
     */
    public boolean isActionAllowed(String actionTypeCode, Protocol protocol) {
        ActionRightMapping rightMapper = new ActionRightMapping();
        rightMapper.setActionTypeCode(actionTypeCode);
        // TODO is following correct?
        rightMapper.setUnitIndicator(getUnit(protocol));
        rightMapper.setCommitteeId(protocol.getProtocolSubmission().getCommitteeId()); // TODO is this correct?
        rightMapper.setScheduleId(protocol.getProtocolSubmission().getScheduleId()); // TODO is this correct?
        DroolsRuleHandler updateHandle = new DroolsRuleHandler("org/kuali/kra/irb/drools/rules/actionRightRules.drl");
        updateHandle.executeRules(rightMapper);
        return hasPermission(protocol, rightMapper);
    }

    private String getUnit(Protocol protocol) {
        String unit = null;
        if (null != protocol.getLeadUnitNumber()) {
            if (protocol.getLeadUnitNumber().equalsIgnoreCase("000001"))
                unit = DEFAULT_UNIT;
            else
                unit = LEAD_UNIT;
        }
        else if (null != protocol.getProtocolSubmission().getCommittee().getHomeUnitNumber())
            unit = HOME_UNIT;
        return unit;
    }

    /**
     * @see org.kuali.kra.irb.actions.submit.ProtocolActionService#getActionsAllowed(org.kuali.kra.irb.Protocol)
     */
    public List<String> getActionsAllowed(Protocol protocol) {

        List<String> actionList = new ArrayList<String>();
        for (String actionTypeCode : actions) {
            if (canPerformAction(actionTypeCode, protocol) && isActionAllowed(actionTypeCode, protocol)) {
                actionList.add(actionTypeCode);
            }
        }
        return actionList;
    }

    private boolean hasPermission(Protocol protocol, ActionRightMapping rightMapper) {
        String unitNumber = null;
        if (null != rightMapper.getUnitIndicator()
                && (rightMapper.getUnitIndicator().equalsIgnoreCase(LEAD_UNIT) || rightMapper.getUnitIndicator().equalsIgnoreCase(
                        DEFAULT_UNIT))) {
            unitNumber = protocol.getLeadUnitNumber();
        }
        else if (null != rightMapper.getUnitIndicator() && rightMapper.getUnitIndicator().equalsIgnoreCase(HOME_UNIT)) {
            unitNumber = protocol.getProtocolSubmission().getCommittee().getHomeUnitNumber();
        }
        boolean flag = false;
        if (null != unitNumber)
            flag = unitAuthorizationService.hasPermission(new UniversalUser(GlobalVariables.getUserSession().getPerson())
                    .getPersonUserIdentifier(), unitNumber, rightMapper.getRightId());
        else
            flag = protocolAuthorizationService.hasPermission(new UniversalUser(GlobalVariables.getUserSession().getPerson())
                    .getPersonUserIdentifier(), protocol, rightMapper.getRightId());
        return flag;
    }

    public boolean canPerformAction(String actionTypeCode, Protocol protocol) {
        String submissionStatusCode = protocol.getProtocolSubmission().getSubmissionStatusCode();
        String submissionTypeCode = protocol.getProtocolSubmission().getSubmissionTypeCode();
        String protocolReviewTypeCode = protocol.getProtocolSubmission().getProtocolReviewTypeCode();
        String protocolStatusCode = protocol.getProtocolStatusCode();
        String scheduleId = protocol.getProtocolSubmission().getScheduleId();
        Integer submissionNumber = protocol.getProtocolSubmission().getSubmissionNumber();
        ProtocolActionMapping protocolAction = new ProtocolActionMapping(actionTypeCode, submissionStatusCode, submissionTypeCode,
            protocolReviewTypeCode, protocolStatusCode, scheduleId, submissionNumber);
        protocolAction.setBusinessObjectService(businessObjectService);
        protocolAction.setDao(protocolDao);
        protocolAction.setProtocol(protocol);
        DroolsRuleHandler updateHandle = new DroolsRuleHandler("org/kuali/kra/irb/drools/rules/canPerformProtocolActionRules.drl");
        updateHandle.executeRules(protocolAction);
        return protocolAction.isAllowed();
    }

    /**
     * @see org.kuali.kra.irb.actions.submit.ProtocolActionService#updateProtocolStatus(org.kuali.kra.irb.actions.ProtocolAction,
     *      org.kuali.kra.irb.Protocol)
     */
    public void updateProtocolStatus(ProtocolAction protocolActionBo, Protocol protocol) {
        runUpdateProtocolRules(protocolActionBo, protocol);
        new ActionLogger().log(protocolActionBo, protocol, businessObjectService);
    }

    public void runUpdateProtocolRules(ProtocolAction protocolActionBo, Protocol protocol) {

        String protocolNumberUpper = protocol.getProtocolNumber().toUpperCase();
        String specialCondition = (protocolNumberUpper.contains("A") ? "A" : (protocolNumberUpper.contains("R") ? "R" : "NONE"));

        ProtocolActionUpdateMapping protocolAction = new ProtocolActionUpdateMapping(protocolActionBo.getProtocolActionTypeCode(),
            protocol.getProtocolSubmission().getProtocolSubmissionType().getSubmissionTypeCode(), protocol.getProtocolStatusCode(),
            specialCondition);
        protocolAction.setProtocol(protocol);
        protocolAction.setProtocolSubmissionStatus(protocol.getProtocolSubmission().getSubmissionStatus());

        DroolsRuleHandler updateHandle = new DroolsRuleHandler("org/kuali/kra/irb/drools/rules/updateProtocolRules.drl");
        updateHandle.executeRules(protocolAction);
        businessObjectService.save(protocol);
    }

}
