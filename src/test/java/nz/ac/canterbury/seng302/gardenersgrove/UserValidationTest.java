package nz.ac.canterbury.seng302.gardenersgrove;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.validation.Validator;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static nz.ac.canterbury.seng302.gardenersgrove.validation.InputValidation.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserValidationTest {
    private User user;

    // No Error cases
    @Test
    void firstNameValidCheck_noError() {
        user = new User("Fabian","Gilson","fabian.gilson@canterbury.ac.nz",
                "Fabian123!", LocalDate.parse("01/01/2009", DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        Validator fnameValidator = checkName(user.getFname());
        assertTrue(fnameValidator.getStatus());
    }

    @Test
    void lastNameValidCheck_noError() {
        user = new User("Fabian","Gilson","fabian.gilson@canterbury.ac.nz"
                ,  "Fabian123!" ,LocalDate.parse("01/01/2009", DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        Validator lnameValidator = checkName(user.getLname());
        assertTrue(lnameValidator.getStatus());
    }

   @Autowired
   private UserRepository userRepository;
    @Test
    void emailNameValidCheck_noError() {
        UserService userService = new UserService(userRepository);
        user = new User("Fabian","Gilson","fabian.gilson@canterbury.ac.nz"
                ,  "Fabian123!" ,LocalDate.parse("01/01/2009", DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        Validator emailValidator = checkEmailSignup(user.getEmail(),userService);
        assertTrue(emailValidator.getStatus());
    }

    @Test
    void dobValidCheck_noError() {
        user = new User("Fabian","Gilson","fabian.gilson@canterbury.ac.nz",
                "Fabian123!",LocalDate.parse("01/01/2009", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        Validator dobValidator = checkDob(user.getDateOfBirth());
        assertTrue(dobValidator.getStatus());
    }

    @Test
    void passwordValidCheck_noError() {
        User invalidUser = new User("Invalid","Person","fabian.gilson@canterbury.ac.nz",
                "Fabian123!",LocalDate.parse("01/01/2009", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        Validator passwordValidator = checkPassword(invalidUser.getPassword());
        assertTrue(passwordValidator.getStatus());
    }

    // Error cases
    @Test
    void firstNameEmptyCheck_error() {
        user = new User("","Gilson","fabian.gilson@canterbury.ac.nz"
                , "Fabian123!" ,LocalDate.parse("01/01/2009", DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        Validator fnameValidator = checkName(user.getFname());
        assertFalse(fnameValidator.getStatus());
    }

    @Test
    void lastNameEmptyCheck_error() {
        user = new User("Fabian","","fabian.gilson@canterbury.ac.nz"
                , "Fabian123!" ,LocalDate.parse("01/01/2009", DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        Validator lnameValidator = checkName(user.getLname());
        assertFalse(lnameValidator.getStatus());
    }

    @Test
    void emailInvalidCheck_error() {
        UserService userService = new UserService(userRepository);
        user = new User("Fabian","Gilson","fabian"
                ,  "Fabian123!" ,LocalDate.parse("01/01/2009", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        Validator emailValidator = checkEmailSignup(user.getEmail(), userService);
        assertFalse(emailValidator.getStatus());
    }


    @Test
    void dobInvalidFormatCheck_error() {
        assertThrows(DateTimeParseException.class, () -> {
            User invalidUser = new User("Fabian", "Gilson", "fabian.gilson@canterbury.ac.nz",
                    "Fabian123!", LocalDate.parse("50/50/2001", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        });
    }

    @Test
    void dobInvalidAgeCheck_error() {
        User invalidUser = new User("Fabian","Gilson","fabian.gilson@canterbury.ac.nz",
                "Fabian123!",LocalDate.parse("09/09/2020", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        Validator dobValidator = checkDob(invalidUser.getDateOfBirth());

        assertFalse(dobValidator.getStatus());
    }

    @Test
    void dobFutureYearDateCheck_error() {
        User invalidUser = new User("Invalid","Person","fabian.gilson@canterbury.ac.nz",
                "Fabian123!",LocalDate.now().plusYears(5));
        Validator dobValidator = checkDob(invalidUser.getDateOfBirth());
        assertFalse(dobValidator.getStatus());
    }

    @Test
    void dobFutureMonthDateCheck_error() {
        User invalidUser = new User("Invalid","Person","fabian.gilson@canterbury.ac.nz",
                "Fabian123!",LocalDate.now().plusMonths(5));
        Validator dobValidator = checkDob(invalidUser.getDateOfBirth());
        assertFalse(dobValidator.getStatus());
    }

    @Test
    void dobFutureDayDateCheck_error() {
        User invalidUser = new User("Invalid","Person","fabian.gilson@canterbury.ac.nz",
                "Fabian123!",LocalDate.now().plusDays(5));
        Validator dobValidator = checkDob(invalidUser.getDateOfBirth());
        assertFalse(dobValidator.getStatus());
    }

    @Test
    void passwordInvalidFormatCheck_error() {
        User invalidUser = new User("Invalid","Person","fabian.gilson@canterbury.ac.nz",
                "abc",LocalDate.parse("01/01/2009", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        Validator passwordValidator = checkPassword(invalidUser.getPassword());
        assertFalse(passwordValidator.getStatus());
    }

}
