package nz.ac.canterbury.team1000.gardenersgrove.form;

import org.springframework.validation.BindingResult;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EditUserForm extends RegistrationForm {
    public void setFromUser(User user) {
        this.firstName = user.getFname();
        this.lastName = user.getLname();
        this.email = user.getEmail();
        this.dob = user.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.noSurnameCheckBox = this.lastName == null || this.lastName.isEmpty();
        this.password = "";
        this.retypePassword = "";
    }

    /**
     * Validates the EditUserForm object and adds any errors to the BindingResult.
     * 
     * @param editUserForm the EditUserForm object to validate
     * @param bindingResult the BindingResult object to which errors will be added
     * @param existingUser the User object representing the user being edited
     */
    public static void validate(EditUserForm editUserForm, BindingResult bindingResult, User existingUser) {
        // Create an ErrorAdder instance with the BindingResult and object name
        ErrorAdder errors = new ErrorAdder(bindingResult, "registrationForm");

        // Validate first name
        if (checkBlank(editUserForm.getFirstName())) {
            errors.add("firstName", "{First/Last} name cannot be empty", editUserForm.getFirstName());
        } else if (checkOverMaxLength(editUserForm.getFirstName(), 64)) {
            errors.add("firstName", "{First/Last} name must be 64 characters long or less", editUserForm.getFirstName());
        } else if (!checkOnlyHasLettersSpacesHyphensApostrophes(editUserForm.getFirstName())) {
            errors.add("firstName", "{First/Last} name must only include letters, spaces, hyphens or apostrophes", editUserForm.getFirstName());
        }

        // Validate last name only if checkbox is not checked
        if (!editUserForm.getNoSurnameCheckBox()) {
            if (checkBlank(editUserForm.getLastName())) {
                errors.add("lastName", "{First/Last} name cannot be empty", editUserForm.getLastName());
            } else if (checkOverMaxLength(editUserForm.getLastName(), 64)) {
                errors.add("lastName", "{First/Last} name must be 64 characters long or less", editUserForm.getLastName());
            } else if (!checkOnlyHasLettersSpacesHyphensApostrophes(editUserForm.getLastName())) {
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
        } else if (!checkDateBefore(editUserForm.getDob(), LocalDate.now().minusYears(13).plusDays(1))) {
            errors.add("dob", "You must be 13 years or older to create an account", editUserForm.getDob());
        } else if (checkDateBefore(editUserForm.getDob(), LocalDate.now().minusYears(120).plusDays(1))) {
            errors.add("dob", "The maximum age allowed is 120 years", editUserForm.getDob());
        }
    }
}
