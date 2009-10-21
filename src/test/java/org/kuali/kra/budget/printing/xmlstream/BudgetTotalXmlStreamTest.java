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
package org.kuali.kra.budget.printing.xmlstream;

import java.util.Map;

import noNamespace.BudgetSalaryDocument;
import noNamespace.SalaryType;

import org.junit.Test;
import org.kuali.kra.budget.BudgetDecimal;
import org.kuali.kra.document.ResearchDocumentBase;
import org.kuali.kra.printing.util.PrintingTestUtils;
import org.kuali.kra.printing.util.XmlStreamTestBase;

/**
 * This class tests generation and validation of XML for Budget Total Report
 * 
 */
public class BudgetTotalXmlStreamTest extends
		XmlStreamTestBase<BudgetTotalXmlStream> {

	@Override
	protected Map<String, Object> getReportParameters() {
		return PrintingTestUtils.getBudgetTotalReportParameters();
	}

	@Override
	protected Class<BudgetTotalXmlStream> getXmlStream() {
		return BudgetTotalXmlStream.class;
	}

	@Override
	protected ResearchDocumentBase prepareData() {
		return PrintingTestUtils.getBudgetDocument();
	}

	private BudgetSalaryDocument getBudgetSalaryDocument() {
		return (BudgetSalaryDocument) xmlObject;
	}

	@Test
	/**
	 * This test the total number of budget periods set on the generated XML
	 * document.
	 */
	public void budgetPeriodTest() {
		BudgetSalaryDocument salaryDocument = getBudgetSalaryDocument();
		assertEquals(1, salaryDocument.getBudgetSalary().getTotalPeriod());
	}

	@Test
	/**
	 * This test validates the calculation of salaries for particular cost
	 * element type in the generated XML document
	 */
	public void budgetPeriodCostTest() {
		BudgetSalaryDocument salaryDocument = getBudgetSalaryDocument();
		for (int i = 0; i < salaryDocument.getBudgetSalary().getSalaryArray().length; i++) {
			SalaryType salaryType = salaryDocument.getBudgetSalary()
					.getSalaryArray(i);
			if ("MTDC-MTDC".equals(salaryType.getName())) {
				assertEquals(new BudgetDecimal(7589.54), new BudgetDecimal(
						salaryType.getPeriodArray(0).getPeriodCost()));
				break;
			}
		}
	}

	@Test
	/**
	 * This test validates the calculation of sum of all the period costs in the
	 * generated XML document
	 */
	public void periodTotalTest() {
		BudgetSalaryDocument salaryDocument = getBudgetSalaryDocument();
		for (int i = 0; i < salaryDocument.getBudgetSalary().getSalaryArray().length; i++) {
			SalaryType salaryType = salaryDocument.getBudgetSalary()
					.getSalaryArray(i);
			if ("Total".equals(salaryType.getName())) {
				assertEquals(new BudgetDecimal(45537.24), new BudgetDecimal(
						salaryType.getPeriodArray(0).getPeriodCost()));
				break;
			}
		}
	}
}
