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
<c:set var="readOnly" value="${not KualiForm.editingMode['modifyBudgets']}" scope="request" />

<c:if test="${KualiForm.editingMode['modifyBudgets']}">
	<c:set var="extraButtonSource" value="${ConfigProperties.kra.externalizable.images.url}buttonsmall_synctoprop.gif" />
	<c:set var="extraButtonProperty" value="methodToCall.synchToProposal" />
	<c:set var="extraButtonAlt" value="Synch to Proposal" />
</c:if>



<kul:documentPage
	showDocumentInfo="true"
	htmlFormAction="budgetPersonnel"
	documentTypeName="BudgetDocument"
  	headerDispatch="${KualiForm.headerDispatch}"
  	headerTabActive="personnel"
  	extraTopButtons="${KualiForm.extraTopButtons}">
  	
  	<div align="right"><kul:help documentTypeName="BudgetDocument" pageName="Project Personnel" /></div>
  	
	<kra:section permission="modifyBudgets">
	  	<kul:uncollapsable tabTitle="Add Project Personnel">
	  		<div align="center">
	            <table cellpadding="0" cellspacing="0" class="grid" summary="">
	              	<tr>
	                	<th class="grid"><div align="right">Person:</div></th>
	                	<td nowrap class="grid">
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
	            </table>
	        </div>
		</kul:uncollapsable> 
	</kra:section>
	<br/>
	
	<kra-b:budgetPersonnel/>

	<kul:documentControls 
		transactionalDocument="false"
		suppressRoutingControls="true"
		viewOnly="${KualiForm.editingMode['viewOnly']}"
		extraButtonSource="${extraButtonSource}"
		extraButtonProperty="${extraButtonProperty}"
		extraButtonAlt="${extraButtonAlt}"
		/>
		
<script type="text/javascript">
	var kualiForm = document.forms['KualiForm'];
	var kualiElements = kualiForm.elements;
</script>
		
<script language="javascript" src="scripts/kuali_application.js"></script>		
<script language="javascript" src="dwr/interface/JobCodeService.js"></script>
</kul:documentPage>