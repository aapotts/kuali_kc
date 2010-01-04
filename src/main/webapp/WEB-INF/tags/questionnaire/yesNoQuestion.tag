<%--
 Copyright 2006-2009 The Kuali Foundation
 
 Licensed under the Educational Community License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.osedu.org/licenses/ECL-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>

<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<%@ attribute name="question" required="true" type="org.kuali.kra.questionnaire.question.Question" %>
<%@ attribute name="answer" required="true" type="org.kuali.kra.questionnaire.answer.Answer" %>
<%@ attribute name="questionIndex" required="true" %>
                        
<%--<c:set var="qname" value="HD${answerHeaderIndex}-QN${questionIndex}"/>            
<div class="Qdiv" id="${qname}div">
    <div class="Qquestiondiv">
        <span class="Qmoreinfocontrol">More Information...</span>
        <!--<span class="Qnumber">1.0.0</span>-->
        <span class="Qquestion">${question.question}</span>
        
    </div>
    <kra-questionnaire:questionMoreInfo question="${question}" />--%>
    <%-- nee 'onCLick' because IE is not working well with 'onChange' which will not take effect until cursor is moving to somewhere --%>
    <div class="${responseDivClass}">
        <span class="Qresponse">
            <c:choose>
                <c:when test="${KualiForm.questionnaireHelper.answerHeaders[answerHeaderIndex].answers[questionIndex].answer eq 'Y'}" >
                    <input type="radio" class="QanswerYesNo" onClick = "answerChanged(this)" style="border:none;" id="questionnaireHelper.answerHeaders[${answerHeaderIndex}].answers[${questionIndex}].answer" name="questionnaireHelper.answerHeaders[${answerHeaderIndex}].answers[${questionIndex}].answer" checked="checked" value="Y" />Yes
                </c:when>
                <c:otherwise >
                    <input type="radio" class="QanswerYesNo" onClick = "answerChanged(this)" style="border:none;" id="questionnaireHelper.answerHeaders[${answerHeaderIndex}].answers[${questionIndex}].answer" name="questionnaireHelper.answerHeaders[${answerHeaderIndex}].answers[${questionIndex}].answer" value="Y" />Yes
                </c:otherwise>
            </c:choose>  
            <c:choose>
                <c:when test="${KualiForm.questionnaireHelper.answerHeaders[answerHeaderIndex].answers[questionIndex].answer eq 'N'}" >
                    <input type="radio" class="QanswerYesNo" onClick = "answerChanged(this)" style="border:none;" id="questionnaireHelper.answerHeaders[${answerHeaderIndex}].answers[${questionIndex}].answer" name="questionnaireHelper.answerHeaders[${answerHeaderIndex}].answers[${questionIndex}].answer" checked="checked" value="N" />No
                </c:when>
                <c:otherwise >
                    <input type="radio" class="QanswerYesNo" onClick = "answerChanged(this)" style="border:none;" id="questionnaireHelper.answerHeaders[${answerHeaderIndex}].answers[${questionIndex}].answer" name="questionnaireHelper.answerHeaders[${answerHeaderIndex}].answers[${questionIndex}].answer"  value="N" />No
                </c:otherwise>
            </c:choose>  
        </span>
    </div>
<%--</div>--%>
