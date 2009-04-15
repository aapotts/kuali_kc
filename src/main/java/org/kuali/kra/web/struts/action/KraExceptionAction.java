/*
 * Copyright 2006-2009 The Kuali Foundation
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

package org.kuali.kra.web.struts.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.kuali.RiceConstants;
import org.kuali.core.bo.user.UniversalUser;
import org.kuali.core.document.Document;
import org.kuali.core.document.authorization.PessimisticLock;
import org.kuali.core.service.BusinessObjectService;
import org.kuali.core.service.PessimisticLockService;
import org.kuali.core.util.GlobalVariables;
import org.kuali.core.util.ObjectUtils;
import org.kuali.kra.authorization.KraAuthorizationConstants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.rice.KNSServiceLocator;


/**
 * Retrieves and populates error messages during exception-handling
 */
public class KraExceptionAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward returnForward = mapping.findForward(RiceConstants.MAPPING_BASIC);

        Exception e = (Exception) request.getAttribute(Globals.EXCEPTION_KEY);
        ActionMessage errorInfo = new ActionMessage(KeyConstants.ERROR_GLOBAL_MESSAGE);
        
        ActionMessages errors = new ActionMessages();
        errors.add(ActionMessages.GLOBAL_MESSAGE, errorInfo);
        
        saveErrors(request, errors);
        
        UniversalUser loggedInUser = GlobalVariables.getUserSession().getUniversalUser();
        String documentNumber = (String) request.getSession().getAttribute(RiceConstants.DOCUMENT_HTTP_SESSION_KEY);
        PessimisticLockService lockService = KNSServiceLocator.getPessimisticLockService();
        
        if(StringUtils.isNotEmpty(documentNumber)) {
            Document document = KNSServiceLocator.getDocumentService().getByDocumentHeaderId(documentNumber);

            if (ObjectUtils.isNotNull(document)) {
                String budgetLockDescriptor = null;
                for(PessimisticLock lock: document.getPessimisticLocks()) {
                    if(StringUtils.isNotEmpty(lock.getLockDescriptor()) && lock.getLockDescriptor().contains(KraAuthorizationConstants.LOCK_DESCRIPTOR_BUDGET)) {
                        budgetLockDescriptor = lock.getLockDescriptor();
                        break;
                    }
                }
            
                lockService.releaseAllLocksForUser(document.getPessimisticLocks(), loggedInUser);
                List<PessimisticLock> otherBudgetLocks = findMatchingLocksWithGivenDescriptor(budgetLockDescriptor); 
                lockService.releaseAllLocksForUser(otherBudgetLocks, loggedInUser, budgetLockDescriptor);
            }
        }
        
        return returnForward;
    }
    
    private List<PessimisticLock> findMatchingLocksWithGivenDescriptor(String lockDescriptor) {
        BusinessObjectService boService = KNSServiceLocator.getBusinessObjectService();
        Map fieldValues = new HashMap();
        fieldValues.put("lockDescriptor", lockDescriptor);
        List<PessimisticLock> matchingLocks = (List<PessimisticLock>) boService.findMatching(PessimisticLock.class, fieldValues);
        return matchingLocks;
    }

}
