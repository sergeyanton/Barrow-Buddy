package nz.ac.canterbury.team1000.gardenersgrove.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class GardenTest {

    User testUser = new User("fname", "lname", "email", "password", null);

    @Test
    void Constructor_WithValidSizeString_ReturnsGardenObjectParsedSize() {
        Garden garden = new Garden("name", "location", "10.5", testUser);
        assertEquals(10.5, garden.getSize());
    }

    @Test
    void Constructor_WithInvalidSizeString_ThtowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Garden("name", "location", "abc", testUser));
    }

    @Test
    void SetSize_WithValidSizeString_ReturnsGardenObjectParsedSize() {
        Garden garden = new Garden("name", "location", (Double) null, testUser);
        garden.setSize("20.5");
        assertEquals(20.5, garden.getSize());
    }

    @Test
    void SetSize_WithInvalidSizeString_ThrowsIllegalArgumentException() {
        Garden garden = new Garden("name", "location", (Double) null, testUser);
        assertThrows(IllegalArgumentException.class, () -> garden.setSize("abc"));
    }

    @Test
    void Constructor_WithNoSize_ReturnsGardenObjectNullSize() {
        Garden garden = new Garden("name", "location", (Double) null, testUser);
        assertEquals(null, garden.getSize());
    }

    @Test
    void Constructor_WithValidSize_ReturnsGardenObjectSize() {
        Garden garden = new Garden("name", "location", 10.5, testUser);
        assertEquals(10.5, garden.getSize());
    }

    @Test
    void Constructor_WithInvalidSize_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Garden("name", "location", -10.5, testUser));
    }

    @Test
    void Constructor_WithNegativeSizeString_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Garden("name", "location", "-10.5", testUser));
    }
}
