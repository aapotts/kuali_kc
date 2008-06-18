-- Create table
create table EPS_PROP_COLUMNS_TO_ALTER
(
  COLUMN_NAME      VARCHAR2(30) not null,
  COLUMN_LABEL     VARCHAR2(30) not null,
  DATA_TYPE        VARCHAR2(9) not null,
  DATA_LENGTH      NUMBER(4),
  HAS_LOOKUP       CHAR(1) not null,
  LOOKUP_WINDOW    VARCHAR2(100),
  LOOKUP_ARGUMENT  VARCHAR2(200), 
  UPDATE_TIMESTAMP DATE not null,
  UPDATE_USER      VARCHAR2(60) not null,
  VER_NBR NUMBER(8,0) DEFAULT 1 NOT NULL ENABLE,
  OBJ_ID VARCHAR2(36) DEFAULT SYS_GUID() NOT NULL ENABLE
);
-- Create/Recreate primary, unique and foreign key constraints 
alter table EPS_PROP_COLUMNS_TO_ALTER
  add constraint PK_EPS_PROP_COLUMNS_TO_ALTER primary key (COLUMN_NAME);