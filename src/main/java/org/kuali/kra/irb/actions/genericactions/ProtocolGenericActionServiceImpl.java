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
package org.kuali.kra.irb.actions.genericactions;

import java.sql.Timestamp;
import java.util.Collection;

import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.infrastructure.RoleConstants;
import org.kuali.kra.irb.Protocol;
import org.kuali.kra.irb.actions.ProtocolAction;
import org.kuali.kra.irb.actions.ProtocolActionType;
import org.kuali.kra.irb.actions.ProtocolStatus;
import org.kuali.rice.kim.service.RoleService;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.util.GlobalVariables;

/**
 * 
 * This class handles the generic actions that can be made to a protocol.  A generic action contain a comment, action date, and a 
 * state change.
 */
public class ProtocolGenericActionServiceImpl implements ProtocolGenericActionService {
    
    private static final String NAMESPACE = "KC-PROTOCOL";
    
    private BusinessObjectService businessObjectService;

    /**
     * Set the business object service.
     * @param businessObjectService the business object service
     */
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
    
    /**{@inheritDoc}**/
    public void close(Protocol protocol, ProtocolGenericActionBean actionBean) throws Exception {
        performGenericAction(protocol, actionBean, ProtocolActionType.CLOSED_ADMINISTRATIVELY_CLOSED, ProtocolStatus.CLOSED_ADMINISTRATIVELY);
    }
    
    /**{@inheritDoc}**/
    public void closeEnrollment(Protocol protocol, ProtocolGenericActionBean actionBean) throws Exception {
        performGenericAction(protocol, actionBean, ProtocolActionType.CLOSED_FOR_ENROLLMENT, ProtocolStatus.ACTIVE_CLOSED_TO_ENROLLMENT);
    }
    
    /**{@inheritDoc}**/
    public void expire(Protocol protocol, ProtocolGenericActionBean actionBean) throws Exception {
        performGenericAction(protocol, actionBean, ProtocolActionType.EXPIRED, ProtocolStatus.EXPIRED);
    }

    /**{@inheritDoc}**/
    public void permitDataAnalysis(Protocol protocol, ProtocolGenericActionBean actionBean) throws Exception {
        performGenericAction(protocol, actionBean, ProtocolActionType.DATA_ANALYSIS_ONLY, ProtocolStatus.ACTIVE_DATA_ANALYSIS_ONLY);
    }

    /**{@inheritDoc}**/
    public void reopen(Protocol protocol, ProtocolGenericActionBean actionBean) throws Exception {
        performGenericAction(protocol, actionBean, ProtocolActionType.REOPEN_ENROLLMENT, ProtocolStatus.ACTIVE_OPEN_TO_ENROLLMENT);
    }

    /**{@inheritDoc}**/
    public void suspend(Protocol protocol, ProtocolGenericActionBean actionBean) throws Exception {
        if (isIrbAdministrator()) {
            performGenericAction(protocol, actionBean, ProtocolActionType.SUSPENDED, ProtocolStatus.SUSPENDED_BY_IRB);
        } else {
            performGenericAction(protocol, actionBean, ProtocolActionType.SUSPENDED, ProtocolStatus.SUSPENDED_BY_PI);
        }
    }
    
    /**{@inheritDoc}**/
    public void suspendByDsmb(Protocol protocol, ProtocolGenericActionBean actionBean) throws Exception {
        performGenericAction(protocol, actionBean, ProtocolActionType.SUSPENDED_BY_DSMB, ProtocolStatus.SUSPENDED_BY_DSMB);
    }
    
    private boolean isIrbAdministrator() {
        String principalId = GlobalVariables.getUserSession().getPrincipalId();
        Collection<String> ids = getRoleService().getRoleMemberPrincipalIds(NAMESPACE, RoleConstants.IRB_ADMINISTRATOR, null);
        return ids.contains(principalId);
    }
    
    private RoleService getRoleService() {
        return KraServiceLocator.getService("kimRoleManagementService");
    }
    
    /**{@inheritDoc}**/
    public void terminate(Protocol protocol, ProtocolGenericActionBean actionBean) throws Exception {
        performGenericAction(protocol, actionBean, ProtocolActionType.TERMINATED, ProtocolStatus.TERMINATED_BY_IRB);
    }
    
    /**
     * 
     * This method performs the Generic action persistence.  A state change, action date, and a comment, that's it
     * @param protocol
     * @param actionBean
     * @param protocolActionType
     * @param newProtocolStatus
     * @throws Exception
     */
    private void performGenericAction(Protocol protocol, ProtocolGenericActionBean actionBean, 
            String protocolActionType, String newProtocolStatus) throws Exception {
        
        ProtocolAction protocolAction = new ProtocolAction(protocol, null, protocolActionType);
        protocolAction.setComments(actionBean.getComments());
        protocolAction.setActionDate(new Timestamp(actionBean.getActionDate().getTime()));
        protocol.getProtocolActions().add(protocolAction);
        
        protocol.setProtocolStatusCode(newProtocolStatus);
        protocol.refreshReferenceObject("protocolStatus");
        businessObjectService.save(protocol);
    }
}