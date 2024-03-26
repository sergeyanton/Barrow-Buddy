package nz.ac.canterbury.team1000.gardenersgrove.form;
import java.time.LocalDate;

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
}
