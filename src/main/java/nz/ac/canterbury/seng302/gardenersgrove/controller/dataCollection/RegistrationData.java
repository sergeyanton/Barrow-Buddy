package nz.ac.canterbury.seng302.gardenersgrove.controller.dataCollection;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

import static nz.ac.canterbury.seng302.gardenersgrove.Validation.InputValidation.hashPassword;


public class RegistrationData {
    private String fName;
    private String lName;
    private Boolean noSurnameCheckBox;
    private String email;
    private String password;
    private String retypePassword;
    private LocalDate dob;

    public RegistrationData(
        @RequestParam(name = "email") String email,
        @RequestParam(name = "fName") String fName,
        @RequestParam(name = "lName") String lName,
        @RequestParam(name = "dob") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dob,
        @RequestParam(name = "password") String password,
        @RequestParam(name = "password") String retypePassword,
        @RequestParam(name = "noSurnameCheckBox", required = false) String noSurnameCheckBox
    )  {
        this.fName = fName;
        this.lName = lName;
        this.noSurnameCheckBox = noSurnameCheckBox != null;
        this.email = email;
        this.password = password;
        this.retypePassword = retypePassword;
        this.dob = dob;

    }


    public static User createNewUser(RegistrationData user) {
        return new User(user.getfName(), user.getlName(), user.getEmail(), hashPassword(user.getPassword()), user.getDob());
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public Boolean getNoSurnameCheckBox() {
        return noSurnameCheckBox;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRetypePassword() {
        return retypePassword;
    }

    public LocalDate getDob() {
        return dob;
    }
}
