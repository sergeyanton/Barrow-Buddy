package nz.ac.canterbury.team1000.gardenersgrove.controller.dataCollection;

import org.springframework.web.bind.annotation.RequestParam;

//TODO this class may not be useful?
public class ResetPasswordData {
    private String email;
    public ResetPasswordData(@RequestParam(name = "email") String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
