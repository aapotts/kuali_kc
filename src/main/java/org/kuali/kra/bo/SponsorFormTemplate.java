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
package org.kuali.kra.bo;

import java.util.LinkedHashMap;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerException;
import org.apache.struts.upload.FormFile;
import org.kuali.core.bo.PersistableAttachment;

public class SponsorFormTemplate extends KraPersistableBusinessObjectBase implements PersistableAttachment{
	private Integer packageNumber;
	private Integer pageNumber;
	private String sponsorCode;
	private byte[] formTemplate;
	private String pageDescription;
    private String fileName;
    private String contentType;
    private byte[] attachmentContent;
    private FormFile templateFile;
    private SponsorForms sponsorForms;
    private Boolean selectToPrint = false;

	public Integer getPackageNumber() {
		return packageNumber;
	}

    public byte[] getAttachmentContent() {
        return this.attachmentContent;
    }

    public void setAttachmentContent(byte[] attachmentContent) {
        this.attachmentContent = attachmentContent;
    }

    public void setPackageNumber(Integer packageNumber) {
		this.packageNumber = packageNumber;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getSponsorCode() {
		return sponsorCode;
	}

	public void setSponsorCode(String sponsorCode) {
		this.sponsorCode = sponsorCode;
	}

	public byte[] getFormTemplate() {
		return formTemplate;
	}

	public void setFormTemplate(byte[] formTemplate) {
		this.formTemplate = formTemplate;
	}

	public String getPageDescription() {
		return pageDescription;
	}

	public void setPageDescription(String pageDescription) {
		this.pageDescription = pageDescription;
	}


	@Override 
	protected LinkedHashMap toStringMapper() {
		LinkedHashMap hashMap = new LinkedHashMap();
		hashMap.put("packageNumber", getPackageNumber());
		hashMap.put("pageNumber", getPageNumber());
		hashMap.put("sponsorCode", getSponsorCode());
		hashMap.put("formTemplate", getFormTemplate());
		hashMap.put("pageDescription", getPageDescription());
		return hashMap;
	}

    public FormFile getTemplateFile() {
        return templateFile;
    }
    
    public void setTemplateFile(FormFile templateFile) {
        this.templateFile = templateFile;
    }

    public void beforeInsert(PersistenceBroker persistenceBroker) throws PersistenceBrokerException {
	    super.beforeInsert(persistenceBroker);
	}

	public void afterLookup(PersistenceBroker persistenceBroker) throws PersistenceBrokerException {
	    super.afterLookup(persistenceBroker);
	}	

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public final SponsorForms getSponsorForms() {
        return sponsorForms;
    }

    public final void setSponsorForms(SponsorForms sponsorForms) {
        this.sponsorForms = sponsorForms;
    }
    

    public final Boolean getSelectToPrint() {
        return selectToPrint;
    }

    public final void setSelectToPrint(Boolean selectToPrint) {
        this.selectToPrint = selectToPrint;
    }
    
}
