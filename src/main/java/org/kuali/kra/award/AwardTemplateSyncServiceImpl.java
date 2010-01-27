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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.plexus.util.StringUtils;
import org.kuali.kra.award.document.AwardDocument;
import org.kuali.kra.award.home.Award;
import org.kuali.kra.award.home.AwardComment;
import org.kuali.kra.award.home.AwardSponsorTerm;
import org.kuali.kra.award.home.AwardSyncable;
import org.kuali.kra.award.home.AwardSyncableList;
import org.kuali.kra.award.home.AwardTemplate;
import org.kuali.kra.award.home.AwardTemplateComment;
import org.kuali.kra.award.home.AwardTemplateReportTerm;
import org.kuali.kra.award.home.AwardTemplateReportTermRecipient;
import org.kuali.kra.award.home.AwardTemplateTerm;
import org.kuali.kra.award.paymentreports.awardreports.AwardReportTerm;
import org.kuali.kra.award.paymentreports.awardreports.AwardReportTermRecipient;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.service.KualiRuleService;
import org.kuali.rice.kns.util.ObjectUtils;
import java.util.Stack;

/**
 * This class is the implementation of AwardTemplateSyncService.
 */
public class AwardTemplateSyncServiceImpl implements AwardTemplateSyncService {

    private BusinessObjectService businessObjectService;
    private KualiRuleService kualiRuleService;
    
    private static final Log LOG = LogFactory.getLog(AwardTemplateSyncService.class);


    
    

    /**
     * @see org.kuali.kra.award.AwardTemplateSyncService#syncToAward(org.kuali.kra.award.document.AwardDocument)
     */
    public boolean syncToAward(AwardDocument awardDocument ) {
        if( LOG.isDebugEnabled() ) LOG.debug( "Starting syncToAward for award doc:"+awardDocument.getAward().getAwardNumber() );
        AwardTemplateSyncScope[] scopes = {  };
        return syncToAward( awardDocument, scopes );
    }
    

    
    /**
     * @see org.kuali.kra.award.AwardTemplateSyncService#syncToAward(org.kuali.kra.award.document.AwardDocument, org.kuali.kra.award.AwardTemplateSyncScope[])
     */
    public boolean syncToAward(AwardDocument awardDocument, AwardTemplateSyncScope[] scopes ) {
        boolean success;
        if( LOG.isDebugEnabled() )
            LOG.debug( "Starting syncToAward for award doc:"+awardDocument.getAward().getAwardNumber()+" with scope(s):"+ArrayUtils.toString(scopes,"@@NULL@@"));
        
        //init the scope stack.
        java.util.Stack<AwardTemplateSyncScope[]> scopeStack = new java.util.Stack<AwardTemplateSyncScope[]>();
        
        Award award = awardDocument.getAward();
        AwardTemplateSyncEvent awardTemplateSyncEvent = 
            new AwardTemplateSyncEvent("Award Sync","document.award.awardTemplate",awardDocument);
        if(!getKualiRuleService().applyRules(awardTemplateSyncEvent)){
            return false;
        }
        try {
            AwardTemplate awardTemplate = fetchAwardTemplate(award);
            sync(awardTemplate, award, scopes, scopeStack,award,awardTemplate);
            success=true;
        }catch (Exception e) {
            success=false;
            LOG.error(e.getCause(),e);
        }
        return success;
    }


    /**
     * @see org.kuali.kra.award.AwardTemplateSyncService#syncToAward(org.kuali.kra.award.document.AwardDocument, java.lang.String, org.kuali.kra.award.AwardTemplateSyncScope[])
     */
    public boolean syncToAward(AwardDocument awardDocument, String syncPropertyName, AwardTemplateSyncScope[] scopes ) {
        boolean success;
        if(LOG.isDebugEnabled())
            LOG.debug( "Starting syncToAward for property:"+syncPropertyName+" for award doc:"+awardDocument.getAward().getAwardNumber()+" with scope(s):"+ArrayUtils.toString(scopes,"@@NULL@@"));
        //init the scope stack
        java.util.Stack<AwardTemplateSyncScope[]> scopeStack = new java.util.Stack<AwardTemplateSyncScope[]>();
        Award award = awardDocument.getAward();
        AwardTemplateSyncEvent awardTemplateSyncEvent = 
            new AwardTemplateSyncEvent("Award Sync","document.award.awardTemplate",awardDocument);
        
        try {
            awardDocument.validateBusinessRules(awardTemplateSyncEvent);
            if( !StringUtils.isEmpty(syncPropertyName) )
                sync(fetchAwardTemplate(award), award, syncPropertyName, scopes, scopeStack, award, fetchAwardTemplate(award));
            else 
                sync( fetchAwardTemplate(award), award, scopes, scopeStack, award, fetchAwardTemplate(award) );
            success=true;
        }catch (Exception e) {
            success=false;
            LOG.error(e.getCause(),e);
        }
        return success;
    }
        
    

    public boolean syncWillClobberData(AwardDocument awardDocument, AwardTemplateSyncScope scope) {
        if( LOG.isDebugEnabled() ) 
            LOG.debug(String.format( "syncWillClobberData called on award number %s with scope %s", awardDocument.getAward().getAwardNumber(), scope ));
        
        java.util.Stack<AwardTemplateSyncScope[]> scopeStack = new java.util.Stack<AwardTemplateSyncScope[]>();
        Award award = awardDocument.getAward();
        AwardTemplateSyncScope[] scopes = {scope};
        try {
            return syncCheck( fetchAwardTemplate(award), award, scopes, scopeStack, award, fetchAwardTemplate(award) );
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            LOG.error(e);
            throw new RuntimeException( e );
        }
    }
    
    
    
    /**
     * This method is used to sync member properties of an award template object to an award object
     * 
     * @param awardTemplateObject
     * @param awardObject
     */
    private boolean syncCheck(Object awardTemplateObject, Object awardObject,AwardTemplateSyncScope[] scopes, java.util.Stack<AwardTemplateSyncScope[]> scopeStack, Award award, AwardTemplate awardTemplate) throws Exception{
        List<Field> allFields = new ArrayList<Field>();
        findAllFields(awardObject.getClass(), allFields);
        
        AwardTemplateSyncScope[] effectiveScopes;
        for (Field field: allFields) {
            if (field.isAnnotationPresent(AwardSyncable.class)){
                effectiveScopes = field.getAnnotation(AwardSyncable.class).scopes();
                if( AwardTemplateSyncScope.CONTAINING_CLASS_INHERIT.isInScope(field))
                    effectiveScopes = scopeStack.peek();
                
                if (AwardTemplateSyncScope.isInScope(effectiveScopes, scopes)) {
                    if( LOG.isDebugEnabled() )
                        LOG.debug(String.format( "Copying field:%s.%s", awardObject.getClass().toString(),field.getName() ));
                    if( checkField(awardTemplateObject, awardObject, field, scopeStack,award,awardTemplate) ) return true;
                } else {
                    if( LOG.isDebugEnabled() )
                        LOG.debug(String.format( "Skiped (not in scope(s) %s):%s.%s", ArrayUtils.toString(scopes), awardObject.getClass().toString(),field.getName() ));
                }
            }
            else if(field.isAnnotationPresent(AwardSyncableList.class)){
                effectiveScopes = scopes;
                if( AwardTemplateSyncScope.CONTAINING_CLASS_INHERIT.isInScope(field))
                    effectiveScopes = scopeStack.peek();
                if( AwardTemplateSyncScope.isInScope(effectiveScopes, scopes)) {
                    if( LOG.isDebugEnabled() )
                        LOG.debug(String.format( "Sync list:%s.%s", awardObject.getClass().toString(),field.getName() ));
                    if( checkList(awardTemplateObject, awardObject, field, effectiveScopes, scopeStack, award, awardTemplate) ) return true;
                } else {
                    if( LOG.isDebugEnabled() )
                        LOG.debug(String.format( "Skipped (not in scope(s) %s) list:%s.%s", ArrayUtils.toString(scopes),awardObject.getClass().toString(),field.getName() ));
                }
                
            } else {
                if ( LOG.isTraceEnabled() ) {
                        LOG.trace( String.format( "Skipped (No Annotation):%s.%s", awardObject.getClass().toString(),field.getName() )  );
                }
            }
        }
        
        return false;
    }
    
    
    
    /**
     * 
     * This method is to sync only fields; but not lists
     * @param awardTemplateObject
     * @param awardObject
     * @throws Exception
     */
    private void syncOnlyFields(Object awardTemplateObject, Object awardObject,AwardTemplateSyncScope[] scopes, java.util.Stack<AwardTemplateSyncScope[]> scopeStack, Award award, AwardTemplate awardTemplate) throws Exception{
        Field[] fields = awardObject.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.isAnnotationPresent(AwardSyncable.class) &&
                    !field.getType().isAssignableFrom(List.class)) {
                if( AwardTemplateSyncScope.isInScope(field.getAnnotation(AwardSyncable.class), scopes)) {
                    if(LOG.isDebugEnabled())
                        LOG.debug( "syncing field:"+awardObject.getClass().toString()+"."+field.getName() );
                    copyField(awardTemplateObject, awardObject, field, scopeStack, award, awardTemplate);
                } else {
                    if(LOG.isDebugEnabled())
                        LOG.debug( "skipping field:"+awardObject.getClass().toString()+"."+field.getName() );
                }
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
    private void extractListFromParentAndSync(Object awardTemplateObject, Object awardObject, Field field, AwardTemplateSyncScope[] scopes, java.util.Stack<AwardTemplateSyncScope[]> scopeStack, Award award, AwardTemplate awardTemplate ) throws Exception {
        field.setAccessible(true);
        List<Object> awardTemplateObjectList = (List)ObjectUtils.getPropertyValue(awardTemplateObject, field.getName());
        AwardSyncableList awardSyncableList = field.getAnnotation(AwardSyncableList.class);
        LOG.debug("");
        scopeStack.push(awardSyncableList.scopes());
        AwardTemplateSyncScope[] effectiveScopes = getEffectiveScope( scopeStack );
        LOG.debug("");
        if(awardTemplateObjectList!=null && !awardTemplateObjectList.isEmpty() && AwardTemplateSyncScope.isInScope( scopes, effectiveScopes )){
            if(awardSyncableList.syncMethodName().equalsIgnoreCase(AwardSyncableList.DEFAULT_METHOD)){
                syncListObjects(awardObject,awardTemplateObjectList,field, scopes, scopeStack, award, awardTemplate);
            }else{
                invokeMethodToSync((Award)awardObject,awardTemplateObjectList,awardSyncableList.syncMethodName(), scopes, scopeStack, award, awardTemplate);
            }
        }
        scopeStack.pop();
    }

    
    

    
    private AwardTemplateSyncScope[] getEffectiveScope( Stack<AwardTemplateSyncScope[]> scopeStack ) {
        AwardTemplateSyncScope[] effectiveScope = null;
        
        for( int i = scopeStack.size()-1;i>=0;i--) {
            if( ArrayUtils.contains( scopeStack.get(i), AwardTemplateSyncScope.CONTAINING_CLASS_INHERIT ) ) continue;
            effectiveScope = scopeStack.get(i);
        }
        return effectiveScope;
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
    private void invokeMethodToSync(Award awardObject, List awardTemplateObjectList,String syncMethodName,AwardTemplateSyncScope[] scopes, Stack<AwardTemplateSyncScope[]> scopeStack, Award award, AwardTemplate awardTemplate) throws Exception{
        Method syncMethod = getClass().getMethod(syncMethodName, new Class[]{Award.class,List.class,Stack.class, Award.class, AwardTemplate.class});
        syncMethod.invoke(this, new Object[]{awardObject,awardTemplateObjectList,scopeStack, award, awardTemplate});
    }

    /**
     * 
     * This is an overloaded method to sync a particular list defined in Award object
     * @param awardTemplateObject
     * @param awardObject
     * @param propertyName
     * @throws Exception
     */
    private void sync(Object awardTemplateObject, Object awardObject,String propertyName, AwardTemplateSyncScope[] scopes,java.util.Stack<AwardTemplateSyncScope[]> scopeStack, Award award, AwardTemplate awardTemplate ) throws Exception{
        Field field = awardObject.getClass().getDeclaredField(propertyName);
        extractListFromParentAndSync(awardTemplateObject,awardObject,field,scopes,scopeStack, award, awardTemplate);
    }
    
    /**
     * This method is used to sync member properties of an award template object to an award object
     * 
     * @param awardTemplateObject
     * @param awardObject
     */
    private void sync(Object awardTemplateObject, Object awardObject,AwardTemplateSyncScope[] scopes, java.util.Stack<AwardTemplateSyncScope[]> scopeStack, Award award, AwardTemplate awardTemplate) throws Exception{
        List<Field> allFields = new ArrayList<Field>();
        findAllFields(awardObject.getClass(), allFields);
        
        AwardTemplateSyncScope[] effectiveScopes;
        for (Field field: allFields) {
            if (field.isAnnotationPresent(AwardSyncable.class)){
                effectiveScopes = field.getAnnotation(AwardSyncable.class).scopes();
                
                
                if( AwardTemplateSyncScope.CONTAINING_CLASS_INHERIT.isInScope(field))
                    effectiveScopes = getEffectiveScope( scopeStack );
                
                if (AwardTemplateSyncScope.isInScope(effectiveScopes, scopes)) {
                    if( LOG.isDebugEnabled() )
                        LOG.debug(String.format( "Copying field:%s.%s", awardObject.getClass().toString(),field.getName() ));
                    copyField(awardTemplateObject, awardObject, field, scopeStack,award,awardTemplate);
                } else {
                    if( LOG.isDebugEnabled() )
                        LOG.debug(String.format( "Skiped (not in scope(s) %s):%s.%s", ArrayUtils.toString(scopes), awardObject.getClass().toString(),field.getName() ));
                }
            }
            else if(field.isAnnotationPresent(AwardSyncableList.class)){
                effectiveScopes = scopes;
                if( AwardTemplateSyncScope.CONTAINING_CLASS_INHERIT.isInScope(field))
                    effectiveScopes = getEffectiveScope(scopeStack);
                if( AwardTemplateSyncScope.isInScope(effectiveScopes, scopes)) {
                    if( LOG.isDebugEnabled() )
                        LOG.debug(String.format( "Sync list:%s.%s", awardObject.getClass().toString(),field.getName() ));
                    extractListFromParentAndSync(awardTemplateObject, awardObject, field, effectiveScopes, scopeStack, award, awardTemplate);
                } else {
                    if( LOG.isDebugEnabled() )
                        LOG.debug(String.format( "Skipped (not in scope(s) %s) list:%s.%s", ArrayUtils.toString(scopes),awardObject.getClass().toString(),field.getName() ));
                }
                
            } else {
                if ( LOG.isTraceEnabled() ) {
                        LOG.trace( String.format( "Skipped (No Annotation):%s.%s", awardObject.getClass().toString(),field.getName() )  );
                }
            }
        }
    }
    
    /**
     * This method uses recursion to find all declared fields for a class hierierachy
     * @param klass
     * @param allFields
     */
    @SuppressWarnings("unchecked")
    private void findAllFields(Class klass, List<Field> allFields) {
        Field[] fields = klass.getDeclaredFields();
        allFields.addAll(Arrays.asList(fields));
        klass = klass.getSuperclass();
        if(klass != null) {
            findAllFields(klass, allFields);
        }
    }

    
    /**
     * This copies value from Award Template object to Award object
     * @param awardTemplateObject
     * @param awardObject
     * @param field
     * @throws Exception
     */
    private void copyField(Object awardTemplateObject, Object awardObject, Field field, java.util.Stack<AwardTemplateSyncScope[]> scopeStack, Award award, AwardTemplate awardTemplate) throws Exception {
        if (field.isAnnotationPresent(AwardSyncable.class)){
            Object value = ObjectUtils.getPropertyValue(awardTemplateObject, field.getName());
            ObjectUtils.setObjectProperty(awardObject, field.getName(), value);
        }
    }
    
    
    /**
     * This checks to see if a field is null.  It is used to check the sync
     * scopes to see if a sync will clobber any data.
     * @param awardTemplateObject
     * @param awardObject
     * @param field
     * @throws Exception
     */
    private boolean checkField(Object awardTemplateObject, Object awardObject, Field field, java.util.Stack<AwardTemplateSyncScope[]> scopeStack, Award award, AwardTemplate awardTemplate) throws Exception {
        if (field.isAnnotationPresent(AwardSyncable.class)){
            Object value = ObjectUtils.getPropertyValue(awardObject, field.getName());
            if( value == null ) return false;
            return true;
        }
        return false;
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
    private boolean checkList(Object awardTemplateObject, Object awardObject, Field field, AwardTemplateSyncScope[] scopes, java.util.Stack<AwardTemplateSyncScope[]> scopeStack, Award award, AwardTemplate awardTemplate ) throws Exception {
        field.setAccessible(true);
        List<Object> awardObjectList = (List)ObjectUtils.getPropertyValue(awardObject, field.getName());
        if( awardObjectList == null || awardObjectList.size() > 0 ) return false;
        return true;
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
    private void syncListObjects(Object awardObject, List<Object> listObject, Field field, AwardTemplateSyncScope[] scopes, java.util.Stack<AwardTemplateSyncScope[]> scopeStack, Award award, AwardTemplate awardTemplate  ) 
                                                                                          throws Exception{
        AwardSyncableList awardSyncableList = field.getAnnotation(AwardSyncableList.class);
        String parentPropertyName = awardSyncableList.parentPropertyName();
        Class syncClass = awardSyncableList.syncClass();
        
        Method templateIsInScopeMethod = findIsInScopeMethodForClass( awardSyncableList.syncSourceClass() );
        Method awardIsInScopeMethod = findIsInScopeMethodForClass( awardSyncableList.syncClass() );
        
        
        List<Object> newObjectList = new ArrayList<Object>(listObject.size());
        Method createNewListElementMethod = getCreateNewListElementMethod( awardSyncableList.syncSourceClass() );
        
        for (Object awardTemplateObject : listObject) {
            if( (Boolean) templateIsInScopeMethod.invoke(null, awardTemplateObject, getEffectiveScope(scopeStack) )) {
                Object newObjectToSync = createNewListElementMethod.invoke(this, awardTemplateObject, awardSyncableList.syncClass(), award, awardTemplate,true );
                sync(awardTemplateObject, newObjectToSync, scopes, scopeStack, award, awardTemplate);
                ObjectUtils.setObjectProperty(newObjectToSync, parentPropertyName, awardObject);
                newObjectList.add(newObjectToSync);
            } else {
               //nothing to do here.
            }
        }
        
        //now we need to loop through the list in the award and save the object that are not in scope, otherwise we will lose them...
        
        List<Object> keepers = new ArrayList<Object>();
        List<Object> awardList = (List<Object>) ObjectUtils.getPropertyValue(awardObject, field.getName());
        for( Object aObject : awardList ) {
            if( !(Boolean)awardIsInScopeMethod.invoke(null, aObject, getEffectiveScope(scopeStack) )) {
                keepers.add(aObject);
            }
        }
        newObjectList.addAll(keepers);
        ObjectUtils.setObjectProperty(awardObject, field.getName(), newObjectList);
    }

    
    
    /**
     * This method finds the appropriate getOrCreateNewListElementObject for a class.  It does this by first trying to return 
     * the method within this implementation with the signature getOrCreateNewListElementObject( syncSourceClass, Class.class, Award.class, AwardTemplate.class ).
     * If it does not find one, it returns the generic method getOrCreateNewListElementObject( Object.class, Class.class, Award.class, AwardTemplate.class ).
     * @param syncSourceClass - The class you wish to find the method for.  If a specific method for this class does not exist, the generic method is returned.
     * @return The best match for the method.
     */
    private Method getCreateNewListElementMethod( Class syncSourceClass ) {
        
        //Try to get the method that is specific for the source class.
        try {
            Method m = AwardTemplateSyncServiceImpl.class.getDeclaredMethod("getOrCreateNewListElementObject", syncSourceClass, Class.class, Award.class, AwardTemplate.class, boolean.class  );
            m.setAccessible(true);
            return m;
        }
        catch (Exception e) {
            if( LOG.isDebugEnabled() )
                LOG.debug( String.format( "Could not find specific getOrCreateNewListElementObject for class %s, using generic method.", syncSourceClass ) );
        }
        //well we failed to get the specific method, so we are going to return the generic method.
        try {
            Method m = AwardTemplateSyncServiceImpl.class.getDeclaredMethod( "getOrCreateNewListElementObject", Object.class, Class.class, Award.class, AwardTemplate.class, boolean.class  );
            m.setAccessible(true);
            return m;
        }
        catch (Exception e) {
            throw new IllegalStateException( "Could not find generic getOrCreateNewListElementObject, this should never happen." );
        }
    }
    
    @SuppressWarnings({ "unused", "unchecked" })
    private Object getOrCreateNewListElementObject( Object sourceObject, java.lang.Class syncClass, Award award, AwardTemplate awardTemplate, boolean createNew ) {
        return createNew?ObjectUtils.createNewObjectFromClass(syncClass):null; 
    }
    
    @SuppressWarnings({ "unused", "unchecked" })
    private AwardComment getOrCreateNewListElementObject( AwardTemplateComment sourceComment, java.lang.Class syncClass, Award award, AwardTemplate awardTemplate, boolean createNew ) {
        AwardComment comment = award.getAwardCommentByType(sourceComment.getCommentTypeCode(), sourceComment.getChecklistPrintFlag(), createNew );
        return comment;
    }
    
    @SuppressWarnings({ "unused", "unchecked" })
    private AwardSponsorTerm getOrCreateNewListElementObject( AwardTemplateTerm sponsorTerm, java.lang.Class syncClass, Award award, AwardTemplate awardTemplate, boolean createNew ) {
        AwardSponsorTerm term = award.getAwardSponsorTermByTemplateTerm( sponsorTerm, createNew );
        return term;
    }
    
    @SuppressWarnings({ "unused", "unchecked" })
    private AwardReportTermRecipient getOrCreateNewListElementObject( AwardTemplateReportTermRecipient recipient, java.lang.Class syncClass, Award award, AwardTemplate awardTemplate, boolean createNew ) {
        AwardReportTermRecipient newRecipient = new AwardReportTermRecipient();
        recipient.refreshReferenceObject("rolodex");
        recipient.refreshReferenceObject("contactType");
        newRecipient.setContactType(recipient.getContactType());
        newRecipient.setNumberOfCopies(recipient.getNumberOfCopies());
        newRecipient.setRolodex(recipient.getRolodex());
        newRecipient.setRolodexId(recipient.getRolodexId());
        newRecipient.setContactTypeCode(recipient.getContactTypeCode());
        return newRecipient;
    }
    
//    @SuppressWarnings({ "unused", "unchecked" })
//    private AwardReportTerm getOrCreateNewListElementObject( AwardTemplateReportTerm reportTerm, java.lang.Class syncClass, Award award, AwardTemplate awardTemplate, boolean createNew ) {
//        AwardReportTerm term = null;
//        
//        term.setReportClass(reportTerm.getReportClass() );
//        term.setReport(reportTerm.getReport());
//        term.setFrequency(reportTerm.getFrequency());
//        term.setFrequencyBase(reportTerm.getFrequencyBase());
//        return term;
//    }
    
    private Method findIsInScopeMethodForClass( Class clazz ) {
        Class klass = AwardTemplateSyncScope.class;
        Method result = null;
        try {
            result = klass.getMethod("isInScope", Object.class,AwardTemplateSyncScope[].class );
        } catch (Exception e1) {
            throw new IllegalStateException( "FATAL: Cannot find method isInScope( Object,AwardTemplateSyncScope[]) on the AwardTemplateSyncScope class.  This should never happen." );
        }
        try {
            result = klass.getMethod("isInScope", clazz, AwardTemplateSyncScope[].class );
        } catch (Exception e) {
            
        }
        
        return result;
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

       
    
//    /**
//     * This method...
//     * @param awardDocument
//     * @param methodUiIdentifier
//     * @param scopes
//     * @return
//     */
//    private boolean syncToAwardByMethod( AwardDocument awardDocument, String methodUiIdentifier, AwardTemplateSyncScope[] scopes ) {
//        boolean success = false;
//        Award award = awardDocument.getAward();
//        AwardTemplateSyncEvent awardTemplateSyncEvent = 
//            new AwardTemplateSyncEvent("Award Sync","document.award.awardTemplate",awardDocument);
//        try {
//            //locate the method to call
//            
//            Method[] methods = awardDocument.getAward().getClass().getDeclaredMethods();
//            List<Method> methodsToCall = new ArrayList<Method>();
//            for( Method method : methods ) {
//                AwardSyncableMethod mt = method.getAnnotation(AwardSyncableMethod.class);
//                if( mt != null && mt.uiIdentifier().equals(methodUiIdentifier)  ) {
//                    for( AwardTemplateSyncScope scope: scopes ) {
//                        if( scope.isInScope(method) ) { 
//                            methodsToCall.add(method);
//                            break;
//                        }
//                    }
//                }
//            }
//            
//            for( Method method : methodsToCall ) {
//                method.invoke(award,fetchAwardTemplate(award));
//            }
//            success = true;
//        } catch ( Exception e ) {
//            LOG.error( "Exception running method", e );
//        }
//            
//        return success;
//    }

    
    /**
     * 
     * This is an overloaded method for syncing only AwardComments.
     * @param awardObject
     * @param templateComments
     * @param objectInList
     * @param propertyName
     */
    public void syncAwardComments(Award awardObject, List<AwardTemplateComment> awardTemplateComments,Stack<AwardTemplateSyncScope[]> scopeStack){
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
    public void syncAwardSponsorTerms(Award awardObject, List<AwardTemplateTerm> awardTemplateTerms, Stack<AwardTemplateSyncScope[]> scopeStack ){
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
