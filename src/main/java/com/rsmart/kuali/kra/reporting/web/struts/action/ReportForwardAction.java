/*
 * Copyright 2005-2010 The Kuali Foundation
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
package com.rsmart.kuali.kra.reporting.web.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.rice.kns.web.struts.action.KualiDocumentActionBase;
import org.kuali.rice.krad.util.GlobalVariables;

import com.rsmart.rfabric.jasperreports.auth.AuthTokenURLGenerator;

/**
 * 
 * This class represents the Struts Action for Commitments page(AwardCommitments.jsp)
 */
public class ReportForwardAction extends KualiDocumentActionBase {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        AuthTokenURLGenerator tokenURLGenerator = new AuthTokenURLGenerator();
        
        String url = tokenURLGenerator.generateRelativeURL(request, "/", null, GlobalVariables.getUserSession().getPrincipalId(), true);
        
        response.sendRedirect(url);
        return null;
    }    
  
    
}
