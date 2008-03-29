/*
 * Copyright 2007 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
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
package org.kuali.kra.proposaldevelopment.web.struts.action;


import static java.util.Collections.sort;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.StringUtils.substringBetween;
import static org.kuali.RiceConstants.EMPTY_STRING;
import static org.kuali.RiceConstants.METHOD_TO_CALL_ATTRIBUTE;
import static org.kuali.kra.infrastructure.Constants.CREDIT_SPLIT_ENABLED_FLAG;
import static org.kuali.kra.infrastructure.Constants.CREDIT_SPLIT_ENABLED_RULE_NAME;
import static org.kuali.kra.infrastructure.Constants.MAPPING_BASIC;
import static org.kuali.kra.infrastructure.Constants.NEW_PERSON_LOOKUP_FLAG;
import static org.kuali.kra.infrastructure.Constants.NEW_PROPOSAL_PERSON_PROPERTY_NAME;
import static org.kuali.kra.infrastructure.Constants.PARAMETER_COMPONENT_DOCUMENT;
import static org.kuali.kra.infrastructure.Constants.PARAMETER_MODULE_PROPOSAL_DEVELOPMENT;
import static org.kuali.kra.infrastructure.KraServiceLocator.getService;
import static org.kuali.kra.logging.FormattedLogger.debug;
import static org.kuali.kra.logging.FormattedLogger.info;
import static org.kuali.kra.logging.FormattedLogger.warn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.core.bo.PersistableBusinessObject;
import org.kuali.core.service.BusinessObjectService;
import org.kuali.core.service.KualiRuleService;
import org.kuali.kra.bo.Unit;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.proposaldevelopment.bo.ProposalPerson;
import org.kuali.kra.proposaldevelopment.bo.ProposalPersonComparator;
import org.kuali.kra.proposaldevelopment.bo.ProposalPersonDegree;
import org.kuali.kra.proposaldevelopment.bo.ProposalPersonRole;
import org.kuali.kra.proposaldevelopment.bo.ProposalPersonUnit;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.proposaldevelopment.rule.event.AddKeyPersonEvent;
import org.kuali.kra.proposaldevelopment.rule.event.ChangeKeyPersonEvent;
import org.kuali.kra.proposaldevelopment.rule.event.SaveKeyPersonEvent;
import org.kuali.kra.proposaldevelopment.web.struts.form.ProposalDevelopmentForm;

/**
 * Handles actions from the Key Persons page of the 
 * <code>{@link org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument}</code>
 *
 * @author $Author: lprzybyl $
 * @version $Revision: 1.50 $
 */
public class ProposalDevelopmentKeyPersonnelAction extends ProposalDevelopmentAction {
    private static final String MISSING_PARAM_MSG = "Couldn't find parameter '%s'";
    private static final String ROLE_CHANGED_MSG  = "roleChanged for person %s = %s";
    private static final String ADDED_PERSON_MSG  = "Added Proposal Person with proposalNumber = %s and proposalPersonNumber = %s";
    private static final String INV_SIZE_MSG      = "Number of investigators are %s";

    /**
     * @see org.kuali.core.web.struts.action.KualiDocumentActionBase#execute(ActionMapping, ActionForm, HttpServletRequest,
     *      HttpServletResponse)
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward retval = super.execute(mapping, form, request, response);
        prepare(form, request);
        return retval;
    }

    /**
     * Common helper method for preparing to <code>{@link #execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)}</code>
     * @param form ActionForm
     * @param request HttpServletRequest
     */
    void prepare(ActionForm form, HttpServletRequest request) {
        ProposalDevelopmentForm pdform = (ProposalDevelopmentForm) form;
        request.setAttribute(NEW_PERSON_LOOKUP_FLAG, EMPTY_STRING);

        pdform.populatePersonEditableFields();
        handleRoleChangeEvents(pdform.getProposalDevelopmentDocument());
        
        debug(INV_SIZE_MSG, pdform.getProposalDevelopmentDocument().getInvestigators().size());
    
        try {
            boolean creditSplitEnabled = getConfigurationService().getIndicatorParameter(PARAMETER_MODULE_PROPOSAL_DEVELOPMENT, PARAMETER_COMPONENT_DOCUMENT, CREDIT_SPLIT_ENABLED_RULE_NAME)
                && pdform.getProposalDevelopmentDocument().getInvestigators().size() > 0;
            request.setAttribute(CREDIT_SPLIT_ENABLED_FLAG, new Boolean(creditSplitEnabled));
        }
        catch (Exception e) {
            warn(MISSING_PARAM_MSG, CREDIT_SPLIT_ENABLED_RULE_NAME);
            warn(e.getMessage());
        }        
    }

    
    /**
     * Called to handle situations when the <code>{@link ProposalPersonRole}</code> is changed on a <code>{@link ProposalPerson}</code>. It
     * does this by looping through a <code>{@link List}</code> of <code>{@link ProposalPerson}</code> instances in a 
     * <code>{@link ProposalDevelopmentDocument}</code>
     *  
     * @param document <code>{@link ProposalDevelopmentDocument}</code> instance with <code>{@link List}</code> of 
     * <code>{@link ProposalPerson}</code> instances. 
     */
    private void handleRoleChangeEvents(ProposalDevelopmentDocument document) {
        for (ProposalPerson person : document.getProposalPersons()) {
            debug(ROLE_CHANGED_MSG, person.getFullName(), person.isRoleChanged());
            
            if (person.isRoleChanged()) {
                changeRole(person, document);
            }
        }
    }

    /**
     * Takes the necessary steps to change a role of a <code>{@link ProposalPerson}</code> instance.<br/>
     * <br/>
     * This includes:
     * <ul>
     *   <li>Updating investigator flag on <code>{@link ProposalPerson}</code></li>
     *   <li>Removing or adding to the investigators <code>{@link List}</code> in the
     *   <code>{@link ProposalDevelopmentDocument}</code> </li>
     *   <li>Adding credit split defaults for investigators</li>
     *   <li>Deleting units and credit splits from key persons</li>
     * </ul>
     * <br/> 
     * This method is typically called from <code>{@link #handleRoleChangeEvents(ProposalDevelopmentDocument)</code>
     * 
     * @param person
     * @param document
     * @see {@link #handleRoleChangeEvents(ProposalDevelopmentDocument)}
     */
    private void changeRole(ProposalPerson person, ProposalDevelopmentDocument document) {
        getKeyPersonnelService().populateProposalPerson(person, document);

        if (!person.isInvestigator()) {
            // Cleanup from investigator related stuff
            document.getInvestigators().remove(person);
            
            // If they are not an Investigator, the homeUnit is not necessary
            person.setHomeUnit(EMPTY_STRING);
            
            List<PersistableBusinessObject> units = new ArrayList<PersistableBusinessObject>();
            units.addAll(person.getUnits());
            List<PersistableBusinessObject> creditSplits = new ArrayList<PersistableBusinessObject>();
            units.addAll(person.getCreditSplits());
            
            getBusinessObjectService().delete(units);
            person.getUnits().clear();
            getBusinessObjectService().delete(creditSplits);
            person.getCreditSplits().clear();
        }
    }

    /**
     * @see org.kuali.core.web.struts.action.KualiDocumentActionBase#refresh(ActionMapping, ActionForm, HttpServletRequest,
     *      HttpServletResponse)
     */
    @Override
    public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProposalDevelopmentForm pdform = (ProposalDevelopmentForm) form;
        ProposalPerson person = null;
        
        if (!isBlank(pdform.getNewRolodexId())) {
            person = getKeyPersonnelService().createProposalPersonFromRolodexId(pdform.getNewRolodexId());
        }
        else if (!isBlank(pdform.getNewPersonId())) {
            person = getKeyPersonnelService().createProposalPersonFromPersonId(pdform.getNewPersonId());
        }
        
        if (person != null) {
            person.setProposalNumber(pdform.getProposalDevelopmentDocument().getProposalNumber());
            person.setProposalPersonRoleId(pdform.getNewProposalPerson().getProposalPersonRoleId());
            pdform.setNewProposalPerson(person);
            request.setAttribute(NEW_PERSON_LOOKUP_FLAG, new Boolean(true));
        }

        return mapping.findForward(MAPPING_BASIC);
    }
    
    /**
     * Locate from Spring the <code>{@link KualiRuleService}</code> singleton
     * 
     * @return KualiRuleService
     */
    private KualiRuleService getKualiRuleService() {
        return getService(KualiRuleService.class);
    }
   
    /**
     * Action for inserting a <code>{@link ProposalPerson}</code> into a <code>{@link ProposalDevelopmentDocument}</code>
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws Exception
     */
    public ActionForward insertProposalPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProposalDevelopmentForm pdform = (ProposalDevelopmentForm) form;
        ProposalDevelopmentDocument document = pdform.getProposalDevelopmentDocument();

        // check any business rules
        boolean rulePassed = getKualiRuleService().applyRules(new AddKeyPersonEvent(pdform.getDocument(), pdform.getNewProposalPerson()));
                
        // if the rule evaluation passed, let's add it
        if (rulePassed) {
            document.addProposalPerson(pdform.getNewProposalPerson());
            info(ADDED_PERSON_MSG, pdform.getNewProposalPerson().getProposalNumber(), pdform.getNewProposalPerson().getProposalPersonNumber());
            getKeyPersonnelService().populateProposalPerson(pdform.getNewProposalPerson(), document);
            sort(document.getProposalPersons(), new ProposalPersonComparator());
            sort(document.getInvestigators(), new ProposalPersonComparator());
            pdform.setNewProposalPerson(new ProposalPerson());
            pdform.setNewRolodexId("");
            pdform.setNewPersonId("");
        }
        
        return mapping.findForward(MAPPING_BASIC);
    }

    
    /**
     * Add a degree to a <code>{@link ProposalPerson}</code>
     *
     * @return ActionForward
     */
    public ActionForward insertDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProposalDevelopmentForm pdform = (ProposalDevelopmentForm) form;
        ProposalDevelopmentDocument document = pdform.getProposalDevelopmentDocument();

        int selectedPersonIndex = getSelectedPersonIndex(request, document);
        ProposalPerson person = document.getProposalPerson(selectedPersonIndex);
        ProposalPersonDegree degree = pdform.getNewProposalPersonDegree().get(selectedPersonIndex);
        degree.setDegreeSequenceNumber(pdform.getProposalDevelopmentDocument().getDocumentNextValue(Constants.PROPOSAL_PERSON_DEGREE_SEQUENCE_NUMBER));
         
        // check any business rules
        
        boolean rulePassed = getKualiRuleService().applyRules(new ChangeKeyPersonEvent(document, person, degree).getProxy(null));

        if (rulePassed) {
            person.addDegree(degree);
            degree.refreshReferenceObject("degreeType");
            pdform.getNewProposalPersonDegree().remove(selectedPersonIndex);
            pdform.getNewProposalPersonDegree().add(selectedPersonIndex,new ProposalPersonDegree());
        }

        return mapping.findForward(MAPPING_BASIC);
    }

    /**
     * Add a unit to a <code>{@link ProposalPerson}</code>
     *
     * @return ActionForward
     */
    public ActionForward insertUnit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProposalDevelopmentForm pdform = (ProposalDevelopmentForm) form;
        ProposalDevelopmentDocument document = pdform.getProposalDevelopmentDocument();

        int selectedPersonIndex = getSelectedPersonIndex(request, document);
        ProposalPerson person = document.getProposalPerson(selectedPersonIndex);
        ProposalPersonUnit unit = getKeyPersonnelService().createProposalPersonUnit(pdform.getNewProposalPersonUnit().get(selectedPersonIndex).getUnitNumber(), person);
        
        // check any business rules
        boolean rulePassed = getKualiRuleService().applyRules(new ChangeKeyPersonEvent(document, person, unit));

        if (rulePassed) {
            getKeyPersonnelService().addUnitToPerson(person, unit);

            pdform.getNewProposalPersonUnit().remove(selectedPersonIndex);
            pdform.getNewProposalPersonUnit().add(selectedPersonIndex,new Unit());
        }

        return mapping.findForward(MAPPING_BASIC);
    }

    /**
     * Remove a <code>{@link ProposalPerson}</code> from the <code>{@link ProposalDevelopmentDocument}</code>
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws Exception
     */
    public ActionForward deletePerson(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProposalDevelopmentForm pdform = (ProposalDevelopmentForm) form;
        ProposalDevelopmentDocument document = pdform.getProposalDevelopmentDocument();
        
        for (Iterator<ProposalPerson> person_it = document.getProposalPersons().iterator(); person_it.hasNext();) {
            ProposalPerson person = person_it.next();
            if (person.isDelete()) {
                person_it.remove();
                document.getInvestigators().remove(person);
                document.removePersonnelAttachmentForDeletedPerson(person);
            }
        }
        return mapping.findForward(MAPPING_BASIC);
    }

    /**
     * Remove a <code>{@link ProposalPersonUnit}</code> from a <code>{@link ProposalPerson}</code>
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws Exception
     */
    public ActionForward deleteUnit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProposalDevelopmentForm pdform = (ProposalDevelopmentForm) form;
        ProposalDevelopmentDocument document = pdform.getProposalDevelopmentDocument();
        
        ProposalPerson selectedPerson = getSelectedPerson(request, document);
        ProposalPersonUnit unit = selectedPerson.getUnit(getSelectedLine(request));
        unit.setDelete(true);
        
        // check any business rules
        boolean rulePassed = getKualiRuleService().applyRules(new ChangeKeyPersonEvent(document, selectedPerson, unit));

        if (rulePassed) {
            selectedPerson.getUnits().remove(getSelectedLine(request));
            getBusinessObjectService().delete(unit);
        }
        
        unit.setDelete(false);

        return mapping.findForward(MAPPING_BASIC);
    }

    /**
     * Remove a <code>{@link ProposalPersonDegree}</code> from a <code>{@link ProposalPerson}</code>
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws Exception
     */
    public ActionForward deleteDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProposalDevelopmentForm pdform = (ProposalDevelopmentForm) form;
        ProposalDevelopmentDocument document = pdform.getProposalDevelopmentDocument();
        
        ProposalPerson selectedPerson = getSelectedPerson(request, document);
        selectedPerson.getProposalPersonDegrees().remove(getSelectedLine(request));

        return mapping.findForward(MAPPING_BASIC);
    }

    /**
     * 
     * This method is to recalculate credit split
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws Exception
     */
    public ActionForward recalculateCreditSplit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {        

        return mapping.findForward(MAPPING_BASIC);
    }


    @Override
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProposalDevelopmentForm pdform = (ProposalDevelopmentForm) form;
        boolean rulePassed = true;

        // check any business rules
        rulePassed &= getKualiRuleService().applyRules(new SaveKeyPersonEvent(NEW_PROPOSAL_PERSON_PROPERTY_NAME, pdform.getDocument()));

        // if the rule evaluation passed, then save. It is possible that invoking save without checking rules first will
        // let the document save anyhow, so let's check first.
        if (rulePassed) {
            return super.save(mapping, form, request, response);
        }
        return mapping.findForward(MAPPING_BASIC);
    }

    /**
     * Parses the method to call attribute to pick off the line number which should have an action performed on it.
     * 
     * @param request
     * @param document the person is selected on
     * @return ProposalPerson
     */
    protected ProposalPerson getSelectedPerson(HttpServletRequest request, ProposalDevelopmentDocument document) {
        ProposalPerson retval = null;
        int selectedLine = -1;
        String parameterName = (String) request.getAttribute(METHOD_TO_CALL_ATTRIBUTE);
        if (isNotBlank(parameterName)) {
            int lineNumber = Integer.parseInt(substringBetween(parameterName, "proposalPerson[", "]."));
            retval = document.getProposalPerson(lineNumber);
        }

        return retval;
    }

    protected int getSelectedPersonIndex(HttpServletRequest request, ProposalDevelopmentDocument document) {
        ProposalPerson retval = null;
        int selectedPersonIndex = -1;
        String parameterName = (String) request.getAttribute(METHOD_TO_CALL_ATTRIBUTE);
        if (isNotBlank(parameterName)) {
            selectedPersonIndex = Integer.parseInt(substringBetween(parameterName, "personIndex","."));
        }

        return selectedPersonIndex;
    }


    private BusinessObjectService getBusinessObjectService() {
        return getService(BusinessObjectService.class);
    }
}
