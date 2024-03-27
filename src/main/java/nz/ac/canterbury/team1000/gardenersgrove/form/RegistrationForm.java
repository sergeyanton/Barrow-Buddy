package nz.ac.canterbury.team1000.gardenersgrove.form;

import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.util.Password;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Entity used to parse and store the data sent through a register POST request
 */
public class RegistrationForm {
    private String firstName;
    private String lastName;
    private Boolean noSurnameCheckBox;
    private String email;
    private String password;
    private String retypePassword;
    private String dob;

    public Boolean getNoSurnameCheckBox() {
        return noSurnameCheckBox;
    }

    public void setNoSurnameCheckBox(Boolean noSurnameCheckBox) {
        this.noSurnameCheckBox = noSurnameCheckBox;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRetypePassword() {
        return retypePassword;
    }

    public void setRetypePassword(String retypePassword) {
        this.retypePassword = retypePassword;
    }

    public String getDob() {
        return dob;
    }

    public LocalDate getDobLocalDate() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(dob, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User getUer() {
        return new User(
            this.firstName,
                this.lastName,
                this.email,
                Password.hashPassword(this.password),
                this.getDobLocalDate()
        );
    }
}
