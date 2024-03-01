package nz.ac.canterbury.seng302.gardenersgrove.dataCollection;

public class RegistrationData {
    public String fName;
    public String lName;
    public Boolean noSurnameCheckBox;
    public String email;
    public String address;
    public String password;
    public String retypePassword;
    public String dob;
    private RegistrationData(String fName, String lName, Boolean noSurnameCheckBox, String email, String address, String password, String retypePassword, String dob) {
        this.fName = fName;
        this.lName = lName;
        this.noSurnameCheckBox = noSurnameCheckBox;
        this.email = email;
        this.address = address;
        this.password = password;
        this.retypePassword = retypePassword;
        this.dob = dob;
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

    public String getAddress() {
        return address;
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
