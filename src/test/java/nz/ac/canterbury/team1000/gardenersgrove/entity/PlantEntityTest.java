package nz.ac.canterbury.team1000.gardenersgrove.entity;

import org.junit.jupiter.api.Test;
import java.time.DateTimeException;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class PlantEntityTest {
    @Test
    void StringConstructor_AllValid_ReturnsCorrectPlantObject() {
        String plantName = "Plant Name";
        String plantCount = "3";
        String description = "This plant is cool";
        String dateString = "30/01/2024";
        Long gardenId = 1L;
        Plant plant = new Plant(plantName, plantCount, description, dateString, gardenId);
        assertEquals(plantName, plant.getName());
        assertEquals(Integer.parseInt(plantCount), plant.getPlantCount());
        assertEquals(description, plant.getDescription());
        assertEquals(LocalDate.of(2024, 1, 30), plant.getPlantedOnDate());
        assertEquals(gardenId, plant.getGardenId());
    }

    @Test
    void SetCount_ValidCountSet_ChangesCountToNewCount() {
        String newCount = "2";
        Plant plant = new Plant();
        plant.setPlantCount(newCount);
        assertEquals(2, plant.getPlantCount());
    }

    @Test
    void SetCount_ValidEmptyCountSet_ChangesCountToNewCount() {
        String newCount = "";
        Plant plant = new Plant();
        plant.setPlantCount(newCount);
        assertEquals(null, plant.getPlantCount());
    }

    @Test
    void SetCount_InvalidCountSet_ThrowsNumberFormatException() {
        String newCount = "d";
        Plant plant = new Plant();
        assertThrows(NumberFormatException.class, () -> plant.setPlantCount(newCount));
    }

    @Test
    void SetPlantedOnDate_ValidDateSet_ChangesPlantedOnDateToNewPlantedOnDate() {
        String dateString = "11/02/2024";
        Plant plant = new Plant();
        plant.setPlantedOnDate(dateString);
        assertEquals(LocalDate.of(2024, 2, 11), plant.getPlantedOnDate());
    }

    @Test
    void SetPlantedOnDate_InvalidDateSet_ThrowsDateTimeException() {
        String dateString = "35/20/2024"; // invalid as no months have 35 days and there are no more
                                          // than 12 months in a year
        Plant plant = new Plant();
        assertThrows(DateTimeException.class, () -> plant.setPlantedOnDate(dateString));
    }

    @Test
    void SetGardenId_ValidGardenIdSet_ChangesGardenIdToNewGardenId() {
        String newGardenId = "2";
        Plant plant = new Plant();
        plant.setGardenId(newGardenId);
        assertEquals(2, plant.getGardenId());
    }

    @Test
    void SetGardenId_InvalidGardenIdSet_ThrowsNumberFormatException() {
        String newGardenId = "d";
        Plant plant = new Plant();
        assertThrows(NumberFormatException.class, () -> plant.setGardenId(newGardenId));
    }
}
