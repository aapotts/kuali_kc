<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<c:set var="negotiationAttributes" value="${DataDictionary.Negotiation.attributes}" />
<c:set var="negotiationUnassociatedDetailAttributes" value="${DataDictionary.NegotiationUnassociatedDetail.attributes}" />
<c:set var="action" value="negotiationNegotiation" />
<c:set var="className" value="org.kuali.kra.negotiations.document.NegotiationDocument" />
<c:set var="readOnly" value="${false}" scope="request" />
<script type='text/javascript' src='dwr/interface/KraPersonService.js'></script>

<kul:tab tabTitle="Negotiation" defaultOpen="true" tabErrorKey="document.negotiation.*" 
					auditCluster="requiredFieldsAuditErrors" tabAuditKey="document.title" useRiceAuditMode="true">
					
	<div class="tab-container" align="center">
    	<h3>
    	    <c:choose><c:when test="${empty KualiForm.document.negotiation.negotiationId}">
    		<span class="subhead-left">New Negotiation</span>
    		</c:when><c:otherwise>
    		<span class="subhead-left">Negotiation ${KualiForm.document.negotiation.negotiationId}</span>
    		</c:otherwise></c:choose>
    		<span class="subhead-right"><kul:help businessObjectClassName="org.kuali.kra.negotiations.bo.Negotiation" altText="help"/></span>
        </h3>
		
		<table cellpadding="4" cellspacing="0" summary="">
            <tr>
		        <th><div align="right"><kul:htmlAttributeLabel attributeEntry="${negotiationAttributes.negotiationStatusId}" /></div></th>
                <td>
                	<kul:htmlControlAttribute property="document.negotiation.negotiationStatusId" attributeEntry="${negotiationAttributes.negotiationStatusId}" readOnly="${readOnly}"/>
                </td>
                <th><div align="right">Negotiation Dates:</div></th>
                <td align="left" valign="middle">
                	Start: <kul:htmlControlAttribute property="document.negotiation.negotiationStartDate" attributeEntry="${negotiationAttributes.negotiationStartDate}" readOnly="${readOnly}"/>
                	End: <kul:htmlControlAttribute property="document.negotiation.negotiationEndDate" attributeEntry="${negotiationAttributes.negotiationEndDate}" readOnly="${readOnly}"/>
                </td>
            </tr>
            <tr>
		        <th><div align="right"><kul:htmlAttributeLabel attributeEntry="${negotiationAttributes.negotiatorPersonId}" /></div></th>
                <td>
                    <html:text property="document.negotiation.negotiator.userName" 
						onblur="loadContactPersonName('document.negotiation.negotiator.userName',
									'negotiator.fullName',
									'na',
									'na',
									'na',
									'document.negotiation.negotiatorPersonId');"
                    	readonly="${readOnly}"/>
                    <c:if test="${!readOnly}">
                        ${kfunc:registerEditableProperty(KualiForm, "document.negotiation.negotiatorPersonId")}
	                    <html:hidden property="document.negotiation.negotiatorPersonId" styleId="document.negotiation.negotiatorPersonId"/>
	                	<kul:lookup boClassName="org.kuali.kra.bo.KcPerson" 
	                                fieldConversions="personId:document.negotiation.negotiatorPersonId" />
                    </c:if>
                    <br/><span id="negotiator.fullName"><c:out value="${KualiForm.document.negotiation.negotiator.fullName}"/></span>
                </td>
                <th><div align="right">Negotiation Age:</div></th>
                <td align="left" valign="middle">
                	<kul:htmlControlAttribute property="document.negotiation.negotiationAge" attributeEntry="${negotiationAttributes.negotiationAge}" readOnly="true"/>
                </td>
            </tr>
            <tr>
		        <th><div align="right"><kul:htmlAttributeLabel attributeEntry="${negotiationAttributes.negotiationAgreementTypeId}" /></div></th>
                <td>
                	<kul:htmlControlAttribute property="document.negotiation.negotiationAgreementTypeId" attributeEntry="${negotiationAttributes.negotiationAgreementTypeId}" readOnly="${readOnly}"/>
                </td>
                <th><div align="right"><kul:htmlAttributeLabel attributeEntry="${negotiationAttributes.anticipatedAwardDate}" /></div></th>
                <td align="left" valign="middle">
                	<kul:htmlControlAttribute property="document.negotiation.anticipatedAwardDate" attributeEntry="${negotiationAttributes.anticipatedAwardDate}" readOnly="${readOnly}"/>
                </td>
            </tr>  
            <tr>
		        <th><div align="right"><kul:htmlAttributeLabel attributeEntry="${negotiationAttributes.documentFolder}" /></div></th>
                <td colspan="3">
                	<kul:htmlControlAttribute property="document.negotiation.documentFolder" attributeEntry="${negotiationAttributes.documentFolder}" readOnly="${readOnly}"/>
                </td>
            </tr>  
            
		</table>
		<h3>Negotiation Attributes:</h3>
		<table cellpadding="4" cellspacing="0" summary="">
            <tr>
		        <th><div align="right">
		        	<kul:htmlAttributeLabel attributeEntry="${negotiationAttributes.negotiationAssociationTypeId}" />
		        </div></th>
                <td>
                
                	<kul:htmlControlAttribute property="document.negotiation.negotiationAssociationTypeId" 
                		attributeEntry="${negotiationAttributes.negotiationAssociationTypeId}" readOnly="${readOnly}"
                		onchange="getElementsByName('methodToCall.changeAssociation')[0].click();"/>                		
                		<html:image property="methodToCall.changeAssociation"
					src='${ConfigProperties.kra.externalizable.images.url}tinybutton-add1.gif' styleClass="tinybutton" style="display:none"/>
					
                </td>
                <th>
                	<div align="right">
                		<c:choose>
                			<c:when test="${KualiForm.displayAward}">
					        	Award: 
					      	</c:when>
					      	<c:when test="${KualiForm.displaySubAward}">
					        	Sub Award: 
					      	</c:when>
					      	<c:when test="${KualiForm.displayProposalLog}">
					        	Proposal Log: 
					      	</c:when>
					      	<c:when test="${KualiForm.displayInstitutionalProposal}">
					        	Institutional Proposal: 
					      	</c:when>
					      	<c:when test="${KualiForm.displayUnAssociatedDetail}">
					        	<kul:htmlAttributeLabel attributeEntry="${negotiationAttributes.associatedDocumentId}" /> 
					      	</c:when>
					      	<c:otherwise>
					        	<kul:htmlAttributeLabel attributeEntry="${negotiationAttributes.associatedDocumentId}" />
					      	</c:otherwise>
					    </c:choose>
                	</div>
                </th>
                <td align="left" valign="middle">
                	<kul:htmlControlAttribute property="document.negotiation.associatedDocumentId" attributeEntry="${negotiationAttributes.associatedDocumentId}" readOnly="true"/>
                	<c:if test="${!readOnly}">
                		${kfunc:registerEditableProperty(KualiForm, "document.negotiation.associatedDocumentId")}
	                	<c:choose>
                			<c:when test="${KualiForm.displayAward}">
                				<kul:lookup boClassName="org.kuali.kra.award.home.Award" 
                					fieldConversions="awardId:document.negotiation.associatedDocumentId" />
					      	</c:when>
					      	<c:when test="${KualiForm.displaySubAward}">
					      	</c:when>
					      	<c:when test="${KualiForm.displayProposalLog}">
					        	<kul:lookup boClassName="org.kuali.kra.institutionalproposal.proposallog.ProposalLog" 
					        		fieldConversions="proposalNumber:document.negotiation.associatedDocumentId" />  
					      	</c:when>
					      	<c:when test="${KualiForm.displayInstitutionalProposal}">
					        	<kul:lookup boClassName="org.kuali.kra.institutionalproposal.home.InstitutionalProposal" 
					        		fieldConversions="proposalId:document.negotiation.associatedDocumentId" /> 
					      	</c:when>
						</c:choose>
					</c:if>
                </td>
            </tr>
            <c:if test="${KualiForm.displayUnAssociatedDetail}">
            	<tr>
            		<th>
            			<div align="right">
            				<kul:htmlAttributeLabel attributeEntry="${negotiationUnassociatedDetailAttributes.title}" />
            			</div>
            		</th>
                	<td>
                		<kul:htmlControlAttribute property="document.negotiation.unAssociatedDetail.title" 
                			attributeEntry="${negotiationUnassociatedDetailAttributes.title}" readOnly="${readOnly}"/>
                	</td>
                	<th>
                		<div align="right">
            				<kul:htmlAttributeLabel attributeEntry="${negotiationUnassociatedDetailAttributes.leadUnitNumber}" />
            			</div>
                	</th>
                	<td>
                		<kul:htmlControlAttribute property="document.negotiation.unAssociatedDetail.leadUnitNumber" 
                			attributeEntry="${negotiationUnassociatedDetailAttributes.leadUnitNumber}" readOnly="${readOnly}"/>
                		<c:if test="${!readOnly}">
	                		${kfunc:registerEditableProperty(KualiForm, "document.negotiation.unAssociatedDetail.leadUnitNumber")}
	                		<kul:lookup boClassName="org.kuali.kra.bo.Unit" 
						        		fieldConversions="unitNumber:document.negotiation.unAssociatedDetail.leadUnitNumber" />
					    </c:if> 
                	</td>
            	</tr>
            	
            	<tr>
            		<th>
                		<div align="right">
            				<kul:htmlAttributeLabel attributeEntry="${negotiationUnassociatedDetailAttributes.piPersonId}" />
            			</div>
                	</th>
                	<td>
                		<html:text property="document.negotiation.unAssociatedDetail.PIEmployee.userName" 
							onblur="loadContactPersonName('document.negotiation.unAssociatedDetail.PIEmployee.userName',
										'PIEmployee.fullName',
										'na',
										'na',
										'na',
										'document.negotiation.unAssociatedDetail.piPersonId');"
	                    	readonly="${readOnly}"/>
	                    <c:if test="${!readOnly}">
	                        ${kfunc:registerEditableProperty(KualiForm, "document.negotiation.unAssociatedDetail.piPersonId")}
		                    <html:hidden property="document.negotiation.unAssociatedDetail.piPersonId" styleId="document.negotiation.unAssociatedDetail.piPersonId"/>
		                	<kul:lookup boClassName="org.kuali.kra.bo.KcPerson" 
		                                fieldConversions="personId:document.negotiation.unAssociatedDetail.piPersonId" />
	                    </c:if>
	                    <br/><span id="PIEmployee.fullName"><c:out value="${KualiForm.document.negotiation.unAssociatedDetail.PIEmployee.fullName}"/></span>
	                </td>
	                <th>
	                	<div align="right">
            				<kul:htmlAttributeLabel attributeEntry="${negotiationUnassociatedDetailAttributes.piRolodexId}" />
            			</div>
	                </th>
	                <td>
	                	<kul:htmlControlAttribute property="document.negotiation.unAssociatedDetail.piRolodexId" 
                			attributeEntry="${negotiationUnassociatedDetailAttributes.piRolodexId}" readOnly="${readOnly}"/>
                		<c:if test="${!readOnly}">
	                		${kfunc:registerEditableProperty(KualiForm, "document.negotiation.unAssociatedDetail.piRolodexId")}
	                		<kul:lookup boClassName="org.kuali.kra.bo.Rolodex" 
						        		fieldConversions="rolodexId:document.negotiation.unAssociatedDetail.piRolodexId" />
					    </c:if> 
	                	<%--
	                	<html:text property="document.negotiation.unAssociatedDetail.PINonEmployee.userName" readonly="${readOnly}"/>
	                    <c:if test="${!readOnly}">
	                        ${kfunc:registerEditableProperty(KualiForm, "document.negotiation.unAssociatedDetail.piRolodexId")}
		                    <html:hidden property="document.negotiation.unAssociatedDetail.piRolodexId" styleId="document.negotiation.unAssociatedDetail.piRolodexId"/>
		                	<kul:lookup boClassName="org.kuali.kra.bo.Rolodex" 
		                                fieldConversions="rolodexId:document.negotiation.unAssociatedDetail.piRolodexId" />
	                    </c:if>
	                    <br/><span id="PINonEmployee.fullName"><c:out value="${KualiForm.document.negotiation.unAssociatedDetail.PINonEmployee.fullName}"/></span>
	                     --%>
	                </td>
            	</tr>
            	
            	<tr>
            		<th>
	                	<div align="right">
            				<kul:htmlAttributeLabel attributeEntry="${negotiationUnassociatedDetailAttributes.contactAdminPersonId}" />
            			</div>
	                </th>
	                <td>
	                	<html:text property="document.negotiation.unAssociatedDetail.contactAdmin.userName" 
							onblur="loadContactPersonName('document.negotiation.unAssociatedDetail.contactAdmin.userName',
										'PINonEmployee.fullName',
										'na',
										'na',
										'na',
										'document.negotiation.unAssociatedDetail.contactAdminPersonId');"
	                    	readonly="${readOnly}"/>
	                    <c:if test="${!readOnly}">
	                        ${kfunc:registerEditableProperty(KualiForm, "document.negotiation.unAssociatedDetail.contactAdminPersonId")}
		                    <html:hidden property="document.negotiation.unAssociatedDetail.contactAdminPersonId" styleId="document.negotiation.unAssociatedDetail.contactAdminPersonId"/>
		                	<kul:lookup boClassName="org.kuali.kra.bo.KcPerson" 
		                                fieldConversions="personId:document.negotiation.unAssociatedDetail.contactAdminPersonId" />
	                    </c:if>
	                    <br/><span id="contactAdmin.fullName"><c:out value="${KualiForm.document.negotiation.unAssociatedDetail.contactAdmin.fullName}"/></span>
	                </td>
	                <th></th>
	                <td></td>
            	</tr>
            	            	
            	<tr>
            		<th>
            			<div align="right">
            				<kul:htmlAttributeLabel attributeEntry="${negotiationUnassociatedDetailAttributes.sponsorCode}" />
            			</div>
            		</th>
                	<td>
                		<kul:htmlControlAttribute property="document.negotiation.unAssociatedDetail.sponsorCode" 
                			attributeEntry="${negotiationUnassociatedDetailAttributes.sponsorCode}" readOnly="${readOnly}"/>
                		<c:if test="${!readOnly}">
	                		${kfunc:registerEditableProperty(KualiForm, "document.negotiation.unAssociatedDetail.sponsorCode")}
	                		<kul:lookup boClassName="org.kuali.kra.bo.Sponsor" 
						        		fieldConversions="sponsorCode:document.negotiation.unAssociatedDetail.sponsorCode" />
					    </c:if>
                	</td>
                	<th>
                		<div align="right">
            				<kul:htmlAttributeLabel attributeEntry="${negotiationUnassociatedDetailAttributes.primeSponsorCode}" />
            			</div>
                	</th>
                	<td>
                		<kul:htmlControlAttribute property="document.negotiation.unAssociatedDetail.primeSponsorCode" 
                			attributeEntry="${negotiationUnassociatedDetailAttributes.primeSponsorCode}" readOnly="${readOnly}"/>
                		<c:if test="${!readOnly}">
	                		${kfunc:registerEditableProperty(KualiForm, "document.negotiation.unAssociatedDetail.primeSponsorCode")}
	                		<kul:lookup boClassName="org.kuali.kra.bo.Sponsor" 
						        		fieldConversions="sponsorCode:document.negotiation.unAssociatedDetail.primeSponsorCode" />
					    </c:if> 
                	</td>
            	</tr>
            	
            	<tr>
            		<th>
            			<div align="right">
            				<kul:htmlAttributeLabel attributeEntry="${negotiationUnassociatedDetailAttributes.sponsorAwardNumber}" />
            			</div>
            		</th>
                	<td>
                		<kul:htmlControlAttribute property="document.negotiation.unAssociatedDetail.sponsorAwardNumber" 
                			attributeEntry="${negotiationUnassociatedDetailAttributes.sponsorAwardNumber}" readOnly="${readOnly}"/>
                	</td>
                	<th>
                		<div align="right">
            				<kul:htmlAttributeLabel attributeEntry="${negotiationUnassociatedDetailAttributes.subAwardOrganizationId}" />
            			</div>
                	</th>
                	<td>
                		<kul:htmlControlAttribute property="document.negotiation.unAssociatedDetail.subAwardOrganizationId" 
                			attributeEntry="${negotiationUnassociatedDetailAttributes.subAwardOrganizationId}" readOnly="${readOnly}"/>
                		<c:if test="${!readOnly}">
	                		${kfunc:registerEditableProperty(KualiForm, "document.negotiation.unAssociatedDetail.subAwardOrganizationId")}
	                		<kul:lookup boClassName="org.kuali.kra.bo.Organization" 
						        		fieldConversions="organizationId:document.negotiation.unAssociatedDetail.subAwardOrganizationId" />
					    </c:if> 
                	</td>
            	</tr>
            	
            </c:if>
		</table>
	</div>	
</kul:tab>