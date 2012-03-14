/* insert the data in the various entity tables and the principal table to create the coi admin user */
INSERT INTO KRIM_ENTITY_T (ENTITY_ID, ACTV_IND, LAST_UPDT_DT,OBJ_ID, VER_NBR) 
    VALUES (KRIM_ENTITY_ID_S.NEXTVAL, 'Y', SYSDATE,SYS_GUID(), 1)
/

INSERT INTO KRIM_ENTITY_NM_T (ENTITY_NM_ID, ENTITY_ID, FIRST_NM, LAST_NM, NM_TYP_CD, DFLT_IND, ACTV_IND, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ENTITY_NM_ID_S.NEXTVAL, KRIM_ENTITY_ID_S.CURRVAL, 'COI', 'Admin', 'PRFR', 'Y', 'Y', SYSDATE, SYS_GUID(), 1)
/

INSERT INTO KRIM_PRNCPL_T (PRNCPL_ID, ENTITY_ID, PRNCPL_NM, PRNCPL_PSWD, ACTV_IND, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES ('10000000121', KRIM_ENTITY_ID_S.CURRVAL, 'coiadmin', 'fK69ATFsAydwQuteang+xMva+Tc=', 'Y', SYSDATE, SYS_GUID(), 1)
/

INSERT INTO KRIM_ENTITY_ENT_TYP_T (ENTITY_ID, ENT_TYP_CD, ACTV_IND, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ENTITY_ID_S.CURRVAL, 'PERSON', 'Y', SYSDATE, SYS_GUID(), 1)
/

INSERT INTO KRIM_ENTITY_ADDR_T (ENTITY_ADDR_ID, ENTITY_ID, ENT_TYP_CD, ADDR_TYP_CD, ADDR_LINE_1, CITY, STATE_PVC_CD, POSTAL_CD, POSTAL_CNTRY_CD, DFLT_IND, ACTV_IND, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ENTITY_ADDR_ID_S.NEXTVAL, KRIM_ENTITY_ID_S.CURRVAL, 'PERSON', 'WRK', '2222 Kuali Drive', 'Coeus', 'MA', '53421', 'US', 'Y', 'Y', SYSDATE, SYS_GUID(), 1)
/

INSERT INTO KRIM_ENTITY_EMAIL_T (ENTITY_EMAIL_ID, ENTITY_ID, ENT_TYP_CD, EMAIL_TYP_CD, EMAIL_ADDR, DFLT_IND, ACTV_IND, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ENTITY_EMAIL_ID_S.NEXTVAL, KRIM_ENTITY_ID_S.CURRVAL, 'PERSON', 'WRK', 'coiadmin@kuali.org', 'Y', 'Y', SYSDATE, SYS_GUID(), 1)
/

INSERT INTO KRIM_ENTITY_EMP_INFO_T (ENTITY_EMP_ID, ENTITY_ID, ENTITY_AFLTN_ID, EMP_REC_ID, EMP_ID, EMP_STAT_CD, EMP_TYP_CD, BASE_SLRY_AMT, PRMRY_DEPT_CD, PRMRY_IND, ACTV_IND, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ENTITY_EMP_ID_S.NEXTVAL, KRIM_ENTITY_ID_S.CURRVAL, (SELECT ENTITY_AFLTN_ID FROM KRIM_ENTITY_AFLTN_T WHERE ENTITY_ID = (SELECT ENTITY_ID FROM KRIM_ENTITY_NM_T WHERE FIRST_NM = 'COI' AND LAST_NM = 'Admin')), '1', (SELECT PRNCPL_NM FROM KRIM_PRNCPL_T WHERE ENTITY_ID = (SELECT ENTITY_ID FROM KRIM_ENTITY_NM_T WHERE FIRST_NM = 'COI' AND LAST_NM = 'Admin')),'A','P',100000,'000001','Y','Y',SYSDATE,SYS_GUID(),1)
/

INSERT INTO KRIM_ENTITY_PHONE_T (ENTITY_PHONE_ID, ENTITY_ID, ENT_TYP_CD, PHONE_TYP_CD, PHONE_NBR, POSTAL_CNTRY_CD, DFLT_IND, ACTV_IND, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ENTITY_PHONE_ID_S.NEXTVAL, KRIM_ENTITY_ID_S.CURRVAL, 'PERSON', 'WRK', '321-321-2282', null, 'Y', 'Y', SYSDATE,SYS_GUID(), 1)
/

INSERT INTO KRIM_ENTITY_PHONE_T (ENTITY_PHONE_ID, ENTITY_ID,ENT_TYP_CD, PHONE_TYP_CD, PHONE_NBR,POSTAL_CNTRY_CD, DFLT_IND, ACTV_IND, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ENTITY_PHONE_ID_S.NEXTVAL, KRIM_ENTITY_ID_S.CURRVAL, 'PERSON', 'FAX', '321-321-2283', null, 'N', 'Y', SYSDATE, SYS_GUID(), 1)
/

/* insert the role-member data to create the association between the pre-existing coi admin role and coiadmin user created above */
INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, ROLE_ID, MBR_ID, MBR_TYP_CD, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ROLE_MBR_ID_S.NEXTVAL, (SELECT ROLE_ID FROM KRIM_ROLE_T WHERE NMSPC_CD = 'KC-COIDISCLOSURE' AND ROLE_NM = 'COI Administrator'), (SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'coiadmin'), 'P', SYSDATE, SYS_GUID(), 1)
/
INSERT INTO KRIM_ROLE_MBR_ATTR_DATA_T (ATTR_DATA_ID, ROLE_MBR_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ATTR_DATA_ID_S.NEXTVAL, KRIM_ROLE_MBR_ID_S.CURRVAL, (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'UnitHierarchy'), (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'unitNumber'), '000001', SYS_GUID(), 1)
/
INSERT INTO KRIM_ROLE_MBR_ATTR_DATA_T (ATTR_DATA_ID, ROLE_MBR_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ATTR_DATA_ID_S.NEXTVAL, KRIM_ROLE_MBR_ID_S.CURRVAL, (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'UnitHierarchy'), (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'subunits'), 'Y', SYS_GUID(), 1)
/



/* insert the data in the various entity tables and the principal table to create the coireviewer user */
INSERT INTO KRIM_ENTITY_T (ENTITY_ID, ACTV_IND, LAST_UPDT_DT,OBJ_ID, VER_NBR) 
    VALUES (KRIM_ENTITY_ID_S.NEXTVAL, 'Y', SYSDATE,SYS_GUID(), 1)
/

INSERT INTO KRIM_ENTITY_NM_T (ENTITY_NM_ID, ENTITY_ID, FIRST_NM, LAST_NM, NM_TYP_CD, DFLT_IND, ACTV_IND, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ENTITY_NM_ID_S.NEXTVAL, KRIM_ENTITY_ID_S.CURRVAL, 'COI', 'Reviewer', 'PRFR', 'Y', 'Y', SYSDATE, SYS_GUID(), 1)
/

INSERT INTO KRIM_PRNCPL_T (PRNCPL_ID, ENTITY_ID, PRNCPL_NM, PRNCPL_PSWD, ACTV_IND, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES ('10000000122', KRIM_ENTITY_ID_S.CURRVAL, 'coireviewer', 'fK69ATFsAydwQuteang+xMva+Tc=', 'Y', SYSDATE, SYS_GUID(), 1)
/

INSERT INTO KRIM_ENTITY_ENT_TYP_T (ENTITY_ID, ENT_TYP_CD, ACTV_IND, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ENTITY_ID_S.CURRVAL, 'PERSON', 'Y', SYSDATE, SYS_GUID(), 1)
/

INSERT INTO KRIM_ENTITY_ADDR_T (ENTITY_ADDR_ID, ENTITY_ID, ENT_TYP_CD, ADDR_TYP_CD, ADDR_LINE_1, CITY, STATE_PVC_CD, POSTAL_CD, POSTAL_CNTRY_CD, DFLT_IND, ACTV_IND, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ENTITY_ADDR_ID_S.NEXTVAL, KRIM_ENTITY_ID_S.CURRVAL, 'PERSON', 'WRK', '2223 Kuali Drive', 'Coeus', 'MA', '53421', 'US', 'Y', 'Y', SYSDATE, SYS_GUID(), 1)
/

INSERT INTO KRIM_ENTITY_EMAIL_T (ENTITY_EMAIL_ID, ENTITY_ID, ENT_TYP_CD, EMAIL_TYP_CD, EMAIL_ADDR, DFLT_IND, ACTV_IND, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ENTITY_EMAIL_ID_S.NEXTVAL, KRIM_ENTITY_ID_S.CURRVAL, 'PERSON', 'WRK', 'coireviewer@kuali.org', 'Y', 'Y', SYSDATE, SYS_GUID(), 1)
/

INSERT INTO KRIM_ENTITY_EMP_INFO_T (ENTITY_EMP_ID, ENTITY_ID, ENTITY_AFLTN_ID, EMP_REC_ID, EMP_ID, EMP_STAT_CD, EMP_TYP_CD, BASE_SLRY_AMT, PRMRY_DEPT_CD, PRMRY_IND, ACTV_IND, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ENTITY_EMP_ID_S.NEXTVAL, KRIM_ENTITY_ID_S.CURRVAL, (SELECT ENTITY_AFLTN_ID FROM KRIM_ENTITY_AFLTN_T WHERE ENTITY_ID = (SELECT ENTITY_ID FROM KRIM_ENTITY_NM_T WHERE FIRST_NM = 'COI' AND LAST_NM = 'Reviewer')), '1', (SELECT PRNCPL_NM FROM KRIM_PRNCPL_T WHERE ENTITY_ID = (SELECT ENTITY_ID FROM KRIM_ENTITY_NM_T WHERE FIRST_NM = 'COI' AND LAST_NM = 'Reviewer')),'A','P',100000,'000001','Y','Y',SYSDATE,SYS_GUID(),1)
/

INSERT INTO KRIM_ENTITY_PHONE_T (ENTITY_PHONE_ID, ENTITY_ID, ENT_TYP_CD, PHONE_TYP_CD, PHONE_NBR, POSTAL_CNTRY_CD, DFLT_IND, ACTV_IND, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ENTITY_PHONE_ID_S.NEXTVAL, KRIM_ENTITY_ID_S.CURRVAL, 'PERSON', 'WRK', '321-321-2284', null, 'Y', 'Y', SYSDATE,SYS_GUID(), 1)
/

INSERT INTO KRIM_ENTITY_PHONE_T (ENTITY_PHONE_ID, ENTITY_ID,ENT_TYP_CD, PHONE_TYP_CD, PHONE_NBR,POSTAL_CNTRY_CD, DFLT_IND, ACTV_IND, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ENTITY_PHONE_ID_S.NEXTVAL, KRIM_ENTITY_ID_S.CURRVAL, 'PERSON', 'FAX', '321-321-2285', null, 'N', 'Y', SYSDATE, SYS_GUID(), 1)
/

/* insert the role-member data to create the association between the pre-existing coi reviewer role and coireviewer user created above */
INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, ROLE_ID, MBR_ID, MBR_TYP_CD, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ROLE_MBR_ID_S.NEXTVAL, (SELECT ROLE_ID FROM KRIM_ROLE_T WHERE NMSPC_CD = 'KC-COIDISCLOSURE' AND ROLE_NM = 'COI Reviewer'), (SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'coireviewer'), 'P', SYSDATE, SYS_GUID(), 1)
/
INSERT INTO KRIM_ROLE_MBR_ATTR_DATA_T (ATTR_DATA_ID, ROLE_MBR_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ATTR_DATA_ID_S.NEXTVAL, KRIM_ROLE_MBR_ID_S.CURRVAL, (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'UnitHierarchy'), (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'unitNumber'), '000001', SYS_GUID(), 1)
/
INSERT INTO KRIM_ROLE_MBR_ATTR_DATA_T (ATTR_DATA_ID, ROLE_MBR_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ATTR_DATA_ID_S.NEXTVAL, KRIM_ROLE_MBR_ID_S.CURRVAL, (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'UnitHierarchy'), (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'subunits'), 'Y', SYS_GUID(), 1)
/



/* insert the data in the various entity tables and the principal table to create the coireporter user */
INSERT INTO KRIM_ENTITY_T (ENTITY_ID, ACTV_IND, LAST_UPDT_DT,OBJ_ID, VER_NBR) 
    VALUES (KRIM_ENTITY_ID_S.NEXTVAL, 'Y', SYSDATE,SYS_GUID(), 1)
/

INSERT INTO KRIM_ENTITY_NM_T (ENTITY_NM_ID, ENTITY_ID, FIRST_NM, LAST_NM, NM_TYP_CD, DFLT_IND, ACTV_IND, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ENTITY_NM_ID_S.NEXTVAL, KRIM_ENTITY_ID_S.CURRVAL, 'COI', 'Reporter', 'PRFR', 'Y', 'Y', SYSDATE, SYS_GUID(), 1)
/

INSERT INTO KRIM_PRNCPL_T (PRNCPL_ID, ENTITY_ID, PRNCPL_NM, PRNCPL_PSWD, ACTV_IND, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES ('10000000123', KRIM_ENTITY_ID_S.CURRVAL, 'coireporter', 'fK69ATFsAydwQuteang+xMva+Tc=', 'Y', SYSDATE, SYS_GUID(), 1)
/

INSERT INTO KRIM_ENTITY_ENT_TYP_T (ENTITY_ID, ENT_TYP_CD, ACTV_IND, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ENTITY_ID_S.CURRVAL, 'PERSON', 'Y', SYSDATE, SYS_GUID(), 1)
/

INSERT INTO KRIM_ENTITY_ADDR_T (ENTITY_ADDR_ID, ENTITY_ID, ENT_TYP_CD, ADDR_TYP_CD, ADDR_LINE_1, CITY, STATE_PVC_CD, POSTAL_CD, POSTAL_CNTRY_CD, DFLT_IND, ACTV_IND, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ENTITY_ADDR_ID_S.NEXTVAL, KRIM_ENTITY_ID_S.CURRVAL, 'PERSON', 'WRK', '2224 Kuali Drive', 'Coeus', 'MA', '53421', 'US', 'Y', 'Y', SYSDATE, SYS_GUID(), 1)
/

INSERT INTO KRIM_ENTITY_EMAIL_T (ENTITY_EMAIL_ID, ENTITY_ID, ENT_TYP_CD, EMAIL_TYP_CD, EMAIL_ADDR, DFLT_IND, ACTV_IND, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ENTITY_EMAIL_ID_S.NEXTVAL, KRIM_ENTITY_ID_S.CURRVAL, 'PERSON', 'WRK', 'coireporter@kuali.org', 'Y', 'Y', SYSDATE, SYS_GUID(), 1)
/

INSERT INTO KRIM_ENTITY_EMP_INFO_T (ENTITY_EMP_ID, ENTITY_ID, ENTITY_AFLTN_ID, EMP_REC_ID, EMP_ID, EMP_STAT_CD, EMP_TYP_CD, BASE_SLRY_AMT, PRMRY_DEPT_CD, PRMRY_IND, ACTV_IND, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ENTITY_EMP_ID_S.NEXTVAL, KRIM_ENTITY_ID_S.CURRVAL, (SELECT ENTITY_AFLTN_ID FROM KRIM_ENTITY_AFLTN_T WHERE ENTITY_ID = (SELECT ENTITY_ID FROM KRIM_ENTITY_NM_T WHERE FIRST_NM = 'COI' AND LAST_NM = 'Reporter')), '1', (SELECT PRNCPL_NM FROM KRIM_PRNCPL_T WHERE ENTITY_ID = (SELECT ENTITY_ID FROM KRIM_ENTITY_NM_T WHERE FIRST_NM = 'COI' AND LAST_NM = 'Reporter')),'A','P',100000,'000001','Y','Y',SYSDATE,SYS_GUID(),1)
/

INSERT INTO KRIM_ENTITY_PHONE_T (ENTITY_PHONE_ID, ENTITY_ID, ENT_TYP_CD, PHONE_TYP_CD, PHONE_NBR, POSTAL_CNTRY_CD, DFLT_IND, ACTV_IND, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ENTITY_PHONE_ID_S.NEXTVAL, KRIM_ENTITY_ID_S.CURRVAL, 'PERSON', 'WRK', '321-321-2286', null, 'Y', 'Y', SYSDATE,SYS_GUID(), 1)
/

INSERT INTO KRIM_ENTITY_PHONE_T (ENTITY_PHONE_ID, ENTITY_ID,ENT_TYP_CD, PHONE_TYP_CD, PHONE_NBR,POSTAL_CNTRY_CD, DFLT_IND, ACTV_IND, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ENTITY_PHONE_ID_S.NEXTVAL, KRIM_ENTITY_ID_S.CURRVAL, 'PERSON', 'FAX', '321-321-2287', null, 'N', 'Y', SYSDATE, SYS_GUID(), 1)
/

/* insert the role-member data to create the association between the pre-existing coi reporter role and coireporter user created above */
INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, ROLE_ID, MBR_ID, MBR_TYP_CD, LAST_UPDT_DT, OBJ_ID, VER_NBR) 
    VALUES (KRIM_ROLE_MBR_ID_S.NEXTVAL, (SELECT ROLE_ID FROM KRIM_ROLE_T WHERE NMSPC_CD = 'KC-COIDISCLOSURE' AND ROLE_NM = 'COI Reporter'), (SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'coireporter'), 'P', SYSDATE, SYS_GUID(), 1)
/
