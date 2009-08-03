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

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.validator.AssertFalse;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.kuali.rice.kns.service.BusinessObjectService;

public class QuestionnaireServiceTest {
    
        private Mockery context = new JUnit4Mockery();


        @Test
        public void testIsQuestionnaireNameExistTrue() {

            final BusinessObjectService businessObjectService = context.mock(BusinessObjectService.class);
            final QuestionnaireServiceImpl questionnaireService = new QuestionnaireServiceImpl();
            questionnaireService.setBusinessObjectService(businessObjectService);
            final Questionnaire questionnaire = new Questionnaire();
            questionnaire.setQuestionnaireId(1);
            questionnaire.setName("exist name");
            final Map<String, Object> fieldValues = new HashMap<String, Object>();
            fieldValues.put("name", "exist name");
            context.checking(new Expectations() {{
                one(businessObjectService).findByPrimaryKey(Questionnaire.class, fieldValues);
                will(returnValue(questionnaire));
            }});

            assertTrue(questionnaireService.isQuestionnaireNameExist(null, "exist name"));

            context.assertIsSatisfied();
                        
        }

        @Test
        public void testIsQuestionnaireNameExistFalse() {

            final BusinessObjectService businessObjectService = context.mock(BusinessObjectService.class);
            final QuestionnaireServiceImpl questionnaireService = new QuestionnaireServiceImpl();
            questionnaireService.setBusinessObjectService(businessObjectService);
            final Questionnaire questionnaire = new Questionnaire();
            questionnaire.setQuestionnaireId(1);
            questionnaire.setName("exist name");
            final Map<String, Object> fieldValues = new HashMap<String, Object>();
            fieldValues.put("name", "exist name");
            context.checking(new Expectations() {{
                one(businessObjectService).findByPrimaryKey(Questionnaire.class, fieldValues);
                will(returnValue(questionnaire));
            }});

            assertTrue(!questionnaireService.isQuestionnaireNameExist(1, "exist name"));

            context.assertIsSatisfied();
                        
        }
        
        @Test
        public void testIsQuestionnaireNameExistFalseNoMatch() {

            final BusinessObjectService businessObjectService = context.mock(BusinessObjectService.class);
            final QuestionnaireServiceImpl questionnaireService = new QuestionnaireServiceImpl();
            questionnaireService.setBusinessObjectService(businessObjectService);
            final Map<String, Object> fieldValues = new HashMap<String, Object>();
            fieldValues.put("name", "not exist name");
            context.checking(new Expectations() {{
                one(businessObjectService).findByPrimaryKey(Questionnaire.class, fieldValues);
                will(returnValue(null));
            }});

            assertTrue(!questionnaireService.isQuestionnaireNameExist(1, "not exist name"));

            context.assertIsSatisfied();
                        
        }
        
    }


