insert into KRMS_TERM_SPEC_T values('KC2000','specifiedGGForm','java.lang.String','Y',1,'Specified GG Form','KC-PD')
/
insert into KRMS_TERM_T values ('KC2000','KC2000',1,'Specified GG Form')
/
insert into KRMS_CNTXT_VLD_TERM_SPEC_T values ('KC2000','KC-PD-CONTEXT','KC2000','Y')
/
insert into KRMS_TERM_RSLVR_T values ('KC1002','KC-PD','javaFunctionResolver','KC1001','KC2000','Y',1)
/
insert into KRMS_TERM_RSLVR_PARM_SPEC_T (TERM_RSLVR_PARM_SPEC_ID, TERM_RSLVR_ID, NM, VER_NBR) 
values ('KC1002', (select TERM_RSLVR_ID from KRMS_TERM_RSLVR_T where NM='javaFunctionResolver' and NMSPC_CD='KC-PD'), 'GrantsGov Form Name', 1)
/
insert into KRMS_TERM_SPEC_CTGRY_T (TERM_SPEC_ID, CTGRY_ID) values ((select TERM_SPEC_ID from KRMS_TERM_SPEC_T where NMSPC_CD='KC-PD' and NM='specifiedGGForm'), 
	(select CTGRY_ID from KRMS_CTGRY_T where NMSPC_CD='KC-PD' and NM='Function'))
/
