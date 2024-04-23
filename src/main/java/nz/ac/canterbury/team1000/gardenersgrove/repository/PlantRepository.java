package nz.ac.canterbury.team1000.gardenersgrove.repository;

import org.springframework.data.jpa.repository.Query;
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

    /**
     * Gets all plants that belong to the garden with the given id
     * Technically the @Query parameter isn't strictly needed,
     * because spring automatically generates the query based off of
     * the METHOD NAME. Personally I think that is insane and way too
     * magic. Hence, the @Query annotation to show exactly what this method does.
     *
     * @param gardenId the ID of the garden
     * @return a list of plants that belong to the garden
     */
    @Query("SELECT p FROM Plant p WHERE p.gardenId = :gardenId")
    List<Plant> findByGardenId(Long gardenId);
}
