package nz.ac.canterbury.team1000.gardenersgrove.form;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
public class FormValidator {
    // Date format for validation
    private static DateTimeFormatter validDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Validates the registration form data and adds validation errors to the BindingResult.
     *
     * @param registrationForm the RegistrationForm object representing the user's registration data
     * @param bindingResult    the BindingResult object for validation errors
     */
    public static void validateRegistrationForm(RegistrationForm registrationForm, BindingResult bindingResult) {
        // Create an ErrorAdder instance with the BindingResult and object name
        ErrorAdder errors = new ErrorAdder(bindingResult, "registrationForm");

        // Validate first name
        if (checkBlank(registrationForm.getFirstName())) {
            errors.add("firstName", "{First/Last} name cannot be empty", registrationForm.getFirstName());
        } else if (checkOverMaxLength(registrationForm.getFirstName(), 64)) {
            errors.add("firstName", "{First/Last} name must be 64 characters long or less", registrationForm.getFirstName());
        } else if (checkOnlyHasLettersSpacesHyphensApostrophes(registrationForm.getFirstName())) {
            errors.add("firstName", "{First/Last} name must only include letters, spaces, hyphens or apostrophes", registrationForm.getFirstName());
        }

        // Validate last name only if checkbox is not checked
        if (!registrationForm.getNoSurnameCheckBox()) {
            if (checkBlank(registrationForm.getLastName())) {
                errors.add("lastName", "{First/Last} name cannot be empty", registrationForm.getLastName());
            } else if (checkOverMaxLength(registrationForm.getLastName(), 64)) {
                errors.add("lastName", "{First/Last} name must be 64 characters long or less", registrationForm.getLastName());
            } else if (checkOnlyHasLettersSpacesHyphensApostrophes(registrationForm.getLastName())) {
                errors.add("lastName", "{First/Last} name must only include letters, spaces, hyphens or apostrophes", registrationForm.getLastName());
            }
        }

        // Validate email
        if (checkBlank(registrationForm.getEmail()) || checkEmailIsInvalid(registrationForm.getEmail())) {
            errors.add("email", "Email address must be in the form ‘jane@doe.nz’", registrationForm.getEmail());
        }

        // Validate password
        if (checkBlank(registrationForm.getPassword())) {
            errors.add("password", "Password cannot be empty", registrationForm.getPassword());
        } else if (checkPasswordIsInvalid(registrationForm.getPassword())) {
            errors.add("password", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character.", registrationForm.getPassword());
        }

        // Validate password match
        if (!registrationForm.getPassword().equals(registrationForm.getRetypePassword())) {
            errors.add("retypePassword", "Passwords do not match", registrationForm.getRetypePassword());
        }

        // Validate date of birth
        if (checkDateNotInCorrectFormat(registrationForm.getDob()) || checkBlank(registrationForm.getDob())) {
            errors.add("dob", "Date in not in valid format, DD/MM/YYYY", registrationForm.getDob());
        } else if (!checkDateBefore(registrationForm.getDob(), LocalDate.now().plusDays(1))) {
            errors.add("dob", "Date cannot be in the future", registrationForm.getDob());
        } else if (!checkDateBefore(registrationForm.getDob(), LocalDate.now().minusYears(13))) {
            errors.add("dob", "You must be 13 years or older to create an account", registrationForm.getDob());
        } else if (checkDateBefore(registrationForm.getDob(), LocalDate.now().minusYears(119))) {
            errors.add("dob", "The maximum age allowed is 120 years", registrationForm.getDob());
        }
    }

    public static void validateEditUserForm(EditUserForm editUserForm, BindingResult bindingResult, User existingUser) {
        // Create an ErrorAdder instance with the BindingResult and object name
        ErrorAdder errors = new ErrorAdder(bindingResult, "registrationForm");

        // Validate first name
        if (checkBlank(editUserForm.getFirstName())) {
            errors.add("firstName", "{First/Last} name cannot be empty", editUserForm.getFirstName());
        } else if (checkOverMaxLength(editUserForm.getFirstName(), 64)) {
            errors.add("firstName", "{First/Last} name must be 64 characters long or less", editUserForm.getFirstName());
        } else if (checkOnlyHasLettersSpacesHyphensApostrophes(editUserForm.getFirstName())) {
            errors.add("firstName", "{First/Last} name must only include letters, spaces, hyphens or apostrophes", editUserForm.getFirstName());
        }

        // Validate last name only if checkbox is not checked
        if (!editUserForm.getNoSurnameCheckBox()) {
            if (checkBlank(editUserForm.getLastName())) {
                errors.add("lastName", "{First/Last} name cannot be empty", editUserForm.getLastName());
            } else if (checkOverMaxLength(editUserForm.getLastName(), 64)) {
                errors.add("lastName", "{First/Last} name must be 64 characters long or less", editUserForm.getLastName());
            } else if (checkOnlyHasLettersSpacesHyphensApostrophes(editUserForm.getLastName())) {
                errors.add("lastName", "{First/Last} name must only include letters, spaces, hyphens or apostrophes", editUserForm.getLastName());
            }
        }

        // Validate email
        if (checkBlank(editUserForm.getEmail()) || checkEmailIsInvalid(editUserForm.getEmail())) {
            errors.add("email", "Email address must be in the form ‘jane@doe.nz’", editUserForm.getEmail());
        }

        // only validate the passwords either of them are not empty
        if (!editUserForm.getPassword().isEmpty() || !editUserForm.getRetypePassword().isEmpty()) {
            // Validate password
            if (checkBlank(editUserForm.getPassword())) {
                errors.add("password", "Password cannot be empty", editUserForm.getPassword());
            } else if (checkPasswordIsInvalid(editUserForm.getPassword())) {
                errors.add("password", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character.", editUserForm.getPassword());
            }
            
            // Validate password match
            if (!editUserForm.getPassword().equals(editUserForm.getRetypePassword())) {
                errors.add("retypePassword", "Passwords do not match", editUserForm.getRetypePassword());
            }
        }

        // Validate date of birth
        if (checkDateNotInCorrectFormat(editUserForm.getDob()) || checkBlank(editUserForm.getDob())) {
            errors.add("dob", "Date in not in valid format, DD/MM/YYYY", editUserForm.getDob());
        } else if (!checkDateBefore(editUserForm.getDob(), LocalDate.now().plusDays(1))) {
            errors.add("dob", "Date cannot be in the future", editUserForm.getDob());
        } else if (!checkDateBefore(editUserForm.getDob(), LocalDate.now().minusYears(13))) {
            errors.add("dob", "You must be 13 years or older to create an account", editUserForm.getDob());
        } else if (checkDateBefore(editUserForm.getDob(), LocalDate.now().minusYears(119))) {
            errors.add("dob", "The maximum age allowed is 120 years", editUserForm.getDob());
        }
    }
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
     * Checks if the given string contains only letters, spaces, hyphens, or apostrophes.
     *
     * @param string the string to check
     * @return true if the string contains only valid characters, false otherwise
     */
    public static boolean checkOnlyHasLettersSpacesHyphensApostrophes (String string) {
        return checkNotMatchesRegex(string, "^[a-zA-Z\\s'-]+$");
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
            LocalDate.parse(dateString, validDateFormat);
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
            LocalDate date = LocalDate.parse(dateString, validDateFormat);
            return date.isBefore(before);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
