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
package org.kuali.kra.coi.personfinancialentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.rice.kns.util.GlobalVariables;

/**
 * 
 * This class is form helper class for financial entity
 */
public class FinancialEntityHelper implements Serializable {

    private static final long serialVersionUID = -5837128667442140384L;
    private FinancialEntityForm form;
    private PersonFinIntDisclosure newPersonFinancialEntity;
    private FinancialEntityReporterUnit newFinancialEntityReporterUnit;
    private FinancialEntityReporter financialEntityReporter;
    private List<PersonFinIntDisclosure> activeFinancialEntities;
    private List<PersonFinIntDisclosure> inactiveFinancialEntities;
    private List<FinancialEntityReporterUnit> deletedUnits;
    private List<FinIntEntityRelType> finEntityRelationshipTypes;
    private int editEntityIndex;
    private List<FinEntityDataMatrixBean> newRelationDetails;
    private List<FinEntityDataMatrixBean> editRelationDetails;
    private Integer newRolodexId;
    
    public FinancialEntityHelper(FinancialEntityForm form) {
        newPersonFinancialEntity = new PersonFinIntDisclosure();
        newPersonFinancialEntity.setCurrentFlag(true);
        financialEntityReporter = new FinancialEntityReporter();
        newPersonFinancialEntity.setPersonId(GlobalVariables.getUserSession().getPrincipalId());
        newPersonFinancialEntity.setFinancialEntityReporterId(financialEntityReporter.getFinancialEntityReporterId());
        setNewFinancialEntityReporterUnit(new FinancialEntityReporterUnit());
        activeFinancialEntities = new ArrayList<PersonFinIntDisclosure>();
        inactiveFinancialEntities = new ArrayList<PersonFinIntDisclosure>();
        finEntityRelationshipTypes = getFinancialEntityService().getFinancialEntityRelationshipTypes();
        deletedUnits = new ArrayList<FinancialEntityReporterUnit>(); 
        newRelationDetails = getFinancialEntityService().getFinancialEntityDataMatrix();
        editRelationDetails = new ArrayList<FinEntityDataMatrixBean>(); 
        editEntityIndex = -1;
        newRolodexId = -1;
        this.form = form;
    }


    public FinancialEntityForm getForm() {
        return form;
    }

    public void setForm(FinancialEntityForm form) {
        this.form = form;
    }


    public PersonFinIntDisclosure getNewPersonFinancialEntity() {
        return newPersonFinancialEntity;
    }


    public void setNewPersonFinancialEntity(PersonFinIntDisclosure newPersonFinancialEntity) {
        this.newPersonFinancialEntity = newPersonFinancialEntity;
    }


    public int getEditEntityIndex() {
        return editEntityIndex;
    }


    public void setEditEntityIndex(int editEntityIndex) {
        this.editEntityIndex = editEntityIndex;
    }


    public List<PersonFinIntDisclosure> getActiveFinancialEntities() {
        return activeFinancialEntities;
    }


    public void setActiveFinancialEntities(List<PersonFinIntDisclosure> activeFinancialEntities) {
        this.activeFinancialEntities = activeFinancialEntities;
    }


    public List<PersonFinIntDisclosure> getInactiveFinancialEntities() {
        return inactiveFinancialEntities;
    }


    public void setInactiveFinancialEntities(List<PersonFinIntDisclosure> inactiveFinancialEntities) {
        this.inactiveFinancialEntities = inactiveFinancialEntities;
    }


    public FinancialEntityReporterUnit getNewFinancialEntityReporterUnit() {
        return newFinancialEntityReporterUnit;
    }


    public void setNewFinancialEntityReporterUnit(FinancialEntityReporterUnit newFinancialEntityReporterUnit) {
        this.newFinancialEntityReporterUnit = newFinancialEntityReporterUnit;
        this.newFinancialEntityReporterUnit.setFinancialEntityReporterId(financialEntityReporter.getFinancialEntityReporterId());
        this.newFinancialEntityReporterUnit.setFinancialEntityReporter(financialEntityReporter);
        this.newFinancialEntityReporterUnit.setPersonId(financialEntityReporter.getPersonId());

    }
    
    private FinancialEntityService getFinancialEntityService() {
        return KraServiceLocator.getService(FinancialEntityService.class);
    }


    public FinancialEntityReporter getFinancialEntityReporter() {
        return financialEntityReporter;
    }
    
    private void refreshFinancialEntityReporter() {
        financialEntityReporter = getFinancialEntityService().getFinancialEntityReporter(
                GlobalVariables.getUserSession().getPrincipalId());
        newPersonFinancialEntity.setFinancialEntityReporterId(financialEntityReporter.getFinancialEntityReporterId());
    }


    public void setFinancialEntityReporter(FinancialEntityReporter financialEntityReporter) {
        this.financialEntityReporter = financialEntityReporter;
    }


    public List<FinancialEntityReporterUnit> getDeletedUnits() {
        return deletedUnits;
    }


    public void setDeletedUnits(List<FinancialEntityReporterUnit> deletedUnits) {
        this.deletedUnits = deletedUnits;
    }


    public List<FinIntEntityRelType> getFinEntityRelationshipTypes() {
        return finEntityRelationshipTypes;
    }


    public void setFinEntityRelationshipTypes(List<FinIntEntityRelType> finEntityRelationshipTypes) {
        this.finEntityRelationshipTypes = finEntityRelationshipTypes;
    }


    public List<FinEntityDataMatrixBean> getNewRelationDetails() {
        return newRelationDetails;
    }


    public void setNewRelationDetails(List<FinEntityDataMatrixBean> newRelationDetails) {
        this.newRelationDetails = newRelationDetails;
    }


    public List<FinEntityDataMatrixBean> getEditRelationDetails() {
        return editRelationDetails;
    }


    public void setEditRelationDetails(List<FinEntityDataMatrixBean> editRelationDetails) {
        this.editRelationDetails = editRelationDetails;
    }

    public void initiate() {
        /* TODO : this is if user to re-enter to financial entity page after leaving it for something else
         * trying to clean up whatever left in the session.
         * Try to combine with the 'init' process when this helper is instantiated ?
         * 
         */
        newPersonFinancialEntity = new PersonFinIntDisclosure();
        newPersonFinancialEntity.setCurrentFlag(true);
        newPersonFinancialEntity.setPersonId(GlobalVariables.getUserSession().getPrincipalId());
        newPersonFinancialEntity.setFinancialEntityReporterId(financialEntityReporter.getFinancialEntityReporterId());
        this.setActiveFinancialEntities(getFinancialEntities(true));
        this.setInactiveFinancialEntities(getFinancialEntities(false));
        this.refreshFinancialEntityReporter();
        this.setNewFinancialEntityReporterUnit(new FinancialEntityReporterUnit());
        newRelationDetails = getFinancialEntityService().getFinancialEntityDataMatrix();
        editRelationDetails = new ArrayList<FinEntityDataMatrixBean>(); 
        newRolodexId = -1;
    }
    
    private List<PersonFinIntDisclosure> getFinancialEntities(boolean active) {
        return getFinancialEntityService().getFinancialEntities(GlobalVariables.getUserSession().getPrincipalId(), active);
    }


    public Integer getNewRolodexId() {
        return newRolodexId;
    }


    public void setNewRolodexId(Integer newRolodexId) {
        this.newRolodexId = newRolodexId;
    }
    

 }
