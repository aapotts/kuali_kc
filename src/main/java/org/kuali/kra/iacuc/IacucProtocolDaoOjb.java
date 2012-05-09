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
package org.kuali.kra.iacuc;

import org.kuali.kra.iacuc.actions.submit.IacucProtocolSubmission;
import org.kuali.kra.iacuc.personnel.IacucProtocolPerson;
import org.kuali.kra.iacuc.personnel.IacucProtocolUnit;
import org.kuali.kra.iacuc.protocol.location.IacucProtocolLocation;
import org.kuali.kra.iacuc.protocol.research.IacucProtocolResearchArea;
import org.kuali.kra.protocol.Protocol;
import org.kuali.kra.protocol.ProtocolDaoOjb;
import org.kuali.kra.protocol.actions.submit.ProtocolSubmission;
import org.kuali.kra.protocol.personnel.ProtocolPerson;
import org.kuali.kra.protocol.personnel.ProtocolUnit;
import org.kuali.kra.protocol.protocol.funding.ProtocolFundingSource;
import org.kuali.kra.protocol.protocol.location.ProtocolLocation;
import org.kuali.kra.protocol.protocol.research.ProtocolResearchArea;
import org.kuali.rice.krad.util.OjbCollectionAware;


/**
 * 
 * This class is the implementation for IacucProtocolDao interface.
 */
public class IacucProtocolDaoOjb extends ProtocolDaoOjb<IacucProtocol> implements OjbCollectionAware, IacucProtocolDao {

    @Override
    protected Class<? extends Protocol> getProtocolBOClassHook() {
        return IacucProtocol.class;
    }

    @Override
    protected Class<? extends ProtocolResearchArea> getProtocolResearchAreaBOClassHook() {
        return IacucProtocolResearchArea.class;
    }

    @Override
    protected Class<? extends ProtocolPerson> getProtocolPersonBOClassHook() {
        return IacucProtocolPerson.class;
    }

    @Override
    protected Class<? extends ProtocolFundingSource> getProtocolFundingSourceBOClassHook() {
        //return IacucProtocolFundingSource.class;
        return ProtocolFundingSource.class;
    }

    @Override
    protected Class<? extends ProtocolLocation> getProtocolLocationBOClassHook() {
        return IacucProtocolLocation.class;
    }

    @Override
    protected Class<? extends ProtocolUnit> getProtocolUnitBOClassHook() {
        return IacucProtocolUnit.class;
    }

    @Override
    protected Class<? extends ProtocolSubmission> getProtocolSubmissionBOClassHook() {
        return IacucProtocolSubmission.class;
    }
}
