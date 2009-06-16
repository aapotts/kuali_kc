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
package org.kuali.kra.irb.actions.amendrenew;

import org.kuali.kra.irb.Protocol;
import org.kuali.kra.irb.ProtocolDocument;
import org.kuali.kra.irb.actions.copy.ProtocolCopyService;

public class ProtocolAmendRenewServiceImpl implements ProtocolAmendRenewService {

    private static final String AMEND_RENEW_NEXT_VALUE = "amendRenew";
    
    private ProtocolCopyService protocolCopyService;
    
    public void setProtocolCopyService(ProtocolCopyService protocolCopyService) {
        this.protocolCopyService = protocolCopyService;
    }
    
    public String createAmendment(ProtocolDocument protocolDocument, ProtocolAmendmentBean amendmentBean) throws Exception {
        String docNbr = protocolCopyService.copyProtocol(protocolDocument, generateProtocolAmendmentNumber(protocolDocument));
        
        return docNbr;
    }

    private String generateProtocolAmendmentNumber(ProtocolDocument protocolDocument) {
        String protocolNumber = protocolDocument.getProtocol().getProtocolNumber();
        Integer nextValue = protocolDocument.getDocumentNextValue(AMEND_RENEW_NEXT_VALUE);
        String s = nextValue.toString();
        int length = s.length();
        for (int i = 0; i < 3 - length; i++) {
            s = "0" + s;
        }
        return protocolNumber + "A" + s;
    }
}
