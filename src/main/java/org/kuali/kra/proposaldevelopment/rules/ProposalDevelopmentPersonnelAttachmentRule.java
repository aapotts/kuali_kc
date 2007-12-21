/*
 * Copyright 2007 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
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
package org.kuali.kra.proposaldevelopment.rules;

import static org.kuali.kra.infrastructure.KeyConstants.ERROR_ATTACHMENT_TYPE_NOT_SELECTED;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.proposaldevelopment.bo.ProposalPersonBiography;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.proposaldevelopment.rule.AddPersonnelAttachmentRule;
import org.kuali.kra.proposaldevelopment.rule.event.AddPersonnelAttachmentEvent;
import org.kuali.kra.rules.ResearchDocumentRuleBase;

public class ProposalDevelopmentPersonnelAttachmentRule extends ResearchDocumentRuleBase implements AddPersonnelAttachmentRule {
    private static final String NEW_PROP_PERSON_BIO = "newPropPersonBio";

    /**
     * rules to check documenttype/filename/description/person are selected or entered for personnel attachment.
     * @see org.kuali.kra.proposaldevelopment.rule.AddPersonnelAttachmentRule#processAddPersonnelAttachmentBusinessRules(org.kuali.kra.proposaldevelopment.rule.event.AddPersonnelAttachmentEvent)
     */
    public boolean processAddPersonnelAttachmentBusinessRules(AddPersonnelAttachmentEvent addPersonnelAttachmentEvent) {
        ProposalDevelopmentDocument document = (ProposalDevelopmentDocument)addPersonnelAttachmentEvent.getDocument();
        ProposalPersonBiography proposalPersonBiography = addPersonnelAttachmentEvent.getProposalPersonBiography();
        boolean rulePassed = true;
        String errorPath = NEW_PROP_PERSON_BIO;

        if(StringUtils.isBlank(proposalPersonBiography.getDocumentTypeCode())){
            rulePassed = false;
            reportError(errorPath+".documentTypeCode", ERROR_ATTACHMENT_TYPE_NOT_SELECTED);
        }
        if(proposalPersonBiography.getProposalPersonNumber() == null || StringUtils.isBlank(proposalPersonBiography.getProposalPersonNumber().toString())){
            rulePassed = false;
            reportError(errorPath+".proposalPersonNumber", KeyConstants.ERROR_PERSONNEL_ATTACHMENT_PERSON_REQUIRED);
        }
        if(StringUtils.isBlank(proposalPersonBiography.getDescription())){
            rulePassed = false;
            reportError(errorPath+".description", KeyConstants.ERROR_PERSONNEL_ATTACHMENT_DESCRITPION_REQUIRED);
        }
        if (StringUtils.isBlank(proposalPersonBiography.getFileName())) {
            rulePassed = false;
            errorPath = errorPath;
                reportError(errorPath+".personnelAttachmentFile", KeyConstants.ERROR_REQUIRED_FOR_FILE_NAME, "File Name");
        }
        
        return rulePassed;
    }

}
