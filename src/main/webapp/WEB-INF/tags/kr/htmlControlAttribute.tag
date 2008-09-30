<%--
 Copyright 2005-2007 The Kuali Foundation.

 Licensed under the Educational Community License, Version 1.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.opensource.org/licenses/ecl1.php

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>
<%@ include file="/kr/WEB-INF/jsp/tldHeader.jsp"%>
<%@ attribute name="property" required="true" %>
<%@ attribute name="attributeEntry" required="true" type="java.util.Map" %>
<%@ attribute name="onblur" required="false" %>
<%@ attribute name="readOnly" required="false" %>
<%@ attribute name="datePicker" required="false" %>
<%@ attribute name="disabled" required="false" %>
<%@ attribute name="onchange" required="false" %>
<%@ attribute name="onclick" required="false" %>
<%@ attribute name="tabindexOverride" required="false" %>
<%@ attribute name="readOnlyBody" required="false"
              description="when readOnly, use the tag body instead of a written hidden field.
              This allows mixing in module-specific inquiries." %>
<%@ attribute name="extraReadOnlyProperty" required="false"
			  description="when readOnly, you can specify extra properties to display alongside of
			  the main property.  The readOnlyBody attribute takes precedence." %>
<%@ attribute name="readOnlyAlternateDisplay" required="false"
              description="when readOnly, you can specify a String value to display instead of
              the main property.  The readOnlyBody and extraReadOnlyProperty attributes take precedence." %>
<%@ attribute name="encryptValue" required="false"
			  description="when readOnly or hidden field, boolean to indicate whether the value should
			  be encrypted and display masked. Defaults to false." %>
<%@ attribute name="displayMask" required="false"
			  description="when a field is not to be displayed in clear text and encrypted as hidden, the
			  string to display." %>
<%@ attribute name="styleClass" required="false"
			  description="When a field has a css class applied to it, make sure that
			  we carry it through."%>

<kul:checkErrors keyMatch="${property}" auditMatch="${property}"/>
<c:choose>
  <%-- border color not supported for select controls, so make background highlighted instead --%>
  <c:when test="${hasErrors==true && !attributeEntry.control.select}">
    <c:set var="textStyle" value="border-color: red"/>
  </c:when>
  <c:when test="${hasErrors==true && attributeEntry.control.select}">
    <c:set var="textStyle" value="background-color:#FFD5D5"/>
  </c:when>
  <c:when test="${readOnly && !hasErrors}">
    <c:set var="textStyle" value="border-color: black"/>
  </c:when>
</c:choose>

<c:set var="disableField" value="false" />
<c:if test="${disabled}">
  <c:set var="disableField" value="true" />
</c:if>

<c:if test="${empty encryptValue}">
    <c:set var="encryptValue" value="false"/>
</c:if>

<c:if test="${empty styleClass}">
	<c:set var="styleClass" value=""/>
</c:if>
<c:choose>
  <c:when test="${!empty tabindexOverride}">
    <c:set var="tabindex" value="${tabindexOverride}"/>
  </c:when>
  <c:otherwise>
    <%-- Can't "not" assign a tab index, so if there is no override we set it to 0 per default --%>
    <c:set var="tabindex" value="0"/>
  </c:otherwise>
</c:choose>

<c:if test="${readOnly}">
   <c:choose>
     <c:when test="${readOnlyBody}">
         <jsp:doBody/>
     </c:when>

     <c:otherwise>
        <c:choose>
         <c:when test="${encryptValue}">
            <%-- mask and encrypt --%>
            <%
              ((org.kuali.core.web.struts.form.KualiForm) request.getAttribute("KualiForm")).setFormatterType((String) property, org.kuali.core.web.format.EncryptionFormatter.class);
            %>
            <html:hidden property="encryptedProperties('${fn:replace(property,'.','_')}')" value="true"/>
            <html:hidden write="false" property="${property}" style="${textStyle}"/>
            ${displayMask}
         </c:when>
         <c:otherwise>
         <logic:empty name="KualiForm" property="${property}">
         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
         </logic:empty>
         <c:if test="${!empty extraReadOnlyProperty}">
           <html:hidden write="false" property="${property}" style="${textStyle}" />
           <html:hidden write="true" property="${extraReadOnlyProperty}" style="${textStyle}" />
         </c:if>
         <c:if test="${empty extraReadOnlyProperty}">
           <html:hidden write="${empty readOnlyAlternateDisplay ? 'true' : 'false'}" property="${property}" style="${textStyle}" />
           ${readOnlyAlternateDisplay}
         </c:if>
     </c:otherwise>
   </c:choose>
     </c:otherwise>
   </c:choose>
</c:if>
<c:if test="${!readOnly}">
  <c:choose>
    <%-- text --%>
    <c:when test="${attributeEntry.control.text == true}">
           <html:text property="${property}" style="${textStyle}" tabindex="${tabindex}"
                           size="${attributeEntry.control.size}" maxlength="${attributeEntry.maxLength}"
                           onblur="${onblur}" onchange="${onchange}" styleId="${property}" disabled="${disableField}"
                           styleClass="${styleClass}"/>

            <c:if test="${datePicker==true}">
                <img src="${ConfigProperties.kr.externalizable.images.url}cal.gif" id="${property}_datepicker" style="cursor: pointer;"
                     title="Date selector" alt="Date selector"
                     onmouseover="this.style.backgroundColor='red';" onmouseout="this.style.backgroundColor='transparent';" />
                <script type="text/javascript">
                  Calendar.setup(
                          {
                            inputField : "${property}", // ID of the input field
                            ifFormat : "%m/%d/%Y", // the date format
                            button : "${property}_datepicker" // ID of the button
                          }
                  );
               </script>
            </c:if>
    </c:when>

    <%-- textarea --%>
    <c:when test="${attributeEntry.control.textarea == true}"> 
            <html:textarea property="${property}" style="${textStyle}" tabindex="${tabindex}" 
                           rows="${attributeEntry.control.rows}" cols="${attributeEntry.control.cols}" 
                           styleId="${property}" disabled="${disableField}" styleClass="${styleClass}" 
                           onkeyup="textLimit(${attributeEntry.maxLength});" /> 
            <script type="text/javascript"> 
              function textLimit(maxlen) { 
              var field=window.document.forms[0].elements['${property}']; 
                if (field.value.length > maxlen) { 
                  field.value = field.value.substr(0, maxlen); 
                } 
              }; 
            </script> 
    </c:when> 

    <%-- select --%>
    <c:when test="${attributeEntry.control.select == true}">
            <c:set var="finderClass" value="${fn:replace(attributeEntry.control.valuesFinder,'.','|')}"/>
            <c:set var="businessObjectClass" value="${fn:replace(attributeEntry.control.businessObject,'.','|')}"/>

            <html:select property="${property}" tabindex="${tabindex}" style="${textStyle}" disabled="${disableField}" onblur="${onblur}" onchange="${onchange}" styleClass="${styleClass}">
              <c:choose>
              	<c:when test="${not empty businessObjectClass}">
                  <c:set var="methodAndParms" value="actionFormUtilMap.getOptionsMap${Constants.ACTION_FORM_UTIL_MAP_METHOD_PARM_DELIMITER}${finderClass}${Constants.ACTION_FORM_UTIL_MAP_METHOD_PARM_DELIMITER}${businessObjectClass}${Constants.ACTION_FORM_UTIL_MAP_METHOD_PARM_DELIMITER}${attributeEntry.control.keyAttribute}${Constants.ACTION_FORM_UTIL_MAP_METHOD_PARM_DELIMITER}${attributeEntry.control.labelAttribute}${Constants.ACTION_FORM_UTIL_MAP_METHOD_PARM_DELIMITER}${attributeEntry.control.includeKeyInLabel}"/>
              	</c:when>
              	<c:otherwise>
                  <c:set var="methodAndParms" value="actionFormUtilMap.getOptionsMap${Constants.ACTION_FORM_UTIL_MAP_METHOD_PARM_DELIMITER}${finderClass}"/>
              	</c:otherwise>
           	  </c:choose>
              <html:optionsCollection property="${methodAndParms}" label="label" value="key"/>
            </html:select>
    </c:when>

    <%-- radio --%>
    <c:when test="${attributeEntry.control.radio == true}">
        <c:set var="finderClass" value="${fn:replace(attributeEntry.control.valuesFinder,'.','|')}"/>
        <c:set var="businessObjectClass" value="${fn:replace(attributeEntry.control.businessObject,'.','|')}"/>

		<c:choose>
      		<c:when test="${not empty businessObjectClass}">
            	<c:set var="methodAndParms" value="actionFormUtilMap.getOptionsMap${Constants.ACTION_FORM_UTIL_MAP_METHOD_PARM_DELIMITER}${finderClass}${Constants.ACTION_FORM_UTIL_MAP_METHOD_PARM_DELIMITER}${businessObjectClass}${Constants.ACTION_FORM_UTIL_MAP_METHOD_PARM_DELIMITER}${attributeEntry.control.keyAttribute}${Constants.ACTION_FORM_UTIL_MAP_METHOD_PARM_DELIMITER}${attributeEntry.control.labelAttribute}${Constants.ACTION_FORM_UTIL_MAP_METHOD_PARM_DELIMITER}${attributeEntry.control.includeKeyInLabel}"/>
      	  	</c:when>
      	  	<c:otherwise>
            	<c:set var="methodAndParms" value="actionFormUtilMap.getOptionsMap${Constants.ACTION_FORM_UTIL_MAP_METHOD_PARM_DELIMITER}${finderClass}"/>
      	  	</c:otherwise>
   	    </c:choose>

       	<logic:iterate name="KualiForm" property="${methodAndParms}" id="KeyValue">
            <html:radio property="${property}" style="${textStyle}" tabindex="${tabindex}"
            	value="key" idName="KeyValue" disabled="${disableField}" onchange="${onchange}"
            	styleClass="${styleClass}"/>${KeyValue.label}
        </logic:iterate>
    </c:when>

    <%-- checkbox --%>
    <c:when test="${attributeEntry.control.checkbox == true}">
            <html:checkbox property="${property}" style="${textStyle}" tabindex="${tabindex}" disabled="${disableField}" onblur="${onblur}"
            	onchange="${onchange}" onclick="${onclick}" styleId="${property}"
            	styleClass="${styleClass}"/>
    </c:when>

    <%-- hidden --%>
    <c:when test="${attributeEntry.control.hidden == true}">
            <html:hidden property="${property}" />
    </c:when>

    <%-- currency --%>
    <c:when test="${attributeEntry.control.currency == true}">
          <html:text property="${property}" style="${textStyle}" tabindex="${tabindex}"
                           size="${attributeEntry.control.size}" maxlength="${attributeEntry.control.formattedMaxLength}"
                           onblur="${onblur}" onchange="${onchange}" styleId="${property}" disabled="${disableField}"
                           styleClass="${styleClass}" />
    </c:when>
  </c:choose>
</c:if>