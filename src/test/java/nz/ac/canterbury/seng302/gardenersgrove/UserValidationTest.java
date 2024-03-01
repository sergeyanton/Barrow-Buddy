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

    @BeforeEach
    void setUp() {
        user = new User("Fabian","Gilson","Fabian123!","20 Kirkwood Ave, Ilam, Christchurch 8041",
                "fabian.gilson@canterbury.ac.nz","01/01/2019");
    }

    @Test
    void firstNameValidCheck_noError() {
        Validator fnameValidator = User.checkName(user.getFname());

        assertTrue(fnameValidator.getStatus());

    }





}
