package nz.ac.canterbury.team1000.gardenersgrove.service;

import java.time.LocalDate;
import java.util.List;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Plant;
import nz.ac.canterbury.team1000.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.team1000.gardenersgrove.repository.PlantRepository;
import nz.ac.canterbury.team1000.gardenersgrove.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

// Got this working with help from tutorial from src: https://www.baeldung.com/spring-beans-integration-test-override
@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class GardenServiceTest {

    @Autowired
    private GardenRepository gardenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private GardenService gardenService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        // Garden entity has a relationship with User so have to establish the user first
        User owner = new User("Jane", "Doe", "johndoe@gmail.com", "password", LocalDate.now(), null);
        testUser = userRepository.save(owner);
    }

    @Test
    public void simpleTest2() {
        GardenService gardenService = new GardenService(gardenRepository);
        Garden result = gardenService.addGarden(new Garden("My Garden", "A", "B", "C", "D", "E", 9000.0, testUser, false));
        Assertions.assertEquals(result.getName(), "My Garden");
    }

    @Test
    public void testSaveGarden_WithOwner_ReturnsCorrectGardenId() {
        Garden testGarden = new Garden(
            "Test Garden",
            "123 Riccarton Road",
            "Riccarton",
            "Christchurch",
            "8041",
            "New Zealand",
            100.0,
            testUser,
            true
        );
        Garden savedGarden = gardenService.addGarden(testGarden);

        Assertions.assertNotNull(savedGarden.getId());
        Assertions.assertEquals(savedGarden.getId(), testGarden.getId());
    }

    @Test
    public void testGetPublicGardens_WithPublicGarden_ReturnsGarden() {
        Garden publicGarden = new Garden(
            "Public Garden",
            "123 Riccarton Road",
            "Riccarton",
            "Christchurch",
            "8041",
            "New Zealand",
            100.0,
            testUser,
            true
        );
        Garden savedGarden = gardenService.addGarden(publicGarden);
        List<Garden> searchResults = gardenService.getPublicGardens();
        Assertions.assertNotNull(savedGarden.getId());
        Assertions.assertTrue(searchResults.contains(publicGarden));
    }

    @Test
    public void testGetPublicGardens_WithPrivateGarden_DoesNotReturnGarden() {
        Garden privateGarden = new Garden(
            "Private Garden",
            "123 Riccarton Road",
            "Riccarton",
            "Christchurch",
            "8041",
            "New Zealand",
            100.0,
            testUser,
            false
        );
        Garden savedGarden = gardenService.addGarden(privateGarden);
        List<Garden> searchResults = gardenService.getPublicGardens();

        Assertions.assertNotNull(savedGarden.getId());
        Assertions.assertFalse(searchResults.contains(privateGarden));
    }

    @Test
    public void testSearchPublicGardens_WithMatchingPublicGarden_ReturnsGarden() {
        Garden publicGarden = new Garden(
            "Public Garden",
            "456 Riccarton Road",
            "Riccarton",
            "Christchurch",
            "8041",
            "New Zealand",
            100.0,
            testUser,
            true
        );Garden savedGarden = gardenService.addGarden(publicGarden);
        List<Garden> searchResults = gardenService.searchGardens("Public");
        Assertions.assertNotNull(savedGarden.getId());
        Assertions.assertTrue(searchResults.contains(publicGarden));
    }

    @Test
    public void testSearchPublicGardens_WithMatchingPrivateGarden_DoesNotReturnGarden() {
        Garden privateGarden = new Garden(
            "Private Garden",
            "456 Riccarton Road",
            "Riccarton",
            "Christchurch",
            "8041",
            "New Zealand",
            100.0,
            testUser,
            false
        );
        Garden savedGarden = gardenService.addGarden(privateGarden);
        List<Garden> searchResults = gardenService.searchGardens("Private");
        Assertions.assertNotNull(savedGarden.getId());
        Assertions.assertFalse(searchResults.contains(privateGarden));
    }

    @Test
    public void testSearchPublicGardens_WithMatchingPublicPlant_ReturnsGarden() {
        Garden publicGarden = new Garden(
            "Public Garden",
            "456 Riccarton Road",
            "Riccarton",
            "Christchurch",
            "8041",
            "New Zealand",
            100.0,
            testUser,
            true
        );
        gardenRepository.save(publicGarden);

        Plant testPlant = new Plant(
            "Cactus",
            1,
            "Spiky",
            LocalDate.now(),
            "",
            publicGarden.getId()
        );
        plantRepository.save(testPlant);

        Garden savedGarden = gardenService.addGarden(publicGarden);
        List<Garden> searchResults = gardenService.searchGardens("Cactus");
        Assertions.assertNotNull(savedGarden.getId());
        Assertions.assertTrue(searchResults.contains(publicGarden));
    }

    @Test
    public void testSearchPublicGardens_WithMatchingPrivatePlant_DoesNotReturnGarden() {
        Garden privateGarden = new Garden(
            "Private Garden",
            "123 Riccarton Road",
            "Riccarton",
            "Christchurch",
            "8041",
            "New Zealand",
            100.0,
            testUser,
            false
        );
        gardenRepository.save(privateGarden);

        Plant testPlant = new Plant(
            "Cactus",
            1,
            "Spiky",
            LocalDate.now(),
            "",
            privateGarden.getId()
        );
        plantRepository.save(testPlant);

        Garden savedGarden = gardenService.addGarden(privateGarden);
        List<Garden> searchResults = gardenService.searchGardens("Cactus");
        Assertions.assertNotNull(savedGarden.getId());
        Assertions.assertFalse(searchResults.contains(privateGarden));
    }


    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        public EmailService emailService() {
            return Mockito.mock(EmailService.class);
        }

        @Bean
        @Primary
        public GardenService gardenService(GardenRepository gardenRepository) {
            return new GardenService(gardenRepository);
        }
    }

}