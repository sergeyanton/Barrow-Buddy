package nz.ac.canterbury.team1000.gardenersgrove.cucumber.step_definitions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.RegistrationForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class U1_StepDefinitions {
  @Autowired
  UserService userService;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  PasswordEncoder passwordEncoder;
  RegistrationForm registrationForm = new RegistrationForm();
  private MvcResult mvcResult;

  @Given("I am on the registration form")
  public void iAmOnTheRegistrationForm() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/register").with(csrf()))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.view().name("pages/registrationPage"));
  }

  @Given("A user exists with email {string} on the register form")
  public void userExists(String email) {
    if (!userService.checkEmail(email)) {
      User userWithGivenEmail = new User("FirstName", "LastName", email, passwordEncoder.encode("password"),
          LocalDate.of(2000, 1, 1), "/images/default_pic.jpg");
      userWithGivenEmail.grantAuthority("ROLE_USER");
      userService.registerUser(userWithGivenEmail);
    }
  }

  @And("I enter details {string}, {string}, a unique email {string}, and {string} on the register form")
  public void iEnterDetailsOnTheRegisterForm(String fName, String lName, String email, String dob) {
    registrationForm.setFirstName(fName);
    registrationForm.setLastName(lName);
    registrationForm.setEmail(email);
    registrationForm.setDob(dob);
  }

  @And("I do not tick the checkbox for no last name on the register form")
  public void iDoNotTickTheCheckboxForNoLastNameOnTheRegisterForm() {
    registrationForm.setNoSurnameCheckBox(false);
  }

  @And("I tick the checkbox for no last name on the register form")
  public void iTickTheCheckboxForNoLastNameOnTheRegisterForm() {
    registrationForm.setNoSurnameCheckBox(true);
  }

  @And("I enter password {string} and retype password {string} on the register form")
  public void iEnterPasswordAndPasswordOnTheRegisterForm(String password, String retypePassword) {
    registrationForm.setPassword(password);
    registrationForm.setRetypePassword(retypePassword);
  }

  @And("I enter a date of birth that means I turn {int} years old in {int} days on the register form")
  public void iEnterADateOfBirthThatMeansITurnYearsOldInDaysOnTheRegisterForm(int age, int numDays) {
    registrationForm.setDob(LocalDate.now().minusYears(age).plusDays(numDays).format(DateTimeFormatter.ofPattern("dd/MM/uuuu")));
  }

  @And("I enter a first name {int} characters long and a last name {int} characters long on the register form")
  public void iEnterAFirstNameCharactersLongAndALastNameCharactersLongOnTheRegisterForm(int firstNameLength,
      int lastNameLength) {
    registrationForm.setFirstName("F".repeat(firstNameLength));
    registrationForm.setLastName("L".repeat(lastNameLength));
    registrationForm.setNoSurnameCheckBox(lastNameLength == 0);
  }

  @When("I click the sign-up button")
  public void iClickTheSignUpButton() throws Exception {
    mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
        .flashAttr("registrationForm", registrationForm)).andReturn();
  }

  @When("I click the cancel button on the register form")
  public void iClickTheCancelButtonOnTheRegisterForm() throws Exception {
    mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/").with(csrf())).andReturn();
  }

  @Then("I am successfully registered")
  public void iAmSuccessfullyRegistered() throws Exception{
    MockMvcResultMatchers.status().is3xxRedirection().match(mvcResult);
    MockMvcResultMatchers.redirectedUrl("/register/verification").match(mvcResult);
  }

  @Then("I am shown the error message {string} on the register form")
  public void iAmShownTheErrorMessageErrorMessageOnTheRegisterForm(String errorMessage) throws Exception {
    MockMvcResultMatchers.status().isOk().match(mvcResult);
    MockMvcResultMatchers.view().name("pages/registrationPage").match(mvcResult);

    List<FieldError> fieldErrors = ((BindingResult) mvcResult.getModelAndView().getModel().get(BindingResult.MODEL_KEY_PREFIX + "registrationForm")).getFieldErrors();

    Assertions.assertTrue(fieldErrors.stream()
        .map(fieldError -> fieldError.getDefaultMessage())
        .anyMatch(defaultMessage -> errorMessage.equals(defaultMessage)));
  }

  @Then("I am taken back to the system’s home page")
  public void iAmTakenBackToTheSystemSHomePage() throws Exception {
    MockMvcResultMatchers.status().isOk().match(mvcResult);
    MockMvcResultMatchers.view().name("pages/landingPage").match(mvcResult);
  }
}
