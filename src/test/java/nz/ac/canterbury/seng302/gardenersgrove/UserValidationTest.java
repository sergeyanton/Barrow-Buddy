package nz.ac.canterbury.seng302.gardenersgrove;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Validator;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static nz.ac.canterbury.seng302.gardenersgrove.Validation.InputValidation.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class UserValidationTest {
    private User user;



    // No Error cases
    @Test
    void firstNameValidCheck_noError() {
        user = new User("Fabian","Gilson","fabian.gilson@canterbury.ac.nz", "Fabian123!", LocalDate.parse("01-01-2009"));

        Validator fnameValidator = checkName(user.getFname());

        assertTrue(fnameValidator.getStatus());
    }

    @Test
    void lastNameValidCheck_noError() {
        user = new User("Fabian","Gilson","fabian.gilson@canterbury.ac.nz"
                ,  "Fabian123!" ,LocalDate.parse("01/01/2009"));

        Validator lnameValidator = checkName(user.getLname());
        assertTrue(lnameValidator.getStatus());
    }

   @Autowired
   private UserRepository userRepository;
    @Test
    void emailNameValidCheck_noError() {
        UserService userService = new UserService(userRepository);
        user = new User("Fabian","Gilson","fabian.gilson@canterbury.ac.nz"
                ,  "Fabian123!" ,LocalDate.parse("01/01/2009"));

        Validator emailValidator = checkEmail(user.getEmail(),userService);
        assertTrue(emailValidator.getStatus());
    }

    @Test
    void dobValidCheck_noError() {
        user = new User("Fabian","Gilson","fabian.gilson@canterbury.ac.nz",
                "Fabian123!",LocalDate.parse("01/01/2009"));
        Validator dobValidator = checkDob(user.getDateOfBirth());
        assertTrue(dobValidator.getStatus());
    }

    // Error cases
    @Test
    void firstNameValidCheck_error() {
        user = new User("","Gilson","fabian.gilson@canterbury.ac.nz"
                , "Fabian123!" ,LocalDate.parse("01/01/2009"));

        Validator fnameValidator = checkName(user.getFname());
        assertFalse(fnameValidator.getStatus());
    }

    @Test
    void lastNameValidCheck_error() {
        user = new User("Fabian","","fabian.gilson@canterbury.ac.nz"
                , "Fabian123!" ,LocalDate.parse("01/01/2009"));

        Validator lnameValidator = checkName(user.getLname());
        assertFalse(lnameValidator.getStatus());
    }

    @Test
    void emailNameValidCheck_error() {
        UserService userService = new UserService(userRepository);
        user = new User("Fabian","Gilson","fabian"
                ,  "Fabian123!" ,LocalDate.parse("01/01/2009"));
        Validator emailValidator = checkEmail(user.getEmail(), userService);
        assertFalse(emailValidator.getStatus());
    }


    @Test
    void dobInvalidFormatCheck_error() {
        User invalidUser = new User("Invalid","Person","fabian.gilson@canterbury.ac.nz",
                "Fabian123!",LocalDate.parse("50/50/2001"));
        Validator dobValidator = checkDob(invalidUser.getDateOfBirth());

        assertFalse(dobValidator.getStatus());
    }

    @Test
    void dobInvalidAgeCheck_error() {
        User invalidUser = new User("Invalid","Person","fabian.gilson@canterbury.ac.nz",
                "Fabian123!",LocalDate.parse("09/09/2020"));
        Validator dobValidator = checkDob(invalidUser.getDateOfBirth());

        assertFalse(dobValidator.getStatus());
    }

    @Test
    void dobFutureDateCheck_error() {
        User invalidUser = new User("Invalid","Person","fabian.gilson@canterbury.ac.nz",
                "Fabian123!",LocalDate.parse("09/09/2040"));
        Validator dobValidator = checkDob(invalidUser.getDateOfBirth());
        assertFalse(dobValidator.getStatus());
    }






}
