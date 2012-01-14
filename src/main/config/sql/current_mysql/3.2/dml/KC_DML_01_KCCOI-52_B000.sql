DELIMITER /
INSERT INTO COI_DISCLOSURE_EVENT_TYPE ( EVENT_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER, OBJ_ID ) 
VALUES ( 1, 'Award', NOW(), 'admin',  UUID() )
/
INSERT INTO COI_DISCLOSURE_EVENT_TYPE ( EVENT_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER, OBJ_ID ) 
VALUES ( 2, 'Proposal', NOW(), 'admin',  UUID() )
/
INSERT INTO COI_DISCLOSURE_EVENT_TYPE ( EVENT_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER, OBJ_ID ) 
VALUES ( 3, 'IRB Protocol', NOW(), 'admin',  UUID() )
/
INSERT INTO COI_DISCLOSURE_EVENT_TYPE ( EVENT_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER, OBJ_ID ) 
VALUES ( 4, 'IACUC Protocol', NOW(), 'admin',  UUID() )
/
INSERT INTO COI_DISCLOSURE_EVENT_TYPE ( EVENT_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER, OBJ_ID ) 
VALUES ( 5, 'New', NOW(), 'admin',  UUID() )
/
INSERT INTO COI_DISCLOSURE_EVENT_TYPE ( EVENT_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER, OBJ_ID ) 
VALUES ( 6, 'Update', NOW(), 'admin',  UUID() )
/
INSERT INTO COI_DISCLOSURE_EVENT_TYPE ( EVENT_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER, OBJ_ID ) 
VALUES ( 7, 'Other', NOW(), 'admin',  UUID() )
/

DELIMITER ;