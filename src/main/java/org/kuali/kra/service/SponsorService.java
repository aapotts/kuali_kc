/*
 * Copyright 2006-2008 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.osedu.org/licenses/ECL-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kra.service;

import java.util.Collection;

import org.kuali.kra.web.struts.form.SponsorHierarchyForm;

public interface SponsorService {
    /**
     * This method returns the sponsor name for a given sponsor code.
     * @param sponsorCode identifier for the sponsor
     * @return The name of the sponsor identified by this code.
     */
    public String getSponsorName(String sponsorCode);
    
    /**
     * 
     * This method to get the list of unique sponsorhierarchy name.
     * @return
     */
    public String getTopSponsorHierarchy();
    
    /**
     * 
     * This method is to retrieve next level nodes.  It is called by ajax.
     * @param node
     * @return
     */
    public String getSubSponsorHierarchiesForTreeView(String hierarchyName, String depth, String groups) ;

    /**
     * 
     * This method save the new hierarchy that is coming from copy action
     * @param sponsorHierarchyForm
     */
    public void copySponsorHierarchy(SponsorHierarchyForm sponsorHierarchyForm);
    
    /**
     * 
     * This method is to delete the selected hierarchy
     * @param sponsorHierarchyForm
     */
    public void deleteSponsorHierarchy(SponsorHierarchyForm sponsorHierarchyForm);
    
    public Collection getTopSponsorHierarchyList();
    public void changeGroupName(String hierarchyName, String depth, String oldGroupName, String groups);
    public void changeSortId(String hierarchyName, String depth, String groups, boolean moveUp);
    public void deleteSponsorHierarchyDwr(String hierarchyName, String depth, String nodeName, String groups, boolean isDeleteSponsor);
    public void addSponsorHierarchyDwr(String hierarchyName, String sponsors, String ascendants);
    public String loadToSponsorHierachyMt(String hierarchyName, String timestampKey);
    public void saveSponsorHierachy(String hierarchyName, String timestampKey);
    public Collection getOldSponsorHierarchyMt();
    /**
     * 
     * This method to clean up the sponsorhierarchtmt table.
     * Can be implemented by adding a link in maintenance tab.
     * Or when user cancel.  Only delete more than one day old & same user.
     * Or use batch.
     */
    public void cleanSponsorHierarchyMt();
    
    public String checkSubGroup(String hierarchyName, String depth, String groups);
}
