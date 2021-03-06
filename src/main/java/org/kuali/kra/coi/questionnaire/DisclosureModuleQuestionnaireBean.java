/*
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.kra.coi.questionnaire;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.bo.CoeusModule;
import org.kuali.kra.coi.CoiDisclProject;
import org.kuali.kra.coi.CoiDisclosure;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.krms.KrmsRulesContext;
import org.kuali.kra.questionnaire.answer.ModuleQuestionnaireBean;
import org.kuali.rice.krad.service.BusinessObjectService;

public class DisclosureModuleQuestionnaireBean extends ModuleQuestionnaireBean {
    
    private CoiDisclosure coiDisclosure;
    
    public DisclosureModuleQuestionnaireBean(CoiDisclosure coiDisclosure) {
        super(CoeusModule.COI_DISCLOSURE_MODULE_CODE, coiDisclosure.getCoiDisclosureId() == null ? "" : coiDisclosure.getCoiDisclosureId().toString(), coiDisclosure.getEventTypeCode(), "-1", 
                coiDisclosure.getCoiDisclosureDocument().getDocumentHeader().hasWorkflowDocument() ? coiDisclosure.getCoiDisclosureDocument().getDocumentHeader().getWorkflowDocument().isApproved() : false);
        this.coiDisclosure = coiDisclosure;
    }
    
    public DisclosureModuleQuestionnaireBean(CoiDisclosure coiDisclosure, CoiDisclProject coiDisclProject, boolean finalDoc) {
        super(CoeusModule.COI_DISCLOSURE_MODULE_CODE, coiDisclosure.getCoiDisclosureId() == null ? "" : coiDisclosure.getCoiDisclosureId().toString(), coiDisclProject.getDisclosureEventType(), coiDisclProject.getCoiProjectId(), finalDoc);
        this.coiDisclosure = coiDisclosure;
    }
    
    public DisclosureModuleQuestionnaireBean(String moduleItemCode, String moduleItemKey, String moduleSubItemCode, String moduleSubItemKey, boolean finalDoc) {
        super(moduleItemCode, moduleItemKey, moduleSubItemCode, moduleSubItemKey, finalDoc);
    }

    @Override
    public KrmsRulesContext getKrmsRulesContextFromBean() {
        if (coiDisclosure != null) {
            return coiDisclosure.getCoiDisclosureDocument();
        } else {
            if (StringUtils.isNotBlank(getModuleItemKey())) {
                return KraServiceLocator.getService(BusinessObjectService.class).findBySinglePrimaryKey(CoiDisclosure.class, Long.valueOf(getModuleItemKey())).getCoiDisclosureDocument();
            } else {
                return null;
            }
        }
    }

    public CoiDisclosure getCoiDisclosure() {
        return coiDisclosure;
    }

    public void setCoiDisclosure(CoiDisclosure coiDisclosure) {
        this.coiDisclosure = coiDisclosure;
    }

}
