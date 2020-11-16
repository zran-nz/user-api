INSERT INTO permission (id, version, auth_key, auth_uris, entity, http_method, creator_id, modifier_id)
VALUES (30101,0,'Index PasswordRecovery','/v[\\d]+/password-recovery','PasswordRecovery','GET',1, 1);
INSERT INTO permission (id, version, auth_key, auth_uris, entity, http_method, creator_id, modifier_id)
VALUES (30102,0,'Create PasswordRecovery','/v[\\d]+/password-recovery','PasswordRecovery','POST',1, 1);
INSERT INTO permission (id, version, auth_key, auth_uris, entity, http_method, creator_id, modifier_id)
VALUES (30103,0,'Read PasswordRecovery','/v[\\d]+/password-recovery/[\\d]+','PasswordRecovery','GET',1, 1);
INSERT INTO permission (id, version, auth_key, auth_uris, entity, http_method, creator_id, modifier_id)
VALUES (30104,0,'Update PasswordRecovery','/v[\\d]+/password-recovery/[\\d]+','PasswordRecovery','PUT',1, 1);
INSERT INTO permission (id, version, auth_key, auth_uris, entity, http_method, creator_id, modifier_id)
VALUES (30105,0,'Delete a PasswordRecovery','/v[\\d]+/password-recovery/[\\d]+','PasswordRecovery','DELETE',1, 1);
INSERT INTO permission (id, version, auth_key, auth_uris, entity, http_method, creator_id, modifier_id)
VALUES (30106,0,'Delete all PasswordRecovery','/v[\\d]+/password-recovery/clear','PasswordRecovery','DELETE',1, 1);
INSERT INTO permission (id, version, auth_key, auth_uris, entity, http_method, creator_id, modifier_id)
VALUES (30107,0,'Excel PasswordRecovery','/v[\\d]+/password-recovery/excel','PasswordRecovery','GET',1, 1);



INSERT INTO role_permission (id, version, creator_id, modifier_id, permission_id, role_id)
VALUES (130101, 0, 1, 1, 30101, 1);
INSERT INTO role_permission (id, version, creator_id, modifier_id, permission_id, role_id)
VALUES (130102, 0, 1, 1, 30102, 1);
INSERT INTO role_permission (id, version, creator_id, modifier_id, permission_id, role_id)
VALUES (130103, 0, 1, 1, 30103, 1);
INSERT INTO role_permission (id, version, creator_id, modifier_id, permission_id, role_id)
VALUES (130104, 0, 1, 1, 30104, 1);
INSERT INTO role_permission (id, version, creator_id, modifier_id, permission_id, role_id)
VALUES (130105, 0, 1, 1, 30105, 1);
INSERT INTO role_permission (id, version, creator_id, modifier_id, permission_id, role_id)
VALUES (130106, 0, 1, 1, 30106, 1);
INSERT INTO role_permission (id, version, creator_id, modifier_id, permission_id, role_id)
VALUES (130107, 0, 1, 1, 30107, 1);
INSERT INTO role_permission (id, version, creator_id, modifier_id, permission_id, role_id)
VALUES (330101, 0, 1, 1, 30101, 3);
INSERT INTO role_permission (id, version, creator_id, modifier_id, permission_id, role_id)
VALUES (330102, 0, 1, 1, 30102, 3);
INSERT INTO role_permission (id, version, creator_id, modifier_id, permission_id, role_id)
VALUES (330103, 0, 1, 1, 30103, 3);
INSERT INTO role_permission (id, version, creator_id, modifier_id, permission_id, role_id)
VALUES (330104, 0, 1, 1, 30104, 3);
INSERT INTO role_permission (id, version, creator_id, modifier_id, permission_id, role_id)
VALUES (330105, 0, 1, 1, 30105, 3);
INSERT INTO role_permission (id, version, creator_id, modifier_id, permission_id, role_id)
VALUES (330106, 0, 1, 1, 30106, 3);
INSERT INTO role_permission (id, version, creator_id, modifier_id, permission_id, role_id)
VALUES (330107, 0, 1, 1, 30107, 3);
INSERT INTO role_permission (id, version, creator_id, modifier_id, permission_id, role_id)
VALUES (430101, 0, 1, 1, 30101, 4);
INSERT INTO role_permission (id, version, creator_id, modifier_id, permission_id, role_id)
VALUES (430102, 0, 1, 1, 30102, 4);
INSERT INTO role_permission (id, version, creator_id, modifier_id, permission_id, role_id)
VALUES (430103, 0, 1, 1, 30103, 4);
INSERT INTO role_permission (id, version, creator_id, modifier_id, permission_id, role_id)
VALUES (430104, 0, 1, 1, 30104, 4);
INSERT INTO role_permission (id, version, creator_id, modifier_id, permission_id, role_id)
VALUES (430105, 0, 1, 1, 30105, 4);
INSERT INTO role_permission (id, version, creator_id, modifier_id, permission_id, role_id)
VALUES (430106, 0, 1, 1, 30106, 4);
INSERT INTO role_permission (id, version, creator_id, modifier_id, permission_id, role_id)
VALUES (430107, 0, 1, 1, 30107, 4);



INSERT INTO role_permission_rule (role_permission_id, rule_id)
VALUES (130101, 1);
INSERT INTO role_permission_rule (role_permission_id, rule_id)
VALUES (130102, 1);
INSERT INTO role_permission_rule (role_permission_id, rule_id)
VALUES (130103, 1);
INSERT INTO role_permission_rule (role_permission_id, rule_id)
VALUES (130104, 1);
INSERT INTO role_permission_rule (role_permission_id, rule_id)
VALUES (130105, 1);
INSERT INTO role_permission_rule (role_permission_id, rule_id)
VALUES (130106, 1);
INSERT INTO role_permission_rule (role_permission_id, rule_id)
VALUES (130107, 1);
