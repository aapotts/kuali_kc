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
package org.kuali.kra.award.comparator;

import java.util.Comparator;

import org.kuali.kra.award.bo.AwardReportTerm;

/**
 * 
 * This class is a comparator for AwardReportTerm's frequencyCode object.
 * It is used for sorting.
 */
public class AwardReportTermsComparator2 implements Comparator<AwardReportTerm> {
    public int compare(AwardReportTerm awardReportTerm1, AwardReportTerm awardReportTerm2) {
        return  awardReportTerm1.getFrequencyCode().compareTo(awardReportTerm2.getFrequencyCode());
    }
  }
