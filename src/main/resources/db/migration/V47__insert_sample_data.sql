DELETE FROM intern;
DELETE FROM employee;

SELECT setval('employee_seq', 1, false);
SELECT setval('intern_seq', 1, false);

-- Employees
INSERT INTO employee (id, first_name, email, department, salary, is_active) VALUES
                                                                                (3, 'Clara Nguyen', 'clara@company.com', 'RH', 2600, false),
                                                                                (4, 'David Petit', 'david.petit@company.com', 'Finance', 3000, false),
                                                                                (5, 'Colombe', 'colombe@company.com', 'IT', 1000000000, true),
                                                                                (6, 'Toto', 'toto@gmail.com', 'RH', 3000, false),
                                                                                (7, 'Mari', 'juana@gmail.com', 'Finance', 6000, true)
ON CONFLICT (id) DO NOTHING;

SELECT setval('employee_seq', COALESCE((SELECT MAX(id) FROM employee), 1));

-- Interns
INSERT INTO intern (id, first_name, email, department, salary, is_remunerate, manager_id) VALUES
                                                                                              (1, 'Léa Dubois', 'lea@company.com', 'Finance', 800, false, NULL),
                                                                                              (2, 'Tom Leroy', 'tom@company.com', 'Marketing', 750, true, 7),
                                                                                              (3, 'Emma Petit', 'emma@company.com', 'Finance', NULL, false, 7),
                                                                                              (4, 'Hugo Bernard', 'hugo@company.com', 'IT', 900, true, 5),
                                                                                              (5, 'Sarah Moreau', 'sarah@company.com', 'Finance', NULL, false, 7),
                                                                                              (6, 'Anaelle', 'anaelle@picoctf.org', 'Marketing', 6, false, 7),
                                                                                              (7, 'yolololol', 'yolololol@company.com', 'IT', 98888, false, 5),
                                                                                              (8, 'Coco', 'coco@gmail.com', 'IT', 1000, false, 5),
                                                                                              (9, 'Sasha', 'sasha@picoctf.org', 'Finance', 400, true, 7),
                                                                                              (10, 'Colombekjlk', 'colombekjlk@company.com', 'Marketing', NULL, false, NULL),
                                                                                              (11, 'Colombe Bis', 'colombe.bis@company.com', 'RH', NULL, false, NULL),
                                                                                              (12, 'lmkmk', 'lmkmk@picoctf.org', 'Finance', NULL, false, 7),
                                                                                              (13, 'S', 's@company.com', NULL, NULL, false, NULL)
ON CONFLICT (id) DO NOTHING;

SELECT setval('intern_seq', COALESCE((SELECT MAX(id) FROM intern), 1));