/*
 * Copyright 2007 The Kuali Foundation.
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
package org.kuali.kra.budget.bo;

import java.util.LinkedHashMap;

import org.kuali.kra.bo.KraPersistableBusinessObjectBase;

public class BudgetCategoryMapping extends KraPersistableBusinessObjectBase {
	private String coeusCategoryCode;
	private String mappingName;
	private String targetCategoryCode;

	public String getCoeusCategoryCode() {
		return coeusCategoryCode;
	}

	public void setCoeusCategoryCode(String coeusCategoryCode) {
		this.coeusCategoryCode = coeusCategoryCode;
	}

	public String getMappingName() {
		return mappingName;
	}

	public void setMappingName(String mappingName) {
		this.mappingName = mappingName;
	}

	public String getTargetCategoryCode() {
		return targetCategoryCode;
	}

	public void setTargetCategoryCode(String targetCategoryCode) {
		this.targetCategoryCode = targetCategoryCode;
	}


	@Override 
	protected LinkedHashMap toStringMapper() {
		LinkedHashMap hashMap = new LinkedHashMap();
		hashMap.put("coeusCategoryCode", getCoeusCategoryCode());
		hashMap.put("mappingName", getMappingName());
		hashMap.put("targetCategoryCode", getTargetCategoryCode());
		return hashMap;
	}
}
