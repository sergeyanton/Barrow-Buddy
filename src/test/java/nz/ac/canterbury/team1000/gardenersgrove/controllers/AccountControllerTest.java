package nz.ac.canterbury.team1000.gardenersgrove.controllers;

import nz.ac.canterbury.team1000.gardenersgrove.controller.AccountController;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.LoginForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.RegistrationForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc
@WithMockUser
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Mock
    private User userMock;

    private RegistrationForm registrationForm;

    private LoginForm loginForm;

    @BeforeEach
    public void beforeEach() {
        userMock = Mockito.mock(User.class);
        Mockito.when(userMock.getFname()).thenReturn("John");
        Mockito.when(userMock.getLname()).thenReturn("Smith");
        Mockito.when(userMock.getEmail()).thenReturn("johnsmith@gmail.com");
        Mockito.when(userMock.getDateOfBirthString()).thenReturn("05/05/1999");
        Mockito.when(userMock.getPassword()).thenReturn("encoded_password");

        registrationForm = new RegistrationForm();
        registrationForm.setFirstName(userMock.getFname());
        registrationForm.setLastName(userMock.getLname());
        registrationForm.setEmail(userMock.getEmail());
        registrationForm.setDob(userMock.getDateOfBirthString());
        registrationForm.setPassword("Pass123$");
        registrationForm.setRetypePassword("Pass123$");
        registrationForm.setNoSurnameCheckBox(userMock.getLname() == null || userMock.getLname().isEmpty());
        Mockito.when(userService.checkEmail(Mockito.any())).thenReturn(false);
    }

    @Test
    public void registerPostRequest_validUserDetails_userRegisteredAndAuthenticated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
                .flashAttr("registrationForm", registrationForm))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));

        Mockito.verify(userService).registerUser(Mockito.any());
    }


    @Test
    void login() {}
}
