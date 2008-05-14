/*
 * Copyright 2008 The Kuali Foundation.
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
package org.kuali.kra.web.struts.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.kuali.core.web.struts.form.KualiForm;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.service.UnitService;

public class UnitHierarchyForm  extends KualiForm {

    private String units;
    private String selectedUnitNumber;
    
    /**
     * Constructs a UnitHierarchyForm.
     */
    public UnitHierarchyForm() {
        super();
        units = KraServiceLocator.getService(UnitService.class).getInitialUnitsForUnitHierarchy();        

    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        // FIXME : just a temporary soln.  it always get the methodtocall='refresh' after it started properly the first time.  
        // need to investigate this.
        this.setMethodToCall("");
    }
    
    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getSelectedUnitNumber() {
        return selectedUnitNumber;
    }

    public void setSelectedUnitNumber(String selectedUnitNumber) {
        this.selectedUnitNumber = selectedUnitNumber;
    }

}
