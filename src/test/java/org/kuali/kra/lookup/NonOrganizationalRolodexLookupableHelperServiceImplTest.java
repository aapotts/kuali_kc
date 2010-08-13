/*
 * Copyright 2005-2010 The Kuali Foundation
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
package org.kuali.kra.lookup;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.kra.bo.NonOrganizationalRolodex;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.test.infrastructure.KcUnitTestBase;
import org.kuali.rice.kns.lookup.KualiLookupableImpl;

/**
 * Class for testing units of functionality for the <code>{@link NonOrganizationalLookupableHelperServiceImpl}
 * 
 */
public class NonOrganizationalRolodexLookupableHelperServiceImplTest extends KcUnitTestBase {

    /**
     * 
     * @throws Exception 
     * @see org.kuali.kra.KraTestBase#setUp()
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }
    
    /**
     * 
     * @throws Exception 
     * @see org.kuali.kra.KraTestBase#tearDown()
     */
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }
    
    @Test
    public void getResultsNonOrganizational() {
        Map fieldValues = new HashMap();
        fieldValues.put("organization", "Lockheed*"); // Search for organizations that start with National
        fieldValues.put("firstName", "Chris"); // Search for organizations that start with National
        
        KualiLookupableImpl lookupableService = KraServiceLocator.getService("nonOrganizationalRolodexLookupable");
        lookupableService.setBusinessObjectClass(NonOrganizationalRolodex.class);
        
        Collection results = lookupableService.getSearchResults(fieldValues);

        assertEquals(1, results.size());
    }
    
    @Test
    public void getResultsOrganizationalOnly() {
        Map fieldValues = new HashMap();
        fieldValues.put("organization", "George*"); // Search for organizations that start with National
        
        KualiLookupableImpl lookupableService = KraServiceLocator.getService("nonOrganizationalRolodexLookupable");
        lookupableService.setBusinessObjectClass(NonOrganizationalRolodex.class);
        
        Collection results = lookupableService.getSearchResults(fieldValues);
        
        assertEquals(6, results.size());
    }
}
