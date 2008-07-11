/*
 * Copyright 2007 The Kuali Foundation.
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
package org.kuali.kra.proposaldevelopment.web.struts.form;



import static org.kuali.kra.infrastructure.Constants.CREDIT_SPLIT_ENABLED_RULE_NAME;
import static org.kuali.kra.infrastructure.Constants.PARAMETER_COMPONENT_DOCUMENT;
import static org.kuali.kra.infrastructure.Constants.PARAMETER_MODULE_PROPOSAL_DEVELOPMENT;
import static org.kuali.kra.infrastructure.KraServiceLocator.getService;
import static org.kuali.kra.logging.FormattedLogger.debug;
import static org.kuali.kra.logging.FormattedLogger.warn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.kuali.core.bo.Parameter;
import org.kuali.core.bo.user.KualiGroup;
import org.kuali.core.bo.user.UniversalUser;
import org.kuali.core.service.BusinessObjectService;
import org.kuali.core.service.DataDictionaryService;
import org.kuali.core.service.KualiConfigurationService;
import org.kuali.core.util.ActionFormUtilMap;
import org.kuali.core.util.GlobalVariables;
import org.kuali.core.web.ui.ExtraButton;
import org.kuali.core.web.ui.HeaderField;
import org.kuali.core.web.ui.KeyLabelPair;
import org.kuali.core.workflow.service.KualiWorkflowDocument;
import org.kuali.kra.bo.Person;
import org.kuali.kra.bo.PersonEditableField;
import org.kuali.kra.bo.SponsorFormTemplate;
import org.kuali.kra.bo.Unit;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.infrastructure.PermissionConstants;
import org.kuali.kra.infrastructure.RoleConstants;
import org.kuali.kra.kim.bo.KimRole;
import org.kuali.kra.kim.service.GroupService;
import org.kuali.kra.proposaldevelopment.bo.Narrative;
import org.kuali.kra.proposaldevelopment.bo.NarrativeUserRights;
import org.kuali.kra.proposaldevelopment.bo.PropScienceKeyword;
import org.kuali.kra.proposaldevelopment.bo.ProposalAbstract;
import org.kuali.kra.proposaldevelopment.bo.ProposalAssignedRole;
import org.kuali.kra.proposaldevelopment.bo.ProposalChangedData;
import org.kuali.kra.proposaldevelopment.bo.ProposalCopyCriteria;
import org.kuali.kra.proposaldevelopment.bo.ProposalLocation;
import org.kuali.kra.proposaldevelopment.bo.ProposalPerson;
import org.kuali.kra.proposaldevelopment.bo.ProposalPersonBiography;
import org.kuali.kra.proposaldevelopment.bo.ProposalPersonDegree;
import org.kuali.kra.proposaldevelopment.bo.ProposalSpecialReview;
import org.kuali.kra.proposaldevelopment.bo.ProposalState;
import org.kuali.kra.proposaldevelopment.bo.ProposalUser;
import org.kuali.kra.proposaldevelopment.bo.ProposalUserEditRoles;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.proposaldevelopment.service.KeyPersonnelService;
import org.kuali.kra.proposaldevelopment.service.ProposalAuthorizationService;
import org.kuali.kra.proposaldevelopment.web.bean.ProposalUserRoles;
import org.kuali.kra.s2s.bo.S2sAppSubmission;
import org.kuali.kra.s2s.bo.S2sOpportunity;
import org.kuali.kra.s2s.bo.S2sSubmissionHistory;
import org.kuali.kra.service.PersonService;
import org.kuali.kra.service.UnitService;
import org.kuali.kra.web.struts.form.ProposalFormBase;
import org.kuali.rice.KNSServiceLocator;

import edu.iu.uis.eden.EdenConstants;

/**
 * This class...
 * @author Kuali Nervous System Team (kualidev@oncourse.iu.edu)
 */
public class ProposalDevelopmentForm extends ProposalFormBase {
    private static final String MISSING_PARAM_MSG = "Couldn't find parameter '%s'";
    private static final String DELETE_SPECIAL_REVIEW_ACTION = "deleteSpecialReview";
    
    private boolean creditSplitEnabled;
    private String primeSponsorName;
    private ProposalLocation newPropLocation;
    private ProposalSpecialReview newPropSpecialReview;
    private ProposalPerson newProposalPerson;
    private List<ProposalPersonDegree> newProposalPersonDegree;
    private List<Unit> newProposalPersonUnit;
    private String newRolodexId;
    private String newPersonId;
    private Narrative newNarrative;
    private FormFile narrativeFile;
    private Map personEditableFields;
    private boolean showMaintenanceLinks;
    private ProposalAbstract newProposalAbstract;
    private ProposalPersonBiography newPropPersonBio;
    private Narrative newInstituteAttachment;
    //private boolean auditActivated;
    private ProposalCopyCriteria copyCriteria;
    private Map<String, Parameter> proposalDevelopmentParameters;
    private Integer answerYesNo;
    private Integer answerYesNoNA;
    private ProposalUser newProposalUser;
    private String newBudgetVersionName;
    private List<ProposalUserRoles> proposalUserRolesList = null;
    private ProposalUserEditRoles proposalUserEditRoles;
    private boolean newProposalPersonRoleRendered;
    private List<NarrativeUserRights> newNarrativeUserRights;
    private S2sOpportunity newS2sOpportunity;
    private List<S2sAppSubmission> newS2sAppSubmission;
    private SortedMap<String, List> customAttributeGroups;
    private Map<String, String[]> customAttributeValues;
    private List<Narrative> narratives;
    private boolean reject;
    private List<KeyLabelPair> exemptNumberList;
    private String[] newExemptNumbers;
    private List<String[]> documentExemptNumbers;
    private String optInUnitDetails;
    private String optInCertificationStatus;
    private ProposalChangedData newProposalChangedData;

    private String proposalFormTabTitle = "Print Sponsor Form Packages ";
    
    public ProposalDevelopmentForm() {
        super();
        this.setDocument(new ProposalDevelopmentDocument());
        initialize();
    }

    /**
     *
     * This method initialize all form variables
     */
    public void initialize() {
        setNewPropLocation(new ProposalLocation());
        setNewPropSpecialReview(new ProposalSpecialReview());
        setNewNarrative(createNarrative());
        setNewProposalPerson(new ProposalPerson());
        setNewProposalPersonDegree(new ArrayList<ProposalPersonDegree>());
        setNewProposalPersonUnit(new ArrayList<Unit>());
        setNewProposalAbstract(new ProposalAbstract());
        setNewProposalUser(new ProposalUser());
        setNewS2sOpportunity(new S2sOpportunity());
        customAttributeValues = new HashMap<String, String[]>();
        setCopyCriteria(new ProposalCopyCriteria(getProposalDevelopmentDocument()));
        DataDictionaryService dataDictionaryService = (DataDictionaryService) KraServiceLocator.getService(Constants.DATA_DICTIONARY_SERVICE_NAME);
        this.setHeaderNavigationTabs((dataDictionaryService.getDataDictionary().getDocumentEntry(org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument.class.getName())).getHeaderTabNavigation());
        proposalDevelopmentParameters = new HashMap<String, Parameter>();
        newProposalPersonRoleRendered = false;
        setNewProposalChangedData(new ProposalChangedData());
    }

    /**
     * This creates a new Narrative. Protected to allow mocks and stubs to provide their own Narrative that doesn't do a user id lookup
     * @return
     */
    protected Narrative createNarrative() {
        return new Narrative();
    }
    /**
     * Multiple Value Lookups return values to the form through the request, but in some instances do not clear previous values from other lookups because the form resides in the session scope. 
     * This is to set the Multiple Value Lookups to a good state. Values getting cleared are:
     * <ul>
     *   <li><code>lookupResultsSequenceNumber</code></li>
     *   <li><code>lookupResultsBOClassName</code></li>
     * </ul>
     * 
     */
    private void clearMultipleValueLookupResults() {
        setLookupResultsSequenceNumber(null);
        setLookupResultsBOClassName(null);
    }

    public ProposalDevelopmentDocument getProposalDevelopmentDocument() {
        return (ProposalDevelopmentDocument) this.getDocument();
    }

    @Override
    public void populate(HttpServletRequest request) {
       
        clearMultipleValueLookupResults();
        super.populate(request);
        ProposalDevelopmentDocument proposalDevelopmentDocument=getProposalDevelopmentDocument();

        proposalDevelopmentDocument.refreshReferenceObject("sponsor");

        // Temporary hack for KRACOEUS-489
        if (getActionFormUtilMap() instanceof ActionFormUtilMap) {
            ((ActionFormUtilMap) getActionFormUtilMap()).clear();
        }       
      
        ProposalCopyCriteria copyCriteria = this.getCopyCriteria();
        if (copyCriteria != null) {
            copyCriteria.setOriginalLeadUnitNumber(proposalDevelopmentDocument.getOwnedByUnitNumber());
        }
    }
    
    protected void populateHeaderFields(KualiWorkflowDocument workflowDocument) {
        super.populateHeaderFields(workflowDocument);
        
        ProposalDevelopmentDocument pd = getProposalDevelopmentDocument();
        ProposalState proposalState = (pd == null) ? null : pd.getProposalState();
        HeaderField docStatus = new HeaderField("DataDictionary.DocumentHeader.attributes.financialDocumentStatusCode", proposalState == null? "" : proposalState.getDescription());
        
        getDocInfo().set(1, docStatus);
    }

    private void populateCurrentProposalColumnValues() {
        DataDictionaryService dataDictionaryService = (DataDictionaryService) KraServiceLocator.getService(Constants.DATA_DICTIONARY_SERVICE_NAME);
        Set<String> attributeNames = dataDictionaryService.getDataDictionary().getDocumentEntry(org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument.class.getName()).getAttributes().keySet();

    }

    public ProposalLocation getNewPropLocation() {
        return newPropLocation;
    }


    public void setNewPropLocation(ProposalLocation newPropLocation) {
        this.newPropLocation = newPropLocation;
    }


    public ProposalSpecialReview getNewPropSpecialReview() {
        return newPropSpecialReview;
    }


    public void setNewPropSpecialReview(ProposalSpecialReview newPropSpecialReview) {
        this.newPropSpecialReview = newPropSpecialReview;
    }

    /**
     * Gets the new proposal abstract.  This is the abstract filled
     * in by the user on the form before pressing the add button. The
     * abstract can be invalid if the user has not specified an abstract type.
     *
     * @return the new proposal abstract
     */
    public ProposalAbstract getNewProposalAbstract() {
        return newProposalAbstract;
    }

    /**
     * Sets the new proposal abstract.  This is the abstract that will be
     * shown to the user on the form.
     *
     * @param newProposalAbstract
     */
    public void setNewProposalAbstract(ProposalAbstract newProposalAbstract) {
        this.newProposalAbstract = newProposalAbstract;
    }

    /* Reset method
     * @param mapping
     * @param request
     * reset check box values in keyword panel and properties that much be read on each request.
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        
        ProposalCopyCriteria copyCriteria = this.getCopyCriteria();
        if (copyCriteria != null) {
            copyCriteria.setIncludeAttachments(false);
            copyCriteria.setIncludeBudget(false);
        }
        
       // following reset the tab stats and will load as default when it returns from lookup.
       // TODO : Do we really need this?
       // implemented headerTab in KraTransactionalDocumentActionBase
       //     this.setTabStates(new HashMap<String, String>());
        this.setCurrentTabIndex(0);


        ProposalDevelopmentDocument proposalDevelopmentDocument = this.getProposalDevelopmentDocument();
        List<PropScienceKeyword> keywords = proposalDevelopmentDocument.getPropScienceKeywords();
        for(int i=0; i<keywords.size(); i++) {
            PropScienceKeyword propScienceKeyword = (PropScienceKeyword)keywords.get(i);
            propScienceKeyword.setSelectKeyword(false);
        }

        
        /* reset check box in sponsor form templates */
        List<SponsorFormTemplate> sponsorFormTemplates = proposalDevelopmentDocument.getSponsorFormTemplates();
        for(SponsorFormTemplate sponsorFormTemplate : sponsorFormTemplates) {
            sponsorFormTemplate.setSelectToPrint(false);
        }
        
        // Clear the edit roles so that they can then be set by struts
        // when the form is submitted.
        ProposalUserEditRoles editRoles = this.getProposalUserEditRoles();
        if (editRoles != null) {
            editRoles.clear();
        }
        
        // reset exempt numbers.
        //setDocumentExemptNumbers(new ArrayList<String[]>());
        resetExemptNumbers();
    }


    /**
     * Sets the primeSponsorName attribute value.
     * @param primeSponsorName The primeSponsorName to set.
     */
    public void setPrimeSponsorName(String primeSponsorName) {
        this.primeSponsorName = primeSponsorName;
    }


    /**
     * Gets the primeSponsorName attribute.
     * @return Returns the primeSponsorName.
     */
    public String getPrimeSponsorName() {
        return primeSponsorName;
    }

    /**
     * Gets the value of newPersonId
     *
     * @return the value of newPersonId
     */
    public String getNewPersonId() {
        return this.newPersonId;
    }

    /**
     * Sets the value of newPersonId
     *
     * @param argNewPersonId Value to assign to this.newPersonId
     */
    public void setNewPersonId(String argNewPersonId) {
        this.newPersonId = argNewPersonId;
    }

    /**
     * Gets the value of newProposalPerson
     *
     * @return the value of newProposalPerson
     */
    public ProposalPerson getNewProposalPerson() {
        return this.newProposalPerson;
    }

    /**
     * Sets the value of newProposalPerson
     *
     * @param argNewProposalPerson Value to assign to this.newProposalPerson
     */
    public void setNewProposalPerson(ProposalPerson argNewProposalPerson) {
        this.newProposalPerson = argNewProposalPerson;
    }

    /**
     * Gets the value of newProposalPersonUnit
     *
     * @return the value of newProposalPersonUnit
     */
    public List<Unit> getNewProposalPersonUnit() {
        if (this.getProposalDevelopmentDocument().getProposalPersons().size() > this.newProposalPersonUnit.size()) {
            this.newProposalPersonUnit.add(this.newProposalPersonUnit.size(), new Unit());
        }
        return this.newProposalPersonUnit;
    }

    /**
     * Sets the value of newProposalPersonUnit
     *
     * @param argUnit Value to assign to this.newProposalPersonUnit
     */
    public void setNewProposalPersonUnit(List<Unit> argUnit) {
        this.newProposalPersonUnit = argUnit;
    }

    /**
     * Gets the value of newProposalPersonDegree
     *
     * @return the value of newProposalPersonDegree
     */
    public List<ProposalPersonDegree> getNewProposalPersonDegree() {

        if (this.getProposalDevelopmentDocument().getProposalPersons().size() > this.newProposalPersonDegree.size()) {
            this.newProposalPersonDegree.add(this.newProposalPersonDegree.size(),new ProposalPersonDegree());
        }
        return this.newProposalPersonDegree;
    }

    /**
     * Sets the value of newProposalPersonDegree
     *
     * @param argDegree Value to assign to this.newProposalPersonDegree
     */
    public void setNewProposalPersonDegree(List<ProposalPersonDegree> argDegree) {
        this.newProposalPersonDegree = argDegree;
    }

    /**
     * Gets the value of newRolodexId
     *
     * @return the value of newRolodexId
     */
    public String getNewRolodexId() {
        return this.newRolodexId;
    }


    /**
     * Sets the value of newRolodexId
     *
     * @param argNewRolodexId Value to assign to this.newRolodexId
     */
    public void setNewRolodexId(String argNewRolodexId) {
        this.newRolodexId = argNewRolodexId;
    }

    /**
     * Gets the newNarrative attribute.
     * @return Returns the newNarrative.
     */
    public Narrative getNewNarrative() {
        return newNarrative;
    }


    /**
     * Sets the newNarrative attribute value.
     * @param newNarrative The newNarrative to set.
     */
    public void setNewNarrative(Narrative newNarrative) {
        this.newNarrative = newNarrative;
    }


    public FormFile getNarrativeFile() {
        return narrativeFile;
    }


    public void setNarrativeFile(FormFile narrativeFile) {
        this.narrativeFile = narrativeFile;
    }

    public boolean isShowMaintenanceLinks(){
        return showMaintenanceLinks;
    }

    public void setShowMaintenanceLinks(boolean showMaintenanceLinks) {
        this.showMaintenanceLinks = showMaintenanceLinks;
    }

    private BusinessObjectService getBusinessObjectService() {
        return KraServiceLocator.getService(BusinessObjectService.class);
    }

    /**
     * Creates the list of <code>{@link PersonEditableField}</code> field names.
     */
    public void populatePersonEditableFields() {
        debug("Adding PersonEditableFields");

        setPersonEditableFields(new HashMap());

        Collection<PersonEditableField> fields = getBusinessObjectService().findAll(PersonEditableField.class);
        for (PersonEditableField field : fields) {
            debug("found field " + field.getFieldName());
            getPersonEditableFields().put(field.getFieldName(), new Boolean(field.isActive()));
        }
    }

    /**
     * Write access to <code>{@link Map}</code> containing persisted <code>{@link PersonEditableField}</code> BO instances.
     *
     * @param fields
     */
    public void setPersonEditableFields(Map fields) {
        personEditableFields = fields;
    }

    /**
     * Get persisted <code>{@link PersonEditableField}</code> BO instances as a <code>{@link Map}</code>. If the <code>{@link Map}</code> containing them is
     *  <code>null</code>, then it gets populated here.
     *
     * @return Map containing person editable fields
     */
    public Map getPersonEditableFields() {
        if (personEditableFields == null) {
            populatePersonEditableFields();
        }
        return personEditableFields;
    }

    public Map getCreditSplitTotals() {
        Map test=getKeyPersonnelService().calculateCreditSplitTotals(getProposalDevelopmentDocument());
        return getKeyPersonnelService().calculateCreditSplitTotals(getProposalDevelopmentDocument());
        
    }


    public ProposalPersonBiography getNewPropPersonBio() {
        return newPropPersonBio;
    }


    public void setNewPropPersonBio(ProposalPersonBiography newPropPersonBio) {
        this.newPropPersonBio = newPropPersonBio;
    }


    public Narrative getNewInstituteAttachment() {
        return newInstituteAttachment;
    }


    public void setNewInstituteAttachment(Narrative newInstituteAttachment) {
        this.newInstituteAttachment = newInstituteAttachment;
    }


    /**
     * Sets the auditActivated attribute value.
     * @param auditActivated The auditActivated to set.
     */
//    public void setAuditActivated(boolean auditActivated) {
//        this.auditActivated = auditActivated;
//    }

    /**
     * Gets the auditActivated attribute.
     * @return Returns the auditActivated.
     */
//    public boolean isAuditActivated() {
//        return auditActivated;
//    }

    /**
     * Sets the customAttributeGroups attribute value.
     * @param customAttributeGroups The customAttributeGroups to set.
     */
    public void setCustomAttributeGroups(SortedMap<String, List> customAttributeGroups) {
        this.customAttributeGroups = customAttributeGroups;
    }

    private KeyPersonnelService getKeyPersonnelService() {
        return getService(KeyPersonnelService.class);
    }

    /**
     * Gets the Copy Criteria for copying a proposal development document.
     * The criteria is user-specified and controls the operation of the
     * copy.
     *
     * @return the proposal copy criteria
     */
    public ProposalCopyCriteria getCopyCriteria() {
        return copyCriteria;
    }

    /**
     * Sets the Copy Criteria for copying a proposal development document.
     * The criteria is user-specified and controls the operation of the
     * copy.
     *
     * @param copyCriteria the new proposal copy criteria
     */
    public void setCopyCriteria(ProposalCopyCriteria copyCriteria) {
        this.copyCriteria = copyCriteria;
    }

    /**
     * Determines if attachments can be copied.
     *
     * @return true if copying attachments is disabled; otherwise false.
     */
    public boolean getIsCopyAttachmentsDisabled() {
        ProposalDevelopmentDocument doc = this.getProposalDevelopmentDocument();
        return !(doc.getNarratives().size() > 0 ||
            doc.getInstituteAttachments().size() > 0 ||
            doc.getPropPersonBios().size() > 0);
    }

    /**
     * Gets the customAttributeGroups attribute.
     * @return Returns the customAttributeGroups.
     */
    public Map<String, List> getCustomAttributeGroups() {
        return customAttributeGroups;
    }


    /**
     * Sets the customAttributeValues attribute value.
     * @param customAttributeValues The customAttributeValues to set.
     */
    public void setCustomAttributeValues(Map<String, String[]> customAttributeValues) {
        this.customAttributeValues = customAttributeValues;
    }


    /**
     * Gets the customAttributeValues attribute.
     * @return Returns the customAttributeValues.
     */
    public Map<String, String[]> getCustomAttributeValues() {
        return customAttributeValues;
    }

    /**
     * This method...
     *
     * @return true if copying budget(s) is disabled; otherwise false.
     */
    public boolean getIsCopyBudgetDisabled() {
        return !(this.getProposalDevelopmentDocument().getBudgetVersionOverviews().size() > 0);
    }


    /**
     * Sets the proposalDevelopmentParameters attribute value.
     * @param proposalDevelopmentParameters The proposalDevelopmentParameters to set.
     */
    public void setProposalDevelopmentParameters(Map<String, Parameter> proposalDevelopmentParameters) {
        this.proposalDevelopmentParameters = proposalDevelopmentParameters;
    }


    /**
     * Gets the proposalDevelopmentParameters attribute.
     * @return Returns the proposalDevelopmentParameters.
     */
    public Map<String, Parameter> getProposalDevelopmentParameters() {
        return proposalDevelopmentParameters;
    }

    public Integer getAnswerYesNo() {
        return Constants.ANSWER_YES_NO;
    }


    public Integer getAnswerYesNoNA() {
        return Constants.ANSWER_YES_NO_NA;
    }
    
    /**
     * Used by the Assigned Roles panel in the Permissions page.  
     * @return
     */
    public List<ProposalAssignedRole> getProposalAssignedRoles() {
        List<ProposalAssignedRole> assignedRoles = new ArrayList<ProposalAssignedRole>();
        
        Collection<KimRole> roles = getKimProposalRoles();
        for (KimRole role : roles) {
            if (!role.isUnassigned()) {
                ProposalAssignedRole assignedRole = 
                    new ProposalAssignedRole(role.getName(), getUsersInRole(role.getName()));
                assignedRoles.add(assignedRole);
            }
        }
        return assignedRoles;
    }
    
    /**
     * Get the full names of the users with the given role in the proposal.
     * @param roleName the name of the role
     * @return the names of users with the role in the document
     */
    private List<String> getUsersInRole(String roleName) {
        List<String> names = new ArrayList<String>();
        List<ProposalUserRoles> proposalUsers = getProposalUserRoles();
        for (ProposalUserRoles proposalUser : proposalUsers) {
            if (proposalUser.getRoleNames().contains(roleName)) {
                names.add(proposalUser.getFullname());
            }
        }
        
        // Sort the list of names.
        
        Collections.sort(names, new Comparator() {
            public int compare(Object o1, Object o2) {
                String name1 = (String) o1;
                String name2 = (String) o2;
                if (name1 == null && name2 == null) return 0;
                if (name1 == null) return -1;
                return name1.compareTo(name2);
            }
        });
        return names;
    }
    
    /** 
     * Gets the new proposal user.  This is the proposal user that is filled
     * in by the user on the form before pressing the add button.
     *
     * @return the new proposal user
     */
    public ProposalUser getNewProposalUser() {
        return newProposalUser;
    }

    /**
     * Sets the new proposal user.  This is the proposal user that will be
     * shown on the form.
     *
     * @param newProposalUser the new proposal user
     */
    public void setNewProposalUser(ProposalUser newProposalUser) {
        this.newProposalUser = newProposalUser;
    }
    
    /**
     * Get the list of all of the Proposal roles (filter out unassigned).
     * @return the list of proposal roles
     */
    public List<KimRole> getProposalRoles() {
        List<KimRole> proposalRoles = new ArrayList<KimRole>();
        Collection<KimRole> roles = getKimProposalRoles();
        for (KimRole role : roles) {
            if (!role.isUnassigned()) {
                proposalRoles.add(role);
            }
        }
        return proposalRoles;
    }
    
    /**
     * Get the list of Proposal User Roles.  Each user has one or more
     * roles assigned to the proposal.  This method builds the list each
     * time it is invoked.  It is always invoked when the Permissions page
     * is displayed.  After the list is built, the list can be obtained
     * via the getCurrentProposalUserRoles() method.  Typically, the 
     * getCurrentProposalUserRoles() is invoked from the Permission Actions.
     * 
     * @return the list of users with proposal roles and sorted by their full name
     */
    public synchronized List<ProposalUserRoles> getProposalUserRoles() {
        if (proposalUserRolesList == null) {
            proposalUserRolesList = new ArrayList<ProposalUserRoles>();
            
            // Add persons into the ProposalUserRolesList for each of the roles.
            Collection<KimRole> roles = getKimProposalRoles();
            for (KimRole role : roles) {
                addPersons(proposalUserRolesList, role.getName());
            }
            
            sortProposalUsers();  
        }
        
        return proposalUserRolesList;
    }
    
    public List<ProposalUserRoles> getCurrentProposalUserRoles() {
        List<ProposalUserRoles> current = new ArrayList<ProposalUserRoles>();
        
        Collection<KimRole> roles = getKimProposalRoles();
        for (KimRole role : roles) {
            addPersons(current, role.getName());
        }
        
        return current;
    }
    
    /**
     * Get all of the proposal roles.
     * @return
     */
    public Collection<KimRole> getKimProposalRoles() {
        List<KimRole> proposalRoles = new ArrayList<KimRole>();
        BusinessObjectService businessObjectService = KraServiceLocator.getService(BusinessObjectService.class);
        Map<String, Object> fieldValues = new HashMap<String, Object>();
        fieldValues.put("roleTypeCode", RoleConstants.PROPOSAL_ROLE_TYPE);
        Collection<KimRole> roles = businessObjectService.findMatching(KimRole.class, fieldValues);
       
        /*
         * Add in unassigned and standard proposal roles first so that
         * they always show up first on the web pages.
         */
        for (KimRole role : roles) {
            if (role.isUnassigned()) {
                proposalRoles.add(0, role);
            } else if (role.isStandardProposalRole()) {
                proposalRoles.add(role);
            }
        }
        
        /*
         * Now add in any user-define proposal roles.
         */
        for (KimRole role : roles) {
            if (!role.isUnassigned() && !role.isStandardProposalRole()) {
                proposalRoles.add(role);
            }
        }
        return proposalRoles;
    }
   
    
    private void sortProposalUsers() {
        // Sort the list of users by their full name.
        
        Collections.sort(proposalUserRolesList, new Comparator() {
            public int compare(Object o1, Object o2) {
                ProposalUserRoles user1 = (ProposalUserRoles) o1;
                ProposalUserRoles user2 = (ProposalUserRoles) o2;
                return user1.getFullname().compareTo(user2.getFullname());
            }
        });
    }
    
    public void addProposalUser(ProposalUser proposalUser) {
        PersonService personService = KraServiceLocator.getService(PersonService.class);
        Person person = personService.getPersonByName(proposalUser.getUsername());
        ProposalUserRoles userRoles = buildProposalUserRoles(person, proposalUser.getRoleName());
        proposalUserRolesList.add(userRoles);
        sortProposalUsers();
    }
    
    /**
     * Add a set of persons to the proposalUserRolesList for a given role.
     * 
     * @param proposalUserRolesList the list to add to
     * @param roleName the name of role to query for persons assigned to that role
     */
    private void addPersons(List<ProposalUserRoles> proposalUserRolesList, String roleName) {
        ProposalAuthorizationService proposalAuthService = KraServiceLocator.getService(ProposalAuthorizationService.class);
        ProposalDevelopmentDocument doc = this.getProposalDevelopmentDocument();
        
        List<Person> persons = proposalAuthService.getPersonsInRole(doc, roleName);
        for (Person person : persons) {
            ProposalUserRoles proposalUserRoles = findProposalUserRoles(proposalUserRolesList, person.getUserName());
            if (proposalUserRoles != null) {
                proposalUserRoles.addRoleName(roleName);
            } else {
                proposalUserRolesList.add(buildProposalUserRoles(person, roleName));
            }
        }
    }
    
    /**
     * Find a user in the list of proposalUserRolesList based upon the user's username.
     * 
     * @param proposalUserRolesList the list to search
     * @param username the user's username to search for
     * @return the proposalUserRoles or null if not found
     */
    private ProposalUserRoles findProposalUserRoles(List<ProposalUserRoles> proposalUserRolesList, String username) {
        for (ProposalUserRoles proposalUserRoles : proposalUserRolesList) {
            if (StringUtils.equals(username, proposalUserRoles.getUsername())) {
                return proposalUserRoles;
            }
        }
        return null;
    }
    
    /**
     * Build a ProposalUserRoles instance.  Assemble the information about
     * the user (person) into a ProposalUserRoles along with the home unit
     * info for that person.
     * 
     * @param person the person
     * @param roleName the name of the role
     * @return a new ProposalUserRoles instance
     */
    private ProposalUserRoles buildProposalUserRoles(Person person, String roleName) {
        ProposalUserRoles proposalUserRoles = new ProposalUserRoles();
        
        // Set the person's username, rolename, fullname, and home unit.

        proposalUserRoles.setUsername(person.getUserName());
        proposalUserRoles.addRoleName(roleName);
        proposalUserRoles.setFullname(person.getFullName());
        proposalUserRoles.setUnitNumber(person.getHomeUnit());
        
        // Query the database to find the name of the unit.
            
        UnitService unitService = KraServiceLocator.getService(UnitService.class);
        Unit unit = unitService.getUnit(person.getHomeUnit());
        if (unit != null) {
            proposalUserRoles.setUnitName(unit.getUnitName());
        }
        
        return proposalUserRoles;
    }

    /**
     * Get the Edit Roles BO that is simply a form filled in by a
     * user via the Edit Roles web page.
     * 
     * @return the edit roles object
     */
    public ProposalUserEditRoles getProposalUserEditRoles() {
        return proposalUserEditRoles;
    }

    /**
     * Set the Edit Roles BO.
     * @param proposalUserEditRoles the Edit Roles BO
     */
    public void setProposalUserEditRoles(ProposalUserEditRoles proposalUserEditRoles) {
        this.proposalUserEditRoles = proposalUserEditRoles;
    }

    public String getNewBudgetVersionName() {
        return newBudgetVersionName;
    }

    public void setNewBudgetVersionName(String newBudgetVersionName) {
        this.newBudgetVersionName = newBudgetVersionName;
    }

    /**
     * Used to indicate to the values finder whether the role has already been rendered
     * 
     * @return true if the role has been rendered already, false otherwise
     */
    public boolean isNewProposalPersonRoleRendered() {
        return newProposalPersonRoleRendered;
    }

    /**
     * Used to indicate to the values finder whether the role has already been rendered
     * 
     * @param newProposalPersonRoleRendered
     */
    public void setNewProposalPersonRoleRendered(boolean newProposalPersonRoleRendered) {
        this.newProposalPersonRoleRendered = newProposalPersonRoleRendered;
    }
    
    /**
     * Get the Header Dispatch.  This determines the action that will occur
     * when the user switches tabs for a proposal.  If the user can modify
     * the proposal, the proposal is automatically saved.  If not (view-only),
     * then a reload will be executed instead.
     * @return the Header Dispatch action
     */
    public String getHeaderDispatch() {
        return this.getDocumentActionFlags().getCanSave() ? "save" : "reload";
    }

    /**
     * Set the New Narrative User Rights.  This is displayed on the View/Edit Rights
     * web page for attachments.
     * @param newNarrativeUserRights the new narrativer user rights
     */
    public void setNewNarrativeUserRights(List<NarrativeUserRights> newNarrativeUserRights) {
        this.newNarrativeUserRights = newNarrativeUserRights;
    }
    
    /**
     * Get the New Narrative User Rights.
     * @return the new narrative user rights
     */
    public List<NarrativeUserRights> getNewNarrativeUserRights() {
        return this.newNarrativeUserRights;
    }
    
    /**
     * Get a New Narrative User Right.
     * @param index the index into the list of narrative user rights
     * @return a new narrative user right
     */
    public NarrativeUserRights getNewNarrativeUserRight(int index) {
        return this.newNarrativeUserRights.get(index);
    }

    public S2sOpportunity getNewS2sOpportunity() {
        return newS2sOpportunity;
    }

    public void setNewS2sOpportunity(S2sOpportunity newS2sOpportunity) {
        this.newS2sOpportunity = newS2sOpportunity;
    }
    
    public List<S2sAppSubmission> getNewS2sAppSubmission() {
        return newS2sAppSubmission;
    }

    public void setNewS2sAppSubmission(List<S2sAppSubmission> newS2sAppSubmission) {
        this.newS2sAppSubmission = newS2sAppSubmission;
    }
    
    /**
     * Set the original list of narratives for comparison when a save occurs.
     * @param narratives the list of narratives
     */
    public void setNarratives(List<Narrative> narratives) {
        this.narratives = narratives;
    }
    
    /**
     * Get the original list of narratives.
     * @return the original list of narratives
     */
    public List<Narrative> getNarratives() {
        return this.narratives;
    }

    public boolean isReject() {
        return reject;
    }

    public void setReject(boolean reject) {
        this.reject = reject;
    }
    
    public List<ExtraButton> getExtraActionsButtons() {
        // clear out the extra buttons array
        extraButtons.clear();
        boolean showSubmitButton = true;
        boolean showResubmitButton = true;
        ProposalDevelopmentDocument doc = this.getProposalDevelopmentDocument();
        if(doc.getS2sSubmissionHistory()!=null && doc.getS2sSubmissionHistory().size()!=0){
            for(S2sSubmissionHistory s2sSubmissionHistory:doc.getS2sSubmissionHistory()){
                if(StringUtils.equalsIgnoreCase(s2sSubmissionHistory.getProposalNumberOrig(),doc.getProposalNumber())){
                    showSubmitButton=false;
                }
                if(StringUtils.equalsIgnoreCase(s2sSubmissionHistory.getOriginalProposalId() ,doc.getProposalNumber())){
                    showResubmitButton=false;
                }
            }
        } else if (doc.getSubmitFlag()) {
            /*
             * If we get here, we have a non-electronic submission which doesn't have a submission history.
             */
            showSubmitButton = false;
            showResubmitButton = false;
        }
        else {
            showResubmitButton=false;
        }  
        
        String externalImageURL = "kra.externalizable.images.url";
        if(showSubmitButton){
            String submitToGrantsGovImage = KraServiceLocator.getService(KualiConfigurationService.class).getPropertyString(externalImageURL) + "buttonsmall_sponsorsubmit.gif";
            addExtraButton("methodToCall.submitToSponsor", submitToGrantsGovImage, "Submit To Sponsor");
        }else if(showResubmitButton){
            String resubmissionImage = KraServiceLocator.getService(KualiConfigurationService.class).getPropertyString(externalImageURL) + "replaceproposal.gif";
            addExtraButton("methodToCall.resubmit", resubmissionImage, "Replace Sponsor");
        }       
        
        return extraButtons;
    }
    
    /**
     * This is a utility method to add a new button to the extra buttons
     * collection.
     *   
     * @param property
     * @param source
     * @param altText
     */ 
    protected void addExtraButton(String property, String source, String altText){
        
        ExtraButton newButton = new ExtraButton();
        
        newButton.setExtraButtonProperty(property);
        newButton.setExtraButtonSource(source);
        newButton.setExtraButtonAltText(altText);
        
        extraButtons.add(newButton);
    }
    
    public KualiConfigurationService getConfigurationService() {
        return getService(KualiConfigurationService.class);
    }

    /**
     * Overridden to force business logic even after validation failures. In this case we want to force the enabling of credit split.
     * 
     * @see org.kuali.core.web.struts.pojo.PojoFormBase#processValidationFail()
     */
    @Override
    public void processValidationFail() {
        try {
            boolean creditSplitEnabled = getConfigurationService().getIndicatorParameter(PARAMETER_MODULE_PROPOSAL_DEVELOPMENT, PARAMETER_COMPONENT_DOCUMENT, CREDIT_SPLIT_ENABLED_RULE_NAME)
                && getProposalDevelopmentDocument().getInvestigators().size() > 0;
            setCreditSplitEnabled(creditSplitEnabled);
        }
        catch (Exception e) {
            warn(MISSING_PARAM_MSG, CREDIT_SPLIT_ENABLED_RULE_NAME);
            warn(e.getMessage());
        }
    }

    /**
     * Gets the creditSplitEnabled attribute. 
     * @return Returns the creditSplitEnabled.
     */
    public boolean isCreditSplitEnabled() {
        return creditSplitEnabled;
    }

    /**
     * Sets the creditSplitEnabled attribute value.
     * @param creditSplitEnabled The creditSplitEnabled to set.
     */
    public void setCreditSplitEnabled(boolean creditSplitEnabled) {
        this.creditSplitEnabled = creditSplitEnabled;
    }

    public List<KeyLabelPair> getExemptNumberList() {
        return exemptNumberList;
    }

    public void setExemptNumberList(List<KeyLabelPair> exemptNumberList) {
        this.exemptNumberList = exemptNumberList;
    }

    public String[] getNewExemptNumbers() {
        return newExemptNumbers;
    }

    public void setNewExemptNumbers(String[] newExemptNumbers) {
        this.newExemptNumbers = newExemptNumbers;
    }
    
    /**
     * 
     * This method is to reset the exempt numbers in proposalspecialreview.  
     * If no exempt number is selected, then it's not in request parameter's list.  So,
     * The old exempt number will not be reset to null.  This is just a way to reset it.
     * TODO : any other option?
     */
    private void resetExemptNumbers() {
        List <String[]> documentExemptNumbers = this.getDocumentExemptNumbers();
        int  i =0;
        if (documentExemptNumbers != null) {
            for (String[] exemptNumbers : documentExemptNumbers) {
                documentExemptNumbers.set(i++, null);
            }
        }
    }

    public List<String[]> getDocumentExemptNumbers() {
        return documentExemptNumbers;
    }

    public void setDocumentExemptNumbers(List<String[]> documentExemptNumbers) {
        this.documentExemptNumbers = documentExemptNumbers;
    }

    public String getOptInUnitDetails() {
        return optInUnitDetails;
    }

    public void setOptInUnitDetails(String optInUnitDetails) {
        this.optInUnitDetails = optInUnitDetails;
    }

    public String getOptInCertificationStatus() {
        return optInCertificationStatus;
    }

    public void setOptInCertificationStatus(String optInCertificationStatus) {
        this.optInCertificationStatus = optInCertificationStatus;
    }
    
    public ProposalChangedData getNewProposalChangedData() {
        return newProposalChangedData;
    }

    public void setNewProposalChangedData(ProposalChangedData newProposalChangedData) {
        this.newProposalChangedData = newProposalChangedData;
    }
    
    public boolean isSubmissionStatusVisible() {
        String routeStatus = this.getProposalDevelopmentDocument().getDocumentHeader().getWorkflowDocument().getRouteHeader()
        .getDocRouteStatus();
        return EdenConstants.ROUTE_HEADER_PROCESSED_CD.equals(routeStatus) || EdenConstants.ROUTE_HEADER_FINAL_CD.equals(routeStatus);
    }
    
    public boolean isSubmissionStatusReadOnly() {
        UniversalUser user = GlobalVariables.getUserSession().getUniversalUser();
        ProposalAuthorizationService proposalAuthService = KraServiceLocator.getService(ProposalAuthorizationService.class);
        boolean canModify = proposalAuthService.hasPermission(user.getPersonUserIdentifier(), this.getProposalDevelopmentDocument(), PermissionConstants.MODIFY_PROPOSAL);
        if (canModify) { return false; }
        List<KualiGroup> groups = user.getGroups();
        for (KualiGroup group: groups) {
            if (group.getGroupName().equals("OSP")) {
                return false;
            }
        }
        return true;
    }
    
    public final String getProposalFormTabTitle() {
        String totalForms = getProposalDevelopmentDocument().getSponsorFormTemplates().size() + "";
        return proposalFormTabTitle.concat("(" + totalForms + ")");
    }

    public final void setProposalFormTabTitle(String proposalFormTabTitle) {
        this.proposalFormTabTitle = proposalFormTabTitle;
    }
 

}
