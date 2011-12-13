DELIMITER /
CREATE TABLE SUBAWARD_CUSTOM_DATA 
   (	SUBAWARD_CUSTOM_DATA_ID DECIMAL(8,0), 
	SUBAWARD_ID DECIMAL(12,0), 
	CUSTOM_ATTRIBUTE_ID DECIMAL(12,0), 
	VALUE VARCHAR(2000), 
	UPDATE_TIMESTAMP DATE, 
	UPDATE_USER VARCHAR(60), 
	VER_NBR DECIMAL(8,0) DEFAULT 1, 
	OBJ_ID VARCHAR(36) NOT NULL,
	SEQUENCE_NUMBER DECIMAL(4,0) NOT NULL,
	SUBAWARD_CODE VARCHAR(20) NOT NULL
  )
/
ALTER TABLE SUBAWARD_CUSTOM_DATA
  ADD CONSTRAINT SUBAWARD_CUSTOM_DATAP1 PRIMARY KEY (SUBAWARD_CUSTOM_DATA_ID)
/
DELIMITER ;
