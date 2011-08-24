set define off
set sqlblanklines on
spool KC-RELEASE-3_1_1-Upgrade-ORACLE-Install.log
delimiter /
@../../current/3.1.1/sequences/KC_SEQ_ALL.sql
@../../current/3.1.1/tables/KC_TBL_AWARD.sql
@../../current/3.1.1/tables/KC_TBL_BUDGET.sql
@../../current/3.1.1/tables/KC_TBL_BUDGET_DETAILS.sql
@../../current/3.1.1/tables/KC_TBL_COST_ELEMENT.sql
@../../current/3.1.1/tables/KC_TBL_EPS_PROP_PERSON_EXT.sql
@../../current/3.1.1/tables/KC_TBL_FIN_OBJECT_CODE_MAPPING.sql
@../../current/3.1.1/tables/KC_TBL_PERSON_EXT_T.sql
@../../current/3.1.1/tables/KC_TBL_REVIEWER_ATTACHMENTS.sql
@../../current/3.1.1/tables/KC_TBL_STATE_CODE.sql
@../../current/3.1.1/tables/KC_TBL_USER_TABLES_TEMP.sql
@../../current/3.1.1/dml/KC_DML_01_KCINFR-347_B000.sql
@../../current/3.1.1/dml/KC_DML_01_KCINFR-348_B000.sql
@../../current/3.1.1/dml/KC_DML_01_KCINFR-350_B000.sql
@../../current/3.1.1/dml/KC_DML_01_KCIRB-1448_B000.sql
@../../current/3.1.1/dml/KC_DML_01_KCIRB-1449_B000.sql
@../../current/3.1.1/constraints/KC_FK_FIN_OBJECT_CODE_MAPPING.sql
@../../current/3.1.1/constraints/KC_PK_REVIEWER_ATTACHMENTS.sql
@../../current/3.1.1/constraints/KC_UQ_FIN_OBJECT_CODE_MAPPING.sql
delimiter ;
commit;
exit
