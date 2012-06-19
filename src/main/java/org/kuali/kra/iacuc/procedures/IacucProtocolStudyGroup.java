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
package org.kuali.kra.iacuc.procedures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kuali.kra.iacuc.species.IacucProtocolSpecies;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.protocol.ProtocolAssociate;
import org.kuali.rice.krad.bo.PersistableBusinessObject;

public class IacucProtocolStudyGroup extends ProtocolAssociate { 
    

    private static final long serialVersionUID = 1L;

    private Integer iacucProtocolStudyGroupId; 
    private Integer studyGroupId; 
    private Integer iacucProtocolSpeciesId; 
    private Integer procedureCategoryCode; 
    private Integer procedureCode; 
    private Integer painCategoryCode; 
    private Integer count; 
    
    private IacucProcedureCategory iacucProcedureCategory; 
    private IacucProcedure iacucProcedure; 
    private IacucProtocolSpecies iacucProtocolSpecies; 
    
    private List<IacucProcedurePersonResponsible> iacucProcedurePersonsResponsible;
    private List<IacucProtocolStudyGroupLocation> iacucProtocolStudyGroupLocations;
    
    private Integer procedureBeanIndex;
    
    public IacucProtocolStudyGroup() { 
        setIacucProcedurePersonsResponsible(new ArrayList<IacucProcedurePersonResponsible>());
        setIacucProtocolStudyGroupLocations(new ArrayList<IacucProtocolStudyGroupLocation>());
    } 
    
    public Integer getIacucProtocolStudyGroupId() {
        return iacucProtocolStudyGroupId;
    }

    public void setIacucProtocolStudyGroupId(Integer iacucProtocolStudyGroupId) {
        this.iacucProtocolStudyGroupId = iacucProtocolStudyGroupId;
    }


    public Integer getStudyGroupId() {
        return studyGroupId;
    }

    public void setStudyGroupId(Integer studyGroupId) {
        this.studyGroupId = studyGroupId;
    }

    public Integer getIacucProtocolSpeciesId() {
        return iacucProtocolSpeciesId;
    }

    public void setIacucProtocolSpeciesId(Integer iacucProtocolSpeciesId) {
        this.iacucProtocolSpeciesId = iacucProtocolSpeciesId;
    }

    public Integer getProcedureCategoryCode() {
        return procedureCategoryCode;
    }

    public void setProcedureCategoryCode(Integer procedureCategoryCode) {
        this.procedureCategoryCode = procedureCategoryCode;
    }

    public Integer getProcedureCode() {
        return procedureCode;
    }

    public void setProcedureCode(Integer procedureCode) {
        this.procedureCode = procedureCode;
    }

    public Integer getPainCategoryCode() {
        return painCategoryCode;
    }

    public void setPainCategoryCode(Integer painCategoryCode) {
        this.painCategoryCode = painCategoryCode;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public IacucProcedureCategory getIacucProcedureCategory() {
        if (iacucProcedureCategory == null) {
            refreshReferenceObject("iacucProcedureCategory");
        }
        return iacucProcedureCategory;
    }

    public void setIacucProcedureCategory(IacucProcedureCategory iacucProcedureCategory) {
        this.iacucProcedureCategory = iacucProcedureCategory;
    }

    public IacucProcedure getIacucProcedure() {
        if (iacucProcedure == null) {
            refreshReferenceObject("iacucProcedure");
        }
        return iacucProcedure;
    }

    public void setIacucProcedure(IacucProcedure iacucProcedure) {
        this.iacucProcedure = iacucProcedure;
    }

    @Override
    public void resetPersistenceState() {
        // TODO Auto-generated method stub
        
    }

    public IacucProtocolSpecies getIacucProtocolSpecies() {
        if (iacucProtocolSpecies == null) {
            refreshReferenceObject("iacucProtocolSpecies");
        }
        return iacucProtocolSpecies;
    }

    public void setIacucProtocolSpecies(IacucProtocolSpecies iacucProtocolSpecies) {
        this.iacucProtocolSpecies = iacucProtocolSpecies;
    }

    public List<IacucProcedurePersonResponsible> getIacucProcedurePersonsResponsible() {
        return iacucProcedurePersonsResponsible;
    }

    public void setIacucProcedurePersonsResponsible(List<IacucProcedurePersonResponsible> iacucProcedurePersonsResponsible) {
        this.iacucProcedurePersonsResponsible = iacucProcedurePersonsResponsible;
    }

    public Integer getProcedureBeanIndex() {
        return procedureBeanIndex;
    }

    public void setProcedureBeanIndex(Integer procedureBeanIndex) {
        this.procedureBeanIndex = procedureBeanIndex;
    }

    public List<IacucProtocolStudyGroupLocation> getIacucProtocolStudyGroupLocations() {
        return iacucProtocolStudyGroupLocations;
    }

    public void setIacucProtocolStudyGroupLocations(List<IacucProtocolStudyGroupLocation> iacucProtocolStudyGroupLocations) {
        this.iacucProtocolStudyGroupLocations = iacucProtocolStudyGroupLocations;
    }

    /**  {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        IacucProtocolStudyGroup other = (IacucProtocolStudyGroup) obj;
        if (this.iacucProtocolStudyGroupId == null) {
            if (other.iacucProtocolStudyGroupId != null) {
                return false;
            }
        } else if (!this.iacucProtocolStudyGroupId.equals(other.iacucProtocolStudyGroupId)) {
            return false;
        }
        if (this.studyGroupId == null) {
            if (other.studyGroupId != null) {
                return false;
            }
        } else if (!this.studyGroupId.equals(other.studyGroupId)) {
            return false;
        }
        if (this.iacucProtocolSpeciesId == null) {
            if (other.iacucProtocolSpeciesId != null) {
                return false;
            }
        } else if (!this.iacucProtocolSpeciesId.equals(other.iacucProtocolSpeciesId)) {
            return false;
        }
        return true;
    }

    @Override
    public List<Collection<PersistableBusinessObject>> buildListOfDeletionAwareLists() {      
        List<Collection<PersistableBusinessObject>> deleteAwareList = super.buildListOfDeletionAwareLists();
        deleteAwareList.add((Collection) getIacucProcedurePersonsResponsible());
        deleteAwareList.add((Collection) getIacucProtocolStudyGroupLocations());
        return deleteAwareList;
    }
    
}