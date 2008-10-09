package org.kuali.kra.proposaldevelopment.bo;

import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.CascadeType;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.IdClass;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.kuali.kra.bo.KraPersistableBusinessObjectBase;
import org.kuali.kra.bo.Rolodex;

/**
 * 
 * This is bo class for proposal location.
 */
@IdClass(org.kuali.kra.proposaldevelopment.bo.id.ProposalLocationId.class)
@Entity
@Table(name="EPS_PROP_LOCATION")
public class ProposalLocation extends KraPersistableBusinessObjectBase {
	@Column(name="LOCATION", nullable=false)
	private String location;
	@Id
	@Column(name="PROPOSAL_NUMBER")
	private String proposalNumber;
    @Column(name="ROLODEX_ID")
	private Integer rolodexId;
    @OneToOne(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST})
	@JoinColumn(name="ROLODEX_ID", insertable=false, updatable=false)
	private Rolodex rolodex;
    @Id
	@Column(name="LOCATION_SEQUENCE_NUMBER")
	private Integer locationSequenceNumber;

    public ProposalLocation() {
        super();
        this.setLocation("");
        this.setRolodexId(0);
        this.setProposalNumber("");
    }

    
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

	public Integer getRolodexId() {
		return rolodexId;
	}

	public void setRolodexId(Integer rolodexId) {
		this.rolodexId = rolodexId;
	}


	@Override 
	protected LinkedHashMap toStringMapper() {
		LinkedHashMap hashMap = new LinkedHashMap();
        hashMap.put("location", getLocation());
        hashMap.put("locationSequenceNumber", getLocationSequenceNumber());
		hashMap.put("proposalNumber", getProposalNumber());
		hashMap.put("rolodexId", getRolodexId());
		return hashMap;
	}

    public Rolodex getRolodex() {
        return rolodex;
    }

    public void setRolodex(Rolodex rolodex) {
        this.rolodex = rolodex;
    }


    public Integer getLocationSequenceNumber() {
        return locationSequenceNumber;
    }


    public void setLocationSequenceNumber(Integer locationSequenceNumber) {
        this.locationSequenceNumber = locationSequenceNumber;
    }
}

