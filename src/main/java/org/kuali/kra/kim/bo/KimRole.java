/*
 * Copyright 2007 The Kuali Foundation
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
package org.kuali.kra.kim.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.kuali.core.bo.PersistableBusinessObjectBase;
import org.kuali.core.service.BusinessObjectService;
import org.kuali.core.util.TypedArrayList;
import org.kuali.kra.infrastructure.KraServiceLocator;

/**
 * A KIM Role defines a role within a given organization, e.g. President,
 * Fiscal Officer, etc.  Roles are assigned one or more Permissions that
 * limit what a person can do.  Persons are assigned to roles either directly
 * or indirectly through groups.  Once assigned to a role, a person obtains
 * all of the permissions in that role.
 *
 * @author Kuali Research Administration Team (kualidev@oncourse.iu.edu)
 */
public class KimRole extends PersistableBusinessObjectBase {

	private static final long serialVersionUID = -8535955276605020423L;
	
	private Long id;
	private String name;
	private String description;
	private String roleTypeCode;
	private Boolean descend;
	private KimRoleType roleType;
	private List<KimRolePermission> rolePermissions;

	/**
	 * Default constructor. Initializes <code>rolePermissions</code>
	 */
	public KimRole() {
	    rolePermissions = new TypedArrayList(KimRolePermission.class);
	}
	
    /**
     * Get the Role's ID.
     * @return the Role's ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the Role's ID.
     * @param id the Role's ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the Role's name.
     * @return the Role's name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the Role's name.
     * @param name the Role's name
     */
    public void setName(String name) {
        this.name = name;
    }
    
	/**
	 * Get the Role's description.
	 * @return the Role's description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the Role's description.
	 * @param description the Role's description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getRoleTypeCode() {
	     return roleTypeCode;
	}

	public void setRoleTypeCode(String roleTypeCode) {
	    this.roleTypeCode = roleTypeCode;
	}

	public KimRoleType getRoleType() {
	    return roleType;
    }

    public void setRoleType(KimRoleType roleType) {
        this.roleType = roleType;
    }
    
    public String getRoleTypeName() {
       return roleType.getDescription();
    }

    public Boolean getDescend() {
        return descend;
    }

    public void setDescend(Boolean descend) {
        this.descend = descend;
    }

    /**
	 * @see org.kuali.core.bo.BusinessObjectBase#toStringMapper()
	 */
	protected LinkedHashMap toStringMapper() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("id", getId());
        map.put("name", getName());
        map.put("roleTypeCode", getRoleTypeCode());
        map.put("description", getDescription());
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
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (!obj.getClass().equals(this.getClass()))
            return false;
        KimRole role = (KimRole) obj;
        return id.equals(role.id);
    }

    /**
     * Gets the rolePermissions attribute. 
     * @return Returns the rolePermissions.
     */
    public List<KimRolePermission> getRolePermissions() {
        return rolePermissions;
    }

    /**
     * Sets the rolePermissions attribute value.
     * @param rolePermissions The rolePermissions to set.
     */
    public void setRolePermissions(List<KimRolePermission> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }

}
