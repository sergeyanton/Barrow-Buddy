package nz.ac.canterbury.team1000.gardenersgrove;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import nz.ac.canterbury.team1000.gardenersgrove.validation.Validator;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import static nz.ac.canterbury.team1000.gardenersgrove.validation.InputValidation.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserValidationTest {
    private User user;

    // No Error cases
//    @Test
//    void firstNameValidCheck_noError() {
//        user = new User("Fabian", "Gilson", "fabian.gilson@canterbury.ac.nz", "Fabian123!",
//                LocalDate.parse("01/01/2009", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//
//        Validator fnameValidator = checkName(user.getFname());
//        assertTrue(fnameValidator.getStatus());
//    }
//
//    @Test
//    void lastNameValidCheck_noError() {
//        user = new User("Fabian", "Gilson", "fabian.gilson@canterbury.ac.nz", "Fabian123!",
//                LocalDate.parse("01/01/2009", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//
//        Validator lnameValidator = checkName(user.getLname());
//        assertTrue(lnameValidator.getStatus());
//    }
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    void emailNameValidCheck_noError() {
//        UserService userService = new UserService(userRepository);
//        user = new User("Fabian", "Gilson", "fabian.gilson@canterbury.ac.nz", "Fabian123!",
//                LocalDate.parse("01/01/2009", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//
//        Validator emailValidator = checkEmailSignup(user.getEmail(), userService);
//        assertTrue(emailValidator.getStatus());
//    }

    @Test
    void dobValidCheck_noError() {
        user = new User("Fabian", "Gilson", "fabian.gilson@canterbury.ac.nz", "Fabian123!",
                LocalDate.parse("01/01/2009", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        Validator dobValidator = checkDob(user.getDateOfBirth());
        assertTrue(dobValidator.getStatus());
        assertEquals(dobValidator.getMessage(), "Ok");
    }

//    @Test
//    void passwordValidCheck_noError() {
//        User invalidUser =
//                new User("Invalid", "Person", "fabian.gilson@canterbury.ac.nz", "Fabian123!",
//                        LocalDate.parse("01/01/2009", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//        Validator passwordValidator = checkPassword(invalidUser.getPassword());
//        assertTrue(passwordValidator.getStatus());
//    }
//
//    // Error cases
//    @Test
//    void firstNameEmptyCheck_error() {
//        user = new User("", "Gilson", "fabian.gilson@canterbury.ac.nz", "Fabian123!",
//                LocalDate.parse("01/01/2009", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//
//        Validator fnameValidator = checkName(user.getFname());
//        assertFalse(fnameValidator.getStatus());
//    }
//
//    @Test
//    void lastNameEmptyCheck_error() {
//        user = new User("Fabian", "", "fabian.gilson@canterbury.ac.nz", "Fabian123!",
//                LocalDate.parse("01/01/2009", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//
//        Validator lnameValidator = checkName(user.getLname());
//        assertFalse(lnameValidator.getStatus());
//    }
//
//    @Test
//    void emailInvalidCheck_error() {
//        UserService userService = new UserService(userRepository);
//        user = new User("Fabian", "Gilson", "fabian", "Fabian123!",
//                LocalDate.parse("01/01/2009", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//        Validator emailValidator = checkEmailSignup(user.getEmail(), userService);
//        assertFalse(emailValidator.getStatus());
//    }


    @Test
    void dobInvalidFormatCheck_error() {
        assertThrows(DateTimeParseException.class, () -> {
            User invalidUser = new User("Fabian", "Gilson", "fabian.gilson@canterbury.ac.nz",
                    "Fabian123!",
                    LocalDate.parse("50/50/2001", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        });
    }

    @Test
    void dobYoungAgeCheck_error() {
        User invalidUser =
                new User("Fabian", "Gilson", "fabian.gilson@canterbury.ac.nz", "Fabian123!",
                        LocalDate.now().minusYears(13).plusDays(1));
        Validator dobValidator = checkDob(invalidUser.getDateOfBirth());

        assertFalse(dobValidator.getStatus());
        assertEquals(dobValidator.getMessage(), "You must be 13 years or older to create an account");
    }

    @Test
    void dobOver120YearsOldCheck_error() {
        User invalidUser =
                new User("Fabian", "Gilson", "fabian.gilson@canterbury.ac.nz", "Fabian123!",
                        LocalDate.now().minusYears(120).minusDays(1));
        Validator dobValidator = checkDob(invalidUser.getDateOfBirth());

        assertFalse(dobValidator.getStatus());
        assertEquals(dobValidator.getMessage(), "The maximum age allowed is 120 years");
    }

    @Test
    void dobFutureYearDateCheck_error() {
        User invalidUser = new User("Invalid", "Person", "fabian.gilson@canterbury.ac.nz",
                "Fabian123!", LocalDate.now().plusYears(5));
        Validator dobValidator = checkDob(invalidUser.getDateOfBirth());
        assertFalse(dobValidator.getStatus());
        assertEquals(dobValidator.getMessage(), "Date cannot be in the future");
    }

    @Test
    void dobFutureMonthDateCheck_error() {
        User invalidUser = new User("Invalid", "Person", "fabian.gilson@canterbury.ac.nz",
                "Fabian123!", LocalDate.now().plusMonths(5));
        Validator dobValidator = checkDob(invalidUser.getDateOfBirth());
        assertFalse(dobValidator.getStatus());
        assertEquals(dobValidator.getMessage(), "Date cannot be in the future");
    }

    @Test
    void dobFutureDayDateCheck_error() {
        User invalidUser = new User("Invalid", "Person", "fabian.gilson@canterbury.ac.nz",
                "Fabian123!", LocalDate.now().plusDays(5));
        Validator dobValidator = checkDob(invalidUser.getDateOfBirth());
        assertFalse(dobValidator.getStatus());
        assertEquals(dobValidator.getMessage(), "Date cannot be in the future");
    }

    @Test
    void dobFiveDigitYearCheck_error() {
        User invalidUser = new User("Invalid", "Person", "fabian.gilson@canterbury.ac.nz",
                "Fabian123!", LocalDate.of(20000, 1, 1));
        Validator dobValidator = checkDob(invalidUser.getDateOfBirth());
        assertFalse(dobValidator.getStatus());
        assertEquals(dobValidator.getMessage(), "Date cannot be in the future");
    }

    @Test
    void dobBillionYearsCheck_error() {
        assertThrows(DateTimeException.class, () -> {
            User invalidUser = new User("Invalid", "Person", "fabian.gilson@canterbury.ac.nz",
                    "Fabian123!", LocalDate.of(1000000000, 1, 1));
        });
    }

//    @Test
//    void passwordInvalidFormatCheck_error() {
//        User invalidUser = new User("Invalid", "Person", "fabian.gilson@canterbury.ac.nz", "abc",
//                LocalDate.parse("01/01/2009", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//        Validator passwordValidator = checkPassword(invalidUser.getPassword());
//        assertFalse(passwordValidator.getStatus());
//    }

}
