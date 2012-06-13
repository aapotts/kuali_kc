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
package org.kuali.kra.krms.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.bo.SponsorHierarchy;
import org.kuali.kra.bo.UnitAdministrator;
import org.kuali.kra.budget.core.Budget;
import org.kuali.kra.budget.document.BudgetDocument;
import org.kuali.kra.budget.nonpersonnel.BudgetLineItem;
import org.kuali.kra.budget.parameters.BudgetPeriod;
import org.kuali.kra.budget.versions.BudgetDocumentVersion;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.krms.service.KcKrmsJavaFunctionTermService;
import org.kuali.kra.proposaldevelopment.bo.DevelopmentProposal;
import org.kuali.kra.proposaldevelopment.bo.Narrative;
import org.kuali.kra.proposaldevelopment.bo.ProposalPerson;
import org.kuali.kra.proposaldevelopment.bo.ProposalPersonBiography;
import org.kuali.kra.s2s.bo.S2sOppForms;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;

public class KcKrmsJavaFunctionTermServiceImpl implements KcKrmsJavaFunctionTermService {
    
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private BusinessObjectService businessObjectService;

    /**
     * 
     * This method checks if the formName is included in the given proposal
     * @param developmentProposal
     * @return 'true' if true
     */
    @Override
    public String specifiedGGForm(DevelopmentProposal developmentProposal, String formNames) {
        String[] formNamesArray = buildArrayFromCommaList(formNames);
        developmentProposal.refreshReferenceObject("s2sOppForms");
        List<S2sOppForms> s2sOppForms = developmentProposal.getS2sOppForms();
        for (int i = 0; i < formNamesArray.length; i++) {
            String formName = formNamesArray[i].trim();
            for (S2sOppForms s2sOppForm : s2sOppForms) {
                if(s2sOppForm.getInclude() && s2sOppForm.getFormName().equals(formName)){
                    return TRUE;
                }
            }
        }
        return FALSE;
    }
    
    /**
     * This method checks if the proposal has multiple PIs set.
     * see FN_MULTIPI_RULE.
     * @param developmentProposal
     * @return 'true' if true
     */
    @Override
    public String multiplePI(DevelopmentProposal developmentProposal) {
        List<ProposalPerson> people = developmentProposal.getProposalPersons();
        for (ProposalPerson person : people) {
            if (person.isMultiplePi()) {
                return TRUE;
            }
        }
        return FALSE;
        
    }
    /**
     * 
     * This method checks if the proposal has multiple PIs set.
     * see FN_S2S_BUDGET_RULE.
     * @param developmentProposal
     * @param formName a comma delimited list of s2s forms to check against.
     * @return 'true' if true
     */
    @Override
    public String s2sBudgetRule(DevelopmentProposal developmentProposal, String formNames){
        /**
         * F.FORM_NAME in ('RR Budget V1-1','RR SubAward Budget V1.2','RR_FedNonFed_SubawardBudget-V1.2',
         * 'RR_FedNonFed_SubawardBudget-V1.1','RR SubAward Budget V1.1','PHS398 Modular Budget V1-1', 'PHS398 Modular Budget V1-2')
         */
        String[] formNamesArray = buildArrayFromCommaList(formNames);
        int li_count_bud = 0;
        for (String formName : formNamesArray) {
            for(S2sOppForms form: developmentProposal.getS2sOppForms()) {
                if (StringUtils.equalsIgnoreCase(formName, form.getFormName()) && form.getInclude()) {
                    li_count_bud++;
                }
            }
        }
        int li_count_s2s = developmentProposal.getS2sOpportunity() != null ? 1 : 0;
        
        if (li_count_bud != 0 && li_count_s2s <= 0) {
            return TRUE;
        }
        return FALSE;
    }
    
    /**
     * 
     * This method checks if the proposal is associated with one of monitored sponsored hierarchies. 
     * see FN_COI_MONITORED_SPONSOR_RULE.
     * @param developmentProposal
     * @param monitoredSponsorHirearchies a comma delimited list of sponsored hirearchies.
     * @return 'true' if true
     */
    @Override
    public String monitoredSponsorRule(DevelopmentProposal developmentProposal, String monitoredSponsorHirearchies) {
        String[] sponsoredHierarchyArray = buildArrayFromCommaList(monitoredSponsorHirearchies);
        ArrayList<SponsorHierarchy> hierarchies = new ArrayList<SponsorHierarchy>();
        for (String hierarchyName : sponsoredHierarchyArray) {
            Map fieldValues = new HashMap();
            fieldValues.put("HIERARCH_NAME", hierarchyName);
            hierarchies.addAll(this.getBusinessObjectService().findMatching(SponsorHierarchy.class, fieldValues));
        }
        for (SponsorHierarchy sh : hierarchies) {
            if (StringUtils.equalsIgnoreCase(sh.getSponsorCode(), developmentProposal.getSponsor().getSponsorCode())
                    || StringUtils.equalsIgnoreCase(sh.getSponsorCode(), developmentProposal.getPrimeSponsor().getSponsorCode())) {
                return TRUE;
            }
        }
        
        return FALSE;
    }
    
    /**
     * 
     * This method determines if the proposal has more than the maximum number of attachments of the types provided in the narativeTypes list.
     * see FN_S2S_RESPLAN_RULE.
     * @param developmentProposal.
     * @param narativeTypes a comma delimited list of narrative types.
     * @param maxNumber the maximum number to check.
     * @return 'true' if true
     */
    @Override
    public String s2sReplanRule(DevelopmentProposal developmentProposal, String narativeTypes, String maxNumber) {
        //- max of 10 narrative types PHS_ResearchPlan_Appendix
        String[] narrativeTypesArray = buildArrayFromCommaList(narativeTypes);
        int[] narrativeCounts = new int[narrativeTypesArray.length];
        int maxNumberInt = Integer.parseInt(maxNumber);
        for (Narrative narrative : developmentProposal.getNarratives()) {
            int narrativePosition = 0;
            for (String narrativeType : narrativeTypesArray) {
                if (StringUtils.equalsIgnoreCase(narrativeType, narrative.getNarrativeType().getDescription())) {
                    narrativeCounts[narrativePosition]++;
                    if (narrativeCounts[narrativePosition] > maxNumberInt) {
                        return TRUE;
                    }
                }
                narrativePosition++;
            }
        }
        return FALSE;
    }
    
    /**
     * 
     * This method checks if the proposal is associated the grants form passed in. 
     * see FN_GG_FORM_RULE.
     * @param developmentProposal
     * @param formName the grants form to check against.
     * @return 'true' if true
     */
    @Override
    public String grantsFormRule(DevelopmentProposal developmentProposal, String formName) {
        for(S2sOppForms form: developmentProposal.getS2sOppForms()) {
            if (StringUtils.equalsIgnoreCase(formName, form.getFormName())) {
                return TRUE;
            }
        }
        return FALSE;
    }
    
    /**
     * 
     * This method checks to see if the biosketch file names contain any restricted special characters.
     * See  fn_prop_pers_att_name_rule 
     * @param developmentProposal
     * @param restrictedSpecialCharacters comma delimited list of spcial characters to restrict.
     * @return 'true' if no special characters are found.
     */
    @Override
    public String biosketchFileNameRule(DevelopmentProposal developmentProposal, String restrictedSpecialCharacters) {
        
        /**
         * i_Pos := instr(ls_FileName, ' ') +
                      instr(ls_FileName, '`') +
                      instr(ls_FileName, '@') +
                      instr(ls_FileName, '#') +
                      instr(ls_FileName, '!') +
                      instr(ls_FileName, '$') +
                      instr(ls_FileName, '%') +
                      instr(ls_FileName, '^') +
                      instr(ls_FileName, '&') +
                      instr(ls_FileName, '*') +
                      instr(ls_FileName, '(') +
                      instr(ls_FileName, ')') +
                      instr(ls_FileName, '{') +
                      instr(ls_FileName, '}') +
                      instr(ls_FileName, '[') +
                      instr(ls_FileName, ']') +
                      instr(ls_FileName, '|') +
                      instr(ls_FileName, '\') +
                      instr(ls_FileName, '/') +
                      instr(ls_FileName, '?') +
                      instr(ls_FileName, '<') +
                      instr(ls_FileName, '>') +
                      instr(ls_FileName, ',') +
                      instr(ls_FileName, ';') +
                      instr(ls_FileName, ':') +
                      instr(ls_FileName, '"') +
                      instr(ls_FileName, '''') +
                      instr(ls_FileName, '`') +
                      instr(ls_FileName, '+') ;
         */
        String[] restrictedSpecialCharactersArray = buildArrayFromCommaList(restrictedSpecialCharacters);
        for (ProposalPersonBiography ppb : developmentProposal.getPropPersonBios()) {
            if (StringUtils.equalsIgnoreCase(ppb.getPropPerDocType().getDescription(), "Biosketch")) {
                for (String character : restrictedSpecialCharactersArray) {
                    if (StringUtils.equalsIgnoreCase(character, ppb.getFileName())) {
                        return FALSE;
                    }
                }
            }
        }
        return TRUE;
        
    }
    
    /**
     * 
     * This method checks to see if the OSP administrator is also a personal person.
     * See  FN_OSP_ADMIN_IS_PERSON 
     * @param developmentProposal
     * @return 'true' if the OSP admin is also a proposal person, otherwise 'false'
     */
    @Override
    public String ospAdminPropPersonRule(DevelopmentProposal developmentProposal) {
        List<UnitAdministrator> ospAdmins = developmentProposal.getUnit().getUnitAdministrators();
        if (ospAdmins != null && ospAdmins.size() > 0) {
            for (ProposalPerson person : developmentProposal.getProposalPersons()) {
                for (UnitAdministrator admin : ospAdmins) {
                    if (StringUtils.equals(person.getPersonId(), admin.getPersonId())) {
                        return TRUE;
                    }
                }
            }
        }
        return FALSE;
    }
    
    /**
     * 
     * This method is used to check if , in any period of the given version of budget, the given cost element has crossed the given limit or not.
     * See  fn_cost_element_ver_per_limit 
     * @param developmentProposal
     * @param versionNumber the version number to be checked
     * @param costElementName the cost element to be checked
     * @param limit the amount limitto be checked
     * @return 'true' - if the total cost of the CE crossed the limit in any one of the period, otherwise 'false'
     */
    @Override
    public String costElementVersionLimit(DevelopmentProposal developmentProposal, String versionNumber, String costElementName, String limit) {
        Long versionNumberLong = Long.parseLong(versionNumber);
        float limitLong = Float.parseFloat(limit);
        for (BudgetDocumentVersion bdv : developmentProposal.getProposalDocument().getBudgetDocumentVersions()) {
            if (bdv.getVersionNumber().equals(versionNumberLong)) {
                try {
                    DocumentService documentService = KraServiceLocator.getService(DocumentService.class);
                    BudgetDocument budgetDocument = (BudgetDocument) documentService.getByDocumentHeaderId(bdv.getDocumentNumber());
                    List<Budget> budgets = budgetDocument.getBudgets();
                    for (Budget budget : budgets) {
                        if (budget.getVersionNumber().equals(versionNumberLong)) {
                            for (BudgetPeriod period : budget.getBudgetPeriods()) {
                                float costElementTotal = 0;
                                for (BudgetLineItem item : period.getBudgetLineItems()) {
                                    if (StringUtils.equalsIgnoreCase(costElementName, item.getCostElementName())) {
                                        costElementTotal = costElementTotal + item.getLineItemCost().getFloatValue();
                                    }
                                }
                                if (costElementTotal > limitLong) {
                                    return TRUE;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    //lets just ignor and return false.
                }
            }
        }
        return FALSE;
    }
    
    /**
     * 
     * This method is used to CHECK IF a proposal's division code field  is  null
     * See  fn_agency_divcode_is_null_rule 
     * @param developmentProposal
     * @return 'true' - if the division field is null, otherwise return false.
     */
    @Override
    public String divisionCodeRule(DevelopmentProposal developmentProposal) {
        return StringUtils.isEmpty(developmentProposal.getActivityTypeCode()) ? TRUE : FALSE;
    }
    
    /**
     * 
     * This method is used to CHECK IF THIS PROPOSAL IS FOR A FELLOWSHIP.  Typically 3 or 7 is a fellowship code.
     * See  FN_IS_FELLOWSHIP 
     * @param developmentProposal
     * @return 'true' - if the division field is a fellowship code, otherwise return false.
     */
    @Override
    public String divisionCodeIsFellowship(DevelopmentProposal developmentProposal, String fellowshipCodes) {
        String[] fellowShipCodeArray = buildArrayFromCommaList(fellowshipCodes);
        for (String code : fellowShipCodeArray) {
            if (StringUtils.equalsIgnoreCase(code, developmentProposal.getActivityTypeCode())) {
                return TRUE;
            }
        }
        return FALSE;
    }
    
    protected String[] buildArrayFromCommaList(String commaList) {
        String[] newArray = commaList.split(","); //MIT Equity Interests
        if(commaList!=null && newArray.length==0){
            newArray = new String[]{commaList.trim()};
        }
        return newArray;
    }
    

    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
    
}
 