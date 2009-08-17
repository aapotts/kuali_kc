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
package org.kuali.kra.questionnaire;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.service.VersioningService;
import org.kuali.kra.service.impl.VersioningServiceImpl;
import org.kuali.rice.kew.util.KEWConstants;
import org.kuali.rice.kns.document.MaintenanceDocumentBase;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.service.SequenceAccessorService;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.util.KNSConstants;
import org.kuali.rice.kns.util.ObjectUtils;
import org.kuali.rice.kns.web.struts.action.KualiMaintenanceDocumentAction;

public class QuestionnaireMaintenanceDocumentAction extends KualiMaintenanceDocumentAction {
    // TODO : big mess is that questionquestions and usages can't be included in xmldoccontent
    // because maintframework - questions & usages are not defined in 'maintsections'
    // then it will not be in xmldoccontent
    @Override
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // TODO Auto-generated method stub
        // if (form instanceof QuestionnaireMaintenanceForm) {
        QuestionnaireMaintenanceForm qnForm = (QuestionnaireMaintenanceForm) form;
        Questionnaire oldBo = (Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument()).getOldMaintainableObject()
                .getBusinessObject();
        Questionnaire newBo = (Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument()).getNewMaintainableObject()
                .getBusinessObject();
        if (((MaintenanceDocumentBase) qnForm.getDocument()).getNewMaintainableObject().getMaintenanceAction().equals(
                KNSConstants.MAINTENANCE_COPY_ACTION)) {
            // newBo.setName(qnForm.getNewQuestionnaire().getName());
            // newBo.setDescription(qnForm.getNewQuestionnaire().getDescription());
            // newBo.setIsFinal(qnForm.getNewQuestionnaire().getIsFinal());
            newBo.setSequenceNumber(1);
            // TODO : set doc# here may cause confusion
            // newBo.setDocumentNumber(qnForm.getDocument().getDocumentNumber());
            if (oldBo.getQuestionnaireId().equals(newBo.getQuestionnaireId())) {
                Integer questionnaireId = Integer.parseInt(KraServiceLocator.getService(SequenceAccessorService.class)
                        .getNextAvailableSequenceNumber("SEQ_QUESTIONNAIRE_ID").toString());
                newBo.setQuestionnaireId(questionnaireId);

            }
        }
        else {
            // if (StringUtils.isNotBlank(qnForm.getSqlScripts())) {
            saveQn(qnForm);
            // }
            Questionnaire questionnaire = (Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument())
                    .getNewMaintainableObject().getBusinessObject();
            if (questionnaire.getSequenceNumber() == null) {
                // TODO : create new first time
                questionnaire.setSequenceNumber(1);
            }
            // questionnaire.refreshReferenceObject("questionnaireQuestions");
            // ((MaintenanceDocumentBase) qnForm.getDocument()).getNewMaintainableObject().setBusinessObject(questionnaire);
            String questions = assembleQuestions(qnForm);
            String usages = assembleUsages((Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument())
                    .getNewMaintainableObject().getBusinessObject());
            qnForm.setEditData(questions + "#;#" + usages);
            if (((MaintenanceDocumentBase) qnForm.getDocument()).getNewMaintainableObject().getMaintenanceAction().equals(
                    KNSConstants.MAINTENANCE_EDIT_ACTION)) {
                // TODO : force it to have the same key, so it can be approve later.
                // rice maintenance framework - 'edit' is expecting old & new have the same pk
                ((Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument()).getOldMaintainableObject().getBusinessObject())
                        .setQuestionnaireRefId(((Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument())
                                .getNewMaintainableObject().getBusinessObject()).getQuestionnaireRefId());
            }
        }
        return super.save(mapping, form, request, response);
    }

    @Override
    public ActionForward docHandler(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // TODO Auto-generated method stub
        ActionForward forward = super.docHandler(mapping, form, request, response);
        // if (form instanceof QuestionnaireMaintenanceForm) {
        // Questionnaire questionnaire = ((QuestionnaireMaintenanceForm) form).getNewQuestionnaire();
        // questionnaire.refreshReferenceObject("questionnaireQuestions");
        QuestionnaireMaintenanceForm qnForm = (QuestionnaireMaintenanceForm) form;
        if (((MaintenanceDocumentBase) qnForm.getDocument()).getNewMaintainableObject().getMaintenanceAction().equals(
                KNSConstants.MAINTENANCE_COPY_ACTION)) {
            // qnForm.setFromQuestionnaire((Questionnaire) ((MaintenanceDocumentBase)
            // qnForm.getDocument()).getOldMaintainableObject()
            // .getBusinessObject());
            // qnForm.setNewQuestionnaire((Questionnaire) ((MaintenanceDocumentBase)
            // qnForm.getDocument()).getNewMaintainableObject()
            // .getBusinessObject());
        }
        else {
            // qnForm.setNewQuestionnaire((Questionnaire) ((MaintenanceDocumentBase)
            // qnForm.getDocument()).getNewMaintainableObject()
            // .getBusinessObject());
            String questions = assembleQuestions(qnForm);
            String usages = assembleUsages((Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument())
                    .getNewMaintainableObject().getBusinessObject());
            qnForm.setEditData(questions + "#;#" + usages);
        }
        // }
        return forward;
    }


    // utility methods
    private String assembleQuestions(QuestionnaireMaintenanceForm questionnaireForm) {

        Questionnaire newQuestionnaire = (Questionnaire) ((MaintenanceDocumentBase) questionnaireForm.getDocument())
        // Questionnaire questionnaire = (Questionnaire) ((MaintenanceDocumentBase) questionnaireForm.getDocument())
                .getNewMaintainableObject().getBusinessObject();
        // newQuestionnaire.refreshReferenceObject("questionnaireQuestions");
        // if kramaintdoc i used then the following should be included in xmldoc
        Map<String, Object> fieldValues = new HashMap<String, Object>();
        fieldValues.put("questionnaireRefId", newQuestionnaire.getQuestionnaireRefId());
        // use findbypk is little stretched. But is is actually doing findmatching has nothing to do with pk
        Questionnaire questionnaire = (Questionnaire) KraServiceLocator.getService(BusinessObjectService.class).findByPrimaryKey(
                Questionnaire.class, fieldValues);
        // not sure why the collections are not retrieved ?
        // maybe these are added by runsql ?
        questionnaire.refreshReferenceObject("questionnaireQuestions");
        questionnaire.refreshReferenceObject("questionnaireUsages");
        /*
         * TODO : object retrieved by busobjservice then assign to newmain.busobj Then go thru document.save, always caused lots of
         * issue. so has copy it then assign to newmaint.busobj
         */
        Questionnaire copyQuestionnaire = (Questionnaire) ObjectUtils.deepCopy(questionnaire);
        // ((MaintenanceDocumentBase) questionnaireForm.getDocument()).getNewMaintainableObject().setBusinessObject(questionnaire);
        ((MaintenanceDocumentBase) questionnaireForm.getDocument()).getNewMaintainableObject().setBusinessObject(copyQuestionnaire);
        // questionnaire.refreshReferenceObject("questionnaireQuestions");
        questionnaireForm.setQuestionNumber(0);
        Collections.sort(questionnaire.getQuestionnaireQuestions(), new QuestionnaireQuestionComparator());
        String result = "parent-0";
        Integer parentNumber = 0;
        List<QuestionnaireQuestion> remainQuestions = new ArrayList<QuestionnaireQuestion>();
        for (QuestionnaireQuestion question : questionnaire.getQuestionnaireQuestions()) {
            if (!question.getParentQuestionNumber().equals(0)) {
                remainQuestions.add((QuestionnaireQuestion) ObjectUtils.deepCopy(question));
            }
        }
        for (QuestionnaireQuestion question : questionnaire.getQuestionnaireQuestions()) {
            if (question.getQuestionNumber() > questionnaireForm.getQuestionNumber()) {
                questionnaireForm.setQuestionNumber(question.getQuestionNumber());
            }
            // TODO : for now just load the 1st level question for editing
            if (question.getParentQuestionNumber().equals(0)) {
                // if (question.getParentQuestionNumber().compareTo(parentNumber) > 0) {
                // parentNumber = question.getParentQuestionNumber();
                // result = result + "#q#parent-" + parentNumber;
                // }
                // qqid/qid/seq/desc/qtypeid/qnum/cond/condvalue/parentqnum/questionseqnum
                String desc = question.getQuestion().getQuestion();
                // TODO : : need to deal with '"' in questio's description
                // also see QuestionLookupAction
                if (desc.indexOf("\"") > 0) {
                    desc = desc.replace("\"", "&#034;");
                }
                result = result + "#q#" + question.getQuestionnaireQuestionsId() + "#f#" + question.getQuestionRefIdFk() + "#f#"
                        + question.getQuestionSeqNumber() + "#f#" + desc + "#f#" + question.getQuestion().getQuestionTypeId()
                        + "#f#" + question.getQuestionNumber() + "#f#" + question.getCondition() + "#f#"
                        + question.getConditionValue() + "#f#" + question.getParentQuestionNumber() + "#f#"
                        + question.getQuestion().getSequenceNumber();
                String childrenResult = getChildren(question, remainQuestions);
                if (StringUtils.isNotBlank(childrenResult)) {
                    result = result + childrenResult;
                }

            }
        }
        questionnaireForm.setQuestionNumber(questionnaireForm.getQuestionNumber() + 1);
        // if (StringUtils.isNotBlank(result)) {
        // result = result.substring(0,result.length()-3);
        // }
        // TODO : test versioning
        Questionnaire qnaire = null;
        try {
            VersioningService versionService = new VersioningServiceImpl();
            qnaire = (Questionnaire) versionService.createNewVersion(questionnaire);
        }
        catch (Exception e) {

        }
        qnaire.getQuestionnaireId();
        return result;


    }

    private String getChildren(QuestionnaireQuestion questionnaireQuestion, List<QuestionnaireQuestion> questionnaireQuestions) {
        String result = "";
        List<QuestionnaireQuestion> remainQuestions = new ArrayList<QuestionnaireQuestion>();
        for (QuestionnaireQuestion question : questionnaireQuestions) {
            if (question.getParentQuestionNumber().equals(questionnaireQuestion.getQuestionNumber())) {
                String desc = question.getQuestion().getQuestion();
                if (desc.indexOf("\"") > 0) {
                    desc = desc.replace("\"", "&#034;");
                }
                result = result + "#q#" + question.getQuestionnaireQuestionsId() + "#f#" + question.getQuestionRefIdFk() + "#f#"
                        + question.getQuestionSeqNumber() + "#f#" + desc + "#f#" + question.getQuestion().getQuestionTypeId()
                        + "#f#" + question.getQuestionNumber() + "#f#" + question.getCondition() + "#f#"
                        + question.getConditionValue() + "#f#" + question.getParentQuestionNumber();
                // TODO : not efficient. should only check the questions that have not checked
                // remainQuestions = ObjectUtils.deepCopy(questionnaireQuestions);
                String childrenResult = getChildren(question, questionnaireQuestions);
                if (StringUtils.isNotBlank(childrenResult)) {
                    result = result + childrenResult;
                }
            }
        }
        return result;
    }

    private String assembleUsages(Questionnaire questionnaire) {
        String result = "";
        for (QuestionnaireUsage questionnaireUsage : questionnaire.getQuestionnaireUsages()) {
            // quid/modulecode/label
            result = result + questionnaireUsage.getQuestionnaireUsageId() + "#f#" + questionnaireUsage.getModuleItemCode() + "#f#"
                    + questionnaireUsage.getQuestionnaireLabel() + "#f#" + questionnaireUsage.getQuestionnaireSequenceNumber()
                    + "#u#";
        }
        if (StringUtils.isNotBlank(result)) {
            result = result.substring(0, result.length() - 3);
        }
        return result;

    }

    // private void setQuestionnaireRefid(Questionnaire questionnaire) {
    // Map<String, Object> fieldValues = new HashMap<String, Object>();
    // fieldValues.put("name", questionnaire.getName());
    // // use findbypk is little stretched. But is is actually doing findmatching has nothing to do with pk
    // Questionnaire questionnaire1 = (Questionnaire) KraServiceLocator.getService(BusinessObjectService.class).findByPrimaryKey(
    // Questionnaire.class, fieldValues);
    // if (questionnaire1 != null) {
    // questionnaire.setQuestionnaireRefId(questionnaire1.getQuestionnaireRefId());
    // questionnaire.setSequenceNumber(questionnaire1.getSequenceNumber());
    // questionnaire.setQuestionnaireId(questionnaire1.getQuestionnaireId());
    // }
    //
    // }

    @Override
    public ActionForward copy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // 'document' is null at this point
        ActionForward forward = super.copy(mapping, form, request, response);
        QuestionnaireMaintenanceForm qnForm = (QuestionnaireMaintenanceForm) form;
        Map<String, Object> fieldValues = new HashMap<String, Object>();
        fieldValues.put("questionnaireRefId", request.getParameter("questionnaireRefId"));
        Questionnaire questionnaire = (Questionnaire) KraServiceLocator.getService(BusinessObjectService.class).findByPrimaryKey(
                Questionnaire.class, fieldValues);
        ((MaintenanceDocumentBase) qnForm.getDocument()).getOldMaintainableObject().setBusinessObject(questionnaire);
        Questionnaire newQuestionnaire = new Questionnaire();
        newQuestionnaire.setDescription(questionnaire.getDescription());
        newQuestionnaire.setName(questionnaire.getName());
        ((MaintenanceDocumentBase) qnForm.getDocument()).getNewMaintainableObject().setBusinessObject(newQuestionnaire);
        return forward;
    }

    @Override
    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // 'document' is null at this point
        ActionForward forward = super.edit(mapping, form, request, response);
        // Integer questionnaireId = Integer.parseInt(request.getParameter("questionnaireId"));
        QuestionnaireMaintenanceForm qnForm = (QuestionnaireMaintenanceForm) form;
        ((Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument()).getNewMaintainableObject().getBusinessObject())
                .setQuestionnaireRefId(Long.parseLong(request.getParameter("questionnaireRefId")));

        // Map<String, Object> fieldValues = new HashMap<String, Object>();
        // fieldValues.put("questionnaireId", request.getParameter("questionnaireId"));
        // use findbypk is little stretched. But is is actually doing findmatching has nothing to do with pk
        // Questionnaire questionnaire =
        // (Questionnaire)KraServiceLocator.getService(BusinessObjectService.class).findByPrimaryKey(Questionnaire.class,
        // fieldValues);
        // qnForm.setNewQuestionnaire(questionnaire);
        String questions = assembleQuestions(qnForm);
        String usages = assembleUsages(((Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument()).getNewMaintainableObject()
                .getBusinessObject()));
        qnForm.setEditData(questions + "#;#" + usages);
        // TODO : hack for 1st save to version
        // qnForm.getNewQuestionnaire().setQuestionnaireId(0);
        return forward;
    }

    @Override
    public ActionForward route(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // TODO Auto-generated method stub
        QuestionnaireMaintenanceForm qnForm = (QuestionnaireMaintenanceForm) form;
        Questionnaire oldBo = (Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument()).getOldMaintainableObject()
                .getBusinessObject();
        Questionnaire newBo = (Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument()).getNewMaintainableObject()
                .getBusinessObject();
        if (((MaintenanceDocumentBase) qnForm.getDocument()).getNewMaintainableObject().getMaintenanceAction().equals(
                KNSConstants.MAINTENANCE_COPY_ACTION)) {
            // newBo.setName(qnForm.getNewQuestionnaire().getName());
            // newBo.setDescription(qnForm.getNewQuestionnaire().getDescription());
            // newBo.setIsFinal(qnForm.getNewQuestionnaire().getIsFinal());
            newBo.setSequenceNumber(1);
            newBo.setDocumentNumber(qnForm.getDocument().getDocumentNumber());
            if (oldBo.getQuestionnaireId().equals(newBo.getQuestionnaireId())) {
                Integer questionnaireId = Integer.parseInt(KraServiceLocator.getService(SequenceAccessorService.class)
                        .getNextAvailableSequenceNumber("SEQ_QUESTIONNAIRE_ID").toString());
                newBo.setQuestionnaireId(questionnaireId);

            }
        }
        else {
            saveQn(qnForm);
            // newBo.setName(qnForm.getNewQuestionnaire().getName());
            // newBo.setDescription(qnForm.getNewQuestionnaire().getDescription());
            // newBo.setIsFinal(qnForm.getNewQuestionnaire().getIsFinal());
            // newBo.setSequenceNumber(qnForm.getNewQuestionnaire().getSequenceNumber());
            // newBo.setVersionNumber(qnForm.getNewQuestionnaire().getVersionNumber());
            // ((MaintenanceDocumentBase) qnForm.getDocument()).getNewMaintainableObject()
            // .setBusinessObject(qnForm.getNewQuestionnaire());
            if (StringUtils.isNotBlank(qnForm.getSqlScripts())) {
                String questions = assembleQuestions(qnForm);
                String usages = assembleUsages(((Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument())
                        .getNewMaintainableObject().getBusinessObject()));
                qnForm.setEditData(questions + "#;#" + usages);
            }
        }
        Long questionnaireRefId = null;
        Long oldQuestionnaireRefId = null;
        if (((MaintenanceDocumentBase) qnForm.getDocument()).getNewMaintainableObject().getMaintenanceAction().equals(
                KNSConstants.MAINTENANCE_NEW_ACTION)) {
            questionnaireRefId = KraServiceLocator.getService(SequenceAccessorService.class).getNextAvailableSequenceNumber(
                    "SEQ_QUESTIONNAIRE_ID");
            oldQuestionnaireRefId = ((Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument()).getNewMaintainableObject()
                    .getBusinessObject()).getQuestionnaireRefId();
            Questionnaire newQuestionnaire = (Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument())
                    .getNewMaintainableObject().getBusinessObject();
            newQuestionnaire.setQuestionnaireRefId(questionnaireRefId);

        }
        if (((MaintenanceDocumentBase) qnForm.getDocument()).getNewMaintainableObject().getMaintenanceAction().equals(
                KNSConstants.MAINTENANCE_EDIT_ACTION)) {
            Map fieldValues = new HashMap<String, Object>();
            fieldValues.put("questionnaireRefId", ((Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument())
                    .getNewMaintainableObject().getBusinessObject()).getQuestionnaireRefId());
            Questionnaire oldQuestionnaire = (Questionnaire) KraServiceLocator.getService(BusinessObjectService.class)
                    .findByPrimaryKey(Questionnaire.class, fieldValues);
            Questionnaire newQuestionnaire = (Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument())
                    .getNewMaintainableObject().getBusinessObject();
            ((Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument()).getOldMaintainableObject().getBusinessObject())
                    .setQuestionnaireRefId(newQuestionnaire.getQuestionnaireRefId());
            newQuestionnaire.setDocumentNumber(qnForm.getDocument().getDocumentNumber());
            newQuestionnaire.setVersionNumber(oldQuestionnaire.getVersionNumber());
        }
        ActionForward forward = null;
        try {
            forward = super.route(mapping, form, request, response);
        }
        catch (Exception e) {
            if (((MaintenanceDocumentBase) qnForm.getDocument()).getNewMaintainableObject().getMaintenanceAction().equals(
                    KNSConstants.MAINTENANCE_NEW_ACTION)
                    && questionnaireRefId != null) {
                // this is only for new action
                // for edit, oldqrefid is null, so should not set here
                ((Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument()).getNewMaintainableObject().getBusinessObject())
                        .setQuestionnaireRefId(oldQuestionnaireRefId);
            }
            throw e;
        }
        // if there is rule violation, then it will be redirected directly to jsp page. the following script will not be executed.
        if (((MaintenanceDocumentBase) qnForm.getDocument()).getNewMaintainableObject().getMaintenanceAction().equals(
                KNSConstants.MAINTENANCE_NEW_ACTION)
                && questionnaireRefId != null) {
            Map fieldValues = new HashMap<String, Object>();
            fieldValues.put("questionnaireRefId", oldQuestionnaireRefId);
            Questionnaire oldQuestionnaire = (Questionnaire) KraServiceLocator.getService(BusinessObjectService.class)
                    .findByPrimaryKey(Questionnaire.class, fieldValues);
            oldQuestionnaire.refreshReferenceObject("questionnaireQuestions");
            oldQuestionnaire.refreshReferenceObject("questionnaireUsages");
            Questionnaire copyQuestionnaire = (Questionnaire) ObjectUtils.deepCopy(oldQuestionnaire);
            // Questionnaire newQuestionnaire = (Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument())
            // .getNewMaintainableObject().getBusinessObject();
            // super.route, also save the newquestionnaire (w/o questions & usages) in db but not committed.
            // retrieve it and attach questions and usages and save
            fieldValues.put("questionnaireRefId", questionnaireRefId);
            Questionnaire newQuestionnaire = (Questionnaire) KraServiceLocator.getService(BusinessObjectService.class)
                    .findByPrimaryKey(Questionnaire.class, fieldValues);
            newQuestionnaire.setQuestionnaireQuestions(copyQuestionnaire.getQuestionnaireQuestions());
            newQuestionnaire.setQuestionnaireUsages(copyQuestionnaire.getQuestionnaireUsages());
            // newQuestionnaire.setVersionNumber(1L);
            newQuestionnaire.setDocumentNumber(qnForm.getDocument().getDocumentNumber());
            fieldValues.put("questionnaireId", "2");
            Collection anaires = KraServiceLocator.getService(BusinessObjectService.class).findMatching(Questionnaire.class,
                    fieldValues);
            // for (QuestionnaireQuestion question : newQuestionnaire.getQuestionnaireQuestions()) {
            // question.setQuestionnaireRefIdFk(null);
            // question.setQuestionnaireQuestionsId(null);
            // question.setVersionNumber(new Long(1));
            // }
            // for (QuestionnaireUsage usage : newQuestionnaire.getQuestionnaireUsages()) {
            // usage.setQuestionnaireRefIdFk(null);
            // usage.setQuestionnaireUsageId(null);
            // usage.setVersionNumber(new Long(1));
            // }

            // List delBos = new ArrayList();
            // delBos.addAll(oldQuestionnaire.getQuestionnaireQuestions());
            // delBos.addAll(oldQuestionnaire.getQuestionnaireUsages());
            // oldQuestionnaire.setQuestionnaireQuestions(new ArrayList<QuestionnaireQuestion>());
            // oldQuestionnaire.setQuestionnaireUsages(new ArrayList<QuestionnaireUsage>());
            getBusinessObjectService().save(newQuestionnaire);
            // getBusinessObjectService().delete(delBos);
            getBusinessObjectService().delete(oldQuestionnaire);
        }
        if (((MaintenanceDocumentBase) qnForm.getDocument()).getNewMaintainableObject().getMaintenanceAction().equals(
                KNSConstants.MAINTENANCE_COPY_ACTION)) {
            Questionnaire newQuestionnaire = (Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument())
                    .getNewMaintainableObject().getBusinessObject();
            Map<String, Object> fieldValues = new HashMap<String, Object>();
            fieldValues.put("name", newQuestionnaire.getName());
            /*
             * TODO : this may have issue because same id with different version may have same name use findbypk is little
             * stretched. But is is actually doing findmatching has nothing to do with pk At this point, the new questionnaire is
             * created in db but not committed yet. Also, newmaintobj.busobj still is not updated with the refid/id, so have to
             * retrieve with 'name' if at this point,the name is unique because rule validation passed. but still like a little
             * hack. can we use documentnumber to retrieve ?
             */
            Questionnaire questionnaire = (Questionnaire) KraServiceLocator.getService(BusinessObjectService.class)
                    .findByPrimaryKey(Questionnaire.class, fieldValues);
            if (questionnaire != null) {
                fieldValues = new HashMap<String, Object>();
                fieldValues.put("questionnaireRefId", ((Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument())
                        .getOldMaintainableObject().getBusinessObject()).getQuestionnaireRefId());
                Questionnaire oldQuestionnaire = (Questionnaire) KraServiceLocator.getService(BusinessObjectService.class)
                        .findByPrimaryKey(Questionnaire.class, fieldValues);
                // Questionnaire copyQuestionnaire = (Questionnaire) ObjectUtils.deepCopy(oldQuestionnaire);
                // copyQuestionnaire.setQuestionnaireRefId(null);
                // copyQuestionnaire.setName(newQuestionnaire.getName());
                // copyQuestionnaire.setDescription(newQuestionnaire.getDescription());
                // copyQuestionnaire.setIsFinal(newQuestionnaire.getIsFinal());
                // for (QuestionnaireQuestion question : copyQuestionnaire.getQuestionnaireQuestions()) {
                // question.setQuestionnaireRefIdFk(null);
                // question.setVersionNumber(0L);
                // }
                // questionnaire.setQuestionnaireQuestions(copyQuestionnaire.getQuestionnaireQuestions());
                // questionnaire.setQuestionnaireUsages(copyQuestionnaire.getQuestionnaireUsages());
                // KraServiceLocator.getService(BusinessObjectService.class).save(questionnaire);
                KraServiceLocator.getService(QuestionnaireService.class).copyQuestionnaire(oldQuestionnaire, questionnaire);
            }

        }
        return forward;

    }

    @Override
    public ActionForward approve(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // TODO if status is final, then should be handled like 'route'
        return super.approve(mapping, form, request, response);
    }

    @Override
    public ActionForward blanketApprove(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // TODO if status is final, then should be handled like 'route'
        return super.blanketApprove(mapping, form, request, response);
    }

    private void saveQn(ActionForm form) {

        QuestionnaireMaintenanceForm qnForm = (QuestionnaireMaintenanceForm) form;
        Questionnaire questionnaire = ((Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument()).getNewMaintainableObject()
                .getBusinessObject());
        if (questionnaire.getSequenceNumber() == null) {
            questionnaire.setSequenceNumber(1);
        }
        String desc = questionnaire.getDescription();
        if (questionnaire.getQuestionnaireRefId() != null) {
            Map pkMap = new HashMap();
            pkMap.put("questionnaireRefId", questionnaire.getQuestionnaireRefId());
            Questionnaire oldQuestionnair = (Questionnaire) KraServiceLocator.getService(BusinessObjectService.class)
                    .findByPrimaryKey(Questionnaire.class, pkMap);
            // if (questionnaire.getQuestionnaireId().equals(0)) {
            if (questionnaire.getQuestionnaireId() != null
                    && qnForm.getMaintenanceAction().equals(KNSConstants.MAINTENANCE_EDIT_ACTION)
                    && qnForm.getDocStatus().equals(KEWConstants.ROUTE_HEADER_INITIATED_CD)) {
                try {
                    VersioningService versionService = new VersioningServiceImpl();
                    // questionnaire = (Questionnaire) versionService.createNewVersion(oldQuestionnair);
                    Questionnaire newQuestionnaire = (Questionnaire) versionService.createNewVersion(oldQuestionnair);
                    questionnaire.setQuestionnaireRefId(null);
                    questionnaire.setSequenceNumber(newQuestionnaire.getSequenceNumber());
                    questionnaire.setQuestionnaireQuestions(newQuestionnaire.getQuestionnaireQuestions());
                    questionnaire.setQuestionnaireUsages(newQuestionnaire.getQuestionnaireUsages());
                }
                catch (Exception e) {

                }

            }
            else {
                if (oldQuestionnair != null) {
                    // cretae new : 1st try failed, then try again, oldquestionnaire is null
                    // not sure why the newmainobj.busobject at this point has qnquestions & usages
                    questionnaire.setVersionNumber(oldQuestionnair.getVersionNumber());
                    questionnaire.setSequenceNumber(oldQuestionnair.getSequenceNumber());
                    questionnaire.setQuestionnaireId(oldQuestionnair.getQuestionnaireId());
                }
            }
        }
        // TODO : this makes newquestionnaire hooked to bo saved in db but not yet committed yet
        Questionnaire copyQuestionnaire = (Questionnaire) ObjectUtils.deepCopy(questionnaire);
        KraServiceLocator.getService(BusinessObjectService.class).save(copyQuestionnaire);
        questionnaire.setVersionNumber(copyQuestionnaire.getVersionNumber());
        // for editing, 1st time the refid will be the new one because versioning
        if (questionnaire.getQuestionnaireRefId() == null
                || !questionnaire.getQuestionnaireRefId().equals(copyQuestionnaire.getQuestionnaireRefId())) {
            questionnaire.setQuestionnaireRefId(copyQuestionnaire.getQuestionnaireRefId());
            ((MaintenanceDocumentBase) qnForm.getDocument()).getNewMaintainableObject().setBusinessObject(questionnaire);
        }
        // TODO : edit newquestionnaire.refid changed
        // qnForm.getNewQuestionnaire().setQuestionnaireRefId(questionnaire.getQuestionnaireRefId());
        // qnForm.getNewQuestionnaire().setSequenceNumber(questionnaire.getSequenceNumber());
        if (StringUtils.isNotBlank(qnForm.getSqlScripts())) {
            runSql(qnForm);
        }


    }

    private void runSql(ActionForm form) {
        QuestionnaireMaintenanceForm qnForm = (QuestionnaireMaintenanceForm) form;
        Questionnaire questionnaire = new Questionnaire();
        questionnaire = ((Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument()).getNewMaintainableObject()
                .getBusinessObject());
        String[] sqls = qnForm.getSqlScripts().split("#S#");
        for (int k = 0; k < sqls.length; k++) {
            KraServiceLocator.getService(QuestionnaireService.class).saveQuestionnaire(sqls[k], questionnaire);
        }
        String error = (String) GlobalVariables.getUserSession().retrieveObject("qnError");
        if (StringUtils.isNotBlank(error)) {
            qnForm.setRetData("<h3>" + error + "</h3>");
            GlobalVariables.getUserSession().addObject("qnError", (Object) null);
        }

    }

    @Override
    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // TODO Auto-generated method stub
        ActionForward forward = super.start(mapping, form, request, response);
        QuestionnaireMaintenanceForm qnForm = (QuestionnaireMaintenanceForm) form;
        if (qnForm.getMaintenanceAction().equals(KNSConstants.MAINTENANCE_NEW_ACTION)
                && ((Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument()).getNewMaintainableObject().getBusinessObject())
                        .getSequenceNumber() == null) {
            ((Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument()).getNewMaintainableObject().getBusinessObject())
                    .setSequenceNumber(1);
        }
        return forward;
    }

    /**
     * TODO : view method, WIP
     * This method...
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // 'document' is null at this point
        ActionForward forward = super.edit(mapping, form, request, response);
        // Integer questionnaireId = Integer.parseInt(request.getParameter("questionnaireId"));
        QuestionnaireMaintenanceForm qnForm = (QuestionnaireMaintenanceForm) form;
        ((Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument()).getNewMaintainableObject().getBusinessObject())
                .setQuestionnaireRefId(Long.parseLong(request.getParameter("questionnaireRefId")));

        String questions = assembleQuestions(qnForm);
        String usages = assembleUsages(((Questionnaire) ((MaintenanceDocumentBase) qnForm.getDocument()).getNewMaintainableObject()
                .getBusinessObject()));
        qnForm.setEditData(questions + "#;#" + usages);
        // TODO : hack for 1st save to version
        // qnForm.getNewQuestionnaire().setQuestionnaireId(0);
        return forward;
    }


}
