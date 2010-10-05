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
package org.kuali.kra.irb.actions.undo;

import java.util.HashMap;
import java.util.Map;

import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.irb.Protocol;
import org.kuali.kra.irb.ProtocolDocument;
import org.kuali.kra.irb.actions.ProtocolAction;
import org.kuali.kra.irb.actions.ProtocolActionType;
import org.kuali.kra.irb.actions.ProtocolStatus;
import org.kuali.kra.irb.actions.copy.ProtocolCopyService;
import org.kuali.kra.irb.actions.submit.ProtocolActionService;
import org.kuali.kra.irb.correspondence.ProtocolCorrespondence;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.service.DocumentService;
import org.kuali.rice.kns.workflow.service.KualiWorkflowDocument;

public class UndoLastActionServiceImpl implements UndoLastActionService {
    private static final String AMEND = "A";
    private static final String RENEW = "R";
    
    private ProtocolActionService protocolActionService;
    private DocumentService documentService;
    private BusinessObjectService businessObjectService;
    
    public void setProtocolActionService(ProtocolActionService protocolActionService) {
        this.protocolActionService = protocolActionService;
    }
    
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }
    
    protected void removeAttachedCorrespondences(ProtocolAction protocolAction) {
        if(protocolAction != null) {
            Map<String, String> fieldValues = new HashMap<String, String>();
            fieldValues.put("actionIdFk", protocolAction.getProtocolActionId().toString());
            fieldValues.put("protocolNumber", protocolAction.getProtocolNumber());
            fieldValues.put("sequenceNumber", protocolAction.getSequenceNumber().toString());
            
            businessObjectService.deleteMatching(ProtocolCorrespondence.class, fieldValues);
        }
    }

    public ProtocolDocument undoLastAction(ProtocolDocument protocolDocument, UndoLastActionBean undoLastActionBean) throws Exception {
        //Undo Protocol Status and Submission Status update
        Protocol protocol = protocolDocument.getProtocol();
        undoLastActionBean.setActionsPerformed(protocol.getProtocolActions());
        
        ProtocolAction lastActionPerformed = undoLastActionBean.getLastPerformedAction();
        if(lastActionPerformed != null) {
            protocolActionService.resetProtocolStatus(lastActionPerformed, protocol);
        }
        
        //Undo possible workflow actions
        ProtocolDocument updatedDocument = undoWorkflowRouting(protocolDocument, lastActionPerformed);
        
        //Revert any correspondence that was sent out
        removeAttachedCorrespondences(lastActionPerformed); 
        
        //Clear the Audit trail - Action history created
        if(!protocolDocument.getDocumentHeader().getWorkflowDocument().stateIsCanceled()) {
            protocol.getProtocolActions().remove(undoLastActionBean.getLastPerformedAction());
        }
        
        //Save the updated Protocol object
        documentService.saveDocument(updatedDocument);
        return updatedDocument;
    }
    
    protected void resetProtocolStatus(Protocol protocol) {
        String protocolNumberUpper = protocol.getProtocolNumber().toUpperCase();
        String prevProtocolStatusCode = (protocolNumberUpper.contains(AMEND) ? ProtocolStatus.AMENDMENT_IN_PROGRESS : (protocolNumberUpper.contains(RENEW) ? ProtocolStatus.RENEWAL_IN_PROGRESS
                : ProtocolStatus.IN_PROGRESS));
        protocol.setProtocolStatusCode(prevProtocolStatusCode);
        protocol.setActive(true);
    }
    
    protected ProtocolDocument undoWorkflowRouting(ProtocolDocument protocolDocument, ProtocolAction lastPerformedAction) throws Exception {
        KualiWorkflowDocument currentWorkflowDocument = protocolDocument.getDocumentHeader().getWorkflowDocument();
        
        //Do we need additional check to see if this is not a Renewal/Amendment Approval? since we already eliminated those options within Authz Logic
        if (currentWorkflowDocument.stateIsCanceled()) {
            protocolDocument = KraServiceLocator.getService(ProtocolCopyService.class).copyProtocol(protocolDocument);
            resetProtocolStatus(protocolDocument.getProtocol());
        } else if(currentWorkflowDocument != null && lastPerformedAction != null && ProtocolActionType.APPROVED.equals(lastPerformedAction.getProtocolActionTypeCode())) {
            currentWorkflowDocument.returnToPreviousRouteLevel("Undo Last Action", currentWorkflowDocument.getDocRouteLevel() - 1);
        } 
        
        return protocolDocument;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
    
}
