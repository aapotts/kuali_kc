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
package org.kuali.kra.institutionalproposal.proposallog;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.maintenance.KraMaintainableImpl;
import org.kuali.kra.service.KcPersonService;
import org.kuali.rice.kim.util.KimConstants;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.maintenance.Maintainable;
import org.kuali.rice.kns.service.DateTimeService;
import org.kuali.rice.kns.util.GlobalVariables;

public class ProposalLogMaintainableImpl extends KraMaintainableImpl implements Maintainable {

    private static final long serialVersionUID = 4690638717398206040L;
    private static final String KIM_PERSON_LOOKUPABLE_REFRESH_CALLER = "kimPersonLookupable";
    
    private static final int FISCAL_YEAR_OFFSET = 6;
    
    private transient DateTimeService dateTimeService;
    private transient KcPersonService kcPersonService;
    
    /**
     * @see org.kuali.rice.kns.maintenance.Maintainable#refresh(String refreshCaller, Map fieldValues, MaintenanceDocument document)
     */
    @Override
    @SuppressWarnings("unchecked")
    public void refresh(String refreshCaller, Map fieldValues, MaintenanceDocument document) {
        super.refresh(refreshCaller, fieldValues, document);
        
        // If a person has been selected, lead unit should default to the person's home unit.
        if (KIM_PERSON_LOOKUPABLE_REFRESH_CALLER.equals(refreshCaller)) {
            ProposalLog proposalLog = (ProposalLog) this.getBusinessObject();
            String principalId = (String) fieldValues.get(KimConstants.PrimaryKeyConstants.PRINCIPAL_ID);
            proposalLog.setPiId(principalId);
            if (proposalLog.getPerson() != null) {
                proposalLog.setLeadUnit(proposalLog.getPerson().getOrganizationIdentifier());
            }
        }
    }
    
    @Override
    public void processAfterNew(MaintenanceDocument document, Map<String,String[]> parameters) {
        ProposalLog proposalLog = (ProposalLog) document.getDocumentBusinessObject();
        if (StringUtils.isBlank(proposalLog.getProposalNumber())) {
            // New record; set default values.
            setupDefaultValues(proposalLog);
        }
    }
    
    @Override
    public void processAfterCopy(MaintenanceDocument document, Map<String,String[]> parameters) {
        ProposalLog proposalLog = (ProposalLog) document.getDocumentBusinessObject();
        proposalLog.setCreateTimestamp(null);
        proposalLog.setCreateUser(null);
        proposalLog.setUpdateTimestamp(null);
        proposalLog.setUpdateUser(null);
        proposalLog.setFiscalMonth(null);
        proposalLog.setFiscalYear(null);
        proposalLog.setLogStatus(ProposalLogUtils.getProposalLogPendingStatusCode());
        proposalLog.setProposalLogTypeCode(ProposalLogUtils.getProposalLogPermanentTypeCode());
    }
    
    /**
     * @see org.kuali.rice.kns.maintenance.Maintainable#prepareForSave()
     */
    @Override
    public void prepareForSave() {
        super.prepareForSave();
        // If this is the initial save, we need to set the fiscal year and month.
        ProposalLog proposalLog = (ProposalLog) this.getBusinessObject();
        Timestamp currentTimestamp = getDateTimeService().getCurrentTimestamp();
        if (StringUtils.isBlank(proposalLog.getProposalNumber())) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, FISCAL_YEAR_OFFSET);
            proposalLog.setFiscalMonth(calendar.get(Calendar.MONTH) + 1);
            proposalLog.setFiscalYear(calendar.get(Calendar.YEAR));
            proposalLog.setCreateTimestamp(currentTimestamp);
            proposalLog.setCreateUser(GlobalVariables.getUserSession().getPrincipalName());
        }
        // We need to set this here so it's in the stored XML
        proposalLog.setUpdateTimestamp(getDateTimeService().getCurrentTimestamp());
    }
    
    private void setupDefaultValues(ProposalLog proposalLog) {
        if (StringUtils.isBlank(proposalLog.getLogStatus())) {
            proposalLog.setLogStatus(ProposalLogUtils.getProposalLogPendingStatusCode());
        }
        if (StringUtils.isBlank(proposalLog.getProposalLogTypeCode())) {
            proposalLog.setProposalLogTypeCode(ProposalLogUtils.getProposalLogPermanentTypeCode());
        }
    }
    
    private KcPersonService getKcPersonService() {
        if (this.kcPersonService == null) {
            kcPersonService = KraServiceLocator.getService(KcPersonService.class);
        }
        return this.kcPersonService;
    }
    
    private DateTimeService getDateTimeService() {
        if (this.dateTimeService == null) {
            dateTimeService = KraServiceLocator.getService(DateTimeService.class);
        }
        return this.dateTimeService;
    }

}
