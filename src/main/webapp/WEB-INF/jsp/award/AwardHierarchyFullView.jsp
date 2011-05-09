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


<kul:documentPage
    showDocumentInfo="true"
    htmlFormAction="awardActions"
    documentTypeName="AwardDocument"
    renderMultipart="false"
    showTabButtons="false"
    auditCount="0"
    headerTabActive="actions">

<c:set var="awardHierarchyAttributes" value="${DataDictionary.AwardHierarchy.attributes}" />
<c:set var="action" value="awardHierarchy" />
<input type="hidden" property="viewOnly" id="viewOnly" name="viewOnly" value="${readOnly}" />
<input type="hidden" id = "canCreateAward" name="canCreateAward" value="${KualiForm.canCreateAward}" />
<input type="hidden" property="docTypeName" name="docTypeName" value="${KualiForm.docTypeName}" />

<kul:tabTop tabTitle="Award Hierarchy" defaultOpen="true">

   <div class="tab-container" align="right">
    	<h3> 
    		<span class="subhead-left">Hierarchy Actions</span>  
    		<span class="subhead-right">
    			<kul:help businessObjectClassName="org.kuali.kra.award.awardhierarchy.AwardHierarchy" altText="help"/>
			</span>
        </h3>        
		    
	<table style="border: medium none ; border-collapse: collapse;">    
	<tbody><tr>
		<td style="border: medium none ; border-collapse: collapse; background-color: rgb(234, 233, 234);">
		<span style="display: inline;" id="treecontrol">
		${fn:length(KualiForm.awardHierarchyNodes)} Nodes: 
		<a href="#" title="Collapse the entire tree below"><img src="static/images/jquery/minus.gif" /> Collapse All</a>
		<a href="#" title="Expand the entire tree below"><img src="static/images/jquery/plus.gif" /> Expand All</a>
		</span>
		</td>
	</tr>
	</tbody></table>	     
    <div style = "background:#e4e4e4; margin: 2px 0pt 0pt; clear: left; height: 725px; overflow-y: scroll; overflow-x: scroll; overflow: scroll; position: relative;" >     
  		<ul id="awardhierarchy" class="filetree stripeli treeview"  >
        <%-- <li><span class="folder">00000</span>
        </li> --%>
    	</ul>
    </div>
    
    </div>
    
	<c:forEach items="${KualiForm.awardHierarchyNodes}" var="tempNode" varStatus="status">
		<c:set var="createChildProperty" value="methodToCall.create.awardNumber${tempNode.key}" />  
		<c:set var="copyAwardProperty" value="methodToCall.copyAward.awardNumber${tempNode.key}" />
		${kfunc:registerEditableProperty(KualiForm, createChildProperty)}  
		${kfunc:registerEditableProperty(KualiForm, copyAwardProperty)}
	</c:forEach> 
	
    <input type="hidden" id = "rootAwardNumber" name="rootAwardNumber" value="${KualiForm.rootAwardNumber}">
    <input type="hidden" id ="currentAwardNumber" name="document.awardList[0].awardNumber" value="${KualiForm.document.awardList[0].awardNumber}">
    <input type="hidden" id ="currentSeqNumber" name="document.awardList[0].sequenceNumber" value="${KualiForm.document.awardList[0].sequenceNumber}">
    <input type="hidden" id = "selectedAwardNumber" name="selectedAwardNumber" value="${(param.selectedAwardNumber == '' or param.selectedAwardNumber == null) ? selectedAwardNumber : param.selectedAwardNumber}">
	
	<c:forEach var="i" begin="1" end="${fn:length(KualiForm.awardHierarchyNodes)}" step="1" varStatus ="status">
		<input type="hidden" id = "awardHierarchyTempObject[${i}].awardNumber1" name="awardHierarchyTempObject[${i}].awardNumber1" value="${KualiForm.awardHierarchyTempObjects[i].awardNumber1}">
		<c:set var="lookupAwardNumber1" value="methodToCall.performLookup.(!!org.kuali.kra.award.home.Award!!).(((awardNumber:awardHierarchyTempObject[${i}].awardNumber1,awardHierarchyTempObject[${i}].awardNumber1:awardNumber))).((``)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~))" />
		${kfunc:registerEditableProperty(KualiForm, lookupAwardNumber1)}
		<input type="hidden" id = "awardHierarchyTempObject[${i}].selectBox1" name="awardHierarchyTempObject[${i}].selectBox1" value="${KualiForm.awardHierarchyTempObjects[i].selectBox1}">
		<input type="hidden" id = "awardHierarchyTempObject[${i}].awardNumber2" name="awardHierarchyTempObject[${i}].awardNumber2" value="${KualiForm.awardHierarchyTempObjects[i].awardNumber2}">
		<c:set var="lookupAwardNumber2" value="methodToCall.performLookup.(!!org.kuali.kra.award.home.Award!!).(((awardNumber:awardHierarchyTempObject[${i}].awardNumber2,awardHierarchyTempObject[${i}].awardNumber2:awardNumber))).((``)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~))" />
		${kfunc:registerEditableProperty(KualiForm, lookupAwardNumber2)}
		<input type="hidden" id = "awardHierarchyTempObject[${i}].selectBox2" name="awardHierarchyTempObject[${i}].selectBox2" value="${KualiForm.awardHierarchyTempObjects[i].selectBox2}">
		<input type="hidden" id = "awardHierarchyTempObject[${i}].copyDescendants" name="awardHierarchyTempObject[${i}].copyDescendants" value="${KualiForm.awardHierarchyTempObjects[i].copyDescendants}">
		<input type="hidden" id = "awardHierarchyTempObject[${i}].createNewChildRadio" name="awardHierarchyTempObject[${i}].createNewChildRadio" value="${KualiForm.awardHierarchyTempObjects[i].createNewChildRadio}">
		<input type="hidden" id = "awardHierarchyTempObject[${i}].copyAwardRadio" name="awardHierarchyTempObject[${i}].copyAwardRadio" value="${KualiForm.awardHierarchyTempObjects[i].copyAwardRadio}">
	</c:forEach>

</kul:tabTop>
<kul:panelFooter />

</kul:documentPage>

	<script language="JavaScript" type="text/javascript" src="dwr/engine.js"></script>

	<script language="JavaScript" type="text/javascript" src="dwr/util.js"></script>
		
	<script language="JavaScript" type="text/javascript" src="dwr/interface/AwardHierarchyUIService.js"></script>	

	<script src="scripts/jquery/jquery.js"></script>
	<link rel="stylesheet" href="css/jquery/new_kuali.css" type="text/css" />
	<link rel="stylesheet" href="css/jquery/kuali-stylesheet.css" type="text/css" />
	<link rel="stylesheet" href="css/jquery/jquery.treeview.css" type="text/css" />

	<script type="text/javascript" src="scripts/jquery/jquery.treeview.js"></script>
	<script type="text/javascript" src="scripts/awardHierarchy.js"></script>