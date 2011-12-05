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
package org.kuali.kra.negotiations.web.struts.form;

import static org.kuali.rice.kns.util.KNSConstants.EMPTY_STRING;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.authorization.KraAuthorizationConstants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.medusa.MedusaBean;
import org.kuali.kra.negotiations.bo.Negotiation;

import org.kuali.kra.negotiations.bo.NegotiationActivity;
import org.kuali.kra.negotiations.bo.NegotiationActivityHistoryLineBean;
import org.kuali.kra.negotiations.bo.NegotiationAssociatedDetailBean;
import org.kuali.kra.negotiations.bo.NegotiationAssociationType;
import org.kuali.kra.negotiations.bo.NegotiationStatus;
import org.kuali.kra.negotiations.bo.NegotiationUnassociatedDetail;
import org.kuali.kra.negotiations.customdata.CustomDataHelper;
import org.kuali.kra.negotiations.customdata.NegotiationCustomData;
import org.kuali.kra.negotiations.document.NegotiationDocument;
import org.kuali.kra.negotiations.notifications.NegotiationNotificationHelper;
import org.kuali.kra.negotiations.service.NegotiationService;
import org.kuali.kra.proposaldevelopment.bo.ProposalPerson;
import org.kuali.kra.proposaldevelopment.bo.ProposalState;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.service.TaskAuthorizationService;
import org.kuali.kra.web.struts.form.KraTransactionalDocumentFormBase;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.util.KNSConstants;
import org.kuali.rice.kns.web.ui.HeaderField;
import org.kuali.rice.kns.workflow.service.KualiWorkflowDocument;

/**
 * 
 * This class holds all the objects required for a negotiation web object.
 */
public class NegotiationForm extends KraTransactionalDocumentFormBase {
    
    private static final org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(NegotiationForm.class);
    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -3641922366447898075L;
    private final String filterAllActivities = "All";
    private final String filterPendingActivities = "Pending";

    
    private List<NegotiationUnassociatedDetail> negotiationUnassociatedDetailsToDelete;
    private NegotiationActivityHelper negotiationActivityHelper;
    private NegotiationAssociatedDetailBean negotiationAssociatedDetailBean;
    private CustomDataHelper negotiationCustomDataFormHelper;
    private CustomDataHelper customDataHelper = new CustomDataHelper(this);
    private NegotiationNotificationHelper notificationHelper;
    private String filterActivities;
    
    private MedusaBean medusaBean;
    
    /**
     * 
     * Constructs a NegotiationForm.java.
     */
    public NegotiationForm() {
        super();
        negotiationUnassociatedDetailsToDelete = new ArrayList<NegotiationUnassociatedDetail>();
        negotiationActivityHelper = new NegotiationActivityHelper(this);
        medusaBean = new MedusaBean();
        negotiationCustomDataFormHelper = new CustomDataHelper(this);
        notificationHelper = new NegotiationNotificationHelper(this);
        filterActivities = "All";
        init();
    }
    
    private void init()
    {
    NegotiationForm negotiationForm = (NegotiationForm) this;
    List<NegotiationCustomData> negotiationCustomDataList = negotiationForm.getNegotiationDocument().getNegotiation().getNegotiationCustomDataList();
    negotiationForm.getCustomDataHelper().buildCustomDataCollectionsOnFormNewNegotiations(
            (SortedMap<String, List>) negotiationForm.getCustomDataHelper().getCustomAttributeGroups(), negotiationForm,
            negotiationForm.getNegotiationDocument().getCustomAttributeDocuments());
    }
    
    /**
     * @see org.kuali.kra.common.customattributes.CustomDataForm#getCustomDataHelper()
     */
    public CustomDataHelper getCustomDataHelper() {
        return customDataHelper;
    }

    /**
     * This method sets the custom data helper
     * 
     * @param customDataHelper
     */
    public void setCustomDataHelper(CustomDataHelper customDataHelper) {
        this.customDataHelper = customDataHelper;
    }

    
    /**
     * This method returns a string representation of the document type
     * 
     * @return
     */
    public String getDocumentTypeName() {
        return "NegotiationDocument";
    }

    
    public NegotiationDocument getNegotiationDocument() {
        return this.getDocument();
    }
    
    @Override
    public NegotiationDocument getDocument() {
        return (NegotiationDocument) super.getDocument();
    }

    public List<NegotiationUnassociatedDetail> getNegotiationUnassociatedDetailsToDelete() {
        return negotiationUnassociatedDetailsToDelete;
    }

    @Override
    protected String getDefaultDocumentTypeName() {
        return "NegotiationDocument";
    }

    @Override
    protected String getLockRegion() {
        return KraAuthorizationConstants.LOCK_NEGOTIATION;
    }

    @Override
    protected void setSaveDocumentControl(Map editMode) {
        getDocumentActions().put(KNSConstants.KUALI_ACTION_CAN_SAVE, KNSConstants.KUALI_DEFAULT_TRUE_VALUE);

    }

    private TaskAuthorizationService getTaskAuthorizationService() {
        return KraServiceLocator.getService(TaskAuthorizationService.class);
    }
    
    private Map convertSetToMap(Set s){
        Map map = new HashMap();
        Iterator i = s.iterator();
        while(i.hasNext()) {
            Object key = i.next();
           map.put(key,KNSConstants.KUALI_DEFAULT_TRUE_VALUE);
        }
        return map;
    }
    
    public BusinessObjectService getBusinessObjectService() {
        return KraServiceLocator.getService(BusinessObjectService.class);
    }
    
    public NegotiationService getNegotiationService() {
        return KraServiceLocator.getService(NegotiationService.class);
    }
    
    private boolean isAssocitationType(String typeCode) {
        if (this.getNegotiationDocument().getNegotiation().getNegotiationAssociationType() != null) {
            return StringUtils.equalsIgnoreCase(typeCode, this.getNegotiationDocument().getNegotiation().getNegotiationAssociationType().getCode());
        }
        return false;
    }
    
    public boolean getDisplayUnAssociatedDetail() {
        return isAssocitationType(NegotiationAssociationType.NONE_ASSOCIATION);
    }
    
    public boolean getDisplayProposalLog() {
        return isAssocitationType(NegotiationAssociationType.PROPOSAL_LOG_ASSOCIATION);
    }
    
    public boolean getDisplayInstitutionalProposal() {
        return isAssocitationType(NegotiationAssociationType.INSTITUATIONAL_PROPOSAL_ASSOCIATION);
    }
    
    public boolean getDisplayAward() {
        return isAssocitationType(NegotiationAssociationType.AWARD_ASSOCIATION);
    }
    
    public boolean getDisplaySubAward() {
        return isAssocitationType(NegotiationAssociationType.SUB_AWARD_ASSOCIATION);
    }

    public NegotiationActivityHelper getNegotiationActivityHelper() {
        return negotiationActivityHelper;
    }

    public void setNegotiationActivityHelper(NegotiationActivityHelper negotiationActivityHelper) {
        this.negotiationActivityHelper = negotiationActivityHelper;
    }
    
    public boolean getDispayAssociatedDetailPanel() { 
        return !getDisplayUnAssociatedDetail() && StringUtils.isNotEmpty(this.getNegotiationDocument().getNegotiation().getAssociatedDocumentId());
    }
    
    
    @Override
    public void populateHeaderFields(KualiWorkflowDocument workflowDocument) {
        super.populateHeaderFields(workflowDocument);
        NegotiationDocument nd = getDocument();
        final String ATTRIB_NEG_ID = "DataDictionary.Negotiation.attributes.negotiationId";
        final String ATTRIB_NEG_USER_NAME = "DataDictionary.Negotiation.attributes.negotiatorUserName";
        
        if (nd == null || nd.getNegotiation() == null)
        {
            getDocInfo().add(2, new HeaderField(ATTRIB_NEG_ID, EMPTY_STRING));
            getDocInfo().add(2, new HeaderField(ATTRIB_NEG_USER_NAME, EMPTY_STRING));            
            return;
        }
        if (nd.getNegotiation().getNegotiationId() == null)
        {
             getDocInfo().add(2, new HeaderField(ATTRIB_NEG_ID, EMPTY_STRING));
        }
        else
        {             
            getDocInfo().add(2, new HeaderField(ATTRIB_NEG_ID, nd.getNegotiation().getNegotiationId().toString()));            
        }
        if (nd.getNegotiation().getNegotiatorUserName() == null)
        {
            getDocInfo().add(2, new HeaderField(ATTRIB_NEG_USER_NAME, EMPTY_STRING));
        }
        else
        {
            getDocInfo().add(2, new HeaderField(ATTRIB_NEG_USER_NAME, nd.getNegotiation().getNegotiatorUserName()));            
        }
        
    }

    
    /**
     * 
     * This method returns the NegotiationAssociatedDetailBean.  If it hasn't been set, it does so.
     * @return
     */
    public NegotiationAssociatedDetailBean getNegotiationAssociatedDetailBean() {
        Negotiation negotiation = getNegotiationDocument().getNegotiation();
        if (negotiationAssociatedDetailBean == null || !StringUtils.equals(negotiationAssociatedDetailBean.getAssociatedDocumentId(), negotiation.getAssociatedDocumentId())) {
            this.negotiationAssociatedDetailBean = getNegotiationService().buildNegotiationAssociatedDetailBean(negotiation);
        }
        return negotiationAssociatedDetailBean;
    }
    
    /**
     * 
     * This method builds the javascript the disables and enables the ending date field based on the status field.
     * @return
     */
    public String getStatusRelatedJavascript() {
        StringBuffer sb = new StringBuffer(100);
        String newLine = "\n ";
        sb.append("function manageStatusEndDate(doUpdateDate){").append(newLine);
        sb.append("var statusField = document.getElementById('document.negotiationList[0].negotiationStatusId');").append(newLine);
        sb.append("var dateField = document.getElementById('document.negotiationList[0].negotiationEndDate');").append(newLine);
        sb.append("var statusFieldSelectedVal = statusField.options[statusField.selectedIndex].value;").append(newLine);
        
        sb.append("if (");
        int currentIndex = 0;
        List<String> completedCodes = this.getNegotiationService().getCompletedStatusCodes();
        for (String currentCode : completedCodes) {
            NegotiationStatus currentStatus = getNegotiationStatus(currentCode);
            sb.append("statusFieldSelectedVal == '").append(currentStatus.getId().toString()).append("'");
            if (currentIndex + 1 < completedCodes.size()) {
                sb.append(" || ");
            }
            currentIndex++;
        }
        sb.append(") {").append(newLine);
        
        sb.append("  dateField.disabled = false;").append(newLine);
        sb.append("  if (dateField.value == '' && doUpdateDate) {").append(newLine);
        sb.append("    var currentTime = new Date();").append(newLine);
        sb.append("    dateField.value = currentTime.getMonth() + 1 + \"/\" +  currentTime.getDate() + \"/\" + currentTime.getFullYear();").append(newLine);
        sb.append("  }").append(newLine).append("} else {").append(newLine);
        sb.append("  dateField.disabled = true;").append(newLine).append("  dateField.value = '';").append(newLine);
        sb.append("}").append(newLine).append("}").append(newLine);
        sb.append("manageStatusEndDate(false);");

        return sb.toString();
    }
    
    private NegotiationStatus getNegotiationStatus(String code) {
        return getNegotiationService().getNegotiationStatus(code);
    }

    public MedusaBean getMedusaBean() {
        return medusaBean;
    }

    public void setMedusaBean(MedusaBean medusaBean) {
        this.medusaBean = medusaBean;
    }

    public String getFilterActivities() {
        return filterActivities;
    }

    public void setFilterActivities(String filterActivities) {
        this.filterActivities = filterActivities;
    }
    
    /**
     * 
     * This method calls the negotiation service and return the results of hte getNegotiationActivityHistoryLineBeans funciton.
     * @return
     */
    public List<NegotiationActivityHistoryLineBean> getNegotiationActivityHistoryLineBeans() {
        return this.getNegotiationService().getNegotiationActivityHistoryLineBeans(this.getNegotiationDocument().getNegotiation().getActivities());
    }

    public NegotiationNotificationHelper getNotificationHelper() {
        return notificationHelper;
    }

    public void setNotificationHelper(NegotiationNotificationHelper notificationHelper) {
        this.notificationHelper = notificationHelper;
    }

    public String getFilterAllActivities() {
        return filterAllActivities;
    }

    public String getFilterPendingActivities() {
        return filterPendingActivities;
    }
}