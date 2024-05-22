package nz.ac.canterbury.team1000.gardenersgrove.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import nz.ac.canterbury.team1000.gardenersgrove.repository.GardenRepository;
import java.util.List;

/**
 * Service class for Gardens, defined by the @link{Service} annotation. This class links
 * automatically with @link{GardenRepository}, see the @link{Autowired} annotation below
 */
@Service
public class GardenService {
    private final GardenRepository gardenRepository;

    @Autowired
    public GardenService(GardenRepository gardenRepository) {
        this.gardenRepository = gardenRepository;
    }

    /**
     * Gets a garden from persistence by searching for the id
     * 
     * @param id id to look for
     * @return the appropriate garden
     */
    public Garden getGardenById(long id) {
        return gardenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid garden id: " + id));
    }

    /**
     * Gets all gardens for the user.
     * 
     * @param ownerId The owner id to search for
     * @return List of gardens for the user
     */
    public List<Garden> getUserGardens(long ownerId) {
        return gardenRepository.findByOwnerId(ownerId);
    }

    /**
     * Gets all public gardens from persistence.
     *
     * @param pageable the Pageable object to specify the page and size
     * @return Page of gardens with isPublic attribute set to true
     */
    public Page<Garden> getPublicGardens(Pageable pageable) {
        return gardenRepository.findByIsPublicTrue(pageable);
    }

    /**
     * Adds a Garden to persistence
     * 
     * @param garden object to persist
     * @return the saved garden object
     */
    public Garden addGarden(Garden garden) {
        return gardenRepository.save(garden);
    }

    /**
     * Updates a Garden in persistence
     *
     * @param gardenId to update
     * @param garden object to update
     * @return the saved garden object
     */
    public Garden updateGardenById(long gardenId, Garden garden) {
        gardenRepository.updateGardenById(gardenId, garden);
        return getGardenById(gardenId);
    }

    /**
     * Queries the repository to find the gardens whose names or plant names match the given query string
     *
     * @param query String to be searched
     * @param pageable the Pageable object to specify the page and size
     * @return Page of garden objects that match the query
     */
    public Page<Garden> searchGardens(String query, Pageable pageable) {
        return gardenRepository.searchPublicGardensByKeyword(query, pageable);
    }
}