package nz.ac.canterbury.team1000.gardenersgrove.cucumber.step_definitions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import nz.ac.canterbury.team1000.gardenersgrove.controller.AccountController;
import nz.ac.canterbury.team1000.gardenersgrove.cucumber.CucumberSpringConfiguration;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.entity.VerificationToken;
import nz.ac.canterbury.team1000.gardenersgrove.form.RegistrationForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.EmailService;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import nz.ac.canterbury.team1000.gardenersgrove.service.ResetTokenService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import nz.ac.canterbury.team1000.gardenersgrove.service.VerificationTokenService;
<<<<<<< Updated upstream
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
=======
>>>>>>> Stashed changes
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

<<<<<<< Updated upstream
public class VerificationAfterRegister {
  private final MockMvc mockMvc;
=======
@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc
public class VerificationAfterRegister {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private EmailService emailService;

  @MockBean
  private VerificationTokenService verificationTokenService;

  @MockBean
  private ResetTokenService resetTokenService;

  @MockBean
  private GardenService gardenService;

  @MockBean
  private AuthenticationManager authenticationManager;

  @MockBean
  private PasswordEncoder passwordEncoder;
>>>>>>> Stashed changes

  public VerificationAfterRegister(CucumberSpringConfiguration cucumberSpringConfiguration) {
    this.mockMvc = cucumberSpringConfiguration.getMockMvc();
    Assertions.assertNotNull(this.mockMvc);
  }
  @MockBean
  private UserService userService;

<<<<<<< Updated upstream
  @MockBean
  private EmailService emailService;

  @MockBean
  private VerificationTokenService verificationTokenService;

  @MockBean
  private ResetTokenService resetTokenService;

  @MockBean
  private GardenService gardenService;
=======
  @Mock
  private VerificationToken verificationTokenMock;

  private RegistrationForm registrationForm;


  @BeforeEach
  public void beforeEach() {
    registrationForm = new RegistrationForm();

    Mockito.when(userService.checkEmail(Mockito.any())).thenReturn(false);
    Mockito.when(userService.isSignedIn()).thenReturn(false);
    Mockito.when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);
>>>>>>> Stashed changes

  @MockBean
  private AuthenticationManager authenticationManager;

  @MockBean
  private PasswordEncoder passwordEncoder;

  @Given("I have registered with the first name {string} and last name {string}, email {string} and password {string}")
  public void iHaveRegisteredWithValidCredentials(String firstName, String lastName, String email,
      String password) throws Exception {
<<<<<<< Updated upstream
    RegistrationForm registrationForm = new RegistrationForm();
=======

    User newUser = new User(firstName, lastName, email, password, LocalDate.now(), " ");
>>>>>>> Stashed changes
    registrationForm.setFirstName(firstName);
    registrationForm.setLastName(lastName);
    registrationForm.setEmail(email);
    registrationForm.setPassword(password);
    registrationForm.setRetypePassword(password);
    registrationForm.setDob("01/01/2000");
    registrationForm.setNoSurnameCheckBox(false);

<<<<<<< Updated upstream
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
        .flashAttr("registrationForm", registrationForm)).andReturn();
  }

//  @Before
//  public void beforeEach() {
//    userService = new UserService(userRepository);
//    verificationTokenService = new VerificationTokenService(verificationTokenRepository);
//    resetTokenService = new ResetTokenService(resetTokenRepository);
//    emailService = new EmailService(Mockito.mock(JavaMailSender.class));
//    AccountController accountController = new AccountController(userService, verificationTokenService, emailService, resetTokenService);
//
//    MOCK_MVC = MockMvcBuilders.standaloneSetup(accountController).build();
//    registrationForm = new RegistrationForm();
//
//
//
//  }
//
//  @Given("I have registered with the first name {string} and last name {string}, email {string} and password {string}")
//  public void iHaveRegisteredWithValidCredentials(String firstName, String lastName, String email,
//      String password) throws Exception {
//    registrationForm.setFirstName(firstName);
//    registrationForm.setLastName(lastName);
//    registrationForm.setEmail(email);
//    registrationForm.setPassword(password);
//    registrationForm.setRetypePassword(password);
//    registrationForm.setDob("01/01/2000");
//    registrationForm.setNoSurnameCheckBox(false);
//
//    MvcResult result = MOCK_MVC.perform(MockMvcRequestBuilders.post("/register").with(csrf())
//        .flashAttr("registrationForm", registrationForm)).andReturn();
//  }
//
//  @When("I access log in page without verifying my account")
//  public void iAccessLoginPageWithoutVerifyingMyAccount() throws Exception {
//    MOCK_MVC.perform(MockMvcRequestBuilders.get("/login"))
//        .andExpect(MockMvcResultMatchers.status().isOk());
//  }
//
//  @Then("I am redirected to the page with URL {string}")
//  public void iAmRedirectedToThePageWithURL(String pageURL) throws Exception {
//    MOCK_MVC.perform(MockMvcRequestBuilders.get(pageURL))
//        .andExpect(MockMvcResultMatchers.redirectedUrl(pageURL));
//  }
=======
    Mockito.when(registrationForm.getUser(Mockito.any())).thenReturn(newUser);
    mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
            .flashAttr("registrationForm", registrationForm))
        .andExpect(MockMvcResultMatchers.redirectedUrl("/register/verification"));
  }

  @When("I access log in page without verifying my account")
  public void iAccessLoginPageWithoutVerifyingMyAccount() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/login"))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Then("I am redirected to the page with URL {string}")
  public void iAmRedirectedToThePageWithURL(String pageURL) throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(pageURL))
        .andExpect(MockMvcResultMatchers.redirectedUrl(pageURL));
  }
>>>>>>> Stashed changes

}
