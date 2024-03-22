package nz.ac.canterbury.team1000.gardenersgrove.controller.dataCollection;

import org.springframework.web.bind.annotation.RequestParam;


/**
 * LoginData entity Used to parse and store the data sent through a login POST request
 */
public class LogInData {
    private String email;
    private String password;

    public LogInData(@RequestParam(name = "email") String email,
            @RequestParam(name = "password") String password) {
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
