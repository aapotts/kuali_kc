<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<c:set var="protocolDocumentAttributes" value="${DataDictionary.ProtocolDocument.attributes}" />
<c:set var="protocolAttributes" value="${DataDictionary.Protocol.attributes}" />
<c:set var="textAreaFieldName" value="document.protocol.title" />
<c:set var="action" value="protocol" />
<c:set var="nonEmpFlag" value="false" />
<c:set var="className" value="org.kuali.kra.protocol.document.ProtocolDocument" />
<c:set var="readOnly" value="${!KualiForm.protocolHelper.modifyProtocol}" />

<kul:tab tabTitle="Required Fields for Saving Document" defaultOpen="false" tabErrorKey="document.protocol.protocolTypeCode*,principalInvestigator*,document.protocol.principalInvestigatorName*,document.protocol.title*,document.protocol.principalInvestigatorId*,document.protocol.leadUnitNumber*, document.currentAwardNumber*,document.continuedFrom,document.sponsorCode*,document.ProtocolTypeCode*,document.requestedStartDateInitial*,document.ownedByUnit*,document.requestedEndDateInitial*,document.activityTypeCode*,document.title" auditCluster="requiredFieldsAuditErrors" tabAuditKey="document.title" useRiceAuditMode="true">
	<div class="tab-container" align="center">
    	<h3>
    		<span class="subhead-left">Required Fields for Saving Document</span>
    		<span class="subhead-right"><kul:help businessObjectClassName="org.kuali.kra.protocol.bo.ProtocolType" altText="help"/></span>
        </h3>
		
		<table cellpadding=4 cellspacing=0 summary="">
            <tr>
            	<th><div align="center"><kul:htmlAttributeLabel attributeEntry="${protocolAttributes.protocolTypeCode}" /></div></th>
                <td align="left" valign="top">
                    <kra:kraControlAttribute property="document.protocol.protocolTypeCode" readOnly="${readOnly}" attributeEntry="${protocolAttributes.protocolTypeCode}" />
                </td>
				
				<th><div align="center"><kul:htmlAttributeLabel attributeEntry="${protocolAttributes.principalInvestigatorId}" /></div></th>
                <td align="left" valign="top">
                <div id="principalInvestigator.div" property="principalInvestigator" >
                        <c:if test="${empty KualiForm.protocolHelper.principalInvestigatorName}">                                                 
                            <input type="hidden" name="protocolHelper.principalInvestigatorName" value="">              
                        </c:if>
                        <c:if test="${empty KualiForm.protocolHelper.personId}">          					                	
                	    	<input type="hidden" name="protocolHelper.personId" value="">              
                	    </c:if>       
                	    <c:if test="${empty KualiForm.protocolHelper.rolodexId}">          					                	
                            <input type="hidden" name="protocolHelper.rolodexId" value="">              
                	    </c:if>   	
						<c:if test="${empty KualiForm.document.protocol.protocolId}">          					
							<label> Employee Search</label>
							<label>
							<kul:lookup boClassName="org.kuali.kra.bo.Person" 
	                         fieldConversions="personId:protocolHelper.personId,fullName:protocolHelper.principalInvestigatorName,homeUnitRef.unitNumber:protocolHelper.lookupUnitNumber,homeUnitRef.unitName:protocolHelper.lookupUnitName" 
	                         /></label>
	                        <br>
							<label>Non-employee Search</label> 
							<label>
							<kul:lookup boClassName="org.kuali.kra.bo.NonOrganizationalRolodex" 
	                         fieldConversions="rolodexId:protocolHelper.rolodexId,unit.unitNumber:protocolHelper.lookupUnitNumber,unit.unitName:protocolHelper.lookupUnitName,fullName:protocolHelper.principalInvestigatorName"  
	                         />   
							</label>
						</c:if>
					<br>
									
					<div id="principalInvestigatorName.div" >
                        <c:if test="${!empty KualiForm.protocolHelper.principalInvestigatorId}">
            				<c:choose>
							    <c:when test="${empty KualiForm.protocolHelper.principalInvestigatorName}">
	                    			<span style='color: red;'>not found</span><br>
	               				</c:when>
	                  			<c:otherwise>
										<c:out value="${KualiForm.protocolHelper.principalInvestigatorName}" /><br>
								</c:otherwise>  
							</c:choose>                        
                        </c:if>
					</div>
                </td>
				</div>


            </tr>
            <tr>
                <th><div align="center"><kul:htmlAttributeLabel attributeEntry="${protocolAttributes.title}" /></div></th>
                <td align="left" valign="top">
                	<kul:htmlControlAttribute property="document.protocol.title" attributeEntry="${protocolAttributes.title}" readOnly="${readOnly}" />
                    <c:if test="${!readOnly}">
                        <kra:expandedTextArea textAreaFieldName="${textAreaFieldName}" action="${action}" textAreaLabel="${protocolAttributes.title.label}" />
                    </c:if>
                </td>
                
                <th><div align="center"><kul:htmlAttributeLabel attributeEntry="${protocolAttributes.leadUnitNumber}" /></div></th>            
                <td align="left" valign="top">
                    <c:if test="${empty KualiForm.document.protocol.protocolId}">
                    	<kul:htmlControlAttribute property="protocolHelper.leadUnitNumber" 
						 attributeEntry="${protocolAttributes.leadUnitNumber}"  
						 onblur="loadUnitNameTo('protocolHelper.leadUnitNumber','protocolHelper.leadUnitName');"/> 
						 						                  
	                    <kul:lookup boClassName="org.kuali.kra.bo.Unit" 
	                     fieldConversions="unitNumber:protocolHelper.leadUnitNumber,unitName:protocolHelper.leadUnitName" />
                    
	                    <kul:directInquiry boClassName="org.kuali.kra.bo.Unit" 
	                     inquiryParameters="protocolHelper.leadUnitNumber:unitNumber" 
	                     anchor="${tabKey}" />
                    </label>
                    <br>
                    </c:if>
                    
                  					
				
                    <div id="protocolHelper.leadUnitName.div" align="left">         
                        <c:out value="${KualiForm.protocolHelper.leadUnitName}" /> 
                        <c:if test="${!empty KualiForm.protocolHelper.leadUnitNumber}">
            				<c:choose>
							<c:when test="${empty KualiForm.protocolHelper.leadUnitName}">
	                    		<span style='color: red;'>not found</span><br>
	               			</c:when>
	                  		<c:otherwise>
                                <kul:htmlControlAttribute property="protocolHelper.leadUnitName" 
                                    attributeEntry="${protocolAttributes.leadUnitName}"   />	
                         	</c:otherwise>  
							</c:choose>                        
                        </c:if>
					</div>
					<c:if test="${!empty KualiForm.document.protocol.protocolId && !empty KualiForm.protocolHelper.leadUnitNumber}">
                       - ${KualiForm.document.protocol.leadUnit.unitNumber}
                    </c:if>
				</td>
                
                
				
                
				
				
            </tr>
		</table>
	</div>	
</kul:tab>		