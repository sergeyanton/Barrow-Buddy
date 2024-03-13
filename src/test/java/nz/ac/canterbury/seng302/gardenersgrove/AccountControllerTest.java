package nz.ac.canterbury.seng302.gardenersgrove;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import nz.ac.canterbury.seng302.gardenersgrove.controller.AccountController;
import nz.ac.canterbury.seng302.gardenersgrove.controller.dataCollection.RegistrationData;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import nz.ac.canterbury.seng302.gardenersgrove.validation.InputValidation;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Locale;

import static nz.ac.canterbury.seng302.gardenersgrove.validation.InputValidation.checkLoginData;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Validator;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private static User user;

    @BeforeAll
    public static void setup() {
        user = new User(
                "John",
                "Doe",
                "test@example.com",
                "testPassword123!",
                LocalDate.of(1990, 1, 1)
        );
    }

    @Test
    @WithMockUser
    public void registerPostRequest_validUserDetails_userRegisteredAndAuthenticated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .with(csrf())
                        .param("email", user.getEmail())
                        .param("fName", user.getFname())
                        .param("lName", user.getLname())
                        .param("dob", user.getDateOfBirth().toString())
                        .param("password", user.getPassword())
                        .param("retypePassword", user.getPassword())
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));

        Mockito.verify(userService).registerUser(Mockito.any());
    }

    @Test
    @WithMockUser
    public void loginPostRequest_validUserDetails_userAuthenticated() throws Exception {
        Mockito.when(userService.isSignedIn()).thenReturn(false);
        Mockito.when(userService.findEmail(Mockito.any())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .with(csrf())
                        .param("email", user.getEmail())
                        .param("password", user.getPassword())
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));

        Mockito.verify(userService).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
    }

//    @Test
//    public void getUser_userIdGiven_returnCorrectUser() throws Exception {
//        String email = "test@example.com";
//        String fName = "John";
//        String lName = "Doe";
//        LocalDate dob = LocalDate.of(1990, 1, 1);
//        String password = "testPassword";
//        String retypePassword = "testPassword";
//
//        RegistrationData registrationData = new RegistrationData(email, fName, lName, dob, password,
//                retypePassword, false);
//        User mockUser = new User(registrationData.getfName(), registrationData.getlName(), registrationData.getEmail(),
//                registrationData.getPassword(), registrationData.getDob());
//        Mockito.when(userService.getUserByEmailAndPassword(mockUser.getEmail(), mockUser.getPassword()).thenReturn(mockUser));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/user/1")
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@example.com"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("testPassword"));
//    }


    @Test
    void login() {
    }
}