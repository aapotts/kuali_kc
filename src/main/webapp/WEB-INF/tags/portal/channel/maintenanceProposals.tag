<%--
 Copyright 2006 The Kuali Foundation.
 
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

<channel:portalChannelTop channelTitle="Proposals" />
<div class="body">
  <ul class="chan">
    <li><portal:portalLink displayTitle="true" title="Abstract Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.proposaldevelopment.bo.AbstractType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li>Budget Categories</li>
    <li><portal:portalLink displayTitle="true" title="Budget Status" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.proposaldevelopment.bo.BudgetStatus&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li><portal:portalLink displayTitle="true" title="Carrier Type" url="#" /></li>
    <li><portal:portalLink displayTitle="true" title="Cost Element" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.budget.bo.CostElement&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li><portal:portalLink displayTitle="true" title="Deadline Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.proposaldevelopment.bo.DeadlineType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li><portal:portalLink displayTitle="true" title="Degree Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.bo.DegreeType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li><portal:portalLink displayTitle="true" title="Mail By" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.proposaldevelopment.bo.MailBy&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li><portal:portalLink displayTitle="true" title="Narrative Status" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.proposaldevelopment.bo.NarrativeType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li><portal:portalLink displayTitle="true" title="Narrative Types" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.proposaldevelopment.bo.NarrativeStatus&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li><portal:portalLink displayTitle="true" title="Person Document Type" url="#" /></li>
    <li><portal:portalLink displayTitle="true" title="Person Table Editable Columns" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.bo.PersonEditableField&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li>Proposal Dev Editable Columns</li>
    <li>Proposal Hierarchy Child Type</li>
    <li>Proposal Status</li>
    <li><portal:portalLink displayTitle="true" title="Rate Class" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.budget.bo.RateClass&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li><portal:portalLink displayTitle="true" title="Rate Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.budget.bo.RateType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li>Result Type</li>
    <li><portal:portalLink displayTitle="true" title="S2S Submission Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.s2s.bo.S2sSubmissionType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li><portal:portalLink displayTitle="true" title="S2S Revision Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.bo.S2sRevisionType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li><portal:portalLink displayTitle="true" title="Valid Calculation Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.budget.bo.ValidCalcType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    <li><portal:portalLink displayTitle="true" title="Valid Cost Element Rate Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.budget.bo.ValidCeRateType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
  </ul>
</div>
<channel:portalChannelBottom />