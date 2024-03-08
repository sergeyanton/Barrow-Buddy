package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import nz.ac.canterbury.seng302.gardenersgrove.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for Gardens, defined by the @link{Service} annotation.
 * This class links automatically with @link{GardenRepository}, see the @link{Autowired} annotation below
 */
@Service
public class PlantService {
    private PlantRepository plantRepository;

    @Autowired
    public PlantService(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    /**
     * Gets a plant from persistence by searching for the id
     * @param id id to look for
     * @return the appropriate plant
     */
    public Plant getPlantById(long id) {
        return plantRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid plant id: " + id));
    }

    /**
     * Gets all plants from persistence
     * @return all plants currently saved in persistence
     */
    public List<Plant> getPlants() {
        return plantRepository.findAll();
    }

    /**
     * Adds a Plant to persistence
     * @param plant object to persist
     * @return the saved plant object
     */
    public Plant addPlant(Plant plant) {
        return plantRepository.save(plant);
    }
}
