/*
 * Copyright 2005-2010 The Kuali Foundation
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
package org.kuali.kra.meeting.print;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.kuali.kra.committee.document.CommitteeDocument;
import org.kuali.kra.document.ResearchDocumentBase;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.irb.correspondence.ProtocolCorrespondenceTemplate;
import org.kuali.kra.irb.correspondence.ProtocolCorrespondenceTemplateService;
import org.kuali.kra.printing.print.AbstractPrint;
import org.kuali.kra.printing.util.PrintingUtils;

public class MeetingAgendaPrint  extends AbstractPrint {

    private static final long serialVersionUID = -4479561442586035578L;
    private static final String AGENDA_TYPE = "9";
    private static String XSL_CONTEXT_DIR = "/org/kuali/kra/printing/stylesheet/";
    private ProtocolCorrespondenceTemplateService protocolCorrespondenceTemplateService;
    
    @Override
    public ResearchDocumentBase getDocument() {
        return document;
    }

    /**
     * This method fetches the XSL style-sheets required for transforming the
     * generated XML into PDF.
     * 
     * @return {@link ArrayList}} of {@link Source} XSLs
     */
//    public List<Source> getXSLT() {
//        // TODO : should we override Print's 'getXSLTemplates' method, instead of implementing this method.
//        Source src = new StreamSource();
//        ArrayList<Source> sourceList = new ArrayList<Source>();
//        // TODO: cniesen - get template (create a service to get the template)
//        ProtocolCorrespondenceTemplate template = new ProtocolCorrespondenceTemplate();
//        src = new StreamSource(new ByteArrayInputStream(template.getCorrespondenceTemplate()));
//        sourceList.add(src);
//        return sourceList;
//    }

    @Override
    public List<Source> getXSLTemplates() {
        Source src = new StreamSource();
        ArrayList<Source> sourceList = new ArrayList<Source>();
        ProtocolCorrespondenceTemplate template = protocolCorrespondenceTemplateService.getProtocolCorrespondenceTemplate(
                ((CommitteeDocument) document).getCommittee().getCommitteeId(), AGENDA_TYPE);
        if (template != null) {
            src = new StreamSource(new ByteArrayInputStream(template.getCorrespondenceTemplate()));
            sourceList.add(src);
        }
        return sourceList;
//        Source src = new StreamSource(new PrintingUtils().getClass()
//                .getResourceAsStream(XSL_CONTEXT_DIR + "/CorresReportAgenda.xsl"));
//        List<Source> sourceList = new ArrayList<Source>();
//        sourceList.add(src);
//        return sourceList;
    }


    public void setProtocolCorrespondenceTemplateService(ProtocolCorrespondenceTemplateService protocolCorrespondenceTemplateService) {
        this.protocolCorrespondenceTemplateService = protocolCorrespondenceTemplateService;
    }
}
