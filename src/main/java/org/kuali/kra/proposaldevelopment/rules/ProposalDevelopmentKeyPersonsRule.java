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
package org.kuali.kra.proposaldevelopment.rules;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.kuali.core.util.GlobalVariables.getErrorMap;
import static org.kuali.kra.infrastructure.KeyConstants.ERROR_ADD_EXISTING_UNIT;
import static org.kuali.kra.infrastructure.KeyConstants.ERROR_DELETE_LEAD_UNIT;
import static org.kuali.kra.infrastructure.KeyConstants.ERROR_INVESTIGATOR_UPBOUND;
import static org.kuali.kra.infrastructure.KeyConstants.ERROR_MISSING_PERSON_ROLE;
import static org.kuali.kra.infrastructure.KeyConstants.ERROR_PROPOSAL_PERSON_EXISTS;
import static org.kuali.kra.infrastructure.KeyConstants.ERROR_INVALID_YEAR;
import static org.kuali.kra.infrastructure.KeyConstants.ERROR_INVALID_UNIT;
import static org.kuali.kra.infrastructure.KeyConstants.ERROR_SELECT_UNIT;
import static org.kuali.kra.infrastructure.KeyConstants.ERROR_ONE_UNIT;
import static org.kuali.kra.infrastructure.KeyConstants.ERROR_PERCENTAGE;
import static org.kuali.kra.infrastructure.KraServiceLocator.getService;
import static org.kuali.kra.logging.FormattedLogger.debug;
import static org.kuali.kra.logging.FormattedLogger.info;
import static org.kuali.kra.infrastructure.Constants.PRINCIPAL_INVESTIGATOR_ROLE;
import org.apache.commons.lang.ObjectUtils;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.RiceKeyConstants;
import org.kuali.core.bo.BusinessObject;
import org.kuali.core.document.Document;
import org.kuali.core.util.GlobalVariables;
import org.kuali.kra.bo.DegreeType;
import org.kuali.kra.bo.Person;
import org.kuali.kra.bo.Rolodex;
import org.kuali.kra.bo.Unit;
import org.kuali.kra.proposaldevelopment.bo.PropScienceKeyword;
import org.kuali.kra.proposaldevelopment.bo.ProposalPerson;
import org.kuali.kra.proposaldevelopment.bo.ProposalPersonCreditSplit;
import org.kuali.kra.proposaldevelopment.bo.ProposalPersonDegree;
import org.kuali.kra.proposaldevelopment.bo.ProposalPersonUnit;
import org.kuali.kra.proposaldevelopment.bo.ProposalUnitCreditSplit;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.proposaldevelopment.rule.AddKeyPersonRule;
import org.kuali.kra.proposaldevelopment.rule.CalculateCreditSplitRule;
import org.kuali.kra.proposaldevelopment.rule.ChangeKeyPersonRule;
import org.kuali.kra.proposaldevelopment.service.KeyPersonnelService;
import org.kuali.kra.proposaldevelopment.service.ProposalPersonService;
import org.kuali.kra.rules.ResearchDocumentRuleBase;

/**
 * Implementation of business rules required for the Key Persons Page of the 
 * <code>{@link org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument}</code>.
 *
 * @see org.kuali.core.rules.BusinessRule
 * @author $Author: jsalam $
 * @version $Revision: 1.33.2.9 $
 */
public class ProposalDevelopmentKeyPersonsRule extends ResearchDocumentRuleBase implements AddKeyPersonRule, ChangeKeyPersonRule,CalculateCreditSplitRule  {
    private static final String PERSON_HAS_UNIT_MSG = "Person %s has unit %s";
    private static final String PROPOSAL_PERSON_KEY = "document.proposalPerson[%d]";
    
    private String formatMessageKey(String key, Object ... objs) {
        StringWriter retval = new StringWriter();
        new PrintWriter(retval).printf(key, objs);
        
        return retval.toString();
    }

    /**
     * @see ResearchDocumentRuleBase#processCustomSaveDocumentBusinessRules(Document)
     */
    @Override
    protected boolean processCustomSaveDocumentBusinessRules(Document document) {
        return processSaveKeyPersonBusinessRules((ProposalDevelopmentDocument) document);
    }

    /**
     * Rule invoked upon saving persons to a 
     * <code>{@link org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument}</code>
     *
     * @param document ProposalDevelopmentDocument being saved
     * @return boolean
     */
    public boolean processSaveKeyPersonBusinessRules(ProposalDevelopmentDocument document) {
        info("Processing Key Personnel Save Document Rule");
        boolean retval = true;
        int pi_cnt = 0;
        int personIndex = 0;
        List<ProposalPerson> investigators=document.getInvestigators();
        String reg="^(100(?:\\.0{1,2})?|0*?\\.\\d{1,2}|\\d{1,2}(?:\\.\\d{1,2})?)$"; 
       
               
        for (ProposalPerson person : document.getProposalPersons()) {
            if (isPrincipalInvestigator(person)) {
                pi_cnt++;
                 
            }
            
            if (isBlank(person.getProposalPersonRoleId()) && person.getRole() == null) { 
                debug("error.missingPersonRole");
                reportError(formatMessageKey(PROPOSAL_PERSON_KEY, personIndex), ERROR_MISSING_PERSON_ROLE);
            }
            personIndex++;
        }

        if (pi_cnt > 1) {
            retval = false;
            reportError("newProposalPerson*", ERROR_INVESTIGATOR_UPBOUND, getKeyPersonnelService().getPrincipalInvestigatorRoleDescription(document));            
        }        
        personIndex=0;
        for (ProposalPerson person : document.getProposalPersons()) {

            if(isCoInvestigator(person) && (person.getUnits() != null) && (person.getUnits().size()==0)){
                reportError("newProposalPerson*", ERROR_ONE_UNIT, person.getFullName());            
            }
            if(isKeyPerson(person) && (person.getOptInUnitStatus().equals("Y")) && (person.getUnits()!= null) && (person.getUnits().size() ==0)){
                reportError("newProposalPerson*", ERROR_ONE_UNIT, person.getFullName());  
            }
            if(isKeyPerson(person) && StringUtils.isBlank(person.getProjectRole())){
                reportError("document.proposalPersons[" + personIndex + "].projectRole",RiceKeyConstants.ERROR_REQUIRED,"Key Person Role");
            }
            if(person.getPercentageEffort()!= null){
                String percentageeffort=String.valueOf(person.getPercentageEffort().intValue());
                if(!(percentageeffort.matches(reg))){
                    GlobalVariables.getErrorMap().putError("document.proposalPersons[" + personIndex + "].percentageEffort", ERROR_PERCENTAGE,
                            new String[] {"Percentage Effort" });
                }
            }
            personIndex++;
        }
        for (Iterator iter = investigators.iterator(); iter.hasNext();) {
            ProposalPerson propPerson = (ProposalPerson) iter.next();
            List<ProposalPersonCreditSplit> personCreditSplit=propPerson.getCreditSplits();
            List<ProposalPersonUnit> propUnitCreditSplit=propPerson.getUnits();
            int i=0;
            for (ProposalPersonCreditSplit creditSplit : personCreditSplit) {
                if(creditSplit.getCredit() !=null){

                    String credit= String.valueOf( creditSplit.getCredit().intValue());
                    if(!(credit.matches(reg))){
                        GlobalVariables.getErrorMap().putError("document.creditSplit", ERROR_PERCENTAGE,
                                new String[] {"Credit Split" });
                        retval=false;
                    }
                }
                
           }
            for(ProposalPersonUnit personUnitSplit:propUnitCreditSplit){
                List<ProposalUnitCreditSplit> unitcreditsplit=personUnitSplit.getCreditSplits();
                for(Iterator it=unitcreditsplit.iterator();it.hasNext();){
                    ProposalUnitCreditSplit unitsplit=(ProposalUnitCreditSplit)it.next();
                    if(unitsplit.getCredit()!= null){
                        String credit=String.valueOf(unitsplit.getCredit().intValue());
                        if(!(credit.matches(reg))){
                            GlobalVariables.getErrorMap().putError("document.creditSplit", ERROR_PERCENTAGE,
                                    new String[] {"Credit Split" });
                            //GlobalVariables.getErrorMap().putError("document.creditSplit", ERROR_PERCENTAGE);
                            retval=false; 
                        }
                        
                    }
                }
             }
           i++;
        }
        
      
    return retval;
    }

    /**
     * Validate the following
     * 
     * <ul>
     *   <li>One principal investigator at a time</li>
     *   <li>0 or more Key Persons or Co-Investigators are allowed</li>
     *   <li>A person cannot have multiple roles, ie. a person can only be added as a key person once.</li>
     * </ul>
     * @see org.kuali.kra.proposaldevelopment.rule.AddKeyPersonRule#processAddKeyPersonBusinessRules(ProposalDevelopmentDocument,ProposalPerson)
     */
    public boolean processAddKeyPersonBusinessRules(ProposalDevelopmentDocument document, ProposalPerson person) {
        boolean retval = true;
        
        debug("validating " + person);
        info("Person role is " + person.getRole());

        if (isPrincipalInvestigator(person) && hasPrincipalInvestigator(document)) {
            debug("error.principalInvestigator.limit");
            reportError("newProposalPerson*", ERROR_INVESTIGATOR_UPBOUND, getKeyPersonnelService().getPrincipalInvestigatorRoleDescription(document));
            retval = false;
        }
        info("roleid is %s", person.getProposalPersonRoleId());
        info("role is %s", person.getRole());
        if (isBlank(person.getProposalPersonRoleId()) && person.getRole() == null) {
            debug("Tried to add person without role");
            reportError("newProposalPerson*", ERROR_MISSING_PERSON_ROLE);
            retval = false;
        }
        
        debug("Does document contain a proposal person with PERSON_ID " + person.getPersonId() + "?");
        debug(document.getProposalPersons().contains(person)+ "");
        
        if (document.getProposalPersons().contains(person)) {
            reportError("newProposalPerson*", ERROR_PROPOSAL_PERSON_EXISTS, person.getFullName());
            retval = false;
        }
        
        if(isNotBlank(person.getProposalPersonRoleId())){
            if (isInvalid(Person.class, keyValue("personId", person.getPersonId())) 
                    && isInvalid(Rolodex.class, keyValue("rolodexId", person.getRolodexId()))) {
                reportError("newProposalPerson*", ERROR_MISSING_PERSON_ROLE, person.getFullName());
                retval = false;
            }
        }
        return retval;
    }
            
    /**
     * @see org.kuali.kra.rules.ResearchDocumentRuleBase#reportError(String, String, String...)
     */
    protected void reportErrorWithPrefix(String errorPathPrefix, String propertyName, String errorKey, String... errorParams) {
        getErrorMap().addToErrorPath(errorPathPrefix);
        super.reportError(propertyName, errorKey, errorParams);
        getErrorMap().removeFromErrorPath(errorPathPrefix);        
    }

    /**
     * @see KeyPersonnelService#isPrincipalInvestigator(ProposalPerson)
     */
    private boolean isPrincipalInvestigator(ProposalPerson person) {
        return getKeyPersonnelService().isPrincipalInvestigator(person);
    }

    /**
     * @see KeyPersonnelService#isPrincipalInvestigator(ProposalPerson)
     */
    private boolean hasPrincipalInvestigator(ProposalDevelopmentDocument document) {
        return getKeyPersonnelService().hasPrincipalInvestigator(document);
    }

    /**
     * @see KeyPersonnelService#isCoInvestigator(ProposalPerson)
     */
    private boolean isCoInvestigator(ProposalPerson person){
        return getKeyPersonnelService().isCoInvestigator(person);
    }
    
    /**
     * @see KeyPersonnelService#isCoInvestigator(ProposalPerson)
     */
    private boolean isKeyPerson(ProposalPerson person){
        return getKeyPersonnelService().isKeyPerson(person);
    }
    /**
     * Locate in Spring <code>{@link KeyPersonnelService}</code> singleton  
     * 
     * @return KeyPersonnelService
     */
    private KeyPersonnelService getKeyPersonnelService() {
        return getService(KeyPersonnelService.class);
    }

    /**
     * Either adding a degree or unit can trigger this rule to be validated
     * 
     * @see org.kuali.kra.proposaldevelopment.rule.ChangeKeyPersonRule#processChangeKeyPersonBusinessRules(org.kuali.kra.proposaldevelopment.bo.ProposalPerson, org.kuali.core.bo.BusinessObject)
     * @see org.kuali.kra.proposaldevelopment.web.struts.action.ProposalDevelopmentKeyPersonnelAction#insertDegree(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     * @see org.kuali.kra.proposaldevelopment.web.struts.action.ProposalDevelopmentKeyPersonnelAction#insertUnit(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public boolean processChangeKeyPersonBusinessRules(ProposalPerson proposalPerson, BusinessObject source,int index) {
        boolean retval = true;
        
        if (source instanceof ProposalPersonDegree) {
            retval &= validateDegree((ProposalPersonDegree) source,index);
        }
        else if (source instanceof ProposalPersonUnit) {
            retval &= validateUnit((ProposalPersonUnit) source, proposalPerson,index);
        }
        
        return retval;
    }

    /**
     * Checks to makes sure that the unit is valid. Usually called as a result of a <code>{@link ProposalPersonUnit}</code> being added to a <code>{@link ProposalPerson}</code>.
     * 
     * @param source
     * @return boolean pass or fail
     */
    private boolean validateUnit(ProposalPersonUnit source, ProposalPerson person,int index) {
        boolean retval = true;
        
        if (source == null) {
            debug("validated null unit");
            return false;
        }
        
        debug("Validating unit %s",  source);
       
        if (source.getUnit() == null && isBlank(source.getUnitNumber()) && (GlobalVariables.getErrorMap().getMessages("document.newProposalPersonUnit*")== null)) {
            GlobalVariables.getErrorMap().putError("document.proposalPersons[" + index + "].newProposalPersonUnit",ERROR_SELECT_UNIT);
            retval = false;
        }
        
        if (isNotBlank(source.getUnitNumber()) && isInvalid(Unit.class, keyValue("unitNumber", source.getUnitNumber())) && (GlobalVariables.getErrorMap().getMessages("document.newProposalPersonUnit*")== null)) {
            GlobalVariables.getErrorMap().putError("document.proposalPersons[" + index + "].newProposalPersonUnit", ERROR_INVALID_UNIT,
                    source.getUnitNumber(), person.getFullName());
            retval = false;
        }

        debug("isLeadUnit %s", source.isLeadUnit());
        if(source.isDelete()){
            if(person.getProposalPersonRoleId().equals(PRINCIPAL_INVESTIGATOR_ROLE)){
               if (isDeletingUnitFromPrincipalInvestigator(source, person)&& (GlobalVariables.getErrorMap().getMessages("document.newProposalPersonUnit*")== null)) {
                   GlobalVariables.getErrorMap().putError("document.proposalPersons[" + index + "].newProposalPersonUnit", ERROR_DELETE_LEAD_UNIT,
                           source.getUnitNumber(), person.getFullName());
                retval = false;
               }
            }
        }else
        {
        if((unitExists(source , person)) && (GlobalVariables.getErrorMap().getMessages("document.newProposalPersonUnit*")== null)){
            GlobalVariables.getErrorMap().putError("document.proposalPersons[" + index + "].newProposalPersonUnit", ERROR_ADD_EXISTING_UNIT,
                     source.getUnitNumber(), person.getFullName());
            retval=false;
        }
        }
         debug("validateUnit = %s", retval);
        
        return retval;
    }
    
    /**
     * Determine whether the <code>{@link ProposalPersonUnit}</code> already exists by Unit Number in the 
     * given <code>{@link ProposalPerson}</code>
     * 
     * @param source
     * @param person
     * @return true or false
     */
    private boolean unitExists(ProposalPersonUnit source, ProposalPerson person) {
        for (ProposalPersonUnit unit : person.getUnits()) {
            if (unit.getUnitNumber().equals(source.getUnitNumber())) { 
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Determine if we are deleting a Lead Unit from a PI
     * 
     * @param unit intending to be deleted
     * @param person possible PI
     * @return boolean
     */
    private boolean isDeletingUnitFromPrincipalInvestigator(ProposalPersonUnit unit, ProposalPerson person) {
        boolean retval = false;
        
        for (Iterator<ProposalPersonUnit> unit_it = person.getUnits().iterator(); unit_it.hasNext() && !retval;) {
            retval = unit_it.next().getUnitNumber().equals(unit.getUnitNumber());
        }
        
        info(PERSON_HAS_UNIT_MSG, person.getProposalPersonNumber(), unit.getUnitNumber());
        
        return retval && unit.isDelete() && unit.isLeadUnit() && getKeyPersonnelService().isPrincipalInvestigator(person);
    }

    /**
     * Checks to makes sure that the degree is valid. Usually called as a result of a <code>{@link ProposalPersonDegree}</code> being added to a <code>{@link ProposalPerson}</code>.
     * 
     * @param source
     * @return boolean
     */
    private boolean validateDegree(ProposalPersonDegree source,int index) {
        boolean retval = true;
     
        String regExpr = "^(16|17|18|19|20)[0-9]{2}$";
        if(source.getGraduationYear()!=null && !(source.getGraduationYear().matches(regExpr)) && GlobalVariables.getErrorMap().getMessages("document.newProposalPersonDegree") == null)
        {            
            GlobalVariables.getErrorMap().putError("document.proposalPersons[" + index + "].newProposalPersonDegree", ERROR_INVALID_YEAR,
                    new String[] { source.getGraduationYear() });
            retval = false;
        }

        if (source == null) {
            return false;
        }
        
        if (isNotBlank(source.getDegreeCode()) && isInvalid(DegreeType.class, keyValue("degreeCode", source.getDegreeCode()))) {
            retval = false;
        }
        if(StringUtils.isBlank(source.getDegreeCode())){
            GlobalVariables.getErrorMap().putError("document.proposalPersons[" + index + "].newProposalPersonDegree", RiceKeyConstants.ERROR_REQUIRED,
                    new String[] {"Degree Type" });
            retval= false;
        }

        if(StringUtils.isBlank(source.getDegree())){

            GlobalVariables.getErrorMap().putError("document.proposalPersons[" + index + "].newProposalPersonDegree", RiceKeyConstants.ERROR_REQUIRED,
                    new String[] {"Degree Description" });
            retval= false;
        }
        
        
        if(StringUtils.isBlank(source.getGraduationYear())){

            GlobalVariables.getErrorMap().putError("document.proposalPersons[" + index + "].newProposalPersonDegree", RiceKeyConstants.ERROR_REQUIRED,
                    new String[] {"Graduation year" });
            retval= false;
        }
        
        
        return retval;
    }
    
    /**
     * Locate <code>{@link ProposalPersonService}</code> instance withing Spring and return it.
     * 
     * @return ProposalPersonService
     */
    private ProposalPersonService getProposalPersonService() {
        return getService(ProposalPersonService.class);
    }

    public boolean processCalculateCreditSplitBusinessRules(ProposalDevelopmentDocument document) {

        List<ProposalPerson> person=document.getInvestigators();
        String reg="^(100(?:\\.0{1,2})?|0*?\\.\\d{1,2}|\\d{1,2}(?:\\.\\d{1,2})?)$"; 
        boolean retval=true;
   

        for (Iterator iter = person.iterator(); iter.hasNext();) {
            ProposalPerson propPerson = (ProposalPerson) iter.next();
            List<ProposalPersonCreditSplit> personCreditSplit=propPerson.getCreditSplits();
            List<ProposalPersonUnit> propUnitCreditSplit=propPerson.getUnits();
            int i=0;
            for (ProposalPersonCreditSplit creditSplit : personCreditSplit) {
                if(creditSplit.getCredit() !=null){

                    String credit= String.valueOf( creditSplit.getCredit().intValue());
                    if(!(credit.matches(reg))){
                        GlobalVariables.getErrorMap().putError("document.creditSplit", ERROR_PERCENTAGE,
                                new String[] {"Credit Split" });
                        retval=false;
                    }
                }
                
           }
            for(ProposalPersonUnit personUnitSplit:propUnitCreditSplit){
                List<ProposalUnitCreditSplit> unitcreditsplit=personUnitSplit.getCreditSplits();
                for(Iterator it=unitcreditsplit.iterator();it.hasNext();){
                    ProposalUnitCreditSplit unitsplit=(ProposalUnitCreditSplit)it.next();
                    if(unitsplit.getCredit()!= null){
                        String credit=String.valueOf(unitsplit.getCredit().intValue());
                        if(!(credit.matches(reg))){
                            GlobalVariables.getErrorMap().putError("document.creditSplit", ERROR_PERCENTAGE,
                                    new String[] {"Credit Split" });
                            retval=false; 
                        }
                        
                    }
                }
                
                
                
            }
            
            i++;
        }
        return retval;
    }
}

