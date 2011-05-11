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
package org.kuali.kra.irb.noteattachment;

import java.io.Serializable;
import java.util.Comparator;

/**
 * 
 * This class is a business object which represents the 
 * Protocol Attachment Filter.  This filter is used to limit
 * and sort protocol attachments.
 */
public class ProtocolAttachmentFilter implements Serializable {

    private static final long serialVersionUID = 53138457226971783L;
    
    private String filterBy;
    private String sortBy;
    
    public String getFilterBy() {
        return filterBy;
    }
    public void setFilterBy(String filterBy) {
        this.filterBy = filterBy;
    }
    public String getSortBy() {
        return sortBy;
    }
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
    
    /**
     * 
     * This method returns a comparator used to sort protocol attachments
     * @return a comparator used to sort protocol attachments
     */
    public Comparator<ProtocolAttachmentProtocol> getProtocolAttachmentComparator() {
        return new ProtocolAttachmentComparatorFactory().getProtocolAttachmentComparator(getSortBy());
    }
    
    


class ProtocolAttachmentComparatorFactory {
    public Comparator<ProtocolAttachmentProtocol> getProtocolAttachmentComparator(String sortBy) {
        if ("DESC".equalsIgnoreCase(sortBy)) {
            return new ProtocolAttachmentDescriptionComparator();
        } else if ("STAT".equalsIgnoreCase(sortBy)) {
            return new ProtocolAttachmentStatusComparator();
        } else if ("LAUP".equalsIgnoreCase(sortBy)) {
            return new ProtocolAttachmentLastUpdatedComparator();
        } else if ("UPBY".equalsIgnoreCase(sortBy)) {
            return new ProtocolAttachmentLastUpdatedByComparator();
        } else {
            return null;
        }
    }
}

    private class ProtocolAttachmentDescriptionComparator implements Comparator<ProtocolAttachmentProtocol>
    {
    
        public int compare(ProtocolAttachmentProtocol arg0, ProtocolAttachmentProtocol arg1) {
            return arg0.getDescription().compareTo(arg1.getDescription());
        }
        
    }
    
    private class ProtocolAttachmentStatusComparator implements Comparator<ProtocolAttachmentProtocol>
    {
    
        public int compare(ProtocolAttachmentProtocol o1, ProtocolAttachmentProtocol o2) {
            return o1.getStatusCode().compareTo(o2.getStatusCode());
        }
        
    }
    
    private class ProtocolAttachmentLastUpdatedComparator implements Comparator<ProtocolAttachmentProtocol>
    {
    
        public int compare(ProtocolAttachmentProtocol o1, ProtocolAttachmentProtocol o2) {
            return o1.getUpdateTimestamp().compareTo(o2.getUpdateTimestamp());
        }
        
    }
    
    private class ProtocolAttachmentLastUpdatedByComparator implements Comparator<ProtocolAttachmentProtocol>
    {
    
        public int compare(ProtocolAttachmentProtocol o1, ProtocolAttachmentProtocol o2) {
            return o1.getUpdateUserFullName().compareTo(o2.getUpdateUserFullName());
        }
        
    }

}
