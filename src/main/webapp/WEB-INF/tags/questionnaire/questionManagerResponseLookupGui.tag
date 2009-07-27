<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<c:set var="currentQuestionType" value="${KualiForm.document.newMaintainableObject.businessObject.questionType.questionTypeName}" />

<c:choose>
    <c:when test="${currentQuestionType == 'Lookup'}">
        <c:set var="preMessage" value="The user will be presented with the ability to search for " />
        <c:set var="postMessage" value="." />
        <c:set var="htmlControlDivStyle" value="display: inline" />
    </c:when>
    <c:otherwise>
        <c:set var="preMessage" value="" />
        <c:set var="postMessage" value="" />
        <c:set var="htmlControlDivStyle" value="display: none" />
    </c:otherwise>
</c:choose>

<div id="lookup_gui_pre_message" style="display: inline">
    ${preMessage}
</div>
<div id="lookup_gui_html_control" style="${htmlControlDivStyle}">
    <kul:htmlControlAttribute property="document.newMaintainableObject.businessObject.lookupGui" 
                              attributeEntry="${DataDictionary.Question.attributes.lookupGui}"
                              onchange="updateLookupReturn(this, updateLookupReturn_Callback)" />
</div>
<div id="lookup_gui_post_message" style="display: inline">
    ${postMessage}
</div>
<div id="lookup_gui_br" style="${htmlControlDivStyle}">
    <br />
</div>