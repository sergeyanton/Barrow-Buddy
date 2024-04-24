package nz.ac.canterbury.team1000.gardenersgrove.form;

import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.*;
import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.checkImageTooBig;

public class ProfilePictureForm {
    protected MultipartFile pictureFile;

    public MultipartFile getPictureFile() {
        return pictureFile;
    }

    public void setPictureFile(MultipartFile pictureFile) {
        this.pictureFile = pictureFile;
    }

    /**
     * Validates the ProfilePictureForm object and adds any errors to the BindingResult.
     *
     * @param profilePictureForm the EditUserForm object to validate
     * @param bindingResult the BindingResult object to which errors will be added
     * @param existingUser the User object representing the user being edited
     */
    public static void validate(ProfilePictureForm profilePictureForm, BindingResult bindingResult, User existingUser) {
        // Create an ErrorAdder instance with the BindingResult and object name
        ErrorAdder errors = new ErrorAdder(bindingResult, "editUserForm");

        // validate image
        if (!profilePictureForm.getPictureFile().isEmpty()) {
            if (checkImageWrongType(profilePictureForm.getPictureFile())) {
                errors.add("pictureFile", "Image must be of type png, jpg or svg", null);
            } else if (checkImageTooBig(profilePictureForm.getPictureFile())) {
                errors.add("pictureFile", "Image must be less than 10MB", null);
            }
        }
    }
}
