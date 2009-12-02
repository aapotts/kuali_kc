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
package org.kuali.kra.irb.actions;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.irb.Protocol;
import org.kuali.kra.irb.ProtocolForm;
import org.kuali.kra.irb.actions.submit.ProtocolReviewer;
import org.kuali.kra.irb.actions.submit.ProtocolSubmission;
import org.kuali.kra.irb.actions.submit.ProtocolSubmissionStatus;
import org.kuali.kra.service.KcPersonService;
import org.kuali.rice.kns.lookup.keyvalues.KeyValuesBase;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.web.struts.form.KualiForm;
import org.kuali.rice.kns.web.ui.KeyLabelPair;

public class ProtocolReviewerValuesFinder extends KeyValuesBase {
    
    public List<KeyLabelPair> getKeyValues() {
        List<KeyLabelPair> keyValues = new ArrayList<KeyLabelPair>();
        keyValues.add(new KeyLabelPair("", "select"));
        
        Protocol protocol = getProtocol();
        if (protocol != null) {
            ProtocolSubmission submission = getCurrentSubmission(protocol);
            if (submission != null) {
                List<ProtocolReviewer> reviewers = submission.getProtocolReviewers();
                for (ProtocolReviewer reviewer : reviewers) {
                    keyValues.add(new KeyLabelPair(reviewer.getProtocolReviewerId().toString(), getPersonName(reviewer.getPersonId())));
                }
            }
        }
        
        return keyValues;
    }

    private String getPersonName(String personId) {
        return getKcPersonService().getKcPersonByPersonId(personId).getFullName();
    }

    private KcPersonService getKcPersonService() {
        return KraServiceLocator.getService(KcPersonService.class);
    }

    private ProtocolSubmission getCurrentSubmission(Protocol protocol) {
        for (ProtocolSubmission submission : protocol.getProtocolSubmissions()) {
            if (StringUtils.equals(submission.getSubmissionStatusCode(), ProtocolSubmissionStatus.IN_AGENDA) ||
                StringUtils.equals(submission.getSubmissionStatusCode(), ProtocolSubmissionStatus.SUBMITTED_TO_COMMITTEE)) {
                return submission;
            }
        }
        return null;
    }

    private Protocol getProtocol() {
        KualiForm form = GlobalVariables.getKualiForm();
        if (form != null && form instanceof ProtocolForm) {
            return ((ProtocolForm) form).getProtocolDocument().getProtocol();
        }
        return null;
    }
}
