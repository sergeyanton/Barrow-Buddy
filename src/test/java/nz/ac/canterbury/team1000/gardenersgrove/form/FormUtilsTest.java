package nz.ac.canterbury.team1000.gardenersgrove.form;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;

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
        Assertions.assertTrue(
                FormUtils.checkOnlyHasLettersMacronsSpacesHyphensApostrophes("abc def-ghi'jkl"));
    }

    @Test
    void checkOnlyHasLettersSpacesHyphensApostrophes_WithOnlyLetters_ReturnsTrue() {
        Assertions.assertTrue(
            FormUtils.checkOnlyHasLettersMacronsSpacesHyphensApostrophes("John"));
    }

    @Test
    void checkOnlyHasLettersSpacesHyphensApostrophes_WithOneLetter_ReturnsTrue() {
        Assertions.assertTrue(
            FormUtils.checkOnlyHasLettersMacronsSpacesHyphensApostrophes("J"));
    }
    @Test
    void checkOnlyHasLettersSpacesHyphensApostrophes_WithOneLetterWithMacron_ReturnsTrue() {
        Assertions.assertTrue(
            FormUtils.checkOnlyHasLettersMacronsSpacesHyphensApostrophes("Ō"));
    }

    @Test
    void checkOnlyHasLettersSpacesHyphensApostrophes_WithOneLetterAndAnotherLetterWithMacron_ReturnsTrue() {
        Assertions.assertTrue(
            FormUtils.checkOnlyHasLettersMacronsSpacesHyphensApostrophes("Ōtautahi"));
    }

    @Test
    void checkOnlyHasLettersSpacesHyphensApostrophes_EndsWithApostrophe_ReturnsTrue() {
        Assertions.assertTrue(
            FormUtils.checkOnlyHasLettersMacronsSpacesHyphensApostrophes("John'"));
    }

    @Test
    void checkOnlyHasLettersSpacesHyphensApostrophes_HasOneWhitespace_ReturnsTrue() {
        Assertions.assertTrue(
            FormUtils.checkOnlyHasLettersMacronsSpacesHyphensApostrophes("John Doe"));
    }

    @Test
    void checkOnlyHasLettersSpacesHyphensApostrophes_OneHyphenInBetween_ReturnsTrue() {
        Assertions.assertTrue(
            FormUtils.checkOnlyHasLettersMacronsSpacesHyphensApostrophes("John-Doe"));
    }

    @Test
    void checkOnlyHasLettersSpacesHyphensApostrophes_WithNumbers_ReturnsFalse() {
        Assertions.assertFalse(FormUtils.checkOnlyHasLettersMacronsSpacesHyphensApostrophes("123"));
    }

    @Test
    void checkOnlyHasLettersSpacesHyphensApostrophes_WithSpecialCharacters_ReturnsFalse() {
        Assertions.assertFalse(FormUtils.checkOnlyHasLettersMacronsSpacesHyphensApostrophes("!@#"));
    }

    @Test
    void checkOnlyHasLettersSpacesHyphensApostrophes_WithOnlyHyphen_ReturnsFalse() {
        Assertions.assertFalse(FormUtils.checkOnlyHasLettersMacronsSpacesHyphensApostrophes("-"));
    }

    @Test
    void checkOnlyHasLettersSpacesHyphensApostrophes_WithOnlyApostrophe_ReturnsFalse() {
        Assertions.assertFalse(FormUtils.checkOnlyHasLettersMacronsSpacesHyphensApostrophes("'"));
    }

    @Test
    void checkOnlyHasLettersSpacesHyphensApostrophes_EndsWithHyphen_ReturnsFalse() {
        Assertions.assertFalse(FormUtils.checkOnlyHasLettersMacronsSpacesHyphensApostrophes("John-"));
    }

    @Test
    void checkOnlyHasLettersSpacesHyphensApostrophes_WithDoubleHyphen_ReturnsFalse() {
        Assertions.assertFalse(FormUtils.checkOnlyHasLettersMacronsSpacesHyphensApostrophes("John--Doe"));
    }

    @Test
    void checkOnlyHasLettersSpacesHyphensApostrophes_WithDoubleApostrophe_ReturnsFalse() {
        Assertions.assertFalse(FormUtils.checkOnlyHasLettersMacronsSpacesHyphensApostrophes("John''Doe"));
    }

    @Test
    void checkOnlyHasLettersSpacesHyphensApostrophes_StartsWithHyphen_ReturnsFalse() {
        Assertions.assertFalse(FormUtils.checkOnlyHasLettersMacronsSpacesHyphensApostrophes("-John"));
    }

    @Test
    void checkOnlyHasLettersSpacesHyphensApostrophes_StartsWithApostrophe_ReturnsFalse() {
        Assertions.assertFalse(FormUtils.checkOnlyHasLettersMacronsSpacesHyphensApostrophes("'John"));
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
    void checkDoubleIsInvalid_OverMaximumValue_ReturnsFalse() {
        String size = "72000.1";
        Assertions.assertFalse(FormUtils.checkDoubleIsInvalid(size));
    }

    @Test
    void checkDoubleTooBig_JustMaxValue_ReturnsFalse() {
        String size = "72000.0";
        Assertions.assertFalse(FormUtils.checkDoubleTooBig(size));
    }

    @Test
    void checkDoubleTooBig_NotAValidDoubleString_ReturnsFalse() {
        String size = "invalid double obviously";
        Assertions.assertFalse(FormUtils.checkDoubleTooBig(size));
    }

    @Test
    void checkNumberTooBig_MaximumPlantCount_ReturnsFalse() {
        String size = "268000";
        Assertions.assertFalse(FormUtils.checkNumberTooBig(size, FormUtils.MAX_PLANT_COUNT));
    }

    @Test
    void checkNumberTooBig_OverMaximumPlantCount_ReturnsTrue() {
        String size = "268001";
        Assertions.assertTrue(FormUtils.checkNumberTooBig(size, FormUtils.MAX_PLANT_COUNT));
    }

    @Test
    void checkNumberTooBig_LeadingZeros_ReturnsFalse() {
        String size = "0".repeat(10000);
        Assertions.assertFalse(FormUtils.checkNumberTooBig(size, FormUtils.MAX_PLANT_COUNT));
    }

    @Test
    void checkNumberTooBig_NotAValidIntegerString_ReturnsFalse() {
        String size = "invalid integer obviously";
        Assertions.assertFalse(FormUtils.checkNumberTooBig(size, FormUtils.MAX_PLANT_COUNT));
    }

    @Test
    void checkNotPositiveInteger_ValidInteger_ReturnsFalse() {
        String size = "3";
        Assertions.assertFalse(FormUtils.checkNotPositiveInteger(size));
    }

    @Test
    void checkNotPositiveInteger_Double_ReturnsTrue() {
        String size = "1.2";
        Assertions.assertTrue(FormUtils.checkNotPositiveInteger(size));
    }

    @Test
    void checkNotPositiveInteger_LeadingZeros_ReturnTrue() {
        String size = "0".repeat(10000);
        Assertions.assertTrue(FormUtils.checkNotPositiveInteger(size));
    }

    @Test
    void checkNotPositiveInteger_NotAValidIntegerString_ReturnsTrue() {
        String size = "invalid integer obviously";
        Assertions.assertTrue(FormUtils.checkNotPositiveInteger(size));
    }

    @Test
    void checkImageWrongType_ValidPng_ReturnsFalse() {
        MultipartFile image = new MockMultipartFile("pictureFile", "newPfp.png", "image/png",
                "file contents".getBytes());
        Assertions.assertFalse(FormUtils.checkImageWrongType(image));
    }

    @Test
    void checkImageWrongType_ValidJpeg_ReturnsFalse() {
        MultipartFile image = new MockMultipartFile("pictureFile", "newPfp.jpeg", "image/jpeg",
                "file contents".getBytes());
        Assertions.assertFalse(FormUtils.checkImageWrongType(image));
    }

    @Test
    void checkImageWrongType_ValidSvg_ReturnsFalse() {
        MultipartFile image = new MockMultipartFile("pictureFile", "newPfp.svg", "image/svg+xml",
                "file contents".getBytes());
        Assertions.assertFalse(FormUtils.checkImageWrongType(image));
    }

    @Test
    void checkImageWrongType_InvalidType_ReturnsTrue() {
        MultipartFile image = new MockMultipartFile("pictureFile", "newPfp.webp", "image/webp",
                "file contents".getBytes());
        Assertions.assertTrue(FormUtils.checkImageWrongType(image));
    }

    @Test
    void checkImageTooBig_BoundarySize_ReturnsFalse() {
        byte[] exactly10mb = new byte[10 * 1024 * 1024];
        MultipartFile image =
                new MockMultipartFile("pictureFile", "newPfp.png", "image/png", exactly10mb);
        Assertions.assertFalse(FormUtils.checkImageTooBig(image));
    }

    @Test
    void checkImageTooBig_OverSizeLimit_ReturnsTrue() {
        byte[] over10mb = new byte[10 * 1024 * 1024 + 1];
        MultipartFile image =
                new MockMultipartFile("pictureFile", "newPfp.png", "image/png", over10mb);
        Assertions.assertTrue(FormUtils.checkImageTooBig(image));
    }

    @Test
    void dateToString_DateWithSingleDigitMonthAndDay_ReturnsDateStringWithZeroPrefixes() {
        LocalDate date = LocalDate.of(2024, 4, 4);

        String dateString = FormUtils.dateToString(date);
        assertEquals("04/04/2024", dateString);
    }

    @Test
    void dateToString_DateWithTwoDigitMonthAndDay_ReturnsDateString() {
        LocalDate date = LocalDate.of(2024, 12, 12);

        String dateString = FormUtils.dateToString(date);
        assertEquals("12/12/2024", dateString);
    }

    @Test
    void dateToString_NullDate_ReturnsEmptyString() {
        LocalDate date = null;

        String dateString = FormUtils.dateToString(date);
        assertEquals("", dateString);
    }

    @Test
    void checkIntegerOutsideRange_WithIntegerStringWithinRange_ReturnsFalse() {
        String value = "5";
        int min = 0;
        int max = 10;

        Assertions.assertFalse(FormUtils.checkIntegerOutsideRange(value, min, max));
    }

    @Test
    void checkIntegerOutsideRange_WithIntegerStringEqualToMin_ReturnsFalse() {
        String value = "0";
        int min = 0;
        int max = 10;

        Assertions.assertFalse(FormUtils.checkIntegerOutsideRange(value, min, max));
    }

    @Test
    void checkIntegerOutsideRange_WithIntegerStringEqualToMax_ReturnsFalse() {
        String value = "10";
        int min = 0;
        int max = 10;

        Assertions.assertFalse(FormUtils.checkIntegerOutsideRange(value, min, max));
    }

    @Test
    void checkIntegerOutsideRange_WithIntegerStringBelowMin_ReturnsTrue() {
        String value = "-1";
        int min = 0;
        int max = 10;

        Assertions.assertTrue(FormUtils.checkIntegerOutsideRange(value, min, max));
    }

    @Test
    void checkIntegerOutsideRange_WithIntegerStringAboveMax_ReturnsTrue() {
        String value = "11";
        int min = 0;
        int max = 10;

        Assertions.assertTrue(FormUtils.checkIntegerOutsideRange(value, min, max));
    }

    @Test
    void checkIntegerOutsideRange_WithNonIntegerString_ReturnsTrue() {
        String value = "abc";
        int min = 0;
        int max = 10;

        Assertions.assertTrue(FormUtils.checkIntegerOutsideRange(value, min, max));
    }

    @Test
    void checkIntegerOutsideRange_WithNoUpperBoundAndIntegerStringAboveLowerBound_ReturnsFalse() {
        String value = "5";
        int min = 0;

        Assertions.assertFalse(FormUtils.checkIntegerOutsideRange(value, min, null));
    }

    @Test
    void checkIntegerOutsideRange_WithNoUpperBoundAndIntegerStringEqualToLowerBound_ReturnsFalse() {
        String value = "0";
        int min = 0;

        Assertions.assertFalse(FormUtils.checkIntegerOutsideRange(value, min, null));
    }

    @Test
    void checkIntegerOutsideRange_WithNoUpperBoundAndIntegerStringBelowLowerBound_ReturnsTrue() {
        String value = "-1";
        int min = 0;

        Assertions.assertTrue(FormUtils.checkIntegerOutsideRange(value, min, null));
    }

    @Test
    void checkIntegerOutsideRange_WithNoLowerBoundAndIntegerStringBelowUpperBound_ReturnsFalse() {
        String value = "5";
        int max = 10;

        Assertions.assertFalse(FormUtils.checkIntegerOutsideRange(value, null, max));
    }

    @Test
    void checkIntegerOutsideRange_WithNoLowerBoundAndIntegerStringEqualToUpperBound_ReturnsFalse() {
        String value = "10";
        int max = 10;

        Assertions.assertFalse(FormUtils.checkIntegerOutsideRange(value, null, max));
    }

    @Test
    void checkIntegerOutsideRange_WithNoLowerBoundAndIntegerStringAboveUpperBound_ReturnsTrue() {
        String value = "11";
        int max = 10;

        Assertions.assertTrue(FormUtils.checkIntegerOutsideRange(value, null, max));
    }

    @Test
    void checkIntegerOutsideRange_WithNoBoundsAndNonIntegerString_ReturnsTrue() {
        String value = "abc";

        Assertions.assertTrue(FormUtils.checkIntegerOutsideRange(value, null, null));
    }

    @Test
    void checkIntegerOutsideRange_InRangeDoubleString_ReturnsTrueItIsNotSupported() {
        String value = "5.0";
        int min = 0;
        int max = 10;

        Assertions.assertTrue(FormUtils.checkIntegerOutsideRange(value, min, max));
    }

    @Test
    void checkDoubleExceedMaxValue_WithDoubleStringAndMax_ReturnsFalse() {
        String value = "5.0";
        double max = 10.0;
        Assertions.assertFalse(FormUtils.checkDoubleExceedMaxValue(value, max));
    }

    @Test
    void checkDoubleExceedMaxValue_WithDoubleStringEqualToMaxZero_ReturnsFalse() {
        String value = "0.0";
        double max = 0.0;
        Assertions.assertFalse(FormUtils.checkDoubleExceedMaxValue(value, max));
    }

    @Test
    void checkDoubleExceedMaxValue_WithDoubleStringEqualToMax_ReturnsFalse() {
        String value = "10.0";
        double max = 10.0;
        Assertions.assertFalse(FormUtils.checkDoubleExceedMaxValue(value, max));
    }

    @Test
    void checkDoubleExceedMaxValue_WithDoubleStringAboveMax_ReturnsTrue() {
        String value = "10.1";
        double max = 10.0;
        Assertions.assertTrue(FormUtils.checkDoubleExceedMaxValue(value, max));
    }

    @Test
    void checkDoubleExceedMaxValue_WithNonDoubleString_ReturnsTrue() {
        String value = "abc";
        double max = 10.0;
        Assertions.assertTrue(FormUtils.checkDoubleExceedMaxValue(value, max));
    }

    @Test
    void checkDoubleExceedMaxValue_WithNoMaxValueStringValid_ReturnsFalse() {
        String value = "5.0";
        Assertions.assertFalse(FormUtils.checkDoubleExceedMaxValue(value, null));
    }

    @Test
    void checkDoubleExceedMaxValue_WithNoMaxValueStringZero_ReturnsFalse() {
        String value = "0.0";
        Assertions.assertFalse(FormUtils.checkDoubleExceedMaxValue(value, null));
    }

    @Test
    void checkDoubleOutsideRange_WithNoMaxValueAndNonDoubleString_ReturnsTrue() {
        String value = "abc";
        Assertions.assertTrue(FormUtils.checkDoubleExceedMaxValue(value, null));
    }

    @Test
    void checkDoubleOutsideRange_WithCommaDecimalSeparator_ReturnsFalse() {
        String value = "5,0";
        double max = 10.0;
        Assertions.assertFalse(FormUtils.checkDoubleExceedMaxValue(value, max));
    }

    @Test
    void checkDoubleNotPositive_WithValueZero_ReturnsTrue() {
        String value = "0";
        Assertions.assertTrue(FormUtils.checkDoubleNotPositive(value));
    }

    @Test
    void checkDoubleNotPositive_WithValueNegative_ReturnsTrue() {
        String value = "-1";
        Assertions.assertTrue(FormUtils.checkDoubleNotPositive(value));
    }

    @Test
    void checkDoubleNotPositive_WithValueNegativeZero_ReturnsTrue() {
        String value = "-0";
        Assertions.assertTrue(FormUtils.checkDoubleNotPositive(value));
    }

    @Test
    void checkDoubleNotPositive_WithValueSmall_ReturnsFalse() {
        String value = "0.000001";
        Assertions.assertFalse(FormUtils.checkDoubleNotPositive(value));
    }

    @Test
    void checkDoubleNotPositive_WithValuePositive_ReturnsFalse() {
        String value = "1";
        Assertions.assertFalse(FormUtils.checkDoubleNotPositive(value));
    }

    @Test
    void checkFieldIncludesOtherFields_WithFieldContainingOneOtherField_ReturnsTrue() {
        String field = "I include the secretWord";
        String otherField = "secretWord";
        Assertions.assertTrue(FormUtils.checkFieldIncludesOtherFields(field, otherField));
    }

    @Test
    void checkFieldIncludesOtherFields_WithFieldNotContainingOneOtherField_ReturnsFalse() {
        String field = "I do not include the s3cretWord";
        String otherField = "secretWord";
        Assertions.assertFalse(FormUtils.checkFieldIncludesOtherFields(field, otherField));
    }

    @Test
    void checkFieldIncludesOtherFields_WithFieldContainingMultipleOtherFields_ReturnsTrue() {
        String field = "I include the secretWord and the secretWord2";
        String[] otherFields = {"secretWord", "secretWord2"};
        Assertions.assertTrue(FormUtils.checkFieldIncludesOtherFields(field, otherFields));
    }

    @Test
    void checkFieldIncludesOtherFields_WithFieldNotContainingMultipleOtherFields_ReturnsFalse() {
        String field = "I do not include the s3cretWord or the s3cretWord2";
        String[] otherFields = {"secretWord", "secretWord2"};
        Assertions.assertFalse(FormUtils.checkFieldIncludesOtherFields(field, otherFields));
    }

    @Test
    void checkFieldIncludesOtherFields_WithOtherFieldInDiffrentCase_ReturnsTrue() {
        String field = "I include the secretWord";
        String otherField = "SecretWord";
        Assertions.assertTrue(FormUtils.checkFieldIncludesOtherFields(field, otherField));
    }

    @Test
    void checkFieldIncludesOtherFields_WithFieldContainingOtherFieldWithSpaces_ReturnsTrue() {
        String field = "I include the secret Word";
        String otherField = "secret Word";
        Assertions.assertTrue(FormUtils.checkFieldIncludesOtherFields(field, otherField));
    }
}
