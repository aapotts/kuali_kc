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
package org.kuali.kra.bo;

import javax.persistence.Version;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.CascadeType;
import javax.persistence.Table;
import javax.persistence.Entity;

import java.util.LinkedHashMap;


@Entity
@Table(name="SP_REV_APPROVAL_TYPE")
public class SpecialReviewApprovalType  extends KraPersistableBusinessObjectBase {

    @Id
	@Column(name="APPROVAL_TYPE_CODE")
	private String approvalTypeCode;
    @Column(name="DESCRIPTION")
	private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApprovalTypeCode() {
        return approvalTypeCode;
    }

    public void setApprovalTypeCode(String approvalTypeCode) {
        this.approvalTypeCode = approvalTypeCode;
    }

    @Override
    protected LinkedHashMap toStringMapper() {
        LinkedHashMap propMap = new LinkedHashMap();
        propMap.put("approvalTypeCode", this.getApprovalTypeCode());
        propMap.put("description", this.getDescription());
        propMap.put("updateTimestamp", this.getUpdateTimestamp());
        propMap.put("updateUser", this.getUpdateUser());
        return propMap;
    }

}




