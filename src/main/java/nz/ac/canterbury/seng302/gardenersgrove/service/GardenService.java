package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for Gardens, defined by the @link{Service} annotation.
 * This class links automatically with @link{GardenRepository}, see the @link{Autowired} annotation below
 */
@Service
public class GardenService {
    private GardenRepository gardenRepository;

    @Autowired
    public GardenService(GardenRepository gardenRepository) {
        this.gardenRepository = gardenRepository;
    }

    /**
     * Gets a garden from persistence by searching for the id
     * @param id id to look for
     * @return the appropriate garden
     */
    public Garden getGardenById(long id) {
        return gardenRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid garden id: " + id));
    }

    /**
     * Gets all gardens from persistence
     * @return all gardens currently saved in persistence
     */
    public List<Garden> getGardens() {
        return gardenRepository.findAll();
    }

    /**
     * Adds a Garden to persistence
     * @param garden object to persist
     * @return the saved garden object
     */
    public Garden addFormResult(Garden garden) {
        return gardenRepository.save(garden);
    }
}
