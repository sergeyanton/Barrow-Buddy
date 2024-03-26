package nz.ac.canterbury.team1000.gardenersgrove.form;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

class ErrorAdder {
    private BindingResult bindingResult;
    private String objectName;

    ErrorAdder (BindingResult bindingResult, String objectName) {
        this.bindingResult = bindingResult;
        this.objectName = objectName;
    }

    public void add(String fieldName, String message) {
        this.bindingResult.addError(new FieldError(this.objectName, fieldName, message));
    }
}

public class Validator {
    private static DateTimeFormatter validDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void validateRegistrationForm(RegistrationForm registrationForm, BindingResult bindingResult) {
        ErrorAdder errors = new ErrorAdder(bindingResult, "registrationForm");
        if (checkBlank(registrationForm.getFirstName())) {
            errors.add("firstName", "{First/Last} name cannot be empty");
        } else if (checkOverMaxLength(registrationForm.getFirstName(), 64)) {
            errors.add("firstName", "{First/Last} name must be 64 characters long or less");
        } else if (checkOnlyHasLettersSpacesHyphensApostrophes(registrationForm.getFirstName())) {
            errors.add("firstName", "{First/Last} name must only include letters, spaces, hyphens or apostrophes");
        }
        if (!registrationForm.getNoSurnameCheckBox()) {
            if (checkBlank(registrationForm.getLastName())) {
                errors.add("lastName", "{First/Last} name cannot be empty");
            } else if (checkOverMaxLength(registrationForm.getLastName(), 64)) {
                errors.add("lastName", "{First/Last} name must be 64 characters long or less");
            } else if (checkOnlyHasLettersSpacesHyphensApostrophes(registrationForm.getLastName())) {
                errors.add("lastName", "{First/Last} name must only include letters, spaces, hyphens or apostrophes");
            }
        }
        if (checkBlank(registrationForm.getEmail()) || checkEmailIsInvalid(registrationForm.getEmail())) {
            errors.add("email", "Email address must be in the form ‘jane@doe.nz’");
        }
        if (checkBlank(registrationForm.getPassword())) {
            errors.add("password", "Password cannot be empty");
        } else if (checkPasswordIsInvalid(registrationForm.getPassword())) {
            errors.add("password", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character.");
        }
        if (!registrationForm.getPassword().equals(registrationForm.getRetypePassword())) {
            errors.add("retypePassword", "Passwords do not match");
        }
        if (checkDateNotInCorrectFormat(registrationForm.getDob())) {
            errors.add("dob", "Date in not in valid format, DD/MM/YYYY");
        } else if (!checkDateBefore(registrationForm.getDob(), LocalDate.now().plusDays(1))) {
            errors.add("dob", "Date cannot be in the future");
        } else if (checkDateBefore(registrationForm.getDob(), LocalDate.now().minusYears(13))) {
            errors.add("dob", "You must be 13 years or older to create an account");
        } else if (!checkDateBefore(registrationForm.getDob(), LocalDate.now().minusYears(120))) {
            errors.add("dob", "The maximum age allowed is 120 years");
        }
    }

    public static boolean checkBlank(String string) {
        return string.isBlank();
    }

    public static boolean checkOverMaxLength(String string, Integer maxLength) {
        return string.length() > maxLength;
    }

    public static boolean checkOnlyHasLettersSpacesHyphensApostrophes (String string) {
        return checkNotMatchesRegex(string, "^[a-zA-Z\\s'-]+$");
    }
    public static boolean checkEmailIsInvalid (String string) {
        return checkNotMatchesRegex(string, "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$");
    }
    public static boolean checkPasswordIsInvalid (String string) {
        return checkNotMatchesRegex(string, "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{8,}$");
    }
    public static boolean checkNotMatchesRegex (String string, String pattern) {
        return !string.matches(pattern);
    }
    public static boolean checkDateNotInCorrectFormat(String dateString) {
        try {
            LocalDate.parse(dateString, validDateFormat);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
    public static boolean checkDateBefore(String dateString, LocalDate before) {
        try {
            LocalDate date = LocalDate.parse(dateString, validDateFormat);

            return date.isBefore(before);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
