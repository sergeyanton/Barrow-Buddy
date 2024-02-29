package nz.ac.canterbury.seng302.gardenersgrove.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class GardenEntityTest {
    @Test
    void Constructor_WithValidSizeString_ReturnsGardenObjectParsedSize() {
        Garden garden = new Garden("name", "location", "10.5");
        assertEquals(10.5, garden.getSize());
    }

    @Test
    void Constructor_WithInvalidSizeString_ThtowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Garden("name", "location", "abc"));
    }
}
