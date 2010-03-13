CREATE TABLE AWARD_REPORT_TERMS ( 
	AWARD_REPORT_TERMS_ID NUMBER(12,0) NOT NULL, 
	AWARD_ID NUMBER(12,0) NOT NULL,
	AWARD_NUMBER VARCHAR2(12) NOT NULL, 
	SEQUENCE_NUMBER NUMBER(4,0) NOT NULL, 
	REPORT_CLASS_CODE VARCHAR2(3) NOT NULL, 
	REPORT_CODE VARCHAR2(3) NOT NULL, 
	FREQUENCY_CODE VARCHAR2(3), 
	FREQUENCY_BASE_CODE VARCHAR2(3), 
	OSP_DISTRIBUTION_CODE VARCHAR2(3), 
	DUE_DATE DATE, 
	VER_NBR NUMBER(8,0) DEFAULT 1 NOT NULL, 
	OBJ_ID VARCHAR2(36) DEFAULT SYS_GUID() NOT NULL, 
	UPDATE_TIMESTAMP DATE NOT NULL, 
	UPDATE_USER VARCHAR2 (60) NOT NULL);

ALTER TABLE AWARD_REPORT_TERMS 
ADD CONSTRAINT PK_AWARD_REPORT_TERMS 
PRIMARY KEY (AWARD_REPORT_TERMS_ID);

