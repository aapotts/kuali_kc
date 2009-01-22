/*
 Copyright 2006-2008 The Kuali Foundation

 Licensed under the Educational Community License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.osedu.org/licenses/ECL-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/
INSERT INTO PROTOCOL_PERSON_ROLES ( PROTOCOL_PERSON_ROLE_ID, DESCRIPTION, UNIT_DETAILS_REQUIRED, UPDATE_TIMESTAMP, UPDATE_USER, VER_NBR, OBJ_ID ) 
VALUES ( 'PI', 'Principal Investigator', 'Y', sysdate, user, 1, 'BE78D35C1E76405AB6955A7B87A8AD23' ); 
INSERT INTO PROTOCOL_PERSON_ROLES ( PROTOCOL_PERSON_ROLE_ID, DESCRIPTION, UNIT_DETAILS_REQUIRED, UPDATE_TIMESTAMP, UPDATE_USER, VER_NBR, OBJ_ID ) 
VALUES ( 'COI', 'Co-Investigator', 'Y', sysdate, user, 1, 'D5AD9B2933DA4CC28922A96A38FE0905' ); 
INSERT INTO PROTOCOL_PERSON_ROLES ( PROTOCOL_PERSON_ROLE_ID, DESCRIPTION, UNIT_DETAILS_REQUIRED, UPDATE_TIMESTAMP, UPDATE_USER, VER_NBR, OBJ_ID ) 
VALUES ( 'SP', 'Study Personnel', 'Y', sysdate, user, 1, '4AEDCBE139A040B9A21DFC4C7AED02BC' ); 
INSERT INTO PROTOCOL_PERSON_ROLES ( PROTOCOL_PERSON_ROLE_ID, DESCRIPTION, UNIT_DETAILS_REQUIRED, UPDATE_TIMESTAMP, UPDATE_USER, VER_NBR, OBJ_ID ) 
VALUES ( 'CA', 'Correspondent Administrator', 'N', sysdate, user, 1, '579644BAD4AD4F6EA2C0AF7A9C246C6C' ); 
INSERT INTO PROTOCOL_PERSON_ROLES ( PROTOCOL_PERSON_ROLE_ID, DESCRIPTION, UNIT_DETAILS_REQUIRED, UPDATE_TIMESTAMP, UPDATE_USER, VER_NBR, OBJ_ID ) 
VALUES ( 'CRC', 'Correspondent - CRC', 'N', sysdate, user, 1, '8A5E7CFB7D014A1A89E1E8A36CDFF86B' ); 
