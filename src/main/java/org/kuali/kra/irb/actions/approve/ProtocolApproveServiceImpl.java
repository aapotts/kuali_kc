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
package org.kuali.kra.irb.actions.approve;

import java.sql.Timestamp;

import org.kuali.kra.irb.Protocol;
import org.kuali.kra.irb.actions.ProtocolAction;
import org.kuali.kra.irb.actions.ProtocolActionType;
import org.kuali.kra.irb.actions.ProtocolStatus;
import org.kuali.rice.kns.service.BusinessObjectService;

public class ProtocolApproveServiceImpl implements ProtocolApproveService {
    
    private BusinessObjectService businessObjectService;

    /**
     * Set the business object service.
     * @param businessObjectService the business object service
     */
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
    
    /**{@inheritDoc}**/
    public void approve(Protocol protocol, ProtocolApproveBean actionBean) throws Exception {
        ProtocolAction protocolAction = new ProtocolAction(protocol, null, ProtocolActionType.APPROVED);
        protocolAction.setComments(actionBean.getComments());
        protocolAction.setActionDate(new Timestamp(actionBean.getActionDate().getTime()));
        protocol.getProtocolActions().add(protocolAction);
        
        protocol.setProtocolStatusCode(ProtocolStatus.ACTIVE_OPEN_TO_ENROLLMENT);
        protocol.setApprovalDate(actionBean.getApprovalDate());
        protocol.setExpirationDate(actionBean.getExpirationDate());
        protocol.refreshReferenceObject("protocolStatus");
        businessObjectService.save(protocol);

    }

}
