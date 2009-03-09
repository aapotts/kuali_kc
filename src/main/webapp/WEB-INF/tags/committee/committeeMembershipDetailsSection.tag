<%@ include file="/WEB-INF/jsp/committee/committeeMember.jsp"%>

<c:choose>
    <c:when test="${empty KualiForm.document.committee.committeeMemberships[memberIndex].personName}">
        <c:set var="parentTabName" value="" />
    </c:when>
    <c:otherwise>
        <bean:define id="parentTabName" name="KualiForm" property="${committeeMembership}.personName"/>
    </c:otherwise>
</c:choose>

<table cellpadding=0 cellspacing=0 summary="">
    <tr>
        <td>
            <kul:innerTab tabTitle="Person Details" parentTab="${parentTabName}" defaultOpen="false" tabErrorKey="">
                <div class="innerTab-container" align="left">
                    <table class=tab cellpadding=0 cellspacing="0" summary=""> 
                        <tr>
                            <th align="left">
                                <div align="right">
                                    <kul:htmlAttributeLabel attributeEntry="${committeeMembershipAttributes.personName}" />
                                </div>
                            </th>
                            <td align="left">
                                <kul:htmlControlAttribute property="${committeeMembership}.personName" 
                                                          attributeEntry="${committeeMembershipAttributes.personName}" 
                                                          readOnly="true" />
                            </td>
                            <th align="left">
                                <div align="right">
                                    <kul:htmlAttributeLabel attributeEntry="${committeeMembershipAttributes.termStartDate}" />
                                </div>
                            </th>
                            <td align="left">
                                <kul:htmlControlAttribute property="${committeeMembership}.termStartDate" 
                                                          attributeEntry="${committeeMembershipAttributes.termStartDate}" 
                                                          datePicker="true" />
                            </td>
                        </tr>
                        <tr>
                            <th align="left">
                                <div align="right">
                                    <kul:htmlAttributeLabel attributeEntry="${committeeMembershipAttributes.membershipTypeCode}" />
                                </div>
                            </th>
                            <td align="left">
                                <kul:htmlControlAttribute property="${committeeMembership}.membershipTypeCode" 
                                                          attributeEntry="${committeeMembershipAttributes.membershipTypeCode}" />
                                <br />
                                <kul:htmlControlAttribute property="${committeeMembership}.paidMember" 
                                                          attributeEntry="${committeeMembershipAttributes.paidMember}" />
                                (paid member)
                            </td>
                            <th align="left">
                                <div align="right">
                                    <kul:htmlAttributeLabel attributeEntry="${committeeMembershipAttributes.termEndDate}" />
                                </div>
                            </th>
                            <td align="left">
                                <kul:htmlControlAttribute property="${committeeMembership}.termEndDate" 
                                                          attributeEntry="${committeeMembershipAttributes.termEndDate}"
                                                          datePicker="true" />
                            </td>
                        <tr>
                            <th align="left">
                                <div align="right">
                                    <kul:htmlAttributeLabel attributeEntry="${committeeMembershipAttributes.contactNotes}" />
                                </div>
                            </th>
                            <td align="left">
                                <kul:htmlControlAttribute property="${committeeMembership}.contactNotes" 
                                                          attributeEntry="${committeeMembershipAttributes.contactNotes}" />
                            </td>
                            <th align="left">
                                <div align="right">
                                    <kul:htmlAttributeLabel attributeEntry="${committeeMembershipAttributes.trainingNotes}" />
                                </div>
                            </th>
                            <td align="left">
                                <kul:htmlControlAttribute property="${committeeMembership}.trainingNotes" 
                                                          attributeEntry="${committeeMembershipAttributes.trainingNotes}" />
                            </td>
                        </tr>
                    </table>
                </div>
            </kul:innerTab>
        </td>
    </tr>
</table>
