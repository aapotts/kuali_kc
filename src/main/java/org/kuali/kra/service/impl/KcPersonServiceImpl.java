/*
 * Copyright 2006-2008 The Kuali Foundation
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
package org.kuali.kra.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.bo.KcPerson;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.service.KcPersonService;

import org.kuali.rice.kim.bo.entity.KimEntity;
import org.kuali.rice.kim.bo.entity.KimPrincipal;
import org.kuali.rice.kim.bo.entity.dto.KimEntityInfo;
import org.kuali.rice.kim.service.IdentityService;

import org.kuali.rice.kim.service.PersonService;
import org.kuali.rice.kim.bo.impl.PersonImpl;;

/**
 * Service for working with KcPerson objects.
 */
public class KcPersonServiceImpl implements KcPersonService {
    
    private IdentityService identityService;
    //private PersonService personService;
    
    /** {@inheritDoc} */
    public List<KcPerson> getKcPersons(final Map<String, String> fieldValues) {
        if (fieldValues == null) {
            throw new IllegalArgumentException("the fieldValues are null");
        }
        
        //convert username and kcpersonid to proper naming such the person service can use them
        if(fieldValues.containsKey("userName")){
            String userNameSearchValue = fieldValues.get("userName");
            fieldValues.put("principalName", userNameSearchValue);
        }
        
        if(fieldValues.containsKey("personId")){
            String personIdSearchValue = fieldValues.get("personId");
            fieldValues.put("principalId", personIdSearchValue);
        }
        if(fieldValues.containsKey("officePhone")){
            String officePhoneSerachValue = fieldValues.get("officePhone");
            fieldValues.put("phoneNumber", officePhoneSerachValue);
        }
        
        //final List<KimEntityInfo> entities = this.identityService.lookupEntityInfo(fieldValues, true);
        final List<PersonImpl> people = KraServiceLocator.getService(PersonService.class).findPeople(fieldValues, true);
        
        //return this.createKcPersonsFrom(entities);
        return this.createKcPersonsFromPeople(people);
    }
    
    /** {@inheritDoc} */
    public KcPerson getKcPersonByUserName(final String userName) {
        if (StringUtils.isEmpty(userName)) {
            throw new IllegalArgumentException("the userName is null or empty");
        }
        KimEntity entity = this.identityService.getEntityInfoByPrincipalName(userName);
        if(entity == null)
            return null;
        
        return KcPerson.fromEntityAndUserName(entity, userName);
    }
    
    /** {@inheritDoc} */
    public KcPerson getKcPersonByPersonId(final String personId) {
        if (StringUtils.isEmpty(personId)) {
            throw new IllegalArgumentException("the personId is null or empty");
        }
        
        return KcPerson.fromEntityAndPersonId(this.identityService.getEntityInfoByPrincipalId(personId), personId);
    }
    
    /**
     * Creates a List of KcPersons from a list of KIM entities.
     * 
     * @param entities the list of entities
     * @return the list of Kc persons
     */
    private List<KcPerson> createKcPersonsFrom(List<? extends KimEntity> entities) {
        List<KcPerson> persons = new ArrayList<KcPerson>();
        
        for (KimEntity entity : entities) {
            for (KimPrincipal principal : entity.getPrincipals()) {
                persons.add(KcPerson.fromEntityAndPersonId(entity, principal.getPrincipalId()));
            }
        }
        
        return persons;
    }
    
    private List<KcPerson> createKcPersonsFromPeople(List<PersonImpl> people) {
        List<KcPerson> persons = new ArrayList<KcPerson>();
        
        for (PersonImpl person : people) {
            persons.add(KcPerson.fromPersonId(person.getPrincipalId()));
            /*for (KimPrincipal principal : person.getPrincipals()) {
                persons.add(KcPerson.fromEntityAndPersonId(entity, principal.getPrincipalId()));
            }*/
        }
        
        return persons;
    }

    /**
     * Sets the Identity Service.
     * @param identityService the Identity Service.
     */
    public void setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
    }
    /*
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }*/
}
