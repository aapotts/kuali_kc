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
package org.kuali.kra.award.web.struts.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kuali.core.datadictionary.DocumentEntry;
import org.kuali.core.datadictionary.HeaderNavigation;
import org.kuali.core.document.authorization.DocumentActionFlags;
import org.kuali.core.service.DataDictionaryService;
import org.kuali.core.web.ui.KeyLabelPair;
import org.kuali.kra.authorization.KraAuthorizationConstants;
import org.kuali.kra.award.bo.AwardComment;
import org.kuali.kra.award.bo.AwardFandaRate;
import org.kuali.kra.award.bo.AwardReportTerm;
import org.kuali.kra.award.bo.AwardReportTermRecipient;
import org.kuali.kra.award.bo.AwardSpecialReview;
import org.kuali.kra.award.bo.AwardSpecialReviewExemption;
import org.kuali.kra.award.bo.ReportClass;
import org.kuali.kra.award.contacts.AwardCentralAdminContactsBean;
import org.kuali.kra.award.contacts.AwardProjectPersonnelBean;
import org.kuali.kra.award.contacts.AwardSponsorContactsBean;
import org.kuali.kra.award.contacts.AwardUnitContactsBean;
import org.kuali.kra.award.detailsdates.DetailsAndDatesFormHelper;
import org.kuali.kra.award.document.AwardDocument;
import org.kuali.kra.award.paymentreports.paymentschedule.PaymentScheduleBean;
import org.kuali.kra.award.paymentreports.specialapproval.approvedequipment.ApprovedEquipmentBean;
import org.kuali.kra.award.paymentreports.specialapproval.foreigntravel.ApprovedForeignTravelBean;
import org.kuali.kra.award.web.struts.action.SponsorTermFormHelper;
import org.kuali.kra.common.customattributes.CustomDataForm;
import org.kuali.kra.common.permissions.web.struts.form.PermissionsForm;
import org.kuali.kra.document.ResearchDocumentBase;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.web.struts.form.Auditable;
import org.kuali.kra.web.struts.form.KraTransactionalDocumentFormBase;
import org.kuali.kra.web.struts.form.MultiLookupFormBase;
import org.kuali.kra.web.struts.form.SpecialReviewFormBase;

import edu.iu.uis.eden.clientapp.IDocHandler;

/**
 * 
 * This class represents the Award Form Struts class.
 */
public class AwardForm extends KraTransactionalDocumentFormBase 
                                        implements MultiLookupFormBase,
                                                    SpecialReviewFormBase<AwardSpecialReviewExemption>,
                                                    Auditable,
                                                    CustomDataForm,
                                                    PermissionsForm {
    
    public static final String SAVE = "save";
    public static final String RELOAD = "reload";
    
    private static final long serialVersionUID = -7633960906991275328L;
    
    private String lookupResultsBOClassName;
    private String lookupResultsSequenceNumber;
    private int awardReportTermPanelNumber;
    private AwardSpecialReview newAwardSpecialReview;
    private List<AwardSpecialReviewExemption> newSpecialReviewExemptions;
    private String[] newExemptionTypeCodes;
    private AwardComment newAwardCostShareComment;
    
    private AwardFandaRate newAwardFandaRate;
    private List<AwardReportTerm> newAwardReportTerm;
    private List<AwardReportTermRecipient> newAwardReportTermRecipient;
    private List<KeyLabelPair> reportClasses;
    
    private ApprovedEquipmentBean approvedEquipmentBean;
    private AwardProjectPersonnelBean projectPersonnelBean;
    private AwardUnitContactsBean unitContactsBean;
    private AwardCentralAdminContactsBean centralAdminContactsBean;
    private AwardSponsorContactsBean sponsorContactsBean;
    private CostShareFormHelper costShareFormHelper;
    private SponsorTermFormHelper sponsorTermFormHelper;
    private ApprovedSubawardFormHelper approvedSubawardFormHelper;
    private DetailsAndDatesFormHelper detailsAndDatesFormHelper;
    private DirectFandADistributionFormHelper directFandADistributionFormHelper;
    
    private ReportClass reportClassForPaymentsAndInvoices;
    private PaymentScheduleBean paymentScheduleBean;
    private ApprovedForeignTravelBean approvedForeignTravelBean;
    
    private boolean auditActivated;
    private CustomDataHelper customDataHelper = new CustomDataHelper(this);
    
    private AwardPermissionsHelper awardPermissionsHelper;
    
    /**
     * 
     * Constructs a AwardForm.
     */
    public AwardForm() {
        super();        
        this.setDocument(new AwardDocument());
        initialize();
    }
    
    /**
     * 
     * This method initialize all form variables
     */
    public void initialize() {
        initializeHeaderNavigationTabs();
        //setPermissionsHelper(new PermissionsHelper(this));
        //newAwardCostShare = new AwardCostShare();
        newAwardFandaRate = new AwardFandaRate();
        setNewAwardReportTerm(new ArrayList<AwardReportTerm>());
        setNewAwardReportTermRecipient(new ArrayList<AwardReportTermRecipient>()); 
        //setNewSponsorTerms(new ArrayList<SponsorTerm>());
        newAwardSpecialReview = new AwardSpecialReview();
        newSpecialReviewExemptions = new ArrayList<AwardSpecialReviewExemption>();
        costShareFormHelper = new CostShareFormHelper(this);
        centralAdminContactsBean = new AwardCentralAdminContactsBean(this);
        sponsorTermFormHelper = new SponsorTermFormHelper(this);
        approvedSubawardFormHelper = new ApprovedSubawardFormHelper(this);
        approvedEquipmentBean = new ApprovedEquipmentBean(this);
        paymentScheduleBean = new PaymentScheduleBean(this);
        approvedForeignTravelBean = new ApprovedForeignTravelBean(this);
        projectPersonnelBean = new AwardProjectPersonnelBean(this);
        unitContactsBean = new AwardUnitContactsBean(this);
        sponsorContactsBean = new AwardSponsorContactsBean(this);
        detailsAndDatesFormHelper = new DetailsAndDatesFormHelper(this);
        directFandADistributionFormHelper = new DirectFandADistributionFormHelper(this);
        setPermissionsHelper(new AwardPermissionsHelper(this));
        //sponsorTermTypes = new ArrayList<KeyLabelPair>();
    }    
    
    /**
     * Get the Header Dispatch.  This determines the action that will occur
     * when the user switches tabs for a budget.  If the user can modify
     * the budget, the budget is automatically saved.  If not (view-only),
     * then a reload will be executed instead.
     * @return the Header Dispatch action
     */
    public String getHeaderDispatch() {
        return this.getDocumentActionFlags().getCanSave() ? SAVE : RELOAD;        
    }
    
    /**
     * 
     * This method returns the AwardDocument object.
     * @return
     */
    public AwardDocument getAwardDocument() {
        return (AwardDocument) super.getDocument();
    }
    
    /**
     * @return
     */
    public ApprovedEquipmentBean getApprovedEquipmentBean() {
        return approvedEquipmentBean;
    }
    
    /**
     * @return
     */
    public ApprovedForeignTravelBean getApprovedForeignTravelBean() {
        return approvedForeignTravelBean;
    }
    
    /**
     * @return
     */
    public AwardCentralAdminContactsBean getCentralAdminContactsBean() {
        return centralAdminContactsBean;
    }
    
    /**
     * @return
     */
    public CostShareFormHelper getCostShareFormHelper() {
        return costShareFormHelper;
    }
    
    /**
     * 
     * This method initializes the loads the header navigation tabs.
     */
    protected void initializeHeaderNavigationTabs(){
        DataDictionaryService dataDictionaryService = getDataDictionaryService();
        DocumentEntry docEntry = dataDictionaryService.getDataDictionary().getDocumentEntry(
                org.kuali.kra.award.document.AwardDocument.class.getName());
        List<HeaderNavigation> navList = docEntry.getHeaderNavigationList();
        HeaderNavigation[] list = new HeaderNavigation[navList.size()];
        navList.toArray(list);
        super.setHeaderNavigationTabs(list); 
    }
    
    /**
     * 
     * This method is a wrapper method for getting DataDictionary Service using the Service Locator.
     * @return
     */
    protected DataDictionaryService getDataDictionaryService(){
        return (DataDictionaryService) KraServiceLocator.getService(Constants.DATA_DICTIONARY_SERVICE_NAME);
    }
    
    /**
     * 
     * This method initializes either the document or the form based on the command value.
     */
    public void initializeFormOrDocumentBasedOnCommand(){
        if (IDocHandler.INITIATE_COMMAND.equals(getCommand())) {
            getAwardDocument().initialize();
        }else{
            initialize();
        }
    }
    public AwardComment getNewAwardCostShareComment() {
        return newAwardCostShareComment;
    }

    public void setNewAwardCostShareComment(AwardComment newAwardCostShareComment) {
        this.newAwardCostShareComment = newAwardCostShareComment;
    }

    /**
     *
     * @return
     */
    public AwardFandaRate getNewAwardFandaRate() {
        return newAwardFandaRate;
    }

    /**
     *
     * @param newAwardFandaRate
     */
    public void setNewAwardFandaRate(AwardFandaRate newAwardFandaRate) {
        this.newAwardFandaRate = newAwardFandaRate;
    }
    
    @Override
    protected void setSaveDocumentControl(DocumentActionFlags tempDocumentActionFlags, @SuppressWarnings("unchecked") Map editMode) {
        tempDocumentActionFlags.setCanSave(true);
    }
    
    @Override
    protected String getLockRegion() {
        return KraAuthorizationConstants.LOCK_DESCRIPTOR_AWARD;
    }
    public List<AwardReportTerm> getNewAwardReportTerm() {
        return newAwardReportTerm;
    }

    public void setNewAwardReportTerm(List<AwardReportTerm> newAwardReportTerm) {
        this.newAwardReportTerm = newAwardReportTerm;
    }

    public List<AwardReportTermRecipient> getNewAwardReportTermRecipient() {
        return newAwardReportTermRecipient;
    }

    public void setNewAwardReportTermRecipient(List<AwardReportTermRecipient> newAwardReportTermRecipient) {
        this.newAwardReportTermRecipient = newAwardReportTermRecipient;
    }

    /**
     * Gets the lookupResultsBOClassName attribute. 
     * @return Returns the lookupResultsBOClassName.
     */
    public String getLookupResultsBOClassName() {
        return lookupResultsBOClassName;
    }

    /**
     * Sets the lookupResultsBOClassName attribute value.
     * @param lookupResultsBOClassName The lookupResultsBOClassName to set.
     */
    public void setLookupResultsBOClassName(String lookupResultsBOClassName) {
        this.lookupResultsBOClassName = lookupResultsBOClassName;
    }

    /**
     * Gets the lookupResultsSequenceNumber attribute. 
     * @return Returns the lookupResultsSequenceNumber.
     */
    public String getLookupResultsSequenceNumber() {
        return lookupResultsSequenceNumber;
    }

    /**
     * Sets the lookupResultsSequenceNumber attribute value.
     * @param lookupResultsSequenceNumber The lookupResultsSequenceNumber to set.
     */
    public void setLookupResultsSequenceNumber(String lookupResultsSequenceNumber) {
        this.lookupResultsSequenceNumber = lookupResultsSequenceNumber;
    }

    /**
     * Gets the newAwardSpecialReview attribute. 
     * @return Returns the newAwardSpecialReview.
     */
    public AwardSpecialReview getNewSpecialReview() {
        return newAwardSpecialReview;
    }

    /**
     * Sets the newAwardSpecialReview attribute value.
     * @param newAwardSpecialReview The newAwardSpecialReview to set.
     */
    public void setNewSpecialReview(AwardSpecialReview newAwardSpecialReview) {
        this.newAwardSpecialReview = newAwardSpecialReview;
    }


    public List<AwardSpecialReviewExemption> getNewSpecialReviewExemptions() {
        return newSpecialReviewExemptions;
    }

    public AwardSpecialReviewExemption getNewSpecialReviewExemption(int index) {
        return newSpecialReviewExemptions.get(index);
    }
    /**
     * Sets the newSpecialReviewExcemptions attribute value.
     * @param newSpecialReviewExcemptions The newSpecialReviewExcemptions to set.
     */
    public void setNewSpecialReviewExemptions(List<AwardSpecialReviewExemption> newSpecialReviewExcemptions) {
        this.newSpecialReviewExemptions = newSpecialReviewExcemptions;
    }

    public ResearchDocumentBase getResearchDocument() {
        return getAwardDocument();
    }

    /**
     * Gets the newExemptionTypeCodes attribute. 
     * @return Returns the newExemptionTypeCodes.
     */
    public String[] getNewExemptionTypeCodes() {
        return newExemptionTypeCodes;
    }

    /**
     * Sets the newExemptionTypeCodes attribute value.
     * @param newExemptionTypeCodes The newExemptionTypeCodes to set.
     */
    public void setNewExemptionTypeCodes(String... newExemptionTypeCodes) {
        this.newExemptionTypeCodes = newExemptionTypeCodes;
    }

    /**
     * @return The selected lead unit
     */
    public String getSelectedLeadUnit() {
        return projectPersonnelBean.getSelectedLeadUnit();
    }
    
    public AwardSponsorContactsBean getSponsorContactsBean() {
        return sponsorContactsBean;
    }
    
    public AwardUnitContactsBean getUnitContactsBean() {
        return unitContactsBean;
    }
    
    public AwardProjectPersonnelBean getProjectPersonnelBean() {
        return projectPersonnelBean;
    }
    
    public int getAwardReportTermPanelNumber() {
        return awardReportTermPanelNumber;
    }

    public void setAwardReportTermPanelNumber(int awardReportTermPanelNumber) {
        this.awardReportTermPanelNumber = awardReportTermPanelNumber;
    }
    
    public List<KeyLabelPair> getReportClasses() {
        return reportClasses;
    }

    public void setReportClasses(List<KeyLabelPair> reportClasses) {
        this.reportClasses = reportClasses;
    }



    /**
     * Gets the approvedSubawardFormHelper attribute. 
     * @return Returns the approvedSubawardFormHelper.
     */
    public ApprovedSubawardFormHelper getApprovedSubawardFormHelper() {
        return approvedSubawardFormHelper;
    }



    /**
     * Sets the approvedSubawardFormHelper attribute value.
     * @param approvedSubawardFormHelper The approvedSubawardFormHelper to set.
     */
    public void setApprovedSubawardFormHelper(ApprovedSubawardFormHelper approvedSubawardFormHelper) {
        this.approvedSubawardFormHelper = approvedSubawardFormHelper;
    }
    
     public ReportClass getReportClassForPaymentsAndInvoices() {
        return reportClassForPaymentsAndInvoices;
    }



    public void setReportClassForPaymentsAndInvoices(ReportClass reportClassForPaymentsAndInvoices) {
        this.reportClassForPaymentsAndInvoices = reportClassForPaymentsAndInvoices;
    }    


    /**
     * Gets the sponsorTermFormHelper attribute. 
     * @return Returns the sponsorTermFormHelper.
     */
    public SponsorTermFormHelper getSponsorTermFormHelper() {
        return sponsorTermFormHelper;
    }



    /**
     * @param unitName
     */
    public void setSelectedLeadUnit(String unitName) {
        projectPersonnelBean.setSelectedLeadUnit(unitName);
    }
    
    /**
     * Sets the sponsorTermFormHelper attribute value.
     * @param sponsorTermFormHelper The sponsorTermFormHelper to set.
     */
    public void setSponsorTermFormHelper(SponsorTermFormHelper sponsorTermFormHelper) {
        this.sponsorTermFormHelper = sponsorTermFormHelper;
    }

    /**
     * Gets the paymentScheduleBean attribute. 
     * @return Returns the paymentScheduleBean.
     */
    public PaymentScheduleBean getPaymentScheduleBean() {
        return paymentScheduleBean;
    }

    /**
     * Sets the paymentScheduleBean attribute value.
     * @param paymentScheduleBean The paymentScheduleBean to set.
     */
    public void setPaymentScheduleBean(PaymentScheduleBean paymentScheduleBean) {
        this.paymentScheduleBean = paymentScheduleBean;
    }
    
    /** {@inheritDoc} */
    public boolean isAuditActivated() {
        return this.auditActivated;
    }

    /** {@inheritDoc} */
    public void setAuditActivated(boolean auditActivated) {
        this.auditActivated = auditActivated;
    }

    public DetailsAndDatesFormHelper getDetailsAndDatesFormHelper() {
        return detailsAndDatesFormHelper;
    }

    public void setDetailsAndDatesFormHelper(DetailsAndDatesFormHelper detailsAndDatesFormHelper) {
        this.detailsAndDatesFormHelper = detailsAndDatesFormHelper;
    }

    /**
     * Gets the permissionsHelper attribute. 
     * @return Returns the awardPermissionsHelper.
     */
    public AwardPermissionsHelper getPermissionsHelper() {
        return awardPermissionsHelper;
    }

    /**
     * Sets the awardPermissionsHelper attribute value.
     * @param permissionsHelper The permissionsHelper to set.
     */
    public void setPermissionsHelper(AwardPermissionsHelper awardPermissionsHelper) {
        this.awardPermissionsHelper = awardPermissionsHelper;
    }
    
    /**
     * This method returns a string representation of the document type
     * @return
     */
    public String getDocumentTypeName() {
        return "AwardDocument";
    }
    
    public String getActionName() {
        return "award";
    }
    
    /**
     * @see org.kuali.kra.common.customattributes.CustomDataForm#getCustomDataHelper()
     */
    public CustomDataHelper getCustomDataHelper() {
        return customDataHelper;
    }

    /**
     * This method sets the custom data helper
     * @param customDataHelper
     */
    public void setCustomDataHelper(CustomDataHelper customDataHelper) {
        this.customDataHelper = customDataHelper;
    }



    /**
     * Gets the directFandADistributionFormHelper attribute. 
     * @return Returns the directFandADistributionFormHelper.
     */
    public DirectFandADistributionFormHelper getDirectFandADistributionFormHelper() {
        return directFandADistributionFormHelper;
    }



    /**
     * Sets the directFandADistributionFormHelper attribute value.
     * @param directFandADistributionFormHelper The directFandADistributionFormHelper to set.
     */
    public void setDirectFandADistributionFormHelper(DirectFandADistributionFormHelper directFandADistributionFormHelper) {
        this.directFandADistributionFormHelper = directFandADistributionFormHelper;
    }
    
    /**
     * Sets the awardAuditActivated attribute value.
     * @param awardAuditActivated The awardAuditActivated to set.
     */
    public void setAwardAuditActivated(boolean awardAuditActivated) {
        this.auditActivated = awardAuditActivated;
    }
}
