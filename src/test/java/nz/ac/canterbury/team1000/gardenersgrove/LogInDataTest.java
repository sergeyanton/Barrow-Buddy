package nz.ac.canterbury.team1000.gardenersgrove;

import nz.ac.canterbury.team1000.gardenersgrove.controller.dataCollection.LogInData;
import nz.ac.canterbury.team1000.gardenersgrove.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static nz.ac.canterbury.team1000.gardenersgrove.validation.InputValidation.checkLoginData;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LogInDataTest {

    private LogInData logInData;

    @Test
    void validEmailAndPasswordFormat_noErrorMessage() {
        logInData = new LogInData("abc123@gmail.com", "Password123!");
        Validator error = checkLoginData(logInData);
        assertTrue(error.getStatus());
        assertEquals("", error.getMessage());
    }

    @Test
    void malformedEmail_errorMessageAppears() {
        logInData = new LogInData("abc123!.", "Password123!");
        Validator error = checkLoginData(logInData);
        assertFalse(error.getStatus());
        assertEquals("Email address must be in the form ‘jane@doe.nz’", error.getMessage());
    }

    @Test
    void emptyEmail_errorMessageAppears() {
        logInData = new LogInData("", "Password123!");
        Validator error = checkLoginData(logInData);
        assertFalse(error.getStatus());
        assertEquals("Email address must be in the form ‘jane@doe.nz’", error.getMessage());
    }

    @Test
    void emptyPassword_errorMessageAppears() {
        logInData = new LogInData("abc123@gmail.com", "");
        Validator error = checkLoginData(logInData);
        assertFalse(error.getStatus());
        assertEquals("The email address is unknown, or the password is invalid", error.getMessage());
    }

}
