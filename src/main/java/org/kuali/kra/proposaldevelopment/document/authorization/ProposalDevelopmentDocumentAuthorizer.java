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
package org.kuali.kra.proposaldevelopment.document.authorization;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.core.authorization.AuthorizationConstants;
import org.kuali.core.bo.user.UniversalUser;
import org.kuali.core.document.Document;
import org.kuali.core.document.authorization.DocumentActionFlags;
import org.kuali.core.document.authorization.PessimisticLock;
import org.kuali.core.document.authorization.TransactionalDocumentAuthorizerBase;
import org.kuali.core.exceptions.DocumentInitiationAuthorizationException;
import org.kuali.core.exceptions.PessimisticLockingException;
import org.kuali.core.util.GlobalVariables;
import org.kuali.core.util.ObjectUtils;
import org.kuali.kra.authorization.ApplicationTask;
import org.kuali.kra.authorization.KraAuthorizationConstants;
import org.kuali.kra.budget.bo.BudgetVersionOverview;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.infrastructure.TaskName;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.service.TaskAuthorizationService;
import org.kuali.rice.KNSServiceLocator;

/**
 * The Proposal Development Document Authorizer.  Primarily responsible for determining if
 * a user has permission to create/modify/view proposals.
 *
 * @author Kuali Research Administration Team (kualidev@oncourse.iu.edu)
 */
public class ProposalDevelopmentDocumentAuthorizer extends TransactionalDocumentAuthorizerBase {
   
    private static final String TRUE = "TRUE";
    private static final String FALSE = "FALSE";
    
    private static Map<String, String> entryEditModeReplacementMap = new HashMap<String, String>();
    
    /**
     * This method is used to check if the given parameters warrant a new lock to be created for the given user. This method
     * utilizes the {@link #isEntryEditMode(java.util.Map.Entry)} method.
     * 
     * @param document -
     *            document to verify lock creation against
     * @param editMode -
     *            edit modes list to check for 'entry type' edit modes
     * @param user -
     *            user the lock will be 'owned' by
     * @return true if the given edit mode map has at least one 'entry type' edit mode... false otherwise
     */
    @Override
    protected boolean isLockRequiredByUser(Document document, Map editMode, UniversalUser user) {
        String activeLockRegion = (String) GlobalVariables.getUserSession().retrieveObject(KraAuthorizationConstants.ACTIVE_LOCK_REGION);
        
        // check for entry edit mode
        for (Iterator iterator = editMode.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (isEntryEditMode(entry) && StringUtils.isNotEmpty(activeLockRegion)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method should be used to define Document Authorizer classes which will use custom lock descriptors in the
     * {@link PessimisticLock} objects NOTE: if this method is overriden to return true then the method
     * {@link #getCustomLockDescriptor(Document, Map, UniversalUser)} must be overriden
     * 
     * @return false if the document will not be using lock descriptors or true if the document will use lock descriptors.
     *         The default return value is false
     */
    @Override
    protected boolean useCustomLockDescriptors() {
        return true;
    }

    /**
     * This method should be overriden by groups requiring the lock descriptor field in the {@link PessimisticLock} objects.
     * If it is not overriden and {@link #useCustomLockDescriptors()} returns true then this method will throw a
     * {@link PessimisticLockingException}
     * 
     * @param document - document being checked for locking
     * @param editMode - editMode generated for passed in user
     * @param user - user attempting to establish locks
     * @return a {@link PessimisticLockingException} will be thrown as the default implementation
     */
    @Override
    protected String getCustomLockDescriptor(Document document, Map editMode, UniversalUser user) {
        String activeLockRegion = (String) GlobalVariables.getUserSession().retrieveObject(KraAuthorizationConstants.ACTIVE_LOCK_REGION);
        if(StringUtils.isNotEmpty(activeLockRegion))
            return document.getDocumentNumber()+"-"+activeLockRegion; 
        
        return null;
    }


    /**
     * @see org.kuali.core.document.authorization.DocumentAuthorizerBase#getEditMode(org.kuali.core.document.Document, org.kuali.core.bo.user.UniversalUser)
     */
    public Map getEditMode(Document doc, UniversalUser user) {
        
        ProposalDevelopmentDocument proposalDoc = (ProposalDevelopmentDocument) doc;
         
        Map editModeMap = new HashMap();
        String proposalNbr = proposalDoc.getProposalNumber();
        
        // The getEditMode() method is invoked when a proposal is accessed for creation and when it
        // is accessed for modification.  New proposals under creation don't have a proposal number.
        // For a new proposal, we have to know if the user has the permission to create a proposal.
        // For a current proposal, we have to know if the user the permission to modify or view the proposal.
        
        String username = user.getPersonUserIdentifier();
        if (proposalNbr == null) {
            if (canCreateProposal(user)) {
                editModeMap.put(AuthorizationConstants.EditMode.FULL_ENTRY, TRUE);
                setPermissions(username, proposalDoc, editModeMap);
            } else {
                editModeMap.put(AuthorizationConstants.EditMode.UNVIEWABLE, TRUE);
            }
        } else {
            if (canExecuteProposalTask(username, proposalDoc, TaskName.MODIFY_PROPOSAL)) {  
                editModeMap.put(AuthorizationConstants.EditMode.FULL_ENTRY, TRUE);
                setPermissions(username, proposalDoc, editModeMap);
            }
            else if (canExecuteProposalTask(username, proposalDoc, TaskName.VIEW_PROPOSAL)) {
                editModeMap.put(AuthorizationConstants.EditMode.VIEW_ONLY, TRUE);
                setPermissions(username, proposalDoc, editModeMap);
            }
            else {
                editModeMap.put(AuthorizationConstants.EditMode.UNVIEWABLE, TRUE);
            }
        }
     
        return editModeMap;
    }

    /**
     * Set the permissions to be used during the creation of the web pages.  
     * The JSP files can access the editModeMap (editingMode) to determine what
     * to display to the user.  For example, a JSP file may contain the following:
     * 
     *     <kra:section permission="modifyProposal">
     *         .
     *         .
     *         .
     *     </kra:section>
     * 
     * In the above example, the contents are only rendered if the user is allowed
     * to modify the proposal.  Note that permissions are always signified as 
     * either TRUE or FALSE.
     * 
     * @param username the user's unique username
     * @param doc the Proposal Development Document
     * @param editModeMap the edit mode map
     */
    @SuppressWarnings("unchecked")
    private void setPermissions(String username, ProposalDevelopmentDocument doc, Map editModeMap) {
        editModeMap.put("modifyProposal", editModeMap.containsKey(AuthorizationConstants.EditMode.FULL_ENTRY) ? TRUE : FALSE);
        editModeMap.put("addBudget", canExecuteTask(username, doc, TaskName.ADD_BUDGET));
        editModeMap.put("openBudgets", canExecuteTask(username, doc, TaskName.OPEN_BUDGETS));
        editModeMap.put("modifyPermissions", canExecuteTask(username, doc, TaskName.MODIFY_PROPOSAL_ROLES));
        editModeMap.put("addNarratives", canExecuteTask(username, doc, TaskName.ADD_NARRATIVE));
        editModeMap.put("certify", canExecuteTask(username, doc, TaskName.CERTIFY));
        editModeMap.put("printProposal", canExecuteTask(username, doc, TaskName.PRINT_PROPOSAL));
        editModeMap.put("alterProposalData", canExecuteTask(username, doc, TaskName.ALTER_PROPOSAL_DATA));
        editModeMap.put("submitToSponsor", canExecuteTask(username, doc, TaskName.SUBMIT_TO_SPONSOR));
        
        entryEditModeReplacementMap.put(KraAuthorizationConstants.ProposalEditMode.MODIFY_PROPOSAL, KraAuthorizationConstants.ProposalEditMode.VIEW_PROPOSAL);
        entryEditModeReplacementMap.put(KraAuthorizationConstants.ProposalEditMode.MODIFY_PERMISSIONS, KraAuthorizationConstants.ProposalEditMode.VIEW_PERMISSIONS);
        entryEditModeReplacementMap.put(KraAuthorizationConstants.ProposalEditMode.ADD_NARRATIVES, KraAuthorizationConstants.ProposalEditMode.VIEW_NARRATIVES);
        entryEditModeReplacementMap.put(KraAuthorizationConstants.BudgetEditMode.MODIFY_BUDGET, KraAuthorizationConstants.BudgetEditMode.VIEW_BUDGET);
        entryEditModeReplacementMap.put(AuthorizationConstants.EditMode.FULL_ENTRY, AuthorizationConstants.EditMode.VIEW_ONLY);
    } 
    
    /**
     * Can the user execute the given task?
     * @param username the user's username
     * @param doc the proposal development document
     * @param taskName the name of the task
     * @return "TRUE" if has permission; otherwise "FALSE"
     */
    private String canExecuteTask(String username, ProposalDevelopmentDocument doc, String taskName) {
        return canExecuteProposalTask(username, doc, taskName) ? TRUE : FALSE;
    }
    
    /**
     * Does the user have the given permission for the given proposal?
     * @param username the user's username
     * @param doc the proposal development document
     * @param taskName the name of the task
     * @return true if has permission; otherwise false
     */
    private boolean canExecuteProposalTask(String username, ProposalDevelopmentDocument doc, String taskName) {
        ProposalTask task = new ProposalTask(taskName, doc);       
        TaskAuthorizationService taskAuthenticationService = KraServiceLocator.getService(TaskAuthorizationService.class);
        return taskAuthenticationService.isAuthorized(username, task);
    }

    /**
     * @see org.kuali.core.document.authorization.DocumentAuthorizerBase#canInitiate(java.lang.String,
     *      org.kuali.core.bo.user.UniversalUser)
     */
    @Override
    public void canInitiate(String documentTypeName, UniversalUser user) {
        super.canInitiate(documentTypeName, user);
        if (!canCreateProposal(user)) {
            throw new DocumentInitiationAuthorizationException(KeyConstants.ERROR_AUTHORIZATION_DOCUMENT_INITIATION, 
                                                               new String[] { user.getPersonUserIdentifier(), documentTypeName });
        }
    }
    
    /**
     * @see org.kuali.core.document.authorization.DocumentAuthorizerBase#hasInitiateAuthorization(org.kuali.core.document.Document, org.kuali.core.bo.user.UniversalUser)
     */
    @Override
    public boolean hasInitiateAuthorization(Document document, UniversalUser user) {
    
        ProposalDevelopmentDocument proposalDoc = (ProposalDevelopmentDocument) document;
         
        String proposalNbr = proposalDoc.getProposalNumber();
        String username = user.getPersonUserIdentifier();
        boolean permission;
        if (proposalNbr == null) {
            permission = canCreateProposal(user);
        }
        else {
            permission = canExecuteProposalTask(username, proposalDoc, TaskName.MODIFY_PROPOSAL);
        }
        return permission;
    }
    
    /**
     * Does the user have permission to create a proposal.  Use the Unit Authorization Service to determine
     * if the user has the CREATE_PROPOSAL permission in any unit.
     * @param user the user
     * @return true if the user has the CREATE_PROPOSAL permission in at least one unit; otherwise false
     */
    private boolean canCreateProposal(UniversalUser user) {
        String username = user.getPersonUserIdentifier();
        ApplicationTask task = new ApplicationTask(TaskName.CREATE_PROPOSAL);       
        TaskAuthorizationService taskAuthenticationService = KraServiceLocator.getService(TaskAuthorizationService.class);
        return taskAuthenticationService.isAuthorized(username, task);
    }
    
    /**
     * @see org.kuali.core.document.authorization.TransactionalDocumentAuthorizerBase#getDocumentActionFlags(org.kuali.core.document.Document, org.kuali.core.bo.user.UniversalUser)
     */
    @Override
    public DocumentActionFlags getDocumentActionFlags(Document document, UniversalUser user) {
        // no copy button
        DocumentActionFlags flags = super.getDocumentActionFlags(document, user);
        flags.setCanCopy(false);
        // NEED TO REDO ANNOTATE CHECK SINCE CHANGED THE VALUE OF FLAGS
        this.setAnnotateFlag(flags);
        
        // Any user who has the Initiate Authorization can save and cancel.
        
        if (this.hasInitiateAuthorization(document, user)) {
            flags.setCanSave(true);
            flags.setCanCancel(true);
            flags.setCanReload(true);
        }

        return flags;
    }
    
    /**
     * This method is used to check if the given {@link Map.Entry} is an 'entry type' edit mode and that the value is set to
     * signify that this user has that edit mode available to them
     * 
     * @param entry -
     *            the {@link Map.Entry} object that contains an edit mode such as the ones returned but
     *            {@link #getEditMode(Document, UniversalUser)}
     * @return true if the given entry has a key signifying an 'entry type' edit mode and the value is equal to
     *         {@link #EDIT_MODE_DEFAULT_TRUE_VALUE}... false if not
     */
    @Override
    protected boolean isEntryEditMode(Map.Entry entry) {
        if (AuthorizationConstants.EditMode.FULL_ENTRY.equals(entry.getKey())
                || KraAuthorizationConstants.ProposalEditMode.ADD_NARRATIVES.equals(entry.getKey())
                || KraAuthorizationConstants.ProposalEditMode.MODIFY_PERMISSIONS.equals(entry.getKey())
                || KraAuthorizationConstants.ProposalEditMode.MODIFY_PROPOSAL.equals(entry.getKey())
                || KraAuthorizationConstants.BudgetEditMode.MODIFY_BUDGET.equals(entry.getKey())
                ) {
            String fullEntryEditModeValue = (String)entry.getValue();
            return ( (ObjectUtils.isNotNull(fullEntryEditModeValue)) && (EDIT_MODE_DEFAULT_TRUE_VALUE.equals(fullEntryEditModeValue)) );
        }
        return false;
    }
    
    /**
     * This method is used to return values needed to replace the given 'entry type' edit mode {@link Map.Entry} with one that will not allow the user to enter data on the document 
     * 
     * @param entry - the current 'entry type' edit mode to replace 
     * @return a Map of edit modes that will be used to replace this edit mode (represented by the given entry parameter)
     */
    @Override
    protected Map getEntryEditModeReplacementMode(Map.Entry entry) {
        Map editMode = new HashMap(); 
        editMode.put(entryEditModeReplacementMap.get(entry.getKey()), EDIT_MODE_DEFAULT_TRUE_VALUE); 
        return editMode;
    }
    
    @Override
    protected Map getEditModeWithEditableModesRemoved(Map currentEditMode) {
        //currentEditMode.put("lockOwnedBy", new UniversalUser());
        return super.getEditModeWithEditableModesRemoved(currentEditMode);
    }
    
    @Override
    protected PessimisticLock createNewPessimisticLock(Document document, Map editMode, UniversalUser user) {
        if (useCustomLockDescriptors()) {
            String lockDescriptor = getCustomLockDescriptor(document, editMode, user);
            ProposalDevelopmentDocument pdDocument = (ProposalDevelopmentDocument) document;
            if(StringUtils.isNotEmpty(lockDescriptor) && lockDescriptor.contains(KraAuthorizationConstants.LOCK_DESCRIPTOR_BUDGET)) {
                for(BudgetVersionOverview budgetOverview: pdDocument.getBudgetVersionOverviews()) {
                    KNSServiceLocator.getPessimisticLockService().generateNewLock(budgetOverview.getDocumentNumber(), lockDescriptor, user);
                }  
            }
            return KNSServiceLocator.getPessimisticLockService().generateNewLock(document.getDocumentNumber(), lockDescriptor, user);
        } else {
            return KNSServiceLocator.getPessimisticLockService().generateNewLock(document.getDocumentNumber(), user);
        }
    }

}
