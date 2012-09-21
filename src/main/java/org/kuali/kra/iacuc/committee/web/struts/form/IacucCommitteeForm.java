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
package org.kuali.kra.iacuc.committee.web.struts.form;

import org.kuali.kra.common.committee.web.struts.form.CommitteeHelper;
import org.kuali.kra.common.committee.web.struts.form.CommonCommitteeForm;

public class IacucCommitteeForm extends CommonCommitteeForm {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 5623611578157741521L;

    @Override
    protected CommitteeHelper getNewCommitteeHelperInstanceHook(CommonCommitteeForm committeeForm) {
        return new IacucCommitteeHelper((IacucCommitteeForm)committeeForm);
    }

    @Override
    protected String getDefaultDocumentTypeName() {
        return "IacucCommitteeDocument";
    }

}
