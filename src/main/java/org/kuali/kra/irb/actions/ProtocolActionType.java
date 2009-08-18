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

import java.util.LinkedHashMap;

import org.kuali.kra.bo.KraPersistableBusinessObjectBase;

/**
 * A Protocol Action Type refers to the type of actions that an
 * that can be performed against a Protocol document.
 */
@SuppressWarnings("serial")
public class ProtocolActionType extends KraPersistableBusinessObjectBase {
    
    public static final String SUBMIT_TO_IRB = "101";
    public static final String WITHDRAWN = "303";
    public static final String REQUEST_TO_CLOSE = "105";
    public static final String REQUEST_FOR_SUSPENSION = "106";
    public static final String REQUEST_TO_CLOSE_ENROLLMENT = "108";
    public static final String REQUEST_TO_REOPEN_ENROLLMENT = "115";
    public static final String REQUEST_FOR_DATA_ANALYSIS_ONLY = "114";
    public static final String NOTIFY_IRB = "116";
    public static final String AMENDMENT_CREATED = "103";
    public static final String RENEWAL_CREATED = "102";
    public static final String APPROVED = "204";
    public static final String DISAPPROVED = "304";
    
    private String protocolActionTypeCode;
    
    private String description;
    
    private boolean triggerSubmission;
    
    private boolean triggerCorrespondence;
    
    /**
     * Constructs a ProtocolActionType.
     */
    public ProtocolActionType() {
        
    }
    
    public void setProtocolActionTypeCode(String protocolActionTypeCode) {
        this.protocolActionTypeCode = protocolActionTypeCode;
    }

    public String getProtocolActionTypeCode() {
        return protocolActionTypeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setTriggerSubmission(boolean triggerSubmission) {
        this.triggerSubmission = triggerSubmission;
    }

    public boolean getTriggerSubmission() {
        return triggerSubmission;
    }

    public void setTriggerCorrespondence(boolean triggerCorrespondence) {
        this.triggerCorrespondence = triggerCorrespondence;
    }

    public boolean getTriggerCorrespondence() {
        return triggerCorrespondence;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected LinkedHashMap toStringMapper() {
        LinkedHashMap map = new LinkedHashMap();
        map.put("protocolActionTypeCode", getProtocolActionTypeCode());
        map.put("description", getDescription());
        map.put("triggerSubmission", getTriggerSubmission());
        map.put("triggerCorrespondence", getTriggerCorrespondence());
        return map;
    }   
}
