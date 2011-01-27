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
package org.kuali.kra.award.web.struts.action;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.StringUtils.substringBetween;
import static org.kuali.rice.kns.util.KNSConstants.METHOD_TO_CALL_ATTRIBUTE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.common.util.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.kra.award.AwardForm;
import org.kuali.kra.award.awardhierarchy.sync.AwardSyncPendingChangeBean;
import org.kuali.kra.award.awardhierarchy.sync.AwardSyncType;
import org.kuali.kra.award.contacts.AwardCreditSplitBean;
import org.kuali.kra.award.contacts.AwardPerson;
import org.kuali.kra.award.contacts.AwardPersonUnit;
import org.kuali.kra.award.contacts.AwardProjectPersonnelBean;
import org.kuali.kra.award.contacts.AwardSponsorContact;
import org.kuali.kra.award.contacts.AwardSponsorContactsBean;
import org.kuali.kra.award.contacts.AwardUnitContactsBean;
import org.kuali.kra.award.home.Award;
import org.kuali.kra.award.home.ContactRole;
import org.kuali.kra.bo.Unit;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.proposaldevelopment.bo.ProposalPersonRole;
import org.kuali.kra.service.ServiceHelper;
import org.kuali.kra.service.SponsorService;
import org.kuali.kra.web.struts.action.StrutsConfirmation;
import org.kuali.rice.kns.service.BusinessObjectService;

/**
 * 
 * This class represents the Struts Action for Contacts page(AwardContacts.jsp)
 */
public class AwardContactsAction extends AwardAction {
    
    private static final String PROJECT_PERSON_PREFIX = ".personIndex";
    private static final String LINE_SUFFIX = ".line";
    private static final String CONFIRM_SYNC_UNIT_CONTACTS = "confirmSyncUnitContacts";
    private static final String CONFIRM_SYNC_UNIT_CONTACTS_KEY = "confirmSyncUnitContactsKey";


    @Override
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AwardForm awardForm = (AwardForm) form;
        Award award = awardForm.getAwardDocument().getAward();
        ActionForward forward;
        if (isValidSave(awardForm)) {
            processAwardPersonChanges(award);
            
            //String leadUnitNumber = award.getAwardDocument().getLeadUnitNumber();
            //System.err.println("in AwardContactsAction, before setLeadUnitOnAwardFromPILeadUnit leadUnitNumber: " + leadUnitNumber);
            //System.err.println(" and the boolean null check:'" + (leadUnitNumber == null) + "'");
            
            setLeadUnitOnAwardFromPILeadUnit(award, awardForm);
            
            //leadUnitNumber = award.getAwardDocument().getLeadUnitNumber();
            //System.err.println("in AwardContactsAction, before init leadUnitNumber: " + leadUnitNumber);
            //System.err.println(" and the boolean null check:'" + (leadUnitNumber == null) + "'");
            
            award.initCentralAdminContacts();
            
            //leadUnitNumber = award.getAwardDocument().getLeadUnitNumber();
            //System.err.println("in AwardContactsAction, after init leadUnitNumber: " + leadUnitNumber);
            //System.err.println(" and the boolean null check:'" + (leadUnitNumber == null) + "'");
            
            forward = super.save(mapping, form, request, response);
        } else {
            forward = mapping.findForward(Constants.MAPPING_AWARD_BASIC);            
        }
        return forward;
    }
    
    private void processAwardPersonChanges(Award award) {
        ArrayList<AwardPerson> PIs = new ArrayList<AwardPerson>();
        for (AwardPerson formPerson : award.getProjectPersons()) {
            if (ContactRole.PI_CODE.equals(formPerson.getContactRole().getRoleCode())) {
                PIs.add(formPerson);
            }
        }
        
        if (PIs.size() > 1) {
            AwardPerson formPersonToSwitchToCOI = getPersonToChange(award, PIs);
            
            if (formPersonToSwitchToCOI != null) {
                Map params = new HashMap();
                params.put("PROP_PERSON_ROLE_ID", ContactRole.COI_CODE);
                ProposalPersonRole coiRole = (ProposalPersonRole) this.getBusinessObjectService().findByPrimaryKey(ProposalPersonRole.class, params);
                
                formPersonToSwitchToCOI.setContactRole(coiRole);
                formPersonToSwitchToCOI.setContactRoleCode(coiRole.getRoleCode());   
                if (award.getPrincipalInvestigator().getUnits().get(0) != null 
                        && award.getPrincipalInvestigator().getUnits().get(0).getUnitNumber() != null) {
                    award.setUnitNumber(award.getPrincipalInvestigator().getUnits().get(0).getUnitNumber());
                }
            }
        }

    }

    private AwardPerson getPersonToChange(Award award, ArrayList<AwardPerson> PIs) {
        Map params = new HashMap();
        params.put("AWARD_ID", award.getAwardId());
        Award databaseAward = (Award) this.getBusinessObjectService().findByPrimaryKey(Award.class, params);
        
        //boolean notFound = true;
        AwardPerson formPersonToSwitchToCOI = null;
        
        Iterator<AwardPerson> PIIterator = PIs.iterator();
        boolean foundPersonToChange = false;
        while (PIIterator.hasNext() && !foundPersonToChange){
            AwardPerson formPerson = PIIterator.next();
            Iterator<AwardPerson> databasePeople = databaseAward.getProjectPersons().iterator();
            while (databasePeople.hasNext() && !foundPersonToChange){
                AwardPerson databasePerson = databasePeople.next();
                if (formPerson.getAwardContactId().equals(databasePerson.getAwardContactId()) 
                        && ContactRole.PI_CODE.equals(databasePerson.getContactRole().getRoleCode())) {
                    //this form person is a PI in the database, therefore this person is the one we need to change, if he wasn't a PI
                    //in the data base, then that is the newer PI
                    foundPersonToChange = true;
                    formPersonToSwitchToCOI = formPerson;
                }
            }
            
            if (!PIIterator.hasNext() && !foundPersonToChange && PIs.size() > 1) {
                // we have two new people and both are set to PI, just use the last one to switch to COI
                formPersonToSwitchToCOI = formPerson;
            }
        }
        return formPersonToSwitchToCOI;
    }
    
    public ActionForward reload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AwardForm awardForm = (AwardForm) form;
        ActionForward forward = super.reload(mapping, form, request, response);
        awardForm.getAwardDocument().getAward().initCentralAdminContacts();

        return forward;
    }
    
    /**
     * This method is called to reset the Lead Unit on the award if the lead unit is changed on the PI.
     * @param award
     */
    @SuppressWarnings("unchecked")
    private void setLeadUnitOnAwardFromPILeadUnit(Award award, AwardForm awardForm) {
        for (AwardPerson person : award.getProjectPersons()) {
            if (person.isPrincipalInvestigator()) {
                
                String unitToUse;
                //if (!StringUtils.isEmpty(awardForm.getProjectPersonnelBean().getSelectedLeadUnit())){
                  //  unitToUse = awardForm.getProjectPersonnelBean().getSelectedLeadUnit();
                //} else if (person.getUnit(0) != null) {
                    unitToUse = person.getUnit(0).getUnitNumber();
                //} else {
                  //  unitToUse = null;
                ////}
                
                System.err.println("unitToUse: " + unitToUse);
                
                List<Unit> units = (List<Unit>) getBusinessObjectService().findMatching(Unit.class, 
                        ServiceHelper.getInstance().buildCriteriaMap("UNIT_NUMBER", unitToUse));
                if (units.size() > 0 && units.get(0) != null) {
                    Unit leadUnit = units.get(0);
                    award.setUnitNumber(leadUnit.getUnitNumber());
                    award.setLeadUnit(leadUnit);
                } else {
                    award.setUnitNumber(null);
                    award.setLeadUnit(null);
                }
            }
        }
    }
    
    
    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward addNewProjectPersonUnit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
                                                                                                                        throws Exception {
        
        AwardPersonUnit unit = getProjectPersonnelBean(form).addNewProjectPersonUnit(getSelectedLine(request));
        if (unit != null) {
            return confirmSyncAction(mapping, form, request, response, AwardSyncType.ADD_SYNC, unit, "projectPersons", null, mapping.findForward(Constants.MAPPING_AWARD_BASIC));       
        } else {
            return mapping.findForward(Constants.MAPPING_AWARD_BASIC);
        }

    }
    
    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward addProjectPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
                                                                                                                        throws Exception {
        AwardPerson awardPerson = getProjectPersonnelBean(form).addProjectPerson();
        if (awardPerson != null) {
            return this.confirmSyncAction(mapping, form, request, response, AwardSyncType.ADD_SYNC, awardPerson, "projectPersons", null, 
                    mapping.findForward(Constants.MAPPING_AWARD_BASIC));
        } else {
            return mapping.findForward(Constants.MAPPING_AWARD_BASIC);
        }

    }
    
    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward addSponsorContact(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
                                                                                                                        throws Exception {
        AwardSponsorContact contact = getSponsorContactsBean(form).addSponsorContact();
        if (contact != null) {
            return this.confirmSyncAction(mapping, form, request, response, AwardSyncType.ADD_SYNC, contact, "sponsorContacts", null, 
                    mapping.findForward(Constants.MAPPING_AWARD_BASIC));
        } else {
            return mapping.findForward(Constants.MAPPING_AWARD_BASIC);
        }
    }
    
    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward addUnitContact(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
                                                                                                                        throws Exception {
        getUnitContactsBean(form).addUnitContact();
        return mapping.findForward(Constants.MAPPING_AWARD_BASIC);
    }
    
    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deleteProjectPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
                                                                                                                        throws Exception {
        AwardPerson awardPerson = getProjectPersonnelBean(form).getProjectPersonnel().get(getLineToDelete(request));
        getProjectPersonnelBean(form).deleteProjectPerson(getLineToDelete(request));
        return this.confirmSyncAction(mapping, form, request, response, AwardSyncType.DELETE_SYNC, awardPerson, "projectPersons", null, mapping.findForward(Constants.MAPPING_AWARD_BASIC));
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deleteProjectPersonUnit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
                                                                                                                        throws Exception {
        AwardPersonUnit unit = getProjectPersonnelBean(form).getProjectPersonnel().get(getProjectPersonIndex(request)).getUnit(getLineToDelete(request));
        getProjectPersonnelBean(form).deleteProjectPersonUnit(getProjectPersonIndex(request), getLineToDelete(request));
        return this.confirmSyncAction(mapping, form, request, response, AwardSyncType.DELETE_SYNC, unit, "projectPersons", null, mapping.findForward(Constants.MAPPING_AWARD_BASIC));
    }
    
    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deleteUnitContact(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
                                                                                                                        throws Exception {

        getUnitContactsBean(form).deleteUnitContact(getLineToDelete(request));
        return mapping.findForward(Constants.MAPPING_AWARD_BASIC);
    }
    
    
    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deleteSponsorContact(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
                                                                                                                        throws Exception {
        AwardSponsorContact contact = getSponsorContactsBean(form).getSponsorContacts().get(getLineToDelete(request));
        getSponsorContactsBean(form).deleteSponsorContact(getLineToDelete(request));
        return this.confirmSyncAction(mapping, form, request, response, AwardSyncType.DELETE_SYNC, contact, "sponsorContacts", null, mapping.findForward(Constants.MAPPING_AWARD_BASIC));
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, ServletRequest request, ServletResponse response) throws Exception {
        ActionForward actionForward = super.execute(mapping, form, request, response);

        SponsorService sponsorService = getSponsorService();
        Award award = getAward(form);

        if (sponsorService.isSponsorNihMultiplePi(award)) {
            award.setNihDescription(getKeyPersonnelService().loadKeyPersonnelRoleDescriptions(true));
        }

        return actionForward;
    }

    /**
     * Simply returns and the recalculation will happen
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward recalculateCreditSplit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
                                                                                                                        throws Exception {
        getAwardCreditSplitBean(form).recalculateCreditSplit();
        return mapping.findForward(Constants.MAPPING_AWARD_BASIC);
    }
    
    /**
     * This is action called when sync the unit contacts is called from Award Unit contacts tab.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward syncDefaultUnitContactsToLeadUnit (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
                                                                                                                    throws Exception {
        return confirm(buildSyncUnitContactsConfirmationQuestion(mapping, form, request, response), CONFIRM_SYNC_UNIT_CONTACTS, "");
    }
    
    /**
     * 
     * This method is to build the confirmation question for syncing unit contacts.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @param deletePeriod
     * @return
     * @throws Exception
     */
    private StrutsConfirmation buildSyncUnitContactsConfirmationQuestion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return buildParameterizedConfirmationQuestion(mapping, form, request, response, CONFIRM_SYNC_UNIT_CONTACTS_KEY,
                KeyConstants.QUESTION_SYNC_UNIT_CONTACTS);
    }
    
    /**
     * This method is called if the user clicks 'yes' in confirmation question.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return mapping forward
     * @throws Exception
     */
    public ActionForward confirmSyncUnitContacts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AwardForm awardForm = (AwardForm) form;
        getUnitContactsBean(awardForm).syncAwardUnitContactsToLeadUnitContacts();
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    /**
     * @return
     */
    protected BusinessObjectService getBusinessObjectService() {
        return KraServiceLocator.getService(BusinessObjectService.class);
    }
    
    private int getProjectPersonIndex(HttpServletRequest request) {
        int selectedPersonIndex = -1;
        String parameterName = (String) request.getAttribute(METHOD_TO_CALL_ATTRIBUTE);
        if (isNotBlank(parameterName)) {
            selectedPersonIndex = Integer.parseInt(substringBetween(parameterName, PROJECT_PERSON_PREFIX, LINE_SUFFIX));
        }

        return selectedPersonIndex;
    }
    
    private AwardCreditSplitBean getAwardCreditSplitBean(ActionForm form) {
        return ((AwardForm) form).getAwardCreditSplitBean();
    }
    
    private AwardProjectPersonnelBean getProjectPersonnelBean(ActionForm form) {
        return ((AwardForm) form).getProjectPersonnelBean();
    }
    
    private AwardSponsorContactsBean getSponsorContactsBean(ActionForm form) {
        return ((AwardForm) form).getSponsorContactsBean();
    }
    
    private AwardUnitContactsBean getUnitContactsBean(ActionForm form) {
        return ((AwardForm) form).getUnitContactsBean();
    }
    
    public ActionForward syncProjectPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        AwardForm awardForm = (AwardForm)form;
        Award award = awardForm.getAwardDocument().getAward();
        AwardPerson person = award.getProjectPerson(getSelectedLine(request));
        getAwardSyncCreationService().addAwardSyncChange(award, new AwardSyncPendingChangeBean(AwardSyncType.ADD_SYNC, person, "projectPersons"));
        return mapping.findForward(Constants.MAPPING_AWARD_BASIC);
    }

    public ActionForward syncProjectPersonUnit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        AwardForm awardForm = (AwardForm)form;
        Award award = awardForm.getAwardDocument().getAward();
        AwardPersonUnit unit = award.getProjectPerson(getProjectPersonIndex(request)).getUnit(getSelectedLine(request));
        getAwardSyncCreationService().addAwardSyncChange(award, new AwardSyncPendingChangeBean(AwardSyncType.ADD_SYNC, unit, "projectPersons"));
        return mapping.findForward(Constants.MAPPING_AWARD_BASIC);
    }  
    
    public ActionForward syncSponsorContact(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        AwardForm awardForm = (AwardForm)form;
        Award award = awardForm.getAwardDocument().getAward();
        AwardSponsorContact contact = award.getSponsorContacts().get(getSelectedLine(request));
        getAwardSyncCreationService().addAwardSyncChange(award, new AwardSyncPendingChangeBean(AwardSyncType.ADD_SYNC, contact, "sponsorContacts"));
        return mapping.findForward(Constants.MAPPING_AWARD_BASIC);
    }    
    
}
