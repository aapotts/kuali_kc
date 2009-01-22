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
package org.kuali.kra.irb.bo;

import java.util.LinkedHashMap;

import org.kuali.kra.bo.KraPersistableBusinessObjectBase;

public class ProtocolPersonRole extends KraPersistableBusinessObjectBase { 
	
	/**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -4525940858799917386L;
    private String protocolPersonRoleId; 
	private String description; 
	private boolean unitDetailsRequired;
	
	public ProtocolPersonRole() { 

	} 
	
	public String getProtocolPersonRoleId() {
		return protocolPersonRoleId;
	}

	public void setProtocolPersonRoleId(String protocolPersonRoleId) {
		this.protocolPersonRoleId = protocolPersonRoleId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override 
	protected LinkedHashMap<String,Object> toStringMapper() {
		LinkedHashMap<String,Object> hashMap = new LinkedHashMap<String,Object>();
		hashMap.put("protocolPersonRoleId", getProtocolPersonRoleId());
		hashMap.put("description", getDescription());
		return hashMap;
	}

    public boolean isUnitDetailsRequired() {
        return unitDetailsRequired;
    }

    public void setUnitDetailsRequired(boolean unitDetailsRequired) {
        this.unitDetailsRequired = unitDetailsRequired;
    }
	
}