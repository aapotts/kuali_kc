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

<channel:portalChannelTop channelTitle="Shared" />
<div class="body">
  <ul class="chan">
    <li><portal:portalLink displayTitle="true" title="Activity Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.proposaldevelopment.bo.ActivityType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
  	<li><portal:portalLink displayTitle="true" title="Budget Category" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.budget.bo.BudgetCategory&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&docFormKey=88888888" /></li>
  	<li><portal:portalLink displayTitle="true" title="Budget Category Mapping" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.budget.bo.BudgetCategoryMapping&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&docFormKey=88888888" /></li>
  	<li><portal:portalLink displayTitle="true" title="Budget Category Maps" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.budget.bo.BudgetCategoryMap&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&docFormKey=88888888" /></li>
  	<li><portal:portalLink displayTitle="true" title="Budget Category Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.budget.bo.BudgetCategoryType&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&docFormKey=88888888" /></li>
    <li>Comment</li>
    <li>Cost Sharing Type</li>
    <li><portal:portalLink displayTitle="true" title="Custom Attribute" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.bo.CustomAttribute&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li><portal:portalLink displayTitle="true" title="Custom Attribute Document" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.bo.CustomAttributeDocument&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li><portal:portalLink displayTitle="true" title="Grants.gov Opportunity Lookup" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.s2s.bo.S2sOpportunity&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li>IDC Rate Types</li>
  	<li><portal:portalLink displayTitle="true" title="Institute La Rate" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.bo.InstituteLaRate&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&docFormKey=88888888" /></li>
    <li><portal:portalLink displayTitle="true" title="Institute Rate" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.bo.InstituteRate&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li><portal:portalLink displayTitle="true" title="Investigator Credit Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.proposaldevelopment.bo.InvestigatorCreditType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li><portal:portalLink displayTitle="true" title="Keywords" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.proposaldevelopment.bo.ScienceKeyword&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li><portal:portalLink displayTitle="true" title="Non-employee Lookup" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.bo.Rolodex&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li><portal:portalLink displayTitle="true" title="Notice of Opportunity" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.bo.NoticeOfOpportunity&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li><portal:portalLink displayTitle="true" title="NSF Science Code" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.bo.NsfCode&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li><portal:portalLink displayTitle="true" title="Object Code" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.budget.bo.CostElement&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&docFormKey=88888888" /></li></li>
    <li><portal:portalLink displayTitle="true" title="Organization" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.bo.Organization&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li><portal:portalLink displayTitle="true" title="Person Document Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.bo.PropPerDocType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li><portal:portalLink displayTitle="true" title="Proposal Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.proposaldevelopment.bo.ProposalType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
  	<li><portal:portalLink displayTitle="true" title="Rate Class" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.budget.bo.RateClass&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&docFormKey=88888888" /></li></li>    
  	<li><portal:portalLink displayTitle="true" title="Rate Class Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.budget.bo.RateClassType&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&docFormKey=88888888" /></li></li>    
  	<li><portal:portalLink displayTitle="true" title="Rate Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.budget.bo.RateType&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&docFormKey=88888888" /></li>
    <li><portal:portalLink displayTitle="true" title="Questions" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.bo.Ynq&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li><portal:portalLink displayTitle="true" title="Special Review Approval Status" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.bo.SpecialReviewApprovalType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li><portal:portalLink displayTitle="true" title="Special Review Approval Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.bo.SpecialReview&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li><portal:portalLink displayTitle="true" title="Valid Special Review Approval" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.bo.ValidSpecialReviewApproval&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
  	<li><portal:portalLink displayTitle="true" title="Valid Calculation Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.budget.bo.ValidCalcType&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&docFormKey=88888888" /></li></li>  	
  	<li><portal:portalLink displayTitle="true" title="Valid Cost Element Job Code" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.budget.bo.ValidCeJobCode&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&docFormKey=88888888" /></li></li>
  	<li><portal:portalLink displayTitle="true" title="Valid Cost Element Rate Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.budget.bo.ValidCeRateType&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&docFormKey=88888888" /></li></li>
  	<li><portal:portalLink displayTitle="true" title="Unit Administrator" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.bo.UnitAdministrator&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&docFormKey=88888888" /></li></li>  
  </ul>
</div>
<channel:portalChannelBottom />
