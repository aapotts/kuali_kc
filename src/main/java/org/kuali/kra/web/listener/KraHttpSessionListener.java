/*
 * Copyright 2005-2010 The Kuali Foundation
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
package org.kuali.kra.web.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSessionEvent;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.authorization.KraAuthorizationConstants;
import org.kuali.rice.kew.exception.WorkflowException;
import org.kuali.rice.kim.bo.Person;
import org.kuali.rice.kns.UserSession;
import org.kuali.rice.kns.document.Document;
import org.kuali.rice.kns.document.authorization.PessimisticLock;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.service.PessimisticLockService;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.util.KNSConstants;
import org.kuali.rice.kns.util.ObjectUtils;
import org.kuali.rice.kns.web.listener.KualiHttpSessionListener;

/**
 * This class is used to handle session timeouts where {@link PessimisticLock} objects should
 * be removed from a document 
 * 
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 *
 */
public class KraHttpSessionListener extends KualiHttpSessionListener {

    /**
     *  EMPTY METHOD IMPLEMENTATION
     */
    public void sessionCreated(HttpSessionEvent se) {
        // no operation required at this time
    }

    /**
     * This method checks for the existence of a document based on session variables and deletes any locks
     * associated with the document that belong to the current user
     * 
     * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent se) {
        String documentNumber = (String) se.getSession().getAttribute(KNSConstants.DOCUMENT_HTTP_SESSION_KEY);
        if (StringUtils.isNotBlank(documentNumber)) {
            try {
                // document service needs the usersession to operate but we need the document from document service to verify it exists
                GlobalVariables.setUserSession((UserSession)se.getSession().getAttribute(KNSConstants.USER_SESSION_KEY));
                Document document = KNSServiceLocator.getDocumentService().getByDocumentHeaderId(documentNumber);
                
                Person loggedInUser = GlobalVariables.getUserSession().getPerson();
                PessimisticLockService lockService = KNSServiceLocator.getPessimisticLockService();
                
                if (ObjectUtils.isNotNull(document)) {
                    lockService.releaseAllLocksForUser(document.getPessimisticLocks(), loggedInUser);
                    releaseCustomBudgetLocks(document, lockService, loggedInUser);
                }
            } catch (WorkflowException e) {
                throw new RuntimeException(e);
            } finally {
                GlobalVariables.setUserSession(null);
            }
           
        }
    }
    
    private void releaseCustomBudgetLocks(Document document, PessimisticLockService lockService, Person loggedInUser) {
        String budgetLockDescriptor = null;
        for(PessimisticLock lock: document.getPessimisticLocks()) {
            if(StringUtils.isNotEmpty(lock.getLockDescriptor()) && lock.getLockDescriptor().contains(KraAuthorizationConstants.LOCK_DESCRIPTOR_BUDGET)) {
                budgetLockDescriptor = lock.getLockDescriptor();
                break;
            }
        }
        
        List<PessimisticLock> otherBudgetLocks = findMatchingLocksWithGivenDescriptor(budgetLockDescriptor); 
        lockService.releaseAllLocksForUser(otherBudgetLocks, loggedInUser, budgetLockDescriptor);
    }
    
    private List<PessimisticLock> findMatchingLocksWithGivenDescriptor(String lockDescriptor) {
        BusinessObjectService boService = KNSServiceLocator.getBusinessObjectService();
        Map fieldValues = new HashMap();
        fieldValues.put("lockDescriptor", lockDescriptor);
        List<PessimisticLock> matchingLocks = (List<PessimisticLock>) boService.findMatching(PessimisticLock.class, fieldValues);
        return matchingLocks;
    }


}
