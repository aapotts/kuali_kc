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
package org.kuali.kra.award.printing.xmlstream;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import noNamespace.AmountInfoType;
import noNamespace.AwardNoticeDocument;
import noNamespace.AwardType;
import noNamespace.AwardNoticeDocument.AwardNotice;
import noNamespace.AwardType.AwardAmountInfo;

import org.apache.xmlbeans.XmlObject;
import org.kuali.kra.award.document.AwardDocument;
import org.kuali.kra.award.home.Award;
import org.kuali.kra.award.printing.AwardPrintParameters;
import org.kuali.kra.award.printing.AwardPrintType;
import org.kuali.kra.document.ResearchDocumentBase;
import org.kuali.kra.service.VersionHistoryService;
import org.kuali.kra.timeandmoney.transactions.AwardAmountTransaction;

/**
 * This class generates XML that conforms with the XSD related to Award Budget
 * History Transaction Report. The data for XML is derived from
 * {@link ResearchDocumentBase} and {@link Map} of details passed to the class.
 * 
 * 
 */
public class AwardBudgetHistoryTransactionXmlStream extends AwardBudgetBaseStream {

	private static final String AWARD_AMOUNT_INFO_MODIFIED_VALUE = "1";
	private final String TRANSACTION_ID = "transactionId";
	private VersionHistoryService versionHistoryService;

	/**
	 * This method generates XML for Award Budget History Transaction Report. It
	 * uses data passed in {@link ResearchDocumentBase} for populating the XML
	 * nodes. The XMl once generated is returned as {@link XmlObject}
	 * 
	 * @param document
	 *            using which XML is generated
	 * @param reportParameters
	 *            parameters related to XML generation
	 * @return {@link XmlObject} representing the XML
	 */
	public Map<String, XmlObject> generateXmlStream(
			ResearchDocumentBase document, Map<String, Object> reportParameters) {
		Map<String, XmlObject> budgetHierarchyMap = new HashMap<String, XmlObject>();
		AwardDocument awardDocument = (AwardDocument) document;

		Award award = awardDocument.getAward();
		int highestTransactionIdIndex = (Integer) reportParameters
				.get(AwardPrintParameters.HIGHEST_TRANSACTION_ID_INDEX
						.getAwardPrintParameter());
		AwardNoticeDocument awardNoticeDocument = AwardNoticeDocument.Factory
				.newInstance();
		AwardNotice awardNotice = AwardNotice.Factory.newInstance();
		awardNotice.setAward(getAwardType(award, highestTransactionIdIndex));
		awardNotice.setSchoolInfo(getSchoolInfoType());
		awardNoticeDocument.setAwardNotice(awardNotice);
		budgetHierarchyMap.put(AwardPrintType.AWARD_BUDGET_HISTORY_TRANSACTION
				.getAwardPrintType(), awardNoticeDocument);
		return budgetHierarchyMap;
	}

	/*
	 * This method will get the transaction id from input report parameters
	 */
	private Long getTransactionId(Map<String, Object> reportParameters) {
		Long transactionId = null;
		if (reportParameters.get(TRANSACTION_ID) != null) {
			transactionId = Long.valueOf(String.valueOf(reportParameters
					.get(TRANSACTION_ID)));
		}

		return transactionId;
	}

	/*
	 * This method will set the values to Award type xml object attributes. It
	 * will set the following values like award amount info , Transaction info .
	 * 
	 */
	private AwardType getAwardType(Award award, int highestTransactionIdIndex) {
		AwardType awardType = AwardType.Factory.newInstance();
		awardType.setAwardAmountInfo(getAwardAmountInfo(award,
				highestTransactionIdIndex));
		awardType.setAwardTransactionInfo(getAwardTransactiontInfo(award));
		return awardType;
	}

	/*
	 * This method will set the values to award amount info xml object
	 * attributes.
	 */
	private AwardAmountInfo getAwardAmountInfo(Award award,
			int highestTransactionIdIndex) {

		AmountInfoType amountInfoType = null;
		AwardAmountInfo awardAmountInfo = AwardAmountInfo.Factory.newInstance();
		List<AmountInfoType> amountInfoTypes = new ArrayList<AmountInfoType>();
		
		org.kuali.kra.award.home.AwardAmountInfo highestAwardAmountInfo = null;
		// highestAwardAmountInfo = award
		// .getAwardAmountInfos().get(highestTransactionIdIndex);
		// FIXME TransactionId comes null for first item. This could be
		// due to some bug. As a workaround until bug is fixed, the list
		// item with empty transaction Id will be skipped and below code will
		// fetch latest award amount transaction
		int index = 0;
		long highestTransactionId = 0;
		highestTransactionIdIndex = 0;
		for (org.kuali.kra.award.home.AwardAmountInfo AmountInfo : award
				.getAwardAmountInfos()) {
			if (AmountInfo.getTransactionId() != null
					&& AmountInfo.getTransactionId() > highestTransactionId) {
				highestTransactionId = AmountInfo.getTransactionId();
				highestTransactionIdIndex = index;
			}
			index++;
		}
		// Once the bug is fixed, above code may be deleted.

		highestAwardAmountInfo = award.getAwardAmountInfos().get(
				highestTransactionIdIndex);

		Long transactionId = highestAwardAmountInfo.getTransactionId();
		amountInfoType = setAwardAmountInfo(award, highestAwardAmountInfo,
				transactionId);
		org.kuali.kra.award.home.AwardAmountInfo prevAwardAmount = getPrevAwardAmountInfo(
				award, transactionId, award.getAwardNumber());
		setAwardAmountInfoModifiedValues(amountInfoType,
				highestAwardAmountInfo, prevAwardAmount);
		amountInfoTypes.add(amountInfoType);

		awardAmountInfo.setAmountInfoArray(amountInfoTypes
				.toArray(new AmountInfoType[0]));
		return awardAmountInfo;
	}

	/*
	 * This method will set the values to award amount info xml object
	 * attributes which are modified by comapring previous award amount info
	 */
	private void setAwardAmountInfoModifiedValues(
			AmountInfoType amountInfoType,
			org.kuali.kra.award.home.AwardAmountInfo awrdAmountInfo,
			org.kuali.kra.award.home.AwardAmountInfo prevAwardAmount) {
		if (prevAwardAmount != null) {
			if (prevAwardAmount.getAmountObligatedToDate() != awrdAmountInfo
					.getAmountObligatedToDate()) {
				amountInfoType
						.setAmtObligatedToDateModified(AWARD_AMOUNT_INFO_MODIFIED_VALUE);
			}
			if (prevAwardAmount.getObliDistributableAmount() != awrdAmountInfo
					.getObliDistributableAmount()) {
				amountInfoType
						.setObligatedDistributableAmtModified(AWARD_AMOUNT_INFO_MODIFIED_VALUE);
			}
			if (prevAwardAmount.getAnticipatedTotalAmount() != awrdAmountInfo
					.getAnticipatedTotalAmount()) {
				amountInfoType
						.setAnticipatedTotalAmtModified(AWARD_AMOUNT_INFO_MODIFIED_VALUE);
			}
			if (prevAwardAmount.getAntDistributableAmount() != awrdAmountInfo
					.getAntDistributableAmount()) {
				amountInfoType
						.setAnticipatedDistributableAmtModified(AWARD_AMOUNT_INFO_MODIFIED_VALUE);
			}
			if (!prevAwardAmount.getObligationExpirationDate().equals(
					awrdAmountInfo.getObligationExpirationDate())) {
				amountInfoType
						.setObligationExpDateModified(AWARD_AMOUNT_INFO_MODIFIED_VALUE);
			}
			if (!prevAwardAmount.getCurrentFundEffectiveDate().equals(
					awrdAmountInfo.getCurrentFundEffectiveDate())) {
				amountInfoType
						.setCurrentFundEffectiveDateModified(AWARD_AMOUNT_INFO_MODIFIED_VALUE);
			}
			if (!prevAwardAmount.getFinalExpirationDate().equals(
					awrdAmountInfo.getFinalExpirationDate())) {
				amountInfoType
						.setFinalExpDateModified(AWARD_AMOUNT_INFO_MODIFIED_VALUE);
			}
		}
	}

	/*
	 * This method will get the previous award amount info for given transaction
	 * id
	 */
	private org.kuali.kra.award.home.AwardAmountInfo getPrevAwardAmountInfo(
			Award award, Long transactionId, String awardNumber) {
		int prevTransactionId = 0;
		List<AwardAmountTransaction> awardAmountTransactions = getAwardAmountTransactions(awardNumber);
		boolean transactionIdFound = false;
		for (AwardAmountTransaction timeAndMoneyActionSummary : awardAmountTransactions) {
			if (transactionId == timeAndMoneyActionSummary
					.getAwardAmountTransactionId().intValue()) {
				transactionIdFound = true;
			}
			if (transactionIdFound) {
				prevTransactionId = timeAndMoneyActionSummary
						.getAwardAmountTransactionId().intValue();
				break;
			}
		}
		org.kuali.kra.award.home.AwardAmountInfo prevAwardAmount = getPrevAwardAmountInfo(
				award, prevTransactionId);
		return prevAwardAmount;
	}

	/*
	 * This method will get the previous awardAmountInfo for transaction id
	 */
	private org.kuali.kra.award.home.AwardAmountInfo getPrevAwardAmountInfo(
			Award award, int transactionId) {
		org.kuali.kra.award.home.AwardAmountInfo awardAmountInfo = null;
		for (org.kuali.kra.award.home.AwardAmountInfo awardAmount : award
				.getAwardAmountInfos()) {
			if (awardAmount.getTransactionId() == null) {
				// FIXME TransactionId comes null for first item. This could be
				// due to some bug. As a workaround until bug is fixed, the list
				// item with empty transaction Id will be skipped
				continue;
			}
			if (transactionId == awardAmount.getTransactionId()) {
				awardAmountInfo = awardAmount;
				break;
			}
		}
		return awardAmountInfo;
	}

	/*
	 * This method will return the award amount transaction list from
	 * timeAndMoney document,which matches award number given.
	 */
	private List<AwardAmountTransaction> getAwardAmountTransactions(
			String awardNumber) {
		Map<String, String> timeAndMoneyMap = new HashMap<String, String>();
		timeAndMoneyMap.put(AWARD_NUMBER_PARAMETER, awardNumber);
		List<AwardAmountTransaction> awardAmountTransactions = (List<AwardAmountTransaction>) businessObjectService
				.findMatching(AwardAmountTransaction.class, timeAndMoneyMap);
		return awardAmountTransactions;
	}

	/*
	 * This method will set the values to award amount info xml object
	 * attributes based on selected award number and transaction id.
	 */
	private AmountInfoType setAwardAmountInfo(Award award,
			org.kuali.kra.award.home.AwardAmountInfo awardAmount,
			Long transacationIdValue) {
		AmountInfoType amountInfoType = AmountInfoType.Factory.newInstance();
		amountInfoType.setTransactionDate(dateTimeService
				.getCalendar(awardAmount.getUpdateTimestamp()));
		if (award.getAccountNumber() != null) {
			amountInfoType.setAccountNumber(award.getAccountNumber());
		}
		if (transacationIdValue != null) {
			amountInfoType.setAmountSequenceNumber(transacationIdValue
					.intValue());
		}
		if (awardAmount.getAmountObligatedToDate() != null) {
			amountInfoType.setAmtObligatedToDate(awardAmount
					.getAmountObligatedToDate().bigDecimalValue());
		}
		if (awardAmount.getAnticipatedChange() != null) {
			amountInfoType.setAnticipatedChange(awardAmount
					.getAnticipatedChange().bigDecimalValue());
		}
		if (awardAmount.getAnticipatedChangeDirect() != null) {
			amountInfoType.setAnticipatedChangeDirect(BigDecimal
					.valueOf(awardAmount.getAnticipatedChangeDirect()));
		}
		if (awardAmount.getAnticipatedChangeIndirect() != null) {
			amountInfoType.setAnticipatedChangeIndirect(BigDecimal
					.valueOf(awardAmount.getAnticipatedChangeIndirect()));
		}
		if (awardAmount.getAntDistributableAmount() != null) {
			amountInfoType.setAnticipatedDistributableAmt(awardAmount
					.getAntDistributableAmount().bigDecimalValue());
		}
		if (awardAmount.getAnticipatedTotalAmount() != null) {
			amountInfoType.setAnticipatedTotalAmt(awardAmount
					.getAnticipatedTotalAmount().bigDecimalValue());
		}
		if (awardAmount.getAnticipatedTotalDirect() != null) {
			amountInfoType.setAnticipatedTotalDirect(awardAmount
					.getAnticipatedTotalDirect().bigDecimalValue());
		}
		if (awardAmount.getAnticipatedTotalIndirect() != null) {
			amountInfoType.setAnticipatedTotalIndirect(awardAmount
					.getAnticipatedTotalIndirect().bigDecimalValue());
		}
		amountInfoType.setAwardNumber(award.getAwardNumber());
		if (awardAmount.getObligationExpirationDate() != null) {
			amountInfoType.setObligationExpirationDate(dateTimeService
					.getCalendar(awardAmount.getObligationExpirationDate()));
		}
		if (awardAmount.getObligatedTotalIndirect() != null) {
			amountInfoType.setObligatedTotalIndirect(awardAmount
					.getObligatedTotalIndirect().bigDecimalValue());
		}
		if (awardAmount.getObligatedTotalDirect() != null) {
			amountInfoType.setObligatedTotalDirect(awardAmount
					.getObligatedTotalDirect().bigDecimalValue());
		}
		if (awardAmount.getCurrentFundEffectiveDate() != null) {
			amountInfoType.setCurrentFundEffectiveDate(dateTimeService
					.getCalendar(awardAmount.getCurrentFundEffectiveDate()));
		}
		return amountInfoType;
	}

	public VersionHistoryService getVersionHistoryService() {
		return versionHistoryService;
	}

	public void setVersionHistoryService(
			VersionHistoryService versionHistoryService) {
		this.versionHistoryService = versionHistoryService;
	}
}
