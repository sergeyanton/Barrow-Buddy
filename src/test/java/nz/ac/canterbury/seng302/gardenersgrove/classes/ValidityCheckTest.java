package nz.ac.canterbury.seng302.gardenersgrove.classes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Optional;

import org.junit.jupiter.api.Test;

public class ValidityCheckTest {

    @Test
    void CheckName_WithValidName_ReturnsEmptyOptional() {
        String name = "Valid Tree Name";
        assertEquals(Optional.empty(), ValidityCheck.validGardenName(name));
    }
    @Test
    void CheckName_WithEmptyName_ReturnsOptionalWithErrorMessage() {
        String name = "";
        assertEquals(Optional.of("Garden name must not be empty"), ValidityCheck.validGardenName(name));
    }

    @Test
    void CheckName_WithWhiteSpaceName_ReturnsOptionalWithErrorMessage() {
        String name = "             ";
        assertEquals(Optional.of("Garden name must not be empty"), ValidityCheck.validGardenName(name));
    }

    @Test
    void CheckName_WithInvalidName_ReturnsOptionalWithErrorMessage() {
        String name = "Invalid Name Because Of The Dollar $ign ";
        assertEquals(Optional.of("Garden name must only include letters, numbers, spaces, dots, hyphens or apostrophes"), ValidityCheck.validGardenName(name));
    }

    /** This test involves the user entering a garden location that contains letters,
     * since letters are considered valid characters, applying it to the ValidityCheck
     * method validGardenLocation() should get the method to return true. */
    @Test
    public void UserEntersGardenLocation_LocationContainsLetters_ReturnsEmptyOptional() {
        String location = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        assertEquals(Optional.empty(), ValidityCheck.validGardenLocation(location));
    }

    /** This test involves the user entering a garden location that only contains numbers,
     * since numbers are considered valid characters, applying it to the ValidityCheck
     * method validGardenLocation() should get the method to return true. */
    @Test
    public void UserEntersGardenLocation_LocationContainsNumbers_ReturnsTrue() {
        String location = "1234567890";
        assertEquals(Optional.empty(), ValidityCheck.validGardenLocation(location));

    }

    /** This test involves the user entering a garden name that only contains valid
     * non-alphanumeric symbols, since letters are considered valid characters,
     * applying it to the ValidityCheck method validGardenName() should get the method
     * to return true. */
    @Test
    public void UserEntersGardenLocation_LocationContainsValidCharacters_ReturnsTrue() {
        String location = "., -'";
        assertEquals(Optional.empty(), ValidityCheck.validGardenLocation(location));

    }

    /** This test involves the user entering an empty garden name, since the garden name
     * cannot be empty, applying it to the ValidityCheck method validGardenName() should
     * get it to return false */
    @Test
    public void UserEntersGardenLocation_LocationIsEmpty_ReturnsFalse() {
        String location = "";
        assertEquals(Optional.of("Location cannot be empty"), ValidityCheck.validGardenLocation(location));

    }

    /** This test involves the user entering a garden name that contains only whitespace,
     * since the garden name cannot be empty, applying it to the ValidityCheck method
     * validGardenName() should get it to return false as the method removes any trailing
     * whitespace */
    @Test
    public void UserEntersGardenLocation_LocationIsOnlyWhitespace_ReturnsFalse() {
        String location = "    ";
        assertEquals(Optional.of("Location cannot be empty"), ValidityCheck.validGardenLocation(location));

    }

    /** This test involves the user entering a garden name that contains invalid characters,
     * since the garden name cannot be empty, applying it to the ValidityCheck method
     * validGardenName() should get it to return false */
    @Test
    public void UserEntersGardenLocation_LocationContainsInvalidCharacters_ReturnsFalse() {
        String location = "`~_+=[]{}:;<>/?!@#$%^&*()";
        assertEquals(Optional.of("Location name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes"), ValidityCheck.validGardenLocation(location));

    }
    @Test
    void ValidGardenSize_WithValidSizeSeperatedWithDot_ReturnsEmptyOptional() {
        String size = "10.5";
        assertEquals(Optional.empty(), ValidityCheck.validateGardenSize(size));
    }

    @Test
    void ValidGardenSize_WithValidSizeSeperatedWithComma_ReturnsEmptyOptional() {
        String size = "10,5";
        assertEquals(Optional.empty(), ValidityCheck.validateGardenSize(size));
    }

    @Test
    void ValidGardenSize_WithInvalidNegativeSize_ReturnsOptionalWithErrorMessage() {
        String size = "-10.5";
        assertEquals(Optional.of("Garden size must be a positive number"),
                ValidityCheck.validateGardenSize(size));
    }

    @Test
    void ValidGardenSize_WithInvalidNonNumericSize_ReturnsOptionalWithErrorMessage() {
        String size = "abc";
        assertEquals(Optional.of("Garden size must be a positive number"),
                ValidityCheck.validateGardenSize(size));
    }

    @Test
    void ValidGardenSize_WithInvalidSizeContainingMultipleDots_ReturnsOptionalWithErrorMessage() {
        String size = "10.5.5";
        assertEquals(Optional.of("Garden size must be a positive number"),
                ValidityCheck.validateGardenSize(size));
    }

    @Test
    void ValidGardenSize_WithInvalidSizeContainingMultipleCommas_ReturnsOptionalWithErrorMessage() {
        String size = "10,5,5";
        assertEquals(Optional.of("Garden size must be a positive number"),
                ValidityCheck.validateGardenSize(size));
    }

    @Test
    void ValidGardenSize_WithInvalidSizeSeperatedByDotAndComma_ReturnsOptionalWithErrorMessage() {
        String size = "10.5,5";
        assertEquals(Optional.of("Garden size must be a positive number"),
                ValidityCheck.validateGardenSize(size));
    }

    @Test
    void ValidGardenSize_WithInvalidSizeBlankString_ReturnsOptionalWithErrorMessage() {
        String size = "";
        assertEquals(Optional.of("Garden size must be a positive number"),
                ValidityCheck.validateGardenSize(size));
    }
}
