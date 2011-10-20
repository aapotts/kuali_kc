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
package org.kuali.kra.coi;

import java.util.LinkedHashMap;

import org.kuali.kra.bo.KraPersistableBusinessObjectBase;
import org.kuali.kra.coi.personfinancialentity.PersonFinIntDisclosure;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.rice.kns.service.SequenceAccessorService;

public class CoiDiscDetail extends KraPersistableBusinessObjectBase { 
    

    private static final long serialVersionUID = 779054686380799255L;
    private Long coiDiscDetailId; 
    private Long coiDisclosureId; 
    private String coiDisclosureNumber; 
    private Integer sequenceNumber; 
    private Integer coiDiscDetailNumber; 
    private Integer moduleCode; 
    private String moduleItemKey; 
    // TODO : since the synthetic key 'personFinIntDisclosureId' is added.  ok to remove entitynumber/entitysequence ?
    private String entityNumber; 
    private Integer entitySequenceNumber; 
    private Integer entityStatusCode; 
    private String description; 
    private String comments; 
    private Long personFinIntDisclosureId;
    private PersonFinIntDisclosure personFinIntDisclosure;
    private CoiEntityStatusCode coiEntityStatusCode; 
    private CoiDisclosure coiDisclosure; 
    
    public CoiDiscDetail() { 

    } 
    
    public CoiDiscDetail(PersonFinIntDisclosure personFinIntDisclosure) { 
        this.setPersonFinIntDisclosureId(personFinIntDisclosure.getPersonFinIntDisclosureId());
        this.setPersonFinIntDisclosure(personFinIntDisclosure);
        this.setEntityNumber(personFinIntDisclosure.getEntityNumber());
        this.setEntitySequenceNumber(personFinIntDisclosure.getSequenceNumber());
        // TODO : not sure about disclosuredetailnumber & expirationdate
        Long nextNumber = KraServiceLocator.getService(SequenceAccessorService.class).getNextAvailableSequenceNumber("SEQ_COI_DISC_DETAILS_ID");
        this.setCoiDiscDetailNumber(nextNumber.intValue());

    } 

    public Long getCoiDiscDetailId() {
        return coiDiscDetailId;
    }

    public void setCoiDiscDetailId(Long coiDiscDetailId) {
        this.coiDiscDetailId = coiDiscDetailId;
    }

    public Long getCoiDisclosureId() {
        return coiDisclosureId;
    }

    public void setCoiDisclosureId(Long coiDisclosureId) {
        this.coiDisclosureId = coiDisclosureId;
    }

    public String getCoiDisclosureNumber() {
        return coiDisclosureNumber;
    }

    public void setCoiDisclosureNumber(String coiDisclosureNumber) {
        this.coiDisclosureNumber = coiDisclosureNumber;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Integer getCoiDiscDetailNumber() {
        return coiDiscDetailNumber;
    }

    public void setCoiDiscDetailNumber(Integer coiDiscDetailNumber) {
        this.coiDiscDetailNumber = coiDiscDetailNumber;
    }

    public Integer getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(Integer moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModuleItemKey() {
        return moduleItemKey;
    }

    public void setModuleItemKey(String moduleItemKey) {
        this.moduleItemKey = moduleItemKey;
    }

    public String getEntityNumber() {
        return entityNumber;
    }

    public void setEntityNumber(String entityNumber) {
        this.entityNumber = entityNumber;
    }

    public Integer getEntitySequenceNumber() {
        return entitySequenceNumber;
    }

    public void setEntitySequenceNumber(Integer entitySequenceNumber) {
        this.entitySequenceNumber = entitySequenceNumber;
    }

    public Integer getEntityStatusCode() {
        return entityStatusCode;
    }

    public void setEntityStatusCode(Integer entityStatusCode) {
        this.entityStatusCode = entityStatusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public CoiEntityStatusCode getCoiEntityStatusCode() {
        return coiEntityStatusCode;
    }

    public void setCoiEntityStatusCode(CoiEntityStatusCode coiEntityStatusCode) {
        this.coiEntityStatusCode = coiEntityStatusCode;
    }

    public CoiDisclosure getCoiDisclosure() {
        return coiDisclosure;
    }

    public void setCoiDisclosure(CoiDisclosure coiDisclosure) {
        this.coiDisclosure = coiDisclosure;
    }

    /** {@inheritDoc} */
    @Override 
    protected LinkedHashMap<String, Object> toStringMapper() {
        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<String, Object>();
        hashMap.put("coiDiscDetailId", this.getCoiDiscDetailId());
        hashMap.put("coiDisclosureId", this.getCoiDisclosureId());
        hashMap.put("coiDisclosureNumber", this.getCoiDisclosureNumber());
        hashMap.put("sequenceNumber", this.getSequenceNumber());
        hashMap.put("coiDiscDetailNumber", this.getCoiDiscDetailNumber());
        hashMap.put("moduleCode", this.getModuleCode());
        hashMap.put("moduleItemKey", this.getModuleItemKey());
        hashMap.put("entityNumber", this.getEntityNumber());
        hashMap.put("entitySequenceNumber", this.getEntitySequenceNumber());
        hashMap.put("entityStatusCode", this.getEntityStatusCode());
        hashMap.put("description", this.getDescription());
        hashMap.put("comments", this.getComments());
        return hashMap;
    }

    public Long getPersonFinIntDisclosureId() {
        return personFinIntDisclosureId;
    }

    public void setPersonFinIntDisclosureId(Long personFinIntDisclosureId) {
        this.personFinIntDisclosureId = personFinIntDisclosureId;
    }

    public PersonFinIntDisclosure getPersonFinIntDisclosure() {
        return personFinIntDisclosure;
    }

    public void setPersonFinIntDisclosure(PersonFinIntDisclosure personFinIntDisclosure) {
        this.personFinIntDisclosure = personFinIntDisclosure;
    }
    
}