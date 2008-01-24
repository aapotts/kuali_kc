/*
 * Copyright 2006-2007 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
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
package org.kuali.kra.proposaldevelopment.bo;

import java.util.List;

/**
 * Used to describe a <code>{@link BusinessObject}</code> with credit that can be split. Usually,
 * this is a <code>{@link ProposalPerson}</code> or the like.
 *
 * @author $Author: lprzybyl $
 * @version $Revision: 1.2 $
 */
public interface CreditSplitable {
    /**
     * Get a <code>{@link List}</code> of credit splits
     *
     * @return List<T>
     */ 
    public List<? extends CreditSplit> getCreditSplits();
    
    /**
     * Name for which to refer to this splitable by
     * 
     * @return String
     */
    public String getName();
}
