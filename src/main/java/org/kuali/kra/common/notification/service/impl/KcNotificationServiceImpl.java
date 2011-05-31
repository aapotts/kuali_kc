/*
 * Copyright 2005-2010 The Kuali Foundation
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
package org.kuali.kra.common.notification.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.kra.common.notification.NotificationContext;
import org.kuali.kra.common.notification.bo.KcNotification;
import org.kuali.kra.common.notification.bo.NotificationType;
import org.kuali.kra.common.notification.bo.NotificationTypeRecipient;
import org.kuali.kra.common.notification.exception.UnknownRoleException;
import org.kuali.kra.common.notification.service.KcNotificationService;
import org.kuali.rice.ken.bo.Notification;
import org.kuali.rice.ken.bo.NotificationChannel;
import org.kuali.rice.ken.bo.NotificationContentType;
import org.kuali.rice.ken.bo.NotificationPriority;
import org.kuali.rice.ken.bo.NotificationProducer;
import org.kuali.rice.ken.bo.NotificationRecipient;
import org.kuali.rice.ken.service.NotificationService;
import org.kuali.rice.ken.util.NotificationConstants;
import org.kuali.rice.kim.bo.role.dto.KimRoleInfo;
import org.kuali.rice.kim.bo.types.dto.AttributeSet;
import org.kuali.rice.kim.service.RoleManagementService;
import org.kuali.rice.kim.util.KimConstants;
import org.kuali.rice.kns.service.BusinessObjectService;

public class KcNotificationServiceImpl implements KcNotificationService {
    
    private static final String ACTION_CODE = "actionCode";
    private static final String NOTIFICATION_TYPE_ID = "notificationTypeId";
    private static final String DOCUMENT_NUMBER = "documentNumber";
    
    private static final Log LOG = LogFactory.getLog(KcNotificationServiceImpl.class);
    
    protected NotificationChannel kcNotificationChannel;
    protected NotificationProducer systemNotificationProducer;
    
    private BusinessObjectService businessObjectService;
    private NotificationService notificationService;
    private RoleManagementService roleManagementService;
    
    /**
     * {@inheritDoc}
     * @see org.kuali.kra.common.notification.service.KcNotificationService#createNotifications(java.lang.String, java.lang.String, 
     *      org.kuali.kra.common.notification.NotificationContext)
     */
    @SuppressWarnings("unchecked")
    public List<KcNotification> createNotifications(String documentNumber, String actionCode, NotificationContext context) {
        List<KcNotification> notifications = new ArrayList<KcNotification>();
        
        Map<String, String> fieldValues = new HashMap<String, String>();
        fieldValues.put(ACTION_CODE, actionCode);
        Collection<NotificationType> notificationTypes = getBusinessObjectService().findMatching(NotificationType.class, fieldValues);
        
        for (NotificationType notificationType : notificationTypes) {
            KcNotification notification = new KcNotification();
            notification.setNotificationTypeId(notificationType.getNotificationTypeId());
            notification.setDocumentNumber(documentNumber);
            String instanceSubject = context.replaceContextVariables(notificationType.getSubject());
            notification.setSubject(instanceSubject);
            String instanceMessage = context.replaceContextVariables(notificationType.getMessage());
            notification.setMessage(instanceMessage);
            notification.setNotificationType(notificationType);
            notifications.add(notification);
        }
        
        return notifications;
    }
    
    /**
     * {@inheritDoc}
     * @see org.kuali.kra.common.notification.service.KcNotificationService#saveNotifications(java.util.List)
     */
    public void saveNotifications(List<KcNotification> notifications) {
        getBusinessObjectService().save(notifications);
    }

    /**
     * {@inheritDoc}
     * @see org.kuali.kra.common.notification.service.KcNotificationService#getNotifications(java.lang.String, java.util.Set)
     */
    @SuppressWarnings("unchecked")
    public List<KcNotification> getNotifications(String documentNumber, Set<String> actionCodes) {
        List<KcNotification> notifications = new ArrayList<KcNotification>();
        
        for (String actionCode : actionCodes) {
            Map<String, String> notificationTypeFieldValues = new HashMap<String, String>();
            notificationTypeFieldValues.put(ACTION_CODE, actionCode);
            Collection<NotificationType> notificationTypes = getBusinessObjectService().findMatching(NotificationType.class, notificationTypeFieldValues);
            
            for (NotificationType notificationType : notificationTypes) {
                Map<String, String> notificationFieldValues = new HashMap<String, String>();
                notificationFieldValues.put(NOTIFICATION_TYPE_ID, notificationType.getNotificationTypeId().toString());
                notificationFieldValues.put(DOCUMENT_NUMBER, documentNumber);
                notifications.addAll(getBusinessObjectService().findMatching(KcNotification.class, notificationFieldValues));
            }
        }
        
        return notifications;
    }
    
    public void sendNotifications(List<KcNotification> kcNotifications, NotificationContext context) {
        
        List<Notification> notifications = new ArrayList<Notification>();
        for (KcNotification kcNotification : kcNotifications) {
            Notification notification = new Notification();
            
            NotificationPriority priority = new NotificationPriority();
            priority.setId(new Long(1)); // TODO System param (normal priority)
            notification.setPriority(priority);
            
            NotificationContentType contentType = new NotificationContentType();
            contentType.setId(new Long(1)); // TODO System param
            notification.setContentType(contentType);
            
            notification.setProducer(getSystemNotificationProducer());
            notification.setChannel(getKcNotificationChannel());
            
            notification.setTitle(kcNotification.getSubject());
            notification.setContent("<content>" + kcNotification.getMessage() + "</content>");
            notification.setDeliveryType(NotificationConstants.DELIVERY_TYPES.FYI);
            
            for (NotificationTypeRecipient roleRecipient : kcNotification.getNotificationType().getNotificationTypeRecipients()) {
                try {
                    context.populateRoleQualifiers(roleRecipient);
                    notification.getRecipients().addAll(resolveRoleRecipients(roleRecipient, context));
                } catch (UnknownRoleException e) {
                    LOG.error("Role id " + e.getRoleId() + " not recognized for context " + e.getContext() + ". " +
                    		"Notification will not be sent for notificationTypeRecipient" + roleRecipient.toString());
                    e.printStackTrace();
                }
            }
            
            notifications.add(notification);
        }
        
        for (Notification notification : notifications) {
            notificationService.sendNotification(notification);
        }
    }
    
    protected NotificationProducer getSystemNotificationProducer() {
        if (this.systemNotificationProducer == null) {
            NotificationProducer np = new NotificationProducer();
            np.setId(new Long(1));
            List<NotificationChannel> notificationChannels = new ArrayList<NotificationChannel>();
            notificationChannels.add(getKcNotificationChannel());
            np.setChannels(notificationChannels);
            this.systemNotificationProducer = np;
        }
        return systemNotificationProducer;
    }
    
    protected NotificationChannel getKcNotificationChannel() {
        if (this.kcNotificationChannel == null) {
            NotificationChannel nc = new NotificationChannel();
            nc.setId(new Long(1000)); // TODO sys parm
            this.kcNotificationChannel = nc;
        }
        return kcNotificationChannel;
    }
    
    protected List<NotificationRecipient> resolveRoleRecipients(NotificationTypeRecipient roleRecipient, NotificationContext context) {
        List<NotificationRecipient> recipients = new ArrayList<NotificationRecipient>();
        KimRoleInfo role = roleManagementService.getRole(roleRecipient.getRoleId());
        
        AttributeSet qualification = new AttributeSet();
        qualification.put(roleRecipient.getRoleQualifier(), roleRecipient.getQualifierValue());
        
        Collection<String> roleMembers = roleManagementService.getRoleMemberPrincipalIds(role.getNamespaceCode(), role.getRoleName(), qualification);
        for (String roleMember : roleMembers) {
            NotificationRecipient recipient = new NotificationRecipient();
            recipient.setRecipientId(roleMember);
            recipient.setRecipientType(KimConstants.KimGroupMemberTypes.PRINCIPAL_MEMBER_TYPE);
            recipients.add(recipient);
        }
        
        return recipients;
    }

    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public RoleManagementService getRoleManagementService() {
        return roleManagementService;
    }

    public void setRoleManagementService(RoleManagementService roleManagementService) {
        this.roleManagementService = roleManagementService;
    }

}