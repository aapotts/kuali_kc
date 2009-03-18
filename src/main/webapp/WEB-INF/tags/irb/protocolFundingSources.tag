<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<c:set var="protocolFundingSourceAttributes" value="${DataDictionary.ProtocolFundingSource.attributes}" />
<c:set var="fundingSourceTypeAttributes" value="${DataDictionary.FundingSourceType.attributes}" />

<kul:tab tabTitle="Funding Sources" defaultOpen="false" tabErrorKey="document.protocolList[0].protocolFundingSource*,document.protocolList[0].newFundingSource*" auditCluster="requiredFieldsAuditErrors" tabAuditKey="document.title" useRiceAuditMode="true">
	<div class="tab-container" align="center">
    	<h3>
    		<span class="subhead-left">Funding Sources</span>
    		<span class="subhead-right"><kul:help businessObjectClassName="org.kuali.kra.protocol.bo.ProtocolType" altText="help"/></span>
        </h3>
		
		
		<table cellpadding=0 cellspacing=0 summary="">
        	<tr>
                <kul:htmlAttributeHeaderCell literalLabel="&nbsp;" scope="col" /> 
                <kul:htmlAttributeHeaderCell attributeEntry="${protocolFundingSourceAttributes.fundingSourceTypeCode}" />
                <kul:htmlAttributeHeaderCell attributeEntry="${protocolFundingSourceAttributes.fundingSource}" />
                <kul:htmlAttributeHeaderCell attributeEntry="${protocolFundingSourceAttributes.fundingSourceName}" />
                <kul:htmlAttributeHeaderCell attributeEntry="${protocolFundingSourceAttributes.fundingSourceTitle}" />
                <kul:htmlAttributeHeaderCell literalLabel="Actions" scope="col" />
                
            </tr>
        	<tr>
        	    <th class="infoline">
        	       <c:out value="Add:" />
                </th>  
                               
<%-- --%>               
                <td>
                    <div align="center">
                        <kul:htmlControlAttribute property="document.protocolList[0].newFundingSource.fundingSourceTypeCode" 
                                                  attributeEntry="${protocolFundingSourceAttributes.fundingSourceTypeCode}" />
                    </div>
                </td> 

                
                <td>
                        <div align="center">
                        <kul:htmlControlAttribute property="document.protocolList[0].newFundingSource.fundingSource" 
                                                  attributeEntry="${protocolFundingSourceAttributes.fundingSource}"
                                                  onblur="loadFundingSourceNameTitle(
                                                          'document.protocolList[0].newFundingSource.fundingSourceTypeCode',
                                                          'document.protocolList[0].newFundingSource.fundingSource',
                                                          'document.protocolList[0].newFundingSource.fundingSourceName',
                                                          'document.protocolList[0].newFundingSource.fundingSourceTitle');" />

<%--                          lookupKeyPath="document.protocolList[0].newFundingSource.fundingSourceTypeType.description" 
 --%>                        
                        <kul:fundingSourceLookup boClassName="${document.protocolList[0].newFundingSource.fundingSourceType.description}" 
                         fieldConversions="unitNumber:document.protocolList[0].newFundingSource.fundingSource,unitName:document.protocolList[0].newFundingSource.fundingSourceName" anchor="${currentTabIndex}"/> 
                        </div>
                
                </td>
             
                <td>
                    <div align="center">
                    <kul:htmlControlAttribute property="document.protocolList[0].newFundingSource.fundingSourceName" 
                                            attributeEntry="${protocolFundingSourceAttributes.fundingSourceName}" />
                    </div>
                </td>
                <td>
                <div align="center">
                    <kul:htmlControlAttribute property="document.protocolList[0].newFundingSource.fundingSourceTitle" 
                                            readOnly="true"      attributeEntry="${protocolFundingSourceAttributes.fundingSourceTitle}" />
                </div>
                   <c:out value="${document.protocolList[0].newFundingSource.fundingSourceTitle}" />
                </td>

                <td class="infoline">
                        <div align="center">
                            <html:image property="methodToCall.addProtocolFundingSource.anchor${tabKey}"
                            src='${ConfigProperties.kra.externalizable.images.url}tinybutton-add1.gif' styleClass="tinybutton"/>
                        </div>
                </td>
            </tr>
            
                        <%-- Existing data --%>
            <c:forEach var="protocolFundingSource" items="${KualiForm.document.protocolList[0].protocolFundingSources}" varStatus="status">
                 <tr>
                    <th class="infoline">
                        <c:out value="${status.index+1}" />
                    </th>
                  <td align="left" valign="middle">
                    <div align="left">
                        <kul:htmlControlAttribute property="document.protocolList[0].protocolFundingSources[${status.index}].fundingSourceType.description" 
                                                  readOnly="true" attributeEntry="${fundingSourceTypeAttributes.description}" /> 
                    </div>
                  </td>
                  <td align="left" valign="middle">
                    <div align="left">
                        <kul:htmlControlAttribute property="document.protocolList[0].protocolFundingSources[${status.index}].fundingSource" 
                                                  readOnly="true" attributeEntry="${protocolFundingSourceAttributes.fundingSource}" /> 
                    </div>                  
                  </td>
                  <td align="left" valign="middle">
                    <div align="left">
                        <kul:htmlControlAttribute property="document.protocolList[0].protocolFundingSources[${status.index}].fundingSourceName" 
                                                  readOnly="true" attributeEntry="${fundingSourceTypeAttributes.description}" />
                    </div>
                  </td>
                  <td align="left" valign="middle">
                    <div align="left">
                        <kul:htmlControlAttribute property="document.protocolList[0].protocolFundingSources[${status.index}].fundingSourceTitle" 
                                                  readOnly="true" attributeEntry="${fundingSourceTypeAttributes.description}" />
                    </div>
                  </td>
                  <td>
                    <div align=center>&nbsp;
<%--                        <kra:section permission="modifyProtocol">  --%>

                            <html:image property="methodToCall.deleteProtocolFundingSource.line${status.index}.anchor${currentTabIndex}"
                                src='${ConfigProperties.kra.externalizable.images.url}tinybutton-delete1.gif' styleClass="tinybutton"/>
<%--                        </kra:section>  --%>
                    </div>
                  </td>
                </tr>

            </c:forEach>
        </table>
	</div>	
</kul:tab>		