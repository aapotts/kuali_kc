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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import noNamespace.AwardTransactionType;
import noNamespace.SchoolInfoType2;
import noNamespace.AwardType.AwardTransactionInfo;

import org.kuali.kra.award.home.Award;
import org.kuali.kra.printing.util.PrintingUtils;
import org.kuali.kra.printing.xmlstream.XmlStream;
import org.kuali.kra.timeandmoney.document.TimeAndMoneyDocument;
import org.kuali.kra.timeandmoney.transactions.AwardAmountTransaction;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.service.DateTimeService;

/**
 * <p>
 * This class will contain all common methods that can be used across all XML
 * generator streams related to Award budget hierarchy and Award budget history .
 * All award report XML stream implementations need to extend and use the
 * functions defined in this class.
 * </p>
 * 
 * @author
 * 
 */
public abstract class AwardBudgetBaseStream implements XmlStream {

	protected BusinessObjectService businessObjectService = null;
	protected DateTimeService dateTimeService = null;
	private static final String SCHOOL_NAME = "SCHOOL_NAME";
	private static final String SCHOOL_ACRONYM = "SCHOOL_ACRONYM";
	protected static final String AWARD_NUMBER_PARAMETER = "awardNumber";

	/**
	 * <p>
	 * This method will set the values to award transaction info xml object
	 * attributes
	 * </p>.
	 * 
	 * @return AwardTransactionInfo xml object
	 */
	protected AwardTransactionInfo getAwardTransactiontInfo(Award award) {
		List<AwardTransactionType> awardTransactionTypeList = new ArrayList<AwardTransactionType>();
		AwardTransactionInfo awardTransactionInfo = AwardTransactionInfo.Factory
				.newInstance();
		AwardTransactionType awardTransactionType = null;
		for (org.kuali.kra.award.home.AwardAmountInfo awardAmount : award
				.getAwardAmountInfos()) {
			AwardAmountTransaction awardAmountTransaction = getAwardAmountTransaction(awardAmount
					.getTimeAndMoneyDocumentNumber());
			if (awardAmountTransaction != null) {
				awardTransactionType = AwardTransactionType.Factory
						.newInstance();
				setAwardAmountTransaction(awardTransactionType,
						awardAmountTransaction);
				awardTransactionTypeList.add(awardTransactionType);
			}
		}
		awardTransactionInfo.setTransactionInfoArray(awardTransactionTypeList
				.toArray(new AwardTransactionType[0]));
		return awardTransactionInfo;
	}

	/*
	 * This method will set the values to award amount transaction xml object
	 * attributes
	 */
	private void setAwardAmountTransaction(
			AwardTransactionType awardTransactionType,
			AwardAmountTransaction awardAmountTransaction) {
		if (awardAmountTransaction.getAwardNumber() != null) {
			awardTransactionType.setAwardNumber(awardAmountTransaction
					.getAwardNumber());
		}
		if (awardAmountTransaction.getTransactionTypeCode() != null) {
			awardTransactionType.setTransactionTypeCode(awardAmountTransaction
					.getTransactionTypeCode());
		}
		if (awardAmountTransaction.getAuthorPersonName() != null) {
			awardTransactionType.setTransactionTypeDesc(awardAmountTransaction
					.getAuthorPersonName());
		}
		if (awardAmountTransaction.getComments() != null) {
			awardTransactionType.setComments(awardAmountTransaction
					.getComments());
		}
		if (awardAmountTransaction.getNoticeDate() != null) {
			awardTransactionType.setNoticeDate(dateTimeService
					.getCalendar(awardAmountTransaction.getNoticeDate()));
		}
	}

	/*
	 * This method will get the AwardAmountTransaction for given
	 * timeAndMoneyDocument Number
	 */
	private AwardAmountTransaction getAwardAmountTransaction(
			String timeAndMoneyDocNumber) {
		AwardAmountTransaction awardAmountTransaction = null;
		Map<String, String> timeAndMoneyMap = new HashMap<String, String>();
		// Time Money Doc number - to be fixed
		timeAndMoneyMap.put("", timeAndMoneyDocNumber);
		List<TimeAndMoneyDocument> timeAndMoneyDocs = (List<TimeAndMoneyDocument>) businessObjectService
				.findMatching(TimeAndMoneyDocument.class, timeAndMoneyMap);
		if (timeAndMoneyDocs != null && !timeAndMoneyDocs.isEmpty()) {
			TimeAndMoneyDocument andMoneyDocument = timeAndMoneyDocs.get(0);
			awardAmountTransaction = andMoneyDocument
					.getNewAwardAmountTransaction();
		}
		return awardAmountTransaction;
	}

	/**
	 * <p>
	 * This method will set the values to school info attributes and finally
	 * returns SchoolInfoType XmlObject.
	 * </p>
	 * 
	 * @return returns SchoolInfoType XmlObject
	 */
	protected SchoolInfoType2 getSchoolInfoType() {
		SchoolInfoType2 schoolInfoType = SchoolInfoType2.Factory.newInstance();
		String schoolName = PrintingUtils.getParameterValue(SCHOOL_NAME);
		String schoolAcronym = PrintingUtils.getParameterValue(SCHOOL_ACRONYM);
		if (schoolName != null) {
			schoolInfoType.setSchoolName(schoolName);
		}
		if (schoolAcronym != null) {
			schoolInfoType.setAcronym(schoolAcronym);
		}
		return schoolInfoType;
	}

	public BusinessObjectService getBusinessObjectService() {
		return businessObjectService;
	}

	public void setBusinessObjectService(
			BusinessObjectService businessObjectService) {
		this.businessObjectService = businessObjectService;
	}

	public DateTimeService getDateTimeService() {
		return dateTimeService;
	}

	public void setDateTimeService(DateTimeService dateTimeService) {
		this.dateTimeService = dateTimeService;
	}
}
