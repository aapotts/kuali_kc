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
package org.kuali.kra.irb.noteattachment;


/**
 * Implementation of {@link AddProtocolAttachmentPersonnelRule AddProtocolAttachmentPersonnelRule}.
 * @see AddProtocolAttachmentPersonnelRule for details
 */
class AddProtocolAttachmentPersonnelRuleImpl implements AddProtocolAttachmentPersonnelRule {

    private static final String PROPERTY_PREFIX = "notesAndAttachmentsHelper.newAttachmentPersonnel";
    
    private final ProtocolAttachmentBaseRuleHelper baseHelper = new ProtocolAttachmentBaseRuleHelper(PROPERTY_PREFIX);
    private final ProtocolAttachmentPersonnelRuleHelper personnelHelper = new ProtocolAttachmentPersonnelRuleHelper(PROPERTY_PREFIX);

    /** {@inheritDoc} */
    public boolean processAddProtocolAttachmentPersonnelRules(AddProtocolAttachmentPersonnelEvent event) {      
        
        final ProtocolAttachmentPersonnel newAttachmentPersonnel = event.getNewAttachmentPersonnel();
        
        boolean valid = this.baseHelper.validType(newAttachmentPersonnel);
        valid &= this.personnelHelper.validPerson(newAttachmentPersonnel);
        valid &= this.baseHelper.validFile(newAttachmentPersonnel);
        valid &= this.baseHelper.validDescription(newAttachmentPersonnel);
        
        return valid;
    }
}
