INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_DTL_TYP_CD,PARM_NM,PARM_TYP_CD,TXT,PARM_DESC_TXT,CONS_CD,OBJ_ID)
  VALUES ('KC','KC-GEN','All','MULTI_CAMPUS_ENABLED','CONFG','N','Enables or disables Multi-Campus mode','A',SYS_GUID());

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD,NMSPC_CD,PARM_DTL_TYP_CD,PARM_NM,TXT,PARM_DESC_TXT,PARM_TYP_CD,CONS_CD,OBJ_ID,VER_NBR) 
  VALUES ('KC','KC-PROTOCOL','Document','protocolAttachmentDefaultSort','ATTP','Default sort for protocol attachments','CONFG','A',SYS_GUID(),'1');

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD, NMSPC_CD, PARM_DTL_TYP_CD, PARM_NM, VER_NBR, PARM_TYP_CD, TXT, PARM_DESC_TXT, CONS_CD, OBJ_ID)
  VALUES ('KC', 'KC-GEN', 'A', 'PERMANENT_RESIDENT_OF_US_PENDING', 1, 'CONFG', 4, 'Permanent Resident of U.S. Pending', 'A', SYS_GUID());

update KRNS_PARM_T set TXT='6' where PARM_NM='budgetPersonDefaultAppointmentType';

update krns_parm_t set txt='1,9' where parm_nm='scope.sync.PAYMENTS_AND_INVOICES_TAB.AwardComment.commentTypeCode';

INSERT INTO KRNS_PARM_T (APPL_NMSPC_CD, NMSPC_CD, PARM_DTL_TYP_CD, PARM_NM, VER_NBR, PARM_TYP_CD, TXT, PARM_DESC_TXT, CONS_CD, OBJ_ID)
  VALUES ('KC', 'KC-GEN', 'A', 'ALLOW_PROPOSAL_PERSON_TO_OVERRIDE_KC_PERSON_EXTENDED_ATTRIBUTES', 1, 'CONFG', 'Y', 'If Y then the proposal person citizenship type is used, if N then the kc extended attributes citizenship type is used', 'A', SYS_GUID());