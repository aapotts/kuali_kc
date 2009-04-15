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

package org.kuali.kra.bo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Unit extends KraPersistableBusinessObjectBase {
	private String unitNumber;
	private String parentUnitNumber;
	private String organizationId;
	private String unitName;
	private Unit parentUnit;
    private List<UnitAdministrator> unitAdministrators;

    private Organization organization;

    public Unit() {
        super();
        unitAdministrators = new ArrayList<UnitAdministrator>();
    }

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}


	public String getParentUnitNumber() {
		return parentUnitNumber;
	}

	public void setParentUnitNumber(String parentUnitNumber) {
		this.parentUnitNumber = parentUnitNumber;
	}

	public String getOrganizationId() {
        if (organizationId == null && this.getParentUnit() != null && this.getParentUnit().getUnitNumber() != null) {
            //will recurse up hierarchy until an Organization Id is found
            return this.getParentUnit().getOrganizationId();
        }
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}


	@Override 
	protected LinkedHashMap toStringMapper() {
		LinkedHashMap hashMap = new LinkedHashMap();
		hashMap.put("unitNumber", getUnitNumber());
		hashMap.put("parentUnitNumber", getParentUnitNumber());
		hashMap.put("organizationId", getOrganizationId());
		hashMap.put("unitName", getUnitName());
		return hashMap;
	}

    /**
     * Gets the organization attribute. 
     * @return Returns the organization.
     */
    public Organization getOrganization() {
        return organization;
    }

    /**
     * Sets the organization attribute value.
     * @param organization The organization to set.
     */
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Unit getParentUnit() {
        return parentUnit;
    }

    public void setParentUnit(Unit parentUnit) {
        this.parentUnit = parentUnit;
    }

    public List<UnitAdministrator> getUnitAdministrators() {
        return unitAdministrators;
    }

    public void setUnitAdministrators(List<UnitAdministrator> unitAdministrators) {
        this.unitAdministrators = unitAdministrators;
    }

    @Override
    public List buildListOfDeletionAwareLists() {
        // TODO : need this ?
        List managedLists = super.buildListOfDeletionAwareLists();
        managedLists.add(getUnitAdministrators());
        return managedLists;
    }
    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (!(obj instanceof Unit))
            return false;
        Unit unit = (Unit) obj;
        return unitNumber.equals(unit.unitNumber);
    }

}
