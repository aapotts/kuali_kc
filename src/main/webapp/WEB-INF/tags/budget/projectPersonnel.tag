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

<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<c:set var="budgetPersonAttributes" value="${DataDictionary.BudgetPerson.attributes}" />

<div id="workarea">
<kul:tab tabTitle="Project Personnel (All Periods)" defaultOpen="true" transparentBackground="true" tabErrorKey="document.budgetPerson*" auditCluster="budgetPersonnelAuditWarnings" tabAuditKey="document.budgetPerson*" useRiceAuditMode="true">
	<div class="tab-container" align="center">
		<c:set var="firstErrorFound" value="false" />
		<c:forEach var="budgetPersons" items="${KualiForm.document.budgetPersons}" varStatus="status">			
			<c:if test="${KualiForm.document.budgetPersons[status.index].jobCode == '' or empty KualiForm.document.budgetPersons[status.index].jobCode}">
				<c:if test="${not empty firstErrorFound && firstErrorFound == false}" >
					<c:set var="firstErrorFound" value="true" />
				</c:if>		
				<div class="error">
					<c:if test="${not empty firstErrorFound && firstErrorFound == true}" >
						<strong>&nbsp;&nbsp;&nbsp;Errors found in this Section:</strong><br/>
						<c:set var="firstErrorFound" value="" />
					</c:if>	
					&nbsp;&nbsp;&nbsp;The Job Code for <c:out value="${KualiForm.document.budgetPersons[status.index].personName}" /> is not complete. You must enter a job code value before budgeting this individual.<br/><br/>
				</div>
			</c:if>		
		</c:forEach>
		
		<div align="left">
    	&nbsp;&nbsp;&nbsp;Changes made in the Project Personnel panel must be saved before the corresponding results are reflected in the Personnel Details panel.<br/><br/>
    	</div>  
    	<div class="h2-container">
    		<span class="subhead-left"><h2>Project Personnel (All Periods)</h2></span>
    		<span class="subhead-right"><kul:help businessObjectClassName="org.kuali.kra.budget.bo.BudgetPerson" altText="help"/></span>
        </div>
        <table id="budget-personnel-table" cellpadding=0 cellspacing="0" summary="">
        	<tr>
	        	<th>&nbsp;</th>
	            <th>Person</th>
	            <th><kul:htmlAttributeLabel attributeEntry="${budgetPersonAttributes.jobCode}" /></div></th>
	            <th><kul:htmlAttributeLabel attributeEntry="${budgetPersonAttributes.appointmentType}" /></div></th>
	            <th><kul:htmlAttributeLabel attributeEntry="${budgetPersonAttributes.calculationBase}" /></div></th>
	            <th><kul:htmlAttributeLabel attributeEntry="${budgetPersonAttributes.effectiveDate}" /></th>
	            <th>Actions</th>
			</tr>
			
			<kra:section permission="modifyBudgets">
			<tr>
	        	<th>Add:</th>
	            <td colspan="6">
            		<label>Employee Search</label>
              		<label><kul:multipleValueLookup boClassName="org.kuali.kra.bo.Person" 
                    	lookedUpCollectionName="newBudgetPersons" /></label><br>
                    <label>Non-employee Search</label>
              		<label><kul:multipleValueLookup boClassName="org.kuali.kra.bo.NonOrganizationalRolodex" 
                    	lookedUpCollectionName="newBudgetRolodexes" /></label><br>
                    <label>To be named</label>
                   	<label><kul:multipleValueLookup boClassName="org.kuali.kra.budget.bo.TbnPerson" 
                    	lookedUpCollectionName="newTbnPersons" /></label>
	            </td>
			</tr>
			</kra:section>
			
            <c:forEach var="person" items="${KualiForm.document.budgetPersons}" varStatus="status">
            <tr>
              	<th scope="row"><div align="center">${status.index + 1}</div></th>
              	<td>${person.personName} <c:if test="${!empty person.role}"><span class="fineprint">(${person.role})</span></c:if></td>
              	
              	<td>
              	 	<c:set var="jobCodeFieldName" value="document.budgetPersons[${status.index}].jobCode"/> 
              	 	<c:set var="jobTitleFieldName" value="document.budgetPersons[${status.index}].jobTitle"/> 
              		<kul:htmlControlAttribute property="document.budgetPersons[${status.index}].jobCode" attributeEntry="${budgetPersonAttributes.jobCode}" onblur="loadJobCodeTitle('${jobCodeFieldName}', '${jobTitleFieldName}');" />
              		<kul:lookup boClassName="org.kuali.kra.budget.bo.JobCode" fieldConversions="jobCode:document.budgetPersons[${status.index}].jobCode,jobTitle:document.budgetPersons[${status.index}].jobTitle" anchor="${tabKey}" />
              		
              		<div id="document.budgetPersons[${status.index}].jobTitle.div" align="left">
                        <c:if test="${!empty KualiForm.document.budgetPersons[status.index].jobCode}">               	    
							${KualiForm.document.budgetPersons[status.index].jobTitle}
							<c:if test="${empty KualiForm.document.budgetPersons[status.index].jobTitle}">
	                    		<span style='color: red;'>not found</span>
							</c:if>                   
                        </c:if>
					</div>
              	</td>
              	<td>
              		<kra:kraControlAttribute property="document.budgetPerson[${status.index}].appointmentTypeCode" readOnly="${readOnly}" attributeEntry="${budgetPersonAttributes.appointmentType}"/>
              	</td>
              	<td>
              		<div align="center">
                  		<kul:htmlControlAttribute property="document.budgetPerson[${status.index}].calculationBase" attributeEntry="${budgetPersonAttributes.calculationBase}" styleClass="amount" />
              		</div>
              	</td>
              	<td>
              		<div align="center">
						<kul:htmlControlAttribute property="document.budgetPerson[${status.index}].effectiveDate" attributeEntry="${budgetPersonAttributes.effectiveDate}" datePicker="true" />
                  	</div>
                </td>
              	<td>
              		<div align=center>&nbsp;
              			<kra:section permission="modifyBudgets">
	              			<html:image property="methodToCall.deleteBudgetPerson.line${status.index}.x" src='${ConfigProperties.kra.externalizable.images.url}tinybutton-delete1.gif' styleClass="tinybutton" />
	              		</kra:section>
              		</div>
              	</td>
			</tr>
        	</c:forEach>
        </table>
        
        <br/>
        <div align="center">
	        <html:image property="methodToCall.synchToProposal" src='${ConfigProperties.kra.externalizable.images.url}tinybutton-syncpersonnel.gif' styleClass="tinybutton"/>
		</div>
    	
     </div>
</kul:tab>


