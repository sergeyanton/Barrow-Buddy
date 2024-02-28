package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fname;

    @Column(nullable = true)
    private String lname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = true)
    private String dateOfBirth;


    protected User() {}

    public User(String fname, String lname, String password, String email, String dateOfBirth) {
        this.fname = fname;
        this.lname = lname;
        this.password = password;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
    }

    public Long getId() {
        return id;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    private static Validator checkName(String userName) {
        Validator isValid = new Validator(true, "Ok");
        if (userName.isBlank()) {isValid.setValid(false,"{First/Last} name cannot be empty and must only include letters, spaces, hyphens or apostrophes");}
        if (userName.length() > 64) {isValid.setValid(false,"{First/Last} name cannot be empty and must only include letters, spaces, hyphens or apostrophes");}
        if (!userName.matches("^[a-zA-Z\\s'-]+$")) {isValid.setValid(false,"{First/Last} name cannot be empty and must only include letters, spaces, hyphens or apostrophes");}
        return isValid;
    }

    private static Validator checkEmail(String userEmail) {
        Validator isValid = new Validator(true, "Ok");
        if (userEmail.isBlank()) {isValid.setValid(false,"Email address must be in the form ‘jane@doe.nz’");}
        if (!userEmail.matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$")) {isValid.setValid(false,"Email address must be in the form ‘jane@doe.nz’");}
        //TODO check if email is in database
        return isValid;
    }

    private static Validator checkDob(String userDob) {
        //Format: DD/MM/YYYY
        Validator isValid = new Validator(true, "Ok");
        if (userDob.isBlank()) {isValid.setValid(false,"Date in not in valid format, DD/MM/YYYY)");}
        int day = Integer.parseInt(userDob.substring(0, 2));
        int month = Integer.parseInt(userDob.substring(3, 5));
        int year = Integer.parseInt(userDob.substring(6));

        if(day < 1){isValid.setValid(false,"Date in not in valid format, DD/MM/YYYY)");}
        if (month %2 != 0 && day > 30) {isValid.setValid(false,"Date in not in valid format, DD/MM/YYYY)");}
        if (month %2 == 0 && day > 31) {isValid.setValid(false,"Date in not in valid format, DD/MM/YYYY)");}
        if (year %4 == 0 && month == 2 && day > 28){isValid.setValid(false,"Date in not in valid format, DD/MM/YYYY)");}

        if (year < 2011){isValid.setValid(false,"You must be 13 years or older to create an account");}
        if (year > 2144){isValid.setValid(false,"The maximum age allowed is 120 years");}

        return isValid;
    }

    private static Validator checkPassword(String password) {
        Validator isValid = new Validator(true,"Ok");
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{8,}$\n")) {
            isValid.setValid(false,"Your password must be  at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character.");
        }
        return isValid;
    }
}
