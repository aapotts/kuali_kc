/*
 * Copyright 2006-2008 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.osedu.org/licenses/ECL-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kra.award.document.authorization;

import java.util.HashSet;
import java.util.Set;

import org.kuali.kra.authorization.ApplicationTask;
import org.kuali.kra.authorization.KcTransactionalDocumentAuthorizerBase;
import org.kuali.kra.award.document.AwardDocument;
import org.kuali.kra.infrastructure.AwardTaskNames;
import org.kuali.kra.infrastructure.TaskName;
import org.kuali.rice.kim.bo.Person;
import org.kuali.rice.kim.util.KimConstants;
import org.kuali.rice.kns.authorization.AuthorizationConstants;
import org.kuali.rice.kns.document.Document;
import org.kuali.rice.kns.util.KNSConstants;

/**
 * This class is the Award Document Authorizer.  It determines the edit modes and
 * document actions for all award documents.
 */
public class AwardDocumentAuthorizer extends KcTransactionalDocumentAuthorizerBase {
    
    /**
     * @see org.kuali.rice.kns.document.authorization.TransactionalDocumentAuthorizer#getEditModes(
     * org.kuali.rice.kns.document.Document, org.kuali.rice.kim.bo.Person, java.util.Set)
     */
    public Set<String> getEditModes(Document document, Person user, Set<String> currentEditModes) {
        Set<String> editModes = new HashSet<String>();
        
        AwardDocument awardDocument = (AwardDocument) document;
        
        if (awardDocument.getAward().getAwardId() == null) {
            if (canCreateAward(user.getPrincipalId())) {
                editModes.add(AuthorizationConstants.EditMode.FULL_ENTRY);
            }
            else {
                editModes.add(AuthorizationConstants.EditMode.UNVIEWABLE);
            }
        }
        else {
            if (canExecuteAwardTask(user.getPrincipalId(), awardDocument, AwardTaskNames.MODIFY_AWARD.getAwardTaskName())) {  
                editModes.add(AuthorizationConstants.EditMode.FULL_ENTRY);
            }
            else if (canExecuteAwardTask(user.getPrincipalId(), awardDocument, AwardTaskNames.VIEW_AWARD.getAwardTaskName())) {
                editModes.add(AuthorizationConstants.EditMode.VIEW_ONLY);
            }
            else {
                editModes.add(AuthorizationConstants.EditMode.UNVIEWABLE);
            }
        }
        
        //FIXME: Adding all budget permissions for the time being.
//        if (canExecuteTask(user, awardDocument, TaskName.ADD_BUDGET)) {
            editModes.add("addBudget");
//        }
                
//        if (canExecuteTask(user, awardDocument, TaskName.OPEN_BUDGETS)) {
            editModes.add("openBudgets");
//        }
                
//        if (canExecuteTask(user, awardDocument, TaskName.MODIFY_BUDGET)) {
            editModes.add("modifyProposalBudget");
//        }

        
        return editModes;
    }
    
    /**
     * @see org.kuali.rice.kns.document.authorization.DocumentAuthorizer#canInitiate(java.lang.String, org.kuali.rice.kim.bo.Person)
     */
    public boolean canInitiate(String documentTypeName, Person user) {
        return canCreateAward(user.getPrincipalId());
    }
  
    /**
     * @see org.kuali.rice.kns.document.authorization.DocumentAuthorizer#canOpen(org.kuali.rice.kns.document.Document, org.kuali.rice.kim.bo.Person)
     */
    public boolean canOpen(Document document, Person user) {
        AwardDocument awardDocument = (AwardDocument) document;
        if (awardDocument.getAward().getAwardId() == null) {
            return canCreateAward(user.getPrincipalId());
        }
        return canExecuteAwardTask(user.getPrincipalId(), (AwardDocument) document, AwardTaskNames.VIEW_AWARD.getAwardTaskName());
    }
    
    /**
     * @see org.kuali.kra.authorization.KcTransactionalDocumentAuthorizerBase#canEdit(org.kuali.rice.kns.document.Document, org.kuali.rice.kim.bo.Person)
     */
    @Override
    public boolean canEdit(Document document, Person user) {
        return canExecuteAwardTask(user.getPrincipalId(), (AwardDocument) document, AwardTaskNames.MODIFY_AWARD.getAwardTaskName());
    }
    
    /**
     * @see org.kuali.kra.authorization.KcTransactionalDocumentAuthorizerBase#canSave(org.kuali.rice.kns.document.Document, org.kuali.rice.kim.bo.Person)
     */
    @Override
    protected boolean canSave(Document document, Person user) {
        return canEdit(document, user);
    }
    
    /**
     * @see org.kuali.kra.authorization.KcTransactionalDocumentAuthorizerBase#canReload(org.kuali.rice.kns.document.Document, org.kuali.rice.kim.bo.Person)
     */
    @Override
    protected boolean canReload(Document document, Person user) {
        return canEdit(document, user);
    }
    
    /**
     * @see org.kuali.kra.authorization.KcTransactionalDocumentAuthorizerBase#canCopy(org.kuali.rice.kns.document.Document, org.kuali.rice.kim.bo.Person)
     */
    @Override
    protected boolean canCopy(Document document, Person user) {
        return false;
    }
    
    /**
     * @see org.kuali.kra.authorization.KcTransactionalDocumentAuthorizerBase#canCancel(org.kuali.rice.kns.document.Document, org.kuali.rice.kim.bo.Person)
     */
    @Override
    protected boolean canCancel(Document document, Person user) {
        return canEdit(document, user);
    }
    
    /**
     * Can the user approve the given document?
     * @param document the document
     * @param user the user
     * @return true if the user can approve the document; otherwise false
     */
    @Override
    protected boolean canApprove(Document document, Person user) {
        return isEnroute(document) && isAuthorizedByTemplate(
                 document,
                 KNSConstants.KUALI_RICE_WORKFLOW_NAMESPACE,
                 KimConstants.PermissionTemplateNames.APPROVE_DOCUMENT,
                 user.getPrincipalId());
    }
    
    /**
     * Can the user disapprove the given document?
     * @param document the document
     * @param user the user
     * @return true if the user can disapprove the document; otherwise false
     */
    @Override
    protected boolean canDisapprove(Document document, Person user) {
        return canApprove(document, user);
    }
    
    /**
     * Can the user blanket approve the given document?
     * @param document the document
     * @param user the user
     * @return true if the user can blanket approve the document; otherwise false
     */
    @Override
    protected boolean canBlanketApprove(Document document, Person user) {
        return !isFinal(document) && isAuthorizedByTemplate(
                document,
                KNSConstants.KUALI_RICE_WORKFLOW_NAMESPACE,
                KimConstants.PermissionTemplateNames.BLANKET_APPROVE_DOCUMENT,
                user.getPrincipalId());
    }
    
    /**
     * Does the user have permission to create a award?
     * @param user the user
     * @return true if the user can create a award; otherwise false
     */
    private boolean canCreateAward(String userId) {
        ApplicationTask task = new ApplicationTask(TaskName.CREATE_AWARD);
        return getTaskAuthorizationService().isAuthorized(userId, task);
    }
    
    /**
     * Does the user have permission to execute the given task for a award?
     * @param username the user's username
     * @param doc the award document
     * @param taskName the name of the task
     * @return true if has permission; otherwise false
     */
    private boolean canExecuteAwardTask(String userId, AwardDocument doc, String taskName) {
        AwardTask task = new AwardTask(taskName, doc.getAward());
        return getTaskAuthorizationService().isAuthorized(userId, task);
    }
}
