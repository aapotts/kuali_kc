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
package org.kuali.kra.award.lookup.keyvalue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kuali.core.web.ui.KeyLabelPair;

public class ReportCodeValuesFinderTest {
    
    ReportCodeValuesFinder reportCodeValuesFinder;
    List<KeyLabelPair> reportCodes;

    @Before
    public void setUp() throws Exception {
        reportCodeValuesFinder = new ReportCodeValuesFinder();
        reportCodeValuesFinder.setReportClassCode("1");
        reportCodes = new ArrayList<KeyLabelPair>();
        reportCodes = reportCodeValuesFinder.getKeyValues();
    }

    @After
    public void tearDown() throws Exception {
        reportCodeValuesFinder = null;
        reportCodes = null;
    }

    @Test
    public final void testGetKeyValues() {
        Assert.assertEquals(11,reportCodes.size());
    }
    
    @Test
    public final void testGetKeyValuesAreNotNull() {
        for(KeyLabelPair keyLabelPair:reportCodes){
            Assert.assertNotNull(keyLabelPair.getKey());
            Assert.assertNotNull(keyLabelPair.getLabel());
        }
    }
    

}

