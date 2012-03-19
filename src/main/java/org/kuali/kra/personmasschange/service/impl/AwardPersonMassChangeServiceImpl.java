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
import org.kuali.kra.award.contacts.AwardPerson;
import org.kuali.kra.award.contacts.AwardSponsorContact;
import org.kuali.kra.award.contacts.AwardUnitContact;
import org.kuali.kra.award.home.Award;
import org.kuali.kra.award.home.ContactRole;
import org.kuali.kra.award.paymentreports.specialapproval.foreigntravel.AwardApprovedForeignTravel;
import org.kuali.kra.bo.KcPerson;
import org.kuali.kra.bo.Rolodex;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.personmasschange.bo.PersonMassChange;
import org.kuali.kra.personmasschange.service.AwardPersonMassChangeService;
import org.kuali.kra.rules.ErrorReporter;
import org.kuali.kra.service.KcPersonService;
import org.kuali.kra.service.RolodexService;
import org.kuali.rice.krad.service.BusinessObjectService;

/**
 * Defines the service for performing a Person Mass Change on Awards.
 */
public class AwardPersonMassChangeServiceImpl implements AwardPersonMassChangeService {
    
    private static final String PMC_LOCKED_FIELD = "personMassChangeDocumentLocked";
    
    private static final String AWARD_ID = "awardId";
    private static final String SEQUENCE_NUMBER = "sequenceNumber";
    
    private static final String AWARD = "award";
    
    private final ErrorReporter errorReporter = new ErrorReporter();

    private BusinessObjectService businessObjectService;
    private KcPersonService kcPersonService;
    private RolodexService rolodexService;
    
    @Override
    public List<Award> getAwardChangeCandidates(PersonMassChange personMassChange) {
        List<Award> awardChangeCandidates = new ArrayList<Award>();
        
        List<Award> awards = new ArrayList<Award>();
        if (personMassChange.getAwardPersonMassChange().requiresChange()) {
            awards.addAll(getAwards(personMassChange));
        }

        for (Award award : awards) {
            if (isAwardChangeCandidate(personMassChange, award)) {
                awardChangeCandidates.add(award);
            }
        }
        
        for (Award awardChangeCandidate : awardChangeCandidates) {
            awardChangeCandidate.getAwardDocument().refreshPessimisticLocks();
            if (!awardChangeCandidate.getAwardDocument().getPessimisticLocks().isEmpty()) {
                reportSoftError(awardChangeCandidate);
            }
        }
        
        return awardChangeCandidates;
    }
    
    private List<Award> getAwards(PersonMassChange personMassChange) {
        List<Award> awardChangeCandidates = new ArrayList<Award>();
        
        Collection<Award> awards = getBusinessObjectService().findAll(Award.class);

        if (personMassChange.isChangeAllSequences()) {
            awardChangeCandidates.addAll(awards);
        } else {
            awardChangeCandidates.addAll(getLatestAwards(awards));
        }
        
        return awardChangeCandidates;
    }
    
    private List<Award> getLatestAwards(Collection<Award> awards) {
        List<Award> latestAwards = new ArrayList<Award>();
        
        for (String uniqueAwardId : getUniqueAwardIds(awards)) {
            Map<String, String> fieldValues = new HashMap<String, String>();
            fieldValues.put(AWARD_ID, uniqueAwardId);
            Collection<Award> uniqueAwards = getBusinessObjectService().findMatchingOrderBy(Award.class, fieldValues, SEQUENCE_NUMBER, false);
            if (!uniqueAwards.isEmpty()) {
                latestAwards.add((Award) CollectionUtils.get(uniqueAwards, 0));
            }
        }
        
        return latestAwards;
    }
    
    private Set<String> getUniqueAwardIds(Collection<Award> awards) {
        Set<String> uniqueAwardIds = new HashSet<String>();
        
        for (Award award : awards) {
            uniqueAwardIds.add(String.valueOf(award.getAwardId()));
        }
        
        return uniqueAwardIds;
    }
    
    private boolean isAwardChangeCandidate(PersonMassChange personMassChange, Award award) {
        boolean isAwardChangeCandidate = false;
        
        List<AwardPerson> awardPersons = award.getProjectPersons();
        List<AwardSponsorContact> awardSponsorContacts = award.getSponsorContacts();
        List<AwardApprovedForeignTravel> awardApprovedForeignTravels = award.getApprovedForeignTravelTrips();
        List<AwardUnitContact> awardUnitContacts = award.getAwardUnitContacts();
        
        String[] investigatorRoles = { ContactRole.PI_CODE, ContactRole.COI_CODE };
        String[] keyStudyPersonRoles = { ContactRole.KEY_PERSON_CODE };
        
        if (personMassChange.getAwardPersonMassChange().isInvestigator()) {
            isAwardChangeCandidate |= isAwardPersonChangeCandidate(personMassChange, awardPersons, investigatorRoles);
        }
        if (personMassChange.getAwardPersonMassChange().isKeyStudyPerson()) {
            isAwardChangeCandidate |= isAwardPersonChangeCandidate(personMassChange, awardPersons, keyStudyPersonRoles);
        }
        if (personMassChange.getAwardPersonMassChange().isUnitContact()) {
            isAwardChangeCandidate |= isAwardUnitContactChangeCandidate(personMassChange, awardUnitContacts);
        }
        if (personMassChange.getAwardPersonMassChange().isSponsorContact()) {
            isAwardChangeCandidate |= isAwardSponsorContactChangeCandidate(personMassChange, awardSponsorContacts);
        }
        if (personMassChange.getAwardPersonMassChange().isApprovedForeignTravel()) {
            isAwardChangeCandidate |= isAwardApprovedForeignTravelChangeCandidate(personMassChange, awardApprovedForeignTravels);
        }

        
        return isAwardChangeCandidate;
    }
    
    private boolean isAwardPersonChangeCandidate(PersonMassChange personMassChange, List<AwardPerson> awardPersons, String... personRoles) {
        boolean isAwardPersonChangeCandidate = false;
        
        for (AwardPerson awardPerson : awardPersons) {
            if (isAwardPersonInRole(awardPerson, personRoles)) {
                if (isPersonIdMassChange(personMassChange, awardPerson.getPersonId()) 
                        || isRolodexIdMassChange(personMassChange, awardPerson.getRolodexId())) {
                    isAwardPersonChangeCandidate = true;
                    break;
                }
            }
        }
        
        return isAwardPersonChangeCandidate;
    }
    
    private boolean isAwardPersonInRole(AwardPerson awardPerson, String... personRoles) {
        boolean isAwardPersonInRole = false;
        
        for (String awardPersonRole : personRoles) {
            if (StringUtils.equals(awardPerson.getRoleCode(), awardPersonRole)) {
                isAwardPersonInRole = true;
                break;
            }
        }
        
        return isAwardPersonInRole;
    }

    private boolean isAwardUnitContactChangeCandidate(PersonMassChange personMassChange, List<AwardUnitContact> awardUnitContacts) {
        boolean isAwardUnitAdministratorChangeCandidate = false;
        
        for (AwardUnitContact awardUnitContact : awardUnitContacts) {
            if (isPersonIdMassChange(personMassChange, awardUnitContact.getPersonId())) {
                isAwardUnitAdministratorChangeCandidate = true;
                break;
            }
        }
        
        return isAwardUnitAdministratorChangeCandidate;
    }
    
    private boolean isAwardSponsorContactChangeCandidate(PersonMassChange personMassChange, List<AwardSponsorContact> awardSponsorContacts) {
        boolean isAwardContactPersonChangeCandidate = false;
        
        for (AwardSponsorContact awardSponsorContact : awardSponsorContacts) {
            if (isRolodexIdMassChange(personMassChange, awardSponsorContact.getRolodexId())) {
                isAwardContactPersonChangeCandidate = true;
                break;
            }
        }
        
        return isAwardContactPersonChangeCandidate;
    }
    
    private boolean isAwardApprovedForeignTravelChangeCandidate(PersonMassChange personMassChange, 
                                                                List<AwardApprovedForeignTravel> awardApprovedForeignTravels) {
        boolean isAwardForeignTripChangeCandidate = false;
        
        for (AwardApprovedForeignTravel awardApprovedForeignTravel : awardApprovedForeignTravels) {
            if (isPersonIdMassChange(personMassChange, awardApprovedForeignTravel.getPersonId()) 
                    || isRolodexIdMassChange(personMassChange, awardApprovedForeignTravel.getRolodexId())) {
                isAwardForeignTripChangeCandidate = true;
                break;
            }
        }
        
        return isAwardForeignTripChangeCandidate;
    }

    @Override
    public void performPersonMassChange(PersonMassChange personMassChange, List<Award> awardChangeCandidates) {
        for (Award awardChangeCandidate : awardChangeCandidates) {
            awardChangeCandidate.getAwardDocument().refreshPessimisticLocks();
            if (awardChangeCandidate.getAwardDocument().getPessimisticLocks().isEmpty()) {
                performAwardInvestigatorPersonMassChange(personMassChange, awardChangeCandidate);
                performAwardKeyStudyPersonPersonMassChange(personMassChange, awardChangeCandidate);
                performAwardUnitContactPersonMassChange(personMassChange, awardChangeCandidate);
                performAwardSponsorContactPersonMassChange(personMassChange, awardChangeCandidate);
                performAwardApprovedForeignTravelPersonMassChange(personMassChange, awardChangeCandidate);
            }
        }
    }
    
    private void performAwardInvestigatorPersonMassChange(PersonMassChange personMassChange, Award award) {
        if (personMassChange.getAwardPersonMassChange().isInvestigator()) {
            String[] personRoles = { ContactRole.PI_CODE, ContactRole.COI_CODE };
            performAwardPersonPersonMassChange(personMassChange, award, personRoles);
        }
    }
    
    private void performAwardKeyStudyPersonPersonMassChange(PersonMassChange personMassChange, Award award) {
        if (personMassChange.getAwardPersonMassChange().isKeyStudyPerson()) {
            String[] personRoles = { ContactRole.KEY_PERSON_CODE };
            performAwardPersonPersonMassChange(personMassChange, award, personRoles);
        }
    }
    
    private void performAwardPersonPersonMassChange(PersonMassChange personMassChange, Award award, String... personRoles) {
        for (AwardPerson awardPerson : award.getProjectPersons()) {
            if (isAwardPersonInRole(awardPerson, personRoles)) {
                if (personMassChange.getReplacerPersonId() != null) {
                    KcPerson person = getKcPersonService().getKcPersonByPersonId(personMassChange.getReplacerPersonId());
                    awardPerson.setPersonId(person.getPersonId());
                    awardPerson.setFullName(person.getFullName());
                    awardPerson.setRolodexId(null);
                } else if (personMassChange.getReplacerRolodexId() != null) {
                    Rolodex rolodex = getRolodexService().getRolodex(Integer.parseInt(personMassChange.getReplacerRolodexId()));
                    awardPerson.setPersonId(null);
                    awardPerson.setRolodexId(rolodex.getRolodexId());
                    awardPerson.setFullName(rolodex.getFullName());
                }

                getBusinessObjectService().save(awardPerson);
            }
        }
    }

    private void performAwardUnitContactPersonMassChange(PersonMassChange personMassChange, Award award) {
        if (personMassChange.getAwardPersonMassChange().isUnitContact()) {
            for (AwardUnitContact awardUnitContact : award.getAwardUnitContacts()) {
                KcPerson person = getKcPersonService().getKcPersonByPersonId(personMassChange.getReplacerPersonId());
                awardUnitContact.setPersonId(person.getPersonId());
                awardUnitContact.setFullName(person.getFullName());

                getBusinessObjectService().save(awardUnitContact);
            }
        }
    }
    
    private void performAwardSponsorContactPersonMassChange(PersonMassChange personMassChange, Award award) {
        if (personMassChange.getAwardPersonMassChange().isSponsorContact()) {
            for (AwardSponsorContact awardSponsorContact : award.getSponsorContacts()) {
                awardSponsorContact.setRolodexId(Integer.parseInt(personMassChange.getReplacerRolodexId()));

                getBusinessObjectService().save(awardSponsorContact);
            }
        }
    }
    
    private void performAwardApprovedForeignTravelPersonMassChange(PersonMassChange personMassChange, Award award) {
        if (personMassChange.getAwardPersonMassChange().isApprovedForeignTravel()) {
            for (AwardApprovedForeignTravel awardApprovedForeignTravel : award.getApprovedForeignTravelTrips()) {
                if (personMassChange.getReplacerPersonId() != null) {
                    KcPerson person = getKcPersonService().getKcPersonByPersonId(personMassChange.getReplacerPersonId());
                    awardApprovedForeignTravel.setPersonId(person.getPersonId());
                    awardApprovedForeignTravel.setTravelerName(person.getFullName());
                    awardApprovedForeignTravel.setRolodexId(null);
                } else if (personMassChange.getReplacerRolodexId() != null) {
                    Rolodex rolodex = getRolodexService().getRolodex(Integer.parseInt(personMassChange.getReplacerRolodexId()));
                    awardApprovedForeignTravel.setPersonId(null);
                    awardApprovedForeignTravel.setRolodexId(rolodex.getRolodexId());
                    awardApprovedForeignTravel.setTravelerName(rolodex.getFullName());
                }

                getBusinessObjectService().save(awardApprovedForeignTravel);
            }
        }
    }
    
    private boolean isPersonIdMassChange(PersonMassChange personMassChange, String personId) {
        String replaceePersonId = personMassChange.getReplaceePersonId();
        return replaceePersonId != null && StringUtils.equals(replaceePersonId, personId);
    }
    
    private boolean isRolodexIdMassChange(PersonMassChange personMassChange, Integer rolodexId) {
        String replaceeRolodexId = personMassChange.getReplaceeRolodexId();
        return replaceeRolodexId != null && StringUtils.equals(replaceeRolodexId, String.valueOf(rolodexId));
    }
    
    private void reportSoftError(Award award) {
        String awardNumber = award.getAwardNumber();
        errorReporter.reportSoftError(PMC_LOCKED_FIELD, KeyConstants.ERROR_PERSON_MASS_CHANGE_DOCUMENT_LOCKED, AWARD, awardNumber);
    }
    
    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }
    
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
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