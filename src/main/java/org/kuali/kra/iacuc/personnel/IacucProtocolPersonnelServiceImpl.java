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
package org.kuali.kra.iacuc.personnel;

import org.kuali.kra.protocol.personnel.ProtocolPersonRoleBase;
import org.kuali.kra.protocol.personnel.ProtocolPersonRoleMappingBase;
import org.kuali.kra.protocol.personnel.ProtocolPersonnelServiceImplBase;
import org.kuali.kra.protocol.personnel.ProtocolUnitBase;

public class IacucProtocolPersonnelServiceImpl extends ProtocolPersonnelServiceImplBase implements IacucProtocolPersonnelService {

    @Override
    protected ProtocolUnitBase createNewProtocolUnitInstanceHook() {
        return new IacucProtocolUnit();
    }

    @Override
    protected String getSequenceNumberNameHook() {
        return "SEQ_IACUC_PROTOCOL_ID";
    }

    @Override
    public Class<? extends ProtocolPersonRoleMappingBase> getProtocolPersonRoleMappingClassHook() {
        return IacucProtocolPersonRoleMapping.class;
    }

    @Override
    public Class<? extends ProtocolPersonRoleBase> getProtocolPersonRoleClassHook() {
        return IacucProtocolPersonRole.class;
    }
    
    

}
