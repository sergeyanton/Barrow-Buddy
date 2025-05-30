package nz.ac.canterbury.team1000.gardenersgrove.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
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
    Page<Garden> findByIsPublicTrue(Pageable pageable);

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
            garden.setDescription(updateGarden.getDescription());

            garden.setAddress(updateGarden.getAddress());
            garden.setSuburb(updateGarden.getSuburb());
            garden.setCity(updateGarden.getCity());
            garden.setPostcode(updateGarden.getPostcode());
            garden.setCountry(updateGarden.getCountry());
            garden.setLatitude(updateGarden.getLatitude());
            garden.setLongitude(updateGarden.getLongitude());


            save(garden);
        }

    }

    /**
     * Searches for public gardens that match the given keyword String.
     * ChatGPT used for help with the query.
     *
     * @param keyword String to search by.
     * @param pageable the Pageable object to specify the page and size
     * @return Page of Garden objects whose name or plants' name matches the keyword.
     */
    @Query("SELECT DISTINCT g FROM Garden g LEFT JOIN Plant p ON g.id = p.gardenId "
        + "WHERE g.isPublic = true AND (LOWER(g.name) LIKE LOWER(concat('%', :keyword, '%')) "
        + "OR (p.name IS NOT NULL AND LOWER(p.name) LIKE LOWER(concat('%', :keyword, '%'))))")
    Page<Garden> searchPublicGardensByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
