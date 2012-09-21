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
package org.kuali.kra.iacuc.committee.rules;

import org.kuali.kra.common.committee.bo.Committee;
import org.kuali.kra.common.committee.document.CommonCommitteeDocument;
import org.kuali.kra.common.committee.lookup.keyvalue.CommitteeIdValuesFinder;
import org.kuali.kra.common.committee.rules.CommitteeDocumentRule;
import org.kuali.kra.iacuc.committee.bo.IacucCommittee;
import org.kuali.kra.iacuc.committee.document.IacucCommitteeDocument;

public class IacucCommitteeDocumentRule extends CommitteeDocumentRule {

    @Override
    protected Class<? extends Committee> getCommonCommitteeBOClassHook() {
        return IacucCommittee.class;
    }

    @Override
    protected CommitteeIdValuesFinder getNewCommitteeIdValuesFinderInstanceHook() {
        // creating anonymous classes in order to avoid inheritance issues
        return new CommitteeIdValuesFinder() {

            /**
             * Comment for <code>serialVersionUID</code>
             */
            private static final long serialVersionUID = 7790195024569716075L;

            @Override
            protected Class<? extends Committee> getCommonCommitteeBOClassHook() {
                return IacucCommittee.class;
            }
            
        };
        
    }

    @Override
    protected Class<? extends CommonCommitteeDocument> getCommonCommitteeDocumentBOClassHook() {
        return IacucCommitteeDocument.class;
    }

}
