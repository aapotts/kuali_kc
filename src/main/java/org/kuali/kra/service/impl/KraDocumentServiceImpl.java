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
package org.kuali.kra.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.kra.committee.bo.Committee;
import org.kuali.kra.committee.bo.CommitteeMembership;
import org.kuali.kra.committee.document.CommitteeDocument;
import org.kuali.kra.committee.service.CommitteeService;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.rice.kew.exception.WorkflowException;
import org.kuali.rice.kns.bo.PersistableBusinessObject;
import org.kuali.rice.kns.document.Document;
import org.kuali.rice.kns.exception.ValidationException;
import org.kuali.rice.kns.rule.event.KualiDocumentEvent;
import org.kuali.rice.kns.rule.event.RouteDocumentEvent;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.service.impl.DocumentServiceImpl;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * 
 * This class is to override documentservice.  It is mainly for CommitteeDocument.
 */
public class KraDocumentServiceImpl extends DocumentServiceImpl {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(KraDocumentServiceImpl.class);

    @Override
    public void validateAndPersistDocument(Document document, KualiDocumentEvent event) throws WorkflowException,
            ValidationException {
        if (document == null) {
            LOG.error("document passed to validateAndPersist was null");
            throw new IllegalArgumentException("invalid (null) document");
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("validating and preparing to persist document " + document.getDocumentNumber());
        }

        document.validateBusinessRules(event);
        document.prepareForSave(event);

        // save the document
        try {
            if (LOG.isInfoEnabled()) {
                LOG.info("storing document " + document.getDocumentNumber());
            }

            if (document instanceof CommitteeDocument) {
                Committee committee = ((CommitteeDocument) document).getCommittee();
                ((CommitteeDocument) document).setCommitteeList(new ArrayList());
                getDocumentDao().save(document);
                ((CommitteeDocument) document).getCommitteeList().add(committee);
                if (event instanceof RouteDocumentEvent) {
                    /*
                     * since builddeleteawarelist is not working for committeedocument, so old doc must be removed manually.
                     * if we decide to 'version' committeedocument, then this is not needed.
                     */
                    Committee committee1 = KraServiceLocator.getService(CommitteeService.class).getCommitteeById(
                            committee.getCommitteeId());
                    if (committee1 != null) {
                        List<PersistableBusinessObject> bos = new ArrayList<PersistableBusinessObject>();
                        if (CollectionUtils.isNotEmpty(committee1.getCommitteeMemberships())) {
                            for (CommitteeMembership membership : committee1.getCommitteeMemberships()) {
                                bos.addAll(membership.getMembershipRoles());
                                bos.addAll(membership.getMembershipExpertise());
                            }
                        }
                        bos.add(committee1);
                        KraServiceLocator.getService(BusinessObjectService.class).delete(bos);
                    }
                    getDocumentDao().save(document);
                }
            }
            else {
                getDocumentDao().save(document);
            }
        }
        catch (OptimisticLockingFailureException e) {
            LOG.error("exception encountered on store of document " + e.getMessage());
            throw e;
        }

        document.postProcessSave(event);
    }

    //    @Override
    //    public void updateDocument(Document document) {
    //        // TODO Auto-generated method stub
    //       // super.updateDocument(document);
    //    }

}
