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
