<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<c:set var="protocolAttributes" value="${DataDictionary.Protocol.attributes}" />
<c:set var="protocolDocumentAttributes" value="${DataDictionary.ProtocolDocument.attributes}" />
		<kul:subtab lookedUpCollectionName="statusAndDate" width="100%" subTabTitle="Status & Dates">      
        <table cellpadding=0 cellspacing=0 summary="">
        	<tr>
				<th width="30%"><div align="right"><kul:htmlAttributeLabel attributeEntry="${protocolAttributes.protocolNumber}"/></div></th>
                <td width="20%">${KualiForm.document.protocol.protocolNumber}&nbsp;</td>
				<th width="30%"><div align="right"><kul:htmlAttributeLabel attributeEntry="${protocolAttributes.protocolStatusCode}"  /></div></th>
                <td width="20%">${KualiForm.document.protocol.protocolStatus.description}&nbsp;</td>
            </tr>
        	<tr>
				<th width="30%"><div align="right"><kul:htmlAttributeLabel attributeEntry="${protocolAttributes.approvalDate}" /></div></th>
                <td width="20%"align="left" valign="middle">
                	<kul:htmlControlAttribute property="document.protocol.approvalDate" attributeEntry="${protocolAttributes.approvalDate}" readOnly="true" />
                </td>
           		<th width="30%"><div align="right"><kul:htmlAttributeLabel attributeEntry="${protocolAttributes.lastApprovalDate}" /></div></th>
                <td width="20%"align="left" valign="middle">
                	<kul:htmlControlAttribute property="document.protocol.lastApprovalDate" attributeEntry="${protocolAttributes.lastApprovalDate}" readOnly="true" />
                </td>
            </tr>
            <tr>
                <!-- submission date is from protocol_submission ? This field is pending -->
                <th width="30%"><div align="right">Submission Date:</div></th>
                <td width="20%">Generated on Submission</td>
                <th width="30%"><div align="right"><kul:htmlAttributeLabel attributeEntry="${protocolAttributes.expirationDate}"/></div></th>
                <td width="20%"align="left" valign="middle">
                	<kul:htmlControlAttribute property="document.protocol.expirationDate" attributeEntry="${protocolAttributes.expirationDate}" readOnly="true" />
                </td>
            </tr>
        </table>
       </kul:subtab> 
