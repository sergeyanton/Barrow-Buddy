package nz.ac.canterbury.team1000.gardenersgrove.form;

import nz.ac.canterbury.team1000.gardenersgrove.entity.User;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.*;

/**
 * Entity used to parse and store the data sent through a register POST request
 */
public class RegistrationForm {
    protected String firstName;
    protected String lastName;
    protected Boolean noSurnameCheckBox;
    protected String email;
    protected String dob;
    protected String password;
    protected String retypePassword;
    protected String picturePath;

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getNoSurnameCheckBox() {
        return noSurnameCheckBox;
    }

    public void setNoSurnameCheckBox(Boolean noSurnameCheckBox) {
        this.noSurnameCheckBox = noSurnameCheckBox;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public LocalDate getDobLocalDate() {
        try {
            return LocalDate.parse(dob, VALID_DATE_FORMAT);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRetypePassword() {
        return retypePassword;
    }

    public void setRetypePassword(String retypePassword) {
        this.retypePassword = retypePassword;
    }

    public void setProfilePictureUrl(String picturePath) {
        this.picturePath = picturePath;
    }
    public String getProfilePictureUrl() {
        return this.picturePath;
    }

    /**
     * Generates a User object with the values from the form.
     *
     * @return new User with attributes directly from the input values in the form.
     */
    public User getUser(PasswordEncoder passwordEncoder) {
        return new User(
                this.firstName,
                this.lastName,
                this.email,
                passwordEncoder.encode(this.password),
                this.getDobLocalDate(),
                "/images/default_pic.jpg"
        );
    }

    /**
     * Validates the registration form data and adds validation errors to the BindingResult.
     *
     * @param registrationForm the RegistrationForm object representing the user's registration data
     * @param bindingResult    the BindingResult object for validation errors
     */
    public static void validate(RegistrationForm registrationForm, BindingResult bindingResult) {
        // Create an ErrorAdder instance with the BindingResult and object name
        ErrorAdder errors = new ErrorAdder(bindingResult, "registrationForm");

        // Validate first name
        if (checkBlank(registrationForm.getFirstName())) {
            errors.add("firstName", "First name cannot be empty", registrationForm.getFirstName());
        } else if (checkOverMaxLength(registrationForm.getFirstName(), 64)) {
            errors.add("firstName", "First name must be 64 characters long or less", registrationForm.getFirstName());
        } else if (!checkOnlyHasLettersMacronsSpacesHyphensApostrophes(registrationForm.getFirstName())) {
            errors.add("firstName", "First name must only include letters, spaces, hyphens or apostrophes", registrationForm.getFirstName());
        }

        // Validate last name only if checkbox is not checked
        if (!registrationForm.getNoSurnameCheckBox()) {
            if (checkBlank(registrationForm.getLastName())) {
                errors.add("lastName", "Last name cannot be empty", registrationForm.getLastName());
            } else if (checkOverMaxLength(registrationForm.getLastName(), 64)) {
                errors.add("lastName", "Last name must be 64 characters long or less", registrationForm.getLastName());
            } else if (!checkOnlyHasLettersMacronsSpacesHyphensApostrophes(registrationForm.getLastName())) {
                errors.add("lastName", "Last name must only include letters, spaces, hyphens or apostrophes", registrationForm.getLastName());
            }
        }

        // Validate email
        if (checkBlank(registrationForm.getEmail()) || checkEmailIsInvalid(registrationForm.getEmail())) {
            errors.add("email", "Email address must be in the form ‘jane@doe.nz’", registrationForm.getEmail());
        } else if (checkOverMaxLength(registrationForm.getEmail(), MAX_DB_STR_LEN)) {
            errors.add("email", "Email address must be " + MAX_DB_STR_LEN + " characters long or less", registrationForm.getEmail());
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

        // Validate date of birth (if there is one)
        if (!checkBlank(registrationForm.getDob())) {
            if (checkDateNotInCorrectFormat(registrationForm.getDob())) {
                errors.add("dob", "Date in not in valid format, DD/MM/YYYY", registrationForm.getDob());
            } else if (!checkDateBefore(registrationForm.getDob(), LocalDate.now().plusDays(1))) {
                errors.add("dob", "Date cannot be in the future", registrationForm.getDob());
            } else if (!checkDateBefore(registrationForm.getDob(), LocalDate.now().minusYears(13).plusDays(1))) {
                errors.add("dob", "You must be 13 years or older to create an account", registrationForm.getDob());
            } else if (checkDateBefore(registrationForm.getDob(), LocalDate.now().minusYears(120))) {
                errors.add("dob", "The maximum age allowed is 120 years", registrationForm.getDob());
            }
        }
    }
}
