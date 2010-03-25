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

import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.CitizenshipDataType;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.CitizenshipDataType.Enum;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document.PHS398CareerDevelopmentAwardSup11;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document.PHS398CareerDevelopmentAwardSup11.ApplicationType;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document.PHS398CareerDevelopmentAwardSup11.CareerDevelopmentAwardAttachments;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document.PHS398CareerDevelopmentAwardSup11.ApplicationType.TypeOfApplication;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document.PHS398CareerDevelopmentAwardSup11.CareerDevelopmentAwardAttachments.CandidateBackground;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document.PHS398CareerDevelopmentAwardSup11.CareerDevelopmentAwardAttachments.CareerDevelopmentAndTrainingActivities;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document.PHS398CareerDevelopmentAwardSup11.CareerDevelopmentAwardAttachments.CareerGoalsAndObjectives;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document.PHS398CareerDevelopmentAwardSup11.CareerDevelopmentAwardAttachments.ConsortiumContractualArrangements;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document.PHS398CareerDevelopmentAwardSup11.CareerDevelopmentAwardAttachments.InclusionEnrollmentReport;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document.PHS398CareerDevelopmentAwardSup11.CareerDevelopmentAwardAttachments.InclusionOfChildren;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document.PHS398CareerDevelopmentAwardSup11.CareerDevelopmentAwardAttachments.InclusionOfWomenAndMinorities;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document.PHS398CareerDevelopmentAwardSup11.CareerDevelopmentAwardAttachments.InsitutionalEnvironment;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document.PHS398CareerDevelopmentAwardSup11.CareerDevelopmentAwardAttachments.InstitutionalCommitment;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document.PHS398CareerDevelopmentAwardSup11.CareerDevelopmentAwardAttachments.IntroductionToApplication;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document.PHS398CareerDevelopmentAwardSup11.CareerDevelopmentAwardAttachments.MentoringPlan;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document.PHS398CareerDevelopmentAwardSup11.CareerDevelopmentAwardAttachments.ProgressReportPublicationList;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document.PHS398CareerDevelopmentAwardSup11.CareerDevelopmentAwardAttachments.ProtectionOfHumanSubjects;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document.PHS398CareerDevelopmentAwardSup11.CareerDevelopmentAwardAttachments.ResearchStrategy;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document.PHS398CareerDevelopmentAwardSup11.CareerDevelopmentAwardAttachments.ResourceSharingPlans;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document.PHS398CareerDevelopmentAwardSup11.CareerDevelopmentAwardAttachments.ResponsibleConductOfResearch;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document.PHS398CareerDevelopmentAwardSup11.CareerDevelopmentAwardAttachments.SelectAgentResearch;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document.PHS398CareerDevelopmentAwardSup11.CareerDevelopmentAwardAttachments.SpecificAims;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document.PHS398CareerDevelopmentAwardSup11.CareerDevelopmentAwardAttachments.StatementsOfSupport;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document.PHS398CareerDevelopmentAwardSup11.CareerDevelopmentAwardAttachments.TargetedPlannedEnrollment;
import gov.grants.apply.forms.phs398CareerDevelopmentAwardSup11V11.PHS398CareerDevelopmentAwardSup11Document.PHS398CareerDevelopmentAwardSup11.CareerDevelopmentAwardAttachments.VertebrateAnimals;
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
 * PHS398CareerDevelopmentAwardSup V1.1 Form is generated using XMLBean classes
 * and is based on PHS398ResearchPlanV1_2 schema.
 * 
 * @author Kuali Research Administration Team (kualidev@oncourse.iu.edu)
 */
public class PHS398CareerDevelopmentAwardSupV1_1Generator extends
		PHS398CareerDevelopmentAwardSupBaseGenerator {
	private static final String PI_CUSTOM_DATA = "PI_CITIZENSHIP_FROM_CUSTOM_DATA";
	private static final String PROPOSAL_TYPE_TASK_ORDER = "6";

	private XmlObject getPHS398CareerDevelopmentAwardSup() {
		PHS398CareerDevelopmentAwardSup11Document phs398CareerDevelopmentAwardSup11Document = PHS398CareerDevelopmentAwardSup11Document.Factory
				.newInstance();
		PHS398CareerDevelopmentAwardSup11 phs398CareerDevelopmentAwardSup11 = PHS398CareerDevelopmentAwardSup11.Factory
				.newInstance();
		phs398CareerDevelopmentAwardSup11
				.setFormVersion(S2SConstants.FORMVERSION_1_1);
		phs398CareerDevelopmentAwardSup11
				.setApplicationType(getApplicationType());
		phs398CareerDevelopmentAwardSup11
				.setCitizenship(getCitizenshipDataType());
		phs398CareerDevelopmentAwardSup11
				.setCareerDevelopmentAwardAttachments(getCareerDevelopmentAwardAttachments());
		phs398CareerDevelopmentAwardSup11Document
				.setPHS398CareerDevelopmentAwardSup11(phs398CareerDevelopmentAwardSup11);
		return phs398CareerDevelopmentAwardSup11Document;
	}

	private Enum getCitizenshipDataType() {
		String citizenSource = "1";
		String piCitizenShipValue = s2sUtilService
				.getParameterValue(PI_CUSTOM_DATA);
		if (piCitizenShipValue != null) {
			citizenSource = piCitizenShipValue;
		}
		if (citizenSource.equals("0")) {
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
		ApplicationType applicationType = ApplicationType.Factory.newInstance();
		if (pdDoc.getDevelopmentProposal().getProposalTypeCode() != null
				&& !pdDoc.getDevelopmentProposal().getProposalTypeCode()
						.equals(PROPOSAL_TYPE_TASK_ORDER)) {
			// Check !=6 to ensure that if proposalType='TASK ORDER", it must
			// not set. THis is because the enum has no
			// entry for TASK ORDER
			applicationType.setTypeOfApplication(TypeOfApplication.Enum
					.forInt(Integer.parseInt(pdDoc.getDevelopmentProposal()
							.getProposalTypeCode())));
		} else {
			applicationType.setTypeOfApplication(TypeOfApplication.NEW);
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
			int narrativeTypeCode = Integer.parseInt(narrative.getNarrativeTypeCode());
			switch (narrativeTypeCode) {
			case NARRATIVE_TYPE_INTRODUCTION_TO_APPLICATION:
				IntroductionToApplication introductionToApplication = IntroductionToApplication.Factory
						.newInstance();
				introductionToApplication
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setIntroductionToApplication(introductionToApplication);
				break;
			case NARRATIVE_TYPE_SPECIFIC_AIMS:
				SpecificAims specificAims = SpecificAims.Factory.newInstance();
				specificAims.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments.setSpecificAims(specificAims);
				break;
			case NARRATIVE_TYPE_INCLUSION_ENROLLMENT_REPORT:
				InclusionEnrollmentReport inclusionEnrollmentReport = InclusionEnrollmentReport.Factory
						.newInstance();
				inclusionEnrollmentReport
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setInclusionEnrollmentReport(inclusionEnrollmentReport);
				break;
			case NARRATIVE_TYPE_PROGRESS_REPORT_PUBLICATION_LIST:
				ProgressReportPublicationList progressReportPublicationList = ProgressReportPublicationList.Factory
						.newInstance();
				progressReportPublicationList
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setProgressReportPublicationList(progressReportPublicationList);
				break;
			case NARRATIVE_TYPE_PROTECTION_OF_HUMAN_SUBJECTS:
				ProtectionOfHumanSubjects protectionOfHumanSubjects = ProtectionOfHumanSubjects.Factory
						.newInstance();
				protectionOfHumanSubjects
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setProtectionOfHumanSubjects(protectionOfHumanSubjects);
				break;
			case NARRATIVE_TYPE_INCLUSION_OF_WOMEN_AND_MINORITIES:
				InclusionOfWomenAndMinorities inclusionOfWomenAndMinorities = InclusionOfWomenAndMinorities.Factory
						.newInstance();
				inclusionOfWomenAndMinorities
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setInclusionOfWomenAndMinorities(inclusionOfWomenAndMinorities);
				break;
			case NARRATIVE_TYPE_TARGETED_PLANNED_ENROLLMENT_TABLE:
				TargetedPlannedEnrollment targetedPlannedEnrollment = TargetedPlannedEnrollment.Factory
						.newInstance();
				targetedPlannedEnrollment
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setTargetedPlannedEnrollment(targetedPlannedEnrollment);
				break;
			case NARRATIVE_TYPE_INCLUSION_OF_CHILDREN:
				InclusionOfChildren inclusionOfChildren = InclusionOfChildren.Factory
						.newInstance();
				inclusionOfChildren.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setInclusionOfChildren(inclusionOfChildren);
				break;
			case NARRATIVE_TYPE_VERTEBRATE_ANIMALS:
				VertebrateAnimals vertebrateAnimals = VertebrateAnimals.Factory
						.newInstance();
				vertebrateAnimals.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setVertebrateAnimals(vertebrateAnimals);
				break;
			case NARRATIVE_TYPE_SELECT_AGENT_RESEARCH:
				SelectAgentResearch selectAgentResearch = SelectAgentResearch.Factory
						.newInstance();
				selectAgentResearch.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setSelectAgentResearch(selectAgentResearch);
				break;
			case NARRATIVE_TYPE_PHS_CAREER_CONSORTIUM_CONTRACT:
				ConsortiumContractualArrangements consortiumContractualArrangements = ConsortiumContractualArrangements.Factory
						.newInstance();
				consortiumContractualArrangements
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setConsortiumContractualArrangements(consortiumContractualArrangements);
				break;
			case NARRATIVE_TYPE_PHS_CAREER_RESOURCE_SHARING_PLAN:
				ResourceSharingPlans resourceSharingPlans = ResourceSharingPlans.Factory
						.newInstance();
				resourceSharingPlans.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setResourceSharingPlans(resourceSharingPlans);
				break;
			case NARRATIVE_TYPE_CANDIDATE_BACKGROUND:
				CandidateBackground candidateBackground = CandidateBackground.Factory
						.newInstance();
				candidateBackground.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setCandidateBackground(candidateBackground);
				break;
			case NARRATIVE_TYPE_CAREER_GOALS_AND_OBJECTIVES:
				CareerGoalsAndObjectives careerGoalsAndObjectives = CareerGoalsAndObjectives.Factory
						.newInstance();
				careerGoalsAndObjectives
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setCareerGoalsAndObjectives(careerGoalsAndObjectives);
				break;
			case NARRATIVE_TYPE_CAREER_DEVELOPMENT_AND_TRAINING:
				CareerDevelopmentAndTrainingActivities careerDevelopmentAndTrainingActivities = CareerDevelopmentAndTrainingActivities.Factory
						.newInstance();
				careerDevelopmentAndTrainingActivities
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setCareerDevelopmentAndTrainingActivities(careerDevelopmentAndTrainingActivities);
				break;
			case NARRATIVE_TYPE_RESPONSIBLE_CONDUCT_OF_RESEARCH:
				ResponsibleConductOfResearch responsibleConductOfResearch = ResponsibleConductOfResearch.Factory
						.newInstance();
				responsibleConductOfResearch
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setResponsibleConductOfResearch(responsibleConductOfResearch);
				break;
			case NARRATIVE_TYPE_PHS398_MENTORING_PLAN:
				MentoringPlan mentoringPlan = MentoringPlan.Factory
						.newInstance();
				mentoringPlan.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setMentoringPlan(mentoringPlan);
				break;
			case NARRATIVE_TYPE_PHS398_MENTOR_STATEMENTS_LETTERS:
				StatementsOfSupport statementsOfSupport = StatementsOfSupport.Factory
						.newInstance();
				statementsOfSupport.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setStatementsOfSupport(statementsOfSupport);
				break;
			case NARRATIVE_TYPE_PSH398_INSTITUTIONAL_ENVIRONMENT:
				InsitutionalEnvironment insitutionalEnvironment = InsitutionalEnvironment.Factory
						.newInstance();
				insitutionalEnvironment
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setInsitutionalEnvironment(insitutionalEnvironment);
				break;
			case NARRATIVE_TYPE_PHS398_INSTITUTIONAL_COMMITMENT:
				InstitutionalCommitment institutionalCommitment = InstitutionalCommitment.Factory
						.newInstance();
				institutionalCommitment
						.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setInstitutionalCommitment(institutionalCommitment);
				break;
			case NARRATIVE_TYPE_PHS_CAREER_APPENDIX:
				AttachedFileDataType attachedFileDataType = AttachedFileDataType.Factory
						.newInstance();
				attachedFileDataType = getAttachedFileType(narrative);
				attachedFileList.add(attachedFileDataType);
				break;
			case NARRATIVE_TYPE_PHS_CAREER_REASEARCH_STRATEGY:
				ResearchStrategy researchStrategy = ResearchStrategy.Factory
						.newInstance();
				researchStrategy.setAttFile(getAttachedFileType(narrative));
				careerDevelopmentAwardAttachments
						.setResearchStrategy(researchStrategy);
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
		PHS398CareerDevelopmentAwardSup11 phs398CareerDevelopmentAwardSup11 = PHS398CareerDevelopmentAwardSup11.Factory
				.newInstance();
		PHS398CareerDevelopmentAwardSup11Document phs398CareerDevelopmentAwardSupDocument = PHS398CareerDevelopmentAwardSup11Document.Factory
				.newInstance();
		phs398CareerDevelopmentAwardSupDocument
				.setPHS398CareerDevelopmentAwardSup11(phs398CareerDevelopmentAwardSup11);
		return phs398CareerDevelopmentAwardSupDocument;
	}

}