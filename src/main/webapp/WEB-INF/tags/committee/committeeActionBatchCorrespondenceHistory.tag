<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<c:set var="committeeAttributes" value="${DataDictionary.Committee.attributes}" />
<c:set var="batchCorrespondenceDetailAttributes" value="${DataDictionary.BatchCorrespondenceDetail.attributes}" />
<c:set var="kraAttributeReferenceDummyAttributes" value="${DataDictionary.KraAttributeReferenceDummy.attributes}" />

<h3>
    <span class="subhead-left">Batch Correspondence History</span>
    <span class="subhead-right"><kul:help businessObjectClassName="org.kuali.kra.committee.bo.CommitteeBatchCorrespondence" altText="help"/></span>
</h3>

<table cellpadding="0" cellspacing="0">
    <tr>
        <th width="20%">
            <div align="right">
                Committee ID:
            </div>
        </th> 
        <td width="30%">
            <div align="left">
                <kul:htmlControlAttribute property="document.committeeList[0].committeeId" 
                                          attributeEntry="${committeeAttributes.committeeId}" 
                                          readOnly="true" />
            </div>
        </td>
        
        <th width="20%">
            <div align="right">
                Committee Name:
            </div>
        </th> 
        <td width="30%">
            <div align="left">
                <kul:htmlControlAttribute property="document.committeeList[0].committeeName" 
                                          attributeEntry="${committeeAttributes.committeeName}" 
                                          readOnly="true" />
            </div>
        </td>
    </tr> 
    <tr>
        <th>
            <div align="right">
                Batch Type:
            </div>
        </th> 
        <td>
            <div align="left">
                <kul:htmlControlAttribute property="committeeHelper.committeeActionsHelper.historyBatchCorrespondenceTypeCode" 
                                          attributeEntry="${batchCorrespondenceDetailAttributes.batchCorrespondenceTypeCode}" 
                                          readOnly="false" />
            </div>
        </td>
        
        <th>
            <div align="right">
                Run Date:
            </div>
        </th> 
        <td>
            <div align="left">
                from
                <kul:htmlControlAttribute property="committeeHelper.committeeActionsHelper.historyStartDate" 
                                          attributeEntry="${kraAttributeReferenceDummyAttributes.genericDate}"
                                          readOnly="false" />
                to
                <kul:htmlControlAttribute property="committeeHelper.committeeActionsHelper.historyEndDate" 
                                          attributeEntry="${kraAttributeReferenceDummyAttributes.genericDate}" />
            </div>
        </td>
    </tr>
    <tr>
        <td class="infoline" colspan="4">
            <div align="center">
                <html:image property="methodToCall.filterBatchCorrespondenceHistory"
                            src='${ConfigProperties.kra.externalizable.images.url}tinybutton-filter.gif' 
                            styleClass="tinybutton"/>
            </div>                         
        </td>
    </tr> 
</table>

<c:if test="${not empty KualiForm.committeeHelper.committeeActionsHelper.historyBatchCorrespondenceTypeCode}">
    <p>&nbsp;</p>
    
    <h3>
        <span class="subhead-left">
            Batch Type:
            <kul:htmlControlAttribute property="committeeHelper.committeeActionsHelper.historyBatchCorrespondenceTypeCode" 
                                      attributeEntry="${batchCorrespondenceDetailAttributes.batchCorrespondenceTypeCode}" 
                                      readOnly="true" />
        </span>
        <span class="subhead-right"><kul:help businessObjectClassName="org.kuali.kra.committee.bo.CommitteeBatchCorrespondence" altText="help" /></span>
    </h3>
    
    <c:if test="${not empty KualiForm.committeeHelper.committeeActionsHelper.batchCorrespondenceHistory}">
        <table cellpadding=0 cellspacing=0 border=0>
            <c:forEach items="${KualiForm.committeeHelper.committeeActionsHelper.batchCorrespondenceHistory}" var="batchCorrespondenceHistory" varStatus="status">
                <tr>
                    <td class="neutral" >
                        <kra-committee:committeeActionBatchCorrespondenceRun committeeBatchCorrespondence="${batchCorrespondenceHistory}" index="${status.index}" />
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>            
</c:if>            
