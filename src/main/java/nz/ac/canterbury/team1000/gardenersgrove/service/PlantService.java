package nz.ac.canterbury.team1000.gardenersgrove.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Plant;
import nz.ac.canterbury.team1000.gardenersgrove.repository.PlantRepository;
import java.util.List;

/**
 * <<<<<<< HEAD Service class for Gardens, defined by the @link{Service} annotation. This class
 * links automatically with @link{GardenRepository}, see the @link{Autowired} annotation below
 * ======= Service class for Plants, defined by the @link{Service} annotation. This class links
 * automatically with @link{PlantRepository}, see the @link{Autowired} annotation below >>>>>>>
 * 1911727fc84a3e4e5caea036e1cc0b168bba2f06
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
     * 
     * @param id id to look for
     * @return the appropriate plant
     */
    public Plant getPlantById(long id) {
        return plantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid plant id: " + id));
    }

    /**
     * Gets a plant from persistence by searching for the id
     *
     * @param id id to look for
     * @return the appropriate plant
     */
    public List<Plant> getPlantsByGardenId(long id) {
        return plantRepository.findByGardenId(id);
    }

    /**
     * Gets all plants from persistence
     * 
     * @return all plants currently saved in persistence
     */
    public List<Plant> getPlants() {
        return plantRepository.findAll();
    }

    /**
     * Adds a Plant to persistence
     * 
     * @param plant object to persist
     * @return the saved plant object
     */
    public Plant addPlant(Plant plant) {
        return plantRepository.save(plant);
    }

    /**
     * Updates a Plant in persistence
     *
     * @param plant object to update
     * @return the saved plant object
     */
    public Plant updatePlant(Plant plant) {
        return plantRepository.save(plant);
    }
}
