/*
 * Copyright 2006-2009 The Kuali Foundation
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
package org.kuali.kra.proposaldevelopment.web.struts.action;

import static java.util.Collections.sort;
import static org.kuali.kra.infrastructure.KraServiceLocator.getService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.kra.bo.Rolodex;
import org.kuali.kra.bo.ScienceKeyword;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.proposaldevelopment.bo.PropScienceKeyword;
import org.kuali.kra.proposaldevelopment.bo.ProposalLocation;
import org.kuali.kra.proposaldevelopment.bo.ProposalPersonComparator;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.proposaldevelopment.rule.event.AddProposalLocationEvent;
import org.kuali.kra.proposaldevelopment.service.KeyPersonnelService;
import org.kuali.kra.proposaldevelopment.service.ProposalDevelopmentService;
import org.kuali.kra.proposaldevelopment.web.struts.form.ProposalDevelopmentForm;
import org.kuali.kra.rice.shim.UniversalUser;
import org.kuali.kra.service.SponsorService;
import org.kuali.rice.kew.util.KEWConstants;
import org.kuali.rice.kns.bo.PersistableBusinessObject;
import org.kuali.rice.kns.document.Document;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.service.KualiConfigurationService;
import org.kuali.rice.kns.service.KualiRuleService;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.util.KNSConstants;

public class ProposalDevelopmentProposalAction extends ProposalDevelopmentAction {
    private static final Log LOG = LogFactory.getLog(ProposalDevelopmentProposalAction.class);

    @Override
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        setKeywordsPanelFlag(request);
        ProposalDevelopmentDocument proposalDevelopmentDocument = ((ProposalDevelopmentForm) form).getDocument();

        KraServiceLocator.getService(ProposalDevelopmentService.class).initializeUnitOrganzationLocation(
                proposalDevelopmentDocument);

        return super.save(mapping, form, request, response);
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionForward actionForward = super.execute(mapping, form, request, response);
        KeyPersonnelService kpservice=KraServiceLocator.getService(KeyPersonnelService.class);
        setKeywordsPanelFlag(request);
        ProposalDevelopmentDocument proposalDevelopmentDocument = ((ProposalDevelopmentForm) form).getDocument();
        if (proposalDevelopmentDocument.getDevelopmentProposal().getOrganizationId() != null
                && proposalDevelopmentDocument.getDevelopmentProposal().getProposalLocations().size() == 0
                && StringUtils.isNotBlank(request.getParameter("methodToCall"))
                && request.getParameter("methodToCall").toString().equals("refresh")
                && StringUtils.isNotBlank(request.getParameter("refreshCaller"))
                && request.getParameter("refreshCaller").toString().equals("kualiLookupable")
                && StringUtils.isNotBlank(request.getParameter("document.organizationId"))) {
            // populate 1st location. Not sure yet
            ProposalLocation propLocation = new ProposalLocation();
            propLocation.setLocation(proposalDevelopmentDocument.getDevelopmentProposal().getOrganization().getOrganizationName());
            propLocation.setLocationSequenceNumber(proposalDevelopmentDocument
                    .getDocumentNextValue(Constants.PROPOSAL_LOCATION_SEQUENCE_NUMBER));
            proposalDevelopmentDocument.getDevelopmentProposal().getProposalLocations().add(propLocation);
        }

        // populate the Prime Sponsor Name if we have the code
        // this is necessary since the name is only on the form not the document
        // and it is only populated by a lookup or through AJAX/DWR
        String primeSponsorCode = proposalDevelopmentDocument.getDevelopmentProposal().getPrimeSponsorCode();
        if (StringUtils.isNotEmpty(primeSponsorCode)) {
            String primeSponsorName = (KraServiceLocator.getService(SponsorService.class).getSponsorName(primeSponsorCode));
            if (StringUtils.isEmpty(primeSponsorName)) {
                primeSponsorName = "not found";
            }
            ((ProposalDevelopmentForm) form).setPrimeSponsorName(primeSponsorName);
        }
        else {
            // TODO: why do we have to do this?
            ((ProposalDevelopmentForm) form).setPrimeSponsorName(null);
        }

        if (proposalDevelopmentDocument.getDevelopmentProposal().getProposalPersons().size() > 0)
            sort(proposalDevelopmentDocument.getDevelopmentProposal().getProposalPersons(), new ProposalPersonComparator());

        if (proposalDevelopmentDocument.getDevelopmentProposal().getInvestigators().size() > 0)
            sort(proposalDevelopmentDocument.getDevelopmentProposal().getInvestigators(), new ProposalPersonComparator());
           
        kpservice.isSponsorNIH(proposalDevelopmentDocument);
        
        return actionForward;
    }

    
    /**
     * 
     * This method sets the flag for keyword display panel - display keyword panel if parameter is set to true
     * 
     * @param request
     */
    private void setKeywordsPanelFlag(HttpServletRequest request) {
        String keywordPanelDisplay = KraServiceLocator.getService(KualiConfigurationService.class).getParameterValue(
                Constants.PARAMETER_MODULE_PROPOSAL_DEVELOPMENT, Constants.PARAMETER_COMPONENT_DOCUMENT,
                Constants.KEYWORD_PANEL_DISPLAY);
        request.getSession().setAttribute(Constants.KEYWORD_PANEL_DISPLAY, keywordPanelDisplay);
    }

    /**
     * 
     * Add new location to proposal document and reset newProplocation from form.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward addLocation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProposalDevelopmentForm proposalDevelopmentForm = (ProposalDevelopmentForm) form;
        ProposalLocation newProposalLocation = proposalDevelopmentForm.getNewPropLocation();
        if (getKualiRuleService().applyRules(
                new AddProposalLocationEvent(Constants.EMPTY_STRING, proposalDevelopmentForm.getDocument(),
                    newProposalLocation))) {
            newProposalLocation.setLocationSequenceNumber(proposalDevelopmentForm.getDocument()
                    .getDocumentNextValue(Constants.PROPOSAL_LOCATION_SEQUENCE_NUMBER));
            proposalDevelopmentForm.getDocument().getDevelopmentProposal().getProposalLocations().add(newProposalLocation);
            proposalDevelopmentForm.setNewPropLocation(new ProposalLocation());
        }

        return mapping.findForward("basic");
    }

    /**
     * 
     * Delete one location/site from proposal document
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deleteLocation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProposalDevelopmentForm proposalDevelopmentForm = (ProposalDevelopmentForm) form;
        proposalDevelopmentForm.getDocument().getDevelopmentProposal().getProposalLocations().remove(getLineToDelete(request));
        return mapping.findForward("basic");
    }

    /**
     * 
     * Clear the address from the site/location selected.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward clearAddress(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProposalDevelopmentForm proposalDevelopmentForm = (ProposalDevelopmentForm) form;
        int index = getSelectedLine(request);

        proposalDevelopmentForm.getDocument().getDevelopmentProposal().getProposalLocations().get(index).setRolodexId(new Integer(0));
        proposalDevelopmentForm.getDocument().getDevelopmentProposal().getProposalLocations().get(index).setRolodex(new Rolodex());

        return mapping.findForward("basic");
    }

    private boolean isDuplicateKeyword(String newScienceKeywordCode, List<PropScienceKeyword> keywords) {
        for (Iterator iter = keywords.iterator(); iter.hasNext();) {
            PropScienceKeyword propScienceKeyword = (PropScienceKeyword) iter.next();
            String scienceKeywordCode = propScienceKeyword.getScienceKeywordCode();
            if (scienceKeywordCode.equalsIgnoreCase(newScienceKeywordCode)) {
                // duplicate keyword
                return true;
            }
        }
        return false;
    }

    public ActionForward selectAllScienceKeyword(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProposalDevelopmentForm proposalDevelopmentForm = (ProposalDevelopmentForm) form;
        ProposalDevelopmentDocument proposalDevelopmentDocument = proposalDevelopmentForm.getDocument();
        List<PropScienceKeyword> keywords = proposalDevelopmentDocument.getDevelopmentProposal().getPropScienceKeywords();
        for (Iterator iter = keywords.iterator(); iter.hasNext();) {
            PropScienceKeyword propScienceKeyword = (PropScienceKeyword) iter.next();
            propScienceKeyword.setSelectKeyword(true);
        }

        return mapping.findForward("basic");
    }

    public ActionForward deleteSelectedScienceKeyword(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProposalDevelopmentForm proposalDevelopmentForm = (ProposalDevelopmentForm) form;
        ProposalDevelopmentDocument proposalDevelopmentDocument = proposalDevelopmentForm.getDocument();
        List<PropScienceKeyword> keywords = proposalDevelopmentDocument.getDevelopmentProposal().getPropScienceKeywords();
        List<PropScienceKeyword> newKeywords = new ArrayList<PropScienceKeyword>();
        for (Iterator iter = keywords.iterator(); iter.hasNext();) {
            PropScienceKeyword propScienceKeyword = (PropScienceKeyword) iter.next();
            if (!propScienceKeyword.getSelectKeyword()) {
                newKeywords.add(propScienceKeyword);
            }
        }
        proposalDevelopmentDocument.getDevelopmentProposal().setPropScienceKeywords(newKeywords);

        return mapping.findForward("basic");
    }

    @Override
    public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        super.refresh(mapping, form, request, response);
        ProposalDevelopmentForm proposalDevelopmentForm = (ProposalDevelopmentForm) form;
        ProposalDevelopmentDocument proposalDevelopmentDocument = proposalDevelopmentForm.getDocument();
        
        if (proposalDevelopmentDocument != null && proposalDevelopmentDocument.getDevelopmentProposal().getPerformingOrganizationId() != null 
				&& proposalDevelopmentDocument.getDevelopmentProposal().getOwnedByUnit() != null && proposalDevelopmentDocument.getDevelopmentProposal().getOwnedByUnit().getOrganizationId() != null 
				&& ((StringUtils.equalsIgnoreCase(proposalDevelopmentDocument.getDevelopmentProposal().getPerformingOrganizationId(), proposalDevelopmentDocument.getDevelopmentProposal().getOwnedByUnit().getOrganizationId())) 
						|| (StringUtils.equalsIgnoreCase(proposalDevelopmentDocument.getDevelopmentProposal().getPerformingOrganizationId().trim(), "")))) {
            proposalDevelopmentDocument.getDevelopmentProposal().setPerformingOrganizationId(proposalDevelopmentDocument.getDevelopmentProposal().getOrganizationId());
        }
        
        proposalDevelopmentDocument.getDevelopmentProposal().refreshReferenceObject("organization");
        proposalDevelopmentDocument.getDevelopmentProposal().refreshReferenceObject("performingOrganization");
                
        for (ProposalLocation proposalLocation : proposalDevelopmentDocument.getDevelopmentProposal().getProposalLocations()) {
            proposalLocation.refreshReferenceObject("rolodex");
        }
        
        // check to see if we are coming back from a lookup
        if (Constants.MULTIPLE_VALUE.equals(proposalDevelopmentForm.getRefreshCaller())) {
            // Multivalue lookup. Note that the multivalue keyword lookup results are returned persisted to avoid using session.
            // Since URLs have a max length of 2000 chars, field conversions can not be done.
            String lookupResultsSequenceNumber = proposalDevelopmentForm.getLookupResultsSequenceNumber();
            if (StringUtils.isNotBlank(lookupResultsSequenceNumber)) {
                Class lookupResultsBOClass = Class.forName(proposalDevelopmentForm.getLookupResultsBOClassName());
                Collection<PersistableBusinessObject> rawValues = KNSServiceLocator.getLookupResultsService()
                        .retrieveSelectedResultBOs(lookupResultsSequenceNumber, lookupResultsBOClass,
                                ((UniversalUser) GlobalVariables.getUserSession().getPerson()).getPersonUniversalIdentifier());
                if (lookupResultsBOClass.isAssignableFrom(ScienceKeyword.class)) {
                    for (Iterator iter = rawValues.iterator(); iter.hasNext();) {
                        ScienceKeyword scienceKeyword = (ScienceKeyword) iter.next();
                        PropScienceKeyword propScienceKeyword = new PropScienceKeyword(proposalDevelopmentDocument
                                .getDevelopmentProposal().getProposalNumber(), scienceKeyword);
                        // ignore / drop duplicates
                        if (!isDuplicateKeyword(propScienceKeyword.getScienceKeywordCode(), proposalDevelopmentDocument
                                .getDevelopmentProposal().getPropScienceKeywords())) {
                            proposalDevelopmentDocument.getDevelopmentProposal().addPropScienceKeyword(propScienceKeyword);
                        }
                    }
                }
            }
        }
        return mapping.findForward("basic");
    }

    @Override
    protected KualiRuleService getKualiRuleService() {
        return getService(KualiRuleService.class);
    }

    @Override
    public ActionForward headerTab(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProposalDevelopmentForm pdform = (ProposalDevelopmentForm) form;
        
        String command = request.getParameter("command");
        String docId = request.getParameter("docId");

        if(StringUtils.isNotEmpty(command) && command.equals("displayDocSearchView") && StringUtils.isNotEmpty(docId)) {
            //This means that the user has come thru the Document Search Page - Copy Action
            Document retrievedDocument = KNSServiceLocator.getDocumentService().getByDocumentHeaderId(docId);
            pdform.setDocument(retrievedDocument);
        }
        
        return super.headerTab(mapping, form, request, response);
    }

    /**
     * 
     * Clears the Mailing Name and Address selected from Delivery info panel.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward clearMailingNameAddress(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProposalDevelopmentForm proposalDevelopmentForm = (ProposalDevelopmentForm) form;
        if (proposalDevelopmentForm.getDocument().getDevelopmentProposal().getRolodex() != null) {
        
            proposalDevelopmentForm.getDocument().getDevelopmentProposal().getRolodex().setAddressLine1("");
            proposalDevelopmentForm.getDocument().getDevelopmentProposal().getRolodex().setAddressLine2("");
            proposalDevelopmentForm.getDocument().getDevelopmentProposal().getRolodex().setAddressLine3("");
            proposalDevelopmentForm.getDocument().getDevelopmentProposal().getRolodex().setCity("");
            proposalDevelopmentForm.getDocument().getDevelopmentProposal().getRolodex().setOrganization("");
            proposalDevelopmentForm.getDocument().getDevelopmentProposal().getRolodex().setState("");
        
        }
      return mapping.findForward("basic");
    }

}
