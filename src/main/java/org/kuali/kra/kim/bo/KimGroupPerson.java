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
package org.kuali.kra.kim.bo;

import javax.persistence.Version;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.CascadeType;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.IdClass;

import java.util.LinkedHashMap;

import org.kuali.core.bo.PersistableBusinessObjectBase;

/**
 * The KIM Group-Person represents the association of a Group with a 
 * Person.  The Group contains the Person as a member.
 *
 * @author Kuali Research Administration Team (kualidev@oncourse.iu.edu)
 */
@IdClass(org.kuali.kra.kim.bo.id.KimGroupPersonId.class)
@Entity
@Table(name="KIM_GROUPS_PERSONS_T")
public class KimGroupPerson extends PersistableBusinessObjectBase {
    
    private static final long serialVersionUID = 7568757741480822886L;
    
    @Id
	@Column(name="GROUP_ID")
	private Long groupId;
    
    @Id
	@Column(name="PERSON_ID")
	private Long personId;

    /**
     * Set the Group's ID.
     * @param groupId the Group's ID
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
    
    /**
     * Get the Group's ID.
     * @return the Group's ID
     */
    public Long getGroupId() {
        return groupId;
    }
    
    /**
     * Set the Person's ID.
     * @param personId the Person's ID
     */
    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    /**
     * Get the Person's ID
     * @return the Person's ID
     */
    public Long getPersonId() {
        return personId;
    }

    /**
     * @see org.kuali.core.bo.BusinessObjectBase#toStringMapper()
     */
    @Override
    protected LinkedHashMap toStringMapper() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("groupId", getGroupId());
        map.put("personId", getPersonId());
        return map;
    }
    
    /**
     * @see org.kuali.core.bo.PersistableBusinessObjectBase#refresh()
     */
    public void refresh() {
        // not going to implement unless necessary
    }
    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!obj.getClass().equals(this.getClass()) ) return false;
        KimGroupPerson groupPerson = (KimGroupPerson)obj;
        return groupId.equals(groupPerson.groupId) &&
               personId.equals(groupPerson.personId);
    }
}

