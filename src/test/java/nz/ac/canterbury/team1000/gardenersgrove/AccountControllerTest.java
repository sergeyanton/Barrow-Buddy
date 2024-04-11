package nz.ac.canterbury.team1000.gardenersgrove;

import nz.ac.canterbury.team1000.gardenersgrove.controller.AccountController;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.RegistrationForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

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
        user = new User("John", "Doe", "test@example.com", "testPassword123!",
                LocalDate.of(1990, 1, 1));
    }

    @Test
    @WithMockUser
    public void registerPostRequest_validUserDetails_userRegisteredAndAuthenticated()
            throws Exception {
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setEmail(user.getEmail());
        registrationForm.setFirstName(user.getFname());
        registrationForm.setLastName(user.getLname());
        registrationForm.setDob(user.getDateOfBirthString());
        registrationForm.setPassword(user.getPassword());
        registrationForm.setRetypePassword(user.getPassword());
        registrationForm.setNoSurnameCheckBox(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
                .flashAttr("registrationForm", registrationForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));


        Mockito.verify(userService).registerUser(Mockito.any());
    }

    @Test
    void login() {}
}
