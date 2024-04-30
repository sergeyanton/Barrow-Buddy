package nz.ac.canterbury.team1000.gardenersgrove.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GardenEntityTest {
    private User user = new User("fname", "lname", "email", "password", null, null);


    @Test
    void Constructor_WithValidSizeString_ReturnsGardenObjectParsedSize() {
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", "10.5", user);
        assertEquals(10.5, garden.getSize());
    }

    @Test
    void Constructor_WithInvalidSizeString_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Garden("name", "location", "location", "location", "location", "location",  "abc", user));
    }

    @Test
    void Constructor_WithValidEmptySizeString_ReturnsGardenObjectNullSize() {
        Garden garden = new Garden("name", "location", "location", "location", "location", "location",  "", user);
        assertNull(garden.getSize());
    }
    @Test
    void SetSize_WithValidSizeString_ReturnsGardenObjectParsedSize() {
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", (Double) null, user);
        garden.setSize("20.5");
        assertEquals(20.5, garden.getSize());
    }

    @Test
    void SetSize_WithInvalidSizeString_ThrowsIllegalArgumentException() {
        Garden garden = new Garden("name", "location", "location", "location", "location", "location",(Double) null, user);
        assertThrows(IllegalArgumentException.class, () -> garden.setSize("abc"));
    }

    @Test
    void Constructor_WithNoSize_ReturnsGardenObjectNullSize() {
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", (Double) null, user);
        assertNull(garden.getSize());
    }

    @Test
    void Constructor_WithValidSize_ReturnsGardenObjectSize() {
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", 10.5, user);
        assertEquals(10.5, garden.getSize());
    }

    @Test
    void Constructor_WithInvalidSize_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Garden("name", "location", "location", "location", "location", "location", -10.5, user));
    }

    @Test
    void Constructor_WithNegativeSizeString_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Garden("name", "location", "location", "location", "location", "location", "-10.5", user));
    }
}
