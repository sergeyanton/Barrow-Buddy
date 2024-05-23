package nz.ac.canterbury.team1000.gardenersgrove.cucumber.step_definitions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.entity.VerificationToken;
import nz.ac.canterbury.team1000.gardenersgrove.form.LoginForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.RegistrationForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.VerificationTokenForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.EmailService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import nz.ac.canterbury.team1000.gardenersgrove.service.VerificationTokenService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class VerificationAfterRegister {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private UserService userService;
  @Autowired
  private JavaMailSender emailSender;
  @Autowired
  private VerificationTokenService verificationTokenService;
  @Mock
  private VerificationToken verificationTokenMock;
  private VerificationTokenForm verificationTokenForm = new VerificationTokenForm();
  private User testUser;

  @Given("I have registered with the first name {string} and last name {string}, email {string} and password {string}")
  public void iHaveRegisteredWithValidCredentials(String firstName, String lastName, String email,
      String password) throws Exception {

    RegistrationForm registrationForm = new RegistrationForm();

    testUser = new User(firstName, lastName, email, password, LocalDate.now(), " ");
    registrationForm.setFirstName(firstName);
    registrationForm.setLastName(lastName);
    registrationForm.setEmail(email);
    registrationForm.setPassword(password);
    registrationForm.setRetypePassword(password);
    registrationForm.setDob("01/01/2000");
    registrationForm.setNoSurnameCheckBox(false);

    mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
        .flashAttr("registrationForm", registrationForm))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/register/verification"));
  }

   @When("I log in")
   public void iAccessLoginPage() throws Exception {
     LoginForm loginForm = new LoginForm();
     loginForm.setEmail(testUser.getEmail());
     loginForm.setPassword(testUser.getPassword());

     mockMvc.perform(MockMvcRequestBuilders.post("/login").with(csrf())
             .flashAttr("loginForm", loginForm))
         .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
         .andExpect(MockMvcResultMatchers.redirectedUrl("/home"));

  }

  @When("I don't verify my account")
  public void iDonTVerifyMyAccount() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/").with(csrf()))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.view().name("pages/landingPage"));
  }

  @When("I verify my account")
  public void iVerifyMyAccount() throws Exception {
    verificationTokenMock = Mockito.mock(VerificationToken.class);
    Mockito.when(verificationTokenMock.getUserId()).thenReturn(1L);
    Mockito.when(verificationTokenMock.getToken()).thenReturn("token");

    verificationTokenForm.setVerificationToken("123456");
    verificationTokenMock.setVerified(true);
    Mockito.when(verificationTokenService.getVerificationTokenByToken(Mockito.any()))
        .thenReturn(verificationTokenMock);
    Mockito.when(userService.findById(verificationTokenMock.getUserId())).thenReturn(testUser);
    Mockito.doNothing().when(verificationTokenService).updateVerifiedByUserId(Mockito.anyLong());
    mockMvc.perform(MockMvcRequestBuilders.post("/register/verification").with(csrf())
            .flashAttr("verificationTokenForm", verificationTokenForm))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));
  }

   @Then("I am redirected to the page with URL {string}")
   public void iAmRedirectedToThePageWithURL(String pageURL) throws Exception {
      LoginForm loginForm = new LoginForm();
      loginForm.setEmail(testUser.getEmail());
      loginForm.setPassword(testUser.getPassword());

      mockMvc.perform(MockMvcRequestBuilders.post("/login").with(csrf())
          .flashAttr("loginForm", loginForm))
          .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
          .andExpect(MockMvcResultMatchers.redirectedUrl(pageURL));
   }



}

