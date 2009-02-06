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
package org.kuali.kra.irb.rule.event;

import org.kuali.core.rule.event.KualiDocumentEvent;
import org.kuali.kra.irb.bo.ProtocolPerson;
import org.kuali.kra.irb.document.ProtocolDocument;

/**
 * Event triggered when a protocol person state is modified on a 
 * <code>{@link ProtocolDocument}</code>
 *
 */
public interface ProtocolPersonnelEvent extends KualiDocumentEvent {
    /**
     * @return <code>{@link ProtocolPerson}</code> that triggered this event.
     */
    public ProtocolPerson getProtocolPerson();
}
