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

package org.kuali.kra.irb;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.util.StringUtils;
import org.kuali.kra.bo.DocumentNextvalue;
import org.kuali.kra.bo.RolePersons;
import org.kuali.kra.document.ResearchDocumentBase;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.irb.actions.ProtocolStatus;
import org.kuali.kra.irb.auth.ProtocolAuthorizationService;
import org.kuali.kra.service.VersioningService;
import org.kuali.rice.kew.dto.DocumentRouteStatusChangeDTO;
import org.kuali.rice.kns.document.Copyable;
import org.kuali.rice.kns.document.SessionDocument;
import org.kuali.rice.kns.service.DocumentService;

/**
 * 
 * This class represents the Protocol Document Object.
 * ProtocolDocument has a 1:1 relationship with Protocol Business Object.
 * We have declared a list of Protocol BOs in the ProtocolDocument at the same time to
 * get around the OJB anonymous keys issue of primary keys of different data types.
 * Also we have provided convenient getter and setter methods so that to the outside world;
 * Protocol and ProtocolDocument can have a 1:1 relationship.
 */
public class ProtocolDocument extends ResearchDocumentBase implements Copyable, SessionDocument { 
	
    private static final String DOCUMENT_TYPE_CODE = "PROT";
    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 803158468103165087L;
    private List<Protocol> protocolList;
	
    /**
     * Constructs a ProtocolDocument object
     */
	public ProtocolDocument() { 
        super();
        protocolList = new ArrayList<Protocol>();
        Protocol newProtocol = new Protocol();
        newProtocol.setProtocolDocument(this);
        protocolList.add(newProtocol);
	} 
	
    public void initialize() {
    }

    
    /**
     * 
     * This method is a convenience method for facilitating a 1:1 relationship between ProtocolDocument 
     * and Protocol to the outside world - aka a single Protocol field associated with ProtocolDocument
     * @return
     */
    public Protocol getProtocol() {
        return protocolList.get(0);
    }

    /**
     * 
     * This method is a convenience method for facilitating a 1:1 relationship between ProtocolDocument 
     * and Protocol to the outside world - aka a single Protocol field associated with ProtocolDocument
     * @param protocol
     */
    public void setProtocol(Protocol protocol) {
        protocolList.set(0, protocol);
    }


    /**
     * 
     * This method is used by OJB to get around with anonymous keys issue.
     * Warning : Developers should never use this method.
     * @return List<Protocol>
     */
    public List<Protocol> getProtocolList() {
        return protocolList;
    }

    /**
     * 
     * This method is used by OJB to get around with anonymous keys issue.
     * Warning : Developers should never use this method
     * @param protocolList
     */
    public void setProtocolList(List<Protocol> protocolList) {
        this.protocolList = protocolList;
    }
    
    /**
     * @see org.kuali.core.bo.PersistableBusinessObjectBase#buildListOfDeletionAwareLists()
     */
    @SuppressWarnings("unchecked")
    @Override
    public List buildListOfDeletionAwareLists() {
        List managedLists = super.buildListOfDeletionAwareLists();
        managedLists.addAll(getProtocol().buildListOfDeletionAwareLists());
        managedLists.add(protocolList);
        return managedLists;
    }
    
    /**
     * @see org.kuali.kra.document.ResearchDocumentBase#getAllRolePersons()
     */
    @Override
    protected List<RolePersons> getAllRolePersons() {
        ProtocolAuthorizationService protocolAuthService = 
               (ProtocolAuthorizationService) KraServiceLocator.getService(ProtocolAuthorizationService.class); 
        return protocolAuthService.getAllRolePersons(getProtocol());
    }
    
    public String getDocumentTypeCode() {
        return DOCUMENT_TYPE_CODE;
    }
    
    public void doRouteStatusChange(DocumentRouteStatusChangeDTO statusChangeEvent) throws Exception {
        if (isFinal(statusChangeEvent)) {
            if (isAmendment()) {
                mergeAmendment(ProtocolStatus.AMENDMENT_MERGED);
            }
            else if (isRenewal()) {
                mergeAmendment(ProtocolStatus.RENEWAL_MERGED);
            }
        }
    }
    
    private void mergeAmendment(String protocolStatusCode) throws Exception {
        ProtocolFinderDao protocolFinder = KraServiceLocator.getService(ProtocolFinderDao.class);
        Protocol currentProtocol = protocolFinder.findCurrentProtocolByNumber(getOriginalProtocolNumber());
        ProtocolDocument newProtocolDocument = createVersion(currentProtocol.getProtocolDocument());
        newProtocolDocument.getProtocol().merge(getProtocol());
        getProtocol().setProtocolStatusCode(protocolStatusCode);
        
        DocumentService documentService = KraServiceLocator.getService(DocumentService.class);
        documentService.saveDocument(this);
        documentService.saveDocument(newProtocolDocument);
    }
    
    private String getOriginalProtocolNumber() {
        return getProtocol().getProtocolNumber().substring(0, 10);
    }

    private boolean isFinal(DocumentRouteStatusChangeDTO statusChangeEvent) {
        return StringUtils.equals("F", statusChangeEvent.getNewRouteStatus());
    }

    private boolean isRenewal() {
        return getProtocol().getProtocolNumber().contains("R");
    }

    public boolean isAmendment() {
        return getProtocol().getProtocolNumber().contains("A");
    }

    private ProtocolDocument createVersion(ProtocolDocument oldDoc) throws Exception {
        VersioningService versioningService = KraServiceLocator.getService(VersioningService.class);
        Protocol newVersion = versioningService.createNewVersion(oldDoc.getProtocol());
        
        DocumentService documentService = KraServiceLocator.getService(DocumentService.class);
        ProtocolDocument protocolDocument = (ProtocolDocument) documentService.getNewDocument(ProtocolDocument.class);
        protocolDocument.getDocumentHeader().setDocumentDescription(oldDoc.getDocumentHeader().getDocumentDescription());
        protocolDocument.setDocumentNextvalues(new ArrayList<DocumentNextvalue>());
        protocolDocument.setProtocol(newVersion);
            
        documentService.saveDocument(protocolDocument);
        
        return protocolDocument;
    }
}