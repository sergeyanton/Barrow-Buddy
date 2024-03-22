package nz.ac.canterbury.team1000.gardenersgrove.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Plant;
import java.util.List;
import java.util.Optional;

/**
 * Plant repository accessor using Spring's @link{CrudRepository}. These (basic) methods are
 * provided for us without the need to write our own implementations
 */
@Repository
public interface PlantRepository extends CrudRepository<Plant, Long> {
    Optional<Plant> findById(long id);

    List<Plant> findAll();
}
