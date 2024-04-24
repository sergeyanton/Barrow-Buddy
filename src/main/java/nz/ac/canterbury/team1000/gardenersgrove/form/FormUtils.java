package nz.ac.canterbury.team1000.gardenersgrove.form;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Helper class for adding validation errors to a BindingResult.
 */
class ErrorAdder {
    // The BindingResult object to which errors will be added
    private BindingResult bindingResult;

    // The name of the object associated with the errors
    private String objectName;

    /**
     * Constructs an ErrorAdder with the specified BindingResult and object name.
     *
     * @param bindingResult the BindingResult object to which errors will be added
     * @param objectName    the name of the object associated with the errors
     */
    ErrorAdder (BindingResult bindingResult, String objectName) {
        this.bindingResult = bindingResult;
        this.objectName = objectName;
    }

    /**
     * Adds a validation error to the BindingResult.
     *
     * @param fieldName the name of the field for which the error occurred
     * @param message   the error message to be added
     */
    public void add(String fieldName, String message, String rejectedValue) {
        this.bindingResult.addError(new FieldError(this.objectName, fieldName, rejectedValue, false, null, null, message));
    }
}

/**
 * Utility class for validating form data.
 */
public class FormUtils {

    /**
     * A date formatter used to parse strings of the form "DD/MM/YYYY" into LocalDate objects.
     * Using 'y' here doesn't work well with the STRICT resolver style unlike 'u'.
     * We have to use a STRICT resolver style to reject invalid month lengths.
     * By default, dates such as the 30th of February would be incorrectly accepted.
     */
    public static final DateTimeFormatter VALID_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    /**
     * The default maximum length of strings in the database.
     * Some data might have a lower maximum (first/last name) or a higher maximum (garden description).
     */
    public static final int MAX_DB_STR_LEN = 255;


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
     * @param string    the string to check
     * @param maxLength the maximum length allowed
     * @return true if the string exceeds the maximum length, false otherwise
     */
    public static boolean checkOverMaxLength(String string, Integer maxLength) {
        return string.length() > maxLength;
    }

    /**
     * Checks if the given string represents a double bigger than the maximum integer value in java.
     * NOTE: Returns false if the string doesn't represent a valid double. Only call this method with
     * valid strings.
     *
     * @param string the string representation of the double to check
     * @return  true if the represented double is greater than the maximum java Integer value,
     *          false if the represented double is not too big, or if the string doesn't represent a valid double
     */
    public static boolean checkDoubleTooBig (String string) {
        try {
            return new BigDecimal(string.replace(",", ".")).compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the given string represents an integer bigger than the maximum integer value in java.
     * NOTE: Returns false if the string doesn't represent a valid integer. Only call this method with
     * valid strings.
     *
     * @param string the string representation of the double to check
     * @return  true if the represented integer is greater than the maximum java Integer value,
     *          false if the represented integer is not too big, or if the string doesn't represent a valid integer
     */
    public static boolean checkIntegerTooBig (String string) {
        try {
            return new BigDecimal(string).compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the given string contains only letters, accented characters, macrons,
     * spaces, hyphens, or apostrophes.
     *
     * @param string the string to check
     * @return true if the string contains only valid characters, false otherwise
     */
    public static boolean checkOnlyHasLettersMacronsSpacesHyphensApostrophes (String string) {
        return !checkNotMatchesRegex(string, "^[\\p{L}’'-]+(?:\\s[\\p{L}’'-]+)*$");
    }

    /**
     * Checks if the given string is only made up of alphanumeric characters, commas,
     * dots, hyphens, and apostrophes.
     *
     * @param string the string to check
     * @return true if the string contains only valid characters, false otherwise
     */
    public static boolean checkValidGardenName (String string) {
        return !checkNotMatchesRegex(string, "^[\\p{L}0-9\\s,.'-]+$");
    }

    /**
     * Checks if the given string is only made up of alphanumeric characters, commas,
     * dots, hyphens, and apostrophes.
     *
     * @param string the string to check
     * @return true if the string contains only valid characters, false otherwise
     */
    public static boolean checkValidLocationName (String string) {
        return checkValidGardenName(string); // may have different definition later
    }

    /**
     * Checks if the given string is only made up of alphanumeric characters, commas,
     * dots, hyphens, and apostrophes.
     *
     * @param string the string to check
     * @return true if the string contains only valid characters, false otherwise
     */
    public static boolean checkValidPlantName (String string) {
        return checkValidGardenName(string); // may have different definition later
    }

    /**
     * Checks if the given string doesn't represent a valid non-negative Double, where the decimal point can also be a comma.
     * NOTE: Does NOT check upper bound for the number.
     * NOTE: Returns true for blank strings.
     *
     * @param string the string to check
     * @return true if the string does not represent a valid double, including blank strings
     */
    public static boolean checkDoubleIsInvalid (String string) {
        return checkNotMatchesRegex(string,"^\\d*[,.]?\\d+$");
    }

    /**
     * Checks if the given string doesn't represent a valid non-negative Integer
     * NOTE: Does NOT check upper bound for the number.
     * NOTE: Returns true for blank strings.
     *
     * @param string the string to check
     * @return true if the string does not represent a valid integer, including blank strings
     */
    public static boolean checkIntegerIsInvalid (String string) {
        return checkNotMatchesRegex(string,"^[0-9]+$");
    }

    /**
     * Checks if the given email address is invalid.
     *
     * @param string the email address to check
     * @return true if the email address is invalid, false otherwise
     */
    public static boolean checkEmailIsInvalid (String string) {
        return checkNotMatchesRegex(string, "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$");
    }

    /**
     * Checks if the given password is invalid.
     *
     * @param string the password to check
     * @return true if the password is invalid, false otherwise
     */
    public static boolean checkPasswordIsInvalid (String string) {
        return checkNotMatchesRegex(string, "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{8,}$");
    }

    /**
     * Checks if the given string does not match the specified regular expression pattern.
     *
     * @param string  the string to check
     * @param pattern the regular expression pattern to match against
     * @return true if the string does not match the pattern, false otherwise
     */
    public static boolean checkNotMatchesRegex (String string, String pattern) {
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
     * @param before     the date to compare against
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
}
