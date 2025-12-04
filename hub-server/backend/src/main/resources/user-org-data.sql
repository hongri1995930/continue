INSERT INTO organizations (org_slug, name) VALUES
('org-acme', 'Acme Org')
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO users (username, display_name) VALUES
('admin', 'Admin User')
ON DUPLICATE KEY UPDATE display_name = VALUES(display_name);

INSERT INTO user_orgs (user_id, org_id)
SELECT u.id, o.id FROM users u, organizations o
WHERE u.username = 'admin' AND o.org_slug = 'org-acme'
ON DUPLICATE KEY UPDATE user_id = user_id;
