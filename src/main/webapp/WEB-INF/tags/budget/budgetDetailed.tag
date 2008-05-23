<%--
 Copyright 2005-2006 The Kuali Foundation.

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

<%@ attribute name="budgetCategoryTypeCodesKey" description="Budget Category Type Code Key" required="true" %>
<%@ attribute name="budgetCategoryTypeCodesLabel" description="Budget Category Type Code Label" required="true" %>
<%@ attribute name="catCodes" description="Category Type Index" required="true" %>

<c:set var="readOnly" value="${not KualiForm.editingMode['modifyBudgets']}" scope="request" />

<c:set var="budgetLineItemAttributes" value="${DataDictionary.BudgetLineItem.attributes}" />
<c:set var="action" value="budgetExpensesAction" />
<c:set var="textAreaFieldName" value="newBudgetLineItems[${catCodes}].lineItemDescription" />

<c:choose>
	<c:when test="${!empty KualiForm.viewBudgetPeriod}" >
		<c:set var="budgetPeriod" value="${KualiForm.viewBudgetPeriod}" />
	</c:when>
	<c:otherwise>
		<c:set var="budgetPeriod" value="1" />
	</c:otherwise>
</c:choose>
	<c:set var="tabErrorKeyString" value=""/>
    <c:forEach var="budgetCategoryTypeIndex" items="${KualiForm.document.budgetCategoryTypeCodes}" varStatus="status1">
    	<c:if test="${budgetCategoryTypeIndex.key ==  budgetCategoryTypeCodesKey}">
    		<c:set var="index" value="0"/>
    		<c:forEach var="budgetLineItems" items="${KualiForm.document.budgetPeriods[budgetPeriod - 1].budgetLineItems}" varStatus="status">			    		
    		<c:if test="${budgetLineItems.budgetCategory.budgetCategoryTypeCode == budgetCategoryTypeIndex.key}" >    						
			<c:set var="index" value="${index+1}"/>
				<c:choose>
	    			<c:when test="${empty tabErrorKeyString}">
	    				<c:set var="tabErrorKeyString" value="document.budgetPeriods[${budgetPeriod - 1}].budgetLineItems[${status.index}].lineItemDescription"/>
	    			</c:when>
	    			<c:otherwise>
	    				<c:set var="tabErrorKeyString" value="${tabErrorKeyString},document.budgetPeriods[${budgetPeriod - 1}].budgetLineItems[${status.index}].lineItemDescription"/>
	    			</c:otherwise>
	    		</c:choose>
	    		<c:choose>
	    			<c:when test="${empty tabErrorKeyString2}">
	    				<c:set var="tabErrorKeyString2" value="document.budgetCategoryTypes[${budgetCategoryTypeIndex.key}].budgetPeriods[${budgetPeriod - 1}].budgetLineItems[${status.index}].*"/>
	    			</c:when>
	    			<c:otherwise>
	    				<c:set var="tabErrorKeyString2" value="${tabErrorKeyString2},document.budgetCategoryTypes[${budgetCategoryTypeIndex.key}].budgetPeriods[${budgetPeriod - 1}].budgetLineItems[${status.index}].*"/>
	    			</c:otherwise>
	    		</c:choose>			    		
    		</c:if>    		
    		<c:if test="${index!=0}">    					    		
    			<c:set var="budgetLineItemSize" value="${index}"/>
    		</c:if>
    		</c:forEach>
    	</c:if>
    </c:forEach>

	<c:set var="budgetExpensePanelReadOnly" value="${KualiForm.document.proposal.budgetVersionOverviews[KualiForm.document.budgetVersionNumber-1].finalVersionFlag}" />
	 	
	<kul:tab tabTitle="${budgetCategoryTypeCodesLabel}" tabItemCount="${budgetLineItemSize}" defaultOpen="false" tabErrorKey="*costElement*,newBudgetLineItems[${catCodes}].*,${tabErrorKeyString},${tabErrorKeyString2}">
		<div class="tab-container" align="center">
    	<div class="h2-container">
    		<span class="subhead-left"><h2>${budgetCategoryTypeCodesLabel}</h2></span>
    		<span class="subhead-right"><kul:help businessObjectClassName="fillMeIn" altText="help"/></span>
        </div>
        <jsp:useBean id="paramMap" class="java.util.HashMap"/>
		<c:set target="${paramMap}" property="budgetCategoryTypeCode" value="${budgetCategoryTypeCodesKey}" />
        <table border="0" cellpadding=0 cellspacing=0 summary="">
          	<tr>
          		<th width="5%"><div align="center">&nbsp</div></th> 
          		<th width="10%"><div align="center"><kul:htmlAttributeLabel attributeEntry="${budgetLineItemAttributes.costElement}" noColon="true" /></div></th>
          		<th width="10%"><div align="center"><kul:htmlAttributeLabel attributeEntry="${budgetLineItemAttributes.lineItemDescription}" noColon="true" /></div></th>
          		<th width="10%"><div align="center"><kul:htmlAttributeLabel attributeEntry="${budgetLineItemAttributes.underrecoveryAmount}" noColon="true" /></div></th>
          		<th width="10%"><div align="center"><kul:htmlAttributeLabel attributeEntry="${budgetLineItemAttributes.costSharingAmount}" noColon="true" /></div></th>
          		<th width="10%"><div align="center"><kul:htmlAttributeLabel attributeEntry="${budgetLineItemAttributes.quantity}" noColon="true" /></div></th>
          		<th width="15%"><div align="center"><kul:htmlAttributeLabel attributeEntry="${budgetLineItemAttributes.lineItemCost}" noColon="true" /></div></th>
              	<kul:htmlAttributeHeaderCell literalLabel="Action" scope="col"/>
          	</tr>    
          	
          	<kra:section permission="modifyBudgets">    
	            <tr>
					<th class="infoline">
						<c:out value="Add:" />
					</th>
					<td valign="middle" class="infoline" nowrap="true">
	                	<div align="center">
	                	<html:select property="newBudgetLineItems[${catCodes}].costElement" tabindex="0" >
	                    <c:forEach items="${krafn:getOptionList('org.kuali.kra.budget.lookup.keyvalue.CostElementValuesFinder', paramMap)}" var="option">
	                    <c:choose>                    	
	                    	<c:when test="${KualiForm.newBudgetLineItems[catCodes].costElement == option.key}">
	                        <option value="${option.key}" selected>${option.label}</option>
	                        </c:when>
	                        <c:otherwise>
	                        <c:out value="${option.label}"/>
	                        <option value="${option.key}">${option.label}</option>
	                        </c:otherwise>
	                    </c:choose>                    
	                    </c:forEach>
	                    </html:select>
	                    <input type="hidden" name="document.budgetCategoryType[${catCodes}]" value="${budgetCategoryTypeCodesKey}">                    
	                	<kul:lookup boClassName="org.kuali.kra.budget.bo.CostElement" fieldConversions="costElement:newBudgetLineItems[${catCodes}].costElement" anchor="${tabKey}" lookupParameters="newBudgetLineItems[${catCodes}].costElement:costElement,document.budgetCategoryType[${catCodes}]:budgetCategoryTypeCode" autoSearch="yes"/>                	
	                	<kul:directInquiry boClassName="org.kuali.kra.budget.bo.CostElement" inquiryParameters="newBudgetLineItems[${catCodes}].costElement:costElement" anchor="${tabKey}"/>	                	
	                	</div>
					</td>
					<td valign="middle" class="infoline">
	                	<div align="center">
	                	<kul:htmlControlAttribute property="newBudgetLineItems[${catCodes}].lineItemDescription" attributeEntry="${budgetLineItemAttributes.lineItemDescription}" readOnly="${budgetExpensePanelReadOnly}"/>
	                	<kra:expandedTextArea textAreaFieldName="${textAreaFieldName}" action="${action}" textAreaLabel="${budgetLineItemAttributes.lineItemDescription.label}" />                	
	                	</div>
					</td>
					<td valign="middle" class="infoline">
	                	<div align="center">
	                	<kul:htmlControlAttribute property="newBudgetLineItems[${catCodes}].underrecoveryAmount" attributeEntry="${budgetLineItemAttributes.underrecoveryAmount}" styleClass="amount" readOnly="true"/>
	                	</div>
					</td>
	                <td valign="middle" class="infoline">
	                	<div align="center">
	                	<kul:htmlControlAttribute property="newBudgetLineItems[${catCodes}].costSharingAmount" attributeEntry="${budgetLineItemAttributes.costSharingAmount}" styleClass="amount" readOnly="${budgetExpensePanelReadOnly}"/>
	                	</div>
					</td>
	                <td valign="middle" class="infoline">
	                	<div align="center">
	                	<kul:htmlControlAttribute property="newBudgetLineItems[${catCodes}].quantity" attributeEntry="${budgetLineItemAttributes.quantity}" styleClass="amount" readOnly="${budgetExpensePanelReadOnly}"/>
	                	</div>
	                </td>
	                <td valign="middle" class="infoline">                	
	                	<div align="center">
	                  	<kul:htmlControlAttribute property="newBudgetLineItems[${catCodes}].lineItemCost" attributeEntry="${budgetLineItemAttributes.lineItemCost}" styleClass="amount" readOnly="${budgetExpensePanelReadOnly}"/> 
	                	</div>
					</td>				
					<td class="infoline">
						<c:if test="${!budgetExpensePanelReadOnly}" >
						<div align=center>
							<html:image property="methodToCall.addBudgetLineItem.budgetCategoryTypeCode${budgetCategoryTypeCodesKey}.catTypeIndex${catCodes}.anchor${tabKey}"
							src='${ConfigProperties.kra.externalizable.images.url}tinybutton-add1.gif' />
						</div>
						</c:if>	
	                </td>			
	            </tr>
            </kra:section>
             
			    <c:forEach var="budgetCategoryTypeIndex" items="${KualiForm.document.budgetCategoryTypeCodes}" varStatus="status1">
			    	<c:if test="${budgetCategoryTypeIndex.key ==  budgetCategoryTypeCodesKey}">
			    		<c:set var="index" value="0"/>
			    		<c:forEach var="budgetLineItems" items="${KualiForm.document.budgetPeriods[budgetPeriod - 1].budgetLineItems}" varStatus="status">
			    		<c:if test="${budgetLineItems.budgetCategory.budgetCategoryTypeCode == budgetCategoryTypeIndex.key}" >
							<kra-b:budgetLineItems budgetPeriod = "${budgetPeriod}" budgetCategoryTypeCode = "${budgetCategoryTypeCodesKey}" budgetLineItemNumber="${status.index}" budgetLineItemSequenceNumber="${index}" innerTabParent="${budgetCategoryTypeCodesLabel}" budgetExpensePanelReadOnly="${budgetExpensePanelReadOnly}"/>
							<c:set var="index" value="${index+1}"/>			    		
			    		</c:if> 		
			    		</c:forEach>
			    	</c:if>
			    </c:forEach>
			    			    			    
        </table>  
	    </div>
	</kul:tab>