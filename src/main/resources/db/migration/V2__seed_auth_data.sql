INSERT INTO roles (name) VALUES ('ADMIN'), ('USER');

INSERT INTO permissions (name)
VALUES
 ('USER_READ'),
 ('USER_WRITE'),
 ('ADMIN_ACCESS');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name = 'ADMIN';

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name = 'USER'
AND p.name = 'USER_READ';
