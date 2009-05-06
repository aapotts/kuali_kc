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
package org.kuali.kra.irb.actions;

import java.io.Serializable;

import org.kuali.kra.irb.actions.submit.ProtocolSubmitActionBean;
import org.kuali.kra.irb.web.struts.form.ProtocolForm;

/**
 * The form helper class for the Protocol Actions tab.
 */
public class ActionHelper implements Serializable {

    /**
     * Each Helper must contain a reference to its document form
     * so that it can access the document.
     */
    private ProtocolForm form;
    
    private ProtocolSubmitActionBean protocolSubmitAction;
   
    /**
     * Constructs an ActionHelper.
     * @param form the protocol form
     */
    public ActionHelper(ProtocolForm form) {
        this.form = form;
        protocolSubmitAction = new ProtocolSubmitActionBean(this);
    }
    
    public void prepareView() {
        protocolSubmitAction.prepareView();
    }
    
    public ProtocolSubmitActionBean getProtocolSubmitAction() {
        return protocolSubmitAction;
    }

    public ProtocolForm getProtocolForm() {
        return form;
    }
}
