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

package org.kuali.kra.proposaldevelopment.bo;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.FetchType;
import javax.persistence.Basic;
import javax.persistence.Lob;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.CascadeType;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.IdClass;

import java.util.LinkedHashMap;

import org.kuali.kra.bo.KraPersistableBusinessObjectBase;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;

/**
 * Every Proposal can have zero or more Abstracts attached to it.
 * An Abstract is composed of a unique type code and a textual string
 * known as the Abstract Details.
 * 
 * @author Kuali Research Administration Team (kualidev@oncourse.iu.edu)
 */
@IdClass(org.kuali.kra.proposaldevelopment.bo.id.ProposalAbstractId.class)
@Entity
@Table(name="EPS_PROP_ABSTRACT")
public class ProposalAbstract extends KraPersistableBusinessObjectBase {
    
    /**
     * Each Abstract is associated with one and only one proposal
     * which is identified by its unique <code>proposalNumber</code>.
     */
    @Id
	@Column(name="PROPOSAL_NUMBER")
	private String proposalNumber;
    
    /**
     * Identifies what type of abstract this is.
     */
    @Id
	@Column(name="ABSTRACT_TYPE_CODE")
	private String abstractTypeCode;
    
	/**
	 * The user-defined textual string for the abstract.
	 */
	@Lob
	@Basic(fetch=FetchType.LAZY)
	@Column(name="ABSTRACT_DETAILS")
	private String abstractDetails;
	
	/**
	 * The AbstractType instance that the above <code>abstractTypeCode</code>  
	 * refers to.  It is stored here to make it easy for a JSP page to
	 * access the abstract type's description.
	 */
	@ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST})
	@JoinColumn(name="ABSTRACT_TYPE_CODE", insertable=false, updatable=false)
	private AbstractType abstractType;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST })
	@JoinColumn(name="PROPOSAL_NUMBER", insertable = false, updatable = false)
	private ProposalDevelopmentDocument proposalDevelopmentDocument;
	
	/**
	 * Constructs a ProposalAbstract.
	 */
	public ProposalAbstract(){
		super();
		this.proposalNumber = null;
		this.setAbstractTypeCode("");
		this.setAbstractDetails("");
	}

	/**
	 * Gets the Proposal Number.
	 * @return the proposal number this abstract is associated with; 
	 *         null if not associated with any proposal.
	 */
	public String getProposalNumber() {
		return proposalNumber;
	}

	/**
	 * Sets the proposal number that this abstract is associated with.
	 * @param proposalNumber a Proposal's unique number; may be null.
	 */
	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

    /**
     * Gets the Abstract Type Code for this abstract.
     * @return the Abstract Type Code.
     */
    public String getAbstractTypeCode() {
        return abstractTypeCode;
    }

    /**
     * Sets the Abstract Type Code for this abstract.
     * @param abstractTypeCode one of the valid abstract type codes.  The database
     *        can be consulted to find the valid type codes.
     */
    public void setAbstractTypeCode(String abstractTypeCode) {
        this.abstractTypeCode = abstractTypeCode;
        
        // When the abstract type code changes, the corresponding
        // abstractType field must also be updated.  A refresh will
        // cause the abstract type to be read from the database. By
        // the magic of OJB, the data member is automatically updated.
        
        //if (this.abstractTypeCode == null || this.abstractTypeCode.equals("")) {
        //    abstractType = null;
       // } else {
        //    this.refreshReferenceObject("abstractType");
        //}
    }
    
	/**
	 * Gets the Details for this abstract.  
	 * @return the abstract's details.
	 */
	public String getAbstractDetails() {
		return abstractDetails;
	}

	/**
	 * Sets the abstract's details.  Note that the details
	 * are not allowed to be null.  Null is automatically
	 * converted to an empty string.
	 * 
	 * @param abstractDetails a user-defined textual string.
	 */
	public void setAbstractDetails(String abstractDetails) {
	    if (abstractDetails == null) {
	        this.abstractDetails = "";
	    } 
	    else {
	        this.abstractDetails = abstractDetails;
	    }
	}

    /**
     * Gets the AbstractType for this abstract.
     * @return the abstract's AbstractType.
     */
    public AbstractType getAbstractType() {
        return abstractType;
    }

    /**
     * Sets the abstract's AbstractType.
     * @param abstractType the AbstractType.
     */
    public void setAbstractType(AbstractType abstractType) {
        this.abstractType = abstractType;
        this.abstractTypeCode = abstractType.getAbstractTypeCode();
    }
    
    public ProposalDevelopmentDocument getProposalDevelopmentDocument() {
        return proposalDevelopmentDocument;
    }

    public void setProposalDevelopmentDocument(ProposalDevelopmentDocument proposalDevelopmentDocument) {
        this.proposalDevelopmentDocument = proposalDevelopmentDocument;
    }

    @Override
	protected LinkedHashMap toStringMapper() {
		LinkedHashMap hashMap = new LinkedHashMap();
		hashMap.put("proposalNumber", getProposalNumber());
		hashMap.put("abstractTypeCode", getAbstractTypeCode());
		hashMap.put("abstractDetails", getAbstractDetails());
		return hashMap;
	}
}

