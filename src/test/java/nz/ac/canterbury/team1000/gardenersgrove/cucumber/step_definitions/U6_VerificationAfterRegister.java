package nz.ac.canterbury.team1000.gardenersgrove.cucumber.step_definitions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.entity.VerificationToken;
import nz.ac.canterbury.team1000.gardenersgrove.form.LoginForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.RegistrationForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.VerificationTokenForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import nz.ac.canterbury.team1000.gardenersgrove.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class U6_VerificationAfterRegister {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private UserService userService;
  @Autowired
  private JavaMailSender emailSender;
  @Autowired
  private VerificationTokenService verificationTokenService;
  @Autowired
  private PasswordEncoder passwordEncoder;
  private User testUser;
  private String plainToken;
  private String password;

  @Given("I have registered with the first name {string} and last name {string}, email {string} and password {string}")
  public void iHaveRegisteredWithValidCredentials(String firstName, String lastName, String email,
      String password) throws Exception {

    RegistrationForm registrationForm = new RegistrationForm();

    registrationForm.setFirstName(firstName);
    registrationForm.setLastName(lastName);
    registrationForm.setEmail(email);
    registrationForm.setPassword(password);
    registrationForm.setRetypePassword(password);
    registrationForm.setDob("01/01/2000");
    registrationForm.setNoSurnameCheckBox(false);

    this.password = password;

    testUser = registrationForm.getUser(passwordEncoder);
    testUser.grantAuthority("ROLE_USER");
    userService.registerUser(testUser);
    VerificationToken verificationTokenTest = new VerificationToken(testUser.getId());
    plainToken = verificationTokenTest.getPlainToken();
    verificationTokenService.addVerificationToken(verificationTokenTest);
  }


  @When("I don't verify my account")
  public void iDonTVerifyMyAccount() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/").with(csrf()))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.view().name("pages/landingPage"));
  }

  @When("I verify my account")
  public void iVerifyMyAccount() throws Exception {
    VerificationTokenForm verificationTokenForm = new VerificationTokenForm();
    verificationTokenForm.setVerificationToken(plainToken);

    mockMvc.perform(MockMvcRequestBuilders.post("/register/verification").with(csrf())
        .flashAttr("verificationTokenForm", verificationTokenForm))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));

  }


  @Then("I try to log in am redirected to the page with URL {string}")
   public void iAmRedirectedToThePageWithURL(String pageURL) throws Exception {
      LoginForm loginForm = new LoginForm();
      loginForm.setEmail(testUser.getEmail());
      loginForm.setPassword(password);

      mockMvc.perform(MockMvcRequestBuilders.post("/login").with(csrf())
          .flashAttr("loginForm", loginForm))
          .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
          .andExpect(MockMvcResultMatchers.redirectedUrl(pageURL));
   }


}

