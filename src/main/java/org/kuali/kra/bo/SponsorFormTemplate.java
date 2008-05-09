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

public class SponsorFormTemplate extends KraPersistableBusinessObjectBase {
	private Integer packageNumber;
	private Integer pageNumber;
	private String sponsorCode;
	private byte[] formTemplate;
	private String pageDescription;

	public Integer getPackageNumber() {
		return packageNumber;
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
}
