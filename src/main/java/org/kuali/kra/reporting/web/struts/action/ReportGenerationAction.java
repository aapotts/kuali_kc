/*
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.kra.reporting.web.struts.action;

import static org.kuali.kra.infrastructure.Constants.MAPPING_BASIC;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.eclipse.birt.report.engine.api.EXCELRenderOption;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.reporting.BirtHelper;
import org.kuali.kra.reporting.bo.BirtParameterBean;
import org.kuali.kra.reporting.bo.CustReportDetails;
import org.kuali.kra.reporting.service.BirtReportService;
import org.kuali.kra.reporting.web.struts.form.ReportGenerationForm;
import org.kuali.kra.rules.ErrorReporter;
import org.kuali.rice.kns.util.WebUtils;
import org.kuali.rice.krad.service.BusinessObjectService;




public class ReportGenerationAction extends ReportGenerationBaseAction {
  
    private BirtReportService birtReportService;
    
    /**
     * sets report parameters to action form     
     * @param mapping the ActionMapping
     * @param form the ActionForm
     * @param request the Request
     * @param response the Response     
     * @return ActionForward     
     */
    public ActionForward getReportParametersFromDesign(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
       
        ReportGenerationForm reportGenerationForm = (ReportGenerationForm) form;
        ArrayList<BirtParameterBean> parameterList = new ArrayList<BirtParameterBean>();       
        if (request.getParameter("reportId") != null) {
            parameterList = KraServiceLocator.getService(BirtReportService.class).getInputParametersFromTemplateFile(
                    request.getParameter("reportId"));
            reportGenerationForm.setReportParameterList(parameterList);
            reportGenerationForm.setReportId(request.getParameter("reportId"));
            reportGenerationForm.setReportName(request.getParameter("reportLabel"));
        }
        return mapping.findForward(MAPPING_BASIC); 
    }
    
    /**
     * prints the selected report     
     * @param mapping the ActionMapping
     * @param form the ActionForm
     * @param request the Request
     * @param response the Response     
     * @return ActionForward     
     */
    public ActionForward printReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
       
        InputStream reportDesignInputStream;
        ByteArrayOutputStream birtFilePrintArrayOutputStream = new ByteArrayOutputStream();
        IReportRunnable iReportRunnableDesign;
        int birtCounter = 0;
        String reportId = request.getParameter("custReportDetails.reportLabelDisplay");
        ArrayList<BirtParameterBean> parameterList = new ArrayList<BirtParameterBean>();
        String printReportFormat = Constants.PDF_REPORT_CONTENT_TYPE;
        String printReportNameAndExtension = Constants.PDF_FILE_EXTENSION;
        boolean isValid = Boolean.TRUE;

        ReportGenerationForm reportGenerationForm = (ReportGenerationForm) form;
        reportDesignInputStream = KraServiceLocator.getService(BirtReportService.class).getReportDesignFileStream(reportId);
        iReportRunnableDesign = BirtHelper.getEngine().openReportDesign(reportDesignInputStream);
        iReportRunnableDesign = getBirtReportService().buildDataSource(iReportRunnableDesign);
        IRunAndRenderTask reportTask = BirtHelper.getEngine().createRunAndRenderTask(iReportRunnableDesign);
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameterList = KraServiceLocator.getService(BirtReportService.class).getInputParametersFromTemplateFile(reportId);
        CustReportDetails reportDetails = KraServiceLocator.getService(BusinessObjectService.class).findBySinglePrimaryKey(
                CustReportDetails.class, reportId);
        reportGenerationForm.setReportParameterList(parameterList);
        reportGenerationForm.setReportId(reportId);
        reportGenerationForm.setReportName(reportDetails.getReportLabel());

        for (BirtParameterBean parameterBean : parameterList) {
            parameters.put(parameterBean.getName(),
                    request.getParameter("reportParameterList[" + birtCounter + "].inputParameterText"));
            birtCounter = birtCounter + 1;
        }
       
        HashMap contextMap = new HashMap();
        reportTask.setAppContext(contextMap);
        reportTask.setParameterValues(parameters);
        isValid = reportTask.validateParameters();
        if (!isValid) {
            (new ErrorReporter()).reportError("reportParameterList[0].inputParameterText",
                    KeyConstants.ERROR_BIRT_REPORT_INPUT_MISSING, "select");
        } else {
            RenderOption renderOption = null;

            if (reportGenerationForm.getReportFormat().equalsIgnoreCase(Constants.REPORT_FORMAT_PDF)) {
                renderOption = new PDFRenderOption();
                printReportFormat = Constants.PDF_REPORT_CONTENT_TYPE;
                printReportNameAndExtension = reportDetails.getReportLabel() + Constants.PDF_FILE_EXTENSION;
                renderOption.setOutputFormat(reportGenerationForm.getReportFormat());
            } else if (reportGenerationForm.getReportFormat().equalsIgnoreCase(Constants.REPORT_FORMAT_HTML)) {
                renderOption = new HTMLRenderOption();
                printReportFormat = Constants.HTML_REPORT_CONTENT_TYPE;
                printReportNameAndExtension = reportDetails.getReportLabel() + Constants.REPORT_FORMAT_HTML_EXTENSION;
                renderOption.setOutputFormat(reportGenerationForm.getReportFormat());
            } else if (reportGenerationForm.getReportFormat().equalsIgnoreCase(Constants.REPORT_FORMAT_EXCEL)) {
                renderOption = new EXCELRenderOption();
                printReportFormat = Constants.EXCEL_REPORT_CONTENT_TYPE;
                printReportNameAndExtension = reportDetails.getReportLabel() + Constants.REPORT_FORMAT_EXCEL_EXTENSION;
                renderOption.setOutputFormat("xls");
            } else {
                printReportFormat = Constants.PDF_REPORT_CONTENT_TYPE;
                renderOption = new PDFRenderOption();
            }

            renderOption.setOutputStream(birtFilePrintArrayOutputStream);
            reportTask.setRenderOption(renderOption);
            reportTask.run();
            WebUtils.saveMimeOutputStreamAsFile(response, printReportFormat, birtFilePrintArrayOutputStream,
                    printReportNameAndExtension);
            reportTask.close();
        }
        return mapping.findForward(MAPPING_BASIC);
    }
    
    public ActionForward close(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return super.close(mapping, form, request, response);
    }

    public void setBirtReportService(BirtReportService birtReportService) {
        this.birtReportService = birtReportService;
    }
    
    public BirtReportService getBirtReportService() {
        return KraServiceLocator.getService(BirtReportService.class);
    }
}
