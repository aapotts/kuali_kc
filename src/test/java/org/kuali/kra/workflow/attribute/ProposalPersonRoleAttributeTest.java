/*
 * Copyright 2006-2009 The Kuali Foundation
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
package org.kuali.kra.workflow.attribute;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.core.UserSession;
import org.kuali.core.service.BusinessObjectService;
import org.kuali.core.service.DocumentService;
import org.kuali.core.util.GlobalVariables;
import org.kuali.core.workflow.service.KualiWorkflowDocument;
import org.kuali.kra.KraTestBase;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.proposaldevelopment.bo.ProposalPerson;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.workflow.ProposalPersonRoleAttribute;
import org.kuali.rice.KNSServiceLocator;
import edu.iu.uis.eden.Id;
import edu.iu.uis.eden.KEWServiceLocator;
import edu.iu.uis.eden.engine.RouteContext;
import edu.iu.uis.eden.routeheader.DocumentRouteHeaderValue;
import edu.iu.uis.eden.routetemplate.ResolvedQualifiedRole;
import edu.iu.uis.eden.routetemplate.Role;
import edu.iu.uis.eden.user.AuthenticationUserId;
public class ProposalPersonRoleAttributeTest extends KraTestBase{
    
    private DocumentService documentService = null;
    private static final Role PROPOSAL_INVESTIGATOR_ROLE = new Role(ProposalPersonRoleAttribute.class, "PROPOSALINVESTIGATOR", "Proposal Investigator");
    private static final Role CO_INVESTIGATOR_ROLE = new Role(ProposalPersonRoleAttribute.class, "COINVESTIGATOR", "Co-Investigator");
    private static final Role INVESTIGATORS = new Role(ProposalPersonRoleAttribute.class, "INVESTIGATORS", "Investigators");
    private static final Role KEY_PERSON_ROLE = new Role(ProposalPersonRoleAttribute.class, "KEYPERSON", "Key Person");
    private static final Role PROPOSAL_PERSON_ROLE = new Role(ProposalPersonRoleAttribute.class, "PROPOSALPERSONS", "Proposal Person");
    private BusinessObjectService bos;
    @Before
    public void setUp() throws Exception {
        super.setUp();
        GlobalVariables.setUserSession(new UserSession("quickstart"));
        documentService = KNSServiceLocator.getDocumentService();
        bos = KraServiceLocator.getService(BusinessObjectService.class);
    }

    @After
    public void tearDown() throws Exception {
        GlobalVariables.setUserSession(null);
        documentService = null;
        super.tearDown();
    }
    @Test
    public void proposalpersontest() throws Exception {
        ProposalDevelopmentDocument proposaldevelopmentdocument = (ProposalDevelopmentDocument) documentService.getNewDocument("ProposalDevelopmentDocument");
        setDocumentFields(proposaldevelopmentdocument);
        documentService.saveDocument(proposaldevelopmentdocument);
        DocumentRouteHeaderValue routeHeader = KEWServiceLocator.getRouteHeaderService().getRouteHeader(proposaldevelopmentdocument.getDocumentHeader().getWorkflowDocument().getRouteHeaderId());
        RouteContext routecontext=RouteContext.createNewRouteContext();  
        routecontext.setDocument(routeHeader);
        ProposalPersonRoleAttribute roleattribute=new ProposalPersonRoleAttribute();
        ResolvedQualifiedRole resolvedrole=roleattribute.resolveQualifiedRole(routecontext, PROPOSAL_INVESTIGATOR_ROLE.getName(), PROPOSAL_INVESTIGATOR_ROLE.getBaseName());
       
        for (Iterator<Id> ids = resolvedrole.getRecipients().iterator(); ids.hasNext();) {
            AuthenticationUserId authid = (AuthenticationUserId) ids.next();
            assertEquals(authid.getId(),"tdurkin");
        }

        ResolvedQualifiedRole resolvedrole1=roleattribute.resolveQualifiedRole(routecontext, CO_INVESTIGATOR_ROLE.getName(), CO_INVESTIGATOR_ROLE.getBaseName());
         for (Iterator<Id> ids = resolvedrole1.getRecipients().iterator(); ids.hasNext();) {
            AuthenticationUserId authid = (AuthenticationUserId) ids.next();
            assertEquals(authid.getId(),"jtester");
        }
        ResolvedQualifiedRole resolvedrole2=roleattribute.resolveQualifiedRole(routecontext, KEY_PERSON_ROLE.getName(), KEY_PERSON_ROLE.getBaseName());
         for (Iterator<Id> ids = resolvedrole2.getRecipients().iterator(); ids.hasNext();) {
            AuthenticationUserId authid = (AuthenticationUserId) ids.next();
            assertEquals(authid.getId(),"bhutchinso");
        }
         

    
    }
    private void setDocumentFields(ProposalDevelopmentDocument document) {
        Date requestedStartDateInitial = new Date(System.currentTimeMillis());
        Date requestedEndDateInitial = new Date(System.currentTimeMillis());
        List<ProposalPerson> ProposalPersons= new ArrayList<ProposalPerson>();

        ProposalPerson person = new ProposalPerson();
        person.setProposalNumber("7");
        person.setProposalPersonNumber(1);
        person.setProposalPersonRoleId("PI");
        person.setPersonId("000000001");
        person.setUserName("tdurkin");
        person.setOptInCertificationStatus("Y");
        person.setOptInUnitStatus("Y");


        ProposalPerson person2 = new ProposalPerson();
        person2.setProposalNumber("7");
        person2.setProposalPersonNumber(3);
        person2.setProposalPersonRoleId("COI");
        person2.setPersonId("000000008");
        person2.setUserName("jtester");
        person2.setOptInCertificationStatus("Y");
        person2.setOptInUnitStatus("Y");
                
        ProposalPerson person3= new ProposalPerson();
        person3.setProposalNumber("7");
        person3.setProposalPersonNumber(5);
        person3.setProposalPersonRoleId("KP");
        person3.setPersonId("000000005");
        person3.setUserName("bhutchinso");
        person3.setOptInCertificationStatus("Y");
        person3.setOptInUnitStatus("Y");
        person3.setProjectRole("test");
        ProposalPersons.add(person);
       
        ProposalPersons.add(person2);
        
        ProposalPersons.add(person3);
        
        
        
        document.setProposalPersons(ProposalPersons);
        document.getDocumentHeader().setFinancialDocumentDescription("ProposalDevelopmentDocumentTest test doc");
        document.setSponsorCode("005770");
        document.setTitle( "project title");
        document.setRequestedStartDateInitial(requestedStartDateInitial);
        document.setRequestedEndDateInitial(requestedEndDateInitial);
        document.setActivityTypeCode("1");
        document.setProposalTypeCode("1");
        document.setOwnedByUnitNumber("000001");
    }
}
