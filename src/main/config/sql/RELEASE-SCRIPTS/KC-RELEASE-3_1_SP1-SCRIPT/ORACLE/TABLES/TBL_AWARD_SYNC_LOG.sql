create table AWARD_SYNC_LOG (
  AWARD_SYNC_LOG_ID NUMBER(22,0),
  AWARD_SYNC_STATUS_ID NUMBER(22,0),
  AWARD_SYNC_CHANGE_ID NUMBER(22,0),
  SUCCESS CHAR(1) NOT NULL,
  LOG_TYPE_CODE CHAR(2) NOT NULL,
  STATUS VARCHAR(4000),
  UPDATE_TIMESTAMP  DATE NOT NULL,
  UPDATE_USER       VARCHAR2(60) NOT NULL,
  VER_NBR           NUMBER(8,0) NOT NULL,
  OBJ_ID            VARCHAR2(36) NOT NULL
);

ALTER TABLE AWARD_SYNC_LOG
	ADD CONSTRAINT PK_AWARD_SYNC_LOG
		PRIMARY KEY (AWARD_SYNC_LOG_ID);
		