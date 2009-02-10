 <%--
 Copyright 2006-2008 The Kuali Foundation

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
<%@ include file="/WEB-INF/jsp/irb/ProtocolPerson.jsp"%>

<c:choose>
	<c:when test="${empty KualiForm.document.protocol.protocolPersons[personIndex].personName}">
		<c:set var="parentTabName" value="" />
	</c:when>
	<c:otherwise>
		<bean:define id="parentTabName" name="KualiForm" property="${protocolPerson}.personName"/>
	</c:otherwise>
</c:choose>

<table cellpadding=0 cellspacing=0 summary="">
	<tr>
		<td>
			<kul:innerTab tabTitle="Person Details" parentTab="${parentTabName}" defaultOpen="false" tabErrorKey="document.protocol.protocolPersons*">
				<div class="innerTab-container" align="left">
            				<table class=tab cellpadding=0 cellspacing="0" summary=""> 
              				<tbody id="G1">
                					<tr>
                  				<th align="left" nowrap="nowrap"> 
								<div align="right">
									<kul:htmlAttributeLabel attributeEntry="${protocolPersonAttributes.protocolPersonRoleId}" />
								</div>
							</th>
                  				<td colspan="3">
              						<kul:htmlControlAttribute property="${protocolPerson}.protocolPersonRoleId" attributeEntry="${protocolPersonAttributes.protocolPersonRoleId}" />
            						<html:image property="methodToCall.updateProtocolPersonView.${protocolPerson}" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-updateview.gif" title="Update View" alt="Update View" styleClass="tinybutton"/>
                   				</td>
                					</tr>              
                					<tr>
                  				<th align="left" nowrap="nowrap"> 
								<div align="right">
									<kul:htmlAttributeLabel attributeEntry="${protocolPersonAttributes.affiliationTypeCode}" />
								</div>
							</th>
                  				<td colspan="3">
              						<kul:htmlControlAttribute property="${protocolPerson}.affiliationTypeCode" attributeEntry="${protocolPersonAttributes.affiliationTypeCode}" />
                   				</td>
                					</tr>

    							<c:if test="${KualiForm.protocolHelper.personTrainingSectionRequired}">
	                				<tr>
	                  					<th align="left" nowrap="nowrap"> 
											<div align="right">
												<kul:htmlAttributeLabel attributeEntry="${protocolPersonAttributes.trained}" />
											</div>
										</th>
	                  					<td colspan="3">
	              							<kul:htmlControlAttribute property="${protocolPerson}.trained" attributeEntry="${protocolPersonAttributes.trained}" readOnly="true"/>
	                   					</td>
	                				</tr>              
    							</c:if> 
     							</tbody>
					</table>
				</div>
			</kul:innerTab>
		</td>
	</tr>
</table>
