<%--
 Copyright 2005-2010 The Kuali Foundation
 
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

<%@ attribute name="businessObjectClassName" required="true" 
              description="The specific per-module business class to use for the help pages" %>
<%@ attribute name="protocolExceptionAttributes" required="true" type="java.util.Map"
              description="The Data Dictionary reference to the Protocol Exception attributes" %>
<%@ attribute name="collectionReference" required="true" type="java.util.List" 
              description="The object reference to the collection that holds all the current Protocol Exception" %>
<%@ attribute name="collectionProperty" required="true" 
              description="The property name of the collection that holds all the current Protocol Exception" %>
<%@ attribute name="action" required="true" 
              description="The name of the action class" %>

<c:set var="readOnly" value="${!KualiForm.iacucProtocolExceptionHelper.modifyProtocolException}" />
<c:set var="commentDisplayLength" value="<%=org.kuali.kra.infrastructure.Constants.IACUC_PROTOCOL_EXCEPTION_DESC_LENGTH%>" />


<kul:tab tabTitle="Protocol Exceptions" defaultOpen="true" alwaysOpen="true" transparentBackground="true" tabErrorKey="newIacucProtocolException*,iacucProtocolExceptionHelper.newIacucProtocolException*,${collectionProperty}*">
    <div class="tab-container" align="center">
    	<h3>
    		<span class="subhead-left">Protocol Exceptions</span>
    		<span class="subhead-right"><kul:help businessObjectClassName="${businessObjectClassName}" altText="help"/></span>
        </h3>
        
        <table id="protocolExceptionTableId" cellpadding="0" cellspacing="0" summary="">
          	<tr>
          		<th><div align="left">&nbsp;</div></th> 
          		<th><div align="center"><kul:htmlAttributeLabel attributeEntry="${protocolExceptionAttributes.exceptionCategoryCode}" noColon="true" /></div></th>
          		<th><div align="center"><kul:htmlAttributeLabel attributeEntry="${protocolExceptionAttributes.iacucProtocolSpeciesId}" noColon="true" /></div></th>
          		<th><div align="center"><kul:htmlAttributeLabel attributeEntry="${protocolExceptionAttributes.exceptionDescription}" noColon="true" /></nobr></div></th>
				<c:if test="${!readOnly}">
					<kul:htmlAttributeHeaderCell literalLabel="Actions" scope="col" />
				</c:if>
          	</tr>     

        	<kra:permission value="${KualiForm.iacucProtocolExceptionHelper.modifyProtocolException}">            
                <tr>
	                <c:set var="textAreaFieldName" value="iacucProtocolExceptionHelper.newIacucProtocolException.exceptionDescription" />
					<th class="infoline" rowspan="2">
						Add:
					</th>
		            <td align="left" valign="middle" class="infoline">
		               	<div align="center">
		               		<kul:htmlControlAttribute property="iacucProtocolExceptionHelper.newIacucProtocolException.exceptionCategoryCode" 
		               		                          attributeEntry="${protocolExceptionAttributes.exceptionCategoryCode}" 
		                                              styleClass="fixed-size-500-select"
		               		                          readOnly="${readOnly}" />
		            	</div>
					</td>
		            <td align="left" valign="middle" class="infoline">
		               	<div align="center">
		               		<kul:htmlControlAttribute property="iacucProtocolExceptionHelper.newIacucProtocolException.iacucProtocolSpeciesId" 
		               		                          attributeEntry="${protocolExceptionAttributes.iacucProtocolSpeciesId}" 
		                                              styleClass="fixed-size-500-select"
		               		                          readOnly="${readOnly}" />
		            	</div>
					</td>
		            <td align="left" valign="middle" class="infoline">
		               	<div align="center">
		               		<kul:htmlControlAttribute property="iacucProtocolExceptionHelper.newIacucProtocolException.exceptionDescription" 
		               		                          attributeEntry="${protocolExceptionAttributes.exceptionDescription}" 
		               		                          readOnly="${readOnly}" />
		            	</div>
					</td>
					<td class="infoline" rowspan="1">
						<div align="center">
							<html:image property="methodToCall.addProtocolException.anchor${tabKey}" 
						            src='${ConfigProperties.kra.externalizable.images.url}tinybutton-add1.gif' 
						            styleClass="tinybutton"/>
	                	</div>
	                </td>
	            </tr>
	        </kra:permission>          
            
        	<c:forEach var="protocolException" items="${collectionReference}" varStatus="status">
                <tr>
	                <c:set var="textAreaFieldName" value="${collectionProperty}[${status.index}].exceptionDescription" />
					<th class="infoline" rowspan="2">
					   <c:out value="${status.index+1}" />
					</th>
		            <td align="left" valign="middle" class="infoline">
		               	<div align="center">
	                        <kul:htmlControlAttribute property="${collectionProperty}[${status.index}].exceptionCategoryCode" 
		                                              attributeEntry="${protocolExceptionAttributes.exceptionCategoryCode}"  
		                                              readOnly="${readOnly}"
		                                              styleClass="fixed-size-500-select"
		                                              readOnlyAlternateDisplay="${iacucProtocolException.iacucExceptionCategory.exceptionCategoryDesc}" 
		                                              />
	            	</div>
		            </td>
		            <td align="left" valign="middle" class="infoline">
		               	<div align="center">
	                        <kul:htmlControlAttribute property="${collectionProperty}[${status.index}].iacucProtocolSpeciesId" 
		                                              attributeEntry="${protocolExceptionAttributes.iacucProtocolSpeciesId}"  
		                                              readOnly="${readOnly}"
		                                              styleClass="fixed-size-500-select"
		                                              readOnlyAlternateDisplay="${iacucProtocolException.iacucProtocolSpecies.iacucSpecies.speciesName}" 
		                                              />
		            	</div>
					</td>
		            <td align="left" valign="middle" class="infoline">
		               	<div align="center">
	                        <c:choose>
	                            <c:when test="${!readOnly}">
	                                <kul:htmlControlAttribute property="${collectionProperty}[${status.index}].exceptionCategoryDesc" 
	                                                          attributeEntry="${protocolExceptionAttributes.exceptionCategoryDesc}"/>
	                            </c:when>
	                            <c:otherwise>
			            		    <kra:truncateComment textAreaFieldName="${collectionProperty}[${status.index}].exceptionCategoryDesc" action="${action}" 
		                                                 textAreaLabel="${protocolExceptionAttributes.exceptionCategoryDesc.label}" textValue="${protocolException.exceptionCategoryDesc}"  
		                                                 displaySize="${commentDisplayLength}"/>
	                            </c:otherwise>
	                        </c:choose>
		            	</div>
					</td>
	            </tr>
        	</c:forEach>
        </table>
    </div> 
</kul:tab>