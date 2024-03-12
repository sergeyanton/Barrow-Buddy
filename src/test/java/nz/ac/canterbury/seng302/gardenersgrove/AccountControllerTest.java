package nz.ac.canterbury.seng302.gardenersgrove;

import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.canterbury.seng302.gardenersgrove.controller.AccountController;
import nz.ac.canterbury.seng302.gardenersgrove.controller.dataCollection.RegistrationData;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import nz.ac.canterbury.seng302.gardenersgrove.validation.InputValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void createUser_validUserGiven_SaveUser() throws Exception {
        String email = "test@example.com";
        String fName = "John";
        String lName = "Doe";
        LocalDate dob = LocalDate.of(1990, 1, 1);
        String password = "testPassword";
        String retypePassword = "testPassword";

        String hashedPassword = InputValidation.hashPassword(password);

        RegistrationData registrationData = new RegistrationData(email, fName, lName, dob, password, retypePassword, false);
        User validUser = RegistrationData.createNewUser(registrationData);

        Mockito.when(userService.registerUser(Mockito.any(User.class))).thenReturn(validUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registrationData)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.redirectedUrl("redirect:/profile"));
    }

    @Test
    public void getUser_userIdGiven_returnCorrectUser() throws Exception {
        String email = "test@example.com";
        String fName = "John";
        String lName = "Doe";
        LocalDate dob = LocalDate.of(1990, 1, 1);
        String password = "testPassword";
        String retypePassword = "testPassword";

        RegistrationData registrationData = new RegistrationData(email, fName, lName, dob, password,
                retypePassword, false);
        User mockUser = new User(registrationData.getfName(), registrationData.getlName(), registrationData.getEmail(),
                registrationData.getPassword(), registrationData.getDob());
        Mockito.when(userService.getUserByEmailAndPassword(mockUser.getEmail(), mockUser.getPassword()).thenReturn(mockUser));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("testPassword"));
    }


    @Test
    void registerValidInputs() throws Exception {
        // Mock the registration data
        String email = "test@example.com";
        String fName = "John";
        String lName = "Doe";
        LocalDate dob = LocalDate.of(1990, 1, 1);
        String password = "testPassword";
        String retypePassword = "testPassword";
        Boolean noSurnameCheckBox = false;
        RegistrationData testInput = new RegistrationData(email, fName, lName, dob, password,
                retypePassword, noSurnameCheckBox);

        User mockUser = new User(testInput.getfName(), testInput.getlName(), testInput.getEmail(), testInput.getPassword(),
                testInput.getDob());

        doNothing().when(userService).registerUser(any(User.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType("application/x-www-form-urlencoded")
                        .param("fName", testInput.getfName())
                        .param("lName", testInput.getlName())
                        .param("email", testInput.getEmail())
                        .param("password", testInput.getPassword())
                        .param("retypePassword", testInput.getRetypePassword())
                        .param("dob", testInput.getDob().toString()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));

    }

    @Test
    void login() {
    }
}