SET MODE MySQL;
-- Setup a test user with password Password1234!
INSERT INTO tab_user(date_of_birth, email, fName, lName, password, picture_path) VALUES ('2003-08-19', 'test@example.com', 'Test', 'User', '$2a$10$Ra2OyNaklSi.YcfvGPuN9urUjCT17A9Kmu57zYftXe0ydZpEqbHg.', '/images/default_pic.jpg');
INSERT INTO tab_user(date_of_birth, email, fName, lName, password, picture_path) VALUES ('2003-08-19', 'user@example.com', 'User', 'Test', '$2a$10$Ra2OyNaklSi.YcfvGPuN9urUjCT17A9Kmu57zYftXe0ydZpEqbHg.', '/images/default_pic.jpg');

-- I Hanan Used a LLM to update this file on 30/04/2024 the following inserts were generated



-- Create a garden the Hamilton Garden (id = 1)
INSERT INTO garden (name, address, suburb, city, postcode, country, size, owner_id, is_public) VALUES ('The Hamilton Gardens', '21 Hungerford Crescent', 'Hamilton East', 'Hamilton', '3216', 'New Zealand', 540000, 1, true);
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
INSERT INTO garden (name, address, suburb, city, postcode, country, size, owner_id, is_public) VALUES ('The Christchurch Botanic Gardens', 'Rolleston Avenue', 'Christchurch Central', 'Christchurch', '8013', 'New Zealand', 210000, 1, false);
-- add some plants to the Christchurch Botanic Gardens (all the plants in this garden ar very stinky! They are also terribly disorganized and have no plant counts)
INSERT INTO plant (name, description, picture_path, garden_id) VALUES ('Corpse Flower', 'The worlds smelliest flower.', '/images/defaultPlantPic.png', 2),
                                                        ('Durian', 'The worlds smelliest fruit.', '/images/defaultPlantPic.png', 2),
                                                        ('Stinkhorn', 'The worlds smelliest mushroom.', '/images/defaultPlantPic.png', 2),
                                                        ('Voodoo Lily', 'The worlds smelliest lily.', '/images/defaultPlantPic.png', 2),
                                                        ('Skunk Cabbage', 'The worlds smelliest cabbage.', '/images/defaultPlantPic.png', 2);

-- Create a garden called the Jack Erskine Foyer Garden Set (id = 3) this garden has no size
INSERT INTO garden (name, address, suburb, city, postcode, country, owner_id, is_public) VALUES ('The Jack Erskine Foyer Garden Set', 'University of Canterbury', 'Ilam', 'Christchurch', '8041', 'New Zealand', 1, false);
-- add some plants to the Jack Erskine Foyer Garden Set these are all house plants and there is at lease 3 of each kind
INSERT INTO plant (name, plant_count, description, picture_path, garden_id) VALUES ('Spider Plant', 3, 'A hardy house plant with long, thin leaves.', '/images/defaultPlantPic.png', 3),
                                                                     ('Peace Lily', 3, 'A hardy house plant with large, white flowers.', '/images/defaultPlantPic.png', 3),
                                                                     ('Aloe Vera', 3, 'A hardy house plant with thick, fleshy leaves.', '/images/defaultPlantPic.png', 3),
                                                                     ('Snake Plant', 3, 'A hardy house plant with tall, thin leaves.', '/images/defaultPlantPic.png', 3),
                                                                     ('Pothos', 3, 'A hardy house plant with heart-shaped leaves.', '/images/defaultPlantPic.png', 3);

-- Create a garden called User Test's Secret Garden (id = 4) This is for testing garden ownership and plant ownership
INSERT INTO garden (name, address, suburb, city, postcode, country, size, owner_id, is_public) VALUES ('User Test''s Secret Garden', '123 Fake Street', 'Fake Suburb', 'Fake City', '1234', 'Fake Country', 100, 2, false);
-- add some plants to User Test's Secret Garden these should all be plants that are illegal to own in New Zealand This user has a LARGE QUANTITY of each plant
INSERT INTO plant (name, plant_count, description, picture_path, garden_id) VALUES ('Cannabis', 420, 'A psychoactive drug plant.', '/images/defaultPlantPic.png', 4),
                                                                                          ('Coca', 100000, 'A psychoactive drug plant.', '/images/defaultPlantPic.png', 4),
                                                                                          ('Opium Poppy', 10000, 'A psychoactive drug plant.', '/images/defaultPlantPic.png', 4),
                                                                                          ('Magic Mushrooms', 1000, 'A psychoactive drug plant.', '/images/defaultPlantPic.png', 4),
                                                                                          ('Khat', 1000, 'A psychoactive drug plant.', '/images/defaultPlantPic.png', 4),
                                                                                          ('Peyote', 1000, 'A psychoactive drug plant.', '/images/defaultPlantPic.png', 4),
                                                                                          ('Salvia', 1000, 'A psychoactive drug plant.', '/images/defaultPlantPic.png', 4),
                                                                                          ('Kratom', 1000, 'A psychoactive drug plant.', '/images/defaultPlantPic.png', 4),
                                                                                          ('Ayahuasca', 1000, 'A psychoactive drug plant.', '/images/defaultPlantPic.png', 4),
                                                                                          ('Iboga', 1000, 'A psychoactive drug plant.', '/images/defaultPlantPic.png', 4);