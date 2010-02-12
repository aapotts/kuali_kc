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
package org.kuali.kra.document;

import java.util.Iterator;

import org.junit.Test;
import org.kuali.kra.bo.SponsorFormTemplate;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.maintenance.MaintenanceDocumentTestBase;
import org.kuali.rice.kns.document.MaintenanceDocumentBase;
import org.kuali.rice.kns.service.DocumentService;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/** 
 * This class test the Sponsor Form Template Maintenance Document.
 * This particular maintenance document is unique in that is has an attachment.
 * Attachments were not working correctly due to OJB issues in rice.
 */
public class SponsorFormTemplateMaintenanceDocumentTest extends MaintenanceDocumentTestBase {

    private static final String DOCTYPE = "SponsorFormTemplateMaintenanceDocument";
    
    public String getDocTypeName() {
        return DOCTYPE;
    }
    
    /** Test basic creation and submit of a Sponsor Form Template Maintenance Document. */
    @Test
    public void testCreateNew() throws Exception {
        HtmlPage maintenancePage = getMaintenanceDocumentPage("Sponsor Form Templates",SponsorFormTemplate.class.getName(),"Kuali :: Sponsor Form Template");
        String documentNumber = getFieldValue(maintenancePage, "document.documentHeader.documentNumber");
        assertContains(maintenancePage, "Edit SponsorFormTemplates New * Package Number: * Page Description: * Page Number: * Sponsor Code: Attachment: ");
        
        // set up required fields for organization
        setFieldValue(maintenancePage, "document.documentHeader.documentDescription", "SponsorFormTemplate Maint Doc - test");
        setFieldValue(maintenancePage, "document.newMaintainableObject.packageNumber", "1");
        setFieldValue(maintenancePage, "document.newMaintainableObject.pageDescription", "test desc");
        setFieldValue(maintenancePage, "document.newMaintainableObject.pageNumber", "111");
        setFieldValue(maintenancePage, "document.newMaintainableObject.sponsorCode", "005626");
        setFieldValue(maintenancePage, "document.newMaintainableObject.templateFile", getFilePath(SponsorFormTemplateMaintenanceDocumentTest.class));
        
        Iterator<DomNode> i = maintenancePage.getAllHtmlChildElements();
        while(i.hasNext()){
            DomNode dn = i.next();
            System.err.println(dn.getNodeName());
        }
        
        HtmlPage routedPage = clickOn(maintenancePage, "methodToCall.route", "Kuali :: Sponsor Form Template");
        
        assertContains(routedPage, "Document was successfully submitted.");
        assertContains(routedPage,"Package Number: 1 Page Description: test desc Page Number: 111 Sponsor Code: 005626 Attachment: SponsorFormTemplateMaintenanceDocumentTest.class ");
        MaintenanceDocumentBase document = (MaintenanceDocumentBase) KraServiceLocator.getService(DocumentService.class).getByDocumentHeaderId(documentNumber);
        assertNotNull(document.getDocumentNumber());
        assertNotNull(document.getDocumentHeader());
        assertEquals(document.getDocumentHeader().getDocumentNumber(),documentNumber);
        SponsorFormTemplate sft = (SponsorFormTemplate)document.getNewMaintainableObject().getBusinessObject();
        assertEquals(sft.getPackageNumber(), Integer.valueOf(1));
        assertEquals(sft.getPageDescription(), "test desc");
        assertEquals(sft.getPageNumber(), Integer.valueOf(111));
        assertEquals(sft.getSponsorCode(), "005626");
    }
}
