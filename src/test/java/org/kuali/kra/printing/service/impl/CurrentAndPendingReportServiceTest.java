/*
 * Copyright 2005-2010 The Kuali Foundation
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
package org.kuali.kra.printing.service.impl;

import org.junit.Test;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.printing.service.CurrentAndPendingReportService;
import org.kuali.kra.printing.util.PrintingServiceTestBase;
import org.kuali.kra.printing.util.PrintingTestUtils;
import org.kuali.kra.proposaldevelopment.bo.AttachmentDataSource;

/**
 * This class tests InstituteProposalPrintingService. It tests all reports that
 * are part of Institute Proposal printing.
 * 
 */

public class CurrentAndPendingReportServiceTest extends
		PrintingServiceTestBase {
	private CurrentAndPendingReportService currentAndPendingReportService;

	/**
	 * This method tests Current Proposal Report. It generates PDF bytes for the
	 * report.
	 */
	@Test
	public void testCurrentProposalReportPrinting() {
		try {
			AttachmentDataSource pdfBytes = getPrintingService()
			        .printCurrentAndPendingSupportReport(CurrentAndPendingReportService.CURRENT_REPORT_TYPE,
							PrintingTestUtils.getCurrentProposalReportParameters());
			// FIXME Writing PDF to disk for testing purpose only.
			PrintingTestUtils.writePdftoDisk(pdfBytes,
			        CurrentAndPendingReportService.CURRENT_REPORT_TYPE);
			assertNotNull(pdfBytes);
		} catch (Exception e) {
			e.printStackTrace();
			//assert false;
			throw new RuntimeException(e);
		}
	}

	/**
	 * This method tests Pending Proposal Report. It generates PDF bytes for the
	 * report.
	 */
	@Test
	public void testPendingProposalReportPrinting() {
		try {
			AttachmentDataSource pdfBytes = getPrintingService()
					.printCurrentAndPendingSupportReport(CurrentAndPendingReportService.PENDING_REPORT_TYPE,
							PrintingTestUtils.getPendingProposalReportParameters());
			// FIXME Writing PDF to disk for testing purpose only.
			PrintingTestUtils.writePdftoDisk(pdfBytes,
					CurrentAndPendingReportService.PENDING_REPORT_TYPE);
			assertNotNull(pdfBytes);
		} catch (Exception e) {
			e.printStackTrace();
			assert false;
		}
	}


	public CurrentAndPendingReportService getPrintingService() {
		if (currentAndPendingReportService != null) {
			currentAndPendingReportService = KraServiceLocator
					.getService(CurrentAndPendingReportService.class);
		}
		return currentAndPendingReportService;
	}
}
