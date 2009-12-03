/*
 * Copyright 2006-2009 The Kuali Foundation
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
package org.kuali.kra.award.lookup.keyvalue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.award.home.Award;
import org.kuali.kra.award.home.AwardAmountInfo;
import org.kuali.kra.award.lookup.AwardTransactionLookupService;
import org.kuali.kra.award.document.AwardDocument;
import org.kuali.kra.award.AwardForm;
import org.kuali.rice.kns.lookup.keyvalues.KeyValuesBase;
import org.kuali.rice.kns.service.KeyValuesService;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.web.ui.KeyLabelPair;

/**
 * Gets all sequence numbers for the current award id.  See
 * the method <code>getKeyValues()</code> for a full description.
 * 
 * @author Kuali Research Administration Team (kualidev@oncourse.iu.edu)
 */
public class AwardTransactionValuesFinder extends KeyValuesBase {
    
    private AwardTransactionLookupService transactionLookupService;
    
    public AwardTransactionValuesFinder() {
        transactionLookupService = KraServiceLocator.getService(AwardTransactionLookupService.class);
    }
    
    /**
     * Grabs the current award from the current form and loops through all
     * AwardAmountInfos and puts the transaction id in both for key and value for
     * each pair.
     * 
     * @return the list of &lt;key, value&gt; pairs of the current award tranaction ids
     * @see org.kuali.rice.kns.lookup.keyvalues.KeyValuesFinder#getKeyValues()
     */
    public List<KeyLabelPair> getKeyValues() {
        AwardForm form = getAwardForm();
        
        List<KeyLabelPair> keyValues = new ArrayList<KeyLabelPair>();
        Integer usableSequence = form.getAwardPrintChangeReport().getAwardVersion();
        if (usableSequence == null) {
            usableSequence = form.getAwardDocument().getAward().getSequenceNumber();
        }
        List<Long> transactionIds = transactionLookupService.getApplicableTransactionIds(form.getAwardDocument().getAward().getAwardNumber(), 
                usableSequence);
        for (Long id : transactionIds) {
            if (id != null) {
                keyValues.add(new KeyLabelPair(id.toString(), id.toString()));
            }
        }
        return keyValues;
    }
    
    /**
     * Get the Award Document for the current session.  The
     * document is within the current form.
     * 
     * @return the current document or null if not found
     */
    private AwardForm getAwardForm() {
        return (AwardForm) GlobalVariables.getKualiForm();
    }

    private AwardTransactionLookupService getTransactionLookupService() {
        return transactionLookupService;
    }

    public void setTransactionLookupService(AwardTransactionLookupService transactionLookupService) {
        this.transactionLookupService = transactionLookupService;
    }
}
