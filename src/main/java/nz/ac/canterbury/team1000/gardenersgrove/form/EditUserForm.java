package nz.ac.canterbury.team1000.gardenersgrove.form;

import org.springframework.validation.BindingResult;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import org.springframework.web.multipart.MultipartFile;

import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class EditUserForm {
    protected String firstName;
    protected String lastName;
    protected Boolean noSurnameCheckBox;
    protected String email;
    protected String dob;
    protected String picturePath;
    protected MultipartFile pictureFile;

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

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public MultipartFile getPictureFile() {
        return pictureFile;
    }

    public void setPictureFile(MultipartFile pictureFile) {
        this.pictureFile = pictureFile;
    }

    /**
     * Gets the date of birth as a LocalDate object and not a string.
     * @return entered date of birth as a LocalDate
     */
    public LocalDate getDobLocalDate() {
        try {
            return LocalDate.parse(dob, VALID_DATE_FORMAT);
        } catch (DateTimeParseException e) {
            return null;
        }
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
        ErrorAdder errors = new ErrorAdder(bindingResult, "editUserForm");

        // Validate first name
        if (checkBlank(editUserForm.getFirstName())) {
            errors.add("firstName", "First name cannot be empty", editUserForm.getFirstName());
        } else if (checkOverMaxLength(editUserForm.getFirstName(), 64)) {
            errors.add("firstName", "First name must be 64 characters long or less", editUserForm.getFirstName());
        } else if (!checkOnlyHasLettersMacronsSpacesHyphensApostrophes(editUserForm.getFirstName())) {
            errors.add("firstName", "First name must only include letters, spaces, hyphens or apostrophes", editUserForm.getFirstName());
        }

        // Validate last name only if checkbox is not checked
        if (!editUserForm.getNoSurnameCheckBox()) {
            if (checkBlank(editUserForm.getLastName())) {
                errors.add("lastName", "Last name cannot be empty", editUserForm.getLastName());
            } else if (checkOverMaxLength(editUserForm.getLastName(), 64)) {
                errors.add("lastName", "Last name must be 64 characters long or less", editUserForm.getLastName());
            } else if (!checkOnlyHasLettersMacronsSpacesHyphensApostrophes(editUserForm.getLastName())) {
                errors.add("lastName", "Last name must only include letters, spaces, hyphens or apostrophes", editUserForm.getLastName());
            }
        }

        // Validate email
        if (checkBlank(editUserForm.getEmail()) || checkEmailIsInvalid(editUserForm.getEmail())) {
            errors.add("email", "Email address must be in the form ‘jane@doe.nz’", editUserForm.getEmail());
        } else if (checkOverMaxLength(editUserForm.getEmail(), MAX_DB_STR_LEN)) {
            errors.add("email", "Email address must be " + MAX_DB_STR_LEN + " characters long or less", editUserForm.getEmail());
        }

        // Validate date of birth (if there is one)
        if (!checkBlank(editUserForm.getDob())) {
            if (checkDateNotInCorrectFormat(editUserForm.getDob())) {
                errors.add("dob", "Date is not in valid format, DD/MM/YYYY", editUserForm.getDob());
            } else if (!checkDateBefore(editUserForm.getDob(), LocalDate.now().plusDays(1))) {
                errors.add("dob", "Date cannot be in the future", editUserForm.getDob());
            } else if (!checkDateBefore(editUserForm.getDob(), LocalDate.now().minusYears(13).plusDays(1))) {
                errors.add("dob", "You must be 13 years or older to create an account", editUserForm.getDob());
            } else if (checkDateBefore(editUserForm.getDob(), LocalDate.now().minusYears(121).plusDays(1))) {
                errors.add("dob", "The maximum age allowed is 120 years", editUserForm.getDob());
            }
        }

        // validate image
        if (!editUserForm.getPictureFile().isEmpty()) {
            if (checkImageWrongType(editUserForm.getPictureFile())) {
                errors.add("pictureFile", "Image must be of type png, jpg or svg", null);
            } else if (checkImageTooBig(editUserForm.getPictureFile())) {
                errors.add("pictureFile", "Image must be less than 10MB", null);
            }
        }
    }
}
