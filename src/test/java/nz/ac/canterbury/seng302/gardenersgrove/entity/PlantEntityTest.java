package nz.ac.canterbury.seng302.gardenersgrove.entity;

import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlantEntityTest {
    @Test
    void CreateNewPlant_AllValid_ReturnsPlantObject() {
        String plantName = "Plant Name";
        int plantCount = 3;
        String description = "This plant is cool";
        String dateString = "30/1/2024";
        int gardenId = 1;
        Plant plant = new Plant(plantName, plantCount, description, dateString, gardenId);
        assertEquals(plantName, plant.getName());
        assertEquals(plantCount, plant.getCount());
        assertEquals(description, plant.getDescription());
        assertEquals(dateString, plant.getPlantedOnDate());
        assertEquals(gardenId, plant.getGardenId());
    }

    @Test
    void CreateNewPlant_InvalidDate_ThrowsException() {
        String dateString = "35/20/2024"; // Date invalid as no months have 35 days and there are no more than 12 months in a year
        Plant plant = new Plant();
        assertThrows(DateTimeException.class, () -> plant.setPlantedOnDate(dateString));
    }
}
