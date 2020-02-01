INSERT INTO user_roles
SELECT u.id AS user_id, r.id AS role_id
FROM users u, roles r
WHERE u.username = "admin" AND r.role IN ("ROLE_USER", "ROLE_ADMIN");
