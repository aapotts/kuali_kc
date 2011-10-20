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
package org.kuali.kra.subaward.document.authorization;
import java.util.HashSet;
import java.util.Set;
import org.kuali.kra.authorization.KcTransactionalDocumentAuthorizerBase;
import org.kuali.kra.subaward.document.SubAwardDocument;
import org.kuali.rice.kim.bo.Person;
import org.kuali.rice.kns.authorization.AuthorizationConstants;
import org.kuali.rice.kns.document.Document;
public class SubAwardDocumentAuthorizer extends KcTransactionalDocumentAuthorizerBase {
    /**
     * @see org.kuali.rice.kns.document.authorization.TransactionalDocumentAuthorizer#getEditModes(
     * org.kuali.rice.kns.document.Document, org.kuali.rice.kim.bo.Person, java.util.Set)
     */
    public Set<String> getEditModes(Document document, Person user, Set<String> currentEditModes) {
        Set<String> editModes = new HashSet<String>();
        
        SubAwardDocument subawardDocument = (SubAwardDocument) document;
        editModes.add(AuthorizationConstants.EditMode.FULL_ENTRY);         
        
        return editModes;
    }
    /**
     * @see org.kuali.kra.authorization.KcTransactionalDocumentAuthorizerBase#canReload(org.kuali.rice.kns.document.Document, org.kuali.rice.kim.bo.Person)
     */
    @Override
    protected boolean canReload(Document document, Person user) {
        return canEdit(document, user);
    }

    public boolean canOpen(Document arg0, Person arg1) {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean canInitiate(String arg0, Person arg1) {
        // TODO Auto-generated method stub
        return true;
    }

}
