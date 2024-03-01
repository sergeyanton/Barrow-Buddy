package nz.ac.canterbury.seng302.gardenersgrove;

import nz.ac.canterbury.seng302.gardenersgrove.classes.ValidityCheck;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class GardenLocationTest {

    /** This test involves the user entering a garden location that contains letters,
     * since letters are considered valid characters, applying it to the ValidityCheck
     * method validGardenLocation() should get the method to return true. */
    @Test
    public void UserEntersGardenLocation_LocationContainsLetters_ReturnsTrue() {
        String validLocation = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        boolean valid = ValidityCheck.validGardenLocation(validLocation);
        Assertions.assertTrue(valid);
    }

    /** This test involves the user entering a garden location that only contains numbers,
     * since numbers are considered valid characters, applying it to the ValidityCheck
     * method validGardenLocation() should get the method to return true. */
    @Test
    public void UserEntersGardenLocation_LocationContainsNumbers_ReturnsTrue() {
        String validLocation = "1234567890";
        boolean valid = ValidityCheck.validGardenLocation(validLocation);
        Assertions.assertTrue(valid);
    }

    /** This test involves the user entering a garden name that only contains valid
     * non-alphanumeric symbols, since letters are considered valid characters,
     * applying it to the ValidityCheck method validGardenName() should get the method
     * to return true. */
    @Test
    public void UserEntersGardenLocation_LocationContainsValidCharacters_ReturnsTrue() {
        String validLocation = "., -'";
        boolean valid = ValidityCheck.validGardenLocation(validLocation);
        Assertions.assertTrue(valid);
    }

    /** This test involves the user entering an empty garden name, since the garden name
     * cannot be empty, applying it to the ValidityCheck method validGardenName() should
     * get it to return false */
    @Test
    public void UserEntersGardenLocation_LocationIsEmpty_ReturnsFalse() {
        String validLocation = "";
        boolean valid = ValidityCheck.validGardenLocation(validLocation);
        Assertions.assertFalse(valid);
    }

    /** This test involves the user entering a garden name that contains only whitespace,
     * since the garden name cannot be empty, applying it to the ValidityCheck method
     * validGardenName() should get it to return false as the method removes trailing
     * whitespace */
    @Test
    public void UserEntersGardenLocation_LocationIsOnlyWhitespace_ReturnsFalse() {
        String validLocation = "    ";
        boolean valid = ValidityCheck.validGardenLocation(validLocation);
        Assertions.assertFalse(valid);
    }

    /** This test involves the user entering a garden name that contains invalid characters,
     * since the garden name cannot be empty, applying it to the ValidityCheck method
     * validGardenName() should get it to return false */
    @Test
    public void UserEntersGardenLocation_LocationContainsInvalidCharacters_ReturnsFalse() {
        String validLocation = "`~_+=[]{}:;<>/?!@#$%^&*()";
        boolean valid = ValidityCheck.validGardenLocation(validLocation);
        Assertions.assertFalse(valid);
    }
}
