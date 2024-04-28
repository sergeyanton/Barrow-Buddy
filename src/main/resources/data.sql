
-- Setup a test user with password Password1234!
INSERT INTO tab_user(id, date_of_birth, email, fName, lName, password) VALUES (1, '2003-08-19', 'test@example.com', 'Test', 'User', '$2a$10$Ra2OyNaklSi.YcfvGPuN9urUjCT17A9Kmu57zYftXe0ydZpEqbHg.');
-- Setup a second test user with Password1234!
INSERT INTO tab_user(id, date_of_birth, email, fName, lName, password) VALUES (2, '2003-08-19', 'test2@example.com', 'Test', 'User', '$2a$10$Ra2OyNaklSi.YcfvGPuN9urUjCT17A9Kmu57zYftXe0ydZpEqbHg.');

INSERT INTO garden (id, name, location, size, owner_id) VALUES (1, 'GardenWith12Plants', 'Springfield', 10, 1);
INSERT INTO garden (id, name, location, size, owner_id) VALUES (2, 'GardenWith10Plants', 'Springfield', 10, 1);
INSERT INTO garden (id, name, location, size, owner_id) VALUES (3, 'TestGarden', 'TestLocation', 1, 1);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (1, 'Plant', 1, 'Its green', '2023-01-01', 2);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (2, 'Plant', 1, 'Its green', '2023-01-01', 2);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (3, 'Plant', 1, 'Its green', '2023-01-01', 2);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (4, 'Plant', 1, 'Its green', '2023-01-01', 2);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (5, 'Plant', 1, 'Its green', '2023-01-01', 2);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (6, 'Plant', 1, 'Its green', '2023-01-01', 2);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (7, 'Plant', 1, 'Its green', '2023-01-01', 2);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (8, 'Plant', 1, 'Its green', '2023-01-01', 2);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (9, 'Plant', 1, 'Its green', '2023-01-01', 2);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (10, 'Plant', 1, 'Its green', '2023-01-01', 2);
INSERT INTO garden (id, name, location, size, owner_id) VALUES (4, 'Woohoo', 'Yeah', 30.5, 1);
INSERT INTO garden (id, name, location, size, owner_id) VALUES (5, 'The Hamilton Gardens', 'Hamilton, New Zealand', 540000, 1);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (11, 'Rose Bush', 200, 'Fragrant, colorful garden flower.', null, 5);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (12, 'Lavender', 50, 'Aromatic purple herb.', null, 5);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (13, 'Sunflower ', 100, 'Tall with large yellow flowers.', null, 5);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (14, 'Fern', 1150, 'Lush, green leafy plant.', null, 5);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (15, 'Tomato', 40, 'Edible red fruiting plant.', null, 5);
INSERT INTO garden (id, name, location, size, owner_id) VALUES (16, 'A1 .-''', 'A1 ,.-''', 123.456,2);
INSERT INTO garden (id, name, location, size, owner_id) VALUES (17, 'Unsized Garden', 'Unsized Garden', null, 2);
INSERT INTO garden (id, name, location, size, owner_id) VALUES (18, 'Long name garden yappa yappa yappa yappa', 'Hmm', 1, 2);
INSERT INTO garden (id, name, location, size, owner_id) VALUES (19, 'Longnamegardenyappayappayappayappa', 'Hmm', 1, 2);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (16, 'Plant', 1, 'Its green', '2023-01-01', 1);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (17, 'Plant', 1, 'Its green', '2023-01-01', 1);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (18, 'Plant', 1, 'Its green', '2023-01-01', 1);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (19, 'Plant', 1, 'Its green', '2023-01-01', 1);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (20, 'Plant', 1, 'Its green', '2023-01-01', 1);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (21, 'Plant', 1, 'Its green', '2023-01-01', 1);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (22, 'Plant', 1, 'Its green', '2023-01-01', 1);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (23, 'Plant', 1, 'Its green', '2023-01-01', 1);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (24, 'Plant', 1, 'Its green', '2023-01-01', 1);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (25, 'Plant', 1, 'Its green', '2023-01-01', 1);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (26, 'Plant', 1, 'Its green', '2023-01-01', 1);
INSERT INTO plant (id, name, plant_count, description, planted_on_date, garden_id) VALUES (27, 'Plant', 1, 'Its green', '2023-01-01', 1);
