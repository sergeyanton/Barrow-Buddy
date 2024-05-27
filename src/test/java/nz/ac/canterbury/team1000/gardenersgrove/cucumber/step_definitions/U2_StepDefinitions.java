package nz.ac.canterbury.team1000.gardenersgrove.cucumber.step_definitions;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.LoginForm;
import nz.ac.canterbury.team1000.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class U2_StepDefinitions {
	@Autowired
	UserService userService;
	LoginForm loginForm = new LoginForm();
	BindingResult bindingResult;
	ArgumentCaptor<FieldError> fieldErrorCaptor = ArgumentCaptor.forClass(FieldError.class);

	@Given("A user exists with email {string} and password {string}")
	public void a_user_exists_with_the_email_and_password(String email, String password) {
		if (userService.getUserByEmailAndPassword(email, password) != null) return;
		User newUser = new User("fname", "lname", email, password, null, "");

		userService.registerUser(newUser);
	}

	@Given("I am on the log in form and enter the email {string} and password {string}")
	public void i_am_on_the_log_in_form_and_enter_the_email_and_password(String email,
		String password) {
		loginForm.setEmail(email);
		loginForm.setPassword(password);
	}

	@When("I click the Sign In button")
	public void i_click_the_sign_in_button() {
		bindingResult = Mockito.mock(BindingResult.class);
		LoginForm.validate(loginForm, bindingResult);
	}

	@Then("I successfully log in")
	public void i_successfully_log_in() {
		Mockito.verify(bindingResult, Mockito.never()).addError(Mockito.any());
		Assertions.assertFalse(bindingResult.hasErrors());
	}

	@Then("Getting user by email and password returns null")
	public void getting_user_by_email_and_password_returns_null() {
		Assertions.assertNull(userService.getUserByEmailAndPassword(loginForm.getEmail(), loginForm.getPassword()));
	}

	@Then("I am shown the error message {string} and I am not logged in")
	public void i_am_shown_the_error_message_and_i_am_not_logged_in(String errorMessage) {
		Mockito.verify(bindingResult).addError(fieldErrorCaptor.capture());
		FieldError fieldError = fieldErrorCaptor.getValue();
		Assertions.assertEquals(errorMessage, fieldError.getDefaultMessage());
	}

	@After
	public void cleanUp() {
		bindingResult = null;
	}
}