<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<%@ attribute name="parentTab" required="true" %>
<%@ attribute name="activityIndex" required="true" %>
<%@ attribute name="activity" required="true" type="org.kuali.kra.negotiations.bo.NegotiationActivity" %>
<%@ attribute name="tabDivClass" required="false" %>

<c:set var="activityAttributes" value="${DataDictionary.NegotiationActivity.attributes}" />
<c:set var="attachmentAttributes" value="${DataDictionary.NegotiationActivityAttachment.attributes}" />
<c:set var="readOnly" value="${false}" scope="request" />

<c:if test="${empty tabDivClass}">
  <c:set var="tabDivClass" value="innerTab-head"/>
</c:if>

<c:set var="newActivity" value="${activityIndex == -1}"/>
<c:choose><c:when test="${activityIndex == -1}">
  <c:set var="tabTitle" value="Add Activity"/>
  <c:set var="activityPath" value="negotiationActivityHelper.newActivity"/>
</c:when><c:otherwise>
  <c:set var="tabTitle" value="${activity.startDate} ${activity.activityType.description}"/>
  <c:set var="activityPath" value="document.negotiationList[0].activities[${activityIndex}]"/>
</c:otherwise></c:choose>

<kul:innerTab parentTab="${parentTab}" tabTitle="${tabTitle}" defaultOpen="false" tabErrorKey="${activityPath}*" useCurrentTabIndexAsKey="true" overrideDivClass="${tabDivClass}">
            <div class="innerTab-container" align="left">
        
        <table cellpadding="4" cellspacing="0" summary="">
            <tr>
		        <th><div align="right"><kul:htmlAttributeLabel attributeEntry="${activityAttributes.locationId}" /></div></th>
                <td>
                	<kul:htmlControlAttribute property="${activityPath}.locationId" attributeEntry="${activityAttributes.locationId}" readOnly="${readOnly}"/>
                </td>
		        <th><div align="right"><kul:htmlAttributeLabel attributeEntry="${activityAttributes.activityTypeId}" /></div></th>
                <td>
                	<kul:htmlControlAttribute property="${activityPath}.activityTypeId" attributeEntry="${activityAttributes.activityTypeId}" readOnly="${readOnly}"/>
                </td>
		        <th><div align="right">Number of Days:</div></th>
                <td>
                	<c:out value="${activity.numberOfDays}"/>
                </td>                
            </tr>
            <tr>
		        <th><div align="right"><kul:htmlAttributeLabel attributeEntry="${activityAttributes.startDate}" /></div></th>
                <td>
                	<kul:htmlControlAttribute property="${activityPath}.startDate" attributeEntry="${activityAttributes.startDate}" readOnly="${readOnly}"/>
                </td>
		        <th><div align="right"><kul:htmlAttributeLabel attributeEntry="${activityAttributes.endDate}" /></div></th>
                <td>
                	<kul:htmlControlAttribute property="${activityPath}.endDate" attributeEntry="${activityAttributes.endDate}" readOnly="${readOnly}"/>
                </td>
		        <th><div align="right"><kul:htmlAttributeLabel attributeEntry="${activityAttributes.createDate}" /></div></th>
                <td>
                	<kul:htmlControlAttribute property="${activityPath}.createDate" attributeEntry="${activityAttributes.createDate}" readOnly="true"/>
                </td>             
            </tr>
            <tr>
		        <th><div align="right"><kul:htmlAttributeLabel attributeEntry="${activityAttributes.followupDate}" /></div></th>
                <td>
                	<kul:htmlControlAttribute property="${activityPath}.followupDate" attributeEntry="${activityAttributes.followupDate}" readOnly="${readOnly}"/>
                </td>
		        <th><div align="right"><kul:htmlAttributeLabel attributeEntry="${activityAttributes.lastModifiedDate}" /></div></th>
                <td>
                	<kul:htmlControlAttribute property="${activityPath}.lastModifiedDate" attributeEntry="${activityAttributes.lastModifiedDate}" readOnly="true"/>
                </td>
		        <th><div align="right"><kul:htmlAttributeLabel attributeEntry="${activityAttributes.lastModifiedUsername}" /></div></th>
                <td>
                	<kul:htmlControlAttribute property="${activityPath}.lastModifiedUser.fullName" attributeEntry="${activityAttributes.lastModifiedUser.fullName}" readOnly="true"/>
                </td>             
            </tr> 
            <tr>
		        <th><div align="right"><kul:htmlAttributeLabel attributeEntry="${activityAttributes.description}" /></div></th>
                <td colspan="5">
                	<kul:htmlControlAttribute property="${activityPath}.description" attributeEntry="${activityAttributes.description}" readOnly="${readOnly}"/>
                	<c:choose><c:when test="${activity.restricted}">
                	   <html:image property="methodToCall.unrestrictActivity.activityIndex${activityIndex}"
   		  					src="${ConfigProperties.kra.externalizable.images.url}tinybutton-unrestrict.gif" styleClass="tinybutton"/>
                	</c:when><c:otherwise>
                	   <html:image property="methodToCall.restrictActivity.activityIndex${activityIndex}"
   		  					src="${ConfigProperties.kra.externalizable.images.url}tinybutton-restrict.gif" styleClass="tinybutton"/>                	
                	</c:otherwise>
                	</c:choose>
                </td>
            </tr>
        </table>
        <table cellpadding="4" cellspacing="0" summary="">
            <tr>
            	<th><div align="right">Attachments:</div></th>
            	<th>File: <html:file property="${activityPath}.newAttachment.newFile"/></th>
            	<th><kul:htmlAttributeLabel attributeEntry="${attachmentAttributes.description}" useShortLabel="true" /> <kul:htmlControlAttribute property="${activityPath}.newAttachment.description" attributeEntry="${attachmentAttributes.description}" readOnly="${readOnly}"/></th>
            	<th><html:image property="methodToCall.addAttachment.activityIndex${activityIndex}"
   		  				src="${ConfigProperties.kra.externalizable.images.url}tinybutton-add1.gif" styleClass="tinybutton"/>
            	</th>
            </tr>
            <c:forEach items="${activity.attachments}" var="attachment" varStatus="ctr">
              <tr>
                <th style="text-align: right;"><c:out value="${ctr.count}"/></th>
                <td><a href="#" class="attachmentLink"><c:out value="${attachment.file.name}"/></a>
                        	<html:image property="methodToCall.viewAttachment.activityIndex${activityIndex}.attachmentIndex${ctr.count-1}"
   		  				src="${ConfigProperties.kra.externalizable.images.url}tinybutton-view.gif" styleClass="tinybutton" />
                </td>
                <td><kul:htmlControlAttribute property="${activityPath}.attachments[${ctr.count-1}].description" attributeEntry="${attachmentAttributes.description}" readOnly="true"/></td>
                <td><html:image property="methodToCall.deleteAttachment.activityIndex${activityIndex}.attachmentIndex${ctr.count-1}"
   		  				src="${ConfigProperties.kr.externalizable.images.url}tinybutton-delete1.gif" styleClass="tinybutton"/>
   		  			<c:choose><c:when test="${attachment.restricted}">
   		  			<html:image property="methodToCall.unrestrictAttachment.activityIndex${activityIndex}.attachmentIndex${ctr.count-1}"
   		  				src="${ConfigProperties.kra.externalizable.images.url}tinybutton-unrestrict.gif" styleClass="tinybutton"/>
   		  			</c:when><c:otherwise>
   		  			<html:image property="methodToCall.restrictAttachment.activityIndex${activityIndex}.attachmentIndex${ctr.count-1}"
   		  				src="${ConfigProperties.kra.externalizable.images.url}tinybutton-restrict.gif" styleClass="tinybutton"/>
   		  			</c:otherwise></c:choose>
   		  		</td>
              </tr>
            </c:forEach>
            <c:if test="${newActivity}">
			<tr>
			    <td class="infoline" style="text-align:center;" colspan="6">
					<html:image property="methodToCall.addActivity"
   		  				src="${ConfigProperties.kra.externalizable.images.url}tinybutton-addactivity.gif" styleClass="tinybutton"/>
			    </td>
			</tr>
			</c:if>
        </table>
        
        </div>
</kul:innerTab>

        
