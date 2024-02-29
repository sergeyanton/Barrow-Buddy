package nz.ac.canterbury.seng302.gardenersgrove.classes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class ValidityCheckTest {
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
}
