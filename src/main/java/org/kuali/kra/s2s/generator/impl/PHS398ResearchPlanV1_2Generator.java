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

import gov.grants.apply.forms.phs398ResearchPlan12V12.PHS398ResearchPlan12Document;
import gov.grants.apply.forms.phs398ResearchPlan12V12.PHS398ResearchPlan12Document.PHS398ResearchPlan12;
import gov.grants.apply.forms.phs398ResearchPlan12V12.PHS398ResearchPlan12Document.PHS398ResearchPlan12.ApplicationType;
import gov.grants.apply.forms.phs398ResearchPlan12V12.PHS398ResearchPlan12Document.PHS398ResearchPlan12.ResearchPlanAttachments;
import gov.grants.apply.forms.phs398ResearchPlan12V12.PHS398ResearchPlan12Document.PHS398ResearchPlan12.ApplicationType.TypeOfApplication;
import gov.grants.apply.forms.phs398ResearchPlan12V12.PHS398ResearchPlan12Document.PHS398ResearchPlan12.ResearchPlanAttachments.BackgroundSignificance;
import gov.grants.apply.forms.phs398ResearchPlan12V12.PHS398ResearchPlan12Document.PHS398ResearchPlan12.ResearchPlanAttachments.HumanSubjectSection;
import gov.grants.apply.forms.phs398ResearchPlan12V12.PHS398ResearchPlan12Document.PHS398ResearchPlan12.ResearchPlanAttachments.InclusionEnrollmentReport;
import gov.grants.apply.forms.phs398ResearchPlan12V12.PHS398ResearchPlan12Document.PHS398ResearchPlan12.ResearchPlanAttachments.IntroductionToApplication;
import gov.grants.apply.forms.phs398ResearchPlan12V12.PHS398ResearchPlan12Document.PHS398ResearchPlan12.ResearchPlanAttachments.OtherResearchPlanSections;
import gov.grants.apply.forms.phs398ResearchPlan12V12.PHS398ResearchPlan12Document.PHS398ResearchPlan12.ResearchPlanAttachments.ProgressReport;
import gov.grants.apply.forms.phs398ResearchPlan12V12.PHS398ResearchPlan12Document.PHS398ResearchPlan12.ResearchPlanAttachments.ProgressReportPublicationList;
import gov.grants.apply.forms.phs398ResearchPlan12V12.PHS398ResearchPlan12Document.PHS398ResearchPlan12.ResearchPlanAttachments.ResearchDesignMethods;
import gov.grants.apply.forms.phs398ResearchPlan12V12.PHS398ResearchPlan12Document.PHS398ResearchPlan12.ResearchPlanAttachments.SpecificAims;
import gov.grants.apply.forms.phs398ResearchPlan12V12.PHS398ResearchPlan12Document.PHS398ResearchPlan12.ResearchPlanAttachments.HumanSubjectSection.InclusionOfChildren;
import gov.grants.apply.forms.phs398ResearchPlan12V12.PHS398ResearchPlan12Document.PHS398ResearchPlan12.ResearchPlanAttachments.HumanSubjectSection.InclusionOfWomenAndMinorities;
import gov.grants.apply.forms.phs398ResearchPlan12V12.PHS398ResearchPlan12Document.PHS398ResearchPlan12.ResearchPlanAttachments.HumanSubjectSection.ProtectionOfHumanSubjects;
import gov.grants.apply.forms.phs398ResearchPlan12V12.PHS398ResearchPlan12Document.PHS398ResearchPlan12.ResearchPlanAttachments.HumanSubjectSection.TargetedPlannedEnrollmentTable;
import gov.grants.apply.forms.phs398ResearchPlan12V12.PHS398ResearchPlan12Document.PHS398ResearchPlan12.ResearchPlanAttachments.OtherResearchPlanSections.ConsortiumContractualArrangements;
import gov.grants.apply.forms.phs398ResearchPlan12V12.PHS398ResearchPlan12Document.PHS398ResearchPlan12.ResearchPlanAttachments.OtherResearchPlanSections.LettersOfSupport;
import gov.grants.apply.forms.phs398ResearchPlan12V12.PHS398ResearchPlan12Document.PHS398ResearchPlan12.ResearchPlanAttachments.OtherResearchPlanSections.MultiplePDPILeadershipPlan;
import gov.grants.apply.forms.phs398ResearchPlan12V12.PHS398ResearchPlan12Document.PHS398ResearchPlan12.ResearchPlanAttachments.OtherResearchPlanSections.ResourceSharingPlans;
import gov.grants.apply.forms.phs398ResearchPlan12V12.PHS398ResearchPlan12Document.PHS398ResearchPlan12.ResearchPlanAttachments.OtherResearchPlanSections.SelectAgentResearch;
import gov.grants.apply.forms.phs398ResearchPlan12V12.PHS398ResearchPlan12Document.PHS398ResearchPlan12.ResearchPlanAttachments.OtherResearchPlanSections.VertebrateAnimals;
import gov.grants.apply.system.attachmentsV10.AttachedFileDataType;
import gov.grants.apply.system.attachmentsV10.AttachmentGroupMin0Max100DataType;

import org.apache.xmlbeans.XmlObject;
import org.kuali.kra.proposaldevelopment.bo.DevelopmentProposal;
import org.kuali.kra.proposaldevelopment.bo.Narrative;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.s2s.util.S2SConstants;

/**
 * Class for generating the XML object for grants.gov PHS398ResearchPlanV1_2.
 * Form is generated using XMLBean classes and is based on
 * PHS398ResearchPlanV1_2 schema.
 * 
 * @author Kuali Research Administration Team (kualidev@oncourse.iu.edu)
 */
public class PHS398ResearchPlanV1_2Generator extends
		PHS398ResearchPlanBaseGenerator {

	/**
	 * 
	 * This method gives the list of attachments for PHS398ResearchPlan form
	 * 
	 * @return phsResearchPlanDocument {@link XmlObject} of type
	 *         PHS398ResearchPlan12Document.
	 */
	private PHS398ResearchPlan12Document getPHS398ResearchPlan() {
		PHS398ResearchPlan12Document phsResearchPlanDocument = PHS398ResearchPlan12Document.Factory
				.newInstance();
		PHS398ResearchPlan12 phsResearchPlan = PHS398ResearchPlan12.Factory
				.newInstance();
		phsResearchPlan.setFormVersion(S2SConstants.FORMVERSION_1_2);
		phsResearchPlan.setApplicationType(getApplicationType());
		ResearchPlanAttachments researchPlanAttachments = ResearchPlanAttachments.Factory
				.newInstance();
		getNarrativeAttachments(researchPlanAttachments);
		AttachmentGroupMin0Max100DataType attachmentGroupMin0Max100DataType = AttachmentGroupMin0Max100DataType.Factory
				.newInstance();
		attachmentGroupMin0Max100DataType
				.setAttachedFileArray(getAttachedFileDataTypes());
		researchPlanAttachments.setAppendix(attachmentGroupMin0Max100DataType);
		phsResearchPlan.setResearchPlanAttachments(researchPlanAttachments);
		phsResearchPlanDocument.setPHS398ResearchPlan12(phsResearchPlan);
		return phsResearchPlanDocument;
	}

	private void getNarrativeAttachments(
			ResearchPlanAttachments researchPlanAttachments) {
		HumanSubjectSection humanSubjectSection = HumanSubjectSection.Factory
				.newInstance();
		OtherResearchPlanSections otherResearchPlanSections = OtherResearchPlanSections.Factory
				.newInstance();

		for (Narrative narrative : pdDoc.getDevelopmentProposal()
				.getNarratives()) {
			switch (Integer.parseInt(narrative.getNarrativeTypeCode())) {
			case INTRODUCTION_TO_APPLICATION:
				IntroductionToApplication introductionToApplication = IntroductionToApplication.Factory
						.newInstance();
				introductionToApplication
						.setAttFile(getAttachedFileType(narrative));
				researchPlanAttachments
						.setIntroductionToApplication(introductionToApplication);
				break;
			case SPECIFIC_AIMS:
				SpecificAims specificAims = SpecificAims.Factory.newInstance();
				specificAims.setAttFile(getAttachedFileType(narrative));
				researchPlanAttachments.setSpecificAims(specificAims);
				break;
			case BACKGROUND_SIGNIFICANCE:
				BackgroundSignificance backgroundSignificance = BackgroundSignificance.Factory
						.newInstance();
				backgroundSignificance
						.setAttFile(getAttachedFileType(narrative));
				researchPlanAttachments
						.setBackgroundSignificance(backgroundSignificance);
				break;
			case PROGRESS_REPORT:
				ProgressReport progressReport = ProgressReport.Factory
						.newInstance();
				progressReport.setAttFile(getAttachedFileType(narrative));
				researchPlanAttachments.setProgressReport(progressReport);
				break;
			case RESEARCH_DESIGN_METHODS:
				ResearchDesignMethods researchDesignMethods = ResearchDesignMethods.Factory
						.newInstance();
				researchDesignMethods
						.setAttFile(getAttachedFileType(narrative));
				researchPlanAttachments
						.setResearchDesignMethods(researchDesignMethods);
				break;
			case INCLUSION_ENROLLMENT_REPORT:
				InclusionEnrollmentReport inclusionEnrollmentReport = InclusionEnrollmentReport.Factory
						.newInstance();
				inclusionEnrollmentReport
						.setAttFile(getAttachedFileType(narrative));
				researchPlanAttachments
						.setInclusionEnrollmentReport(inclusionEnrollmentReport);
				break;
			case PROGRESS_REPORT_PUBLICATION_LIST:
				ProgressReportPublicationList progressReportPublicationList = ProgressReportPublicationList.Factory
						.newInstance();
				progressReportPublicationList
						.setAttFile(getAttachedFileType(narrative));
				researchPlanAttachments
						.setProgressReportPublicationList(progressReportPublicationList);
				break;
			case PROTECTION_OF_HUMAN_SUBJECTS:
				ProtectionOfHumanSubjects protectionOfHumanSubjects = ProtectionOfHumanSubjects.Factory
						.newInstance();
				protectionOfHumanSubjects
						.setAttFile(getAttachedFileType(narrative));
				humanSubjectSection
						.setProtectionOfHumanSubjects(protectionOfHumanSubjects);
				break;
			case INCLUSION_OF_WOMEN_AND_MINORITIES:
				InclusionOfWomenAndMinorities inclusionOfWomenAndMinorities = InclusionOfWomenAndMinorities.Factory
						.newInstance();
				inclusionOfWomenAndMinorities
						.setAttFile(getAttachedFileType(narrative));
				humanSubjectSection
						.setInclusionOfWomenAndMinorities(inclusionOfWomenAndMinorities);
				break;
			case TARGETED_PLANNED_ENROLLMENT_TABLE:
				TargetedPlannedEnrollmentTable tarPlannedEnrollmentTable = TargetedPlannedEnrollmentTable.Factory
						.newInstance();
				tarPlannedEnrollmentTable
						.setAttFile(getAttachedFileType(narrative));
				humanSubjectSection
						.setTargetedPlannedEnrollmentTable(tarPlannedEnrollmentTable);
				break;
			case INCLUSION_OF_CHILDREN:
				InclusionOfChildren inclusionOfChildren = InclusionOfChildren.Factory
						.newInstance();
				inclusionOfChildren.setAttFile(getAttachedFileType(narrative));
				humanSubjectSection.setInclusionOfChildren(inclusionOfChildren);
				break;
			case VERTEBRATE_ANIMALS:
				VertebrateAnimals vertebrateAnimals = VertebrateAnimals.Factory
						.newInstance();
				vertebrateAnimals.setAttFile(getAttachedFileType(narrative));
				otherResearchPlanSections
						.setVertebrateAnimals(vertebrateAnimals);
				break;
			case SELECT_AGENT_RESEARCH:
				SelectAgentResearch selectAgentResearch = SelectAgentResearch.Factory
						.newInstance();
				selectAgentResearch.setAttFile(getAttachedFileType(narrative));
				otherResearchPlanSections
						.setSelectAgentResearch(selectAgentResearch);
				break;
			case MULTIPLE_PI_LEADERSHIP_PLAN:
				MultiplePDPILeadershipPlan multiplePILeadershipPlan = MultiplePDPILeadershipPlan.Factory
						.newInstance();
				multiplePILeadershipPlan
						.setAttFile(getAttachedFileType(narrative));
				otherResearchPlanSections
						.setMultiplePDPILeadershipPlan(multiplePILeadershipPlan);
				break;
			case CONSORTIUM_CONTRACTUAL_ARRANGEMENTS:
				ConsortiumContractualArrangements contractualArrangements = ConsortiumContractualArrangements.Factory
						.newInstance();
				contractualArrangements
						.setAttFile(getAttachedFileType(narrative));
				otherResearchPlanSections
						.setConsortiumContractualArrangements(contractualArrangements);
				break;
			case LETTERS_OF_SUPPORT:
				LettersOfSupport lettersOfSupport = LettersOfSupport.Factory
						.newInstance();
				lettersOfSupport.setAttFile(getAttachedFileType(narrative));
				otherResearchPlanSections.setLettersOfSupport(lettersOfSupport);
				break;
			case RESOURCE_SHARING_PLANS:
				ResourceSharingPlans resourceSharingPlans = ResourceSharingPlans.Factory
						.newInstance();
				resourceSharingPlans.setAttFile(getAttachedFileType(narrative));
				otherResearchPlanSections
						.setResourceSharingPlans(resourceSharingPlans);
				break;
			}
		}
		researchPlanAttachments.setHumanSubjectSection(humanSubjectSection);
		researchPlanAttachments
				.setOtherResearchPlanSections(otherResearchPlanSections);
	}

	/**
	 * 
	 * This method is used to get ApplicationType from
	 * ProposalDevelopmentDocument
	 * 
	 * @return ApplicationType corresponding to the proposal type code.
	 */
	private ApplicationType getApplicationType() {
		ApplicationType applicationType = ApplicationType.Factory.newInstance();
		DevelopmentProposal developmentProposal = pdDoc
				.getDevelopmentProposal();
		if (developmentProposal.getProposalTypeCode() != null
				&& Integer.parseInt(developmentProposal.getProposalTypeCode()) < PROPOSAL_TYPE_CODE_6) {
			// Check <6 to ensure that if proposalType='TASk ORDER", it must
			// not be
			// set. THis is because enum ApplicationType has no
			// entry for TASK ORDER
			TypeOfApplication.Enum typeOfApplication = TypeOfApplication.Enum
					.forInt(Integer.parseInt(developmentProposal
							.getProposalTypeCode()));
			applicationType.setTypeOfApplication(typeOfApplication);
		}
		return applicationType;
	}

	/**
	 * 
	 * This method is used to get List of appendix attachments from
	 * NarrativeAttachmentList
	 * 
	 * @return AttachedFileDataType[] array of attachments for the corresponding
	 *         narrative type code APPENDIX.
	 */
	private AttachedFileDataType[] getAttachedFileDataTypes() {
		int size = 0;
		AttachedFileDataType[] attachedFileDataTypes = null;
		DevelopmentProposal developmentProposal = pdDoc
				.getDevelopmentProposal();
		for (Narrative narrative : developmentProposal.getNarratives()) {
			if (narrative.getNarrativeTypeCode() != null
					&& Integer.parseInt(narrative.getNarrativeTypeCode()) == APPENDIX) {
				size++;
			}
		}
		attachedFileDataTypes = new AttachedFileDataType[size];
		int attachments = 0;
		for (Narrative narrative : developmentProposal.getNarratives()) {
			if (narrative.getNarrativeTypeCode() != null
					&& Integer.parseInt(narrative.getNarrativeTypeCode()) == APPENDIX) {
				attachedFileDataTypes[attachments] = getAttachedFileType(narrative);
				attachments++;
			}
		}
		return attachedFileDataTypes;
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
		return getPHS398ResearchPlan();
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
		PHS398ResearchPlan12 phsResearchPlan = (PHS398ResearchPlan12) xmlObject;
		PHS398ResearchPlan12Document phsResearchPlanDocument = PHS398ResearchPlan12Document.Factory
				.newInstance();
		phsResearchPlanDocument.setPHS398ResearchPlan12(phsResearchPlan);
		return phsResearchPlanDocument;
	}
}
