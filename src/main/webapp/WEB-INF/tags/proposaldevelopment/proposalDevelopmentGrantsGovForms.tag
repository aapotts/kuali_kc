<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<c:set var="proposalDevelopmentAttributes" value="${DataDictionary.ProposalDevelopmentDocument.attributes}" />
<c:set var="s2sFormAttributes" value="${DataDictionary.S2sOppForms.attributes}" />
<c:set var="textAreaFieldName" value="document.programAnnouncementTitle" />
<c:set var="action" value="proposalDevelopmentProposal" />

          	<tr>
				<td>
<kul:innerTab parentTab="Opportunity Search" defaultOpen="false" tabTitle="Forms">
<div class="innerTab-container" align="left">
 <table class=tab cellpadding=0 cellspacing="0" summary=""> 
 <tbody id="G1">
    	<tr>
	    	<th><div align="center"><kul:htmlAttributeLabel attributeEntry="${s2sFormAttributes.formName}" noColon="true" /></div></th>
			<th><div align="center"><kul:htmlAttributeLabel attributeEntry="${s2sFormAttributes.mandatory}" noColon="true" /></div></th>
			<th><div align="center"><kul:htmlAttributeLabel attributeEntry="${s2sFormAttributes.include}" noColon="true" /></div></th>
			<th><div align="center"><kul:htmlAttributeLabel attributeEntry="${s2sFormAttributes.available}" noColon="true" /></div></th>			
			<th width="150"><div align="center">
			Select to Print:
			<br/>
			<a href="#">All Included</a>
			|
			<a href="#">None</a>
			</div></th>
    	</tr>
    	
    	<c:forEach var="form" items="${KualiForm.document.s2sOpportunity.s2sOppForms}" varStatus="status">
	             <tr>	                
	                <td align="left" valign="middle">
	                	<kul:htmlControlAttribute property="document.s2sOpportunity.s2sOppForms[${status.index}].formName" attributeEntry="${s2sFormAttributes.formName}" readOnly="true" />
					</td>	                
	                <td>
	                	<div align="center">
	                	<kul:htmlControlAttribute property="document.s2sOpportunity.s2sOppForms[${status.index}].mandatory" attributeEntry="${s2sFormAttributes.mandatory}" readOnly="true"/>
	                	</div>
	                </td>
	                <td>
	                <div align="center">
	                <c:set var="isMandatory" value="${KualiForm.document.s2sOpportunity.s2sOppForms[status.index].mandatory}" /> 
	                <c:choose>
		               	<c:when test="${isMandatory}"> 		                	
		               		<kul:htmlControlAttribute property="document.s2sOpportunity.s2sOppForms[${status.index}].include" attributeEntry="${s2sFormAttributes.include}" readOnly="true"/>
		               	</c:when>
		               	<c:otherwise>
							<kul:htmlControlAttribute property="document.s2sOpportunity.s2sOppForms[${status.index}].include" attributeEntry="${s2sFormAttributes.include}"/>                		
 		               	</c:otherwise>
	                </c:choose>
	                </td>
	                </div> 
	                <td align="left" valign="middle">
	                	<c:set var="isAvailable" value="${KualiForm.document.s2sOpportunity.s2sOppForms[status.index].available}" />	                	
	                	<c:choose>
		                	<c:when test="${isAvailable}">		                			                	
		                		<kul:htmlControlAttribute property="document.s2sOpportunity.s2sOppForms[${status.index}].available" attributeEntry="${s2sFormAttributes.available}"/>
		                		<c:out value="Available"/>
		                	</c:when>
		                	<c:otherwise>
								<c:out value="Not Available"/>
								<kul:htmlControlAttribute property="document.s2sOpportunity.s2sOppForms[${status.index}].available" attributeEntry="${s2sFormAttributes.available}"/>		                			                		
 		                	</c:otherwise>
	                	</c:choose>
	                </td>
	                <td align="center" valign="middle">
	                	<div align="center">
	                	<html:checkbox property="document.s2sOpportunity.s2sOppForms[${status.index}].selectToPrint"/>
	                	<!--   <kul:htmlControlAttribute property="document.s2sOpportunity.s2sOppForms[${status.index}].selectToPrint" attributeEntry="${s2sFormAttributes.selectToPrint}" />-->
	                	</div>
	                </td>
	                
	            </tr>    	
    	</c:forEach>        
	   </tbody>
</table></div>    
</kul:innerTab>
</td></tr></table></div>


