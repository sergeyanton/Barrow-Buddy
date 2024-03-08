package nz.ac.canterbury.seng302.gardenersgrove.entity;

import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

public class PlantEntityTest {
    @Test
    void Constructor_AllValid_ReturnsPlantObject() {
        String plantName = "Plant Name";
        String plantCount = "3";
        String description = "This plant is cool";
        String dateString = "30/01/2024";
        String gardenId = "1";
        Plant plant = new Plant(plantName, plantCount, description, dateString, gardenId);
        assertEquals(plantName, plant.getName());
        assertEquals(Integer.parseInt(plantCount), plant.getCount());
        assertEquals(description, plant.getDescription());
        assertEquals(dateString, plant.getPlantedOnDate());
        assertEquals(Integer.parseInt(gardenId), plant.getGardenId());
    }

    @Test
    void SetCount_ValidCountSet_ChangesCountToNewCount() {
        String newCount = "2";
        int newCountInt = Integer.parseInt(newCount);
        Plant plant = new Plant();
        plant.setCount(newCount);
        assertEquals(plant.getCount(), newCountInt);
    }

    @Test
    void SetCount_InvalidCountSet_ThrowsNumberFormatException() {
        String newCount = "d";
        Plant plant = new Plant();
        assertThrows(NumberFormatException.class, () -> plant.setCount(newCount));
    }

    @Test
    void SetPlantedOnDate_ValidDateSet_ChangesPlantedOnDateToNewPlantedOnDate() {
        String dateString = "11/02/2024";
        Plant plant = new Plant();
        plant.setPlantedOnDate(dateString);
        assertEquals(plant.getPlantedOnDate(), dateString);
    }

    @Test
    void SetPlantedOnDate_InvalidDateSet_ThrowsDateTimeException() {
        String dateString = "35/20/2024"; // invalid as no months have 35 days and there are no more than 12 months in a year
        Plant plant = new Plant();
        assertThrows(DateTimeException.class, () -> plant.setPlantedOnDate(dateString));
    }

    @Test
    void SetGardenId_ValidGardenIdSet_ChangesGardenIdToNewGardenId() {
        String newGardenId = "2";
        int newGardenIdInt = Integer.parseInt(newGardenId);
        Plant plant = new Plant();
        plant.setGardenId(newGardenId);
        assertEquals(plant.getGardenId(), newGardenIdInt);
    }

    @Test
    void SetGardenId_InvalidGardenIdSet_ThrowsNumberFormatException() {
        String newGardenId = "d";
        Plant plant = new Plant();
        assertThrows(NumberFormatException.class, () -> plant.setCount(newGardenId));
    }
}
