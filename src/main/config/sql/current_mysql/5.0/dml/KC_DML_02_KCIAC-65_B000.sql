DELIMITER /
INSERT INTO IACUC_PROTOCOL_PERSON_ROLES (PROTOCOL_PERSON_ROLE_ID,DESCRIPTION,UNIT_DETAILS_REQUIRED,AFFILIATION_DETAILS_REQUIRED,TRAINING_DETAILS_REQUIRED,COMMENTS_DETAILS_REQUIRED,ACTIVE_FLAG,UPDATE_TIMESTAMP,UPDATE_USER,VER_NBR,OBJ_ID)
  VALUES ('PI','Principal Investigator','Y','Y','Y','N','Y',NOW(),'admin',1,UUID())
/
INSERT INTO IACUC_PROTOCOL_PERSON_ROLES (PROTOCOL_PERSON_ROLE_ID,DESCRIPTION,UNIT_DETAILS_REQUIRED,AFFILIATION_DETAILS_REQUIRED,TRAINING_DETAILS_REQUIRED,COMMENTS_DETAILS_REQUIRED,ACTIVE_FLAG,UPDATE_TIMESTAMP,UPDATE_USER,VER_NBR,OBJ_ID)
  VALUES ('COI','Co-Investigator','Y','Y','Y','N','Y',NOW(),'admin',1,UUID())
/
INSERT INTO IACUC_PROTOCOL_PERSON_ROLES (PROTOCOL_PERSON_ROLE_ID,DESCRIPTION,UNIT_DETAILS_REQUIRED,AFFILIATION_DETAILS_REQUIRED,TRAINING_DETAILS_REQUIRED,COMMENTS_DETAILS_REQUIRED,ACTIVE_FLAG,UPDATE_TIMESTAMP,UPDATE_USER,VER_NBR,OBJ_ID)
  VALUES ('SP','Study Personnel','N','Y','Y','N','Y',NOW(),'admin',1,UUID())
/
INSERT INTO IACUC_PROTOCOL_PERSON_ROLES (PROTOCOL_PERSON_ROLE_ID,DESCRIPTION,UNIT_DETAILS_REQUIRED,AFFILIATION_DETAILS_REQUIRED,TRAINING_DETAILS_REQUIRED,COMMENTS_DETAILS_REQUIRED,ACTIVE_FLAG,UPDATE_TIMESTAMP,UPDATE_USER,VER_NBR,OBJ_ID)
  VALUES ('CRC','Correspondents','N','N','N','Y','Y',NOW(),'admin',1,UUID())
/
DELIMITER ;
