set define off
set sqlblanklines on
spool KC-RELEASE-4_0-Demo-ORACLE-Install.log
@../../current/4.0/dml/KC_DML_01_CUSTOM_ATTRIBUTE_DOCUMENT_0TSD.sql
@../../current/4.0/dml/KC_DML_01_KCINFR-477_00SD.sql
@../../current/4.0/dml/KC_DML_01_SPONSOR_FORM_TEMPLATES_00SD.sql
@../../current/4.0/dml/KC_DML_02_SPONSOR_FORM_TEMPLATES_00SD.sql
@../../current/4.0/dml/KC_DML_03_SPONSOR_FORM_TEMPLATES_00SD.sql
@../../current/4.0/dml/KC_DML_04_SPONSOR_FORM_TEMPLATES_00SD.sql
commit;
exit
