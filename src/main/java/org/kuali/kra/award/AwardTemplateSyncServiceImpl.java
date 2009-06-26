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
package org.kuali.kra.award;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.kra.award.document.AwardDocument;
import org.kuali.kra.award.home.Award;
import org.kuali.kra.award.home.AwardSyncable;
import org.kuali.kra.award.home.AwardSyncableList;
import org.kuali.kra.award.home.AwardTemplate;
import org.kuali.kra.award.home.AwardTemplateComment;
import org.kuali.kra.award.home.AwardTemplateTerm;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.service.KualiRuleService;
import org.kuali.rice.kns.util.ObjectUtils;
import org.kuali.kra.award.paymentreports.awardreports.AwardReportTerm;
import org.kuali.kra.award.paymentreports.awardreports.AwardReportTermRecipient;

/**
 * This class is the implementation of AwardTemplateSyncService.
 */
public class AwardTemplateSyncServiceImpl implements AwardTemplateSyncService {

    private BusinessObjectService businessObjectService;
    private KualiRuleService kualiRuleService;
    
    private static final Log LOG = LogFactory.getLog(AwardTemplateSyncServiceImpl.class);



    /**
     * 
     * This method is to sync only fields; but not lists
     * @param awardTemplateObject
     * @param awardObject
     * @throws Exception
     */
    private void syncOnlyFields(Object awardTemplateObject, Object awardObject) throws Exception{
        Field[] fields = awardObject.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.isAnnotationPresent(AwardSyncable.class) &&
                    !field.getType().isAssignableFrom(List.class)){
                copyField(awardTemplateObject, awardObject, field);
            }
        }
    }
    /**
     * This method is for extracting the appropriate list from award by using property name and sync the list
     * @param awardTemplateObject
     * @param awardObject
     * @param propertyName
     * @param awardSyncableList 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void extractListFromParentAndSync(Object awardTemplateObject, Object awardObject, Field field) throws Exception {
        field.setAccessible(true);
        List<Object> awardTemplateObjectList = (List)ObjectUtils.getPropertyValue(awardTemplateObject, field.getName());
        AwardSyncableList awardSyncableList = field.getAnnotation(AwardSyncableList.class);
        if(awardTemplateObjectList!=null && !awardTemplateObjectList.isEmpty()){
            if(awardSyncableList.syncMethodName().equalsIgnoreCase(AwardSyncableList.DEFAULT_METHOD)){
                syncListObjects(awardObject,awardTemplateObjectList,field);
            }else{
                invokeMethodToSync((Award)awardObject,awardTemplateObjectList,awardSyncableList.syncMethodName());
            }
        }
    }

    /**
     * 
     * This method is to invoke individual method if there is a method mentioned in the AwardSyncableList annotation
     * @param awardObject
     * @param awardTemplateObjectList
     * @param syncMethodName
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void invokeMethodToSync(Award awardObject, List awardTemplateObjectList,String syncMethodName) throws Exception{
        Method syncMethod = getClass().getMethod(syncMethodName, new Class[]{Award.class,List.class});
        syncMethod.invoke(this, new Object[]{awardObject,awardTemplateObjectList});
    }

    /**
     * 
     * This is an overloaded method to sync a particular list defined in Award object
     * @param awardTemplateObject
     * @param awardObject
     * @param propertyName
     * @throws Exception
     */
    private void sync(Object awardTemplateObject, Object awardObject,String propertyName) throws Exception{
        Field field = awardObject.getClass().getDeclaredField(propertyName);
        extractListFromParentAndSync(awardTemplateObject,awardObject,field);
    }
    /**
     * This method is used to sync member properties of an award template object to an award object
     * 
     * @param awardTemplateObject
     * @param awardObject
     */
    private void sync(Object awardTemplateObject, Object awardObject) throws Exception{
        Field[] fields = awardObject.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.isAnnotationPresent(AwardSyncable.class)){
                copyField(awardTemplateObject, awardObject, field);
            }
            if(field.isAnnotationPresent(AwardSyncableList.class)){
                extractListFromParentAndSync(awardTemplateObject, awardObject, field);
            }
        }
    }

    
    /**
     * This copies value from Award Template object to Award object
     * @param awardTemplateObject
     * @param awardObject
     * @param field
     * @throws Exception
     */
    private void copyField(Object awardTemplateObject, Object awardObject, Field field) throws Exception {
        if (field.isAnnotationPresent(AwardSyncable.class)){
            Object value = ObjectUtils.getPropertyValue(awardTemplateObject, field.getName());
            ObjectUtils.setObjectProperty(awardObject, field.getName(), value);
        }
    }
    /**
     * This is the default method use to sync objects. This method is implemented to work on row Object.
     * If we need to change the implementation of sync functionality for a specific list, 
     * we have to overload this method with specific type.
     * @param awardObject
     * @param listObject
     * @param objectInList
     * @param field
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void syncListObjects(Object awardObject, List<Object> listObject, Field field) 
                                                                                                throws Exception{
        AwardSyncableList awardSyncableList = field.getAnnotation(AwardSyncableList.class);
        String parentPropertyName = awardSyncableList.parentPropertyName();
        Class syncClass = awardSyncableList.syncClass();
        List<Object> newObjectList = new ArrayList<Object>(listObject.size());
        for (Object awardTemplateObject : listObject) {
            Object newObjectToSync = ObjectUtils.createNewObjectFromClass(syncClass);
            sync(awardTemplateObject, newObjectToSync);
            ObjectUtils.setObjectProperty(newObjectToSync, parentPropertyName, awardObject);
            newObjectList.add(newObjectToSync);
        }
        ObjectUtils.setObjectProperty(awardObject, field.getName(), newObjectList);
    }


    /**
     * This method is to fetch the award template by using template code from Award object.
     * 
     * @param Award object
     * @return AwardTemplate object
     */
    private AwardTemplate fetchAwardTemplate(Award award) {
        award.refreshReferenceObject("awardTemplate");
        AwardTemplate awardTemplate = award.getAwardTemplate();
        if (awardTemplate == null && award.getTemplateCode() != null) {
            Map<String, Integer> primaryKeys = new HashMap<String, Integer>();
            primaryKeys.put("templateCode", award.getTemplateCode());
            awardTemplate = (AwardTemplate) businessObjectService.findByPrimaryKey(AwardTemplate.class, primaryKeys);
        }
        return awardTemplate;
    }

    /**
     * Gets the businessObjectService attribute.
     * 
     * @return Returns the businessObjectService.
     */
    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }

    /**
     * Sets the businessObjectService attribute value.
     * 
     * @param businessObjectService The businessObjectService to set.
     */
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    /**
     * @see org.kuali.kra.award.AwardTemplateSyncService#syncToAward(java.lang.Object, java.lang.Object)
     */
    public boolean syncToAward(AwardDocument awardDocument) {
        boolean success;
        Award award = awardDocument.getAward();
        AwardTemplateSyncEvent awardTemplateSyncEvent = 
            new AwardTemplateSyncEvent("Award Sync","document.award.awardTemplate",awardDocument);
        if(!getKualiRuleService().applyRules(awardTemplateSyncEvent)){
            return false;
        }
        try {
            AwardTemplate awardTemplate = fetchAwardTemplate(award);
            if (isSyncAll(award)){
                sync(awardTemplate, award);
            }else{
                syncOnlyFields(awardTemplate, award);
            }
            success=true;
        }catch (Exception e) {
            success=false;
            LOG.error(e.getCause(),e);
        }
        return success;
    }

    /**
     * @see org.kuali.kra.award.AwardTemplateSyncService#syncToAward(org.kuali.kra.award.home.Award, java.lang.String)
     */
    public boolean syncToAward(AwardDocument awardDocument, String syncPropertyName) {
        boolean success;
        Award award = awardDocument.getAward();
        AwardTemplateSyncEvent awardTemplateSyncEvent = 
            new AwardTemplateSyncEvent("Award Sync","document.award.awardTemplate",awardDocument);
        
        try {
            awardDocument.validateBusinessRules(awardTemplateSyncEvent);
            sync(fetchAwardTemplate(award), award, syncPropertyName);
            success=true;
        }catch (Exception e) {
            success=false;
            LOG.error(e.getCause(),e);
        }
        return success;
    }
    /**
     * 
     * This is an overloaded method for syncing only AwardComments.
     * @param awardObject
     * @param templateComments
     * @param objectInList
     * @param propertyName
     */
    public void syncAwardComments(Award awardObject, List<AwardTemplateComment> awardTemplateComments){
        awardObject.addTemplateComments(awardTemplateComments);
    }
    
    /**
     * 
     * This is an overloaded method for syncing only AwardSponsorTerms.
     * @param awardObject
     * @param templateTerms
     * @param objectInList
     * @param propertyName
     */
    public void syncAwardSponsorTerms(Award awardObject, List<AwardTemplateTerm> awardTemplateTerms){
        awardObject.addTemplateTerms(awardTemplateTerms);
    }
    /**
     * 
     * This method checks whether any of the list properties in Award object has already data in it. 
     * If yes, it should not sync any of the details from the list but sync only declared properties.
     * @param Award object
     * @return true, if sync needs to be applied to all list properties, else false.
     */
    private boolean isSyncAll(Award awardObject) {
        boolean syncAll = awardObject.getAwardComments().isEmpty() 
                            && awardObject.getAwardReportTermItems().isEmpty()
                            && awardObject.getSponsorContacts().isEmpty() 
                            && awardObject.getAwardSponsorTerms().isEmpty();

        return syncAll;
    }
    /**
     * Gets the kualiRuleService attribute. 
     * @return Returns the kualiRuleService.
     */
    public KualiRuleService getKualiRuleService() {
        return kualiRuleService;
    }
    /**
     * Sets the kualiRuleService attribute value.
     * @param kualiRuleService The kualiRuleService to set.
     */
    public void setKualiRuleService(KualiRuleService kualiRuleService) {
        this.kualiRuleService = kualiRuleService;
    }
}
