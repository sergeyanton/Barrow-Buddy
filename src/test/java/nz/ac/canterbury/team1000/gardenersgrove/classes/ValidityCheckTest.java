package nz.ac.canterbury.team1000.gardenersgrove.classes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import nz.ac.canterbury.team1000.gardenersgrove.classes.ValidityCheck;

public class ValidityCheckTest {

    @Test
    void CheckName_WithValidName_ReturnsEmptyOptional() {
        String name = "Valid Tree Name";
        assertEquals(Optional.empty(), ValidityCheck.validGardenName(name));
    }

    @Test
    void CheckName_LettersWithAccent_ReturnsEmptyOptional() {
        String name = "àèìòùÀÈÌÒÙáéíóúýÁÉÍÓÚÝâêîôûÂÊÎÔÛãñõÃÑÕäëïöüÿÄËÏÖÜŸåÅæÆœŒçÇðÐøØß";
        assertEquals(Optional.empty(), ValidityCheck.validGardenName(name));
    }

    @Test
    void CheckName_WithEmptyName_ReturnsOptionalWithErrorMessage() {
        String name = "";
        assertEquals(Optional.of("Garden name cannot by empty"),
                ValidityCheck.validGardenName(name));
    }

    @Test
    void CheckName_WithWhiteSpaceName_ReturnsOptionalWithErrorMessage() {
        String name = "             ";
        assertEquals(Optional.of("Garden name cannot by empty"),
                ValidityCheck.validGardenName(name));
    }

    @Test
    void CheckName_WithInvalidName_ReturnsOptionalWithErrorMessage() {
        String name = "Invalid Name Because Of The Dollar $ign ";
        assertEquals(Optional.of(
                        "Garden name must only include letters, numbers, spaces, dots, hyphens or apostrophes"),
                ValidityCheck.validGardenName(name));
    }

    /**
     * This test involves the user entering a garden location that contains letters, since letters
     * are considered valid characters, applying it to the ValidityCheck method
     * validGardenLocation() should get the method to return true.
     */
    @Test
    public void UserEntersGardenLocation_LocationContainsLetters_ReturnsEmptyOptional() {
        String location = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        assertEquals(Optional.empty(), ValidityCheck.validGardenLocation(location));
    }

    /**
     * This test involves the user entering a garden location that only contains numbers, since
     * numbers are considered valid characters, applying it to the ValidityCheck method
     * validGardenLocation() should get the method to return true.
     */
    @Test
    public void UserEntersGardenLocation_LocationContainsNumbers_ReturnsTrue() {
        String location = "1234567890";
        assertEquals(Optional.empty(), ValidityCheck.validGardenLocation(location));

    }

    /**
     * This test involves the user entering a garden name that only contains valid non-alphanumeric
     * symbols, since letters are considered valid characters, applying it to the ValidityCheck
     * method validGardenName() should get the method to return true.
     */
    @Test
    public void UserEntersGardenLocation_LocationContainsValidCharacters_ReturnsTrue() {
        String location = "., -'";
        assertEquals(Optional.empty(), ValidityCheck.validGardenLocation(location));

    }

    /**
     * This test involves the user entering an empty garden name, since the garden name cannot be
     * empty, applying it to the ValidityCheck method validGardenName() should get it to return
     * false
     */
    @Test
    public void UserEntersGardenLocation_LocationIsEmpty_ReturnsFalse() {
        String location = "";
        assertEquals(Optional.of("Location cannot be empty"),
                ValidityCheck.validGardenLocation(location));

    }

    /**
     * This test involves the user entering a garden name that contains only whitespace, since the
     * garden name cannot be empty, applying it to the ValidityCheck method validGardenName() should
     * get it to return false as the method removes any trailing whitespace
     */
    @Test
    public void UserEntersGardenLocation_LocationIsOnlyWhitespace_ReturnsFalse() {
        String location = "    ";
        assertEquals(Optional.of("Location cannot be empty"),
                ValidityCheck.validGardenLocation(location));

    }

    /**
     * This test involves the user entering a garden name that contains invalid characters, since
     * the garden name cannot be empty, applying it to the ValidityCheck method validGardenName()
     * should get it to return false
     */
    @Test
    public void UserEntersGardenLocation_LocationContainsInvalidCharacters_ReturnsFalse() {
        String location = "`~_+=[]{}:;<>/?!@#$%^&*()";
        assertEquals(Optional.of(
                        "Location name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes"),
                ValidityCheck.validGardenLocation(location));

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
    void ValidGardenSize_WithValidEmptySizeString_ReturnsEmptyOptional() {
        String size = "";
        assertEquals(Optional.empty(), ValidityCheck.validateGardenSize(size));
    }

    @Test
    void ValidGardenSize_WithValidWhitespaceSizeString_ReturnsEmptyOptional() {
        String size = "         ";
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
    void ValidGardenSize_WithInvalidSeperator_ReturnsOptionalWithErrorMessage() {
        // This test is important because it checks that the number can't be seperated by any
        // character. Previously the '.' in the regex was not escaped and any seperator worked
        String size = "2x2"; // 2.2 seperated by an x
        assertEquals(Optional.of("Garden size must be a positive number"),
                ValidityCheck.validateGardenSize(size));
    }

    @Test
    void ValidatePlantName_ValidName_ReturnsEmptyOptional() {
        String plantName = "Valid Plant Name";
        assertEquals(Optional.empty(), ValidityCheck.validatePlantName(plantName));
    }

    @Test
    void CheckPlantName_LettersWithAccent_ReturnsEmptyOptional() {
        String name = "àèìòùÀÈÌÒÙáéíóúýÁÉÍÓÚÝâêîôûÂÊÎÔÛãñõÃÑÕäëïöüÿÄËÏÖÜŸåÅæÆœŒçÇðÐøØß";
        assertEquals(Optional.empty(), ValidityCheck.validatePlantName(name));
    }

    @Test
    void ValidatePlantName_NameBlank_ReturnsOptionalErrorMessage() {
        String plantName = "";
        assertEquals(Optional.of("Plant name must not be empty"),
                ValidityCheck.validatePlantName(plantName));
    }

    @Test
    void ValidatePlantName_NameContainsInvalidCharacters_ReturnsOptionalErrorMessage() {
        String plantName = "~`!@#$%^&*()_+={}[]:;<>/?";
        assertEquals(Optional.of(
                        "Plant name must only include letters, numbers, spaces, dots, hyphens or apostrophes"),
                ValidityCheck.validatePlantName(plantName));
    }

    @Test
    void ValidatePlantName_NameIsWhitespace_ReturnsOptionalErrorMessage() {
        String plantName = "     ";
        assertEquals(Optional.of("Plant name must not be empty"),
                ValidityCheck.validatePlantName(plantName));
    }

    @Test
    void ValidatePlantCount_PlantCountValid_ReturnsEmptyOptional() {
        String count = "8";
        assertEquals(Optional.empty(), ValidityCheck.validatePlantCount(count));
    }

    @Test
    void ValidatePlantCount_PlantCountEmpty_ReturnsEmptyOptional() {
        String count = "";
        assertEquals(Optional.empty(), ValidityCheck.validatePlantCount(count));
    }

    @Test
    void ValidatePlantCount_PlantCountWhitespace_ReturnsEmptyOptional() {
        String count = "            ";
        assertEquals(Optional.empty(), ValidityCheck.validatePlantCount(count));
    }

    @Test
    void ValidatePlantCount_PlantCountZero_ReturnsOptionalErrorMessage() {
        String count = "0";
        assertEquals(Optional.of("Plant count must only be a positive integer"),
                ValidityCheck.validatePlantCount(count));
    }

    @Test
    void ValidatePlantCount_PlantCountNegative_ReturnsOptionalErrorMessage() {
        String count = "-1";
        assertEquals(Optional.of("Plant count must only be a positive integer"),
                ValidityCheck.validatePlantCount(count));
    }

    @Test
    void ValidatePlantCount_PlantCountNonNumeric_ReturnsOptionalErrorMessage() {
        String count = "count";
        assertEquals(Optional.of("Plant count must only be a positive integer"),
                ValidityCheck.validatePlantCount(count));
    }

    /**
     * Boundary test for the plant count, the plant count is the maximum value that can be stored as an integer
     */
    @Test
    void ValidatePlantCount_PlantCountTooBig_ReturnsOptionalErrorMessage() {
        String count = "2147483648";
        assertEquals(Optional.of("Plant count must only be a positive integer"),
                ValidityCheck.validatePlantCount(count));
    }


    @Test
    void ValidatePlantCount_PlantCountTooBig_ReturnsOptional() {
        String count = "2147483647";
        assertEquals(Optional.empty(), ValidityCheck.validatePlantCount(count));
    }

    @Test
    void ValidateDescription_DescriptionValid_ReturnsEmptyOptional() {
        String description = "This plant is cool.";
        assertEquals(Optional.empty(), ValidityCheck.validatePlantDescription(description));
    }

    @Test
    void ValidateDescription_ValidEmpty_ReturnsEmptyOptional() {
        String description = "";
        assertEquals(Optional.empty(), ValidityCheck.validatePlantDescription(description));
    }

    @Test
    void ValidateDescription_ValidWhitespace_ReturnsEmptyOptional() {
        String description = "               ";
        assertEquals(Optional.empty(), ValidityCheck.validatePlantDescription(description));
    }

    @Test
    void ValidateDescription_DescriptionOver512Characters_ReturnsOptionalErrorMessage() {
        String description = "a".repeat(513); // Creates a string with 513 characters
        assertEquals(Optional.of("Plant description must be less than 512 characters"),
                ValidityCheck.validatePlantDescription(description));
    }

    @Test
    void ValidateDate_Valid_ReturnsEmptyOptional() {
        String date = "2024-10-15";
        assertEquals(Optional.empty(), ValidityCheck.validateDate(date));
    }

    @Test
    void ValidateDate_ValidEmpty_ReturnsEmptyOptional() {
        String date = "";
        assertEquals(Optional.empty(), ValidityCheck.validateDate(date));
    }

    @Test
    void ValidateDate_ValidWhitespace_ReturnsEmptyOptional() {
        String date = "             ";
        assertEquals(Optional.empty(), ValidityCheck.validateDate(date));
    }

    @Test
    void ValidateDate_InvalidDates_ReturnsOptionalWithErrorMessage() {
        String date1 = "10000-10-15";
        String date2 = "absolutely not a date";
        String date3 = "15-10-2003";
        String date4 = "2024-02-31";
        String date5 = "0000-13-15";


        assertEquals(Optional.of("Date is not in valid format, (DD/MM/YYYY)"),
                ValidityCheck.validateDate(date1));
        assertEquals(Optional.of("Date is not in valid format, (DD/MM/YYYY)"),
                ValidityCheck.validateDate(date2));
        assertEquals(Optional.of("Date is not in valid format, (DD/MM/YYYY)"),
                ValidityCheck.validateDate(date3));
        assertEquals(Optional.of("Date is not in valid format, (DD/MM/YYYY)"),
                ValidityCheck.validateDate(date4));
        assertEquals(Optional.of("Date is not in valid format, (DD/MM/YYYY)"),
                ValidityCheck.validateDate(date5));
    }

    @Test
    void ValidateDate_InvalidMonthEdgeCases_ReturnsOptionalWithErrorMessage() {
        String date1 = "2020-13-15";
        String date2 = "2020-00-15";

        assertEquals(Optional.of("Date is not in valid format, (DD/MM/YYYY)"),
                ValidityCheck.validateDate(date1));
        assertEquals(Optional.of("Date is not in valid format, (DD/MM/YYYY)"),
                ValidityCheck.validateDate(date2));
    }
}
