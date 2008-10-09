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
import javax.persistence.FetchType;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.CascadeType;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Transient;

import java.util.LinkedHashMap;

import org.kuali.kra.bo.KraPersistableBusinessObjectBase;
import org.kuali.kra.bo.Ynq;
import org.kuali.kra.lookup.keyvalue.YnqAnswersValuesFinder;

@IdClass(org.kuali.kra.proposaldevelopment.bo.id.ProposalPersonYnqId.class)
@Entity
@Table(name="EPS_PROP_PERS_YNQ")
public class ProposalPersonYnq extends KraPersistableBusinessObjectBase {

    @Id
    @Column(name="PROPOSAL_NUMBER")
    private String proposalNumber;
    
	@Id
	@Column(name="PROP_PERSON_NUMBER")
	private Integer proposalPersonNumber;
	
	@Id
	@Column(name="QUESTION_ID")
	private String questionId;
	
	@Column(name="ANSWER")
	private String answer;
	
	@Transient
    private String dummyAnswer;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST})
	@JoinColumn(name="QUESTION_ID", insertable=false, updatable=false)
	private Ynq ynq;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST })
    @JoinColumns({@JoinColumn(name="PROPOSAL_NUMBER", insertable = false, updatable = false),
                  @JoinColumn(name="PROP_PERSON_NUMBER", insertable=false, updatable=false)})
    private ProposalPerson proposalPerson;

	public ProposalPersonYnq(){
		super();
	}

	public Integer getProposalPersonNumber() {
		return proposalPersonNumber;
	}

	public void setProposalPersonNumber(Integer proposalPersonNumber) {
		this.proposalPersonNumber = proposalPersonNumber;
	}

	public String getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}


	@Override 
	protected LinkedHashMap toStringMapper() {
		LinkedHashMap hashMap = new LinkedHashMap();
		hashMap.put("proposalPersonNumber", getProposalPersonNumber());
		hashMap.put("proposalNumber", getProposalNumber());
		hashMap.put("questionId", getQuestionId());
		hashMap.put("answer", getAnswer());
		//hashMap.put("ynq", getYnq());
		return hashMap;
	}

    public Ynq getYnq() {
        return ynq;
    }

    public void setYnq(Ynq ynq) {
        this.ynq = ynq;
    }

    public String getDummyAnswer() {
        return dummyAnswer;
    }

    public void setDummyAnswer(String dummyAnswer) {
        this.dummyAnswer = dummyAnswer;
    }

    public ProposalPerson getProposalPerson() {
        return proposalPerson;
    }

    public void setProposalPerson(ProposalPerson proposalPerson) {
        this.proposalPerson = proposalPerson;
    }
}

