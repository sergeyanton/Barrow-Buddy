package nz.ac.canterbury.seng302.gardenersgrove.controller.dataCollection;

import org.springframework.web.bind.annotation.RequestParam;


/**
 *
 */
public class LogInData {
    private String email;
    private String password;

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
