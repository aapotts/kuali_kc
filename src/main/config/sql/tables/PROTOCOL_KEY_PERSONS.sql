/*
 * Copyright 2006-2008 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.osedu.org/licenses/ECL-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* Table Script */ 
CREATE TABLE PROTOCOL_KEY_PERSONS ( 
	PROTOCOL_KEY_PERSONS_ID NUMBER(12,0) NOT NULL, 
	PROTOCOL_ID NUMBER(12,0) NOT NULL, 
	VER_NBR NUMBER(8,0) DEFAULT 1 NOT NULL, 
	OBJ_ID VARCHAR2(36) DEFAULT SYS_GUID() NOT NULL, 
	PROTOCOL_NUMBER VARCHAR2(20) NOT NULL, 
	SEQUENCE_NUMBER NUMBER(4,0) NOT NULL, 
	PERSON_ID VARCHAR2(9) NOT NULL, 
	PERSON_NAME VARCHAR2(90) NOT NULL, 
	PERSON_ROLE VARCHAR2(60), 
	NON_EMPLOYEE_FLAG VARCHAR2(1) NOT NULL, 
	AFFILIATION_TYPE_CODE NUMBER(3,0), 
	UPDATE_TIMESTAMP DATE, 
	UPDATE_USER VARCHAR2(60))
/

/* Primary Key Constraint */ 
ALTER TABLE PROTOCOL_KEY_PERSONS 
ADD CONSTRAINT PK_PROTOCOL_KEY_PERSONS 
PRIMARY KEY (PROTOCOL_KEY_PERSONS_ID)
/

/* *************** MODIFIED PRIMARY KEY COLUMN - Introduced new primary key for existing composite key ************ 
ALTER TABLE PROTOCOL_KEY_PERSONS 
ADD CONSTRAINT PK_PROTOCOL_KEY_PERSONS 
PRIMARY KEY (PROTOCOL_NUMBER, SEQUENCE_NUMBER, PERSON_ID)
/
*************** MODIFIED PRIMARY KEY COLUMN - Introduced new primary key for existing composite key ************ */ 
 


/* Foreign Key Constraint(s) */ 
ALTER TABLE PROTOCOL_KEY_PERSONS 
ADD CONSTRAINT FK_PROTOCOL_KEY_PERSONS 
FOREIGN KEY (PROTOCOL_ID) 
REFERENCES PROTOCOL (PROTOCOL_ID) 
/

ALTER TABLE PROTOCOL_KEY_PERSONS 
ADD CONSTRAINT FK_PROTOCOL_KEY_PERSONS2 
FOREIGN KEY (AFFILIATION_TYPE_CODE) 
REFERENCES AFFILIATION_TYPE (AFFILIATION_TYPE_CODE) 
/

/* *************** MODIFIED FOREIGN KEY COLUMN - Composite keys are removed from KCRA ************ 
ALTER TABLE PROTOCOL_KEY_PERSONS 
ADD CONSTRAINT FK_PROTOCOL_KEY_PERSONS 
FOREIGN KEY (PROTOCOL_NUMBER, SEQUENCE_NUMBER) 
REFERENCES OSP$PROTOCOL (PROTOCOL_NUMBER, SEQUENCE_NUMBER) 
/

*************** MODIFIED FOREIGN KEY COLUMN - Composite keys are removed from KCRA ************ */ 

