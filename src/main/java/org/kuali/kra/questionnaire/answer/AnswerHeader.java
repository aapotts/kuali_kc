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
package org.kuali.kra.questionnaire.answer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.kuali.kra.bo.KraPersistableBusinessObjectBase;
import org.kuali.kra.questionnaire.Questionnaire;

/**
 * Holds additional information related to a series of {@link Answer Answers}.
 */
public class AnswerHeader extends KraPersistableBusinessObjectBase {

    private static final long serialVersionUID = 1L;

    private Long answerHeaderId;
    private String moduleItemCode;
    private String moduleItemKey;
    private Integer moduleSubItemCode;
    private String moduleSubItemKey;
    private Long questionnaireRefIdFk;
    private Questionnaire questionnaire;
    private boolean completed = false;

    private List<Answer> answers;
    
    // Transient properties for questionnaire answer 
    private boolean newerVersionPublished = false;
    private String updateOption ;
    private String showQuestions ;

    public AnswerHeader() {
        super();
        showQuestions = "N";
    }

    public AnswerHeader(ModuleQuestionnaireBean moduleQuestionnaireBean, Long questionnaireRefIdFk) {
        this.moduleItemCode = moduleQuestionnaireBean.getModuleItemCode();
        this.moduleItemKey = moduleQuestionnaireBean.getModuleItemKey();
        this.moduleSubItemKey = moduleQuestionnaireBean.getModuleSubItemKey();
        this.questionnaireRefIdFk = questionnaireRefIdFk;
        // current coeus is setting this to 0
        this.moduleSubItemCode = 0;
        answers = new ArrayList<Answer>();
        showQuestions = "N";

    }

    /**
     * Gets the moduleItemCode attribute.
     * 
     * @return Returns the moduleItemCode.
     */
    public String getModuleItemCode() {
        return this.moduleItemCode;
    }

    /**
     * Sets the moduleItemCode attribute value.
     * 
     * @param moduleItemCode The moduleItemCode to set.
     */
    public void setModuleItemCode(String moduleItemCode) {
        this.moduleItemCode = moduleItemCode;
    }

    /**
     * Gets the moduleItemKey attribute.
     * 
     * @return Returns the moduleItemKey.
     */
    public String getModuleItemKey() {
        return this.moduleItemKey;
    }

    /**
     * Sets the moduleItemKey attribute value.
     * 
     * @param moduleItemKey The moduleItemKey to set.
     */
    public void setModuleItemKey(String moduleItemKey) {
        this.moduleItemKey = moduleItemKey;
    }

    /**
     * Gets the moduleSubItemCode attribute.
     * 
     * @return Returns the moduleSubItemCode.
     */
    public Integer getModuleSubItemCode() {
        return this.moduleSubItemCode;
    }

    /**
     * Sets the moduleSubItemCode attribute value.
     * 
     * @param moduleSubItemCode The moduleSubItemCode to set.
     */
    public void setModuleSubItemCode(Integer moduleSubItemCode) {
        this.moduleSubItemCode = moduleSubItemCode;
    }

    /**
     * Gets the moduleSubItemKey attribute.
     * 
     * @return Returns the moduleSubItemKey.
     */
    public String getModuleSubItemKey() {
        return this.moduleSubItemKey;
    }

    /**
     * Sets the moduleSubItemKey attribute value.
     * 
     * @param moduleSubItemKey The moduleSubItemKey to set.
     */
    public void setModuleSubItemKey(String moduleSubItemKey) {
        this.moduleSubItemKey = moduleSubItemKey;
    }

    /**
     * Gets the questionnaireId attribute.
     * 
     * @return Returns the questionnaireRefId.
     */
    public Long getQuestionnaireRefIdFk() {
        return this.questionnaireRefIdFk;
    }

    /**
     * Sets the questionnaireId attribute value.
     * 
     * @param questionnaireId The questionnaireId to set.
     */
    public void setQuestionnaireRefIdFk(Long questionnaireRefIdFk) {
        this.questionnaireRefIdFk = questionnaireRefIdFk;
    }

    /**
     * Gets the questionnaire attribute.
     * 
     * @return Returns the questionnaire.
     */
    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    /**
     * Sets the questionnaire attribute value.
     * 
     * @param questionnaire The questionnaire to set.
     */
    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    /**
     * Gets the completed attribute.
     * 
     * @return Returns the completed.
     */
    public boolean getCompleted() {
        return this.completed;
    }

    /**
     * Sets the completed attribute value.
     * 
     * @param completed The completed to set.
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Gets the answers attribute.
     * 
     * @return Returns the answers.
     */
    public List<Answer> getAnswers() {
        return this.answers;
    }

    /**
     * Sets the answers attribute value.
     * 
     * @param answers The answers to set.
     */
    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    /** {@inheritDoc} */
    @Override
    protected LinkedHashMap<String, Object> toStringMapper() {
        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<String, Object>();
        hashMap.put("moduleItemCode", this.getModuleItemCode());
        hashMap.put("moduleItemKey", this.getModuleItemKey());
        hashMap.put("moduleSubItemCode", this.getModuleSubItemCode());
        hashMap.put("moduleSubItemKey", this.getModuleSubItemKey());
        hashMap.put("questionnaireRefIdFk", this.getQuestionnaireRefIdFk());
        hashMap.put("answerHeaderId", this.getAnswerHeaderId());
        hashMap.put("completed", Boolean.valueOf(this.getCompleted()));
        return hashMap;
    }

    public Long getAnswerHeaderId() {
        return answerHeaderId;
    }

    public void setAnswerHeaderId(Long answerHeaderId) {
        this.answerHeaderId = answerHeaderId;
    }
    
    public boolean isNewerVersionPublished() {
        return newerVersionPublished;
    }

    public void setNewerVersionPublished(boolean newerVersionPublished) {
        this.newerVersionPublished = newerVersionPublished;
    }

    public String getUpdateOption() {
        return updateOption;
    }

    public void setUpdateOption(String updateOption) {
        this.updateOption = updateOption;
    }

    public String getShowQuestions() {
        return showQuestions;
    }

    public void setShowQuestions(String showQuestions) {
        this.showQuestions = showQuestions;
    }


}
