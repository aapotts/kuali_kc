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
package org.kuali.kra.proposaldevelopment.lookup.keyvalue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.core.web.ui.KeyLabelPair;
import org.kuali.kra.keyvalue.ValuesFinderTestBase;

/**
 * Unit test of the {@link PersonEditableFieldValuesFinder} 
 * 
 */
public class PersonEditableFieldValuesFinderTest extends ValuesFinderTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        setTestClass(PersonEditableFieldValuesFinder.class);
        super.setUp();
    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }
    
    @Override
    protected void addKeyValues() {
        testKeyValues.add(new KeyLabelPair("", "select"));
        testKeyValues.add(new KeyLabelPair("PI", "Proposal Investigator Contact"));
        testKeyValues.add(new KeyLabelPair("COI", "Proposal Investigator Multiple"));
        testKeyValues.add(new KeyLabelPair("KP", "Key Person"));
    }

    /**
     * Basic success case
     * 
     * @see org.kuali.kra.keyvalue.ValuesFinderTestBase#testGetKeyValues()
     */
    @Test 
    public void testGetKeyValues() throws Exception {
        super.testGetKeyValues();
    }
}
