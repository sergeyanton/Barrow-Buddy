package nz.ac.canterbury.team1000.gardenersgrove.form;

import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.checkBlank;
import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.checkPasswordIsInvalid;

import org.springframework.validation.BindingResult;

import nz.ac.canterbury.team1000.gardenersgrove.entity.User;

public class UpdatePasswordForm {
    protected User currentUser;
    protected String oldPassword;
    protected String newPassword;
    protected String retypeNewPassword;

    public void setFormUser(User user) {
        this.currentUser = user;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getRetypeNewPassword() {
        return retypeNewPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setRetypeNewPassword(String retypeNewPassword) {
        this.retypeNewPassword = retypeNewPassword;
    }

    public static void validate(UpdatePasswordForm updatePasswordForm, BindingResult bindingResult, User existingUser) {
        // Create ErrorAdder instance with the BindingResult and object name
        
    }
}
