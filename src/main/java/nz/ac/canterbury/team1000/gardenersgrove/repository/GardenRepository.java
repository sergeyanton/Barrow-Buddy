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

    /**
     * Updates the garden with the given id to the new garden details.
     *
     * @param id the id of the garden to update
     * @param updateGarden the new garden details
     */
    default void updateGardenById(long id, Garden updateGarden) {
        Optional<Garden> optionalGarden = findById(id);
        if (optionalGarden.isPresent()) {
            Garden garden = optionalGarden.get();
            garden.setName(updateGarden.getName());
            garden.setSize(updateGarden.getSize());
            garden.setIsPublic(updateGarden.getIsPublic());

            garden.setAddress(updateGarden.getAddress());
            garden.setSuburb(updateGarden.getSuburb());
            garden.setCity(updateGarden.getCity());
            garden.setPostcode(updateGarden.getPostcode());
            garden.setCountry(updateGarden.getCountry());
            garden.setLocationValid(updateGarden.getLocationValid());

            save(garden);
        }

    }
}
