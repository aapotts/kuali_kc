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
package org.kuali.kra.rules;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.bo.CustomAttribute;
import org.kuali.kra.bo.CustomAttributeDataType;
import org.kuali.kra.bo.CustomAttributeDocument;
import org.kuali.kra.document.ResearchDocumentBase;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.rule.CustomAttributeRule;
import org.kuali.kra.rule.event.SaveCustomAttributeEvent;
import org.kuali.kra.service.CustomAttributeService;
import org.kuali.rice.kns.datadictionary.validation.ValidationPattern;
import org.kuali.rice.kns.datadictionary.validation.charlevel.AnyCharacterValidationPattern;
import org.kuali.rice.kns.datadictionary.validation.charlevel.NumericValidationPattern;
import org.kuali.rice.kns.datadictionary.validation.fieldlevel.DateValidationPattern;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.util.RiceKeyConstants;

public class KraCustomAttributeRule extends ResearchDocumentRuleBase implements CustomAttributeRule {

    private static final Map<String, ValidationPattern> VALIDATION_CLASSES;
    static {
        final Map<String, ValidationPattern> tempPatterns = new HashMap<String, ValidationPattern>();
        
        final AnyCharacterValidationPattern anyCharVal = new AnyCharacterValidationPattern();
        anyCharVal.setAllowWhitespace(true);
        
        tempPatterns.put("String", anyCharVal);
        tempPatterns.put("Date", new DateValidationPattern());
        tempPatterns.put("Number", new NumericValidationPattern());
        
        VALIDATION_CLASSES = Collections.unmodifiableMap(tempPatterns);
    }
    
    /** 
     * gets the {@link CustomAttributeService CustomAttributeService}
     * @return {@link CustomAttributeService CustomAttributeService}
     */
    protected CustomAttributeService getCustomAttributeService() {
        return KraServiceLocator.getService(CustomAttributeService.class);
    }
    
    /**
     * 
     * @see org.kuali.kra.rule.CustomAttributeRule#processCustomAttributeRules(org.kuali.kra.rule.event.SaveCustomAttributeEvent)
     */
    public boolean processCustomAttributeRules(SaveCustomAttributeEvent saveCustomAttributeEvent) {
        ResearchDocumentBase document = (ResearchDocumentBase) saveCustomAttributeEvent.getDocument();
        Map<String, CustomAttributeDocument> customAttributeDocuments = document.getCustomAttributeDocuments();
        boolean valid = true;
        for (Map.Entry<String, CustomAttributeDocument> customAttributeDocumentEntry : customAttributeDocuments.entrySet()) {
            CustomAttributeDocument customAttributeDocument = customAttributeDocumentEntry.getValue();

            CustomAttribute customAttribute = customAttributeDocument.getCustomAttribute();
            String errorKey = "customAttributeValues(id" + customAttribute.getId() + ")";
            if (StringUtils.isNotBlank(customAttribute.getValue())) {
                valid &= validateAttributeFormat(customAttribute, errorKey);
            }
        }

        return valid;
    }


    /**
     * 
     * This method is to validate the format/length of custom attribute
     * @param customAttribute
     * @param errorKey
     * @return
     */
    private boolean validateAttributeFormat(CustomAttribute customAttribute, String errorKey) {

        CustomAttributeDataType customAttributeDataType = customAttribute.getCustomAttributeDataType();
        String attributeValue = customAttribute.getValue();
        if (customAttributeDataType == null && customAttribute.getDataTypeCode() != null) {
            customAttributeDataType = this.getCustomAttributeService().getCustomAttributeDataType(
                    customAttribute.getDataTypeCode());
        }

        if (customAttributeDataType != null) {
            Integer maxLength = customAttribute.getDataLength();
            if ((maxLength != null) && (maxLength.intValue() < attributeValue.length())) {
                GlobalVariables.getErrorMap().putError(errorKey, RiceKeyConstants.ERROR_MAX_LENGTH,
                        new String[] {customAttribute.getLabel(), maxLength.toString() });
                return false;
            }

            final ValidationPattern validationPattern = VALIDATION_CLASSES.get(customAttributeDataType.getDescription());

            // if there is no data type matched, then set error ?
            Pattern validationExpression = validationPattern.getRegexPattern();
            
            String validFormat = getValidFormat(customAttributeDataType.getDescription());

            if (validationExpression != null && !validationExpression.pattern().equals(".*")) {

                if (!validationExpression.matcher(attributeValue).matches()) {
                    GlobalVariables.getErrorMap().putError(errorKey, KeyConstants.ERROR_INVALID_FORMAT_WITH_FORMAT,
                            new String[] {customAttribute.getLabel(), attributeValue, validFormat });
                    return false;
                }
            }
        }
        return true;
    }
    
    private String getValidFormat(String dataType) {
        String validFormat = Constants.DATA_TYPE_STRING;
        if(dataType.equalsIgnoreCase("Number")) {
            validFormat = Constants.DATA_TYPE_NUMBER;
        }else if(dataType.equalsIgnoreCase("Date")) {
            validFormat = Constants.DATA_TYPE_DATE;
        }
        return validFormat;
    }


}
