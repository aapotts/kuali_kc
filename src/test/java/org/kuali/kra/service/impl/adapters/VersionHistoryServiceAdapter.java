package org.kuali.kra.service.impl.adapters;

import org.kuali.kra.SequenceOwner;
import org.kuali.kra.bo.versioning.VersionHistory;
import org.kuali.kra.bo.versioning.VersionStatus;
import org.kuali.kra.service.VersionHistoryService;
import org.kuali.rice.kns.service.BusinessObjectService;

import java.util.List;

/**
 * Adapter for VersionHistoryService
 */
public class VersionHistoryServiceAdapter implements VersionHistoryService {
    BusinessObjectService bos;

    public VersionHistory createVersionHistory(SequenceOwner<? extends SequenceOwner<?>> sequenceOwner, VersionStatus versionStatus, String userId) {
        return null;
    }

    public VersionHistory findActiveVersion(Class<? extends SequenceOwner> klass, String versionName) {
        return null;
    }

    public List<VersionHistory> loadVersionHistory(Class<? extends SequenceOwner> klass, String versionName) {
        return null;
    }

    public void setBusinessObjectService(BusinessObjectService bos) {
        this.bos = bos;
    }

    public BusinessObjectService getBusinessObjectService() { 
        return bos;
    }   
    
}
