ALTER TABLE PROTOCOL_FUNDING_SOURCE DROP CONSTRAINT FK_PROTOCOL_FUNDING_SOURCE2;
ALTER TABLE PROTOCOL_FUNDING_SOURCE MODIFY (FUNDING_SOURCE_TYPE_CODE VARCHAR2(3));

-- These must be run together to avoid errors --
-- ALTER TABLE FUNDING_SOURCE_TYPE MODIFY (FUNDING_SOURCE_TYPE_CODE VARCHAR2(3)); --
ALTER TABLE FUNDING_SOURCE_TYPE 
  ADD (FUNDING_SOURCE_TYPE_CODE_TEMP VARCHAR2(3));
UPDATE FUNDING_SOURCE_TYPE
  SET FUNDING_SOURCE_TYPE_CODE_TEMP = FUNDING_SOURCE_TYPE_CODE;  
ALTER TABLE FUNDING_SOURCE_TYPE
  DROP COLUMN FUNDING_SOURCE_TYPE_CODE;
ALTER TABLE FUNDING_SOURCE_TYPE 
  ADD (FUNDING_SOURCE_TYPE_CODE VARCHAR2(3));
UPDATE FUNDING_SOURCE_TYPE
  SET FUNDING_SOURCE_TYPE_CODE = FUNDING_SOURCE_TYPE_CODE_TEMP;  
ALTER TABLE FUNDING_SOURCE_TYPE
  DROP COLUMN FUNDING_SOURCE_TYPE_CODE_TEMP;
ALTER TABLE FUNDING_SOURCE_TYPE
  ADD PRIMARY KEY (FUNDING_SOURCE_TYPE_CODE);

ALTER TABLE PROTOCOL_FUNDING_SOURCE
  ADD CONSTRAINT FK_PROTOCOL_FUNDING_SOURCE2 FOREIGN KEY (FUNDING_SOURCE_TYPE_CODE)
  REFERENCES FUNDING_SOURCE_TYPE (FUNDING_SOURCE_TYPE_CODE);