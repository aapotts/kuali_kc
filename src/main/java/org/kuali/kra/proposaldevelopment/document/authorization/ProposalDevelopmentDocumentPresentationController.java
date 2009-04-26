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
package org.kuali.kra.proposaldevelopment.document.authorization;

import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.service.KraWorkflowService;
import org.kuali.rice.kns.document.Document;
import org.kuali.rice.kns.document.authorization.TransactionalDocumentPresentationControllerBase;

public class ProposalDevelopmentDocumentPresentationController extends TransactionalDocumentPresentationControllerBase {

    @Override
    protected boolean canSave(Document document) {
        // TODO : rice upgrade 11 - if in workflow, then should not be saved.  Not sure yet.
        return !KraServiceLocator.getService(KraWorkflowService.class).isInWorkflow(document) && super.canSave(document);
    }

}
