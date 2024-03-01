package nz.ac.canterbury.seng302.gardenersgrove;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class UserValidationTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("Fabian","Gilson","fabian.gilson@canterbury.ac.nz","20 Kirkwood Ave, Ilam, Christchurch 8041",
                "Fabian123!","01/01/2009");
    }

    @Test
    void firstNameValidCheck_noError() {
        Validator fnameValidator = User.checkName(user.getFname());

        assertTrue(fnameValidator.getStatus());
    }

    @Test
    void dobValidCheck_noError() {
        Validator dobValidator = User.checkDob(user.getDateOfBirth());
        assertTrue(dobValidator.getStatus());
    }

    @Test
    void dobInvalidFormatCheck_error() {
        User invalidUser = new User("Invalid","Person","fabian.gilson@canterbury.ac.nz","20 Kirkwood Ave, Ilam, Christchurch 8041",
                "Fabian123!","50/50/2001");
        Validator dobValidator = User.checkDob(invalidUser.getDateOfBirth());

        assertFalse(dobValidator.getStatus());
    }

    @Test
    void dobInvalidAgeCheck_error() {
        User invalidUser = new User("Invalid","Person","fabian.gilson@canterbury.ac.nz","20 Kirkwood Ave, Ilam, Christchurch 8041",
                "Fabian123!","09/09/2020");
        Validator dobValidator = User.checkDob(invalidUser.getDateOfBirth());

        assertFalse(dobValidator.getStatus());
    }

    @Test
    void dobFutureDateCheck_error() {
        User invalidUser = new User("Invalid","Person","fabian.gilson@canterbury.ac.nz","20 Kirkwood Ave, Ilam, Christchurch 8041",
                "Fabian123!","09/09/2040");
        Validator dobValidator = User.checkDob(invalidUser.getDateOfBirth());

        assertFalse(dobValidator.getStatus());
    }





}
