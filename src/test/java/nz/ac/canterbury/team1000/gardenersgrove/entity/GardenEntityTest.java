package nz.ac.canterbury.team1000.gardenersgrove.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GardenEntityTest {
    private User user = new User("fname", "lname", "email", "password", null, null);


    @Test
    void Constructor_WithValidSizeString_ReturnsGardenObjectParsedSize() {
<<<<<<< HEAD
<<<<<<< HEAD
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", false, "10.5", "", user, false);
=======
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", null, null, "10.5", user, false);
>>>>>>> 7fc08cc3510fed12b8d1fa09d120a1a65cb77c52
=======
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", null, null, "", "10.5", user, false);
>>>>>>> 21d89ad15f3b66f35e384a11c65353671559e1cc
        assertEquals(10.5, garden.getSize());
    }

    @Test
    void Constructor_WithInvalidSizeString_ThrowsIllegalArgumentException() {
<<<<<<< HEAD
<<<<<<< HEAD
        assertThrows(IllegalArgumentException.class, () -> new Garden("name", "location", "location", "location", "location", "location", false, "abc", "", user, false));
=======
        assertThrows(IllegalArgumentException.class, () -> new Garden("name", "location", "location", "location", "location", "location", null, null, "abc", user, false));
>>>>>>> 7fc08cc3510fed12b8d1fa09d120a1a65cb77c52
=======
        assertThrows(IllegalArgumentException.class, () -> new Garden("name", "location", "location", "location", "location", "location", null, null, "", "abc", user, false));
>>>>>>> 21d89ad15f3b66f35e384a11c65353671559e1cc
    }

    @Test
    void Constructor_WithValidEmptySizeString_ReturnsGardenObjectNullSize() {
<<<<<<< HEAD
<<<<<<< HEAD
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", false,  "", "", user, false);
=======
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", null, null,  "", user, false);
>>>>>>> 7fc08cc3510fed12b8d1fa09d120a1a65cb77c52
=======
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", null, null,  "", "", user, false);
>>>>>>> 21d89ad15f3b66f35e384a11c65353671559e1cc
        assertNull(garden.getSize());
    }
    @Test
    void SetSize_WithValidSizeString_ReturnsGardenObjectParsedSize() {
<<<<<<< HEAD
<<<<<<< HEAD
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", false, (Double) null, "", user, false);
=======
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", null, null, (Double) null, user, false);
>>>>>>> 7fc08cc3510fed12b8d1fa09d120a1a65cb77c52
=======
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", null, null, (Double) null, "", user, false);
>>>>>>> 21d89ad15f3b66f35e384a11c65353671559e1cc
        garden.setSize("20.5");
        assertEquals(20.5, garden.getSize());
    }

    @Test
    void SetSize_WithInvalidSizeString_ThrowsIllegalArgumentException() {
<<<<<<< HEAD
<<<<<<< HEAD
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", false, (Double) null, "", user, false);
=======
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", null, null, (Double) null, user, false);
>>>>>>> 7fc08cc3510fed12b8d1fa09d120a1a65cb77c52
=======
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", null, null, (Double) null, "", user, false);
>>>>>>> 21d89ad15f3b66f35e384a11c65353671559e1cc
        assertThrows(IllegalArgumentException.class, () -> garden.setSize("abc"));
    }

    @Test
    void Constructor_WithNoSize_ReturnsGardenObjectNullSize() {
<<<<<<< HEAD
<<<<<<< HEAD
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", false, (Double) null, "", user, false);
=======
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", null, null, (Double) null, user, false);
>>>>>>> 7fc08cc3510fed12b8d1fa09d120a1a65cb77c52
=======
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", null, null, (Double) null, "", user, false);
>>>>>>> 21d89ad15f3b66f35e384a11c65353671559e1cc
        assertNull(garden.getSize());
    }

    @Test
    void Constructor_WithValidSize_ReturnsGardenObjectSize() {
<<<<<<< HEAD
<<<<<<< HEAD
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", false, 10.5, "", user, false);
=======
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", null, null, 10.5, user, false);
>>>>>>> 7fc08cc3510fed12b8d1fa09d120a1a65cb77c52
=======
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", null, null, 10.5, "", user, false);
>>>>>>> 21d89ad15f3b66f35e384a11c65353671559e1cc
        assertEquals(10.5, garden.getSize());
    }

    @Test
    void Constructor_WithInvalidSize_ThrowsIllegalArgumentException() {
<<<<<<< HEAD
<<<<<<< HEAD
        assertThrows(IllegalArgumentException.class, () -> new Garden("name", "location", "location", "location", "location", "location", false, -10.5, "", user, false));
=======
        assertThrows(IllegalArgumentException.class, () -> new Garden("name", "location", "location", "location", "location", "location", null, null, -10.5, user, false));
>>>>>>> 7fc08cc3510fed12b8d1fa09d120a1a65cb77c52
=======
        assertThrows(IllegalArgumentException.class, () -> new Garden("name", "location", "location", "location", "location", "location", null, null, -10.5, "", user, false));
>>>>>>> 21d89ad15f3b66f35e384a11c65353671559e1cc
    }

    @Test
    void Constructor_WithNegativeSizeString_ThrowsIllegalArgumentException() {
<<<<<<< HEAD
<<<<<<< HEAD
        assertThrows(IllegalArgumentException.class, () -> new Garden("name", "location", "location", "location", "location", "location", false, "-10.5", "", user, false));
=======
        assertThrows(IllegalArgumentException.class, () -> new Garden("name", "location", "location", "location", "location", "location", null, null, "", "-10.5", user, false));
>>>>>>> 21d89ad15f3b66f35e384a11c65353671559e1cc
    }

    @Test
    void Constructor_WithNullDescription_DescriptionIsNull() {
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", null, null, 10.5, null, user, false);
        assertNull(garden.getDescription());
    }

    @Test
    void Constructor_WithEmptyDescription_DescriptionIsNull() {
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", null, null, 10.5, "", user, false);
        assertNull(garden.getDescription());
    }

    @Test
    void Constructor_WithWhitespaceDescription_DescriptionIsNull() {
        Garden garden = new Garden("name", "location", "location", "location", "location", "location", null, null, 10.5, " ", user, false);
        assertNull(garden.getDescription());
<<<<<<< HEAD
=======
        assertThrows(IllegalArgumentException.class, () -> new Garden("name", "location", "location", "location", "location", "location", null, null, "-10.5", user, false));
>>>>>>> 7fc08cc3510fed12b8d1fa09d120a1a65cb77c52
=======
>>>>>>> 21d89ad15f3b66f35e384a11c65353671559e1cc
    }
}
