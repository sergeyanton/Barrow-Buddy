package nz.ac.canterbury.seng302.gardenersgrove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import nz.ac.canterbury.seng302.gardenersgrove.classes.ValidityCheck;


public class GardenNameTest {

    /** This test involves the user entering a garden name that only contains letters,
     * since letters are considered valid characters, applying it to the ValidityCheck
     * method validGardenName() should get the method to return true. */
    @Test
    public void UserEntersGardenName_NameContainsLetters_ReturnsTrue() {
        String validName = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        boolean valid = ValidityCheck.validGardenName(validName);
        Assertions.assertTrue(valid);
    }

    /** This test involves the user entering a garden name that only contains numbers,
     * since numbers are considered valid characters, applying it to the ValidityCheck
     * method validGardenName() should get the method to return true. */
    @Test
    public void UserEntersGardenName_NameContainsNumbers_ReturnsTrue() {
        String validName = "1234567890";
        boolean valid = ValidityCheck.validGardenName(validName);
        Assertions.assertTrue(valid);
    }

    /** This test involves the user entering a garden name that only contains valid
     * non-alphanumeric symbols, since letters are considered valid characters,
     * applying it to the ValidityCheck method validGardenName() should get the method
     * to return true. */
    @Test
    public void UserEntersGardenName_NameContainsValidCharacters_ReturnsTrue() {
        String validName = "., -'";
        boolean valid = ValidityCheck.validGardenName(validName);
        Assertions.assertTrue(valid);
    }

    /** This test involves the user entering an empty garden name, since the garden name
     * cannot be empty, applying it to the ValidityCheck method validGardenName() should
     * get it to return false */
    @Test
    public void UserEntersGardenName_NameIsEmpty_ReturnsFalse() {
        String validName = "";
        boolean valid = ValidityCheck.validGardenName(validName);
        Assertions.assertFalse(valid);
    }

    /** This test involves the user entering a garden name that contains only whitespace,
     * since the garden name cannot be empty, applying it to the ValidityCheck method
     * validGardenName(0 should get it to return false as the method removes trailing
     * whitespace */
    @Test
    public void UserEntersGardenName_NameIsOnlyWhitespace_ReturnsFalse() {
        String validName = "    ";
        boolean valid = ValidityCheck.validGardenName(validName);
        Assertions.assertFalse(valid);
    }

    /** This test involves the user entering a garden name that contains invalid characters,
     * since the garden name cannot be empty, applying it to the ValidityCheck method
     * validGardenName() should get it to return false */
    @Test
    public void UserEntersGardenName_NameContainsInvalidCharacters_ReturnsFalse() {
        String validName = "`~_+=[]{}:;<>/?!@#$%^&*()";
        boolean valid = ValidityCheck.validGardenName(validName);
        Assertions.assertFalse(valid);
    }
}
