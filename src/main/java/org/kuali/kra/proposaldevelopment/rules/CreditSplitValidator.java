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
package org.kuali.kra.proposaldevelopment.rules;

import static org.kuali.core.util.GlobalVariables.getErrorMap;
import static org.kuali.kra.infrastructure.KeyConstants.ERROR_ALL_PERSON_CREDIT_SPLIT_UPBOUND;
import static org.kuali.kra.infrastructure.KeyConstants.ERROR_PERSON_CREDIT_SPLIT_UPBOUND;
import static org.kuali.kra.infrastructure.KraServiceLocator.getService;

import java.util.Iterator;
import java.util.List;

import org.kuali.core.util.KualiDecimal;
import org.kuali.kra.proposaldevelopment.bo.CreditSplit;
import org.kuali.kra.proposaldevelopment.bo.CreditSplitable;
import org.kuali.kra.proposaldevelopment.bo.InvestigatorCreditType;
import org.kuali.kra.proposaldevelopment.bo.ProposalPerson;
import org.kuali.kra.proposaldevelopment.bo.ProposalPersonCreditSplit;
import org.kuali.kra.proposaldevelopment.bo.ProposalPersonUnit;
import org.kuali.kra.proposaldevelopment.bo.ProposalUnitCreditSplit;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;

import org.kuali.kra.proposaldevelopment.service.KeyPersonnelService;

/**
 * Validates Credit Splits on a <code>{@link ProposalPerson}</code> and/or <code>{@link ProposalPersonUnit}</code> by
 * traversing the tree of <code>{@link ProposalPerson}</code> <code>{@link ProposalPersonUnit}</code> instances.
 *
 * @author $Author: lprzybyl $
 * @version $Revision: 1.2 $
 */
public class CreditSplitValidator {
    private static final KualiDecimal CREDIT_UPBOUND = new KualiDecimal(1.0);
    
    /**
     * Validates the credit splits of an entire document by traversing it. If the Investigator is instead a Principal Investigator,
     * the units should all add up to 1.0.
     *
     * @param document The document to validate the credit splits of
     * @return boolean
     */
    public boolean validate(ProposalDevelopmentDocument document) {
        boolean retval = true;

        KualiDecimal allPersonCreditTotal = KualiDecimal.ZERO;
        retval &= validate(document.getInvestigators().iterator(), CreditSplitable.class, allPersonCreditTotal);
        
        // Reloop investigators. Maybe there's a better way to do this.
        for (ProposalPerson investigator : document.getInvestigators()) {
            if (getKeyPersonnelService().isPrincipalInvestigator(investigator)) {
                retval &= validate(investigator.getUnits().iterator(), CreditSplitable.class, KualiDecimal.ZERO);
            }
        }
        
        return retval;        
    }
    
    /**
     * Delegates to either <code>{@link #validateCreditSplitable(Iterator, KualiDecimal)}</code> or 
     * <code>{@link #validateCreditSplit(Iterator, KualiDecimal)}</code> for generic access. Kind of a 
     * hack/workaround, but all the ways of doing this are really hacks because of erasures.
     * 
     * @param collection_it iterator collection that is validated
     * @param iterator_type the type of iterator since the erasure hides this (erases it really)
     * @param cummulative the amount to accumulate through recursion.
     * @param boolean is valid?
     */
    public boolean validate(Iterator collection_it, Class<?> iterator_type, KualiDecimal cummulative) {
        if (CreditSplit.class.isAssignableFrom(iterator_type)) {
            return validateCreditSplit((Iterator<? extends CreditSplit>) collection_it, cummulative);
        }
        return validateCreditSplitable((Iterator<? extends CreditSplitable>) collection_it, cummulative);
    }
    
    /**
     * Validates a collection of anything splitable. This implies that it contains <code>{@link CreditSplit}</code> instances.
     * 
     * @param splitable_it
     * @param greaterCummulative
     * @return boolean is valid?
     */
    public boolean validateCreditSplitable(Iterator<? extends CreditSplitable> splitable_it, KualiDecimal greaterCummulative) {
        boolean retval = true;
     
        if (!splitable_it.hasNext()) {
            return CREDIT_UPBOUND.compareTo(greaterCummulative) > 0;
        }
        
        CreditSplitable splitable = splitable_it.next();
     
        KualiDecimal lesserCummulative = KualiDecimal.ZERO;        
        retval &= validate(splitable.getCreditSplits().iterator(), CreditSplit.class, lesserCummulative);
     
        greaterCummulative.add(lesserCummulative);
     
        return validateCreditSplitable(splitable_it, greaterCummulative);
    }


    /**
     * Validates a collection of anything splits. 
     * 
     * @param creditSplit_it
     * @param lesserCummulative
     * @return boolean is valid?
     */
    public boolean validateCreditSplit(Iterator<? extends CreditSplit> creditSplit_it, KualiDecimal lesserCummulative) {
        if (!creditSplit_it.hasNext()) {
            return CREDIT_UPBOUND.compareTo(lesserCummulative) >= 0;
        }
        
        CreditSplit creditSplit = creditSplit_it.next();
        lesserCummulative.add(creditSplit.getCredit());
     
        return validateCreditSplit(creditSplit_it, lesserCummulative);
    }

    private KeyPersonnelService getKeyPersonnelService() {
        return getService(KeyPersonnelService.class);
    }
}
