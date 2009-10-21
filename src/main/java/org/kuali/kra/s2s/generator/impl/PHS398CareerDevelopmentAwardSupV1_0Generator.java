/*
 * Copyright 2008 The Kuali Foundation.
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
package org.kuali.kra.s2s.generator.impl;

import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.CitizenshipDataType;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.CitizenshipDataType.Enum;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.ApplicationType;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.CareerDevelopmentAwardAttachments;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.ApplicationType.TypeOfApplication;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.CareerDevelopmentAwardAttachments.BackgroundAndSignificance;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.CareerDevelopmentAwardAttachments.CandidateBackground;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.CareerDevelopmentAwardAttachments.CareerDevelopmentAndTrainingActivities;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.CareerDevelopmentAwardAttachments.CareerGoalsAndObjectives;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.CareerDevelopmentAwardAttachments.ConsortiumContractualArrangements;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.CareerDevelopmentAwardAttachments.InclusionEnrollmentReport;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.CareerDevelopmentAwardAttachments.InclusionOfChildren;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.CareerDevelopmentAwardAttachments.InclusionOfWomenAndMinorities;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.CareerDevelopmentAwardAttachments.InsitutionalEnvironment;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.CareerDevelopmentAwardAttachments.InstitutionalCommitment;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.CareerDevelopmentAwardAttachments.IntroductionToApplication;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.CareerDevelopmentAwardAttachments.MentoringPlan;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.CareerDevelopmentAwardAttachments.PreliminaryStudiesProgressReport;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.CareerDevelopmentAwardAttachments.ProgressReportPublicationList;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.CareerDevelopmentAwardAttachments.ProtectionOfHumanSubjects;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.CareerDevelopmentAwardAttachments.ResearchDesignAndMethods;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.CareerDevelopmentAwardAttachments.ResourceSharingPlans;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.CareerDevelopmentAwardAttachments.ResponsibleConductOfResearch;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.CareerDevelopmentAwardAttachments.SelectAgentResearch;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.CareerDevelopmentAwardAttachments.SpecificAims;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.CareerDevelopmentAwardAttachments.StatementsOfSupport;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.CareerDevelopmentAwardAttachments.TargetedPlannedEnrollment;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSupV10.PHS398CareerDevelopmentAwardSupDocument.PHS398CareerDevelopmentAwardSup.CareerDevelopmentAwardAttachments.VertebrateAnimals;
import gov.grants.apply.forms.phs398ResearchPlan12V12.PHS398ResearchPlan12Document;
import gov.grants.apply.system.attachmentsV10.AttachedFileDataType;
import gov.grants.apply.system.attachmentsV10.AttachmentGroupMin0Max100DataType;

import java.util.ArrayList;
import java.util.List;

import org.apache.xmlbeans.XmlObject;
import org.kuali.kra.proposaldevelopment.bo.Narrative;
import org.kuali.kra.proposaldevelopment.bo.ProposalPerson;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.s2s.util.S2SConstants;

/**
 * Class for generating the XML object for grants.gov
 * PHS398CareerDevelopmentAwardSup V1.0 Form is generated using XMLBean classes
 * and is based on PHS398ResearchPlanV1_2 schema.
 * 
 * @author Kuali Research Administration Team (kualidev@oncourse.iu.edu)
 */
public class PHS398CareerDevelopmentAwardSupV1_0Generator extends
		PHS398CareerDevelopmentAwardSupBaseGenerator {
	private static final String PI_CUSTOM_DATA = "PI_CITIZENSHIP_FROM_CUSTOM_DATA";

	private XmlObject getPHS398CareerDevelopmentAwardSup() {
		PHS398CareerDevelopmentAwardSupDocument phs398CareerDevelopmentAwardSupDocument = PHS398CareerDevelopmentAwardSupDocument.Factory
				.newInstance();
		PHS398CareerDevelopmentAwardSup phs398CareerDevelopmentAwardSup = PHS398CareerDevelopmentAwardSup.Factory
				.newInstance();
		phs398CareerDevelopmentAwardSup
				.setFormVersion(S2SConstants.FORMVERSION_1_0);
		phs398CareerDevelopmentAwardSup
				.setApplicationType(getApplicationType());
		phs398CareerDevelopmentAwardSup
				.setCitizenship(getCitizenshipDataType());
		phs398CareerDevelopmentAwardSup
				.setCareerDevelopmentAwardAttachments(getCareerDevelopmentAwardAttachments());
		phs398CareerDevelopmentAwardSupDocument
				.setPHS398CareerDevelopmentAwardSup(phs398CareerDevelopmentAwardSup);
		return phs398CareerDevelopmentAwardSupDocument;
	}

	private Enum getCitizenshipDataType() {
		int citizenSource = 1;
		String piCitizenShipValue = s2sUtilService
				.getParameterValue(PI_CUSTOM_DATA);
		if (piCitizenShipValue != null) {
			citizenSource = Integer.parseInt(piCitizenShipValue);
		}
		if (citizenSource == 0) {
			for (ProposalPerson proposalPerson : pdDoc.getDevelopmentProposal()
					.getProposalPersons()) {
				if (proposalPerson.isInvestigator()) {
					// TODO fetch warehouse person
				}
			}
		} else {
			// TODO fetch warehouse person
		}
		return CitizenshipDataType.PERMANENT_RESIDENT_OF_U_S;
	}

	private ApplicationType getApplicationType() {
		ApplicationType applicationType = null;
		if (pdDoc.getDevelopmentProposal().getProposalTypeCode() != null) {
			applicationType = ApplicationType.Factory.newInstance();
			if (pdDoc.getDevelopmentProposal().getProposalTypeCode()
					.equals("1")) {
				applicationType.setTypeOfApplication(TypeOfApplication.NEW);
			} else if (pdDoc.getDevelopmentProposal().getProposalTypeCode()
					.equals("3")) {
				applicationType
						.setTypeOfApplication(TypeOfApplication.CONTINUATION);
			} else if (pdDoc.getDevelopmentProposal().getProposalTypeCode()
					.equals("4")) {
				applicationType
						.setTypeOfApplication(TypeOfApplication.REVISION);
			} else if (pdDoc.getDevelopmentProposal().getProposalTypeCode()
					.equals("5")) {
				applicationType.setTypeOfApplication(TypeOfApplication.RENEWAL);
			} else if (pdDoc.getDevelopmentProposal().getProposalTypeCode()
					.equals("6")) {
				applicationType
						.setTypeOfApplication(TypeOfApplication.RESUBMISSION);
			} else if (pdDoc.getDevelopmentProposal().getProposalTypeCode()
					.equals("7")) {
				applicationType.setTypeOfApplication(TypeOfApplication.NEW);
			} else {
				applicationType.setTypeOfApplication(TypeOfApplication.NEW);
			}
		}
		return applicationType;
	}

	/*
	 * This method fetches all attachments related to Career development award.
	 */
	private CareerDevelopmentAwardAttachments getCareerDevelopmentAwardAttachments() {
		CareerDevelopmentAwardAttachments careerDevelopmentAwardAttachments = CareerDevelopmentAwardAttachments.Factory
				.newInstance();
		AttachmentGroupMin0Max100DataType attachmentGroupMin0Max100DataType = AttachmentGroupMin0Max100DataType.Factory
				.newInstance();
		List<AttachedFileDataType> attachedFileList = new ArrayList<AttachedFileDataType>();
		for (Narrative narrative : pdDoc.getDevelopmentProposal()
				.getNarratives()) {
			switch (Integer.parseInt(narrative.getNarrativeTypeCode())) {
			case INTRODUCTION_TO_APPLICATION:
				IntroductionToApplication introductionToApplication = IntroductionToApplication.Factory
						.newInstance();
				introductionToApplication
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setIntroductionToApplication(introductionToApplication);
				break;
			case SPECIFIC_AIMS:
				SpecificAims specificAims = SpecificAims.Factory.newInstance();
				specificAims.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments.setSpecificAims(specificAims);
				break;
			case BACKGROUND_SIGNIFICANCE:
				BackgroundAndSignificance backgroundAndSignificance = BackgroundAndSignificance.Factory
						.newInstance();
				backgroundAndSignificance
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setBackgroundAndSignificance(backgroundAndSignificance);
				break;
			case RESEARCH_DESIGN_METHODS:
				ResearchDesignAndMethods researchDesignAndMethods = ResearchDesignAndMethods.Factory
						.newInstance();
				researchDesignAndMethods
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setResearchDesignAndMethods(researchDesignAndMethods);
				break;
			case INCLUSION_ENROLLMENT_REPORT:
				InclusionEnrollmentReport inclusionEnrollmentReport = InclusionEnrollmentReport.Factory
						.newInstance();
				inclusionEnrollmentReport
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setInclusionEnrollmentReport(inclusionEnrollmentReport);
				break;
			case PROGRESS_REPORT_PUBLICATION_LIST:
				ProgressReportPublicationList progressReportPublicationList = ProgressReportPublicationList.Factory
						.newInstance();
				progressReportPublicationList
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setProgressReportPublicationList(progressReportPublicationList);
				break;
			case PROTECTION_OF_HUMAN_SUBJECTS:
				ProtectionOfHumanSubjects protectionOfHumanSubjects = ProtectionOfHumanSubjects.Factory
						.newInstance();
				protectionOfHumanSubjects
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setProtectionOfHumanSubjects(protectionOfHumanSubjects);
				break;
			case INCLUSION_OF_WOMEN_AND_MINORITIES:
				InclusionOfWomenAndMinorities inclusionOfWomenAndMinorities = InclusionOfWomenAndMinorities.Factory
						.newInstance();
				inclusionOfWomenAndMinorities
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setInclusionOfWomenAndMinorities(inclusionOfWomenAndMinorities);
				break;
			case TARGETED_PLANNED_ENROLLMENT_TABLE:
				TargetedPlannedEnrollment targetedPlannedEnrollment = TargetedPlannedEnrollment.Factory
						.newInstance();
				targetedPlannedEnrollment
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setTargetedPlannedEnrollment(targetedPlannedEnrollment);
				break;
			case INCLUSION_OF_CHILDREN:
				InclusionOfChildren inclusionOfChildren = InclusionOfChildren.Factory
						.newInstance();
				inclusionOfChildren.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setInclusionOfChildren(inclusionOfChildren);
				break;
			case VERTEBRATE_ANIMALS:
				VertebrateAnimals vertebrateAnimals = VertebrateAnimals.Factory
						.newInstance();
				vertebrateAnimals.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setVertebrateAnimals(vertebrateAnimals);
				break;
			case SELECT_AGENT_RESEARCH:
				SelectAgentResearch selectAgentResearch = SelectAgentResearch.Factory
						.newInstance();
				selectAgentResearch.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setSelectAgentResearch(selectAgentResearch);
				break;
			case PHS_CAREER_PRELIM_STUDIES_PROGREP:
				PreliminaryStudiesProgressReport preliminaryStudiesProgressReport = PreliminaryStudiesProgressReport.Factory
						.newInstance();
				preliminaryStudiesProgressReport
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setPreliminaryStudiesProgressReport(preliminaryStudiesProgressReport);
				break;
			case PHS_CAREER_CONSORTIUM_CONTRACT:
				ConsortiumContractualArrangements consortiumContractualArrangements = ConsortiumContractualArrangements.Factory
						.newInstance();
				consortiumContractualArrangements
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setConsortiumContractualArrangements(consortiumContractualArrangements);
				break;
			case PHS_CAREER_RESOURCE_SHARING_PLAN:
				ResourceSharingPlans resourceSharingPlans = ResourceSharingPlans.Factory
						.newInstance();
				resourceSharingPlans.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setResourceSharingPlans(resourceSharingPlans);
				break;
			case CANDIDATE_BACKGROUND:
				CandidateBackground candidateBackground = CandidateBackground.Factory
						.newInstance();
				candidateBackground.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setCandidateBackground(candidateBackground);
				break;
			case CAREER_GOALS_AND_OBJECTIVES:
				CareerGoalsAndObjectives careerGoalsAndObjectives = CareerGoalsAndObjectives.Factory
						.newInstance();
				careerGoalsAndObjectives
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setCareerGoalsAndObjectives(careerGoalsAndObjectives);
				break;
			case CAREER_DEVELOPMENT_AND_TRAINING:
				CareerDevelopmentAndTrainingActivities careerDevelopmentAndTrainingActivities = CareerDevelopmentAndTrainingActivities.Factory
						.newInstance();
				careerDevelopmentAndTrainingActivities
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setCareerDevelopmentAndTrainingActivities(careerDevelopmentAndTrainingActivities);
				break;
			case RESPONSIBLE_CONDUCT_OF_RESEARCH:
				ResponsibleConductOfResearch responsibleConductOfResearch = ResponsibleConductOfResearch.Factory
						.newInstance();
				responsibleConductOfResearch
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setResponsibleConductOfResearch(responsibleConductOfResearch);
				break;
			case PHS398_MENTORING_PLAN:
				MentoringPlan mentoringPlan = MentoringPlan.Factory
						.newInstance();
				mentoringPlan.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setMentoringPlan(mentoringPlan);
				break;
			case PHS398_MENTOR_STATEMENTS_LETTERS:
				StatementsOfSupport statementsOfSupport = StatementsOfSupport.Factory
						.newInstance();
				statementsOfSupport.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setStatementsOfSupport(statementsOfSupport);
				break;
			case PSH398_INSTITUTIONAL_ENVIRONMENT:
				InsitutionalEnvironment insitutionalEnvironment = InsitutionalEnvironment.Factory
						.newInstance();
				insitutionalEnvironment
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setInsitutionalEnvironment(insitutionalEnvironment);
				break;
			case PHS398_INSTITUTIONAL_COMMITMENT:
				InstitutionalCommitment institutionalCommitment = InstitutionalCommitment.Factory
						.newInstance();
				institutionalCommitment
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setInstitutionalCommitment(institutionalCommitment);
				break;
			case PHS_CAREER_APPENDIX:
				AttachedFileDataType attachedFileDataType = AttachedFileDataType.Factory
						.newInstance();
				attachedFileDataType = getAttachedFileType(narrative);
				attachedFileList.add(attachedFileDataType);
				break;
			}
		}
		attachmentGroupMin0Max100DataType.setAttachedFileArray(attachedFileList
				.toArray(new AttachedFileDataType[0]));
		careerDevelopmentAwardAttachments
				.setAppendix(attachmentGroupMin0Max100DataType);
		return careerDevelopmentAwardAttachments;
	}

	/**
	 * This method creates {@link XmlObject} of type
	 * {@link PHS398ResearchPlan12Document} by populating data from the given
	 * {@link ProposalDevelopmentDocument}
	 * 
	 * @param proposalDevelopmentDocument
	 *            for which the {@link XmlObject} needs to be created
	 * @return {@link XmlObject} which is generated using the given
	 *         {@link ProposalDevelopmentDocument}
	 * @see org.kuali.kra.s2s.generator.S2SFormGenerator#getFormObject(ProposalDevelopmentDocument)
	 */
	public XmlObject getFormObject(
			ProposalDevelopmentDocument proposalDevelopmentDocument) {
		this.pdDoc = proposalDevelopmentDocument;
		return getPHS398CareerDevelopmentAwardSup();
	}

	/**
	 * This method typecasts the given {@link XmlObject} to the required
	 * generator type and returns back the document of that generator type.
	 * 
	 * @param xmlObject
	 *            which needs to be converted to the document type of the
	 *            required generator
	 * @return {@link XmlObject} document of the required generator type
	 * @see org.kuali.kra.s2s.generator.S2SFormGenerator#getFormObject(XmlObject)
	 */
	public XmlObject getFormObject(XmlObject xmlObject) {
		PHS398CareerDevelopmentAwardSup phs398CareerDevelopmentAwardSup = PHS398CareerDevelopmentAwardSup.Factory
				.newInstance();
		PHS398CareerDevelopmentAwardSupDocument phs398CareerDevelopmentAwardSupDocument = PHS398CareerDevelopmentAwardSupDocument.Factory
				.newInstance();
		phs398CareerDevelopmentAwardSupDocument
				.setPHS398CareerDevelopmentAwardSup(phs398CareerDevelopmentAwardSup);
		return phs398CareerDevelopmentAwardSupDocument;
	}

}