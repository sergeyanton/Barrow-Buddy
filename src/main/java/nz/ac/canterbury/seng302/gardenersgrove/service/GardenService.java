package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for Gardens, defined by the @link{Service} annotation.
 * This class links automatically with @link{GardenRepository}, see the @link{Autowired} annotation below
 */
@Service
public class GardenService {
    private GardenRepository gardenRepository;

//    @Autowired
    public GardenService(GardenRepository gardenRepository) {
        this.gardenRepository = gardenRepository;
    }
    /**
     * Gets all FormResults from persistence
     * @return all FormResults currently saved in persistence
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
