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
package org.kuali.kra.krms;

public final class KcKrmsConstants {
    
    public static final String UNIT_NUMBER = "Unit Number";
    
    // Ideally these should be in Rice's KrmsConstants
    public static final String MESSAGE_SEPARATOR = ":";
    public static final String MESSAGE_TYPE_ERROR = "E";
    public static final String MESSAGE_TYPE_WARNING = "W";
    
    /**
     * private utility class ctor.
     * @throws UnsupportedOperationException if called.
     */
    private KcKrmsConstants() {
        throw new UnsupportedOperationException("do not call me");
    }
    
    public static final class ProposalDevelopment {
        
        public static final String PROPOSAL_DEVELOPMENT_CONTEXT = "KC Proposal Development Context";
        
        public static final String TOTAL_COST = "totalCost";
        
        public static final String TOTAL_DIRECT_COST = "totalDirectCost";
        
        public static final String TOTAL_INDIRECT_COST = "totalIndirectCost";
        
        public static final String COST_SHARE_AMOUNT = "costShareAmount";
        
        public static final String UNDERRECOVERY_AMOUNT = "underrecoveryAmount";
        
        public static final String TOTAL_COST_INITIAL = "totalCostInitial";
        
        public static final String TOTAL_DIRECT_COST_LIMIT = "totalDirectCostLimit";
        
        public static final String CFDA_NUMBER = "cfdaNumber";
        
        public static final String OPPORTUNITY_ID = "opportunityId";
        
    }
    
    public static final class IrbProtocol {
        
        public static final String IRB_PROTOCOL_CONTEXT = "KC IRB Protocol Context";
        
        public static final String PROTOCOL_REFERENCE_NUMBER_1 = "protocolReferenceNumber1";
        
        public static final String PROTOCOL_REFERENCE_NUMBER_2 = "protocolReferenceNumber2";
        
        public static final String FDA_APPLICATION_NUMBER = "fdaApplicationNumber";
    }
    
    public static final class IacucProtocol {
        
        public static final String IACUC_PROTOCOL_CONTEXT = "KC IACUC Protocol Context";
        
        public static final String IACUC_REFERENCE_NUMBER_1 = "iacucReferenceNumber1";
        
        public static final String IACUC_REFERENCE_NUMBER_2 = "iacucReferenceNumber2";
        
        public static final String IACUC_FDA_APPLICATION_NUMBER = "iacucFdaApplicationNumber";
    }

}
