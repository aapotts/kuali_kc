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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.kuali.kra.authorization.ApplicationTask;
import org.kuali.kra.award.document.AwardDocument;
import org.kuali.kra.infrastructure.AwardTaskNames;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.infrastructure.TaskName;
import org.kuali.kra.rice.shim.DocumentInitiationAuthorizationException;
import org.kuali.kra.rice.shim.UniversalUser;
import org.kuali.kra.service.TaskAuthorizationService;
import org.kuali.rice.kim.bo.Person;
import org.kuali.rice.kns.authorization.AuthorizationConstants;
import org.kuali.rice.kns.document.Document;
import org.kuali.rice.kns.document.authorization.TransactionalDocumentAuthorizerBase;
import org.kuali.rice.kns.util.KNSConstants;

/**
 * 
 * This class represents the Award Document Authorizer.
 */
public class AwardDocumentAuthorizer extends TransactionalDocumentAuthorizerBase {
    
    private static final String TRUE = "TRUE";
    
    /**
     * Used for permissions
     * @see org.kuali.core.document.authorization.DocumentAuthorizerBase#getEditMode(org.kuali.core.document.Document, org.kuali.core.bo.user.UniversalUser)
     */
    @SuppressWarnings("unchecked")
    public Map getEditMode(Document doc, UniversalUser user) {
                 
        Map editModeMap = new HashMap();
        
        AwardDocument awardDocument = (AwardDocument) doc;
        
        String username = user.getPersonUserIdentifier();
        if (awardDocument.getAward().getAwardId() == null) {
            String editMode = canCreateAward(user) ? AuthorizationConstants.EditMode.FULL_ENTRY : AuthorizationConstants.EditMode.UNVIEWABLE;
            editModeMap.put(editMode, TRUE);
        }else {
            if (canExecuteAwardTask(username, awardDocument, AwardTaskNames.MODIFY_AWARD.getAwardTaskName())) {  
                editModeMap.put(AuthorizationConstants.EditMode.FULL_ENTRY, TRUE);
            }else if (canExecuteAwardTask(username, awardDocument, AwardTaskNames.VIEW_AWARD.getAwardTaskName())) {
                editModeMap.put(AuthorizationConstants.EditMode.VIEW_ONLY, TRUE);
            }else {
                editModeMap.put(AuthorizationConstants.EditMode.UNVIEWABLE, TRUE);
            }
        }
        
        return editModeMap;
    }
    
    /**
     * 
     * 
     * @param documentTypeName
     * @param user
     */
    public void canInitiate(String documentTypeName, UniversalUser user) {
        super.canInitiate(documentTypeName, user);
        if (!canCreateAward(user)) {
            throw new DocumentInitiationAuthorizationException(KeyConstants.ERROR_AUTHORIZATION_DOCUMENT_INITIATION, 
                                                               new String[] { user.getPersonUserIdentifier(), documentTypeName });
        }
    }

    /**
     * 
     * This method determines if user has initiate authorization on the document.
     * @param document
     * @param user
     * @return
     */
    public boolean hasInitiateAuthorization(Document document, UniversalUser user) {
   
        AwardDocument awardDocument = (AwardDocument) document;
        
        boolean permission;
        if (awardDocument.getAward().getAwardId() == null) {
            permission = canCreateAward(user);
        }else {
            String username = user.getPersonUserIdentifier();
            permission = canExecuteAwardTask(username, awardDocument, AwardTaskNames.MODIFY_AWARD.getAwardTaskName());
        }
        return permission;
    }
   
    /**
     * 
     * @see org.kuali.rice.kns.document.authorization.DocumentAuthorizerBase#getDocumentActions(org.kuali.rice.kns.document.Document, 
     *                                                                                              org.kuali.rice.kim.bo.Person, java.util.Set)
     */
    @Override
    public Set<String> getDocumentActions(Document document, Person user, Set<String> documentActions) {
        super.getDocumentActions(document, user, documentActions);
        documentActions.remove(KNSConstants.KUALI_ACTION_CAN_COPY);
        
//      Any user who has the Initiate Authorization can save and cancel.
        if (this.hasInitiateAuthorization(document, new UniversalUser(user))) {
            documentActions.add(KNSConstants.KUALI_ACTION_CAN_SAVE);
            documentActions.add(KNSConstants.KUALI_ACTION_CAN_CANCEL);
            documentActions.add(KNSConstants.KUALI_ACTION_CAN_RELOAD);
        } else {
            documentActions.remove(KNSConstants.KUALI_ACTION_CAN_SAVE);
            documentActions.remove(KNSConstants.KUALI_ACTION_CAN_CANCEL);
            documentActions.remove(KNSConstants.KUALI_ACTION_CAN_RELOAD);
        }
        
        return documentActions;
    }
    
    /**
     * Does the user have permission to create a award?
     * @param user the user
     * @return true if the user can create a award; otherwise false
     */
    private boolean canCreateAward(UniversalUser user) {
        String username = user.getPersonUserIdentifier();
        ApplicationTask task = new ApplicationTask(TaskName.CREATE_AWARD);
        return getTaskAuthorizationService().isAuthorized(username, task);
    }
    
    /**
     * Does the user have permission to execute the given task for a award?
     * @param username the user's username
     * @param doc the award document
     * @param taskName the name of the task
     * @return true if has permission; otherwise false
     */
    private boolean canExecuteAwardTask(String username, AwardDocument doc, String taskName) {
        AwardTask task = new AwardTask(taskName, doc.getAward());
        return getTaskAuthorizationService().isAuthorized(username, task);
    }
    
    /**
     * 
     * This is a helper method for retrieving TaskAuthorizationService using the service locator.
     * @return
     */
    protected TaskAuthorizationService getTaskAuthorizationService(){
        return (TaskAuthorizationService) KraServiceLocator.getService(TaskAuthorizationService.class);        
    }
}
