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

package org.kuali.kra.irb.committee.document;

import java.util.ArrayList;
import java.util.List;

import org.kuali.core.document.Copyable;
import org.kuali.core.document.SessionDocument;
import org.kuali.kra.bo.RolePersons;
import org.kuali.kra.document.ResearchDocumentBase;
import org.kuali.kra.irb.committee.bo.Committee;

/**
 * The Committee Document wraps a single Committee BO.  
 * The document is necessary for workflow.
 */
@SuppressWarnings("serial")
public class CommitteeDocument extends ResearchDocumentBase implements Copyable, SessionDocument { 
	
    /*
     * It may be seem strange, but we use a list in order to store a
     * single Committtee BO.  This is due to a problem for one-to-one
     * relationships within OJB in regards to anonymous keys.  We are
     * forced to use a one-to-many relationship.
     */
    private List<Committee> committeeList = new ArrayList<Committee>();
    
    /**
     * Constructs a CommitteeDocument object
     */
	public CommitteeDocument() {
        committeeList.add(new Committee());
	} 
	
    /**
     * @see org.kuali.kra.document.ResearchDocumentBase#initialize()
     */
    public void initialize() {
    }

    /**
     * Get the Committee BO.  This is a convenience method for easily
     * obtaining the single Committee BO in the list.
     * @return the Committee BO
     */
    public Committee getCommittee() {
        return committeeList.get(0);
    }

    /**
     * Set the Committee BO.  This is a convenience method to easily
     * insert the committee into the list.
     * @param committee the Committee BO
     */
    public void setCommittee(Committee committee) {
        committeeList.set(0, committee);
    }

    /**
     * Get the list of committees.  
     * WARNING: Developers should never call this method.
     *          This method is for OJB use only.
     * @return the list with the single committee
     */
    public List<Committee> getCommitteeList() {
        return committeeList;
    }

    /**
     * Set the list of committees.
     * WARNING: Developers should never call this method.
     *          This method is for OJB use only.
     * @param committeeList the list containing the single committee
     */
    public void setCommitteeList(List<Committee> committeeList) {
        this.committeeList = committeeList;
    }

    /**
     * @see org.kuali.core.bo.PersistableBusinessObjectBase#buildListOfDeletionAwareLists()
     */
    @SuppressWarnings("unchecked")
    @Override
    public List buildListOfDeletionAwareLists() {
        List managedLists = super.buildListOfDeletionAwareLists();
        managedLists.add(committeeList);
        return managedLists;
    }
    
    /**
     * @see org.kuali.kra.document.ResearchDocumentBase#getAllRolePersons()
     */
    protected List<RolePersons> getAllRolePersons() {
        return new ArrayList<RolePersons>();
    }
}