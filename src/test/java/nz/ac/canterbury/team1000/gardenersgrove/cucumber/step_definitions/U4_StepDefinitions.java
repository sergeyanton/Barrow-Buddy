package nz.ac.canterbury.team1000.gardenersgrove.cucumber.step_definitions;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import nz.ac.canterbury.team1000.gardenersgrove.form.EditUserForm;
import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
public class U4_StepDefinitions {
	EditUserForm editUserForm = new EditUserForm();
	BindingResult bindingResult;
	ArgumentCaptor<FieldError> fieldErrorCaptor = ArgumentCaptor.forClass(FieldError.class);

	@Given("I am on the edit user profile form and enter first name {string} and last name {string}")
	public void iAmOnTheEditUserProfileFormAndEnterFirstNameFirstNameAndLastNameLastName(String firstName, String lastName) {
		editUserForm.setFirstName(firstName);
		editUserForm.setLastName(lastName);
		editUserForm.setNoSurnameCheckBox(lastName.isBlank());
		editUserForm.setPicturePath("/images/default_pic.jpg");
		editUserForm.setPictureFile(new MockMultipartFile("pictureFile", new byte[0]));
	}

	@Given("I am on the edit user profile form and enter a first name {int} characters long and a last name {int} characters long")
	public void iAmOnTheEditUserProfileFormAndEnterAFirstNameCharactersLongAndALastNameCharactersLong(
		int firstNameLength, int lastNameLength) {
		editUserForm.setFirstName("F".repeat(firstNameLength));
		editUserForm.setLastName("L".repeat(lastNameLength));
		editUserForm.setNoSurnameCheckBox(lastNameLength == 0);
		editUserForm.setPicturePath("/images/default_pic.jpg");
		editUserForm.setPictureFile(new MockMultipartFile("pictureFile", new byte[0]));
	}

	@And("I enter email {string} on the edit user profile form")
	public void iEnterEmailEmailOnTheEditUserProfileForm(String email) {
		editUserForm.setEmail(email);
	}

	@And("I tick the checkbox for no last name on the edit user profile form")
	public void iTickTheCheckboxForNoLastNameOnTheEditUserProfileForm() {
		editUserForm.setNoSurnameCheckBox(true);
	}

	@And("I do not tick the checkbox for no last name on the edit user profile form")
	public void iDoNotTickTheCheckboxForNoLastNameOnTheEditUserProfileForm() {
		editUserForm.setNoSurnameCheckBox(false);
	}

	@And("I enter date of birth {string} on the edit user profile form")
	public void iEnterDateOfBirthDobOnTheEditUserProfileForm(String dob) {
		editUserForm.setDob(dob);
	}

	@And("I enter a date of birth that means I turn {int} years old in {int} days on the edit user profile form")
	public void iEnterADateOfBirthThatMeansITurnYearsOldInDaysOnTheEditUserProfileForm(int age, int numDays) {
		editUserForm.setDob(LocalDate.now().minusYears(age).plusDays(numDays).format(DateTimeFormatter.ofPattern("dd/MM/uuuu")));
	}

	@When("I click the edit profile button")
	public void iClickTheEditProfileButton() {
		bindingResult = Mockito.mock(BindingResult.class);
		EditUserForm.validate(editUserForm, bindingResult, null);
	}

	@Then("My new details are saved")
	public void myNewDetailsAreSaved() {
		Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
		Assertions.assertFalse(bindingResult.hasErrors());
	}

	@Then("I am shown the error message {string} on the edit user profile form")
	public void iAmShownTheErrorMessageErrorMessageOnTheEditUserProfileForm(String errorMessage) {
		Mockito.verify(bindingResult).addError(fieldErrorCaptor.capture());
		FieldError fieldError = fieldErrorCaptor.getValue();
		Assertions.assertEquals(errorMessage, fieldError.getDefaultMessage());
	}

	@After
	public void cleanUp() {
		bindingResult = null;
	}
}
