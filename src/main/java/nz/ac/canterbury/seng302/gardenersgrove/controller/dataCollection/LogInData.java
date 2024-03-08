package nz.ac.canterbury.seng302.gardenersgrove.controller.dataCollection;

import org.springframework.web.bind.annotation.RequestParam;

/**
 * Represents the email and password data taken from the user login.
 */
public class LogInData {
    private String email;
    private String password;

    /**
     * Creates a LogInData object with the requested data from the login page.
     *
     * @param email email address given by user
     * @param password password given by user
     */
    public LogInData (
            @RequestParam(name = "email") String email,
            @RequestParam(name = "password") String password
    ){
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

