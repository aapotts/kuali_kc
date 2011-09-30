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
package org.kuali.kra.irb.notification;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.bo.KraPersistableBusinessObjectBase;
import org.kuali.kra.common.notification.bo.NotificationModuleRoleQualifier;
import org.kuali.kra.common.notification.service.KcNotificationRoleQualifierService;
import org.kuali.kra.irb.Protocol;
import org.kuali.kra.kim.bo.KcKimAttributes;


public class IRBNotificationRoleQualifierServiceImpl implements KcNotificationRoleQualifierService {

    private Protocol protocol;
    
    public IRBNotificationRoleQualifierServiceImpl(Protocol protocol) {
        this.protocol = protocol;
    }
    
    /**
     * 
     * @see org.kuali.kra.common.notification.service.KcNotificationRoleQualifierService#getRoleQualifierValue(org.kuali.kra.common.notification.bo.NotificationModuleRoleQualifier)
     */
    @Override
    public String getRoleQualifierValue(NotificationModuleRoleQualifier qualifier) {

        if (StringUtils.equals(qualifier.getDocumentName(), Protocol.class.getName())) {
            String qName = qualifier.getQualifier();
            
            if (qName.equalsIgnoreCase(KcKimAttributes.UNIT_NUMBER)) {
                return getProtocol().getLeadUnitNumber();
            } else if (qName.equalsIgnoreCase(KcKimAttributes.PROTOCOL)) {
                return getProtocol().getProtocolNumber();
            }
        }
        
        return null;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

}
