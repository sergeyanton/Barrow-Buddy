package nz.ac.canterbury.team1000.gardenersgrove.cucumber.step_definitions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import nz.ac.canterbury.team1000.gardenersgrove.controller.AccountController;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.entity.VerificationToken;
import nz.ac.canterbury.team1000.gardenersgrove.form.ForgotPasswordForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.LoginForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.RegistrationForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.ResetPasswordForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.VerificationTokenForm;
import nz.ac.canterbury.team1000.gardenersgrove.repository.ResetTokenRepository;
import nz.ac.canterbury.team1000.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.team1000.gardenersgrove.repository.VerificationTokenRepository;
import nz.ac.canterbury.team1000.gardenersgrove.service.EmailService;
import nz.ac.canterbury.team1000.gardenersgrove.service.GardenService;
import nz.ac.canterbury.team1000.gardenersgrove.service.ResetTokenService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import nz.ac.canterbury.team1000.gardenersgrove.service.VerificationTokenService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
public class VerificationAfterRegister {
  @MockBean
  private PasswordEncoder passwordEncoder;
  @Autowired
  public UserRepository userRepository;
  @Autowired
  public VerificationTokenRepository verificationTokenRepository;
  @Autowired
  public ResetTokenRepository resetTokenRepository;
  public UserService userService;
  public VerificationTokenService verificationTokenService;
  public EmailService emailService;
  public ResetTokenService resetTokenService;
  private RegistrationForm registrationForm;
  public static MockMvc MOCK_MVC;



  @Before
  public void beforeEach() {
    userService = new UserService(userRepository);
    verificationTokenService = new VerificationTokenService(verificationTokenRepository);
    resetTokenService = new ResetTokenService(resetTokenRepository);
    emailService = new EmailService(Mockito.mock(JavaMailSender.class));
    AccountController accountController = new AccountController(userService, verificationTokenService, emailService, resetTokenService);

    MOCK_MVC = MockMvcBuilders.standaloneSetup(accountController).build();
    registrationForm = new RegistrationForm();



  }

  @Given("I have registered with the first name {string} and last name {string}, email {string} and password {string}")
  public void iHaveRegisteredWithValidCredentials(String firstName, String lastName, String email,
      String password) throws Exception {

    User newUser = new User(firstName, lastName, email, password, LocalDate.now()," ");
    registrationForm.setFirstName(firstName);
    registrationForm.setLastName(lastName);
    registrationForm.setEmail(email);
    registrationForm.setPassword(password);
    registrationForm.setRetypePassword(password);
    registrationForm.setDob("01/01/2000");
    registrationForm.setNoSurnameCheckBox(false);

    Mockito.when(registrationForm.getUser(Mockito.any())).thenReturn(newUser);
    MOCK_MVC.perform(MockMvcRequestBuilders.post("/register").with(csrf())
        .flashAttr("registrationForm", registrationForm));
  }

  @When("I access log in page without verifying my account")
  public void iAccessLoginPageWithoutVerifyingMyAccount() throws Exception {
    MOCK_MVC.perform(MockMvcRequestBuilders.get("/login"))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Then("I am redirected to the page with URL {string}")
  public void iAmRedirectedToThePageWithURL(String pageURL) throws Exception {
    MOCK_MVC.perform(MockMvcRequestBuilders.get(pageURL))
        .andExpect(MockMvcResultMatchers.redirectedUrl(pageURL));
  }

}
