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
package org.kuali.kra.coi.disclosure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.kra.coi.CoiAction;
import org.kuali.kra.coi.CoiDisclosure;
import org.kuali.kra.coi.CoiDisclosureDocument;
import org.kuali.kra.coi.CoiDisclosureForm;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.rule.event.KraDocumentEventBaseExtension;
import org.kuali.rice.core.config.ConfigContext;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.util.KNSConstants;
import org.springframework.util.CollectionUtils;

public class CoiDisclosureAction extends CoiAction {

   
    public ActionForward addDisclosurePersonUnit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        CoiDisclosureForm coiDisclosureForm = (CoiDisclosureForm) form;
        DisclosureHelper disclosureHelper = coiDisclosureForm.getDisclosureHelper();
        if (checkRule(new AddDisclosureReporterUnitEvent("disclosureHelper.newDisclosurePersonUnit", disclosureHelper.getNewDisclosurePersonUnit(),
                ((CoiDisclosureDocument)coiDisclosureForm.getDocument()).getCoiDisclosure().getDisclosureReporter().getDisclosurePersonUnits()))) {
            getCoiDisclosureService().addDisclosureReporterUnit(
                   ((CoiDisclosureDocument)coiDisclosureForm.getDocument()).getCoiDisclosure().getDisclosureReporter(),
                   disclosureHelper.getNewDisclosurePersonUnit());
            disclosureHelper.setNewDisclosurePersonUnit(new DisclosurePersonUnit());
        }
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    public ActionForward deleteDisclosurePersonUnit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        CoiDisclosureForm coiDisclosureForm = (CoiDisclosureForm) form;
        DisclosureHelper disclosureHelper = coiDisclosureForm.getDisclosureHelper();
        getCoiDisclosureService().deleteDisclosureReporterUnit(((CoiDisclosureDocument)coiDisclosureForm.getDocument()).getCoiDisclosure().getDisclosureReporter(), disclosureHelper.getDeletedUnits(), getSelectedLine(request));
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    private CoiDisclosureService getCoiDisclosureService() {
        return KraServiceLocator.getService(CoiDisclosureService.class);
    }

    private boolean checkRule(KraDocumentEventBaseExtension event) {
        return event.getRule().processRules(event);
    }

    @Override
    public final ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        
        ActionForward actionForward = mapping.findForward(Constants.MAPPING_BASIC);
        CoiDisclosureForm coiDisclosureForm = (CoiDisclosureForm) form;
        CoiDisclosure coiDisclosure = ((CoiDisclosureDocument)coiDisclosureForm.getDocument()).getCoiDisclosure();
        if (coiDisclosure.getCoiDisclosureId() == null) {
            coiDisclosure.initRequiredFields();            
        } else {
            getCoiDisclosureService().resetLeadUnit(coiDisclosure.getDisclosureReporter());
        }
        actionForward = super.save(mapping, form, request, response);
        if (KNSConstants.SAVE_METHOD.equals(coiDisclosureForm.getMethodToCall()) && coiDisclosureForm.isAuditActivated() 
                && GlobalVariables.getMessageMap().hasNoErrors()) {
            actionForward = mapping.findForward("disclosureActions");
        }

        return actionForward;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // TODO Auto-generated method stub
        ActionForward actionForward = super.execute(mapping, form, request, response);
        CoiDisclosureDocument coiDisclosureDocument = (CoiDisclosureDocument)((CoiDisclosureForm) form).getDocument();
        coiDisclosureDocument.getCoiDisclosure().initSelectedUnit();
        checkToLoadDisclosureDetails(coiDisclosureDocument.getCoiDisclosure());
        return actionForward;

    }

    private void checkToLoadDisclosureDetails(CoiDisclosure coiDisclosure) {
        // TODO : load FE disclosure when creating coi disc
        // still need more clarification on whether there is any other occasion this need to be loaded
        if (coiDisclosure.getCoiDisclosureId() == null && CollectionUtils.isEmpty(coiDisclosure.getCoiDiscDetails())) {
            getCoiDisclosureService().initializeDisclosureDetails(coiDisclosure);
        } else {
            getCoiDisclosureService().updateDisclosureDetails(coiDisclosure);
        }
        
    }
    
    public ActionForward newFinancialEntity(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionForward actionForward = save(mapping, form, request, response);
        if (GlobalVariables.getMessageMap().hasNoErrors()) {
            String forward = ConfigContext.getCurrentContextConfig().getProperty("kuali.docHandler.url.prefix") + 
                    "/financialEntityEditNew.do?methodToCall=addNewCoiDiscFinancialEntity&coiDocId="+((CoiDisclosureForm) form).getDocument().getDocumentNumber();
//            "/portal.do?channelTitle=Financial%20Entity&channelUrl=financialEntityManagement.do?methodToCall=editNew&coiDocId="+((CoiDisclosureForm) form).getDocument().getDocumentNumber();
            return new ActionForward(forward, true);
        }
        return actionForward;

    }

    public ActionForward editFinancialEntity(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionForward actionForward = save(mapping, form, request, response);
        if (GlobalVariables.getMessageMap().hasNoErrors()) {
            CoiDisclosureForm coiDisclosureForm = (CoiDisclosureForm) form;
            CoiDisclosure coiDisclosure = ((CoiDisclosureDocument)coiDisclosureForm.getDocument()).getCoiDisclosure();
            String forward = ConfigContext.getCurrentContextConfig().getProperty("kuali.docHandler.url.prefix") + 
                    "/financialEntityEditList.do?methodToCall=editActiveFinancialEntity&coiDocId="+((CoiDisclosureForm) form).getDocument().getDocumentNumber()+"&financialEntityHelper.editCoiEntityId="+coiDisclosure.getCoiDiscDetails().get(getSelectedLine(request)).getPersonFinIntDisclosureId() ;
           // "/portal.do?channelTitle=Financial%20Entity&channelUrl=financialEntityManagement.do?methodToCall=editList&coiDocId="+((CoiDisclosureForm) form).getDocument().getDocumentNumber()+"&financialEntityHelper.editEntityIndex"+getSelectedLine(request);
            return new ActionForward(forward, true);
        }
        return actionForward;

    }

}
