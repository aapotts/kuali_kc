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
import java.util.List;

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
 * 
 * Person roles that might be replaced are: Investigator, Unit Contact, Sponsor Contact, Approved Foreign Travel.
 */
public class AwardPersonMassChangeServiceImpl implements AwardPersonMassChangeService {
    
    private static final String PMC_LOCKED_FIELD = "personMassChangeDocumentLocked";
    
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
        List<Award> awards = new ArrayList<Award>();
        
        Collection<Award> allAwards = getBusinessObjectService().findAll(Award.class);

        if (personMassChange.isChangeAllSequences()) {
            awards.addAll(allAwards);
        } else {
            awards.addAll(getLatestAwards(allAwards));
        }
        
        return awards;
    }
    
    private List<Award> getLatestAwards(Collection<Award> awards) {
        List<Award> latestAwards = new ArrayList<Award>();
        
        for (Award award : awards) {
            if (award.isActiveVersion()) {
                latestAwards.add(award);
            }
        }
        
        return latestAwards;
    }
    
    private boolean isAwardChangeCandidate(PersonMassChange personMassChange, Award award) {
        boolean isAwardChangeCandidate = false;
        
        List<AwardPerson> persons = award.getProjectPersons();
        List<AwardSponsorContact> sponsorContacts = award.getSponsorContacts();
        List<AwardApprovedForeignTravel> approvedForeignTravels = award.getApprovedForeignTravelTrips();
        List<AwardUnitContact> unitContacts = award.getAwardUnitContacts();
        
        String[] investigatorRoles = { ContactRole.PI_CODE, ContactRole.COI_CODE };
        String[] keyStudyPersonRoles = { ContactRole.KEY_PERSON_CODE };
        
        if (personMassChange.getAwardPersonMassChange().isInvestigator()) {
            isAwardChangeCandidate |= isPersonChangeCandidate(personMassChange, persons, investigatorRoles);
        }
        if (personMassChange.getAwardPersonMassChange().isKeyStudyPerson()) {
            isAwardChangeCandidate |= isPersonChangeCandidate(personMassChange, persons, keyStudyPersonRoles);
        }
        if (personMassChange.getAwardPersonMassChange().isUnitContact()) {
            isAwardChangeCandidate |= isUnitContactChangeCandidate(personMassChange, unitContacts);
        }
        if (personMassChange.getAwardPersonMassChange().isSponsorContact()) {
            isAwardChangeCandidate |= isSponsorContactChangeCandidate(personMassChange, sponsorContacts);
        }
        if (personMassChange.getAwardPersonMassChange().isApprovedForeignTravel()) {
            isAwardChangeCandidate |= isApprovedForeignTravelChangeCandidate(personMassChange, approvedForeignTravels);
        }

        return isAwardChangeCandidate;
    }
    
    private boolean isPersonChangeCandidate(PersonMassChange personMassChange, List<AwardPerson> persons, String... personRoles) {
        boolean isPersonChangeCandidate = false;
        
        for (AwardPerson person : persons) {
            if (isPersonInRole(person, personRoles)) {
                if (isPersonIdMassChange(personMassChange, person.getPersonId()) || isRolodexIdMassChange(personMassChange, person.getRolodexId())) {
                    isPersonChangeCandidate = true;
                    break;
                }
            }
        }
        
        return isPersonChangeCandidate;
    }
    
    private boolean isPersonInRole(AwardPerson person, String... personRoles) {
        boolean isPersonInRole = false;
        
        for (String personRole : personRoles) {
            if (StringUtils.equals(person.getRoleCode(), personRole)) {
                isPersonInRole = true;
                break;
            }
        }
        
        return isPersonInRole;
    }

    private boolean isUnitContactChangeCandidate(PersonMassChange personMassChange, List<AwardUnitContact> unitContacts) {
        boolean isUnitContactChangeCandidate = false;
        
        for (AwardUnitContact unitContact : unitContacts) {
            if (isPersonIdMassChange(personMassChange, unitContact.getPersonId())) {
                isUnitContactChangeCandidate = true;
                break;
            }
        }
        
        return isUnitContactChangeCandidate;
    }
    
    private boolean isSponsorContactChangeCandidate(PersonMassChange personMassChange, List<AwardSponsorContact> sponsorContacts) {
        boolean isSponsorContactChangeCandidate = false;
        
        for (AwardSponsorContact sponsorContact : sponsorContacts) {
            if (isRolodexIdMassChange(personMassChange, sponsorContact.getRolodexId())) {
                isSponsorContactChangeCandidate = true;
                break;
            }
        }
        
        return isSponsorContactChangeCandidate;
    }
    
    private boolean isApprovedForeignTravelChangeCandidate(PersonMassChange personMassChange, List<AwardApprovedForeignTravel> approvedForeignTravels) {
        boolean isApprovedForeignTravelChangeCandidate = false;
        
        for (AwardApprovedForeignTravel approvedForeignTravel : approvedForeignTravels) {
            if (isPersonIdMassChange(personMassChange, approvedForeignTravel.getPersonId()) 
                    || isRolodexIdMassChange(personMassChange, approvedForeignTravel.getRolodexId())) {
                isApprovedForeignTravelChangeCandidate = true;
                break;
            }
        }
        
        return isApprovedForeignTravelChangeCandidate;
    }

    @Override
    public void performPersonMassChange(PersonMassChange personMassChange, List<Award> awardChangeCandidates) {
        for (Award awardChangeCandidate : awardChangeCandidates) {
            awardChangeCandidate.getAwardDocument().refreshPessimisticLocks();
            if (awardChangeCandidate.getAwardDocument().getPessimisticLocks().isEmpty()) {
                performInvestigatorPersonMassChange(personMassChange, awardChangeCandidate);
                performKeyStudyPersonPersonMassChange(personMassChange, awardChangeCandidate);
                performUnitContactPersonMassChange(personMassChange, awardChangeCandidate);
                performSponsorContactPersonMassChange(personMassChange, awardChangeCandidate);
                performApprovedForeignTravelPersonMassChange(personMassChange, awardChangeCandidate);
            }
        }
    }
    
    private void performInvestigatorPersonMassChange(PersonMassChange personMassChange, Award award) {
        if (personMassChange.getAwardPersonMassChange().isInvestigator()) {
            String[] personRoles = { ContactRole.PI_CODE, ContactRole.COI_CODE };
            performPersonPersonMassChange(personMassChange, award, personRoles);
        }
    }
    
    private void performKeyStudyPersonPersonMassChange(PersonMassChange personMassChange, Award award) {
        if (personMassChange.getAwardPersonMassChange().isKeyStudyPerson()) {
            String[] personRoles = { ContactRole.KEY_PERSON_CODE };
            performPersonPersonMassChange(personMassChange, award, personRoles);
        }
    }
    
    private void performPersonPersonMassChange(PersonMassChange personMassChange, Award award, String... personRoles) {
        for (AwardPerson person : award.getProjectPersons()) {
            if (isPersonInRole(person, personRoles)) {
                if (personMassChange.getReplacerPersonId() != null) {
                    KcPerson kcPerson = getKcPersonService().getKcPersonByPersonId(personMassChange.getReplacerPersonId());
                    person.setPersonId(kcPerson.getPersonId());
                    person.setFullName(kcPerson.getFullName());
                    person.setRolodexId(null);
                } else if (personMassChange.getReplacerRolodexId() != null) {
                    Rolodex rolodex = getRolodexService().getRolodex(Integer.parseInt(personMassChange.getReplacerRolodexId()));
                    person.setPersonId(null);
                    person.setRolodexId(rolodex.getRolodexId());
                    person.setFullName(rolodex.getFullName());
                }

                getBusinessObjectService().save(person);
            }
        }
    }

    private void performUnitContactPersonMassChange(PersonMassChange personMassChange, Award award) {
        if (personMassChange.getAwardPersonMassChange().isUnitContact()) {
            for (AwardUnitContact unitContact : award.getAwardUnitContacts()) {
                KcPerson kcPerson = getKcPersonService().getKcPersonByPersonId(personMassChange.getReplacerPersonId());
                unitContact.setPersonId(kcPerson.getPersonId());
                unitContact.setFullName(kcPerson.getFullName());

                getBusinessObjectService().save(unitContact);
            }
        }
    }
    
    private void performSponsorContactPersonMassChange(PersonMassChange personMassChange, Award award) {
        if (personMassChange.getAwardPersonMassChange().isSponsorContact()) {
            for (AwardSponsorContact sponsorContact : award.getSponsorContacts()) {
                sponsorContact.setRolodexId(Integer.parseInt(personMassChange.getReplacerRolodexId()));

                getBusinessObjectService().save(sponsorContact);
            }
        }
    }
    
    private void performApprovedForeignTravelPersonMassChange(PersonMassChange personMassChange, Award award) {
        if (personMassChange.getAwardPersonMassChange().isApprovedForeignTravel()) {
            for (AwardApprovedForeignTravel approvedForeignTravel : award.getApprovedForeignTravelTrips()) {
                if (personMassChange.getReplacerPersonId() != null) {
                    KcPerson kcPerson = getKcPersonService().getKcPersonByPersonId(personMassChange.getReplacerPersonId());
                    approvedForeignTravel.setPersonId(kcPerson.getPersonId());
                    approvedForeignTravel.setTravelerName(kcPerson.getFullName());
                    approvedForeignTravel.setRolodexId(null);
                } else if (personMassChange.getReplacerRolodexId() != null) {
                    Rolodex rolodex = getRolodexService().getRolodex(Integer.parseInt(personMassChange.getReplacerRolodexId()));
                    approvedForeignTravel.setPersonId(null);
                    approvedForeignTravel.setRolodexId(rolodex.getRolodexId());
                    approvedForeignTravel.setTravelerName(rolodex.getFullName());
                }

                getBusinessObjectService().save(approvedForeignTravel);
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