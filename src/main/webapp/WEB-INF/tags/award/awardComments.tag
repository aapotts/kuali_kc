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
<c:set var="syncPropertyName" value="awardComments" />

<kul:tabTop tabTitle="Comments" defaultOpen="false" tabErrorKey="*">
	<div class="tab-container" align="center">
    	<table id="comments-table" cellpadding="0" cellspacing="0" summary="Sponsor Template">
		<tr>
        <h3>
    		<span class="subhead-left">Comments</span>
        </h3>
         <c:forEach var="commentType" items="${KualiForm.awardCommentBean.awardCommentScreenDisplayTypes}" varStatus="commentTypeIndex">        	        	
			<kra-a:awardCommentsTypes index="${commentTypeIndex.index}" commentTypeDescription="${commentType.description}" />
		</c:forEach>
		</tr>
		<tr>
          	<th colspan="2" align="center" scope="row">
          		<div align="center">
        			<html:image property="methodToCall.syncAwardTemplate.syncPropertyName${syncPropertyName}.anchor${tabKey}"
				src='${ConfigProperties.kra.externalizable.images.url}tinybutton-synctotemplate.gif' styleClass="tinybutton"/>
			</div>
        	</th>
		</tr>
		</table>
 	</div>
</kul:tabTop>