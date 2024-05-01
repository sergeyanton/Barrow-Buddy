package nz.ac.canterbury.team1000.gardenersgrove.form;

import org.springframework.validation.BindingResult;

import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.checkBlank;
import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.checkPasswordIsInvalid;

/**
 * Entity used to parse and store the data sent through a resetPassword POST request
 */
public class ResetPasswordForm {
    protected String newPassword;
    protected String retypePassword;

    public String getNewPassword() {
        return newPassword;
    }
    public String getRetypePassword() {
        return retypePassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    public void setRetypePassword(String retypePassword) {
        this.retypePassword = retypePassword;
    }

    /**
     * Validates the resetPassword form data and adds validation errors to the BindingResult.
     *
     * @param resetPasswordForm  the ResetPasswordForm object representing the entered password data
     * @param bindingResult      the BindingResult object for validation errors
     */
    public static void validate(ResetPasswordForm resetPasswordForm, BindingResult bindingResult) {
        // Create ErrorAdder instance with the BindingResult and object name
        ErrorAdder errors = new ErrorAdder(bindingResult, "resetPasswordForm");

        // Validate new password
        if (checkBlank(resetPasswordForm.getNewPassword())) {
            errors.add("newPassword", "New password cannot be empty", resetPasswordForm.getNewPassword());
        } else if (checkPasswordIsInvalid(resetPasswordForm.getNewPassword())) {
            errors.add("newPassword", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character.", resetPasswordForm.getNewPassword());
        }

        // Validate new password and retyped password match
        if (!resetPasswordForm.getNewPassword().equals(resetPasswordForm.getRetypePassword())) {
            errors.add("retypePassword", "The new passwords do not match", resetPasswordForm.getRetypePassword());
        }
    }
}
