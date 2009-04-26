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
package org.kuali.kra.committee.rules;

import java.sql.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.bo.Unit;
import org.kuali.kra.committee.bo.Committee;
import org.kuali.kra.committee.bo.CommitteeMembership;
import org.kuali.kra.committee.bo.CommitteeMembershipRole;
import org.kuali.kra.committee.document.CommitteeDocument;
import org.kuali.kra.committee.rule.AddCommitteeMembershipRoleRule;
import org.kuali.kra.committee.rule.AddCommitteeMembershipRule;
import org.kuali.kra.committee.rule.event.AddCommitteeMembershipEvent;
import org.kuali.kra.committee.rule.event.AddCommitteeMembershipRoleEvent;
import org.kuali.kra.committee.service.CommitteeService;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.rule.BusinessRuleInterface;
import org.kuali.kra.rule.event.KraDocumentEventBaseExtension;
import org.kuali.kra.rules.ResearchDocumentRuleBase;
import org.kuali.kra.service.UnitService;
import org.kuali.rice.kns.document.Document;
import org.kuali.rice.kns.util.GlobalVariables;

/**
 * This is the main business rule class for the Committee Document.  It
 * is responsible for customized workflow related business rule checking such
 * saving, routing, etc.  All committee specific actions, e.g. adding members,
 * this class will act as a controller and forward the rules checking to 
 * another class within this package.
 */
@SuppressWarnings("unchecked")
public class CommitteeDocumentRule extends ResearchDocumentRuleBase implements BusinessRuleInterface, AddCommitteeMembershipRule, AddCommitteeMembershipRoleRule {
    
    private final String PROPERTY_NAME_PREFIX = "document.committeeList[0].committeeMemberships[";
    private final String PROPERTY_NAME_TERM_START_DATE = "].termStartDate";
    private final String PROPERTY_NAME_TERM_END_DATE = "].termEndDate";
    private final String PROPERTY_NAME_NEW_ROLE_PREFIX = "membershipRolesHelper.newCommitteeMembershipRoles[";
    private final String PROPERTY_NAME_ROLE_PREFIX = "].membershipRoles[";
    private final String PROPERTY_NAME_ROLE_CODE = "].membershipRoleCode";
    private final String PROPERTY_NAME_ROLE_START_DATE = "].startDate";
    private final String PROPERTY_NAME_ROLE_END_DATE = "].endDate";
    private final String PROPERTY_NAME_NEW_EXPERTISE_PREFIX ="membershipExpertiseHelper.newCommitteeMembershipExpertise[";
    private final String PROPERTY_NAME_RESEARCH_AREA_CODE = "].researchAreaCode";

    static private final boolean VALIDATION_REQUIRED = true;
    
    // KRACOEUS-641: Changed CHOMP_LAST_LETTER_S_FROM_COLLECTION_NAME to false to prevent duplicate error messages
    static private final boolean CHOMP_LAST_LETTER_S_FROM_COLLECTION_NAME = false;
    
    /**
     * @see org.kuali.core.rules.DocumentRuleBase#processCustomRouteDocumentBusinessRules(org.kuali.rice.kns.document.Document)
     */
    @Override
    protected boolean processCustomRouteDocumentBusinessRules(Document document) {
        boolean retval = true;
        
        retval &= super.processCustomRouteDocumentBusinessRules(document);
        
        return retval;
    }

    /**
     * @see org.kuali.core.rules.DocumentRuleBase#processCustomSaveDocumentBusinessRules(org.kuali.rice.kns.document.Document)
     */
    @Override
    protected boolean processCustomSaveDocumentBusinessRules(Document document) {
       
        boolean valid = true;
        
        GlobalVariables.getErrorMap().addToErrorPath("document");
        
        /* 
         * The Kuali Core business rules don't check to see if the required fields are
         * set in order to save the document.  Thus, that check must be performed here.
         * Note that the method validateDocumentAndUpdatableReferencesRecursively() does
         * not return whether validation failed or succeeded.  Therefore, we check the
         * the global error map.  If it isn't empty, we assume that the errors were put
         * there by this method.
         */
        getDictionaryValidationService().validateDocumentAndUpdatableReferencesRecursively(document, getMaxDictionaryValidationDepth(), VALIDATION_REQUIRED, CHOMP_LAST_LETTER_S_FROM_COLLECTION_NAME);
        valid &= GlobalVariables.getErrorMap().isEmpty();
        
        valid &= validateUniqueCommitteeId((CommitteeDocument) document);
        valid &= validateHomeUnit((CommitteeDocument) document);
        
        GlobalVariables.getErrorMap().removeFromErrorPath("document");

        valid &= validateCommitteeMemberships((CommitteeDocument) document);
        
        return valid;
    }
    
    /**
     * Verify that we are not saving a committee with a duplicate Committee ID.
     * In other words, each committee must have a unique Committee ID.
     * @param document Committee Document
     * @return true if valid; otherwise false
     */
    private boolean validateUniqueCommitteeId(CommitteeDocument document) {
        
        boolean valid = true;
        
        CommitteeService committeeService = KraServiceLocator.getService(CommitteeService.class);
        Committee committee = committeeService.getCommitteeById(document.getCommittee().getCommitteeId());
        
        // The committee is null if we are creating a committee with a new committee ID or
        // we are changing the committee ID.
        
        if (committee != null) {
            
            // There is no conflict if we are only modifying the same committee.
            
            if (!committee.getId().equals(document.getCommittee().getId())) {
                
                // We can have a conflict if we find a different committee in the database
                // and it has the same ID as the committee we are trying to save.
                
                if (StringUtils.equals(committee.getCommitteeId(), document.getCommittee().getCommitteeId())) {
                    valid = false;
                    reportError(Constants.COMMITTEE_PROPERTY_KEY + "List[0].committeeId", 
                                KeyConstants.ERROR_COMMITTEE_DUPLICATE_ID);
                }
            }
        }
        return valid;
    }
    
    /**
     * Verify that the unit number if is valid.  We can ignore a blank
     * home unit number since it is a required field and that business logic
     * will flag a blank value as invalid.
     * @param document the Committee document
     * @return true if valid; otherwise false
     */
    private boolean validateHomeUnit(CommitteeDocument document) {
        
        boolean valid = true;
        
        String homeUnitNumber = document.getCommittee().getHomeUnitNumber();
        if (!StringUtils.isBlank(homeUnitNumber)) {
            UnitService unitService = KraServiceLocator.getService(UnitService.class);
            Unit homeUnit = unitService.getUnit(homeUnitNumber);
            if (homeUnit == null) {
                valid = false;
                reportError(Constants.COMMITTEE_PROPERTY_KEY + "List[0].homeUnitNumber", 
                            KeyConstants.ERROR_INVALID_UNIT, homeUnitNumber);
            }
        }
        
        return valid;
    }
    
    private boolean validateCommitteeMemberships(CommitteeDocument committeeDocument) {
        boolean isValid = true;
        List<CommitteeMembership> committeeMemberships = committeeDocument.getCommittee().getCommitteeMemberships(); 
        
        for(CommitteeMembership committeeMembership : committeeMemberships) {
            int membershipIndex = committeeMemberships.indexOf(committeeMembership); 
            isValid &= isValidTermStartEndDates(committeeMembership, membershipIndex);
            isValid &= isValidRoles(committeeMembership, membershipIndex);
            isValid &= hasExpertise(committeeMembership, membershipIndex);
            // To keep the errors more comprehensible the role overlap check is done after other errors are resolved
            if (isValid) {
                isValid &= hasNoTermOverlap(committeeMemberships, committeeMembership, membershipIndex);
            }

        }
        return isValid;
    }
    
    /**
     * Check that the person does not have other entries whose term overlap.
     * (A member may not have the same role for overlapping time periods.)
     *
     * Checks are done only against records that are ahead of this one since these
     * have passes validations and therefore have valid term dates.
     * This method also displays the appropriate error message.
     * 
     * @param committeeMemberships - the committee memberships of the current committee
     * @param committeeMembership - the committee membership which contains the to be validated data
     * @param membershipIndex - the index position of the committeeMembership
     * @return <code>true</code> if the role does not overlap with another role, <code>false</code> otherwise
     */
    private boolean hasNoTermOverlap(List<CommitteeMembership> committeeMemberships, CommitteeMembership committeeMembership, int membershipIndex) {
        boolean isValid = true;

        for (int i=0; i < membershipIndex; i++) {
            CommitteeMembership tmpMember = committeeMemberships.get(i);
            if (tmpMember.isSamePerson(committeeMembership)) {
                if (isWithinPeriod(committeeMembership.getTermStartDate(), tmpMember.getTermStartDate(), tmpMember.getTermEndDate())) {
                    isValid = false;
                    reportError(PROPERTY_NAME_PREFIX + membershipIndex + PROPERTY_NAME_TERM_START_DATE, KeyConstants.ERROR_COMMITTEE_MEMBERSHIP_PERSON_DUPLICATE,
                            tmpMember.getFormattedTermStartDate(), tmpMember.getFormattedTermEndDate());
                } else if (isWithinPeriod(committeeMembership.getTermEndDate(), tmpMember.getTermStartDate(), tmpMember.getTermEndDate())) {
                    isValid = false;
                    reportError(PROPERTY_NAME_PREFIX + membershipIndex + PROPERTY_NAME_TERM_END_DATE, KeyConstants.ERROR_COMMITTEE_MEMBERSHIP_PERSON_DUPLICATE,
                            tmpMember.getFormattedTermStartDate(), tmpMember.getFormattedTermEndDate());
                } 
            }
        }
        
        return isValid;
    }

    /**
     * Verify the Term Start and Term End dates
     * 
     * Date validation is done by the data dictionary.  
     * Validate that Term End date is greater than or equal to the Term Start date.
     * 
     * This method also displays the appropriate error message.
     * 
     * @param committeeMembership - the committeeMembership which contains the to be validated data
     * @param membershipIndex - the index position of the committeeMembership
     * @return <code>true</code> if the term start and end dates are valid, <code>false</code> otherwise
     */
    private boolean isValidTermStartEndDates(CommitteeMembership committeeMembership, int membershipIndex) {
        boolean isValid = true;
        
        if (committeeMembership.getTermStartDate() != null && committeeMembership.getTermEndDate() != null 
                && committeeMembership.getTermEndDate().before(committeeMembership.getTermStartDate())) {
            isValid = false;
            reportError(PROPERTY_NAME_PREFIX + membershipIndex + PROPERTY_NAME_TERM_END_DATE, 
                        KeyConstants.ERROR_COMMITTEE_MEMBERSHIP_TERM_END_DATE_BEFORE_TERM_START_DATE);
        }
        
       return isValid;
    }
    
    /**
     * Verify Roles
     * 
     * @param committeeMembership - the committeeMembership which contains the to be validated data
     * @param membershipIndex - the index position of the committeeMembership
     * @return <code>true</code> if the roles are valid, <code>false</code> otherwise
     */
    private boolean isValidRoles(CommitteeMembership committeeMembership, int membershipIndex) {
        boolean isValid = true;
        List<CommitteeMembershipRole> membershipRoles = committeeMembership.getMembershipRoles();

        isValid &= hasRoles(committeeMembership, membershipIndex);
        
        for (CommitteeMembershipRole membershipRole : membershipRoles) {
            int roleIndex = membershipRoles.indexOf(membershipRole);
            isValid &= isValidRoleStartEndDates(membershipRole, membershipIndex, roleIndex);
            isValid &= roleDatesWithinTermDates(committeeMembership, membershipRole, membershipIndex, roleIndex);
            // To keep the errors more comprehensible the role overlap check is done after other errors are resolved
            if (isValid) {
                isValid &= hasNoRoleOverlap(committeeMembership, membershipRole, membershipIndex, roleIndex);
            }
        }
        
        return isValid;
    }

    /**
     * Verify that the committee membership has at least one role assigned.
     *  
     * This method also displays the appropriate error message.
     * 
     * @param committeeMembership - the committeeMembership which contains the to be validated data
     * @param membershipIndex - the index position of the committeeMembership
     * @return <code>true</code> when the committee membership has at least one role assigned, <code>false</code> otherwise
     */
    private boolean hasRoles(CommitteeMembership committeeMembership, int membershipIndex) {
        boolean hasExpertise = true;

        if (committeeMembership.getMembershipRoles().isEmpty()) {
            hasExpertise = false;
            reportError(PROPERTY_NAME_NEW_ROLE_PREFIX + membershipIndex + PROPERTY_NAME_ROLE_CODE, 
                    KeyConstants.ERROR_COMMITTEE_MEMBERSHIP_ROLE_MISSING);
        }
                
        return hasExpertise;
    }

    /**
     * Verify the Role Start and Role End dates.
     * 
     * Date validation is done by the data dictionary.  
     * Validate that Role End date is greater than or equal to the Role Start date.
     * 
     * This method also displays the appropriate error message.
     * 
     * @param membershipRole - the membershipRole which contains the to be validated data
     * @param membershipIndex - the index position of the committeeMembership
     * @param roleIndex - the index position of the membershipRole
     * @return <code>true</code> if the role start and end dates are valid, <code>false</code> otherwise
     */
    private boolean isValidRoleStartEndDates(CommitteeMembershipRole membershipRole, int membershipIndex, int roleIndex) {
        boolean isValid = true;
        
        if (membershipRole.getStartDate() != null && membershipRole.getEndDate() != null 
                && membershipRole.getEndDate().before(membershipRole.getStartDate())) {
            isValid = false;
            reportError(PROPERTY_NAME_PREFIX + membershipIndex + PROPERTY_NAME_ROLE_PREFIX + roleIndex + PROPERTY_NAME_ROLE_END_DATE, 
                        KeyConstants.ERROR_COMMITTEE_MEMBERSHIP_ROLE_END_DATE_BEFORE_ROLE_START_DATE);
        }
        
       return isValid;
    }

    /**
     * Verify that the role dates are within the term period.
     * 
     * This method also displays the appropriate error message.
     * 
     * @param committeeMembership - the committeeMembership of whom the membershipRole is to be validated
     * @param membershipRole - the membershipRole which contains the to be validated data
     * @param membershipIndex - the index position of the committeeMembership
     * @param indexOf - the index position of the membershipRole
     * @return <code>true</code> if the role dates are within the term period, <code>false</code> otherwise
     */
    private boolean roleDatesWithinTermDates(CommitteeMembership committeeMembership, CommitteeMembershipRole membershipRole,
            int membershipIndex, int roleIndex) {
        boolean isValid = true;
        
        if ((committeeMembership.getTermStartDate() != null) && (committeeMembership.getTermEndDate() != null) 
                && (membershipRole.getStartDate() != null) && (membershipRole.getEndDate() != null)) {
            if (hasDateOutsideCommitteeMembershipTerm(committeeMembership, membershipRole.getStartDate())) {
                isValid = false;
                reportError(PROPERTY_NAME_PREFIX + membershipIndex + PROPERTY_NAME_ROLE_PREFIX + roleIndex + PROPERTY_NAME_ROLE_START_DATE, 
                        KeyConstants.ERROR_COMMITTEE_MEMBERSHIP_ROLE_START_DATE_OUTSIDE_TERM);
            }
            if (hasDateOutsideCommitteeMembershipTerm(committeeMembership, membershipRole.getEndDate())) {
                isValid = false;
                reportError(PROPERTY_NAME_PREFIX + membershipIndex + PROPERTY_NAME_ROLE_PREFIX + roleIndex + PROPERTY_NAME_ROLE_END_DATE, 
                        KeyConstants.ERROR_COMMITTEE_MEMBERSHIP_ROLE_END_DATE_OUTSIDE_TERM);
            }
        }
        
        return isValid;
    }

    /**
     * Check if the date is outside the committee membership term.
     * If any of the date are null the method returns false.
     * 
     * @param committeeMembership - the committeeMembership whose term we are comparing against
     * @param date - the date to be checked
     * @return <code>true</code> if the date is outside the committee membership term, <code>false</code> otherwise
     */
    private boolean hasDateOutsideCommitteeMembershipTerm(CommitteeMembership committeeMembership, Date date) {
        boolean isOutside = false;
        if ((committeeMembership.getTermStartDate() != null) && (committeeMembership.getTermEndDate() != null) && (date != null)) {
            if (date.before(committeeMembership.getTermStartDate()) || date.after(committeeMembership.getTermEndDate())) {
                isOutside = true;
            }
        }
        return isOutside;
    }

    /**
     * Check that the role does not have other entries whose time periods overlap.
     * (A member may not have the same role for overlapping time periods.)
     * 
     * This method also displays the appropriate error message.
     * 
     * @param committeeMembership - the committeeMembership of whom the membershipRole is to be validated
     * @param membershipRole - the membershipRole which contains the to be validated data
     * @param membershipIndex - the index position of the committeeMembership
     * @param indexOf - the index position of the membershipRole
     * @return <code>true</code> if the role does not overlap with another role, <code>false</code> otherwise
     */
    private boolean hasNoRoleOverlap(CommitteeMembership committeeMembership, CommitteeMembershipRole membershipRole,
            int membershipIndex, int roleIndex) {
        boolean isValid = true;

        for (CommitteeMembershipRole tmpRole : committeeMembership.getMembershipRoles()) {
            if (roleIndex != committeeMembership.getMembershipRoles().indexOf(tmpRole) 
                    && membershipRole.getMembershipRoleCode().equals(tmpRole.getMembershipRoleCode())) {
                if (isWithinPeriod(membershipRole.getStartDate(), tmpRole.getStartDate(), tmpRole.getEndDate()) 
                        || isWithinPeriod(membershipRole.getEndDate(), tmpRole.getStartDate(), tmpRole.getEndDate())) {
                    isValid = false;
                    reportError(PROPERTY_NAME_PREFIX + membershipIndex + PROPERTY_NAME_ROLE_PREFIX + roleIndex + PROPERTY_NAME_ROLE_CODE, 
                            KeyConstants.ERROR_COMMITTEE_MEMBERSHIP_ROLE_DUPLICATE);
                } 
            }
        }
        
        return isValid;
    }

    /**
     * Verify that a date is within a period
     * 
     * @param date - the date that needs to be within the period
     * @param periodStart - the date on which the period begins
     * @param periodEnd - the date on which the period ends
     * @return <code>true</code> if date is within the period, <code>false</code> otherwise
     */
    private boolean isWithinPeriod(Date date, Date periodStart, Date periodEnd) {
        return !(date.before(periodStart) || date.after(periodEnd));
    }
    
    /**
     * Verify that the committee membership has at least one expertise assigned.
     *  
     * @param committeeMembership - the committeeMembership which contains the to be validated data
     * @param membershipIndex - the index position of the committeeMembership
     * @return <code>true</code> when the committee membership has at least one expertise assigned, <code>false</code> otherwise
     */
    private boolean hasExpertise(CommitteeMembership committeeMembership, int membershipIndex) {
        boolean hasExpertise = true;

        if (committeeMembership.getMembershipExpertise().isEmpty()) {
            hasExpertise = false;
            reportError(PROPERTY_NAME_NEW_EXPERTISE_PREFIX + membershipIndex + PROPERTY_NAME_RESEARCH_AREA_CODE, 
                    KeyConstants.ERROR_COMMITTEE_MEMBERSHIP_EXPERTISE_MISSING);
        }
                
        return hasExpertise;
    }

    /**
     * @see org.kuali.core.rule.DocumentAuditRule#processRunAuditBusinessRules(org.kuali.core.document.Document)
     */
    public boolean processRunAuditBusinessRules(Document document) {
        boolean retval = true;
        
        retval &= super.processRunAuditBusinessRules(document);
        
        return retval;
    }
    
    /**
     * @see org.kuali.kra.committee.rule.AddCommitteeMembershipRule#processAddCommitteeMembershipRules(org.kuali.kra.committee.rule.event.AddCommitteeMembershipEvent)
     */
    public boolean processAddCommitteeMembershipBusinessRules(AddCommitteeMembershipEvent addCommitteeMembershipEvent) {
        return new CommitteeMembershipRule().processAddCommitteeMembershipBusinessRules(addCommitteeMembershipEvent);
    }
    
    /**
     * @see org.kuali.kra.committee.rule.AddCommitteeMembershipRoleRule#processAddCommitteeMembershipRoleBusinessRules(org.kuali.kra.committee.rule.event.AddCommitteeMembershipRoleEvent)
     */
    public boolean processAddCommitteeMembershipRoleBusinessRules(AddCommitteeMembershipRoleEvent addCommitteeMembershipRoleEvent) {
        return new CommitteeMembershipRule().processAddCommitteeMembershipRoleBusinessRules(addCommitteeMembershipRoleEvent);
    }

    /**
     * @see org.kuali.kra.rule.BusinessRuleInterface#processRules(org.kuali.kra.rule.event.KraDocumentEventBaseExtension)
     */
    public boolean processRules(KraDocumentEventBaseExtension event) {
        boolean retVal = false;
        retVal = event.getRule().processRules(event);
        return retVal;
    }

}
