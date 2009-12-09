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
package org.kuali.kra.irb.actions.reopen;

import org.kuali.kra.irb.Protocol;
import org.kuali.kra.irb.actions.ProtocolGenericActionBean;

/**
 * The Protocol Reopen Service is used re-open a protocol for enrollment.
 */
public interface ProtocolReopenService {

    /**
     * Reopen a protocol for enrollment.
     * @param protocol
     * @param actionBean
     * @throws Exception
     */
    public void reopen(Protocol protocol, ProtocolGenericActionBean actionBean) throws Exception;
}
