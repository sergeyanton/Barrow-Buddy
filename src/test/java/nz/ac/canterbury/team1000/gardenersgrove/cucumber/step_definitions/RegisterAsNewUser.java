package nz.ac.canterbury.team1000.gardenersgrove.cucumber.step_definitions;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nz.ac.canterbury.team1000.gardenersgrove.form.RegistrationForm;
import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class RegisterAsNewUser {
  RegistrationForm registrationForm = new RegistrationForm();
  BindingResult bindingResult;
  ArgumentCaptor<FieldError> fieldErrorCaptor = ArgumentCaptor.forClass(FieldError.class);

  @Given("I am on the registration form and enter first name {string} and last name {string}")
  public void iAmOnTheRegistrationFormAndEnterFirstNameAndLastName(String firstName, String lastName) {
    registrationForm.setFirstName(firstName);
    registrationForm.setLastName(lastName);
    registrationForm.setNoSurnameCheckBox(lastName.isBlank());
  }

  @And("I enter email {string}")
  public void iEnterEmail(String email) {
    registrationForm.setEmail(email);
  }

  @And("I enter password {string} and retype password {string}")
  public void iEnterPasswordAndRetypePassword(String password, String retypePassword) {
    registrationForm.setPassword(password);
    registrationForm.setRetypePassword(retypePassword);
  }

  @And("I enter date of birth {string}")
  public void iEnterDateOfBirth(String dob) {
    registrationForm.setDob(dob);
  }

  @When("I click the sign-up button")
  public void iClickTheSignUpButton() {
    bindingResult = Mockito.mock(BindingResult.class);
    RegistrationForm.validate(registrationForm, bindingResult);
  }

  @Then("I am successfully registered")
  public void iAmSuccessfullyRegistered() {
    Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
    Assertions.assertFalse(bindingResult.hasErrors());
  }

  @Then("I am shown the error message {string}")
  public void iAmShownTheErrorMessage(String errorMessage) {
    Mockito.verify(bindingResult).addError(fieldErrorCaptor.capture());
    FieldError fieldError = fieldErrorCaptor.getValue();
    Assertions.assertEquals(errorMessage, fieldError.getDefaultMessage());
  }

  @After
  public void cleanUp() {
    bindingResult = null;
  }

}
