package nz.ac.canterbury.team1000.gardenersgrove.form;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;

import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.*;

public class ForgotPasswordForm {
    protected String email;

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }


    public static void validate(ForgotPasswordForm forgotPasswordForm, BindingResult bindingResult) {
        // Create an ErrorAdder instance with the BindingResult and object name
        ErrorAdder errors = new ErrorAdder(bindingResult, "forgotPasswordForm");

        // Validate email
        if (checkBlank(forgotPasswordForm.getEmail())) {
            errors.add("email", "Email address must not be empty", forgotPasswordForm.getEmail());
        } else if (checkEmailIsInvalid(forgotPasswordForm.getEmail())) {
            errors.add("email", "Email address must be in the form ‘jane@doe.nz’", forgotPasswordForm.getEmail());
        }

    }

}
