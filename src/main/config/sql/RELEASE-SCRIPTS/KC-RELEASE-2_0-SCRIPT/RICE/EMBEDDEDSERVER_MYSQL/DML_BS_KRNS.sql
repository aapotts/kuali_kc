-- REM INSERTING INTO KRNS_NMSPC_T
INSERT INTO KRNS_NMSPC_T (APPL_NMSPC_CD,NMSPC_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES (NULL,'KC-AB','Award Budget','Y',1,UUID());

INSERT INTO KRNS_NMSPC_T (APPL_NMSPC_CD,NMSPC_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES (NULL,'KC-ADM','Kuali Coeus-Office of Sponsored Projects','Y',1,UUID());

INSERT INTO KRNS_NMSPC_T (APPL_NMSPC_CD,NMSPC_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES (NULL,'KC-AWARD','Award','Y',1,UUID());

INSERT INTO KRNS_NMSPC_T (APPL_NMSPC_CD,NMSPC_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES ('RICE','KC-B','Budget','Y',1,UUID());

INSERT INTO KRNS_NMSPC_T (APPL_NMSPC_CD,NMSPC_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES (NULL,'KC-GEN','General Kuali Coeus','Y',1,UUID());

INSERT INTO KRNS_NMSPC_T (APPL_NMSPC_CD,NMSPC_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES (NULL,'KC-IDM','Kuali Coeus KIM IDM & AuthZ','Y',1,UUID());

INSERT INTO KRNS_NMSPC_T (APPL_NMSPC_CD,NMSPC_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES (NULL,'KC-IP','Institutional Proposal','Y',1,UUID());

INSERT INTO KRNS_NMSPC_T (APPL_NMSPC_CD,NMSPC_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES ('RICE','KC-M','Maintenance','Y',1,UUID());

INSERT INTO KRNS_NMSPC_T (APPL_NMSPC_CD,NMSPC_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES ('RICE','KC-PD','Proposal Development','Y',1,UUID());

INSERT INTO KRNS_NMSPC_T (APPL_NMSPC_CD,NMSPC_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES (NULL,'KC-PROTOCOL','KC IRB Protocol','Y',1,UUID());

INSERT INTO KRNS_NMSPC_T (APPL_NMSPC_CD,NMSPC_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES (NULL,'KC-QUESTIONNAIRE','KC Questionnaire','Y',1,UUID());

INSERT INTO KRNS_NMSPC_T (APPL_NMSPC_CD,NMSPC_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES (NULL,'KC-SYS','Kuali Coeus System','Y',1,UUID());

INSERT INTO KRNS_NMSPC_T (APPL_NMSPC_CD,NMSPC_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES (NULL,'KC-T','Time And Money','Y',1,UUID());

INSERT INTO KRNS_NMSPC_T (APPL_NMSPC_CD,NMSPC_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES (NULL,'KC-UNT','Kuali Coeus - Department','Y',1,UUID());

INSERT INTO KRNS_NMSPC_T (APPL_NMSPC_CD,NMSPC_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES (NULL,'KC-WKFLW','KC Workflow Infrastructure','Y',1,UUID());

-- REM INSERTING INTO KRNS_PARM_DTL_TYP_T
INSERT INTO KRNS_PARM_DTL_TYP_T (NMSPC_CD,PARM_DTL_TYP_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES ('KC-AB','D','Document','Y',1,UUID());

INSERT INTO KRNS_PARM_DTL_TYP_T (NMSPC_CD,PARM_DTL_TYP_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES ('KC-AWARD','D','Document','Y',1,UUID());

INSERT INTO KRNS_PARM_DTL_TYP_T (NMSPC_CD,PARM_DTL_TYP_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES ('KC-B','A','All','Y',1,UUID());

INSERT INTO KRNS_PARM_DTL_TYP_T (NMSPC_CD,PARM_DTL_TYP_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES ('KC-B','D','Document','Y',1,UUID());

INSERT INTO KRNS_PARM_DTL_TYP_T (NMSPC_CD,PARM_DTL_TYP_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES ('KR-WKFLW','Rule','Rule','Y',1,UUID());

INSERT INTO KRNS_PARM_DTL_TYP_T (NMSPC_CD,PARM_DTL_TYP_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES ('KC-PD','A','All','Y',1,UUID());

INSERT INTO KRNS_PARM_DTL_TYP_T (NMSPC_CD,PARM_DTL_TYP_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES ('KC-PD','D','Document','Y',1,UUID());

INSERT INTO KRNS_PARM_DTL_TYP_T (NMSPC_CD,PARM_DTL_TYP_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES ('KC-PD','L','Lookup','Y',1,UUID());

INSERT INTO KRNS_PARM_DTL_TYP_T (NMSPC_CD,PARM_DTL_TYP_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES ('KC-PROTOCOL','D','Document','Y',1,UUID());

INSERT INTO KRNS_PARM_DTL_TYP_T (NMSPC_CD,PARM_DTL_TYP_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES ('KC-QUESTIONNAIRE','P','Permissions','Y',1,UUID());

INSERT INTO KRNS_PARM_DTL_TYP_T (NMSPC_CD,PARM_DTL_TYP_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES ('KC-GEN','A','All','Y',1,UUID());

INSERT INTO KRNS_PARM_DTL_TYP_T (NMSPC_CD,PARM_DTL_TYP_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES ('KC-GEN','D','Document','Y',1,UUID());

INSERT INTO KRNS_PARM_DTL_TYP_T (NMSPC_CD,PARM_DTL_TYP_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES ('KC-GEN','DocumentType','Cusatom Attribute Document Type','Y',1,UUID());

INSERT INTO KRNS_PARM_DTL_TYP_T (NMSPC_CD,PARM_DTL_TYP_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES ('KC-IP','D','Document','Y',1,UUID());

INSERT INTO KRNS_PARM_DTL_TYP_T (NMSPC_CD,PARM_DTL_TYP_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES ('KC-M','D','Document','Y',1,UUID());

INSERT INTO KRNS_PARM_DTL_TYP_T (NMSPC_CD,PARM_DTL_TYP_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES ('KC-T','D','Document','Y',1,UUID());

INSERT INTO KRNS_PARM_DTL_TYP_T (NMSPC_CD,PARM_DTL_TYP_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES ('KR-IDM','PersonDocumentName','Person Document Name','Y',1,UUID());

INSERT INTO KRNS_PARM_DTL_TYP_T (NMSPC_CD,PARM_DTL_TYP_CD,NM,ACTV_IND,VER_NBR,OBJ_ID) 
VALUES ('KR-WKFLW','DocSearchCriteriaDTO','Document Search','Y',1,UUID());

-- REM INSERTING INTO KRNS_PARM_T
INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetStatusCompleteCode','D','CONFG','A','1','Code corresponding to the budget status of Complete',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','defaultOverheadRateClassCode','D','CONFG','A','1','The overhead rate class a new Budget should default to',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','proposalBudgetStatusHelpUrl','D','HELP','A','default.htm?turl=Documents/budgetstatus.htm','Budget Parameters Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetUnrecoveredFandAApplicabilityFlag','D','CONFG','A','Y',' Flag indicating if Unrecovered F&A is applicable for the budget',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetCostSharingApplicabilityFlag','D','CONFG','A','Y',' Flag indicating if Cost Sharing is applicable for the budget',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetCurrentFiscalYear','D','CONFG','A','07/01/2000',' The starting fiscal year for a budget',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetPersonDefaultCalculationBase','D','CONFG','A','0','The Calculation Base a new Budget Person should default to',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetPersonDefaultAppointmentType','D','CONFG','A','7','The Appointment Type a new Budget Person should default to',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetPersonDefaultJobCode','D','CONFG','A','0','The Job Code a new Budget Person should default to',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','defaultModularFlag','D','CONFG','A','N','Default value of modular flag for a new Budget.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','defaultUnderrecoveryRateClassCode','D','CONFG','A','1','The underrecovery rate class a new Budget should default to',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','JOBCODE_VALIDATION_ENABLED','D','CONFG','A','Y','Whether Job code based validation is enabled',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetUnrecoveredFandAEnforcementFlag','D','CONFG','A','Y','Flag indicating if Unrecovered F and A allocation should be enforced',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetCostSharingEnforcementFlag','D','CONFG','A','Y','Flag indicating if Cost Sharing allocation should be enforced',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetPersonDefaultPeriodType','D','CONFG','A','3','Default Period Type',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetModularHelpUrl','D','HELP','A','default.htm?turl=Documents/modularbudgetoverviewpanel.htm','Budget Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetNonPersonnelHelp','D','HELP','A','default.htm?turl=Documents/nonpersonneltab.htm','Budget Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetPersonnelHelp','D','HELP','A','default.htm?turl=Documents/personneltab.htm','Budget Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetRatesHelp','D','HELP','A','default.htm?turl=Documents/rateclassessubpanelontheobjectcodenamesubpanel.htm','Budget Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetParametersHelp','D','HELP','A','default.htm?turl=Documents/parameterstab.htm','Budget Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetVersionsHelp','D','HELP','A','default.htm?turl=Documents/budgetversionspanel.htm','Budget Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetPersonnelDetailsHelpUrl','D','HELP','A','default.htm?turl=Documents/personneldetailpanel.htm','Budget Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetCategoryHelpUrl','D','HELP','A','default.htm?turl=Documents/budgetcategory.htm','Budget Category Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetProjectIncomeHelpUrl','D','HELP','A','default.htm?turl=Documents/projectincomepanel.htm','Budget Project Income Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','fnaRateClassTypeCode','D','CONFG','A','O','Rate class type code for F and A',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','consortiumFnaCostElements','D','CONFG','A','420630;420610','Cost elements considered to be consortium F and A',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetUnrecoveredFandAHelpUrl','D','HELP','A','default.htm?turl=Documents/unrecoveredfapanel.htm','Budget Unrecovered F and A Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','defaultOverheadRateTypeCode','D','CONFG','A','1','The overhead rate type a new Budget should default to',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetPersonDetailsDefaultPeriodType','D','CONFG','A','3','The Period Type of a newly budgeted Person should default to',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetRateClassHelpUrl','D','HELP','A','default.htm?turl=Documents%2Frateclassessubpanelontheobjectcodenamesubpanel.htm','Rate Class Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetPeriodHelpUrl','D','HELP','A','default.htm?turl=Documents/budgetperiodstotalspanel.htm','Budget Period Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetStatusHelpUrl','D','HELP','A','default.htm?turl=Documents/budgetstatus.htm','Budget Status Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetActionsHelp','D','HELP','A','default.htm?turl=Documents/budgetactionstab.htm','Budget Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetModularBudgetHelp','D','HELP','A','default.htm?turl=Documents/modularbudgettab.htm','Budget Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetDistributionAndIncomeHelp','D','HELP','A','default.htm?turl=Documents/distributionincometab.htm','Budget Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetSummaryHelp','D','HELP','A','default.htm?turl=Documents/summarypanel.htm','Budget Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetCostShareHelpUrl','D','HELP','A','default.htm?turl=Documents/costsharingpanel.htm','Budget Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetLineItemHelpUrl','D','HELP','A','default.htm?turl=Documents/lineitemdetailssubpanel.htm','Budget Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetPersonHelpUrl','D','HELP','A','default.htm?turl=Documents/budgetperiodstotals.htm','Budget Person Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetModularIdcHelpUrl','D','HELP','A','default.htm?turl=Documents/modularbudgetoverviewpanel.htm','Budget Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetDocumentHelp','D','HELP','A','default.htm?turl=Documents/budgetdocument.htm','Budget Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetStatusIncompleteCode','D','CONFG','A','2','Code corresponding to the budget status of Incomplete',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','activityTypeMaintenanceHelp','D','HELP','A','default.htm?turl=Documents/activitytype.htm','Activity Type Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','budgetCategoryMappingMaintenanceHelp','D','HELP','A','default.htm?turl=Documents/budgetcategorymapping.htm','Budget Category Mapping Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','scienceKeywordMaintenanceHelp','D','HELP','A','default.htm?turl=Documents%2Fsciencekeywordmaintenancedocument.htm','Science Keyword Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','investigatorCreditTypeMaintenanceHelp','D','HELP','A','default.htm?turl=Documents/investigatorcredittype.htm','Investigator Credit Type Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','instituteRateMaintenanceHelp','D','HELP','A','default.htm?turl=Documents/instituterate.htm','Institute Rate Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','instituteLaRateMaintenanceHelp','D','HELP','A','default.htm?turl=Documents/institutelarate.htm','Institute La Rate Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','customAttributeDocumentMaintenanceHelp','D','HELP','A','default.htm?turl=Documents/customattribute.htm','Custom Attribute Document Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','customAttributeMaintenanceHelp','D','HELP','A','default.htm?turl=Documents/customattributedocument.htm','Custom Attribute Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','costElementMaintenanceHelp','D','HELP','A','default.htm?turl=Documents/costelement.htm','Cost Element Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','budgetCategoryTypeMaintenanceHelp','D','HELP','A','default.htm?turl=Documents/budgetcategorytype.htm','Budget Category Type Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','budgetCategoryMapMaintenanceHelp','D','HELP','A','default.htm?turl=Documents/budgetcategorymaps.htm','Budget Category Mapping Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','specialReviewApprovalStatusMaintenanceHelp','D','HELP','A','default.htm?turl=Documents%2Fspecialreviewapprovalstatusmaintenancedocument.htm','Special Review Approval Status Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','validSpecialReviewApprovalMaintenanceHelp','D','HELP','A','default.htm?turl=Documents%2Fvalidspecialreviewapprovalmaintenancedocument.htm','Valid Special Review Approval Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','ynqMaintenanceHelp','D','HELP','A','default.htm?turl=Documents%2Fynqmaintenancedocument.htm','YNQ Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','rateTypeMaintenanceHelp','D','HELP','A','default.htm?turl=Documents%2Fratetypemaintenancedocument.htm','Rate Type Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','rateClassTypeMaintenanceHelp','D','HELP','A','default.htm?turl=Documents%2Frateclasstypemaintenancedocument.htm','Rate Class Type Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','rateClassMaintenanceHelp','D','HELP','A','default.htm?turl=Documents%2Frateclassmaintenancedocument.htm','Rate Class Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','proposalTypeMaintenanceHelp','D','HELP','A','default.htm?turl=Documents%2Fproposaltypemaintenancedocument.htm','Proposal Type Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','organizationMaintenanceHelp','D','HELP','A','default.htm?turl=Documents%2Forganization.htm','Organization Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','noticeOfOpportunityMaintenanceHelp','D','HELP','A','default.htm?turl=Documents/noticeofopportunity.htm','Notice of Opportunity Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','degreeTypeMaintenanceHelp','D','HELP','A','default.htm?turl=Documents/degreetype.htm','Degree Type Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','deadlineTypeMaintenanceHelp','D','HELP','A','default.htm?turl=Documents/deadlinetype.htm','Deadline Type Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','budgetStatusMaintenanceHelp','D','HELP','A','default.htm?turl=Documents/budgetstatus.htm','Budget Status Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','abstractTypeMaintenanceHelp','D','HELP','A','default.htm?turl=Documents/abstracttype.htm','Abstract Type Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','stateMaintenanceHelp','D','HELP','A','default.htm?turl=Documents%2Fstatemaintenancedocument.htm','State Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','sponsorMaintenanceHelp','D','HELP','A','default.htm?turl=Documents%2Fsponsormaintenancedocument.htm','Sponsor Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','validCeRateTypeMaintenanceHelp','D','HELP','A','default.htm?turl=Documents%2Fvalidcostelementratetypemaintenancedocument.htm','Valid Cost Element Rate Type Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','validCalcTypeMaintenanceHelp','D','HELP','A','default.htm?turl=Documents%2Fvalidcalculationtypemaintenancedocument.htm','Valid Calculation Type Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','specialReviewApprovalTypeMaintenanceHelp','D','HELP','A','default.htm?turl=Documents%2Fspecialreviewapprovaltypemaintenancedocument.htm','Special Review Approval Type Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','s2sSubmissionTypeMaintenanceHelp','D','HELP','A','default.htm?turl=Documents%2Fs2ssubmissiontypemaintenancedocument.htm','S2S Submission Type Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','s2sRevisionTypeMaintenanceHelp','D','HELP','A','default.htm?turl=Documents%2Fs2srevisiontypemaintenancedocument.htm','S2S Revision Type Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','personEditableFieldMaintenanceHelp','D','HELP','A','default.htm?turl=Documents/persontableeditablecolumns.htm','Person Editable Fields Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','personMaintenanceHelp','D','HELP','A','default.htm?turl=Documents/personmaintenancedocument.htm','Person Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','narrativeTypeMaintenanceHelp','D','HELP','A','default.htm?turl=Documents/narrativetype.htm','Narrative Type Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','narrativeStatusMaintenanceHelp','D','HELP','A','default.htm?turl=Documents/narrativestatus.htm','Narrative Status Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','mailByMaintenanceHelp','D','HELP','A','default.htm?turl=Documents/mailby.htm','Mail By Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','budgetCategoryMaintenanceHelp','D','HELP','A','default.htm?turl=Documents/budgetcategory.htm','Budget Category Maintenance Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','multipleValueLookupResultsPerPage','L','CONFG','A','200','Limit results returned for lookup - multiple results',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposaldevelopment.creditsplit.enabled','D','CONFG','A','Y','Determines whether the Credit Split is turned on for proposal',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-GEN','personrole.nih.kp','A','CONFG','A','Key Person','Description of key person for Non-NIH Proposals',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-GEN','personrole.nih.pi','A','CONFG','A','PI/Contact','Description of principal investigator contact for Non-NIH Proposals',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-GEN','personrole.nih.coi','A','CONFG','A','PI/Multiple','Description of principal investigator multiple for Non-NIH Proposals',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposaldevelopment.proposaltype.new','D','CONFG','A','1','ProposalTypeCode of NEW',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposaldevelopment.displayKeywordPanel','D','CONFG','A','TRUE','Display Proposal Keyword panel',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-GEN','sponsorGroupHierarchyName','A','CONFG','A','Sponsor Groups','Sponsor Group Hierarchy Name',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentPersonHelpUrl','D','HELP','A','default.htm?turl=Documents/personpanel.htm','Person Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentCreditSplitHelpUrl','D','HELP','A','default.htm?turl=Documents/combinedcreditsplitpanel.htm','Credit Split Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentKeywordsHelpUrl','D','HELP','A','default.htm?turl=Documents%2Fkeywordspanel1.htm','Keywords Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentLocationHelpUrl','D','HELP','A','default.htm?turl=Documents/organizationlocationpanel.htm','Location Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentSponsorHelpUrl','D','HELP','A','default.htm?turl=Documents/sponsorprograminformationpanel.htm','Sponsor Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentActionsHelp','D','HELP','A','default.htm?turl=Documents/proposalactionstab.htm','Proposal Development Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentGrantsGovHelp','D','HELP','A','default.htm?turl=Documents/grantsgovtab.htm','Proposal Development Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentProposalHelp','D','HELP','A','default.htm?turl=Documents/proposalpagetab.htm','Proposal Development Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentAbstractsAttachmentsHelp','D','HELP','A','default.htm?turl=Documents/abstractsandattachmentstab.htm','Proposal Development Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentQuestionsHelp','D','HELP','A','default.htm?turl=Documents/questionstab.htm','Proposal Development Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentCustomDataHelp','D','HELP','A','default.htm?turl=Documents%2Fcustomdatatab1.htm','Proposal Development Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentSpecialReviewHelp','D','HELP','A','default.htm?turl=Documents/specialreviewtab.htm','Proposal Development Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentKeyPersonnelHelp','D','HELP','A','default.htm?turl=Documents/keypersonneltab.htm','Proposal Development Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','s2s.submissiontype.application','D','CONFG','A','2','Submission Type of Application',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-GEN','sponsorLevelHierarchy','A','CONFG','A','NIH','Sponsor Level Hierarchy',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentCustomAttributeHelpUrl','D','HELP','A','default.htm?turl=Documents%2Fcustomattributedocument.htm','Custom Attribute Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalUserHelpUrl','D','HELP','A','default.htm?turl=Documents/userspanel.htm','Proposal Development Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentBudgetVersionsHelp','D','HELP','A','default.htm?turl=Documents%2Fbudgetversionspanel.htm','Budget Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','numberPerSponsorHierarchyGroup','A','CONFG','A','300','Number of nodes per sponsor group',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','pessimisticLocking.expirationAge','D','CONFG','A','1440','The expiration timeout in minutes; expired locks are deleted',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','pessimisticLocking.cronExpression','D','CONFG','A','0 0 1 * * ?','The Cron Expression for Quartz to activate a clearing of old locks',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','initialUnitLoadDepth','D','CONFG','A','3','Initial UnitHierarchy Load Depth',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentNarrativeHelpUrl','D','HELP','A','default.htm?turl=Documents/abstractsandattachmentstab.htm','Narrative Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentCopyCriteriaHelpUrl','D','HELP','A','default.htm?turl=Documents/copytonewdocumentpanel.htm','Proposal Copy Criteria Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentOpportunityHelpUrl','D','HELP','A','default.htm?turl=Documents/grantsgovlookup.htm','Grants.gov Opportunity Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentDocumentHelp','D','HELP','A','default.htm?turl=Documents/proposaldevelopmentdocument.htm','Proposal Development Document Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentPermissionsHelp','D','HELP','A','default.htm?turl=Documents/permissionstab.htm','Proposal Development Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentSpecialReviewHelpUrl','D','HELP','A','default.htm?turl=Documents/specialreviewpanel.htm','Special Review Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentProposalTypeHelpUrl','D','HELP','A','default.htm?turl=Documents/requiredfieldsforsavingdocumentpanel.htm','Proposal Type Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentMailByHelpUrl','D','HELP','A','default.htm?turl=Documents/mailby.htm','Mail By Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentYnqHelpUrl','D','HELP','A','default.htm?turl=Documents/proposalquestionspanelexample.htm','Yes/No Questions Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentAbstractHelpUrl','D','HELP','A','default.htm?turl=Documents/abstractspanel.htm','Abstract Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalDevelopmentOrganizationHelpUrl','D','HELP','A','default.htm?turl=Documents/organizationlocationpanel.htm','Organization Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','s2s.submissiontype.changedCorrected','D','CONFG','A','3','SubmissionType of Changed/Corrected',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','s2s.revisiontype.other','D','CONFG','A','E','RevisionType of Other',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-GEN','personrole.readonly.roles','A','CONFG','A','KP','Proposal Person Role Id list for roles that are read-only',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposaldevelopment.proposaltype.continuation','D','CONFG','A','4','ProposalTypeCode of CONTINUATION',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposaldevelopment.proposaltype.revision','D','CONFG','A','5','ProposalTypeCode of REVISION',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposaldevelopment.proposaltype.renewal','D','CONFG','A','3','ProposalTypeCode of RENEWAL',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-GEN','personrole.kp','A','CONFG','A','Key Person','Description of key person for NIH Proposals',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-GEN','personrole.coi','A','CONFG','A','Co-Investigator','Description of co-investigator for NIH Proposals',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-GEN','personrole.pi','A','CONFG','A','Principal Investigator','Description of principal investigator for NIH Proposals',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposalNarrativeTypeGroup','D','CONFG','A','P','Define Narrative Type Group for Proposal Attachments',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','instituteNarrativeTypeGroup','D','CONFG','A','O','Define Narrative Type Group for Institute Attachments',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','deliveryInfoDisplayIndicator','D','CONFG','A','Y','Flag to display delivery infor panel',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetTotalsHelp','D','HELP','A','default.htm?turl=Documents/summarytab.htm','Budget Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetbudgetPersonnelDetailsHelpUrl','D','HELP','A','default.htm?turl=Documents/personneldetailpanel.htm','Budget Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetExpensesHelp','D','HELP','A','default.htm?turl=Documents/expensestabactionbutton.htm','Budget Page Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PROTOCOL','irb.protocol.referenceID1','D','CONFG','A','Reference ID1','Referece id is configurable at impl time',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PROTOCOL','irb.protocol.referenceID2','D','CONFG','A','Reference ID2','Referece id is configurable at impl time',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','mit.idc.validation.enabled','D','CONFG','A','1','MitIdcValidationEnabled is configurable at impl time',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','federalCapitalizationMinimum','D','CONFG','A','0.00','Federal Capitalization Minimum',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','institutionCapitalizationMinimum','D','CONFG','A','0.00','Institution Capitalization Minimum',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 

VALUES ('KUALI','KC-PROTOCOL','protocolPersonTrainingSectionRequired','D','CONFG','A','True','Implementing institution can decide on whether to display training section',1,UUID());
INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','reportClassForPaymentsAndInvoices','D','CONFG','A','6','Report Class For Payments And Invoices',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','contactTypeOther','D','CONFG','A','8','Contact Type Code For Contact Type Other',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','scheduleGenerationPeriodInYearsWhenFrequencyBaseCodeIsFinalExpirationDate','D','CONFG','A','1','Schedule Generation Period In Years When Frequency Base Code Is Final Expiration Date',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PROTOCOL','irb.protocol.billable','D','CONFG','A','Y','Billable is configurable at impl time',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','award.creditsplit.enabled','D','CONFG','A','Y','Determines whether the Credit Split is turned on for Award',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PROTOCOL','irb.protocol.award.linking.enabled','D','CONFG','A','Y','Linking from Award to Protocol Funding source is configurable at impl time',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PROTOCOL','irb.protocol.development.proposal.linking.enabled','D','CONFG','A','Y','Linking from Development Proposal to PROTOCOL Funding source is configurable at impl time',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PROTOCOL','irb.protocol.institute.proposal.linking.enabled','D','CONFG','A','Y','Linking from Institute Proposal to PROTOCOL Funding source is configurable at impl time',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','closeoutReportTypeUserDefined','D','CONFG','A','UD','User Defined Close out Report Type',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','closeoutReportTypeFinancialReport','D','CONFG','A','1','This system parameter maps the CloseoutReportType Financial Report(closeoutReoprtTypeCode=1) with ReportClass Fiscal(reportClassCode=1). If this system parameter is changed - the corresponding values in CloseoutReportType and ReportClass tables should be updated as well.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','closeoutReportTypeTechnical','D','CONFG','A','4','This system parameter maps the CloseoutReportType Technical(closeoutReoprtTypeCode=4) with ReportClass Technical Management(reportClassCode=4). If this system parameter is changed - the corresponding values in CloseoutReportType and ReportClass tables should be updated as well.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','closeoutReportTypePatent','D','CONFG','A','3','This system parameter maps the CloseoutReportType Patent(closeoutReoprtTypeCode=3) with ReportClass Intellectual Property(reportClassCode=3). If this system parameter is changed - the corresponding values in CloseoutReportType and ReportClass tables should be updated as well.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','closeoutReportTypeProperty','D','CONFG','A','2','This system parameter maps the CloseoutReportType Property(closeoutReoprtTypeCode=2) with ReportClass Property(reportClassCode=2). If this system parameter is changed - the corresponding values in CloseoutReportType and ReportClass tables should be updated as well.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','pessimisticLocking.timeout','D','CONFG','A','1440','The expiration timeout in minutes; expired locks are deleted',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','instituteRateClassTypes','A','CONFG','A','E;I;O;V;X','Manages a list of Institute rate class types.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','instituteLaRateClassTypes','A','CONFG','A','Y;L','Manages a list of Institute La rate class types.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','budgetCategoryType.personnel','D','CONFG','A','P','Personnel Budget Category Type',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','PROPOSAL_CONTACT_TYPE','D','CONFG','A','6','Value for Proposal Contact Type',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','MULTI_CAMPUS_ENABLED','D','CONFG','A','0','Flag for enabling/disabling Multicampus',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','DHHS_AGREEMENT','D','CONFG','A','0','Value for DHHS Agreement',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','SCHEDULER_SERVICE_ENABLED','D','CONFG','A','0','Value for enabling s2s polling service',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PROTOCOL','IRB_COMM_SELECTION_DURING_SUBMISSION','D','CONFG','A','O','Implementing institution can decide to allow committee/schedule/reviewers to be selected upon an IRB submission.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-IP','proposalcommenttype.generalcomment','D','CONFG','A','16','Code for General Proposal Comment Type',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-IP','proposalcommenttype.reviewercomment','D','CONFG','A','17','Code for IP Reviewer Proposal Comment Type',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-QUESTIONNAIRE','associateModuleQuestionnairePermission','P','CONFG','A','Modify ProposalDevelopmentDocument:KRA-PD;Modify Protocol:KC-PROTOCOL','List of permissions that are allowed to associate a module with questionnaire.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','GENERIC_SPONSOR_CODE','D','CONFG','A','009800','Generic sponsor code used for printing sponsor form',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposaldevelopment.autogenerate.institutionalproposal','D','CONFG','A','Y','Should an Institutional Proposal be automatically generated',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KR-NS','ALLOW_ENROUTE_BLANKET_APPROVE_WITHOUT_APPROVAL_REQUEST_IND','Document','CONFG','A','N','Controls whether the nervous system will show the blanket approve button to a user who is authorized for blanket approval but is neither the initiator of the particular document nor the recipient of an active, pending, approve action request.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-IP','InstitutionalProposalConstactsHelpUrl','D','HELP','A','default.htm?turl=Documents/institutionalproposaldocument.htm','Institutional Proposal Contacts Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-IP','InstitutionalProposalCustomDataHelpUrl','D','HELP','A','default.htm?turl=Documents/customdatatab2.htm','Institutional Proposal Custom Data Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-IP','InstitutionalProposalSpecialReviewHelpUrl','D','HELP','A','default.htm?turl=Documents/specialreviewtab1.htm','Institutional Proposal Special Review Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-IP','InstitutionalProposalIPReviewHelpUrl','D','HELP','A','default.htm?turl=Documents/intellectualpropertyreviewtab.htm','Institutional Proposal Intellectual Property Review Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-IP','InstitutionalProposalDistributionHelpUrl','D','HELP','A','default.htm?turl=Documents/distributiontab.htm','Institutional Proposal Distribution Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-IP','InstitutionalProposalIPReviewActivityHelpUrl','D','HELP','A','default.htm?turl=Documents/institutionalproposaldocument.htm','Institutional Proposal Intellectual Property Reivew Activity Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','awardHelpUrl','D','HELP','A','default.htm?turl=Documents/awarddocument.htm','Award Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','awardHomeHelp','D','HELP','A','default.htm?turl=Documents/awarddocument.htm','Award Home Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','awardContactsHelp','D','HELP','A','default.htm?turl=Documents/awarddocument.htm','Award Contacts Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','awardCommitmentsHelp','D','HELP','A','default.htm?turl=Documents/commitmentstab.htm','Award Commitments Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','awardTimeAndMoneyHelp','D','HELP','A','default.htm?turl=Documents/timeandmoneysubpanel.htm','Award Time and Money Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','awardPaymentsReportsAndTermsHelp','D','HELP','A','default.htm?turl=Documents/awardpaymentschedulesection.htm','Award Payments Reports and Terms Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','awardSpecialReviewHelp','D','HELP','A','default.htm?turl=Documents/specialreviewpanel.htm','Award Special Review Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','awardCustomDataHelp','D','HELP','A','default.htm?turl=Documents/kcawardtabs.htm','Award Custom Data Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','awardQuestionsHelp','D','HELP','A','default.htm?turl=Documents/awarddocument.htm','Award Questions Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','awardNoteAndAttachmentsHelp','D','HELP','A','default.htm?turl=Documents/commentsnotesattachmentstab.htm','Award Note and Attachments Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','awardActionsHelp','D','HELP','A','default.htm?turl=Documents/awarddocument.htm','Award Actions Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','awardApprovedEquipmentHelpUrl','D','HELP','A','default.htm?turl=Documents/specialapprovalpanel.htm','Award Approved Equipment Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','awardApprovedForeignTravelHelpUrl','D','HELP','A','default.htm?turl=Documents/specialapprovalpanel.htm','Award Approved Foreign Travel Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','awardApprovedSubawardHelpUrl','D','HELP','A','default.htm?turl=Documents/subawardpanel.htm','Award Approved Subaward Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','awardCommentHelpUrl','D','HELP','A','default.htm?turl=Documents/awarddocument.htm','Award Comment Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','awardContactHelpUrl','D','HELP','A','default.htm?turl=Documents/awarddocument.htm','Award Contact Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','awardCostShareHelpUrl','D','HELP','A','default.htm?turl=Documents/awarddocument.htm','Award Cost Share Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','awardCustomDataHelpUrl','D','HELP','A','default.htm?turl=Documents/kcawardtabs.htm','Award Custom Data Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','awardPersonCreditSplitHelpUrl','D','HELP','A','default.htm?turl=Documents/awarddocument.htm','Award Person Credit Split Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','CommentTypeHelpUrl','D','HELP','A','default.htm?turl=Documents/commenttype.htm','Award Comment Type Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','CostShareTypeHelpUrl','D','HELP','A','default.htm?turl=Documents/costsharingtype.htm','Award Cost Share Type Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','awardFandaRateHelpUrl','D','HELP','A','default.htm?turl=Documents/faratessubpanel.htm','Award F and A Rate Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','awardReportTermHelpUrl','D','HELP','A','default.htm?turl=Documents/awarddocument.htm','Award Report Term Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 

VALUES ('KUALI','KC-AWARD','awardCloseoutHelpUrl','D','HELP','A','default.htm?turl=Documents/awarddocument.htm','Award Report Term Help',1,UUID());
INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','awardAttachmentsHelpUrl','D','HELP','A','default.htm?turl=Documents/attachmentspanel.htm','Award Attachments Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-T','timeAndMoneyHelp','D','HELP','A','default.htm?turl=Documents/timeandmoneysubpanel.htm','Time And Money Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-T','TransactionHelp','D','HELP','A','default.htm?turl=Documents/transactionspanel.htm','Transaction Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-T','PendingTransactionHelp','D','HELP','A','default.htm?turl=Documents/transactionspanel.htm','Pending Transaction Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-T','awardHierarchyNodeHelpUrl','D','HELP','A','default.htm?turl=Documents/awardhierarchypanel.htm','Award Hierarchy Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-T','awardHierarchyHelpUrl','D','HELP','A','default.htm?turl=Documents/awardhierarchypanel.htm','Award Hierarchy Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-GEN','permissionsHelpUrl','D','HELP','A','default.htm?turl=Documents/permissionstab.htm','Institutional Proposal Intellectual Property Reivew Activity Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','SCHOOL_NAME','D','CONFG','A','Kuali Coeus','School Name',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','SCHOOL_ACRONYM','D','CONFG','A','KC','School acronym',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','ENABLE_AWD_ANT_OBL_DIRECT_INDIRECT_COST','D','CONFG','A','2143','obligated direct indirect cost',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','FELLOWSHIP_OSP_ADMIN','D','CONFG','A','qucikStart','Fellowship admin name',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','PI_CITIZENSHIP_FROM_CUSTOM_DATA','D','CONFG','A','1','It defines where the citizenship info should fetch from',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','proposalHierarchySubProjectDirectCostElement','D','CONFG','A','PHTD01','The Cost Element to be used for the Direct Cost sub-project summary line items in a Proposal Hierarchy budget',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','proposalHierarchySubProjectIndirectCostElement','D','CONFG','A','PHTID02','The Cost Element to be used for the Indirect Cost sub-project summary line items in a Proposal Hierarchy budget',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','FEDERAL_ID_COMES_FROM_CURRENT_AWARD','D','CONFG','A','N','Determines whether the Grants.Gov Federal ID must be populated from the current award.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','proposaldevelopment.proposaltype.resubmission','D','CONFG','A','2','ProposalTypeCode of RESUBMISSION',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-IP','institutionalproposal.creditsplit.enabled','D','CONFG','A','Y','Determines whether the Credit Split is turned on for Institutional Proposal',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','awardBudgetEbRateClassCode','D','CONFG','A','5','The EB rate class code to be used for award budget if the eb rates are overridden on commitements tab',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','awardBudgetEbRateTypeCode','D','CONFG','A','6','The EB rate type code to be used for award budget if the eb rates are overridden on commitements tab',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','defaultFnARateClassCode','D','CONFG','A','1','The OH rate class code to be used for award budget if the fna rates are overridden on commitements tab',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','AWARD_BUDGET_STATUS_IN_PROGRESS_CODE','D','CONFG','A','1','Default award budget status code',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-B','AWARD_BUDGET_TYPE_NEW_PARAMETER','D','CONFG','A','1','Default award budget type code',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','scope.sync.PAYMENTS_AND_INVOICES_TAB.AwardComment.commentTypeCode','D','CONFG','A','1','Comma delimited list of comment type codes to sync on the Payments and Invoices Tab.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','scope.sync.COMMENTS_TAB.AwardComment.commentTypeCode','D','CONFG','A','2,3,4,5,6','Comma delimited list of comment type codes to sync on the Comments Tab.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','scope.sync.COST_SHARE.AwardComment.commentTypeCode','D','CONFG','A','9','Comma delimited list of comment type codes to sync on the Cost Share Tab.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','scope.sync.PREAWARD_AUTHORIZATIONS_TAB.AwardComment.commentTypeCode','D','CONFG','A','18,19','Comma delimited list of comment type codes to sync on the PreAward Authorizations Tab.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','scope.sync.RATES_TAB.AwardComment.commentTypeCode','D','CONFG','A','20','Comma delimited list of comment type codes to sync on the Rates Tab.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','scope.sync.PAYMENTS_AND_INVOICES_TAB.AwardReportTerm.reportClassCode','D','CONFG','A','6','Comma delimited list of reportClassCodes for reports to sync on the Reports tab.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','scope.sync.REPORTS_TAB.AwardReportTerm.reportClassCode','D','CONFG','A','1,2,3,4,5,7','Comma delimited list of reportClassCodes for reports to sync on the Reports Tab.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AB','awardBudgetStatusInProgress','D','CONFG','A','1','This system parameter maps the AwardBudget status In Progress',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AB','awardBudgetStatusSubmitted','D','CONFG','A','5','This system parameter maps the AwardBudget status Submitted',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AB','awardBudgetStatusRejected','D','CONFG','A','8','This system parameter maps the AwardBudget status Rejected',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AB','awardBudgetStatusToBePosted','D','CONFG','A','10','This system parameter maps the AwardBudget status To Be Posted',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AB','awardBudgetStatusPosted','D','CONFG','A','9','This system parameter maps the AwardBudget status Posted',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AB','awardBudgetStatusErrorInPosting','D','CONFG','A','11','This system parameter maps the AwardBudget status Error In Posting',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AB','AWARD_BUDGET_POST_ENABLED','D','CONFG','A','1','This system parameter enables on demand Award Budget Posting',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-GEN','customAttributeDocumentType','DocumentType','CONFG','A','AWRD=Award;INPR=Institutional Proposal;PRDV=Proposal Development;PROT=Protocol','List of Custom Attribute Document type name.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AB','awardBudgetTypeNew','D','CONFG','A','1','This system parameter maps the AwardBudget type New',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AB','awardBudgetTypeRebudget','D','CONFG','A','2','This system parameter maps the AwardBudget type Rebudget',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','DEFAULT_BIOGRAPHY_DOCUMENT_TYPE_CODE','D','CONFG','A','1','Value of the default biography document type code. This is the document type code that will be used when adding new users to a Proposal Development Document and they have an attached Biosketch file.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AWARD','TXN_TYPE_DEF_COPIED_AWARD','D','CONFG','A','9','New Transaction',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','ACTIVITY_TYPE_CODE_RESEARCH','D','CONFG','A','1','Code corresponding to Activity Type: Research.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','ACTIVITY_TYPE_CODE_INSTRUCTION','D','CONFG','A','2','Code corresponding to Activity Type: Instruction.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','ACTIVITY_TYPE_CODE_PUBLIC_SERVICE','D','CONFG','A','3','Code corresponding to Activity Type: Public Service.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','ACTIVITY_TYPE_CODE_CLINICAL_TRIAL','D','CONFG','A','4','Code corresponding to Activity Type: Clinical Trial.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','ACTIVITY_TYPE_CODE_OTHER','D','CONFG','A','5','Code corresponding to Activity Type: Other.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','ACTIVITY_TYPE_CODE_FELLOWSHIP_PRE_DOCTORAL','D','CONFG','A','6','Code corresponding to Activity Type: Fellowship - Pre-Doctoral.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','ACTIVITY_TYPE_CODE_FELLOWSHIP_POST_DOCTORAL','D','CONFG','A','7','Code corresponding to Activity Type: Fellowship - Post-Doctoral.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','ACTIVITY_TYPE_CODE_STUDENT_SERVICES','D','CONFG','A','8','Code corresponding to Activity Type: Student Services.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','ACTIVITY_TYPE_CODE_CONSTRUCTION','D','CONFG','A','9','Code corresponding to Activity Type: Construction.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','S2S_SUBMISSION_TYPE_CODE_PREAPPLICATION','D','CONFG','A','1','Code corresponding to S2S Submission Type: Pre-Application.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','S2S_SUBMISSION_TYPE_CODE_APPLICATION','D','CONFG','A','2','Code corresponding to S2S Submission Type: Application.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','S2S_SUBMISSION_TYPE_CODE_CHANGE_CORRECTED_APPLICATION','D','CONFG','A','3','Code corresponding to S2S Submission Type: Change/Corrected Application.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','PROPOSAL_TYPE_CODE_NEW','D','CONFG','A','1','Code corresponding to Proposal Type: New.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','PROPOSAL_TYPE_CODE_RESUBMISSION','D','CONFG','A','2','Code corresponding to Proposal Type: Resubmission.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','PROPOSAL_TYPE_CODE_RENEWAL','D','CONFG','A','3','Code corresponding to Proposal Type: Renewal.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','PROPOSAL_TYPE_CODE_CONTINUATION','D','CONFG','A','4','Code corresponding to Proposal Type: Continuation.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','PROPOSAL_TYPE_CODE_REVISION','D','CONFG','A','5','Code corresponding to Proposal Type: Revision.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','PROPOSAL_TYPE_CODE_TASK_ORDER','D','CONFG','A','6','Code corresponding to Proposal Type: Task Order.',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-PD','s2sschedulercronExpressionstarttime','D','CONFG','A','01-JAN-2010 01:00 AM','Start Time expression for the S2S Polling Process. The S2S Polling Process will only start if this parameters date is before today. Must be formatted as "dd-MMM-yyyy hh:mm a". For example "01-JAN-2010 01:00 AM".',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-AB','awardBudgetStatusDoNotPost','D','CONFG','A','12','This system parameter maps the AwardBudget status Do Not Post',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-IP','InstitutionalProposalActionsHelpUrl','D','HELP','A','default.htm?turl=Documents/institutionalproposalactionstab.htm','Institutional Proposal Actions Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-IP','InstitutionalProposalHelpUrl','D','HELP','A','default.htm?turl=Documents/institutionalproposaldocument.htm','Institutional Proposal Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-IP','InstitutionalProposalUnitAdministratorHelpUrl','D','HELP','A','default.htm?turl=Documents/institutionalproposaldocument.htm','Institutional Proposal Unit Administrator Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','sponsorHierarchyHelp','D','HELP','A','default.htm?turl=Documents%2Fsponsorhierarchy.htm','Sponsor Hierarchy Help',1,UUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_NM,PARM_DTL_TYP_CD,PARM_TYP_CD,CONS_CD,TXT,PARM_DESC_TXT,VER_NBR,OBJ_ID) 
VALUES ('KUALI','KC-M','sponsorHierarchyCreateNewHelp','D','HELP','A','default.htm?turl=Documents%2Fsponsorhierarchy.htm','Sponsor Hierarchy Help',1,UUID());

INSERT INTO KRNS_CAMPUS_T (ACTV_IND,CAMPUS_CD,CAMPUS_NM,CAMPUS_SHRT_NM,CAMPUS_TYP_CD,OBJ_ID,VER_NBR)
  VALUES ('Y','UN','UNIVERSITY','UNIVERSITY','B',UUID(),1);

COMMIT;
