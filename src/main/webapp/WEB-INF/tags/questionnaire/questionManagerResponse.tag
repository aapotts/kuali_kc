<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<script>
    function showQuestionType() {
        document.getElementById("question_response_type_select").style.display = "none";
        document.getElementById("question_response_type_yes_no").style.display = "none";
        document.getElementById("question_response_type_yes_no_na").style.display = "none";
        document.getElementById("question_response_type_number").style.display = "none";
        document.getElementById("question_response_type_date").style.display = "none";
        document.getElementById("question_response_type_text").style.display = "none";
        document.getElementById("question_response_type_lookup").style.display = "none";
        document.getElementById("question_response_type_unknown").style.display = "none";

        switch(document.getElementById("document.newMaintainableObject.businessObject.questionTypeId").value) {
        case "" :
            document.getElementById("question_response_type_select").style.display = "block";
            break;
        case "1" :
            document.getElementById("question_response_type_yes_no").style.display = "block";
            break;
        case "2" :
            document.getElementById("question_response_type_yes_no_na").style.display = "block";
            break;
        case "3" :
            document.getElementById("question_response_type_number").style.display = "block";
            break;
        case "4" :
            document.getElementById("question_response_type_date").style.display = "block";
            break;
        case "5" :
            document.getElementById("question_response_type_text").style.display = "block";
            break;
        case "6" :
            document.getElementById("question_response_type_lookup").style.display = "block";
            break;
        default :
            document.getElementById("question_response_type_unknown").style.display = "block";
        }
    }
</script>

<div class="tab-container" align="center">
    <h3>
        <span class="subhead-left"> Response </span>
    </h3>
        
    <table id="response-table" width="100%" cellpadding="0" cellspacing="0" class="datatable">
        <tr>
            <th align="center" valign="middle" width="115">Type</th>
            <th align="center" valign="middle">Values</th>
        </tr>
        <tr>
            <td align="left" valign="middle">
                <div align="center">
                    <kul:htmlControlAttribute property="document.newMaintainableObject.businessObject.questionTypeId" 
                                              attributeEntry="${DataDictionary.Question.attributes.questionTypeId}"
                                              readOnlyAlternateDisplay="${KualiForm.document.newMaintainableObject.businessObject.questionType.questionTypeName}"
                                              onchange="javascript:showQuestionType();" />
                    <noscript>
                        <html:image property="methodToCall.loadRecurrence.anchor${tabKey}"
                                    src='${ConfigProperties.kra.externalizable.images.url}tinybutton-refresh.gif' styleClass="tinybutton" />
                    </noscript>
                </div>
            </td>
            <td>

                <%-- Start No Selection --%>
                <kra-questionnaire:questionManagerResponseDivStyle questionType='select' />
                    <p> 
                        <i>Please select the Type of response you would like for this question.</i> <br />
                    </p>
                </div>
                <%-- End No Selection --%>
                
                <%-- Start Yes/No Answer --%>
                <kra-questionnaire:questionManagerResponseDivStyle questionType="Yes/No" />
                    <p> 
                        The user will be presented with the following radio buttons: Yes, No. <br />
                        Only one selection is possible. <br />
                        A selection is required. <br />
                    </p>
                </div>
                <%-- End Yes/No Answer --%>

                <%-- Start Yes/No/NA Answer --%>
                <kra-questionnaire:questionManagerResponseDivStyle questionType="Yes/No/NA" />
                    <p> 
                        The user will be presented with the following pulldown: Yes, No, Not Applicable. <br />
                        Only one selection is possible. <br />
                        A selection is required. <br />
                    </p>
                </div>
                <%-- End Yes/No/NA Answer --%>
                
                <%-- Start Number --%>
                <kra-questionnaire:questionManagerResponseDivStyle questionType="Number" />
                    <p> 
                        The user will be presented with
                        <kul:htmlControlAttribute property="document.newMaintainableObject.businessObject.displayedAnswers" 
                                                  attributeEntry="${DataDictionary.Question.attributes.displayedAnswers}" />
                        text boxes. <br />
                        The entered value will be validated requiring a number only. <br />
                        The maximum length of the number in characters is
                        <kul:htmlControlAttribute property="document.newMaintainableObject.businessObject.answerMaxLength" 
                                                  attributeEntry="${DataDictionary.Question.attributes.answerMaxLength}" />.
                        <br />
                        The number of possible answers is
                        <kul:htmlControlAttribute property="document.newMaintainableObject.businessObject.maxAnswers" 
                                                  attributeEntry="${DataDictionary.Question.attributes.maxAnswers}" />,
                        not exceeding the number of text boxes presented. <br />
                    </p>
                </div>
                <%-- End Number --%>
                
                <%-- Start Date --%>
                <kra-questionnaire:questionManagerResponseDivStyle questionType="Date" />
                    <p> 
                        The user will be presented with
                        <kul:htmlControlAttribute property="document.newMaintainableObject.businessObject.displayedAnswers" 
                                                  attributeEntry="${DataDictionary.Question.attributes.displayedAnswers}" /> 
                        text boxes. <br />
                        The entered value will be validated for a date in MM/DD/YYYY format. <br />
                        A response is required for each text box. <br />
                        The number of possible answers is
                        <kul:htmlControlAttribute property="document.newMaintainableObject.businessObject.maxAnswers" 
                                                  attributeEntry="${DataDictionary.Question.attributes.maxAnswers}" />, 
                        not exceeding the number of text boxes presented. <br />
                    </p>
                </div>
                <%-- End Date --%>
                
                <%-- Start Text --%>
                <kra-questionnaire:questionManagerResponseDivStyle questionType="Text" />
                    <p> 
                        The user will be presented with
                        <kul:htmlControlAttribute property="document.newMaintainableObject.businessObject.displayedAnswers" 
                                                  attributeEntry="${DataDictionary.Question.attributes.displayedAnswers}" /> 
                        text areas. <br />
                        The number of possible answers is
                        <kul:htmlControlAttribute property="document.newMaintainableObject.businessObject.maxAnswers" 
                                                  attributeEntry="${DataDictionary.Question.attributes.maxAnswers}" />,
                        not exceeding the number of text areas presented. <br />
                        The maximum length of each response in characters: 
                        <kul:htmlControlAttribute property="document.newMaintainableObject.businessObject.answerMaxLength" 
                                                  attributeEntry="${DataDictionary.Question.attributes.answerMaxLength}" />.
                        <br />
                    </p>
                </div>
                <%-- End Text --%>

                <%-- Start Lookup --%>
                <kra-questionnaire:questionManagerResponseDivStyle questionType="Lookup" />
                    <p>
                        The user will be presented with the ability to search for 
                        <kul:htmlControlAttribute property="document.newMaintainableObject.businessObject.lookupGui" 
                                                  attributeEntry="${DataDictionary.Question.attributes.lookupGui}"
                                                  readOnlyAlternateDisplay="${KualiForm.document.newMaintainableObject.businessObject.lookupGui}"
                                                  onchange="updateLookupReturn(this, updateLookupReturn_Callback)" />. <br />
                        The field to return is 
                        <kul:htmlControlAttribute property="document.newMaintainableObject.businessObject.lookupName" 
                                                  attributeEntry="${DataDictionary.Question.attributes.lookupName}"
                                                  readOnlyAlternateDisplay="${KualiForm.document.newMaintainableObject.businessObject.lookupName}" />.
                        <c:if test="${!readOnly}">
                            <noscript>
                                <html:image property="methodToCall.refreshPulldownOptions" 
                                            styleClass="tinybutton" 
	                	                    src='${ConfigProperties.kra.externalizable.images.url}arrow_refresh.png' />
	                	    </noscript>
	                	</c:if> <br />
                        The number of possible returns is
                        <kul:htmlControlAttribute property="document.newMaintainableObject.businessObject.maxAnswers" 
                                                  attributeEntry="${DataDictionary.Question.attributes.maxAnswers}" />.
                        <br />
                    </p>
                </div>
                <%-- End Lookup --%>

                <%-- Start Unknown --%>
                <kra-questionnaire:questionManagerResponseDivStyle questionType="Unknown" />
                    <p>
                        <i>The question type is not yet supported.  Contact the system administrator to have this fixed.  Meanwhile please select a different Type of response for for this question.</i> <br />
                    </p>
                </div>
                <%-- End Unknown --%>

            </td>
        </tr>
    </table>
</div>
