<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<%@ attribute name="committeeMembership" description="The current committee membership which is being processed" required="true" %>
<%@ attribute name="memberIndex" description="The index of the current committee membership which is being processed" required="true" %>

<c:set var="committeeMembershipAttributes" value="${DataDictionary.CommitteeMembership.attributes}" />
<c:set var="personAttributes" value="${DataDictionary.Person.attributes}" />