-- add standard permissions for time and money document
insert into krim_perm_t values (1180, sys_guid(), 1, '10', 'KC-T', 'Create Time And Money Document', 'Initiate a new Time and Money Document', 'Y');
insert into krim_perm_t values (1181, sys_guid(), 1, '16', 'KC-T', 'Modify Time And Money Document', 'Modify a Time and Money Document', 'Y');
insert into krim_perm_t values (1182, sys_guid(), 1, '15', 'KC-T', 'Save Time And Money Document', 'Save a Time and Money Document', 'Y');
insert into krim_perm_t values (1183, sys_guid(), 1, '5', 'KC-T', 'Submit Time And Money Document', 'Submit a Time and Money Document', 'Y');
insert into krim_perm_t values (1184, sys_guid(), 1, '40', 'KC-T', 'Open Time And Money Document', 'Open a Time and Money Document', 'Y');
insert into krim_perm_t values (1185, sys_guid(), 1, '14', 'KC-T', 'Cancel Time And Money Document', 'Cancel a Time and Money Document', 'Y');
insert into krim_perm_t values (1186, sys_guid(), 1, '10008', 'KC-T', 'View Time And Money Document', 'View a Time and Money Document', 'Y');

COMMIT;