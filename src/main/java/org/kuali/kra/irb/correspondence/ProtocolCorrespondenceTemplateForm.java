/*
 * Copyright 2006-2010 The Kuali Foundation
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
package org.kuali.kra.irb.correspondence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.service.LookupService;
import org.kuali.rice.kns.util.ActionFormUtilMap;
import org.kuali.rice.kns.web.struts.form.KualiForm;

/**
 * 
 * Form of the ProtocolCorrespondenceTemplate.
 */
public class ProtocolCorrespondenceTemplateForm extends KualiForm {

    private static final long serialVersionUID = 6043169784839779473L;
    
    private List<ProtocolCorrespondenceType> correspondenceTypes;    
    private List<ProtocolCorrespondenceTemplate> newDefaultCorrespondenceTemplates;
    private List<ProtocolCorrespondenceTemplate> newCorrespondenceTemplates;
    private List<ProtocolCorrespondenceTemplate> deletedCorrespondenceTemplates;
    
    public ProtocolCorrespondenceTemplateForm() {
        super();
        this.setCorrespondenceTypes(initCorrespondenceTypes());
        this.resetForm();
        this.deletedCorrespondenceTemplates = new ArrayList<ProtocolCorrespondenceTemplate>();
    }

    public void setCorrespondenceTypes(List<ProtocolCorrespondenceType> correspondenceTypes) {
        this.correspondenceTypes = correspondenceTypes;
    }

    public List<ProtocolCorrespondenceType> getCorrespondenceTypes() {
        return correspondenceTypes;
    }

    public void setNewDefaultCorrespondenceTemplates(List<ProtocolCorrespondenceTemplate> newDefaultCorrespondenceTemplates) {
        this.newDefaultCorrespondenceTemplates = newDefaultCorrespondenceTemplates;
    }

    public List<ProtocolCorrespondenceTemplate> getNewDefaultCorrespondenceTemplates() {
        return newDefaultCorrespondenceTemplates;
    }

    public void setNewCorrespondenceTemplates(List<ProtocolCorrespondenceTemplate> newCorrespondenceTemplates) {
        this.newCorrespondenceTemplates = newCorrespondenceTemplates;
    }

    public List<ProtocolCorrespondenceTemplate> getNewCorrespondenceTemplates() {
        return newCorrespondenceTemplates;
    }

    public List<ProtocolCorrespondenceTemplate> getDeletedCorrespondenceTemplates() {
        return deletedCorrespondenceTemplates;
    }

    public void setDeletedCorrespondenceTemplates(List<ProtocolCorrespondenceTemplate> deletedCorrespondenceTemplates) {
        this.deletedCorrespondenceTemplates = deletedCorrespondenceTemplates;
    }

    /**
     * This method returns all existing correspondence types from the database
     * 
     * @return List<ProtocolCorrespondenceType> 
     */
    @SuppressWarnings("unchecked")
    private List<ProtocolCorrespondenceType> initCorrespondenceTypes() {
        LookupService lookupService = KNSServiceLocator.getLookupService();
        return (List<ProtocolCorrespondenceType>) lookupService.findCollectionBySearchUnbounded(ProtocolCorrespondenceType.class, new HashMap());
    }

    /**
     * 
     * This method resets the input fields for the default correspondence templates and the new committee correspondence templates.
     */
    @SuppressWarnings("unused")
    public void resetForm() {
        this.newDefaultCorrespondenceTemplates = new ArrayList<ProtocolCorrespondenceTemplate>();
        this.newCorrespondenceTemplates = new ArrayList<ProtocolCorrespondenceTemplate>();
        for (ProtocolCorrespondenceType correspondenceType : this.getCorrespondenceTypes()) {
            this.newDefaultCorrespondenceTemplates.add(new ProtocolCorrespondenceTemplate());
            this.newCorrespondenceTemplates.add(new ProtocolCorrespondenceTemplate());
        }
    }

    @Override
    public void populate(HttpServletRequest request) {
        super.populate(request);
        
        // Clear values finder cache so that new committees are recognized by the committee values finder (drop-down box). 
        if (getActionFormUtilMap() != null && getActionFormUtilMap() instanceof ActionFormUtilMap) {
            ((ActionFormUtilMap) getActionFormUtilMap()).setCacheValueFinderResults(false);
        }
    }
}
