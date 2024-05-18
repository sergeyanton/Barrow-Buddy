package nz.ac.canterbury.team1000.gardenersgrove.cucumber.step_definitions;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import nz.ac.canterbury.team1000.gardenersgrove.form.EditUserForm;
import org.mockito.ArgumentCaptor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
public class U4_StepDefinitions {
	EditUserForm editUserForm = new EditUserForm();
	BindingResult bindingResult;
	ArgumentCaptor<FieldError> fieldErrorCaptor = ArgumentCaptor.forClass(FieldError.class);

	@Given("I am on the edit user profile form and enter first name {string} and last name {string}")
	public void iAmOnTheEditUserProfileFormAndEnterFirstNameFirstNameAndLastNameLastName(String firstName, String lastName) {
	}

	@And("I enter email {string} on the edit user profile form")
	public void iEnterEmailEmailOnTheEditUserProfileForm(String email) {
	}

	@And("I do not tick the checkbox for no last name on the edit user profile form")
	public void iDoNotTickTheCheckboxForNoLastNameOnTheEditUserProfileForm() {
	}

	@And("I enter password {string} and retype password {string} on the edit user profile form")
	public void iEnterPasswordPasswordAndRetypePasswordPasswordOnTheEditUserProfileForm(String password, String retypePassword) {
	}

	@And("I enter date of birth {string} on the edit user profile form")
	public void iEnterDateOfBirthDobOnTheEditUserProfileForm(String dob) {
	}

	@When("I click the sign-up button on the edit user profile form")
	public void iClickTheSignUpButtonOnTheEditUserProfileForm() {
	}

	@Then("My new details are saved")
	public void myNewDetailsAreSaved() {
	}
	@After
	public void cleanUp() {
		bindingResult = null;
	}


}
