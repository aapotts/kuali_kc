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
package org.kuali.kra.lookup.keyvalue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.core.datadictionary.BusinessObjectEntry;
import org.kuali.core.lookup.keyvalues.KeyValuesBase;
import org.kuali.core.service.DataDictionaryService;
import org.kuali.core.web.ui.KeyLabelPair;
import org.kuali.kra.infrastructure.KraServiceLocator;

public class LookupableBoValuesFinder extends KeyValuesBase {

    /**
     * Get a list of the lookupable classes
     * @see org.kuali.core.lookup.keyvalues.KeyValuesFinder#getKeyValues()
     */
    public List<KeyLabelPair> getKeyValues() {
        List<KeyLabelPair> keyValues = new ArrayList<KeyLabelPair>();
        keyValues.add(new KeyLabelPair("", "select"));

        DataDictionaryService dataDictionaryService = KraServiceLocator.getService(DataDictionaryService.class);
        // this only has entries that have been loaded - force load?
        dataDictionaryService.forceCompleteDataDictionaryLoad();

    	Map<String, BusinessObjectEntry> businessObjectEntries = dataDictionaryService.getDataDictionary().getBusinessObjectEntries();
    	for (String businessObject: businessObjectEntries.keySet()) {
    	    if ((businessObjectEntries.get(businessObject).hasLookupDefinition()) && (businessObject.startsWith("org.kuali.kra"))) {
        		KeyLabelPair keyLabelPair = new KeyLabelPair(businessObject, StringUtils.removeEnd(businessObjectEntries.get(businessObject).getLookupDefinition().getTitle()," Lookup"));
        		if (!keyValues.contains(keyLabelPair)) {
        		    keyValues.add(keyLabelPair);
        		}
    	    }
    	}

    	return keyValues;
    }

}
