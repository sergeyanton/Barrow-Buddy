package nz.ac.canterbury.team1000.gardenersgrove.form;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FormUtilsTest {
    @Test
    void checkBlank_WithEmptyString_ReturnsTrue() {
        Assertions.assertTrue(FormUtils.checkBlank(""));
    }

    @Test
    void checkBlank_WithSpaces_ReturnsTrue() {
        Assertions.assertTrue(FormUtils.checkBlank("   "));
    }

    @Test
    void checkBlank_WithNonEmptyString_ReturnsFalse() {
        Assertions.assertFalse(FormUtils.checkBlank("abc"));
    }

    @Test
    void checkOverMaxLength_WithMax6And7LengthString_ReturnsTrue() {
        Assertions.assertTrue(FormUtils.checkOverMaxLength("1234567", 6));
    }

    @Test
    void checkOverMaxLength_WithMax6And6LengthString_ReturnsFalse() {
        Assertions.assertFalse(FormUtils.checkOverMaxLength("123456", 6));
    }

    @Test
    void checkOverMaxLength_WithMax6And5LengthString_ReturnsFalse() {
        Assertions.assertFalse(FormUtils.checkOverMaxLength("12345", 6));
    }

    @Test
    void checkOnlyHasLettersSpacesHyphensApostrophes_WithLettersSpacesHyphensApostrophes_ReturnsTrue() {
        Assertions.assertTrue(FormUtils.checkOnlyHasLettersSpacesHyphensApostrophes("abc def-ghi'jkl"));
    }

    @Test
    void checkOnlyHasLettersSpacesHyphensApostrophes_WithNumbers_ReturnsFalse() {
        Assertions.assertFalse(FormUtils.checkOnlyHasLettersSpacesHyphensApostrophes("123"));
    }

    @Test
    void checkOnlyHasLettersSpacesHyphensApostrophes_WithSpecialCharacters_ReturnsFalse() {
        Assertions.assertFalse(FormUtils.checkOnlyHasLettersSpacesHyphensApostrophes("!@#"));
    }

    @Test
    void checkEmailIsInvalid_WithInvalidEmailNoAtOrSuffix_ReturnsTrue() {
        Assertions.assertTrue(FormUtils.checkEmailIsInvalid("abc"));
    }

    @Test
    void checkEmailIsInvalid_WithInvalidEmailNoSuffix_ReturnsTrue() {
        Assertions.assertTrue(FormUtils.checkEmailIsInvalid("abc@"));
    }

    @Test
    void checkEmailIsInvalid_WithInvalidEmailNoAt_ReturnsTrue() {
        Assertions.assertTrue(FormUtils.checkEmailIsInvalid("abc.nz"));
    }

    @Test
    void checkEmailIsInvalid_WithInvalidEmailNoPrefix_ReturnsTrue() {
        Assertions.assertTrue(FormUtils.checkEmailIsInvalid("@abc.nz"));
    }

    @Test
    void checkEmailIsInvalid_WithValidEmail_ReturnsFalse() {
        Assertions.assertFalse(FormUtils.checkEmailIsInvalid("jane@doe.nz"));
    }

    @Test
    void checkPasswordIsInvalid_WithPasswordLessThan8Characters_ReturnsTrue() {
        Assertions.assertTrue(FormUtils.checkPasswordIsInvalid("1234567"));
    }

    @Test
    void checkPasswordIsInvalid_WithPassword8Numbers_ReturnsTrue() {
        Assertions.assertTrue(FormUtils.checkPasswordIsInvalid("12345678"));
    }

    @Test
    void checkPasswordIsInvalid_WithPassword8LowerCaseLetters_ReturnsTrue() {
        Assertions.assertTrue(FormUtils.checkPasswordIsInvalid("abcdefgh"));
    }

    @Test
    void checkPasswordIsInvalid_WithPassword8SpecialCharacters_ReturnsTrue() {
        Assertions.assertTrue(FormUtils.checkPasswordIsInvalid("!@#$%^&*"));
    }

    @Test
    void checkPasswordIsInvalid_WithPassword8UpperCaseLetters_ReturnsTrue() {
        Assertions.assertTrue(FormUtils.checkPasswordIsInvalid("ABCDEFGH"));
    }

    @Test
    void checkPasswordIsInvalid_WithPassword8MixedCharactersAndSymbols_ReturnsFalse() {
        Assertions.assertFalse(FormUtils.checkPasswordIsInvalid("Abc123!@#"));
    }

    @Test
    void checkDateNotInCorrectFormat_WithNonSlashSeparatedDate_ReturnsTrue() {
        Assertions.assertTrue(FormUtils.checkDateNotInCorrectFormat("01-01-2000"));
    }

    @Test
    void checkDateNotInCorrectFormat_WithMonthDayYearDate_ReturnsTrue() {
        Assertions.assertTrue(FormUtils.checkDateNotInCorrectFormat("01/28/2000"));
    }

    @Test
    void checkDateNotInCorrectFormat_WithDayMonthYearDate_ReturnsFalse() {
        Assertions.assertFalse(FormUtils.checkDateNotInCorrectFormat("28/01/2000"));
    }

    @Test
    void checkDateNotInCorrectFormat_WithDayMonthYear5DigitYearDate_ReturnsTrue() {
        Assertions.assertTrue(FormUtils.checkDateNotInCorrectFormat("28/01/20000"));
    }

    @Test
    void checkDateNotInCorrectFormat_LeapDayWrongYear_ReturnsTrue() {
        Assertions.assertTrue(FormUtils.checkDateNotInCorrectFormat("29/02/2003"));
    }
    @Test
    void checkDateNotInCorrectFormat_LeapDayRightYear_ReturnsFalse() {
        Assertions.assertFalse(FormUtils.checkDateNotInCorrectFormat("29/02/2004"));
    }

    @Test
    void checkDateWithinBoundary_DateAfter_ReturnsFalse() {
        Assertions.assertFalse(FormUtils.checkDateBefore("03/01/2011", LocalDate.of(2011, 1, 2)));
    }

    @Test
    void checkDateWithinBoundary_DateEqual_ReturnsFalse() {
        Assertions.assertFalse(FormUtils.checkDateBefore("02/01/2011", LocalDate.of(2011, 1, 2)));
    }

    @Test
    void checkDateWithinBoundary_DateBefore_ReturnsTrue() {
        Assertions.assertTrue(FormUtils.checkDateBefore("01/01/2011", LocalDate.of(2011, 1, 2)));
    }

    @Test
    void checkGardenName_WithValidName_ReturnsTrue() {
        String name = "Valid Tree Name";
        Assertions.assertTrue(FormUtils.checkValidGardenName(name));
    }

    @Test
    void checkGardenName_AtypicalLetters_ReturnsTrue() {
        String name = "àèìòùÀÈÌÒÙáéíóúýÁÉÍÓÚÝâêîôûÂÊÎÔÛãñõÃÑÕäëïöüÿÄËÏÖÜŸåÅæÆœŒçÇðÐøØß";
        Assertions.assertTrue(FormUtils.checkValidGardenName(name));
    }

    @Test
    void checkGardenName_WithInvalidName_ReturnsFalse() {
        String name = "Invalid Name Because Of The Dollar $ign ";
        Assertions.assertFalse(FormUtils.checkValidGardenName(name));
    }

    @Test
    void checkGardenLocation_WithValidLocation_ReturnsTrue() {
        String location = "Valid Location Name";
        Assertions.assertTrue(FormUtils.checkValidLocationName(location));
    }

    @Test
    void checkGardenLocation_AtypicalLetters_ReturnsTrue() {
        String location = "àèìòùÀÈÌÒÙáéíóúýÁÉÍÓÚÝâêîôûÂÊÎÔÛãñõÃÑÕäëïöüÿÄËÏÖÜŸåÅæÆœŒçÇðÐøØß";
        Assertions.assertTrue(FormUtils.checkValidLocationName(location));
    }

    @Test
    void checkGardenLocation_WithInvalidLocation_ReturnsFalse() {
        String location = "Invalid Location Because Of The @ Symbol";
        Assertions.assertFalse(FormUtils.checkValidLocationName(location));
    }

    @Test
    void checkDoubleIsInvalid_WithValidZero_ReturnsFalse() {
        String size = "0";
        Assertions.assertFalse(FormUtils.checkDoubleIsInvalid(size));
    }

    @Test
    void checkDoubleIsInvalid_WithValidInteger_ReturnsFalse() {
        String size = "32";
        Assertions.assertFalse(FormUtils.checkDoubleIsInvalid(size));
    }

    @Test
    void checkDoubleIsInvalid_WithValidDecimal_ReturnsFalse() {
        String size = "5.5";
        Assertions.assertFalse(FormUtils.checkDoubleIsInvalid(size));
    }

    @Test
    void checkDoubleIsInvalid_WithValidComma_ReturnsFalse() {
        String size = "5,3";
        Assertions.assertFalse(FormUtils.checkDoubleIsInvalid(size));
    }

    @Test
    void checkDoubleIsInvalid_WithValidDecimalNoIntegerPart_ReturnsFalse() {
        String size = ".123";
        Assertions.assertFalse(FormUtils.checkDoubleIsInvalid(size));
    }
    @Test
    void checkDoubleIsInvalid_WithValidCommaNoIntegerPart_ReturnsFalse() {
        String size = ",123";
        Assertions.assertFalse(FormUtils.checkDoubleIsInvalid(size));
    }

    @Test
    void checkDoubleIsInvalid_BadString_ReturnsTrue() {
        String size = "3 m^2";
        Assertions.assertTrue(FormUtils.checkDoubleIsInvalid(size));
    }
    @Test
    void checkDoubleIsInvalid_OverMaximumIntegerValue_ReturnsFalse() {
        String size = "2147483647.1";
        Assertions.assertFalse(FormUtils.checkDoubleIsInvalid(size));
    }

    @Test
    void checkDoubleTooBig_MaximumIntegerValue_ReturnsFalse() {
        String size = "2147483647"; // maximum integer value
        Assertions.assertFalse(FormUtils.checkDoubleTooBig(size));
    }

    @Test
    void checkDoubleTooBig_OverMaximumIntegerValue_ReturnsTrue() {
        String size = "2147483647.1";
        Assertions.assertTrue(FormUtils.checkDoubleTooBig(size));
    }

    @Test
    void checkDoubleTooBig_NotAValidDoubleString_ReturnsFalse() {
        String size = "invalid double obviously";
        Assertions.assertFalse(FormUtils.checkDoubleTooBig(size));
    }

    @Test
    void checkIntegerTooBig_MaximumIntegerValue_ReturnsFalse() {
        String size = "2147483647"; // maximum integer value
        Assertions.assertFalse(FormUtils.checkIntegerTooBig(size));
    }

    @Test
    void checkIntegerTooBig_OverMaximumIntegerValue_ReturnsTrue() {
        String size = "2147483648";
        Assertions.assertTrue(FormUtils.checkIntegerTooBig(size));
    }

    @Test
    void checkIntegerTooBig_LeadingZeros_ReturnsFalse() {
        String size = "0".repeat(10000); // maximum integer value
        Assertions.assertFalse(FormUtils.checkIntegerTooBig(size));
    }

    @Test
    void checkIntegerTooBig_NotAValidIntegerString_ReturnsFalse() {
        String size = "invalid integer obviously";
        Assertions.assertFalse(FormUtils.checkIntegerTooBig(size));
    }

    @Test
    void checkIntegerIsInvalid_ValidInteger_ReturnsFalse() {
        String size = "3";
        Assertions.assertFalse(FormUtils.checkIntegerIsInvalid(size));
    }

    @Test
    void checkIntegerIsInvalid_Double_ReturnsTrue() {
        String size = "1.2";
        Assertions.assertTrue(FormUtils.checkIntegerIsInvalid(size));
    }

    @Test
    void checkIntegerIsInvalid_LeadingZeros_ReturnFalse() {
        String size = "0".repeat(10000);
        Assertions.assertFalse(FormUtils.checkIntegerIsInvalid(size));
    }

    @Test
    void checkIntegerIsInvalid_NotAValidIntegerString_ReturnsTrue() {
        String size = "invalid integer obviously";
        Assertions.assertTrue(FormUtils.checkIntegerIsInvalid(size));
    }
}
