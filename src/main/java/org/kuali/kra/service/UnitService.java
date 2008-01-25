/*
 * Copyright 2007 The Kuali Foundation.
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
package org.kuali.kra.service;

import java.util.Collection;

import org.kuali.kra.bo.Unit;

public interface UnitService {
    /**
     * This method returns the Unit name for a given Unit Number.
     * @param unitNumber identifier for the unit
     * @return The name of the unit identified by this number.
     */
    public String getUnitName(String unitNumber);

    /**
     * 
     * This method is to get all units.
     * @return
     */
    public Collection<Unit> getUnits();
}
