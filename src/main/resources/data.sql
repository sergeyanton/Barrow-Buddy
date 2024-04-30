
-- Setup a test user with password Password1234!
INSERT IGNORE INTO tab_user(id, date_of_birth, email, fName, lName, password, picture_path) VALUES (1, '2003-08-19', 'test@example.com', 'Test', 'User', '$2a$10$Ra2OyNaklSi.YcfvGPuN9urUjCT17A9Kmu57zYftXe0ydZpEqbHg.', '/images/default_pic.jpg');

-- I Hanan Used a LLM to update this file on 30/04/2024 the following inserts were generated

-- Create a garden the Hamilton Garden (id = 1)
INSERT IGNORE INTO garden (id, name, address, suburb, city, postcode, country, size) VALUES (1, 'The Hamilton Garden', '21 Hungerford Crescent', 'Hamilton East', 'Hamilton', '3216', 'New Zealand', 540000);
-- add some plants to the Hamilton Garden
INSERT IGNORE INTO plant (name, plant_count, description, planted_on_date, garden_id) VALUES ('Rose Bush', 200, 'Fragrant, colorful garden flower.', null, 1),
                                                                                      ('Lavender', 50, 'Aromatic purple herb.', null, 1),
                                                                                      ('Sunflower ', 100, 'Tall with large yellow flowers.', null, 1),
                                                                                      ('Fern', 1150, 'Lush, green leafy plant.', null, 1),
                                                                                      ('Tomato', 40, 'Edible red fruiting plant.', null, 1);

-- Create a garden called the Christchurch Botanic Gardens (id = 2)
INSERT IGNORE INTO garden (id, name, address, suburb, city, postcode, country, size) VALUES (2, 'The Christchurch Botanic Gardens', 'Rolleston Avenue', 'Christchurch Central', 'Christchurch', '8013', 'New Zealand', 210000);
-- add some plants to the Christchurch Botanic Gardens (all the plants in this garden ar very stinky! They are also terribly disorganized and have no plant counts)
INSERT IGNORE INTO plant (name, description, garden_id) VALUES ('Corpse Flower', 'The worlds smelliest flower.', 2),
                                                        ('Durian', 'The worlds smelliest fruit.', 2),
                                                        ('Stinkhorn', 'The worlds smelliest mushroom.', 2),
                                                        ('Voodoo Lily', 'The worlds smelliest lily.', 2),
                                                        ('Skunk Cabbage', 'The worlds smelliest cabbage.', 2);

-- Create a garden called the Jack Erskine Foyer Garden Set (id = 3) this garden has no size
INSERT IGNORE INTO garden (id, name, address, suburb, city, postcode, country) VALUES (3, 'The Jack Erskine Foyer Garden Set', 'University of Canterbury', 'Ilam', 'Christchurch', '8041', 'New Zealand');
-- add some plants to the Jack Erskine Foyer Garden Set these are all house plants and there is at lease 3 of each kind
INSERT IGNORE INTO plant (name, plant_count, description, garden_id) VALUES ('Spider Plant', 3, 'A hardy house plant with long, thin leaves.', 3),
                                                                     ('Peace Lily', 3, 'A hardy house plant with large, white flowers.', 3),
                                                                     ('Aloe Vera', 3, 'A hardy house plant with thick, fleshy leaves.', 3),
                                                                     ('Snake Plant', 3, 'A hardy house plant with tall, thin leaves.', 3),
                                                                     ('Pothos', 3, 'A hardy house plant with heart-shaped leaves.', 3);