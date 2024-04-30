
-- Setup a test user with password Password1234!
INSERT INTO tab_user(id, date_of_birth, email, fName, lName, password, picture_path) VALUES (1, '2003-08-19', 'test@example.com', 'Test', 'User', '$2a$10$Ra2OyNaklSi.YcfvGPuN9urUjCT17A9Kmu57zYftXe0ydZpEqbHg.', '/images/default_pic.jpg');

-- I Hanan Used a LLM to update this file on 30/04/2024 the following inserts were generated

-- Create a garden the Hamilton Garden (id = 1)
INSERT INTO garden (id, name, address, suburb, city, postcode, country, size) VALUES (1, 'The Hamilton Gardens', '21 Hungerford Crescent', 'Hamilton East', 'Hamilton', '3216', 'New Zealand', 540000);
-- add some plants to the Hamilton Garden
INSERT INTO plant (name, plant_count, description, planted_on_date, picture_path, garden_id) VALUES ('Rose Bush', 200, 'Fragrant, colorful garden flower.', null, '/images/defaultPlantPic.png', 1),
                                                                                      ('Lavender', 50, 'Aromatic purple herb.', null, '/images/defaultPlantPic.png', 1),
                                                                                      ('Sunflower ', 100, 'Tall with large yellow flowers.', null, '/images/defaultPlantPic.png', 1),
                                                                                      ('Fern', 1150, 'Lush, green leafy plant.', null, '/images/defaultPlantPic.png', 1),
                                                                                      ('Tomato', 40, 'Edible red fruiting plant.', null, '/images/defaultPlantPic.png', 1),
                                                                                      ('Basil', 30, 'Aromatic herb used in cooking.', null, '/images/defaultPlantPic.png', 1),
                                                                                      ('Cactus', 20, 'Hardy desert plant with spines.', null, '/images/defaultPlantPic.png', 1),
                                                                                      ('Daisy', 45, 'Small white flowers with a yellow center.', null, '/images/defaultPlantPic.png', 1),
                                                                                      ('Lemon Tree', 90, 'Produces yellow citrus fruits.', null, '/images/defaultPlantPic.png', 1),
                                                                                      ('Lily', 60, 'Elegant flowers with various colors and patterns.', null, '/images/defaultPlantPic.png', 1);


-- Create a garden called the Christchurch Botanic Gardens (id = 2)
INSERT INTO garden (id, name, address, suburb, city, postcode, country, size) VALUES (2, 'The Christchurch Botanic Gardens', 'Rolleston Avenue', 'Christchurch Central', 'Christchurch', '8013', 'New Zealand', 210000);
-- add some plants to the Christchurch Botanic Gardens (all the plants in this garden ar very stinky! They are also terribly disorganized and have no plant counts)
INSERT INTO plant (name, description, picture_path, garden_id) VALUES ('Corpse Flower', 'The worlds smelliest flower.', '/images/defaultPlantPic.png', 2),
                                                        ('Durian', 'The worlds smelliest fruit.', '/images/defaultPlantPic.png', 2),
                                                        ('Stinkhorn', 'The worlds smelliest mushroom.', '/images/defaultPlantPic.png', 2),
                                                        ('Voodoo Lily', 'The worlds smelliest lily.', '/images/defaultPlantPic.png', 2),
                                                        ('Skunk Cabbage', 'The worlds smelliest cabbage.', '/images/defaultPlantPic.png', 2);

-- Create a garden called the Jack Erskine Foyer Garden Set (id = 3) this garden has no size
INSERT INTO garden (id, name, address, suburb, city, postcode, country) VALUES (3, 'The Jack Erskine Foyer Garden Set', 'University of Canterbury', 'Ilam', 'Christchurch', '8041', 'New Zealand');
-- add some plants to the Jack Erskine Foyer Garden Set these are all house plants and there is at lease 3 of each kind
INSERT INTO plant (name, plant_count, description, picture_path, garden_id) VALUES ('Spider Plant', 3, 'A hardy house plant with long, thin leaves.', '/images/defaultPlantPic.png', 3),
                                                                     ('Peace Lily', 3, 'A hardy house plant with large, white flowers.', '/images/defaultPlantPic.png', 3),
                                                                     ('Aloe Vera', 3, 'A hardy house plant with thick, fleshy leaves.', '/images/defaultPlantPic.png', 3),
                                                                     ('Snake Plant', 3, 'A hardy house plant with tall, thin leaves.', '/images/defaultPlantPic.png', 3),
                                                                     ('Pothos', 3, 'A hardy house plant with heart-shaped leaves.', '/images/defaultPlantPic.png', 3);