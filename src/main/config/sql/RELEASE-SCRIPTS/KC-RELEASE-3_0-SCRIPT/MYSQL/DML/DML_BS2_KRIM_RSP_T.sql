INSERT INTO KRIM_RSP_ID_S VALUES (NULL);

INSERT INTO KRIM_RSP_T ( RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND )
SELECT MAX(ID), UUID(), 1, 1, 'KC-WKFLW', 'IRB Reviewer Approve Online Review', 'Protocol Document - IRB Reviewer approves online review', 'Y' FROM KRIM_RSP_ID_S;

INSERT INTO KRIM_RSP_ID_S VALUES (NULL);

INSERT INTO KRIM_RSP_T ( RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND )
SELECT MAX(ID), UUID(), 1, 1, 'KC-WKFLW', 'IRB Admin Approve Online Review', 'Protocol Online Review Document - IRB Admin approves online review', 'Y' FROM KRIM_RSP_ID_S;

INSERT INTO KRIM_RSP_ID_S VALUES (NULL);

INSERT INTO KRIM_RSP_T ( RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND )
SELECT MAX(ID), UUID(), 1, 1, 'KC-WKFLW', 'IRB Admin Approve Review Request', 'Protocol Online Review Document - IRB Admin approves online review request made by PI during protocol submission.', 'Y' FROM KRIM_RSP_ID_S;

INSERT INTO KRIM_RSP_ID_S VALUES (NULL);

INSERT INTO KRIM_RSP_T ( RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND )
SELECT MAX(ID), UUID(), 1, 1, 'KC-WKFLW', 'ProtocolUnitApprovalResponsibility', 'ProtocolUnitApprovalResponsibility', 'Y' FROM KRIM_RSP_ID_S;

COMMIT;