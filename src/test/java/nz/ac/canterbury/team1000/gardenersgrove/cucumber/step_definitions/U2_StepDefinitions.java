package nz.ac.canterbury.team1000.gardenersgrove.cucumber.step_definitions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.LoginForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class U2_StepDefinitions {
	@Autowired
	UserService userService;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	PasswordEncoder passwordEncoder;
	LoginForm loginForm = new LoginForm();
	BindingResult bindingResult;
	ArgumentCaptor<FieldError> fieldErrorCaptor = ArgumentCaptor.forClass(FieldError.class);

	private User testUser;
	private String testPassword;

	@Given("A user exists with email {string} and password {string}")
	public void a_user_exists_with_the_email_and_password(String email, String password) {
		if (email.isEmpty() || userService.checkEmail(email)) {
			this.testUser = userService.findEmail(email);
			return;
		}
		this.testUser = new User("fname", "lname", email, passwordEncoder.encode(password), null, "/images/default_pic.jpg");

		testUser.grantAuthority("ROLE_USER");
		userService.registerUser(testUser);
		testPassword = password;
	}

	@Given("I am on the log in page")
	public void i_am_on_the_log_in_page() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/login").with(csrf()))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
			.andExpect(MockMvcResultMatchers.view().name("pages/loginPage"));
	}

	@When("I log in with a valid user with email {string} and password {string}")
	public void i_log_in_with_a_valid_user_with_the_email_and_password(String email, String password) throws Exception {
		loginForm.setEmail(email);
		loginForm.setPassword(password);

		mockMvc.perform(MockMvcRequestBuilders.post("/login").with(csrf())
				.flashAttr("loginForm", loginForm))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/home"));
	}

	@When("I log in with an invalid user with the email {string} and password {string}")
	public void i_log_in_with_an_invalid_user_with_the_email_and_password(String email, String password) throws Exception {
		loginForm.setEmail(email);
		loginForm.setPassword(password);

		mockMvc.perform(MockMvcRequestBuilders.post("/login").with(csrf())
			.flashAttr("loginForm", loginForm))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
			.andExpect(MockMvcResultMatchers.view().name("pages/loginPage"));
	}

	@Then("I successfully log in")
	public void i_successfully_log_in() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/").with(csrf()))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
			.andExpect(MockMvcResultMatchers.view().name("pages/homePage"));
	}

	@Then("I remain on the log in page and I get an error message in the field {string}")
	public void getting_user_by_email_and_password_returns_null(String fieldName) throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/login").with(csrf())
			.flashAttr("loginForm", loginForm))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pages/loginPage"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("loginForm", fieldName));
	}

	@After
	public void cleanUp() {
		bindingResult = null;
	}
}