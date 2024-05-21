package nz.ac.canterbury.team1000.gardenersgrove.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.repository.GardenRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Disabled
@DataJpaTest
@Import(GardenService.class)
public class GardenServiceTest {
    User testUser = new User(
        "first",
        "last",
        "email",
        "password",
        LocalDate.of(2003, 8, 19),
        ""
    );

    @Test
    public void simpleTest() {
        GardenService gardenService = new GardenService(new GardenRepository() {
            @Override
            public Optional<Garden> findById(long id) {
                return Optional.empty();
            }

            @Override
            public List<Garden> findAll() {
                return null;
            }

            @Override
            public <S extends Garden> S save(S entity) {
                // assume there is some modification at the service layer that we check here
                // instead of just the same values
                Assertions.assertEquals("My Garden", entity.getName());
                return entity;
            }

            @Override
            public <S extends Garden> Iterable<S> saveAll(Iterable<S> entities) {
                return null;
            }

            @Override
            public Optional<Garden> findById(Long aLong) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Long aLong) {
                return false;
            }

            @Override
            public Iterable<Garden> findAllById(Iterable<Long> longs) {
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
            public void delete(Garden entity) {

            }

            @Override
            public void deleteAllById(Iterable<? extends Long> longs) {

            }

            @Override
            public void deleteAll(Iterable<? extends Garden> entities) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public List<Garden> findByOwnerId(long ownerId) {
                return null;
            }
        });
<<<<<<< HEAD
        gardenService.addGarden(new Garden("My Garden", "A", "B", "C", "D", "E", false, 9000.0, "", testUser, false));
=======
        gardenService.addGarden(new Garden("My Garden", "A", "B", "C", "D", "E", null, null, 9000.0, testUser, false));
>>>>>>> 7fc08cc3510fed12b8d1fa09d120a1a65cb77c52
    }

    @Autowired
    private GardenRepository gardenRepository;

    @Test
    public void simpleTest2() {
        GardenService gardenService = new GardenService(gardenRepository);
<<<<<<< HEAD
        Garden result = gardenService.addGarden(new Garden("My Garden", "A", "B", "C", "D", "E", false, 9000.0, "", testUser, false));
=======
        Garden result = gardenService.addGarden(new Garden("My Garden", "A", "B", "C", "D", "E", null, null, 9000.0, testUser, false));
>>>>>>> 7fc08cc3510fed12b8d1fa09d120a1a65cb77c52
        Assertions.assertEquals(result.getName(), "My Garden");
    }
}
