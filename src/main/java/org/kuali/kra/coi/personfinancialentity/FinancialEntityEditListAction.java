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
package org.kuali.kra.coi.personfinancialentity;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.service.VersionException;
import org.kuali.rice.kns.question.ConfirmationQuestion;
import org.kuali.rice.kns.util.KNSConstants;

/**
 * 
 * This class for the struts action of 'my financial entities' page.
 */
public class FinancialEntityEditListAction extends FinancialEntityAction{
    private static final String DEACTIVATE_ENTITY_QUESTION="DeactivateEntity";
    // TODO : db column is '2000', but coeus shows 1000 limit; so just follow coeus message.
    private static final String DEACTIVATE_ENTITY_REASON_MAXLENGTH = "1000";
    private static final String PROCESS_STATUS_FINAL = "F";

    
    
    public ActionForward editActiveFinancialEntity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        FinancialEntityHelper financialEntityHelper = ((FinancialEntityForm) form).getFinancialEntityHelper();
        financialEntityHelper.setEditType(ACTIVATE_ENTITY);
        editFinancialEntity(form, request);
       return mapping.findForward(Constants.MAPPING_BASIC);
    }

    public ActionForward editInactiveFinancialEntity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        FinancialEntityHelper financialEntityHelper = ((FinancialEntityForm) form).getFinancialEntityHelper();
        financialEntityHelper.setEditType(INACTIVATE_ENTITY);
        editFinancialEntity(form, request);
       return mapping.findForward(Constants.MAPPING_BASIC);
    }


    /**
     * 
     * This method is called when 'edit' button is clicked for an active financial entity.  It will set up the
     * index, so ui will display the financial entity panel for editing.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    private void editFinancialEntity(ActionForm form, HttpServletRequest request) throws Exception {
        FinancialEntityHelper financialEntityHelper = ((FinancialEntityForm) form).getFinancialEntityHelper();

        int entityIndex = getSelectedLine(request);
        PersonFinIntDisclosure personFinIntDisclosure = getFinancialEntities(form).get(entityIndex);
        financialEntityHelper.setEditEntityIndex(entityIndex);
        financialEntityHelper.setEditRelationDetails(getFinancialEntityService().getFinancialEntityDataMatrixForEdit(personFinIntDisclosure.getPerFinIntDisclDetails()));
        financialEntityHelper.resetPrevSponsorCode();
    }

    private List<PersonFinIntDisclosure> getFinancialEntities(ActionForm form) {
        
        FinancialEntityHelper financialEntityHelper = ((FinancialEntityForm) form).getFinancialEntityHelper();
        if (StringUtils.equals(ACTIVATE_ENTITY, financialEntityHelper.getEditType())) {
            return ((FinancialEntityForm) form).getFinancialEntityHelper()
                .getActiveFinancialEntities();
        } else {
            return ((FinancialEntityForm) form).getFinancialEntityHelper()
                    .getInactiveFinancialEntities();
        }

    }
    
    /**
     * 
     * This method to inactive the selected financial entity
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward inactivateFinancialEntity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        int entityIndex = getSelectedLine(request);
        Object question = request.getParameter(KNSConstants.QUESTION_INST_ATTRIBUTE_NAME);
        Object buttonClicked = request.getParameter(KNSConstants.QUESTION_CLICKED_BUTTON);
        String reason = request.getParameter(KNSConstants.QUESTION_REASON_ATTRIBUTE_NAME);
        String callerString = String.format("inactivateFinancialEntity.line%s.anchor%s", entityIndex, 0);
        if (question == null) {
            return this.performQuestionWithInput(mapping, form, request, response, DEACTIVATE_ENTITY_QUESTION,
                    "Are you sure you want to deactivate this financial entity ?", KNSConstants.CONFIRMATION_QUESTION,
                    callerString, "");
        }
        else if ((DEACTIVATE_ENTITY_QUESTION.equals(question)) && ConfirmationQuestion.NO.equals(buttonClicked)) {
            // nothing to do.
        } else {
            if (StringUtils.isBlank(reason) || reason.length() > 1000) {
                if (reason == null) {
                    reason = ""; // Prevents null pointer exception in performQuestion
                }
                return this.performQuestionWithInputAgainBecauseOfErrors(mapping, form, request, response,
                        DEACTIVATE_ENTITY_QUESTION, "Are you sure you want to deactivate this financial entity ?",
                        KNSConstants.CONFIRMATION_QUESTION, callerString, "", reason,
                        KeyConstants.ERROR_DEACTIVATE_FINANCIAL_ENTITY_REASON_REQUIRED, KNSConstants.QUESTION_REASON_ATTRIBUTE_NAME,
                        DEACTIVATE_ENTITY_REASON_MAXLENGTH);
            } else {

                PersonFinIntDisclosure personFinIntDisclosure = ((FinancialEntityForm) form).getFinancialEntityHelper()
                        .getActiveFinancialEntities().get(entityIndex);
                ((FinancialEntityForm) form).getFinancialEntityHelper().setEditRelationDetails(getFinancialEntityService().getFinancialEntityDataMatrixForEdit(personFinIntDisclosure.getPerFinIntDisclDetails()));
                versionFinancialEntity(form, personFinIntDisclosure,2, reason);
            }
        }
        ((FinancialEntityForm) form).getFinancialEntityHelper().setEditEntityIndex(-1);
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    /**
     * 
     * This method to active the selected financial entity
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward activateFinancialEntity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        int entityIndex = getSelectedLine(request);
        FinancialEntityHelper financialEntityHelper = ((FinancialEntityForm) form).getFinancialEntityHelper();
        PersonFinIntDisclosure personFinIntDisclosure = financialEntityHelper.getInactiveFinancialEntities().get(entityIndex);
        financialEntityHelper.setEditRelationDetails(getFinancialEntityService().getFinancialEntityDataMatrixForEdit(personFinIntDisclosure.getPerFinIntDisclDetails()));
        versionFinancialEntity(form, personFinIntDisclosure,1, Constants.EMPTY_STRING);
        financialEntityHelper.setEditEntityIndex(-1);
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    /*
     * to version person financial entity and save.
     */
    private PersonFinIntDisclosure versionFinancialEntity(ActionForm form, PersonFinIntDisclosure personFinIntDisclosure, Integer statusCode, String statusDesc) throws VersionException {
        FinancialEntityHelper financialEntityHelper = ((FinancialEntityForm) form).getFinancialEntityHelper();
        PersonFinIntDisclosure newVersionDisclosure = getFinancialEntityService().versionPersonFinintDisclosure(personFinIntDisclosure, financialEntityHelper.getEditRelationDetails());

        newVersionDisclosure.setStatusCode(statusCode);
        newVersionDisclosure.setStatusDescription(statusDesc);
        // the auto-retrieve is true. If it is not refresh here, then after save, the status code return to '1'
        // same refresh for 'activate'
        newVersionDisclosure.refreshReferenceObject("finIntEntityStatus");
        saveFinancialEntity(form, newVersionDisclosure);
        return newVersionDisclosure;
//        ((FinancialEntityForm) form).getFinancialEntityHelper().setActiveFinancialEntities(getFinancialEntities(true));
//        ((FinancialEntityForm) form).getFinancialEntityHelper().setInactiveFinancialEntities(getFinancialEntities(false));
        
    }
    /**
     * 
     * This method to handle the submit action for edited financial entity
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward submit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        FinancialEntityHelper financialEntityHelper = ((FinancialEntityForm) form).getFinancialEntityHelper();

           int entityIndex = getSelectedLine(request);
            PersonFinIntDisclosure personFinIntDisclosure = getFinancialEntities(form).get(entityIndex);

            if (isValidToSave(personFinIntDisclosure, getErrotPropertyPrefix(form, entityIndex))) {
                if (StringUtils.equals(PROCESS_STATUS_FINAL, personFinIntDisclosure.getProcessStatus())) {
                    PersonFinIntDisclosure newFinIntDisclosure = versionFinancialEntity(form, personFinIntDisclosure,StringUtils.equals(ACTIVATE_ENTITY, financialEntityHelper.getEditType()) ? 1 : 2, Constants.EMPTY_STRING);
                    resetEditEntityIndex(form, newFinIntDisclosure.getPersonFinIntDisclosureId());
                } else {
                    personFinIntDisclosure.setProcessStatus(PROCESS_STATUS_FINAL);
                    resetFinEntityDet(financialEntityHelper, personFinIntDisclosure);
                    saveFinancialEntity(form, personFinIntDisclosure);                     
                }
            }
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    private String getErrotPropertyPrefix(ActionForm form, int entityIndex) {
        
        FinancialEntityHelper financialEntityHelper = ((FinancialEntityForm) form).getFinancialEntityHelper();
        if (StringUtils.equals(ACTIVATE_ENTITY, financialEntityHelper.getEditType())) {
            return "financialEntityHelper.activeFinancialEntities[" + entityIndex + "]";
        } else {
            return "financialEntityHelper.inactiveFinancialEntities[" + entityIndex + "]";
        }

    }

    /**
     * 
     * This method is to save the editing financial entity.  process_status is 'S'
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        FinancialEntityHelper financialEntityHelper = ((FinancialEntityForm) form).getFinancialEntityHelper();

        int entityIndex = getSelectedLine(request);
        PersonFinIntDisclosure personFinIntDisclosure = getFinancialEntities(form).get(entityIndex);

        if (isValidToSave(personFinIntDisclosure, getErrotPropertyPrefix(form, entityIndex))) {
            if (StringUtils.equals(PROCESS_STATUS_FINAL, personFinIntDisclosure.getProcessStatus())) {
                PersonFinIntDisclosure newVersionDisclosure = getFinancialEntityService().versionPersonFinintDisclosure(
                        personFinIntDisclosure, financialEntityHelper.getEditRelationDetails());
                newVersionDisclosure.setProcessStatus("S");
                newVersionDisclosure.setStatusDescription(Constants.EMPTY_STRING);
                saveFinancialEntity(form, newVersionDisclosure);
                resetEditEntityIndex(form, newVersionDisclosure.getPersonFinIntDisclosureId());
            }
            else {
                resetFinEntityDet(financialEntityHelper, personFinIntDisclosure);
                saveFinancialEntity(form, personFinIntDisclosure);
            }
            // saveFinancialEntity(form, personFinIntDisclosure);
        }
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    private void resetFinEntityDet(FinancialEntityHelper financialEntityHelper, PersonFinIntDisclosure personFinIntDisclosure) {
        if (CollectionUtils.isNotEmpty(personFinIntDisclosure.getPerFinIntDisclDetails())) {
            getBusinessObjectService().delete(personFinIntDisclosure.getPerFinIntDisclDetails());
        }
        personFinIntDisclosure.setPerFinIntDisclDetails(getFinancialEntityService().getFinDisclosureDetails(
                financialEntityHelper.getEditRelationDetails(), personFinIntDisclosure.getEntityNumber(),
                personFinIntDisclosure.getSequenceNumber()));
        
    }

    /*
     * after versioned, the retrieved list may not be exactly the same as before version.
     * so, this is just to make sure it is fine.
     */
    private void resetEditEntityIndex(ActionForm form, Long personFinIntDisclosureId) {
        FinancialEntityHelper financialEntityHelper = ((FinancialEntityForm) form).getFinancialEntityHelper();
        int i = 0;
        for (PersonFinIntDisclosure personFinIntDisclosure : getFinancialEntities(form)) {
            if (personFinIntDisclosure.getPersonFinIntDisclosureId().equals(personFinIntDisclosureId)) {
                financialEntityHelper.setEditEntityIndex(i);
                break;
            }
            i++;
        }
    }
}
