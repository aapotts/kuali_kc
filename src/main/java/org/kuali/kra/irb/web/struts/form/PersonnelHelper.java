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
package org.kuali.kra.irb.web.struts.form;

import static org.kuali.kra.infrastructure.KraServiceLocator.getService;

import java.util.ArrayList;
import java.util.List;

import org.kuali.core.bo.user.UniversalUser;
import org.kuali.core.service.KualiConfigurationService;
import org.kuali.core.util.GlobalVariables;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.infrastructure.TaskName;
import org.kuali.kra.irb.bo.Protocol;
import org.kuali.kra.irb.bo.ProtocolPerson;
import org.kuali.kra.irb.bo.ProtocolUnit;
import org.kuali.kra.irb.document.ProtocolDocument;
import org.kuali.kra.irb.document.authorization.ProtocolTask;
import org.kuali.kra.service.TaskAuthorizationService;

public class PersonnelHelper {
    
    /**
     * Each Helper must contain a reference to its document form
     * so that it can access the actual document.
     */
    private ProtocolForm form;
    
    private boolean modifyProtocol;
    private ProtocolPerson newProtocolPerson;
    private List<ProtocolUnit> newProtocolPersonUnits;
    private boolean personTrainingSectionRequired;

    public PersonnelHelper(ProtocolForm form) {
        setForm(form); 
        setNewProtocolPerson(new ProtocolPerson());
        setNewProtocolPersonUnits(new ArrayList<ProtocolUnit>());
    }    
    
    public void prepareView() {
        initializePermissions(getProtocol());    
        initializeTrainingSection();
    }
    
    private Protocol getProtocol() {
        ProtocolDocument document = form.getDocument();
        if (document == null || document.getProtocol() == null) {
            throw new IllegalArgumentException("invalid (null) ProtocolDocument in ProtocolForm");
        }
        return document.getProtocol();
    }
   
    private void initializePermissions(Protocol protocol) {
        initializeModifyProtocolPermission(protocol);
    }

    private void initializeModifyProtocolPermission(Protocol protocol) {
        ProtocolTask task = new ProtocolTask(TaskName.MODIFY_PROTOCOL, protocol);
        modifyProtocol = getTaskAuthorizationService().isAuthorized(getUsername(), task);     
    }
    
    private TaskAuthorizationService getTaskAuthorizationService() {
        return KraServiceLocator.getService(TaskAuthorizationService.class);
    }
    
    private String getUsername() {
        UniversalUser user = GlobalVariables.getUserSession().getUniversalUser();
        return user.getPersonUserIdentifier();
    }
    
    public boolean getModifyProtocol() {
        return modifyProtocol;
    }

    public void setNewProtocolPerson(ProtocolPerson newProtocolPerson) {
        this.newProtocolPerson = newProtocolPerson;
    }

    public ProtocolPerson getNewProtocolPerson() {
        return newProtocolPerson;
    }

    public List<ProtocolUnit> getNewProtocolPersonUnits() {
        if (getForm().getDocument().getProtocol().getProtocolPersons().size() > this.newProtocolPersonUnits.size()) {
            this.newProtocolPersonUnits.add(this.newProtocolPersonUnits.size(), new ProtocolUnit());
        }
        return newProtocolPersonUnits;
    }

    public void setNewProtocolPersonUnits(List<ProtocolUnit> newProtocolPersonUnits) {
        this.newProtocolPersonUnits = newProtocolPersonUnits;
    }

    public ProtocolForm getForm() {
        return form;
    }

    public void setForm(ProtocolForm form) {
        this.form = form;
    }

    private void initializeTrainingSection() {
        setPersonTrainingSectionRequired(Boolean.parseBoolean(getParameterValue(Constants.PARAMETER_PROTOCOL_PERSON_TRAINING_SECTION)));
    }

    /**
     * This method is to get parameter value
     * @return parameter value
     */
    private String getParameterValue(String parameterName) {
        KualiConfigurationService configService = getService(KualiConfigurationService.class);
        return (configService.getParameter(Constants.PARAMETER_MODULE_PROTOCOL, Constants.PARAMETER_COMPONENT_DOCUMENT, parameterName)).getParameterValue();        
    }

    public boolean isPersonTrainingSectionRequired() {
        return personTrainingSectionRequired;
    }

    public void setPersonTrainingSectionRequired(boolean personTrainingSectionRequired) {
        this.personTrainingSectionRequired = personTrainingSectionRequired;
    }   

}
