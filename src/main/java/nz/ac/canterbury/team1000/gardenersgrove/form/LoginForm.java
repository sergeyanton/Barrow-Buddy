package nz.ac.canterbury.team1000.gardenersgrove.form;

import org.springframework.validation.BindingResult;

import static nz.ac.canterbury.team1000.gardenersgrove.form.FormUtils.*;

/**
 * Entity used to parse and store the data sent through a login POST request
 */
public class LoginForm {
    protected String email;
    protected String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Validates the login form data and adds validation errors to the BindingResult.
     *
     * @param loginForm     the LoginForm object representing the user's login data
     * @param bindingResult the BindingResult object for validation errors
     */
    public static void validate(LoginForm loginForm, BindingResult bindingResult) {
        ErrorAdder errors = new ErrorAdder(bindingResult, "loginForm");

        // validate email
        if (checkBlank(loginForm.getEmail()) || checkEmailIsInvalid(loginForm.getEmail())) {
            errors.add("email", "Email address must be in the form ‘jane@doe.nz’", loginForm.getEmail());
        } else if (checkOverMaxLength(loginForm.getEmail(), MAX_DB_STR_LEN)) {
            errors.add("email", "Email address must be " + MAX_DB_STR_LEN + " characters long or less", loginForm.getEmail());
        }

        // validate password
        if (checkBlank(loginForm.getPassword())) {
            errors.add("password", "The email address is unknown, or the password is invalid", loginForm.getPassword());
        }
    }
}
