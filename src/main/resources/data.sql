INSERT INTO groups (id, name, admin)
SELECT 1, 'user', false
WHERE NOT EXISTS (SELECT 1 FROM groups WHERE id = 1);

INSERT INTO groups (id, name, admin)
SELECT 2, 'admin', true
WHERE NOT EXISTS (SELECT 1 FROM groups WHERE id = 2);