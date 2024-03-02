package nz.ac.canterbury.seng302.gardenersgrove.controller.dataCollection;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;

import java.util.Date;

public class RegistrationData {
    private final String fname;
    private final String lname;
    private final Boolean noSurnameCheckBox;
    private final String email;
    private final String password;
    private final String retypePassword;
    private final String dob;


    public RegistrationData(String fname, String lname, Boolean noSurnameCheckBox, String email, String password, String retypePassword, String dob) {
        this.fname = fname;
        this.lname = lname;
        this.noSurnameCheckBox = noSurnameCheckBox;
        this.email = email;
        this.password = password;
        this.retypePassword = retypePassword;
        this.dob = dob;
    }

    public static User createNewUser(RegistrationData user) {
        return new User(user.getfName(), user.getlName(), user.getEmail(), user.getPassword(), user.getPassword());
    }

    public String getfName() {
        return fname;
    }

    public String getlName() {
        return lname;
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

    public String getDob() {
        return dob;
    }
}
