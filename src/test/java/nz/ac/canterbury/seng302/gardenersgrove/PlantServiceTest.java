package nz.ac.canterbury.seng302.gardenersgrove;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import nz.ac.canterbury.seng302.gardenersgrove.repository.PlantRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.PlantService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@Import(PlantService.class)
public class PlantServiceTest {

    @Test
    public void simpleTest() {
        PlantService plantService = new PlantService(new PlantRepository() {
            @Override
            public Optional<Plant> findById(long id) {
                return Optional.empty();
            }

            @Override
            public List<Plant> findAll() {
                return null;
            }

            @Override
            public <S extends Plant> S save(S entity) {
                // assume there is some modification at the service layer that we check here
                // instead of just the same values
                Assertions.assertEquals("My Plant", entity.getName());
                return entity;
            }

            @Override
            public <S extends Plant> Iterable<S> saveAll(Iterable<S> entities) {
                return null;
            }

            @Override
            public Optional<Plant> findById(Long aLong) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Long aLong) {
                return false;
            }

            @Override
            public Iterable<Plant> findAllById(Iterable<Long> longs) {
                return null;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Long aLong) {

            }

            @Override
            public void delete(Plant entity) {

            }

            @Override
            public void deleteAllById(Iterable<? extends Long> longs) {

            }

            @Override
            public void deleteAll(Iterable<? extends Plant> entities) {

            }

            @Override
            public void deleteAll() {

            }
        });
        plantService.addPlant(new Plant("My Plant", 1, "My plant is cool", "30/1/2021", 1));
    }

    // @Autowired
    // private PlantService formService;

    @Autowired
    private PlantRepository plantRepository;

    @Test
    public void CreateNewPlant_NewPlantCreated_PlantInRepository() {
        PlantService gardenService = new PlantService(plantRepository);
        Plant result = gardenService.addPlant(new Plant("My Plant", 1, "My plant is cool", "30/1/2021", 1));
        Assertions.assertEquals(result.getName(), "My Plant");
    }
}