package nz.ac.canterbury.team1000.gardenersgrove.form;

import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.checkBlank;
import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.checkFieldIncludesOtherFields;
import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.checkPasswordIsInvalid;

import org.springframework.validation.BindingResult;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;

public class UpdatePasswordForm {
    protected String password;
    protected String newPassword;
    protected String retypeNewPassword;
    protected User existingUser;

    public void setExistingUser(User existingUser) {
        this.existingUser = existingUser;
    }

    public User getExistingUser() {
        return existingUser;
    }

    public String getPassword() {
        return password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getRetypeNewPassword() {
        return retypeNewPassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setRetypeNewPassword(String retypeNewPassword) {
        this.retypeNewPassword = retypeNewPassword;
    }

    public static void validate(UpdatePasswordForm updatePasswordForm,
            BindingResult bindingResult) {
        String invalidPasswordString =
                "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character.";

        // Create ErrorAdder instance with the BindingResult and object name
        ErrorAdder errors = new ErrorAdder(bindingResult, "updatePasswordForm");

        if (updatePasswordForm.getExistingUser() == null) {
            errors.add("existingUser", "User does not exist", null);
        }

        // Validate password (just that it isn't empty - this is not explicitly in the ACs)
        if (checkBlank(updatePasswordForm.getPassword())) {
            errors.add("password", "Password cannot be empty", updatePasswordForm.getPassword());
        }

        // Validate new password
        if (checkBlank(updatePasswordForm.getNewPassword())) {
            errors.add("newPassword", "New password cannot be empty",
                    updatePasswordForm.getNewPassword());
        } else if (checkPasswordIsInvalid(updatePasswordForm.getNewPassword())) {
            errors.add("newPassword", invalidPasswordString, updatePasswordForm.getNewPassword());
        }

        // Validate new password and retyped password match
        if (!updatePasswordForm.getNewPassword()
                .equals(updatePasswordForm.getRetypeNewPassword())) {
            errors.add("retypeNewPassword", "The new passwords do not match",
                    updatePasswordForm.getRetypeNewPassword());
        } else if (updatePasswordForm.getExistingUser() != null) {
            User existingUser = updatePasswordForm.getExistingUser();
            // check that the password does not include other fields
            Boolean includesAnyOtherFields =
                    checkFieldIncludesOtherFields(updatePasswordForm.getNewPassword(),
                            existingUser.getFname(), existingUser.getLname(),
                            existingUser.getEmail(), existingUser.getDateOfBirthString());
            if (includesAnyOtherFields) {
                errors.add("newPassword", invalidPasswordString,
                        updatePasswordForm.getNewPassword());
            }
        }
    }
}
