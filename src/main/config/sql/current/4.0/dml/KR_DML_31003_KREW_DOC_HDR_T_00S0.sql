INSERT INTO KREW_DOC_HDR_T (DOC_HDR_ID,DOC_TYP_ID,DOC_HDR_STAT_CD,TTL,INITR_PRNCPL_ID,RTE_PRNCPL_ID,DOC_VER_NBR,RTE_LVL,CRTE_DT,RTE_STAT_MDFN_DT,APRV_DT,FNL_DT,STAT_MDFN_DT,OBJ_ID,VER_NBR) 
    VALUES (4091,(SELECT MAX(DOC_TYP_ID) FROM KREW_DOC_TYP_T WHERE DOC_TYP_NM = 'AwardDocument'),'S','KC Award - *****PLACEHOLDER*****',(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),null,1,0,SYSDATE,SYSDATE,null,null,SYSDATE,SYS_GUID(),12)
/
INSERT INTO KREW_DOC_HDR_T (DOC_HDR_ID,DOC_TYP_ID,DOC_HDR_STAT_CD,TTL,INITR_PRNCPL_ID,RTE_PRNCPL_ID,DOC_VER_NBR,RTE_LVL,CRTE_DT,RTE_STAT_MDFN_DT,APRV_DT,FNL_DT,STAT_MDFN_DT,OBJ_ID,VER_NBR) 
    VALUES (4092,(SELECT MAX(DOC_TYP_ID) FROM KREW_DOC_TYP_T WHERE DOC_TYP_NM = 'AwardDocument'),'F','KC Award - Sync Descendants, 300120',(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),1,4,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYS_GUID(),63)
/
INSERT INTO KREW_DOC_HDR_T (DOC_HDR_ID,DOC_TYP_ID,DOC_HDR_STAT_CD,TTL,INITR_PRNCPL_ID,RTE_PRNCPL_ID,DOC_VER_NBR,RTE_LVL,CRTE_DT,RTE_STAT_MDFN_DT,APRV_DT,FNL_DT,STAT_MDFN_DT,OBJ_ID,VER_NBR) 
    VALUES (4093,(SELECT MAX(DOC_TYP_ID) FROM KREW_DOC_TYP_T WHERE DOC_TYP_NM = 'AwardDocument'),'F','KC Award - Sync Descendants, 300120',(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),1,4,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYS_GUID(),54)
/
INSERT INTO KREW_DOC_HDR_T (DOC_HDR_ID,DOC_TYP_ID,DOC_HDR_STAT_CD,TTL,INITR_PRNCPL_ID,RTE_PRNCPL_ID,DOC_VER_NBR,RTE_LVL,CRTE_DT,RTE_STAT_MDFN_DT,APRV_DT,FNL_DT,STAT_MDFN_DT,OBJ_ID,VER_NBR) 
    VALUES (4094,(SELECT MAX(DOC_TYP_ID) FROM KREW_DOC_TYP_T WHERE DOC_TYP_NM = 'AwardDocument'),'F','KC Award - Sync Descendants, 300120',(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),1,4,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYS_GUID(),60)
/
INSERT INTO KREW_DOC_HDR_T (DOC_HDR_ID,DOC_TYP_ID,DOC_HDR_STAT_CD,TTL,INITR_PRNCPL_ID,RTE_PRNCPL_ID,DOC_VER_NBR,RTE_LVL,CRTE_DT,RTE_STAT_MDFN_DT,APRV_DT,FNL_DT,STAT_MDFN_DT,OBJ_ID,VER_NBR) 
    VALUES (4095,(SELECT MAX(DOC_TYP_ID) FROM KREW_DOC_TYP_T WHERE DOC_TYP_NM = 'AwardDocument'),'F','KC Award - Sync Descendants, 300120',(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),1,4,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYS_GUID(),57)
/
INSERT INTO KREW_DOC_HDR_T (DOC_HDR_ID,DOC_TYP_ID,DOC_HDR_STAT_CD,TTL,INITR_PRNCPL_ID,RTE_PRNCPL_ID,DOC_VER_NBR,RTE_LVL,CRTE_DT,RTE_STAT_MDFN_DT,APRV_DT,FNL_DT,STAT_MDFN_DT,OBJ_ID,VER_NBR) 
    VALUES (4096,(SELECT MAX(DOC_TYP_ID) FROM KREW_DOC_TYP_T WHERE DOC_TYP_NM = 'AwardDocument'),'F','KC Award - Sync Descendants, 300120',(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),1,4,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYS_GUID(),54)
/
INSERT INTO KREW_DOC_HDR_T (DOC_HDR_ID,DOC_TYP_ID,DOC_HDR_STAT_CD,TTL,INITR_PRNCPL_ID,RTE_PRNCPL_ID,DOC_VER_NBR,RTE_LVL,CRTE_DT,RTE_STAT_MDFN_DT,APRV_DT,FNL_DT,STAT_MDFN_DT,OBJ_ID,VER_NBR) 
    VALUES (4097,(SELECT MAX(DOC_TYP_ID) FROM KREW_DOC_TYP_T WHERE DOC_TYP_NM = 'AwardDocument'),'F','KC Award - Sync Descendants, 300120',(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),1,4,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYS_GUID(),54)
/
INSERT INTO KREW_DOC_HDR_T (DOC_HDR_ID,DOC_TYP_ID,DOC_HDR_STAT_CD,TTL,INITR_PRNCPL_ID,RTE_PRNCPL_ID,DOC_VER_NBR,RTE_LVL,CRTE_DT,RTE_STAT_MDFN_DT,APRV_DT,FNL_DT,STAT_MDFN_DT,OBJ_ID,VER_NBR) 
    VALUES (4098,(SELECT MAX(DOC_TYP_ID) FROM KREW_DOC_TYP_T WHERE DOC_TYP_NM = 'AwardDocument'),'F','KC Award - Sync Descendants, 300120',(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),1,4,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYS_GUID(),63)
/
INSERT INTO KREW_DOC_HDR_T (DOC_HDR_ID,DOC_TYP_ID,DOC_HDR_STAT_CD,TTL,INITR_PRNCPL_ID,RTE_PRNCPL_ID,DOC_VER_NBR,RTE_LVL,CRTE_DT,RTE_STAT_MDFN_DT,APRV_DT,FNL_DT,STAT_MDFN_DT,OBJ_ID,VER_NBR) 
    VALUES (4099,(SELECT MAX(DOC_TYP_ID) FROM KREW_DOC_TYP_T WHERE DOC_TYP_NM = 'AwardDocument'),'F','KC Award - Sync Descendants, 300120',(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),1,4,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYS_GUID(),54)
/
INSERT INTO KREW_DOC_HDR_T (DOC_HDR_ID,DOC_TYP_ID,DOC_HDR_STAT_CD,TTL,INITR_PRNCPL_ID,RTE_PRNCPL_ID,DOC_VER_NBR,RTE_LVL,CRTE_DT,RTE_STAT_MDFN_DT,APRV_DT,FNL_DT,STAT_MDFN_DT,OBJ_ID,VER_NBR) 
    VALUES (4100,(SELECT MAX(DOC_TYP_ID) FROM KREW_DOC_TYP_T WHERE DOC_TYP_NM = 'AwardDocument'),'F','KC Award - Sync Descendants, 300120',(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),1,4,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYS_GUID(),51)
/
INSERT INTO KREW_DOC_HDR_T (DOC_HDR_ID,DOC_TYP_ID,DOC_HDR_STAT_CD,TTL,INITR_PRNCPL_ID,RTE_PRNCPL_ID,DOC_VER_NBR,RTE_LVL,CRTE_DT,RTE_STAT_MDFN_DT,APRV_DT,FNL_DT,STAT_MDFN_DT,OBJ_ID,VER_NBR) 
    VALUES (4101,(SELECT MAX(DOC_TYP_ID) FROM KREW_DOC_TYP_T WHERE DOC_TYP_NM = 'AwardDocument'),'F','KC Award - Sync Descendants, 300120',(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),1,6,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYS_GUID(),53)
/
INSERT INTO KREW_DOC_HDR_T (DOC_HDR_ID,DOC_TYP_ID,DOC_HDR_STAT_CD,TTL,INITR_PRNCPL_ID,RTE_PRNCPL_ID,DOC_VER_NBR,RTE_LVL,CRTE_DT,RTE_STAT_MDFN_DT,APRV_DT,FNL_DT,STAT_MDFN_DT,OBJ_ID,VER_NBR) 
    VALUES (4102,(SELECT MAX(DOC_TYP_ID) FROM KREW_DOC_TYP_T WHERE DOC_TYP_NM = 'AwardDocument'),'F','KC Award - Created by Award 010002-00001 Ver 2',(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),1,2,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYS_GUID(),25)
/
INSERT INTO KREW_DOC_HDR_T (DOC_HDR_ID,DOC_TYP_ID,DOC_HDR_STAT_CD,TTL,INITR_PRNCPL_ID,RTE_PRNCPL_ID,DOC_VER_NBR,RTE_LVL,CRTE_DT,RTE_STAT_MDFN_DT,APRV_DT,FNL_DT,STAT_MDFN_DT,OBJ_ID,VER_NBR) 
    VALUES (4103,(SELECT MAX(DOC_TYP_ID) FROM KREW_DOC_TYP_T WHERE DOC_TYP_NM = 'AwardDocument'),'F','KC Award - Created by Award 010002-00001 Ver 2',(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),1,2,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYS_GUID(),25)
/
INSERT INTO KREW_DOC_HDR_T (DOC_HDR_ID,DOC_TYP_ID,DOC_HDR_STAT_CD,TTL,INITR_PRNCPL_ID,RTE_PRNCPL_ID,DOC_VER_NBR,RTE_LVL,CRTE_DT,RTE_STAT_MDFN_DT,APRV_DT,FNL_DT,STAT_MDFN_DT,OBJ_ID,VER_NBR) 
    VALUES (4104,(SELECT MAX(DOC_TYP_ID) FROM KREW_DOC_TYP_T WHERE DOC_TYP_NM = 'AwardDocument'),'F','KC Award - Created by Award 010002-00001 Ver 2',(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),1,2,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYS_GUID(),25)
/
INSERT INTO KREW_DOC_HDR_T (DOC_HDR_ID,DOC_TYP_ID,DOC_HDR_STAT_CD,TTL,INITR_PRNCPL_ID,RTE_PRNCPL_ID,DOC_VER_NBR,RTE_LVL,CRTE_DT,RTE_STAT_MDFN_DT,APRV_DT,FNL_DT,STAT_MDFN_DT,OBJ_ID,VER_NBR) 
    VALUES (4105,(SELECT MAX(DOC_TYP_ID) FROM KREW_DOC_TYP_T WHERE DOC_TYP_NM = 'AwardDocument'),'F','KC Award - Created by Award 010002-00001 Ver 2',(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),1,2,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYS_GUID(),25)
/
INSERT INTO KREW_DOC_HDR_T (DOC_HDR_ID,DOC_TYP_ID,DOC_HDR_STAT_CD,TTL,INITR_PRNCPL_ID,RTE_PRNCPL_ID,DOC_VER_NBR,RTE_LVL,CRTE_DT,RTE_STAT_MDFN_DT,APRV_DT,FNL_DT,STAT_MDFN_DT,OBJ_ID,VER_NBR) 
    VALUES (4106,(SELECT MAX(DOC_TYP_ID) FROM KREW_DOC_TYP_T WHERE DOC_TYP_NM = 'AwardDocument'),'F','KC Award - Created by Award 010002-00001 Ver 2',(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),1,2,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYS_GUID(),25)
/
INSERT INTO KREW_DOC_HDR_T (DOC_HDR_ID,DOC_TYP_ID,DOC_HDR_STAT_CD,TTL,INITR_PRNCPL_ID,RTE_PRNCPL_ID,DOC_VER_NBR,RTE_LVL,CRTE_DT,RTE_STAT_MDFN_DT,APRV_DT,FNL_DT,STAT_MDFN_DT,OBJ_ID,VER_NBR) 
    VALUES (4107,(SELECT MAX(DOC_TYP_ID) FROM KREW_DOC_TYP_T WHERE DOC_TYP_NM = 'AwardDocument'),'F','KC Award - Created by Award 010002-00001 Ver 2',(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),1,2,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYS_GUID(),25)
/
INSERT INTO KREW_DOC_HDR_T (DOC_HDR_ID,DOC_TYP_ID,DOC_HDR_STAT_CD,TTL,INITR_PRNCPL_ID,RTE_PRNCPL_ID,DOC_VER_NBR,RTE_LVL,CRTE_DT,RTE_STAT_MDFN_DT,APRV_DT,FNL_DT,STAT_MDFN_DT,OBJ_ID,VER_NBR) 
    VALUES (4108,(SELECT MAX(DOC_TYP_ID) FROM KREW_DOC_TYP_T WHERE DOC_TYP_NM = 'AwardDocument'),'F','KC Award - Created by Award 010002-00001 Ver 2',(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),1,2,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYSDATE,SYS_GUID(),25)
/
INSERT INTO KREW_DOC_HDR_T (DOC_HDR_ID,DOC_TYP_ID,DOC_HDR_STAT_CD,TTL,INITR_PRNCPL_ID,RTE_PRNCPL_ID,DOC_VER_NBR,RTE_LVL,CRTE_DT,RTE_STAT_MDFN_DT,APRV_DT,FNL_DT,STAT_MDFN_DT,OBJ_ID,VER_NBR) 
    VALUES (4109,(SELECT MAX(DOC_TYP_ID) FROM KREW_DOC_TYP_T WHERE DOC_TYP_NM = 'AwardDocument'),'R','KC Award - Sync Descendants, 300120',(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),(SELECT PRNCPL_ID FROM KRIM_PRNCPL_T WHERE PRNCPL_NM = 'quickstart'),1,2,SYSDATE,SYSDATE,null,null,SYSDATE,SYS_GUID(),29)
/
