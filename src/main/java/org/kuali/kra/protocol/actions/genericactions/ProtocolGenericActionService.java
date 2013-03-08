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
package org.kuali.kra.protocol.actions.genericactions;

import org.kuali.kra.protocol.ProtocolBase;
import org.kuali.kra.protocol.ProtocolDocumentBase;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;

/**
 * Defines the functions needed for the generic action service functions.
 */
/**
 * This class...
 */
public interface ProtocolGenericActionService {
    
// TODO *********commented the code below during IACUC refactoring*********     
//    /**
//     * Close a protocol.
//     * @param protocol ProtocolBase object
//     * @param actionBean ProtocolGenericActionBean object
//     * @throws Exception if there was a general problem performing the action
//     */
//    void close(ProtocolBase protocol, ProtocolGenericActionBean actionBean) throws Exception;
//    
//    /**
//     * Close enrollment for a protocol.
//     * @param protocol ProtocolBase object
//     * @param actionBean ProtocolGenericActionBean object
//     * @throws Exception if there was a general problem performing the action
//     */
//    void closeEnrollment(ProtocolBase protocol, ProtocolGenericActionBean actionBean) throws Exception;
//
//    /**
//     * Defer a protocol.
//     * @param protocol ProtocolBase object
//     * @param actionBean ProtocolGenericActionBean object
//     * @return the newly versioned ProtocolBase document
//     * @throws Exception if there was a general problem performing the action
//     */
//    ProtocolDocumentBase defer(ProtocolBase protocol, ProtocolGenericActionBean actionBean) throws Exception;
    
    
    
    /**
     * Handles the versioning of the protocol following its disapproval in routing. It will reset the status of the new
     * copy of the protocol to be 'pending/in progress', and change the submission status to 'disapproved'.
     * @param protocol ProtocolBase object
     * @return the newly versioned ProtocolBase document
     * @throws Exception if there was a general problem performing the action
     */
    ProtocolDocumentBase versionAfterDisapprovalInRouting(ProtocolBase protocol) throws Exception;
    
    
    /**
     * This method will insert the disapproved action into the given protocol's action list using the 
     * annotation and date data from the actionTakenVal argument. It will then update the protocol's 
     * status and its submission status to 'disapproved' and save the protocol. 
     * @param protocol
     * @param actionTakenVal
     */
    void recordDisapprovedInRoutingActionAndUpdateStatuses(ProtocolBase protocol, ActionTakenValue latestCurrentActionTakenVal);
    
    /**
     * Disapproves a protocol.
     * @param protocol ProtocolBase object
     * @param actionBean ProtocolGenericActionBean object
     * @throws Exception if there was a general problem performing the action
     */
    void disapprove(ProtocolBase protocol, ProtocolGenericActionBean actionBean) throws Exception;
    
    /**
     * Expire a protocol.
     * @param protocol ProtocolBase object
     * @param actionBean ProtocolGenericActionBean object
     * @throws Exception if there was a general problem performing the action
     */
    void expire(ProtocolBase protocol, ProtocolGenericActionBean actionBean) throws Exception;
    
    
// TODO *********commented the code below during IACUC refactoring*********     
//    /**
//     * Record IRB Acknowledgement for a protocol.
//     * @param protocol ProtocolBase object
//     * @param actionBean ProtocolGenericActionBean object
//     * @throws Exception if there was a general problem performing the action
//     */
//    void irbAcknowledgement(ProtocolBase protocol, ProtocolGenericActionBean actionBean) throws Exception;
//    
//    /**
//     * Permit Data Analysis.
//     * @param protocol ProtocolBase object
//     * @param actionBean ProtocolGenericActionBean object
//     * @throws Exception if there was a general problem performing the action
//     */
//    void permitDataAnalysis(ProtocolBase protocol, ProtocolGenericActionBean actionBean) throws Exception;
//    
//    /**
//     * Reopen a protocol for enrollment.
//     * @param protocol ProtocolBase object
//     * @param actionBean ProtocolGenericActionBean object
//     * @throws Exception if there was a general problem performing the action
//     */
//    void reopenEnrollment(ProtocolBase protocol, ProtocolGenericActionBean actionBean) throws Exception;
    
    /**
     * Returns the protocol to the PI for specific minor revisions.
     * @param protocol ProtocolBase object
     * @param actionBean ProtocolGenericActionBean object
     * @return the newly versioned ProtocolBase document
     * @throws Exception if there was a general problem performing the action
     */
    ProtocolDocumentBase returnForSMR(ProtocolBase protocol, ProtocolGenericActionBean actionBean) throws Exception;
    

    /**
     * Returns the protocol to the PI for substantial revisions.
     * @param protocol ProtocolBase object
     * @param actionBean ProtocolGenericActionBean object
     * @return the newly versioned ProtocolBase document
     * @throws Exception if there was a general problem performing the action
     */
    ProtocolDocumentBase returnForSRR(ProtocolBase protocol, ProtocolGenericActionBean actionBean) throws Exception;
    
    /**
     * Returns the protocol to the PI.
     * @param protocol ProtocolBase object
     * @param actionBean ProtocolGenericActionBean object
     * @return the newly versioned ProtocolBase document
     * @throws Exception if there was a general problem performing the action
     */
    ProtocolDocumentBase returnToPI(ProtocolBase protocol, ProtocolGenericActionBean actionBean) throws Exception;
    

    /**
     * Suspend the protocol by the PI or by the IRB Administrator.
     * @param protocol ProtocolBase object
     * @param actionBean ProtocolGenericActionBean object
     * @throws Exception if there was a general problem performing the action
     */
    void suspend(ProtocolBase protocol, ProtocolGenericActionBean actionBean) throws Exception;

// TODO *********commented the code below during IACUC refactoring*********     
//    /**
//     * Suspend the protocol by DSMB.
//     * @param protocol ProtocolBase object
//     * @param actionBean ProtocolGenericActionBean object
//     * @throws Exception if there was a general problem performing the action
//     */
//    void suspendByDsmb(ProtocolBase protocol, ProtocolGenericActionBean actionBean) throws Exception;
    
    /**
     * Terminate a protocol.
     * @param protocol ProtocolBase object
     * @param actionBean ProtocolGenericActionBean object
     * @throws Exception if there was a general problem performing the action
     */
    void terminate(ProtocolBase protocol, ProtocolGenericActionBean actionBean) throws Exception;

    
}
