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
package org.kuali.kra.protocol.actions.reviewcomments;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.protocol.onlinereview.ProtocolReviewAttachmentBase;
import org.kuali.kra.rule.BusinessRuleInterface;
import org.kuali.kra.rules.ResearchDocumentRuleBase;
import org.kuali.rice.krad.util.GlobalVariables;

/**
 * 
 * This class is validating the description of review attachments is not empty 
 */
public class ProtocolManageReviewAttachmentRule<E extends ProtocolManageReviewAttachmentEventBase<?>> extends ResearchDocumentRuleBase implements BusinessRuleInterface<E> {

    /**
     * {@inheritDoc}
     * 
     * @see org.kuali.kra.rule.BusinessRuleInterface#processRules(org.kuali.kra.rule.event.KraDocumentEventBaseExtension)
     */
    public boolean processRules(E event) {
        boolean isValid = true;


        GlobalVariables.getMessageMap().clearErrorPath();
        GlobalVariables.getMessageMap().addToErrorPath(event.getPropertyName());
        int index = 0;

        for (ProtocolReviewAttachmentBase reviewAttachment : event.getReviewAttachments()) {
            if (StringUtils.isEmpty(reviewAttachment.getDescription())) {
                isValid = false;
                GlobalVariables.getMessageMap().putError(String.format("reviewAttachments[%s].description", index),
                        KeyConstants.ERROR_ONLINE_REVIEW_ATTACHMENT_DESCRIPTION_REQUIRED);
            }
            index++;
        }

        return isValid;
    }
}
