ALTER TABLE "EN_RTE_NODE_CFG_PARM_T" ADD CONSTRAINT "EN_RTE_NODE_CFG_PARM_TR1"  
FOREIGN KEY ("RTE_NODE_CFG_PARM_ND")                                            
REFERENCES "EN_RTE_NODE_T" ("RTE_NODE_ID") ENABLE;                              
                                                                                
                                                                                
ALTER TABLE "EN_RULE_BASE_VAL_T" ADD CONSTRAINT "EN_RULE_BASE_VAL_TR1" FOREIGN  
KEY ("RULE_EXPR_ID")                                                            
REFERENCES "EN_RULE_EXPR_T" ("RULE_EXPR_ID") ENABLE;                            
                                                                                
                                                                                
ALTER TABLE "EN_WRKGRP_EXT_DTA_T" ADD CONSTRAINT "EN_WRKGRP_EXT_DTA_TR1" FOREIGN
KEY ("WRKGRP_EXT_ID")                                                           
REFERENCES "EN_WRKGRP_EXT_T" ("WRKGRP_EXT_ID") ENABLE;                          
                                                                                
                                                                                
ALTER TABLE "EN_WRKGRP_EXT_T" ADD CONSTRAINT "EN_WRKGRP_EXT_TR1" FOREIGN KEY    
("WRKGRP_ID", "WRKGRP_VER_NBR")                                                 
REFERENCES "EN_WRKGRP_T" ("WRKGRP_ID", "WRKGRP_VER_NBR") ENABLE;                
ALTER TABLE "EN_WRKGRP_EXT_T" ADD CONSTRAINT "EN_WRKGRP_EXT_TR2" FOREIGN KEY    
("WRKGRP_TYP_ATTRIB_ID")                                                        
REFERENCES "EN_WRKGRP_TYP_ATTRIB_T" ("WRKGRP_TYP_ATTRIB_ID") ENABLE;            
                                                                                
                                                                                
ALTER TABLE "EN_WRKGRP_TYP_ATTRIB_T" ADD CONSTRAINT "EN_WRKGRP_TYP_ATTRIB_TR1"  
FOREIGN KEY ("WRKGRP_TYP_ID")                                                   
REFERENCES "EN_WRKGRP_TYP_T" ("WRKGRP_TYP_ID") ENABLE;                          
ALTER TABLE "EN_WRKGRP_TYP_ATTRIB_T" ADD CONSTRAINT "EN_WRKGRP_TYP_ATTRIB_TR2"  
FOREIGN KEY ("ATTRIB_ID")                                                       
REFERENCES "EN_RULE_ATTRIB_T" ("RULE_ATTRIB_ID") ENABLE;                        
                                                                                
                                                                                
ALTER TABLE "EPS_PROP_ABSTRACT" ADD CONSTRAINT "FK_EPS_PROP_ABSTRACT_KRA"       
FOREIGN KEY ("PROPOSAL_NUMBER")                                                 
REFERENCES "EPS_PROPOSAL" ("PROPOSAL_NUMBER") ENABLE;                           
ALTER TABLE "EPS_PROP_ABSTRACT" ADD CONSTRAINT "FK_EPS_PROP_ABSTRACT_TYPE_KRA"  
FOREIGN KEY ("ABSTRACT_TYPE_CODE")                                              
REFERENCES "ABSTRACT_TYPE" ("ABSTRACT_TYPE_CODE") ENABLE;                       
                                                                                
                                                                                
ALTER TABLE "EPS_PROP_LOCATION" ADD CONSTRAINT "FK_EPS_PROP_LOCATION_KRA"       
FOREIGN KEY ("PROPOSAL_NUMBER")                                                 
REFERENCES "EPS_PROPOSAL" ("PROPOSAL_NUMBER") ENABLE;                           
                                                                                
                                                                                
ALTER TABLE "EPS_PROP_PERSON_BIO" ADD CONSTRAINT "FK1_EPS_PROP_PERSON_BIO_KRA"  
FOREIGN KEY ("DOCUMENT_TYPE_CODE")                                              
REFERENCES "EPS_PROP_PER_DOC_TYPE" ("DOCUMENT_TYPE_CODE") ENABLE;               
                                         
ALTER TABLE EPS_PROP_PERSON_BIO_ATTACHMENT
    ADD ( CONSTRAINT FK1_EPS_PROP_PSN_BIO_ATT_KRA
	FOREIGN KEY(PROPOSAL_NUMBER, PROP_PERSON_NUMBER, BIO_NUMBER)
	REFERENCES EPS_PROP_PERSON_BIO(PROPOSAL_NUMBER, PROP_PERSON_NUMBER, BIO_NUMBER)
	ON DELETE  CASCADE );
                                                                                
ALTER TABLE "EPS_PROP_SCIENCE_KEYWORD" ADD CONSTRAINT                           
"FK_EPS_PROP_SCIE_KEY_KEYW_KRA" FOREIGN KEY ("SCIENCE_KEYWORD_CODE")            
REFERENCES "SCIENCE_KEYWORD" ("SCIENCE_KEYWORD_CODE") ENABLE;                   
ALTER TABLE "EPS_PROP_SCIENCE_KEYWORD" ADD CONSTRAINT                           
"FK_EPS_PROP_SCIE_KEY_PROP_KRA" FOREIGN KEY ("PROPOSAL_NUMBER")                 
REFERENCES "EPS_PROPOSAL" ("PROPOSAL_NUMBER") ENABLE;                           
                                                                                
                                                                                
ALTER TABLE "EPS_PROP_SPECIAL_REVIEW" ADD CONSTRAINT                            
"FK_EPS_PROP_SPECIAL_REVIEW_KRA" FOREIGN KEY ("PROPOSAL_NUMBER")                
REFERENCES "EPS_PROPOSAL" ("PROPOSAL_NUMBER") ENABLE;                           
ALTER TABLE "EPS_PROP_SPECIAL_REVIEW" ADD CONSTRAINT                            
"FK_EPS_PROP_SP_APPROV_TYPE_KRA" FOREIGN KEY ("APPROVAL_TYPE_CODE")             
REFERENCES "SP_REV_APPROVAL_TYPE" ("APPROVAL_TYPE_CODE") ENABLE;                
ALTER TABLE "EPS_PROP_SPECIAL_REVIEW" ADD CONSTRAINT "FK_EPS_PROP_SP_CODE_KRA"  
FOREIGN KEY ("SPECIAL_REVIEW_CODE")                                             
REFERENCES "SPECIAL_REVIEW" ("SPECIAL_REVIEW_CODE") ENABLE;                     
                                                                                
                                                                                
ALTER TABLE "EPS_PROP_USER_ROLES" ADD CONSTRAINT "FK_EPS_PROP_USER_ROLES_KRA"   
FOREIGN KEY ("PROPOSAL_NUMBER")                                                 
REFERENCES "EPS_PROPOSAL" ("PROPOSAL_NUMBER") ENABLE;                           
ALTER TABLE "EPS_PROP_USER_ROLES" ADD CONSTRAINT "FK_EPS_ROLE_ID_KRA" FOREIGN   
KEY ("ROLE_ID")                                                                 
REFERENCES "ROLE" ("ROLE_ID") ENABLE;                                           
ALTER TABLE "EPS_PROP_USER_ROLES" ADD CONSTRAINT "FK_USER_ID_KRA" FOREIGN KEY   
("USER_ID")                                                                     
REFERENCES "PERSON" ("PERSON_ID") ENABLE;                                       
                                                                                
                                                                                
ALTER TABLE "FP_DOC_TYPE_T" ADD CONSTRAINT "FP_DOC_TYPE_TR1" FOREIGN KEY        
("FDOC_GRP_CD")                                                                 
REFERENCES "FP_DOC_GROUP_T" ("FDOC_GRP_CD") ENABLE;                             
                                                                                
                                                                                
ALTER TABLE "FP_MAINTENANCE_DOCUMENT_T" ADD CONSTRAINT                          
"FP_MAINTENANCE_DOCUMENT_TR1" FOREIGN KEY ("FDOC_NBR")                          
REFERENCES "FP_DOC_HEADER_T" ("FDOC_NBR") ENABLE;                               
                                                                                
                                                                                
ALTER TABLE "KIM_GROUPS_GROUPS_T" ADD CONSTRAINT "KIM_GROUPS_GROUPS_FK1" FOREIGN
KEY ("PARENT_GROUP_ID")                                                         
REFERENCES "KIM_GROUPS_T" ("ID") ENABLE;                                        
ALTER TABLE "KIM_GROUPS_GROUPS_T" ADD CONSTRAINT "KIM_GROUPS_GROUPS_FK2" FOREIGN
KEY ("MEMBER_GROUP_ID")                                                         
REFERENCES "KIM_GROUPS_T" ("ID") ENABLE;                                        
                                                                                
                                                                                
ALTER TABLE "KIM_GROUPS_PERSONS_T" ADD CONSTRAINT "KIM_GROUPS_PERSONS_FK1"      
FOREIGN KEY ("GROUP_ID")                                                        
REFERENCES "KIM_GROUPS_T" ("ID") ENABLE;                                        
ALTER TABLE "KIM_GROUPS_PERSONS_T" ADD CONSTRAINT "KIM_GROUPS_PERSONS_FK2"      
FOREIGN KEY ("PERSON_ID")                                                       
REFERENCES "KIM_PERSONS_T" ("ID") ENABLE;                                       
                                                                                
                                                                                
ALTER TABLE "KIM_GROUP_ATTRIBUTES_T" ADD CONSTRAINT "KIM_GROUP_ATTRIBUTES_FK1"  
FOREIGN KEY ("GROUP_ID")                                                        
REFERENCES "KIM_GROUPS_T" ("ID") ENABLE;                                        
ALTER TABLE "KIM_GROUP_ATTRIBUTES_T" ADD CONSTRAINT "KIM_GROUP_ATTRIBUTES_FK2"  
FOREIGN KEY ("ATTRIBUTE_TYPE_ID")                                               
REFERENCES "KIM_ATTRIBUTE_TYPES_T" ("ID") ENABLE;                               
                                                                                
                                                                                
ALTER TABLE "KIM_NAMESPACE_DFLT_ATTRIBS_T" ADD CONSTRAINT                       
"KIM_NMSPCE_DFLT_ATTR_FK1" FOREIGN KEY ("NAMESPACE_ID")                         
REFERENCES "KIM_NAMESPACES_T" ("ID") ENABLE;                                    
ALTER TABLE "KIM_NAMESPACE_DFLT_ATTRIBS_T" ADD CONSTRAINT                       
"KIM_NMSPCE_DFLT_ATTR_FK2" FOREIGN KEY ("ATTRIBUTE_TYPE_ID")                    
REFERENCES "KIM_ATTRIBUTE_TYPES_T" ("ID") ENABLE;                               
                                                                                
                                                                                
ALTER TABLE "KIM_PERMISSIONS_T" ADD CONSTRAINT "KIM_PERMISSIONS_FK1" FOREIGN KEY
("NAMESPACE_ID")                                                                
REFERENCES "KIM_NAMESPACES_T" ("ID") ENABLE;                                    
                                                                                
                                                                                
ALTER TABLE "KIM_PERSON_ATTRIBUTES_T" ADD CONSTRAINT "KIM_PERSON_ATTRIBUTES_FK1"
FOREIGN KEY ("PERSON_ID")                                                       
REFERENCES "KIM_PERSONS_T" ("ID") ENABLE;                                       
ALTER TABLE "KIM_PERSON_ATTRIBUTES_T" ADD CONSTRAINT "KIM_PERSON_ATTRIBUTES_FK2"
FOREIGN KEY ("ATTRIBUTE_TYPE_ID")                                               
REFERENCES "KIM_ATTRIBUTE_TYPES_T" ("ID") ENABLE;                               
ALTER TABLE "KIM_PERSON_ATTRIBUTES_T" ADD CONSTRAINT "KIM_PERSON_ATTRIBUTES_FK3"
FOREIGN KEY ("SPONSOR_NAMESPACE_ID")                                            
REFERENCES "KIM_NAMESPACES_T" ("ID") ENABLE;                                    
                                                                                
                                                                                
ALTER TABLE "KIM_PERSON_QUALIFIED_GROUP_T" ADD CONSTRAINT                       
"KIM_PERSON_QUALIFIED_GROUP_FK1" FOREIGN KEY ("PERSON_ID")                      
REFERENCES "KIM_PERSONS_T" ("ID") ENABLE;                                       
ALTER TABLE "KIM_PERSON_QUALIFIED_GROUP_T" ADD CONSTRAINT                       
"KIM_PERSON_QUALIFIED_GROUP_FK2" FOREIGN KEY ("GROUP_ID")                       
REFERENCES "KIM_GROUPS_T" ("ID") ENABLE;                                        
ALTER TABLE "KIM_PERSON_QUALIFIED_GROUP_T" ADD CONSTRAINT                       
"KIM_PERSON_QUALIFIED_GROUP_FK3" FOREIGN KEY ("GROUP_ATTRIBUTE_ID")             
REFERENCES "KIM_GROUP_ATTRIBUTES_T" ("ID") ENABLE;                              
                                                                                
                                                                                
ALTER TABLE "KIM_PERSON_QUALIFIED_ROLE_T" ADD CONSTRAINT                        
"KIM_PERSON_QUALIFIED_ROLE_FK1" FOREIGN KEY ("PERSON_ID")                       
REFERENCES "KIM_PERSONS_T" ("ID") ENABLE;                                       
ALTER TABLE "KIM_PERSON_QUALIFIED_ROLE_T" ADD CONSTRAINT                        
"KIM_PERSON_QUALIFIED_ROLE_FK2" FOREIGN KEY ("ROLE_ID")                         
REFERENCES "KIM_ROLES_T" ("ID") ENABLE;                                         
ALTER TABLE "KIM_PERSON_QUALIFIED_ROLE_T" ADD CONSTRAINT                        
"KIM_PERSON_QUALIFIED_ROLE_FK3" FOREIGN KEY ("ROLE_ATTRIBUTE_ID")               
REFERENCES "KIM_ROLE_ATTRIBUTES_T" ("ID") ENABLE;                               
                                                                                
                                                                                
ALTER TABLE "KIM_ROLES_GROUPS_T" ADD CONSTRAINT "KIM_ROLES_GROUPS_FK1" FOREIGN  
KEY ("GROUP_ID")                                                                
REFERENCES "KIM_GROUPS_T" ("ID") ENABLE;                                        
ALTER TABLE "KIM_ROLES_GROUPS_T" ADD CONSTRAINT "KIM_ROLES_GROUPS_FK2" FOREIGN  
KEY ("ROLE_ID")                                                                 
REFERENCES "KIM_ROLES_T" ("ID") ENABLE;                                         
                                                                                
                                                                                
ALTER TABLE "KIM_ROLES_PERMISSIONS_T" ADD CONSTRAINT "KIM_ROLES_PERMISSIONS_FK1"
FOREIGN KEY ("ROLE_ID")                                                         
REFERENCES "KIM_ROLES_T" ("ID") ENABLE;                                         
ALTER TABLE "KIM_ROLES_PERMISSIONS_T" ADD CONSTRAINT "KIM_ROLES_PERMISSIONS_FK2"
FOREIGN KEY ("PERMISSION_ID")                                                   
REFERENCES "KIM_PERMISSIONS_T" ("ID") ENABLE;                                   
                                                                                
                                                                                
ALTER TABLE "KIM_ROLES_PERSONS_T" ADD CONSTRAINT "KIM_ROLES_PERSONS_FK1" FOREIGN
KEY ("ROLE_ID")                                                                 
REFERENCES "KIM_ROLES_T" ("ID") ENABLE;                                         
ALTER TABLE "KIM_ROLES_PERSONS_T" ADD CONSTRAINT "KIM_ROLES_PERSONS_FK2" FOREIGN
KEY ("PERSON_ID")                                                               
REFERENCES "KIM_PERSONS_T" ("ID") ENABLE;                                       
                                                                                
                                                                                
ALTER TABLE "KIM_ROLE_ATTRIBUTES_T" ADD CONSTRAINT "KIM_ROLE_ATTRIBUTES_FK1"    
FOREIGN KEY ("ROLE_ID")                                                         
REFERENCES "KIM_ROLES_T" ("ID") ENABLE;                                         
ALTER TABLE "KIM_ROLE_ATTRIBUTES_T" ADD CONSTRAINT "KIM_ROLE_ATTRIBUTES_FK2"    
FOREIGN KEY ("ATTRIBUTE_TYPE_ID")                                               
REFERENCES "KIM_ATTRIBUTE_TYPES_T" ("ID") ENABLE;                               
                                                                                
                                                                                
ALTER TABLE "KRA_USER" ADD CONSTRAINT "FK_USER_UNIT_NUMBER_KRA" FOREIGN KEY     
("UNIT_NUMBER")                                                                 
REFERENCES "UNIT" ("UNIT_NUMBER") ENABLE;                                       
                                                                                
                                                                                
ALTER TABLE "KR_QRTZ_BLOB_TRIGGERS" ADD CONSTRAINT "KR_QRTZ_BLOB_TRIGGERS_TR1"  
FOREIGN KEY ("TRIGGER_NAME", "TRIGGER_GROUP")                                   
REFERENCES "KR_QRTZ_TRIGGERS" ("TRIGGER_NAME", "TRIGGER_GROUP") ENABLE;         
                                                                                
                                                                                
ALTER TABLE "KR_QRTZ_CRON_TRIGGERS" ADD CONSTRAINT "KR_QRTZ_CRON_TRIGGERS_TR1"  
FOREIGN KEY ("TRIGGER_NAME", "TRIGGER_GROUP")                                   
REFERENCES "KR_QRTZ_TRIGGERS" ("TRIGGER_NAME", "TRIGGER_GROUP") ENABLE;         
                                                                                
                                                                                
ALTER TABLE "KR_QRTZ_JOB_LISTENERS" ADD CONSTRAINT "KR_QRTZ_JOB_LISTENERS_TR1"  
FOREIGN KEY ("JOB_NAME", "JOB_GROUP")                                           
REFERENCES "KR_QRTZ_JOB_DETAILS" ("JOB_NAME", "JOB_GROUP") ENABLE;              
                                                                                
                                                                                
ALTER TABLE "KR_QRTZ_SIMPLE_TRIGGERS" ADD CONSTRAINT "KR_QRTZ_SIMPLE_TRIGGERS"  
FOREIGN KEY ("TRIGGER_NAME", "TRIGGER_GROUP")                                   
REFERENCES "KR_QRTZ_TRIGGERS" ("TRIGGER_NAME", "TRIGGER_GROUP") ENABLE;         
                                                                                
                                                                                
ALTER TABLE "KR_QRTZ_TRIGGERS" ADD CONSTRAINT "KR_QRTZ_TRIGGERS_TR1" FOREIGN KEY
("JOB_NAME", "JOB_GROUP")                                                       
REFERENCES "KR_QRTZ_JOB_DETAILS" ("JOB_NAME", "JOB_GROUP") ENABLE;              
                                                                                
                                                                                
ALTER TABLE "KR_QRTZ_TRIGGER_LISTENERS" ADD CONSTRAINT                          
"KR_QRTZ_TRIGGER_LISTENERS_TR1" FOREIGN KEY ("TRIGGER_NAME", "TRIGGER_GROUP")   
REFERENCES "KR_QRTZ_TRIGGERS" ("TRIGGER_NAME", "TRIGGER_GROUP") ENABLE;         
                                                                                
                                                                                
ALTER TABLE "NARRATIVE" ADD CONSTRAINT "FK_NARRATIVE_KRA" FOREIGN KEY           
("PROPOSAL_NUMBER")                                                             
REFERENCES "EPS_PROPOSAL" ("PROPOSAL_NUMBER") ENABLE;                           
ALTER TABLE "NARRATIVE" ADD CONSTRAINT "FK_NARRATIVE_STATUS_CODE_KRA" FOREIGN   
KEY ("MODULE_STATUS_CODE")                                                      
REFERENCES "NARRATIVE_STATUS" ("NARRATIVE_STATUS_CODE") ENABLE;                 
ALTER TABLE "NARRATIVE" ADD CONSTRAINT "FK_NARRATIVE_TYPE_CODE_KRA" FOREIGN KEY 
("NARRATIVE_TYPE_CODE")                                                         
REFERENCES "NARRATIVE_TYPE" ("NARRATIVE_TYPE_CODE") ENABLE;                     
                                                                                
                                                                                
ALTER TABLE "NARRATIVE_ATTACHMENT" ADD CONSTRAINT "FK_NARRATIVE_ATTACHMENT_KRA" 
FOREIGN KEY ("PROPOSAL_NUMBER", "MODULE_NUMBER")                                
REFERENCES "NARRATIVE" ("PROPOSAL_NUMBER", "MODULE_NUMBER") ENABLE;             
                                                                                
                                                                                
ALTER TABLE "NARRATIVE_USER_RIGHTS" ADD CONSTRAINT                              
"FK_NARRATIVE_USER_RIGHTS_KRA" FOREIGN KEY ("PROPOSAL_NUMBER", "MODULE_NUMBER") 
REFERENCES "NARRATIVE" ("PROPOSAL_NUMBER", "MODULE_NUMBER") ENABLE;             
                                                                                
                                                                                
ALTER TABLE "NOTIFICATIONS" ADD CONSTRAINT "NOTIFICATIONS_NOTIFICATIO_FK1"      
FOREIGN KEY ("NOTIFICATION_CHANNEL_ID")                                         
REFERENCES "NOTIFICATION_CHANNELS" ("ID") ENABLE;                               
ALTER TABLE "NOTIFICATIONS" ADD CONSTRAINT "NOTIFICATIONS_NOTIFICATIO_FK2"      
FOREIGN KEY ("CONTENT_TYPE_ID")                                                 
REFERENCES "NOTIFICATION_CONTENT_TYPES" ("ID") ENABLE;                          
ALTER TABLE "NOTIFICATIONS" ADD CONSTRAINT "NOTIFICATIONS_NOTIFICATIO_FK3"      
FOREIGN KEY ("PRIORITY_ID")                                                     
REFERENCES "NOTIFICATION_PRIORITIES" ("ID") ENABLE;                             
ALTER TABLE "NOTIFICATIONS" ADD CONSTRAINT "NOTIFICATIONS_NOTIFICATIO_FK4"      
FOREIGN KEY ("PRODUCER_ID")                                                     
REFERENCES "NOTIFICATION_PRODUCERS" ("ID") ENABLE;                              
                                                                                
                                                                                
ALTER TABLE "NOTIFICATION_CHANNEL_PRODUCERS" ADD CONSTRAINT                     
"NOTIFICATION_CHANNEL_PROD_FK1" FOREIGN KEY ("CHANNEL_ID")                      
REFERENCES "NOTIFICATION_CHANNELS" ("ID") ENABLE;                               
ALTER TABLE "NOTIFICATION_CHANNEL_PRODUCERS" ADD CONSTRAINT                     
"NOTIFICATION_CHANNEL_PROD_FK2" FOREIGN KEY ("PRODUCER_ID")                     
REFERENCES "NOTIFICATION_PRODUCERS" ("ID") ENABLE;                              
                                                                                
                                                                                
ALTER TABLE "NOTIFICATION_MSG_DELIVS" ADD CONSTRAINT "NOTIF_MSG_DELIVS_FK1"     
FOREIGN KEY ("NOTIFICATION_ID")                                                 
REFERENCES "NOTIFICATIONS" ("ID") ENABLE;                                       
                                                                                
                                                                                
ALTER TABLE "NOTIFICATION_RECIPIENTS" ADD CONSTRAINT                            
"NOTIFICATION_RECIPIENTS_N_FK1" FOREIGN KEY ("NOTIFICATION_ID")                 
REFERENCES "NOTIFICATIONS" ("ID") ENABLE;                                       
                                                                                
                                                                                
ALTER TABLE "NOTIFICATION_RECIPIENTS_LISTS" ADD CONSTRAINT                      
"NOTIFICATION_RECIPIENTS_L_FK1" FOREIGN KEY ("CHANNEL_ID")                      
REFERENCES "NOTIFICATION_CHANNELS" ("ID") ENABLE;                               
                                                                                
                                                                                
ALTER TABLE "NOTIFICATION_REVIEWERS" ADD CONSTRAINT                             
"NOTIFICATION_REVIEWERS_N_FK1" FOREIGN KEY ("CHANNEL_ID")                       
REFERENCES "NOTIFICATION_CHANNELS" ("ID") ENABLE;                               
                                                                                
                                                                                
ALTER TABLE "NOTIFICATION_SENDERS" ADD CONSTRAINT                               
"NOTIFICATION_SENDERS_NOTI_FK1" FOREIGN KEY ("NOTIFICATION_ID")                 
REFERENCES "NOTIFICATIONS" ("ID") ENABLE;                                       
                                                                                
                                                                                
ALTER TABLE "PROPOSAL_NEXTVALUE" ADD CONSTRAINT "FK_PROPOSAL_NUMBER" FOREIGN KEY
("PROPOSAL_NUMBER")                                                             
REFERENCES "EPS_PROPOSAL" ("PROPOSAL_NUMBER") ENABLE;                           
                                                                                
                                                                                
ALTER TABLE "ROLE_RIGHTS" ADD CONSTRAINT "FK_ROLE_RIGHTS_KRA" FOREIGN KEY       
("RIGHT_ID")                                                                    
REFERENCES "RIGHTS" ("RIGHT_ID") ENABLE;                                        
ALTER TABLE "ROLE_RIGHTS" ADD CONSTRAINT "FK_ROLE_RIGHTS_ROLE_KRA" FOREIGN KEY  
("ROLE_ID")                                                                     
REFERENCES "ROLE" ("ROLE_ID") ENABLE;                                           
                                                                                
                                                                                
ALTER TABLE "ROLODEX" ADD CONSTRAINT "FK_ROLODEX_COUNTRY_KRA" FOREIGN KEY       
("COUNTRY_CODE")                                                                
REFERENCES "COUNTRY_CODE" ("COUNTRY_CODE") ENABLE;                              
                                                                                
                                                                                
ALTER TABLE "SH_ATT_T" ADD CONSTRAINT "SH_ATT_TR1" FOREIGN KEY ("NTE_ID")       
REFERENCES "SH_NTE_T" ("NTE_ID") ENABLE;                                        
                                                                                
                                                                                
ALTER TABLE "SH_LOCK_T" ADD CONSTRAINT "SH_LOCK_TR1" FOREIGN KEY                
("TRN_SMPHR_TYP_CD")                                                            
REFERENCES "SH_LOCK_TYP_DESC_T" ("TRN_SMPHR_TYP_CD") ENABLE;                    
                                                                                
                                                                                
ALTER TABLE "SH_NTE_T" ADD CONSTRAINT "SH_NTE_TR1" FOREIGN KEY ("NTE_TYP_CD")   
REFERENCES "SH_NTE_TYP_T" ("NTE_TYP_CD") ENABLE;                                
                                                                                
                                                                                
ALTER TABLE "SH_PARM_DTL_TYP_T" ADD CONSTRAINT "SH_PARM_DTL_TYP_TR1" FOREIGN KEY
("SH_PARM_NMSPC_CD")                                                            
REFERENCES "SH_PARM_NMSPC_T" ("SH_PARM_NMSPC_CD") ENABLE;                       
                                                                                
                                                                                
ALTER TABLE "SH_PARM_T" ADD CONSTRAINT "SH_PARM_TR1" FOREIGN KEY                
("SH_PARM_NMSPC_CD")                                                            
REFERENCES "SH_PARM_NMSPC_T" ("SH_PARM_NMSPC_CD") ENABLE;                       
ALTER TABLE "SH_PARM_T" ADD CONSTRAINT "SH_PARM_TR2" FOREIGN KEY                
("SH_PARM_TYP_CD")                                                              
REFERENCES "SH_PARM_TYP_T" ("SH_PARM_TYP_CD") ENABLE;                           
                                                                                
                                                                                
ALTER TABLE "SPONSOR" ADD CONSTRAINT "FK_SPONSOR_ROLODEX_KRA" FOREIGN KEY       
("ROLODEX_ID")                                                                  
REFERENCES "ROLODEX" ("ROLODEX_ID") ENABLE;                                     
ALTER TABLE "SPONSOR" ADD CONSTRAINT "FK_SPONSOR_TYPE_CODE_KRA" FOREIGN KEY     
("SPONSOR_TYPE_CODE")                                                           
REFERENCES "SPONSOR_TYPE" ("SPONSOR_TYPE_CODE") ENABLE;                         
                                                                                
                                                                                
ALTER TABLE "STATE_CODE" ADD CONSTRAINT "FK_STATE_CODE_COUNTRY" FOREIGN KEY     
("COUNTRY_CODE")                                                                
REFERENCES "COUNTRY_CODE" ("COUNTRY_CODE") ENABLE;                              
                                                                                
                                                                                
ALTER TABLE "TRV_ACCT" ADD CONSTRAINT "TRV_ACCT_FK1" FOREIGN KEY ("ACCT_FO_ID") 
REFERENCES "TRV_ACCT_FO" ("ACCT_FO_ID") ENABLE;                                 
                                                                                
                                                                                
ALTER TABLE "UNIT_HIERARCHY" ADD CONSTRAINT "FK_UNIT_HIERARCHY_UNIT_KRA" FOREIGN
KEY ("UNIT_NUMBER")                                                             
REFERENCES "UNIT" ("UNIT_NUMBER") ENABLE;                                       
ALTER TABLE "UNIT_HIERARCHY" ADD CONSTRAINT "FK_UNIT_HIERAR_PARENT_UNIT_KRA"    
FOREIGN KEY ("PARENT_UNIT_NUMBER")                                              
REFERENCES "UNIT" ("UNIT_NUMBER") ENABLE;                                       
                                                                                
                                                                                
ALTER TABLE "USER_CHANNEL_SUBSCRIPTIONS" ADD CONSTRAINT                         
"USER_CHANNEL_SUBSCRIPTION_FK1" FOREIGN KEY ("CHANNEL_ID")                      
REFERENCES "NOTIFICATION_CHANNELS" ("ID") ENABLE;                               
                                                                                
                                                                                
ALTER TABLE "USER_DELIVERER_CONFIG" ADD CONSTRAINT "USER_DELIVERER_CONFIG_FK1"  
FOREIGN KEY ("CHANNEL_ID")                                                      
REFERENCES "NOTIFICATION_CHANNELS" ("ID") ENABLE;                               
                                                                                
                                                                                
ALTER TABLE "USER_ROLES" ADD CONSTRAINT "FK_USER_ROLES_KRA" FOREIGN KEY         
("ROLE_ID")                                                                     
REFERENCES "ROLE" ("ROLE_ID") ENABLE;                                           
ALTER TABLE "USER_ROLES" ADD CONSTRAINT "FK_USER_ROLES_UNIT_NUMBER_KRA" FOREIGN 
KEY ("UNIT_NUMBER")                                                             
REFERENCES "UNIT" ("UNIT_NUMBER") ENABLE;                                       
ALTER TABLE "USER_ROLES" ADD CONSTRAINT "FK_USER_ROLES_USER_KRA" FOREIGN KEY    
("USER_ID")                                                                     
REFERENCES "KRA_USER" ("USER_ID") ENABLE;                                       
                                                                                
                                                                                
ALTER TABLE "VALID_SP_REV_APPROVAL" ADD CONSTRAINT                              
"FK_VALID_SP_REV_APPROVAL_KRA" FOREIGN KEY ("SPECIAL_REVIEW_CODE")              
REFERENCES "SPECIAL_REVIEW" ("SPECIAL_REVIEW_CODE") ENABLE;                     
ALTER TABLE "VALID_SP_REV_APPROVAL" ADD CONSTRAINT                              
"FK_VALID_SP_REV_APPROV_TP_KRA" FOREIGN KEY ("APPROVAL_TYPE_CODE")              
REFERENCES "SP_REV_APPROVAL_TYPE" ("APPROVAL_TYPE_CODE") ENABLE;                
                                                                                
                                                                                
ALTER TABLE "YNQ_EXPLANATION" ADD CONSTRAINT "FK_YNQ_EXPLANATION_KRA" FOREIGN   
KEY ("QUESTION_ID")                                                             
REFERENCES "YNQ" ("QUESTION_ID") ENABLE;                                        
                                                                                

ALTER TABLE YNQ_EXPLANATION ADD (CONSTRAINT "FK_YNQ_EXPLANATION_TYPE_KRA" FOREIGN KEY ("EXPLANATION_TYPE")
	  REFERENCES "YNQ_EXPLANATION_TYPE" ("EXPLANATION_TYPE") );

ALTER TABLE EPS_PROP_YNQ ADD (CONSTRAINT "FK_EPS_PROP_YNQ_KRA" FOREIGN KEY ("PROPOSAL_NUMBER")
	  REFERENCES "EPS_PROPOSAL" ("PROPOSAL_NUMBER") );

 ALTER TABLE EPS_PROP_YNQ ADD (CONSTRAINT "FK_EPS_PROP_YNQ_ID_KRA" FOREIGN KEY ("QUESTION_ID")
	  REFERENCES "YNQ" ("QUESTION_ID") );

 ALTER TABLE EPS_PROP_PERS_YNQ ADD (CONSTRAINT "FK_EPS_PROP_PERS_YNQ_ID_KRA" FOREIGN KEY ("QUESTION_ID")
	  REFERENCES "YNQ" ("QUESTION_ID") );

/*Starting s2s and budget */

ALTER TABLE S2S_OPPORTUNITY ADD (CONSTRAINT "FK1_S2S_OPPORTUNITY_KRA" FOREIGN KEY ("S2S_SUBMISSION_TYPE_CODE")
	  REFERENCES "S2S_SUBMISSION_TYPE" ("S2S_SUBMISSION_TYPE_CODE") );
	   
ALTER TABLE BUDGET_DETAILS_CAL_AMTS ADD (CONSTRAINT "FK_BUDGET_DETAILS_CAL_RATE_KRA" FOREIGN KEY ("RATE_CLASS_CODE", "RATE_TYPE_CODE")
	  REFERENCES "RATE_TYPE" ("RATE_CLASS_CODE", "RATE_TYPE_CODE") );

ALTER TABLE BUDGET_DETAILS ADD (CONSTRAINT "FK_BUDGET_DETAILS_CATEGORY_KRA" FOREIGN KEY ("BUDGET_CATEGORY_CODE")
	  REFERENCES "BUDGET_CATEGORY" ("BUDGET_CATEGORY_CODE") ); 

ALTER TABLE BUDGET_DETAILS ADD (CONSTRAINT "FK_BUDGET_DETAILS_CE_KRA" FOREIGN KEY ("COST_ELEMENT")
	  REFERENCES "COST_ELEMENT" ("COST_ELEMENT") );
	  
ALTER TABLE BUDGET_PERSONNEL_CAL_AMTS ADD (CONSTRAINT "FK_BUDGET_PER_CAL_RATE_KRA" FOREIGN KEY ("RATE_CLASS_CODE", "RATE_TYPE_CODE")
	  REFERENCES "RATE_TYPE" ("RATE_CLASS_CODE", "RATE_TYPE_CODE") ); 	  	  	  	  
  	  	  
ALTER TABLE EPS_PROP_RATES ADD (CONSTRAINT "FK_EPS_PROP_RATES_ACTIVITY_KRA" FOREIGN KEY ("ACTIVITY_TYPE_CODE")
	  REFERENCES "ACTIVITY_TYPE" ("ACTIVITY_TYPE_CODE") ); 
 
ALTER TABLE EPS_PROP_RATES ADD (CONSTRAINT "FK_EPS_PROP_RATES_CLASS_KRA" FOREIGN KEY ("RATE_CLASS_CODE", "RATE_TYPE_CODE")
	  REFERENCES "RATE_TYPE" ("RATE_CLASS_CODE", "RATE_TYPE_CODE") );
	  
ALTER TABLE INSTITUTE_LA_RATES ADD (CONSTRAINT "FK_INSTITUTE_LA_RATES_UNIT_KRA" FOREIGN KEY ("UNIT_NUMBER")
	  REFERENCES "UNIT" ("UNIT_NUMBER") ); 
    	  	     	  	  
ALTER TABLE INSTITUTE_RATES ADD (CONSTRAINT "FK_INSTITUTE_RATES_KRA" FOREIGN KEY ("RATE_CLASS_CODE", "RATE_TYPE_CODE")
	  REFERENCES "RATE_TYPE" ("RATE_CLASS_CODE", "RATE_TYPE_CODE") ); 
 
ALTER TABLE INSTITUTE_RATES ADD (CONSTRAINT "FK_INST_RATES_ACTIVITY_KRA" FOREIGN KEY ("ACTIVITY_TYPE_CODE")
	  REFERENCES "ACTIVITY_TYPE" ("ACTIVITY_TYPE_CODE") ); 
 
ALTER TABLE INSTITUTE_RATES ADD (CONSTRAINT "FK_INST_RATES_UNIT_NUMBER_KRA" FOREIGN KEY ("UNIT_NUMBER")
	  REFERENCES "UNIT" ("UNIT_NUMBER") ); 
     	  	     	  	  
ALTER TABLE COST_ELEMENT ADD (CONSTRAINT "FK_COST_ELEMENT_CATEGORY_KRA" FOREIGN KEY ("BUDGET_CATEGORY_CODE")
	  REFERENCES "BUDGET_CATEGORY" ("BUDGET_CATEGORY_CODE") );

ALTER TABLE VALID_CALC_TYPES ADD (CONSTRAINT "FK_RATE_CLASS_CODE_KRA" FOREIGN KEY ("RATE_CLASS_CODE", "RATE_TYPE_CODE")
	  REFERENCES "RATE_TYPE" ("RATE_CLASS_CODE", "RATE_TYPE_CODE") );
	  
ALTER TABLE VALID_CE_RATE_TYPES ADD (CONSTRAINT "FK_VALID_CE_RATE_TYPES_KRA" FOREIGN KEY ("RATE_CLASS_CODE", "RATE_TYPE_CODE")
	  REFERENCES "RATE_TYPE" ("RATE_CLASS_CODE", "RATE_TYPE_CODE") ); 
 
ALTER TABLE VALID_CE_RATE_TYPES ADD (CONSTRAINT "FK_VALID_CE_RATE_TYPES_CE_KRA" FOREIGN KEY ("COST_ELEMENT")
	  REFERENCES "COST_ELEMENT" ("COST_ELEMENT") );

	  
/*
ALTER TABLE S2S_OPPORTUNITY ADD (CONSTRAINT "FK_S2S_OPPORTUNITY_KRA" FOREIGN KEY ("PROPOSAL_NUMBER")
	  REFERENCES "EPS_PROPOSAL" ("PROPOSAL_NUMBER") );
	  
ALTER TABLE S2S_OPP_FORMS ADD (CONSTRAINT "FK_S2S_OPP_FORMS_KRA" FOREIGN KEY ("PROPOSAL_NUMBER")
	  REFERENCES "S2S_OPPORTUNITY" ("PROPOSAL_NUMBER") );
	  
ALTER TABLE S2S_APP_SUBMISSION ADD (CONSTRAINT "FK_S2S_APP_SUBMISSION_KRA" FOREIGN KEY ("PROPOSAL_NUMBER")
	  REFERENCES "EPS_PROPOSAL" ("PROPOSAL_NUMBER") );
	  
ALTER TABLE S2S_APPLICATION ADD (CONSTRAINT "FK_S2S_APPLICATION_KRA" FOREIGN KEY ("PROPOSAL_NUMBER")
	  REFERENCES "S2S_APP_SUBMISSION" ("PROPOSAL_NUMBER") );
	  
ALTER TABLE S2S_APP_ATTACHMENTS ADD (CONSTRAINT "FK_S2S_ATTACHMENTS_KRA" FOREIGN KEY ("PROPOSAL_NUMBER")
	  REFERENCES "S2S_APP_SUBMISSION" ("PROPOSAL_NUMBER") );

ALTER TABLE BUDGET_SUB_AWARDS ADD (CONSTRAINT "FK1_BUDGET_SUB_AWARDS_KRA" FOREIGN KEY ("PROPOSAL_NUMBER", "VERSION_NUMBER")
	  REFERENCES "BUDGET" ("PROPOSAL_NUMBER", "VERSION_NUMBER") );
	  
ALTER TABLE BUDGET_SUB_AWARD_ATT ADD (CONSTRAINT "FK1_BUDGET_SUB_AWARD_ATT_KRA" FOREIGN KEY ("PROPOSAL_NUMBER", "VERSION_NUMBER", "SUB_AWARD_NUMBER")
	  REFERENCES "BUDGET_SUB_AWARDS" ("PROPOSAL_NUMBER", "VERSION_NUMBER", "SUB_AWARD_NUMBER") ON DELETE CASCADE );	  
 	  	  
ALTER TABLE BUDGET_DETAILS_CAL_AMTS ADD (CONSTRAINT "FK_BUDGET_DETAILS_CAL_AMTS_KRA" FOREIGN KEY ("PROPOSAL_NUMBER", "VERSION_NUMBER", "BUDGET_PERIOD", "LINE_ITEM_NUMBER")
	  REFERENCES "BUDGET_DETAILS" ("PROPOSAL_NUMBER", "VERSION_NUMBER", "BUDGET_PERIOD", "LINE_ITEM_NUMBER") ON DELETE CASCADE ); 

	  
ALTER TABLE BUDGET_DETAILS ADD (CONSTRAINT "FK_BUDGET_DETAILS_KRA" FOREIGN KEY ("PROPOSAL_NUMBER", "VERSION_NUMBER", "BUDGET_PERIOD")
	  REFERENCES "BUDGET_PERIODS" ("PROPOSAL_NUMBER", "VERSION_NUMBER", "BUDGET_PERIOD") ON DELETE CASCADE ); 

ALTER TABLE BUDGET_PERIODS ADD (CONSTRAINT "FK_BUDGET_PERIODS_KRA" FOREIGN KEY ("PROPOSAL_NUMBER", "VERSION_NUMBER")
	  REFERENCES "BUDGET" ("PROPOSAL_NUMBER", "VERSION_NUMBER") );

ALTER TABLE BUDGET_PERSONNEL_CAL_AMTS ADD (CONSTRAINT "FK_BUDGET_PER_CAL_AMTS_KRA" FOREIGN KEY ("PROPOSAL_NUMBER", "VERSION_NUMBER", "BUDGET_PERIOD", "LINE_ITEM_NUMBER", "PERSON_NUMBER")
	  REFERENCES "BUDGET_PERSONNEL_DETAILS" ("PROPOSAL_NUMBER", "VERSION_NUMBER", "BUDGET_PERIOD", "LINE_ITEM_NUMBER", "PERSON_NUMBER") ON DELETE CASCADE ); 
 
ALTER TABLE BUDGET_PERSONNEL_DETAILS ADD (CONSTRAINT "FK_BUDGET_PER_DETAILS_KRA" FOREIGN KEY ("PROPOSAL_NUMBER", "VERSION_NUMBER", "BUDGET_PERIOD", "LINE_ITEM_NUMBER")
	  REFERENCES "BUDGET_DETAILS" ("PROPOSAL_NUMBER", "VERSION_NUMBER", "BUDGET_PERIOD", "LINE_ITEM_NUMBER") ON DELETE CASCADE );

ALTER TABLE BUDGET_PERSONS ADD (CONSTRAINT "FK_BUDGET_PERSONS_KRA" FOREIGN KEY ("PROPOSAL_NUMBER", "VERSION_NUMBER")
	  REFERENCES "BUDGET" ("PROPOSAL_NUMBER", "VERSION_NUMBER") ); 

ALTER TABLE EPS_PROP_RATES ADD (CONSTRAINT "FK_EPS_PROP_RATES_KRA" FOREIGN KEY ("PROPOSAL_NUMBER", "VERSION_NUMBER")
	  REFERENCES "BUDGET" ("PROPOSAL_NUMBER", "VERSION_NUMBER") ); 

ALTER TABLE EPS_PROP_LA_RATES ADD (CONSTRAINT "FK_EPS_PROP_LA_RATES_KRA" FOREIGN KEY ("PROPOSAL_NUMBER", "VERSION_NUMBER")
	  REFERENCES "BUDGET" ("PROPOSAL_NUMBER", "VERSION_NUMBER") ); 
 
 
*/
