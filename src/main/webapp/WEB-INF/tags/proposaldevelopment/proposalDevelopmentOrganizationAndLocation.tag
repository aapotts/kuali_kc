<%--
 Copyright 2007 The Kuali Foundation.
 
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
 <%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<c:set var="proposalDevelopmentAttributes" value="${DataDictionary.ProposalDevelopmentDocument.attributes}" />
<c:set var="organizationAttributes" value="${DataDictionary.Organization.attributes}" />
<c:set var="propLocationAttributes" value="${DataDictionary.PropLocation.attributes}" />
<c:set var="rolodexAttributes" value="${DataDictionary.Rolodex.attributes}" />

<kul:tab tabTitle="Organization/Location" defaultOpen="true" tabErrorKey="document.organizationId*,document.performingOrganizationId*,document.propLocation*,document.newPropLocation*">
	<div class="tab-container" align="center">
    	<div class="h2-container">
    		<span class="subhead-left"><h2>Organization</h2></span>
    		<span class="subhead-right"><kul:help businessObjectClassName="fillMeIn" altText="help"/></span>
        </div>
        
        <table cellpadding=0 cellspacing=0 summary="">
             <tr>
				<th><div align="right"><kul:htmlAttributeLabel attributeEntry="${proposalDevelopmentAttributes.organizationId}" /></div></th>
                <td align="left" valign="middle">
                	<kul:htmlControlAttribute property="document.organizationId" attributeEntry="${proposalDevelopmentAttributes.organizationId}" />
                    <c:out value="${KualiForm.document.organization.organizationName}"/>
                    <kul:lookup boClassName="org.kuali.kra.bo.Organization" 
                    fieldConversions="organizationId:document.organizationId,congressionalDistrict:document.organization.congressionalDistrict,organizationName:document.organization.organizationName
                      ,rolodex.firstName:document.organization.rolodex.firstName
                      ,rolodex.lastName:document.organization.rolodex.lastName
                      ,rolodex.addressLine1:document.organization.rolodex.addressLine1
                      ,rolodex.addressLine2:document.organization.rolodex.addressLine2
                      ,rolodex.addressLine3:document.organization.rolodex.addressLine3
                      ,rolodex.city:document.organization.rolodex.city
                      ,rolodex.state:document.organization.rolodex.state"/> <br/>
                  </td>
             </tr>
             <tr>
				<th><div align="right">Authorized Representative Name & Address:</div></th>
                <td align="left" valign="middle">                  
                    <c:out value="${KualiForm.document.organization.rolodex.firstName}"/>&nbsp                    
                    <c:out value="${KualiForm.document.organization.rolodex.lastName}"/><br>
                    <c:out value="${KualiForm.document.organization.rolodex.addressLine1}"/><br>
                    <c:out value="${KualiForm.document.organization.rolodex.addressLine2}"/><br>
                    <c:out value="${KualiForm.document.organization.rolodex.addressLine3}"/><br>
                    <c:out value="${KualiForm.document.organization.rolodex.city}"/><br>
                    <c:out value="${KualiForm.document.organization.rolodex.state}"/><br>
				</td>
				</tr>
				<tr>
				<th><div align="right"><kul:htmlAttributeLabel attributeEntry="${organizationAttributes.congressionalDistrict}" /></div></th>
                <td>
                	<c:out value="${KualiForm.document.organization.congressionalDistrict}"/>
                </td>
            </tr>
        	<tr>
				<th><div align="right"><kul:htmlAttributeLabel attributeEntry="${proposalDevelopmentAttributes.performingOrganizationId}" /></div></th>
                <td>                	
	               	<c:choose>
	                	<c:when test="${!empty KualiForm.document.performingOrganization.organizationName}"> 
	                    	<kul:htmlControlAttribute property="document.performingOrganizationId" attributeEntry="${proposalDevelopmentAttributes.performingOrganizationId}" />               
	                    	<c:out value="${KualiForm.document.performingOrganization.organizationName}"/>
	                	</c:when>
	                	<c:otherwise>
	                    	<input type="hidden" name="document.performingOrganizationId" value="${KualiForm.document.organizationId}">
	                    	<c:out value="${KualiForm.document.organization.organizationName}"/>
	                	</c:otherwise>   
	                </c:choose>   
                    <kul:lookup boClassName="org.kuali.kra.bo.Organization" 
                    fieldConversions="organizationId:document.performingOrganizationId,organizationName:document.performingOrganization.organizationName" />
                </td>
            </tr>
        </table>
        <div class="h2-container">
    		<span class="subhead-left"><h2>Performance Site Locations</h2></span>
    		<span class="subhead-right"><kul:help businessObjectClassName="fillMeIn" altText="help"/></span>
        </div>
        <table>
             <tr>
              	<th><div align="left">&nbsp</div></th>  
				<th><div align="center"><kul:htmlAttributeLabel attributeEntry="${propLocationAttributes.location}" skipHelpUrl="true" noColon="true" /></div></th>
              	<kul:htmlAttributeHeaderCell literalLabel="Address" scope="col"/> 
              	<kul:htmlAttributeHeaderCell literalLabel="Actions" scope="col"/>
             </tr>
             
            <kra-pd:propLocationDisplay index="-1" locationIter="${KualiForm.document.newPropLocation}" docLocation="document.newPropLocation"/> 
             
        	<c:forEach var="location" items="${KualiForm.document.propLocation}" varStatus="status">
          		<kra-pd:propLocationDisplay index="${status.index}" locationIter="${location}" docLocation="document.propLocation[${status.index}]"/> 
        	</c:forEach>        
        </table>
      
    </div>
</kul:tab>
