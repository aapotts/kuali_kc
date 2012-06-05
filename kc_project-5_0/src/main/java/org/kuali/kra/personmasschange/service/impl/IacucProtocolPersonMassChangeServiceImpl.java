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
package org.kuali.kra.personmasschange.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.kra.bo.KcPerson;
import org.kuali.kra.bo.Rolodex;
import org.kuali.kra.iacuc.IacucProtocol;
import org.kuali.kra.iacuc.personnel.IacucProtocolPersonRole;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.personmasschange.bo.PersonMassChange;
import org.kuali.kra.personmasschange.service.IacucProtocolPersonMassChangeService;
import org.kuali.kra.protocol.actions.submit.ProtocolReviewer;
import org.kuali.kra.protocol.onlinereview.ProtocolOnlineReview;
import org.kuali.kra.protocol.personnel.ProtocolPerson;
import org.kuali.kra.protocol.personnel.ProtocolPersonTrainingService;
import org.kuali.kra.protocol.personnel.ProtocolUnit;
import org.kuali.kra.rules.ErrorReporter;
import org.kuali.kra.service.KcPersonService;
import org.kuali.kra.service.PersonEditableService;
import org.kuali.kra.service.RolodexService;
import org.kuali.rice.krad.service.BusinessObjectService;

/**
 * Defines the service for performing a Person Mass Change on IACUC Protocols.
 */
public class IacucProtocolPersonMassChangeServiceImpl implements IacucProtocolPersonMassChangeService {
    
    private static final String PMC_LOCKED_FIELD = "personMassChangeDocumentLocked";

    private static final String PROTOCOL_NUMBER = "protocolNumber";
    private static final String SEQUENCE_NUMBER = "sequenceNumber";
    
    private static final String PROTOCOL = "protocol";
    
    private final ErrorReporter errorReporter = new ErrorReporter();
    
    private BusinessObjectService businessObjectService;
    private PersonEditableService personEditableService;
    private ProtocolPersonTrainingService protocolPersonTrainingService;
    private KcPersonService kcPersonService;
    private RolodexService rolodexService;
    
    @Override
    public List<IacucProtocol> getIacucProtocolChangeCandidates(PersonMassChange personMassChange) {
        List<IacucProtocol> protocolChangeCandidates = new ArrayList<IacucProtocol>();
        
        List<IacucProtocol> protocols = new ArrayList<IacucProtocol>();
        if (personMassChange.getProtocolPersonMassChange().requiresChange()) {
            protocols.addAll(getProtocols(personMassChange));
        }

        for (IacucProtocol protocol : protocols) {
            if (isProtocolChangeCandidate(personMassChange, protocol)) {
                protocolChangeCandidates.add(protocol);
            }
        }
        
        for (IacucProtocol protocolChangeCandidate : protocolChangeCandidates) {
            if (!protocolChangeCandidate.getProtocolDocument().getPessimisticLocks().isEmpty()) {
                reportSoftError(protocolChangeCandidate);
            }
        }
        
        return protocolChangeCandidates;
    }
    
    private List<IacucProtocol> getProtocols(PersonMassChange personMassChange) {
        List<IacucProtocol> protocols = new ArrayList<IacucProtocol>();
        
        Collection<IacucProtocol> allProtocols = getBusinessObjectService().findAll(IacucProtocol.class);

        if (personMassChange.isChangeAllSequences()) {
            protocols.addAll(allProtocols);
        } else {
            protocols.addAll(getLatestProtocols(allProtocols));
        }
        
        return protocols;
    }
    
    private List<IacucProtocol> getLatestProtocols(Collection<IacucProtocol> protocols) {
        List<IacucProtocol> latestProtocols = new ArrayList<IacucProtocol>();
        
        for (String uniqueProtocolNumber : getUniqueProtocolNumbers(protocols)) {
            Map<String, String> fieldValues = new HashMap<String, String>();
            fieldValues.put(PROTOCOL_NUMBER, uniqueProtocolNumber);
            Collection<IacucProtocol> uniqueProtocols = getBusinessObjectService().findMatchingOrderBy(IacucProtocol.class, fieldValues, SEQUENCE_NUMBER, false);
            if (!uniqueProtocols.isEmpty()) {
                latestProtocols.add((IacucProtocol) CollectionUtils.get(uniqueProtocols, 0));
            }
        }
        
        return latestProtocols;
    }
    
    private Set<String> getUniqueProtocolNumbers(Collection<IacucProtocol> protocols) {
        Set<String> uniqueProtocolIds = new HashSet<String>();
        
        for (IacucProtocol protocol : protocols) {
            uniqueProtocolIds.add(protocol.getProtocolNumber());
        }
        
        return uniqueProtocolIds;
    }
    
    private boolean isProtocolChangeCandidate(PersonMassChange personMassChange, IacucProtocol protocol) {
        boolean isProtocolChangeCandidate = false;
        
        List<ProtocolPerson> persons = protocol.getProtocolPersons();
        List<ProtocolOnlineReview> onlineReviews = protocol.getProtocolOnlineReviews();
        
        String[] investigatorRoles = { IacucProtocolPersonRole.ROLE_PRINCIPAL_INVESTIGATOR, IacucProtocolPersonRole.ROLE_CO_INVESTIGATOR };
        String[] keyStudyPersonRoles = { IacucProtocolPersonRole.ROLE_STUDY_PERSONNEL };
        String[] correspondentsRoles = { IacucProtocolPersonRole.ROLE_CORRESPONDENTS };
        
        if (personMassChange.getProtocolPersonMassChange().isInvestigator()) {
            isProtocolChangeCandidate |= isPersonChangeCandidate(personMassChange, persons, investigatorRoles);
        }
        if (personMassChange.getProtocolPersonMassChange().isKeyStudyPerson()) {
            isProtocolChangeCandidate |= isPersonChangeCandidate(personMassChange, persons, keyStudyPersonRoles);
        }
        if (personMassChange.getProtocolPersonMassChange().isCorrespondents()) {
            isProtocolChangeCandidate |= isPersonChangeCandidate(personMassChange, persons, correspondentsRoles);
        }
        if (personMassChange.getProtocolPersonMassChange().isReviewer()) {
            isProtocolChangeCandidate |= isReviewerChangeCandidate(personMassChange, onlineReviews);
        }
        
        return isProtocolChangeCandidate;
    }
    
    private boolean isPersonChangeCandidate(PersonMassChange personMassChange, List<ProtocolPerson> persons, String... personRoles) {
        boolean isPersonChangeCandidate = false;
        
        for (ProtocolPerson person : persons) {
            if (isPersonInRole(person, personRoles)) {
                if (isPersonIdMassChange(personMassChange, person.getPersonId()) || isRolodexIdMassChange(personMassChange, person.getRolodexId())) {
                    isPersonChangeCandidate = true;
                    break;
                }
            }
        }
        
        return isPersonChangeCandidate;
    }
    
    private boolean isPersonInRole(ProtocolPerson protocolPerson, String... personRoles) {
        boolean isPersonInRole = false;
        
        for (String personRole : personRoles) {
            if (StringUtils.equals(protocolPerson.getProtocolPersonRoleId(), personRole)) {
                isPersonInRole = true;
                break;
            }
        }
        
        return isPersonInRole;
    }
    
    private boolean isReviewerChangeCandidate(PersonMassChange personMassChange, List<ProtocolOnlineReview> onlineReviews) {
        boolean isReviewerChangeCandidate = false;
        
        for (ProtocolOnlineReview onlineReview : onlineReviews) {
            ProtocolReviewer reviewer = onlineReview.getProtocolReviewer();
            if (isPersonIdMassChange(personMassChange, reviewer.getPersonId()) || isRolodexIdMassChange(personMassChange, reviewer.getRolodexId())) {
                isReviewerChangeCandidate = true;
                break;
            }
        }
        
        return isReviewerChangeCandidate;
    }

    @Override
    public void performPersonMassChange(PersonMassChange personMassChange, List<IacucProtocol> protocolChangeCandidates) {
        for (IacucProtocol protocolChangeCandidate : protocolChangeCandidates) {
            if (protocolChangeCandidate.getProtocolDocument().getPessimisticLocks().isEmpty()) {
                performInvestigatorPersonMassChange(personMassChange, protocolChangeCandidate);
                performKeyStudyPersonPersonMassChange(personMassChange, protocolChangeCandidate);
                performCorrespondentsPersonMassChange(personMassChange, protocolChangeCandidate);
                performReviewerPersonMassChange(personMassChange, protocolChangeCandidate);
            }
        }
    }
    
    private void performInvestigatorPersonMassChange(PersonMassChange personMassChange, IacucProtocol protocol) {
        if (personMassChange.getProtocolPersonMassChange().isInvestigator()) {
            String[] personRoles = { IacucProtocolPersonRole.ROLE_PRINCIPAL_INVESTIGATOR, IacucProtocolPersonRole.ROLE_CO_INVESTIGATOR };
            performPersonPersonMassChange(personMassChange, protocol, personRoles);
        }
    }
    
    private void performKeyStudyPersonPersonMassChange(PersonMassChange personMassChange, IacucProtocol protocol) {
        if (personMassChange.getProtocolPersonMassChange().isKeyStudyPerson()) {
            String[] personRoles = { IacucProtocolPersonRole.ROLE_STUDY_PERSONNEL };
            performPersonPersonMassChange(personMassChange, protocol, personRoles);
        }
    }
    
    private void performCorrespondentsPersonMassChange(PersonMassChange personMassChange, IacucProtocol protocol) {
        if (personMassChange.getProtocolPersonMassChange().isCorrespondents()) {
            String[] personRoles = { IacucProtocolPersonRole.ROLE_CORRESPONDENTS };
            performPersonPersonMassChange(personMassChange, protocol, personRoles);
        }
    }

    private void performPersonPersonMassChange(PersonMassChange personMassChange, IacucProtocol protocol, String... personRoles) {
        for (ProtocolPerson person : protocol.getProtocolPersons()) {
            if (isPersonInRole(person, personRoles)) {
                if (isPersonIdMassChange(personMassChange, person.getPersonId()) || isRolodexIdMassChange(personMassChange, person.getRolodexId())) {
                    if (personMassChange.getReplacerPersonId() != null) {
                        KcPerson kcPerson = getKcPersonService().getKcPersonByPersonId(personMassChange.getReplacerPersonId());
                        person.setPersonId(personMassChange.getReplacerPersonId());
                        person.setRolodexId(null);
                        person.setPersonName(kcPerson.getFullName());
                        getPersonEditableService().populateContactFieldsFromPersonId(person);
                        getProtocolPersonTrainingService().setTrainedFlag(person);
                        
                        for (ProtocolUnit unit : person.getProtocolUnits()) {
                            unit.setPersonId(personMassChange.getReplacerPersonId());
                        }
                    } else if (personMassChange.getReplacerRolodexId() != null) {
                        Rolodex rolodex = getRolodexService().getRolodex(personMassChange.getReplacerRolodexId());
                        person.setPersonId(null);
                        person.setRolodexId(rolodex.getRolodexId());
                        person.setPersonName(rolodex.getFullName());
                        getPersonEditableService().populateContactFieldsFromRolodexId(person);
                        getProtocolPersonTrainingService().setTrainedFlag(person);
                        
                        for (ProtocolUnit unit : person.getProtocolUnits()) {
                            unit.setPersonId(null);
                        }
                    }
    
                    getBusinessObjectService().save(person);
                }
            }
        }
    }

    private void performReviewerPersonMassChange(PersonMassChange personMassChange, IacucProtocol protocol) {
        if (personMassChange.getProtocolPersonMassChange().isReviewer()) {
            for (ProtocolOnlineReview onlineReview : protocol.getProtocolOnlineReviews()) {
                ProtocolReviewer reviewer = onlineReview.getProtocolReviewer();
                if (isPersonIdMassChange(personMassChange, reviewer.getPersonId()) || isRolodexIdMassChange(personMassChange, reviewer.getRolodexId())) {
                    if (personMassChange.getReplacerPersonId() != null) {
                        KcPerson kcPerson = getKcPersonService().getKcPersonByPersonId(personMassChange.getReplacerPersonId());
                        reviewer.setPersonId(kcPerson.getPersonId());
                        reviewer.setRolodexId(null);
                        reviewer.setNonEmployeeFlag(false);
                    } else if (personMassChange.getReplacerRolodexId() != null) {
                        Rolodex rolodex = getRolodexService().getRolodex(personMassChange.getReplacerRolodexId());
                        reviewer.setRolodexId(rolodex.getRolodexId());
                        reviewer.setPersonId(null);
                        reviewer.setNonEmployeeFlag(true);
                    }
                
                    getBusinessObjectService().save(reviewer);
                }
            }
        }
    }
    
    private boolean isPersonIdMassChange(PersonMassChange personMassChange, String personId) {
        String replaceePersonId = personMassChange.getReplaceePersonId();
        return replaceePersonId != null && replaceePersonId.equals(personId);
    }
    
    private boolean isRolodexIdMassChange(PersonMassChange personMassChange, Integer rolodexId) {
        Integer replaceeRolodexId = personMassChange.getReplaceeRolodexId();
        return replaceeRolodexId != null && replaceeRolodexId.equals(rolodexId);
    }
    
    private void reportSoftError(IacucProtocol protocol) {
        String protocolNumber = protocol.getProtocolNumber();
        errorReporter.reportSoftError(PMC_LOCKED_FIELD, KeyConstants.ERROR_PERSON_MASS_CHANGE_DOCUMENT_LOCKED, PROTOCOL, protocolNumber);
    }
    
    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }
    
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
    
    public PersonEditableService getPersonEditableService() {
        return personEditableService;
    }

    public void setPersonEditableService(PersonEditableService personEditableService) {
        this.personEditableService = personEditableService;
    }

    public ProtocolPersonTrainingService getProtocolPersonTrainingService() {
        return protocolPersonTrainingService;
    }

    public void setProtocolPersonTrainingService(ProtocolPersonTrainingService protocolPersonTrainingService) {
        this.protocolPersonTrainingService = protocolPersonTrainingService;
    }

    public KcPersonService getKcPersonService() {
        return kcPersonService;
    }
    
    public void setKcPersonService(KcPersonService kcPersonService) {
        this.kcPersonService = kcPersonService;
    }
    
    public RolodexService getRolodexService() {
        return rolodexService;
    }
    
    public void setRolodexService(RolodexService rolodexService) {
        this.rolodexService = rolodexService;
    }
    
}