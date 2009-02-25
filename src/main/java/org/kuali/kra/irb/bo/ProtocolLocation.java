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
import org.kuali.kra.bo.Organization;
import org.kuali.kra.bo.Rolodex;
import org.kuali.kra.infrastructure.Constants;

/**
 * This class represents the Protocol Location Business Object.
 */
public class ProtocolLocation extends KraPersistableBusinessObjectBase { 
	
	/**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 6509347537504066578L;
    private Integer protocolLocationId; 
	private Long protocolId; 
	private String protocolNumber; 
	private Integer sequenceNumber; 
	private String protocolOrganizationTypeCode; 
	private String organizationId; 
	private Integer rolodexId; 
	
    private Rolodex rolodex;
	private Organization organization; 
	private ProtocolOrganizationType protocolOrganizationType; 
	
	/**
	 * Constructs a ProtocolLocation.java.
	 */
	public ProtocolLocation() { 
	    /**
	     * Set default protocol organization type code.
	     * Initially set Organization type code drop down to this value. 
	     */
	    setProtocolOrganizationTypeCode(Constants.DEFAULT_PROTOCOL_ORGANIZATION_TYPE_CODE);
	} 
	
	public Integer getProtocolLocationId() {
		return protocolLocationId;
	}

	public void setProtocolLocationId(Integer protocolLocationId) {
		this.protocolLocationId = protocolLocationId;
	}

	public Long getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(Long protocolId) {
		this.protocolId = protocolId;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getProtocolOrganizationTypeCode() {
		return protocolOrganizationTypeCode;
	}

	public void setProtocolOrganizationTypeCode(String protocolOrganizationTypeCode) {
		this.protocolOrganizationTypeCode = protocolOrganizationTypeCode;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getRolodexId() {
		return rolodexId;
	}

	public void setRolodexId(Integer rolodexId) {
		this.rolodexId = rolodexId;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public ProtocolOrganizationType getProtocolOrganizationType() {
		return protocolOrganizationType;
	}

	public void setProtocolOrganizationType(ProtocolOrganizationType protocolOrganizationType) {
		this.protocolOrganizationType = protocolOrganizationType;
	}

	/**
	 * @see org.kuali.core.bo.BusinessObjectBase#toStringMapper()
	 */
	@Override 
	protected LinkedHashMap<String,Object> toStringMapper() {
		LinkedHashMap<String,Object> hashMap = new LinkedHashMap<String,Object>();
		hashMap.put("protocolLocationId", getProtocolLocationId());
		hashMap.put("protocolId", getProtocolId());
		hashMap.put("protocolNumber", getProtocolNumber());
		hashMap.put("sequenceNumber", getSequenceNumber());
		hashMap.put("protocolOrganizationTypeCode", getProtocolOrganizationTypeCode());
		hashMap.put("organizationId", getOrganizationId());
		hashMap.put("rolodexId", getRolodexId());
		return hashMap;
	}

    public Rolodex getRolodex() {
        return rolodex;
    }

    public void setRolodex(Rolodex rolodex) {
        this.rolodex = rolodex;
    }

    public void init(Protocol protocol) {
        setProtocolLocationId(null);
        setProtocolId(protocol.getProtocolId());
        setProtocolNumber(protocol.getProtocolNumber());
    }

}