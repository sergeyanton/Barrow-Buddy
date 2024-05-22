package nz.ac.canterbury.team1000.gardenersgrove.cucumber.step_definitions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.RegistrationForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.EmailService;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
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


  // public VerificationAfterRegister(CucumberSpringConfiguration cucumberSpringConfiguration) {
  // this.mockMvc = cucumberSpringConfiguration.getMockMvc();
  // Assertions.assertNotNull(this.mockMvc);
  // }

  @Autowired
  private UserService userService;

//  @Autowired
//  @MockBean
//  private EmailService emailService = Mockito.mock(EmailService.class);

  @Autowired
  private JavaMailSender emailSender;

  @Given("I have registered with the first name {string} and last name {string}, email {string} and password {string}")
  public void iHaveRegisteredWithValidCredentials(String firstName, String lastName, String email,
      String password) throws Exception {
    RegistrationForm registrationForm = new RegistrationForm();

    User newUser = new User(firstName, lastName, email, password, LocalDate.now(), " ");
    registrationForm.setFirstName(firstName);
    registrationForm.setLastName(lastName);
    registrationForm.setEmail(email);
    registrationForm.setPassword(password);
    registrationForm.setRetypePassword(password);
    registrationForm.setDob("01/01/2000");
    registrationForm.setNoSurnameCheckBox(false);

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
        .flashAttr("registrationForm", registrationForm)).andReturn();
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

}

