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
package org.kuali.kra.award.bo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AwardTest { 
    private static final int AWARD_ATTRIBUTES_COUNT = 44;
    
    private Award awardBo;
    
    @Before
    public void setUp() throws Exception {
        awardBo = new Award();
    }

    @After
    public void tearDown() throws Exception {
        awardBo = null;
    }
    
    @Test
    public void testAwardBoAttributesCount() throws Exception {              
        Assert.assertEquals(awardBo.toStringMapper().size(),AWARD_ATTRIBUTES_COUNT);
    }
    
}
