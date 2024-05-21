package nz.ac.canterbury.team1000.gardenersgrove.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GardenEntityTest {
    private User user = new User("fname", "lname", "email", "password", null, null);


    @Test
    void Constructor_WithValidSizeString_ReturnsGardenObjectParsedSize() {
<<<<<<< HEAD
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", false, "10.5", "", user, false);
=======
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", null, null, "10.5", user, false);
>>>>>>> 7fc08cc3510fed12b8d1fa09d120a1a65cb77c52
        assertEquals(10.5, garden.getSize());
    }

    @Test
    void Constructor_WithInvalidSizeString_ThrowsIllegalArgumentException() {
<<<<<<< HEAD
        assertThrows(IllegalArgumentException.class, () -> new Garden("name", "location", "location", "location", "location", "location", false, "abc", "", user, false));
=======
        assertThrows(IllegalArgumentException.class, () -> new Garden("name", "location", "location", "location", "location", "location", null, null, "abc", user, false));
>>>>>>> 7fc08cc3510fed12b8d1fa09d120a1a65cb77c52
    }

    @Test
    void Constructor_WithValidEmptySizeString_ReturnsGardenObjectNullSize() {
<<<<<<< HEAD
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", false,  "", "", user, false);
=======
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", null, null,  "", user, false);
>>>>>>> 7fc08cc3510fed12b8d1fa09d120a1a65cb77c52
        assertNull(garden.getSize());
    }
    @Test
    void SetSize_WithValidSizeString_ReturnsGardenObjectParsedSize() {
<<<<<<< HEAD
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", false, (Double) null, "", user, false);
=======
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", null, null, (Double) null, user, false);
>>>>>>> 7fc08cc3510fed12b8d1fa09d120a1a65cb77c52
        garden.setSize("20.5");
        assertEquals(20.5, garden.getSize());
    }

    @Test
    void SetSize_WithInvalidSizeString_ThrowsIllegalArgumentException() {
<<<<<<< HEAD
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", false, (Double) null, "", user, false);
=======
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", null, null, (Double) null, user, false);
>>>>>>> 7fc08cc3510fed12b8d1fa09d120a1a65cb77c52
        assertThrows(IllegalArgumentException.class, () -> garden.setSize("abc"));
    }

    @Test
    void Constructor_WithNoSize_ReturnsGardenObjectNullSize() {
<<<<<<< HEAD
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", false, (Double) null, "", user, false);
=======
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", null, null, (Double) null, user, false);
>>>>>>> 7fc08cc3510fed12b8d1fa09d120a1a65cb77c52
        assertNull(garden.getSize());
    }

    @Test
    void Constructor_WithValidSize_ReturnsGardenObjectSize() {
<<<<<<< HEAD
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", false, 10.5, "", user, false);
=======
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", null, null, 10.5, user, false);
>>>>>>> 7fc08cc3510fed12b8d1fa09d120a1a65cb77c52
        assertEquals(10.5, garden.getSize());
    }

    @Test
    void Constructor_WithInvalidSize_ThrowsIllegalArgumentException() {
<<<<<<< HEAD
        assertThrows(IllegalArgumentException.class, () -> new Garden("name", "location", "location", "location", "location", "location", false, -10.5, "", user, false));
=======
        assertThrows(IllegalArgumentException.class, () -> new Garden("name", "location", "location", "location", "location", "location", null, null, -10.5, user, false));
>>>>>>> 7fc08cc3510fed12b8d1fa09d120a1a65cb77c52
    }

    @Test
    void Constructor_WithNegativeSizeString_ThrowsIllegalArgumentException() {
<<<<<<< HEAD
        assertThrows(IllegalArgumentException.class, () -> new Garden("name", "location", "location", "location", "location", "location", false, "-10.5", "", user, false));
    }

    @Test
    void Constructor_WithNullDescription_DescriptionIsNull() {
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", false, 10.5, null, user, false);
        assertNull(garden.getDescription());
    }

    @Test
    void Constructor_WithEmptyDescription_DescriptionIsNull() {
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", false, 10.5, "", user, false);
        assertNull(garden.getDescription());
    }

    @Test
    void Constructor_WithWhitespaceDescription_DescriptionIsNull() {
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", false, 10.5, " ", user, false);
        assertNull(garden.getDescription());
=======
        assertThrows(IllegalArgumentException.class, () -> new Garden("name", "location", "location", "location", "location", "location", null, null, "-10.5", user, false));
>>>>>>> 7fc08cc3510fed12b8d1fa09d120a1a65cb77c52
    }
}
