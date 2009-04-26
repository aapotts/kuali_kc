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
package org.kuali.kra.proposaldevelopment.service.impl;

import static java.util.Collections.sort;
import static org.kuali.kra.logging.BufferedLogger.debug;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.kra.authorization.KraAuthorizationConstants;
import org.kuali.kra.bo.ExemptionType;
import org.kuali.kra.bo.Organization;
import org.kuali.kra.bo.Person;
import org.kuali.kra.bo.Unit;
import org.kuali.kra.budget.bo.BudgetVersionOverview;
import org.kuali.kra.budget.document.BudgetDocument;
import org.kuali.kra.budget.service.BudgetService;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.infrastructure.PermissionConstants;
import org.kuali.kra.lookup.keyvalue.ExtendedPersistableBusinessObjectValuesFinder;
import org.kuali.kra.lookup.keyvalue.KeyLabelPairComparator;
import org.kuali.kra.proposaldevelopment.bo.ProposalColumnsToAlter;
import org.kuali.kra.proposaldevelopment.bo.ProposalExemptNumber;
import org.kuali.kra.proposaldevelopment.bo.ProposalLocation;
import org.kuali.kra.proposaldevelopment.bo.ProposalOverview;
import org.kuali.kra.proposaldevelopment.bo.ProposalSpecialReview;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.proposaldevelopment.rule.event.AddBudgetVersionEvent;
import org.kuali.kra.proposaldevelopment.rules.BudgetVersionRule;
import org.kuali.kra.proposaldevelopment.service.ProposalDevelopmentService;
import org.kuali.kra.proposaldevelopment.web.struts.form.ProposalDevelopmentForm;
import org.kuali.kra.service.KraPersistenceStructureService;
import org.kuali.kra.service.UnitAuthorizationService;
import org.kuali.kra.web.struts.action.AuditActionHelper;
import org.kuali.rice.kew.exception.WorkflowException;
import org.kuali.rice.kns.bo.BusinessObject;
import org.kuali.rice.kns.bo.PersistableBusinessObject;
import org.kuali.rice.kns.document.authorization.PessimisticLock;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.service.DocumentService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.service.KualiConfigurationService;
import org.kuali.rice.kns.service.KualiRuleService;
import org.kuali.rice.kns.service.PessimisticLockService;
import org.kuali.rice.kns.util.AuditCluster;
import org.kuali.rice.kns.util.AuditError;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.util.ObjectUtils;
import org.kuali.rice.kns.util.TypedArrayList;
import org.kuali.rice.kns.web.ui.KeyLabelPair;

// TODO : extends PersistenceServiceStructureImplBase is a hack to temporarily resolve get class descriptor.
public class ProposalDevelopmentServiceImpl implements ProposalDevelopmentService {
    private BusinessObjectService businessObjectService;
    private UnitAuthorizationService unitAuthService;
    private KraPersistenceStructureService kraPersistenceStructureService;
    private BudgetService budgetService;
    private PessimisticLockService pessimisticLockService;
    private BudgetVersionRule budgetVersionRule;
    
    /**
     * This method...
     * 
     * @param proposalDevelopmentDocument
     * @param proposalOrganization
     */
    public void initializeUnitOrganzationLocation(ProposalDevelopmentDocument proposalDevelopmentDocument) {
        Organization proposalOrganization = proposalDevelopmentDocument.getOrganization();

        // Unit number chosen, set Organzation, etc...
        if (proposalDevelopmentDocument.getOwnedByUnitNumber() != null && proposalOrganization == null) {
            // get Lead Unit details
            proposalDevelopmentDocument.refreshReferenceObject("ownedByUnit");
            String organizationId = proposalDevelopmentDocument.getOwnedByUnit().getOrganizationId();

            // get Organzation assoc. w/ Lead Unit
            proposalDevelopmentDocument.setOrganizationId(organizationId);
            proposalDevelopmentDocument.refreshReferenceObject("organization");
            proposalOrganization = proposalDevelopmentDocument.getOrganization();
            proposalDevelopmentDocument.setPerformingOrganizationId(organizationId);
            proposalDevelopmentDocument.refreshReferenceObject("performingOrganization");

            // initialize Proposal Locations with Organization details
            if (proposalDevelopmentDocument.getProposalLocations().isEmpty()) {
                ProposalLocation newProposalLocation = new ProposalLocation();
                newProposalLocation.setLocation(proposalOrganization.getOrganizationName());
                newProposalLocation.setRolodexId(proposalOrganization.getContactAddressId());
                newProposalLocation.refreshReferenceObject("rolodex");
                newProposalLocation.setLocationSequenceNumber(proposalDevelopmentDocument
                        .getDocumentNextValue(Constants.PROPOSAL_LOCATION_SEQUENCE_NUMBER));
                proposalDevelopmentDocument.getProposalLocations().add(0, newProposalLocation);
            }

        }
    }

    public List<Unit> getDefaultModifyProposalUnitsForUser(String userName) {
        Map queryMap = new HashMap();
        queryMap.put("userName", userName);


        List<Person> persons = (List<Person>) getBusinessObjectService().findMatching(Person.class, queryMap);

        if (persons.size() > 1) {
            throw new RuntimeException("More than one person retieved for userName: " + userName);
        }

        Person person = persons.get(0);

        return unitAuthService.getUnits(person.getUserName(), PermissionConstants.CREATE_PROPOSAL);
    }

    /**
     * Gets units for the given names. Useful when you know what you want.
     * 
     * @param unitNumbers varargs representation of unitNumber array
     * @return Collection<Unit>
     */
    private Collection<Unit> getUnitsWithNumbers(String... unitNumbers) {
        Collection<Unit> retval = new ArrayList<Unit>();

        for (String unitNumber : unitNumbers) {
            Map query_map = new HashMap();
            query_map.put("unitNumber", unitNumber);
            retval.add((Unit) getBusinessObjectService().findByPrimaryKey(Unit.class, query_map));
        }

        return retval;
    }

    /**
     * Accessor for <code>{@link BusinessObjectService}</code>
     * 
     * @param bos BusinessObjectService
     */
    public void setBusinessObjectService(BusinessObjectService bos) {
        businessObjectService = bos;
    }

    /**
     * Accessor for <code>{@link BusinessObjectService}</code>
     * 
     * @return BusinessObjectService
     */
    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }
    
    /**
     * Set the Unit Authorization Service.  Injected by Spring.
     * @param unitAuthService
     */
    public void setUnitAuthorizationService(UnitAuthorizationService unitAuthService) {
        this.unitAuthService = unitAuthService;
    }

    /**
     * 
     * @see org.kuali.kra.proposaldevelopment.service.ProposalDevelopmentService#validateBudgetAuditRule(org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument)
     */
    public boolean validateBudgetAuditRule(ProposalDevelopmentDocument proposalDevelopmentDocument) throws Exception {
        boolean valid = true;
        boolean finalAndCompleteBudgetVersionFound = false;
        boolean budgetVersionsExists = false;
        List<AuditError> auditErrors = new ArrayList<AuditError>();
        String budgetStatusCompleteCode = KraServiceLocator.getService(KualiConfigurationService.class).getParameter(
                Constants.PARAMETER_MODULE_BUDGET, Constants.PARAMETER_COMPONENT_DOCUMENT, Constants.BUDGET_STATUS_COMPLETE_CODE).getParameterValue();
        for (BudgetVersionOverview budgetVersion : proposalDevelopmentDocument.getBudgetVersionOverviews()) {
            budgetVersionsExists = true;
            if (budgetVersion.isFinalVersionFlag()) {
                valid &= applyAuditRuleForBudgetDocument(budgetVersion);
                if (proposalDevelopmentDocument.getBudgetStatus()!= null 
                        && proposalDevelopmentDocument.getBudgetStatus().equals(budgetStatusCompleteCode)) {
                    finalAndCompleteBudgetVersionFound = true;
                }
            }
        }
        if(budgetVersionsExists && !finalAndCompleteBudgetVersionFound){
            auditErrors.add(new AuditError("document.budgetVersionOverview", KeyConstants.AUDIT_ERROR_NO_BUDGETVERSION_COMPLETE_AND_FINAL, Constants.BUDGET_VERSIONS_PAGE + "." + Constants.BUDGET_VERSIONS_PANEL_ANCHOR));
            valid &= false;
        }
        if (auditErrors.size() > 0) {
            GlobalVariables.getAuditErrorMap().put("budgetVersionErrors", new AuditCluster(Constants.BUDGET_VERSION_PANEL_NAME, auditErrors, Constants.AUDIT_ERRORS));
        }

        return valid;
    }
    
    /**
     * 
     * @see org.kuali.kra.proposaldevelopment.service.ProposalDevelopmentService#validateBudgetAuditRuleBeforeSaveBudgetVersion(org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument)
     */
    public boolean validateBudgetAuditRuleBeforeSaveBudgetVersion(ProposalDevelopmentDocument proposalDevelopmentDocument)
            throws Exception {
        boolean valid = true;
        for (BudgetVersionOverview budgetVersion : proposalDevelopmentDocument.getBudgetVersionOverviews()) {
            String budgetStatusCompleteCode = KraServiceLocator.getService(KualiConfigurationService.class).getParameter(
                    Constants.PARAMETER_MODULE_BUDGET, Constants.PARAMETER_COMPONENT_DOCUMENT,
                    Constants.BUDGET_STATUS_COMPLETE_CODE).getParameterValue();
            // if status is complete and version is not final, then business rule will take care of it
            if (budgetVersion.isFinalVersionFlag() && budgetVersion.getBudgetStatus() != null
                    && budgetVersion.getBudgetStatus().equals(budgetStatusCompleteCode)) {
                valid &= applyAuditRuleForBudgetDocument(budgetVersion);
            }
        }

        if (!valid) {
            // audit warnings are OK.  only audit errors prevent to change to complete status.
            valid = true;
            for (Object key : GlobalVariables.getAuditErrorMap().keySet()) {
                AuditCluster auditCluster = (AuditCluster)GlobalVariables.getAuditErrorMap().get(key);
                if (auditCluster.getCategory().equals(Constants.AUDIT_ERRORS)) {
                    valid = false;
                    break;
                }
            }
        }

        return valid;
    }

    private boolean applyAuditRuleForBudgetDocument(BudgetVersionOverview budgetVersion) throws Exception {
        DocumentService documentService = KraServiceLocator.getService(DocumentService.class);
        BudgetDocument budgetDocument = (BudgetDocument) documentService.getByDocumentHeaderId(budgetVersion.getDocumentNumber());
        return new AuditActionHelper().auditUnconditionally(budgetDocument);

    }

    /**
     * 
     * @see org.kuali.kra.proposaldevelopment.service.ProposalDevelopmentService#getExemptionTypeKeyValues()
     */
    public List<KeyLabelPair> getExemptionTypeKeyValues() {
        // TODO this is to get the key values pair for exempt numbers - any other options
        // put in service ?
        ExtendedPersistableBusinessObjectValuesFinder finder = new ExtendedPersistableBusinessObjectValuesFinder();
        finder.setBusinessObjectClass(ExemptionType.class);
        finder.setKeyAttributeName("exemptionTypeCode");
        finder.setLabelAttributeName("description");
        List<KeyLabelPair> exemptionTypes = finder.getKeyValues();
        sort(exemptionTypes, new KeyLabelPairComparator());
        return exemptionTypes;
    }


    public void populateExemptNumbersToForm(ProposalDevelopmentForm proposalDevelopmentForm) {
        // initial load
        List<ProposalSpecialReview> proposalSpecialReviews = proposalDevelopmentForm.getProposalDevelopmentDocument()
                .getPropSpecialReviews();
        int i = 0;
        List<String[]> documentExemptNumbers = proposalDevelopmentForm.getDocumentExemptNumbers();
        for (ProposalSpecialReview proposalSpecialReview : proposalSpecialReviews) {
            List<ProposalExemptNumber> proposalExemptNumbers = proposalSpecialReview.getProposalExemptNumbers();
            String[] exemptNumbers = null;
            //if (proposalExemptNumbers != null && proposalExemptNumbers.size() > 0) {
                if (documentExemptNumbers == null  || documentExemptNumbers.size() - 1 < i ) {
                    if (documentExemptNumbers == null) {
                        documentExemptNumbers = new ArrayList<String[]>();
                        proposalDevelopmentForm.setDocumentExemptNumbers(documentExemptNumbers);
                    }
                    documentExemptNumbers.add(exemptNumbers);
                }
                exemptNumbers = documentExemptNumbers.get(i++);
                if ((exemptNumbers == null || exemptNumbers.length == 0) && proposalExemptNumbers != null
                        && proposalExemptNumbers.size() > 0) {
                    int idx = 0;
                    if (exemptNumbers == null || exemptNumbers.length == 0) {
                        exemptNumbers = new String[proposalExemptNumbers.size()];
                        documentExemptNumbers.remove(i-1);
                        documentExemptNumbers.add(i-1,exemptNumbers);
                    }
                    for (Object proposalExemptNumber : proposalExemptNumbers) {
                        exemptNumbers[idx++] = (((ProposalExemptNumber) proposalExemptNumber).getExemptionTypeCode());
                    }
                }
           // }
        }

    }


    public void populateProposalExempNumbers(ProposalDevelopmentForm proposalDevelopmentForm) {
        // initial load
        List<ProposalSpecialReview> proposalSpecialReviews = proposalDevelopmentForm.getProposalDevelopmentDocument()
                .getPropSpecialReviews();
        int i = 0;
        List<String[]> documentExemptNumbers = proposalDevelopmentForm.getDocumentExemptNumbers();
        for (ProposalSpecialReview proposalSpecialReview : proposalSpecialReviews) {
            List newProposalExemptNumbers = new TypedArrayList(ProposalExemptNumber.class);
            
            if (documentExemptNumbers != null && documentExemptNumbers.size() > 0) {
                String[] exemptNumbers = documentExemptNumbers.get(i++);
                if (exemptNumbers !=null) {
                    for (String exemptNumber : exemptNumbers) {
                        if (StringUtils.isNotBlank(exemptNumber)) {
                            ProposalExemptNumber proposalExemptNumber = new ProposalExemptNumber();
                            proposalExemptNumber.setProposalNumber(proposalSpecialReview.getProposalNumber());
                            proposalExemptNumber.setSpecialReviewNumber((proposalSpecialReview.getSpecialReviewNumber()));
                            proposalExemptNumber.setExemptionTypeCode(exemptNumber);
                            // TODO : below is to prevent optimistic locking issue. Tested to see if it is really needed.
                            // if (proposalExemptNumber != null && proposalExemptNumbers.size() > newProposalExemptNumbers.size()) {
                            // proposalExemptNumber.setVersionNumber(((ProposalExemptNumber)proposalExemptNumbers.get(proposalExemptNumbers.size()
                            // - 1)).getVersionNumber());
                            // }
                            newProposalExemptNumbers.add(proposalExemptNumber);
                        }
                    }
                }
            }
            proposalSpecialReview.setProposalExemptNumbers(newProposalExemptNumbers);
        }

    }

    public String populateProposalEditableFieldMetaDataForAjaxCall(String proposalNumber, String editableFieldDBColumn) {
        return populateProposalEditableFieldMetaData(proposalNumber, editableFieldDBColumn);
    }
    
    private ProposalOverview getProposalOverview(String proposalNumber) {
        Map<String, Object> primaryKeys = new HashMap<String, Object>();
        primaryKeys.put("proposalNumber", proposalNumber);
        ProposalOverview currentProposal = (ProposalOverview) businessObjectService.findByPrimaryKey(ProposalOverview.class, primaryKeys);
        return currentProposal;
    }

    private String getLookupDisplayValue(String lookupClassName, String value, String displayAttributeName) {
        Map<String, Object> primaryKeys = new HashMap<String, Object>();
        List<String> lookupClassPkFields = null;
        Class lookupClass = null;
        String displayValue = "";
        String returnValue = "";
        PersistableBusinessObject businessObject = null;
        
        if(StringUtils.isNotEmpty(lookupClassName)) {
            try {
                lookupClass = Class.forName(lookupClassName);
                lookupClassPkFields = (List<String>) kraPersistenceStructureService.getPrimaryKeys(lookupClass);
            }
            catch (ClassNotFoundException e) {}
            
            if(CollectionUtils.isNotEmpty(lookupClassPkFields)){
                returnValue = StringUtils.isNotEmpty(lookupClassPkFields.get(0)) ? lookupClassPkFields.get(0) : "";
                
                if(StringUtils.isNotEmpty(value)) {
                    primaryKeys.put(lookupClassPkFields.get(0), value);
                    businessObject = (PersistableBusinessObject) businessObjectService.findByPrimaryKey(lookupClass, primaryKeys);
                    if(businessObject != null) {
                        displayValue = getPropertyValue(businessObject, displayAttributeName);
                        displayValue = StringUtils.isNotEmpty(displayValue) ? displayValue : "";
                    }
                } 
            }
        } 
        
        return returnValue + "," + displayAttributeName + "," + displayValue;
    }
    
    private String getPropertyValue(BusinessObject businessObject, String fieldName) {
        String displayValue = "";
        try {
            displayValue = (String) ObjectUtils.getPropertyValue(businessObject, fieldName);
        }
        //Might happen due to Unknown Property Exception 
        catch (RuntimeException e) { }
        return displayValue;  
    }
    
    public Object getProposalFieldValueFromDBColumnName(String proposalNumber, String dbColumnName) {
        Object fieldValue = null;
        Map<String, String> fieldMap = kraPersistenceStructureService.getDBColumnToObjectAttributeMap(ProposalOverview.class);
        String proposalAttributeName = fieldMap.get(dbColumnName);
        if(StringUtils.isNotEmpty(proposalAttributeName)) {
            ProposalOverview currentProposal =  getProposalOverview(proposalNumber);
            if(currentProposal != null) {
                fieldValue = ObjectUtils.getPropertyValue(currentProposal, proposalAttributeName);
            }
        }
        return fieldValue;
    }
    
    private String populateProposalEditableFieldMetaData(String proposalNumber, String editableFieldDBColumn) { 
        String returnValue = "";
        if(GlobalVariables.getErrorMap() != null) {
            GlobalVariables.getErrorMap().clear();
        }
        
        Object fieldValue = getProposalFieldValueFromDBColumnName(proposalNumber, editableFieldDBColumn);
        Map<String, Object> primaryKeys = new HashMap<String, Object>();
        primaryKeys.put("columnName", editableFieldDBColumn);
        ProposalColumnsToAlter editableColumn = (ProposalColumnsToAlter) businessObjectService.findByPrimaryKey(ProposalColumnsToAlter.class, primaryKeys);
        
        if(editableColumn.getHasLookup()) {
             returnValue = getLookupDisplayValue(editableColumn.getLookupClass(), (fieldValue != null ? fieldValue.toString() : ""), editableColumn.getLookupReturn());
        } else if (fieldValue != null && editableColumn.getDataType().equalsIgnoreCase("DATE")){
            returnValue = ",," + KNSServiceLocator.getDateTimeService().toString((Date) fieldValue, "MM/dd/yyyy");
        } else if (fieldValue != null) {
            returnValue = ",," + fieldValue.toString();
        } else {
            returnValue = ",,";
        }

        returnValue+=","+editableColumn.getDataType();
        returnValue+=","+editableColumn.getHasLookup();
        returnValue+=","+editableColumn.getLookupClass();
        
        return returnValue; 
    }

    /**
     * Service method for adding a {@link BudgetVersionOverview} to a {@link ProposalDevelopmentDocument}. If a 
     * {@link BudgetVersionOverview} instance with the  <code>versionName</code> already exists 
     * in the {@link ProposalDevelopmentDocument}, then a hard error will occur. Try it and you'll see what I mean.
     * 
     * @param document instance to add {@link BudgetVersionOverview} to
     * @param versionName of the {@link BudgetVersionOverview}
     */
    public void addBudgetVersion(ProposalDevelopmentDocument document, String versionName) throws WorkflowException {
        if (!isNewBudgetVersionNameValid(document, versionName)) {
            debug("Buffered Version not Valid");
            return;
        }

        BudgetDocument newBudgetDoc = getBudgetService().getNewBudgetVersion(document, versionName);
        
        PessimisticLock budgetLockForProposalDoc = null;

        for(PessimisticLock pdLock : document.getPessimisticLocks()) {
            if(pdLock.getLockDescriptor().contains(KraAuthorizationConstants.LOCK_DESCRIPTOR_BUDGET)) {
                budgetLockForProposalDoc = pdLock;
                break;
            }
        }

        try {
            PessimisticLock budgetLockForBudgetDoc = getPessimisticLockService().generateNewLock(newBudgetDoc.getDocumentNumber(), budgetLockForProposalDoc.getLockDescriptor(), budgetLockForProposalDoc.getOwnedByUser());
            newBudgetDoc.addPessimisticLock(budgetLockForBudgetDoc);
        } catch (Exception e) {
            
        }

        document.addNewBudgetVersion(newBudgetDoc, versionName, false);
    }

    /**
     * Runs business rules on the given name of a {@link BudgetVersionOverview} instance and {@link ProposalDevelopmentDocument} instance to 
     * determine if it is ok to add a {@link BudgetVersionOverview} instance to a {@link BudgetDocument} instance. If the business rules fail, 
     * this should be false and there will be errors in the error map.<br/>
     *
     * <p>Takes care of all the setup and calling of the {@link KualiRuleService}. Uses the {@link AddBudgetVersionEvent}.</p>
     *
     * @param document {@link ProposalDevelopmentDocument} to validate against
     * @param name of the pseudo-{@link BudgetVersionOverview} instance to validate
     * @returns true if the rules passed, false otherwise
     */
    private boolean isNewBudgetVersionNameValid(ProposalDevelopmentDocument document,  String name) {
        debug("Invoking budgetrule " + getBudgetVersionRule());
        return new AddBudgetVersionEvent(document, name).invokeRuleMethod(getBudgetVersionRule());
    }

    public KraPersistenceStructureService getKraPersistenceStructureService() {
        return kraPersistenceStructureService;
    }

    public void setKraPersistenceStructureService(KraPersistenceStructureService kraPersistenceStructureService) {
        this.kraPersistenceStructureService = kraPersistenceStructureService;
    }

    /**
     * Retrieve injected <code>{@link BudgetService}</code> singleton
     * 
     * @return BudgetService
     */
    public BudgetService getBudgetService() {
        return budgetService;
    }

    /**
     * Inject <code>{@link BudgetService}</code> singleton
     * 
     * @return budgetService to assign
     */
    public void setBudgetService(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    /**
     * Retrieve injected <code>{@link PessimisticLockService}</code> singleton
     * 
     * @return PessimisticLockService
     */
    public PessimisticLockService getPessimisticLockService() {
        return pessimisticLockService;
    }

    /**
     * Inject <code>{@link PessimisticLockService}</code> singleton
     * 
     * @param pessimisticLockService to assign
     */
    public void setPessimisticLockService(PessimisticLockService pessimisticLockService) {
        this.pessimisticLockService = pessimisticLockService;
    }

    /**
     * Retrieve injected <code>{@link BudgetVersionRule}</code> singleton
     * 
     * @return BudgetVersionRule
     */
    public BudgetVersionRule getBudgetVersionRule() {
        return budgetVersionRule;
    }

    /**
     * Inject <code>{@BudgetVersionRule}</code> singleton
     * 
     * @return BudgetVersionRule
     */
    public void setBudgetVersionRule(BudgetVersionRule budgetVersionRule) {
        this.budgetVersionRule = budgetVersionRule;
    }
}
