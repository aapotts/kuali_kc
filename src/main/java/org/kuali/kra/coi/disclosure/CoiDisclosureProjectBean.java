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
package org.kuali.kra.coi.disclosure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.kuali.kra.bo.KraPersistableBusinessObjectBase;
import org.kuali.kra.coi.CoiDiscDetail;

public class CoiDisclosureProjectBean implements Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -100427824220789523L;
    // TODO : should create interface "CoiDisclosureable" for these project. It is to close to 4.0 release
    // so wait after 4.0
    private KraPersistableBusinessObjectBase disclosureProject;
    private List<CoiDiscDetail> projectDiscDetails;

    public CoiDisclosureProjectBean() {
        projectDiscDetails = new ArrayList<CoiDiscDetail> ();
    }
    

    public KraPersistableBusinessObjectBase getDisclosureProject() {
        return disclosureProject;
    }

    public void setDisclosureProject(KraPersistableBusinessObjectBase disclosureProject) {
        this.disclosureProject = disclosureProject;
    }

    public List<CoiDiscDetail> getProjectDiscDetails() {
        return projectDiscDetails;
    }

    public void setProjectDiscDetails(List<CoiDiscDetail> projectDiscDetails) {
        this.projectDiscDetails = projectDiscDetails;
    }

}
