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
package org.kuali.kra.s2s.bo.id;

import java.io.Serializable;

import javax.persistence.Column;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Primary Key for the S2sOppForms BO.
 */
@SuppressWarnings("serial")
public class S2sOppFormsId implements Serializable {
    
    @Column(name="PROPOSAL_NUMBER")
    private String proposalNumber;

    @Column(name="OPP_NAME_SPACE")
    private String oppNameSpace;
    
    public String getProposalNumber() {
        return this.proposalNumber;
    }
    
    public String getOppNameSpace() {
        return this.oppNameSpace;
    }
    
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof S2sOppFormsId)) return false;
        if (obj == null) return false;
        S2sOppFormsId other = (S2sOppFormsId) obj;
        return StringUtils.equals(proposalNumber, other.proposalNumber) &&
               StringUtils.equals(oppNameSpace, other.oppNameSpace);
    }
    
    public int hashCode() {
        return new HashCodeBuilder().append(proposalNumber).append(oppNameSpace).toHashCode();
    }
}
