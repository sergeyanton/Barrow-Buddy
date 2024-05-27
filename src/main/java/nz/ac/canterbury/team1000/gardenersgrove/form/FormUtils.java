package nz.ac.canterbury.team1000.gardenersgrove.form;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Arrays;
import java.util.List;

/**
 * Helper class for adding validation errors to a BindingResult.
 */
class ErrorAdder {
    // The BindingResult object to which errors will be added
    private final BindingResult bindingResult;

    // The name of the object associated with the errors
    private final String objectName;

    /**
     * Constructs an ErrorAdder with the specified BindingResult and object name.
     *
     * @param bindingResult the BindingResult object to which errors will be added
     * @param objectName the name of the object associated with the errors
     */
    ErrorAdder(BindingResult bindingResult, String objectName) {
        this.bindingResult = bindingResult;
        this.objectName = objectName;
    }

    /**
     * Adds a validation error to the BindingResult.
     *
     * @param fieldName the name of the field for which the error occurred
     * @param message the error message to be added
     */
    public void add(String fieldName, String message, String rejectedValue) {
        this.bindingResult.addError(new FieldError(this.objectName, fieldName, rejectedValue, false,
                null, null, message));
    }
}


/**
 * Utility class for validating form data.
 */
public class FormUtils {

    /**
     * A date formatter used to parse strings of the form "DD/MM/YYYY" into LocalDate objects. Using
     * 'y' here doesn't work well with the STRICT resolver style unlike 'u'. We have to use a STRICT
     * resolver style to reject invalid month lengths. By default, dates such as the 30th of
     * February would be incorrectly accepted.
     */
    public static final DateTimeFormatter VALID_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    /**
     * The default maximum length of strings in the database. Some data might have a lower maximum
     * (first/last name) or a higher maximum (garden description).
     */
    public static final int MAX_DB_STR_LEN = 255;

    public static final int MAX_PLANT_COUNT = 268000;
    /**
     * The default maximum garden size.
     */
    public static final int MAX_GARDEN_SIZE = 72000;

    /**
     * The allowed MIME types for uploaded images.
     */
    public static final List<String> ALLOWED_IMAGE_TYPES =
            Arrays.asList("image/jpeg", "image/png", "image/svg+xml");

    /**
     * The maximum size allowed for an image that the user can upload.
     */
    public static final int MAX_IMAGE_SIZE_BYTES = 10 * 1024 * 1024;

    /**
     * Checks if the given string is blank.
     *
     * @param string the string to check
     * @return true if the string is blank, false otherwise
     */
    public static boolean checkBlank(String string) {
        return string.isBlank();
    }

    /**
     * Checks if the given string exceeds the maximum length.
     *
     * @param string the string to check
     * @param maxLength the maximum length allowed
     * @return true if the string exceeds the maximum length, false otherwise
     */
    public static boolean checkOverMaxLength(String string, Integer maxLength) {
        return string.length() > maxLength;
    }

    /**
     * Checks if the given string is less than the under length.
     *
     * @param string the string to check
     * @param underLength the maximum length allowed
     * @return true if the string is less than the under length, false otherwise
     */
    public static boolean checkUnderLength(String string, Integer underLength) {
        return string.length() < underLength;
    }


    /**
     * Checks if the given string represents a double bigger than the maximum integer value in java.
     * NOTE: Returns false if the string doesn't represent a valid double. Only call this method
     * with valid strings.
     *
     * @param string the string representation of the double to check
     * @return true if the represented double is greater than the maximum java Integer value, false
     *         if the represented double is not too big, or if the string doesn't represent a valid
     *         double
     */
    public static boolean checkDoubleTooBig(String string) {
        try {
            return new BigDecimal(string.replace(",", "."))
                    .compareTo(BigDecimal.valueOf(MAX_GARDEN_SIZE)) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the given string represents an integer bigger than the maximum integer that is
     * provided NOTE: Returns false if the string doesn't represent a valid integer. Only call this
     * method with valid strings.
     *
     * @param providedValue the string representation of the number to check
     * @param maxValue the max value that is provided to compare with
     * @return true if the provided value is greater than the maximum value, false if the provided
     *         value is not too big, or if the string doesn't represent a valid integer
     */
    public static boolean checkNumberTooBig(String providedValue, long maxValue) {
        try {
            return new BigDecimal(providedValue).compareTo(BigDecimal.valueOf(maxValue)) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the given string contains only letters, accented characters, macrons, spaces,
     * hyphens, or apostrophes.
     *
     * @param string the string to check
     * @return true if the string contains only valid characters, false otherwise
     */
    public static boolean checkOnlyHasLettersMacronsSpacesHyphensApostrophes(String string) {
        return !checkNotMatchesRegex(string, "^[\\p{L}’'-]+(?:\\s[\\p{L}’'-]+)*$");
    }

    /**
     * Checks if the given string is only made up of alphanumeric characters, commas, dots, hyphens,
     * and apostrophes.
     *
     * @param string the string to check
     * @return true if the string contains only valid characters, false otherwise
     */
    public static boolean checkValidGardenName(String string) {
        return !checkNotMatchesRegex(string, "^[\\p{L}0-9\\s,.'-]+$");
    }

    /**
     * Checks if the given string must have at least one letter.
     *
     * @param string the string to check
     * @return true if the string contains at least one letter, false otherwise
     */
    public static boolean checkValidGardenDescription(String string) {
        return !checkNotMatchesRegex(string, "(?=.*?[A-Za-z]).+");
    }

    /**
     * Checks if the given string is only made up of alphanumeric characters, commas, dots, hyphens,
     * and apostrophes.
     *
     * @param string the string to check
     * @return true if the string contains only valid characters, false otherwise
     */
    public static boolean checkValidLocationName(String string) {
        return checkValidGardenName(string); // may have different definition later
    }

    /**
     * Checks if the given string is only made up of alphanumeric characters, commas, dots, hyphens,
     * and apostrophes.
     *
     * @param string the string to check
     * @return true if the string contains only valid characters, false otherwise
     */
    public static boolean checkValidPlantName(String string) {
        return checkValidGardenName(string); // may have different definition later
    }

    /**
     * Checks if the given string doesn't represent a valid non-negative Double, where the decimal
     * point can also be a comma. NOTE: Does NOT check upper bound for the number. NOTE: Returns
     * true for blank strings.
     *
     * @param string the string to check
     * @return true if the string does not represent a valid double, including blank strings
     */
    public static boolean checkDoubleIsInvalid(String string) {
        return checkNotMatchesRegex(string, "^\\d*[,.]?\\d+$");
    }

    /**
     * Checks if the given string doesn't represent a valid non-negative Integer NOTE: Does NOT
     * check upper bound for the number. NOTE: Returns true for blank strings.
     *
     * @param string the string to check
     * @return true if the string does not represent a valid integer, including blank strings
     */
    public static boolean checkNotPositiveInteger(String string) {
        return checkNotMatchesRegex(string, "^[1-9]\\d*$");
    }

    /**
     * Checks if an integer fits within the specified range. NOTE: Returns true for blank or invalid
     * strings. Should not be used to check for invalid integers.
     *
     * @param string the string to check
     * @param min the minimum value allowed, or null if there is no minimum
     * @param max the maximum value allowed, or null if there is no maximum
     * @return true if the integer is outside the specified range, false otherwise
     */
    public static boolean checkIntegerOutsideRange(String string, Integer min, Integer max) {
        try {
            int value = Integer.parseInt(string);
            if (min != null && value < min)
                return true;
            if (max != null && value > max)
                return true;
            return false;
        } catch (NumberFormatException e) {
            // make sure we fail if the string is not a valid integer
            return true;
        }
    }

    /**
     * Checks if a double doesn't go over the max value. NOTE: Returns true for blank or invalid
     * strings. Should not be used to check for invalid doubles.
     *
     * @param string the string to check
     * @param max the maximum value allowed, or null if there is no maximum
     * @return true if the double is too big of what is provided as max value, false otherwise
     */
    public static boolean checkDoubleExceedMaxValue(String string, Double max) {
        try {
            double value = Double.parseDouble(string.replace(",", "."));
            if (max != null && value > max)
                return true;
            return false;
        } catch (NumberFormatException e) {
            // make sure we fail if the string is not a valid double
            return true;
        }
    }

    /**
     * Checks if a string (gets converted to double) is positive NOTE: Returns true for invalid
     * strings. Should not be used to check for invalid doubles.
     *
     * @param string the string to check
     * @return true if the double is outside the specified range, false otherwise
     */
    public static boolean checkDoubleNotPositive(String string) {
        try {
            double value = Double.parseDouble(string.replace(",", "."));
            if (value <= 0)
                return true;
            return false;
        } catch (NumberFormatException e) {
            // make sure we fail if the string is not a valid double
            return true;
        }
    }

    /**
     * Checks if the given email address is invalid.
     *
     * @param string the email address to check
     * @return true if the email address is invalid, false otherwise
     */
    public static boolean checkEmailIsInvalid(String string) {
        return checkNotMatchesRegex(string, "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$");
    }

    /**
     * Checks if the given password is invalid.
     *
     * @param string the password to check
     * @return true if the password is invalid, false otherwise
     */
    public static boolean checkPasswordIsInvalid(String string) {
        return checkNotMatchesRegex(string,
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{8,}$");
    }

    /**
     * Checks if the given string does not match the specified regular expression pattern.
     *
     * @param string the string to check
     * @param pattern the regular expression pattern to match against
     * @return true if the string does not match the pattern, false otherwise
     */
    public static boolean checkNotMatchesRegex(String string, String pattern) {
        return !string.matches(pattern);
    }

    /**
     * Checks if the given date string is not in the correct format.
     *
     * @param dateString the date string to check
     * @return true if the date string is not in the correct format, false otherwise
     */
    public static boolean checkDateNotInCorrectFormat(String dateString) {
        try {
            LocalDate.parse(dateString, VALID_DATE_FORMAT);
        } catch (DateTimeParseException e) {
            // date is incorrect format
            return true;
        }
        // date is correct format
        return false;
    }

    /**
     * Checks if the given date string is before the specified date.
     *
     * @param dateString the date string to check
     * @param before the date to compare against
     * @return true if the date string is before the specified date, false otherwise
     */
    public static boolean checkDateBefore(String dateString, LocalDate before) {
        try {
            LocalDate date = LocalDate.parse(dateString, VALID_DATE_FORMAT);
            return date.isBefore(before);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Checks if the given image file is an accepted type
     *
     * @param image the image file to check
     * @return true if the image is NOT an accepted type
     */
    public static boolean checkImageWrongType(MultipartFile image) {
        return !ALLOWED_IMAGE_TYPES.contains(image.getContentType());
    }

    /**
     * Checks if the given image file is too big
     *
     * @param image the image file to check
     * @return true if the image is over the size limit
     */
    public static boolean checkImageTooBig(MultipartFile image) {
        return image.getSize() > MAX_IMAGE_SIZE_BYTES;
    }

    /**
     * Converts LocalDate to a string in the format "DD/MM/YYYY".
     *
     * @param date the date to convert
     * @return the string representation of the date in the format "DD/MM/YYYY" or an empty string
     *         if the date is null.
     */
    public static String dateToString(LocalDate date) {
        if (date == null)
            return "";
        return date.format(VALID_DATE_FORMAT);
    }

    /**
     * Checks if one field includes any of the other fields
     * 
     * @param field the field to check
     * @param otherFields the other fields to check against
     * @return true if the field includes any of the other fields, false otherwise
     */
    public static boolean checkFieldIncludesOtherFields(String field, String... otherFields) {
        for (String otherField : otherFields) {
            if (field.toLowerCase().contains(otherField.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
