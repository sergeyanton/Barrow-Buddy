package nz.ac.canterbury.team1000.gardenersgrove.form;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Entity used to parse and store the data sent through a register POST request
 */
public class RegistrationForm {
    @NotBlank(message = "{First/Last} name cannot be empty")
    @Max(value = 64, message = "{First/Last} name must be 64 characters long or less")
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "{First/Last} name must only include letters, spaces, hyphens or apostrophes")
    private String firstName;

    @NotBlank(message = "{First/Last} name cannot be empty")
    @Max(value = 64, message = "{First/Last} name must be 64 characters long or less")
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "{First/Last} name must only include letters, spaces, hyphens or apostrophes")
    private String lastName;

//    @NotNull
//    private Boolean noSurnameCheckBox;
//    @NotBlank(message = "Email address must be in the form ‘jane@doe.nz’")
//    @Email(message = "Email address must be in the form ‘jane@doe.nz’")
//    private String email;
//    @NotBlank(message = "Password cannot be empty.")
//    private String password;
//    @NotNull(message = "Password ")
//    private String retypePassword;
//    @NotBlank
//    private LocalDate dob;

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
