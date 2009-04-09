/*
 * Copyright 2006-2008 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kra.irb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.core.web.ui.Field;
import org.kuali.core.web.ui.Row;
import org.kuali.kra.KraTestBase;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.irb.bo.Protocol;
import org.kuali.kra.irb.dao.ProtocolDao;
import org.kuali.kra.irb.document.ProtocolDocument;
import org.kuali.kra.irb.lookup.ProtocolLookupableHelperServiceImpl;
import org.kuali.kra.irb.personnel.ProtocolPerson;

public class ProtocolLookupHelperServiceTest extends KraTestBase {

    ProtocolLookupableHelperServiceImpl protocolLookupableHelperServiceImpl;
    private static final String EDIT_URL ="<a href=\"../protocolProtocol.do?methodToCall=docHandler&command=initiate&docTypeName=ProtocolDocument&protocolNumber=100\">edit</a>";
    private static final String COPY_URL = "<a href=\"../DocCopyHandler.do?docId=101&command=displayDocSearchView&documentTypeName=ProtocolDocument\">copy</a>";
    private static final String UNIT_INQ_URL ="inquiry.do?businessObjectClassName=org.kuali.kra.bo.Unit&unitNumber=000001&methodToCall=start";
    private static final String PERSON_INQ_URL ="inquiry.do?businessObjectClassName=org.kuali.kra.bo.Person&personId=000000001&methodToCall=start";
    private static final String ROLODEX_INQ_URL ="inquiry.do?businessObjectClassName=org.kuali.kra.bo.Rolodex&rolodexId=1727&methodToCall=start";
    private static final int NUMBER_LOOKUP_CRITERIA_FIELDS = 18;
    private Mockery context = new JUnit4Mockery();
    @Before
    public void setUp() throws Exception {
        super.setUp();
        protocolLookupableHelperServiceImpl = (ProtocolLookupableHelperServiceImpl)KraServiceLocator.getService("protocolLookupableHelperService");
        protocolLookupableHelperServiceImpl.setBusinessObjectClass(Protocol.class);
   }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        protocolLookupableHelperServiceImpl = null;
    }

    /**
     * 
     * This method is to test getrows.  The lookup fields will be updated and make sure 
     * a few of the drop down fields are sert as dropdown_refresh 
     */
    @Test
    public void testGetRows() {
        List <String> indicatorNames = new ArrayList<String>();
        indicatorNames.add("personEmployeeIndicator");
        indicatorNames.add("investigatorEmployeeIndicator");
        indicatorNames.add("fundingSourceTypeCode");

        List<Row> rows = protocolLookupableHelperServiceImpl.getRows();
        assertEquals(rows.size(), NUMBER_LOOKUP_CRITERIA_FIELDS);
        for (Row row : rows) {
            for (Field field : row.getFields()) {
                if (field.getPropertyName().equals("personId") || field.getPropertyName().equals("principalInvestigatorId")) {
                    assertDropDownField(field, "personId","org.kuali.kra.bo.Person");
                } else if (field.getPropertyName().equals("fundingSource")) {
                    assertDropDownField(field, "sponsorCode","org.kuali.kra.bo.Sponsor");
                } else if (field.getPropertyName().equals("researchAreaCode")) {
                    assertDropDownField(field, "researchAreaCode","org.kuali.kra.bo.ResearchArea");
                } else if (indicatorNames.contains(field.getPropertyName())) {
                    assertEquals(field.getFieldType(), Field.DROPDOWN_REFRESH);
                } 
            }
        }
    }
    
    /*
     * 
     * This method is to make sure the looks fields are set up properly.
     * @param field
     * @param keyName
     * @param className
     */
    private void assertDropDownField(Field field, String keyName,String className) {
        assertEquals(field.getFieldConversions(), keyName+":"+field.getPropertyName());
        assertEquals(field.getLookupParameters(), field.getPropertyName()+":"+keyName);
        assertEquals(field.getInquiryParameters(), field.getPropertyName()+":"+keyName);
        assertEquals(field.getQuickFinderClassNameImpl(), className);

    }

    /**
     * 
     * This method test a few specila inquirey fields are set up properly.
     */
    @Test
    public void testGetInquiryUrl() {
        Protocol protocol = initProtocol();
        String inquiryUrl = protocolLookupableHelperServiceImpl.getInquiryUrl(protocol, "leadUnitNumber");
        assertEquals(inquiryUrl, UNIT_INQ_URL);
        inquiryUrl = protocolLookupableHelperServiceImpl.getInquiryUrl(protocol, "principalInvestigatorId");
        assertEquals(inquiryUrl, PERSON_INQ_URL);
        ProtocolPerson protocolPerson = protocol.getProtocolPersons().get(0);
        protocolPerson.setPersonId("");
        protocolPerson.setRolodexId(new Integer(1727));
        protocol.getProtocolPersons().clear();
        protocol.getProtocolPersons().add(protocolPerson);
        inquiryUrl = protocolLookupableHelperServiceImpl.getInquiryUrl(protocol, "principalInvestigatorId");
        assertEquals(inquiryUrl, ROLODEX_INQ_URL);
    }
    
    /**
     * 
     * This method to check the 'edit' link is correct
     */
    @Test
    public void testGetActionUrl() {
        Protocol protocol = new Protocol();
        protocol.setProtocolNumber("100");
        ProtocolDocument document = new ProtocolDocument();
        document.setDocumentNumber("101");
        protocol.setProtocolDocument(document);
        String actionUrl = protocolLookupableHelperServiceImpl.getActionUrls(protocol);
        assertEquals(actionUrl, EDIT_URL+"&nbsp"+COPY_URL);
    }
        
    
    /**
     * 
     * This method to check the number of return results is correct.
     * This is using mock for protocoldao.
     * @throws Exception
     */
    @Test 
    public void testGetSearchResults() throws Exception {
        ProtocolLookupableHelperServiceImpl lookupHelperService = new ProtocolLookupableHelperServiceImpl();
        
        final ProtocolDao protocolDao = context.mock(ProtocolDao.class);
        context.checking(new Expectations() {{
            Map<String, String> fieldValues = new HashMap<String, String>();

            List<Protocol> protocols = new ArrayList<Protocol>();
            protocols.add(new Protocol());
            protocols.add(new Protocol());
            protocols.add(new Protocol());

            one(protocolDao).getProtocols(fieldValues);
            will(returnValue(protocols));
        }});
        lookupHelperService.setProtocolDao(protocolDao);
        assertEquals(lookupHelperService.getSearchResults(new HashMap<String, String>()).size(), 3);

    }

        
    /*
     * 
     * This method is to set up protocol for get inquiry url test
     * @return
     */
    private Protocol initProtocol() {
        Protocol protocol = new Protocol();
        protocol.setLeadUnitNumber("000001");
        
        protocol.setPrincipalInvestigatorId("000000001");
        ProtocolPerson protocolPerson = new ProtocolPerson();
        protocolPerson.setPersonId("000000001");
        protocol.getProtocolPersons().add(protocolPerson);
        protocolPerson.setProtocolPersonRoleId("PI");
        return protocol;
        
    }
}
