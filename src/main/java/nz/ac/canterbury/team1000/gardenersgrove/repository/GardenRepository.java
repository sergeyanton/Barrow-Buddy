package nz.ac.canterbury.team1000.gardenersgrove.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import java.util.List;
import java.util.Optional;

/**
 * Garden repository accessor using Spring's @link{CrudRepository}. These (basic) methods are
 * provided for us without the need to write our own implementations
 */
@Repository
public interface GardenRepository extends CrudRepository<Garden, Long> {
    Optional<Garden> findById(long id);

    List<Garden> findAll();
    List<Garden> findByOwnerId(long ownerId);
}
