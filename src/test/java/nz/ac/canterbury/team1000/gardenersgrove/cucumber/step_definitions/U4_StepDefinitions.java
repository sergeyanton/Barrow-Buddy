package nz.ac.canterbury.team1000.gardenersgrove.cucumber.step_definitions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.form.EditUserForm;
import nz.ac.canterbury.team1000.gardenersgrove.form.LoginForm;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
public class U4_StepDefinitions {
	@Autowired
	UserService userService;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	PasswordEncoder passwordEncoder;
	LoginForm loginForm = new LoginForm();
	EditUserForm editUserForm = new EditUserForm();
	BindingResult bindingResult;
	ArgumentCaptor<FieldError> fieldErrorCaptor = ArgumentCaptor.forClass(FieldError.class);
	private User testUser;
	private Map<String, Object> modelTemplate;
	private final String PASSWORD = "Password1234!";

	@Given("I am logged in with the name {string} {string}, email {string}, and date of birth {string}")
	public void iAmLoggedInWithTheNameEmailAndDateOfBirth(String fName, String lName, String email,
		String dob) throws Exception {
		if (userService.checkEmail(email)) {
			testUser = userService.findEmail(email);
		} else {
			testUser = new User(fName, lName == "" ? null : lName, email, passwordEncoder.encode(PASSWORD), LocalDate.parse(dob, DateTimeFormatter.ofPattern("dd/MM/uuuu")), "/images/default_pic.jpg");
			testUser.grantAuthority("ROLE_USER");
			userService.registerUser(testUser);
		}

		loginForm.setEmail(testUser.getEmail());
		loginForm.setPassword(PASSWORD);

		mockMvc.perform(MockMvcRequestBuilders.post("/login").with(csrf())
				.flashAttr("loginForm", loginForm))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/home"));
	}

	@Given("A user exists with email {string}")
	public void emailIsTaken(String email) {
		if (!userService.checkEmail(email)) {
			User u = new User("FirstName", "LastName", email, passwordEncoder.encode(PASSWORD),
				LocalDate.of(2000, 1, 1), "/images/default_pic.jpg");
			u.grantAuthority("ROLE_USER");
			userService.registerUser(u);
		}
	}

	@Given("I am on the user profile page")
	public void iAmOnTheUserProfilePage() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/profile").with(csrf()))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
			.andExpect(MockMvcResultMatchers.view().name("pages/profilePage"));
	}

	@When("I click the Edit button on my user profile page")
	public void iClickTheEditButtonOnMyUserProfilePage() throws Exception {
		editUserForm = (EditUserForm) mockMvc.perform(
				MockMvcRequestBuilders.get("/editProfile").with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn()
			.getModelAndView().getModel().get("editUserForm");
	}

	@Then("I see my details prefilled on the Edit Profile form, {string}, {string}, {string}, & {string}")
	public void iSeeMyDetailsPrefilledOnTheEditProfileForm(String fName, String lName, String email,
		String dob) {
		assertEquals(fName, editUserForm.getFirstName());
		assertEquals(lName, editUserForm.getLastName());
		assertEquals(email, editUserForm.getEmail());
		assertEquals(dob, editUserForm.getDob());
	}

	@Then("I see the checkbox for having no last name is checked")
	public void iSeeTheCheckboxForHavingNoLastNameIsChecked() {
		assertEquals(true, editUserForm.getNoSurnameCheckBox());
	}

	@Given("I am on the edit user profile form")
	public void iAmOnTheEditUserProfileForm() {
		editUserForm.setPictureFile(new MockMultipartFile("pictureFile", new byte[0]));
	}

	@And("I enter details {string}, {string}, {string}, and {string} on the edit user profile form")
	public void iEnterDetailsFirstNameLastNameEmailAndDobOnTheEditUserProfileForm(String fName, String lName, String email, String dob) {
		editUserForm.setFirstName(fName);
		editUserForm.setLastName(lName);
		editUserForm.setEmail(email);
		editUserForm.setDob(dob);
	}

	@And("I enter a first name {int} characters long and a last name {int} characters long")
	public void iEnterAFirstNameCharactersLongAndALastNameCharactersLong(int firstNameLength, int lastNameLength) {
		editUserForm.setFirstName("F".repeat(firstNameLength));
		editUserForm.setLastName("L".repeat(lastNameLength));
	}

	@And("I tick the checkbox for no last name on the edit user profile form")
	public void iTickTheCheckboxForNoLastNameOnTheEditUserProfileForm() {
		editUserForm.setNoSurnameCheckBox(true);
	}

	@And("I do not tick the checkbox for no last name on the edit user profile form")
	public void iDoNotTickTheCheckboxForNoLastNameOnTheEditUserProfileForm() {
		editUserForm.setNoSurnameCheckBox(false);
	}

	@And("I change my date of birth to mean that I turn {int} years old in {int} days on the edit user profile form")
	public void iEnterADateOfBirthThatMeansITurnYearsOldInDaysOnTheEditUserProfileForm(int age, int numDays) {
		editUserForm.setDob(LocalDate.now().minusYears(age).plusDays(numDays).format(DateTimeFormatter.ofPattern("dd/MM/uuuu")));
	}

	// Do not have the current level of knowledge to test this exactly how we want with MockMVC
	@When("I click the edit profile button")
	public void iClickTheEditProfileButton(){
		bindingResult = Mockito.mock(BindingResult.class);
		if (userService.checkEmail(editUserForm.getEmail()) && !testUser.getEmail().equals(editUserForm.getEmail())) {
			bindingResult.addError(new FieldError("editUserForm", "email", editUserForm.getEmail(), false, null, null, "Email already in use"));
		}
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

	@When("I click the Cancel button on my edit user profile form")
	public void iClickTheCancelButtonOnMyEditUserProfileForm() throws Exception {
		modelTemplate = mockMvc.perform(
				MockMvcRequestBuilders.get("/profile").with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn()
			.getModelAndView().getModel();
	}

	@Then("I am taken back to my profile page and my details are still {string} {string}, {string}, and {string}")
	public void iAmTakenBackToMyProfilePageAndMyDetailsAreStillAnd(String fName, String lName,
		String email, String dob){
		assertEquals(fName, modelTemplate.get("fName"));
		assertEquals(lName, modelTemplate.get("lName"));
		assertEquals(email, modelTemplate.get("email"));
		assertEquals(dob, modelTemplate.get("dob"));
	}

	@After
	public void cleanUp() {
		bindingResult = null;
	}
}
