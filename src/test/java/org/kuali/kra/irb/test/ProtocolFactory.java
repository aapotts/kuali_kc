/*
 * Copyright 2006-2008 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.osedu.org/licenses/ECL-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kra.irb.test;

import java.util.ArrayList;

import org.kuali.kra.bo.DocumentNextvalue;
import org.kuali.kra.irb.Protocol;
import org.kuali.kra.irb.ProtocolDocument;
import org.kuali.kra.irb.personnel.ProtocolPerson;
import org.kuali.kra.irb.personnel.ProtocolUnit;
import org.kuali.rice.kew.exception.WorkflowException;
import org.kuali.rice.kns.service.DocumentService;
import org.kuali.rice.kns.service.KNSServiceLocator;

/**
 * Base class for Protocol business rule tests.
 * 
 */
public class ProtocolFactory {

    private static final String DEFAULT_DOCUMENT_DESCRIPTION = "Protocol Document";
    private static final String PROTOCOL_NUMBER = "1";
    private static final String PROTOCOL_STATUS_STR = "100"; //test of option "Pending/In Progress";
    private static final String PROTOCOL_TYPE_CODE_STR = "1";//test of option "Standard";
    private static final String PROTOCOL_TITLE_STR = "New protocol test";
    private static final String PRINCIPAL_INVESTIGATOR_ID = "000000001";
    private static final String PRINCIPAL_INVESTIGATOR_NAME = "Terry Durkin";
    private static final String PRINCIPAL_INVESTIGATOR_UNIT = "BL-BL";
    private static final String PRINCIPAL_INVESTIGATOR_ROLE = "PI";
    private static final String REFERENCE_PERSON_ROLE = "protocolPersonRole";
    private static final String REFERENCE_UNIT = "unit";
   
    /**
     * Get a new Protocol Document.
     * 
     * @return a new Protocol Document.
     * @throws WorkflowException
     */
    public static ProtocolDocument createProtocolDocument() throws WorkflowException {
        DocumentService documentService = KNSServiceLocator.getDocumentService();
        ProtocolDocument protocolDocument = (ProtocolDocument) documentService.getNewDocument("ProtocolDocument");
        setProtocolRequiredFields(protocolDocument);
        documentService.saveDocument(protocolDocument);
        return protocolDocument;
    }

    /**
     * This method is to set required fields for Protocol document
     * @param document
     */
    public static void setProtocolRequiredFields(ProtocolDocument document) {
        Protocol protocol = document.getProtocol();
        document.getDocumentHeader().setDocumentDescription(DEFAULT_DOCUMENT_DESCRIPTION);
        document.setDocumentNextvalues(new ArrayList<DocumentNextvalue>());
        protocol.setProtocolDocument(document);
        protocol.setProtocolTypeCode(PROTOCOL_TYPE_CODE_STR);
        protocol.setTitle(PROTOCOL_TITLE_STR);
        protocol.setProtocolNumber(PROTOCOL_NUMBER);
        protocol.setSequenceNumber(0);
        
        ProtocolPerson protocolPerson = getProtocolPerson(PRINCIPAL_INVESTIGATOR_ID, PRINCIPAL_INVESTIGATOR_NAME, PRINCIPAL_INVESTIGATOR_ROLE);
        
        ProtocolUnit protocolUnit = new ProtocolUnit();
        protocolUnit.setUnitNumber(PRINCIPAL_INVESTIGATOR_UNIT);
        protocolUnit.setLeadUnitFlag(true);
        protocolUnit.setProtocolNumber(PROTOCOL_NUMBER);
        protocolUnit.setSequenceNumber(0);
        protocolUnit.refreshReferenceObject(REFERENCE_UNIT);

        protocol.setLeadUnitForValidation(protocolUnit);
        protocolPerson.getProtocolUnits().add(protocolUnit);
        
        protocol.getProtocolPersons().add(protocolPerson);
        protocol.setLeadUnitNumber(PRINCIPAL_INVESTIGATOR_UNIT);
        protocol.setPrincipalInvestigatorId(PRINCIPAL_INVESTIGATOR_ID);
    }
    
    /**
     * This method is to get protocol person details
     * @param personId
     * @param personName
     * @param personRole
     * @return
     */
    public static ProtocolPerson getProtocolPerson(String personId, String personName, String personRole) {
        ProtocolPerson protocolPerson = new ProtocolPerson();
        protocolPerson.setPersonId(personId);
        protocolPerson.setPersonName(personName);
        protocolPerson.setProtocolPersonRoleId(personRole);
        protocolPerson.setPreviousPersonRoleId(personRole);
        protocolPerson.setProtocolNumber(PROTOCOL_NUMBER);
        protocolPerson.setSequenceNumber(0);
        protocolPerson.refreshReferenceObject(REFERENCE_PERSON_ROLE);
        return protocolPerson;
    }
    
    /**
     * This method is to get protocol person with role PI
     * @return
     */
    public static ProtocolPerson getPrincipalInvestigator() {
        return getProtocolPerson(PRINCIPAL_INVESTIGATOR_ID, PRINCIPAL_INVESTIGATOR_NAME, PRINCIPAL_INVESTIGATOR_ROLE);
    }
}
