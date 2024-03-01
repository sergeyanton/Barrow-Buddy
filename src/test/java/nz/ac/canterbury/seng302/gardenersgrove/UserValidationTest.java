package nz.ac.canterbury.seng302.gardenersgrove;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class UserValidationTest {
    private User user;

    @Test
    void firstNameValidCheck_noError() {
        user = new User("Fabian","Gilson","fabian.gilson@canterbury.ac.nz"
                , "20 Kirkwood Ave, Ilam, Christchurch 8041", "Fabian123!" ,"01/01/2019");

        Validator fnameValidator = User.checkName(user.getFname());
        assertTrue(fnameValidator.getStatus());
    }

    @Test
    void lastNameValidCheck_noError() {
        user = new User("Fabian","Gilson","fabian.gilson@canterbury.ac.nz"
                , "20 Kirkwood Ave, Ilam, Christchurch 8041", "Fabian123!" ,"01/01/2019");

        Validator lnameValidator = User.checkName(user.getLname());
        assertTrue(lnameValidator.getStatus());
    }

    @Test
    void emailNameValidCheck_noError() {
        user = new User("Fabian","Gilson","fabian.gilson@canterbury.ac.nz"
                , "20 Kirkwood Ave, Ilam, Christchurch 8041", "Fabian123!" ,"01/01/2019");

        Validator emailValidator = User.checkEmail(user.getEmail());
        assertTrue(emailValidator.getStatus());
    }







}
